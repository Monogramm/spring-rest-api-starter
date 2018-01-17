/*
 * Creation by madmath03 the 2017-12-03.
 */

package com.monogramm.starter.config.filter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * {@link SimpleCorsFilter} Unit Test.
 * 
 * @author madmath03
 */
public class SimpleCorsFilterTest {

  private SimpleCorsFilter filter;

  private HttpServletRequest request;
  private HttpServletResponse response;
  private FilterChain chain;

  /**
   * @throws java.lang.Exception If test class initialization crashes.
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {}

  /**
   * @throws java.lang.Exception If test class clean up crashes.
   */
  @AfterClass
  public static void tearDownAfterClass() throws Exception {}

  /**
   * @throws java.lang.Exception If test initialization crashes.
   */
  @Before
  public void setUp() throws Exception {
    this.filter = new SimpleCorsFilter();

    this.request = mock(HttpServletRequest.class);
    this.response = mock(HttpServletResponse.class);
    this.chain = mock(FilterChain.class);
  }

  /**
   * @throws java.lang.Exception If test clean up crashes.
   */
  @After
  public void tearDown() throws Exception {
    this.filter = null;

    Mockito.reset(request);
    Mockito.reset(response);
    Mockito.reset(chain);
  }

  /**
   * Test method for {@link SimpleCorsFilter#init(javax.servlet.FilterConfig)}.
   * 
   * @throws ServletException if the initialization crashed.
   */
  @Test
  public void testInit() throws ServletException {
    this.filter.init(null);
  }

  /**
   * Test method for
   * {@link SimpleCorsFilter#doFilter(javax.servlet.ServletRequest, ServletResponse, FilterChain)}.
   * 
   * @throws IOException if an I/O error occurs during this filter's processing of the request
   * @throws ServletException if the processing fails for any other reason
   */
  @Test
  public void testDoFilter() throws IOException, ServletException {
    when(request.getMethod()).thenReturn("OPTIONS");


    this.filter.doFilter(request, response, chain);


    verify(response, times(1)).setHeader("Access-Control-Allow-Origin", "*");
    verify(response, times(1)).setHeader("Access-Control-Allow-Methods",
        "OPTIONS, HEAD, GET, POST, PUT, DELETE");
    verify(response, times(1)).setHeader("Access-Control-Max-Age", "3600");
    verify(response, times(1)).setHeader("Access-Control-Allow-Headers",
        "Content-Type, x-requested-with, Authorization, "
            + "X-Monogramm-Filter, X-Monogramm-Sort, X-Monogramm-Start-At, X-Monogramm-End-At");

    verify(response, times(1)).setStatus(HttpServletResponse.SC_OK);

    verifyNoMoreInteractions(response);

    verify(request, times(1)).getMethod();

    verifyNoMoreInteractions(request);

    verifyNoMoreInteractions(chain);
  }

  /**
   * Test method for
   * {@link SimpleCorsFilter#doFilter(javax.servlet.ServletRequest, ServletResponse, FilterChain)}.
   * 
   * @throws IOException if an I/O error occurs during this filter's processing of the request
   * @throws ServletException if the processing fails for any other reason
   */
  @Test
  public void testDoFilterNotOptionRequest() throws IOException, ServletException {
    when(request.getMethod()).thenReturn("POST");


    this.filter.doFilter(request, response, chain);


    verify(response, times(1)).setHeader("Access-Control-Allow-Origin", "*");
    verify(response, times(1)).setHeader("Access-Control-Allow-Methods",
        "OPTIONS, HEAD, GET, POST, PUT, DELETE");
    verify(response, times(1)).setHeader("Access-Control-Max-Age", "3600");
    verify(response, times(1)).setHeader("Access-Control-Allow-Headers",
        "Content-Type, x-requested-with, Authorization, "
            + "X-Monogramm-Filter, X-Monogramm-Sort, X-Monogramm-Start-At, X-Monogramm-End-At");

    verify(request, times(1)).getMethod();

    verify(chain, times(1)).doFilter(request, response);

    verifyNoMoreInteractions(response);

    verifyNoMoreInteractions(request);

    verifyNoMoreInteractions(chain);
  }

  /**
   * Test method for
   * {@link SimpleCorsFilter#doFilter(javax.servlet.ServletRequest, ServletResponse, FilterChain)}.
   * 
   * @throws IOException if an I/O error occurs during this filter's processing of the request
   * @throws ServletException if the processing fails for any other reason
   */
  @Test
  public void testDoFilterNullRequest() throws IOException, ServletException {
    this.filter.doFilter(null, response, chain);


    verify(response, times(1)).setHeader("Access-Control-Allow-Origin", "*");
    verify(response, times(1)).setHeader("Access-Control-Allow-Methods",
        "OPTIONS, HEAD, GET, POST, PUT, DELETE");
    verify(response, times(1)).setHeader("Access-Control-Max-Age", "3600");
    verify(response, times(1)).setHeader("Access-Control-Allow-Headers",
        "Content-Type, x-requested-with, Authorization, "
            + "X-Monogramm-Filter, X-Monogramm-Sort, X-Monogramm-Start-At, X-Monogramm-End-At");

    verify(chain, times(1)).doFilter(null, response);

    verifyNoMoreInteractions(response);

    verifyNoMoreInteractions(chain);
  }

  /**
   * Test method for
   * {@link SimpleCorsFilter#doFilter(javax.servlet.ServletRequest, ServletResponse, FilterChain)}.
   * 
   * @throws IOException if an I/O error occurs during this filter's processing of the request
   * @throws ServletException if the processing fails for any other reason
   */
  @Test
  public void testDoFilterNullResponse() throws IOException, ServletException {
    when(request.getMethod()).thenReturn("OPTIONS");


    this.filter.doFilter(request, null, chain);


    verify(chain, times(1)).doFilter(request, null);

    verifyNoMoreInteractions(request);

    verifyNoMoreInteractions(chain);
  }

  /**
   * Test method for {@link SimpleCorsFilter#destroy()}.
   */
  @Test
  public void testDestroy() {
    this.filter.destroy();
  }

}
