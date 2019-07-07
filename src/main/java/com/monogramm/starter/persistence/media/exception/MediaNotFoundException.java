/*
 * Creation by madmath03 the 2019-01-24.
 */

package com.monogramm.starter.persistence.media.exception;

import com.monogramm.starter.persistence.EntityNotFoundException;
import com.monogramm.starter.persistence.media.entity.Media;

/**
 * <em>"{@link Media} not found"</em> Exception.
 * 
 * @author madmath03
 */
public class MediaNotFoundException extends EntityNotFoundException {

  /**
   * The {@code serialVersionUID}.
   */
  private static final long serialVersionUID = -856246176128256794L;

  /**
   * Create a {@link MediaNotFoundException}.
   * 
   */
  public MediaNotFoundException() {
    super();
  }

  /**
   * Create a {@link MediaNotFoundException}.
   * 
   * @param message the detail message.
   */
  public MediaNotFoundException(String message) {
    super(message);
  }

  /**
   * Create a {@link MediaNotFoundException}.
   * 
   * @param cause the cause. (A {@code null} value is permitted, and indicates that the cause is
   *        nonexistent or unknown.)
   */
  public MediaNotFoundException(Throwable cause) {
    super(cause);
  }

  /**
   * Create a {@link MediaNotFoundException}.
   * 
   * @param message the detail message.
   * @param cause the cause. (A {@code null} value is permitted, and indicates that the cause is
   *        nonexistent or unknown.)
   */
  public MediaNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Create a {@link MediaNotFoundException}.
   * 
   * @param message the detail message.
   * @param cause the cause. (A {@code null} value is permitted, and indicates that the cause is
   *        nonexistent or unknown.)
   * @param enableSuppression whether or not suppression is enabled or disabled
   * @param writableStackTrace whether or not the stack trace should be writable
   */
  public MediaNotFoundException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
