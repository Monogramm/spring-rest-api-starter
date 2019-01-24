/*
 * Creation by madmath03 the 2019-01-24.
 */

package com.monogramm.starter.persistence.media.service;

import com.monogramm.starter.dto.media.MediaDto;
import com.monogramm.starter.persistence.AbstractGenericBridge;
import com.monogramm.starter.persistence.media.entity.Media;
import com.monogramm.starter.persistence.user.dao.IUserRepository;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Bridge to convert a {@link MediaDto} to a {@link Media} and vice versa.
 * 
 * @author madmath03
 */
public class MediaBridge extends AbstractGenericBridge<Media, MediaDto> {

  /**
   * Create a {@link MediaBridge}.
   * 
   * <p>
   * <strong>Use with caution:</strong> this constructor will not set the @{code userRepository},
   * preventing any search in the Persistence Storage for the relations objects. This might be
   * dangerous when converting {@link #toEntity(MediaDto)} as no consistency check will be done but
   * it will definitely improve performance.
   * </p>
   * 
   */
  public MediaBridge() {
    super();
  }

  /**
   * Create a {@link MediaBridge}.
   * 
   * @param userRepository repository to lookup users.
   */
  @Autowired
  public MediaBridge(IUserRepository userRepository) {
    super(userRepository);
  }

  @Override
  protected Media buildEntity() {
    return new Media();
  }

  @Override
  protected MediaDto buildDto() {
    return new MediaDto();
  }

  @Override
  public Media toEntity(final MediaDto dto) {
    final Media entity = super.toEntity(dto);

    entity.setName(dto.getName());
    entity.setDescription(dto.getDescription());
    entity.setStartDate(dto.getStartDate());
    entity.setEndDate(dto.getEndDate());
    entity.setPath(dto.getPath());

    return entity;
  }

  @Override
  public MediaDto toDto(final Media entity) {
    final MediaDto dto = super.toDto(entity);

    dto.setName(entity.getName());
    dto.setDescription(entity.getDescription());
    dto.setStartDate(entity.getStartDate());
    dto.setEndDate(entity.getEndDate());
    dto.setPath(entity.getPath());

    return dto;
  }

}
