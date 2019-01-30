/*
 * Creation by madmath03 the 2019-01-30.
 */

package com.monogramm.starter.config.properties;

import java.nio.file.Path;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

/**
 * Welcome Properties.
 * 
 * @see ApplicationProperties
 * 
 * @author madmath03
 */
@Configuration
@ConfigurationProperties(prefix = "application.welcome")
@Validated
public class WelcomeProperties {

  /**
   * {@code application.welcome.app_banner} property.
   */
  private Path appBanner;
  /**
   * {@code application.welcome.ascii_art} property.
   */
  private String asciiArt;

  /**
   * Get the {@link #appBanner}.
   * 
   * @return the {@link #appBanner}.
   */
  public Path getAppBanner() {
    return appBanner;
  }

  /**
   * Set the {@link #appBanner}.
   * 
   * @param appBanner the {@link #appBanner} to set.
   */
  public void setAppBanner(Path appBanner) {
    this.appBanner = appBanner;
  }

  /**
   * Get the {@link #asciiArt}.
   * 
   * @return the {@link #asciiArt}.
   */
  public String getAsciiArt() {
    return asciiArt;
  }

  /**
   * Set the {@link #asciiArt}.
   * 
   * @param asciiArt the {@link #asciiArt} to set.
   */
  public void setAsciiArt(String asciiArt) {
    this.asciiArt = asciiArt;
  }

}
