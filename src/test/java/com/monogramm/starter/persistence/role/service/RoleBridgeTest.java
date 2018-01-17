/*
 * Creation by madmath03 the 2017-11-19.
 */

package com.monogramm.starter.persistence.role.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.monogramm.starter.dto.role.RoleDto;
import com.monogramm.starter.persistence.AbstractGenericBridgeTest;
import com.monogramm.starter.persistence.permission.dao.IPermissionRepository;
import com.monogramm.starter.persistence.permission.entity.Permission;
import com.monogramm.starter.persistence.role.entity.Role;
import com.monogramm.starter.persistence.user.dao.IUserRepository;
import com.monogramm.starter.persistence.user.entity.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * {@link RoleBridge} Unit Test.
 * 
 * @author madmath03
 */
public class RoleBridgeTest extends AbstractGenericBridgeTest<Role, RoleDto, RoleBridge> {

  private IPermissionRepository permissionRepository;

  /**
   * Get the {@link #permissionRepository}.
   * 
   * @return the {@link #permissionRepository}.
   */
  protected final IPermissionRepository getPermissionRepository() {
    return permissionRepository;
  }

  @Override
  protected RoleBridge buildTestBridge() {
    return new RoleBridge();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.monogramm.starter.persistence.AbstractGenericBridgeTest#setUp()
   */
  @Before
  public void setUp() throws Exception {
    super.setUp();

    permissionRepository = mock(IPermissionRepository.class);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.monogramm.starter.persistence.AbstractGenericBridgeTest#tearDown()
   */
  @After
  public void tearDown() throws Exception {
    super.tearDown();

    Mockito.reset(permissionRepository);
  }

  /**
   * Test method for {@link RoleBridge#RoleBridge()}.
   */
  @Test
  public void testRoleBridge() {
    final RoleBridge roleBridge = new RoleBridge();

    super.testAbstractGenericBridge(roleBridge);
    assertNull(roleBridge.getPermissionRepository());
  }

  /**
   * Test method for {@link RoleBridge#RoleBridge(IUserRepository, IPermissionRepository)}.
   */
  @Test
  public void testRoleBridgeIUserRepositoryIPermissionRepository() {
    final RoleBridge roleBridge = new RoleBridge(getUserRepository(), getPermissionRepository());

    super.testAbstractGenericBridgeIUserRepository(roleBridge);
    assertNotNull(roleBridge.getPermissionRepository());
    assertEquals(this.getPermissionRepository(), roleBridge.getPermissionRepository());
  }

  /**
   * Test method for {@link RoleBridge#RoleBridge(IUserRepository, IPermissionRepository)}.
   */
  @Test
  public void testRoleBridgeIUserRepositoryNull() {
    final RoleBridge roleBridge = new RoleBridge(getUserRepository(), null);

    super.testAbstractGenericBridgeIUserRepository(roleBridge);
    assertNull(roleBridge.getPermissionRepository());
  }

  /**
   * Test method for {@link RoleBridge#RoleBridge(IUserRepository, IPermissionRepository)}.
   */
  @Test
  public void testRoleBridgeNullNull() {
    final RoleBridge roleBridge = new RoleBridge(null, null);

    super.testAbstractGenericBridge(roleBridge);
    assertNull(roleBridge.getPermissionRepository());
  }

  /**
   * Test method for {@link RoleBridge#buildEntity()}.
   */
  @Test
  public void testBuildEntity() {
    super.testBuildEntity();

    assertNull(this.getBridge().buildEntity().getName());
    assertTrue(this.getBridge().buildEntity().getPermissions().isEmpty());
  }

  /**
   * Test method for {@link RoleBridge#buildDto()}.
   */
  @Test
  public void testBuildDto() {
    super.testBuildDto();

    assertNull(this.getBridge().buildDto().getName());
    assertTrue(this.getBridge().buildDto().getPermissions().length == 0);
  }

  /**
   * Test method for {@link RoleBridge#toEntity(com.monogramm.starter.dto.role.RoleDto)}.
   */
  @Test
  public void testToEntityRoleDTONoPermissionRepository() {
    final RoleDto dto = this.getBridge().buildDto();
    this.getBridge().setPermissionRepository(null);
    Role entity = this.getBridge().toEntity(dto);

    // Check data
    assertNull(entity.getName());
    assertEquals(dto.getName(), entity.getName());
    assertTrue(entity.getPermissions().isEmpty());

    // Set up the DTO
    final String name = "TEST";
    dto.setName(name);

    final UUID[] dtoPermissions = new UUID[2];
    for (int i = 0; i < dtoPermissions.length; i++) {
      dtoPermissions[i] = UUID.randomUUID();
    }
    dto.setPermissions(dtoPermissions);

    // Test that update of DTO is not reported to previous entity
    assertNull(entity.getName());
    assertNotEquals(dto.getName(), entity.getName());
    assertTrue(entity.getPermissions().isEmpty());

    // Test that update of DTO is reported to new entity
    entity = this.getBridge().toEntity(dto);

    assertNotNull(entity.getName());
    assertEquals(dto.getName(), entity.getName());
    assertFalse(entity.getPermissions().isEmpty());
    assertEquals(dto.getPermissions().length, entity.getPermissions().size());
  }

  /**
   * Test method for {@link RoleBridge#toEntity(com.monogramm.starter.dto.role.RoleDto)}.
   */
  @Test
  public void testToEntityRoleDTOWithPermissionRepository() {
    final RoleDto dto = this.getBridge().buildDto();
    this.getBridge().setPermissionRepository(getPermissionRepository());
    Role entity = this.getBridge().toEntity(dto);

    // Check data
    assertNull(entity.getName());
    assertEquals(dto.getName(), entity.getName());
    assertTrue(entity.getPermissions().isEmpty());

    // Set up the DTO
    final String name = "TEST";
    dto.setName(name);

    final UUID[] dtoPermissions = new UUID[2];
    for (int i = 0; i < dtoPermissions.length; i++) {
      dtoPermissions[i] = UUID.randomUUID();
    }
    dto.setPermissions(dtoPermissions);

    // Test that update of DTO is not reported to previous entity
    assertNull(entity.getName());
    assertNotEquals(dto.getName(), entity.getName());
    assertTrue(entity.getPermissions().isEmpty());

    // Test that update of DTO is reported to new entity
    entity = this.getBridge().toEntity(dto);

    assertNotNull(entity.getName());
    assertEquals(dto.getName(), entity.getName());
    assertTrue(entity.getPermissions().isEmpty());
    assertNotEquals(dto.getPermissions().length, entity.getPermissions().size());


    for (int i = 0; i < dtoPermissions.length; i++) {
      when(permissionRepository.findById(dtoPermissions[i]))
          .thenReturn(Permission.builder().id(dtoPermissions[i]).build());
    }
    entity = this.getBridge().toEntity(dto);
    assertFalse(entity.getPermissions().isEmpty());
    assertEquals(dto.getPermissions().length, entity.getPermissions().size());
  }

  /**
   * Test method for {@link RoleBridge#toDto(com.monogramm.starter.persistence.role.entity.Role)}.
   */
  @Test
  public void testToDTORole() {
    final String name = "TEST";
    final Role entity = this.getBridge().buildEntity();

    RoleDto dto = this.getBridge().toDto(entity);

    assertEquals(dto.getName(), entity.getName());
    entity.setName(name);
    assertNotEquals(dto.getName(), entity.getName());

    assertTrue(dto.getPermissions().length == 0);
    final Collection<Permission> permissions = new ArrayList<>(2);
    permissions.add(Permission.builder().id(UUID.randomUUID()).build());
    permissions.add(Permission.builder().id(UUID.randomUUID()).build());
    entity.addPermissions(permissions);
    assertTrue(dto.getPermissions().length == 0);
    assertNotEquals(dto.getPermissions().length, entity.getPermissions().size());


    entity.setCreatedAt(new Date());
    entity.setCreatedBy(User.builder().id(UUID.randomUUID()).build());
    entity.setModifiedAt(new Date());
    entity.setModifiedBy(User.builder().id(UUID.randomUUID()).build());
    entity.setOwner(User.builder().id(UUID.randomUUID()).build());

    dto = this.getBridge().toDto(entity);

    assertEquals(entity.getName(), dto.getName());
    assertEquals(dto.getPermissions().length, entity.getPermissions().size());
  }

}
