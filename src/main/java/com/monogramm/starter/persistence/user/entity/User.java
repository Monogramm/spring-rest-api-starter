package com.monogramm.starter.persistence.user.entity;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.madmath03.password.Passwords;
import com.monogramm.starter.persistence.AbstractGenericEntity;
import com.monogramm.starter.persistence.AbstractToken;
import com.monogramm.starter.persistence.parameter.entity.Parameter;
import com.monogramm.starter.persistence.permission.entity.Permission;
import com.monogramm.starter.persistence.role.entity.Role;
import com.monogramm.starter.persistence.type.entity.Type;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import javax.persistence.Table;

@Entity
@Table(name = "user_account")
public class User extends AbstractGenericEntity {
  /**
   * The {@code serialVersionUID}.
   */
  private static final long serialVersionUID = -7706401632533879097L;

  /**
   * Username maximum length.
   */
  public static final int MAX_LENGTH_USERNAME = 255;
  /**
   * Email maximum length.
   */
  public static final int MAX_LENGTH_EMAIL = 255;
  /**
   * Password maximum length.
   */
  public static final int MAX_LENGTH_PASSWORD = 512;

  /**
   * Get a new {@link UserBuilder}.
   *
   * @return a new {@link UserBuilder}.
   */
  public static UserBuilder builder() {
    return new UserBuilder();
  }

  /**
   * Get a new {@link UserBuilder}.
   * 
   * @param username the user name of the user being built.
   *
   * @return a new {@link UserBuilder}.
   */
  public static UserBuilder builder(final String username) {
    return new UserBuilder(username);
  }

  /**
   * Get a new {@link UserBuilder}.
   * 
   * @param username the user name of the user being built.
   * @param email the email of the user being built.
   *
   * @return a new {@link UserBuilder}.
   */
  public static UserBuilder builder(final String username, final String email) {
    return new UserBuilder(username, email);
  }

  /**
   * The user's account name.
   */
  @Column(name = "username", unique = true, nullable = false, length = MAX_LENGTH_USERNAME)
  private String username = null;

  /**
   * The user's account email.
   */
  @Column(name = "email", unique = true, nullable = false, length = MAX_LENGTH_EMAIL)
  private String email = null;

  /**
   * The user's account hashed password.
   */
  @Column(name = "password", length = MAX_LENGTH_PASSWORD, updatable = true)
  private String password = null;

  /**
   * Is the user's account active.
   */
  @Column(name = "enabled", nullable = false)
  private boolean enabled = true;

  /**
   * Is the user's account verified.
   */
  @Column(name = "verified", nullable = false)
  private boolean verified = false;

