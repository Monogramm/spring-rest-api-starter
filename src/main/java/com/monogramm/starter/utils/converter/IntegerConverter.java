/*
 * Creation by madmath03 the 2019-01-06.
 */

package com.monogramm.starter.utils.converter;

import javax.persistence.AttributeConverter;

/**
 * Integer Converter.
 * 
 * @author madmath03
 */
public class IntegerConverter implements AttributeConverter<Integer, String> {

  public static class IntegerExporter implements ParameterExporter {

    @Override
    public String apply(Object object) {
      return object instanceof Integer ? ((Integer) object).toString() : null;
    }

  }

  public static class IntegerImporter implements ParameterImporter<Integer> {

    @Override
    public Integer apply(String value) {
      return value != null ? Integer.valueOf(value) : null;
    }

  }

  private final IntegerExporter exporter;

  private final IntegerImporter importer;

  /**
   * Create a {@link IntegerConverter}.
   * 
   */
  public IntegerConverter() {
    this.exporter = new IntegerExporter();
    this.importer = new IntegerImporter();
  }

  @Override
  public String convertToDatabaseColumn(Integer attribute) {
    return this.exporter.apply(attribute);
  }

  @Override
  public Integer convertToEntityAttribute(String dbData) {
    return this.importer.apply(dbData);
  }

}
