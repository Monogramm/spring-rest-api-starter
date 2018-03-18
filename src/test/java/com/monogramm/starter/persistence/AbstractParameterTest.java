/*
 * Creation by madmath03 the 2017-12-17.
 */

package com.monogramm.starter.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.JsonProcessingException;

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

    this.getEntity().setValue(DUMMY_VALUE);
    assertNotNull(this.getEntity().toJson());
  }

  @Override
  public void testToString() {
    super.testToString();

    this.getEntity().setName(DUMMY_NAME);
    assertNotNull(this.getEntity().toString());

    this.getEntity().setDescription(DUMMY_DESCRIPTION);
    assertNotNull(this.getEntity().toString());

    this.getEntity().setValue(DUMMY_VALUE);
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

    copy.setValue(DUMMY_VALUE);

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

    copy.setValue(DUMMY_VALUE);

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
   */
  @Test
  public void testSetValueString() {
    final String test = "TEST";
    this.getEntity().setValue(test);
    assertEquals(test, this.getEntity().getValue());
  }

  /**
   * Test method for {@link AbstractParameter#setValue(Object)}.
   */
  @Test
  public void testSetValueObject() {
    final Object test = 42.0;
    this.getEntity().setValue(test);
    assertEquals(test.toString(), this.getEntity().getValue());
    assertEquals(ParameterType.DOUBLE, this.getEntity().getType());
  }

}
