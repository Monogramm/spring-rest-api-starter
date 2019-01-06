package com.monogramm.starter.api.oauth.controller;

import com.monogramm.starter.api.AbstractGenericController;

import java.util.Locale;

import javax.annotation.Resource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The OAuth API Controller.
 * 
 * @see RevokeTokenEndpoint
 * 
 * @author madmath03
 */
@RestController
public final class OAuthController {
  /**
   * The end point logger.
   */
  private static final Logger LOG = LogManager.getLogger(OAuthController.class);

  /**
   * The main data type handled by this controller.
   */
  public static final String TYPE = "oauth";
  /**
   * The request base path of this controller.
   */
  public static final String CONTROLLER_PATH = AbstractGenericController.SEP + TYPE;

  /**
   * The request path for login.
   */
  public static final String TOKEN_PATH = CONTROLLER_PATH + "/token";

  /**
   * The request path for revoking OAuth token.
   */
  public static final String REVOKE_TOKEN_PATH = TOKEN_PATH + "/revoke";

  /**
   * The request path for revoking OAuth refresh token.
   */
  public static final String REVOKE_REFRESH_TOKEN_PATH = TOKEN_PATH + "/revoke-refresh-token";

  @Resource(name = "tokenServices")
  private ConsumerTokenServices tokenServices;


  @Autowired
  private MessageSource messageSource;

  private Locale locale = Locale.getDefault();

  /**
   * Create a {@link OAuthController}.
   * 
   * @param userService the user service.
   */
  private OAuthController() {}

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
   * @see <a href="https://www.baeldung.com/spring-security-oauth-revoke-tokens">Spring Security
   *      OAuth2 – Simple Token Revocation</a>
   * 
   * @param tokenId <em>Required Parameter:</em> the access token id.
   * 
   * @return the access token id which was revoked.
   */
  @PostMapping(value = REVOKE_TOKEN_PATH)
  public String revokeToken(@PathVariable final String tokenId) {
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
   * @see <a href="https://www.baeldung.com/spring-security-oauth-revoke-tokens">Spring Security
   *      OAuth2 – Simple Token Revocation</a>
   * 
   * @param tokenId <em>Required Parameter:</em> the refresh token id.
   * 
   * @return the access token id which was revoked.
   */
  @PostMapping(value = REVOKE_REFRESH_TOKEN_PATH + "/{tokenId:.*}")
  public String revokeRefreshToken(@PathVariable final String tokenId) {
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
