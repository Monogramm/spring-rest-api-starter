/*
 * Creation by madmath03 the 2017-11-25
 */

package com.monogramm.starter.persistence.permission.exception;

import com.monogramm.starter.persistence.EntityNotFoundException;
import com.monogramm.starter.persistence.permission.entity.Permission;

/**
 * <em>"{@link Permission}  not found"</em> Exception.
 * 
 * @author madmath03
 */
public class PermissionNotFoundException extends EntityNotFoundException {

  /**
   * The {@code serialVersionUID}.
   */
  private static final long serialVersionUID = 4331389591826563678L;

  /**
   * Create a {@link PermissionNotFoundException}.
   * 
   */
  public PermissionNotFoundException() {
    super();
  }

  /**
   * Create a {@link PermissionNotFoundException}.
   * 
   * @param message the detail message.
   */
  public PermissionNotFoundException(String message) {
    super(message);
  }

  /**
   * Create a {@link PermissionNotFoundException}.
   * 
   * @param cause the cause. (A {@code null} value is permitted, and indicates that the cause is
   *        nonexistent or unknown.)
   */
  public PermissionNotFoundException(Throwable cause) {
    super(cause);
  }

  /**
   * Create a {@link PermissionNotFoundException}.
   * 
   * @param message the detail message.
   * @param cause the cause. (A {@code null} value is permitted, and indicates that the cause is
   *        nonexistent or unknown.)
   */
  public PermissionNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Create a {@link PermissionNotFoundException}.
   * 
   * @param message the detail message.
   * @param cause the cause. (A {@code null} value is permitted, and indicates that the cause is
   *        nonexistent or unknown.)
   * @param enableSuppression whether or not suppression is enabled or disabled
   * @param writableStackTrace whether or not the stack trace should be writable
   */
  public PermissionNotFoundException(String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
