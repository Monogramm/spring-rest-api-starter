/*
 * Creation by madmath03 the 2017-11-25
 */

package com.monogramm.starter.persistence.permission.service;

import com.monogramm.starter.dto.permission.PermissionDto;
import com.monogramm.starter.persistence.AbstractGenericBridge;
import com.monogramm.starter.persistence.permission.entity.Permission;
import com.monogramm.starter.persistence.user.dao.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Bridge to convert a {@link PermissionDto} to a {@link Permission} and vice versa.
 * 
 * @author madmath03
 */
public class PermissionBridge extends AbstractGenericBridge<Permission, PermissionDto> {

  /**
   * Create a {@link PermissionBridge}.
   * 
   * <p>
   * <strong>Use with caution:</strong> this constructor will not set the @{code userRepository},
   * preventing any search in the Persistence Storage for the relations objects. This might be
   * dangerous when converting {@link #toEntity(PermissionDto)} as no consistency check will be done
   * but it will definitely improve performance.
   * </p>
   * 
   */
  public PermissionBridge() {
    super();
  }

  /**
   * Create a {@link PermissionBridge}.
   * 
   * @param userRepository repository to lookup users.
   */
  @Autowired
  public PermissionBridge(UserRepository userRepository) {
    super(userRepository);
  }

  @Override
  protected Permission buildEntity() {
    return new Permission();
  }

  @Override
  protected PermissionDto buildDto() {
    return new PermissionDto();
  }

  @Override
  public Permission toEntity(final PermissionDto dto) {
    final Permission entity = super.toEntity(dto);

    entity.setName(dto.getName());

    return entity;
  }

  @Override
  public PermissionDto toDto(final Permission entity) {
    final PermissionDto dto = super.toDto(entity);

    dto.setName(entity.getName());

    return dto;
  }
}
