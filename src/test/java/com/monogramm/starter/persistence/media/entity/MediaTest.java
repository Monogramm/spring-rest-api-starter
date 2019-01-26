/*
 * Creation by madmath03 the 2019-01-24.
 */

package com.monogramm.starter.persistence.media.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.monogramm.starter.persistence.AbstractGenericEntityTest;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import org.junit.Test;

/**
 * {@link Media} Unit Test.
 * 
 * @author madmath03
 */
public class MediaTest extends AbstractGenericEntityTest<Media> {

  private static final String DISPLAYNAME = "Foo";
  private static final String DESCRIPTION = "DUMMY DESCRIPTION";
  private static final String PATH = "TEST/MYFILE.TXT";

  @Override
  protected Media buildTestEntity() {
    return Media.builder().build();
  }

  protected Media buildTestEntity(final String name, final String path) {
    return Media.builder(name, path).build();
  }

  @Override
  public void testToJson() throws JsonProcessingException {
    super.testToJson();

    this.getEntity().setName(DISPLAYNAME);
    assertNotNull(this.getEntity().toJson());
  }

  @Override
  public void testToString() {
    super.testToString();

    this.getEntity().setName(DISPLAYNAME);
    assertNotNull(this.getEntity().toString());
  }

  @Override
  public void testHashCode() {
    super.testHashCode();

    final Media copy = new Media(this.getEntity());

    assertEquals(copy.hashCode(), this.getEntity().hashCode());

    copy.setName(DISPLAYNAME);
    assertNotEquals(copy.hashCode(), this.getEntity().hashCode());
    copy.setName(null);
    assertEquals(copy.hashCode(), this.getEntity().hashCode());

    copy.setDescription(DESCRIPTION);
    assertNotEquals(copy.hashCode(), this.getEntity().hashCode());
    copy.setDescription(null);
    assertEquals(copy.hashCode(), this.getEntity().hashCode());

    final Date startDate = new Date();
    copy.setStartDate(startDate);
    assertNotEquals(copy.hashCode(), this.getEntity().hashCode());
    copy.setStartDate(null);
    assertEquals(copy.hashCode(), this.getEntity().hashCode());

    final Date endDate = new Date();
    copy.setEndDate(endDate);
    assertNotEquals(copy.hashCode(), this.getEntity().hashCode());
    copy.setEndDate(null);
    assertEquals(copy.hashCode(), this.getEntity().hashCode());

    copy.setPath(PATH);
    assertNotEquals(copy.hashCode(), this.getEntity().hashCode());
    copy.setPath((Path) null);
    assertEquals(copy.hashCode(), this.getEntity().hashCode());
  }

  @Override
  public void testEqualsObject() {
    super.testEqualsObject();

    final Media copy = new Media(this.getEntity());

    assertEquals(copy, this.getEntity());

    copy.setName(DISPLAYNAME);
    assertNotEquals(copy, this.getEntity());
    copy.setName(null);
    assertEquals(copy, this.getEntity());

    copy.setDescription(DESCRIPTION);
    assertNotEquals(copy, this.getEntity());
    copy.setDescription(null);
    assertEquals(copy, this.getEntity());

    final Date startDate = new Date();
    copy.setStartDate(startDate);
    assertNotEquals(copy, this.getEntity());
    copy.setStartDate(null);
    assertEquals(copy, this.getEntity());

    final Date endDate = new Date();
    copy.setEndDate(endDate);
    assertNotEquals(copy, this.getEntity());
    copy.setEndDate(null);
    assertEquals(copy, this.getEntity());

    copy.setPath(PATH);
    assertNotEquals(copy, this.getEntity());
    copy.setPath((Path) null);
    assertNotEquals(copy, this.getEntity());
    copy.setPath(this.getEntity().getPath());
    assertEquals(copy, this.getEntity());
  }

  /**
   * Test method for {@link Media#Media()}.
   */
  @Test
  public void testMedia() {
    final Media media = new Media();

    assertNull(media.getName());
  }

