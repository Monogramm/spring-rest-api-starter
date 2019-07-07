/*
 * Creation by madmath03 the 2017-11-16.
 */

package com.monogramm.starter.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * {@link AbstractGenericDto} Unit Test.
 * 
 * @author madmath03
 */
public abstract class AbstractGenericDtoTest<T extends AbstractGenericDto> {

  /**
   * The tested DTO.
   */
  private T dto;

  /**
   * Build a DTO to test.
   * 
   * @return an DTO for tests.
   */
  abstract protected T buildTestDto();

  /**
   * Build a DTO copy to test.
   * 
   * @param DTO to copy.
   * 
   * @return an DTO copy for tests.
   */
  abstract protected T buildTestDto(T other);

  /**
   * Build a DTO copy to test.
   * 
   * @return an DTO for tests.
   */
  protected T buildCopyTestDto() {
    return this.buildTestDto(this.getDto());
  }

  /**
   * Get the {@link #dto}.
   * 
   * @return the {@link #dto}.
   */
  public final T getDto() {
    return dto;
  }

  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception {
    this.dto = this.buildTestDto();

    assertNotNull("The tested DTO cannot be null!", dto);
  }

  /**
   * @throws java.lang.Exception
   */
  @After
  public void tearDown() throws Exception {
    this.dto = null;
  }

  /**
   * Test method for {@link AbstractGenericDto#AbstractGenericDto()}.
   */
  @Test
  public void testAbstractGenericDto() {
    assertNotNull(dto);
  }

  /**
   * Test method for
   * {@link AbstractGenericDto#AbstractGenericDto(com.monogramm.starter.dto.AbstractGenericDto)}.
   */
  @Test
  public void testAbstractGenericDtoAbstractGenericDto() {
    this.testAbstractGenericDtoAbstractGenericDto(this.buildTestDto(this.buildTestDto()));
  }

  /**
   * Test method for
   * {@link AbstractGenericDto#AbstractGenericDto(com.monogramm.starter.dto.AbstractGenericDto)}.
   */
  private void testAbstractGenericDtoAbstractGenericDto(final T dto) {
    assertNotNull(dto);
    assertEquals(this.dto, dto);
    assertNotSame(this.dto, dto);

    assertEquals(this.dto.getId(), dto.getId());
    assertEquals(this.dto.getCreatedAt(), dto.getCreatedAt());
    assertEquals(this.dto.getCreatedBy(), dto.getCreatedBy());
    assertEquals(this.dto.getModifiedAt(), dto.getModifiedAt());
    assertEquals(this.dto.getModifiedBy(), dto.getModifiedBy());
    assertEquals(this.dto.getOwner(), dto.getOwner());
  }

  /**
   * Test method for {@link AbstractGenericDto#getId()}.
   */
  @Test
  public void testGetId() {
    assertNull(dto.getId());
  }

  /**
   * Test method for {@link AbstractGenericDto#setId(java.util.UUID)}.
   */
  @Test
  public void testSetId() {
    final UUID id = UUID.randomUUID();
    dto.setId(id);
    assertEquals(id, dto.getId());
  }

  /**
   * Test method for {@link AbstractGenericDto#getCreatedAt()}.
   */
  @Test
  public void testGetCreatedAt() {
    assertNull(dto.getCreatedAt());
  }

  /**
   * Test method for {@link AbstractGenericDto#setCreatedAt(java.util.Date)}.
   */
  @Test
  public void testSetCreatedAt() {
    final Date date = new Date();
    dto.setCreatedAt(date);
    assertEquals(date, dto.getCreatedAt());
  }

  /**
   * Test method for {@link AbstractGenericDto#getCreatedBy()}.
   */
  @Test
  public void testGetCreatedBy() {
    assertNull(dto.getCreatedBy());
  }

  /**
   * Test method for {@link AbstractGenericDto#setCreatedBy(java.util.UUID)}.
   */
  @Test
  public void testSetCreatedBy() {
    final UUID id = UUID.randomUUID();
    dto.setCreatedBy(id);
    assertEquals(id, dto.getCreatedBy());
  }

  /**
   * Test method for {@link AbstractGenericDto#getModifiedAt()}.
   */
  @Test
  public void testGetModifiedAt() {
    assertNull(dto.getModifiedAt());
  }

  /**
   * Test method for {@link AbstractGenericDto#setModifiedAt(java.util.Date)}.
   */
  @Test
  public void testSetModifiedAt() {
    final Date date = new Date();
    dto.setModifiedAt(date);
    assertEquals(date, dto.getModifiedAt());
  }

  /**
   * Test method for {@link AbstractGenericDto#getModifiedBy()}.
   */
  @Test
  public void testGetModifiedBy() {
    assertNull(dto.getModifiedBy());
  }

  /**
   * Test method for {@link AbstractGenericDto#setModifiedBy(java.util.UUID)}.
   */
  @Test
  public void testSetModifiedBy() {
    final UUID id = UUID.randomUUID();
    dto.setModifiedBy(id);
    assertEquals(id, dto.getModifiedBy());
  }

  /**
   * Test method for {@link AbstractGenericDto#getOwner()}.
   */
  @Test
  public void testGetOwner() {
    assertNull(dto.getOwner());
  }

  /**
   * Test method for {@link AbstractGenericDto#setOwner(java.util.UUID)}.
   */
  @Test
  public void testSetOwner() {
    final UUID id = UUID.randomUUID();
    dto.setOwner(id);
    assertEquals(id, dto.getOwner());
  }

