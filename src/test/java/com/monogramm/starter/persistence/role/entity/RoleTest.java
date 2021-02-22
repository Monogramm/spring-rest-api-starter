/*
 * Creation by madmath03 the 2017-08-29.
 */

package com.monogramm.starter.persistence.role.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.monogramm.starter.persistence.AbstractGenericEntityTest;
import com.monogramm.starter.persistence.permission.entity.Permission;
import com.monogramm.starter.persistence.user.entity.User;

import java.util.Collections;
import java.util.UUID;

import org.junit.Test;

/**
 * {@link Role} Unit Test.
 * 
 * @author madmath03
 */
public class RoleTest extends AbstractGenericEntityTest<Role> {

  private static final String DISPLAYNAME = "Foo";

  @Override
  protected Role buildTestEntity() {
    return Role.builder().build();
  }

  protected Role buildTestEntity(final String name) {
    return Role.builder(name).build();
  }

  @Override
  public void testToJson() throws JsonProcessingException {
    super.testToJson();

    final User testUser = User.builder(this.getClass().toString()).id(UUID.randomUUID()).build();
    this.getEntity().addUser(testUser);
    assertNotNull(this.getEntity().toJson());

    testUser.setRole(this.getEntity());
    assertNotNull(this.getEntity().toJson());

    final Permission testPermission =
        Permission.builder(this.getClass().toString()).id(UUID.randomUUID()).build();
    this.getEntity().addPermission(testPermission);
    assertNotNull(this.getEntity().toJson());

    testPermission.addRole(this.getEntity());
    assertNotNull(this.getEntity().toJson());
  }

  @Override
  public void testToString() {
    super.testToString();

    final User testUser = new User(this.getClass().toString());
    this.getEntity().addUser(testUser);
    assertNotNull(this.getEntity().toString());

    testUser.setRole(this.getEntity());
    assertNotNull(this.getEntity().toString());

    final Permission testPermission = new Permission(this.getClass().toString());
    this.getEntity().addPermission(testPermission);
    assertNotNull(this.getEntity().toString());

    testPermission.addRole(this.getEntity());
    assertNotNull(this.getEntity().toString());
  }

  @Override
  public void testHashCode() {
    super.testHashCode();

    final Role copy = new Role(this.getEntity());

    assertEquals(copy.hashCode(), this.getEntity().hashCode());

    copy.setName(DISPLAYNAME);

    assertNotEquals(copy.hashCode(), this.getEntity().hashCode());

    copy.setName(null);

    assertEquals(copy.hashCode(), this.getEntity().hashCode());
  }

  @Override
  public void testEqualsObject() {
    super.testEqualsObject();

    final Role copy = new Role(this.getEntity());

    assertEquals(copy, this.getEntity());

    copy.setName(DISPLAYNAME);

    assertNotEquals(copy, this.getEntity());

    copy.setName(null);

    assertEquals(copy, this.getEntity());
  }

  /**
   * Test method for {@link Role#Role()}.
   */
  @Test
  public void testRole() {
    final Role role = new Role();

    assertNull(role.getName());
  }

  /**
   * Test method for {@link Role#Role(String)}.
   */
  @Test
  public void testRoleString() {
    final String name = "TEST";
    final Role role = new Role(name);

    assertEquals(name, role.getName());
  }

  /**
   * Test method for {@link Role#Role()}.
   */
  @Test
  public void testRoleRole() {
    new Role(this.getEntity());
  }

  /**
   * Test method for {@link Role#Role()}.
   */
  @Test(expected = NullPointerException.class)
  public void testRoleRoleNull() {
    new Role((Role) null);
  }

  /**
   * Test method for {@link Role#getName()}.
   */
  @Test
  public void testGetName() {
    assertNull(this.getEntity().getName());
  }

  /**
   * Test method for {@link Role#setName(java.lang.String)}.
   */
  @Test
  public void testSetName() {
    final String name = "TEST";

    this.getEntity().setName(name);

    assertEquals(name, this.getEntity().getName());
  }

  /**
   * Test method for {@link Role#update(Role)}.
   */
  @Test
  public void testUpdate() {
    super.testUpdate();

    final Role anotherEntity = this.buildTestEntity();

    this.getEntity().update(anotherEntity);

    assertNotSame(this.getEntity(), anotherEntity);

    assertNull(this.getEntity().getName());
    assertEquals(this.getEntity().getName(), anotherEntity.getName());
  }

