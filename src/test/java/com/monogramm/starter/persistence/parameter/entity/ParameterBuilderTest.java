/*
 * Creation by madmath03 the 2017-08-27.
 */

package com.monogramm.starter.persistence.parameter.entity;

import com.monogramm.starter.persistence.AbstractParameterBuilderTest;
import com.monogramm.starter.persistence.parameter.entity.Parameter.ParameterBuilder;

/**
 * {@link ParameterBuilder} Unit Test.
 * 
 * @author madmath03
 */
public class ParameterBuilderTest extends AbstractParameterBuilderTest<Parameter.ParameterBuilder> {

  @Override
  protected ParameterBuilder buildTestEntityBuilder() {
    return Parameter.builder();
  }

}
