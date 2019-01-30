/*
 * Creation by madmath03 the 2019-01-30.
 */

package com.monogramm.starter.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

/**
 * Email Properties.
 * 
 * @see ApplicationProperties
 * 
 * @author madmath03
 */
@Configuration
@ConfigurationProperties(prefix = "application.email")
@Validated
public class EmailProperties {

  /**
   * {@code application.email.no_reply} property.
   */
  private String noReply = "no_reply@company.com";

  /**
   * {@code application.email.app_title} property.
   */
  private String appTitle = "Application";

  /**
   * Get the {@link #noReply}.
   * 
   * @return the {@link #noReply}.
   */
  public String getNoReply() {
    return noReply;
  }

  /**
   * Set the {@link #noReply}.
   * 
   * @param noReply the {@link #noReply} to set.
   */
  public void setNoReply(String noReply) {
    this.noReply = noReply;
  }

  /**
   * Get the {@link #appTitle}.
   * 
   * @return the {@link #appTitle}.
   */
  public String getAppTitle() {
    return appTitle;
  }

  /**
   * Set the {@link #appTitle}.
   * 
   * @param appTitle the {@link #appTitle} to set.
   */
  public void setAppTitle(String appTitle) {
    this.appTitle = appTitle;
  }

}
