/*
 * Creation by madmath03 the 2017-11-23.
 */

package com.monogramm.starter.api.oauth.controller;

import java.util.Locale;

import javax.annotation.Resource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Logout functionality for an OAuth2 Spring Security application.
 * 
 * @see <a href="http://www.baeldung.com/logout-spring-security-oauth">Baeldung - Logout in a OAuth
 *      Secured Application</a>
 * 
 * @author madmath03
 */
@FrameworkEndpoint
public class RevokeTokenEndpoint {
  /**
   * The end point logger.
   */
  private static final Logger LOG = LogManager.getLogger(RevokeTokenEndpoint.class);

  /**
   * The request path for revoking OAuth token.
   */
  public static final String REVOKE_TOKEN_PATH = OAuthController.TOKEN_PATH + "/revoke";

  /**
   * The request path for revoking OAuth refresh token.
   */
  public static final String REVOKE_REFRESH_TOKEN_PATH =
      OAuthController.TOKEN_PATH + "/revokeRefreshToken";

  @Resource(name = "tokenServices")
  private ConsumerTokenServices tokenServices;


  @Autowired
  private MessageSource messageSource;

  private Locale locale = Locale.getDefault();


  /**
   * Revoke an access token.
   * 
   * <p>
   * Logging out in an OAuth-secured environment involves <strong>rendering the user’s Access Token
   * invalid</strong> – so it can no longer be used.
   * </p>
   * 
   * <p>
   * In a <em>JdbcTokenStore</em>-based implementation, this means removing the token from the
   * TokenStore.
   * </p>
   * 
   * @param tokenId <em>Required Parameter:</em> the access token id.
   */
  @RequestMapping(method = RequestMethod.POST, value = "/" + REVOKE_TOKEN_PATH + "/{tokenId:.*}")
  @ResponseBody
  public String revokeToken(@PathVariable String tokenId) {
    if (tokenServices.revokeToken(tokenId) && LOG.isInfoEnabled()) {
      final String msg = messageSource.getMessage("controller.oauth.token_revoked",
          new String[] {tokenId}, locale);
      LOG.info(msg);
    }

    return tokenId;
  }


  /**
   * Revoke a refresh token.
   * 
   * <p>
   * Logging out in an OAuth-secured environment involves <strong>rendering the user’s Refresh Token
   * invalid</strong> – so it can no longer be used.
   * </p>
   * 
   * <p>
   * In a <em>JdbcTokenStore</em>-based implementation, this means removing the token from the
   * TokenStore.
   * </p>
   * 
   * @param tokenId <em>Required Parameter:</em> the refresh token id.
   */
  @RequestMapping(method = RequestMethod.POST,
      value = "/" + REVOKE_REFRESH_TOKEN_PATH + "/{tokenId:.*}")
  @ResponseBody
  public String revokeRefreshToken(@PathVariable String tokenId) {
    if (tokenServices instanceof JdbcTokenStore) {
      ((JdbcTokenStore) tokenServices).removeRefreshToken(tokenId);
      if (LOG.isInfoEnabled()) {
        final String msg = messageSource.getMessage("controller.oauth.refresh_token_revoked",
            new String[] {tokenId}, locale);
        LOG.info(msg);
      }
    }

    return tokenId;
  }

}
