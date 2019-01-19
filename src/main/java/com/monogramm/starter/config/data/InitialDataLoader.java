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

  public static final String SAMPLE_DEMO_NAME = "demo";
  public static final String SAMPLE_DEMO_EMAIL = "demo@monogramm.io";
  public static final char[] SAMPLE_DEMO_PASSWORD = {'p', 'a', 's', 's', 'w', 'o', 'r', 'd'};

  public static final String SAMPLE_SUPPORT_NAME = "support";
  public static final String SAMPLE_SUPPORT_EMAIL = "support@monogramm.io";

  public static final String DEFAULT_ADMIN_NAME = "admin";
  public static final String DEFAULT_ADMIN_EMAIL = "admin@monogramm.io";

  private Type userType;
  private Type roleType;
  private Type typeType;
  private Type permissionType;
  private Type parameterType;

  private Role adminRole;
  private Role supportRole;
  private Role userRole;

  private User adminUser;

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
    boolean initDone = true;

    // Setup the initial types
    initDone &= this.initDefaultTypes();

    // Setup the initial roles
    initDone &= this.initDefaultRoles();

    // Setup permissions by role and type
    initDone &= this.initDefaultPermissions();

    // Setup the initial parameters
    initDone &= this.initDefaultParameters();

    // Setup the initial users
    initDone &= this.initDefaultUsers();

    return initDone;
  }

  private boolean initDefaultTypes() {
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

    return this.userType != null && this.roleType != null && this.typeType != null
        && this.permissionType != null && this.parameterType != null;
  }

  private boolean initDefaultRoles() {
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

    return this.adminRole != null && this.supportRole != null && this.userRole != null;
  }

  private boolean initDefaultPermissions() {
    boolean permissionsCreated = true;

    for (final Type type : this.getTypes()) {
      final Collection<Permission> permissions = this.createAllPermissions(type);
      permissionsCreated &= permissions != null && !permissions.isEmpty();
    }

    return permissionsCreated;
  }

  private boolean initDefaultParameters() {
    // XXX Create a parameter containing the application version
    // TODO Create a parameter to disable user registration at will

    return true;
  }

  private boolean initDefaultUsers() {
    // Create admin user
    final char[] adminPassword;
    final boolean logPassword;

    final String defaultAdminPassword =
        this.getEnv().getProperty("application.data.admin_password");
    if (defaultAdminPassword != null && !defaultAdminPassword.isEmpty()) {
      // Use default admin password if any defined in application properties
      adminPassword = defaultAdminPassword.toCharArray();
      logPassword = false;
    } else {
      // or generate a random password and log it
      adminPassword = Passwords.generateRandomPassword();
      logPassword = true;
    }

    this.adminUser = this.createUser(DEFAULT_ADMIN_NAME, DEFAULT_ADMIN_EMAIL, adminPassword,
        adminRole, logPassword);

    this.updateOwner(adminUser, adminUser, this.getUserService());

    return this.adminUser != null;
  }

  @Override
  public boolean initDemoData() {
    boolean initDone = true;

    initDone &= this.initDemoUsers();

    return initDone;
  }

  private boolean initDemoUsers() {
    final char[] supportPassword = Passwords.generateRandomPassword();
    final User supportUser =
        this.createUser(SAMPLE_SUPPORT_NAME, SAMPLE_SUPPORT_EMAIL, supportPassword, supportRole);

    final char[] demoPassword = SAMPLE_DEMO_PASSWORD.clone();
    final User demoUser =
        this.createUser(SAMPLE_DEMO_NAME, SAMPLE_DEMO_EMAIL, demoPassword, userRole);

    // Make users owner of their own account
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
