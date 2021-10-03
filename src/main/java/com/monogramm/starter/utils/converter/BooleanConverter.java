/*
 * Creation by madmath03 the 2019-01-06.
 */

package com.monogramm.starter.utils.converter;

import javax.persistence.AttributeConverter;

/**
 * Boolean Converter.
 * 
 * @author madmath03
 */
public class BooleanConverter implements AttributeConverter<Boolean, String> {

  public static class BooleanExporter implements ParameterExporter {

    @Override
    public String apply(Object object) {
      return object instanceof Boolean ? ((Boolean) object).toString() : null;
    }

  }

  public static class BooleanImporter implements ParameterImporter<Boolean> {

    @Override
    public Boolean apply(String value) {
      return value != null ? Boolean.valueOf(value) : null;
    }

  }

  private final BooleanExporter exporter;

  private final BooleanImporter importer;

  /**
   * Create a {@link BooleanConverter}.
   * 
   */
  public BooleanConverter() {
    this.exporter = new BooleanExporter();
    this.importer = new BooleanImporter();
  }

  @Override
  public String convertToDatabaseColumn(Boolean attribute) {
    return this.exporter.apply(attribute);
  }

  @Override
  public Boolean convertToEntityAttribute(String dbData) {
    return this.importer.apply(dbData);
  }

}
