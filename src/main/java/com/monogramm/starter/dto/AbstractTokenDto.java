/*
 * Creation by madmath03 the 2017-12-20.
 */

package com.monogramm.starter.dto;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * AbstractTokenDto.
 * 
 * @author madmath03
 */
public abstract class AbstractTokenDto extends AbstractGenericDto {
  /**
   * The {@code serialVersionUID}.
   */
  private static final long serialVersionUID = -7094739871369028535L;

  /**
   * The verification code.
   */
  private String code;

  /**
   * The verification code expiry date.
   */
  private Date expiryDate;

  /**
   * Foreign key (relation) to the user\'s account.
   */
  private UUID user;

  /**
   * Create a {@link AbstractTokenDto}.
   * 
   */
  public AbstractTokenDto() {
    super();
  }

  /**
   * Create a copy of a {@link AbstractTokenDto}.
   * 
   * @param other the other DTO to copy.
   * 
   * @throws NullPointerException if the other DTO is {@code null}.
   */
  public AbstractTokenDto(final AbstractTokenDto other) {
    super(other);

    this.code = other.code;
    this.expiryDate = other.expiryDate;
    this.user = other.user;
  }

  /**
   * Get the {@link #code}.
   * 
   * @return the {@link #code}.
   */
  public final String getCode() {
    return code;
  }

  /**
   * Set the {@link #code}.
   * 
   * @param code the {@link #code} to set.
   */
  public final void setCode(String code) {
    this.code = code;
  }

  /**
   * Get the {@link #expiryDate}.
   * 
   * @return the {@link #expiryDate}.
   */
  public final Date getExpiryDate() {
    return expiryDate;
  }

  /**
   * Set the {@link #expiryDate}.
   * 
   * @param expiryDate the {@link #expiryDate} to set.
   */
  public final void setExpiryDate(Date expiryDate) {
    this.expiryDate = expiryDate;
  }

  /**
   * Get the {@link #user}.
   * 
   * @return the {@link #user}.
   */
  public final UUID getUser() {
    return user;
  }

  /**
   * Set the {@link #user}.
   * 
   * @param user the {@link #user} to set.
   */
  public final void setUser(UUID user) {
    this.user = user;
  }

  @Override
  public int hashCode() {
    int hash = super.hashCode();

    if (this.getCode() == null) {
      hash *= 31;
    } else {
      hash = hash * 31 + this.getCode().hashCode();
    }

    if (this.getExpiryDate() == null) {
      hash *= 31;
    } else {
      hash = hash * 31 + this.getExpiryDate().hashCode();
    }

    return hash;
  }

  @Override
  public boolean equals(final Object obj) {
    boolean equals = super.equals(obj);

    if (equals && obj instanceof AbstractTokenDto) {
      final AbstractTokenDto other = (AbstractTokenDto) obj;
      equals = Objects.equals(getCode(), other.getCode());

      if (equals) {
        equals = Objects.equals(getExpiryDate(), other.getExpiryDate());
      }
    }

    return equals;
  }

}
