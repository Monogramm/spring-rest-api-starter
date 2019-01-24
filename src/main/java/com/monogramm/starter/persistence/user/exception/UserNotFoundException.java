/*
 * Creation by madmath03 the 2017-09-03.
 */

package com.monogramm.starter.persistence.user.exception;

import com.monogramm.starter.persistence.EntityNotFoundException;

/**
 * <em>"{@link User}  not found"</em> Exception.
 * 
 * @author madmath03
 */
public class UserNotFoundException extends EntityNotFoundException {

  /**
   * The {@code serialVersionUID}.
   */
  private static final long serialVersionUID = -5718978633699801683L;

  /**
   * Create a {@link UserNotFoundException}.
   * 
   */
  public UserNotFoundException() {
    super();
  }

  /**
   * Create a {@link UserNotFoundException}.
   * 
   * @param message the detail message.
   */
  public UserNotFoundException(String message) {
    super(message);
  }

  /**
   * Create a {@link UserNotFoundException}.
   * 
   * @param cause the cause. (A {@code null} value is permitted, and indicates that the cause is
   *        nonexistent or unknown.)
   */
  public UserNotFoundException(Throwable cause) {
    super(cause);
  }

  /**
   * Create a {@link UserNotFoundException}.
   * 
   * @param message the detail message.
   * @param cause the cause. (A {@code null} value is permitted, and indicates that the cause is
   *        nonexistent or unknown.)
   */
  public UserNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Create a {@link UserNotFoundException}.
   * 
   * @param message the detail message.
   * @param cause the cause. (A {@code null} value is permitted, and indicates that the cause is
   *        nonexistent or unknown.)
   * @param enableSuppression whether or not suppression is enabled or disabled
   * @param writableStackTrace whether or not the stack trace should be writable
   */
  public UserNotFoundException(String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
