/*
 * Creation by madmath03 the 2017-11-16.
 */

package com.monogramm.starter.dto.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import com.monogramm.starter.dto.AbstractGenericDtoTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * {@link TypeDto} Unit Test.
 * 
 * @author madmath03
 */
public class TypeDtoTest extends AbstractGenericDtoTest<TypeDto> {

  private static final String DISPLAYNAME = "Foo";

  @Override
  protected TypeDto buildTestDto() {
    return new TypeDto();
  }

  @Override
  protected TypeDto buildTestDto(TypeDto other) {
    return new TypeDto(other);
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

    final TypeDto copy = this.buildCopyTestDto();

    assertEquals(copy.hashCode(), this.getDto().hashCode());

    copy.setName(DISPLAYNAME);

    assertNotEquals(copy.hashCode(), this.getDto().hashCode());

    copy.setName(null);

    assertEquals(copy.hashCode(), this.getDto().hashCode());
  }

  @Override
  public void testEqualsObject() {
    super.testEqualsObject();

    final TypeDto copy = this.buildCopyTestDto();

    assertEquals(copy, this.getDto());

    copy.setName(DISPLAYNAME);

    assertNotEquals(copy, this.getDto());

    copy.setName(null);

    assertEquals(copy, this.getDto());
  }

  /**
   * Test method for {@link TypeDto#TypeDTO()}.
   */
  @Test
  public void testTypeDto() {
    assertNull(getDto().getName());
  }

  /**
   * Test method for {@link TypeDto#TypeDTO(TypeDto)}.
   */
  @Test
  public void testTypeDtoTypeDto() {
    final TypeDto dto = this.buildCopyTestDto();

    assertEquals(getDto().getName(), dto.getName());
    final String name = "TEST";
    dto.setName(name);
    assertNotEquals(getDto().getName(), dto.getName());
  }

  /**
   * Test method for {@link TypeDto#getName()}.
   */
  @Test
  public void testGetName() {
    assertNull(this.getDto().getName());
  }

  /**
   * Test method for {@link TypeDto#setName(java.lang.String)}.
   */
  @Test
  public void testSetName() {
    final String name = "TEST";

    this.getDto().setName(name);

    assertEquals(name, this.getDto().getName());
  }
}
