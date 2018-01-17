/*
 * Creation by madmath03 the 2017-09-03.
 */

package com.monogramm.starter.persistence.user.exception;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.monogramm.starter.persistence.user.exception.UserNotFoundException;

import org.junit.Test;

/**
 * {@link UserNotFoundException} Unit Test.
 * 
 * @author madmath03
 */
public class UserNotFoundExceptionTest {

  private static final String MESSAGE = "Foo";
  private static final Throwable CAUSE = new Exception();

  /**
   * Test method for
   * {@link UserNotFoundException#UserNotFoundException()}.
   */
  @Test
  public void testUserNotFoundException() {
    final UserNotFoundException exception = new UserNotFoundException();

    assertNotNull(exception);
  }

  /**
   * Test method for
   * {@link UserNotFoundException#UserNotFoundException(java.lang.String)}.
   */
  @Test
  public void testUserNotFoundExceptionString() {
    final UserNotFoundException exception = new UserNotFoundException(MESSAGE);

    assertEquals(MESSAGE, exception.getMessage());
  }

  /**
   * Test method for
   * {@link UserNotFoundException#UserNotFoundException(java.lang.Throwable)}.
   */
  @Test
  public void testUserNotFoundExceptionThrowable() {
    final UserNotFoundException exception = new UserNotFoundException(CAUSE);

    assertEquals(CAUSE, exception.getCause());
  }

  /**
   * Test method for
   * {@link UserNotFoundException#UserNotFoundException(java.lang.String, java.lang.Throwable)}.
   */
  @Test
  public void testUserNotFoundExceptionStringThrowable() {
    final UserNotFoundException exception = new UserNotFoundException(MESSAGE, CAUSE);

    assertEquals(MESSAGE, exception.getMessage());
    assertEquals(CAUSE, exception.getCause());
  }

  /**
   * Test method for
   * {@link UserNotFoundException#UserNotFoundException(java.lang.String, java.lang.Throwable, boolean, boolean)}.
   */
  @Test
  public void testUserNotFoundExceptionStringThrowableBooleanBoolean() {
    UserNotFoundException exception = new UserNotFoundException(MESSAGE, CAUSE, true, true);

    assertEquals(MESSAGE, exception.getMessage());
    assertEquals(CAUSE, exception.getCause());

    exception = new UserNotFoundException(MESSAGE, CAUSE, true, false);

    assertEquals(MESSAGE, exception.getMessage());
    assertEquals(CAUSE, exception.getCause());

    exception = new UserNotFoundException(MESSAGE, CAUSE, false, true);

    assertEquals(MESSAGE, exception.getMessage());
    assertEquals(CAUSE, exception.getCause());

    exception = new UserNotFoundException(MESSAGE, CAUSE, false, false);

    assertEquals(MESSAGE, exception.getMessage());
    assertEquals(CAUSE, exception.getCause());
  }

}
