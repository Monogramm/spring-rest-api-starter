/*
 * Creation by madmath03 the 2017-11-25
 */

package com.monogramm.starter.persistence.permission.entity;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.monogramm.starter.persistence.AbstractGenericEntityTest;
import com.monogramm.starter.persistence.permission.entity.Permission;
import com.monogramm.starter.persistence.role.entity.Role;

import java.util.Collections;
import java.util.UUID;

import org.junit.Test;

/**
 * {@link Permission} Unit Test.
 * 
 * @author madmath03
 */
public class PermissionTest extends AbstractGenericEntityTest<Permission> {

  private static final String DISPLAYNAME = "Foo";

  @Override
  protected Permission buildTestEntity() {
    return Permission.builder().build();
  }

  protected Permission buildTestEntity(final String name) {
    return Permission.builder(name).build();
  }

  @Override
  public void testToJson() throws JsonProcessingException {
    super.testToJson();

    final Role testRole = new Role(this.getClass().toString());
    this.getEntity().addRole(testRole);
    assertNotNull(this.getEntity().toJson());

    testRole.addPermission(this.getEntity());
    assertNotNull(this.getEntity().toJson());
  }

  @Override
  public void testToString() {
    super.testToString();

    final Role testRole = new Role(this.getClass().toString());
    this.getEntity().addRole(testRole);
    assertNotNull(this.getEntity().toString());

    testRole.addPermission(this.getEntity());
    assertNotNull(this.getEntity().toString());
  }

  @Override
  public void testHashCode() {
    super.testHashCode();

    final Permission copy = new Permission(this.getEntity());

    assertEquals(copy.hashCode(), this.getEntity().hashCode());

    copy.setName(DISPLAYNAME);

    assertNotEquals(copy.hashCode(), this.getEntity().hashCode());

    copy.setName(null);

    assertEquals(copy.hashCode(), this.getEntity().hashCode());
  }

  @Override
  public void testEqualsObject() {
    super.testEqualsObject();

    final Permission copy = new Permission(this.getEntity());

    assertEquals(copy, this.getEntity());

    copy.setName(DISPLAYNAME);

    assertNotEquals(copy, this.getEntity());

    copy.setName(null);

    assertEquals(copy, this.getEntity());
  }

  /**
   * Test method for {@link Permission#Permission()}.
   */
  @Test
  public void testPermission() {
    final Permission permission = new Permission();

    assertNull(permission.getName());
  }

  /**
   * Test method for {@link Permission#Permission(String)}.
   */
  @Test
  public void testPermissionString() {
    final String name = "TEST";
    final Permission permission = new Permission(name);

    assertEquals(name, permission.getName());
  }

  /**
   * Test method for {@link Permission#Permission()}.
   */
  @Test
  public void testPermissionPermission() {
    new Permission(this.getEntity());
  }

  /**
   * Test method for {@link Permission#Permission()}.
   */
  @Test(expected = NullPointerException.class)
  public void testPermissionPermissionNull() {
    new Permission((Permission) null);
  }

  /**
   * Test method for {@link Permission#getName()}.
   */
  @Test
  public void testGetName() {
    assertNull(this.getEntity().getName());
  }

  /**
   * Test method for {@link Permission#setName(java.lang.String)}.
   */
  @Test
  public void testSetName() {
    final String name = "TEST";

    this.getEntity().setName(name);

    assertEquals(name, this.getEntity().getName());
  }

  /**
   * Test method for {@link Permission#update(Permission)}.
   */
  @Test
  public void testUpdate() {
    super.testUpdate();

    final Permission anotherEntity = this.buildTestEntity();

    this.getEntity().update(anotherEntity);

    assertNotSame(this.getEntity(), anotherEntity);

    assertNull(this.getEntity().getName());
    assertEquals(this.getEntity().getName(), anotherEntity.getName());
  }

  /**
   * Test method for {@link Permission#update(Permission)}.
   */
  @Test
  public void testUpdateName() {
    final Permission anotherEntity = this.buildTestEntity(DISPLAYNAME);

    this.getEntity().update(anotherEntity);

    assertNotSame(this.getEntity(), anotherEntity);

    assertNotNull(this.getEntity().getName());
    assertEquals(this.getEntity().getName(), anotherEntity.getName());
  }

  /**
   * Test method for {@link Permission#getRoles()}.
   */
  @Test
  public void testGetRoles() {
    assertNotNull(this.getEntity().getRoles().size());
    assertEquals(0, this.getEntity().getRoles().size());
  }

  /**
   * Test method for {@link Permission#addRole(com.monogramm.starter.persistence.role.entity.Role)}.
   */
  @Test
  public void testAddRole() {
    assertNotNull(this.getEntity().getRoles().size());
    assertEquals(0, this.getEntity().getRoles().size());

    final Role role = Role.builder(this.getClass().toString()).id(UUID.randomUUID()).build();
    assertTrue(this.getEntity().addRole(role));
    assertFalse(this.getEntity().addRole(role));
    assertNotNull(this.getEntity().getRoles().size());
    assertEquals(1, this.getEntity().getRoles().size());

    assertTrue(this.getEntity().addRole(Role.builder().id(UUID.randomUUID()).build()));
    assertFalse(this.getEntity().addRole(role));
    assertNotNull(this.getEntity().getRoles().size());
    assertEquals(2, this.getEntity().getRoles().size());
  }

