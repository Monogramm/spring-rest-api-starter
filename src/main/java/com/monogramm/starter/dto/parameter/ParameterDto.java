/*
 * Creation by madmath03 the 2018-01-09.
 */

package com.monogramm.starter.dto.parameter;

import com.monogramm.starter.dto.AbstractParameterDto;

/**
 * ParameterDto.
 * 
 * @author madmath03
 */
public class ParameterDto extends AbstractParameterDto {

  /**
   * The {@code serialVersionUID}.
   */
  private static final long serialVersionUID = 5239222855579699591L;

  /**
   * Create a {@link ParameterDto}.
   * 
   */
  public ParameterDto() {
    super();
  }

  /**
   * Create a copy of a {@link ParameterDto}.
   * 
   * @param other the other DTO to copy.
   * 
   * @throws NullPointerException if the other DTO is {@code null}.
   */
  public ParameterDto(ParameterDto other) {
    super(other);
  }

}
