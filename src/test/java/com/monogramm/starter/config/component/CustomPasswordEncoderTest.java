/*
 * Creation by madmath03 the 2017-12-03.
 */

package com.monogramm.starter.config.component;

import static org.junit.Assert.assertTrue;

import com.github.madmath03.password.Passwords;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * {@link CustomPasswordEncoder} Unit Test.
 * 
 * @author madmath03
 */
public class CustomPasswordEncoderTest {

  private static final String PASSWORD_STR = "password";
  private static final char[] PASSWORD = PASSWORD_STR.toCharArray();
  private static final String HASHED_PASSWORD;

  static {
    HASHED_PASSWORD = Passwords.getHash(PASSWORD.clone());
  }

  private CustomPasswordEncoder encoder;

  /**
   * @throws java.lang.Exception If test initialization crashes.
   */
  @Before
  public void setUp() throws Exception {
    this.encoder = new CustomPasswordEncoder();
  }

  /**
   * @throws java.lang.Exception If test clean up crashes.
   */
  @After
  public void tearDown() throws Exception {
    this.encoder = null;
  }

  /**
   * Test method for
   * {@link CustomPasswordEncoder#matches(java.lang.CharSequence, java.lang.String)}.
   */
  @Test
  public void testMatches() {
    assertTrue(this.encoder.matches(PASSWORD_STR, HASHED_PASSWORD));
  }

  /**
   * Test method for {@link CustomPasswordEncoder#encode(java.lang.CharSequence)}.
   */
  @Test
  public void testEncode() {
    final String hash = this.encoder.encode(PASSWORD_STR);

    assertTrue(Passwords.isExpectedPassword(PASSWORD_STR, hash));
    assertTrue(Passwords.isExpectedPassword(PASSWORD_STR, HASHED_PASSWORD));
  }

}
