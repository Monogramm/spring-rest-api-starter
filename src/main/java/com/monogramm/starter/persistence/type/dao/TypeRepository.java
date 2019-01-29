package com.monogramm.starter.persistence.type.dao;

import com.monogramm.starter.persistence.GenericRepository;
import com.monogramm.starter.persistence.type.entity.Type;

import java.util.List;
import java.util.UUID;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * The {@link Type} Data Access Object (DAO) interface.
 * 
 * @author madmath03
 */
@Repository
public interface TypeRepository extends GenericRepository<Type> {

  /**
   * Find all types from the repository containing the name while ignoring case.
   * 
   * @param name the name content to search.
   * 
   * @return the list of all the types matching the search through the repository.
   */
  @Query("FROM Type AS t " + "WHERE LOWER(t.name) LIKE concat('%', LOWER(:name), '%')")
  List<Type> findAllContainingNameIgnoreCase(@Param("name") final String name);

  /**
   * Find an type through its name while ignoring case.
   * 
   * @param name the name to search.
   * 
   * @return the type matching the name.
   * 
   * @throws NoResultException if no type matches the name in the repository.
   * @throws NonUniqueResultException if several types match the name in the repository.
   */
  Type findByNameIgnoreCase(final String name);

  /**
   * Tests if an entity exists in the repository for the given primary key or the name.
   * 
   * @param typeId the identifier of the type to check existence.
   * @param name the name of the type to check existence.
   * 
   * @return {@code true} if type exists, {@code false} otherwise.
   */
  @Query("SELECT count(t) > 0 FROM Type AS t "
      + "WHERE t.id = :typeId OR LOWER(t.name) = LOWER(:name)")
  boolean exists(@Param("typeId") final UUID typeId, @Param("name") final String name);
}
