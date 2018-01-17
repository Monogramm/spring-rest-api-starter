/*
 * Creation by madmath03 the 2018-01-09.
 */

package com.monogramm.starter.persistence.parameter.service;

import com.monogramm.starter.dto.parameter.ParameterDto;
import com.monogramm.starter.persistence.AbstractGenericService;
import com.monogramm.starter.persistence.EntityNotFoundException;
import com.monogramm.starter.persistence.parameter.dao.IParameterRepository;
import com.monogramm.starter.persistence.parameter.entity.Parameter;
import com.monogramm.starter.persistence.parameter.exception.ParameterNotFoundException;
import com.monogramm.starter.persistence.user.dao.IUserRepository;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ParameterService.
 * 
 * @author madmath03
 */
@Service
public class ParameterService extends AbstractGenericService<Parameter, ParameterDto>
    implements IParameterService {

  /**
   * Create a {@link ParameterService}.
   * 
   * @param repository the entity repository.
   * @param userRepository the user repository.
   */
  @Autowired
  public ParameterService(IParameterRepository repository, IUserRepository userRepository) {
    super(repository, userRepository, new ParameterBridge(userRepository));
  }

  @Override
  protected IParameterRepository getRepository() {
    return (IParameterRepository) super.getRepository();
  }

  @Override
  public ParameterBridge getBridge() {
    return (ParameterBridge) super.getBridge();
  }

  @Override
  protected boolean exists(Parameter entity) {
    return getRepository().exists(entity.getId(), entity.getName());
  }

  @Override
  protected EntityNotFoundException createEntityNotFoundException(Parameter entity) {
    return new ParameterNotFoundException("Following parameter not found:" + entity);
  }

  @Override
  protected EntityNotFoundException createEntityNotFoundException(UUID entityId) {
    return new ParameterNotFoundException("No parameter for ID=" + entityId);
  }

  @Transactional(readOnly = true)
  @Override
  public Parameter findByName(String name) {
    return getRepository().findByNameIgnoreCase(name);
  }

}
