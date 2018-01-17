/*
 * Creation by madmath03 the 2017-12-22.
 */

package com.monogramm.starter.persistence;

import com.monogramm.starter.dto.AbstractGenericDto;
import com.monogramm.starter.persistence.user.dao.IUserRepository;

import java.util.List;
import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;

/**
 * Abstract Generic Service.
 * 
 * <p>
 * The {@link #exists(AbstractGenericEntity)} test may be overridden to match with the entity
 * functional uniqueness instead of the technical primary key existence.
 * </p>
 * 
 * @param <T> the entity type used to manage data in persistence storage.
 * @param <D> the DTO type to convert entities to/from.
 * 
 * @author madmath03
 */
public abstract class AbstractGenericService<T extends AbstractGenericEntity,
    D extends AbstractGenericDto> implements GenericService<T, D> {

  private final GenericRepository<T> repository;

  private final IUserRepository userRepository;

  private final AbstractGenericBridge<T, D> bridge;

  /**
   * Create a {@link AbstractGenericService}.
   * 
   * @param repository The Entity Data Access Object (DAO).
   * @param userRepository The User Entity Data Access Object (DAO).
   * @param bridge a bridge to convert this DTO to an entity and vice versa.
   * 
   * @throws IllegalArgumentException if {@code repository} or {@code bridge} is {@code null}.
   */
  protected AbstractGenericService(GenericRepository<T> repository,
      final IUserRepository userRepository, AbstractGenericBridge<T, D> bridge) {
    super();
    if (repository == null || userRepository == null || bridge == null) {
      throw new IllegalArgumentException("Repositories and bridge cannot be null.");
    }
    this.repository = repository;
    this.userRepository = userRepository;
    this.bridge = bridge;
  }

  /**
   * Create an "<em>entity not found</em>" for a given entity.
   * 
   * @param entity the entity which was not found by the persistence layer.
   * 
   * @return an "<em>entity not found</em>" for a given entity.
   */
  protected abstract EntityNotFoundException createEntityNotFoundException(T entity);

  /**
   * Create an "<em>entity not found</em>" for a given entity ID.
   * 
   * @param entityId the entity ID which was not found by the persistence layer.
   * 
   * @return an "<em>entity not found</em>" for a given entity ID.
   */
  protected abstract EntityNotFoundException createEntityNotFoundException(UUID entityId);

  /**
   * Tests if an entity exists in the repository.
   * 
   * @param entity the entity to check existence in the repository.
   * 
   * @return {@code true} if entity exists, {@code false} otherwise.
   */
  protected boolean exists(T entity) {
    return repository.exists(entity.getId());
  }

  /**
   * Get the {@link #repository}.
   * 
   * @return the {@link #repository}.
   */
  protected GenericRepository<T> getRepository() {
    return repository;
  }

  /**
   * Get the {@link #userRepository}.
   * 
   * @return the {@link #userRepository}.
   */
  protected final IUserRepository getUserRepository() {
    return userRepository;
  }

  @Override
  public AbstractGenericBridge<T, D> getBridge() {
    return bridge;
  }

  @Override
  public List<T> findAll() {
    return repository.findAll();
  }

  @Override
  public T findById(UUID entityId) {
    return repository.findById(entityId);
  }

  @Override
  public boolean add(T entity) {
    final boolean added;

    if (this.exists(entity)) {
      added = false;
    } else {
      repository.add(entity);
      added = true;
    }

    return added;
  }

  @Override
  @Transactional(rollbackFor = {EntityNotFoundException.class})
  public T update(T entity) {
    final T updatedEntity = repository.update(entity);

    if (updatedEntity == null) {
      throw this.createEntityNotFoundException(entity);
    }

    return updatedEntity;
  }

  @Override
  @Transactional(rollbackFor = {EntityNotFoundException.class})
  public void deleteById(UUID entityId) {
    final Integer deleted = repository.deleteById(entityId);

    if (deleted == null || deleted == 0) {
      throw this.createEntityNotFoundException(entityId);
    }
  }

}
