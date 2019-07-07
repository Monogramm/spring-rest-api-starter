package com.monogramm.starter.persistence.type.service;

import com.monogramm.starter.config.security.IAuthenticationFacade;
import com.monogramm.starter.dto.type.TypeDto;
import com.monogramm.starter.persistence.AbstractGenericService;
import com.monogramm.starter.persistence.type.dao.TypeRepository;
import com.monogramm.starter.persistence.type.entity.Type;
import com.monogramm.starter.persistence.type.exception.TypeNotFoundException;
import com.monogramm.starter.persistence.user.dao.UserRepository;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * {@link Type} service.
 * 
 * @author madmath03
 */
@Service
public class TypeServiceImpl extends AbstractGenericService<Type, TypeDto> implements TypeService {

  /**
   * Create a {@link TypeServiceImpl}.
   * 
   * @param typeDao the type repository.
   * @param userDao the user repository.
   * @param authenticationFacade a facade to retrieve the authentication object.
   */
  @Autowired
  public TypeServiceImpl(final TypeRepository typeDao, final UserRepository userDao,
      IAuthenticationFacade authenticationFacade) {
    super(typeDao, userDao, new TypeBridge(userDao), authenticationFacade);
  }

  @Override
  protected TypeRepository getRepository() {
    return (TypeRepository) super.getRepository();
  }

  @Override
  public TypeBridge getBridge() {
    return (TypeBridge) super.getBridge();
  }

  @Override
  protected boolean exists(Type entity) {
    return getRepository().exists(entity.getId(), entity.getName());
  }

  @Override
  protected TypeNotFoundException createEntityNotFoundException(Type entity) {
    return new TypeNotFoundException("Following type not found:" + entity);
  }

  @Override
  protected TypeNotFoundException createEntityNotFoundException(UUID entityId) {
    return new TypeNotFoundException("No type for ID=" + entityId);
  }

  @Transactional(readOnly = true)
  @Override
  public Type findByName(final String name) {
    return getRepository().findByNameIgnoreCase(name);
  }

  @Transactional(readOnly = true)
  @Override
  public List<Type> findAllByName(final String name) {
    return getRepository().findAllContainingNameIgnoreCase(name);
  }
}
