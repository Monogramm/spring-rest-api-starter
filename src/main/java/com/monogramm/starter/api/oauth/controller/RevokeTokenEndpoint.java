/*
 * Creation by madmath03 the 2017-11-23.
 */

package com.monogramm.starter.api.oauth.controller;

import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
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

  private static final String TOKEN_HEADER = "Authorization";
  private static final String TOKEN_BEARER = "Bearer";

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
   * @param request <em>Required Parameter:</em> the HTTP request.
   */
  @RequestMapping(method = RequestMethod.DELETE, value = "/" + OAuthController.TOKEN_PATH)
  @ResponseBody
  public void revokeToken(final HttpServletRequest request) {
    final String authorization = request.getHeader(TOKEN_HEADER);

    if (authorization != null && authorization.contains(TOKEN_BEARER)) {
      final String tokenId = authorization.substring(TOKEN_BEARER.length() + 1);

      if (tokenServices.revokeToken(tokenId) && LOG.isInfoEnabled()) {
        final String msg = messageSource.getMessage(
            "controller.oauth.token_revoked", new String[] {tokenId}, locale);
        LOG.info(msg);
      }
    }
  }

}
