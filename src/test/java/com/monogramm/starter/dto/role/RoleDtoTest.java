/*
 * Creation by madmath03 the 2017-11-16.
 */

package com.monogramm.starter.dto.role;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import com.monogramm.starter.dto.AbstractGenericDtoTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * {@link RoleDto} Unit Test.
 * 
 * @author madmath03
 */
public class RoleDtoTest extends AbstractGenericDtoTest<RoleDto> {

  private static final String DISPLAYNAME = "Foo";

  @Override
  protected RoleDto buildTestDto() {
    return new RoleDto();
  }

  @Override
  protected RoleDto buildTestDto(RoleDto other) {
    return new RoleDto(other);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.monogramm.starter.api.AbstractGenericDTOTest#setUp()
   */
  @Before
  public void setUp() throws Exception {
    super.setUp();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.monogramm.starter.api.AbstractGenericDTOTest#tearDown()
   */
  @After
  public void tearDown() throws Exception {
    super.tearDown();
  }

  @Override
  public void testHashCode() {
    super.testHashCode();

    final RoleDto copy = this.buildCopyTestDto();

    assertEquals(copy.hashCode(), this.getDto().hashCode());

    copy.setName(DISPLAYNAME);

    assertNotEquals(copy.hashCode(), this.getDto().hashCode());

    copy.setName(null);

    assertEquals(copy.hashCode(), this.getDto().hashCode());
  }

  @Override
  public void testEqualsObject() {
    super.testEqualsObject();

    final RoleDto copy = this.buildCopyTestDto();

    assertEquals(copy, this.getDto());

    copy.setName(DISPLAYNAME);

    assertNotEquals(copy, this.getDto());

    copy.setName(null);

    assertEquals(copy, this.getDto());
  }

  /**
   * Test method for {@link RoleDto#RoleDto()}.
   */
  @Test
  public void testRoleDto() {
    assertNull(getDto().getName());
  }

  /**
   * Test method for {@link RoleDto#RoleDto(RoleDto)}.
   */
  @Test
  public void testRoleDtoRoleDto() {
    final RoleDto dto = this.buildCopyTestDto();

    assertEquals(getDto().getName(), dto.getName());
    final String name = "TEST";
    dto.setName(name);
    assertNotEquals(getDto().getName(), dto.getName());
  }

  /**
   * Test method for {@link RoleDto#getName()}.
   */
  @Test
  public void testGetName() {
    assertNull(this.getDto().getName());
  }

  /**
   * Test method for {@link RoleDto#setName(java.lang.String)}.
   */
  @Test
  public void testSetName() {
    final String name = "TEST";

    this.getDto().setName(name);

    assertEquals(name, this.getDto().getName());
  }
}
