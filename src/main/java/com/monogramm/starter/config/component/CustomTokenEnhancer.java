/*
 * Creation by madmath03 the 2017-11-20.
 */

package com.monogramm.starter.config.component;

import com.monogramm.starter.config.OAuth2WebSecurityConfig;
import com.monogramm.starter.dto.oauth.OAuthResponse;
import com.monogramm.starter.persistence.user.entity.User;
import com.monogramm.starter.persistence.user.service.IUserService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
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
 * @author madmath03
 */
public class CustomTokenEnhancer implements TokenEnhancer {

  /**
   * Logger for {@link CustomTokenEnhancer}.
   */
  private static final Logger LOG = LogManager.getLogger(CustomTokenEnhancer.class);

  private IUserService userService;

  /**
   * Create a {@link CustomTokenEnhancer}.
   * 
   * @param userService the user service.
   */
  @Autowired
  public CustomTokenEnhancer(IUserService userService) {
    super();
    this.userService = userService;
  }

  @Override
  public OAuth2AccessToken enhance(final OAuth2AccessToken accessToken,
      final OAuth2Authentication authentication) {
    final Map<String, Object> additionalInfo = new HashMap<>();

    additionalInfo.put("timestamp", new Date());

    // TODO Use a Custom UserDetails retrieving all this on login
    final String authName = authentication.getName();
    User user;
    try {
      user = this.userService.findByEmail(authName);
    } catch (Exception e) {
      LOG.debug("enhance(authName=" + authName + ")", e);
      user = null;
    }

    if (user != null) {
      additionalInfo.put("principal_id", user.getId());
      additionalInfo.put("principal_name", user.getUsername());
      additionalInfo.put("principal_email", user.getEmail());
      additionalInfo.put("verified", user.isVerified());
    } else {
      additionalInfo.put("principal_email", authName);
    }

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

        if (auth == null || auth.isEmpty()) {
          continue;
        } else if (auth.startsWith(OAuth2WebSecurityConfig.ROLE_PREFIX)) {
          roleList.add(auth);
        } else {
          authList.add(auth);
        }
      }

      authoritiesValue = authList.toArray(new String[] {});
      rolesValue = roleList.toArray(new String[] {});
    }
    additionalInfo.put("authorities", authoritiesValue);
    additionalInfo.put("roles", rolesValue);


    ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);

    return accessToken;
  }

}