  /**
   * Test method for {@link Media#Media(String)}.
   */
  @Test
  public void testMediaString() {
    final String name = "TEST";
    final Media media = new Media(name);

    assertEquals(name, media.getName());
  }

  /**
   * Test method for {@link Media#Media()}.
   */
  @Test
  public void testMediaMedia() {
    new Media(this.getEntity());
  }

  /**
   * Test method for {@link Media#Media()}.
   */
  @Test(expected = NullPointerException.class)
  public void testMediaMediaNull() {
    new Media((Media) null);
  }

  /**
   * Test method for {@link Media#getName()}.
   */
  @Test
  public void testGetName() {
    assertNull(this.getEntity().getName());
  }

  /**
   * Test method for {@link Media#setName(java.lang.String)}.
   */
  @Test
  public void testSetName() {
    final String name = "TEST";

    this.getEntity().setName(name);

    assertEquals(name, this.getEntity().getName());
  }

  /**
   * Test method for {@link Media#getDescription()}.
   */
  @Test
  public void testGetDescription() {
    assertNull(this.getEntity().getDescription());
  }

  /**
   * Test method for {@link Media#setDescription(java.lang.String)}.
   */
  @Test
  public void testSetDescription() {
    final String description = "TEST";

    this.getEntity().setDescription(description);

    assertEquals(description, this.getEntity().getDescription());
  }

  /**
   * Test method for {@link Media#getStartDate()}.
   */
  @Test
  public void testGetStartDate() {
    assertNull(this.getEntity().getStartDate());
  }

  /**
   * Test method for {@link Media#setStartDate(Date)}.
   */
  @Test
  public void testSetStartDate() {
    final Date startDate = new Date();

    this.getEntity().setStartDate(startDate);

    assertEquals(startDate, this.getEntity().getStartDate());
  }

  /**
   * Test method for {@link Media#getEndDate()}.
   */
  @Test
  public void testGetEndDate() {
    assertNull(this.getEntity().getEndDate());
  }

  /**
   * Test method for {@link Media#setEndDate(Date)}.
   */
  @Test
  public void testSetEndDate() {
    final Date endDate = new Date();

    this.getEntity().setEndDate(endDate);

    assertEquals(endDate, this.getEntity().getEndDate());
  }

  /**
   * Test method for {@link Media#getPath()}.
   */
  @Test
  public void testGetPath() {
    assertNull(this.getEntity().getPath());
  }

  /**
   * Test method for {@link Media#setPath(java.lang.String)}.
   */
  @Test
  public void testSetPathString() {
    final String path = "TEST/MYFILE.TXT";

    this.getEntity().setPath(path);

    assertEquals(path, this.getEntity().getPath());
  }

  /**
   * Test method for {@link Media#setPath(java.nio.file.Path)}.
   */
  @Test
  public void testSetPathPath() {
    final String pathStr = "TEST/MYFILE.TXT";
    final Path path = Paths.get(pathStr);

    this.getEntity().setPath(path);

    assertEquals(path.toString(), this.getEntity().getPath());
  }

  /**
   * Test method for {@link Media#update(Media)}.
   */
  @Test
  public void testUpdate() {
    super.testUpdate();

    final Media anotherEntity = this.buildTestEntity();

    this.getEntity().update(anotherEntity);

    assertNotSame(this.getEntity(), anotherEntity);

    assertNull(this.getEntity().getName());
    assertEquals(this.getEntity().getName(), anotherEntity.getName());
  }

  /**
   * Test method for {@link Media#update(Media)}.
   */
  @Test
  public void testUpdateName() {
    final Media anotherEntity = this.buildTestEntity(DISPLAYNAME, PATH);

    this.getEntity().update(anotherEntity);

    assertNotSame(this.getEntity(), anotherEntity);

    // Update of name is prohibited
    assertNull(this.getEntity().getName());
    assertNotEquals(this.getEntity().getName(), anotherEntity.getName());
  }

}
