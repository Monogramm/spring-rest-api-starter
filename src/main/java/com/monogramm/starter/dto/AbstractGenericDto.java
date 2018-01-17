/*
 * Creation by madmath03 the 2017-11-13.
 */

package com.monogramm.starter.dto;

import com.monogramm.starter.persistence.AbstractGenericEntity;
import com.monogramm.starter.utils.Jsonable;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * AbstractGenericDto.
 * 
 * @see AbstractGenericEntity
 * 
 * @author madmath03
 */
public abstract class AbstractGenericDto implements Serializable, Jsonable {
  /**
   * The {@code serialVersionUID}.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The Universally Unique Identifier (primary key) of this record.
   */
  private UUID id;

  /**
   * An auto-populating date/time stamp of when the record was created.
   */
  private Date createdAt;

  /**
   * Id (relation) to whom created the record.
   */
  private UUID createdBy;

  /**
   * An auto-populating date/time stamp of when the record was last modified.
   */
  private Date modifiedAt;

  /**
   * Id (relation) to whom last modified the record.
   */
  private UUID modifiedBy;

  /**
   * Id (relation) to whom owns the record.
   */
  private UUID owner;


  /**
   * Create a {@link AbstractGenericDto}.
   * 
   */
  public AbstractGenericDto() {
    super();
  }

  /**
   * Create a copy of a {@link AbstractGenericEntity}.
   * 
   * @param other the other DTO to copy.
   * 
   * @throws NullPointerException if the {@code other} DTO is @{code null}.
   */
  public AbstractGenericDto(final AbstractGenericDto other) {
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
  public final UUID getId() {
    return id;
  }

  /**
   * Set the {@link #id}.
   * 
   * @param id the {@link #id} to set.
   */
  public final void setId(final UUID id) {
    this.id = id;
  }

  /**
   * Get the {@link #createdAt}.
   * 
   * @return the {@link #createdAt}.
   */
  public final Date getCreatedAt() {
    return createdAt;
  }

  /**
   * Set the {@link createdAt}.
   * 
   * @param createdAt the {@link #createdAt} to set.
   */
  public final void setCreatedAt(final Date createdAt) {
    this.createdAt = createdAt;
  }

  /**
   * Get the {@link #createdBy}.
   * 
   * @return the {@link #createdBy}.
   */
  public final UUID getCreatedBy() {
    return createdBy;
  }

  /**
   * Set the {@link createdBy}.
   * 
   * @param createdBy the {@link #createdBy} to set.
   */
  public final void setCreatedBy(final UUID createdBy) {
    this.createdBy = createdBy;
  }

  /**
   * Get the {@link #modifiedAt}.
   * 
   * @return the {@link #modifiedAt}.
   */
  public final Date getModifiedAt() {
    return modifiedAt;
  }

  /**
   * Set the {@link modifiedAt}.
   * 
   * @param modifiedAt the {@link #modifiedAt} to set.
   */
  public final void setModifiedAt(final Date modifiedAt) {
    this.modifiedAt = modifiedAt;
  }

  /**
   * Get the {@link #modifiedBy}.
   * 
   * @return the {@link #modifiedBy}.
   */
  public final UUID getModifiedBy() {
    return modifiedBy;
  }

  /**
   * Set the {@link modifiedBy}.
   * 
   * @param modifiedBy the {@link #modifiedBy} to set.
   */
  public final void setModifiedBy(final UUID modifiedBy) {
    this.modifiedBy = modifiedBy;
  }

  /**
   * Get the {@link #owner}.
   * 
   * @return the {@link #owner}.
   */
  public final UUID getOwner() {
    return owner;
  }

  /**
   * Set the {@link owner}.
   * 
   * @param owner the {@link #owner} to set.
   */
  public final void setOwner(final UUID owner) {
    this.owner = owner;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
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
    } else if (!(obj instanceof AbstractGenericDto)) {
      equals = false;
    } else {
      final AbstractGenericDto other = (AbstractGenericDto) obj;
      equals = Objects.equals(getId(), other.getId());
    }

    return equals;
  }

}
