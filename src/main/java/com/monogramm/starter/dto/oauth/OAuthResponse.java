/*
 * Creation by madmath03 the 2017-11-09.
 */

package com.monogramm.starter.dto.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.UUID;

/**
 * The OAuth response.
 * 
 * @author madmath03
 */
public class OAuthResponse {
  @JsonProperty("access_token")
  private String accessToken;

  @JsonProperty("timestamp")
  private Date timestamp;

  @JsonProperty("principal_id")
  private UUID principalId;

  @JsonProperty("principal_name")
  private String principalName;

  @JsonProperty("principal_email")
  private String principalEmail;

  @JsonProperty("authorities")
  private String[] authorities = {};

  @JsonProperty("roles")
  private String[] roles = {};

  public OAuthResponse() {
    super();
  }

  /**
   * Create a {@link OAuthResponse}.
   * 
   * @param accessToken access token.
   * @param timestamp response time stamp.
   * @param principalId principal id.
   * @param principalName principal name.
   * @param principalEmail principal email.
   * @param authorities the user authorities.
   * @param roles the user roles.
   */
  public OAuthResponse(String accessToken, Date timestamp, UUID principalId, String principalName,
      String principalEmail, String[] authorities, String[] roles) {
    super();
    this.accessToken = accessToken;
    this.timestamp = timestamp;
    this.principalId = principalId;
    this.principalName = principalName;
    this.principalEmail = principalEmail;
    this.authorities = authorities;
    this.roles = roles;
  }

  /**
   * Get the {@link #accessToken}.
   * 
   * @return the {@link #accessToken}.
   */
  public final String getAccessToken() {
    return accessToken;
  }

  /**
   * Set the {@link #accessToken}.
   * 
   * @param accessToken the {@link #accessToken} to set.
   */
  public final void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  /**
   * Get the {@link #timestamp}.
   * 
   * @return the {@link #timestamp}.
   */
  public final Date getTimestamp() {
    return timestamp;
  }

  /**
   * Set the {@link #timestamp}.
   * 
   * @param timestamp the {@link #timestamp} to set.
   */
  public final void setTimestamp(Date timestamp) {
    this.timestamp = timestamp;
  }

  /**
   * Get the {@link #principalId}.
   * 
   * @return the {@link #principalId}.
   */
  public final UUID getPrincipalId() {
    return principalId;
  }

  /**
   * Set the {@link #principalId}.
   * 
   * @param principalId the {@link #principalId} to set.
   */
  public final void setPrincipalId(UUID principalId) {
    this.principalId = principalId;
  }

  /**
   * Get the {@link #principalName}.
   * 
   * @return the {@link #principalName}.
   */
  public final String getPrincipalName() {
    return principalName;
  }

  /**
   * Set the {@link #principalName}.
   * 
   * @param principalName the {@link #principalName} to set.
   */
  public final void setPrincipalName(String principalName) {
    this.principalName = principalName;
  }

  /**
   * Get the {@link #principalEmail}.
   * 
   * @return the {@link #principalEmail}.
   */
  public final String getPrincipalEmail() {
    return principalEmail;
  }

  /**
   * Set the {@link #principalEmail}.
   * 
   * @param principalEmail the {@link #principalEmail} to set.
   */
  public final void setPrincipalEmail(String principalEmail) {
    this.principalEmail = principalEmail;
  }

  /**
   * Get the {@link #authorities}.
   * 
   * @return the {@link #authorities}.
   */
  public final String[] getAuthorities() {
    return authorities;
  }

  /**
   * Set the {@link #authorities}.
   * 
   * @param authorities the {@link #authorities} to set.
   */
  public final void setAuthorities(String[] authorities) {
    this.authorities = authorities;
  }

  /**
   * Get the {@link #roles}.
   * 
   * @return the {@link #roles}.
   */
  public final String[] getRoles() {
    return roles;
  }

  /**
   * Set the {@link #roles}.
   * 
   * @param roles the {@link #roles} to set.
   */
  public final void setRoles(String[] roles) {
    this.roles = roles;
  }
}
