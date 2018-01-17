/*
 * Creation by madmath03 the 2017-11-25
 */

package com.monogramm.starter.persistence.permission.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.monogramm.starter.dto.permission.PermissionDto;
import com.monogramm.starter.persistence.AbstractGenericBridgeTest;
import com.monogramm.starter.persistence.permission.entity.Permission;
import com.monogramm.starter.persistence.user.entity.User;

import java.util.Date;
import java.util.UUID;

import org.junit.Test;

/**
 * {@link PermissionBridge} Unit Test.
 * 
 * @author madmath03
 */
public class PermissionBridgeTest
    extends AbstractGenericBridgeTest<Permission, PermissionDto, PermissionBridge> {

  @Override
  protected PermissionBridge buildTestBridge() {
    return new PermissionBridge();
  }

  /**
   * Test method for {@link PermissionBridge#PermissionBridge()}.
   */
  @Test
  public void testPermissionBridge() {
    final PermissionBridge permissionBridge = new PermissionBridge();

    super.testAbstractGenericBridge(permissionBridge);
  }

  /**
   * Test method for
   * {@link PermissionBridge#PermissionBridge(com.monogramm.starter.persistence.user.dao.IUserRepository)}.
   */
  @Test
  public void testPermissionBridgeIUserRepository() {
    final PermissionBridge permissionBridge = new PermissionBridge(getUserRepository());

    super.testAbstractGenericBridgeIUserRepository(permissionBridge);
  }

  /**
   * Test method for
   * {@link PermissionBridge#PermissionBridge(com.monogramm.starter.persistence.user.dao.IUserRepository)}.
   */
  @Test
  public void testPermissionBridgeNull() {
    final PermissionBridge permissionBridge = new PermissionBridge(null);

    super.testAbstractGenericBridge(permissionBridge);
  }

  /**
   * Test method for {@link PermissionBridge#buildEntity()}.
   */
  @Test
  public void testBuildEntity() {
    super.testBuildEntity();

    assertNull(this.getBridge().buildEntity().getName());
  }

  /**
   * Test method for {@link PermissionBridge#buildDto()}.
   */
  @Test
  public void testBuildDto() {
    super.testBuildDto();

    assertNull(this.getBridge().buildDto().getName());
  }

  /**
   * Test method for
   * {@link PermissionBridge#toEntity(com.monogramm.starter.dto.permission.PermissionDto)}.
   */
  @Test
  public void testToEntityPermissionDTO() {
    final PermissionDto dto = this.getBridge().buildDto();

    final String name = "TEST";
    dto.setName(name);

    Permission entity = this.getBridge().toEntity(dto);

    assertEquals(entity.getName(), dto.getName());
  }

  /**
   * Test method for
   * {@link PermissionBridge#toDto(com.monogramm.starter.persistence.permission.entity.Permission)}.
   */
  @Test
  public void testToDTOPermission() {
    final String name = "TEST";
    final Permission entity = this.getBridge().buildEntity();
    entity.setName(name);

    PermissionDto dto = this.getBridge().toDto(entity);

    entity.setCreatedAt(new Date());
    entity.setCreatedBy(User.builder().id(UUID.randomUUID()).build());
    entity.setModifiedAt(new Date());
    entity.setModifiedBy(User.builder().id(UUID.randomUUID()).build());
    entity.setOwner(User.builder().id(UUID.randomUUID()).build());
    dto = this.getBridge().toDto(entity);

    assertEquals(dto.getName(), entity.getName());
  }

}
