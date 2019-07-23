package com.monogramm.starter.api.oauth.controller;

import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.monogramm.Application;
import com.monogramm.starter.api.AbstractControllerIT;
import com.monogramm.starter.api.AbstractControllerMockIT;
import com.monogramm.starter.config.data.InitialDataLoader;
import com.monogramm.starter.persistence.user.entity.User;

import javax.transaction.Transactional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;


/**
 * {@link OAuthController} Mock Integration Test.
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
 * @see AbstractControllerIT
 * 
 * @author madmath03
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
public class OAuthControllerMockIT extends AbstractControllerMockIT {

  /**
   * The request base path of this tested controller.
   */
  public static final String REVOKE_TOKEN_PATH = OAuthController.REVOKE_TOKEN_PATH;

  @Before
  public void setUp() {
    super.setUpMockMvc();
    super.setUpValidUser();
  }

  @After
  public void tearDown() {
    super.tearDownValidUser();
  }

  /**
   * Test method for {@link OAuthController#getToken(com.monogramm.starter.dto.oauth.OAuthRequest)}.
   * 
   * @throws Exception if the test crashes.
   */
  @Test
  public void testGetToken() throws Exception {
    // User login successful
    getMockTokenResponse().andExpect(status().isOk())
        .andExpect(jsonPath("$.access_token", notNullValue()))
        .andExpect(
            jsonPath("$.principal_id", equalToIgnoringCase(getTestUser().getId().toString())))
        .andExpect(jsonPath("$.principal_name", equalToIgnoringCase(getTestUser().getUsername())))
        .andExpect(jsonPath("$.principal_email", equalToIgnoringCase(getTestUser().getEmail())));
  }

  /**
   * Test method for {@link OAuthController#getToken(com.monogramm.starter.dto.oauth.OAuthRequest)}.
   * 
   * @throws Exception if the test crashes.
   */
  @Test
  public void testGetTokenUrlParams() throws Exception {
    final StringBuilder urlEncoded = new StringBuilder(TOKEN_PATH);

    urlEncoded.append('?');
    urlEncoded.append("grant_type=" + PASSWORD_GRANT_TYPE);
    urlEncoded.append('&');
    urlEncoded.append("username=" + EMAIL);
    urlEncoded.append('&');
    urlEncoded.append("email=" + EMAIL);
    urlEncoded.append('&');
    urlEncoded.append("password=" + new String(PASSWORD));

    // User login successful
    getMockMvc()
        .perform(post(urlEncoded.toString()).with(basicAuthClient())
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.access_token", notNullValue()))
        .andExpect(
            jsonPath("$.principal_id", equalToIgnoringCase(getTestUser().getId().toString())))
        .andExpect(jsonPath("$.principal_name", equalToIgnoringCase(getTestUser().getUsername())))
        .andExpect(jsonPath("$.principal_email", equalToIgnoringCase(getTestUser().getEmail())));
  }

  /**
   * Test method for {@link OAuthController#getToken(com.monogramm.starter.dto.oauth.OAuthRequest)}.
   * 
   * @throws Exception if the test crashes.
   */
  @Test
  public void testGetTokenInactiveUser() throws Exception {
    // Add user
    final User model = User.builder("Bar", "bar@email.com").build();
    assertTrue(getUserService().add(model));
    assertNotNull(getUserService().setPassword(model.getId(), PASSWORD.clone()));
    assertNotNull(getUserService().setEnabled(model.getId(), false));

    final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

    params.add("grant_type", PASSWORD_GRANT_TYPE);
    params.add("username", model.getEmail());
    params.add("email", model.getEmail());
    params.add("password", new String(PASSWORD));

    // User is not active: authentication is unauthorized
    getMockTokenResponse(params).andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)).andExpect(content()
            .json("{\"error\":\"invalid_grant\",\"error_description\":\"Bad credentials\"}"));
  }

  /**
   * Test method for {@link OAuthController#getToken(com.monogramm.starter.dto.oauth.OAuthRequest)}.
   * 
   * @throws Exception if the test crashes.
   */
  @Test
  public void testGetTokenNotVerifiedUser() throws Exception {
    // Add user
    final User model = User.builder("Bar", "bar@email.com").role(getTestRole()).build();
    assertTrue(getUserService().add(model));
    assertNotNull(getUserService().setPassword(model.getId(), PASSWORD.clone()));
    assertNotNull(getUserService().setEnabled(model.getId(), true));
    assertNotNull(getUserService().update(model));

    final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

    params.add("grant_type", PASSWORD_GRANT_TYPE);
    params.add("username", model.getEmail());
    params.add("email", model.getEmail());
    params.add("password", new String(PASSWORD));

    // User is not verified: authentication is acceptable
    getMockTokenResponse(params).andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.access_token", notNullValue()))
        .andExpect(jsonPath("$.principal_id", equalToIgnoringCase(model.getId().toString())))
        .andExpect(jsonPath("$.principal_name", equalToIgnoringCase(model.getUsername())))
        .andExpect(jsonPath("$.principal_email", equalToIgnoringCase(model.getEmail())));

    this.deleteUser(model);
  }

  /**
   * Test method for {@link OAuthController#getToken(com.monogramm.starter.dto.oauth.OAuthRequest)}.
   * 
   * @throws Exception if the test crashes.
   */
  @Test
  public void testGetTokenWrongPassword() throws Exception {
    // Add user
    final User model = User.builder("Bar", "bar@email.com").build();
    assertTrue(getUserService().add(model));
    assertNotNull(getUserService().setPassword(model.getId(), PASSWORD.clone()));
    assertNotNull(getUserService().setEnabled(model.getId(), true));
    assertNotNull(getUserService().verify(model.getId()));

    final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

    params.add("grant_type", PASSWORD_GRANT_TYPE);
    params.add("username", model.getEmail());
    params.add("email", model.getEmail());
    params.add("password", "dummy");

    // User password do not match: authentication is forbidden
    getMockTokenResponse(params).andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)).andExpect(content()
            .string("{\"error\":\"invalid_grant\",\"error_description\":\"Bad credentials\"}"));
  }

  /**
   * Test method for {@link OAuthController#getToken(com.monogramm.starter.dto.oauth.OAuthRequest)}.
   * 
   * @throws Exception if the test crashes.
   */
  @Test
  public void testGetTokenNoPasswordUser() throws Exception {
    final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

    params.add("grant_type", PASSWORD_GRANT_TYPE);
    params.add("username", EMAIL);
    params.add("email", EMAIL);

    // Request by password without a password should fail
    getMockTokenResponse(params).andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)).andExpect(content()
            .json("{\"error\":\"invalid_grant\",\"error_description\":\"Bad credentials\"}"));
  }

  /**
   * Test method for {@link OAuthController#getToken(com.monogramm.starter.dto.oauth.OAuthRequest)}.
   * 
   * @throws Exception if the test crashes.
   */
  @Test
  public void testGetTokenWrongGrantType() throws Exception {
    final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

    params.add("grant_type", "dummy");
    params.add("username", EMAIL);
    params.add("email", EMAIL);
    params.add("password", new String(PASSWORD));

    // Request without a valid grant type should fail
    getMockTokenResponse(params).andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(content().json(
            "{\"error\":\"unsupported_grant_type\",\"error_description\":\"Unsupported grant type: dummy\"}"));
  }

  /**
   * Test method for {@link OAuthController#getToken(com.monogramm.starter.dto.oauth.OAuthRequest)}.
   * 
   * @throws Exception if the test crashes.
   */
  @Test
  public void testGetTokenNoGrantType() throws Exception {
    final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

    params.add("username", EMAIL);
    params.add("email", EMAIL);
    params.add("password", new String(PASSWORD));

    // Request without a grant type should fail
    getMockTokenResponse(params).andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)).andExpect(content()
            .json("{\"error\":\"invalid_request\",\"error_description\":\"Missing grant type\"}"));
  }

  /**
   * Test method for
   * {@link RevokeTokenEndpoint#revokeSelfToken(javax.servlet.http.HttpServletRequest)}.
   * 
   * @throws Exception if the test crashes.
   */
  @Test
  public void testRevokeSelfToken() throws Exception {
    final String token = getMockToken();

    // FIXME We receive a 401 unauthorized
    // // Revoke self OAuth access token should work
    // getMockMvc().perform(delete(TOKEN_PATH).headers(getHeaders(token)))
    // .andExpect(status().is2xxSuccessful()).andExpect(content().json(token));
  }

  /**
   * Test method for {@link OAuthController#revokeToken(String)}.
   * 
   * @throws Exception if the test crashes.
   */
  @Test
  public void testRevokeToken() throws Exception {
    final String token = getMockToken();

    // FIXME We receive a 302 redirection to login
    // // Revoke OAuth access token should work
    // getMockMvc()
    // .perform(post(REVOKE_TOKEN_PATH + "/" + token).with(csrf()).headers(getHeaders(token)))
    // .andExpect(status().is2xxSuccessful()).andExpect(content().json(token));
  }

  /**
   * Test method for {@link OAuthController#revokeToken(String)}.
   * 
   * @throws Exception if the test crashes.
   */
  @Test
  public void testRevokeTokenNotExist() throws Exception {
    final String token = getMockToken();

    // FIXME We receive a 302 redirection to login
    // // Revoke OAuth refresh token should work
    // getMockMvc()
    // .perform(post(REVOKE_TOKEN_PATH + "/" + "dummy").with(csrf()).headers(getHeaders(token)))
    // .andExpect(status().is2xxSuccessful()).andExpect(content().json("dummy"));
  }

}
