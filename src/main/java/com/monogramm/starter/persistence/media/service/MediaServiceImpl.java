/*
 * Creation by madmath03 the 2019-01-24.
 */

package com.monogramm.starter.persistence.media.service;

import com.monogramm.starter.config.security.IAuthenticationFacade;
import com.monogramm.starter.dto.media.MediaDto;
import com.monogramm.starter.persistence.AbstractGenericService;
import com.monogramm.starter.persistence.EntityNotFoundException;
import com.monogramm.starter.persistence.media.dao.MediaRepository;
import com.monogramm.starter.persistence.media.entity.Media;
import com.monogramm.starter.persistence.media.exception.MediaNotFoundException;
import com.monogramm.starter.persistence.media.exception.MediaStorageException;
import com.monogramm.starter.persistence.media.properties.FileStorageProperties;
import com.monogramm.starter.persistence.user.dao.UserRepository;
import com.monogramm.starter.persistence.user.entity.User;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * {@link Media} service.
 * 
 * @author madmath03
 */
@Service
public class MediaServiceImpl extends AbstractGenericService<Media, MediaDto>
    implements MediaService {

  private final FileStorageProperties fileProperties;

  /**
   * Create a {@link MediaService}.
   * 
   * @param repository the media repository.
   * @param userRepository the user repository.
   * @param authenticationFacade a facade to retrieve the authentication object.
   * @param fileProperties file storage properties
   */
  @Autowired
  public MediaServiceImpl(MediaRepository repository, UserRepository userRepository,
      IAuthenticationFacade authenticationFacade, final FileStorageProperties fileProperties) {
    super(repository, userRepository, new MediaBridge(userRepository), authenticationFacade);

    if (fileProperties == null) {
      throw new IllegalArgumentException("File storage properties cannot be null!");
    }
    this.fileProperties = fileProperties;

    try {
      final Path storageLocation = this.getStorageLocation();
      Files.createDirectories(storageLocation);
    } catch (Exception e) {
      throw new MediaStorageException(
          "Could not create the directory where the uploaded files will be stored.", e);
    }
  }

  @Override
  protected MediaRepository getRepository() {
    return (MediaRepository) super.getRepository();
  }

  @Override
  public MediaBridge getBridge() {
    return (MediaBridge) super.getBridge();
  }

  /**
   * File storage location.
   * 
   * @return storage location.
   */
  protected Path getStorageLocation() {
    return this.fileProperties.getUploadDir().toAbsolutePath().normalize();
  }


  /**
   * Construct a path from storage location for specified entity.
   * 
   * <p>
   * Basically, we use the entity {@code UUID} as the containing folder.
   * </p>
   * 
   * @param entity entity
   * @param storageLocation storage location
   * 
   * @return the entity directory for the storage.
   * 
   * @throws NullPointerException if entity or entity UUID is {@code null}
   */
  protected static Path getEntityDirectory(final Media entity, final Path storageLocation) {
    return storageLocation.resolve(entity.getPath()).normalize();
  }

  /**
   * Construct a path from storage location for specified entity.
   * 
   * <p>
   * Basically, we use the entity {@code UUID} as the containing folder.
   * </p>
   * 
   * @param entity entity
   * 
   * @return the entity directory for the storage.
   * 
   * @throws NullPointerException if entity or entity UUID is {@code null}
   */
  protected Path getEntityDirectory(final Media entity) {
    final Path storageLocation = this.getStorageLocation();
    return getEntityDirectory(entity, storageLocation);
  }


  @Override
  public Resource loadById(UUID entityId) {
    final Media entity = this.findById(entityId);
    return loadFromStorage(entity);
  }

  @Override
  public Resource loadByIdAndOwner(UUID entityId, User owner) {
    final Media entity = this.findByIdAndOwner(entityId, owner);
    return loadFromStorage(entity);
  }

  private Resource loadFromStorage(final Media entity) {
    final Resource file;

    if (entity != null) {
      final Path mediaFolder = this.getEntityDirectory(entity);
      file = StorageUtils.loadFileAsResource(mediaFolder.resolve(entity.getName()));
    } else {
      file = null;
    }

    return file;
  }


  @Override
  @Transactional
  public boolean add(Media entity) {
    return addToRepositoryAndStorage(entity, entity.getInputStream());
  }

  private boolean addToRepositoryAndStorage(Media entity, InputStream inputStream) {
    final boolean persisted = super.add(entity);

    boolean stored;
    if (persisted) {
      final Path mediaFolder = this.getEntityDirectory(entity);
      stored = StorageUtils.storeFile(entity.getName(), inputStream, mediaFolder) != null;
      // If storage fails, the exception should trigger the rollback of persistence
    } else {
      stored = false;
    }

    return persisted && stored;
  }


  @Override
  @Transactional(rollbackFor = {EntityNotFoundException.class}, readOnly=false)
  public void deleteById(UUID entityId) {
    // Only delete if has administration authorities
    final Media entity = getRepository().findById(entityId);

    if (entity == null) {
      throw this.createEntityNotFoundException(entityId);
    }

    final Path mediaFolder = this.getEntityDirectory(entity);
    final Integer deleted = getRepository().deleteById(entityId);

    if (deleted == null || deleted == 0) {
      throw this.createEntityNotFoundException(entityId);
    } else {
      StorageUtils.deleteFile(mediaFolder);
      // If storage removal fails, the exception should trigger the rollback in database
    }
  }

  @Override
  @Transactional(rollbackFor = {EntityNotFoundException.class}, readOnly=false)
  public void deleteByIdAndOwner(UUID entityId, User owner) {
    // Only delete if has administration authorities
    final Media entity = getRepository().findByIdAndOwner(entityId, owner);

    if (entity == null) {
      throw this.createEntityNotFoundException(entityId);
    }

    final Path mediaFolder = this.getEntityDirectory(entity);
    getRepository().delete(entity);
    StorageUtils.deleteFile(mediaFolder);
    // If storage removal fails, the exception should trigger the rollback in database
  }


  @Override
  protected boolean exists(Media entity) {
    return getRepository().exists(entity.getId(), entity.getName());
  }

  @Override
  protected MediaNotFoundException createEntityNotFoundException(Media entity) {
    return new MediaNotFoundException("Following media not found:" + entity);
  }

  @Override
  protected MediaNotFoundException createEntityNotFoundException(UUID entityId) {
    return new MediaNotFoundException("No media for ID=" + entityId);
  }

  @Transactional(readOnly = true)
  @Override
  public Media findByName(final String name) {
    return getRepository().findByNameIgnoreCase(name);
  }

  @Transactional(readOnly = true)
  @Override
  public List<Media> findAllByName(final String name) {
    return getRepository().findAllContainingNameIgnoreCase(name);
  }

}
