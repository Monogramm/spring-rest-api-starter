/*
 * Creation by madmath03 the 2018-01-09.
 */

package com.monogramm.starter.persistence.parameter.service;

import com.monogramm.starter.dto.parameter.ParameterDto;
import com.monogramm.starter.persistence.GenericService;
import com.monogramm.starter.persistence.parameter.entity.Parameter;
import com.monogramm.starter.persistence.parameter.exception.ParameterNotFoundException;

/**
 * {@link Parameter} service interface.
 * 
 * @author madmath03
 */
public interface ParameterService extends GenericService<Parameter, ParameterDto> {

  @Override
  ParameterBridge getBridge();

  /**
   * Find an parameter through its name while ignoring case.
   * 
   * @param name the name to search.
   * 
   * @return the parameter matching the name.
   * 
   * @throws ParameterNotFoundException if no parameter matches the name in the repository.
   */
  Parameter findByName(final String name);

}
