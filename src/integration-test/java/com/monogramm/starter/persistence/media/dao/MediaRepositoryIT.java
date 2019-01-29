/*
 * Creation by madmath03 the 2019-01-24.
 */

package com.monogramm.starter.persistence.media.dao;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import com.monogramm.starter.persistence.AbstractGenericRepositoryIT;
import com.monogramm.starter.persistence.media.entity.Media;
import com.monogramm.starter.persistence.media.exception.MediaNotFoundException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link MediaReposiroty} Integration Test.
 * 
 * @author madmath03
 */
public class MediaReposirotyIT extends AbstractGenericRepositoryIT<Media, MediaRepository> {

  /**
   * Logger for {@link MediaReposirotyIT}.
   */
  private static final Logger LOG = LoggerFactory.getLogger(MediaReposirotyIT.class);

  private static final String PREFIX = MediaReposirotyIT.class.getSimpleName() + "_";

  private static final String DISPLAYNAME = MediaReposirotyIT.class.getSimpleName();
  private static final String PATH = "data/integration-test/media";

  private static final Path TEST_MEDIA_PATH = Paths.get(PATH);

  private Path tempDirectory;
  private Path tempFile;

  private Path tempExistingDirectory;
  private Path tempExistingFile;

  /**
   * @throws java.lang.Exception if the test setup crashes.
   */
  @Override
  @Before
  public void setUp() {
    super.setUp();

    try {
      this.tempDirectory = Files.createTempDirectory(PREFIX);
      this.tempFile = Files.createTempFile(tempDirectory, PREFIX, ".tmp");
    } catch (IOException e) {
      fail(e.getMessage());
    }
  }

  /**
   * @throws java.lang.Exception if the test cleanup crashes.
   */
  @Override
  @After
  public void tearDown() {
    super.tearDown();

    if (this.tempFile != null) {
      try {
        Files.delete(this.tempFile);
      } catch (IOException e) {
        LOG.error("Error while deleting " + tempFile, e);
      }
    }
    this.tempFile = null;

    if (this.tempDirectory != null) {
      try {
        Files.delete(this.tempDirectory);
      } catch (IOException e) {
        LOG.error("Error while deleting " + tempDirectory, e);
      }
    }
    this.tempDirectory = null;


    if (this.tempExistingFile != null) {
      try {
        Files.delete(this.tempExistingFile);
      } catch (IOException e) {
        LOG.error("Error while deleting " + tempExistingFile, e);
      }
    }
    this.tempExistingFile = null;

    if (this.tempExistingDirectory != null) {
      try {
        Files.delete(this.tempExistingDirectory);
      } catch (IOException e) {
        LOG.error("Error while deleting " + tempExistingDirectory, e);
      }
    }
    this.tempExistingDirectory = null;
  }

  @Override
  protected Media buildTestEntity() {
    return Media.builder(DISPLAYNAME, PATH).build();
  }

  @Override
  protected void addTestEntity(Media entity) {
    if (entity.getInputStream() == null) {
      InputStream inputStream = null;
      try {
        inputStream = new FileInputStream(tempFile.toFile());
      } catch (FileNotFoundException e) {
        fail(e.getMessage());
      }
      assertNotNull(inputStream);

      entity.setInputStream(inputStream);
    }
    super.addTestEntity(entity);

    try {
      this.tempExistingDirectory = Paths.get(entity.getId().toString());
      final Path tempExistingDirFullPath =
          Files.createDirectories(TEST_MEDIA_PATH.resolve(tempExistingDirectory));
      this.tempExistingFile = Files.createTempFile(tempExistingDirFullPath, PREFIX, ".tmp");
    } catch (IOException e) {
      fail(e.getMessage());
    }
  }

  @Test
  @Override
  public void testAdd() {
    super.testAdd();
  }

  @Test
  @Override
  public void testDeleteById() {
    super.testDeleteById();

    assertFalse(tempExistingDirectory.toFile().exists());
  }

  @Test
  @Override
  public void testDeleteByIdAndOwner() {
    super.testDeleteByIdAndOwner();

    assertFalse(tempExistingDirectory.toFile().exists());
  }

  /**
   * Test method for {@link IMediaRepository#findAll()}.
   */
  @Override
  @Test
  public void testFindAll() {
    int expectedSize = 0;

    final List<Media> actual = getRepository().findAll();

    assertNotNull(actual);
    assertEquals(expectedSize, actual.size());
  }

  /**
   * Test method for {@link IMediaRepository#findAllContainingNameIgnoreCase(java.lang.String)}.
   */
  @Test
  public void testFindAllContainingNameIgnoreCase() {
    final List<Media> models = new ArrayList<>();

    final List<Media> actual = getRepository().findAllContainingNameIgnoreCase(DISPLAYNAME);

    assertThat(actual, is(models));
  }

  /**
   * Test method for {@link IMediaRepository#findByNameIgnoreCase(String)}.
   * 
   * @throws MediaNotFoundException if the media is not found.
   */
  @Test
  public void testFindByNameIgnoreCase() {
    final Media model = this.buildTestEntity();
    model.setName(model.getName().toUpperCase());
    addTestEntity(model);

    final Media actual = getRepository().findByNameIgnoreCase(DISPLAYNAME);

    assertThat(actual, is(model));
  }

  /**
   * Test method for {@link IMediaRepository#findByNameIgnoreCase(java.lang.String)}.
   * 
   * @throws MediaNotFoundException if the media is not found.
   */
  @Test
  public void testFindByNameIgnoreCaseNoResult() {
    assertNull(getRepository().findByNameIgnoreCase(DISPLAYNAME));
  }

  /**
   * Test method for {@link IMediaRepository#findByNameIgnoreCase(java.lang.String)}.
   * 
   * @throws MediaNotFoundException if the media is not found.
   */
  @Test
  public void testFindByNameIgnoreCaseNonUnique() {
    addTestEntity(Media.builder(DISPLAYNAME + "1", PATH).build());
    addTestEntity(Media.builder(DISPLAYNAME + "2", PATH).build());

    assertNull(getRepository().findByNameIgnoreCase(DISPLAYNAME));
  }

  /**
   * Test method for {@link IMediaRepository#findByNameIgnoreCase(java.lang.String)}.
   * 
   * @throws MediaNotFoundException if the media is not found.
   */
  @Test
  public void testFindByNameIgnoreCaseNotFound() {
    assertNull(getRepository().findByNameIgnoreCase(null));
  }

  /**
   * Test method for {@link IMediaRepository#exists(java.util.UUID, java.lang.String)}.
   */
  @Test
  public void testExistsUuidString() {
    final boolean expected = true;
    final Media model = this.buildTestEntity();
    final List<Media> models = new ArrayList<>(1);
    models.add(model);
    getRepository().save(models);

    final boolean actual = getRepository().exists(model.getId(), model.getName());

    assertThat(actual, is(expected));
  }

  /**
   * Test method for {@link IMediaRepository#exists(java.util.UUID, java.lang.String)}.
   */
  @Test
  public void testExistsUuidStringNotFound() {
    final boolean expected = false;

    final boolean actual = getRepository().exists(RANDOM_ID, DISPLAYNAME);

    assertThat(actual, is(expected));
  }

}
