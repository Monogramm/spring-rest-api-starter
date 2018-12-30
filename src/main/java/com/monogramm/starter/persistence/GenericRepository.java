/*
 * Creation by madmath03 the 2017-09-03.
 */

package com.monogramm.starter.persistence;

import com.monogramm.starter.persistence.user.entity.User;

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
   * Find an entity through its primary key and owner.
   * 
   * @param entityId the entity unique identifier.
   * @param owner the entity owner.
   * 
   * @return the entity matching the identifier and owner, or {@code null} if none matches.
   */
  @Transactional(readOnly = true)
  T findByIdAndOwner(final UUID entityId, final User owner);

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
   * Update an entity through the repository only if owned by given owner id.
   * 
   * <p>
   * Secure method to ensure you only update if you own the data by providing the authenticated user
   * as owner. {@link #update(AbstractGenericEntity)} should be used instead if authenticated user
   * has administration permissions.
   * </p>
   * 
   * @param entity the reference entity used for the update.
   * @param owner the entity owner.
   * 
   * @return the updated entity, {@code null} if entity was not found in persistence layer.
   * 
   * @throws NullPointerException if the {@code entity} is {@code null}.
   */
  default T updateByOwner(final T entity, final User owner) {
    T updateEntity = findByIdAndOwner(entity.getId(), owner);

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
   * Delete an entity through the repository only if owned by given owner id.
   * 
   * <p>
   * Secure method to ensure you only delete if you own the data by providing the authenticated user
   * as owner. {@link #deleteById(UUID)} should be used instead if authenticated user has
   * administration permissions.
   * </p>
   * 
   * @param entityId the entity identifier of the entity to delete.
   * @param owner the entity owner.
   * 
   * @return the number of deleted entities.
   */
  Integer deleteByIdAndOwner(final UUID entityId, final User owner);

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
