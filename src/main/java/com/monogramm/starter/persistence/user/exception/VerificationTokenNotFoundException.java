/*
 * Creation by madmath03 the 2017-12-18.
 */

package com.monogramm.starter.persistence.user.exception;

import com.monogramm.starter.persistence.EntityNotFoundException;
import com.monogramm.starter.persistence.user.entity.VerificationToken;

/**
 * <em>"{@link VerificationToken}  not found"</em> Exception.
 * 
 * @author madmath03
 */
public class VerificationTokenNotFoundException extends EntityNotFoundException {

  /**
   * The {@code serialVersionUID}.
   */
  private static final long serialVersionUID = -5718978633699801683L;

  /**
   * Create a {@link VerificationTokenNotFoundException}.
   * 
   */
  public VerificationTokenNotFoundException() {
    super();
  }

  /**
   * Create a {@link VerificationTokenNotFoundException}.
   * 
   * @param message the detail message.
   */
  public VerificationTokenNotFoundException(String message) {
    super(message);
  }

  /**
   * Create a {@link VerificationTokenNotFoundException}.
   * 
   * @param cause the cause. (A {@code null} value is permitted, and indicates that the cause is
   *        nonexistent or unknown.)
   */
  public VerificationTokenNotFoundException(Throwable cause) {
    super(cause);
  }

  /**
   * Create a {@link VerificationTokenNotFoundException}.
   * 
   * @param message the detail message.
   * @param cause the cause. (A {@code null} value is permitted, and indicates that the cause is
   *        nonexistent or unknown.)
   */
  public VerificationTokenNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Create a {@link VerificationTokenNotFoundException}.
   * 
   * @param message the detail message.
   * @param cause the cause. (A {@code null} value is permitted, and indicates that the cause is
   *        nonexistent or unknown.)
   * @param enableSuppression whether or not suppression is enabled or disabled
   * @param writableStackTrace whether or not the stack trace should be writable
   */
  public VerificationTokenNotFoundException(String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
