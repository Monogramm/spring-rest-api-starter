/*
 * Creation by madmath03 the 2018-12-29.
 */

package com.monogramm.starter.config.security;

import com.monogramm.starter.config.OAuth2WebSecurityConfig;
import com.monogramm.starter.config.component.CustomTokenEnhancer;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

/**
 * IAuthenticationFacade.
 * 
 * @see <a href="https://www.baeldung.com/get-user-in-spring-security">Retrieve User Information in
 *      Spring Security</a>
 * 
 * @author madmath03
 */
public interface IAuthenticationFacade {

  String DEFAULT_ROLE_PREFIX = OAuth2WebSecurityConfig.ROLE_PREFIX;

  /**
   * Get the authentication information.
   * 
   * @return {@code Authentication} object.
   */
  Authentication getAuthentication();


  /**
   * Extract the authentication decoded details from authentication object.
   * 
   * @see CustomTokenEnhancer
   * 
   * @param authentication the authentication object.
   * 
   * @return the decoded authentication details.
   * 
   * @throws NullPointerException if authentication is {@code null}
   */
  static Map<?, ?> getAuthDetails(final Authentication authentication) {
    Map<?, ?> details = null;

    if (authentication != null) {
      final Object rawDetails = authentication.getDetails();
      if (rawDetails instanceof OAuth2AuthenticationDetails) {
        final Object decodedDetails =
            ((OAuth2AuthenticationDetails) rawDetails).getDecodedDetails();

        if (decodedDetails instanceof Map) {
          details = (Map<?, ?>) decodedDetails;
        }
      }
    }

    return details;
  }

  /**
   * Extract the authentication decoded details from authentication object.
   * 
   * @see CustomTokenEnhancer
   * 
   * @return the decoded authentication details.
   */
  default Map<?, ?> getAuthDetails() {
    Map<?, ?> details = null;

    final Authentication authentication = this.getAuthentication();
    if (authentication != null) {
      details = IAuthenticationFacade.getAuthDetails(authentication);
    }

    return details;
  }


  /**
   * Extract the principal unique id from the authentication object.
   * 
   * <p>
   * You should only use this function if that is only interaction you plan to have with the
   * authentication object decoded details.
   * </p>
   * 
   * @see CustomTokenEnhancer
   * 
   * @param authentication the authentication object.
   * 
   * @return the principal unique id.
   * 
   * @throws NullPointerException if authentication is {@code null}
   */
  static UUID getPrincipalId(final Authentication authentication) {
    UUID principalId = null;

    final Map<?, ?> details = getAuthDetails(authentication);
    if (details != null) {
      principalId = IAuthenticationFacade.getPrincipalId(details);
    }

    return principalId;
  }

