/*
 * Creation by madmath03 the 2017-11-20.
 */

package com.monogramm.starter.config.component;

import com.monogramm.starter.config.OAuth2WebSecurityConfig;
import com.monogramm.starter.dto.oauth.OAuthResponse;
import com.monogramm.starter.persistence.user.entity.User;
import com.monogramm.starter.persistence.user.service.UserService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

/**
 * CustomTokenEnhancer.
 * 
 * @see OAuthResponse
 * 
 * @see <a href="https://www.baeldung.com/spring-security-oauth-jwt">Using JWT with Spring Security
 *      OAuth</a>
 * 
 * @author madmath03
 */
public class CustomTokenEnhancer implements TokenEnhancer {

  /**
   * Logger for {@link CustomTokenEnhancer}.
   */
  private static final Logger LOG = LoggerFactory.getLogger(CustomTokenEnhancer.class);


  /**
   * Timestamp additional field.
   */
  public static final String TIMESTAMP = "timestamp";


  /**
   * Principal Unique ID additional field.
   */
  public static final String UUID = "principal_id";
  /**
   * Principal username additional field.
   */
  public static final String USERNAME = "principal_name";
  /**
   * Principal email additional field.
   */
  public static final String EMAIL = "principal_email";
  /**
   * Principal verified additional field.
   */
  public static final String VERIFIED = "verified";


  /**
   * Principal roles additional field.
   */
  public static final String ROLES = "roles";
  /**
   * Principal permissions additional field.
   */
  public static final String PERMISSIONS = "authorities";


  private UserService userService;

  /**
   * Create a {@link CustomTokenEnhancer}.
   * 
   * @param userService the user service.
   */
  @Autowired
  public CustomTokenEnhancer(UserService userService) {
    super();
    this.userService = userService;
  }

  @Override
  public OAuth2AccessToken enhance(final OAuth2AccessToken accessToken,
      final OAuth2Authentication authentication) {
    if (accessToken instanceof DefaultOAuth2AccessToken) {
      final Map<String, Object> additionalInfo = this.getAdditionalInformation(authentication);

      ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
    }

    return accessToken;
  }

  private Map<String, Object> getAdditionalInformation(final OAuth2Authentication authentication) {
    final Map<String, Object> additionalInfo = new HashMap<>();

    additionalInfo.put(TIMESTAMP, new Date());

    // Set user email, username and UUID
    this.enrichUserDetails(authentication, additionalInfo);

    // Set the roles and authorities
    this.enrichAuthorities(authentication, additionalInfo);

    return additionalInfo;
  }

  private void enrichUserDetails(final OAuth2Authentication authentication,
      final Map<String, Object> additionalInfo) {
    // TODO Use a Custom UserDetails retrieving all this on login
    // https://www.baeldung.com/spring-security-authentication-with-a-database

    final String authName = authentication.getName();
    User user;
    try {
      user = this.userService.findByUsernameOrEmail(authName, authName);
    } catch (Exception e1) {
      LOG.debug("No username or email found matching \"" + authName + "\"", e1);
      user = null;
    }

    if (user != null) {
      additionalInfo.put(UUID, user.getId());
      additionalInfo.put(USERNAME, user.getUsername());
      additionalInfo.put(EMAIL, user.getEmail());
      additionalInfo.put(VERIFIED, user.isVerified());
    }
  }

  private void enrichAuthorities(final OAuth2Authentication authentication,
      final Map<String, Object> additionalInfo) {
    // Set the roles and authorities
    final Collection<GrantedAuthority> authorities = authentication.getAuthorities();

    final String[] authoritiesValue;
    final String[] rolesValue;
    if (authorities == null || authorities.isEmpty()) {
      authoritiesValue = new String[] {};
      rolesValue = new String[] {};
    } else {
      final Collection<String> authList = new ArrayList<>();
      final Collection<String> roleList = new ArrayList<>();

      for (GrantedAuthority authority : authorities) {
        final String auth = authority.getAuthority();

        if (auth != null && !auth.isEmpty()) {
          if (auth.startsWith(OAuth2WebSecurityConfig.ROLE_PREFIX)) {
            roleList.add(auth);
          } else {
            authList.add(auth);
          }
        }
      }

      authoritiesValue = authList.toArray(new String[] {});
      rolesValue = roleList.toArray(new String[] {});
    }

    // Add the authorities and roles
    additionalInfo.put(PERMISSIONS, authoritiesValue);
    additionalInfo.put(ROLES, rolesValue);
  }

}
