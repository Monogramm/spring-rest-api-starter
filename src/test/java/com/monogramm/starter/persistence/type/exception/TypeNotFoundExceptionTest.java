/*
 * Creation by madmath03 the 2017-09-04.
 */

package com.monogramm.starter.persistence.type.exception;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.monogramm.starter.persistence.type.exception.TypeNotFoundException;

import org.junit.Test;

/**
 * {@link TypeNotFoundException} Unit Test.
 * 
 * @author madmath03
 */
public class TypeNotFoundExceptionTest {

  private static final String MESSAGE = "Foo";
  private static final Throwable CAUSE = new Exception();

  /**
   * Test method for {@link TypeNotFoundException#TypeNotFoundException()}.
   */
  @Test
  public void testTypeNotFoundException() {
    final TypeNotFoundException exception = new TypeNotFoundException();

    assertNotNull(exception);
  }

  /**
   * Test method for {@link TypeNotFoundException#TypeNotFoundException(java.lang.String)}.
   */
  @Test
  public void testTypeNotFoundExceptionString() {
    final TypeNotFoundException exception = new TypeNotFoundException(MESSAGE);

    assertEquals(MESSAGE, exception.getMessage());
  }

  /**
   * Test method for {@link TypeNotFoundException#TypeNotFoundException(java.lang.Throwable)}.
   */
  @Test
  public void testTypeNotFoundExceptionThrowable() {
    final TypeNotFoundException exception = new TypeNotFoundException(CAUSE);

    assertEquals(CAUSE, exception.getCause());
  }

  /**
   * Test method for
   * {@link TypeNotFoundException#TypeNotFoundException(java.lang.String, java.lang.Throwable)}.
   */
  @Test
  public void testTypeNotFoundExceptionStringThrowable() {
    final TypeNotFoundException exception = new TypeNotFoundException(MESSAGE, CAUSE);

    assertEquals(MESSAGE, exception.getMessage());
    assertEquals(CAUSE, exception.getCause());
  }

  /**
   * Test method for
   * {@link TypeNotFoundException#TypeNotFoundException(java.lang.String, java.lang.Throwable, boolean, boolean)}.
   */
  @Test
  public void testTypeNotFoundExceptionStringThrowableBooleanBoolean() {
    TypeNotFoundException exception = new TypeNotFoundException(MESSAGE, CAUSE, true, true);

    assertEquals(MESSAGE, exception.getMessage());
    assertEquals(CAUSE, exception.getCause());

    exception = new TypeNotFoundException(MESSAGE, CAUSE, true, false);

    assertEquals(MESSAGE, exception.getMessage());
    assertEquals(CAUSE, exception.getCause());

    exception = new TypeNotFoundException(MESSAGE, CAUSE, false, true);

    assertEquals(MESSAGE, exception.getMessage());
    assertEquals(CAUSE, exception.getCause());

    exception = new TypeNotFoundException(MESSAGE, CAUSE, false, false);

    assertEquals(MESSAGE, exception.getMessage());
    assertEquals(CAUSE, exception.getCause());
  }

}
