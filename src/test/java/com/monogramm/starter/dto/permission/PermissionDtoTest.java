/*
 * Creation by madmath03 the 2017-11-25
 */

package com.monogramm.starter.dto.permission;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.monogramm.starter.dto.AbstractGenericDto;
import com.monogramm.starter.dto.AbstractGenericDtoTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * {@link PermissionDto} Unit Test.
 * 
 * @author madmath03
 */
public class PermissionDtoTest extends AbstractGenericDtoTest<PermissionDto> {

  private static final String DISPLAY_NAME = "Foo";

  @Override
  protected PermissionDto buildTestDto() {
    return new PermissionDto();
  }
  
  @Override
  protected PermissionDto buildTestDto(PermissionDto other) {
    return new PermissionDto(other);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.monogramm.starter.api.AbstractGenericDtoTest#setUp()
   */
  @Before
  public void setUp() throws Exception {
    super.setUp();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.monogramm.starter.api.AbstractGenericDtoTest#tearDown()
   */
  @After
  public void tearDown() throws Exception {
    super.tearDown();
  }

  @Override
  public void testHashCode() {
    super.testHashCode();

    final PermissionDto copy = this.buildCopyTestDto();

    assertEquals(copy.hashCode(), this.getDto().hashCode());

    copy.setName(DISPLAY_NAME);

    assertNotEquals(copy.hashCode(), this.getDto().hashCode());

    copy.setName(null);

    assertEquals(copy.hashCode(), this.getDto().hashCode());
  }

  @Override
  public void testEqualsObject() {
    super.testEqualsObject();

    final PermissionDto copy = this.buildCopyTestDto();

    assertEquals(copy, this.getDto());

    copy.setName(DISPLAY_NAME);

    assertNotEquals(copy, this.getDto());

    copy.setName(null);

    assertEquals(copy, this.getDto());
  }

  /**
   * Test method for {@link PermissionDto#PermissionDto()}.
   */
  @Test
  public void testPermissionDto() {
    assertNull(getDto().getName());
  }

  /**
   * Test method for {@link PermissionDto#PermissionDto(PermissionDto)}.
   */
  @Test
  public void testPermissionDtoPermissionDto() {
    final PermissionDto dto = this.buildCopyTestDto();

    assertEquals(getDto().getName(), dto.getName());
    final String name = "TEST";
    dto.setName(name);
    assertNotEquals(getDto().getName(), dto.getName());
  }

  /**
   * Test method for {@link PermissionDto#getName()}.
   */
  @Test
  public void testGetName() {
    assertNull(this.getDto().getName());
  }

  /**
   * Test method for {@link PermissionDto#setName(java.lang.String)}.
   */
  @Test
  public void testSetName() {
    final String name = "TEST";

    this.getDto().setName(name);

    assertEquals(name, this.getDto().getName());
  }

  /**
   * Test method for {@link AbstractGenericDto#compareTo(AbstractGenericDto)}.
   */
  @Test
  public void testCompareToCopyAlteredName() {
    assertNotNull(getDto());

    final PermissionDto otherDto = this.buildTestDto(getDto());
    assertNotNull(otherDto);

    final String name = this.getClass().getSimpleName();
    getDto().setName(name);
    assertEquals(1, getDto().compareTo(otherDto));
    assertEquals(-1, otherDto.compareTo(getDto()));

    otherDto.setName(name);
    assertEquals(0, getDto().compareTo(otherDto));
    assertEquals(0, otherDto.compareTo(getDto()));
  }

}
