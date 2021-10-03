package com.monogramm.starter.persistence.type.entity;

import com.monogramm.starter.persistence.AbstractGenericEntity;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "type")
public class Type extends AbstractGenericEntity {
  /**
   * The {@code serialVersionUID}.
   */
  private static final long serialVersionUID = 8824141692184776818L;

  /**
   * Display name maximum length.
   */
  public static final int MAX_LENGTH_DISPLAYNAME = 30;

  /**
   * Get a new {@link TypeBuilder}.
   *
   * @return a new {@link TypeBuilder}.
   */
  public static TypeBuilder builder() {
    return new TypeBuilder();
  }

  /**
   * Get a new {@link TypeBuilder}.
   * 
   * @param name the name of your record being built.
   *
   * @return a new {@link TypeBuilder}.
   */
  public static TypeBuilder builder(final String name) {
    return new TypeBuilder(name);
  }

  /**
   * The name of this record.
   */
  @Column(name = "name", unique = true, nullable = false, length = MAX_LENGTH_DISPLAYNAME)
  private String name;

  /**
   * Create a {@link Type}.
   * 
   */
  public Type() {
    super();
  }

  /**
   * Create a {@link Type}.
   * 
   * @param name name of this record.
   */
  public Type(final String name) {
    super();
    this.name = name;
  }

  /**
   * Create a copy of a {@link Type}.
   * 
   * @param other the other entity to copy.
   * 
   * @throws NullPointerException if the {@code other} entity is @{code null}.
   */
  public Type(final Type other) {
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

  @Override
  public <T extends AbstractGenericEntity> void update(T entity) {
    super.update(entity);

    if (entity instanceof Type) {
      final Type type = (Type) entity;

      this.setName(type.getName());
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

    if (equals && obj instanceof Type) {
      final Type other = (Type) obj;
      equals = Objects.equals(getName(), other.getName());
    }

    return equals;
  }

  /**
   * A functional programming type builder.
   * 
   * @author madmath03
   */
  public static class TypeBuilder extends AbstractGenericEntity.Builder<Type> {

    /**
     * Create a {@link TypeBuilder}.
     *
     */
    private TypeBuilder() {
      super();
    }

    /**
     * Create a {@link TypeBuilder}.
     * 
     * @param name the name of your record being built.
     */
    private TypeBuilder(final String name) {
      this();
      this.name(name);
    }

    @Override
    protected final Type buildEntity() {
      return new Type();
    }

    /**
     * Set the name and return the builder.
     * 
     * @see Type#setName(String)
     * 
     * @param name the name of your record being built.
     * 
     * @return the builder.
     */
    public TypeBuilder name(final String name) {
      this.getEntity().setName(name);
      return this;
    }
  }

}
