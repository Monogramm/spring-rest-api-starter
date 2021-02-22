/*
 * Creation by madmath03 the 2019-01-06.
 */

package com.monogramm.starter.utils.converter;

import com.monogramm.starter.persistence.AbstractParameter;

import java.util.function.Function;

/**
 * A functional interface for exporting an object to a string representation.
 * 
 * @see ParameterImporter
 * @see AbstractParameter#setValue(String)
 * 
 * @author madmath03
 */
public interface ParameterExporter extends Function<Object, String> {

  /**
   * Export an object to a string representation.
   * 
   * @see ParameterImporter#apply(String)
   * 
   * @param object object to export to a string.
   * 
   * @return the object as a string.
   */
  @Override
  String apply(final Object object);

}
