/*
 * Creation by madmath03 the 2019-01-13.
 */

package com.monogramm.starter.api.discoverability.event;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationEvent;

/**
 * Abstract resource event.
 * 
 * @see <a href="https://www.baeldung.com/rest-api-discoverability-with-spring">HATEOAS for a Spring
 *      REST Service</a>
 * 
 * @param <T> Type of the result that is being handled (commonly Entities).
 * 
 * @author madmath03
 */
public class AbstractResourceEvent<T> extends ApplicationEvent {

  /**
   * The {@code serialVersionUID}.
   */
  private static final long serialVersionUID = 7259844597009552604L;

  private final transient HttpServletResponse response;

  /**
   * Create a {@link AbstractResourceEvent}.
   * 
   * @param source source object
   * @param response HTTP response
   */
  public AbstractResourceEvent(T source, HttpServletResponse response) {
    super(source);

    this.response = response;
  }

  /**
   * Get HTTP response linked to event.
   * 
   * @return HTTP response
   */
  public HttpServletResponse getResponse() {
    return response;
  }

  @Override
  public T getSource() {
    return (T) super.getSource();
  }

}
