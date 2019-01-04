/*
 * Creation by madmath03 the 2017-12-20.
 */

package com.monogramm.starter.api.user.listener;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.monogramm.starter.api.user.event.OnRegistrationCompleteEvent;
import com.monogramm.starter.persistence.user.entity.User;
import com.monogramm.starter.persistence.user.exception.VerificationTokenNotFoundException;
import com.monogramm.starter.persistence.user.service.IVerificationTokenService;

import java.util.Locale;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * {@link RegistrationListener} Unit Test.
 * 
 * @author madmath03
 */
public class RegistrationListenerTest {

  private IVerificationTokenService verificationService;

  private MessageSource messages;

  private JavaMailSender mailSender;

  private Environment env;

  private RegistrationListener listener;

  /**
   * @throws java.lang.Exception if the test setup crashes.
   */
  @Before
  public void setUp() throws Exception {
    verificationService = mock(IVerificationTokenService.class);
    assertNotNull(verificationService);

    messages = mock(MessageSource.class);
    assertNotNull(messages);

    mailSender = mock(JavaMailSender.class);
    assertNotNull(mailSender);

    env = mock(Environment.class);
    assertNotNull(env);

    this.listener = new RegistrationListener(verificationService, messages, mailSender, env);
  }

  /**
   * @throws java.lang.Exception if the test cleanup crashes.
   */
  @After
  public void tearDown() throws Exception {
    Mockito.reset(verificationService);
    Mockito.reset(messages);
    Mockito.reset(mailSender);
    Mockito.reset(env);

    this.listener = null;
  }

  /**
   * Test method for
   * {@link RegistrationListener#RegistrationListener(IVerificationTokenService, MessageSource, JavaMailSender, Environment)}.
   */
  @Test
  public void testRegistrationListener() {
    assertNotNull(new RegistrationListener(verificationService, messages, mailSender, env));
  }

  /**
   * Test method for
   * {@link RegistrationListener#RegistrationListener(IVerificationTokenService, MessageSource, JavaMailSender, Environment)}.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testRegistrationListenerNullTokenService() {
    new RegistrationListener(null, messages, mailSender, env);
  }

  /**
   * Test method for
   * {@link RegistrationListener#RegistrationListener(IVerificationTokenService, MessageSource, JavaMailSender, Environment)}.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testRegistrationListenerNullMessageSource() {
    new RegistrationListener(verificationService, null, mailSender, env);
  }

  /**
   * Test method for
   * {@link RegistrationListener#RegistrationListener(IVerificationTokenService, MessageSource, JavaMailSender, Environment)}.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testRegistrationListenerNullJavaMailSender() {
    new RegistrationListener(verificationService, messages, null, env);
  }

  /**
   * Test method for
   * {@link RegistrationListener#RegistrationListener(IVerificationTokenService, MessageSource, JavaMailSender, Environment)}.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testRegistrationListenerNullEnvironment() {
    new RegistrationListener(verificationService, messages, mailSender, null);
  }

  /**
   * Test method for {@link RegistrationListener#onApplicationEvent(OnRegistrationCompleteEvent)}.
   */
  @Test
  public void testOnApplicationEvent() {
    final User user = User.builder().build();
    final Locale locale = Locale.getDefault();
    final String appUrl = "http://dummy/";
    final OnRegistrationCompleteEvent event =
        new OnRegistrationCompleteEvent(user, Locale.getDefault(), appUrl);

    // Mock
    when(verificationService.add(anyObject())).thenReturn(true);

    final String subject = "dummy_subject";
    when(messages.getMessage("api.user.listener.confirm_email_subject", null, locale))
        .thenReturn(subject);
    final String message = "dummy_message";
    when(messages.getMessage(any(String.class), any(String[].class), any(Locale.class)))
        .thenReturn(message);

    when(env.getProperty("application.email.no_reply")).thenReturn("no_replay@dummy.com");

    // Operation
    this.listener.onApplicationEvent(event);

    // Check
    verify(verificationService, times(1)).add(anyObject());
    verifyNoMoreInteractions(verificationService);

    verify(messages, times(1)).getMessage("api.user.listener.confirm_email_subject", null, locale);
    verify(messages, times(2)).getMessage(any(String.class), any(String[].class),
        any(Locale.class));
    verifyNoMoreInteractions(verificationService);

    verify(env, times(1)).getProperty("spring.application.name");
    verify(env, times(1)).getProperty("application.email.no_reply");
    verifyNoMoreInteractions(env);

    verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    verifyNoMoreInteractions(mailSender);
  }

  /**
   * Test method for {@link RegistrationListener#onApplicationEvent(OnRegistrationCompleteEvent)}.
   */
  @Test(expected = VerificationTokenNotFoundException.class)
  public void testOnApplicationEventTokenNotAdded() {
    final User user = User.builder().build();
    final String appUrl = "http://dummy/";
    final OnRegistrationCompleteEvent event =
        new OnRegistrationCompleteEvent(user, Locale.getDefault(), appUrl);

    // Mock
    when(verificationService.add(anyObject())).thenReturn(false);

    // Operation
    this.listener.onApplicationEvent(event);
  }

}
