/*
 * Creation by madmath03 the 2017-08-17.
 */

package com.monogramm.starter.api;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.jayway.awaitility.Awaitility;
import com.monogramm.starter.persistence.permission.entity.Permission;
import com.monogramm.starter.persistence.permission.exception.PermissionNotFoundException;
import com.monogramm.starter.persistence.permission.service.IPermissionService;
import com.monogramm.starter.persistence.role.entity.Role;
import com.monogramm.starter.persistence.role.exception.RoleNotFoundException;
import com.monogramm.starter.persistence.role.service.IRoleService;
import com.monogramm.starter.persistence.user.entity.User;
import com.monogramm.starter.persistence.user.exception.UserNotFoundException;
import com.monogramm.starter.persistence.user.service.IUserService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

/**
 * Integration Test abstract controller.
 * 
 * <p>
 * The Integration tests should be run on a freshly created database which only contains the initial
 * data.
 * </p>
 * 
 * @author madmath03
 */
@ActiveProfiles(profiles = {"it"})
public abstract class AbstractControllerIT {
  /**
   * Logger for {@link AbstractControllerFullIT}.
   */
  private static final Logger LOG = LogManager.getLogger(AbstractControllerIT.class);

  /**
   * The request path for login.
   */
  public static final String TOKEN_PATH = "/oauth/token";

  protected static final String ROLE_DISPLAYNAME = "IT-God";

  protected static final String USERNAME = "IT.God";
  protected static final String EMAIL = "it.god@creation.com";

  protected static final String PASSWORD_STR = "password";
  protected static final char[] PASSWORD = PASSWORD_STR.toCharArray();

  protected static final String PASSWORD_GRANT_TYPE = "password";

  protected static final String CLIENT_ID = "clientIdPassword";
  protected static final String CLIENT_SECRET = "secret";

  /**
   * Inner object writer for JSON conversion.
   */
  private static final ObjectWriter ow;

  static {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    ow = objectMapper.writer().withDefaultPrettyPrinter();
  }

  private static final char CHARACTER = 'a';



  @Autowired
  private IPermissionService permissionService;

  @Autowired
  private IRoleService roleService;

  private Role testRole;

  @Autowired
  private IUserService userService;

  private User testUser;


  /**
   * Create a {@link AbstractControllerIT}.
   * 
   */
  public AbstractControllerIT() {}



  /**
   * Convert any object to a JSON String.
   * 
   * @param object an object to convert.
   * @return a JSON String representation of the given object.
   * 
   * @throws JsonProcessingException if the conversion crashed.
   */
  public final String toJson(final Object object) throws JsonProcessingException {
    return ow.writeValueAsString(object);
  }

  /**
   * Convert any object to a JSON bytes array.
   * 
   * @param object an object to convert.
   * @return a JSON bytes array representation of the given object.
   * 
   * @throws JsonProcessingException if the conversion crashed.
   */
  public final byte[] toJsonBytes(Object object) throws JsonProcessingException {
    return ow.writeValueAsBytes(object);
  }

  /**
   * Generate a dummy String of the given size.
   * 
   * @param length size of the String to generate.
   * @return a dummy String of the given size.
   */
  public final String createStringWithLength(final int length) {
    final StringBuilder builder = new StringBuilder();

    for (int index = 0; index < length; index++) {
      builder.append(CHARACTER);
    }

    return builder.toString();
  }

  /**
   * Pause the current thread until the condition is true or the given amount of time is reached.
   * 
   * @param timeout the timeout
   * @param unit the unit
   * @param condition the condition that is responsible for executing the assertion and throwing
   *        AssertionError on failure.
   */
  protected final void pause(final long timeout, final TimeUnit unit, final Runnable condition) {
    Awaitility.await().atMost(timeout, unit).until(condition);
  }



  /**
   * Set up a valid User with a role containing the given permissions.
   * 
   * @see #tearDownValidUser()
   * 
   * @param mock if mock permissions should be created instead of retrieving them from persistence
   *        storage.
   * @param permissions the permissions name to give to the user role.
   * 
   * @throws PermissionNotFoundException if any of the permissions does not exist.
   * @throws UserNotFoundException if the user was not created properly.
   */
  protected void setUpValidUser(final String... permissions) {
    // Add a role
    testRole = this.createRole(ROLE_DISPLAYNAME, permissions);
    assertNotNull(testRole);

    // Add a user and verify it
    testUser = this.createUser(USERNAME, EMAIL, PASSWORD, testRole);
    assertNotNull(testUser);
  }

