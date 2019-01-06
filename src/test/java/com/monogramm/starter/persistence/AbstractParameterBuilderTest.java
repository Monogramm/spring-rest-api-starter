/*
 * Creation by madmath03 the 2018-01-08.
 */

package com.monogramm.starter.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.monogramm.starter.persistence.AbstractParameter.AbstractParameterBuilder;

import org.junit.Test;

/**
 * {@link AbstractParameterBuilder} Unit Test.
 * 
 * @author madmath03
 */
public abstract class AbstractParameterBuilderTest<
    T extends AbstractParameterBuilder<? extends AbstractParameter>>
    extends AbstractGenericEntityBuilderTest<T> {

  /**
   * Test method for {@link AbstractParameterBuilder#name(String)}.
   */
  @Test
  public void testName() {
    T builder = this.buildTestEntityBuilder();

    assertEquals(builder, builder.name(null));

    final String name = "Test";
    assertEquals(name, builder.name(name).build().getName());
  }

  /**
   * Test method for {@link AbstractParameterBuilder#description(String)}.
   */
  @Test
  public void testDescription() {
    T builder = this.buildTestEntityBuilder();

    assertEquals(builder, builder.description(null));

    final String description = "Test";
    assertEquals(description, builder.description(description).build().getDescription());
  }

  /**
   * Test method for {@link AbstractParameterBuilder#type(ParameterType)}.
   */
  @Test
  public void testType() {
    T builder = this.buildTestEntityBuilder();

    assertEquals(builder, builder.name(null));

    final ParameterType type = ParameterType.BOOLEAN;
    assertEquals(type, builder.type(type).build().getType());
  }

  /**
   * Test method for {@link AbstractParameterBuilder#value(Object)}.
   */
  @Test
  public void testValue() {
    T builder = this.buildTestEntityBuilder();

    assertEquals(builder, builder.value(null));
  }

  /**
   * Test method for {@link AbstractParameterBuilder#value(Object)}.
   */
  @Test
  public void testValueNull() {
    T builder = this.buildTestEntityBuilder();

    assertEquals(builder, builder.value(null));

    for (final ParameterType type : ParameterType.values()) {
      builder.type(type);
      assertNull(builder.value(null).build().getValue());
    }
  }

}
