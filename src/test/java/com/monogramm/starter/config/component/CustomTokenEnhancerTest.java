/*
 * Creation by madmath03 the 2017-12-03.
 */

package com.monogramm.starter.config.component;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.monogramm.starter.persistence.user.entity.User;
import com.monogramm.starter.persistence.user.service.IUserService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

/**
 * {@link CustomTokenEnhancer} Unit Test.
 * 
 * @author madmath03
 */
public class CustomTokenEnhancerTest {

  private static final String DUMMY_TOKEN = UUID.randomUUID().toString();

  private static final String DUMMY_NAME = "PRINCIPAL_ID";

  private static final String DUMMY_USERNAME = "USERNAME";
  private static final String DUMMY_EMAIL = "EMAIL";

  private static final String DUMMY_ROLE = "ROLE_DUMMY";
  private static final String DUMMY_AUTH = "DUMMIES";
  private static final Collection<GrantedAuthority> DUMMY_AUTHORITIES = new ArrayList<>();

  private IUserService userService;
  private CustomTokenEnhancer enhancer;
  private OAuth2Authentication oauthAuthentication;

  /**
   * @throws java.lang.Exception If test class initialization crashes.
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    DUMMY_AUTHORITIES.add(new SimpleGrantedAuthority(DUMMY_ROLE));
    DUMMY_AUTHORITIES.add(new SimpleGrantedAuthority(DUMMY_AUTH));
    DUMMY_AUTHORITIES.add(new GrantedAuthority() {
      /**
       * The {@code serialVersionUID}.
       */
      private static final long serialVersionUID = 1L;

