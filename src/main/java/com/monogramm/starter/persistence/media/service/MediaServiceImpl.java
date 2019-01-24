/*
 * Creation by madmath03 the 2019-01-24.
 */

package com.monogramm.starter.persistence.media.service;

import com.monogramm.starter.config.security.IAuthenticationFacade;
import com.monogramm.starter.dto.media.MediaDto;
import com.monogramm.starter.persistence.AbstractGenericService;
import com.monogramm.starter.persistence.media.dao.MediaRepository;
import com.monogramm.starter.persistence.media.entity.Media;
import com.monogramm.starter.persistence.media.exception.MediaNotFoundException;
import com.monogramm.starter.persistence.user.dao.IUserRepository;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
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

  /**
   * Create a {@link MediaService}.
   * 
   * @param repository the media repository.
   * @param userRepository the user repository.
   * @param authenticationFacade a facade to retrieve the authentication object.
   */
  @Autowired
  public MediaServiceImpl(MediaRepository repository, IUserRepository userRepository,
      IAuthenticationFacade authenticationFacade) {
    super(repository, userRepository, new MediaBridge(userRepository), authenticationFacade);
  }

  @Override
  protected MediaRepository getRepository() {
    return (MediaRepository) super.getRepository();
  }

  @Override
  public MediaBridge getBridge() {
    return (MediaBridge) super.getBridge();
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
