/*
 * Creation by madmath03 the 2019-01-06.
 */

package com.monogramm.starter.utils.converter;

import java.awt.Color;
import java.awt.Transparency;

import javax.persistence.AttributeConverter;

/**
 * Color Converter.
 * 
 * @author madmath03
 */
public class ColorConverter implements AttributeConverter<Color, String> {

  public static class ColorExporter implements ParameterExporter {

    @Override
    public String apply(Object object) {
      final String value;

      if (object instanceof Color) {
        final Color color = ((Color) object);

        final StringBuilder colorBuilder = new StringBuilder("#");

        componentToString(colorBuilder, color.getRed());
        componentToString(colorBuilder, color.getBlue());
        componentToString(colorBuilder, color.getGreen());

        if (color.getTransparency() != Transparency.OPAQUE) {
          componentToString(colorBuilder, color.getAlpha());
        }

        value = colorBuilder.toString();
      } else {
        value = null;
      }

      return value;
    }

    private void componentToString(final StringBuilder builder, int component) {
      final String comp = Integer.toHexString(component);

      if (comp.length() == 1) {
        builder.append("0");
      }
      builder.append(comp);
    }

  }

  public static class ColorImporter implements ParameterImporter<Color> {

    @Override
    public Color apply(String value) {
      return value != null ? Color.decode(value) : null;
    }

  }

  private final ColorExporter exporter;

  private final ColorImporter importer;

  /**
   * Create a {@link ColorConverter}.
   * 
   */
  public ColorConverter() {
    this.exporter = new ColorExporter();
    this.importer = new ColorImporter();
  }

  @Override
  public String convertToDatabaseColumn(Color attribute) {
    return this.exporter.apply(attribute);
  }

  @Override
  public Color convertToEntityAttribute(String dbData) {
    return this.importer.apply(dbData);
  }

}
