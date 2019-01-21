/*
 * Creation by madmath03 the 2018-12-29.
 */

package com.monogramm.starter.config.security;

import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * AuthenticationFacade.
 * 
 * @see <a href="https://www.baeldung.com/get-user-in-spring-security">Retrieve User Information in
 *      Spring Security</a>
 * 
 * @author madmath03
 */
@Component
public class AuthenticationFacade implements IAuthenticationFacade {

  private Set<String> roles;
  private String defaultRolePrefix = DEFAULT_ROLE_PREFIX;

  @Override
  public Authentication getAuthentication() {
    return SecurityContextHolder.getContext().getAuthentication();
  }

  @Override
  public final boolean hasAnyAuthority(String... authorities) {
    return hasAnyAuthorityName(null, authorities);
  }

  @Override
  public final boolean hasAnyRole(String... roles) {
    return hasAnyAuthorityName(defaultRolePrefix, roles);
  }

  private boolean hasAnyAuthorityName(String prefix, String... roles) {
    final Set<String> roleSet = getAuthoritySet();

    return IAuthenticationFacade.hasAnyAuthorityName(roleSet, prefix, roles);
  }

  private Set<String> getAuthoritySet() {
    if (roles == null) {
      final Authentication authentication = this.getAuthentication();

      roles = IAuthenticationFacade.getAuthoritySet(authentication);
    }

    return roles;
  }

}
