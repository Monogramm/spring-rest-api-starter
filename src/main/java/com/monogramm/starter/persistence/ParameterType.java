package com.monogramm.starter.persistence;

import com.monogramm.starter.utils.converter.AnyConverter.AnyExporter;
import com.monogramm.starter.utils.converter.AnyConverter.AnyImporter;
import com.monogramm.starter.utils.converter.BooleanConverter.BooleanExporter;
import com.monogramm.starter.utils.converter.BooleanConverter.BooleanImporter;
import com.monogramm.starter.utils.converter.ColorConverter.ColorExporter;
import com.monogramm.starter.utils.converter.ColorConverter.ColorImporter;
import com.monogramm.starter.utils.converter.DateConverter.DateExporter;
import com.monogramm.starter.utils.converter.DateConverter.DateImporter;
import com.monogramm.starter.utils.converter.DateTimeConverter.DateTimeExporter;
import com.monogramm.starter.utils.converter.DateTimeConverter.DateTimeImporter;
import com.monogramm.starter.utils.converter.DoubleConverter.DoubleExporter;
import com.monogramm.starter.utils.converter.DoubleConverter.DoubleImporter;
import com.monogramm.starter.utils.converter.IntegerConverter.IntegerExporter;
import com.monogramm.starter.utils.converter.IntegerConverter.IntegerImporter;
import com.monogramm.starter.utils.converter.ParameterExporter;
import com.monogramm.starter.utils.converter.ParameterImporter;
import com.monogramm.starter.utils.converter.PathConverter.PathExporter;
import com.monogramm.starter.utils.converter.PathConverter.PathImporter;
import com.monogramm.starter.utils.converter.StringConverter.StringExporter;
import com.monogramm.starter.utils.converter.StringConverter.StringImporter;
import com.monogramm.starter.utils.converter.TimeConverter.TimeExporter;
import com.monogramm.starter.utils.converter.TimeConverter.TimeImporter;
import com.monogramm.starter.utils.converter.UrlConverter.UrlExporter;
import com.monogramm.starter.utils.converter.UrlConverter.UrlImporter;

import java.awt.Color;
import java.net.URL;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Parameter Types and their matching object.
 * 
 * <p>
 * The types available here closely matches the types of HTML5 {@code <input>} and is intended to
 * provide a simple way to serialize data between front and back.
 * </p>
 * 
 * @see AbstractParameter
 * 
 * @author madmath03
 */
public enum ParameterType {
  /**
   * An URL parameter.
   */
  URL(URL.class, new UrlExporter(), new UrlImporter()),
  /**
   * A Path parameter.
   */
  PATH(Path.class, new PathExporter(), new PathImporter()),
  /**
   * A color parameter.
   */
  COLOR(Color.class, new ColorExporter(), new ColorImporter()),
  /**
   * A Date and Time parameter.
   */
  DATE_TIME(LocalDateTime.class, new DateTimeExporter(), new DateTimeImporter()),
  /**
   * A Time parameter.
   */
  TIME(LocalTime.class, new TimeExporter(), new TimeImporter()),
  /**
   * A Date parameter.
   */
  DATE(LocalDate.class, new DateExporter(), new DateImporter()),
  /**
   * A double parameter.
   */
  DOUBLE(Double.class, new DoubleExporter(), new DoubleImporter()),
  /**
   * An integer parameter.
   */
  INTEGER(Integer.class, new IntegerExporter(), new IntegerImporter()),
  /**
   * An integer parameter.
   */
  BOOLEAN(Boolean.class, new BooleanExporter(), new BooleanImporter()),
  /**
   * A String parameter.
   */
  STRING(String.class, new StringExporter(), new StringImporter()),
  /**
   * A parameter whose value can be anything.
   * 
   * <p>
   * Provides JSON serialization of objects but no deserialization.
   * </p>
   */
  ANY(Object.class, new AnyExporter(), new AnyImporter());

  /**
   * Get the most appropriate parameter type for an object.
   * 
   * @param object object for which to find the most appropriate {@link ParameterType}.
   * 
   * @return the most appropriate {@link ParameterType} for given object.
   */
  public static ParameterType typeOf(final Object object) {
    ParameterType type = ANY;

    if (object != null) {
      for (ParameterType t : values()) {
        if (t.typeClass.isInstance(object)) {
          type = t;
          break;
        }
      }
    }

    return type;
  }


  private final Class<?> typeClass;

  private final ParameterExporter exporter;

  private final ParameterImporter<?> importer;


  /**
   * Create a {@link ParameterType}.
   * 
   * @param typeClass class associated to this type.
   * @param exporter function responsible of exporting an object to a string value parameter.
   * @param importer function responsible of importing an object from a string value parameter.
   */
  private ParameterType(Class<?> typeClass, ParameterExporter exporter,
      ParameterImporter<?> importer) {
    this.typeClass = typeClass;
    this.exporter = exporter;
    this.importer = importer;
  }


  /**
   * Get the {@link #typeClass}.
   * 
   * @return the {@link #typeClass}.
   */
  public Class<?> getTypeClass() {
    return typeClass;
  }

  /**
   * Write an object to a string representation.
   * 
   * @param object object to export to a string.
   * 
   * @return the object as a string.
   */
  public String write(Object object) {
    return this.exporter.apply(object);
  }

  /**
   * Read an object from a string representation.
   * 
   * @param value value to import as an object.
   * 
   * @return the value as an object.
   */
  public Object read(String value) {
    return this.importer.apply(value);
  }

}
