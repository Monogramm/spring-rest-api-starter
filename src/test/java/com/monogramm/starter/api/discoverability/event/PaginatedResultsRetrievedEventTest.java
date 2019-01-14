/*
 * Creation by madmath03 the 2019-01-13.
 */

package com.monogramm.starter.api.discoverability.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import com.monogramm.starter.persistence.AbstractGenericEntity;

import javax.servlet.http.HttpServletResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * {@link PaginatedResultsRetrievedEvent} Unit Test.
 * 
 * @author madmath03
 */
public class PaginatedResultsRetrievedEventTest extends
    AbstractResourceEventTest<Class<AbstractGenericEntity>, PaginatedResultsRetrievedEvent> {

  private UriComponentsBuilder uriBuilder = new UriComponentsBuilder() {};

  private int page = 1;

  private int totalPages = 42;

  private int size = 20;

  private HttpServletResponse response;

  /**
   * @throws java.lang.Exception if the test setup crashes.
   */
  @Before
  public void setUp() throws Exception {
    this.response = mock(HttpServletResponse.class);

    super.setUp();
  }

  /**
   * @throws java.lang.Exception if the test cleanup crashes.
   */
  @After
  public void tearDown() throws Exception {
    super.tearDown();
  }

  @Override
  protected PaginatedResultsRetrievedEvent buildTestEvent() {
    return new PaginatedResultsRetrievedEvent(response, uriBuilder, page, totalPages, size);
  }

  @Override
  protected Class<AbstractGenericEntity> getTestSource() {
    return AbstractGenericEntity.class;
  }

  @Override
  protected HttpServletResponse getTestResponse() {
    return response;
  }

  /**
   * Test method for {@link PaginatedResultsRetrievedEvent#getUriBuilder()}.
   */
  @Test
  public void testGetUriBuilder() {
    assertNotNull(getEvent().getUriBuilder());
    assertEquals(uriBuilder, getEvent().getUriBuilder());
  }

  /**
   * Test method for {@link PaginatedResultsRetrievedEvent#getPage()}.
   */
  @Test
  public void testGetPage() {
    assertNotNull(getEvent().getPage());
    assertEquals(page, getEvent().getPage());
  }

  /**
   * Test method for {@link PaginatedResultsRetrievedEvent#getTotalPages()}.
   */
  @Test
  public void testGetTotalPages() {
    assertNotNull(getEvent().getTotalPages());
    assertEquals(totalPages, getEvent().getTotalPages());
  }

  /**
   * Test method for {@link PaginatedResultsRetrievedEvent#getPageSize()}.
   */
  @Test
  public void testGetPageSize() {
    assertNotNull(getEvent().getPageSize());
    assertEquals(size, getEvent().getPageSize());
  }

}
