/*
 * Creation by madmath03 the 2017-12-20.
 */

package com.monogramm.starter.persistence;

import com.monogramm.starter.dto.AbstractTokenDto;
import com.monogramm.starter.persistence.user.dao.IUserRepository;
import com.monogramm.starter.persistence.user.entity.User;

/**
 * AbstractTokenBridge.
 * 
 * @author madmath03
 */
public abstract class AbstractTokenBridge<T extends AbstractToken,
    D extends AbstractTokenDto> extends AbstractGenericBridge<T, D> {

  /**
   * Create a {@link AbstractTokenBridge}.
   * 
   */
  protected AbstractTokenBridge() {
    super();
  }

  /**
   * Create a {@link AbstractTokenBridge}.
   * 
   * @param userRepository repository to lookup users.
   */
  public AbstractTokenBridge(IUserRepository userRepository) {
    super(userRepository);
  }

  @Override
  public T toEntity(final D dto) {
    final T entity = super.toEntity(dto);

    entity.setCode(dto.getCode());
    entity.setExpiryDate(dto.getExpiryDate());

    if (dto.getUser() != null) {
      final User user;
      if (getUserRepository() == null) {
        user = User.builder().id(dto.getUser()).build();
      } else {
        user = this.getUserRepository().findById(dto.getUser());
      }
      entity.setUser(user);
    }

    return entity;
  }

  @Override
  public D toDto(final T entity) {
    final D dto = super.toDto(entity);

    dto.setCode(entity.getCode());
    dto.setExpiryDate(entity.getExpiryDate());

    if (entity.getUser() != null) {
      dto.setUser(entity.getUser().getId());
    }

    return dto;
  }

}