  /**
   * Get the permissions from the given names.
   * 
   * @param permissions the permissions name.
   * 
   * @return the collection of permissions matching the given names.
   * 
   * @throws PermissionNotFoundException if any of the permissions does not exist.
   */
  protected final Collection<Permission> getPermissions(final String... permissions) {
    // Find or create the asked permissions
    final Collection<Permission> entityPermissions = new ArrayList<>();

    if (permissions != null) {
      for (String permissionName : permissions) {
        final Permission permission = permissionService.findByName(permissionName);

        assertNotNull(permission);

        entityPermissions.add(permission);
      }
    }

    return entityPermissions;
  }

  /**
   * Create a role with the given permissions.
   * 
   * <p>
   * Make sure to delete the role at the end of your test!
   * </p>
   * 
   * @see #getPermissions(String...)
   * 
   * @param name the role name.
   * @param permissions the permissions name.
   * 
   * @return a role with the given permissions.
   * 
   * @throws PermissionNotFoundException if any of the permissions does not exist.
   */
  protected final Role createRole(final String name, final String... permissions) {
    final Collection<Permission> rolePermissions = getPermissions(permissions);

    // Add a role
    final Role role = Role.builder(name).permissions(rolePermissions).build();
    assertTrue(roleService.add(role));

    return role;
  }

  /**
   * Create a user the given role.
   * 
   * <p>
   * Make sure to delete the user at the end of your test!
   * </p>
   * 
   * @param username the user name.
   * @param email the user email.
   * @param password the user password.
   * @param role the role of the user.
   * 
   * @return a user the given role.
   * 
   * @throws UserNotFoundException if the user was not created properly.
   */
  protected final User createUser(final String username, final String email, char[] password,
      final Role role) {
    // Add a user and verify it
    final User user = User.builder(username, email).role(role).build();

    assertTrue(userService.add(user));
    if (password != null) {
      assertNotNull(userService.setPassword(user.getId(), password.clone()));
      assertNotNull(userService.enable(user.getId()));
      assertNotNull(userService.verify(user.getId()));
    }

    return user;
  }

  /**
   * Deletes the test user and test role that might have been created by a call to
   * {@link #setUpValidUser(String...)}.
   */
  protected void tearDownValidUser() {
    this.deleteUser(testUser);
    testUser = null;

    this.deleteRole(testRole);
    testRole = null;
  }

  /**
   * Delete a user from database.
   * 
   * @param user the user to delete.
   */
  protected final void deleteUser(final User user) {
    try {
      if (user != null && user.getId() != null) {
        userService.deleteById(user.getId());
      }
    } catch (UserNotFoundException e) {
      LOG.trace("User already deleted: " + user, e);
    }
  }

  /**
   * Delete a role from database.
   * 
   * @param role the role to delete.
   */
  protected final void deleteRole(final Role role) {
    try {
      if (role != null && role.getId() != null) {
        roleService.deleteById(role.getId());
      }
    } catch (RoleNotFoundException e) {
      LOG.trace("Role already deleted: " + role, e);
    }
  }


  /**
   * Return a default HTTP header.
   * 
   * @return a default HTTP header.
   */
  protected static HttpHeaders getHeaders() {
    return getHeaders(null);
  }

  /**
   * Return a default HTTP header.
   * 
   * @param accessToken an authenticated user access token.
   * 
   * @return a default HTTP header.
   */
  protected static HttpHeaders getHeaders(final String accessToken) {
    final HttpHeaders headers = new HttpHeaders();

    headers.setContentType(MediaType.APPLICATION_JSON);
    if (accessToken != null) {
      headers.add("Authorization", "Bearer " + accessToken);
    }

    return headers;
  }



  /**
   * Get the {@link #permissionService}.
   * 
   * @return the {@link #permissionService}.
   */
  protected final IPermissionService getPermissionService() {
    return permissionService;
  }

  /**
   * Get the {@link #roleService}.
   * 
   * @return the {@link #roleService}.
   */
  protected final IRoleService getRoleService() {
    return roleService;
  }

  /**
   * Get the {@link #testRole}.
   * 
   * @return the {@link #testRole}.
   */
  protected final Role getTestRole() {
    return testRole;
  }

  /**
   * Get the {@link #userService}.
   * 
   * @return the {@link #userService}.
   */
  protected final IUserService getUserService() {
    return userService;
  }

  /**
   * Get the {@link #testUser}.
   * 
   * @return the {@link #testUser}.
   */
  protected final User getTestUser() {
    return testUser;
  }
}
