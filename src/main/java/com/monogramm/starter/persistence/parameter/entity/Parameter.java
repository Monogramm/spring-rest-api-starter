/*
 * Creation by madmath03 the 2018-01-09.
 */

package com.monogramm.starter.persistence.parameter.entity;

import com.monogramm.starter.persistence.AbstractParameter;
import com.monogramm.starter.persistence.ParameterType;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Parameter.
 * 
 * @author madmath03
 */
@Entity
@Table(name = "parameter")
public class Parameter extends AbstractParameter {

  /**
   * The {@code serialVersionUID}.
   */
  private static final long serialVersionUID = -1638975400723419031L;

  /**
   * Get a new {@link ParameterBuilder}.
   *
   * @return a new {@link ParameterBuilder}.
   */
  public static ParameterBuilder builder() {
    return new ParameterBuilder();
  }

  /**
   * Get a new {@link ParameterBuilder}.
   * 
   * @param name the name of your record being built.
   * @param value the value of your record being built.
   *
   * @return a new {@link ParameterBuilder}.
   */
  public static ParameterBuilder builder(final String name, final Object value) {
    return new ParameterBuilder(name, value);
  }

  /**
   * Get a new {@link ParameterBuilder}.
   * 
   * @param name the name of your record being built.
   * @param type the type of your record being built.
   * @param value the value of your record being built.
   *
   * @return a new {@link ParameterBuilder}.
   */
  public static ParameterBuilder builder(final String name, final ParameterType type,
      final Object value) {
    return new ParameterBuilder(name, type, value);
  }

  /**
   * Create a {@link Parameter}.
   * 
   */
  public Parameter() {
    super();
  }

  /**
   * Create a copy of a {@link Parameter}.
   * 
   * @param other the other entity to copy.
   * 
   * @throws NullPointerException if the {@code other} entity is @{code null}.
   */
  public Parameter(Parameter other) {
    super(other);
  }

  /**
   * Create a {@link Parameter}.
   * 
   * @param name the parameter name.
   * @param value the parameter value.
   */
  public Parameter(String name, Object value) {
    super(name, value);
  }

  /**
   * Create a {@link Parameter}.
   * 
   * @param name the parameter name.
   * @param type the parameter type.
   * @param value the parameter value.
   */
  public Parameter(String name, ParameterType type, Object value) {
    super(name, type, value);
  }

  /**
   * A functional programming {@link Parameter} builder.
   * 
   * @author madmath03
   */
  public static class ParameterBuilder
      extends AbstractParameter.AbstractParameterBuilder<Parameter> {

    /**
     * Create a {@link ParameterBuilder}.
     * 
     */
    public ParameterBuilder() {
      super();
    }

    /**
     * Create a {@link ParameterBuilder}.
     * 
     * @param name the name of your record being built.
     * @param value the value of your record being built.
     */
    public ParameterBuilder(String name, Object value) {
      super(name, value);
    }

    /**
     * Create a {@link ParameterBuilder}.
     * 
     * @param name the name of your record being built.
     * @param type the type of your record being built.
     * @param value the value of your record being built.
     */
    public ParameterBuilder(String name, ParameterType type, Object value) {
      super(name, type, value);
    }

    @Override
    protected Parameter buildEntity() {
      return new Parameter();
    }

  }

}
