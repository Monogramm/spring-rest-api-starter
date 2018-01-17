/*
 * Creation by madmath03 the 2018-01-09.
 */

package com.monogramm.starter.persistence.parameter.entity;

import static org.junit.Assert.assertNotNull;

import com.monogramm.starter.persistence.AbstractParameterTest;

import org.junit.Test;

/**
 * {@link Parameter} Unit Test.
 * 
 * @author madmath03
 */
public class ParameterTest extends AbstractParameterTest<Parameter> {

  /**
   * Test method for {@link Parameter#builder()}.
   */
  @Test
  public void testBuilder() {
    assertNotNull(Parameter.builder());
  }

  /**
   * Test method for {@link Parameter#builder(java.lang.String, java.lang.String)}.
   */
  @Test
  public void testBuilderStringString() {
    assertNotNull(Parameter.builder("test", "42"));
  }

  @Override
  protected Parameter buildTestEntity(String name, Object value) {
    return new Parameter(name, value);
  }

  @Override
  protected Parameter buildTestEntity(Parameter other) {
    return new Parameter(other);
  }

  @Override
  protected Parameter buildTestEntity() {
    return new Parameter();
  }

}
