/*
 * Creation by madmath03 the 2017-11-18.
 */

package com.monogramm.starter.persistence.user.service;

import com.github.madmath03.password.Passwords;
import com.monogramm.starter.dto.user.UserDto;
import com.monogramm.starter.persistence.AbstractGenericBridge;
import com.monogramm.starter.persistence.role.dao.RoleRepository;
import com.monogramm.starter.persistence.role.entity.Role;
import com.monogramm.starter.persistence.user.dao.UserRepository;
import com.monogramm.starter.persistence.user.entity.User;

/**
 * Bridge to convert a {@link UserDto} to a {@link User} and vice versa.
 * 
 * @author madmath03
 */
public class UserBridge extends AbstractGenericBridge<User, UserDto> {
  private RoleRepository roleRepository;

  /**
   * Create a {@link UserBridge}.
   * 
   * <p>
   * <strong>Use with caution:</strong> this constructor will not set the @{code userRepository},
   * preventing any search in the Persistence Storage for the relations objects. This might be
   * dangerous when converting {@link #toEntity(UserDto)} as no consistency check will be done but
   * it will definitely improve performance.
   * </p>
   * 
   */
  public UserBridge() {
    super();
  }

  /**
   * Create a {@link UserBridge}.
   * 
   * @param userRepository repository to lookup users.
   * @param roleRepository repository to lookup roles.
   */
  public UserBridge(final UserRepository userRepository, final RoleRepository roleRepository) {
    super(userRepository);
    this.roleRepository = roleRepository;
  }

  @Override
  protected User buildEntity() {
    return new User();
  }

  @Override
  protected UserDto buildDto() {
    return new UserDto();
  }

  @Override
  public User toEntity(final UserDto dto) {
    final User entity = super.toEntity(dto);

    entity.setUsername(dto.getUsername());
    entity.setEmail(dto.getEmail());
    if (dto.getPassword() != null) {
      entity.setPassword(Passwords.getHash(dto.getPassword()));
    }
    entity.setEnabled(dto.isEnabled());
    entity.setVerified(dto.isVerified());

    if (dto.getRole() != null) {
      final Role role;
      if (roleRepository == null) {
        role = Role.builder().id(dto.getRole()).build();
      } else {
        role = this.roleRepository.findById(dto.getRole());
      }
      entity.setRole(role);
    }

    return entity;
  }

  @Override
  public UserDto toDto(final User entity) {
    final UserDto dto = super.toDto(entity);

    dto.setUsername(entity.getUsername());
    dto.setEmail(entity.getEmail());
    dto.setPassword(null);
    dto.setEnabled(entity.isEnabled());
    dto.setVerified(entity.isVerified());

    if (entity.getRole() != null) {
      final Role role = entity.getRole();
      dto.setRole(role.getId());
    }

    return dto;
  }

  /**
   * Get the {@link #roleRepository}.
   * 
   * @return the {@link #roleRepository}.
   */
  protected final RoleRepository getRoleRepository() {
    return roleRepository;
  }

  /**
   * Set the {@link roleRepository}.
   * 
   * @param roleRepository the {@link #roleRepository} to set.
   */
  protected final void setRoleRepository(RoleRepository roleRepository) {
    this.roleRepository = roleRepository;
  }

}
