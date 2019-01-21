/*
 * Creation by madmath03 the 2018-01-09.
 */

package com.monogramm.starter.api.discoverability.exception;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * {@link PageNotFoundException} Unit Test.
 * 
 * @author madmath03
 */
public class PageNotFoundExceptionTest {

  private static final String MESSAGE = "Foo";
  private static final Throwable CAUSE = new Exception();

  /**
   * Test method for {@link PageNotFoundException#PageNotFoundException()}.
   */
  @Test
  public void testPageNotFoundException() {
    final PageNotFoundException exception = new PageNotFoundException();

    assertNotNull(exception);
  }

  /**
   * Test method for {@link PageNotFoundException#PageNotFoundException(java.lang.String)}.
   */
  @Test
  public void testPageNotFoundExceptionString() {
    final PageNotFoundException exception = new PageNotFoundException(MESSAGE);

    assertEquals(MESSAGE, exception.getMessage());
  }

  /**
   * Test method for {@link PageNotFoundException#PageNotFoundException(java.lang.Throwable)}.
   */
  @Test
  public void testPageNotFoundExceptionThrowable() {
    final PageNotFoundException exception = new PageNotFoundException(CAUSE);

    assertEquals(CAUSE, exception.getCause());
  }

  /**
   * Test method for
   * {@link PageNotFoundException#PageNotFoundException(java.lang.String, java.lang.Throwable)}.
   */
  @Test
  public void testPageNotFoundExceptionStringThrowable() {
    final PageNotFoundException exception = new PageNotFoundException(MESSAGE, CAUSE);

    assertEquals(MESSAGE, exception.getMessage());
    assertEquals(CAUSE, exception.getCause());
  }

  /**
   * Test method for
   * {@link PageNotFoundException#PageNotFoundException(java.lang.String, java.lang.Throwable, boolean, boolean)}.
   */
  @Test
  public void testPageNotFoundExceptionStringThrowableBooleanBoolean() {
    PageNotFoundException exception = new PageNotFoundException(MESSAGE, CAUSE, true, true);

    assertEquals(MESSAGE, exception.getMessage());
    assertEquals(CAUSE, exception.getCause());

    exception = new PageNotFoundException(MESSAGE, CAUSE, true, false);

    assertEquals(MESSAGE, exception.getMessage());
    assertEquals(CAUSE, exception.getCause());

    exception = new PageNotFoundException(MESSAGE, CAUSE, false, true);

    assertEquals(MESSAGE, exception.getMessage());
    assertEquals(CAUSE, exception.getCause());

    exception = new PageNotFoundException(MESSAGE, CAUSE, false, false);

    assertEquals(MESSAGE, exception.getMessage());
    assertEquals(CAUSE, exception.getCause());
  }

}
