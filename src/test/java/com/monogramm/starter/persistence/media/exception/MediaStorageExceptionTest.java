/*
 * Creation by madmath03 the 2019-01-24.
 */

package com.monogramm.starter.persistence.media.exception;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * {@link MediaStorageExceptionTest} Unit Test.
 * 
 * @author madmath03
 */
public class MediaStorageExceptionTest {

  private static final String MESSAGE = "Foo";
  private static final Throwable CAUSE = new Exception();

  /**
   * Test method for {@link MediaStorageException#MediaStorageException()}.
   */
  @Test
  public void testMediaStorageException() {
    final MediaStorageException exception = new MediaStorageException();

    assertNotNull(exception);
  }

  /**
   * Test method for {@link MediaStorageException#MediaStorageException(java.lang.String)}.
   */
  @Test
  public void testMediaStorageExceptionString() {
    final MediaStorageException exception = new MediaStorageException(MESSAGE);

    assertEquals(MESSAGE, exception.getMessage());
  }

  /**
   * Test method for {@link MediaStorageException#MediaStorageException(java.lang.Throwable)}.
   */
  @Test
  public void testMediaStorageExceptionThrowable() {
    final MediaStorageException exception = new MediaStorageException(CAUSE);

    assertEquals(CAUSE, exception.getCause());
  }

  /**
   * Test method for
   * {@link MediaStorageException#MediaStorageException(java.lang.String, java.lang.Throwable)}.
   */
  @Test
  public void testMediaStorageExceptionStringThrowable() {
    final MediaStorageException exception = new MediaStorageException(MESSAGE, CAUSE);

    assertEquals(MESSAGE, exception.getMessage());
    assertEquals(CAUSE, exception.getCause());
  }

  /**
   * Test method for
   * {@link MediaStorageException#MediaStorageException(java.lang.String, java.lang.Throwable, boolean, boolean)}.
   */
  @Test
  public void testMediaStorageExceptionStringThrowableBooleanBoolean() {
    MediaStorageException exception = new MediaStorageException(MESSAGE, CAUSE, true, true);

    assertEquals(MESSAGE, exception.getMessage());
    assertEquals(CAUSE, exception.getCause());

    exception = new MediaStorageException(MESSAGE, CAUSE, true, false);

    assertEquals(MESSAGE, exception.getMessage());
    assertEquals(CAUSE, exception.getCause());

    exception = new MediaStorageException(MESSAGE, CAUSE, false, true);

    assertEquals(MESSAGE, exception.getMessage());
    assertEquals(CAUSE, exception.getCause());

    exception = new MediaStorageException(MESSAGE, CAUSE, false, false);

    assertEquals(MESSAGE, exception.getMessage());
    assertEquals(CAUSE, exception.getCause());
  }

}
