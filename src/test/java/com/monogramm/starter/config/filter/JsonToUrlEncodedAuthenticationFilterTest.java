/*
 * Creation by madmath03 the 2017-12-03.
 */

package com.monogramm.starter.config.filter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;

import org.apache.catalina.connector.RequestFacade;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.DelegatingServletInputStream;

/**
 * {@link JsonToUrlEncodedAuthenticationFilter} Unit Test.
 * 
 * @author madmath03
 */
public class JsonToUrlEncodedAuthenticationFilterTest {

  private static final String TEST_METHOD = "TEST";

  private static final String DUMMY_PARAM = "DUMMY";
  private static final String[] DUMMY_VALUE = new String[] {"42"};

  private static final String EMPTY_PARAM = "EMPTY";
  private static final String[] EMPTY_VALUE = new String[] {};

  private static final String NULL_PARAM = "NULL";

  private static final Map<String, String[]> DATA = new HashMap<>();
  private static final Map<String, String[]> DATA_EMPTY = new HashMap<>();

  private JsonToUrlEncodedAuthenticationFilter filter;

  private RequestFacade request;
  private ServletResponse response;
  private FilterChain chain;

  private DelegatingServletInputStream is;
  private DelegatingServletInputStream emptyIs;
  private DelegatingServletInputStream nullIs;

  /**
   * @throws java.lang.Exception If test class initialization crashes.
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    // Construct a Map then convert it to an InputStream
    DATA.put(DUMMY_PARAM, DUMMY_VALUE);
    DATA.put(EMPTY_PARAM, EMPTY_VALUE);
    DATA.put(NULL_PARAM, null);
  }

  /**
   * @throws java.lang.Exception If test class clean up crashes.
   */
  @AfterClass
  public static void tearDownAfterClass() throws Exception {
    DATA.clear();
  }

  /**
   * @throws java.lang.Exception If test initialization crashes.
   */
  @Before
  public void setUp() throws Exception {
    this.filter = new JsonToUrlEncodedAuthenticationFilter();

    this.request = mock(RequestFacade.class);
    this.response = mock(ServletResponse.class);
    this.chain = mock(FilterChain.class);

    this.is = this.createInputStream(DATA);

    this.emptyIs = this.createInputStream(DATA_EMPTY);

    this.nullIs = this.createInputStream(null);
  }

  private DelegatingServletInputStream createInputStream(final Map<String, String[]> data)
      throws IOException {
    // Convert Map to JSON then to byte array
    byte[] bytes = new ObjectMapper().writer().writeValueAsBytes(data);

    // Create a mock input stream from the Map bytes
    return new DelegatingServletInputStream(new ByteArrayInputStream(bytes));
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

    if (this.is != null) {
      this.is.close();
    }
    this.is = null;

    if (this.emptyIs != null) {
      this.emptyIs.close();
    }
    this.emptyIs = null;

    if (this.nullIs != null) {
      this.nullIs.close();
    }
    this.nullIs = null;
  }

  /**
   * Test method for {@link JsonToUrlEncodedAuthenticationFilter#init(javax.servlet.FilterConfig)}.
   * 
   * @throws ServletException if the initialization crashed.
   */
  @Test
  public void testInit() throws ServletException {
    this.filter.init(null);
  }

  /**
   * Test method for
   * {@link JsonToUrlEncodedAuthenticationFilter#doFilter(javax.servlet.ServletRequest, ServletResponse, FilterChain)}.
   * 
   * @throws IOException if an I/O error occurs during this filter's processing of the request
   * @throws ServletException if the processing fails for any other reason
   */
  @Test
  public void testDoFilter() throws IOException, ServletException {
    when(request.getContentType()).thenReturn("application/json");
    when(request.getServletPath()).thenReturn("/oauth/token");
    when(request.getInputStream()).thenReturn(this.is);
    when(request.getMethod()).thenReturn(TEST_METHOD);


    this.filter.doFilter(request, response, chain);


    verify(request, times(1)).getContentType();
    verify(request, times(1)).getServletPath();
    verify(request, times(1)).getInputStream();
    verify(request, times(1)).getMethod();

    verifyNoMoreInteractions(request);
  }

