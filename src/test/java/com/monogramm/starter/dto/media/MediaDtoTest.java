/*
 * Creation by madmath03 the 2017-11-16.
 */

package com.monogramm.starter.dto.media;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.monogramm.starter.dto.AbstractGenericDto;
import com.monogramm.starter.dto.AbstractGenericDtoTest;

import java.util.Date;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * {@link MediaDto} Unit Test.
 * 
 * @author madmath03
 */
public class MediaDtoTest extends AbstractGenericDtoTest<MediaDto> {

  private static final String DISPLAYNAME = "Foo";
  private static final String DESCRIPTION = "DUMMY DESCRIPTION";
  private static final String PATH = UUID.randomUUID().toString();

  @Override
  protected MediaDto buildTestDto() {
    return new MediaDto();
  }

  @Override
  protected MediaDto buildTestDto(MediaDto other) {
    return new MediaDto(other);
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

    final MediaDto copy = this.buildCopyTestDto();

    assertEquals(copy.hashCode(), this.getDto().hashCode());

    copy.setName(DISPLAYNAME);
    assertNotEquals(copy.hashCode(), this.getDto().hashCode());
    copy.setName(null);
    assertEquals(copy.hashCode(), this.getDto().hashCode());

    copy.setDescription(DESCRIPTION);
    assertNotEquals(copy.hashCode(), this.getDto().hashCode());
    copy.setDescription(null);
    assertEquals(copy.hashCode(), this.getDto().hashCode());

    final Date startDate = new Date();
    copy.setStartDate(startDate);
    assertNotEquals(copy.hashCode(), this.getDto().hashCode());
    copy.setStartDate(null);
    assertEquals(copy.hashCode(), this.getDto().hashCode());

    final Date endDate = new Date();
    copy.setEndDate(endDate);
    assertNotEquals(copy.hashCode(), this.getDto().hashCode());
    copy.setEndDate(null);
    assertEquals(copy.hashCode(), this.getDto().hashCode());

    copy.setPath(PATH);
    assertNotEquals(copy.hashCode(), this.getDto().hashCode());
    copy.setPath(null);
    assertEquals(copy.hashCode(), this.getDto().hashCode());
  }

  @Override
  public void testEqualsObject() {
    super.testEqualsObject();

    final MediaDto copy = this.buildCopyTestDto();

    assertEquals(copy, this.getDto());

    copy.setName(DISPLAYNAME);
    assertNotEquals(copy, this.getDto());
    copy.setName(null);
    assertEquals(copy, this.getDto());

    copy.setDescription(DESCRIPTION);
    assertNotEquals(copy, this.getDto());
    copy.setDescription(null);
    assertEquals(copy, this.getDto());

    final Date startDate = new Date();
    copy.setStartDate(startDate);
    assertNotEquals(copy, this.getDto());
    copy.setStartDate(null);
    assertEquals(copy, this.getDto());

    final Date endDate = new Date();
    copy.setEndDate(endDate);
    assertNotEquals(copy, this.getDto());
    copy.setEndDate(null);
    assertEquals(copy, this.getDto());

    copy.setPath(PATH);
    assertNotEquals(copy, this.getDto());
    copy.setPath(null);
    assertEquals(copy, this.getDto());
  }

  /**
   * Test method for {@link MediaDto#MediaDto()}.
   */
  @Test
  public void testMediaDto() {
    assertNull(getDto().getName());
  }

  /**
   * Test method for {@link MediaDto#MediaDto(MediaDto)}.
   */
  @Test
  public void testMediaDtoMediaDto() {
    final MediaDto dto = this.buildCopyTestDto();

    assertEquals(getDto().getName(), dto.getName());
    final String name = "TEST";
    dto.setName(name);
    assertNotEquals(getDto().getName(), dto.getName());
  }

  /**
   * Test method for {@link MediaDto#getName()}.
   */
  @Test
  public void testGetName() {
    assertNull(this.getDto().getName());
  }

  /**
   * Test method for {@link MediaDto#setName(java.lang.String)}.
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

    final MediaDto otherDto = this.buildTestDto(getDto());
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
