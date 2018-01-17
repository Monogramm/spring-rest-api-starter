/*
 * Creation by madmath03 the 2017-09-03.
 */

package com.monogramm.starter.persistence;

import java.util.List;
import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

/**
 * Generic Repository interface.
 * 
 * @author madmath03
 */
@NoRepositoryBean
public interface GenericRepository<T extends AbstractGenericEntity> extends JpaRepository<T, UUID> {
  /**
   * Find all entities from the repository.
   * 
   * @return the list of all the entities available through the repository.
   */
  @Transactional(readOnly = true)
  List<T> findAll();

  /**
   * Find an entity through its primary key.
   * 
   * @param entityId the entity unique identifier.
   * 
   * @return the entity matching the identifier, or {@code null} if none matches.
   */
  @Transactional(readOnly = true)
  T findById(final UUID entityId);

  /**
   * Add an entity to the repository.
   * 
   * @param entity an entity to add.
   * 
   * @throws DataIntegrityViolationException if any constraints failed during insert.
   */
  default void add(final T entity) {
    this.saveAndFlush(entity);
  }

  /**
   * Update an entity through the repository.
   * 
   * @param entity the reference entity used for the update.
   * 
   * @return the updated entity, {@code null} if entity was not found in persistence layer.
   * 
   * @throws NullPointerException if the {@code entity} is {@code null}.
   */
  default T update(final T entity) {
    T updateEntity = findById(entity.getId());

    if (updateEntity != null) {
      updateEntity.update(entity);

      updateEntity = this.save(updateEntity);
    }

    return updateEntity;
  }

  /**
   * Delete an entity through the repository.
   * 
   * @param entityId the entity identifier of the entity to delete.
   * 
   * @return the number of deleted entities.
   */
  Integer deleteById(final UUID entityId);

  /**
   * Tests if an entity exists in the repository for the given primary key.
   * 
   * @param entityId the unique identifier of the entity to check existence.
   * 
   * @return {@code true} if entity with the given identifier exists, {@code false} otherwise.
   */
  @Transactional(readOnly = true)
  boolean exists(final UUID entityId);

}
