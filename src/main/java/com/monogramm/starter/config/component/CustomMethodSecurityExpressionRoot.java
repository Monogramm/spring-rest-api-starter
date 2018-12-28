package com.monogramm.starter.config.component;

import com.monogramm.starter.dto.AbstractGenericDto;
import com.monogramm.starter.persistence.AbstractGenericEntity;
import com.monogramm.starter.persistence.user.entity.User;

import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

/**
 * Custom Method Security Expression.
 * 
 * @see CustomTokenEnhancer
 * @see CustomMethodSecurityExpressionHandler
 * 
 * @see <a href="https://www.baeldung.com/spring-security-create-new-custom-security-expression">A
 *      Custom Security Expression with Spring Security</a>
 * 
 * @author madmath03
 */
public class CustomMethodSecurityExpressionRoot extends SecurityExpressionRoot
    implements MethodSecurityExpressionOperations {

  private Object filterObject;
  private Object returnObject;

  public CustomMethodSecurityExpressionRoot(Authentication authentication) {
    super(authentication);
  }

  /**
   * Test if authenticated user is owner of the target object.
   * 
   * @return {@code true} if the authenticated user is owner of the target object.
   */
  public boolean isOwner() {
    boolean isOwner = false;

    final Object data = this.getReturnData();

    final Map<?, ?> details = this.getAuthDetails();
    if (data != null && details != null) {
      final UUID principalId = this.getPrincipalId(details);

      if (principalId != null) {
        if (data instanceof AbstractGenericDto) {
          isOwner = principalId.equals(((AbstractGenericDto) data).getOwner());
        } else if (data instanceof AbstractGenericEntity) {
          final User owner = ((AbstractGenericEntity) data).getOwner();
          isOwner = owner != null && principalId.equals(owner.getId());
        }
      }
    }

    return isOwner;
  }

  private Map<?, ?> getAuthDetails() {
    Map<?, ?> details = null;

    final Authentication authentication = this.getAuthentication();
    if (authentication.getDetails() instanceof OAuth2AuthenticationDetails) {
      final Object decodedDetails =
          ((OAuth2AuthenticationDetails) authentication.getDetails()).getDecodedDetails();

      if (decodedDetails instanceof Map) {
        details = (Map<?, ?>) decodedDetails;
      }
    }

    return details;
  }

  private UUID getPrincipalId(Map<?, ?> details) {
    UUID principalId = null;

    if (details.containsKey(CustomTokenEnhancer.UUID)) {
      final Object idValue = details.get(CustomTokenEnhancer.UUID);

      try {
        principalId = UUID.fromString((String) idValue);
      } catch (Exception e) {
        principalId = null;
      }
    }

    return principalId;
  }

  private Object getReturnData() {
    final Object targetData;

    final Object object = this.getReturnObject();
    if (object instanceof ResponseEntity) {
      targetData = ((ResponseEntity<?>) object).getBody();
    } else {
      targetData = object;
    }

    return targetData;
  }

  @Override
  public void setFilterObject(Object filterObject) {
    this.filterObject = filterObject;
  }

  @Override
  public Object getFilterObject() {
    return this.filterObject;
  }

  @Override
  public void setReturnObject(Object returnObject) {
    this.returnObject = returnObject;
  }

  @Override
  public Object getReturnObject() {
    return this.returnObject;
  }

  @Override
  public Object getThis() {
    return this;
  }

}
