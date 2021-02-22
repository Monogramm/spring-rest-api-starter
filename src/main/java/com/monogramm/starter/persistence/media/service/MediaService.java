/*
 * Creation by madmath03 the 2019-01-24.
 */

package com.monogramm.starter.persistence.media.service;

import com.monogramm.starter.dto.media.MediaDto;
import com.monogramm.starter.persistence.GenericService;
import com.monogramm.starter.persistence.media.entity.Media;
import com.monogramm.starter.persistence.media.exception.MediaNotFoundException;
import com.monogramm.starter.persistence.user.entity.User;

import java.util.List;
import java.util.UUID;

import org.springframework.core.io.Resource;

/**
 * {@link Media} service interface.
 * 
 * @author madmath03
 */
public interface MediaService extends GenericService<Media, MediaDto> {

  @Override
  MediaBridge getBridge();

  /**
   * Load a media through its primary key.
   * 
   * @param entityId the media unique identifier.
   * 
   * @return the media content matching the identifier, or {@code null} if none matches.
   */
  public Resource loadById(UUID entityId);

  /**
   * Load a media through its primary key and owner id.
   * 
   * @param entityId the entity unique identifier.
   * @param ownerId the entity identifier of the entity owner.
   * 
   * @return the media content matching the identifier and owner, or {@code null} if none matches.
   */
  default Resource loadByIdAndOwner(final UUID entityId, final UUID ownerId) {
    final User owner = User.builder().id(ownerId).build();
    return this.loadByIdAndOwner(entityId, owner);
  }

  /**
   * Load a media through its primary key and owner id.
   * 
   * @param entityId the media unique identifier.
   * @param owner the entity owner.
   * 
   * @return the media content matching the identifier and owner, or {@code null} if none matches.
   */
  public Resource loadByIdAndOwner(UUID entityId, User owner);

  /**
   * Find all medias by their name while ignoring case.
   * 
   * @param name the name content to search.
   * 
   * @return the list of all the medias matching the search.
   */
  List<Media> findAllByName(final String name);

  /**
   * Find an media through its name while ignoring case.
   * 
   * @param name the name to search.
   * 
   * @return the media matching the name.
   * 
   * @throws MediaNotFoundException if no media matches the name in the repository.
   */
  Media findByName(final String name);
}
