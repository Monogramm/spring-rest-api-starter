/*
 * Creation by madmath03 the 2017-12-20.
 */

package com.monogramm.starter.persistence.user.exception;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * {@link PasswordResetTokenNotFoundException} Unit Test.
 * 
 * @author madmath03
 */
public class PasswordResetTokenNotFoundExceptionTest {

  private static final String MESSAGE = "Foo";
  private static final Throwable CAUSE = new Exception();

  /**
   * Test method for
   * {@link PasswordResetTokenNotFoundException#PasswordResetTokenNotFoundException()}.
   */
  @Test
  public void testPasswordResetTokenNotFoundException() {
    final PasswordResetTokenNotFoundException exception = new PasswordResetTokenNotFoundException();

    assertNotNull(exception);
  }

  /**
   * Test method for
   * {@link PasswordResetTokenNotFoundException#PasswordResetTokenNotFoundException(java.lang.String)}.
   */
  @Test
  public void testPasswordResetTokenNotFoundExceptionString() {
    final PasswordResetTokenNotFoundException exception =
        new PasswordResetTokenNotFoundException(MESSAGE);

    assertEquals(MESSAGE, exception.getMessage());
  }

  /**
   * Test method for
   * {@link PasswordResetTokenNotFoundException#PasswordResetTokenNotFoundException(java.lang.Throwable)}.
   */
  @Test
  public void testPasswordResetTokenNotFoundExceptionThrowable() {
    final PasswordResetTokenNotFoundException exception =
        new PasswordResetTokenNotFoundException(CAUSE);

    assertEquals(CAUSE, exception.getCause());
  }

  /**
   * Test method for
   * {@link PasswordResetTokenNotFoundException#PasswordResetTokenNotFoundException(java.lang.String, java.lang.Throwable)}.
   */
  @Test
  public void testPasswordResetTokenNotFoundExceptionStringThrowable() {
    final PasswordResetTokenNotFoundException exception =
        new PasswordResetTokenNotFoundException(MESSAGE, CAUSE);

    assertEquals(MESSAGE, exception.getMessage());
    assertEquals(CAUSE, exception.getCause());
  }

  /**
   * Test method for
   * {@link PasswordResetTokenNotFoundException#PasswordResetTokenNotFoundException(java.lang.String, java.lang.Throwable, boolean, boolean)}.
   */
  @Test
  public void testPasswordResetTokenNotFoundExceptionStringThrowableBooleanBoolean() {
    PasswordResetTokenNotFoundException exception =
        new PasswordResetTokenNotFoundException(MESSAGE, CAUSE, true, true);

    assertEquals(MESSAGE, exception.getMessage());
    assertEquals(CAUSE, exception.getCause());

    exception = new PasswordResetTokenNotFoundException(MESSAGE, CAUSE, true, false);

    assertEquals(MESSAGE, exception.getMessage());
    assertEquals(CAUSE, exception.getCause());

    exception = new PasswordResetTokenNotFoundException(MESSAGE, CAUSE, false, true);

    assertEquals(MESSAGE, exception.getMessage());
    assertEquals(CAUSE, exception.getCause());

    exception = new PasswordResetTokenNotFoundException(MESSAGE, CAUSE, false, false);

    assertEquals(MESSAGE, exception.getMessage());
    assertEquals(CAUSE, exception.getCause());
  }

}
