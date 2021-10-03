/*
 * Creation by madmath03 the 2017-11-09.
 */

package com.monogramm.starter.dto.user;

import com.monogramm.starter.utils.validation.PasswordConfirmationDto;
import com.monogramm.starter.utils.validation.ValidEmail;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * A registration Data Transfer Object.
 * 
 * <p>
 * This should be used as a base class for a registration request.
 * </p>
 * 
 * @author madmath03
 */
public class RegistrationDto extends PasswordConfirmationDto {
  @NotNull
  @NotEmpty
  private String username = null;

  @ValidEmail
  @NotNull
  @NotEmpty
  private String email = null;

  /**
   * Create a {@link RegistrationDto}.
   */
  public RegistrationDto() {
    super();
  }

  /**
   * Create a {@link RegistrationDto}.
   * 
   * @param username the registration username.
   * @param email the registration email.
   * @param password the password.
   * @param matchingPassword the matching password.
   * 
   * @throws NullPointerException if {@code password} or {@code matchingPassword} is {@code null}.
   */
  public RegistrationDto(final String username, final String email, final char[] password,
      final char[] matchingPassword) {
    super(password, matchingPassword);
    this.username = username;
    this.email = email;
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
  public final void setUsername(final String username) {
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
  public final void setEmail(final String email) {
    this.email = email;
  }

}
