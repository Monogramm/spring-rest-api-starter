/*
 * Creation by madmath03 the 2017-12-18.
 */

package com.monogramm.starter.api.user.event;

import com.monogramm.starter.api.AbstractMailSendingEvent;
import com.monogramm.starter.api.user.listener.RegistrationListener;
import com.monogramm.starter.persistence.user.entity.User;

import java.util.Locale;

/**
 * OnRegistrationCompleteEvent.
 * 
 * @see RegistrationListener
 * 
 * @see <a href="http://www.baeldung.com/registration-verify-user-by-email">Registration – Activate
 *      a New User by Email</a>
 * 
 * @author madmath03
 */
public class OnRegistrationCompleteEvent extends AbstractMailSendingEvent<User> {
  /**
   * The {@code serialVersionUID}.
   */
  private static final long serialVersionUID = -7114614486625625481L;

  /**
   * Create a {@link OnRegistrationCompleteEvent}.
   * 
   * @param user the user registered.
   * @param locale the locale of the registration request. Usually used to define the locale of the
   *        registration verification.
   * @param appUrl the application URL.
   */
  public OnRegistrationCompleteEvent(User user, Locale locale, String appUrl) {
    super(user, locale, appUrl);
  }
}
