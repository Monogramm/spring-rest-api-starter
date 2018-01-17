/*
 * Creation by madmath03 the 2017-12-22.
 */

package com.monogramm.starter.api;

import java.util.Locale;

import org.springframework.context.ApplicationEvent;

/**
 * AbstractMailSendingEvent.
 * 
 * @author madmath03
 */
public class AbstractMailSendingEvent<T> extends ApplicationEvent {

  /**
   * The {@code serialVersionUID}.
   */
  private static final long serialVersionUID = 4695942224351346356L;

  private final Locale locale;
  private final String appUrl;

  /**
   * Create a {@link AbstractMailSendingEvent}.
   * 
   * @param source the source which generated the event.
   * @param locale the locale of the registration request. Usually used to define the locale of the
   *        registration verification.
   * @param appUrl the application URL.
   */
  protected AbstractMailSendingEvent(T source, Locale locale, String appUrl) {
    super(source);

    this.locale = locale;
    this.appUrl = appUrl;
  }

  @SuppressWarnings("unchecked")
  @Override
  public T getSource() {
    return (T) super.getSource();
  }

  /**
   * Get the {@link #locale}.
   * 
   * @return the {@link #locale}.
   */
  public Locale getLocale() {
    return locale;
  }

  /**
   * Get the {@link #appUrl}.
   * 
   * @return the {@link #appUrl}.
   */
  public String getAppUrl() {
    return appUrl;
  }
}
