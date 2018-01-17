/*
 * Creation by madmath03 the 2018-01-09.
 */

package com.monogramm.starter.persistence.parameter.service;

import com.monogramm.starter.dto.parameter.ParameterDto;
import com.monogramm.starter.persistence.AbstractParameterBridgeTest;
import com.monogramm.starter.persistence.parameter.entity.Parameter;
import com.monogramm.starter.persistence.user.dao.IUserRepository;

/**
 * {@link ParameterBridge} Unit Test.
 * 
 * @author madmath03
 */
public class ParameterBridgeTest
    extends AbstractParameterBridgeTest<Parameter, ParameterDto, ParameterBridge> {

  @Override
  protected ParameterBridge buildTestBridge(IUserRepository userRepository) {
    return new ParameterBridge(userRepository);
  }

  @Override
  protected ParameterBridge buildTestBridge() {
    return new ParameterBridge();
  }

}
