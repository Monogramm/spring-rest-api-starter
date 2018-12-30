/*
 * Creation by madmath03 the 2017-12-04.
 */

package com.monogramm.starter.api;

import com.monogramm.starter.config.security.IAuthenticationFacade;
import com.monogramm.starter.dto.AbstractGenericDto;
import com.monogramm.starter.persistence.AbstractGenericEntity;
import com.monogramm.starter.persistence.EntityNotFoundException;
import com.monogramm.starter.persistence.GenericService;
import com.monogramm.starter.utils.validation.ValidUuid;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Generic Entity Controller.
 * 
 * <p>
 * Provides the typical CRUD operations. The implementation need to call (or override) the methods
 * here in order to map a a request path to each of them.
 * </p>
 * 
 * @param <T> the entity type used to manage data in persistence storage.
 * @param <D> the DTO type to convert entities to/from.
 * 
 * @author madmath03
 */
public abstract class AbstractGenericController<T extends AbstractGenericEntity,
    D extends AbstractGenericDto> {

  /**
   * Logger for {@link AbstractGenericController}.
   */
  private static final Logger LOG = LogManager.getLogger(AbstractGenericController.class);

  private final GenericService<T, D> service;

  /**
   * Create a {@link AbstractGenericController}.
   * 
   * @param service the entity service.
   */
  public AbstractGenericController(final GenericService<T, D> service) {
    super();
    this.service = service;
  }


  /**
   * Get the {@link #service}.
   * 
   * @return the {@link #service}.
   */
  protected GenericService<T, D> getService() {
    return service;
  }

  /**
   * Get the controller administration authorities.
   * 
   * <p>
   * This is used to secure entity modifications by ownership if authenticated user does not have
   * administration permissions.
   * </p>
   * 
   * @return the controller administration authorities.
   */
  protected abstract String[] getAdminAuthorities();

  /**
   * Get the controller base path.
   * 
   * <p>
   * This is used to return additional information like the path an entity which was created.
   * </p>
   * 
   * @return the controller base path.
   */
  protected abstract String getControllerPath();

  /**
   * Get a {@link T} entity by its unique identifier.
   * 
   * <p>
   * Returns a {@link D} JSON representation about a single data.
   * </p>
   * 
   * @param id <em>Required URL Path variable:</em> universal unique identifier (i.e. {@code UUID}).
   * 
   * @return
   *         <ul>
   * 
   *         <li>
   *         <p>
   *         <strong>Success Response:</strong>
   *         </p>
   * 
   *         <ul>
   *         <li>
   *         <p>
   *         <strong>Code:</strong> <code>HttpStatus.OK</code>
   *         </p>
   *         <p>
   *         <strong>Content:</strong> a {@link D} JSON representation of the entity {@link T}
   *         </p>
   *         </li>
   *         </ul>
   * 
   *         </li>
   * 
   *         <li>
   *         <p>
   *         <strong>Error Response:</strong>
   *         </p>
   * 
   *         <ul>
   *         <li>
   *         <p>
   *         <strong>Code:</strong> <code>HttpStatus.NOT_FOUND</code>
   *         </p>
   *         <p>
   *         <strong>Content:</strong> <code>null</code>
   *         </p>
   *         </li>
   *         </ul>
   * 
   *         </li>
   * 
   *         </ul>
   */
  public ResponseEntity<D> getDataById(@PathVariable @ValidUuid String id) {
    D dto = null;
    HttpStatus status;

    T entity;
    try {
      entity = this.service.findById(UUID.fromString(id));
    } catch (IllegalArgumentException e) {
      LOG.debug("getDataById(id=" + id + ")", e);
      entity = null;
    }

    if (entity == null) {
      status = HttpStatus.NOT_FOUND;
    } else {
      dto = this.service.toDto(entity);
      status = HttpStatus.OK;
    }

    return new ResponseEntity<>(dto, status);
  }

  /**
   * Get all available {@link T} entities.
   * 
   * <p>
   * Returns a {@link D} JSON representation about a data array.
   * </p>
   * 
   * @return
   *         <ul>
   * 
   *         <li>
   *         <p>
   *         <strong>Success Response:</strong>
   *         </p>
   * 
   *         <ul>
   *         <li>
   *         <p>
   *         <strong>Code:</strong> <code>HttpStatus.OK</code>
   *         </p>
   *         <p>
   *         <strong>Content:</strong> a {@link D} JSON representation of a {@link T} Array
   *         </p>
   *         </li>
   *         </ul>
   * 
   *         </li>
   * 
   *         </ul>
   */
  public ResponseEntity<List<D>> getAllData() {
    final List<D> result = service.toDto(service.findAll());

    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  /**
   * Add a {@link T}.
   * 
   * <p>
   * Create an entity and return a {@link D} JSON representation about the entity created.
   * </p>
   * 
   * @param dto <em>Required Body Content:</em> a {@link D} JSON representation about the {@link T}
   *        to create.
   * @param builder an URI builder to build the URI to the created {@link T} in the response.
   * 
   * @return
   *         <ul>
   * 
   *         <li>
   *         <p>
   *         <strong>Success Response:</strong>
   *         </p>
   * 
   *         <ul>
   *         <li>
   *         <p>
   *         <strong>Code:</strong> <code>HttpStatus.CREATED</code>
   *         </p>
   *         <p>
   *         <strong>Content:</strong> a {@link D} JSON representation of the {@link T} created
   *         </p>
   *         </li>
   *         </ul>
   * 
   *         </li>
   * 
   *         <li>
   *         <p>
   *         <strong>Error Response:</strong>
   *         </p>
   * 
   *         <ul>
   *         <li>
   *         <p>
   *         <strong>Code:</strong> <code>HttpStatus.CONFLICT</code>
   *         </p>
   *         <p>
   *         <strong>Content:</strong> <code>{}</code>
   *         </p>
   *         </li>
   *         </ul>
   * 
   *         </li>
   * 
   *         </ul>
   */
  public ResponseEntity<D> addData(@RequestBody D dto, UriComponentsBuilder builder) {
    final T entity = this.service.toEntity(dto);
    final boolean added = service.add(entity);

    final ResponseEntity<D> response;
    if (added) {
      final HttpHeaders headers = new HttpHeaders();
      headers.setLocation(
          builder.path(this.getControllerPath() + "/{id}").buildAndExpand(dto.getId()).toUri());

      response = new ResponseEntity<>(this.service.toDto(entity), headers, HttpStatus.CREATED);
    } else {
      response = new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    return response;
  }

  /**
   * Update a {@link T} entity.
   * 
   * <p>
   * Update an entity and return a {@link D} JSON representation about the entity updated.
   * </p>
   * 
   * @param authentication Authentication information. Should be automatically provided by Spring.
   * @param id <em>Required URL Path variable:</em> universal unique identifier ( i.e.
   *        {@code UUID}).
   * @param dto <em>Required Body Content:</em> a {@link D} JSON representation about the {@link T}
   *        to update.
   * 
   * @return
   *         <ul>
   * 
   *         <li>
   *         <p>
   *         <strong>Success Response:</strong>
   *         </p>
   * 
   *         <ul>
   *         <li>
   *         <p>
   *         <strong>Code:</strong> <code>HttpStatus.OK</code>
   *         </p>
   *         <p>
   *         <strong>Content:</strong> a {@link D} JSON representation of the {@link T} updated
   *         </p>
   *         </li>
   *         </ul>
   * 
   *         </li>
   * 
   *         <li>
   *         <p>
   *         <strong>Error Response:</strong>
   *         </p>
   * 
   *         <ul>
   *         <li>
   *         <p>
   *         <strong>Code:</strong> <code>HttpStatus.NOT_FOUND</code>
   *         </p>
   *         <p>
   *         <strong>Content:</strong> <code>{}</code>
   *         </p>
   *         </li>
   *         </ul>
   * 
   *         <p>
   *         OR
   *         </p>
   * 
   *         <ul>
   *         <li>
   *         <p>
   *         <strong>Code:</strong> <code>HttpStatus.BAD_REQUEST</code>
   *         </p>
   *         <p>
   *         <strong>Content:</strong> <code>{}</code>
   *         </p>
   *         </li>
   *         </ul>
   * 
   *         </li>
   * 
   *         </ul>
   */
  public ResponseEntity<D> updateData(Authentication authentication,
      @PathVariable @ValidUuid String id, @RequestBody D dto) {
    HttpStatus status;
    D updatedDto = null;

    try {
      if (dto == null || dto.getId() == null || !Objects.equals(id, dto.getId().toString())) {
        status = HttpStatus.BAD_REQUEST;
      } else {
        // Only update if owner or has administration authorities
        final T updatedEntity;

        final String[] adminAuthorities = this.getAdminAuthorities();
        if (adminAuthorities != null && adminAuthorities.length > 0
            && !IAuthenticationFacade.hasAnyAuthority(authentication, adminAuthorities)) {
          final UUID ownerId = IAuthenticationFacade.getPrincipalId(authentication);

          updatedEntity = service.updateByOwner(this.service.toEntity(dto), ownerId);
        } else {
          updatedEntity = service.update(this.service.toEntity(dto));
        }

        if (updatedEntity == null) {
          status = HttpStatus.NOT_FOUND;
        } else {
          updatedDto = this.service.toDto(updatedEntity);
          status = HttpStatus.OK;
        }
      }
    } catch (EntityNotFoundException e) {
      LOG.debug("updateData(id=" + id + ")", e);
      status = HttpStatus.NOT_FOUND;
    }

    return new ResponseEntity<>(updatedDto, status);
  }

  /**
   * Delete a {@link T}.
   * 
   * @param authentication Authentication information. Should be automatically provided by Spring.
   * @param id <em>Required URL Path variable:</em> universal unique identifier (i.e. {@code UUID}).
   * 
   * @return
   *         <ul>
   * 
   *         <li>
   *         <p>
   *         <strong>Success Response:</strong>
   *         </p>
   * 
   *         <ul>
   *         <li>
   *         <p>
   *         <strong>Code:</strong> <code>HttpStatus.NO_CONTENT</code>
   *         </p>
   *         <p>
   *         <strong>Content:</strong> <code>null</code>
   *         </p>
   *         </li>
   *         </ul>
   * 
   *         </li>
   * 
   *         <li>
   *         <p>
   *         <strong>Error Response:</strong>
   *         </p>
   * 
   *         <ul>
   *         <li>
   *         <p>
   *         <strong>Code:</strong> <code>HttpStatus.NOT_FOUND</code>
   *         </p>
   *         <p>
   *         <strong>Content:</strong> <code>null</code>
   *         </p>
   *         </li>
   *         </ul>
   * 
   *         </li>
   * 
   *         </ul>
   */
  public ResponseEntity<Void> deleteData(Authentication authentication,
      @PathVariable @ValidUuid String id) {
    HttpStatus status;

    try {
      // Only delete if owner or has administration authorities
      final String[] adminAuthorities = this.getAdminAuthorities();
      if (adminAuthorities != null && adminAuthorities.length > 0
          && !IAuthenticationFacade.hasAnyAuthority(authentication, adminAuthorities)) {
        final UUID ownerId = IAuthenticationFacade.getPrincipalId(authentication);

        service.deleteByIdAndOwner(UUID.fromString(id), ownerId);
      } else {
        service.deleteById(UUID.fromString(id));
      }

      status = HttpStatus.NO_CONTENT;
    } catch (EntityNotFoundException | IllegalArgumentException e) {
      LOG.debug("deleteData(id=" + id + ")", e);
      status = HttpStatus.NOT_FOUND;
    }

    return new ResponseEntity<>(status);
  }

}
