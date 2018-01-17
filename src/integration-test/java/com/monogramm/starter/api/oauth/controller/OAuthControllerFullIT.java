package com.monogramm.starter.api.oauth.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import com.monogramm.Application;
import com.monogramm.starter.api.AbstractControllerFullIT;
import com.monogramm.starter.config.data.InitialDataLoader;
import com.monogramm.starter.dto.oauth.OAuthRequest;
import com.monogramm.starter.dto.oauth.OAuthResponse;
import com.monogramm.starter.persistence.user.entity.User;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * {@link OAuthController} Integration Test.
 * 
 * <p>
 * We assume the environment is freshly created and only contains the initial data.
 * </p>
 * 
 * <p>
 * Spring boot test is searching {@code @SpringBootConfiguration} or {@code @SpringBootApplication}.
 * In this case it will automatically find {@link Application} boot main class.
 * </p>
 * 
 * @see Application
 * @see InitialDataLoader
 * @see AbstractControllerFullIT
 * 
 * @author madmath03
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class OAuthControllerFullIT extends AbstractControllerFullIT {

  @Before
  public void setUp() {
    super.setUpValidUser();
  }

  @After
  public void tearDown() {
    super.tearDownValidUser();
  }

  @Test
  public void givenDBUserWhenRevokeTokenThenAuthorized() throws URISyntaxException {
    final String accessToken = obtainAccessToken(CLIENT_ID, CLIENT_SECRET, EMAIL, PASSWORD_STR);

    assertNotNull(accessToken);
  }

  private String obtainAccessToken(String clientId, String clientSecret, String username,
      String password) throws URISyntaxException {
    final String url = this.getUrl(TOKEN_PATH);

    final Map<String, String> params = new HashMap<>();

    params.put("grant_type", PASSWORD_GRANT_TYPE);
    params.put("client_id", clientId);
    params.put("client_secret", clientSecret);
    params.put("username", username);
    params.put("password", password);

    final Response response = RestAssured.given().auth().preemptive().basic(clientId, clientSecret)
        .and().with().params(params).when().post(url);

    return response.jsonPath().getString("access_token");
  }

  @Test
  public void testGetToken() throws URISyntaxException {
    // Request by password
    final ResponseEntity<OAuthResponse> responseEntity = super.getFullTokenResponse();
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

    final OAuthResponse response = responseEntity.getBody();
    assertNotNull(response);
    assertNotNull(response.getAccessToken());

    final User testUser = this.getTestUser();
    assertEquals(testUser.getId(), response.getPrincipalId());
    assertEquals(testUser.getUsername(), response.getPrincipalName());
    assertEquals(testUser.getEmail(), response.getPrincipalEmail());
  }

  @Test
  public void testGetTokenNoAuthorizationHeader() throws URISyntaxException {
    final HttpHeaders headers = getHeaders();

    final String url = this.getUrl(TOKEN_PATH);

    final OAuthRequest objAuth = new OAuthRequest();
    objAuth.setUsername(getTestUser().getEmail());
    objAuth.setEmail(getTestUser().getEmail());
    objAuth.setPassword(PASSWORD.clone());
    objAuth.setGrantType(PASSWORD_GRANT_TYPE);
    objAuth.setClientId(CLIENT_ID);
    objAuth.setClientSecret(CLIENT_SECRET);

    final HttpEntity<OAuthRequest> requestEntity = new HttpEntity<>(objAuth, headers);

    // Request by password
    final ResponseEntity<OAuthResponse> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.POST, requestEntity, OAuthResponse.class);
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

    final OAuthResponse response = responseEntity.getBody();
    assertNotNull(response);
    assertNotNull(response.getAccessToken());

    final User testUser = this.getTestUser();
    assertEquals(testUser.getId(), response.getPrincipalId());
    assertEquals(testUser.getUsername(), response.getPrincipalName());
    assertEquals(testUser.getEmail(), response.getPrincipalEmail());
  }

  @Test
  public void testGetTokenNotActiveUser() throws URISyntaxException {
    // Add user
    final User model = createUser("Bar", "bar@email.com", null, getTestRole());
    getUserService().setPassword(model.getId(), PASSWORD.clone());
    getUserService().setEnabled(model.getId(), false);
    getUserService().verify(model.getId());

    final HttpHeaders headers = getHeaders();

    final String url = this.getUrl(TOKEN_PATH);

    final OAuthRequest objAuth = new OAuthRequest();
    objAuth.setUsername(model.getEmail());
    objAuth.setEmail(model.getEmail());
    objAuth.setPassword(PASSWORD.clone());
    objAuth.setGrantType(PASSWORD_GRANT_TYPE);
    objAuth.setClientId(CLIENT_ID);
    objAuth.setClientSecret(CLIENT_SECRET);

    final HttpEntity<OAuthRequest> requestEntity = new HttpEntity<>(objAuth, headers);

    // Request by password
    final ResponseEntity<OAuthResponse> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.POST, requestEntity, OAuthResponse.class);

    deleteUser(model);

    assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
  }

  @Test
  public void testGetTokenNotVerifiedUser() throws URISyntaxException {
    // Add user
    final User model = createUser("Bar", "bar@email.com", null, getTestRole());
    getUserService().add(model);
    getUserService().setPassword(model.getId(), PASSWORD.clone());
    getUserService().setEnabled(model.getId(), true);

    final HttpHeaders headers = getHeaders();

    final String url = this.getUrl(TOKEN_PATH);

    final OAuthRequest objAuth = new OAuthRequest();
    objAuth.setUsername(getTestUser().getEmail());
    objAuth.setEmail(getTestUser().getEmail());
    objAuth.setPassword(PASSWORD.clone());
    objAuth.setGrantType(PASSWORD_GRANT_TYPE);
    objAuth.setClientId(CLIENT_ID);
    objAuth.setClientSecret(CLIENT_SECRET);

    final HttpEntity<OAuthRequest> requestEntity = new HttpEntity<>(objAuth, headers);

    // Request by password
    final ResponseEntity<OAuthResponse> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.POST, requestEntity, OAuthResponse.class);

    deleteUser(model);

    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

    final OAuthResponse response = responseEntity.getBody();
    assertNotNull(response);
    assertNotNull(response.getAccessToken());

    final User testUser = this.getTestUser();
    assertEquals(testUser.getId(), response.getPrincipalId());
    assertEquals(testUser.getUsername(), response.getPrincipalName());
    assertEquals(testUser.getEmail(), response.getPrincipalEmail());
  }

  @Test
  public void testGetTokenWrongUser() throws URISyntaxException {
    final HttpHeaders headers = getHeaders();

    final String url = this.getUrl(TOKEN_PATH);

    final OAuthRequest objAuth = new OAuthRequest();
    objAuth.setUsername("dummy_user");
    objAuth.setEmail("dummy_user@noob.com");
    objAuth.setPassword(PASSWORD.clone());
    objAuth.setGrantType(PASSWORD_GRANT_TYPE);
    objAuth.setClientId(CLIENT_ID);
    objAuth.setClientSecret(CLIENT_SECRET);

    final HttpEntity<OAuthRequest> requestEntity = new HttpEntity<>(objAuth, headers);

    // Request by password
    final ResponseEntity<OAuthResponse> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.POST, requestEntity, OAuthResponse.class);

    assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
  }

  @Test
  public void testGetTokenNoUser() throws URISyntaxException {
    final HttpHeaders headers = getHeaders();

    final String url = this.getUrl(TOKEN_PATH);

    final OAuthRequest objAuth = new OAuthRequest();
    objAuth.setPassword(PASSWORD.clone());
    objAuth.setGrantType(PASSWORD_GRANT_TYPE);
    objAuth.setClientId(CLIENT_ID);
    objAuth.setClientSecret(CLIENT_SECRET);

    final HttpEntity<OAuthRequest> requestEntity = new HttpEntity<>(objAuth, headers);

    // Request by password
    final ResponseEntity<OAuthResponse> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.POST, requestEntity, OAuthResponse.class);

    assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
  }

  @Test
  public void testGetTokenWrongPassword() throws URISyntaxException {
    final HttpHeaders headers = getHeaders();

    final String url = this.getUrl(TOKEN_PATH);

    final OAuthRequest objAuth = new OAuthRequest();
    objAuth.setUsername(getTestUser().getEmail());
    objAuth.setEmail(getTestUser().getEmail());
    objAuth.setPassword(new char[] {});
    objAuth.setGrantType(PASSWORD_GRANT_TYPE);
    objAuth.setClientId(CLIENT_ID);
    objAuth.setClientSecret(CLIENT_SECRET);

    final HttpEntity<OAuthRequest> requestEntity = new HttpEntity<>(objAuth, headers);

    // Request by password
    final ResponseEntity<OAuthResponse> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.POST, requestEntity, OAuthResponse.class);

    assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
  }

  @Test
  public void testGetTokenNoPassword() throws URISyntaxException {
    final HttpHeaders headers = getHeaders();

    final String url = this.getUrl(TOKEN_PATH);

    final OAuthRequest objAuth = new OAuthRequest();
    objAuth.setUsername(getTestUser().getEmail());
    objAuth.setEmail(getTestUser().getEmail());
    objAuth.setGrantType(PASSWORD_GRANT_TYPE);
    objAuth.setClientId(CLIENT_ID);
    objAuth.setClientSecret(CLIENT_SECRET);

    final HttpEntity<OAuthRequest> requestEntity = new HttpEntity<>(objAuth, headers);

    // Request by password
    final ResponseEntity<OAuthResponse> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.POST, requestEntity, OAuthResponse.class);

    assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
  }

  @Test
  public void testGetTokenWrongGrantType() throws URISyntaxException {
    final HttpHeaders headers = getHeaders();

    final String url = this.getUrl(TOKEN_PATH);

    final OAuthRequest objAuth = new OAuthRequest();
    objAuth.setUsername(getTestUser().getEmail());
    objAuth.setEmail(getTestUser().getEmail());
    objAuth.setPassword(PASSWORD.clone());
    objAuth.setGrantType("dummy_grant_type");
    objAuth.setClientId(CLIENT_ID);
    objAuth.setClientSecret(CLIENT_SECRET);

    final HttpEntity<OAuthRequest> requestEntity = new HttpEntity<>(objAuth, headers);

    // Request by password
    final ResponseEntity<OAuthResponse> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.POST, requestEntity, OAuthResponse.class);

    assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
  }

  @Test
  public void testGetTokenNoGrantType() throws URISyntaxException {
    final HttpHeaders headers = getHeaders();

    final String url = this.getUrl(TOKEN_PATH);

    final OAuthRequest objAuth = new OAuthRequest();
    objAuth.setUsername(getTestUser().getEmail());
    objAuth.setEmail(getTestUser().getEmail());
    objAuth.setPassword(PASSWORD.clone());
    objAuth.setClientId(CLIENT_ID);
    objAuth.setClientSecret(CLIENT_SECRET);

    final HttpEntity<OAuthRequest> requestEntity = new HttpEntity<>(objAuth, headers);

    // Request by password
    final ResponseEntity<OAuthResponse> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.POST, requestEntity, OAuthResponse.class);

    assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
  }

  @Test
  public void testGetTokenWrongClient() throws URISyntaxException {
    final HttpHeaders headers = getHeaders();

    final String url = this.getUrl(TOKEN_PATH);

    final OAuthRequest objAuth = new OAuthRequest();
    objAuth.setUsername(getTestUser().getEmail());
    objAuth.setEmail(getTestUser().getEmail());
    objAuth.setPassword(PASSWORD.clone());
    objAuth.setGrantType(PASSWORD_GRANT_TYPE);
    objAuth.setClientId("dummy_client");
    objAuth.setClientSecret(CLIENT_SECRET);

    final HttpEntity<OAuthRequest> requestEntity = new HttpEntity<>(objAuth, headers);

    // Request by password
    final ResponseEntity<OAuthResponse> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.POST, requestEntity, OAuthResponse.class);

    assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
  }

  @Test
  public void testGetTokenNoClient() throws URISyntaxException {
    final HttpHeaders headers = getHeaders();

    final String url = this.getUrl(TOKEN_PATH);

    final OAuthRequest objAuth = new OAuthRequest();
    objAuth.setUsername(getTestUser().getEmail());
    objAuth.setEmail(getTestUser().getEmail());
    objAuth.setPassword(PASSWORD.clone());
    objAuth.setGrantType(PASSWORD_GRANT_TYPE);
    objAuth.setClientSecret(CLIENT_SECRET);

    final HttpEntity<OAuthRequest> requestEntity = new HttpEntity<>(objAuth, headers);

    // Request by password
    final ResponseEntity<OAuthResponse> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.POST, requestEntity, OAuthResponse.class);

    assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
  }

  @Test
  public void testGetTokenWrongClientSecret() throws URISyntaxException {
    final HttpHeaders headers = getHeaders();

    final String url = this.getUrl(TOKEN_PATH);

    final OAuthRequest objAuth = new OAuthRequest();
    objAuth.setUsername(getTestUser().getEmail());
    objAuth.setEmail(getTestUser().getEmail());
    objAuth.setPassword(PASSWORD.clone());
    objAuth.setGrantType(PASSWORD_GRANT_TYPE);
    objAuth.setClientId(CLIENT_ID);
    objAuth.setClientSecret("dummy_secret");

    final HttpEntity<OAuthRequest> requestEntity = new HttpEntity<>(objAuth, headers);

    // Request by password
    final ResponseEntity<OAuthResponse> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.POST, requestEntity, OAuthResponse.class);

    assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
  }

  @Test
  public void testGetTokenNoClientSecret() throws URISyntaxException {
    final HttpHeaders headers = getHeaders();

    final String url = this.getUrl(TOKEN_PATH);

    final OAuthRequest objAuth = new OAuthRequest();
    objAuth.setUsername(getTestUser().getEmail());
    objAuth.setEmail(getTestUser().getEmail());
    objAuth.setPassword(PASSWORD.clone());
    objAuth.setGrantType(PASSWORD_GRANT_TYPE);
    objAuth.setClientId(CLIENT_ID);

    final HttpEntity<OAuthRequest> requestEntity = new HttpEntity<>(objAuth, headers);

    // Request by password
    final ResponseEntity<OAuthResponse> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.POST, requestEntity, OAuthResponse.class);

    assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
  }

}
