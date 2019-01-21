package com.monogramm.starter.config.i8n;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * Application Message configuration.
 * 
 * @author madmath03
 * 
 * @see <a href=
 *      "https://github.com/pkainulainen/spring-data-jpa-examples/tree/master">spring-data-jpa-examples</a>
 */
@Configuration
public class ApplicationMessage {

  private static final String MESSAGE_SOURCE_BASE_NAME = "i18n/messages";

  /**
   * Message source.
   * 
   * @return message source.
   */
  @Bean
  public MessageSource messageSource() {
    final ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();

    messageSource.setBasename(MESSAGE_SOURCE_BASE_NAME);
    messageSource.setUseCodeAsDefaultMessage(true);
    messageSource.setAlwaysUseMessageFormat(true);

    return messageSource;
  }
}
