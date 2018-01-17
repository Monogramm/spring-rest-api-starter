/*
 * Creation by madmath03 the 2018-01-09.
 */

package com.monogramm.starter.persistence.parameter.exception;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * {@link ParameterNotFoundException} Unit Test.
 * 
 * @author madmath03
 */
public class ParameterNotFoundExceptionTest {

  private static final String MESSAGE = "Foo";
  private static final Throwable CAUSE = new Exception();

  /**
   * Test method for {@link ParameterNotFoundException#ParameterNotFoundException()}.
   */
  @Test
  public void testParameterNotFoundException() {
    final ParameterNotFoundException exception = new ParameterNotFoundException();

    assertNotNull(exception);
  }

  /**
   * Test method for
   * {@link ParameterNotFoundException#ParameterNotFoundException(java.lang.String)}.
   */
  @Test
  public void testParameterNotFoundExceptionString() {
    final ParameterNotFoundException exception = new ParameterNotFoundException(MESSAGE);

    assertEquals(MESSAGE, exception.getMessage());
  }

  /**
   * Test method for
   * {@link ParameterNotFoundException#ParameterNotFoundException(java.lang.Throwable)}.
   */
  @Test
  public void testParameterNotFoundExceptionThrowable() {
    final ParameterNotFoundException exception = new ParameterNotFoundException(CAUSE);

    assertEquals(CAUSE, exception.getCause());
  }

  /**
   * Test method for
   * {@link ParameterNotFoundException#ParameterNotFoundException(java.lang.String, java.lang.Throwable)}.
   */
  @Test
  public void testParameterNotFoundExceptionStringThrowable() {
    final ParameterNotFoundException exception = new ParameterNotFoundException(MESSAGE, CAUSE);

    assertEquals(MESSAGE, exception.getMessage());
    assertEquals(CAUSE, exception.getCause());
  }

  /**
   * Test method for
   * {@link ParameterNotFoundException#ParameterNotFoundException(java.lang.String, java.lang.Throwable, boolean, boolean)}.
   */
  @Test
  public void testParameterNotFoundExceptionStringThrowableBooleanBoolean() {
    ParameterNotFoundException exception =
        new ParameterNotFoundException(MESSAGE, CAUSE, true, true);

    assertEquals(MESSAGE, exception.getMessage());
    assertEquals(CAUSE, exception.getCause());

    exception = new ParameterNotFoundException(MESSAGE, CAUSE, true, false);

    assertEquals(MESSAGE, exception.getMessage());
    assertEquals(CAUSE, exception.getCause());

    exception = new ParameterNotFoundException(MESSAGE, CAUSE, false, true);

    assertEquals(MESSAGE, exception.getMessage());
    assertEquals(CAUSE, exception.getCause());

    exception = new ParameterNotFoundException(MESSAGE, CAUSE, false, false);

    assertEquals(MESSAGE, exception.getMessage());
    assertEquals(CAUSE, exception.getCause());
  }

}
