package com.monogramm.starter.persistence;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.monogramm.starter.persistence.user.entity.User;
import com.monogramm.starter.utils.Jsonable;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;

/**
 * An abstract for generic entities.
 * 
 * <p>
 * Provides a Universally Unique Identifier as primary key, creation and modification date and user
 * reference and ownership reference.
 * </p>
 * 
 * <p>
 * <strong>Note:</strong> Make sure to mark all properties getter/setter as non-final, otherwise
 * Javassist Lazy Loaded proxies will not be able override them.
 * </p>
 * 
 * @author madmath03
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@MappedSuperclass
public abstract class AbstractGenericEntity implements Serializable, Jsonable {
  /**
   * The {@code serialVersionUID}.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The Universally Unique Identifier (primary key) of this record.
   */
  @Id
  @GeneratedValue(generator = "uuid2")
  @GenericGenerator(name = "uuid2", strategy = "uuid2")
  @Column(name = "id", columnDefinition = "BINARY(16)", nullable = false, updatable = false)
  private UUID id;

  /**
   * An auto-populating date/time stamp of when the record was created.
   */
  @Column(name = "created_at", columnDefinition = "TIMESTAMP", nullable = false, updatable = false)
  private Date createdAt;

  /**
   * Foreign key (relation) to whom created the record.
   */
  @JsonIdentityReference(alwaysAsId = true)
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "created_by", columnDefinition = "BINARY(16)", nullable = true)
  private User createdBy = null;

  /**
   * An auto-populating date/time stamp of when the record was last modified.
   */
  @Column(name = "modified_at", columnDefinition = "TIMESTAMP", nullable = true, insertable = false)
  private Date modifiedAt = null;

  /**
   * Foreign key (relation) to whom last modified the record.
   */
  @JsonIdentityReference(alwaysAsId = true)
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "modified_by", columnDefinition = "BINARY(16)", nullable = true)
  private User modifiedBy = null;

  /**
   * Foreign key (relation) to whom owns the record.
   */
  @JsonIdentityReference(alwaysAsId = true)
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "owner", columnDefinition = "BINARY(16)", nullable = true)
  private User owner = null;


  /**
   * Create a {@link AbstractGenericEntity}.
   * 
   */
  public AbstractGenericEntity() {
    super();
  }

  /**
   * Create a copy of a {@link AbstractGenericEntity}.
   * 
   * @param other the other entity to copy.
   * 
   * @throws NullPointerException if the {@code other} entity is @{code null}.
   */
  public AbstractGenericEntity(final AbstractGenericEntity other) {
    super();

    this.id = other.getId();
    this.createdAt = other.getCreatedAt();
    this.createdBy = other.getCreatedBy();
    this.modifiedAt = other.getModifiedAt();
    this.modifiedBy = other.getModifiedBy();
    this.owner = other.getOwner();
  }


  /**
   * Get the {@link #id}.
   * 
   * @return the {@link #id}.
   */
  public UUID getId() {
    return id;
  }

  /**
   * Set the {@link #id}.
   * 
   * @param id the {@link #id} to set.
   */
  public void setId(final UUID id) {
    this.id = id;
  }

  /**
   * Get the {@link #createdAt}.
   * 
   * @return the {@link #createdAt}.
   */
  public Date getCreatedAt() {
    return createdAt;
  }

  /**
   * Set the {@link createdAt}.
   * 
   * @param createdAt the {@link #createdAt} to set.
   */
  public void setCreatedAt(final Date createdAt) {
    this.createdAt = createdAt;
  }

  /**
   * Get the {@link #createdBy}.
   * 
   * @return the {@link #createdBy}.
   */
  public User getCreatedBy() {
    return createdBy;
  }

  /**
   * Set the {@link createdBy}.
   * 
   * @param createdBy the {@link #createdBy} to set.
   */
  public void setCreatedBy(final User createdBy) {
    this.createdBy = createdBy;
  }

  /**
   * Get the {@link #modifiedAt}.
   * 
   * @return the {@link #modifiedAt}.
   */
  public Date getModifiedAt() {
    return modifiedAt;
  }

  /**
   * Set the {@link modifiedAt}.
   * 
   * @param modifiedAt the {@link #modifiedAt} to set.
   */
  public void setModifiedAt(final Date modifiedAt) {
    this.modifiedAt = modifiedAt;
  }

  /**
   * Get the {@link #modifiedBy}.
   * 
   * @return the {@link #modifiedBy}.
   */
  public User getModifiedBy() {
    return modifiedBy;
  }

  /**
   * Set the {@link modifiedBy}.
   * 
   * @param modifiedBy the {@link #modifiedBy} to set.
   */
  public void setModifiedBy(final User modifiedBy) {
    this.modifiedBy = modifiedBy;
  }

  /**
   * Get the {@link #owner}.
   * 
   * @return the {@link #owner}.
   */
  public User getOwner() {
    return owner;
  }

  /**
   * Set the {@link owner}.
   * 
   * @param owner the {@link #owner} to set.
   */
  public void setOwner(final User owner) {
    this.owner = owner;
  }

  /**
   * Update the entity with the properties of another entity.
   * 
   * @param <T> Entity class.
   * 
   * @param entity another entity.
   * 
   * @throws NullPointerException if {@code entity} is {@code null}.
   */
  public <T extends AbstractGenericEntity> void update(final T entity) {
    if (!Objects.equals(this.getOwner(), entity.getOwner())) {
      this.setOwner(entity.getOwner());
    }
  }

  @Override
  public int hashCode() {
    int hash = 17;

    if (this.getId() != null) {
      hash = hash * 31 + this.getId().hashCode();
    }

    return hash;
  }

  @Override
  public boolean equals(final Object obj) {
    final boolean equals;

    if (this == obj) {
      equals = true;
    } else if (obj == null) {
      equals = false;
    } else if (!(obj instanceof AbstractGenericEntity)) {
      equals = false;
    } else {
      final AbstractGenericEntity other = (AbstractGenericEntity) obj;
      equals = Objects.equals(getId(), other.getId());
    }

    return equals;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  @PrePersist
  protected void preInsert() {
    if (this.id == null) {
      this.id = UUID.randomUUID();
    }
    if (this.createdAt == null) {
      this.createdAt = new Date();
    }
  }

  @PreUpdate
  protected void preUpdate() {
    this.modifiedAt = new Date();
  }

  /**
   * A functional programming entity builder.
   * 
   * @param <T> the type of entity being built.
   * 
   * @author madmath03
   */
  public abstract static class Builder<T extends AbstractGenericEntity> {

    /**
     * The entity currently being built.
     */
    private final T entity;

    /**
     * Create a {@link Builder} with no initial property.
     */
    public Builder() {
      this.entity = this.buildEntity();
    }

    /**
     * Construct an entity to build.
     * 
     * @return an entity for build.
     */
    protected abstract T buildEntity();

    /**
     * Get the {@link #entity}.
     * 
     * @return the {@link #entity}.
     */
    protected final T getEntity() {
      return entity;
    }

    /**
     * Build the entity prepared by the builder.
     * 
     * @return a new entity as prepared by the builder.
     */
    public T build() {
      return entity;
    }

    /**
     * Set the entity id and return the builder.
     * 
     * @see AbstractGenericEntity#setId(UUID)
     * 
     * @param id the id of the entity being built.
     * 
     * @return the builder.
     */
    public Builder<T> id(final UUID id) {
      entity.setId(id);
      return this;
    }

    /**
     * Set the entity id and return the builder.
     * 
     * @see AbstractGenericEntity#setCreatedAt(Date)
     * 
     * @param date the creation date of the entity being built.
     * 
     * @return the builder.
     */
    public Builder<T> createdAt(final Date date) {
      entity.setCreatedAt(date);
      return this;
    }

    /**
     * Set the entity id of the user who created the entity and return the builder.
     * 
     * @see AbstractGenericEntity#setCreatedBy(User)
     * 
     * @param user the user who created the entity of the entity being built.
     * 
     * @return the builder.
     */
    public Builder<T> createdBy(final User user) {
      entity.setCreatedBy(user);
      return this;
    }

    /**
     * Set the entity last modification date and return the builder.
     * 
     * @see AbstractGenericEntity#setModifiedBy(User)
     * 
     * @param date the last modification date of the entity being built.
     * 
     * @return the builder.
     */
    public Builder<T> modifiedAt(final Date date) {
      entity.setModifiedAt(date);
      return this;
    }

    /**
     * Set the entity id of the user who did the last modification and return the builder.
     * 
     * @see AbstractGenericEntity#setModifiedBy(User)
     * 
     * @param user the user who did the last modification of the entity being built.
     * 
     * @return the builder.
     */
    public Builder<T> modifiedBy(final User user) {
      entity.setModifiedBy(user);
      return this;
    }

    /**
     * Set the entity owner's id and return the builder.
     * 
     * @see AbstractGenericEntity#setOwner(User)
     * 
     * @param user the owner of the entity being built.
     * 
     * @return the builder.
     */
    public Builder<T> owner(final User user) {
      entity.setOwner(user);
      return this;
    }
  }


}
