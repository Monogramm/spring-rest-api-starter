/*
 * Creation by madmath03 the 2017-11-10.
 */

package com.monogramm.starter.dto.user;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import com.monogramm.starter.utils.validation.PasswordConfirmationDtoTest;

import org.junit.Test;

/**
 * {@link RegistrationDto} Unit Test.
 * 
 * @author madmath03
 */
public class RegistrationDtoTest extends PasswordConfirmationDtoTest {

  private static final String USERNAME = "Foo";
  private static final String EMAIL = "foo@email.com";

  @Override
  protected RegistrationDto buildTestDto() {
    return new RegistrationDto();
  }

  /**
   * Test method for {@link RegistrationDto#RegistrationDto()}.
   */
  @Test
  public void testRegistrationDto() {
    final RegistrationDto registrationDto = this.buildTestDto();

    assertNull(registrationDto.getUsername());

    assertNull(registrationDto.getEmail());

    assertNull(registrationDto.getPassword());
    assertNull(registrationDto.getMatchingPassword());
  }

  /**
   * Test method for
   * {@link RegistrationDto#RegistrationDto(java.lang.String, java.lang.String, char[], char[])}.
   */
  @Test
  public void testRegistrationDtoStringStringCharArrayCharArray() {
    final RegistrationDto registrationDto =
        new RegistrationDto(USERNAME, EMAIL, PASSWORD, DROWSSAP);

    assertEquals(USERNAME, registrationDto.getUsername());

    assertEquals(EMAIL, registrationDto.getEmail());

    assertNotEquals(PASSWORD, registrationDto.getPassword());
    assertArrayEquals(PASSWORD, registrationDto.getPassword());

    assertNotEquals(DROWSSAP, registrationDto.getMatchingPassword());
    assertArrayEquals(DROWSSAP, registrationDto.getMatchingPassword());
  }

  /**
   * Test method for
   * {@link RegistrationDto#RegistrationDto(java.lang.String, java.lang.String, char[], char[])}.
   */
  @Test(expected = NullPointerException.class)
  public void testRegistrationDtoStringStringNullCharArray() {
    new RegistrationDto(USERNAME, EMAIL, null, DROWSSAP);
  }

  /**
   * Test method for
   * {@link RegistrationDto#RegistrationDto(java.lang.String, java.lang.String, char[], char[])}.
   */
  @Test(expected = NullPointerException.class)
  public void testRegistrationDtoStringStringCharArrayNull() {
    new RegistrationDto(USERNAME, EMAIL, PASSWORD, null);
  }

  /**
   * Test method for
   * {@link RegistrationDto#RegistrationDto(java.lang.String, java.lang.String, char[], char[])}.
   */
  @Test(expected = NullPointerException.class)
  public void testRegistrationDtoStringStringNullNull() {
    new RegistrationDto(USERNAME, EMAIL, null, null);
  }

  /**
   * Test method for {@link RegistrationDto#getUsername()}.
   */
  @Test
  public void testGetUsername() {
    RegistrationDto registrationDto = this.buildTestDto();

    assertNull(registrationDto.getUsername());

    registrationDto = new RegistrationDto(USERNAME, EMAIL, PASSWORD, DROWSSAP);

    assertEquals(USERNAME, registrationDto.getUsername());
  }

  /**
   * Test method for {@link RegistrationDto#setUsername(java.lang.String)}.
   */
  @Test
  public void testSetUsername() {
    final RegistrationDto registrationDto = this.buildTestDto();

    registrationDto.setUsername(USERNAME);

    assertEquals(USERNAME, registrationDto.getUsername());
  }

  /**
   * Test method for {@link RegistrationDto#getEmail()}.
   */
  @Test
  public void testGetEmail() {
    RegistrationDto registrationDto = this.buildTestDto();

    assertNull(registrationDto.getEmail());

    registrationDto = new RegistrationDto(USERNAME, EMAIL, PASSWORD, DROWSSAP);

    assertEquals(EMAIL, registrationDto.getEmail());
  }

  /**
   * Test method for {@link RegistrationDto#setEmail(java.lang.String)}.
   */
  @Test
  public void testSetEmail() {
    final RegistrationDto registrationDto = this.buildTestDto();

    registrationDto.setEmail(EMAIL);

    assertEquals(EMAIL, registrationDto.getEmail());
  }

}
