/*
 * Creation by madmath03 the 2017-12-18.
 */

package com.monogramm.starter.dto.user;

import com.monogramm.starter.dto.AbstractTokenDto;
import com.monogramm.starter.persistence.user.entity.VerificationToken;

import java.util.Objects;

/**
 * VerificationTokenDto.
 * 
 * @see VerificationToken
 * 
 * @author madmath03
 */
public class VerificationTokenDto extends AbstractTokenDto {
  /**
   * The {@code serialVersionUID}.
   */
  private static final long serialVersionUID = 3149753334430068545L;

  /**
   * The verification field.
   */
  private String field;
  /**
   * The verification field value.
   */
  private String value;

  /**
   * Create a {@link VerificationTokenDto}.
   * 
   */
  public VerificationTokenDto() {
    super();
  }

  /**
   * Create a copy of a {@link VerificationTokenDto}.
   * 
   * @param other the other {@link VerificationTokenDto} to copy.
   */
  public VerificationTokenDto(VerificationTokenDto other) {
    super(other);
  }

  /**
   * Get the {@link #field}.
   * 
   * @return the {@link #field}.
   */
  public String getField() {
    return field;
  }

  /**
   * Set the {@link #field}.
   * 
   * @param field the {@link #field} to set.
   */
  public void setField(String field) {
    this.field = field;
  }

  /**
   * Get the {@link #value}.
   * 
   * @return the {@link #value}.
   */
  public String getValue() {
    return value;
  }

  /**
   * Set the {@link #value}.
   * 
   * @param value the {@link #value} to set.
   */
  public void setValue(String value) {
    this.value = value;
  }

  @Override
  public int hashCode() {
    int hash = super.hashCode();

    if (this.getField() == null) {
      hash *= 31;
    } else {
      hash = hash * 31 + this.getField().hashCode();
    }

    if (this.getValue() == null) {
      hash *= 31;
    } else {
      hash = hash * 31 + this.getValue().hashCode();
    }

    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    boolean equals = super.equals(obj);

    if (equals && obj instanceof VerificationTokenDto) {
      final VerificationTokenDto other = (VerificationTokenDto) obj;

      equals = Objects.equals(getField(), other.getField())
          && Objects.equals(getValue(), other.getValue());
    }

    return equals;
  }

}
