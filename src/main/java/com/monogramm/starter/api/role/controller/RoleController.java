package com.monogramm.starter.api.role.controller;

import com.monogramm.starter.api.AbstractGenericController;
import com.monogramm.starter.config.OAuth2WebSecurityConfig;
import com.monogramm.starter.config.data.GenericOperation;
import com.monogramm.starter.dto.role.RoleDto;
import com.monogramm.starter.persistence.role.entity.Role;
import com.monogramm.starter.persistence.role.exception.RoleNotFoundException;
import com.monogramm.starter.persistence.role.service.RoleService;
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
 * The {@link Role} API Controller.
 * 
 * @author madmath03
 */
@RestController
public class RoleController extends AbstractGenericController<Role, RoleDto> {
  /**
   * The main data type handled by this controller.
   */
  public static final String TYPE = "Roles";
  /**
   * The request base path of this controller.
   */
  public static final String CONTROLLER_PATH = SEP + "roles";

  /**
   * The Authority data type of this controller.
   */
  public static final String AUTH_TYPE = "ROLES";

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
   * Create a {@link RoleController}.
   * 
   * @param messageSource the i18n message source.
   * @param eventPublisher the event publisher.
   * @param roleService the role service.
   */
  @Autowired
  public RoleController(MessageSource messageSource, ApplicationEventPublisher eventPublisher,
      RoleService roleService) {
    super(messageSource, eventPublisher, roleService);
  }

  @Override
  protected RoleNotFoundException buildEntityNotFoundException(String id, WebRequest request) {
    final Locale locale = request.getLocale();
    final String msg = getMessageSource().getMessage("controller.entity_not_found",
        new String[] {Role.class.getSimpleName(), id}, locale);

    return new RoleNotFoundException(msg);
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
  protected RoleService getService() {
    return (RoleService) super.getService();
  }

  @Override
  @GetMapping(value = CONTROLLER_PATH + "/{id}")
  @PreAuthorize(value = "hasAuthority('" + AUTH_READ + "')")
  @PostAuthorize("hasAuthority('" + AUTH_LIST + "') || isOwner()")
  public RoleDto getDataById(@PathVariable @ValidUuid String id, WebRequest request,
      HttpServletResponse response) {
    return super.getDataById(id, request, response);
  }

  @Override
  @GetMapping(value = CONTROLLER_PATH)
  @PreAuthorize(value = "hasAnyAuthority('" + AUTH_LIST + "', '" + AUTH_READ + "')")
  public List<RoleDto> getAllData(
      @RequestParam(value = SORT, defaultValue = DEFAULT_SORT_QUERY) String sort,
      Authentication authentication) {
    return super.getAllData(sort, authentication);
  }

  @Override
  @GetMapping(value = CONTROLLER_PATH, params = {PAGE})
  @PreAuthorize(value = "hasAnyAuthority('" + AUTH_LIST + "', '" + AUTH_READ + "')")
  public List<RoleDto> getAllDataPaginated(
      @RequestParam(value = SORT, defaultValue = DEFAULT_SORT_QUERY) String sort,
      @RequestParam(value = PAGE) int page,
      @RequestParam(value = SIZE, defaultValue = DEFAULT_SIZE) int size,
      Authentication authentication, WebRequest request, UriComponentsBuilder builder,
      HttpServletResponse response) {
    return super.getAllDataPaginated(sort, page, size, authentication, request, builder, response);
  }

  @Override
  @PostMapping(value = CONTROLLER_PATH, consumes = "application/json")
  @PreAuthorize(value = "hasAuthority('" + AUTH_CREATE + "')")
  public ResponseEntity<RoleDto> addData(Authentication authentication, @RequestBody RoleDto dto,
      UriComponentsBuilder builder, HttpServletResponse response) {
    return super.addData(authentication, dto, builder, response);
  }

  @Override
  @PutMapping(value = CONTROLLER_PATH + "/{id}", consumes = "application/json")
  @PreAuthorize(value = "hasAuthority('" + AUTH_UPDATE + "')")
  public ResponseEntity<RoleDto> updateData(Authentication authentication,
      @PathVariable @ValidUuid String id, @RequestBody RoleDto dto) {
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
