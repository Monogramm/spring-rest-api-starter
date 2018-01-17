/*
 * Creation by madmath03 the 2017-11-25
 */

package com.monogramm.starter.persistence.permission.dao;

import com.monogramm.starter.persistence.GenericRepository;
import com.monogramm.starter.persistence.permission.entity.Permission;
import com.monogramm.starter.persistence.permission.exception.PermissionNotFoundException;

import java.util.List;
import java.util.UUID;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * The Permissions Data Access Object (DAO) interface.
 * 
 * @author madmath03
 */
@Repository
public interface IPermissionRepository extends GenericRepository<Permission> {

  /**
   * Find all permissions from the repository containing the name while ignoring case.
   * 
   * @param name the name content to search.
   * 
   * @return the list of all the permissions matching the search through the repository.
   */
  @Transactional(readOnly = true)
  @Query("FROM Permission AS p "
      + "WHERE LOWER(p.name) LIKE concat('%', LOWER(:name), '%')")
  List<Permission> findAllContainingNameIgnoreCase(
      @Param("name") final String name);

  /**
   * Find an permission through its name while ignoring case.
   * 
   * @param name the name to search.
   * 
   * @return the permission matching the name.
   * 
   * @throws PermissionNotFoundException if no permission matches the name in the
   *         repository.
   * @throws NoResultException if no permission matches the name in the repository.
   * @throws NonUniqueResultException if several permissions match the name in the
   *         repository.
   */
  @Transactional(readOnly = true)
  Permission findByNameIgnoreCase(final String name);

  /**
   * Tests if an entity exists in the repository for the given primary key or the name.
   * 
   * @param permissionId the identifier of the permission to check existence.
   * @param name the name of the permission to check existence.
   * 
   * @return {@code true} if permission exists, {@code false} otherwise.
   */
  @Transactional(readOnly = true)
  @Query("SELECT count(p) > 0 FROM Permission AS p "
      + "WHERE p.id = :permissionId OR LOWER(p.name) = LOWER(:name)")
  boolean exists(@Param("permissionId") final UUID permissionId,
      @Param("name") final String name);
}
