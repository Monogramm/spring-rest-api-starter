/*
 * Creation by madmath03 the 2019-01-06.
 */

package com.monogramm.starter.utils.converter;

import javax.persistence.AttributeConverter;

/**
 * String Converter.
 * 
 * @author madmath03
 */
public class StringConverter implements AttributeConverter<String, String> {

  public static class StringExporter implements ParameterExporter {

    @Override
    public String apply(Object object) {
      return object instanceof String ? ((String) object).toString() : null;
    }

  }

  public static class StringImporter implements ParameterImporter<String> {

    @Override
    public String apply(String value) {
      return value != null ? value.toString() : null;
    }

  }

  private final StringExporter exporter;

  private final StringImporter importer;

  /**
   * Create a {@link StringConverter}.
   * 
   */
  public StringConverter() {
    this.exporter = new StringExporter();
    this.importer = new StringImporter();
  }

  @Override
  public String convertToDatabaseColumn(String attribute) {
    return this.exporter.apply(attribute);
  }

  @Override
  public String convertToEntityAttribute(String dbData) {
    return this.importer.apply(dbData);
  }

}
