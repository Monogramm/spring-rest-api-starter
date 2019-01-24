/*
 * Creation by madmath03 the 2017-11-14.
 */

package com.monogramm.starter.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.monogramm.starter.dto.AbstractGenericDto;
import com.monogramm.starter.persistence.user.entity.User;

import java.util.Objects;
import java.util.UUID;

/**
 * UserDto.
 * 
 * @see User
 * 
 * @author madmath03
 */
public class UserDto extends AbstractGenericDto {
  /**
   * The {@code serialVersionUID}.
   */
  private static final long serialVersionUID = 5802214564389531420L;

  /**
   * The user's account name.
   */
  private String username;

  /**
   * The user's account email.
   */
  private String email;

  /**
   * The user's account hashed password.
   */
  private char[] password;

  /**
   * Is the user's account active.
   */
  private boolean enabled = true;

  /**
   * Is the user's account verified.
   */
  private boolean verified = false;

  /**
   * Foreign key (relation) to the user\'s account role.
   */
  private UUID role;

  /**
   * The user\'s account role display name.
   */
  private String roleName;


  /**
   * Create a {@link UserDto}.
   * 
   */
  public UserDto() {
    super();
  }

  /**
   * Create a copy of a {@link UserDto}.
   * 
   * @param other the other DTO to copy.
   * 
   * @throws NullPointerException if the other DTO is {@code null}.
   */
  public UserDto(final UserDto other) {
    super(other);

    this.username = other.getUsername();
    this.email = other.getEmail();
    this.password = other.getPassword();
    this.enabled = other.isEnabled();
    this.verified = other.isVerified();
    this.role = other.getRole();
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
    this.password = password;
  }

  /**
   * Get the {@link #enabled}.
   * 
   * @return the {@link #enabled}.
   */
  public final boolean isEnabled() {
    return enabled;
  }

  /**
   * Set the {@link enabled}.
   * 
   * @param enabled the {@link #enabled} to set.
   */
  public final void setEnabled(final boolean enabled) {
    this.enabled = enabled;
  }

  /**
   * Get the {@link #verified}.
   * 
   * @return the {@link #verified}.
   */
  public final boolean isVerified() {
    return verified;
  }

  /**
   * Set the {@link verified}.
   * 
   * @param verified the {@link #verified} to set.
   */
  public final void setVerified(final boolean verified) {
    this.verified = verified;
  }

  /**
   * Get the {@link #role}.
   * 
   * @return the {@link #role}.
   */
  public final UUID getRole() {
    return role;
  }

  /**
   * Set the {@link role}.
   * 
   * @param role the {@link #role} to set.
   */
  public final void setRole(UUID role) {
    this.role = role;
  }

  /**
   * Get the {@link #roleName}.
   * 
   * @return the {@link #roleName}.
   */
  @JsonIgnore
  public String getRoleName() {
    return roleName;
  }

  /**
   * Set the {@link #roleName}.
   * 
   * @param roleName the {@link #roleName} to set.
   */
  public void setRoleName(String roleName) {
    this.roleName = roleName;
  }

  @Override
  public int hashCode() {
    int hash = super.hashCode();

    if (this.getUsername() != null) {
      hash = hash * 31 + this.getUsername().hashCode();
    }

    if (this.getEmail() != null) {
      hash = hash * 31 + this.getEmail().hashCode();
    }

    return hash;
  }

  @Override
  public boolean equals(final Object obj) {
    boolean equals = super.equals(obj);

    if (equals && obj instanceof UserDto) {
      final UserDto other = (UserDto) obj;
      equals = Objects.equals(getUsername(), other.getUsername());

      if (equals) {
        equals = Objects.equals(getEmail(), other.getEmail());
      }
    }

    return equals;
  }

}