  /**
   * Test method for {@link Role#update(Role)}.
   */
  @Test
  public void testUpdateName() {
    final Role anotherEntity = this.buildTestEntity(DISPLAYNAME);

    this.getEntity().update(anotherEntity);

    assertNotSame(this.getEntity(), anotherEntity);

    assertNotNull(this.getEntity().getName());
    assertEquals(this.getEntity().getName(), anotherEntity.getName());
  }

  /**
   * Test method for {@link Role#getUsers()}.
   */
  @Test
  public void testGetUsers() {
    assertNotNull(this.getEntity().getUsers().size());
    assertEquals(0, this.getEntity().getUsers().size());
  }

  /**
   * Test method for {@link Role#addUser(com.monogramm.starter.persistence.user.entity.User)}.
   */
  @Test
  public void testAddUser() {
    assertNotNull(this.getEntity().getUsers().size());
    assertEquals(0, this.getEntity().getUsers().size());

    final User user = User.builder(this.getClass().toString()).id(UUID.randomUUID()).build();
    assertTrue(this.getEntity().addUser(user));
    assertFalse(this.getEntity().addUser(user));
    assertNotNull(this.getEntity().getUsers().size());
    assertEquals(1, this.getEntity().getUsers().size());

    assertTrue(this.getEntity().addUser(User.builder().id(UUID.randomUUID()).build()));
    assertFalse(this.getEntity().addUser(user));
    assertNotNull(this.getEntity().getUsers().size());
    assertEquals(2, this.getEntity().getUsers().size());
  }

  /**
   * Test method for {@link Role#addUser(com.monogramm.starter.persistence.user.entity.User)}.
   */
  @Test(expected = NullPointerException.class)
  public void testAddUserNull() {
    assertNotNull(this.getEntity().getUsers().size());
    assertEquals(0, this.getEntity().getUsers().size());

    this.getEntity().addUser(null);
  }

  /**
   * Test method for {@link Role#addUsers(java.util.Collection)}.
   */
  @Test
  public void testAddUsers() {
    assertNotNull(this.getEntity().getUsers().size());
    assertEquals(0, this.getEntity().getUsers().size());

    final User user = User.builder(this.getClass().toString()).id(UUID.randomUUID()).build();
    assertTrue(this.getEntity().addUsers(Collections.singleton(user)));
    assertFalse(this.getEntity().addUsers(Collections.singleton(user)));
    assertNotNull(this.getEntity().getUsers().size());
    assertEquals(1, this.getEntity().getUsers().size());

    assertTrue(this.getEntity()
        .addUsers(Collections.singleton(User.builder().id(UUID.randomUUID()).build())));
    assertFalse(this.getEntity().addUsers(Collections.singleton(user)));
    assertNotNull(this.getEntity().getUsers().size());
    assertEquals(2, this.getEntity().getUsers().size());
  }

  /**
   * Test method for {@link Role#addUsers(java.util.Collection)}.
   */
  @Test(expected = NullPointerException.class)
  public void testAddUsersNull() {
    assertNotNull(this.getEntity().getUsers().size());
    assertEquals(0, this.getEntity().getUsers().size());

    this.getEntity().addUsers(Collections.singleton(null));
  }

  /**
   * Test method for {@link Role#addUsers(java.util.Collection)}.
   */
  @Test(expected = NullPointerException.class)
  public void testAddUsersCollectionNull() {
    assertNotNull(this.getEntity().getUsers().size());
    assertEquals(0, this.getEntity().getUsers().size());

    this.getEntity().addUsers(null);
  }

  /**
   * Test method for {@link Role#removeUser(com.monogramm.starter.persistence.user.entity.User)}.
   */
  @Test
  public void testRemoveUser() {
    assertNotNull(this.getEntity().getUsers().size());
    assertEquals(0, this.getEntity().getUsers().size());

    final User user = User.builder(this.getClass().toString()).id(UUID.randomUUID()).build();
    this.getEntity().addUser(user);
    assertTrue(this.getEntity().removeUser(user));
    assertNotNull(this.getEntity().getUsers().size());
    assertEquals(0, this.getEntity().getUsers().size());

    this.getEntity().addUser(user);
    this.getEntity().addUser(User.builder().id(UUID.randomUUID()).build());
    assertTrue(this.getEntity().removeUser(user));
    assertNotNull(this.getEntity().getUsers().size());
    assertEquals(1, this.getEntity().getUsers().size());
  }

