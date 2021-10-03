/*
 * Creation by madmath03 the 2019-01-06.
 */

package com.monogramm.starter.utils.converter;

import java.time.LocalDateTime;

import javax.persistence.AttributeConverter;

/**
 * DateTime Converter.
 * 
 * @author madmath03
 */
public class DateTimeConverter implements AttributeConverter<LocalDateTime, String> {

  public static class DateTimeExporter implements ParameterExporter {

    @Override
    public String apply(Object object) {
      return object instanceof LocalDateTime ? ((LocalDateTime) object).toString() : null;
    }

  }

  public static class DateTimeImporter implements ParameterImporter<LocalDateTime> {

    @Override
    public LocalDateTime apply(String value) {
      return value != null ? LocalDateTime.parse(value) : null;
    }

  }

  private final DateTimeExporter exporter;

  private final DateTimeImporter importer;

  /**
   * Create a {@link DateTimeConverter}.
   * 
   */
  public DateTimeConverter() {
    this.exporter = new DateTimeExporter();
    this.importer = new DateTimeImporter();
  }

  @Override
  public String convertToDatabaseColumn(LocalDateTime attribute) {
    return this.exporter.apply(attribute);
  }

  @Override
  public LocalDateTime convertToEntityAttribute(String dbData) {
    return this.importer.apply(dbData);
  }

}
