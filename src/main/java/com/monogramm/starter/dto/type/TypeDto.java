/*
 * Creation by madmath03 the 2017-11-13.
 */

package com.monogramm.starter.dto.type;

import com.monogramm.starter.dto.AbstractGenericDto;
import com.monogramm.starter.persistence.type.entity.Type;

import java.util.Objects;

/**
 * TypeDto.
 * 
 * @see Type
 * 
 * @author madmath03
 */
public class TypeDto extends AbstractGenericDto {
  /**
   * The {@code serialVersionUID}.
   */
  private static final long serialVersionUID = -4925882485299333240L;

  /**
   * The name of this record.
   */
  private String name = null;

  /**
   * Create a {@link TypeDto}.
   * 
   */
  public TypeDto() {
    super();
  }

  /**
   * Create a copy of a {@link TypeDto}.
   * 
   * @param other the other DTO to copy.
   * 
   * @throws NullPointerException if the other DTO is {@code null}.
   */
  public TypeDto(final TypeDto other) {
    super(other);

    this.name = other.getName();
  }

  /**
   * Get the {@link #name}.
   * 
   * @return the {@link #name}.
   */
  public final String getName() {
    return name;
  }

  /**
   * Set the {@link name}.
   * 
   * @param name the {@link #name} to set.
   */
  public final void setName(String name) {
    this.name = name;
  }

  @Override
  public int hashCode() {
    int hash = super.hashCode();

    if (this.getName() == null) {
      hash *= 31;
    } else {
      hash = hash * 31 + this.getName().hashCode();
    }

    return hash;
  }

  @Override
  public boolean equals(final Object obj) {
    boolean equals = super.equals(obj);

    if (equals && obj instanceof TypeDto) {
      final TypeDto other = (TypeDto) obj;
      equals = Objects.equals(getName(), other.getName());
    }

    return equals;
  }

}