  /**
   * Foreign key (relation) to the user\'s account role.
   */
  @JsonIdentityReference(alwaysAsId = true)
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "role", nullable = true)
  private Role role = null;


  // ################################################################
  // Relationships with all other entities
  // ################################################################

  /**
   * User accounts created by this user.
   */
  @JsonIgnore
  @OneToMany(mappedBy = CREATED_BY_FIELD, fetch = FetchType.LAZY)
  private final Set<User> createdUsers = new HashSet<>();
  /**
   * User accounts modified by this user.
   */
  @JsonIgnore
  @OneToMany(mappedBy = MODIFIED_BY_FIELD, fetch = FetchType.LAZY)
  private final Set<User> modifiedUsers = new HashSet<>();
  /**
   * User accounts owned by this user.
   */
  @JsonIgnore
  @OneToMany(mappedBy = OWNER_FIELD, fetch = FetchType.LAZY)
  private final Set<User> ownedUsers = new HashSet<>();


  /**
   * Types created by this user.
   */
  @JsonIgnore
  @OneToMany(mappedBy = CREATED_BY_FIELD, fetch = FetchType.LAZY)
  private final Set<Type> createdTypes = new HashSet<>();
  /**
   * Types modified by this user.
   */
  @JsonIgnore
  @OneToMany(mappedBy = MODIFIED_BY_FIELD, fetch = FetchType.LAZY)
  private final Set<Type> modifiedTypes = new HashSet<>();
  /**
   * Types owned by this user.
   */
  @JsonIgnore
  @OneToMany(mappedBy = OWNER_FIELD, fetch = FetchType.LAZY)
  private final Set<Type> ownedTypes = new HashSet<>();


  /**
   * Roles created by this user.
   */
  @JsonIgnore
  @OneToMany(mappedBy = CREATED_BY_FIELD, fetch = FetchType.LAZY)
  private final Set<Role> createdRoles = new HashSet<>();
  /**
   * Roles modified by this user.
   */
  @JsonIgnore
  @OneToMany(mappedBy = MODIFIED_BY_FIELD, fetch = FetchType.LAZY)
  private final Set<Role> modifiedRoles = new HashSet<>();
  /**
   * Roles owned by this user.
   */
  @JsonIgnore
  @OneToMany(mappedBy = OWNER_FIELD, fetch = FetchType.LAZY)
  private final Set<Role> ownedRoles = new HashSet<>();


  /**
   * Permissions created by this user.
   */
  @JsonIgnore
  @OneToMany(mappedBy = CREATED_BY_FIELD, fetch = FetchType.LAZY)
  private final Set<Permission> createdPermissions = new HashSet<>();
  /**
   * Permissions modified by this user.
   */
  @JsonIgnore
  @OneToMany(mappedBy = MODIFIED_BY_FIELD, fetch = FetchType.LAZY)
  private final Set<Permission> modifiedPermissions = new HashSet<>();
  /**
   * Permissions owned by this user.
   */
  @JsonIgnore
  @OneToMany(mappedBy = OWNER_FIELD, fetch = FetchType.LAZY)
  private final Set<Permission> ownedPermissions = new HashSet<>();


  /**
   * Parameters created by this user.
   */
  @JsonIgnore
  @OneToMany(mappedBy = CREATED_BY_FIELD, fetch = FetchType.LAZY)
  private final Set<Parameter> createdParameters = new HashSet<>();
  /**
   * Parameters modified by this user.
   */
  @JsonIgnore
  @OneToMany(mappedBy = MODIFIED_BY_FIELD, fetch = FetchType.LAZY)
  private final Set<Parameter> modifiedParameters = new HashSet<>();
  /**
   * Parameters owned by this user.
   */
  @JsonIgnore
  @OneToMany(mappedBy = OWNER_FIELD, fetch = FetchType.LAZY)
  private final Set<Parameter> ownedParameters = new HashSet<>();


  /**
   * Password reset tokens created by this user.
   */
  @JsonIgnore
  @OneToMany(mappedBy = CREATED_BY_FIELD, fetch = FetchType.LAZY)
  private final Set<PasswordResetToken> createdPasswordResetTokens = new HashSet<>();
  /**
   * Password reset tokens modified by this user.
   */
  @JsonIgnore
  @OneToMany(mappedBy = MODIFIED_BY_FIELD, fetch = FetchType.LAZY)
  private final Set<PasswordResetToken> modifiedPasswordResetTokens = new HashSet<>();
  /**
   * Password reset tokens owned by this user.
   */
  @JsonIgnore
  @OneToMany(mappedBy = OWNER_FIELD, fetch = FetchType.LAZY)
  private final Set<PasswordResetToken> ownedPasswordResetTokens = new HashSet<>();
  /**
   * Password reset tokens associated to this user.
   */
  @JsonIgnore
  @OneToMany(mappedBy = AbstractToken.USER_FIELD, fetch = FetchType.LAZY,
      cascade = CascadeType.REMOVE)
  private final Set<PasswordResetToken> passwordResetTokens = new HashSet<>();


  /**
   * Parameters created by this user.
   */
  @JsonIgnore
  @OneToMany(mappedBy = CREATED_BY_FIELD, fetch = FetchType.LAZY)
  private final Set<VerificationToken> createdVerificationTokens = new HashSet<>();
  /**
   * Parameters modified by this user.
   */
  @JsonIgnore
  @OneToMany(mappedBy = MODIFIED_BY_FIELD, fetch = FetchType.LAZY)
  private final Set<VerificationToken> modifiedVerificationTokens = new HashSet<>();
  /**
   * Parameters owned by this user.
   */
  @JsonIgnore
  @OneToMany(mappedBy = OWNER_FIELD, fetch = FetchType.LAZY)
  private final Set<VerificationToken> ownedVerificationTokens = new HashSet<>();
  /**
   * Password reset tokens associated to this user.
   */
  @JsonIgnore
  @OneToMany(mappedBy = AbstractToken.USER_FIELD, fetch = FetchType.LAZY,
      cascade = CascadeType.REMOVE)
  private final Set<VerificationToken> verificationTokens = new HashSet<>();


  // ################################################################


  /**
   * Create a {@link User}.
   * 
   */
  public User() {
    super();
  }

  /**
   * Create a {@link User}.
   * 
   * @param username username of this user.
   */
  public User(final String username) {
    super();
    this.username = username;
  }

  /**
   * Create a {@link User}.
   * 
   * @param username username of this user.
   * @param email email of this user.
   */
  public User(final String username, final String email) {
    super();
    this.username = username;
    this.email = email;
  }

  /**
   * Create a copy of a {@link User}.
   * 
   * @param other the other entity to copy.
   * 
   * @throws NullPointerException if the {@code other} entity is @{code null}.
   */
  public User(final User other) {
    super(other);

    this.username = other.getUsername();
    this.email = other.getEmail();
    this.password = other.getPassword();
    this.enabled = other.isEnabled();
    this.verified = other.isVerified();
    this.username = other.getUsername();
  }

  /**
   * Get the {@link #username}.
   * 
   * @return the {@link #username}.
   */
  public String getUsername() {
    return username;
  }

  /**
   * Set the {@link username}.
   * 
   * @param username the {@link #username} to set.
   */
  public void setUsername(final String username) {
    this.username = username;
  }

  /**
   * Get the {@link #email}.
   * 
   * @return the {@link #email}.
   */
  public String getEmail() {
    return email;
  }

  /**
   * Set the {@link email}.
   * 
   * <p>
   * Note that changing the email will set the user as unverified.
   * </p>
   * 
   * @param email the {@link #email} to set.
   */
  public void setEmail(final String email) {
    if (!Objects.equals(this.email, email)) {
      this.email = email;
      this.setVerified(false);
    }
  }

  /**
   * Get the {@link #password}.
   * 
   * @return the {@link #password}.
   */
  public String getPassword() {
    return password;
  }

  /**
   * Set the {@link password}.
   * 
   * @param password the clear {@link #password} to hash and set.
   */
  public final void setPassword(final char... password) {
    if (password == null) {
      this.password = null;
    } else {
      this.password = Passwords.getHash(password);
    }
  }

  /**
   * Set the {@link password}.
   * 
   * @param password the hashed {@link #password} to set.
   */
  public void setPassword(final String password) {
    this.password = password;
  }

  /**
   * Get the {@link #enabled}.
   * 
   * @return the {@link #enabled}.
   */
  public boolean isEnabled() {
    return enabled;
  }

  /**
   * Set the {@link enabled}.
   * 
   * @param enabled the {@link #enabled} to set.
   */
  public void setEnabled(final boolean enabled) {
    this.enabled = enabled;
  }

  /**
   * Get the {@link #verified}.
   * 
   * @return the {@link #verified}.
   */
  public boolean isVerified() {
    return verified;
  }

  /**
   * Set the {@link verified}.
   * 
   * @param verified the {@link #verified} to set.
   */
  public void setVerified(final boolean verified) {
    this.verified = verified;
  }

  /**
   * Get the {@link #role}.
   * 
   * @return the {@link #role}.
   */
  public Role getRole() {
    return role;
  }

  /**
   * Set the {@link role}.
   * 
   * @param role the {@link #role} to set.
   */
  public void setRole(final Role role) {
    if (this.role == role) {
      return;
    }

    if (this.role != null) {
      this.role.removeUser(this);
    }

    this.role = role;

    if (role != null) {
      role.addUser(this);
    }
  }

  @Override
  public <T extends AbstractGenericEntity> void update(T entity) {
    super.update(entity);

    if (entity instanceof User) {
      final User user = (User) entity;

      this.setUsername(user.getUsername());
      this.setEmail(user.getEmail());
      if (user.getPassword() != null) {
        this.setPassword(user.getPassword());
      }
      if (user.getRole() != null) {
        this.setRole(user.getRole());
      }
    }
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

    if (equals && obj instanceof User) {
      final User other = (User) obj;
      equals = Objects.equals(getUsername(), other.getUsername());

      if (equals) {
        equals = Objects.equals(getEmail(), other.getEmail());
      }
    }

    return equals;
  }

  @PreRemove
  protected void preRemove() {
    // Set to null references to this user
    for (User entity : this.createdUsers) {
      entity.setCreatedBy(null);
    }
    for (User entity : this.modifiedUsers) {
      entity.setModifiedBy(null);
    }
    for (User entity : this.ownedUsers) {
      entity.setOwner(null);
    }

    // Set to null references to this user
    for (Type entity : this.createdTypes) {
      entity.setCreatedBy(null);
    }
    for (Type entity : this.modifiedTypes) {
      entity.setModifiedBy(null);
    }
    for (Type entity : this.ownedTypes) {
      entity.setOwner(null);
    }

    // Set to null references to this user
    for (Role entity : this.createdRoles) {
      entity.setCreatedBy(null);
    }
    for (Role entity : this.modifiedRoles) {
      entity.setModifiedBy(null);
    }
    for (Role entity : this.ownedRoles) {
      entity.setOwner(null);
    }

    // Set to null references to this user
    for (Permission entity : this.createdPermissions) {
      entity.setCreatedBy(null);
    }
    for (Permission entity : this.modifiedPermissions) {
      entity.setModifiedBy(null);
    }
    for (Permission entity : this.ownedPermissions) {
      entity.setOwner(null);
    }

    // Set to null references to this user
    for (Parameter entity : this.createdParameters) {
      entity.setCreatedBy(null);
    }
    for (Parameter entity : this.modifiedParameters) {
      entity.setModifiedBy(null);
    }
    for (Parameter entity : this.ownedParameters) {
      entity.setOwner(null);
    }

    // Set to null references to this user
    for (PasswordResetToken entity : this.createdPasswordResetTokens) {
      entity.setCreatedBy(null);
    }
    for (PasswordResetToken entity : this.modifiedPasswordResetTokens) {
      entity.setModifiedBy(null);
    }
    for (PasswordResetToken entity : this.ownedPasswordResetTokens) {
      entity.setOwner(null);
    }
    // associated tokens should be removed as well

    // Set to null references to this user
    for (VerificationToken entity : this.createdVerificationTokens) {
      entity.setCreatedBy(null);
    }
    for (VerificationToken entity : this.modifiedVerificationTokens) {
      entity.setModifiedBy(null);
    }
    for (VerificationToken entity : this.ownedVerificationTokens) {
      entity.setOwner(null);
    }
    // associated tokens should be removed as well

  }

  /**
   * A functional programming user builder.
   * 
   * @author madmath03
   */
  public static class UserBuilder extends AbstractGenericEntity.Builder<User> {

    /**
     * Create a {@link UserBuilder}.
     *
     */
    private UserBuilder() {
      super();
    }

    /**
     * Create a {@link UserBuilder}.
     * 
     * @param username the user name of the user being built.
     */
    private UserBuilder(final String username) {
      this();
      this.username(username);
    }

    /**
     * Create a {@link UserBuilder}.
     * 
     * @param username the user name of the user being built.
     * @param email the email of the user being built.
     */
    private UserBuilder(final String username, final String email) {
      this();
      this.username(username);
      this.email(email);
    }

    @Override
    protected final User buildEntity() {
      return new User();
    }

    /**
     * Set the user name and return the builder.
     * 
     * @see User#setUsername(String)
     * 
     * @param username the user name of the user being built.
     * 
     * @return the builder.
     */
    public UserBuilder username(final String username) {
      this.getEntity().setUsername(username);
      return this;
    }

    /**
     * Set the user email and return the builder.
     * 
     * @see User#setEmail(String)
     * 
     * @param email the email of the user being built.
     * 
     * @return the builder.
     */
    public UserBuilder email(final String email) {
      this.getEntity().setEmail(email);
      return this;
    }

    /**
     * Hash and set the user password of the user who created the entity and return the builder.
     * 
     * @see User#setPassword(char...)
     * 
     * @param password the clear password to hash and set for the user being built.
     * 
     * @return the builder.
     */
    public UserBuilder password(final char... password) {
      this.getEntity().setPassword(password);
      return this;
    }

    /**
     * Set the user password of the user who created the entity and return the builder.
     * 
     * @see User#setPassword(String)
     * 
     * @param password the password of the user being built.
     * 
     * @return the builder.
     */
    public UserBuilder password(final String password) {
      this.getEntity().setPassword(password);
      return this;
    }

    /**
     * Set the user active status and return the builder.
     * 
     * @see User#setEnabled(boolean)
     * 
     * @param enabled the active status of the user being built.
     * 
     * @return the builder.
     */
    public UserBuilder enabled(final boolean enabled) {
      this.getEntity().setEnabled(enabled);
      return this;
    }

    /**
     * Set the user verified status and return the builder.
     * 
     * @see User#setVerified(boolean)
     * 
     * @param verified the verified status of the user being built.
     * 
     * @return the builder.
     */
    public UserBuilder verified(final boolean verified) {
      this.getEntity().setVerified(verified);
      return this;
    }

    /**
     * Set the role and return the builder.
     * 
     * @see User#setRole(Role)
     * 
     * @param role the role id of the user being built.
     * 
     * @return the builder.
     */
    public UserBuilder role(final Role role) {
      this.getEntity().setRole(role);
      return this;
    }
  }

}
