/*
 * Creation by madmath03 the 2017-11-05.
 */

package com.monogramm.starter.utils.validation;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * A Password Confirmation DTO.
 * 
 * @author madmath03
 */
@PasswordMatches
public class PasswordConfirmationDto {
  @NotNull
  @NotEmpty
  private char[] password = null;

  @NotNull
  @NotEmpty
  @JsonProperty("matching_password")
  private char[] matchingPassword = null;

  /**
   * Create a {@link PasswordConfirmationDto}.
   */
  public PasswordConfirmationDto() {
    super();
  }

  /**
   * Create a {@link PasswordConfirmationDto}.
   * 
   * @param password the password.
   * @param matchingPassword the matching password.
   * 
   * @throws NullPointerException if {@code password} or {@code matchingPassword} is {@code null}.
   */
  public PasswordConfirmationDto(final char[] password, final char[] matchingPassword) {
    super();
    this.password = password.clone();
    this.matchingPassword = matchingPassword.clone();
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
   * 
   * @throws NullPointerException if {@code password} or {@code matchingPassword} is {@code null}.
   */
  public final void setPassword(final char[] password) {
    this.password = password.clone();
  }

  /**
   * Get the {@link #matchingPassword}.
   * 
   * @return the {@link #matchingPassword}.
   */
  public final char[] getMatchingPassword() {
    return matchingPassword;
  }

  /**
   * Set the {@link matchingPassword}.
   * 
   * @param matchingPassword the {@link #matchingPassword} to set.
   * 
   * @throws NullPointerException if {@code password} or {@code matchingPassword} is {@code null}.
   */
  public final void setMatchingPassword(final char[] matchingPassword) {
    this.matchingPassword = matchingPassword.clone();
  }

}