      @Override
      public String getAuthority() {
        return "";
      }

    });
    DUMMY_AUTHORITIES.add(new GrantedAuthority() {
      /**
       * The {@code serialVersionUID}.
       */
      private static final long serialVersionUID = 1L;

      @Override
      public String getAuthority() {
        return null;
      }

    });
  }

  /**
   * @throws java.lang.Exception If test class clean up crashes.
   */
  @AfterClass
  public static void tearDownAfterClass() throws Exception {
    DUMMY_AUTHORITIES.clear();
  }

  /**
   * @throws java.lang.Exception If test initialization crashes.
   */
  @Before
  public void setUp() throws Exception {
    this.userService = mock(IUserService.class);

    this.enhancer = new CustomTokenEnhancer(this.userService);

    this.oauthAuthentication = mock(OAuth2Authentication.class);
  }

  /**
   * @throws java.lang.Exception If test clean up crashes.
   */
  @After
  public void tearDown() throws Exception {
    Mockito.reset(userService);

    this.enhancer = null;

    Mockito.reset(oauthAuthentication);
  }

  /**
   * Test method for {@link CustomTokenEnhancer#enhance(OAuth2AccessToken, OAuth2Authentication)}.
   */
  @Test
  public void testEnhance() {
    final User user = User.builder(DUMMY_USERNAME, DUMMY_EMAIL).id(UUID.randomUUID()).build();
    final OAuth2AccessToken token = new DefaultOAuth2AccessToken(DUMMY_TOKEN);

    when(oauthAuthentication.getName()).thenReturn(DUMMY_NAME);

    when(userService.findByUsernameOrEmail(DUMMY_NAME, DUMMY_NAME)).thenReturn(user);

    when(oauthAuthentication.getAuthorities()).thenReturn(DUMMY_AUTHORITIES);

    final OAuth2AccessToken enhancedToken = this.enhancer.enhance(token, oauthAuthentication);

    verify(oauthAuthentication, times(1)).getName();
    verify(oauthAuthentication, times(1)).getAuthorities();
    verifyNoMoreInteractions(oauthAuthentication);

    verify(userService, times(1)).findByUsernameOrEmail(DUMMY_NAME, DUMMY_NAME);
    verifyNoMoreInteractions(userService);

    assertNotNull(enhancedToken);
    assertEquals(token, enhancedToken);

    final Map<String, Object> additionalInformation = enhancedToken.getAdditionalInformation();
    assertNotNull(additionalInformation);
    assertNotNull(additionalInformation.get(CustomTokenEnhancer.TIMESTAMP));

    assertEquals(user.getId(), additionalInformation.get(CustomTokenEnhancer.UUID));
    assertEquals(user.getUsername(), additionalInformation.get(CustomTokenEnhancer.USERNAME));
    assertEquals(user.getEmail(), additionalInformation.get(CustomTokenEnhancer.EMAIL));
    assertTrue(additionalInformation.containsKey(CustomTokenEnhancer.VERIFIED));

    assertArrayEquals(new String[] {DUMMY_AUTH},
        (String[]) additionalInformation.get(CustomTokenEnhancer.PERMISSIONS));
    assertArrayEquals(new String[] {DUMMY_ROLE},
        (String[]) additionalInformation.get(CustomTokenEnhancer.ROLES));
  }

  /**
   * Test method for {@link CustomTokenEnhancer#enhance(OAuth2AccessToken, OAuth2Authentication)}.
   */
  @Test
  public void testEnhanceNoUser() {
    final OAuth2AccessToken token = new DefaultOAuth2AccessToken(DUMMY_TOKEN);

    when(oauthAuthentication.getName()).thenReturn(DUMMY_NAME);
    when(oauthAuthentication.getAuthorities()).thenReturn(DUMMY_AUTHORITIES);

    when(userService.findByUsernameOrEmail(DUMMY_NAME, DUMMY_NAME)).thenReturn(null);

    final OAuth2AccessToken enhancedToken = this.enhancer.enhance(token, oauthAuthentication);

    verify(oauthAuthentication, times(1)).getName();
    verify(oauthAuthentication, times(1)).getAuthorities();
    verifyNoMoreInteractions(oauthAuthentication);

    verify(userService, times(1)).findByUsernameOrEmail(DUMMY_NAME, DUMMY_NAME);
    verifyNoMoreInteractions(userService);

    assertNotNull(enhancedToken);
    assertEquals(token, enhancedToken);

    final Map<String, Object> additionalInformation = enhancedToken.getAdditionalInformation();
    assertNotNull(additionalInformation);
    assertNotNull(additionalInformation.get(CustomTokenEnhancer.TIMESTAMP));

    assertFalse(additionalInformation.containsKey(CustomTokenEnhancer.UUID));
    assertFalse(additionalInformation.containsKey(CustomTokenEnhancer.USERNAME));
    assertFalse(additionalInformation.containsKey(CustomTokenEnhancer.EMAIL));
    assertFalse(additionalInformation.containsKey(CustomTokenEnhancer.VERIFIED));

    assertArrayEquals(new String[] {DUMMY_AUTH},
        (String[]) additionalInformation.get(CustomTokenEnhancer.PERMISSIONS));
    assertArrayEquals(new String[] {DUMMY_ROLE},
        (String[]) additionalInformation.get(CustomTokenEnhancer.ROLES));
  }

  /**
   * Test method for {@link CustomTokenEnhancer#enhance(OAuth2AccessToken, OAuth2Authentication)}.
   */
  @Test
  public void testEnhanceEmptyAuthorities() {
    final OAuth2AccessToken token = new DefaultOAuth2AccessToken(DUMMY_TOKEN);

    when(oauthAuthentication.getName()).thenReturn("");
    when(oauthAuthentication.getAuthorities()).thenReturn(Collections.emptyList());

    final OAuth2AccessToken enhancedToken = this.enhancer.enhance(token, oauthAuthentication);

    verify(oauthAuthentication, times(1)).getName();
    verify(oauthAuthentication, times(1)).getAuthorities();
    verifyNoMoreInteractions(oauthAuthentication);

    assertNotNull(enhancedToken);
    assertEquals(token, enhancedToken);

    final Map<String, Object> additionalInformation = enhancedToken.getAdditionalInformation();
    assertNotNull(additionalInformation);
    assertNotNull(additionalInformation.get(CustomTokenEnhancer.TIMESTAMP));

    assertFalse(additionalInformation.containsKey(CustomTokenEnhancer.UUID));
    assertFalse(additionalInformation.containsKey(CustomTokenEnhancer.USERNAME));
    assertFalse(additionalInformation.containsKey(CustomTokenEnhancer.EMAIL));
    assertFalse(additionalInformation.containsKey(CustomTokenEnhancer.VERIFIED));

    assertArrayEquals(new String[] {},
        (String[]) additionalInformation.get(CustomTokenEnhancer.PERMISSIONS));
    assertArrayEquals(new String[] {},
        (String[]) additionalInformation.get(CustomTokenEnhancer.ROLES));
  }

  /**
   * Test method for {@link CustomTokenEnhancer#enhance(OAuth2AccessToken, OAuth2Authentication)}.
   */
  @Test
  public void testEnhanceNullAuthorities() {
    final OAuth2AccessToken token = new DefaultOAuth2AccessToken(DUMMY_TOKEN);

    when(oauthAuthentication.getName()).thenReturn(null);
    when(oauthAuthentication.getAuthorities()).thenReturn(null);

    final OAuth2AccessToken enhancedToken = this.enhancer.enhance(token, oauthAuthentication);

    verify(oauthAuthentication, times(1)).getName();
    verify(oauthAuthentication, times(1)).getAuthorities();
    verifyNoMoreInteractions(oauthAuthentication);

    assertNotNull(enhancedToken);
    assertEquals(token, enhancedToken);

    final Map<String, Object> additionalInformation = enhancedToken.getAdditionalInformation();
    assertNotNull(additionalInformation);
    assertNotNull(additionalInformation.get(CustomTokenEnhancer.TIMESTAMP));

    assertFalse(additionalInformation.containsKey(CustomTokenEnhancer.UUID));
    assertFalse(additionalInformation.containsKey(CustomTokenEnhancer.USERNAME));
    assertFalse(additionalInformation.containsKey(CustomTokenEnhancer.EMAIL));
    assertFalse(additionalInformation.containsKey(CustomTokenEnhancer.VERIFIED));

    assertArrayEquals(new String[] {},
        (String[]) additionalInformation.get(CustomTokenEnhancer.PERMISSIONS));
    assertArrayEquals(new String[] {},
        (String[]) additionalInformation.get(CustomTokenEnhancer.ROLES));
  }

}
