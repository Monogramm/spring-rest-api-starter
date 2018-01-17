/*
 * Creation by madmath03 the 2017-12-20.
 */

package com.monogramm.starter.dto.user;

import com.monogramm.starter.utils.validation.PasswordConfirmationDto;
import com.monogramm.starter.utils.validation.ValidEmail;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * A password reset Data Transfer Object.
 * 
 * <p>
 * This should be used as a base class for a password reset request.
 * </p>
 * 
 * @author madmath03
 */
public class PasswordResetDto extends PasswordConfirmationDto {
  @ValidEmail
  @NotNull
  @NotEmpty
  private String email = null;

  @NotNull
  @NotEmpty
  private String token = null;

  /**
   * Create a {@link PasswordResetDto}.
   * 
   */
  public PasswordResetDto() {
    super();
  }

  /**
   * Create a {@link PasswordResetDto}.
   * 
   * @param email the user email.
   * @param token the password reset token.
   * @param password the password.
   * @param matchingPassword the matching password.
   */
  public PasswordResetDto(String email, String token, char[] password, char[] matchingPassword) {
    super(password, matchingPassword);

    this.email = email;
    this.token = token;
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
   * Set the {@link #email}.
   * 
   * @param email the {@link #email} to set.
   */
  public final void setEmail(String email) {
    this.email = email;
  }

  /**
   * Get the {@link #token}.
   * 
   * @return the {@link #token}.
   */
  public final String getToken() {
    return token;
  }

  /**
   * Set the {@link #token}.
   * 
   * @param token the {@link #token} to set.
   */
  public final void setToken(String token) {
    this.token = token;
  }

}
