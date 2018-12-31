/*
 * Creation by madmath03 the 2017-11-11.
 */

package com.monogramm.starter.config.data;

import com.github.madmath03.password.Passwords;
import com.monogramm.starter.api.parameter.controller.ParameterController;
import com.monogramm.starter.api.permission.controller.PermissionController;
import com.monogramm.starter.api.role.controller.RoleController;
import com.monogramm.starter.api.type.controller.TypeController;
import com.monogramm.starter.api.user.controller.UserController;
import com.monogramm.starter.persistence.parameter.service.IParameterService;
import com.monogramm.starter.persistence.permission.entity.Permission;
import com.monogramm.starter.persistence.permission.service.IPermissionService;
import com.monogramm.starter.persistence.role.entity.Role;
import com.monogramm.starter.persistence.role.service.IRoleService;
import com.monogramm.starter.persistence.type.entity.Type;
import com.monogramm.starter.persistence.type.service.ITypeService;
import com.monogramm.starter.persistence.user.entity.User;
import com.monogramm.starter.persistence.user.service.IUserService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Initial Data Loader.
 * 
 * <p>
 * Sets up the initial data configuration for the API to work properly.
 * </p>
 * 
 * @author madmath03
 */
@Component
public class InitialDataLoader extends AbstractDataLoader {

  public static final String USER_ROLE = "User";
  public static final String SUPPORT_ROLE = "Support";
  public static final String ADMIN_ROLE = "Admin";

  public static final String SAMPLE_USER_NAME = "demo";
  public static final String SAMPLE_USER_EMAIL = "demo@monogramm.io";
  public static final char[] SAMPLE_USER_PASSWORD = {'p', 'a', 's', 's', 'w', 'o', 'r', 'd'};

  public static final String SAMPLE_SUPPORT_NAME = "support";
  public static final String SAMPLE_SUPPORT_EMAIL = "support@monogramm.io";

  public static final String SAMPLE_ADMIN_NAME = "admin";
  public static final String SAMPLE_ADMIN_EMAIL = "admin@monogramm.io";

  private Type userType;
  private Type roleType;
  private Type typeType;
  private Type permissionType;
  private Type parameterType;

  private Role adminRole;
  private Role supportRole;
  private Role userRole;


  private final Map<Type, Map<GenericOperation, Set<Role>>> typePermissions = new HashMap<>();


  /**
   * Create a {@link InitialDataLoader}.
   * 
   * @param env application environment properties.
   * @param messageSource the messages i8n source.
   * @param userService the user service.
   * @param roleService the role service.
   * @param permissionService the permission service.
   * @param typeService the type service.
   * @param parameterService the parameters service.
   */
  @Autowired
  public InitialDataLoader(Environment env, MessageSource messageSource, IUserService userService,
      IRoleService roleService, IPermissionService permissionService, ITypeService typeService,
      IParameterService parameterService) {
    super(env, messageSource, userService, roleService, permissionService, typeService,
        parameterService);
  }

  @Override
  public boolean initDefaultData() {

    // Setup the initial roles
    if (this.userType == null) {
      this.userType = this.createType(UserController.TYPE);
    }

    if (this.roleType == null) {
      this.roleType = this.createType(RoleController.TYPE);
    }

    if (this.typeType == null) {
      this.typeType = this.createType(TypeController.TYPE);
    }

    if (this.permissionType == null) {
      this.permissionType = this.createType(PermissionController.TYPE);
    }

    if (this.parameterType == null) {
      this.parameterType = this.createType(ParameterController.TYPE);
    }



    // Setup the initial roles
    if (this.adminRole == null) {
      this.adminRole = this.createRole(ADMIN_ROLE);
      this.addAllPermissions(userType, adminRole);
      this.addAllPermissions(roleType, adminRole);
      this.addAllPermissions(typeType, adminRole);
      this.addAllPermissions(permissionType, adminRole);
      this.addAllPermissions(parameterType, adminRole);
    }

    if (this.supportRole == null) {
      this.supportRole = this.createRole(SUPPORT_ROLE);
      this.addAllPermissions(userType, supportRole);
      // Construct regular operations arrays
      final GenericOperation[] supportOperations = {GenericOperation.READ, GenericOperation.LIST};
      this.addAllPermissions(roleType, supportOperations, supportRole);
      this.addAllPermissions(typeType, supportOperations, supportRole);
      this.addAllPermissions(permissionType, supportOperations, supportRole);
      this.addAllPermissions(parameterType, supportOperations, supportRole);
    }

    if (this.userRole == null) {
      this.userRole = this.createRole(USER_ROLE);
      this.addPermission(userType, GenericOperation.READ, userRole);
      this.addPermission(userType, GenericOperation.UPDATE, userRole);
    }



    // Setup permissions by role and type
    for (final Type type : this.getTypes()) {
      this.createAllPermissions(type);
    }

    return true;
  }

