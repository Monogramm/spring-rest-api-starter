/*
 * Creation by madmath03 the 2019-01-29.
 */

package com.monogramm.starter.config.properties;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

/**
 * Data Properties.
 * 
 * @see ApplicationProperties
 * 
 * @author madmath03
 */
@Configuration
@ConfigurationProperties(prefix = "application.data")
@Validated
public class DataProperties {

  /**
   * {@code application.data.domain_name} property.
   */
  @NotBlank
  private String domainName = "monogramm.io";
  /**
   * {@code application.data.admin_password} property.
   */
  private String adminPassword;

  /**
   * {@code application.data.demo} property.
   */
  @NotNull
  private boolean demo = true;

  /**
   * Get the {@link #domainName}.
   * 
   * @return the {@link #domainName}.
   */
  public String getDomainName() {
    return domainName;
  }

  /**
   * Set the {@link #domainName}.
   * 
   * @param domainName the {@link #domainName} to set.
   */
  public void setDomainName(String domainName) {
    this.domainName = domainName;
  }

  /**
   * Get the {@link #adminPassword}.
   * 
   * @return the {@link #adminPassword}.
   */
  public String getAdminPassword() {
    return adminPassword;
  }

  /**
   * Set the {@link #adminPassword}.
   * 
   * @param adminPassword the {@link #adminPassword} to set.
   */
  public void setAdminPassword(String adminPassword) {
    this.adminPassword = adminPassword;
  }

  /**
   * Get the {@link #demo}.
   * 
   * @return the {@link #demo}.
   */
  public boolean isDemo() {
    return demo;
  }

  /**
   * Set the {@link #demo}.
   * 
   * @param demo the {@link #demo} to set.
   */
  public void setDemo(boolean demo) {
    this.demo = demo;
  }

}
