/*
 * Creation by madmath03 the 2019-01-06.
 */

package com.monogramm.starter.utils.converter;

import java.net.MalformedURLException;
import java.net.URL;

import javax.persistence.AttributeConverter;

/**
 * URL Converter.
 * 
 * @author madmath03
 */
public class UrlConverter implements AttributeConverter<URL, String> {

  public static class UrlExporter implements ParameterExporter {

    @Override
    public String apply(Object object) {
      return object instanceof URL ? ((URL) object).toString() : null;
    }

  }

  public static class UrlImporter implements ParameterImporter<URL> {

    @Override
    public URL apply(String value) {
      URL url;
      try {
        url = new URL(value);
      } catch (MalformedURLException e) {
        url = null;
      }
      return url;
    }

  }

  private final UrlExporter exporter;

  private final UrlImporter importer;

  /**
   * Create a {@link UrlConverter}.
   * 
   */
  public UrlConverter() {
    this.exporter = new UrlExporter();
    this.importer = new UrlImporter();
  }

  @Override
  public String convertToDatabaseColumn(URL attribute) {
    return this.exporter.apply(attribute);
  }

  @Override
  public URL convertToEntityAttribute(String dbData) {
    return this.importer.apply(dbData);
  }

}
