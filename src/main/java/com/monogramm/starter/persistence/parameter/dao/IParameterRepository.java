/*
 * Creation by madmath03 the 2018-01-09.
 */

package com.monogramm.starter.persistence.parameter.dao;

import com.monogramm.starter.persistence.GenericRepository;
import com.monogramm.starter.persistence.parameter.entity.Parameter;

import java.util.UUID;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * IParameterRepository.
 * 
 * @author madmath03
 */
@Repository
public interface IParameterRepository extends GenericRepository<Parameter> {

  /**
   * Find a parameter through its name while ignoring case.
   * 
   * @param name the name to search.
   * 
   * @return the parameter matching the name.
   * 
   * @throws NoResultException if no parameter matches the name in the repository.
   * @throws NonUniqueResultException if several parameters match the name in the repository.
   */
  Parameter findByNameIgnoreCase(final String name);

  /**
   * Tests if an entity exists in the repository for the given primary key or the name.
   * 
   * @param id the identifier of the parameter to check existence.
   * @param name the name of the parameter to check existence.
   * 
   * @return {@code true} if parameter exists, {@code false} otherwise.
   */
  @Query("SELECT count(p) > 0 FROM Parameter AS p "
      + "WHERE p.id = :id OR LOWER(p.name) = LOWER(:name)")
  boolean exists(@Param("id") final UUID id, @Param("name") final String name);

}
