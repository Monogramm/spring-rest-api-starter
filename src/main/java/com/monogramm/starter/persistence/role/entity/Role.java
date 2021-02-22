package com.monogramm.starter.persistence.role.entity;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.monogramm.starter.persistence.AbstractGenericEntity;
import com.monogramm.starter.persistence.permission.entity.Permission;
import com.monogramm.starter.persistence.user.entity.User;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

@Entity
@Table(name = "role")
public class Role extends AbstractGenericEntity {
  /**
   * The {@code serialVersionUID}.
   */
  private static final long serialVersionUID = 973216963525006343L;

  /**
   * Display name maximum length.
   */
  public static final int MAX_LENGTH_DISPLAYNAME = 30;

  /**
   * Get a new {@link RoleBuilder}.
   *
   * @return a new {@link RoleBuilder}.
   */
  public static RoleBuilder builder() {
    return new RoleBuilder();
  }

  /**
   * Get a new {@link RoleBuilder}.
   * 
   * @param name the name of your record being built.
   *
   * @return a new {@link RoleBuilder}.
   */
  public static RoleBuilder builder(final String name) {
    return new RoleBuilder(name);
  }

  /**
   * The name of this record.
   */
  @Column(name = "name", unique = true, nullable = false, length = MAX_LENGTH_DISPLAYNAME)
  private String name = null;

  /**
   * Users having this role through retrieved through their foreign key relation.
   */
  @JsonIgnore
  @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
  private final Set<User> users = new HashSet<>();

