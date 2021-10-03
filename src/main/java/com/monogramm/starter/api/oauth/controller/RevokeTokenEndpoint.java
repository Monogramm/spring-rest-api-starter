/*
 * Creation by madmath03 the 2017-11-23.
 */

package com.monogramm.starter.api.oauth.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Logout functionality for an OAuth2 Spring Security application.
 * 
 * @see OAuthController
 * 
 * @see <a href="http://www.baeldung.com/logout-spring-security-oauth">Baeldung - Logout in a OAuth
 *      Secured Application</a>
 * 
 * @author madmath03
 */
@FrameworkEndpoint
public class RevokeTokenEndpoint {

  private static final String TOKEN_HEADER = "Authorization";
  private static final String TOKEN_BEARER = "Bearer";

  @Autowired
  private OAuthController oauthController;


  /**
   * Revoke self access token.
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
  @DeleteMapping(value = OAuthController.TOKEN_PATH)
  @ResponseBody
  public void revokeSelfToken(final HttpServletRequest request) {
    final String authorization = request.getHeader(TOKEN_HEADER);

    if (authorization != null && authorization.startsWith(TOKEN_BEARER)) {
      final String tmpTokenId = authorization.substring(TOKEN_BEARER.length() + 1);
      oauthController.revokeToken(tmpTokenId);
    }

  }

}
