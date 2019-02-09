/*
 * Creation by madmath03 the 2019-01-29.
 */

package com.monogramm.starter.config.properties;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

/**
 * Security Properties.
 * 
 * @see ApplicationProperties
 * 
 * @author madmath03
 */
@Configuration
@ConfigurationProperties(prefix = "application.security")
@Validated
public class ApplicationSecurityProperties {

  /**
   * {@code application.security.signing-key} property.
   */
  private boolean debug = false;

  /**
   * {@code application.security.key-pair-path} property.
   */
  private String keyPairPath;
  /**
   * {@code application.security.key-pair-password} property.
   */
  private String keyPairPassword;
  /**
   * {@code application.security.key-pair-store-alias} property.
   */
  private String keyPairAlias;

  /**
   * {@code application.security.verifying-key-path} property.
   */
  private String verifyingKeyPath;
  /**
   * {@code application.security.signing-key-path} property.
   */
  private String signingKeyPath;

  /**
   * {@code application.security.signing-key} property.
   */
  @NotBlank
  private String signingKey = "123";


  /**
   * Get the {@link #debug}.
   * 
   * @return the {@link #debug}.
   */
  public boolean isDebug() {
    return debug;
  }

  /**
   * Set the {@link #debug}.
   * 
   * @param debug the {@link #debug} to set.
   */
  public void setDebug(boolean debug) {
    this.debug = debug;
  }

  /**
   * Get the {@link #keyPairPath}.
   * 
   * @return the {@link #keyPairPath}.
   */
  public String getKeyPairPath() {
    return keyPairPath;
  }

  /**
   * Set the {@link #keyPairPath}.
   * 
   * @param keyPairPath the {@link #keyPairPath} to set.
   */
  public void setKeyPairPath(String keyPairPath) {
    this.keyPairPath = keyPairPath;
  }

  /**
   * Get the {@link #keyPairPassword}.
   * 
   * @return the {@link #keyPairPassword}.
   */
  public String getKeyPairPassword() {
    return keyPairPassword;
  }

  /**
   * Set the {@link #keyPairPassword}.
   * 
   * @param keyPairPassword the {@link #keyPairPassword} to set.
   */
  public void setKeyPairPassword(String keyPairPassword) {
    this.keyPairPassword = keyPairPassword;
  }

  /**
   * Get the {@link #keyPairAlias}.
   * 
   * @return the {@link #keyPairAlias}.
   */
  public String getKeyPairAlias() {
    return keyPairAlias;
  }

  /**
   * Set the {@link #keyPairAlias}.
   * 
   * @param keyPairAlias the {@link #keyPairAlias} to set.
   */
  public void setKeyPairAlias(String keyPairAlias) {
    this.keyPairAlias = keyPairAlias;
  }

  /**
   * Get the {@link #verifyingKeyPath}.
   * 
   * @return the {@link #verifyingKeyPath}.
   */
  public String getVerifyingKeyPath() {
    return verifyingKeyPath;
  }

  /**
   * Set the {@link #verifyingKeyPath}.
   * 
   * @param verifyingKeyPath the {@link #verifyingKeyPath} to set.
   */
  public void setVerifyingKeyPath(String verifyingKeyPath) {
    this.verifyingKeyPath = verifyingKeyPath;
  }

  /**
   * Get the {@link #signingKeyPath}.
   * 
   * @return the {@link #signingKeyPath}.
   */
  public String getSigningKeyPath() {
    return signingKeyPath;
  }

  /**
   * Set the {@link #signingKeyPath}.
   * 
   * @param signingKeyPath the {@link #signingKeyPath} to set.
   */
  public void setSigningKeyPath(String signingKeyPath) {
    this.signingKeyPath = signingKeyPath;
  }

  /**
   * Get the {@link #signingKey}.
   * 
   * @return the {@link #signingKey}.
   */
  public String getSigningKey() {
    return signingKey;
  }

  /**
   * Set the {@link #signingKey}.
   * 
   * @param signingKey the {@link #signingKey} to set.
   */
  public void setSigningKey(String signingKey) {
    this.signingKey = signingKey;
  }

}
