/*
 * Creation by madmath03 the 2017-11-18.
 */

package com.monogramm.starter.persistence.type.service;

import com.monogramm.starter.dto.type.TypeDto;
import com.monogramm.starter.persistence.AbstractGenericBridge;
import com.monogramm.starter.persistence.type.entity.Type;
import com.monogramm.starter.persistence.user.dao.UserRepository;

/**
 * Bridge to convert a {@link TypeDto} to a {@link Type} and vice versa.
 * 
 * @author madmath03
 */
public class TypeBridge extends AbstractGenericBridge<Type, TypeDto> {

  /**
   * Create a {@link TypeBridge}.
   * 
   * <p>
   * <strong>Use with caution:</strong> this constructor will not set the @{code userRepository},
   * preventing any search in the Persistence Storage for the relations objects. This might be
   * dangerous when converting {@link #toEntity(TypeDto)} as no consistency check will be done but
   * it will definitely improve performance.
   * </p>
   * 
   */
  public TypeBridge() {
    super();
  }

  /**
   * Create a {@link TypeBridge}.
   * 
   * @param userRepository repository to lookup users.
   */
  public TypeBridge(UserRepository userRepository) {
    super(userRepository);
  }

  @Override
  protected Type buildEntity() {
    return new Type();
  }

  @Override
  protected TypeDto buildDto() {
    return new TypeDto();
  }

  @Override
  public Type toEntity(final TypeDto dto) {
    final Type entity = super.toEntity(dto);

    entity.setName(dto.getName());

    return entity;
  }

  @Override
  public TypeDto toDto(final Type entity) {
    final TypeDto dto = super.toDto(entity);

    dto.setName(entity.getName());

    return dto;
  }
}
