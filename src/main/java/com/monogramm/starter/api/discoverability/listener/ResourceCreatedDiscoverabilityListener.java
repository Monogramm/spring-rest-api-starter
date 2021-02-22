/*
 * Creation by madmath03 the 2019-01-13.
 */

package com.monogramm.starter.api.discoverability.listener;

import com.google.common.base.Preconditions;
import com.google.common.net.HttpHeaders;
import com.monogramm.starter.api.AbstractGenericController;
import com.monogramm.starter.api.discoverability.event.ResourceCreatedEvent;

import java.net.URI;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Listener that is triggered when a resource is fired.
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
@Component
public class ResourceCreatedDiscoverabilityListener
    implements ApplicationListener<ResourceCreatedEvent> {

  @Override
  public void onApplicationEvent(final ResourceCreatedEvent resourceCreatedEvent) {
    Preconditions.checkNotNull(resourceCreatedEvent);

    final HttpServletResponse response = resourceCreatedEvent.getResponse();
    final UUID idOfNewResource = resourceCreatedEvent.getIdOfNewResource();

    addLinkHeaderOnResourceCreation(response, idOfNewResource);
  }

  protected static void addLinkHeaderOnResourceCreation(final HttpServletResponse response,
      final UUID idOfNewResource) {
    final UriComponentsBuilder uriBuilder = ServletUriComponentsBuilder.fromCurrentRequestUri();

    addLinkHeaderOnResourceCreation(uriBuilder, response, idOfNewResource);
  }

  protected static void addLinkHeaderOnResourceCreation(final UriComponentsBuilder uriBuilder,
      final HttpServletResponse response, final UUID idOfNewResource) {
    final URI uri = uriBuilder.path("/{idOfNewResource}").buildAndExpand(idOfNewResource).toUri();

    response.setHeader(HttpHeaders.LOCATION, uri.toASCIIString());
  }

}
