/*
 * Creation by madmath03 the 2019-01-24.
 */

package com.monogramm.starter.persistence.media.exception;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * {@link MediaNotFoundExceptionTest} Unit Test.
 * 
 * @author madmath03
 */
public class MediaNotFoundExceptionTest {

  private static final String MESSAGE = "Foo";
  private static final Throwable CAUSE = new Exception();

  /**
   * Test method for {@link MediaNotFoundException#MediaNotFoundException()}.
   */
  @Test
  public void testMediaNotFoundException() {
    final MediaNotFoundException exception = new MediaNotFoundException();

    assertNotNull(exception);
  }

  /**
   * Test method for {@link MediaNotFoundException#MediaNotFoundException(java.lang.String)}.
   */
  @Test
  public void testMediaNotFoundExceptionString() {
    final MediaNotFoundException exception = new MediaNotFoundException(MESSAGE);

    assertEquals(MESSAGE, exception.getMessage());
  }

  /**
   * Test method for {@link MediaNotFoundException#MediaNotFoundException(java.lang.Throwable)}.
   */
  @Test
  public void testMediaNotFoundExceptionThrowable() {
    final MediaNotFoundException exception = new MediaNotFoundException(CAUSE);

    assertEquals(CAUSE, exception.getCause());
  }

  /**
   * Test method for
   * {@link MediaNotFoundException#MediaNotFoundException(java.lang.String, java.lang.Throwable)}.
   */
  @Test
  public void testMediaNotFoundExceptionStringThrowable() {
    final MediaNotFoundException exception = new MediaNotFoundException(MESSAGE, CAUSE);

    assertEquals(MESSAGE, exception.getMessage());
    assertEquals(CAUSE, exception.getCause());
  }

  /**
   * Test method for
   * {@link MediaNotFoundException#MediaNotFoundException(java.lang.String, java.lang.Throwable, boolean, boolean)}.
   */
  @Test
  public void testMediaNotFoundExceptionStringThrowableBooleanBoolean() {
    MediaNotFoundException exception = new MediaNotFoundException(MESSAGE, CAUSE, true, true);

    assertEquals(MESSAGE, exception.getMessage());
    assertEquals(CAUSE, exception.getCause());

    exception = new MediaNotFoundException(MESSAGE, CAUSE, true, false);

    assertEquals(MESSAGE, exception.getMessage());
    assertEquals(CAUSE, exception.getCause());

    exception = new MediaNotFoundException(MESSAGE, CAUSE, false, true);

    assertEquals(MESSAGE, exception.getMessage());
    assertEquals(CAUSE, exception.getCause());

    exception = new MediaNotFoundException(MESSAGE, CAUSE, false, false);

    assertEquals(MESSAGE, exception.getMessage());
    assertEquals(CAUSE, exception.getCause());
  }

}
