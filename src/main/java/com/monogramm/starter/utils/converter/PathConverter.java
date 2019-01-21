/*
 * Creation by madmath03 the 2019-01-06.
 */

package com.monogramm.starter.utils.converter;

import java.nio.file.Path;
import java.nio.file.Paths;

import javax.persistence.AttributeConverter;

/**
 * Path Converter.
 * 
 * @author madmath03
 */
public class PathConverter implements AttributeConverter<Path, String> {

  public static class PathExporter implements ParameterExporter {

    @Override
    public String apply(Object object) {
      return object instanceof Path ? ((Path) object).toString() : null;
    }

  }

  public static class PathImporter implements ParameterImporter<Path> {

    @Override
    public Path apply(String value) {
      return value != null ? Paths.get(value) : null;
    }

  }

  private final PathExporter exporter;

  private final PathImporter importer;

  /**
   * Create a {@link PathConverter}.
   * 
   */
  public PathConverter() {
    this.exporter = new PathExporter();
    this.importer = new PathImporter();
  }

  @Override
  public String convertToDatabaseColumn(Path attribute) {
    return this.exporter.apply(attribute);
  }

  @Override
  public Path convertToEntityAttribute(String dbData) {
    return this.importer.apply(dbData);
  }

}
