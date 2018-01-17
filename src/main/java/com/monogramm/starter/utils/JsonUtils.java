/*
 * Creation by madmath03 the 2017-11-13.
 */

package com.monogramm.starter.utils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * JSON utilities.
 * 
 * @author madmath03
 */
public final class JsonUtils {

  /**
   * Logger for {@link JsonUtils}.
   */
  private static final Logger LOG = LogManager.getLogger(JsonUtils.class);

  /**
   * A static object mapper to parse objects from/to JSON.
   */
  static final ObjectMapper MAPPER = new ObjectMapper();

  /**
   * A static object writer to parse objects to JSON.
   */
  static final ObjectWriter WRITER;

  static {
    MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    WRITER = MAPPER.writer().withDefaultPrettyPrinter();
  }

  /**
   * Returns a JSON string representation of an object.
   * 
   * <p>
   * This method is generally intended to serialize objects during <em>JSON</em> serialization.
   * </p>
   * 
   * @param object the object to convert to a JSON string.
   *
   * @return a JSON string representation of the object.
   */
  public static String json(final Object object) {
    String json;
    try {
      json = JsonUtils.toJson(object);
    } catch (JsonProcessingException e) {
      json = null;
      LOG.error(e);
    }
    return json;
  }

  /**
   * Returns a JSON string representation of an object.
   * 
   * <p>
   * This method is generally intended to serialize objects during <em>JSON</em> serialization.
   * </p>
   * 
   * @param object the object to convert to a JSON string.
   *
   * @return a JSON string representation of the object.
   * 
   * @throws JsonProcessingException if the object could not be serialized to JSON.
   */
  public static String toJson(final Object object) throws JsonProcessingException {
    return JsonUtils.WRITER.writeValueAsString(object);
  }

  /**
   * Returns an object based on a JSON byte Array representation.
   * 
   * <p>
   * This method is generally intended to deserialize objects from a <em>JSON</em> serialization.
   * </p>
   * 
   * @param <T> the type of the returned object.
   * @param sourceArray the JSON source byte Array.
   * @param valueType the class type of the returned object.
   *
   * @return an object based on the JSON representation.
   * 
   * @throws IOException if a low-level I/O problem (unexpected end-of-input, network error) occurs
   *         (passed through as-is without additional wrapping -- note that this is one case where
   *         {@link DeserializationFeature#WRAP_EXCEPTIONS} does NOT result in wrapping of exception
   *         even if enabled)
   * @throws JsonParseException if underlying input contains invalid content of type
   *         {@link JsonParser} supports (JSON for default case)
   * @throws JsonMappingException if the input JSON structure does not match structure expected for
   *         result type (or has other mismatch issues)
   */
  public static <T> T fromJson(final byte[] sourceArray, final Class<T> valueType)
      throws IOException {
    return MAPPER.readValue(sourceArray, valueType);
  }

  /**
   * Returns an object based on a JSON string representation.
   * 
   * <p>
   * This method is generally intended to deserialize objects from a <em>JSON</em> serialization.
   * </p>
   * 
   * @param <T> the type of the returned object.
   * @param source the JSON source String.
   * @param valueType the class type of the returned object.
   *
   * @return an object based on the JSON representation.
   * 
   * @throws IOException if a low-level I/O problem (unexpected end-of-input, network error) occurs
   *         (passed through as-is without additional wrapping -- note that this is one case where
   *         {@link DeserializationFeature#WRAP_EXCEPTIONS} does NOT result in wrapping of exception
   *         even if enabled)
   * @throws JsonParseException if underlying input contains invalid content of type
   *         {@link JsonParser} supports (JSON for default case)
   * @throws JsonMappingException if the input JSON structure does not match structure expected for
   *         result type (or has other mismatch issues)
   */
  public static <T> T fromJson(final String source, final Class<T> valueType) throws IOException {
    return MAPPER.readValue(source, valueType);
  }

  /**
   * Returns an object based on a JSON Reader.
   * 
   * <p>
   * This method is generally intended to deserialize objects from a <em>JSON</em> serialization.
   * </p>
   * 
   * @param <T> the type of the returned object.
   * @param sourceReader the JSON source Reader.
   * @param valueType the class type of the returned object.
   *
   * @return an object based on the JSON representation.
   * 
   * @throws IOException if a low-level I/O problem (unexpected end-of-input, network error) occurs
   *         (passed through as-is without additional wrapping -- note that this is one case where
   *         {@link DeserializationFeature#WRAP_EXCEPTIONS} does NOT result in wrapping of exception
   *         even if enabled)
   * @throws JsonParseException if underlying input contains invalid content of type
   *         {@link JsonParser} supports (JSON for default case)
   * @throws JsonMappingException if the input JSON structure does not match structure expected for
   *         result type (or has other mismatch issues)
   */
  public static <T> T fromJson(final Reader sourceReader, final Class<T> valueType)
      throws IOException {
    return MAPPER.readValue(sourceReader, valueType);
  }

  /**
   * Returns an object based on a JSON Input Stream.
   * 
   * <p>
   * This method is generally intended to deserialize objects from a <em>JSON</em> serialization.
   * </p>
   * 
   * @param <T> the type of the returned object.
   * @param sourceStream the JSON source Input Stream.
   * @param valueType the class type of the returned object.
   *
   * @return an object based on the JSON representation.
   * 
   * @throws IOException if a low-level I/O problem (unexpected end-of-input, network error) occurs
   *         (passed through as-is without additional wrapping -- note that this is one case where
   *         {@link DeserializationFeature#WRAP_EXCEPTIONS} does NOT result in wrapping of exception
   *         even if enabled)
   * @throws JsonParseException if underlying input contains invalid content of type
   *         {@link JsonParser} supports (JSON for default case)
   * @throws JsonMappingException if the input JSON structure does not match structure expected for
   *         result type (or has other mismatch issues)
   */
  public static <T> T fromJson(final InputStream sourceStream, final Class<T> valueType)
      throws IOException {
    return MAPPER.readValue(sourceStream, valueType);
  }

  /**
   * Returns an object based on a JSON File.
   * 
   * <p>
   * This method is generally intended to deserialize objects from a <em>JSON</em> serialization.
   * </p>
   * 
   * @param <T> the type of the returned object.
   * @param sourceFile the JSON source File.
   * @param valueType the class type of the returned object.
   *
   * @return an object based on the JSON representation.
   * 
   * @throws IOException if a low-level I/O problem (unexpected end-of-input, network error) occurs
   *         (passed through as-is without additional wrapping -- note that this is one case where
   *         {@link DeserializationFeature#WRAP_EXCEPTIONS} does NOT result in wrapping of exception
   *         even if enabled)
   * @throws JsonParseException if underlying input contains invalid content of type
   *         {@link JsonParser} supports (JSON for default case)
   * @throws JsonMappingException if the input JSON structure does not match structure expected for
   *         result type (or has other mismatch issues)
   */
  public static <T> T fromJson(final File sourceFile, final Class<T> valueType) throws IOException {
    return MAPPER.readValue(sourceFile, valueType);
  }

  /**
   * Hidden creation of a {@link JsonUtils}.
   * 
   */
  private JsonUtils() {
    super();
  }

}
