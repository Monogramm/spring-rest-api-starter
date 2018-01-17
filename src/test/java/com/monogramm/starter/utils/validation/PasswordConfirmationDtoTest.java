/*
 * Creation by madmath03 the 2017-11-10.
 */

package com.monogramm.starter.utils.validation;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * {@link PasswordConfirmationDto} Unit Test.
 * 
 * @author madmath03
 */
public class PasswordConfirmationDtoTest {
  protected static final char[] PASSWORD = {'p', 'a', 's', 's', 'w', 'o', 'r', 'd'};
  protected static final char[] DROWSSAP = {'d', 'r', 'o', 'w', 's', 's', 'a', 'p'};

  protected PasswordConfirmationDto buildTestDto() {
    return new PasswordConfirmationDto();
  }

  /**
   * Test method for {@link PasswordConfirmationDto#PasswordConfirmationDTO()}.
   */
  @Test
  public void testPasswordConfirmationDTO() {
    final PasswordConfirmationDto passwordConfirmationDto = this.buildTestDto();

    assertNull(passwordConfirmationDto.getPassword());
    assertNull(passwordConfirmationDto.getMatchingPassword());
  }

  /**
   * Test method for {@link PasswordConfirmationDto#PasswordConfirmationDTO(char[], char[])}.
   */
  @Test
  public void testPasswordConfirmationDTOCharArrayCharArray() {
    final PasswordConfirmationDto passwordConfirmationDto =
        new PasswordConfirmationDto(PASSWORD, DROWSSAP);

    assertNotEquals(PASSWORD, passwordConfirmationDto.getPassword());
    assertArrayEquals(PASSWORD, passwordConfirmationDto.getPassword());
    assertNotEquals(DROWSSAP, passwordConfirmationDto.getMatchingPassword());
    assertArrayEquals(DROWSSAP, passwordConfirmationDto.getMatchingPassword());
  }

  /**
   * Test method for {@link PasswordConfirmationDto#PasswordConfirmationDTO(char[], char[])}.
   */
  @Test(expected = NullPointerException.class)
  public void testPasswordConfirmationDTONullCharArray() {
    new PasswordConfirmationDto(null, DROWSSAP);
  }

  /**
   * Test method for {@link PasswordConfirmationDto#PasswordConfirmationDTO(char[], char[])}.
   */
  @Test(expected = NullPointerException.class)
  public void testPasswordConfirmationDTOCharArrayNull() {
    new PasswordConfirmationDto(PASSWORD, null);
  }

  /**
   * Test method for {@link PasswordConfirmationDto#PasswordConfirmationDTO(char[], char[])}.
   */
  @Test(expected = NullPointerException.class)
  public void testPasswordConfirmationDTONullNull() {
    new PasswordConfirmationDto(null, null);
  }

  /**
   * Test method for {@link PasswordConfirmationDto#getPassword()}.
   */
  @Test
  public void testGetPassword() {
    PasswordConfirmationDto passwordConfirmationDto = this.buildTestDto();

    assertNull(passwordConfirmationDto.getPassword());

    passwordConfirmationDto = new PasswordConfirmationDto(PASSWORD, DROWSSAP);

    assertNotEquals(PASSWORD, passwordConfirmationDto.getPassword());
    assertArrayEquals(PASSWORD, passwordConfirmationDto.getPassword());
  }

  /**
   * Test method for {@link PasswordConfirmationDto#setPassword(char[])}.
   */
  @Test
  public void testSetPassword() {
    final PasswordConfirmationDto passwordConfirmationDto = this.buildTestDto();

    passwordConfirmationDto.setPassword(PASSWORD);

    assertNotEquals(PASSWORD, passwordConfirmationDto.getPassword());
    assertArrayEquals(PASSWORD, passwordConfirmationDto.getPassword());

    passwordConfirmationDto.setPassword(DROWSSAP);

    assertNotEquals(DROWSSAP, passwordConfirmationDto.getPassword());
    assertArrayEquals(DROWSSAP, passwordConfirmationDto.getPassword());
  }

  /**
   * Test method for {@link PasswordConfirmationDto#setPassword(char[])}.
   */
  @Test(expected = NullPointerException.class)
  public void testSetPasswordNull() {
    final PasswordConfirmationDto passwordConfirmationDto = this.buildTestDto();

    passwordConfirmationDto.setPassword(null);
  }

  /**
   * Test method for {@link PasswordConfirmationDto#getMatchingPassword()}.
   */
  @Test
  public void testGetMatchingPassword() {
    PasswordConfirmationDto passwordConfirmationDto = this.buildTestDto();

    assertNull(passwordConfirmationDto.getPassword());

    passwordConfirmationDto = new PasswordConfirmationDto(PASSWORD, DROWSSAP);

    assertNotEquals(DROWSSAP, passwordConfirmationDto.getMatchingPassword());
    assertArrayEquals(DROWSSAP, passwordConfirmationDto.getMatchingPassword());
  }

  /**
   * Test method for {@link PasswordConfirmationDto#setMatchingPassword(char[])}.
   */
  @Test
  public void testSetMatchingPassword() {
    final PasswordConfirmationDto passwordConfirmationDto = this.buildTestDto();

    passwordConfirmationDto.setMatchingPassword(PASSWORD);

    assertNotEquals(PASSWORD, passwordConfirmationDto.getMatchingPassword());
    assertArrayEquals(PASSWORD, passwordConfirmationDto.getMatchingPassword());

    passwordConfirmationDto.setMatchingPassword(DROWSSAP);

    assertNotEquals(DROWSSAP, passwordConfirmationDto.getMatchingPassword());
    assertArrayEquals(DROWSSAP, passwordConfirmationDto.getMatchingPassword());
  }

  /**
   * Test method for {@link PasswordConfirmationDto#setMatchingPassword(char[])}.
   */
  @Test(expected = NullPointerException.class)
  public void testSetMatchingPasswordNull() {
    final PasswordConfirmationDto passwordConfirmationDto = this.buildTestDto();

    passwordConfirmationDto.setMatchingPassword(null);
  }

}
