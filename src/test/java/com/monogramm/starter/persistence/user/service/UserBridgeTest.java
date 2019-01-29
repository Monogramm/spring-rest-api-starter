/*
 * Creation by madmath03 the 2017-11-19.
 */

package com.monogramm.starter.persistence.user.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.github.madmath03.password.Passwords;
import com.monogramm.starter.dto.user.UserDto;
import com.monogramm.starter.persistence.AbstractGenericBridgeTest;
import com.monogramm.starter.persistence.role.dao.RoleRepository;
import com.monogramm.starter.persistence.role.entity.Role;
import com.monogramm.starter.persistence.user.entity.User;

import java.util.Date;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * {@link UserBridge} Unit Test.
 * 
 * @author madmath03
 */
public class UserBridgeTest extends AbstractGenericBridgeTest<User, UserDto, UserBridge> {

  private RoleRepository roleRepository;

  /**
   * Get the {@link #roleRepository}.
   * 
   * @return the {@link #roleRepository}.
   */
  protected final RoleRepository getRoleRepository() {
    return roleRepository;
  }

  @Override
  protected UserBridge buildTestBridge() {
    return new UserBridge();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.monogramm.starter.persistence.AbstractGenericBridgeTest#setUp()
   */
  @Before
  public void setUp() throws Exception {
    super.setUp();

    roleRepository = mock(RoleRepository.class);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.monogramm.starter.persistence.AbstractGenericBridgeTest#tearDown()
   */
  @After
  public void tearDown() throws Exception {
    super.tearDown();

    Mockito.reset(roleRepository);
  }

  /**
   * Test method for {@link UserBridge#UserBridge()}.
   */
  @Test
  public void testUserBridge() {
    final UserBridge userBridge = new UserBridge();

    super.testAbstractGenericBridge(userBridge);
    assertNull(userBridge.getRoleRepository());
  }

  /**
   * Test method for
   * {@link UserBridge#UserBridge(com.monogramm.starter.persistence.user.dao.UserRepository, com.monogramm.starter.persistence.role.dao.RoleRepository)}.
   */
  @Test
  public void testUserBridgeIUserRepositoryIRoleRepository() {
    final UserBridge userBridge = new UserBridge(getUserRepository(), getRoleRepository());

    super.testAbstractGenericBridgeIUserRepository(userBridge);
    assertNotNull(userBridge.getRoleRepository());
    assertEquals(this.getRoleRepository(), userBridge.getRoleRepository());
  }

  /**
   * Test method for
   * {@link UserBridge#UserBridge(com.monogramm.starter.persistence.user.dao.UserRepository, com.monogramm.starter.persistence.role.dao.RoleRepository)}.
   */
  @Test
  public void testUserBridgeIUserRepositoryNull() {
    final UserBridge userBridge = new UserBridge(getUserRepository(), null);

    super.testAbstractGenericBridgeIUserRepository(userBridge);
    assertNull(userBridge.getRoleRepository());
  }

  /**
   * Test method for
   * {@link UserBridge#UserBridge(com.monogramm.starter.persistence.user.dao.UserRepository, com.monogramm.starter.persistence.role.dao.RoleRepository)}.
   */
  @Test
  public void testUserBridgeNullNull() {
    final UserBridge userBridge = new UserBridge(null, null);

    super.testAbstractGenericBridge(userBridge);
    assertNull(userBridge.getRoleRepository());
  }

  /**
   * Test method for {@link UserBridge#buildEntity()}.
   */
  @Test
  public void testBuildEntity() {
    super.testBuildEntity();

    assertNull(this.getBridge().buildEntity().getUsername());
    assertNull(this.getBridge().buildEntity().getEmail());
    assertNull(this.getBridge().buildEntity().getRole());
  }

  /**
   * Test method for {@link UserBridge#buildDto()}.
   */
  @Test
  public void testBuildDto() {
    super.testBuildDto();

    assertNull(this.getBridge().buildDto().getUsername());
    assertNull(this.getBridge().buildDto().getEmail());
    assertNull(this.getBridge().buildDto().getRole());
  }

  /**
   * Test method for {@link UserBridge#toEntity(com.monogramm.starter.dto.user.UserDto)}.
   */
  @Test
  public void testToEntityUserDto() {
    final UserDto dto = this.getBridge().buildDto();
    User entity = this.getBridge().toEntity(dto);

    // Check data
    assertNull(entity.getUsername());
    assertEquals(dto.getUsername(), entity.getUsername());
    assertNull(entity.getEmail());
    assertEquals(dto.getEmail(), entity.getEmail());
    assertNull(entity.getPassword());
    assertEquals(dto.isEnabled(), entity.isEnabled());
    assertEquals(dto.isVerified(), entity.isVerified());
    assertNull(entity.getRole());

    // Set up the DTO
    final String username = "TEST";
    dto.setUsername(username);
    final String email = "TEST@EMAIL.COM";
    dto.setEmail(email);
    final char[] password = {'p', 'a', 's', 's', 'w', 'o', 'r', 'd'};
    dto.setPassword(password.clone());
    dto.setEnabled(!dto.isEnabled());
    dto.setVerified(!dto.isVerified());
    final Role role = Role.builder().id(UUID.randomUUID()).build();
    dto.setRole(role.getId());

    // Test that update of DTO is not reported to previous entity
    assertNotEquals(dto.getUsername(), entity.getUsername());
    assertNotEquals(dto.getEmail(), entity.getEmail());
    assertNull(entity.getPassword());
    assertNotEquals(dto.isEnabled(), entity.isEnabled());
    assertNotEquals(dto.isVerified(), entity.isVerified());
    assertNull(entity.getRole());
  }

  /**
   * Test method for {@link UserBridge#toEntity(com.monogramm.starter.dto.user.UserDto)}.
   */
  @Test
  public void testToEntityUserDtoNoRoleRepository() {
    final UserDto dto = this.getBridge().buildDto();
    this.getBridge().setRoleRepository(null);

    // Set up the DTO
    final String username = "TEST";
    dto.setUsername(username);
    final String email = "TEST@EMAIL.COM";
    dto.setEmail(email);
    final char[] password = {'p', 'a', 's', 's', 'w', 'o', 'r', 'd'};
    dto.setPassword(password.clone());
    dto.setEnabled(!dto.isEnabled());
    dto.setVerified(!dto.isVerified());
    final Role role = Role.builder().id(UUID.randomUUID()).build();
    dto.setRole(role.getId());

    // Test that update of DTO is reported to new entity
    User entity = this.getBridge().toEntity(dto);

    // Check data
    assertEquals(dto.getUsername(), entity.getUsername());
    assertEquals(dto.getEmail(), entity.getEmail());
    assertTrue(Passwords.isExpectedPassword(password, entity.getPassword()));
    assertEquals(dto.isEnabled(), entity.isEnabled());
    assertEquals(dto.isVerified(), entity.isVerified());
    assertEquals(dto.getRole(), entity.getRole().getId());
  }

  /**
   * Test method for {@link UserBridge#toEntity(com.monogramm.starter.dto.user.UserDto)}.
   */
  @Test
  public void testToEntityUserDtoWithRoleRepository() {
    final UserDto dto = this.getBridge().buildDto();
    this.getBridge().setRoleRepository(roleRepository);

    // Set up the DTO
    final String username = "TEST";
    dto.setUsername(username);
    final String email = "TEST@EMAIL.COM";
    dto.setEmail(email);
    final char[] password = {'p', 'a', 's', 's', 'w', 'o', 'r', 'd'};
    dto.setPassword(password.clone());
    dto.setEnabled(!dto.isEnabled());
    dto.setVerified(!dto.isVerified());
    final Role role = Role.builder().id(UUID.randomUUID()).build();
    dto.setRole(role.getId());

    // Set up stubs
    when(roleRepository.findById(dto.getRole())).thenReturn(role);

    // Test that update of DTO is reported to new entity
    User entity = this.getBridge().toEntity(dto);

    // Check data
    assertEquals(dto.getUsername(), entity.getUsername());
    assertEquals(dto.getEmail(), entity.getEmail());
    assertTrue(Passwords.isExpectedPassword(password, entity.getPassword()));
    assertEquals(dto.isEnabled(), entity.isEnabled());
    assertEquals(dto.isVerified(), entity.isVerified());
    assertEquals(dto.getRole(), entity.getRole().getId());

    // Check stubs call
    verify(roleRepository, times(1)).findById(dto.getRole());
    verifyNoMoreInteractions(roleRepository);
  }

  /**
   * Test method for {@link UserBridge#toDto(com.monogramm.starter.persistence.user.entity.User)}.
   */
  @Test
  public void testToDtoUser() {
    final User entity = this.getBridge().buildEntity();
    UserDto dto = this.getBridge().toDto(entity);

    assertEquals(dto.getUsername(), entity.getUsername());
    final String username = "TEST";
    entity.setUsername(username);
    assertNotEquals(dto.getUsername(), entity.getUsername());

    assertEquals(dto.getEmail(), entity.getEmail());
    final String email = "TEST@EMAIL.COM";
    entity.setEmail(email);
    assertNotEquals(dto.getEmail(), entity.getEmail());

    assertNull(dto.getPassword());
    final char[] password = {'p', 'a', 's', 's', 'w', 'o', 'r', 'd'};
    entity.setPassword(password);
    assertNotEquals(dto.getPassword(), entity.getPassword());

    assertEquals(dto.isEnabled(), entity.isEnabled());
    entity.setEnabled(!entity.isEnabled());
    assertNotEquals(dto.isEnabled(), entity.isEnabled());

    assertEquals(dto.isVerified(), entity.isVerified());
    entity.setVerified(!entity.isVerified());
    assertNotEquals(dto.isVerified(), entity.isVerified());

    assertNull(dto.getRole());
    final Role role = new Role();
    role.setId(UUID.randomUUID());
    entity.setRole(role);
    assertNotEquals(dto.getRole(), entity.getRole().getId());


    entity.setCreatedAt(new Date());
    entity.setCreatedBy(User.builder().id(UUID.randomUUID()).build());
    entity.setModifiedAt(new Date());
    entity.setModifiedBy(User.builder().id(UUID.randomUUID()).build());
    entity.setOwner(User.builder().id(UUID.randomUUID()).build());

    dto = this.getBridge().toDto(entity);

    assertEquals(dto.getUsername(), entity.getUsername());
    assertEquals(dto.getEmail(), entity.getEmail());
    assertNull(dto.getPassword());
    assertEquals(dto.isEnabled(), entity.isEnabled());
    assertEquals(dto.isVerified(), entity.isVerified());
    assertNotNull(dto.getRole());
    assertEquals(dto.getRole(), entity.getRole().getId());

  }

  /**
   * Test method for {@link UserBridge#getRoleRepository()}.
   */
  @Test
  public void testGetRoleRepository() {
    assertNull(this.getBridge().getRoleRepository());
  }

  /**
   * Test method for
   * {@link UserBridge#setRoleRepository(com.monogramm.starter.persistence.role.dao.RoleRepository)}.
   */
  @Test
  public void testSetRoleRepository() {
    this.getBridge().setRoleRepository(roleRepository);
    assertEquals(roleRepository, this.getBridge().getRoleRepository());
  }

}