  /**
   * Test method for
   * {@link JsonToUrlEncodedAuthenticationFilter#doFilter(javax.servlet.ServletRequest, ServletResponse, FilterChain)}.
   * 
   * @throws IOException if an I/O error occurs during this filter's processing of the request
   * @throws ServletException if the processing fails for any other reason
   */
  @Test
  public void testDoFilterEmptyStream() throws IOException, ServletException {
    when(request.getContentType()).thenReturn("application/json");
    when(request.getServletPath()).thenReturn("/oauth/token");
    when(request.getInputStream()).thenReturn(this.emptyIs);
    when(request.getMethod()).thenReturn(TEST_METHOD);


    this.filter.doFilter(request, response, chain);


    verify(request, times(1)).getContentType();
    verify(request, times(1)).getServletPath();
    verify(request, times(1)).getInputStream();
    verify(request, times(1)).getMethod();

    verifyNoMoreInteractions(request);
  }

  /**
   * Test method for
   * {@link JsonToUrlEncodedAuthenticationFilter#doFilter(javax.servlet.ServletRequest, ServletResponse, FilterChain)}.
   * 
   * @throws IOException if an I/O error occurs during this filter's processing of the request
   * @throws ServletException if the processing fails for any other reason
   */
  @Test
  public void testDoFilterNullStream() throws IOException, ServletException {
    when(request.getContentType()).thenReturn("application/json");
    when(request.getServletPath()).thenReturn("/oauth/token");
    when(request.getInputStream()).thenReturn(this.nullIs);
    when(request.getMethod()).thenReturn(TEST_METHOD);


    this.filter.doFilter(request, response, chain);


    verify(request, times(1)).getContentType();
    verify(request, times(1)).getServletPath();
    verify(request, times(1)).getInputStream();
    verify(request, times(1)).getMethod();

    verifyNoMoreInteractions(request);
  }

  /**
   * Test method for
   * {@link JsonToUrlEncodedAuthenticationFilter#doFilter(javax.servlet.ServletRequest, ServletResponse, FilterChain)}.
   * 
   * @throws IOException if an I/O error occurs during this filter's processing of the request
   * @throws ServletException if the processing fails for any other reason
   */
  @Test
  public void testDoFilterNotOauthRequest() throws IOException, ServletException {
    when(request.getContentType()).thenReturn("application/json");
    when(request.getServletPath()).thenReturn("/not_oauth/token");


    this.filter.doFilter(request, response, chain);


    verify(request, times(1)).getContentType();
    verify(request, times(1)).getServletPath();

    verifyNoMoreInteractions(request);

    verify(chain, times(1)).doFilter(request, response);

    verifyNoMoreInteractions(request);
  }

  /**
   * Test method for
   * {@link JsonToUrlEncodedAuthenticationFilter#doFilter(javax.servlet.ServletRequest, ServletResponse, FilterChain)}.
   * 
   * @throws IOException if an I/O error occurs during this filter's processing of the request
   * @throws ServletException if the processing fails for any other reason
   */
  @Test
  public void testDoFilterNotJsonRequest() throws IOException, ServletException {
    when(request.getContentType()).thenReturn("application/not_json");


    this.filter.doFilter(request, response, chain);

    verify(request, times(1)).getContentType();

    verifyNoMoreInteractions(request);

    verify(chain, times(1)).doFilter(request, response);

    verifyNoMoreInteractions(request);
  }

  /**
   * Test method for
   * {@link JsonToUrlEncodedAuthenticationFilter#doFilter(javax.servlet.ServletRequest, ServletResponse, FilterChain)}.
   * 
   * @throws IOException if an I/O error occurs during this filter's processing of the request
   * @throws ServletException if the processing fails for any other reason
   */
  @Test
  public void testDoFilterNullRequest() throws IOException, ServletException {
    this.filter.doFilter(null, response, chain);


    verify(chain, times(1)).doFilter(null, response);

    verifyNoMoreInteractions(request);
  }

  /**
   * Test method for {@link JsonToUrlEncodedAuthenticationFilter#destroy()}.
   */
  @Test
  public void testDestroy() {
    this.filter.destroy();
  }

}
