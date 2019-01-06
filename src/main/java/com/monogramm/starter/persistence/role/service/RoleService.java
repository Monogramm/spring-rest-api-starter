package com.monogramm.starter.persistence.role.service;

import com.monogramm.starter.config.security.IAuthenticationFacade;
import com.monogramm.starter.dto.role.RoleDto;
import com.monogramm.starter.persistence.AbstractGenericService;
import com.monogramm.starter.persistence.permission.dao.IPermissionRepository;
import com.monogramm.starter.persistence.role.dao.IRoleRepository;
import com.monogramm.starter.persistence.role.entity.Role;
import com.monogramm.starter.persistence.role.exception.RoleNotFoundException;
import com.monogramm.starter.persistence.user.dao.IUserRepository;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * {@link Role} service.
 * 
 * @author madmath03
 */
@Service
public class RoleService extends AbstractGenericService<Role, RoleDto> implements IRoleService {

  private final IPermissionRepository permissionRepository;

  /**
   * Create a {@link RoleService}.
   * 
   * @param roleDao the role repository.
   * @param userDao the user repository.
   * @param permissionDao the permission repository.
   * @param authenticationFacade a facade to retrieve the authentication object.
   */
  @Autowired
  public RoleService(final IRoleRepository roleDao, final IUserRepository userDao,
      final IPermissionRepository permissionDao, IAuthenticationFacade authenticationFacade) {
    super(roleDao, userDao, new RoleBridge(userDao, permissionDao), authenticationFacade);
    if (permissionDao == null) {
      throw new IllegalArgumentException("Permission repository cannot be null.");
    }
    this.permissionRepository = permissionDao;
  }

  /**
   * Get the {@link #permissionRepository}.
   * 
   * @return the {@link #permissionRepository}.
   */
  protected final IPermissionRepository getPermissionRepository() {
    return permissionRepository;
  }

  @Override
  protected IRoleRepository getRepository() {
    return (IRoleRepository) super.getRepository();
  }

  @Override
  public RoleBridge getBridge() {
    return (RoleBridge) super.getBridge();
  }

  @Override
  protected boolean exists(Role entity) {
    return getRepository().exists(entity.getId(), entity.getName());
  }

  @Override
  protected RoleNotFoundException createEntityNotFoundException(Role entity) {
    return new RoleNotFoundException("Following role not found:" + entity);
  }

  @Override
  protected RoleNotFoundException createEntityNotFoundException(UUID entityId) {
    return new RoleNotFoundException("No role for ID=" + entityId);
  }

  @Transactional(readOnly = true)
  @Override
  public Role findByName(final String name) {
    return getRepository().findByNameIgnoreCase(name);
  }

  @Transactional(readOnly = true)
  @Override
  public List<Role> findAllByName(final String name) {
    return getRepository().findAllContainingNameIgnoreCase(name);
  }
}
