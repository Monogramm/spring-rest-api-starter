/*
 * Creation by madmath03 the 2017-09-11.
 */

package com.monogramm.starter.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.monogramm.starter.api.oauth.controller.RevokeTokenEndpoint;
import com.monogramm.starter.config.component.CustomTokenEnhancer;
import com.monogramm.starter.dto.oauth.OAuthRequest;
import com.monogramm.starter.dto.oauth.OAuthResponse;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Full Integration Test (with RestTemplate and such) abstract controller.
 * 
 * @author madmath03
 */
public abstract class AbstractControllerFullIT extends AbstractControllerIT {

  /**
   * The protocol of the tests URLs.
   */
  public static final String DEFAULT_SCHEME = "http";

  /**
   * The origin (protocol + domain) of the tests URLs.
   */
  public static final String DEFAULT_HOST = "localhost";


  private static final String CLIENT_BASIC_AUTH_TOKEN;

  static {
    final String token = CLIENT_ID + ":" + CLIENT_SECRET;
    CLIENT_BASIC_AUTH_TOKEN = Base64.getEncoder().encodeToString(token.getBytes());
  }



  @Value("${local.server.port}")
  private int port;

  // @Value("${spring.data.rest.base-path}")
  @Value("${server.context-path}")
  private String basePath;



  @Autowired
  private TestRestTemplate restTemplate;



  /**
   * Get an URL of the test application.
   * 
   * @param pathComponents the path components to add to the root.
   * 
   * @return the root URL of the test application.
   * 
   * @throws URISyntaxException if the URL could not be created.
   */
  protected String getUrl(final Object... pathComponents) throws URISyntaxException {
    return getUrl(pathComponents, null);
  }

  /**
   * Get an URL of the test application.
   * 
   * @param pathComponents the path components to add to the root.
   * @param parameters the URL parameters.
   * 
   * @return the root URL of the test application.
   * 
   * @throws URISyntaxException if the URL could not be created.
   */
  protected String getUrl(final Object[] pathComponents, final String parameters)
      throws URISyntaxException {
    final StringBuilder pathBuilder = new StringBuilder(basePath);

    if (pathComponents != null) {
      for (final Object pathComponent : pathComponents) {
        pathBuilder.append(pathComponent);
      }
    }

    final URI root =
        new URI(DEFAULT_SCHEME, null, DEFAULT_HOST, port, pathBuilder.toString(), parameters, null);
    final String tempUrl = root.toString();

    // Restore any '{' or '}' character needed by TestRestTemplate substitution
    return tempUrl.replaceAll("%7B", "{").replaceAll("%7D", "}");
  }

  /**
   * Get the root URL of the test application.
   * 
   * @return the root URL of the test application.
   * 
   * @throws URISyntaxException if the root URL could not be created.
   */
  protected String getRootUrl() throws URISyntaxException {
    return getUrl();
  }


  /**
   * Get a OAuth2 token response for the test user created by {@link #setUp(String...)}.
   * 
   * @see OAuthRequest
   * @see OAuthResponse
   * @see CustomTokenEnhancer
   * 
   * @throws URISyntaxException if the OAuth2 token request URL could not be created.
   */
  protected ResponseEntity<OAuthResponse> getFullTokenResponse() throws URISyntaxException {
    final HttpHeaders headers = getHeaders();

    headers.add("Authorization", "Basic " + CLIENT_BASIC_AUTH_TOKEN);

    final String url = this.getUrl(TOKEN_PATH);

    final OAuthRequest objAuth = new OAuthRequest();
    objAuth.setUsername(getTestUser().getEmail());
    objAuth.setEmail(getTestUser().getEmail());
    objAuth.setPassword(PASSWORD.clone());
    objAuth.setGrantType(PASSWORD_GRANT_TYPE);
    objAuth.setClientId(CLIENT_ID);
    objAuth.setClientSecret(CLIENT_SECRET);

    final HttpEntity<OAuthRequest> requestEntity = new HttpEntity<>(objAuth, headers);


    return restTemplate.exchange(url, HttpMethod.POST, requestEntity, OAuthResponse.class);
  }

  /**
   * Get a OAuth2 token for the test user created by {@link #setUp(String...)}.
   * 
   * @see OAuthRequest
   * @see OAuthResponse
   * @see CustomTokenEnhancer
   * 
   * @throws URISyntaxException if the OAuth2 token request URL could not be created.
   */
  protected String getFullToken() throws URISyntaxException {
    final ResponseEntity<OAuthResponse> responseEntity = this.getFullTokenResponse();

    assertNotNull(responseEntity);
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

    final OAuthResponse response = responseEntity.getBody();

    assertNotNull(response);
    assertNotNull(response.getAccessToken());

    return response.getAccessToken();
  }

  /**
   * Revoke a OAuth2 token.
   * 
   * @see OAuthRequest
   * @see OAuthResponse
   * @see CustomTokenEnhancer
   * @see RevokeTokenEndpoint
   * 
   * @throws URISyntaxException if the OAuth2 token request URL could not be created.
   */
  protected void revokeToken(final String accessToken) throws URISyntaxException {
    final HttpHeaders headers = getHeaders(accessToken);

    final String url = this.getUrl(TOKEN_PATH);

    final HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

    // Request by password
    restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, Void.class);
  }



  /**
   * Get the {@link #restTemplate}.
   * 
   * @return the {@link #restTemplate}.
   */
  protected final TestRestTemplate getRestTemplate() {
    return restTemplate;
  }

}
