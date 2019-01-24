/*
 * Creation by madmath03 the 2019-01-24.
 */

package com.monogramm.starter.persistence.media.service;

import com.monogramm.starter.dto.media.MediaDto;
import com.monogramm.starter.persistence.GenericService;
import com.monogramm.starter.persistence.media.entity.Media;
import com.monogramm.starter.persistence.media.exception.MediaNotFoundException;

import java.util.List;

/**
 * {@link Media} service interface.
 * 
 * @author madmath03
 */
public interface MediaService extends GenericService<Media, MediaDto> {

  @Override
  MediaBridge getBridge();

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
