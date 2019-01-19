/*
 * Creation by madmath03 the 2019-01-13.
 */

package com.monogramm.starter.api.discoverability.event;

import com.monogramm.starter.api.AbstractGenericController;
import com.monogramm.starter.persistence.AbstractGenericEntity;

import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

/**
 * Event that is fired when a resource is created.
 * 
 * <p>
 * This event object contains all the information needed to create the URL for the created result.
 * </p>
 * 
 * @see AbstractGenericController#addData(org.springframework.security.core.Authentication,
 *      com.monogramm.starter.dto.AbstractGenericDto,
 *      org.springframework.web.util.UriComponentsBuilder, HttpServletResponse)
 * 
 * @see <a href="https://www.baeldung.com/rest-api-discoverability-with-spring">HATEOAS for a Spring
 *      REST Service</a>
 * 
 * @author madmath03
 */
public class ResourceCreatedEvent extends AbstractResourceEvent<AbstractGenericEntity> {

  /**
   * The {@code serialVersionUID}.
   */
  private static final long serialVersionUID = -5171021604193349716L;

  private final UUID idOfNewResource;

  /**
   * Create a {@link ResourceCreatedEvent}.
   * 
   * @param source source object
   * @param response HTTP response
   * 
   * @throws NullPointerException if the source is {@code null}
   */
  public ResourceCreatedEvent(AbstractGenericEntity source, HttpServletResponse response) {
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
