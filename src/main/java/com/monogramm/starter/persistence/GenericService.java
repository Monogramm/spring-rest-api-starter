/*
 * Creation by madmath03 the 2017-09-03.
 */

package com.monogramm.starter.persistence;

import com.monogramm.starter.dto.AbstractGenericDto;
import com.monogramm.starter.persistence.user.entity.User;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * Generic Service interface.
 * 
 * @param <T> the entity type used to manage data in persistence storage.
 * @param <D> the DTO type to convert entities to/from.
 * 
 * @author madmath03
 */
public interface GenericService<T extends AbstractGenericEntity, D extends AbstractGenericDto> {

  /**
   * Get a bridge to convert this DTO to an entity and vice versa.
   * 
   * @return a bridge to convert this DTO to an entity and vice versa.
   */
  AbstractGenericBridge<T, D> getBridge();

  /**
   * Convert a DTO to an entity matching the data of the given DTO.
   * 
   * <p>
   * This method is simply an alias to {@link AbstractGenericBridge#toEntity(AbstractGenericDto)}.
   * </p>
   * 
   * @param dto the DTO convert to an entity.
   * 
   * @return an entity matching the data of the given DTO.
   * 
   * @throws NullPointerException if {@code dto} is {@code null}, or if
   *         {@link AbstractGenericBridge#buildEntity()} returned {@code null}.
   */
  default T toEntity(final D dto) {
    return this.getBridge().toEntity(dto);
  }

  /**
   * Convert a DTO List to an entity Collection matching the data of the given DTOs.
   * 
   * <p>
   * This method is simply an alias to {@link AbstractGenericBridge#toEntity(Collection)}.
   * </p>
   * 
   * @param dtos the DTOs to convert to entities.
   * 
   * @return entities matching the data of the given DTO.
   * 
   * @throws NullPointerException if {@code dtos} is {@code null}, contains {@code null}, or if
   *         {@link AbstractGenericBridge#buildEntity()} returned {@code null}.
   */
  default List<T> toEntity(final Collection<D> dtos) {
    return this.getBridge().toEntity(dtos);
  }

  /**
   * Convert an entity to a DTO matching the data of the given entity.
   * 
   * <p>
   * This method is simply an alias to {@link AbstractGenericBridge#toDto(AbstractGenericEntity)}.
   * </p>
   * 
   * @param entity the entity convert to an DTO.
   * 
   * @return a DTO matching the data of the given entity.
   * 
   * @throws NullPointerException if {@code entity} is {@code null}, or if
   *         {@link AbstractGenericBridge#buildDto()} returned {@code null}.
   */
  default D toDto(final T entity) {
    return this.getBridge().toDto(entity);
  }

  /**
   * Convert entities to a DTO List matching the data of the given entities.
   * 
   * <p>
   * This method is simply an alias to {@link AbstractGenericBridge#toDto(Collection)}.
   * </p>
   * 
   * @param entities the entities to convert to an DTO.
   * 
   * @return a DTO Collection matching the data of the given entities.
   * 
   * @throws NullPointerException if {@code entity} is {@code null}, contains {@code null}, or if
   *         {@link AbstractGenericBridge#buildDto()} returned {@code null}.
   */
  default List<D> toDto(final Collection<T> entities) {
    return this.getBridge().toDto(entities);
  }



  /**
   * Find all entities.
   * 
   * @return the list of all the entities available through the service.
   */
  List<T> findAll();

  /**
   * Returns all entities sorted by the given options.
   * 
   * @param sort sort conditions
   * @return all entities sorted by the given options
   */
  List<T> findAll(Sort sort);

  /**
   * Returns a {@link Page} of entities meeting the paging restriction provided in the
   * {@code Pageable} object.
   * 
   * @param pageable paging conditions
   * 
   * @return a page of entities
   */
  Page<T> findAll(Pageable pageable);

  /**
   * Returns a {@link Page} of entities meeting the paging restriction. Pages are zero indexed, thus
   * providing 0 for {@code page} will return the first page.
   * 
   * @param page zero-based page index.
   * @param size the size of the page to be returned.
   * 
   * @return a page of entities
   */
  default Page<T> findAll(int page, int size) {
    return this.findAll(page, size, null);
  }

