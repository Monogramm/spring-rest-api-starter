/* 
 * Creation by madmath03 the 2019-01-24.
 */

package com.monogramm.starter.persistence.media.exception;

import com.monogramm.starter.persistence.EntityNotFoundException;
import com.monogramm.starter.persistence.media.entity.Media;

/**
 * <em>"{@link Media} storage"</em> Exception.
 * 
 * @see <a href=
 *      "https://www.callicoder.com/spring-boot-file-upload-download-rest-api-example/">Spring Boot
 *      File Upload / Download Rest API Example</a>
 * 
 * @author madmath03
 */
public class MediaStorageException extends EntityNotFoundException {

  /**
   * The {@code serialVersionUID}.
   */
  private static final long serialVersionUID = -856246176128256794L;

  /**
   * Create a {@link MediaStorageException}.
   * 
   */
  public MediaStorageException() {
    super();
  }

  /**
   * Create a {@link MediaStorageException}.
   * 
   * @param message the detail message.
   */
  public MediaStorageException(String message) {
    super(message);
  }

  /**
   * Create a {@link MediaStorageException}.
   * 
   * @param cause the cause. (A {@code null} value is permitted, and indicates that the cause is
   *        nonexistent or unknown.)
   */
  public MediaStorageException(Throwable cause) {
    super(cause);
  }

  /**
   * Create a {@link MediaStorageException}.
   * 
   * @param message the detail message.
   * @param cause the cause. (A {@code null} value is permitted, and indicates that the cause is
   *        nonexistent or unknown.)
   */
  public MediaStorageException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Create a {@link MediaStorageException}.
   * 
   * @param message the detail message.
   * @param cause the cause. (A {@code null} value is permitted, and indicates that the cause is
   *        nonexistent or unknown.)
   * @param enableSuppression whether or not suppression is enabled or disabled
   * @param writableStackTrace whether or not the stack trace should be writable
   */
  public MediaStorageException(String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
  
}
