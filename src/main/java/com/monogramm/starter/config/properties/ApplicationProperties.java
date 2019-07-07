/*
 * Creation by madmath03 the 2019-01-29.
 */

package com.monogramm.starter.config.properties;

import com.monogramm.starter.persistence.media.properties.FileStorageProperties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

/**
 * ApplicationProperties.
 * 
 * @see <a href= "https://www.baeldung.com/configuration-properties-in-spring-boot">Guide
 *      to @ConfigurationProperties in Spring Boot</a>
 * 
 * @author madmath03
 */
@Configuration
@ConfigurationProperties(prefix = "application")
@Validated
public class ApplicationProperties {

  /**
   * {@code application.name} property.
   */
  private String name;
  /**
   * {@code application.build_version} property.
   */
  private String buildVersion;
  /**
   * {@code application.build_timestamp} property.
   */
  private String buildTimestamp;


  private EmailProperties email;

  private FileStorageProperties file;

  private ApplicationSecurityProperties security;

  private DataProperties data;

  private WelcomeProperties welcome;


  /**
   * Get the {@link #name}.
   * 
   * @return the {@link #name}.
   */
  public String getName() {
    return name;
  }

  /**
   * Set the {@link #name}.
   * 
   * @param name the {@link #name} to set.
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Get the {@link #buildVersion}.
   * 
   * @return the {@link #buildVersion}.
   */
  public String getBuildVersion() {
    return buildVersion;
  }

  /**
   * Set the {@link #buildVersion}.
   * 
   * @param buildVersion the {@link #buildVersion} to set.
   */
  public void setBuildVersion(String buildVersion) {
    this.buildVersion = buildVersion;
  }

  /**
   * Get the {@link #buildTimestamp}.
   * 
   * @return the {@link #buildTimestamp}.
   */
  public String getBuildTimestamp() {
    return buildTimestamp;
  }

  /**
   * Set the {@link #buildTimestamp}.
   * 
   * @param buildTimestamp the {@link #buildTimestamp} to set.
   */
  public void setBuildTimestamp(String buildTimestamp) {
    this.buildTimestamp = buildTimestamp;
  }

  /**
   * Get the {@link #email}.
   * 
   * @return the {@link #email}.
   */
  public EmailProperties getEmail() {
    return email;
  }

  /**
   * Set the {@link #email}.
   * 
   * @param email the {@link #email} to set.
   */
  public void setEmail(EmailProperties email) {
    this.email = email;
  }

  /**
   * Get the {@link #file} related properties.
   * 
   * @return the {@link #file}.
   */
  public FileStorageProperties getFile() {
    return file;
  }

  /**
   * Set the {@link #file} related properties.
   * 
   * @param file the {@link #file} to set.
   */
  public void setFile(FileStorageProperties file) {
    this.file = file;
  }

  /**
   * Get the {@link #security}.
   * 
   * @return the {@link #security}.
   */
  public ApplicationSecurityProperties getSecurity() {
    return security;
  }

  /**
   * Set the {@link #security}.
   * 
   * @param security the {@link #security} to set.
   */
  public void setSecurity(ApplicationSecurityProperties security) {
    this.security = security;
  }

  /**
   * Get the {@link #data}.
   * 
   * @return the {@link #data}.
   */
  protected DataProperties getData() {
    return data;
  }

  /**
   * Set the {@link #data}.
   * 
   * @param data the {@link #data} to set.
   */
  protected void setData(DataProperties data) {
    this.data = data;
  }

  /**
   * Get the {@link #welcome}.
   * 
   * @return the {@link #welcome}.
   */
  public WelcomeProperties getWelcome() {
    return welcome;
  }

  /**
   * Set the {@link #welcome}.
   * 
   * @param welcome the {@link #welcome} to set.
   */
  public void setWelcome(WelcomeProperties welcome) {
    this.welcome = welcome;
  }

}
