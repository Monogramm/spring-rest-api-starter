package com.monogramm.starter.persistence.permission.service;

import com.monogramm.starter.dto.permission.PermissionDto;
import com.monogramm.starter.persistence.AbstractGenericService;
import com.monogramm.starter.persistence.permission.dao.IPermissionRepository;
import com.monogramm.starter.persistence.permission.entity.Permission;
import com.monogramm.starter.persistence.permission.exception.PermissionNotFoundException;
import com.monogramm.starter.persistence.user.dao.IUserRepository;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PermissionService extends AbstractGenericService<Permission, PermissionDto>
    implements IPermissionService {

  /**
   * Create a {@link PermissionService}.
   * 
   * @param permissionDao the permission repository.
   * @param userDao the user repository.
   */
  @Autowired
  public PermissionService(final IPermissionRepository permissionDao,
      final IUserRepository userDao) {
    super(permissionDao, userDao, new PermissionBridge(userDao));
  }

  @Override
  protected IPermissionRepository getRepository() {
    return (IPermissionRepository) super.getRepository();
  }

  @Override
  public PermissionBridge getBridge() {
    return (PermissionBridge) super.getBridge();
  }

  @Override
  protected boolean exists(Permission entity) {
    return getRepository().exists(entity.getId(), entity.getName());
  }

  @Override
  protected PermissionNotFoundException createEntityNotFoundException(Permission entity) {
    return new PermissionNotFoundException("Following permission not found:" + entity);
  }

  @Override
  protected PermissionNotFoundException createEntityNotFoundException(UUID entityId) {
    return new PermissionNotFoundException("No permission for ID=" + entityId);
  }

  @Transactional(readOnly = true)
  @Override
  public Permission findByName(final String name) {
    return getRepository().findByNameIgnoreCase(name);
  }

  @Transactional(readOnly = true)
  @Override
  public List<Permission> findAllByName(final String name) {
    return getRepository().findAllContainingNameIgnoreCase(name);
  }
}
