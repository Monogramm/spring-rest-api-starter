/*
 * Creation by madmath03 the 2017-12-22.
 */

package com.monogramm.starter.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.monogramm.starter.persistence.user.entity.User;

import java.util.Locale;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * AbstractMailSendingEventTest.
 * 
 * @author madmath03
 */
public abstract class AbstractMailSendingEventTest<T> {

  private AbstractMailSendingEvent<T> event;

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

  protected abstract AbstractMailSendingEvent<T> buildTestEvent();

  /**
   * Get the {@link #testUser}.
   * 
   * @return the {@link #testUser}.
   */
  protected abstract User getTestUser();

  /**
   * Get the {@link #testLocale}.
   * 
   * @return the {@link #testLocale}.
   */
  protected abstract Locale getTestLocale();

  /**
   * Get the {@link #testAppUrl}.
   * 
   * @return the {@link #testAppUrl}.
   */
  protected abstract String getTestAppUrl();

  /**
   * Test method for
   * {@link AbstractMailSendingEvent#AbstractMailSendingEvent(Object, Locale, String)}.
   */
  @Test
  public void testAbstractMailSendingEvent() {
    assertNotNull(event);
  }

  /**
   * Test method for {@link AbstractMailSendingEvent#getSource()}.
   */
  @Test
  public void testGetSource() {
    assertNotNull(event.getSource());
    assertEquals(getTestUser(), event.getSource());
  }

  /**
   * Test method for {@link AbstractMailSendingEvent#getLocale()}.
   */
  @Test
  public void testGetLocale() {
    assertNotNull(event.getLocale());
    assertEquals(getTestLocale(), event.getLocale());
  }

  /**
   * Test method for {@link AbstractMailSendingEvent#getAppUrl()}.
   */
  @Test
  public void testGetAppUrl() {
    assertNotNull(event.getAppUrl());
    assertEquals(getTestAppUrl(), event.getAppUrl());
  }

}
