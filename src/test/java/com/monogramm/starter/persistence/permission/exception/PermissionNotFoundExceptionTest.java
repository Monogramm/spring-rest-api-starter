/*
 * Creation by madmath03 the 2017-11-25
 */

package com.monogramm.starter.persistence.permission.exception;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.monogramm.starter.persistence.permission.exception.PermissionNotFoundException;

import org.junit.Test;

/**
 * {@link PermissionNotFoundException} Unit Test.
 * 
 * @author madmath03
 */
public class PermissionNotFoundExceptionTest {

  private static final String MESSAGE = "Foo";
  private static final Throwable CAUSE = new Exception();

  /**
   * Test method for {@link PermissionNotFoundException#PermissionNotFoundException()}.
   */
  @Test
  public void testPermissionNotFoundException() {
    final PermissionNotFoundException exception = new PermissionNotFoundException();

    assertNotNull(exception);
  }

  /**
   * Test method for
   * {@link PermissionNotFoundException#PermissionNotFoundException(java.lang.String)}.
   */
  @Test
  public void testPermissionNotFoundExceptionString() {
    final PermissionNotFoundException exception = new PermissionNotFoundException(MESSAGE);

    assertEquals(MESSAGE, exception.getMessage());
  }

  /**
   * Test method for
   * {@link PermissionNotFoundException#PermissionNotFoundException(java.lang.Throwable)}.
   */
  @Test
  public void testPermissionNotFoundExceptionThrowable() {
    final PermissionNotFoundException exception = new PermissionNotFoundException(CAUSE);

    assertEquals(CAUSE, exception.getCause());
  }

  /**
   * Test method for
   * {@link PermissionNotFoundException#PermissionNotFoundException(java.lang.String, java.lang.Throwable)}.
   */
  @Test
  public void testPermissionNotFoundExceptionStringThrowable() {
    final PermissionNotFoundException exception = new PermissionNotFoundException(MESSAGE, CAUSE);

    assertEquals(MESSAGE, exception.getMessage());
    assertEquals(CAUSE, exception.getCause());
  }

  /**
   * Test method for
   * {@link PermissionNotFoundException#PermissionNotFoundException(java.lang.String, java.lang.Throwable, boolean, boolean)}.
   */
  @Test
  public void testPermissionNotFoundExceptionStringThrowableBooleanBoolean() {
    PermissionNotFoundException exception =
        new PermissionNotFoundException(MESSAGE, CAUSE, true, true);

    assertEquals(MESSAGE, exception.getMessage());
    assertEquals(CAUSE, exception.getCause());

    exception = new PermissionNotFoundException(MESSAGE, CAUSE, true, false);

    assertEquals(MESSAGE, exception.getMessage());
    assertEquals(CAUSE, exception.getCause());

    exception = new PermissionNotFoundException(MESSAGE, CAUSE, false, true);

    assertEquals(MESSAGE, exception.getMessage());
    assertEquals(CAUSE, exception.getCause());

    exception = new PermissionNotFoundException(MESSAGE, CAUSE, false, false);

    assertEquals(MESSAGE, exception.getMessage());
    assertEquals(CAUSE, exception.getCause());
  }

}
