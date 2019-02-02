/*
 * Creation by madmath03 the 2019-01-24.
 */

package com.monogramm.starter.api.media.controller;

import com.monogramm.starter.api.AbstractGenericController;
import com.monogramm.starter.config.OAuth2WebSecurityConfig;
import com.monogramm.starter.config.data.GenericOperation;
import com.monogramm.starter.dto.media.MediaDto;
import com.monogramm.starter.persistence.EntityNotFoundException;
import com.monogramm.starter.persistence.media.entity.Media;
import com.monogramm.starter.persistence.media.exception.MediaNotFoundException;
import com.monogramm.starter.persistence.media.service.MediaService;
import com.monogramm.starter.utils.validation.ValidUuid;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * The {@link Media} API Controller.
 * 
 * @author madmath03
 */
@RestController
public class MediaController extends AbstractGenericController<Media, MediaDto> {

  /**
   * Logger for {@link MediaController}.
   */
  private static final Logger LOG = LoggerFactory.getLogger(MediaController.class);

  /**
   * The main data type handled by this controller.
   */
  public static final String TYPE = "Media";
  /**
   * The request base path of this controller.
   */
  public static final String CONTROLLER_PATH = SEP + "media";
  /**
   * The download path of media files.
   */
  public static final String DOWNLOAD_PATH = CONTROLLER_PATH + SEP + "download";
  /**
   * The upload path of media files.
   */
  public static final String UPLOAD_PATH = CONTROLLER_PATH + SEP + "upload";

  /**
   * The Authority data type of this controller.
   */
  public static final String AUTH_TYPE = "MEDIA";

  /**
   * The Authority for the reading operations of this controller.
   * 
   * @see GenericOperation#READ
   */
  public static final String AUTH_READ = OAuth2WebSecurityConfig.AUTH_PREFIX + AUTH_TYPE
      + GenericOperation.PERM_SEP + GenericOperation.PERM_READ;
  /**
   * The Authority for the listing operations of this controller.
   * 
   * @see GenericOperation#LIST
   */
  public static final String AUTH_LIST = OAuth2WebSecurityConfig.AUTH_PREFIX + AUTH_TYPE
      + GenericOperation.PERM_SEP + GenericOperation.PERM_LIST;
  /**
   * The Authority for the creation operations of this controller.
   * 
   * @see GenericOperation#LIST
   */
  public static final String AUTH_CREATE = OAuth2WebSecurityConfig.AUTH_PREFIX + AUTH_TYPE
      + GenericOperation.PERM_SEP + GenericOperation.PERM_CREATE;
  /**
   * The Authority for the update operations of this controller.
   * 
   * @see GenericOperation#LIST
   */
  public static final String AUTH_UPDATE = OAuth2WebSecurityConfig.AUTH_PREFIX + AUTH_TYPE
      + GenericOperation.PERM_SEP + GenericOperation.PERM_UPDATE;
  /**
   * The Authority for the listing operations of this controller.
   * 
   * @see GenericOperation#LIST
   */
  public static final String AUTH_DELETE = OAuth2WebSecurityConfig.AUTH_PREFIX + AUTH_TYPE
      + GenericOperation.PERM_SEP + GenericOperation.PERM_DELETE;

  /**
   * The Authorities describing Administration permissions of this controller.
   */
  protected static final String[] ADMIN_AUTH = {AUTH_LIST};


  /**
   * Create a {@link MediaController}.
   * 
   * @param messageSource the i18n message source.
   * @param eventPublisher the event publisher.
   * @param mediaService the media service.
   */
  @Autowired
  public MediaController(MessageSource messageSource, ApplicationEventPublisher eventPublisher,
      MediaService mediaService) {
    super(messageSource, eventPublisher, mediaService);
  }

  @Override
  protected MediaNotFoundException buildEntityNotFoundException(String id, WebRequest request) {
    final Locale locale = request.getLocale();
    return this.buildEntityNotFoundException(id, locale);
  }

  protected MediaNotFoundException buildEntityNotFoundException(String id, ServletRequest request) {
    final Locale locale = request.getLocale();
    return this.buildEntityNotFoundException(id, locale);
  }

  protected MediaNotFoundException buildEntityNotFoundException(String id, Locale locale) {
    final String msg = getMessageSource().getMessage("controller.entity_not_found",
        new String[] {Media.class.getSimpleName(), id}, locale);

    return new MediaNotFoundException(msg);
  }

  @Override
  protected String[] getAdminAuthorities() {
    return ADMIN_AUTH;
  }

  @Override
  protected String getControllerPath() {
    return CONTROLLER_PATH;
  }

  @Override
  protected MediaService getService() {
    return (MediaService) super.getService();
  }

  @Override
  @GetMapping(value = CONTROLLER_PATH + "/{id}")
  @PreAuthorize(value = "hasAuthority('" + AUTH_READ + "')")
  @PostAuthorize("hasAuthority('" + AUTH_LIST + "') || isOwner()")
  public MediaDto getDataById(@PathVariable @ValidUuid String id, WebRequest request,
      HttpServletResponse response) {
    return super.getDataById(id, request, response);
  }

