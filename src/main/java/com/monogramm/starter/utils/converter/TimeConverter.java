/*
 * Creation by madmath03 the 2019-01-06.
 */

package com.monogramm.starter.utils.converter;

import java.time.LocalTime;

import javax.persistence.AttributeConverter;

/**
 * Time Converter.
 * 
 * @author madmath03
 */
public class TimeConverter implements AttributeConverter<LocalTime, String> {

  public static class TimeExporter implements ParameterExporter {

    @Override
    public String apply(Object object) {
      return object instanceof LocalTime ? ((LocalTime) object).toString() : null;
    }

  }

  public static class TimeImporter implements ParameterImporter<LocalTime> {

    @Override
    public LocalTime apply(String value) {
      return value != null ? LocalTime.parse(value) : null;
    }

  }

  private final TimeExporter exporter;

  private final TimeImporter importer;

  /**
   * Create a {@link TimeConverter}.
   * 
   */
  public TimeConverter() {
    this.exporter = new TimeExporter();
    this.importer = new TimeImporter();
  }

  @Override
  public String convertToDatabaseColumn(LocalTime attribute) {
    return this.exporter.apply(attribute);
  }

  @Override
  public LocalTime convertToEntityAttribute(String dbData) {
    return this.importer.apply(dbData);
  }

}
