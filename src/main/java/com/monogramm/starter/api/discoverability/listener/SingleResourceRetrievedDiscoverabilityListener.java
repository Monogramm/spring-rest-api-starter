/*
 * Creation by madmath03 the 2019-01-13.
 */

package com.monogramm.starter.api.discoverability.listener;

import com.google.common.base.Preconditions;
import com.google.common.net.HttpHeaders;
import com.monogramm.starter.api.AbstractGenericController;
import com.monogramm.starter.api.discoverability.event.SingleResourceRetrievedEvent;
import com.monogramm.starter.api.discoverability.utils.LinkUtils;
import com.monogramm.starter.api.discoverability.utils.LinkUtils.Relation;

import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * Listener that is triggered when a single result event is fired.
 * 
 * @see AbstractGenericController#getDataById(String,
 *      org.springframework.web.context.request.WebRequest, HttpServletResponse)
 * 
 * @see <a href="https://www.baeldung.com/rest-api-discoverability-with-spring">HATEOAS for a Spring
 *      REST Service</a>
 * 
 * @author madmath03
 */
@Component
public class SingleResourceRetrievedDiscoverabilityListener
    implements ApplicationListener<SingleResourceRetrievedEvent> {

  @Override
  public void onApplicationEvent(final SingleResourceRetrievedEvent resourceRetrievedEvent) {
    Preconditions.checkNotNull(resourceRetrievedEvent);

    final HttpServletResponse response = resourceRetrievedEvent.getResponse();
    final UUID idOfNewResource = resourceRetrievedEvent.getIdOfNewResource();

    addLinkHeaderOnSingleResourceRetrieval(response, idOfNewResource);
  }

  protected static void addLinkHeaderOnSingleResourceRetrieval(final HttpServletResponse response,
      final UUID idOfNewResource) {
    final String requestUrl =
        ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri().toASCIIString();

    addLinkHeaderOnSingleResourceRetrieval(requestUrl, response, idOfNewResource);
  }

  protected static void addLinkHeaderOnSingleResourceRetrieval(final String requestUrl,
      final HttpServletResponse response, final UUID idOfNewResource) {
    final int positionOfLastSlash = requestUrl.lastIndexOf('/');
    final String uriForResourceSelf =
        requestUrl.substring(0, positionOfLastSlash) + idOfNewResource;

    final String linkHeaderValue = LinkUtils.createLinkHeader(uriForResourceSelf, Relation.SELF);
    response.addHeader(HttpHeaders.LINK, linkHeaderValue);
  }

}
