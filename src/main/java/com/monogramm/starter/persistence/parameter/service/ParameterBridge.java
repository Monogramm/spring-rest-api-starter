/*
 * Creation by madmath03 the 2018-01-09.
 */

package com.monogramm.starter.persistence.parameter.service;

import com.monogramm.starter.dto.parameter.ParameterDto;
import com.monogramm.starter.persistence.AbstractParameterBridge;
import com.monogramm.starter.persistence.parameter.entity.Parameter;
import com.monogramm.starter.persistence.user.dao.UserRepository;

/**
 * ParameterBridge.
 * 
 * @author madmath03
 */
public class ParameterBridge extends AbstractParameterBridge<Parameter, ParameterDto> {

  /**
   * Create a {@link ParameterBridge}.
   * 
   */
  public ParameterBridge() {
    super();
  }

  /**
   * Create a {@link ParameterBridge}.
   * 
   * @param userRepository repository to lookup users.
   */
  public ParameterBridge(UserRepository userRepository) {
    super(userRepository);
  }

  @Override
  protected Parameter buildEntity() {
    return new Parameter();
  }

  @Override
  protected ParameterDto buildDto() {
    return new ParameterDto();
  }

}
