/*
 * Creation by madmath03 the 2019-01-24.
 */

package com.monogramm.starter.persistence.media.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.monogramm.starter.persistence.AbstractGenericEntityBuilderTest;
import com.monogramm.starter.persistence.media.entity.Media.MediaBuilder;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import org.junit.Test;

/**
 * {@link MediaBuilder} Unit Test.
 * 
 * @author madmath03
 */
public class MediaBuilderTest extends AbstractGenericEntityBuilderTest<Media.MediaBuilder> {

  private static final String DISPLAYNAME = "TEST";
  private static final String PATH = "TEST/MYFILE.TXT";

  @Override
  protected MediaBuilder buildTestEntityBuilder() {
    return Media.builder();
  }

  /**
   * Test method for {@link Media#builder()}.
   */
  @Test
  public void testGetBuilder() {
    Media.MediaBuilder builder = Media.builder();

    assertNotNull(builder);

    final Media mediaBuilt = builder.build();
    assertNotNull(mediaBuilt);
    assertNull(mediaBuilt.getName());
  }

  /**
   * Test method for {@link Media#builder(java.lang.String)}.
   */
  @Test
  public void testGetBuilderString() {
    Media.MediaBuilder builder = Media.builder(DISPLAYNAME, PATH);

    assertNotNull(builder);

    final Media mediaBuilt = builder.build();
    assertNotNull(mediaBuilt);
    assertEquals(DISPLAYNAME, mediaBuilt.getName());
  }

  /**
   * Test method for {@link Media.MediaBuilder#name(java.lang.String)}.
   */
  @Test
  public void testName() {
    assertEquals(this.getEntityBuilder(), this.getEntityBuilder().name(null));

    final String name = "TEST";
    assertEquals(name, this.getEntityBuilder().name(name).build().getName());
  }

  /**
   * Test method for {@link Media.MediaBuilder#description(java.lang.String)}.
   */
  @Test
  public void testDescription() {
    assertEquals(this.getEntityBuilder(), this.getEntityBuilder().description(null));

    final String description = "TEST";
    assertEquals(description,
        this.getEntityBuilder().description(description).build().getDescription());
  }

  /**
   * Test method for {@link Media.MediaBuilder#startDate(java.util.Date)}.
   */
  @Test
  public void testStartDate() {
    assertEquals(this.getEntityBuilder(), this.getEntityBuilder().startDate(null));

    final Date startDate = new Date();
    assertEquals(startDate, this.getEntityBuilder().startDate(startDate).build().getStartDate());
  }

  /**
   * Test method for {@link Media.MediaBuilder#endDate(java.util.Date)}.
   */
  @Test
  public void testEndDate() {
    assertEquals(this.getEntityBuilder(), this.getEntityBuilder().endDate(null));

    final Date endDate = new Date();
    assertEquals(endDate, this.getEntityBuilder().endDate(endDate).build().getEndDate());
  }

  /**
   * Test method for {@link Media.MediaBuilder#path(java.lang.String)}.
   */
  @Test
  public void testPathString() {
    assertEquals(this.getEntityBuilder(), this.getEntityBuilder().path((String) null));

    final String path = "TEST/MYFILE.TXT";
    assertEquals(path, this.getEntityBuilder().path(path).build().getPath());
  }

  /**
   * Test method for {@link Media.MediaBuilder#path(java.nio.file.Path)}.
   */
  @Test
  public void testPathPath() {
    assertEquals(this.getEntityBuilder(), this.getEntityBuilder().path((Path) null));

    final String pathStr = "TEST/MYFILE.TXT";
    final Path path = Paths.get(pathStr);
    assertEquals(path.toString(), this.getEntityBuilder().path(path).build().getPath());
  }

}