  /**
   * Returns a {@link Page} of entities meeting the paging restriction with sort parameters applied.
   * Pages are zero indexed, thus providing 0 for {@code page} will return the first page.
   * 
   * @param page zero-based page index.
   * @param size the size of the page to be returned.
   * @param sort can be {@literal null}.
   * 
   * @return a page of entities
   */
  default Page<T> findAll(int page, int size, Sort sort) {
    final Pageable pageable = new PageRequest(page, size, sort);

    return this.findAll(pageable);
  }



  /**
   * Find an entity through its primary key.
   * 
   * @param entityId the entity unique identifier.
   * 
   * @return the entity matching the identifier, or {@code null} if none matches.
   */
  T findById(UUID entityId);

  /**
   * Find an entity through its primary key and owner id.
   * 
   * @param entityId the entity unique identifier.
   * @param ownerId the entity identifier of the entity owner.
   * 
   * @return the entity matching the identifier and owner, or {@code null} if none matches.
   */
  default T findByIdAndOwner(final UUID entityId, final UUID ownerId) {
    final User owner = User.builder().id(ownerId).build();
    return this.findByIdAndOwner(entityId, owner);
  }

  /**
   * Find an entity through its primary key and owner.
   * 
   * @param entityId the entity unique identifier.
   * @param owner the entity owner.
   * 
   * @return the entity matching the identifier and owner, or {@code null} if none matches.
   */
  T findByIdAndOwner(final UUID entityId, final User owner);

  /**
   * Add an entity.
   * 
   * @param entity an entity to add.
   * 
   * @return {@code true} if the entity was added, {@code false} otherwise.
   */
  boolean add(T entity);

  /**
   * Update an entity through the repository.
   * 
   * @param entity the reference entity used for the update.
   * 
   * @return the updated entity.
   * 
   * @throws EntityNotFoundException if no entity matches the reference entity in the repository.
   * @throws NullPointerException if the {@code entity} is {@code null}.
   */
  T update(T entity);

  /**
   * Update an entity through the repository only if owned by given owner id.
   * 
   * <p>
   * Secure method to ensure you only update if you own the data by providing the authenticated user
   * unique id as owner. {@link #update(AbstractGenericEntity)} should be used instead of
   * authenticated user has administration permissions.
   * </p>
   * 
   * @param entity the reference entity used for the update.
   * @param ownerId the entity identifier of the entity owner.
   * 
   * @return the updated entity, {@code null} if entity was not found in persistence layer.
   * 
   * @throws EntityNotFoundException if no entity matches the entity identifier in the repository.
   * @throws NullPointerException if the {@code entity} is {@code null}.
   */
  default T updateByOwner(final T entity, final UUID ownerId) {
    final User owner = User.builder().id(ownerId).build();
    return this.updateByOwner(entity, owner);
  }

  /**
   * Update an entity through the repository only if owned by given owner.
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
   * @throws EntityNotFoundException if no entity matches the entity identifier in the repository.
   * @throws NullPointerException if the {@code entity} is {@code null}.
   */
  T updateByOwner(final T entity, final User owner);

  /**
   * Delete an entity.
   * 
   * @param entityId the unique identifier of the entity to delete.
   * 
   * @throws EntityNotFoundException if no entity matches the entity identifier in the repository.
   */
  void deleteById(UUID entityId);

  /**
   * Delete an entity through the repository only if owned by given owner id.
   * 
   * <p>
   * Secure method to ensure you only delete if you own the data by providing the authenticated user
   * unique id as owner. {@link #deleteById(UUID)} should be used instead if authenticated user has
   * administration permissions.
   * </p>
   * 
   * @param entityId the entity identifier of the entity to delete.
   * @param ownerId the entity identifier of the entity owner.
   * 
   * @throws EntityNotFoundException if no entity matches the entity identifier in the repository.
   */
  default void deleteByIdAndOwner(final UUID entityId, final UUID ownerId) {
    final User owner = User.builder().id(ownerId).build();
    this.deleteByIdAndOwner(entityId, owner);
  }

  /**
   * Delete an entity through the repository only if owned by given owner.
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
   * @throws EntityNotFoundException if no entity matches the entity identifier in the repository.
   */
  void deleteByIdAndOwner(final UUID entityId, final User owner);

}
