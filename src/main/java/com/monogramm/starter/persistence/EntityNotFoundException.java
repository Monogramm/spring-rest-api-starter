/*
 * Creation by madmath03 the 2017-09-03.
 */

package com.monogramm.starter.persistence;

/**
 * <em>"{@link AbstractGenericEntity} not found"</em> Exception.
 * 
 * @author madmath03
 */
public abstract class EntityNotFoundException extends RuntimeException {

  /**
   * The {@code serialVersionUID}.
   */
  private static final long serialVersionUID = 2016265732131475104L;

  /**
   * Create a {@link EntityNotFoundException}.
   * 
   */
  public EntityNotFoundException() {
    super();
  }

  /**
   * Create a {@link EntityNotFoundException}.
   * 
   * @param message the detail message.
   */
  public EntityNotFoundException(String message) {
    super(message);
  }

  /**
   * Create a {@link EntityNotFoundException}.
   * 
   * @param cause the cause. (A {@code null} value is permitted, and indicates that the cause is
   *        nonexistent or unknown.)
   */
  public EntityNotFoundException(Throwable cause) {
    super(cause);
  }

  /**
   * Create a {@link EntityNotFoundException}.
   * 
   * @param message the detail message.
   * @param cause the cause. (A {@code null} value is permitted, and indicates that the cause is
   *        nonexistent or unknown.)
   */
  public EntityNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Create a {@link EntityNotFoundException}.
   * 
   * @param message the detail message.
   * @param cause the cause. (A {@code null} value is permitted, and indicates that the cause is
   *        nonexistent or unknown.)
   * @param enableSuppression whether or not suppression is enabled or disabled
   * @param writableStackTrace whether or not the stack trace should be writable
   */
  public EntityNotFoundException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
