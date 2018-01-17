/*
 * Creation by madmath03 the 2017-12-18.
 */

package com.monogramm.starter.api.user.listener;

import com.monogramm.starter.api.AbstractMailSendingListener;
import com.monogramm.starter.api.user.event.OnPasswordResetEvent;
import com.monogramm.starter.persistence.user.entity.PasswordResetToken;
import com.monogramm.starter.persistence.user.entity.User;
import com.monogramm.starter.persistence.user.exception.PasswordResetTokenNotFoundException;
import com.monogramm.starter.persistence.user.service.IPasswordResetTokenService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * PasswordResetListener.
 * 
 * @see OnPasswordResetEvent
 * 
 * @see <a href="http://www.baeldung.com/spring-security-registration-i-forgot-my-password">Spring
 *      Security – Reset Your Password</a>
 * 
 * @author madmath03
 */
@Component
public class PasswordResetListener
    extends AbstractMailSendingListener<PasswordResetToken, OnPasswordResetEvent> {

  private final IPasswordResetTokenService passwordResetTokenService;

  /**
   * Create a {@link PasswordResetListener}.
   * 
   * @param passwordResetTokenService password reset token service.
   * @param messages application messages.
   * @param mailSender mail sender.
   * @param env application environment.
   * 
   * @throws IllegalArgumentException if any of the parameters is {@code null}.
   */
  @Autowired
  public PasswordResetListener(IPasswordResetTokenService passwordResetTokenService,
      MessageSource messages, JavaMailSender mailSender, Environment env) {
    super(messages, mailSender, env);

    if (passwordResetTokenService == null) {
      throw new IllegalArgumentException("Password reset token service cannot be null.");
    }
    this.passwordResetTokenService = passwordResetTokenService;
  }

  @Override
  protected PasswordResetToken generateToken(final OnPasswordResetEvent event) {
    final User user = event.getSource();

    // Save request to verify account
    final PasswordResetToken token = new PasswordResetToken();
    token.setUser(user);

    if (!passwordResetTokenService.add(token)) {
      throw new PasswordResetTokenNotFoundException();
    }

    return token;
  }

  @Override
  protected String getSubjectKey() {
    return "api.user.listener.reset_password_subject";
  }

  @Override
  protected String getMessageKey() {
    return "api.user.listener.reset_password_message";
  }

}
