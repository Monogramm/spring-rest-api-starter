/*
 * Creation by madmath03 the 2017-09-03.
 */

package com.monogramm.starter.persistence;

import com.monogramm.starter.dto.AbstractGenericDto;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

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
   * Find an entity through its primary key.
   * 
   * @param entityId the entity unique identifier.
   * 
   * @return the entity matching the identifier, or {@code null} if none matches.
   */
  T findById(UUID entityId);

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
   */
  T update(T entity);

  /**
   * Delete an entity.
   * 
   * @param entityId the unique identifier of the entity to delete.
   * 
   * @throws EntityNotFoundException if no entity matches the entity identifier in the repository.
   */
  void deleteById(UUID entityId);
}