  @Override
  @GetMapping(value = CONTROLLER_PATH)
  @PreAuthorize(value = "hasAuthority('" + AUTH_LIST + "')")
  public List<MediaDto> getAllData(
      @RequestParam(value = SORT, defaultValue = DEFAULT_SORT_QUERY) String sort) {
    return super.getAllData(sort);
  }

  @Override
  @GetMapping(value = CONTROLLER_PATH, params = {PAGE})
  @PreAuthorize(value = "hasAuthority('" + AUTH_LIST + "')")
  public List<MediaDto> getAllDataPaginated(
      @RequestParam(value = SORT, defaultValue = DEFAULT_SORT_QUERY) String sort,
      @RequestParam(value = PAGE) int page,
      @RequestParam(value = SIZE, defaultValue = DEFAULT_SIZE) int size, WebRequest request,
      UriComponentsBuilder builder, HttpServletResponse response) {
    return super.getAllDataPaginated(sort, page, size, request, builder, response);
  }

  @Override
  @PutMapping(value = CONTROLLER_PATH + "/{id}", consumes = "application/json")
  @PreAuthorize(value = "hasAuthority('" + AUTH_UPDATE + "')")
  public ResponseEntity<MediaDto> updateData(Authentication authentication,
      @PathVariable @ValidUuid String id, @RequestBody MediaDto dto) {
    return super.updateData(authentication, id, dto);
  }

  @Override
  @DeleteMapping(value = CONTROLLER_PATH + "/{id}")
  @PreAuthorize(value = "hasAuthority('" + AUTH_DELETE + "')")
  public ResponseEntity<Void> deleteData(Authentication authentication,
      @PathVariable @ValidUuid String id) {
    return super.deleteData(authentication, id);
  }

  @Override
  @DeleteMapping(value = CONTROLLER_PATH, params = {IDS})
  @PreAuthorize(value = "hasAuthority('" + AUTH_DELETE + "')")
  public ResponseEntity<Void> deleteAllData(Authentication authentication,
      @RequestParam(value = IDS) String ids) {
    return super.deleteAllData(authentication, ids);
  }


  /**
   * Download a {@link Media} resource by its entity unique identifier.
   * 
   * <p>
   * Returns a {@code ResponseEntity<Resource>} of the Media file.
   * </p>
   * 
   * @param id <em>Required URL Path variable:</em> universal unique identifier (i.e. {@code UUID}).
   * @param request the Servlet Request.
   * @param response the HTTP response.
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
   *         <strong>Content:</strong> a {@code ResponseEntity<Resource>} of the Media file
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
   * 
   * @throws IllegalArgumentException if the specified is unique ID is not valid
   * @throws EntityNotFoundException if no entity matches the specified unique ID
   */
  @GetMapping(value = DOWNLOAD_PATH + "/{id}")
  @PreAuthorize(value = "permitAll()")
  public ResponseEntity<Resource> downloadMediaById(@PathVariable @ValidUuid String id,
      HttpServletRequest request, HttpServletResponse response) {
    // Convert ID
    final UUID uniqueId = UUID.fromString(id);

    // Search entity
    final Resource resource = this.getService().loadById(uniqueId);
    if (resource == null) {
      throw this.buildEntityNotFoundException(id, request);
    }

    // Try to determine file's content type
    String contentType = null;
    try {
      final ServletContext context = request.getServletContext();
      contentType = context.getMimeType(resource.getFile().getAbsolutePath());
    } catch (IOException ex) {
      LOG.debug("Could not determine file type.");
    }

    // Fallback to the default content type if type could not be determined
    if (contentType == null) {
      contentType = "application/octet-stream";
    }

    // Convert entity to ReponseEntity
    return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
        .header(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + resource.getFilename() + "\"")
        .body(resource);
  }


  private MediaDto dtoFromFile(MultipartFile file) {
    final MediaDto dto = new MediaDto();
    dto.setResource(file);

    return dto;
  }

  /**
   * Add multiple {@link Media}.
   * 
   * <p>
   * Create multiple Media and return a list of {@link MediaDto} the media created.
   * </p>
   * 
   * @param authentication Authentication information. Should be automatically provided by Spring.
   * @param files <em>Required Parameter:</em> files to upload.
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
   *         <strong>Content:</strong> a list of {@link MediaDto} of the {@link Media} created
   *         </p>
   *         </li>
   *         </ul>
   * 
   *         </li>
   * 
   *         </ul>
   */
  @PostMapping(value = UPLOAD_PATH, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @PreAuthorize(value = "hasAuthority('" + AUTH_CREATE + "')")
  public List<MediaDto> uploadMultipleMedia(Authentication authentication,
      @RequestParam("files") MultipartFile... files) {
    final List<MediaDto> dtos = new ArrayList<>(files.length);

    for (MultipartFile file : files) {
      final MediaDto dto = this.dtoFromFile(file);
      dtos.add(super.addData(authentication, dto));
    }

    return dtos;
  }

}
