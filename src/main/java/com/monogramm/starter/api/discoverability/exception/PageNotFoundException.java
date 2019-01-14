/*
 * Creation by madmath03 the 2019-01-13.
 */

package com.monogramm.starter.api.discoverability.exception;

import com.monogramm.starter.api.AbstractGenericController;
import com.monogramm.starter.persistence.EntityNotFoundException;

/**
 * PageNotFoundException.
 * 
 * @see AbstractGenericController#getAllDataPaginated(int, int,
 *      org.springframework.web.util.UriComponentsBuilder, javax.servlet.http.HttpServletResponse)
 * 
 * @author madmath03
 */
public class PageNotFoundException extends EntityNotFoundException {

  /**
   * The {@code serialVersionUID}.
   */
  private static final long serialVersionUID = -3035888005084885296L;

  /**
   * Create a {@link PageNotFoundException}.
   * 
   */
  public PageNotFoundException() {
    super();
  }

  /**
   * Create a {@link PageNotFoundException}.
   * 
   * @param message the detail message.
   */
  public PageNotFoundException(String message) {
    super(message);
  }

  /**
   * Create a {@link PageNotFoundException}.
   * 
   * @param cause the cause. (A {@code null} value is permitted, and indicates that the cause is
   *        nonexistent or unknown.)
   */
  public PageNotFoundException(Throwable cause) {
    super(cause);
  }

  /**
   * Create a {@link PageNotFoundException}.
   * 
   * @param message the detail message.
   * @param cause the cause. (A {@code null} value is permitted, and indicates that the cause is
   *        nonexistent or unknown.)
   */
  public PageNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Create a {@link PageNotFoundException}.
   * 
   * @param message the detail message.
   * @param cause the cause. (A {@code null} value is permitted, and indicates that the cause is
   *        nonexistent or unknown.)
   * @param enableSuppression whether or not suppression is enabled or disabled
   * @param writableStackTrace whether or not the stack trace should be writable
   */
  public PageNotFoundException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
