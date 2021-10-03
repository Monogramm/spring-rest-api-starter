/*
 * Creation by madmath03 the 2017-11-23.
 */

package com.monogramm.starter.api;

import com.monogramm.starter.persistence.EntityNotFoundException;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * REST exception handlers defined at a global level for the application.
 * 
 * @author madmath03
 */
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

  /**
   * ExceptionMessage.
   * 
   * @author madmath03
   */
  public class ExceptionMessage {

    private final Date timestamp = new Date();

    private final String message;

    private final String error;

    private final String description;

    /**
     * Create a {@link ExceptionMessage}.
     * 
     * @param throwable a throwable.
     */
    public ExceptionMessage(final Throwable throwable) {
      this(throwable, null);
    }

    /**
     * Create a {@link ExceptionMessage}.
     * 
     * @param throwable a throwable.
     * @param message a message to describe the context of the received throwable.
     */
    public ExceptionMessage(final Throwable throwable, final String message) {
      super();
      this.message = message;
      this.error = throwable.getMessage();
      if (throwable.getCause() == null) {
        this.description = null;
      } else {
        this.description = throwable.getCause().getMessage();
      }
    }

    /**
     * Get the {@link #timestamp}.
     * 
     * @return the {@link #timestamp}.
     */
    public final Date getTimestamp() {
      return timestamp;
    }

    /**
     * Get the {@link #message}.
     * 
     * @return the {@link #message}.
     */
    public final String getMessage() {
      return message;
    }

    /**
     * Get the {@link #error}.
     * 
     * @return the {@link #error}.
     */
    public final String getError() {
      return error;
    }

    /**
     * Get the {@link #description}.
     * 
     * @return the {@link #description}.
     */
    public final String getDescription() {
      return description;
    }

  }

  private static final Logger LOG =
      LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);

  /**
   * Catch all for any other exceptions.
   * 
   * @param exception any kind of exception that would not match other handlers.
   * 
   * @return a response describing the exception.
   */
  @ExceptionHandler({Exception.class})
  @ResponseBody
  public ResponseEntity<ExceptionMessage> handleAnyException(final Exception exception) {
    return errorResponse(exception, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  /**
   * Handle authorization failures commonly thrown from code.
   * 
   * @param failure a exception describing the failure.
   * 
   * @return a response describing the failure.
   */
  @ExceptionHandler({AccessDeniedException.class})
  @ResponseBody
  public ResponseEntity<ExceptionMessage> handleAuthFailures(final Exception failure) {
    return errorResponse(failure, HttpStatus.UNAUTHORIZED);
  }

  /**
   * Handle requests whose information do not match with entity in the persistence storage(s).
   * 
   * @param failure an exception describing the unknown data.
   * 
   * @return a response describing the exception.
   */
  @ExceptionHandler({EntityNotFoundException.class})
  @ResponseBody
  public ResponseEntity<ExceptionMessage> handleEntityNotFound(final Exception failure) {
    return errorResponse(failure, HttpStatus.NOT_FOUND);
  }

  /**
   * Handle failures commonly thrown from code.
   * 
   * @param failure a exception describing the failure.
   * 
   * @return a response describing the failure.
   */
  @ExceptionHandler({InvocationTargetException.class, IllegalArgumentException.class,
      ClassCastException.class, ConversionFailedException.class})
  @ResponseBody
  public ResponseEntity<ExceptionMessage> handleMiscFailures(final Exception failure) {
    return errorResponse(failure, HttpStatus.BAD_REQUEST);
  }

  /**
   * Send a 409 Conflict in case of concurrent modification.
   * 
   * @param conflictEx a conflict exception.
   * 
   * @return a response describing the conflict.
   */
  @ExceptionHandler({ObjectOptimisticLockingFailureException.class,
      OptimisticLockingFailureException.class, DataIntegrityViolationException.class})
  @ResponseBody
  public ResponseEntity<ExceptionMessage> handleConflict(final Exception conflictEx) {
    return errorResponse(conflictEx, HttpStatus.CONFLICT);
  }

  /**
   * Send a 501 Not Implemented in case of unsupported operation.
   * 
   * @param unsupportedEx a conflict exception.
   * 
   * @return a response describing the operation.
   */
  @ExceptionHandler({UnsupportedOperationException.class})
  @ResponseBody
  public ResponseEntity<ExceptionMessage> handleUnsupportedOperation(
      final Exception unsupportedEx) {
    return errorResponse(unsupportedEx, HttpStatus.NOT_IMPLEMENTED);
  }

  /**
   * Create an error response.
   * 
   * @param throwable the throwable that was catch if any.
   * @param status the code to return.
   * 
   * @return an error response.
   */
  protected ResponseEntity<ExceptionMessage> errorResponse(final Throwable throwable,
      final HttpStatus status) {
    final ExceptionMessage message;
    if (null == throwable) {
      LOG.error("Unknown error caught in RESTController {}", status);
      message = null;
    } else {
      LOG.error("Error caught: " + throwable.getMessage(), throwable);
      message = new ExceptionMessage(throwable);
    }

    return response(message, status);
  }

  /**
   * Create a response.
   * 
   * @param <T> The type of the response body.
   * @param body the body of the response.
   * @param status the response code.
   * 
   * @return a response.
   */
  protected <T> ResponseEntity<T> response(final T body, final HttpStatus status) {
    LOG.debug("Responding with a status of {}", status);
    return new ResponseEntity<>(body, new HttpHeaders(), status);
  }
}
