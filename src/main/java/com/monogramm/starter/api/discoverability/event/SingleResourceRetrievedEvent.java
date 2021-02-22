/*
 * Creation by madmath03 the 2019-01-13.
 */

package com.monogramm.starter.api.discoverability.event;

import com.monogramm.starter.api.AbstractGenericController;
import com.monogramm.starter.persistence.AbstractGenericEntity;

import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

/**
 * Event that is fired when a single result is retrieved.
 * 
 * <p>
 * This event object contains all the information needed to create the URL for the created result.
 * </p>
 * 
 * @see AbstractGenericController#getDataById(String,
 *      org.springframework.web.context.request.WebRequest, HttpServletResponse)
 * 
 * @see <a href="https://www.baeldung.com/rest-api-discoverability-with-spring">HATEOAS for a Spring
 *      REST Service</a>
 * 
 * @author madmath03
 */
public class SingleResourceRetrievedEvent extends AbstractResourceEvent<AbstractGenericEntity> {

  /**
   * The {@code serialVersionUID}.
   */
  private static final long serialVersionUID = 6664275562706082268L;

  private final UUID idOfNewResource;

  /**
   * Create a {@link SingleResourceRetrievedEvent}.
   * 
   * @param source source object
   * @param response HTTP response
   * 
   * @throws NullPointerException if the source is {@code null}
   */
  public SingleResourceRetrievedEvent(AbstractGenericEntity source, HttpServletResponse response) {
    super(source, response);

    this.idOfNewResource = source.getId();
  }

  /**
   * Get the {@link #idOfNewResource}.
   * 
   * @return the {@link #idOfNewResource}.
   */
  public final UUID getIdOfNewResource() {
    return idOfNewResource;
  }

}
