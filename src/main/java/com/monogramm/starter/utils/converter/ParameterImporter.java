/*
 * Creation by madmath03 the 2019-01-06.
 */

package com.monogramm.starter.utils.converter;

import com.monogramm.starter.persistence.AbstractParameter;

import java.util.function.Function;

/**
 * A functional interface for importing a string representation of a parameter to an object.
 * 
 * @see ParameterExporter
 * @see AbstractParameter#getValue()
 * 
 * @param <T> target data type
 * 
 * @author madmath03
 */
public interface ParameterImporter<T> extends Function<String, T> {

  /**
   * Import an object from a string representation.
   * 
   * @see ParameterExporter#apply(Object)
   * 
   * @param value value to import as an object.
   * 
   * @return the value as an object.
   */
  @Override
  T apply(final String value);

}