  /**
   * Test method for {@link Permission#addRole(com.monogramm.starter.persistence.role.entity.Role)}.
   */
  @Test(expected = NullPointerException.class)
  public void testAddRoleNull() {
    assertNotNull(this.getEntity().getRoles().size());
    assertEquals(0, this.getEntity().getRoles().size());

    this.getEntity().addRole(null);
  }

  /**
   * Test method for {@link Permission#addRoles(java.util.Collection)}.
   */
  @Test
  public void testAddRoles() {
    assertNotNull(this.getEntity().getRoles().size());
    assertEquals(0, this.getEntity().getRoles().size());

    final Role role = Role.builder(this.getClass().toString()).id(UUID.randomUUID()).build();
    assertTrue(this.getEntity().addRoles(Collections.singleton(role)));
    assertFalse(this.getEntity().addRoles(Collections.singleton(role)));
    assertNotNull(this.getEntity().getRoles().size());
    assertEquals(1, this.getEntity().getRoles().size());

    assertTrue(this.getEntity()
        .addRoles(Collections.singleton(Role.builder().id(UUID.randomUUID()).build())));
    assertFalse(this.getEntity().addRoles(Collections.singleton(role)));
    assertNotNull(this.getEntity().getRoles().size());
    assertEquals(2, this.getEntity().getRoles().size());
  }

  /**
   * Test method for {@link Permission#addRoles(java.util.Collection)}.
   */
  @Test(expected = NullPointerException.class)
  public void testAddRolesNull() {
    assertNotNull(this.getEntity().getRoles().size());
    assertEquals(0, this.getEntity().getRoles().size());

    this.getEntity().addRoles(Collections.singleton(null));
  }

  /**
   * Test method for {@link Permission#addRoles(java.util.Collection)}.
   */
  @Test(expected = NullPointerException.class)
  public void testAddRolesCollectionNull() {
    assertNotNull(this.getEntity().getRoles().size());
    assertEquals(0, this.getEntity().getRoles().size());

    this.getEntity().addRoles(null);
  }

  /**
   * Test method for
   * {@link Permission#removeRole(com.monogramm.starter.persistence.role.entity.Role)}.
   */
  @Test
  public void testRemoveRole() {
    assertNotNull(this.getEntity().getRoles().size());
    assertEquals(0, this.getEntity().getRoles().size());

    final Role role = Role.builder(this.getClass().toString()).id(UUID.randomUUID()).build();
    this.getEntity().addRole(role);
    assertTrue(this.getEntity().removeRole(role));
    assertNotNull(this.getEntity().getRoles().size());
    assertEquals(0, this.getEntity().getRoles().size());

    this.getEntity().addRole(role);
    this.getEntity().addRole(Role.builder().id(UUID.randomUUID()).build());
    assertTrue(this.getEntity().removeRole(role));
    assertNotNull(this.getEntity().getRoles().size());
    assertEquals(1, this.getEntity().getRoles().size());
  }

  /**
   * Test method for
   * {@link Permission#removeRole(com.monogramm.starter.persistence.role.entity.Role)}.
   */
  @Test
  public void testRemoveRoleNull() {
    assertNotNull(this.getEntity().getRoles().size());
    assertEquals(0, this.getEntity().getRoles().size());

    assertFalse(this.getEntity().removeRole(null));
  }

  /**
   * Test method for {@link Permission#removeRoles(java.util.Collection)}.
   */
  @Test
  public void testRemoveRoles() {
    assertNotNull(this.getEntity().getRoles().size());
    assertEquals(0, this.getEntity().getRoles().size());

    final Role role = Role.builder(this.getClass().toString()).id(UUID.randomUUID()).build();
    this.getEntity().addRole(role);
    assertTrue(this.getEntity().removeRoles(Collections.singleton(role)));
    assertNotNull(this.getEntity().getRoles().size());
    assertEquals(0, this.getEntity().getRoles().size());

    this.getEntity().addRole(role);
    this.getEntity().addRole(Role.builder().id(UUID.randomUUID()).build());
    assertTrue(this.getEntity().removeRoles(Collections.singleton(role)));
    assertNotNull(this.getEntity().getRoles().size());
    assertEquals(1, this.getEntity().getRoles().size());
  }

  /**
   * Test method for {@link Permission#removeRoles(java.util.Collection)}.
   */
  @Test
  public void testRemoveRolesNull() {
    assertNotNull(this.getEntity().getRoles().size());
    assertEquals(0, this.getEntity().getRoles().size());

    assertFalse(this.getEntity().removeRoles(Collections.singleton(null)));
  }

  /**
   * Test method for {@link Permission#removeRoles(java.util.Collection)}.
   */
  @Test(expected = NullPointerException.class)
  public void testRemoveRolesCollectionNull() {
    assertNotNull(this.getEntity().getRoles().size());
    assertEquals(0, this.getEntity().getRoles().size());

    this.getEntity().removeRoles(null);
  }

}
