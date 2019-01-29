/*
 * Creation by madmath03 the 2018-01-08.
 */

package com.monogramm.starter.dto;

import com.monogramm.starter.persistence.ParameterType;

import java.util.Comparator;
import java.util.Objects;

/**
 * AbstractParameterDto.
 * 
 * @author madmath03
 */
public abstract class AbstractParameterDto extends AbstractGenericDto {

  /**
   * Default {@link AbstractParameterDto} comparator.
   * 
   * <p>
   * Compares {@link AbstractParameterDto} by their name, then switch to
   * {@link AbstractGenericDtoComparator} compare.
   * </p>
   * 
   * @author madmath03
   */
  public static class AbstractParameterDtoComparator extends AbstractGenericDtoComparator {

    @Override
    public int compare(AbstractGenericDto o1, AbstractGenericDto o2) {
      final int compare;

      if (o1 instanceof AbstractParameterDto && o2 instanceof AbstractParameterDto) {
        final int compareName = compareOnString(((AbstractParameterDto) o1).getName(),
            ((AbstractParameterDto) o2).getName());

        if (compareName != 0) {
          compare = compareName;
        } else {
          compare = super.compare(o1, o2);
        }
      } else {
        compare = super.compare(o1, o2);
      }

      return compare;
    }

  }

  /**
   * Default {@link AbstractParameterDto} comparator.
   */
  public static final Comparator<AbstractGenericDto> DEFAULT_ABSTRACT_PARAMETER_DTO_COMPARATOR =
      new AbstractParameterDtoComparator();

  /**
   * The {@code serialVersionUID}.
   */
  private static final long serialVersionUID = 4010104695384193136L;

  /**
   * The parameter name.
   */
  private String name;

  /**
   * The parameter description.
   */
  private String description;

  /**
   * The parameter type.
   */
  private String type = ParameterType.ANY.toString();

  /**
   * The parameter value.
   */
  private String value;

  /**
   * Create a {@link AbstractParameterDto}.
   * 
   */
  public AbstractParameterDto() {
    super();
  }

  /**
   * Create a copy of a {@link AbstractParameterDto}.
   * 
   * @param other the other DTO to copy.
   * 
   * @throws NullPointerException if the other DTO is {@code null}.
   */
  public AbstractParameterDto(final AbstractParameterDto other) {
    super(other);

    this.name = other.name;
    this.description = other.description;
    this.type = other.type;
    this.value = other.value;
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
   * Set the {@link #name}.
   * 
   * @param name the {@link #name} to set.
   */
  public final void setName(String name) {
    this.name = name;
  }

  /**
   * Get the {@link #description}.
   * 
   * @return the {@link #description}.
   */
  public final String getDescription() {
    return description;
  }

  /**
   * Set the {@link #description}.
   * 
   * @param description the {@link #description} to set.
   */
  public final void setDescription(String description) {
    this.description = description;
  }

  /**
   * Get the {@link #type}.
   * 
   * @return the {@link #type}.
   */
  public final String getType() {
    return type;
  }

  /**
   * Set the {@link #type}.
   * 
   * @param type the {@link #type} to set.
   */
  public final void setType(String type) {
    this.type = type;
  }

  /**
   * Get the {@link #value}.
   * 
   * @return the {@link #value}.
   */
  public final String getValue() {
    return value;
  }

  /**
   * Set the {@link #value}.
   * 
   * @param value the {@link #value} to set.
   */
  public final void setValue(String value) {
    this.value = value;
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

    if (equals && obj instanceof AbstractParameterDto) {
      final AbstractParameterDto other = (AbstractParameterDto) obj;
      equals = Objects.equals(getName(), other.getName())
          && Objects.equals(getDescription(), other.getDescription())
          && Objects.equals(getType(), other.getType())
          && Objects.equals(getValue(), other.getValue());
    }

    return equals;
  }

  @Override
  public int compareTo(AbstractGenericDto other) {
    final int compare;

    if (other instanceof AbstractParameterDto) {
      compare = DEFAULT_ABSTRACT_PARAMETER_DTO_COMPARATOR.compare(this, other);
    } else {
      compare = super.compareTo(other);
    }

    return compare;
  }

}
