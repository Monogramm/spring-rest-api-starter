/*
 * Creation by madmath03 the 2018-01-08.
 */

package com.monogramm.starter.persistence;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;

/**
 * Abstract Parameter entity.
 * 
 * @author madmath03
 */
@MappedSuperclass
public abstract class AbstractParameter extends AbstractGenericEntity {

  /**
   * The {@code serialVersionUID}.
   */
  private static final long serialVersionUID = 984519142288845529L;


  /**
   * Parameter name property.
   */
  public static final String NAME_PROPERTY = "name";
  /**
   * Parameter name length.
   */
  public static final int MAX_LENGTH_NAME = 32;


  /**
   * Parameter description property.
   */
  public static final String DESCRIPTION_PROPERTY = "description";
  /**
   * Parameter description length.
   */
  public static final int MAX_LENGTH_DESCRIPTION = 512;


  /**
   * Parameter type property.
   */
  public static final String TYPE_PROPERTY = "type";
  /**
   * Parameter type length.
   */
  public static final int MAX_LENGTH_TYPE = 255;


  /**
   * Parameter value property.
   */
  public static final String VALUE_PROPERTY = "value";
  /**
   * Parameter value length.
   */
  public static final int MAX_LENGTH_VALUE = 1024;


  /**
   * The parameter name.
   */
  @Column(name = NAME_PROPERTY, unique = true, nullable = false, length = MAX_LENGTH_NAME)
  private String name;

  /**
   * The parameter description.
   */
  @Column(name = DESCRIPTION_PROPERTY, nullable = true, length = MAX_LENGTH_DESCRIPTION)
  private String description;

  /**
   * The parameter type.
   */
  @Column(name = TYPE_PROPERTY, nullable = false, length = MAX_LENGTH_TYPE)
  @Enumerated(EnumType.STRING)
  private ParameterType type = ParameterType.ANY;

  /**
   * The parameter value.
   */
  @Column(name = VALUE_PROPERTY, nullable = true, length = MAX_LENGTH_VALUE)
  private String value;

  /**
   * Create a {@link AbstractParameter}.
   * 
   */
  public AbstractParameter() {
    super();
  }

  /**
   * Create a copy of a {@link AbstractParameter}.
   * 
   * @param other the other entity to copy.
   * 
   * @throws NullPointerException if the {@code other} entity is @{code null}.
   */
  public AbstractParameter(final AbstractParameter other) {
    super(other);

    this.name = other.name;
    this.description = other.description;
    this.type = other.type;
    this.value = other.value;
  }

