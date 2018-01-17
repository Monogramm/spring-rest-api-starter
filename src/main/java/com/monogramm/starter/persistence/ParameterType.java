package com.monogramm.starter.persistence;

import com.monogramm.starter.utils.JsonUtils;

import java.awt.Color;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Parameter Types and their matching object.
 * 
 * @see AbstractParameter
 * 
 * @author madmath03
 */
public enum ParameterType {
  /**
   * An URL parameter.
   */
  URL(URL.class, object -> object.toString(), value -> {
    URL url;
    try {
      url = new URL(value);
    } catch (MalformedURLException e) {
      url = null;
    }
    return url;
  }),
  /**
   * A Path parameter.
   */
  PATH(Path.class, object -> object.toString(), Paths::get),
  /**
   * A color parameter.
   */
  COLOR(Color.class, object -> object.toString(), Color::decode),
  /**
   * A Date and Time parameter.
   */
  DATE_TIME(LocalDateTime.class, object -> object.toString(), LocalDateTime::parse),
  /**
   * A Time parameter.
   */
  TIME(LocalTime.class, object -> object.toString(), LocalTime::parse),
  /**
   * A Date parameter.
   */
  DATE(LocalDate.class, object -> object.toString(), LocalDate::parse),
  /**
   * A double parameter.
   */
  DOUBLE(Double.class, object -> object.toString(), Double::valueOf),
  /**
   * An integer parameter.
   */
  INTEGER(Integer.class, object -> object.toString(), Integer::valueOf),
  /**
   * An integer parameter.
   */
  BOOLEAN(Boolean.class, object -> object.toString(), Boolean::valueOf),
  /**
   * A String parameter.
   */
  STRING(String.class, object -> object.toString(), String::toString),
  /**
   * A parameter whose value can be anything.
   * 
   * <p>
   * Provides JSON serialization of objects but no deserialization.
   * </p>
   */
  ANY(Object.class, JsonUtils::json, value -> {
    throw new UnsupportedOperationException();
  });


  /**
   * A functional interface for exporting an object representation of a parameter to a string
   * representation.
   * 
   * @see AbstractParameter#setValue(String)
   * 
   * @author madmath03
   */
  public interface ParameterExporter {

    /**
     * Export an object to a string representation.
     * 
     * @see ParameterImporter#run(String)
     * 
     * @param object object to export to a string.
     * 
     * @return the object as a string.
     */
    String run(final Object object);
  }


  /**
   * A functional interface for importing a string representation of a parameter to an object
   * representation.
   * 
   * @see AbstractParameter#getValue()
   * 
   * @author madmath03
   */
  public interface ParameterImporter<T> {

    /**
     * Import an object from a string representation.
     * 
     * @see ParameterExporter#run(Object)
     * 
     * @param value value to import as an object.
     * 
     * @return the value as an object.
     */
    T run(final String value);
  }

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
    return this.exporter.run(object);
  }

  /**
   * Read an object from a string representation.
   * 
   * @param value value to import as an object.
   * 
   * @return the value as an object.
   */
  public Object read(String value) {
    return this.importer.run(value);
  }

}
