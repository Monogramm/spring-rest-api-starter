/*
 * Creation by madmath03 the 2017-11-25
 */

package com.monogramm.starter.persistence.permission.service;

import com.monogramm.starter.dto.permission.PermissionDto;
import com.monogramm.starter.persistence.GenericService;
import com.monogramm.starter.persistence.permission.entity.Permission;
import com.monogramm.starter.persistence.permission.exception.PermissionNotFoundException;

import java.util.List;

/**
 * {@link Permission} service interface.
 * 
 * @author madmath03
 */
public interface IPermissionService extends GenericService<Permission, PermissionDto> {

  @Override
  PermissionBridge getBridge();

  /**
   * Find all permissions by their name while ignoring case.
   * 
   * @param name the name content to search.
   * 
   * @return the list of all the permissions matching the search.
   */
  List<Permission> findAllByName(final String name);

  /**
   * Find an permission through its name while ignoring case.
   * 
   * @param name the name to search.
   * 
   * @return the permission matching the name.
   * 
   * @throws PermissionNotFoundException if no permission matches the name in the repository.
   */
  Permission findByName(final String name);

}
