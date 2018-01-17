/*
 * Creation by madmath03 the 2017-09-03.
 */

package com.monogramm.starter.persistence.role.exception;

import com.monogramm.starter.persistence.EntityNotFoundException;

/**
 * RoleNotFoundException.
 * 
 * @author madmath03
 */
public class RoleNotFoundException extends EntityNotFoundException {

  /**
   * The {@code serialVersionUID}.
   */
  private static final long serialVersionUID = 4331389591826563678L;

  /**
   * Create a {@link RoleNotFoundException}.
   * 
   */
  public RoleNotFoundException() {
    super();
  }

  /**
   * Create a {@link RoleNotFoundException}.
   * 
   * @param message the detail message.
   */
  public RoleNotFoundException(String message) {
    super(message);
  }

  /**
   * Create a {@link RoleNotFoundException}.
   * 
   * @param cause the cause. (A {@code null} value is permitted, and indicates that the cause is
   *        nonexistent or unknown.)
   */
  public RoleNotFoundException(Throwable cause) {
    super(cause);
  }

  /**
   * Create a {@link RoleNotFoundException}.
   * 
   * @param message the detail message.
   * @param cause the cause. (A {@code null} value is permitted, and indicates that the cause is
   *        nonexistent or unknown.)
   */
  public RoleNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Create a {@link RoleNotFoundException}.
   * 
   * @param message the detail message.
   * @param cause the cause. (A {@code null} value is permitted, and indicates that the cause is
   *        nonexistent or unknown.)
   * @param enableSuppression whether or not suppression is enabled or disabled
   * @param writableStackTrace whether or not the stack trace should be writable
   */
  public RoleNotFoundException(String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
