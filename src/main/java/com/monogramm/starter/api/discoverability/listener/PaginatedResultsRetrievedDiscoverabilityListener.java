/*
 * Creation by madmath03 the 2019-01-13.
 */

package com.monogramm.starter.api.discoverability.listener;

import com.google.common.base.Preconditions;
import com.google.common.net.HttpHeaders;
import com.monogramm.starter.api.AbstractGenericController;
import com.monogramm.starter.api.discoverability.event.PaginatedResultsRetrievedEvent;
import com.monogramm.starter.api.discoverability.utils.LinkUtils;
import com.monogramm.starter.api.discoverability.utils.LinkUtils.Relation;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Listener that is triggered when a paginated search is fired.
 * 
 * @see <a href="https://www.baeldung.com/rest-api-pagination-in-spring">REST Pagination in
 *      Spring</a>
 * 
 * @author madmath03
 */
@Component
public class PaginatedResultsRetrievedDiscoverabilityListener
    implements ApplicationListener<PaginatedResultsRetrievedEvent> {

  /**
   * Page number query parameter name.
   */
  public static final String PAGE = AbstractGenericController.PAGE;
  /**
   * Page size query parameter name.
   */
  public static final String SIZE = AbstractGenericController.SIZE;


  private final Environment env;

  /**
   * Create a {@link PaginatedResultsRetrievedDiscoverabilityListener}.
   * 
   * @param env application environment properties.
   */
  @Autowired
  public PaginatedResultsRetrievedDiscoverabilityListener(Environment env) {
    super();

    if (env == null) {
      throw new IllegalArgumentException("Application environment cannot be null.");
    }
    this.env = env;
  }


  @Override
  public final void onApplicationEvent(final PaginatedResultsRetrievedEvent ev) {
    Preconditions.checkNotNull(ev);

    final String serverPort = this.env.getProperty("server.port");
    final String contextPath = this.env.getProperty("server.context-path");
    final UriComponentsBuilder uriBuilder = ev.getUriBuilder();
    uriBuilder.port(serverPort);
    uriBuilder.path(contextPath);

    addLinkHeaderOnPagedResourceRetrieval(uriBuilder, ev.getResponse(), ev.getPage(),
        ev.getTotalPages(), ev.getPageSize());
  }


  /**
   * Add link to response headers.
   * 
   * @param uriBuilder an URI builder to build the URI to the createddata in the response.
   * @param response HTTP response.
   * @param page zero-based page index.
   * @param totalPages total number of pages.
   * @param pageSize the size of the page to be returned.
   */
  protected static void addLinkHeaderOnPagedResourceRetrieval(final UriComponentsBuilder uriBuilder,
      final HttpServletResponse response, final int page, final int totalPages,
      final int pageSize) {

    final String controllerPath =
        ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri().getPath();

    addLinkHeaderOnPagedResourceRetrieval(controllerPath, uriBuilder, response, page, totalPages,
        pageSize);
  }

  /**
   * Add link to response headers for specified base request URL.
   * 
   * @param urlPath request URL.
   * @param uriBuilder an URI builder to build the URI to the created data in the response.
   * @param response HTTP response.
   * @param page zero-based page index.
   * @param totalPages total number of pages.
   * @param pageSize the size of the page to be returned.
   */
  protected static void addLinkHeaderOnPagedResourceRetrieval(final String urlPath,
      final UriComponentsBuilder uriBuilder, final HttpServletResponse response, final int page,
      final int totalPages, final int pageSize) {
    uriBuilder.path(urlPath);

    final StringBuilder linkHeader = constructLinkHeaderOnPagedResourceRetrieval(urlPath,
        uriBuilder, response, page, totalPages, pageSize);

    if (linkHeader.length() > 0) {
      response.addHeader(HttpHeaders.LINK, linkHeader.toString());
    }
  }



  private static StringBuilder constructLinkHeaderOnPagedResourceRetrieval(final String requestUrl,
      final UriComponentsBuilder uriBuilder, final HttpServletResponse response, final int page,
      final int totalPages, final int pageSize) {
    final StringBuilder linkHeader = new StringBuilder();

    if (hasNextPage(page, totalPages)) {
      final String uriForNextPage = constructNextPageUri(uriBuilder, page, pageSize);
      linkHeader.append(LinkUtils.createLinkHeader(uriForNextPage, Relation.NEXT));
    }

    if (hasPreviousPage(page)) {
      final String uriForPrevPage = constructPrevPageUri(uriBuilder, page, pageSize);
      appendCommaIfNecessary(linkHeader);
      linkHeader.append(LinkUtils.createLinkHeader(uriForPrevPage, Relation.PREV));
    }

    if (hasFirstPage(page)) {
      final String uriForFirstPage = constructFirstPageUri(uriBuilder, pageSize);
      appendCommaIfNecessary(linkHeader);
      linkHeader.append(LinkUtils.createLinkHeader(uriForFirstPage, Relation.FIRST));
    }

    if (hasLastPage(page, totalPages)) {
      final String uriForLastPage = constructLastPageUri(uriBuilder, totalPages, pageSize);
      appendCommaIfNecessary(linkHeader);
      linkHeader.append(LinkUtils.createLinkHeader(uriForLastPage, Relation.LAST));
    }

    return linkHeader;
  }


  private static String constructNextPageUri(final UriComponentsBuilder uriBuilder, final int page,
      final int size) {
    return uriBuilder.replaceQueryParam(PAGE, page + 1).replaceQueryParam(SIZE, size).build()
        .encode().toUriString();
  }

  private static String constructPrevPageUri(final UriComponentsBuilder uriBuilder, final int page,
      final int size) {
    return uriBuilder.replaceQueryParam(PAGE, page - 1).replaceQueryParam(SIZE, size).build()
        .encode().toUriString();
  }

  private static String constructFirstPageUri(final UriComponentsBuilder uriBuilder,
      final int size) {
    return uriBuilder.replaceQueryParam(PAGE, 0).replaceQueryParam(SIZE, size).build().encode()
        .toUriString();
  }

  private static String constructLastPageUri(final UriComponentsBuilder uriBuilder,
      final int totalPages, final int size) {
    return uriBuilder.replaceQueryParam(PAGE, totalPages).replaceQueryParam(SIZE, size).build()
        .encode().toUriString();
  }

  private static boolean hasNextPage(final int page, final int totalPages) {
    return page < (totalPages - 1);
  }

  private static boolean hasPreviousPage(final int page) {
    return page > 0;
  }

  private static boolean hasFirstPage(final int page) {
    return hasPreviousPage(page);
  }

  private static boolean hasLastPage(final int page, final int totalPages) {
    return (totalPages > 1) && hasNextPage(page, totalPages);
  }

  private static void appendCommaIfNecessary(final StringBuilder linkHeader) {
    if (linkHeader.length() > 0) {
      linkHeader.append(", ");
    }
  }

}
