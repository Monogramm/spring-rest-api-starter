/*
 * Creation by madmath03 the 2017-11-23.
 */

package com.monogramm.starter.config.component;

import com.github.madmath03.password.Passwords;

import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * CustomPasswordEncoder.
 * 
 * @author madmath03
 */
public class CustomPasswordEncoder implements PasswordEncoder {

  @Override
  public boolean matches(final CharSequence rawPassword, final String encodedPassword) {
    return Passwords.isExpectedPassword(rawPassword, encodedPassword);
  }

  @Override
  public String encode(final CharSequence rawPassword) {
    return Passwords.getHash(this.convert(rawPassword));
  }

  /**
   * Convert a char sequence to a char array.
   * 
   * @param rawPassword a char sequence to convert.
   * 
   * @return a char array.
   */
  private char[] convert(final CharSequence rawPassword) {
    final char[] password = new char[rawPassword.length()];

    for (int i = 0, n = rawPassword.length(); i < n; i++) {
      password[i] = rawPassword.charAt(i);
    }

    return password;
  }
}
