/*
 * Creation by madmath03 the 2017-11-25
 */

package com.monogramm.starter.persistence.permission.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.monogramm.starter.persistence.AbstractGenericEntity;
import com.monogramm.starter.persistence.role.entity.Role;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

@Entity
@Table(name = "permission")
public class Permission extends AbstractGenericEntity {
  /**
   * The {@code serialVersionUID}.
   */
  private static final long serialVersionUID = 973216963525006343L;

  /**
   * Display name maximum length.
   */
  public static final int MAX_LENGTH_DISPLAYNAME = 30;

  /**
   * Get a new {@link PermissionBuilder}.
   *
   * @return a new {@link PermissionBuilder}.
   */
  public static PermissionBuilder builder() {
    return new PermissionBuilder();
  }

  /**
   * Get a new {@link PermissionBuilder}.
   * 
   * @param name the name of your record being built.
   *
   * @return a new {@link PermissionBuilder}.
   */
  public static PermissionBuilder builder(final String name) {
    return new PermissionBuilder(name);
  }

  /**
   * The name of this record.
   */
  @Column(name = "name", unique = true, nullable = false, length = MAX_LENGTH_DISPLAYNAME)
  private String name = null;

  /**
   * Users having the current permission through retrieved through their foreign key relation.
   */
  @JsonIgnore
  @ManyToMany(mappedBy = "permissions", fetch = FetchType.EAGER)
  private final Set<Role> roles = new HashSet<>();

  /**
   * Create a {@link Permission}.
   * 
   */
  public Permission() {
    super();
  }

  /**
   * Create a {@link Permission}.
   * 
   * @param name name of this record.
   */
  public Permission(final String name) {
    super();
    this.name = name;
  }

  /**
   * Create a copy of a {@link Permission}.
   * 
   * @param other the other entity to copy.
   * 
   * @throws NullPointerException if the {@code other} entity is @{code null}.
   */
  public Permission(final Permission other) {
    super(other);

    this.name = other.getName();
  }

  /**
   * Get the {@link #name}.
   * 
   * @return the {@link #name}.
   */
  public String getName() {
    return name;
  }

  /**
   * Set the {@link name}.
   * 
   * @param name the {@link #name} to set.
   */
  public void setName(final String name) {
    this.name = name;
  }

  /**
   * Get the {@link #roles}.
   * 
   * @return the {@link #roles}.
   */
  public Set<Role> getRoles() {
    return this.roles;
  }

  /**
   * Add a role to this permission.
   * 
   * @param role role to add to the permission.
   * 
   * @return {@code true} if the role was added, {@code false} otherwise.
   * 
   * @throws NullPointerException if the role is {@code null}.
   */
  public final boolean addRole(final Role role) {
    if (this.getRoles().add(role)) {
      role.addPermission(this);
      return true;
    } else {
      return false;
    }
  }

  /**
   * Add a collection of roles to this permission.
   * 
   * @param roles a roles collection to add to the permission.
   * 
   * @return {@code true} if any role was added, {@code false} otherwise.
   * 
   * @throws NullPointerException if the roles collection contains a {@code null} element, or if the
   *         roles collection is {@code null}.
   */
  public final boolean addRoles(final Collection<Role> roles) {
    if (this.getRoles().addAll(roles)) {
      roles.forEach(role -> role.addPermission(this));
      return true;
    } else {
      return false;
    }
  }

  /**
   * Remove a role from this permission.
   * 
   * @param role role to remove from the permission.
   * 
   * @return {@code true} if the role was removed, {@code false} otherwise.
   * 
   * @throws NullPointerException if the role is {@code null}.
   */
  public final boolean removeRole(final Role role) {
    if (this.getRoles().remove(role)) {
      role.removePermission(this);
      return true;
    } else {
      return false;
    }
  }

  /**
   * Remove a collection of roles to this permission.
   * 
   * @param roles a roles collection to remove from the permission.
   * 
   * @return {@code true} if any role was removed, {@code false} otherwise.
   * 
   * @throws NullPointerException if the role collection contains a {@code null} element, or if the
   *         role collection is {@code null}.
   */
  public final boolean removeRoles(final Collection<Role> roles) {
    if (this.getRoles().removeAll(roles)) {
      roles.forEach(role -> role.removePermission(this));
      return true;
    } else {
      return false;
    }
  }

  @Override
  public <T extends AbstractGenericEntity> void update(T entity) {
    super.update(entity);

    if (entity instanceof Permission) {
      final Permission permission = (Permission) entity;
      
      this.setName(permission.getName());
    }
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

    if (equals && obj instanceof Permission) {
      final Permission other = (Permission) obj;
      equals = Objects.equals(getName(), other.getName());
    }

    return equals;
  }

  @Override
  public String toString() {
    return ReflectionToStringBuilder.toStringExclude(this, "roles");
  }

  /**
   * A functional programming permission builder.
   * 
   * @author madmath03
   */
  public static class PermissionBuilder extends AbstractGenericEntity.Builder<Permission> {

    /**
     * Create a {@link PermissionBuilder}.
     *
     */
    private PermissionBuilder() {
      super();
    }

    /**
     * Create a {@link PermissionBuilder}.
     * 
     * @param name the name of your record being built.
     */
    private PermissionBuilder(final String name) {
      this();
      this.name(name);
    }

    @Override
    protected final Permission buildEntity() {
      return new Permission();
    }

    /**
     * Set the name and return the builder.
     * 
     * @see Permission#setName(String)
     * 
     * @param name the name of your record being built.
     * 
     * @return the builder.
     */
    public PermissionBuilder name(final String name) {
      this.getEntity().setName(name);
      return this;
    }
  }

}
