/*
 * Creation by madmath03 the 2017-12-20.
 */

package com.monogramm.starter.persistence;

import com.monogramm.starter.dto.AbstractParameterDto;
import com.monogramm.starter.persistence.user.dao.UserRepository;

/**
 * AbstractParameterBridge.
 * 
 * @author madmath03
 */
public abstract class AbstractParameterBridge<T extends AbstractParameter,
    D extends AbstractParameterDto> extends AbstractGenericBridge<T, D> {

  /**
   * Create a {@link AbstractParameterBridge}.
   * 
   */
  protected AbstractParameterBridge() {
    super();
  }

  /**
   * Create a {@link AbstractParameterBridge}.
   * 
   * @param userRepository repository to lookup users.
   */
  public AbstractParameterBridge(UserRepository userRepository) {
    super(userRepository);
  }

  @Override
  public T toEntity(final D dto) {
    final T entity = super.toEntity(dto);

    entity.setName(dto.getName());
    entity.setDescription(dto.getDescription());
    if (dto.getType() != null) {
      entity.setType(ParameterType.valueOf(dto.getType().toUpperCase()));
    }
    entity.setValue(dto.getValue());

    return entity;
  }

  @Override
  public D toDto(final T entity) {
    final D dto = super.toDto(entity);

    dto.setName(entity.getName());
    dto.setDescription(entity.getDescription());
    if (entity.getType() != null) {
      dto.setType(entity.getType().toString());
    }
    dto.setValue(entity.getValue());

    return dto;
  }

}
