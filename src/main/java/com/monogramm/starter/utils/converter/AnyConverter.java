/*
 * Creation by madmath03 the 2019-01-06.
 */

package com.monogramm.starter.utils.converter;

import com.monogramm.starter.utils.JsonUtils;

import javax.persistence.AttributeConverter;

/**
 * Any Converter.
 * 
 * @author madmath03
 */
public class AnyConverter implements AttributeConverter<Object, String> {

  public static class AnyExporter implements ParameterExporter {

    @Override
    public String apply(Object object) {
      return object != null ? JsonUtils.json(object) : null;
    }

  }

  public static class AnyImporter implements ParameterImporter<Object> {

    @Override
    public Object apply(String value) {
      throw new UnsupportedOperationException("Cannot import to unknown class: " + value);
    }

  }

  private final AnyExporter exporter;

  private final AnyImporter importer;

  /**
   * Create a {@link AnyConverter}.
   * 
   */
  public AnyConverter() {
    this.exporter = new AnyExporter();
    this.importer = new AnyImporter();
  }

  @Override
  public String convertToDatabaseColumn(Object attribute) {
    return this.exporter.apply(attribute);
  }

  @Override
  public Object convertToEntityAttribute(String dbData) {
    return this.importer.apply(dbData);
  }

}
