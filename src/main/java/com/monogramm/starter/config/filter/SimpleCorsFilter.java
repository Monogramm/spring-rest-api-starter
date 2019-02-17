/*
 * Creation by madmath03 the 2017-12-01.
 */

package com.monogramm.starter.config.filter;

import com.google.common.net.HttpHeaders;

import java.io.IOException;
import java.util.function.Function;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Simple CORS Filter.
 * 
 * @author madmath03
 */
@Component
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class SimpleCorsFilter implements Filter {

  /**
   * Concatenate given values to a String of comma separated string representation.
   * 
   * @param values value to concatenate.
   * 
   * @return a list of comma separated string representation
   */
  protected static String listValues(Object... values) {
    return listValues(Object::toString, values);
  }

  /**
   * Concatenate given values to a String of comma separated string representation.
   * 
   * @param <T> type of values to list
   * 
   * @param toString function to generate a string representation for each of the values.
   * @param values value to concatenate.
   * 
   * @return a list of comma separated string representation
   */
  protected static <T> String listValues(Function<T, String> toString, T[] values) {
    final StringBuilder builder = new StringBuilder();

    if (values.length > 0) {
      for (int i = 0; i < values.length - 1; i++) {
        builder.append(toString.apply(values[i])).append(", ");
      }
      builder.append(toString.apply(values[values.length - 1]));
    }

    return builder.toString();
  }

  /**
   * Simple enumeration to keep track of allowed headers of the API.
   * 
   * @author madmath03
   */
  public enum AccessControlAllowHeaders {
    /** HTTP Authorization header. **/
    AUTHORIZATION(HttpHeaders.AUTHORIZATION),
    /** Content type. **/
    CONTENT_TYPE(HttpHeaders.CONTENT_TYPE),
    /** Encoding type. **/
    ENC_TYPE("enctype"),
    /** Response type. **/
    RESPONSE_TYPE("responseType"),
    /** Requested with. **/
    REQUESTED_WITH("x-requested-with"),
    /** Custom header for filtering results. **/
    CUSTOM_FILTER("X-Custom-Filter"),
    /** Custom header for paging results. **/
    CUSTOM_PAGE("X-Custom-Page"),
    /** Custom header for page size of paginated results. **/
    CUSTOM_SIZE("X-Custom-Size"),
    /** Custom header for sorting results. **/
    CUSTOM_SORT("X-Custom-Sort");

    public final String value;

    /**
     * Create a {@link AccessControlAllowHeaders}.
     * 
     * @param value header name
     */
    private AccessControlAllowHeaders(String value) {
      this.value = value;
    }

    private static String allHeaders = null;

    /**
     * Get all allowed headers allowed separated by commas.
     * 
     * @return all allowed headers allowed separated by commas.
     */
    protected static String getAllHeaders() {
      if (allHeaders == null) {
        final AccessControlAllowHeaders[] headers = AccessControlAllowHeaders.values();
        allHeaders = SimpleCorsFilter.listValues(header -> header.value, headers);
      }
      return allHeaders;
    }
  }

  public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "*";

  /**
   * Allowed HTTP methods of the API.
   * 
   * <p>
   * Basically all but {@code PATCH} and {@code TRACE}.
   * </p>
   */
  protected static final RequestMethod[] ACCESS_CONTROL_ALLOW_METHODS =
      {RequestMethod.OPTIONS, RequestMethod.HEAD, RequestMethod.GET, RequestMethod.POST,
          RequestMethod.PUT, RequestMethod.DELETE};
  /**
   * Allowed HTTP methods of the API.
   * 
   * @see #ACCESS_CONTROL_ALLOW_METHODS
   */
  public static final String ACCESS_CONTROL_ALLOW_METHODS_LIST;

  static {
    final StringBuilder builder = new StringBuilder();

    for (int i = 0; i < ACCESS_CONTROL_ALLOW_METHODS.length - 1; i++) {
      RequestMethod method = ACCESS_CONTROL_ALLOW_METHODS[i];
      builder.append(method).append(", ");
    }
    builder.append(ACCESS_CONTROL_ALLOW_METHODS[ACCESS_CONTROL_ALLOW_METHODS.length - 1]);

    ACCESS_CONTROL_ALLOW_METHODS_LIST = builder.toString();
  }

  /**
   * Allowed HTTP methods of the API.
   */
  public static final String ACCESS_CONTROL_MAX_AGE = "3600";

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
   */
  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    // Nothing to do
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse,
   * javax.servlet.FilterChain)
   */
  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    ServletResponse finalResponse;

    if (response instanceof HttpServletResponse) {
      final HttpServletResponse httpResponse = (HttpServletResponse) response;

      httpResponse.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN);
      httpResponse.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS,
          ACCESS_CONTROL_ALLOW_METHODS_LIST);
      httpResponse.setHeader(HttpHeaders.ACCESS_CONTROL_MAX_AGE, ACCESS_CONTROL_MAX_AGE);
      httpResponse.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS,
          AccessControlAllowHeaders.getAllHeaders());

      httpResponse.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS,
          listValues(HttpHeaders.LINK, HttpHeaders.LOCATION));

      if (request instanceof HttpServletRequest) {
        final HttpServletRequest httpRequest = (HttpServletRequest) request;

        if (RequestMethod.OPTIONS.toString().equalsIgnoreCase(httpRequest.getMethod())) {
          // Always return OK for OPTIONS method
          httpResponse.setStatus(HttpServletResponse.SC_OK);
          return;
        }
      }

      finalResponse = httpResponse;
    } else {
      finalResponse = response;
    }

    chain.doFilter(request, finalResponse);
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.Filter#destroy()
   */
  @Override
  public void destroy() {
    // Nothing to do
  }

}
