/*
 * Creation by madmath03 the 2018-01-09.
 */

package com.monogramm.starter.dto.parameter;

import com.monogramm.starter.dto.AbstractParameterDtoTest;

/**
 * {@link ParameterDto} Unit Test.
 * 
 * @author madmath03
 */
public class ParameterDtoTest extends AbstractParameterDtoTest<ParameterDto> {

  @Override
  protected ParameterDto buildTestDto() {
    return new ParameterDto();
  }

  @Override
  protected ParameterDto buildTestDto(ParameterDto other) {
    return new ParameterDto(other);
  }

}