  /**
   * Create a {@link AbstractParameter}.
   * 
   * @param name the parameter name.
   * @param value the parameter value.
   */
  public AbstractParameter(String name, Object value) {
    super();

    this.setName(name);
    this.setValue(value);
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
   * Set the {@link #name}.
   * 
   * @param name the {@link #name} to set.
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Get the {@link #description}.
   * 
   * @return the {@link #description}.
   */
  public String getDescription() {
    return description;
  }

  /**
   * Set the {@link #description}.
   * 
   * @param description the {@link #description} to set.
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Get the {@link #type}.
   * 
   * @return the {@link #type}.
   */
  public ParameterType getType() {
    return type;
  }

  /**
   * Set the {@link #type}.
   * 
   * @param type the {@link #type} to set.
   */
  public void setType(ParameterType type) {
    this.type = type;
  }

  /**
   * Get the {@link #value}.
   * 
   * @see ParameterType#read(String)
   * 
   * @return the {@link #value}.
   */
  public Object readValue() {
    return type.read(value);
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
   * Write the {@link #value} using the {@link #type} writer.
   * 
   * @see ParameterType#write(Object)
   */
  public void writeValue(String value) {
    type.write(value);
  }

  /**
   * Set the {@link #value}.
   * 
   * <p>
   * If the {@link #type} is {@code null}, it will be updated based on the value class.
   * </p>
   * 
   * @see ParameterType#typeOf(Object)
   * @see #setType(ParameterType)
   * 
   * @param value the {@link #value} to set.
   */
  public void setValue(String value) {
    this.value = value;
  }

  /**
   * Set the {@link #value}.
   * 
   * <p>
   * If the {@link #type} is {@code null}, it will be updated based on the value class.
   * </p>
   * 
   * @see ParameterType#typeOf(Object)
   * @see #setType(ParameterType)
   * 
   * @param value the {@link #value} to set.
   */
  public void setValue(Object value) {
    if (value != null) {
      final ParameterType typeOfValue = ParameterType.typeOf(value);
      this.setType(typeOfValue);
      this.setValue(typeOfValue.write(value));
    } else {
      this.setValue((String) null);
    }
  }

  @Override
  public <T extends AbstractGenericEntity> void update(T entity) {
    super.update(entity);

    if (entity instanceof AbstractParameter) {
      final AbstractParameter parameter = (AbstractParameter) entity;

      this.setName(parameter.getName());
      this.setDescription(parameter.getDescription());
      this.setType(parameter.getType());
      this.setValue(parameter.getValue());
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

    if (this.getDescription() == null) {
      hash *= 31;
    } else {
      hash = hash * 31 + this.getDescription().hashCode();
    }

    if (this.getType() == null) {
      hash *= 31;
    } else {
      hash = hash * 31 + this.getType().hashCode();
    }

    if (this.getValue() == null) {
      hash *= 31;
    } else {
      hash = hash * 31 + this.getValue().hashCode();
    }

    return hash;
  }

  @Override
  public boolean equals(final Object obj) {
    boolean equals = super.equals(obj);

    if (equals && obj instanceof AbstractParameter) {
      final AbstractParameter other = (AbstractParameter) obj;
      equals = Objects.equals(getName(), other.getName())
          && Objects.equals(getDescription(), other.getDescription())
          && Objects.equals(getType(), other.getType())
          && Objects.equals(getValue(), other.getValue());
    }

    return equals;
  }

  /**
   * A functional programming {@link AbstractParameter} builder.
   * 
   * @author madmath03
   */
  public abstract static class AbstractParameterBuilder<T extends AbstractParameter>
      extends AbstractGenericEntity.Builder<T> {

    /**
     * Create a {@link AbstractParameterBuilder}.
     *
     */
    protected AbstractParameterBuilder() {
      super();
    }

    /**
     * Create a {@link AbstractParameterBuilder}.
     * 
     * @param name the name of your record being built.
     * @param value the value of your record being built.
     */
    protected AbstractParameterBuilder(final String name, final Object value) {
      this();
      this.name(name).value(value);
    }

    /**
     * Set the name and return the builder.
     * 
     * @see AbstractParameter#setName(String)
     * 
     * @param name the name of your record being built.
     * 
     * @return the builder.
     */
    public AbstractParameterBuilder<T> name(final String name) {
      this.getEntity().setName(name);
      return this;
    }

    /**
     * Set the description and return the builder.
     * 
     * @see AbstractParameter#setDescription(String)
     * 
     * @param description the description of your record being built.
     * 
     * @return the builder.
     */
    public AbstractParameterBuilder<T> description(final String description) {
      this.getEntity().setDescription(description);
      return this;
    }

    /**
     * Set the type and return the builder.
     * 
     * @see AbstractParameter#setType(ParameterType)
     * 
     * @param type the type of your record being built.
     * 
     * @return the builder.
     */
    public AbstractParameterBuilder<T> type(final ParameterType type) {
      this.getEntity().setType(type);
      return this;
    }

    /**
     * Set the value and return the builder.
     * 
     * @see AbstractParameter#setValue(Object)
     * 
     * @param value the value of your record being built.
     * 
     * @return the builder.
     */
    public AbstractParameterBuilder<T> value(final Object value) {
      this.getEntity().setValue(value);
      return this;
    }
  }

}