  /**
   * Extract the principal unique id from the decoded details.
   * 
   * @see CustomTokenEnhancer
   * 
   * @param details the decoded details.
   * 
   * @return the principal unique id.
   * 
   * @throws NullPointerException if authentication is {@code null}
   */
  static UUID getPrincipalId(final Map<?, ?> details) {
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

  /**
   * Extract the principal unique id from the decoded details.
   * 
   * @see CustomTokenEnhancer
   * 
   * @return the principal unique id.
   */
  default UUID getPrincipalId() {
    UUID principalId = null;

    final Map<?, ?> details = this.getAuthDetails();
    if (details != null) {
      principalId = IAuthenticationFacade.getPrincipalId(details);
    }

    return principalId;
  }


  /**
   * Determines if the {@link #getAuthentication()} has a particular authority within
   * {@link Authentication#getAuthorities()}.
   * 
   * @param authority the authority to test (i.e. "ROLE_USER")
   * 
   * @return {@code true} if the authority is found, else {@code false}.
   */
  default boolean hasAuthority(String authority) {
    return this.hasAnyAuthority(authority);
  }

  /**
   * Determines if the specified authentication object has any of the specified authorities within
   * {@link Authentication#getAuthorities()}.
   * 
   * @param authentication the authentication object.
   * @param authorities the authorities to test (i.e. "ROLE_USER", "ROLE_ADMIN")
   * 
   * @return {@code true} if any of the authorities is found, else {@code false}.
   */
  static boolean hasAnyAuthority(final Authentication authentication, String... authorities) {
    return hasAnyAuthorityName(authentication, null, authorities);
  }

  /**
   * Determines if the {@link #getAuthentication()} has any of the specified authorities within
   * {@link Authentication#getAuthorities()}.
   * 
   * @param authorities the authorities to test (i.e. "ROLE_USER", "ROLE_ADMIN")
   * 
   * @return {@code true} if any of the authorities is found, else {@code false}.
   */
  boolean hasAnyAuthority(String... authorities);


  /**
   * Determines if the {@link #getAuthentication()} has a particular authority within
   * {@link Authentication#getAuthorities()}.
   * 
   * <p>
   * This is similar to {@link #hasAuthority(String)} except that this method implies that the
   * String passed in is a role. For example, if "USER" is passed in the implementation may convert
   * it to use "ROLE_USER" instead. The way in which the role is converted may depend on the
   * implementation settings.
   * </p>
   *
   * @param role the authority to test (i.e. "USER")
   * 
   * @return {@code true} if the authority is found, else {@code false}.
   */
  default boolean hasRole(String role) {
    return this.hasAnyRole(role);
  }

  /**
   * Determines if the {@link #getAuthentication()} has any of the specified authorities within
   * {@link Authentication#getAuthorities()}.
   * 
   * <p>
   * This is a similar to {@link #hasAnyAuthority(Authentication, String...)} except that this
   * method implies that the String passed in is a role. For example, if "USER" is passed in the
   * implementation may convert it to use "ROLE_USER" instead. The way in which the role is
   * converted may depend on the implementation settings.
   * </p>
   *
   * @param authentication the authentication object.
   * @param roles the authorities to test (i.e. "USER", "ADMIN")
   * 
   * @return {@code true} if any of the authorities is found, else {@code false}.
   */
  static boolean hasAnyRole(final Authentication authentication, String... roles) {
    return hasAnyAuthorityName(authentication, DEFAULT_ROLE_PREFIX, roles);
  }

  /**
   * Determines if the {@link #getAuthentication()} has any of the specified authorities within
   * {@link Authentication#getAuthorities()}.
   * 
   * <p>
   * This is a similar to {@link #hasAnyAuthority(String...)} except that this method implies that
   * the String passed in is a role. For example, if "USER" is passed in the implementation may
   * convert it to use "ROLE_USER" instead. The way in which the role is converted may depend on the
   * implementation settings.
   * </p>
   *
   * @param roles the authorities to test (i.e. "USER", "ADMIN")
   * 
   * @return {@code true} if any of the authorities is found, else {@code false}.
   */
  boolean hasAnyRole(String... roles);


  /**
   * Test if an authentication object has any of the authorities specified.
   * 
   * @param authentication the authentication object.
   * @param prefix the default role prefix.
   * @param roles any of the roles needed.
   * 
   * @return {@code true} if the authentication object contains any of the specified roles.
   */
  static boolean hasAnyAuthorityName(final Authentication authentication, String prefix,
      String... roles) {
    final Set<String> roleSet = getAuthoritySet(authentication);

    return hasAnyAuthorityName(roleSet, prefix, roles);
  }

  /**
   * Test if a set of roles has any of the authorities specified.
   * 
   * @see #getAuthoritySet(Authentication)
   * 
   * @param roleSet a set of roles.
   * @param prefix the default role prefix.
   * @param roles any of the roles needed.
   * 
   * @return {@code true} if the set of roles contains any of the specified roles.
   */
  static boolean hasAnyAuthorityName(final Set<String> roleSet, String prefix, String... roles) {
    if (roleSet != null) {

      for (String role : roles) {
        String defaultedRole = getRoleWithDefaultPrefix(prefix, role);
        if (roleSet.contains(defaultedRole)) {
          return true;
        }
      }

    }

    return false;
  }

  /**
   * Get a set containing the authorities from the authentication object.
   * 
   * @param authentication the authentication object.
   * 
   * @return a set containing the authorities.
   */
  static Set<String> getAuthoritySet(final Authentication authentication) {
    final Set<String> roles;

    if (authentication != null) {
      Collection<? extends GrantedAuthority> userAuthorities = authentication.getAuthorities();

      roles = AuthorityUtils.authorityListToSet(userAuthorities);
    } else {
      roles = null;
    }

    return roles;
  }

  /**
   * Prefixes role with {@code defaultRolePrefix} if {@code defaultRolePrefix} is non-null and if
   * {@code role} does not already start with {@code defaultRolePrefix}.
   *
   * @param defaultRolePrefix default role prefix.
   * @param role role name
   * 
   * @return role with prefix.
   */
  static String getRoleWithDefaultPrefix(String defaultRolePrefix, String role) {
    if (role == null || defaultRolePrefix == null || defaultRolePrefix.length() == 0
        || role.startsWith(defaultRolePrefix)) {
      return role;
    }
    return defaultRolePrefix + role;
  }

}