  /**
   * Test method for {@link Role#removeUser(com.monogramm.starter.persistence.user.entity.User)}.
   */
  @Test
  public void testRemoveUserNull() {
    assertNotNull(this.getEntity().getUsers().size());
    assertEquals(0, this.getEntity().getUsers().size());

    assertFalse(this.getEntity().removeUser(null));
  }

  /**
   * Test method for {@link Role#removeUsers(java.util.Collection)}.
   */
  @Test
  public void testRemoveUsers() {
    assertNotNull(this.getEntity().getUsers().size());
    assertEquals(0, this.getEntity().getUsers().size());

    final User user = User.builder(this.getClass().toString()).id(UUID.randomUUID()).build();
    this.getEntity().addUser(user);
    assertTrue(this.getEntity().removeUsers(Collections.singleton(user)));
    assertNotNull(this.getEntity().getUsers().size());
    assertEquals(0, this.getEntity().getUsers().size());

    this.getEntity().addUser(user);
    this.getEntity().addUser(User.builder().id(UUID.randomUUID()).build());
    assertTrue(this.getEntity().removeUsers(Collections.singleton(user)));
    assertNotNull(this.getEntity().getUsers().size());
    assertEquals(1, this.getEntity().getUsers().size());
  }

  /**
   * Test method for {@link Role#removeUsers(java.util.Collection)}.
   */
  @Test
  public void testRemoveUsersNull() {
    assertNotNull(this.getEntity().getUsers().size());
    assertEquals(0, this.getEntity().getUsers().size());

    assertFalse(this.getEntity().removeUsers(Collections.singleton(null)));
  }

  /**
   * Test method for {@link Role#removeUsers(java.util.Collection)}.
   */
  @Test(expected = NullPointerException.class)
  public void testRemoveUsersCollectionNull() {
    assertNotNull(this.getEntity().getUsers().size());
    assertEquals(0, this.getEntity().getUsers().size());

    this.getEntity().removeUsers(null);
  }

  /**
   * Test method for {@link Role#getPermissions()}.
   */
  @Test
  public void testGetPermissions() {
    assertNotNull(this.getEntity().getPermissions());
    assertTrue(this.getEntity().getPermissions().isEmpty());
  }

  /**
   * Test method for
   * {@link Role#addPermission(com.monogramm.starter.persistence.permission.entity.Permission)}.
   */
  @Test
  public void testAddPermission() {
    assertNotNull(this.getEntity().getPermissions());
    assertTrue(this.getEntity().getPermissions().isEmpty());

    final Permission permission =
        Permission.builder(this.getClass().toString()).id(UUID.randomUUID()).build();
    assertTrue(this.getEntity().addPermission(permission));
    assertFalse(this.getEntity().addPermission(permission));
    assertNotNull(this.getEntity().getPermissions());
    assertEquals(1, this.getEntity().getPermissions().size());

    assertTrue(this.getEntity().addPermission(Permission.builder().id(UUID.randomUUID()).build()));
    assertFalse(this.getEntity().addPermission(permission));
    assertNotNull(this.getEntity().getPermissions());
    assertEquals(2, this.getEntity().getPermissions().size());
  }

  /**
   * Test method for
   * {@link Role#addPermission(com.monogramm.starter.persistence.permission.entity.Permission)}.
   */
  @Test(expected = NullPointerException.class)
  public void testAddPermissionNull() {
    assertNotNull(this.getEntity().getPermissions());
    assertTrue(this.getEntity().getPermissions().isEmpty());

    this.getEntity().addPermission(null);
  }

  /**
   * Test method for {@link Role#addPermissions(java.util.Collection)}.
   */
  @Test
  public void testAddPermissions() {
    assertNotNull(this.getEntity().getPermissions());
    assertTrue(this.getEntity().getPermissions().isEmpty());

    final Permission permission =
        Permission.builder(this.getClass().toString()).id(UUID.randomUUID()).build();
    assertTrue(this.getEntity().addPermissions(Collections.singleton(permission)));
    assertFalse(this.getEntity().addPermissions(Collections.singleton(permission)));
    assertNotNull(this.getEntity().getPermissions());
    assertEquals(1, this.getEntity().getPermissions().size());

    assertTrue(this.getEntity()
        .addPermissions(Collections.singleton(Permission.builder().id(UUID.randomUUID()).build())));
    assertFalse(this.getEntity().addPermissions(Collections.singleton(permission)));
    assertNotNull(this.getEntity().getPermissions());
    assertEquals(2, this.getEntity().getPermissions().size());
  }

