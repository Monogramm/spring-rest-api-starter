package com.monogramm.starter.config.security;

import com.monogramm.starter.config.OAuth2WebSecurityConfig;
import com.monogramm.starter.config.component.CustomTokenEnhancer;
import com.monogramm.starter.dto.AbstractGenericDto;

import java.io.Serializable;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

/**
 * Custom Permission Evaluator.
 * 
 * @see CustomTokenEnhancer
 * @see OAuth2WebSecurityConfig
 * @see <a href="https://www.baeldung.com/spring-security-create-new-custom-security-expression">A
 *      Custom Security Expression with Spring Security</a>
 * 
 * @author madmath03
 */
public class CustomPermissionEvaluator implements PermissionEvaluator {

  @Override
  public boolean hasPermission(Authentication auth, Object targetDomainObject, Object permission) {
    if ((auth == null) || (targetDomainObject == null) || !(permission instanceof String)) {
      return false;
    }

    // First extract the response body
    final Object targetData;
    if (targetDomainObject instanceof ResponseEntity) {
      targetData = ((ResponseEntity<?>) targetDomainObject).getBody();
    } else {
      targetData = targetDomainObject;
    }
    if (targetData == null) {
      return false;
    }

    // If we return a DTO, we want to test the entity matching
    final String className = targetData.getClass().getSimpleName();

    final String targetType;
    if (targetData instanceof AbstractGenericDto) {
      targetType = OAuth2WebSecurityConfig.AUTH_PREFIX
          + className.substring(0, className.length() - 3).toUpperCase();
    } else {
      targetType = OAuth2WebSecurityConfig.AUTH_PREFIX + className.toUpperCase();
    }

    return hasPrivilege(auth, targetType, permission.toString().toUpperCase());
  }

  @Override
  public boolean hasPermission(Authentication auth, Serializable targetId, String targetType,
      Object permission) {
    if ((auth == null) || (targetType == null) || !(permission instanceof String)) {
      return false;
    }

    return hasPrivilege(auth, OAuth2WebSecurityConfig.AUTH_PREFIX + targetType.toUpperCase(),
        permission.toString().toUpperCase());
  }

  private boolean hasPrivilege(Authentication auth, String targetType, String permission) {
    for (GrantedAuthority grantedAuth : auth.getAuthorities()) {
      if (grantedAuth.getAuthority().startsWith(targetType)
          && grantedAuth.getAuthority().contains(permission)) {
        return true;
      }
    }

    return false;
  }

}
