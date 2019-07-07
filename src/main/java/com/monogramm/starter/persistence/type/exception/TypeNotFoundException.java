/*
 * Creation by madmath03 the 2017-09-03.
 */

package com.monogramm.starter.persistence.type.exception;

import com.monogramm.starter.persistence.EntityNotFoundException;
import com.monogramm.starter.persistence.type.entity.Type;

/**
 * <em>"{@link Type}  not found"</em> Exception.
 * 
 * @author madmath03
 */
public class TypeNotFoundException extends EntityNotFoundException {

  /**
   * The {@code serialVersionUID}.
   */
  private static final long serialVersionUID = 4331389591826563678L;

  /**
   * Create a {@link TypeNotFoundException}.
   * 
   */
  public TypeNotFoundException() {
    super();
  }

  /**
   * Create a {@link TypeNotFoundException}.
   * 
   * @param message the detail message.
   */
  public TypeNotFoundException(String message) {
    super(message);
  }

  /**
   * Create a {@link TypeNotFoundException}.
   * 
   * @param cause the cause. (A {@code null} value is permitted, and indicates that the cause is
   *        nonexistent or unknown.)
   */
  public TypeNotFoundException(Throwable cause) {
    super(cause);
  }

  /**
   * Create a {@link TypeNotFoundException}.
   * 
   * @param message the detail message.
   * @param cause the cause. (A {@code null} value is permitted, and indicates that the cause is
   *        nonexistent or unknown.)
   */
  public TypeNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Create a {@link TypeNotFoundException}.
   * 
   * @param message the detail message.
   * @param cause the cause. (A {@code null} value is permitted, and indicates that the cause is
   *        nonexistent or unknown.)
   * @param enableSuppression whether or not suppression is enabled or disabled
   * @param writableStackTrace whether or not the stack trace should be writable
   */
  public TypeNotFoundException(String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