  @Override
  public boolean initDemoData() {
    final char[] adminPassword = Passwords.generateRandomPassword();
    final User adminUser =
        this.createUser(SAMPLE_ADMIN_NAME, SAMPLE_ADMIN_EMAIL, adminPassword, adminRole);

    final char[] supportPassword = Passwords.generateRandomPassword();
    final User supportUser =
        this.createUser(SAMPLE_SUPPORT_NAME, SAMPLE_SUPPORT_EMAIL, supportPassword, supportRole);

    final char[] demoPassword = SAMPLE_USER_PASSWORD.clone();
    final User demoUser =
        this.createUser(SAMPLE_USER_NAME, SAMPLE_USER_EMAIL, demoPassword, userRole);



    // Update data owner
    for (final Type type : this.getTypes()) {
      this.updateOwner(type, adminUser, this.getTypeService());
    }

    for (final Permission permission : this.getPermissions()) {
      this.updateOwner(permission, adminUser, this.getPermissionService());
    }

    for (final Role role : this.getRoles()) {
      this.updateOwner(role, adminUser, this.getRoleService());
    }

    this.updateOwner(adminUser, adminUser, this.getUserService());
    this.updateOwner(supportUser, supportUser, this.getUserService());
    this.updateOwner(demoUser, demoUser, this.getUserService());

    return true;
  }

  /**
   * Add a role associated to all existing operations for the given type.
   * 
   * @param type a type to which the role must be granted operations.
   * @param role a role to add.
   */
  private void addAllPermissions(final Type type, final Role role) {
    addAllPermissions(type, GenericOperation.values(), role);
  }

  /**
   * Add a role associated to all existing operations for the given type.
   * 
   * @param type a type to which the role must be granted operations.
   * @param operations operations to grant to the role.
   * @param role a role to add.
   * 
   * @throws NullPointerException if the {@code values}
   */
  private void addAllPermissions(final Type type, final GenericOperation[] operations,
      final Role role) {
    for (GenericOperation operation : operations) {
      this.addPermission(type, operation, role);
    }
  }

  /**
   * Add a role associated to this operation for the given type.
   * 
   * @param type a type to which the role must be granted operation.
   * @param role a role to add.
   */
  private void addPermission(final Type type, final GenericOperation operation, final Role role) {
    final Map<GenericOperation, Set<Role>> operationRoles =
        this.typePermissions.computeIfAbsent(type, t -> new HashMap<>());

    final Set<Role> roles = operationRoles.computeIfAbsent(operation, t -> new HashSet<>());

    roles.add(role);
  }

  /**
   * Create the generic permissions for a type.
   * 
   * @param type a type for which to create generic permissions.
   * 
   * @return the permissions created.
   */
  protected Collection<Permission> createAllPermissions(final Type type) {
    final Collection<Permission> allPermissions = new ArrayList<>();

    for (final Entry<GenericOperation, Set<Role>> operationRoles : this.typePermissions.get(type)
        .entrySet()) {
      allPermissions.add(this.createPermission(operationRoles.getKey().permissionName(type),
          operationRoles.getValue()));
    }

    return allPermissions;
  }

}
