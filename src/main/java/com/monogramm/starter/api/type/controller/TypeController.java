package com.monogramm.starter.api.type.controller;

import com.monogramm.starter.api.AbstractGenericController;
import com.monogramm.starter.config.OAuth2WebSecurityConfig;
import com.monogramm.starter.config.data.GenericOperation;
import com.monogramm.starter.dto.type.TypeDto;
import com.monogramm.starter.persistence.type.entity.Type;
import com.monogramm.starter.persistence.type.exception.TypeNotFoundException;
import com.monogramm.starter.persistence.type.service.TypeService;
import com.monogramm.starter.utils.validation.ValidUuid;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
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
import org.springframework.web.util.UriComponentsBuilder;

/**
 * The {@link Type} API Controller.
 * 
 * @author madmath03
 */
@RestController
public class TypeController extends AbstractGenericController<Type, TypeDto> {
  /**
   * The main data type handled by this controller.
   */
  public static final String TYPE = "Types";
  /**
   * The request base path of this controller.
   */
  public static final String CONTROLLER_PATH = SEP + "types";

  /**
   * The Authority data type of this controller.
   */
  public static final String AUTH_TYPE = "TYPES";

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
   * Create a {@link TypeController}.
   * 
   * @param messageSource the i18n message source.
   * @param eventPublisher the event publisher.
   * @param typeService the type service.
   */
  @Autowired
  public TypeController(MessageSource messageSource, ApplicationEventPublisher eventPublisher,
      TypeService typeService) {
    super(messageSource, eventPublisher, typeService);
  }

  @Override
  protected TypeNotFoundException buildEntityNotFoundException(String id, WebRequest request) {
    final Locale locale = request.getLocale();
    final String msg = getMessageSource().getMessage("controller.entity_not_found",
        new String[] {Type.class.getSimpleName(), id}, locale);

    return new TypeNotFoundException(msg);
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
  protected TypeService getService() {
    return (TypeService) super.getService();
  }

  @Override
  @GetMapping(value = CONTROLLER_PATH + "/{id}")
  @PreAuthorize(value = "hasAuthority('" + AUTH_READ + "')")
  @PostAuthorize("hasAuthority('" + AUTH_LIST + "') || isOwner()")
  public TypeDto getDataById(@PathVariable @ValidUuid String id, WebRequest request,
      HttpServletResponse response) {
    return super.getDataById(id, request, response);
  }

  @Override
  @GetMapping(value = CONTROLLER_PATH)
  @PreAuthorize(value = "hasAuthority('" + AUTH_LIST + "')")
  public List<TypeDto> getAllData(
      @RequestParam(value = SORT, defaultValue = DEFAULT_SORT_QUERY) String sort) {
    return super.getAllData(sort);
  }

  @Override
  @GetMapping(value = CONTROLLER_PATH, params = {PAGE})
  @PreAuthorize(value = "hasAuthority('" + AUTH_LIST + "')")
  public List<TypeDto> getAllDataPaginated(
      @RequestParam(value = SORT, defaultValue = DEFAULT_SORT_QUERY) String sort,
      @RequestParam(value = PAGE) int page,
      @RequestParam(value = SIZE, defaultValue = DEFAULT_SIZE) int size, WebRequest request,
      UriComponentsBuilder builder, HttpServletResponse response) {
    return super.getAllDataPaginated(sort, page, size, request, builder, response);
  }

  @Override
  @PostMapping(value = CONTROLLER_PATH, consumes = "application/json")
  @PreAuthorize(value = "hasAuthority('" + AUTH_CREATE + "')")
  public ResponseEntity<TypeDto> addData(Authentication authentication, @RequestBody TypeDto dto,
      UriComponentsBuilder builder, HttpServletResponse response) {
    return super.addData(authentication, dto, builder, response);
  }

  @Override
  @PutMapping(value = CONTROLLER_PATH + "/{id}", consumes = "application/json")
  @PreAuthorize(value = "hasAuthority('" + AUTH_UPDATE + "')")
  public ResponseEntity<TypeDto> updateData(Authentication authentication,
      @PathVariable @ValidUuid String id, @RequestBody TypeDto dto) {
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
}
