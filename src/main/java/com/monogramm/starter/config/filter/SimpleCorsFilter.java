/*
 * Creation by madmath03 the 2017-12-01.
 */

package com.monogramm.starter.config.filter;

import java.io.IOException;

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

/**
 * SimpleCorsFilter.
 * 
 * @author madmath03
 */
@Component
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class SimpleCorsFilter implements Filter {

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

      httpResponse.setHeader("Access-Control-Allow-Origin", "*");
      httpResponse.setHeader("Access-Control-Allow-Methods",
          "OPTIONS, HEAD, GET, POST, PUT, DELETE");
      httpResponse.setHeader("Access-Control-Max-Age", "3600");
      httpResponse.setHeader("Access-Control-Allow-Headers",
          "Content-Type, x-requested-with, Authorization, "
              + "X-Custom-Filter, X-Custom-Sort, X-Custom-Size, "
              + "X-Custom-Start-At, X-Custom-End-At");

      if (request instanceof HttpServletRequest) {
        final HttpServletRequest httpRequest = (HttpServletRequest) request;

        if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
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
