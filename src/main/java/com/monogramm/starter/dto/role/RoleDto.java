/*
 * Creation by madmath03 the 2017-11-13.
 */

package com.monogramm.starter.dto.role;

import com.monogramm.starter.dto.AbstractGenericDto;
import com.monogramm.starter.persistence.role.entity.Role;

import java.util.Objects;
import java.util.UUID;

/**
 * RoleDto.
 * 
 * @see Role
 * 
 * @author madmath03
 */
public class RoleDto extends AbstractGenericDto {
  /**
   * The {@code serialVersionUID}.
   */
  private static final long serialVersionUID = -787812501741328068L;

  /**
   * The name of this record.
   */
  private String name;

  /**
   * Array of Id (relation) to the permissions attached to this role.
   */
  private UUID[] permissions = {};

  /**
   * Create a {@link RoleDto}.
   * 
   */
  public RoleDto() {
    super();
  }

  /**
   * Create a copy of a {@link RoleDto}.
   * 
   * @param other the other DTO to copy.
   * 
   * @throws NullPointerException if the other DTO is {@code null}.
   */
  public RoleDto(final RoleDto other) {
    super(other);

    this.name = other.getName();
    this.permissions = other.getPermissions();
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

  /**
   * Get the {@link #permissions}.
   * 
   * @return the {@link #permissions}.
   */
  public final UUID[] getPermissions() {
    return permissions;
  }

  /**
   * Set the {@link permissions}.
   * 
   * @param permissions the {@link #permissions} to set.
   */
  public final void setPermissions(final UUID[] permissions) {
    this.permissions = permissions;
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

    if (equals && obj instanceof RoleDto) {
      final RoleDto other = (RoleDto) obj;
      equals = Objects.equals(getName(), other.getName());
    }

    return equals;
  }

}
