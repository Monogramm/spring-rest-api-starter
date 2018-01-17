/*
 * Creation by madmath03 the 2017-12-18.
 */

package com.monogramm.starter.persistence.user.exception;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * {@link VerificationTokenNotFoundException} Unit Test.
 * 
 * @author madmath03
 */
public class VerificationTokenNotFoundExceptionTest {

  private static final String MESSAGE = "Foo";
  private static final Throwable CAUSE = new Exception();

  /**
   * Test method for
   * {@link VerificationTokenNotFoundException#VerificationTokenNotFoundException()}.
   */
  @Test
  public void testVerificationTokenNotFoundException() {
    final VerificationTokenNotFoundException exception = new VerificationTokenNotFoundException();

    assertNotNull(exception);
  }

  /**
   * Test method for
   * {@link VerificationTokenNotFoundException#VerificationTokenNotFoundException(java.lang.String)}.
   */
  @Test
  public void testVerificationTokenNotFoundExceptionString() {
    final VerificationTokenNotFoundException exception =
        new VerificationTokenNotFoundException(MESSAGE);

    assertEquals(MESSAGE, exception.getMessage());
  }

  /**
   * Test method for
   * {@link VerificationTokenNotFoundException#VerificationTokenNotFoundException(java.lang.Throwable)}.
   */
  @Test
  public void testVerificationTokenNotFoundExceptionThrowable() {
    final VerificationTokenNotFoundException exception =
        new VerificationTokenNotFoundException(CAUSE);

    assertEquals(CAUSE, exception.getCause());
  }

  /**
   * Test method for
   * {@link VerificationTokenNotFoundException#VerificationTokenNotFoundException(java.lang.String, java.lang.Throwable)}.
   */
  @Test
  public void testVerificationTokenNotFoundExceptionStringThrowable() {
    final VerificationTokenNotFoundException exception =
        new VerificationTokenNotFoundException(MESSAGE, CAUSE);

    assertEquals(MESSAGE, exception.getMessage());
    assertEquals(CAUSE, exception.getCause());
  }

  /**
   * Test method for
   * {@link VerificationTokenNotFoundException#VerificationTokenNotFoundException(java.lang.String, java.lang.Throwable, boolean, boolean)}.
   */
  @Test
  public void testVerificationTokenNotFoundExceptionStringThrowableBooleanBoolean() {
    VerificationTokenNotFoundException exception =
        new VerificationTokenNotFoundException(MESSAGE, CAUSE, true, true);

    assertEquals(MESSAGE, exception.getMessage());
    assertEquals(CAUSE, exception.getCause());

    exception = new VerificationTokenNotFoundException(MESSAGE, CAUSE, true, false);

    assertEquals(MESSAGE, exception.getMessage());
    assertEquals(CAUSE, exception.getCause());

    exception = new VerificationTokenNotFoundException(MESSAGE, CAUSE, false, true);

    assertEquals(MESSAGE, exception.getMessage());
    assertEquals(CAUSE, exception.getCause());

    exception = new VerificationTokenNotFoundException(MESSAGE, CAUSE, false, false);

    assertEquals(MESSAGE, exception.getMessage());
    assertEquals(CAUSE, exception.getCause());
  }

}
