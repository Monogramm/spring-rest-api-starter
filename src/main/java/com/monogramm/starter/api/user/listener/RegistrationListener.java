/*
 * Creation by madmath03 the 2017-12-18.
 */

package com.monogramm.starter.api.user.listener;

import com.monogramm.starter.api.AbstractMailSendingListener;
import com.monogramm.starter.api.user.event.OnRegistrationCompleteEvent;
import com.monogramm.starter.persistence.user.entity.User;
import com.monogramm.starter.persistence.user.entity.VerificationToken;
import com.monogramm.starter.persistence.user.exception.VerificationTokenNotFoundException;
import com.monogramm.starter.persistence.user.service.VerificationTokenService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * RegistrationListener.
 * 
 * @see OnRegistrationCompleteEvent
 * 
 * @see <a href="http://www.baeldung.com/registration-verify-user-by-email">Registration – Activate
 *      a New User by Email</a>
 * 
 * @author madmath03
 */
@Component
public class RegistrationListener
    extends AbstractMailSendingListener<VerificationToken, OnRegistrationCompleteEvent> {

  private final VerificationTokenService verificationService;

  /**
   * Create a {@link RegistrationListener}.
   * 
   * @param verificationService verification token service.
   * @param messages application messages.
   * @param mailSender mail sender.
   * @param env application environment.
   * 
   * @throws IllegalArgumentException if any of the parameters is {@code null}.
   */
  @Autowired
  public RegistrationListener(VerificationTokenService verificationService, MessageSource messages,
      JavaMailSender mailSender, Environment env) {
    super(messages, mailSender, env);

    if (verificationService == null) {
      throw new IllegalArgumentException("Verification token service cannot be null.");
    }
    this.verificationService = verificationService;
  }

  @Override
  protected VerificationToken generateToken(final OnRegistrationCompleteEvent event) {
    final User user = event.getSource();

    // Save request to verify account
    final VerificationToken token = new VerificationToken();
    token.setUser(user);
    
    if (!verificationService.add(token)) {
      throw new VerificationTokenNotFoundException();
    }

    return token;
  }

  @Override
  protected String getSubjectKey() {
    return "api.user.listener.confirm_email_subject";
  }

  @Override
  protected String getMessageKey() {
    return "api.user.listener.confirm_email_message";
  }

}