  /**
   * Test method for {@link AbstractGenericDto#hashCode()}.
   */
  @Test
  public void testHashCode() {
    assertEquals(dto.hashCode(), dto.hashCode());

    final T anotherDto = this.buildTestDto();
    assertEquals(anotherDto.hashCode(), dto.hashCode());


    dto.setId(UUID.randomUUID());
    assertEquals(dto.hashCode(), dto.hashCode());
    assertNotEquals(anotherDto.hashCode(), dto.hashCode());

    anotherDto.setId(UUID.randomUUID());
    assertNotEquals(anotherDto.hashCode(), dto.hashCode());

    anotherDto.setId(dto.getId());
    assertEquals(anotherDto.hashCode(), dto.hashCode());
  }

  /**
   * Test method for {@link AbstractGenericDto#equals(java.lang.Object)}.
   */
  @Test
  public void testEqualsObject() {
    assertEquals(dto, dto);
    assertNotEquals(dto, null);
    assertNotEquals(dto, new Object());

    final T anotherDto = this.buildTestDto();

    assertEquals(dto, anotherDto);
    assertEquals(anotherDto, dto);

    dto.setId(UUID.randomUUID());

    assertNotEquals(dto, anotherDto);
    assertNotEquals(anotherDto, dto);

    anotherDto.setId(UUID.randomUUID());

    assertNotEquals(dto, anotherDto);
    assertNotEquals(anotherDto, dto);

    anotherDto.setId(dto.getId());

    assertEquals(dto, anotherDto);
    assertEquals(anotherDto, dto);
  }

  /**
   * Test method for {@link AbstractGenericDto#toJson()}.
   */
  @Test
  public void testToJson() {
    try {
      assertNotNull(dto.toJson());
    } catch (JsonProcessingException e) {
      fail("toJSON failed: " + e);
    }

    dto.setCreatedAt(new Date());
    dto.setCreatedBy(UUID.randomUUID());
    dto.setModifiedAt(new Date());
    dto.setModifiedBy(UUID.randomUUID());
    dto.setOwner(UUID.randomUUID());
    try {
      assertNotNull(dto.toJson());
    } catch (JsonProcessingException e) {
      fail("toJSON failed: " + e);
    }
  }

  /**
   * Test method for {@link AbstractGenericDto#toString()}.
   */
  @Test
  public void testToString() {
    try {
      assertNotNull(dto.toString());
    } catch (Exception e) {
      fail("toString failed: " + e);
    }

    dto.setCreatedAt(new Date());
    dto.setCreatedBy(UUID.randomUUID());
    dto.setModifiedAt(new Date());
    dto.setModifiedBy(UUID.randomUUID());
    dto.setOwner(UUID.randomUUID());
    try {
      assertNotNull(dto.toString());
    } catch (Exception e) {
      fail("toString failed: " + e);
    }
  }

  /**
   * Test method for {@link AbstractGenericDto#compareTo(AbstractGenericDto)}.
   */
  @Test
  public void testCompareToSelf() {
    assertNotNull(dto);
    assertEquals(0, dto.compareTo(dto));
  }

  /**
   * Test method for {@link AbstractGenericDto#compareTo(AbstractGenericDto)}.
   */
  @Test
  public void testCompareToNull() {
    assertNotNull(dto);
    assertEquals(1, dto.compareTo(null));
  }

  /**
   * Test method for {@link AbstractGenericDto#compareTo(AbstractGenericDto)}.
   */
  @Test
  public void testCompareToCopy() {
    assertNotNull(dto);

    final T otherDto = this.buildTestDto(dto);
    assertNotNull(otherDto);
    assertEquals(0, dto.compareTo(otherDto));
    assertEquals(0, otherDto.compareTo(dto));
  }

  /**
   * Test method for {@link AbstractGenericDto#compareTo(AbstractGenericDto)}.
   */
  @Test
  public void testCompareToCopyAlteredCreatedAt() {
    assertNotNull(dto);

    final T otherDto = this.buildTestDto(dto);
    assertNotNull(otherDto);

    dto.setCreatedAt(new Date(System.currentTimeMillis() - 36_000L));
    assertEquals(1, dto.compareTo(otherDto));
    assertEquals(-1, otherDto.compareTo(dto));

    otherDto.setCreatedAt(new Date(System.currentTimeMillis() + 36_000L));
    assertEquals(1, dto.compareTo(otherDto));
    assertEquals(-1, otherDto.compareTo(dto));
  }

  /**
   * Test method for {@link AbstractGenericDto#compareTo(AbstractGenericDto)}.
   */
  @Test
  public void testCompareToCopyAlteredModifiedAt() {
    assertNotNull(dto);

    final T otherDto = this.buildTestDto(dto);
    assertNotNull(otherDto);

    dto.setModifiedAt(new Date(System.currentTimeMillis() - 36_000L));
    assertEquals(1, dto.compareTo(otherDto));
    assertEquals(-1, otherDto.compareTo(dto));

    otherDto.setModifiedAt(new Date(System.currentTimeMillis() + 36_000L));
    assertEquals(1, dto.compareTo(otherDto));
    assertEquals(-1, otherDto.compareTo(dto));
  }

  /**
   * Test method for {@link AbstractGenericDto#compareTo(AbstractGenericDto)}.
   */
  @Test
  public void testCompareToSort() {
    assertNotNull(dto);

    final T otherDto = this.buildTestDto(dto);
    assertNotNull(otherDto);

    dto.setCreatedAt(new Date(System.currentTimeMillis() - 36_000L));
    otherDto.setCreatedAt(new Date(System.currentTimeMillis() + 36_000L));

    final List<T> list = new ArrayList<>(2);
    list.add(dto);
    list.add(otherDto);

    assertEquals(dto, list.get(0));
    assertEquals(otherDto, list.get(1));

    Collections.sort(list);

    assertEquals(otherDto, list.get(0));
    assertEquals(dto, list.get(1));
  }

}
