/*
 * Creation by madmath03 the 2017-11-23.
 */

package com.monogramm.starter.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monogramm.starter.config.component.CustomServletRequestWrapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.connector.RequestFacade;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * JsonToUrlEncodedAuthenticationFilter.
 * 
 * @author madmath03
 */
@Component
@Order(value = Integer.MIN_VALUE)
public class JsonToUrlEncodedAuthenticationFilter implements Filter {

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
    ServletRequest finalRequest;

    // Convert OAuth token JSON requests
    if (request instanceof RequestFacade
        && "application/json".equalsIgnoreCase(request.getContentType())
        && "/oauth/token".equals(((RequestFacade) request).getServletPath())) {
      final RequestFacade requestFacade = (RequestFacade) request;

      final Map<String, String[]> parameters =
          this.readAndConvertContent(requestFacade.getInputStream());

      parameters.put("_method", new String[] {requestFacade.getMethod()});

      final HttpServletRequest convertedRequest =
          new CustomServletRequestWrapper(requestFacade, parameters);

      finalRequest = convertedRequest;
    } else {
      finalRequest = request;
    }

    chain.doFilter(finalRequest, response);
  }

  private byte[] readContent(final InputStream is) throws IOException {
    byte[] json;

    try (ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
      int read;
      final byte[] data = new byte[16384];

      while ((read = is.read(data, 0, data.length)) != -1) {
        buffer.write(data, 0, read);
      }
      buffer.flush();
      json = buffer.toByteArray();
    }

    return json;
  }

  private <T> T readContent(final InputStream is, final Class<T> resultClass) throws IOException {
    final byte[] json = this.readContent(is);

    final T result;
    if (json == null || json.length == 0) {
      result = null;
    } else {
      result = new ObjectMapper().readValue(json, resultClass);
    }

    return result;
  }

  private Map<String, String[]> convertMap(final Map<?, ?> map) {
    final Map<String, String[]> parameters = new HashMap<>();

    if (map != null) {
      for (final Map.Entry<?, ?> entry : map.entrySet()) {
        if (entry.getKey() == null) {
          continue;
        }

        final String key = entry.getKey().toString();
        final String[] value;
        if (entry.getValue() == null) {
          value = new String[] {null};
        } else {
          value = new String[] {entry.getValue().toString()};
        }

        parameters.put(key, value);
      }
    }

    return parameters;
  }

  private Map<String, String[]> readAndConvertContent(final InputStream is) throws IOException {
    return this.convertMap(this.readContent(is, HashMap.class));
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
