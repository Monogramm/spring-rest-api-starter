/*
 * Creation by madmath03 the 2019-01-06.
 */

package com.monogramm.starter.utils.converter;

import java.time.LocalDate;

import javax.persistence.AttributeConverter;

/**
 * Date Converter.
 * 
 * @author madmath03
 */
public class DateConverter implements AttributeConverter<LocalDate, String> {

  public static class DateExporter implements ParameterExporter {

    @Override
    public String apply(Object object) {
      return object instanceof LocalDate ? ((LocalDate) object).toString() : null;
    }

  }

  public static class DateImporter implements ParameterImporter<LocalDate> {

    @Override
    public LocalDate apply(String value) {
      return value != null ? LocalDate.parse(value) : null;
    }

  }

  private final DateExporter exporter;

  private final DateImporter importer;

  /**
   * Create a {@link DateConverter}.
   * 
   */
  public DateConverter() {
    this.exporter = new DateExporter();
    this.importer = new DateImporter();
  }

  @Override
  public String convertToDatabaseColumn(LocalDate attribute) {
    return this.exporter.apply(attribute);
  }

  @Override
  public LocalDate convertToEntityAttribute(String dbData) {
    return this.importer.apply(dbData);
  }

}