  /**
   * Test method for {@link Role#addPermissions(java.util.Collection)}.
   */
  @Test(expected = NullPointerException.class)
  public void testAddPermissionsNull() {
    assertNotNull(this.getEntity().getPermissions());
    assertTrue(this.getEntity().getPermissions().isEmpty());

    this.getEntity().addPermissions(Collections.singleton(null));
  }

  /**
   * Test method for {@link Role#addPermissions(java.util.Collection)}.
   */
  @Test(expected = NullPointerException.class)
  public void testAddPermissionsCollectionNull() {
    assertNotNull(this.getEntity().getPermissions());
    assertTrue(this.getEntity().getPermissions().isEmpty());

    this.getEntity().addPermissions(null);
  }

  /**
   * Test method for
   * {@link Role#removePermission(com.monogramm.starter.persistence.permission.entity.Permission)}.
   */
  @Test
  public void testRemovePermission() {
    assertNotNull(this.getEntity().getPermissions());
    assertTrue(this.getEntity().getPermissions().isEmpty());

    final Permission permission =
        Permission.builder(this.getClass().toString()).id(UUID.randomUUID()).build();
    this.getEntity().addPermission(permission);
    assertTrue(this.getEntity().removePermission(permission));
    assertNotNull(this.getEntity().getPermissions());
    assertTrue(this.getEntity().getPermissions().isEmpty());

    this.getEntity().addPermission(permission);
    this.getEntity().addPermission(Permission.builder().id(UUID.randomUUID()).build());
    assertTrue(this.getEntity().removePermission(permission));
    assertNotNull(this.getEntity().getPermissions());
    assertEquals(1, this.getEntity().getPermissions().size());
  }

  /**
   * Test method for
   * {@link Role#removePermission(com.monogramm.starter.persistence.permission.entity.Permission)}.
   */
  @Test
  public void testRemovePermissionNull() {
    assertNotNull(this.getEntity().getPermissions());
    assertTrue(this.getEntity().getPermissions().isEmpty());

    assertFalse(this.getEntity().removePermission(null));
  }

  /**
   * Test method for {@link Role#removePermissions(java.util.Collection)}.
   */
  @Test
  public void testRemovePermissions() {
    assertNotNull(this.getEntity().getPermissions());
    assertTrue(this.getEntity().getPermissions().isEmpty());

    final Permission permission =
        Permission.builder(this.getClass().toString()).id(UUID.randomUUID()).build();
    this.getEntity().addPermission(permission);
    assertTrue(this.getEntity().removePermissions(Collections.singleton(permission)));
    assertNotNull(this.getEntity().getPermissions());
    assertTrue(this.getEntity().getPermissions().isEmpty());

    this.getEntity().addPermission(permission);
    this.getEntity().addPermission(Permission.builder().id(UUID.randomUUID()).build());
    assertTrue(this.getEntity().removePermissions(Collections.singleton(permission)));
    assertNotNull(this.getEntity().getPermissions());
    assertEquals(1, this.getEntity().getPermissions().size());
  }

  /**
   * Test method for {@link Role#removePermissions(java.util.Collection)}.
   */
  @Test
  public void testRemovePermissionsNull() {
    assertNotNull(this.getEntity().getPermissions());
    assertTrue(this.getEntity().getPermissions().isEmpty());

    assertFalse(this.getEntity().removePermissions(Collections.singleton(null)));
  }

  /**
   * Test method for {@link Role#removePermissions(java.util.Collection)}.
   */
  @Test(expected = NullPointerException.class)
  public void testRemovePermissionsCollectionNull() {
    assertNotNull(this.getEntity().getPermissions());
    assertTrue(this.getEntity().getPermissions().isEmpty());

    this.getEntity().removePermissions(null);
  }

  /**
   * Test method for {@link Role#clearPermissions()}.
   */
  @Test
  public void testClearPermissions() {
    assertNotNull(this.getEntity().getPermissions());
    assertTrue(this.getEntity().getPermissions().isEmpty());

    this.getEntity().clearPermissions();
    assertNotNull(this.getEntity().getPermissions());
    assertTrue(this.getEntity().getPermissions().isEmpty());

    this.getEntity().addPermission(Permission.builder().id(UUID.randomUUID()).build());
    assertNotNull(this.getEntity().getPermissions());
    assertEquals(1, this.getEntity().getPermissions().size());

    this.getEntity().clearPermissions();
    assertNotNull(this.getEntity().getPermissions());
    assertTrue(this.getEntity().getPermissions().isEmpty());
  }

}
