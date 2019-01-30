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
   * {@code application.security.public-key-path} property.
   */
  private String publicKeyPath;

  /**
   * {@code application.security.private-key-path} property.
   */
  private String privateKeyPath;
  /**
   * {@code application.security.private-key-password} property.
   */
  private String privateKeyPassword;
  /**
   * {@code application.security.private-key-pair} property.
   */
  private String privateKeyPair;

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
   * Get the {@link #publicKeyPath}.
   * 
   * @return the {@link #publicKeyPath}.
   */
  public String getPublicKeyPath() {
    return publicKeyPath;
  }

  /**
   * Set the {@link #publicKeyPath}.
   * 
   * @param publicKeyPath the {@link #publicKeyPath} to set.
   */
  public void setPublicKeyPath(String publicKeyPath) {
    this.publicKeyPath = publicKeyPath;
  }

  /**
   * Get the {@link #privateKeyPath}.
   * 
   * @return the {@link #privateKeyPath}.
   */
  public String getPrivateKeyPath() {
    return privateKeyPath;
  }

  /**
   * Set the {@link #privateKeyPath}.
   * 
   * @param privateKeyPath the {@link #privateKeyPath} to set.
   */
  public void setPrivateKeyPath(String privateKeyPath) {
    this.privateKeyPath = privateKeyPath;
  }

  /**
   * Get the {@link #privateKeyPassword}.
   * 
   * @return the {@link #privateKeyPassword}.
   */
  public String getPrivateKeyPassword() {
    return privateKeyPassword;
  }

  /**
   * Set the {@link #privateKeyPassword}.
   * 
   * @param privateKeyPassword the {@link #privateKeyPassword} to set.
   */
  public void setPrivateKeyPassword(String privateKeyPassword) {
    this.privateKeyPassword = privateKeyPassword;
  }

  /**
   * Get the {@link #privateKeyPair}.
   * 
   * @return the {@link #privateKeyPair}.
   */
  public String getPrivateKeyPair() {
    return privateKeyPair;
  }

  /**
   * Set the {@link #privateKeyPair}.
   * 
   * @param privateKeyPair the {@link #privateKeyPair} to set.
   */
  public void setPrivateKeyPair(String privateKeyPair) {
    this.privateKeyPair = privateKeyPair;
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
