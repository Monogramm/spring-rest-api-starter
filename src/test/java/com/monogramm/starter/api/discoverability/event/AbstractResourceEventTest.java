/*
 * Creation by madmath03 the 2019-01-13.
 */

package com.monogramm.starter.api.discoverability.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.servlet.http.HttpServletResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * {@link AbstractResourceEvent} Unit Test.
 * 
 * @author madmath03
 */
public abstract class AbstractResourceEventTest<O, T extends AbstractResourceEvent<O>> {

  private T event;

  /**
   * @throws java.lang.Exception if the test setup crashes.
   */
  @Before
  public void setUp() throws Exception {
    this.event = this.buildTestEvent();
  }

  /**
   * @throws java.lang.Exception if the test cleanup crashes.
   */
  @After
  public void tearDown() throws Exception {
    this.event = null;
  }

  /**
   * Get the {@link #event}.
   * 
   * @return the {@link #event}.
   */
  protected final T getEvent() {
    return event;
  }

  protected abstract T buildTestEvent();

  protected abstract O getTestSource();

  protected abstract HttpServletResponse getTestResponse();

  /**
   * Test method for {@link AbstractResourceEventTest#AbstractResourceEventTest()}.
   */
  @Test
  public void testAbstractMailSendingEvent() {
    assertNotNull(event);
  }

  /**
   * Test method for
   * {@link com.monogramm.starter.api.discoverability.event.AbstractResourceEvent#getSource()}.
   */
  @Test
  public void testGetSource() {
    assertNotNull(event.getSource());
    assertEquals(getTestSource(), event.getSource());
  }

  /**
   * Test method for
   * {@link com.monogramm.starter.api.discoverability.event.AbstractResourceEvent#getResponse()}.
   */
  @Test
  public void testGetResponse() {
    assertNotNull(event.getResponse());
    assertEquals(getTestResponse(), event.getResponse());
  }

}
