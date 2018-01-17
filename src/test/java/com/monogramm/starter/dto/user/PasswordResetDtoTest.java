/*
 * Creation by madmath03 the 2017-12-20.
 */

package com.monogramm.starter.dto.user;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import com.monogramm.starter.utils.validation.PasswordConfirmationDtoTest;

import org.junit.Test;

/**
 * {@link PasswordResetDto} Unit Test.
 * 
 * @author madmath03
 */
public class PasswordResetDtoTest extends PasswordConfirmationDtoTest {
  private static final String EMAIL = "foo@email.com";
  private static final String TOKEN = "Foo";

  @Override
  protected PasswordResetDto buildTestDto() {
    return new PasswordResetDto();
  }

  /**
   * Test method for {@link PasswordResetDto#PasswordResetDto()}.
   */
  @Test
  public void testPasswordResetDto() {
    final PasswordResetDto registrationDto = this.buildTestDto();

    assertNull(registrationDto.getEmail());

    assertNull(registrationDto.getPassword());
    assertNull(registrationDto.getMatchingPassword());
  }

  /**
   * Test method for
   * {@link PasswordResetDto#PasswordResetDto(java.lang.String, java.lang.String, char[], char[])}.
   */
  @Test
  public void testPasswordResetDtoStringStringCharArrayCharArray() {
    final PasswordResetDto registrationDto = new PasswordResetDto(EMAIL, TOKEN, PASSWORD, DROWSSAP);

    assertEquals(EMAIL, registrationDto.getEmail());

    assertNotEquals(PASSWORD, registrationDto.getPassword());
    assertArrayEquals(PASSWORD, registrationDto.getPassword());

    assertNotEquals(DROWSSAP, registrationDto.getMatchingPassword());
    assertArrayEquals(DROWSSAP, registrationDto.getMatchingPassword());
  }

  /**
   * Test method for {@link PasswordResetDto#getEmail()}.
   */
  @Test
  public void testGetEmail() {
    PasswordResetDto registrationDto = this.buildTestDto();

    assertNull(registrationDto.getEmail());

    registrationDto = new PasswordResetDto(EMAIL, TOKEN, PASSWORD, DROWSSAP);

    assertEquals(EMAIL, registrationDto.getEmail());
  }

  /**
   * Test method for {@link PasswordResetDto#setEmail(java.lang.String)}.
   */
  @Test
  public void testSetEmail() {
    final PasswordResetDto registrationDto = this.buildTestDto();

    registrationDto.setEmail(EMAIL);

    assertEquals(EMAIL, registrationDto.getEmail());
  }

  /**
   * Test method for {@link PasswordResetDto#getToken()}.
   */
  @Test
  public void testGetToken() {
    PasswordResetDto registrationDto = this.buildTestDto();

    assertNull(registrationDto.getEmail());

    registrationDto = new PasswordResetDto(EMAIL, TOKEN, PASSWORD, DROWSSAP);

    assertEquals(TOKEN, registrationDto.getToken());
  }

  /**
   * Test method for {@link PasswordResetDto#setToken(java.lang.String)}.
   */
  @Test
  public void testSetToken() {
    final PasswordResetDto registrationDto = this.buildTestDto();

    registrationDto.setToken(EMAIL);

    assertEquals(EMAIL, registrationDto.getToken());
  }

}
