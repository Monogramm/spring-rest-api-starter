/*
 * Creation by madmath03 the 2018-01-09.
 */

package com.monogramm.starter.persistence.parameter.service;

import com.monogramm.starter.config.security.IAuthenticationFacade;
import com.monogramm.starter.dto.parameter.ParameterDto;
import com.monogramm.starter.persistence.AbstractGenericService;
import com.monogramm.starter.persistence.EntityNotFoundException;
import com.monogramm.starter.persistence.parameter.dao.ParameterRepository;
import com.monogramm.starter.persistence.parameter.entity.Parameter;
import com.monogramm.starter.persistence.parameter.exception.ParameterNotFoundException;
import com.monogramm.starter.persistence.user.dao.UserRepository;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * {@link Parameter} service.
 * 
 * @author madmath03
 */
@Service
public class ParameterServiceImpl extends AbstractGenericService<Parameter, ParameterDto>
    implements ParameterService {

  /**
   * Create a {@link ParameterServiceImpl}.
   * 
   * @param repository the entity repository.
   * @param userRepository the user repository.
   * @param authenticationFacade a facade to retrieve the authentication object.
   */
  @Autowired
  public ParameterServiceImpl(ParameterRepository repository, UserRepository userRepository,
      IAuthenticationFacade authenticationFacade) {
    super(repository, userRepository, new ParameterBridge(userRepository), authenticationFacade);
  }

  @Override
  protected ParameterRepository getRepository() {
    return (ParameterRepository) super.getRepository();
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
