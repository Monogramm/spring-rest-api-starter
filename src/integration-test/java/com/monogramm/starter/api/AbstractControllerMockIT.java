/*
 * Creation by madmath03 the 2017-12-17.
 */

package com.monogramm.starter.api;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import com.monogramm.starter.config.component.CustomTokenEnhancer;
import com.monogramm.starter.dto.oauth.OAuthRequest;
import com.monogramm.starter.dto.oauth.OAuthResponse;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

/**
 * AbstractControllerMockIT.
 * 
 * @author madmath03
 */
public class AbstractControllerMockIT extends AbstractControllerIT {



  public static RequestPostProcessor basicAuthClient() {
    return httpBasic(CLIENT_ID, CLIENT_SECRET);
  }


  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext webApplicationContext;



  /**
   * Set up a Mock MVC based on the web application context.
   */
  protected void setUpMockMvc() {
    this.mockMvc = webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
  }

  /**
   * Get a valid OAuth2 token response for the test user created by {@link #setUp(String...)}.
   * 
   * @see OAuthRequest
   * @see OAuthResponse
   * @see CustomTokenEnhancer
   * 
   * @throws Exception if the mock fails.
   */
  protected ResultActions getMockTokenResponse() throws Exception {
    final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

    params.add("grant_type", PASSWORD_GRANT_TYPE);
    params.add("username", EMAIL);
    params.add("email", EMAIL);
    params.add("password", new String(PASSWORD));

    return getMockTokenResponse(params);
  }

  /**
   * Get a OAuth2 token response for the given request parameters.
   * 
   * @see OAuthRequest
   * @see OAuthResponse
   * @see CustomTokenEnhancer
   * 
   * @param params the token request parameters.
   * 
   * @throws Exception if the mock fails.
   */
  protected ResultActions getMockTokenResponse(final MultiValueMap<String, String> params)
      throws Exception {
    // User login successful
    return mockMvc.perform(post(TOKEN_PATH).params(params).with(basicAuthClient())
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .accept(MediaType.APPLICATION_JSON_UTF8_VALUE));
  }

  /**
   * Get a OAuth2 token for the test user created by {@link #setUp(String...)}.
   * 
   * @see OAuthRequest
   * @see OAuthResponse
   * @see CustomTokenEnhancer
   * 
   * @throws UnsupportedEncodingException if the encoding is not supported.
   * @throws Exception if the mock fails.
   */
  protected String getMockToken() throws Exception {
    final ResultActions result = this.getMockTokenResponse();

    String resultString = result.andReturn().getResponse().getContentAsString();

    final JacksonJsonParser jsonParser = new JacksonJsonParser();
    return jsonParser.parseMap(resultString).get("access_token").toString();
  }

  /**
   * Get a OAuth2 token for the test given request parameters.
   * 
   * @see OAuthRequest
   * @see OAuthResponse
   * @see CustomTokenEnhancer
   * 
   * @param params the token request parameters.
   * 
   * @throws UnsupportedEncodingException if the encoding is not supported.
   * @throws Exception if the mock fails.
   */
  protected String getMockToken(final MultiValueMap<String, String> params) throws Exception {
    final ResultActions result =
        this.getMockTokenResponse(params).andExpect(status().is2xxSuccessful())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));

    String resultString = result.andReturn().getResponse().getContentAsString();

    final JacksonJsonParser jsonParser = new JacksonJsonParser();
    return jsonParser.parseMap(resultString).get("access_token").toString();
  }



  /**
   * Get the {@link #mockMvc}.
   * 
   * @return the {@link #mockMvc}.
   */
  protected final MockMvc getMockMvc() {
    return mockMvc;
  }

}
