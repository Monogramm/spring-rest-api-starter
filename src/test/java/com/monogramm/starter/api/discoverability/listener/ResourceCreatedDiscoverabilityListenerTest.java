/*
 * Creation by madmath03 the 2019-01-28.
 */

package com.monogramm.starter.api.discoverability.listener;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.google.common.net.HttpHeaders;
import com.monogramm.starter.api.discoverability.event.ResourceCreatedEvent;
import com.monogramm.starter.persistence.AbstractGenericEntity;

import java.net.URI;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * {@link ResourceCreatedDiscoverabilityListener} Unit Test.
 * 
 * @author madmath03
 */
public class ResourceCreatedDiscoverabilityListenerTest {

  private UUID idOfNewResource;
  private UriComponentsBuilder uriBuilder;

  private HttpServletResponse response;

  private ResourceCreatedDiscoverabilityListener listener;

  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception {
    this.idOfNewResource = UUID.randomUUID();
    this.uriBuilder = UriComponentsBuilder.newInstance();
    this.response = mock(HttpServletResponse.class);

    this.listener = new ResourceCreatedDiscoverabilityListener();
  }

  /**
   * @throws java.lang.Exception
   */
  @After
  public void tearDown() throws Exception {
    this.idOfNewResource = null;
    this.uriBuilder = null;

    Mockito.reset(this.response);
    this.response = null;

    this.listener = null;
  }

  /**
   * Test method for
   * {@link ResourceCreatedDiscoverabilityListener#ResourceCreatedDiscoverabilityListener()}.
   */
  @Test
  public void testPaginatedResultsRetrievedDiscoverabilityListener() {
    assertNotNull(this.listener);
  }

  /**
   * Test method for
   * {@link ResourceCreatedDiscoverabilityListener#onApplicationEvent(ResourceCreatedEvent)}.
   */
  @Test(expected = IllegalStateException.class)
  public void testOnApplicationEvent() {
    // Mock
    final AbstractGenericEntity source = new AbstractGenericEntity() {
      private static final long serialVersionUID = 1L;
    };
    final ResourceCreatedEvent event = new ResourceCreatedEvent(source, this.response);

    // Operation
    this.listener.onApplicationEvent(event);
  }

  /**
   * Test method for
   * {@link ResourceCreatedDiscoverabilityListener#addLinkHeaderOnResourceCreation(UriComponentsBuilder, HttpServletResponse, UUID)}.
   */
  @Test
  public void testAddLinkHeaderOnSingleResourceRetrieval() {
    // Mock
    final URI uri = UriComponentsBuilder.newInstance().path("/{idOfNewResource}")
        .buildAndExpand(idOfNewResource).toUri();

    // Operation
    ResourceCreatedDiscoverabilityListener.addLinkHeaderOnResourceCreation(uriBuilder, response,
        idOfNewResource);

    // Check
    verify(this.response, times(1)).setHeader(HttpHeaders.LOCATION, uri.toASCIIString());
    verifyNoMoreInteractions(this.response);
  }

}
