/*
 * Creation by madmath03 the 2019-01-06.
 */

package com.monogramm.starter.utils.converter;

import javax.persistence.AttributeConverter;

/**
 * Double Converter.
 * 
 * @author madmath03
 */
public class DoubleConverter implements AttributeConverter<Double, String> {

  public static class DoubleExporter implements ParameterExporter {

    @Override
    public String apply(Object object) {
      return object instanceof Double ? ((Double) object).toString() : null;
    }

  }

  public static class DoubleImporter implements ParameterImporter<Double> {

    @Override
    public Double apply(String value) {
      return value != null ? Double.valueOf(value) : null;
    }

  }

  private final DoubleExporter exporter;

  private final DoubleImporter importer;

  /**
   * Create a {@link DoubleConverter}.
   * 
   */
  public DoubleConverter() {
    this.exporter = new DoubleExporter();
    this.importer = new DoubleImporter();
  }

  @Override
  public String convertToDatabaseColumn(Double attribute) {
    return this.exporter.apply(attribute);
  }

  @Override
  public Double convertToEntityAttribute(String dbData) {
    return this.importer.apply(dbData);
  }

}
