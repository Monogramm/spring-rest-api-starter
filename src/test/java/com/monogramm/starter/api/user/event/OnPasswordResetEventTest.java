/*
 * Creation by madmath03 the 2017-12-20.
 */

package com.monogramm.starter.api.user.event;

import com.monogramm.starter.api.AbstractMailSendingEvent;
import com.monogramm.starter.api.AbstractMailSendingEventTest;
import com.monogramm.starter.persistence.user.entity.User;

import java.util.Locale;

import org.junit.After;
import org.junit.Before;

/**
 * {@link OnPasswordResetEvent} Unit Test.
 * 
 * @author madmath03
 */
public class OnPasswordResetEventTest extends AbstractMailSendingEventTest<User> {

  private User user;
  private Locale locale;
  private String appUrl;

  @Override
  protected AbstractMailSendingEvent<User> buildTestEvent() {
    return new OnPasswordResetEvent(user, locale, appUrl);
  }

  @Override
  protected User getTestUser() {
    return user;
  }

  @Override
  protected Locale getTestLocale() {
    return locale;
  }

  @Override
  protected String getTestAppUrl() {
    return appUrl;
  }

  /**
   * @throws java.lang.Exception if the test setup crashes.
   */
  @Before
  public void setUp() throws Exception {
    this.user = new User();
    this.locale = Locale.getDefault();
    this.appUrl = "http://dummy/";

    super.setUp();
  }

  /**
   * @throws java.lang.Exception if the test cleanup crashes.
   */
  @After
  public void tearDown() throws Exception {
    this.user = null;
    this.locale = null;
    this.appUrl = null;

    super.tearDown();
  }

}
