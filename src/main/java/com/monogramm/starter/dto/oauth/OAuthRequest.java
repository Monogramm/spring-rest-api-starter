/*
 * Creation by madmath03 the 2017-11-09.
 */

package com.monogramm.starter.dto.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The OAuth request.
 * 
 * @author madmath03
 */
public class OAuthRequest {
  private String username;
  private String email;
  private char[] password;

  @JsonProperty("client_id")
  private String clientId;
  @JsonProperty("client_secret")
  private String clientSecret;

  @JsonProperty("grant_type")
  private String grantType;

  public OAuthRequest() {
    super();
  }

  /**
   * Create a {@link OAuthRequest}.
   * 
   * @param username user name.
   * @param email email address.
   * @param password user account password.
   * @param grantType grant type.
   * @param clientId OAuth client ID.
   * @param clientSecret OAuth client secret.
   */
  public OAuthRequest(String username, String email, char[] password, String grantType,
      String clientId, String clientSecret) {
    super();
    this.username = username;
    this.email = email;
    this.password = password == null ? null : password.clone();
    this.grantType = grantType;
    this.clientId = clientId;
    this.clientSecret = clientSecret;
  }

  /**
   * Get the {@link #username}.
   * 
   * @return the {@link #username}.
   */
  public final String getUsername() {
    return username;
  }

  /**
   * Set the {@link username}.
   * 
   * @param username the {@link #username} to set.
   */
  public final void setUsername(String username) {
    this.username = username;
  }

  /**
   * Get the {@link #email}.
   * 
   * @return the {@link #email}.
   */
  public final String getEmail() {
    return email;
  }

  /**
   * Set the {@link email}.
   * 
   * @param email the {@link #email} to set.
   */
  public final void setEmail(String email) {
    this.email = email;
  }

  /**
   * Get the {@link #password}.
   * 
   * @return the {@link #password}.
   */
  public final char[] getPassword() {
    return password;
  }

  /**
   * Set the {@link password}.
   * 
   * @param password the {@link #password} to set.
   */
  public final void setPassword(char[] password) {
    this.password = password == null ? null : password.clone();
  }

  /**
   * Get the {@link #grantType}.
   * 
   * @return the {@link #grantType}.
   */
  public final String getGrantType() {
    return grantType;
  }

  /**
   * Set the {@link grantType}.
   * 
   * @param grantType the {@link #grantType} to set.
   */
  public final void setGrantType(String grantType) {
    this.grantType = grantType;
  }

  /**
   * Get the {@link #clientId}.
   * 
   * @return the {@link #clientId}.
   */
  public String getClientId() {
    return clientId;
  }

  /**
   * Set the {@link #clientId}.
   * 
   * @param clientId the {@link #clientId} to set.
   */
  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  /**
   * Get the {@link #clientSecret}.
   * 
   * @return the {@link #clientSecret}.
   */
  public String getClientSecret() {
    return clientSecret;
  }

  /**
   * Set the {@link #clientSecret}.
   * 
   * @param clientSecret the {@link #clientSecret} to set.
   */
  public void setClientSecret(String clientSecret) {
    this.clientSecret = clientSecret;
  }

}
