package com.monogramm.starter.persistence.role.dao;

import com.monogramm.starter.persistence.GenericRepository;
import com.monogramm.starter.persistence.role.entity.Role;

import java.util.List;
import java.util.UUID;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * The Roles Data Access Object (DAO) interface.
 * 
 * @author madmath03
 */
@Repository
public interface IRoleRepository extends GenericRepository<Role> {

  /**
   * Find all roles from the repository containing the name while ignoring case.
   * 
   * @param name the name content to search.
   * 
   * @return the list of all the roles matching the search through the repository.
   */
  @Query("FROM Role AS r "
      + "WHERE LOWER(r.name) LIKE concat('%', LOWER(:name), '%')")
  List<Role> findAllContainingNameIgnoreCase(@Param("name") final String name);

  /**
   * Find an role through its name while ignoring case.
   * 
   * @param name the name to search.
   * 
   * @return the role matching the name.
   * 
   * @throws NoResultException if no role matches the name in the repository.
   * @throws NonUniqueResultException if several roles match the name in the repository.
   */
  Role findByNameIgnoreCase(final String name);

  /**
   * Tests if an entity exists in the repository for the given primary key or the name.
   * 
   * @param roleId the identifier of the role to check existence.
   * @param name the name of the role to check existence.
   * 
   * @return {@code true} if role exists, {@code false} otherwise.
   */
  @Query("SELECT count(r) > 0 FROM Role AS r "
      + "WHERE r.id = :roleId OR LOWER(r.name) = LOWER(:name)")
  boolean exists(@Param("roleId") final UUID roleId,
      @Param("name") final String name);
}
