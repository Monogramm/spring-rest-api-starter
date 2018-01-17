/*
 * Creation by madmath03 the 2017-11-25
 */

package com.monogramm.starter.dto.permission;

import com.monogramm.starter.dto.AbstractGenericDto;
import com.monogramm.starter.persistence.permission.entity.Permission;

import java.util.Objects;

/**
 * PermissionDto.
 * 
 * @see Permission
 * 
 * @author madmath03
 */
public class PermissionDto extends AbstractGenericDto {
  /**
   * The {@code serialVersionUID}.
   */
  private static final long serialVersionUID = -787812501741328068L;

  /**
   * The name of this record.
   */
  private String name;

  /**
   * Create a {@link PermissionDto}.
   * 
   */
  public PermissionDto() {
    super();
  }

  /**
   * Create a copy of a {@link PermissionDto}.
   * 
   * @param other the other DTO to copy.
   * 
   * @throws NullPointerException if the other DTO is {@code null}.
   */
  public PermissionDto(final PermissionDto other) {
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
  public final void setName(final String name) {
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

    if (equals && obj instanceof PermissionDto) {
      final PermissionDto other = (PermissionDto) obj;
      equals = Objects.equals(getName(), other.getName());
    }

    return equals;
  }

}
