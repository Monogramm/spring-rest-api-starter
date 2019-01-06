/*
 * Creation by madmath03 the 2017-12-17.
 */

package com.monogramm.starter.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.awt.Color;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.Test;

/**
 * {@link AbstractParameter} Unit Test.
 * 
 * @author madmath03
 */
public abstract class AbstractParameterTest<T extends AbstractParameter>
    extends AbstractGenericEntityTest<T> {

  protected static final String DUMMY_NAME = "Foo";
  protected static final String DUMMY_DESCRIPTION = "Foo";
  protected static final Object DUMMY_VALUE = 42;

  /**
   * Build an entity to test.
   * 
   * @param name the name of your record being built.
   * @param type the type of your record being built.
   * @param value the value of your record being built.
   * 
   * @return an entity for tests.
   */
  abstract protected T buildTestEntity(final String name, ParameterType type, final Object value);

  /**
   * Build an entity to test.
   * 
   * @param name the name of your record being built.
   * @param value the value of your record being built.
   * 
   * @return an entity for tests.
   */
  abstract protected T buildTestEntity(final String name, final Object value);

  /**
   * Build a copy of an entity to test.
   * 
   * @param other the other {@link AbstractParameter} to copy.
   * 
   * @return an entity for tests.
   */
  abstract protected T buildTestEntity(T other);

  @Override
  public void testToJson() throws JsonProcessingException {
    super.testToJson();

    this.getEntity().setName(DUMMY_NAME);
    assertNotNull(this.getEntity().toJson());

    this.getEntity().setDescription(DUMMY_DESCRIPTION);
    assertNotNull(this.getEntity().toJson());

    this.getEntity().writeValue(DUMMY_VALUE);
    assertNotNull(this.getEntity().toJson());
  }

  @Override
  public void testToString() {
    super.testToString();

    this.getEntity().setName(DUMMY_NAME);
    assertNotNull(this.getEntity().toString());

    this.getEntity().setDescription(DUMMY_DESCRIPTION);
    assertNotNull(this.getEntity().toString());

    this.getEntity().writeValue(DUMMY_VALUE);
    assertNotNull(this.getEntity().toString());
  }

  @Override
  public void testHashCode() {
    assertEquals(this.getEntity().hashCode(), this.getEntity().hashCode());

    final T copy = this.buildTestEntity(this.getEntity());

    assertEquals(copy.hashCode(), this.getEntity().hashCode());

    copy.setName(DUMMY_NAME);

    assertNotEquals(copy.hashCode(), this.getEntity().hashCode());

    copy.setName(null);

    assertEquals(copy.hashCode(), this.getEntity().hashCode());

    copy.setDescription(DUMMY_DESCRIPTION);

    assertNotEquals(copy.hashCode(), this.getEntity().hashCode());

    copy.setDescription(null);

    assertEquals(copy.hashCode(), this.getEntity().hashCode());

    copy.writeValue(DUMMY_VALUE);

    assertNotEquals(copy.hashCode(), this.getEntity().hashCode());

    copy.setValue(null);

    assertNotEquals(copy.hashCode(), this.getEntity().hashCode());

    copy.setType(ParameterType.ANY);

    assertEquals(copy.hashCode(), this.getEntity().hashCode());

    copy.setType(null);

    assertNotEquals(copy.hashCode(), this.getEntity().hashCode());
  }

  @Override
  public void testEqualsObject() {
    assertEquals(this.getEntity(), this.getEntity());
    assertNotEquals(this.getEntity(), null);
    assertNotEquals(this.getEntity(), new Object());


    final T anotherEntity = this.buildTestEntity();

    assertEquals(this.getEntity(), anotherEntity);
    assertEquals(anotherEntity, this.getEntity());


    final T copy = this.buildTestEntity(this.getEntity());

    assertEquals(copy, this.getEntity());

    copy.setName(DUMMY_NAME);

    assertNotEquals(copy, this.getEntity());

    copy.setName(null);

    assertEquals(copy, this.getEntity());

    copy.setDescription(DUMMY_DESCRIPTION);

    assertNotEquals(copy, this.getEntity());

    copy.setDescription(null);

    assertEquals(copy, this.getEntity());

    copy.writeValue(DUMMY_VALUE);

    assertNotEquals(copy, this.getEntity());

    copy.setValue(null);

    assertNotEquals(copy, this.getEntity());

    copy.setType(ParameterType.ANY);

    assertEquals(copy, this.getEntity());

    copy.setType(null);

    assertNotEquals(copy, this.getEntity());
  }

  /**
   * Test method for {@link AbstractParameter#AbstractParameter()}.
   */
  @Test
  public void testAbstractParameter() {
    final AbstractParameter parameter = this.buildTestEntity();

    assertNull(parameter.getName());
    assertNull(parameter.getDescription());
    assertEquals(ParameterType.ANY, parameter.getType());
    assertNull(parameter.getValue());
  }

  /**
   * Test method for {@link AbstractParameter#AbstractParameter(java.lang.String, Object)}.
   */
  @Test
  public void testAbstractParameterStringDate() {
    final AbstractParameter parameter = this.buildTestEntity(DUMMY_NAME, DUMMY_VALUE);

    assertEquals(DUMMY_NAME, parameter.getName());
    assertNull(parameter.getDescription());
    assertEquals(ParameterType.INTEGER, parameter.getType());
    assertEquals(DUMMY_VALUE.toString(), parameter.getValue());
  }

  /**
   * Test method for {@link AbstractParameter#AbstractParameter(java.lang.String, java.util.Date)}.
   */
  @Test
  public void testAbstractParameterNullNull() {
    final AbstractParameter parameter = this.buildTestEntity(null, null);

    assertNull(parameter.getName());
    assertNull(parameter.getDescription());
    assertEquals(ParameterType.ANY, parameter.getType());
    assertNull(parameter.getValue());
  }

  /**
   * Test method for {@link AbstractParameter#AbstractParameter(String, ParameterType, Object)}.
   */
  @Test
  public void testAbstractParameterStringParameterTypeDate() {
    final AbstractParameter parameter =
        this.buildTestEntity(DUMMY_NAME, ParameterType.INTEGER, DUMMY_VALUE);

    assertEquals(DUMMY_NAME, parameter.getName());
    assertNull(parameter.getDescription());
    assertEquals(ParameterType.INTEGER, parameter.getType());
    assertEquals(DUMMY_VALUE.toString(), parameter.getValue());
  }

  /**
   * Test method for {@link AbstractParameter#AbstractParameter(AbstractParameter)}.
   */
  @Test
  public void testAbstractParameterAbstractParameter() {
    final AbstractParameter test = this.buildTestEntity(this.getEntity());

    assertNotNull(test);
    assertEquals(test, this.getEntity());
  }

  /**
   * Test method for {@link AbstractParameter#update(AbstractParameter)}.
   */
  @Test
  public void testUpdateAbstractParameter() {
    final AbstractParameter anotherEntity = this.buildTestEntity();

    this.getEntity().update(anotherEntity);

    assertNotSame(this.getEntity(), anotherEntity);

    assertNull(this.getEntity().getName());
    assertEquals(this.getEntity().getName(), anotherEntity.getName());
    assertNull(this.getEntity().getDescription());
    assertEquals(this.getEntity().getDescription(), anotherEntity.getDescription());
    assertNotNull(this.getEntity().getType());
    assertEquals(this.getEntity().getType(), anotherEntity.getType());
    assertNull(this.getEntity().getValue());
    assertEquals(this.getEntity().getValue(), anotherEntity.getValue());
  }

  /**
   * Test method for {@link AbstractParameter#getName()}.
   */
  @Test
  public void testGetName() {
    assertNull(this.getEntity().getName());
  }

  /**
   * Test method for {@link AbstractParameter#setName(java.lang.String)}.
   */
  @Test
  public void testSetName() {
    final String test = "TEST";
    this.getEntity().setName(test);
    assertEquals(test, this.getEntity().getName());
  }

  /**
   * Test method for {@link AbstractParameter#getDescription()}.
   */
  @Test
  public void testGetDescription() {
    assertNull(this.getEntity().getDescription());
  }

  /**
   * Test method for {@link AbstractParameter#setDescription(String)}.
   */
  @Test
  public void testSetDescription() {
    final String test = "TEST";
    this.getEntity().setDescription(test);
    assertEquals(test, this.getEntity().getDescription());
  }

  /**
   * Test method for {@link AbstractParameter#getType()}.
   */
  @Test
  public void testGetType() {
    assertNotNull(this.getEntity().getType());
  }

  /**
   * Test method for {@link AbstractParameter#setType(ParameterType)}.
   */
  @Test
  public void testSetType() {
    final ParameterType test = ParameterType.typeOf(DUMMY_VALUE);
    this.getEntity().setType(test);
    assertEquals(test, this.getEntity().getType());
  }

  /**
   * Test method for {@link AbstractParameter#getValue()}.
   */
  @Test
  public void testGetValue() {
    assertNull(this.getEntity().getValue());
  }

  /**
   * Test method for {@link AbstractParameter#setValue(java.lang.String)}.
   * 
   * @throws MalformedURLException
   */
  @Test
  public void testSetValueURL() throws MalformedURLException {
    final String test = "https://uss.enterprise.com";
    this.getEntity().setValue(test);
    assertEquals(test, this.getEntity().getValue());
  }

  /**
   * Test method for {@link AbstractParameter#setValue(java.lang.String)}.
   */
  @Test
  public void testSetValuePath() {
    final String test = ".";
    this.getEntity().setValue(test);
    assertEquals(test, this.getEntity().getValue());
  }

  /**
   * Test method for {@link AbstractParameter#setValue(java.lang.String)}.
   */
  @Test
  public void testSetValueColor() {
    final String test = "#000000";
    this.getEntity().setValue(test);
    assertEquals(test, this.getEntity().getValue());
  }

  /**
   * Test method for {@link AbstractParameter#setValue(java.lang.String)}.
   */
  @Test
  public void testSetValueDateTime() {
    final String test = "2007-12-03T10:15:30";
    this.getEntity().setValue(test);
    assertEquals(test, this.getEntity().getValue());
  }

  /**
   * Test method for {@link AbstractParameter#setValue(java.lang.String)}.
   */
  @Test
  public void testSetValueTime() {
    final String test = "10:15";
    this.getEntity().setValue(test);
    assertEquals(test, this.getEntity().getValue());
  }

  /**
   * Test method for {@link AbstractParameter#setValue(java.lang.String)}.
   */
  @Test
  public void testSetValueDate() {
    final String test = "2007-12-03";
    this.getEntity().setValue(test);
    assertEquals(test, this.getEntity().getValue());
  }

  /**
   * Test method for {@link AbstractParameter#setValue(Object)}.
   */
  @Test
  public void testSetValueDouble() {
    final String test = "42.0";
    this.getEntity().setValue(test);
    assertEquals(test.toString(), this.getEntity().getValue());
  }

  /**
   * Test method for {@link AbstractParameter#setValue(Object)}.
   */
  @Test
  public void testSetValueInteger() {
    final String test = "42";
    this.getEntity().setValue(test);
    assertEquals(test.toString(), this.getEntity().getValue());
  }

  /**
   * Test method for {@link AbstractParameter#setValue(Object)}.
   */
  @Test
  public void testSetValueBoolean() {
    final String test = "true";
    this.getEntity().setValue(test);
    assertEquals(test.toString(), this.getEntity().getValue());
  }

  /**
   * Test method for {@link AbstractParameter#setValue(Object)}.
   */
  @Test
  public void testSetValueString() {
    final String test = "Say \"hello\" and enter";
    this.getEntity().setValue(test);
    assertEquals(test.toString(), this.getEntity().getValue());
  }

  /**
   * Test method for {@link AbstractParameter#setValue(Object)}.
   */
  @Test
  public void testSetValueObject() {
    final String test = "{}";
    this.getEntity().setValue(test);
    assertEquals(test.toString(), this.getEntity().getValue());
  }

  /**
   * Test method for {@link AbstractParameter#readValue()}.
   * 
   * @throws MalformedURLException
   */
  @Test
  public void testReadValueURL() throws MalformedURLException {
    final String value = "https://uss.enterprise.com";
    final URL test = new URL(value);
    this.getEntity().setType(ParameterType.URL);
    this.getEntity().setValue(value);
    assertEquals(test, this.getEntity().readValue());
  }

  /**
   * Test method for {@link AbstractParameter#readValue()}.
   */
  @Test
  public void testReadValuePath() {
    final String value = ".";
    final Path test = Paths.get(value);
    this.getEntity().setType(ParameterType.PATH);
    this.getEntity().setValue(value);
    assertEquals(test, this.getEntity().readValue());
  }

  /**
   * Test method for {@link AbstractParameter#readValue()}.
   */
  @Test
  public void testReadValueColor() {
    final String value = "#ff0000";
    final Color test = Color.RED;
    this.getEntity().setType(ParameterType.COLOR);
    this.getEntity().setValue(value);
    assertEquals(test, this.getEntity().readValue());
  }

  /**
   * Test method for {@link AbstractParameter#readValue()}.
   */
  @Test
  public void testReadValueDateTime() {
    final LocalDateTime test = LocalDateTime.now();
    final String value = test.toString();
    this.getEntity().setType(ParameterType.DATE_TIME);
    this.getEntity().setValue(value);
    assertEquals(test, this.getEntity().readValue());
  }

  /**
   * Test method for {@link AbstractParameter#readValue()}.
   */
  @Test
  public void testReadValueTime() {
    final LocalTime test = LocalTime.now();
    final String value = test.toString();
    this.getEntity().setType(ParameterType.TIME);
    this.getEntity().setValue(value);
    assertEquals(test, this.getEntity().readValue());
  }

  /**
   * Test method for {@link AbstractParameter#readValue()}.
   */
  @Test
  public void testReadValueDate() {
    final LocalDate test = LocalDate.now();
    final String value = test.toString();
    this.getEntity().setType(ParameterType.DATE);
    this.getEntity().setValue(value);
    assertEquals(test, this.getEntity().readValue());
  }

  /**
   * Test method for {@link AbstractParameter#readValue()}.
   */
  @Test
  public void testReadValueDouble() {
    final String value = "42.0";
    final Object test = 42.0;
    this.getEntity().setType(ParameterType.DOUBLE);
    this.getEntity().setValue(value);
    assertEquals(test, this.getEntity().readValue());
  }

  /**
   * Test method for {@link AbstractParameter#readValue()}.
   */
  @Test
  public void testReadValueInteger() {
    final String value = "42";
    final Object test = 42;
    this.getEntity().setType(ParameterType.INTEGER);
    this.getEntity().setValue(value);
    assertEquals(test, this.getEntity().readValue());
  }

  /**
   * Test method for {@link AbstractParameter#readValue()}.
   */
  @Test
  public void testReadValueBoolean() {
    final String value = "true";
    final Object test = true;
    this.getEntity().setType(ParameterType.BOOLEAN);
    this.getEntity().setValue(value);
    assertEquals(test, this.getEntity().readValue());
  }

  /**
   * Test method for {@link AbstractParameter#readValue()}.
   */
  @Test
  public void testReadValueString() {
    final String test = "Say \"hello\" and enter";
    this.getEntity().setType(ParameterType.STRING);
    this.getEntity().setValue(test);
    assertEquals(test, this.getEntity().getValue());
  }

  /**
   * Test method for {@link AbstractParameter#readValue()}.
   */
  @Test(expected = UnsupportedOperationException.class)
  public void testReadValueAny() {
    this.getEntity().setType(ParameterType.ANY);
    this.getEntity().readValue();
  }

  /**
   * Test method for {@link AbstractParameter#writeValue(Object)}.
   * 
   * @throws MalformedURLException
   */
  @Test
  public void testWriteValueURL() throws MalformedURLException {
    final String value = "https://uss.enterprise.com";
    final URL test = new URL(value);
    this.getEntity().writeValue(test);
    assertEquals(ParameterType.URL, this.getEntity().getType());
    assertEquals(value, this.getEntity().getValue());

    this.getEntity().writeValue(null);
    assertNull(this.getEntity().getValue());
  }

  /**
   * Test method for {@link AbstractParameter#writeValue(Object)}.
   */
  @Test
  public void testWriteValuePath() {
    final String value = ".";
    final Path test = Paths.get(value);
    this.getEntity().writeValue(test);
    assertEquals(ParameterType.PATH, this.getEntity().getType());
    assertEquals(value, this.getEntity().getValue());

    this.getEntity().writeValue(null);
    assertNull(this.getEntity().getValue());
  }

  /**
   * Test method for {@link AbstractParameter#writeValue(Object)}.
   */
  @Test
  public void testWriteValueColor() {
    final String value = "#ff0000";
    final Color test = Color.decode(value);
    this.getEntity().writeValue(test);
    assertEquals(ParameterType.COLOR, this.getEntity().getType());
    assertEquals("#ff0000", this.getEntity().getValue());

    this.getEntity().writeValue(null);
    assertNull(this.getEntity().getValue());
  }

  /**
   * Test method for {@link AbstractParameter#writeValue(Object)}.
   */
  @Test
  public void testWriteValueDateTime() {
    final LocalDateTime test = LocalDateTime.now();
    this.getEntity().writeValue(test);
    assertEquals(ParameterType.DATE_TIME, this.getEntity().getType());
    assertEquals(test.toString(), this.getEntity().getValue());

    this.getEntity().writeValue(null);
    assertNull(this.getEntity().getValue());
  }

  /**
   * Test method for {@link AbstractParameter#writeValue(Object)}.
   */
  @Test
  public void testWriteValueTime() {
    final LocalTime test = LocalTime.now();
    this.getEntity().writeValue(test);
    assertEquals(ParameterType.TIME, this.getEntity().getType());
    assertEquals(test.toString(), this.getEntity().getValue());

    this.getEntity().writeValue(null);
    assertNull(this.getEntity().getValue());
  }

  /**
   * Test method for {@link AbstractParameter#writeValue(Object)}.
   */
  @Test
  public void testWriteValueDate() {
    final LocalDate test = LocalDate.now();
    this.getEntity().writeValue(test);
    assertEquals(ParameterType.DATE, this.getEntity().getType());
    assertEquals(test.toString(), this.getEntity().getValue());

    this.getEntity().writeValue(null);
    assertNull(this.getEntity().getValue());
  }

  /**
   * Test method for {@link AbstractParameter#writeValue(Object)}.
   */
  @Test
  public void testWriteValueDouble() {
    final String value = "42.0";
    final Object test = 42.0;
    this.getEntity().writeValue(test);
    assertEquals(ParameterType.DOUBLE, this.getEntity().getType());
    assertEquals(value, this.getEntity().getValue());

    this.getEntity().writeValue(null);
    assertNull(this.getEntity().getValue());
  }

  /**
   * Test method for {@link AbstractParameter#writeValue(Object)}.
   */
  @Test
  public void testWriteValueInteger() {
    final String value = "42";
    final Object test = 42;
    this.getEntity().writeValue(test);
    assertEquals(ParameterType.INTEGER, this.getEntity().getType());
    assertEquals(value, this.getEntity().getValue());

    this.getEntity().writeValue(null);
    assertNull(this.getEntity().getValue());
  }

  /**
   * Test method for {@link AbstractParameter#writeValue(Object)}.
   */
  @Test
  public void testWriteValueBoolean() {
    final String value = "true";
    final Object test = true;
    this.getEntity().writeValue(test);
    assertEquals(ParameterType.BOOLEAN, this.getEntity().getType());
    assertEquals(value, this.getEntity().getValue());

    this.getEntity().writeValue(null);
    assertNull(this.getEntity().getValue());
  }

  /**
   * Test method for {@link AbstractParameter#writeValue(Object)}.
   */
  @Test
  public void testWriteValueString() {
    final Object test = "Say \"hello\" and enter";
    this.getEntity().writeValue(test);
    assertEquals(ParameterType.STRING, this.getEntity().getType());
    assertEquals(test.toString(), this.getEntity().getValue());

    this.getEntity().writeValue(null);
    assertNull(this.getEntity().getValue());
  }

  /**
   * Test method for {@link AbstractParameter#writeValue(Object)}.
   */
  @Test
  public void testWriteValueObject() {
    final String value = "\"dummy\" : \"hello there\"";
    final Object test = new Object() {
      public String dummy = "hello there";
    };
    this.getEntity().writeValue(test);
    assertEquals(ParameterType.ANY, this.getEntity().getType());
    assertNotNull(this.getEntity().getValue());
    assertTrue(this.getEntity().getValue().startsWith("{"));
    assertTrue(this.getEntity().getValue().contains(value));
    assertTrue(this.getEntity().getValue().endsWith("}"));

    this.getEntity().writeValue(null);
    assertNull(this.getEntity().getValue());
  }

}
