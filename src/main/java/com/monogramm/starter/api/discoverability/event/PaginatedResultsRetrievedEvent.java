/*
 * Creation by madmath03 the 2019-01-13.
 */

package com.monogramm.starter.api.discoverability.event;

import com.monogramm.starter.api.AbstractGenericController;
import com.monogramm.starter.persistence.AbstractGenericEntity;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.util.UriComponentsBuilder;

/**
 * Event that is fired when a paginated search is performed.
 * 
 * <p>
 * This event object contains all the information needed to create the URL for the paginated
 * results.
 * </p>
 * 
 * @see AbstractGenericController#getAllDataPaginated(String, int, int,
 *      org.springframework.web.context.request.WebRequest, UriComponentsBuilder,
 *      HttpServletResponse)
 * 
 * @see <a href="https://www.baeldung.com/rest-api-pagination-in-spring">REST Pagination in
 *      Spring</a>
 * 
 * @author madmath03
 */
public class PaginatedResultsRetrievedEvent
    extends AbstractResourceEvent<Class<AbstractGenericEntity>> {

  /**
   * The {@code serialVersionUID}.
   */
  private static final long serialVersionUID = 7840535006448340556L;

  private final transient UriComponentsBuilder uriBuilder;
  private final int page;
  private final int totalPages;
  private final int pageSize;

  /**
   * Create a {@link ResourceCreatedEvent}.
   * 
   * @param response HTTP response.
   * @param builder an URI builder to build the URI to the created data in the response.
   * @param page zero-based page index.
   * @param nbPages total number of pages.
   * @param size the size of the page to be returned.
   */
  public PaginatedResultsRetrievedEvent(HttpServletResponse response, UriComponentsBuilder builder,
      int page, int nbPages, int size) {
    super(AbstractGenericEntity.class, response);

    this.uriBuilder = builder;
    this.page = page;
    this.totalPages = nbPages;
    this.pageSize = size;
  }

  /**
   * Get the {@link #uriBuilder}.
   * 
   * @return the {@link #uriBuilder}.
   */
  public final UriComponentsBuilder getUriBuilder() {
    return uriBuilder;
  }

  /**
   * Get the {@link #page}.
   * 
   * @return the {@link #page}.
   */
  public final int getPage() {
    return page;
  }

  /**
   * Get the {@link #totalPages}.
   * 
   * @return the {@link #totalPages}.
   */
  public final int getTotalPages() {
    return totalPages;
  }

  /**
   * Get the {@link #pageSize}.
   * 
   * @return the {@link #pageSize}.
   */
  public final int getPageSize() {
    return pageSize;
  }

}
