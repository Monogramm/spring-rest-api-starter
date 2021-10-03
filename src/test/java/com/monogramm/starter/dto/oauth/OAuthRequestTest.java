/*
 * Creation by madmath03 the 2017-09-06.
 */

package com.monogramm.starter.dto.oauth;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.monogramm.starter.dto.oauth.OAuthRequest;

import org.junit.Test;

/**
 * {@link OAuthRequest} Unit Test.
 * 
 * @author madmath03
 */
public class OAuthRequestTest {

  private static final String USERNAME = "TEST";

  private static final String EMAIL = "TEST@TEST.COM";

  private static final char[] PASSWORD = {'p', 'a', 's', 's', 'w', 'o', 'r', 'd'};

  private static final String PASSWORD_GRANT_TYPE = "password";

  private static final String CLIENT_ID = "clientIdPassword";
  private static final String CLIENT_SECRET = "secret";

  /**
   * Test method for {@link OAuthRequest#OAuthRequest()}.
   */
  @Test
  public void testOAuthRequest() {
    final OAuthRequest request = new OAuthRequest();

    assertNotNull(request);

    assertNull(request.getUsername());
    assertNull(request.getEmail());
    assertNull(request.getPassword());
    assertNull(request.getGrantType());
    assertNull(request.getClientId());
    assertNull(request.getClientSecret());
  }

  /**
   * Test method for {@link OAuthRequest#OAuthRequest(String, String, char[], String)}.
   */
  @Test
  public void testOAuthRequestStringStringCharArrayString() {
    final OAuthRequest request =
        new OAuthRequest(USERNAME, EMAIL, PASSWORD, PASSWORD_GRANT_TYPE, CLIENT_ID, CLIENT_SECRET);

    assertNotNull(request);

    assertEquals(USERNAME, request.getUsername());
    assertEquals(EMAIL, request.getEmail());
    assertArrayEquals(PASSWORD, request.getPassword());
    assertEquals(PASSWORD_GRANT_TYPE, request.getGrantType());
    assertEquals(CLIENT_ID, request.getClientId());
    assertEquals(CLIENT_SECRET, request.getClientSecret());
  }

  /**
   * Test method for {@link OAuthRequest#OAuthRequest(String, String, char[], String)}.
   */
  @Test
  public void testOAuthRequestStringStringNullString() {
    final OAuthRequest request =
        new OAuthRequest(USERNAME, EMAIL, null, PASSWORD_GRANT_TYPE, CLIENT_ID, CLIENT_SECRET);

    assertNotNull(request);

    assertEquals(USERNAME, request.getUsername());
    assertEquals(EMAIL, request.getEmail());
    assertArrayEquals(null, request.getPassword());
    assertEquals(PASSWORD_GRANT_TYPE, request.getGrantType());
    assertEquals(CLIENT_ID, request.getClientId());
    assertEquals(CLIENT_SECRET, request.getClientSecret());
  }

  /**
   * Test method for {@link OAuthRequest#getUsername()}.
   */
  @Test
  public void testGetUsername() {
    final OAuthRequest request = new OAuthRequest();

    assertNull(request.getUsername());
  }

  /**
   * Test method for {@link OAuthRequest#setUsername(java.lang.String)}.
   */
  @Test
  public void testSetUsername() {
    final OAuthRequest request = new OAuthRequest();

    request.setUsername(USERNAME);

    assertEquals(USERNAME, request.getUsername());
  }

  /**
   * Test method for {@link OAuthRequest#getEmail()}.
   */
  @Test
  public void testGetEmail() {
    final OAuthRequest request = new OAuthRequest();

    assertNull(request.getEmail());
  }

  /**
   * Test method for {@link OAuthRequest#setEmail(java.lang.String)}.
   */
  @Test
  public void testSetEmail() {
    final OAuthRequest request = new OAuthRequest();

    request.setEmail(EMAIL);

    assertEquals(EMAIL, request.getEmail());
  }

  /**
   * Test method for {@link OAuthRequest#getPassword()}.
   */
  @Test
  public void testGetPassword() {
    final OAuthRequest request = new OAuthRequest();

    assertNull(request.getPassword());
  }

  /**
   * Test method for {@link OAuthRequest#setPassword(char[])}.
   */
  @Test
  public void testSetPassword() {
    final OAuthRequest request = new OAuthRequest();

    request.setPassword(null);

    assertNull(request.getPassword());

    request.setPassword(PASSWORD);

    assertArrayEquals(PASSWORD, request.getPassword());
  }

  /**
   * Test method for {@link OAuthRequest#getGrantType()}.
   */
  @Test
  public void testGetGrantType() {
    final OAuthRequest request = new OAuthRequest();

    assertNull(request.getGrantType());
  }

  /**
   * Test method for {@link OAuthRequest#setGrantType(java.lang.String)}.
   */
  @Test
  public void testSetGrantType() {
    final OAuthRequest request = new OAuthRequest();

    request.setGrantType(PASSWORD_GRANT_TYPE);

    assertEquals(PASSWORD_GRANT_TYPE, request.getGrantType());
  }

  /**
   * Test method for {@link OAuthRequest#getClientId()}.
   */
  @Test
  public void testGetClientId() {
    final OAuthRequest request = new OAuthRequest();

    assertNull(request.getClientId());
  }

  /**
   * Test method for {@link OAuthRequest#setClientId(java.lang.String)}.
   */
  @Test
  public void testSetClientId() {
    final OAuthRequest request = new OAuthRequest();

    request.setClientId(CLIENT_ID);

    assertEquals(CLIENT_ID, request.getClientId());
  }

  /**
   * Test method for {@link OAuthRequest#getClientSecret()}.
   */
  @Test
  public void testGetClientSecret() {
    final OAuthRequest request = new OAuthRequest();

    assertNull(request.getClientSecret());
  }

  /**
   * Test method for {@link OAuthRequest#setClientSecret(java.lang.String)}.
   */
  @Test
  public void testSetClientSecret() {
    final OAuthRequest request = new OAuthRequest();

    request.setClientSecret(EMAIL);

    assertEquals(EMAIL, request.getClientSecret());
  }

}
