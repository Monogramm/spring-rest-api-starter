/*
 * Creation by madmath03 the 2018-01-09.
 */

package com.monogramm.starter.persistence.parameter.exception;

import com.monogramm.starter.persistence.EntityNotFoundException;
import com.monogramm.starter.persistence.parameter.entity.Parameter;

/**
 * <em>"{@link Parameter}  not found"</em> Exception.
 * 
 * @author madmath03
 */
public class ParameterNotFoundException extends EntityNotFoundException {

  /**
   * The {@code serialVersionUID}.
   */
  private static final long serialVersionUID = 5143585822244266125L;

  /**
   * Create a {@link ParameterNotFoundException}.
   * 
   */
  public ParameterNotFoundException() {
    super();
  }

  /**
   * Create a {@link ParameterNotFoundException}.
   * 
   * @param message the detail message.
   */
  public ParameterNotFoundException(String message) {
    super(message);
  }

  /**
   * Create a {@link ParameterNotFoundException}.
   * 
   * @param cause the cause. (A {@code null} value is permitted, and indicates that the cause is
   *        nonexistent or unknown.)
   */
  public ParameterNotFoundException(Throwable cause) {
    super(cause);
  }

  /**
   * Create a {@link ParameterNotFoundException}.
   * 
   * @param message the detail message.
   * @param cause the cause. (A {@code null} value is permitted, and indicates that the cause is
   *        nonexistent or unknown.)
   */
  public ParameterNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Create a {@link ParameterNotFoundException}.
   * 
   * @param message the detail message.
   * @param cause the cause. (A {@code null} value is permitted, and indicates that the cause is
   *        nonexistent or unknown.)
   * @param enableSuppression whether or not suppression is enabled or disabled
   * @param writableStackTrace whether or not the stack trace should be writable
   */
  public ParameterNotFoundException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
