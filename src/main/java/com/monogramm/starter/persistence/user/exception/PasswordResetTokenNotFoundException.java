/*
 * Creation by madmath03 the 2017-12-20.
 */

package com.monogramm.starter.persistence.user.exception;

import com.monogramm.starter.persistence.EntityNotFoundException;

/**
 * PasswordResetNotFoundException.
 * 
 * @author madmath03
 */
public class PasswordResetTokenNotFoundException extends EntityNotFoundException {

  /**
   * The {@code serialVersionUID}.
   */
  private static final long serialVersionUID = -7716571082815053276L;

  /**
   * Create a {@link PasswordResetTokenNotFoundException}.
   * 
   */
  public PasswordResetTokenNotFoundException() {
    super();
  }

  /**
   * Create a {@link PasswordResetTokenNotFoundException}.
   * 
   * @param message the detail message.
   */
  public PasswordResetTokenNotFoundException(String message) {
    super(message);
  }

  /**
   * Create a {@link PasswordResetTokenNotFoundException}.
   * 
   * @param cause the cause. (A {@code null} value is permitted, and indicates that the cause is
   *        nonexistent or unknown.)
   */
  public PasswordResetTokenNotFoundException(Throwable cause) {
    super(cause);
  }

  /**
   * Create a {@link PasswordResetTokenNotFoundException}.
   * 
   * @param message the detail message.
   * @param cause the cause. (A {@code null} value is permitted, and indicates that the cause is
   *        nonexistent or unknown.)
   */
  public PasswordResetTokenNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Create a {@link PasswordResetTokenNotFoundException}.
   * 
   * @param message the detail message.
   * @param cause the cause. (A {@code null} value is permitted, and indicates that the cause is
   *        nonexistent or unknown.)
   * @param enableSuppression whether or not suppression is enabled or disabled
   * @param writableStackTrace whether or not the stack trace should be writable
   */
  public PasswordResetTokenNotFoundException(String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