  /**
   * Permissions attached to this role through their foreign key relation.
   */
  @JsonIdentityReference(alwaysAsId = true)
  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "role_permission",
      joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "permission_id", referencedColumnName = "id"))
  private final Set<Permission> permissions = new HashSet<>();

  /**
   * Create a {@link Role}.
   * 
   */
  public Role() {
    super();
  }

  /**
   * Create a {@link Role}.
   * 
   * @param name name of this record.
   */
  public Role(final String name) {
    super();
    this.name = name;
  }

  /**
   * Create a copy of a {@link Role}.
   * 
   * @param other the other entity to copy.
   * 
   * @throws NullPointerException if the {@code other} entity is @{code null}.
   */
  public Role(final Role other) {
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
   * Get the {@link #users}.
   * 
   * @return the {@link #users}.
   */
  public Set<User> getUsers() {
    return this.users;
  }

  /**
   * Add a user to this role.
   * 
   * @param user user to add to the role.
   * 
   * @return {@code true} if the user was added, {@code false} otherwise.
   * 
   * @throws NullPointerException if the user is {@code null}.
   */
  public final boolean addUser(final User user) {
    if (this.getUsers().add(user)) {
      user.setRole(this);
      return true;
    } else {
      return false;
    }
  }

  /**
   * Add a collection of users to this role.
   * 
   * @param users a users collection to add to the role.
   * 
   * @return {@code true} if any user was added, {@code false} otherwise.
   * 
   * @throws NullPointerException if the users collection contains a {@code null} element, or if the
   *         users collection is {@code null}.
   */
  public final boolean addUsers(final Collection<User> users) {
    if (this.getUsers().addAll(users)) {
      users.forEach(user -> user.setRole(this));
      return true;
    } else {
      return false;
    }
  }

  /**
   * Remove a user from this role.
   * 
   * @param user user to remove the role.
   * 
   * @return {@code true} if the user was removed, {@code false} otherwise.
   * 
   * @throws NullPointerException if the user is {@code null}.
   */
  public final boolean removeUser(final User user) {
    if (this.getUsers().remove(user)) {
      user.setRole(null);
      return true;
    } else {
      return false;
    }
  }

  /**
   * Remove a collection of users to this role.
   * 
   * @param users a users collection to remove to the role.
   * 
   * @return {@code true} if any user was removed, {@code false} otherwise.
   * 
   * @throws NullPointerException if the users collection contains a {@code null} element, or if the
   *         users collection is {@code null}.
   */
  public final boolean removeUsers(final Collection<User> users) {
    if (this.getUsers().removeAll(users)) {
      users.forEach(user -> user.setRole(null));
      return true;
    } else {
      return false;
    }
  }

  /**
   * Get the {@link #permissions}.
   * 
   * @return the {@link #permissions}.
   */
  public Set<Permission> getPermissions() {
    return permissions;
  }

  /**
   * Add a permission to this role.
   * 
   * @param permission permission to add to the role.
   * 
   * @return {@code true} if the permission was added, {@code false} otherwise.
   * 
   * @throws NullPointerException if the permission is {@code null}.
   */
  public final boolean addPermission(final Permission permission) {
    if (this.getPermissions().add(permission)) {
      permission.addRole(this);
      return true;
    } else {
      return false;
    }
  }

  /**
   * Add a collection of permissions to this role.
   * 
   * @param permissions a permissions collection to add to the role.
   * 
   * @return {@code true} if any permission was added, {@code false} otherwise.
   * 
   * @throws NullPointerException if the permissions collection contains a {@code null} element, or
   *         if the permissions collection is {@code null}.
   */
  public final boolean addPermissions(final Collection<Permission> permissions) {
    if (this.getPermissions().addAll(permissions)) {
      permissions.forEach(permission -> permission.addRole(this));
      return true;
    } else {
      return false;
    }
  }

  /**
   * Remove a permission from this role.
   * 
   * @param permission permission to remove from the role.
   * 
   * @return {@code true} if the permission was removed, {@code false} otherwise.
   * 
   * @throws NullPointerException if the permission is {@code null}.
   */
  public final boolean removePermission(final Permission permission) {
    if (this.getPermissions().remove(permission)) {
      permission.removeRole(this);
      return true;
    } else {
      return false;
    }
  }

  /**
   * Remove a collection of permissions to this role.
   * 
   * @param permissions a permissions collection to remove from the role.
   * 
   * @return {@code true} if any permission was removed, {@code false} otherwise.
   * 
   * @throws NullPointerException if the permission collection contains a {@code null} element, or
   *         if the permission collection is {@code null}.
   */
  public final boolean removePermissions(final Collection<Permission> permissions) {
    if (this.getPermissions().removeAll(permissions)) {
      permissions.forEach(permission -> permission.removeRole(this));
      return true;
    } else {
      return false;
    }
  }

  /**
   * Clears this role's permissions.
   */
  public final void clearPermissions() {
    Permission[] clonePermissions = this.getPermissions().toArray(new Permission[] {});

    this.getPermissions().clear();

    for (Permission permission : clonePermissions) {
      permission.removeRole(this);
    }
  }

  @Override
  public <T extends AbstractGenericEntity> void update(T entity) {
    super.update(entity);

    if (entity instanceof Role) {
      final Role role = (Role) entity;

      this.setName(role.getName());
      this.clearPermissions();
      this.addPermissions(role.getPermissions());
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

    if (equals && obj instanceof Role) {
      final Role other = (Role) obj;
      equals = Objects.equals(getName(), other.getName());
    }

    return equals;
  }

  @Override
  public String toString() {
    return ReflectionToStringBuilder.toStringExclude(this, "users");
  }

  /**
   * A functional programming role builder.
   * 
   * @author madmath03
   */
  public static class RoleBuilder extends AbstractGenericEntity.Builder<Role> {

    /**
     * Create a {@link RoleBuilder}.
     *
     */
    private RoleBuilder() {
      super();
    }

    /**
     * Create a {@link RoleBuilder}.
     * 
     * @param name the name of your record being built.
     */
    private RoleBuilder(final String name) {
      this();
      this.name(name);
    }

    @Override
    protected final Role buildEntity() {
      return new Role();
    }

    /**
     * Set the name and return the builder.
     * 
     * @see Role#setName(String)
     * 
     * @param name the name of your record being built.
     * 
     * @return the builder.
     */
    public RoleBuilder name(final String name) {
      this.getEntity().setName(name);
      return this;
    }

    /**
     * Add permissions and return the builder.
     * 
     * @see Role#addPermissions(Collection)
     * 
     * @param permissions a permissions collection to add to the role.
     * 
     * @return the builder.
     */
    public RoleBuilder permissions(final Collection<Permission> permissions) {
      this.getEntity().addPermissions(permissions);
      return this;
    }
  }

}
