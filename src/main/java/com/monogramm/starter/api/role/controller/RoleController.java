package com.monogramm.starter.api.role.controller;

import com.monogramm.starter.api.AbstractGenericController;
import com.monogramm.starter.config.OAuth2WebSecurityConfig;
import com.monogramm.starter.config.data.GenericOperation;
import com.monogramm.starter.dto.role.RoleDto;
import com.monogramm.starter.persistence.role.entity.Role;
import com.monogramm.starter.persistence.role.service.IRoleService;
import com.monogramm.starter.utils.validation.ValidUuid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * The Role API Controller.
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
  public static final String CONTROLLER_PATH = '/' + TYPE;

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
   * Create a {@link RoleController}.
   * 
   * @param roleService the role service.
   */
  @Autowired
  public RoleController(IRoleService roleService) {
    super(roleService);
  }

  @Override
  protected String getControllerPath() {
    return CONTROLLER_PATH;
  }
  
  @Override
  protected IRoleService getService() {
    return (IRoleService) super.getService();
  }

  @Override
  @RequestMapping(value = CONTROLLER_PATH + "/{id}", method = RequestMethod.GET)
  @PreAuthorize(value = "hasAuthority('" + AUTH_READ + "')")
  public ResponseEntity<RoleDto> getDataById(@PathVariable @ValidUuid String id) {
    return super.getDataById(id);
  }

  @Override
  @RequestMapping(value = CONTROLLER_PATH, method = RequestMethod.GET)
  @PreAuthorize(value = "hasAuthority('" + AUTH_LIST + "')")
  public ResponseEntity<List<RoleDto>> getAllData() {
    return super.getAllData();
  }

  @Override
  @RequestMapping(value = CONTROLLER_PATH, method = RequestMethod.POST,
      consumes = "application/json")
  @PreAuthorize(value = "hasAuthority('" + AUTH_CREATE + "')")
  public ResponseEntity<RoleDto> addData(@RequestBody RoleDto dto, UriComponentsBuilder builder) {
    return super.addData(dto, builder);
  }

  @Override
  @RequestMapping(value = CONTROLLER_PATH + "/{id}", method = RequestMethod.PUT,
      consumes = "application/json")
  @PreAuthorize(value = "hasAuthority('" + AUTH_UPDATE + "')")
  public ResponseEntity<RoleDto> updateData(@PathVariable @ValidUuid String id,
      @RequestBody RoleDto dto) {
    return super.updateData(id, dto);
  }

  @Override
  @RequestMapping(value = CONTROLLER_PATH + "/{id}", method = RequestMethod.DELETE)
  @PreAuthorize(value = "hasAuthority('" + AUTH_DELETE + "')")
  public ResponseEntity<Void> deleteData(@PathVariable @ValidUuid String id) {
    return super.deleteData(id);
  }
}
