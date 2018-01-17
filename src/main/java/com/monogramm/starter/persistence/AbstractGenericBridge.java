/*
 * Creation by madmath03 the 2017-11-18.
 */

package com.monogramm.starter.persistence;

import com.monogramm.starter.dto.AbstractGenericDto;
import com.monogramm.starter.persistence.user.dao.IUserRepository;
import com.monogramm.starter.persistence.user.entity.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Bridge to convert a DTO to an entity.
 * 
 * @param <T> the entity type to build to.
 * @param <D> the DTO type to convert from.
 * 
 * @author madmath03
 */
public abstract class AbstractGenericBridge<T extends AbstractGenericEntity,
    D extends AbstractGenericDto> {
  private IUserRepository userRepository;

  /**
   * Create a {@link AbstractGenericBridge}.
   * 
   * <p>
   * <strong>Use with caution:</strong> this constructor will not set the @{code userRepository},
   * preventing any search in the Persistence Storage for the relations objects. This might be
   * dangerous when converting {@link #toEntity(AbstractGenericDto)} as no consistency check will be
   * done but it will definitely improve performance.
   * </p>
   * 
   */
  protected AbstractGenericBridge() {
    super();
  }

  /**
   * Create a {@link AbstractGenericBridge}.
   * 
   * @param userRepository repository to lookup users.
   */
  public AbstractGenericBridge(final IUserRepository userRepository) {
    super();
    this.userRepository = userRepository;
  }

  /**
   * Get the {@link #userRepository}.
   * 
   * @return the {@link #userRepository}.
   */
  protected final IUserRepository getUserRepository() {
    return userRepository;
  }

  /**
   * Set the {@link userRepository}.
   * 
   * @param userRepository the {@link #userRepository} to set.
   */
  protected final void setUserRepository(IUserRepository userRepository) {
    this.userRepository = userRepository;
  }

  /**
   * Construct an empty entity for conversion.
   * 
   * @return an entity for conversion.
   */
  protected abstract T buildEntity();

  /**
   * Construct an empty DTO for conversion.
   * 
   * @return an DTO for conversion.
   */
  protected abstract D buildDto();

  /**
   * Convert a DTO to an entity matching the data of the given DTO.
   * 
   * <p>
   * If repositories are attached to this bridge, relations to foreign objects should be retrieved
   * by their appropriate repository. If no repository is available for a relation, a dummy object
   * containing only the identifier of the foreign object should be returned instead.
   * </p>
   * 
   * @param dto the DTO to convert to an entity.
   * 
   * @return an entity matching the data of the given DTO.
   * 
   * @throws NullPointerException if {@code dto} is {@code null}, or if {@link #buildEntity()}
   *         returned {@code null}.
   */
  public T toEntity(final D dto) {
    final T entity = this.buildEntity();

    entity.setId(dto.getId());

    entity.setCreatedAt(dto.getCreatedAt());
    if (dto.getCreatedBy() != null) {
      final User createdBy;
      if (userRepository == null) {
        createdBy = User.builder().id(dto.getCreatedBy()).build();
      } else {
        createdBy = this.userRepository.findById(dto.getCreatedBy());
      }
      entity.setCreatedBy(createdBy);
    }

    entity.setModifiedAt(dto.getModifiedAt());
    if (dto.getModifiedBy() != null) {
      final User modifiedBy;
      if (userRepository == null) {
        modifiedBy = User.builder().id(dto.getModifiedBy()).build();
      } else {
        modifiedBy = this.userRepository.findById(dto.getModifiedBy());
      }
      entity.setModifiedBy(modifiedBy);
    }

    if (dto.getOwner() != null) {
      final User owner;
      if (userRepository == null) {
        owner = User.builder().id(dto.getOwner()).build();
      } else {
        owner = this.userRepository.findById(dto.getOwner());
      }
      entity.setOwner(owner);
    }

    return entity;
  }

  /**
   * Convert a DTO List to an entity Collection matching the data of the given DTOs.
   * 
   * <p>
   * If repositories are attached to this bridge, relations to foreign objects should be retrieved
   * by their appropriate repository. If no repository is available for a relation, a dummy object
   * containing only the identifier of the foreign object should be returned instead.
   * </p>
   * 
   * @param dtos the DTOs to convert to entities.
   * 
   * @return entities matching the data of the given DTO.
   * 
   * @throws NullPointerException if {@code dtos} is {@code null}, contains {@code null}, or if
   *         {@link #buildEntity()} returned {@code null}.
   */
  public List<T> toEntity(final Collection<D> dtos) {
    final List<T> entities;

    if (dtos == null) {
      entities = Collections.emptyList();
    } else {
      entities = new ArrayList<>(dtos.size());
      dtos.forEach(dto -> entities.add(this.toEntity(dto)));
    }

    return entities;
  }

  /**
   * Convert an entity to a DTO matching the data of the given entity.
   * 
   * @param entity the entity to convert to an DTO.
   * 
   * @return a DTO matching the data of the given entity.
   * 
   * @throws NullPointerException if {@code entity} is {@code null}, or if {@link #buildDto()}
   *         returned {@code null}.
   */
  public D toDto(final T entity) {
    final D dto = this.buildDto();

    dto.setId(entity.getId());

    dto.setCreatedAt(entity.getCreatedAt());
    if (entity.getCreatedBy() != null) {
      dto.setCreatedBy(entity.getCreatedBy().getId());
    }

    dto.setModifiedAt(entity.getModifiedAt());
    if (entity.getModifiedBy() != null) {
      dto.setModifiedBy(entity.getModifiedBy().getId());
    }

    if (entity.getOwner() != null) {
      dto.setOwner(entity.getOwner().getId());
    }

    return dto;
  }

  /**
   * Convert entities to a DTO List matching the data of the given entities.
   * 
   * @param entities the entities to convert to an DTO.
   * 
   * @return a DTO List matching the data of the given entities.
   * 
   * @throws NullPointerException if {@code entity} is {@code null}, contains {@code null}, or if
   *         {@link #buildDto()} returned {@code null}.
   */
  public List<D> toDto(final Collection<T> entities) {
    final List<D> dtos;

    if (entities == null) {
      dtos = Collections.emptyList();
    } else {
      dtos = new ArrayList<>(entities.size());
      entities.forEach(entity -> dtos.add(this.toDto(entity)));
    }

    return dtos;
  }
}
