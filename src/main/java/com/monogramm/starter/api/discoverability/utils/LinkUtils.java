/*
 * Creation by madmath03 the 2019-01-13.
 */

package com.monogramm.starter.api.discoverability.utils;

import com.monogramm.starter.api.discoverability.listener.PaginatedResultsRetrievedDiscoverabilityListener;

/**
 * Provides some constants and utility methods to build a Link Header to be stored in the
 * {@code HttpServletResponse} object.
 * 
 * @see <a href="https://www.baeldung.com/rest-api-discoverability-with-spring">HATEOAS for a Spring
 *      REST Service</a>
 * 
 * @author madmath03
 */
public final class LinkUtils {

  public static final String REL_SELF = "self";
  public static final String REL_NEXT = "next";
  public static final String REL_PREV = "prev";
  public static final String REL_FIRST = "first";
  public static final String REL_LAST = "last";

  /**
   * Discoverability relations.
   * 
   * @author madmath03
   */
  public enum Relation {
    /**
     * Relation to self entity.
     */
    SELF(REL_SELF),
    /**
     * Relation to next page.
     * 
     * @see PaginatedResultsRetrievedDiscoverabilityListener
     */
    NEXT(REL_NEXT),
    /**
     * Relation to previous page.
     * 
     * @see PaginatedResultsRetrievedDiscoverabilityListener
     */
    PREV(REL_PREV),
    /**
     * Relation to first page.
     * 
     * @see PaginatedResultsRetrievedDiscoverabilityListener
     */
    FIRST(REL_FIRST),
    /**
     * Relation to last page.
     * 
     * @see PaginatedResultsRetrievedDiscoverabilityListener
     */
    LAST(REL_LAST);

    private final String rel;

    /**
     * Create a {@link Relation}.
     * 
     * @param relation the relative relation path
     */
    private Relation(String relation) {
      this.rel = relation;
    }

    /**
     * Get the {@link #rel}.
     * 
     * @return the {@link #rel}.
     */
    protected final String getRel() {
      return rel;
    }

  }


  /**
   * Creates a Link Header to be stored in the {@code HttpServletResponse} to provide
   * Discoverability features to the user.
   * 
   * @param uri the base URI
   * @param rel the relative path
   * 
   * @return the complete URL
   */
  public static String createLinkHeader(final String uri, final Relation rel) {
    return LinkUtils.createLinkHeader(uri, rel.getRel());
  }

  /**
   * Creates a Link Header to be stored in the {@link HttpServletResponse} to provide
   * Discoverability features to the user.
   * 
   * @param uri the base URI
   * @param rel the relative path
   * 
   * @return the complete URL
   */
  private static String createLinkHeader(final String uri, final String rel) {
    return "<" + uri + ">; rel=\"" + rel + "\"";
  }


  /**
   * Create a {@link LinkUtils}.
   * 
   */
  private LinkUtils() {}

}
