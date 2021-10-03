/*
 * Creation by madmath03 the 2017-12-20.
 */

package com.monogramm.starter.api.user.event;

import com.monogramm.starter.api.AbstractMailSendingEvent;
import com.monogramm.starter.api.user.listener.PasswordResetListener;
import com.monogramm.starter.persistence.user.entity.User;

import java.util.Locale;

/**
 * OnPasswordResetEvent.
 * 
 * @see PasswordResetListener
 * 
 * @see <a href="http://www.baeldung.com/spring-security-registration-i-forgot-my-password">Spring
 *      Security – Reset Your Password</a>
 * 
 * @author madmath03
 */
public class OnPasswordResetEvent extends AbstractMailSendingEvent<User> {
  /**
   * The {@code serialVersionUID}.
   */
  private static final long serialVersionUID = -7114614486625625481L;

  /**
   * Create a {@link OnPasswordResetEvent}.
   * 
   * @param user the user which requested a password reset.
   * @param locale the locale of the request. Usually used to define the locale of the email.
   * @param appUrl the application URL.
   */
  public OnPasswordResetEvent(User user, Locale locale, String appUrl) {
    super(user, locale, appUrl);
  }
}
