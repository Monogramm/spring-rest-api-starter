/*
 * Creation by madmath03 the 2017-09-06.
 */

package com.monogramm.starter.dto.oauth;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Date;
import java.util.UUID;

import org.junit.Test;

/**
 * {@link OAuthResponse} Unit Test.
 * 
 * @author madmath03
 */
public class OAuthResponseTest {

  private static final String ACCES_TOKEN = "dummy_token";

  private static final Date TIMESTAMP = new Date();

  private static final UUID ID = UUID.randomUUID();

  private static final String NAME = "dummy_name";

  private static final String EMAIL = "dummy_email";

  private static final String[] AUTHORITIES = {"dummy_authority"};

  private static final String[] ROLES = {"role_dummy"};

  /**
   * Test method for {@link OAuthResponse#OAuthResponse()}.
   */
  @Test
  public void testOAuthResponse() {
    final OAuthResponse response = new OAuthResponse();

    assertNotNull(response);

    assertNull(response.getAccessToken());
    assertNull(response.getTimestamp());
    assertNull(response.getPrincipalId());
    assertNull(response.getPrincipalName());
    assertNull(response.getPrincipalEmail());
    assertNotNull(response.getAuthorities());
    assertEquals(0, response.getAuthorities().length);
    assertNotNull(response.getRoles());
    assertEquals(0, response.getRoles().length);
  }

  /**
   * Test method for {@link OAuthResponse#OAuthResponse(String, String)}.
   */
  @Test
  public void testOAuthResponseStringString() {
    final OAuthResponse response =
        new OAuthResponse(ACCES_TOKEN, TIMESTAMP, ID, NAME, EMAIL, AUTHORITIES, ROLES);

    assertNotNull(response);

    assertEquals(ACCES_TOKEN, response.getAccessToken());
    assertEquals(TIMESTAMP, response.getTimestamp());
    assertEquals(ID, response.getPrincipalId());
    assertEquals(NAME, response.getPrincipalName());
    assertEquals(EMAIL, response.getPrincipalEmail());
    assertArrayEquals(AUTHORITIES, response.getAuthorities());
    assertArrayEquals(ROLES, response.getRoles());
  }

  /**
   * Test method for {@link OAuthResponse#getAccessToken()}.
   */
  @Test
  public void testGetAccessToken() {
    final OAuthResponse response = new OAuthResponse();

    assertNull(response.getAccessToken());
  }

  /**
   * Test method for {@link OAuthResponse#setAccessToken(java.lang.String)}.
   */
  @Test
  public void testSetAccessToken() {
    final OAuthResponse response = new OAuthResponse();

    response.setAccessToken(ACCES_TOKEN);

    assertEquals(ACCES_TOKEN, response.getAccessToken());
  }

  /**
   * Test method for {@link OAuthResponse#getTimestamp()}.
   */
  @Test
  public void testGetTimestamp() {
    final OAuthResponse response = new OAuthResponse();

    assertNull(response.getTimestamp());
  }

  /**
   * Test method for {@link OAuthResponse#setTimestamp(Date)}.
   */
  @Test
  public void testSetTimestamp() {
    final OAuthResponse response = new OAuthResponse();

    response.setTimestamp(TIMESTAMP);

    assertEquals(TIMESTAMP, response.getTimestamp());
  }

  /**
   * Test method for {@link OAuthResponse#getPrincipalId()}.
   */
  @Test
  public void testGetPrincipalId() {
    final OAuthResponse response = new OAuthResponse();

    assertNull(response.getPrincipalId());
  }

  /**
   * Test method for {@link OAuthResponse#setPrincipalId(UUID)}.
   */
  @Test
  public void testSetPrincipalId() {
    final OAuthResponse response = new OAuthResponse();

    response.setPrincipalId(ID);

    assertEquals(ID, response.getPrincipalId());
  }

  /**
   * Test method for {@link OAuthResponse#getPrincipalName()}.
   */
  @Test
  public void testGetPrincipalName() {
    final OAuthResponse response = new OAuthResponse();

    assertNull(response.getPrincipalName());
  }

  /**
   * Test method for {@link OAuthResponse#setPrincipalName(java.lang.String)}.
   */
  @Test
  public void testSetPrincipalName() {
    final OAuthResponse response = new OAuthResponse();

    response.setPrincipalName(NAME);

    assertEquals(NAME, response.getPrincipalName());
  }

  /**
   * Test method for {@link OAuthResponse#getPrincipalEmail()}.
   */
  @Test
  public void testGetPrincipalEmail() {
    final OAuthResponse response = new OAuthResponse();

    assertNull(response.getPrincipalEmail());
  }

  /**
   * Test method for {@link OAuthResponse#setPrincipalEmail(java.lang.String)}.
   */
  @Test
  public void testSetPrincipalEmail() {
    final OAuthResponse response = new OAuthResponse();

    response.setPrincipalEmail(EMAIL);

    assertEquals(EMAIL, response.getPrincipalEmail());
  }

  /**
   * Test method for {@link OAuthResponse#getAuthorities()}.
   */
  @Test
  public void testGetAuthorities() {
    final OAuthResponse response = new OAuthResponse();

    assertNotNull(response.getAuthorities());
    assertEquals(0, response.getAuthorities().length);
  }

  /**
   * Test method for {@link OAuthResponse#setAuthorities(String[])}.
   */
  @Test
  public void testSetAuthorities() {
    final OAuthResponse response = new OAuthResponse();

    response.setAuthorities(AUTHORITIES);

    assertArrayEquals(AUTHORITIES, response.getAuthorities());
  }

  /**
   * Test method for {@link OAuthResponse#getRoles()}.
   */
  @Test
  public void testGetRoles() {
    final OAuthResponse response = new OAuthResponse();

    assertNotNull(response.getRoles());
    assertEquals(0, response.getRoles().length);
  }

  /**
   * Test method for {@link OAuthResponse#setRoles(String[])}.
   */
  @Test
  public void testSetRoles() {
    final OAuthResponse response = new OAuthResponse();

    response.setRoles(ROLES);

    assertArrayEquals(ROLES, response.getRoles());
  }

}
