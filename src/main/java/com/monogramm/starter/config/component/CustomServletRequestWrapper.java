/*
 * Creation by madmath03 the 2017-11-23.
 */

package com.monogramm.starter.config.component;

import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.springframework.security.web.savedrequest.Enumerator;

/**
 * CustomServletRequestWrapper.
 * 
 * @author madmath03
 */
public class CustomServletRequestWrapper extends HttpServletRequestWrapper {

  private final Map<String, String[]> params;

  /**
   * Create a {@link CustomServletRequestWrapper}.
   * 
   * @param request The request to wrap.
   * @param params The request parameters.
   */
  public CustomServletRequestWrapper(final HttpServletRequest request,
      final Map<String, String[]> params) {
    super(request);

    if (params == null) {
      this.params = request.getParameterMap();
    } else {
      this.params = params;
    }
  }

  @Override
  public String getParameter(String name) {
    final String param;

    final String[] value = this.params.get(name);
    if (value != null && value.length > 0) {
      param = value[0];
    } else {
      param = "";
    }

    return param;
  }

  @Override
  public Map<String, String[]> getParameterMap() {
    return this.params;
  }

  @Override
  public Enumeration<String> getParameterNames() {
    return new Enumerator<>(params.keySet());
  }

  @Override
  public String[] getParameterValues(String name) {
    return params.get(name);
  }
}
