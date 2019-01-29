/*
 * Creation by madmath03 the 2017-11-18.
 */

package com.monogramm.starter.persistence.role.service;

import com.monogramm.starter.dto.role.RoleDto;
import com.monogramm.starter.persistence.AbstractGenericBridge;
import com.monogramm.starter.persistence.permission.dao.PermissionRepository;
import com.monogramm.starter.persistence.permission.entity.Permission;
import com.monogramm.starter.persistence.role.entity.Role;
import com.monogramm.starter.persistence.user.dao.UserRepository;

import java.util.Collection;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Bridge to convert a {@link RoleDto} to a {@link Role} and vice versa.
 * 
 * @author madmath03
 */
public class RoleBridge extends AbstractGenericBridge<Role, RoleDto> {
  private PermissionRepository permissionRepository;

  /**
   * Create a {@link RoleBridge}.
   * 
   * <p>
   * <strong>Use with caution:</strong> this constructor will not set the @{code userRepository},
   * preventing any search in the Persistence Storage for the relations objects. This might be
   * dangerous when converting {@link #toEntity(RoleDto)} as no consistency check will be done but
   * it will definitely improve performance.
   * </p>
   * 
   */
  public RoleBridge() {
    super();
  }

  /**
   * Create a {@link RoleBridge}.
   * 
   * @param userRepository repository to lookup users.
   * @param permissionRepository repository to lookup permissions.
   */
  @Autowired
  public RoleBridge(UserRepository userRepository, PermissionRepository permissionRepository) {
    super(userRepository);
    this.permissionRepository = permissionRepository;
  }

  @Override
  protected Role buildEntity() {
    return new Role();
  }

  @Override
  protected RoleDto buildDto() {
    return new RoleDto();
  }

  @Override
  public Role toEntity(final RoleDto dto) {
    final Role entity = super.toEntity(dto);

    entity.setName(dto.getName());

    if (dto.getPermissions() != null) {
      if (permissionRepository == null) {
        for (final UUID id : dto.getPermissions()) {
          entity.addPermission(Permission.builder().id(id).build());
        }
      } else {
        for (final UUID id : dto.getPermissions()) {
          final Permission permission = this.permissionRepository.findById(id);
          if (permission != null) {
            entity.addPermission(permission);
          }
        }
      }
    }

    return entity;
  }

  @Override
  public RoleDto toDto(final Role entity) {
    final RoleDto dto = super.toDto(entity);

    dto.setName(entity.getName());

    final Collection<Permission> entityPermisssions = entity.getPermissions();
    if (entityPermisssions != null) {
      final Permission[] permissions = entityPermisssions.toArray(new Permission[] {});
      final UUID[] dtoPermissions = new UUID[permissions.length];

      for (int i = 0; i < permissions.length; i++) {
        dtoPermissions[i] = permissions[i].getId();
      }

      dto.setPermissions(dtoPermissions);
    }

    return dto;
  }

  /**
   * Get the {@link #permissionRepository}.
   * 
   * @return the {@link #permissionRepository}.
   */
  public final PermissionRepository getPermissionRepository() {
    return permissionRepository;
  }

  /**
   * Set the {@link #permissionRepository}.
   * 
   * @param permissionRepository the {@link #permissionRepository} to set.
   */
  public final void setPermissionRepository(PermissionRepository permissionRepository) {
    this.permissionRepository = permissionRepository;
  }

}
