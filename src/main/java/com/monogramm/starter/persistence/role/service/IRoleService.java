package com.monogramm.starter.persistence.role.service;

import com.monogramm.starter.dto.role.RoleDto;
import com.monogramm.starter.persistence.GenericService;
import com.monogramm.starter.persistence.role.entity.Role;
import com.monogramm.starter.persistence.role.exception.RoleNotFoundException;

import java.util.List;

public interface IRoleService extends GenericService<Role, RoleDto> {

  @Override
  RoleBridge getBridge();

  /**
   * Find all roles by their name while ignoring case.
   * 
   * @param name the name content to search.
   * 
   * @return the list of all the roles matching the search.
   */
  List<Role> findAllByName(final String name);

  /**
   * Find an role through its name while ignoring case.
   * 
   * @param name the name to search.
   * 
   * @return the role matching the name.
   * 
   * @throws RoleNotFoundException if no role matches the name in the repository.
   */
  Role findByName(final String name);

}
