/*
 * Creation by madmath03 the 2017-12-22.
 */

package com.monogramm.starter.api;

import com.monogramm.starter.persistence.AbstractToken;
import com.monogramm.starter.persistence.user.entity.User;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * AbstractMailSendingListener.
 * 
 * @author madmath03
 */
public abstract class AbstractMailSendingListener<T extends AbstractToken,
    E extends AbstractMailSendingEvent<User>> implements ApplicationListener<E> {

  /**
   * Logger for {@link AbstractMailSendingListener}.
   */
  private static final Logger LOG = LoggerFactory.getLogger(AbstractMailSendingListener.class);

  private final MessageSource messages;

  private final JavaMailSender mailSender;

  private final Environment env;

  /**
   * Create a {@link AbstractMailSendingListener}.
   * 
   * @param messages application messages.
   * @param mailSender mail sender.
   * @param env application environment properties.
   * 
   * @throws IllegalArgumentException if any of the parameters is {@code null}.
   */
  protected AbstractMailSendingListener(MessageSource messages, JavaMailSender mailSender,
      Environment env) {
    super();

    if (messages == null) {
      throw new IllegalArgumentException("Application i8n messages source cannot be null.");
    }
    this.messages = messages;

    if (mailSender == null) {
      throw new IllegalArgumentException("Application mail sender cannot be null.");
    }
    this.mailSender = mailSender;

    if (env == null) {
      throw new IllegalArgumentException("Application environment cannot be null.");
    }
    this.env = env;
  }

  protected abstract T generateToken(E event);

  protected abstract String getSubjectKey();

  protected abstract String getMessageKey();

  @Override
  public void onApplicationEvent(E event) {
    this.sendEmail(event);
  }

  private void sendEmail(E event) {
    final User user = event.getSource();

    // Save and retrieve token request
    final T token = this.generateToken(event);

    final Locale locale = event.getLocale();

    final String appName = env.getProperty("spring.application.name");
    final String subject = messages.getMessage(this.getSubjectKey(), null, locale);
    final String message =
        messages.getMessage(this.getMessageKey(), new String[] {token.getCode()}, locale);

    // Send an email to verify email address
    final SimpleMailMessage email = new SimpleMailMessage();
    email.setTo(user.getEmail());
    email.setSubject(appName + " - " + subject);
    email.setText(message);
    email.setFrom(env.getProperty("application.email.no_reply"));

    if (LOG.isDebugEnabled()) {
      LOG.debug("Sending email to {}: {}", user.getEmail(), email);
    }

    mailSender.send(email);
  }
}
