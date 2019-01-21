/*
 * Creation by madmath03 the 2017-11-16.
 */

package com.monogramm.starter.dto.user;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import com.monogramm.starter.dto.AbstractGenericDtoTest;
import com.monogramm.starter.persistence.role.dao.IRoleRepository;

import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * {@link UserDto} Unit Test.
 * 
 * @author madmath03
 */
public class UserDtoTest extends AbstractGenericDtoTest<UserDto> {

  private static final String USERNAME = "Foo";
  private static final String EMAIL = "foo@email.com";

  private IRoleRepository roleRepository;

  @Override
  protected UserDto buildTestDto() {
    return new UserDto();
  }

  @Override
  protected UserDto buildTestDto(UserDto other) {
    return new UserDto(other);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.monogramm.starter.api.AbstractGenericDTOTest#setUp()
   */
  @Before
  public void setUp() throws Exception {
    super.setUp();

    roleRepository = mock(IRoleRepository.class);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.monogramm.starter.api.AbstractGenericDTOTest#tearDown()
   */
  @After
  public void tearDown() throws Exception {
    super.tearDown();

    Mockito.reset(roleRepository);
  }

  @Override
  public void testHashCode() {
    super.testHashCode();

    final UserDto copy = this.buildCopyTestDto();

    assertEquals(copy.hashCode(), this.getDto().hashCode());

    copy.setUsername(USERNAME);

    assertNotEquals(copy.hashCode(), this.getDto().hashCode());

    copy.setUsername(null);

    assertEquals(copy.hashCode(), this.getDto().hashCode());

    copy.setEmail(EMAIL);

    assertNotEquals(copy.hashCode(), this.getDto().hashCode());

    copy.setEmail(null);

    assertEquals(copy.hashCode(), this.getDto().hashCode());
  }

  @Override
  public void testEqualsObject() {
    super.testEqualsObject();

    final UserDto copy = this.buildCopyTestDto();

    assertEquals(copy, this.getDto());

    copy.setUsername(USERNAME);

    assertNotEquals(copy, this.getDto());

    copy.setUsername(null);

    assertEquals(copy, this.getDto());

    copy.setEmail(EMAIL);

    assertNotEquals(copy.hashCode(), this.getDto().hashCode());

    copy.setEmail(null);

    assertEquals(copy.hashCode(), this.getDto().hashCode());
  }

  /**
   * Test method for {@link UserDto#UserDto()}.
   */
  @Test
  public void testUserDto() {
    assertNull(getDto().getUsername());
    assertNull(getDto().getEmail());
    assertNull(getDto().getPassword());
    assertTrue(getDto().isEnabled());
    assertFalse(getDto().isVerified());
    assertNull(getDto().getRole());
  }

  /**
   * Test method for {@link UserDto#UserDto(com.monogramm.starter.dto.user.UserDto)}.
   */
  @Test
  public void testUserDtoUserDto() {
    final UserDto dto = this.buildCopyTestDto();

    assertEquals(getDto().getUsername(), dto.getUsername());
    final String username = "TEST";
    dto.setUsername(username);
    assertNotEquals(getDto().getUsername(), dto.getUsername());

    assertEquals(getDto().getEmail(), dto.getEmail());
    final String email = "TEST@EMAIL.COM";
    dto.setEmail(email);
    assertNotEquals(getDto().getEmail(), dto.getEmail());

    assertEquals(getDto().getPassword(), dto.getPassword());
    final char[] password = {'p', 'a', 's', 's', 'w', 'o', 'r', 'd'};
    dto.setPassword(password);
    assertNotEquals(getDto().getPassword(), dto.getPassword());

    assertEquals(getDto().isEnabled(), dto.isEnabled());
    dto.setEnabled(!dto.isEnabled());
    assertNotEquals(getDto().isEnabled(), dto.isEnabled());
    dto.setEnabled(!dto.isEnabled());
    assertEquals(getDto().isEnabled(), dto.isEnabled());

    assertEquals(getDto().isVerified(), dto.isVerified());
    dto.setVerified(!dto.isVerified());
    assertNotEquals(getDto().isVerified(), dto.isVerified());
    dto.setVerified(!dto.isVerified());
    assertEquals(getDto().isVerified(), dto.isVerified());

    assertEquals(getDto().getRole(), dto.getRole());
    dto.setRole(UUID.randomUUID());
    assertNotEquals(getDto().getRole(), dto.getRole());
  }

  /**
   * Test method for {@link UserDto#getUsername()}.
   */
  @Test
  public void testGetUsername() {
    assertNull(this.getDto().getUsername());
  }

  /**
   * Test method for {@link UserDto#setUsername(java.lang.String)}.
   */
  @Test
  public void testSetUsername() {
    final String username = "TEST";
    this.getDto().setUsername(username);
    assertEquals(username, this.getDto().getUsername());
  }

  /**
   * Test method for {@link UserDto#getEmail()}.
   */
  @Test
  public void testGetEmail() {
    assertNull(this.getDto().getEmail());
  }

  /**
   * Test method for {@link UserDto#setEmail(java.lang.String)}.
   */
  @Test
  public void testSetEmail() {
    final String email = "TEST@EMAIL.COM";
    this.getDto().setEmail(email);
    assertEquals(email, this.getDto().getEmail());
    assertFalse(this.getDto().isVerified());

    final String newEmail = "NEWTEST@EMAIL.COM";
    this.getDto().setEmail(newEmail);
    assertEquals(newEmail, this.getDto().getEmail());
  }

  /**
   * Test method for {@link UserDto#getPassword()}.
   */
  @Test
  public void testGetPassword() {
    assertNull(this.getDto().getPassword());
  }

  /**
   * Test method for {@link UserDto#setPassword(java.lang.String)}.
   */
  @Test
  public void testSetPassword() {
    final char[] password = {'p', 'a', 's', 's', 'w', 'o', 'r', 'd'};
    this.getDto().setPassword(password.clone());
    assertNotNull(this.getDto().getPassword());
    assertArrayEquals(password, this.getDto().getPassword());
  }

  /**
   * Test method for {@link UserDto#isEnabled()}.
   */
  @Test
  public void testIsEnabled() {
    assertTrue(this.getDto().isEnabled());
  }

  /**
   * Test method for {@link UserDto#setEnabled(boolean)}.
   */
  @Test
  public void testSetEnabled() {
    final boolean enabled = true;
    this.getDto().setEnabled(enabled);
    assertEquals(enabled, this.getDto().isEnabled());
  }

  /**
   * Test method for {@link UserDto#isVerified()}.
   */
  @Test
  public void testIsVerified() {
    assertFalse(this.getDto().isVerified());
  }

  /**
   * Test method for {@link UserDto#setVerified(boolean)}.
   */
  @Test
  public void testSetVerified() {
    final boolean verified = true;
    this.getDto().setVerified(verified);
    assertEquals(verified, this.getDto().isVerified());
  }

  /**
   * Test method for {@link UserDto#getRole()}.
   */
  @Test
  public void testGetRole() {
    assertNull(this.getDto().getRole());
  }

  /**
   * Test method for {@link UserDto#setRole(java.util.UUID)}.
   */
  @Test
  public void testSetRole() {
    final UUID id = UUID.randomUUID();
    this.getDto().setRole(id);
    assertEquals(id, this.getDto().getRole());
  }
}
