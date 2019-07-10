/*
 * Creation by madmath03 the 2019-01-24.
 */

package com.monogramm.starter.persistence.media.service;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.monogramm.starter.dto.media.MediaDto;
import com.monogramm.starter.persistence.AbstractGenericServiceTest;
import com.monogramm.starter.persistence.EntityNotFoundException;
import com.monogramm.starter.persistence.media.dao.MediaRepository;
import com.monogramm.starter.persistence.media.entity.Media;
import com.monogramm.starter.persistence.media.exception.MediaNotFoundException;
import com.monogramm.starter.persistence.media.properties.FileStorageProperties;
import com.monogramm.starter.persistence.permission.dao.PermissionRepository;
import com.monogramm.starter.persistence.user.dao.UserRepository;
import com.monogramm.starter.persistence.user.entity.User;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.core.io.Resource;

/**
 * {@link MediaServiceImpl} Unit Test.
 * 
 * @author madmath03
 */
public class MediaServiceImplTest
    extends AbstractGenericServiceTest<Media, MediaDto, MediaServiceImpl> {

  private static final String PREFIX = MediaServiceImplTest.class.getSimpleName() + "_";

  private static final String DISPLAYNAME = MediaServiceImplTest.class.getSimpleName();

  private static final String TEST_MEDIA_PATH_VALUE = "data/test/media";
  private static final Path TEST_MEDIA_PATH = Paths.get(TEST_MEDIA_PATH_VALUE);
  private static final FileStorageProperties STORAGE_PROPERTIES =
      new FileStorageProperties(TEST_MEDIA_PATH);

  private Path tempDirectory;
  private Path tempFile;

  private Path tempExistingDirectory;
  private Path tempExistingFile;

  /**
   * @throws java.lang.Exception if the test setup crashes.
   */
  @Before
  public void setUp() throws Exception {
    super.setUp();

    this.tempDirectory = Files.createTempDirectory(PREFIX);
    this.tempFile = Files.createTempFile(tempDirectory, PREFIX, ".tmp");

    this.tempExistingDirectory = Paths.get(ID.toString());
    final Path tempExistingDirFullPath =
        Files.createDirectories(TEST_MEDIA_PATH.resolve(tempExistingDirectory));
    this.tempExistingFile = Files.createTempFile(tempExistingDirFullPath, PREFIX, ".tmp");
  }

  /**
   * @throws java.lang.Exception if the test cleanup crashes.
   */
  @After
  public void tearDown() throws Exception {
    super.tearDown();

    Files.delete(tempFile);
    FileUtils.cleanDirectory(tempDirectory.toFile());
    Files.delete(tempDirectory);

    this.tempExistingFile.toFile().delete();
    this.tempExistingDirectory.toFile().delete();
  }

  @Override
  protected MediaServiceImpl buildTestService() {
    return new MediaServiceImpl(getMockRepository(), getMockUserRepository(),
        getMockAuthenticationFacade(), STORAGE_PROPERTIES);
  }

  @Override
  protected MediaRepository buildMockRepository() {
    return mock(MediaRepository.class);
  }

  @Override
  public MediaRepository getMockRepository() {
    return (MediaRepository) super.getMockRepository();
  }

  @Override
  protected Media buildTestEntity() {
    return Media.builder(DISPLAYNAME).path(UUID.randomUUID().toString()).id(ID).build();
  }

  @Override
  protected EntityNotFoundException buildEntityNotFoundException() {
    return new MediaNotFoundException();
  }

  @Override
  @Test
  public void testExists() {
    final Media model = this.buildTestEntity();

    when(getMockRepository().exists(model.getId(), model.getName())).thenReturn(true);

    assertTrue(getService().exists(model));

    verify(getMockRepository(), times(1)).exists(model.getId(), model.getName());
    verifyNoMoreInteractions(getMockRepository());
  }

  @Override
  @Test
  public void testExistsNotFound() {
    final Media model = this.buildTestEntity();

    when(getMockRepository().exists(model.getId(), model.getName())).thenReturn(false);

    assertFalse(getService().exists(model));

    verify(getMockRepository(), times(1)).exists(model.getId(), model.getName());
    verifyNoMoreInteractions(getMockRepository());
  }

  /**
   * Test method for {@link MediaService#add(com.monogramm.starter.persistence.media.entity.Media)}.
   */
  @Override
  @Test
  public void testAdd() {
    final Media model = this.buildTestEntity();

    InputStream inputStream = null;
    try {
      inputStream = new FileInputStream(tempFile.toFile());
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      fail(e.getMessage());
    }
    assertNotNull(inputStream);

    model.setInputStream(inputStream);

    when(getMockRepository().exists(model.getId(), model.getName())).thenReturn(false);

    assertTrue(getService().add(model));

    try {
      inputStream.close();
    } catch (IOException e) {
      e.printStackTrace();
      fail(e.getMessage());
    }

    ArgumentCaptor<Media> mediaArgument = ArgumentCaptor.forClass(Media.class);
    verify(getMockRepository(), times(1)).exists(model.getId(), model.getName());
    verify(getMockRepository(), times(1)).add(mediaArgument.capture());
    verifyNoMoreInteractions(getMockRepository());

    final Media actual = mediaArgument.getValue();

    assertThat(actual.getName(), is(model.getName()));
  }

  /**
   * Test method for {@link MediaService#add(com.monogramm.starter.persistence.media.entity.Media)}.
   */
  @Override
  @Test
  public void testAddAlreadyExists() {
    final Media model = this.buildTestEntity();

    when(getMockRepository().exists(model.getId(), model.getName())).thenReturn(true);

    assertFalse(getService().add(model));

    verify(getMockRepository(), times(1)).exists(model.getId(), model.getName());
    verifyNoMoreInteractions(getMockRepository());
  }

  @Override
  @Test
  public void testDeleteById() {
    final Media model = this.buildTestEntity();
    model.setPath(tempExistingDirectory);

    when(getMockRepository().findById(ID)).thenReturn(model);
    when(getMockRepository().deleteById(ID)).thenReturn(1);

    getService().deleteById(ID);

    verify(getMockRepository(), times(1)).findById(ID);
    verify(getMockRepository(), times(1)).deleteById(ID);
    verifyNoMoreInteractions(getMockRepository());

    assertFalse(tempExistingDirectory.toFile().exists());
  }

  @Override
  @Test
  public void testDeleteByIdAndOwner() {
    final Media model = this.buildTestEntity();
    model.setPath(tempExistingDirectory);
    final UUID ownerId = null;
    final User owner = User.builder().id(ownerId).build();

    when(getMockRepository().findByIdAndOwner(ID, owner)).thenReturn(model);
    //when(getMockRepository().delete(model));

    getService().deleteByIdAndOwner(ID, ownerId);

    verify(getMockRepository(), times(1)).findByIdAndOwner(ID, owner);
    verify(getMockRepository(), times(1)).delete(model);
    verifyNoMoreInteractions(getMockRepository());

    assertFalse(tempExistingDirectory.toFile().exists());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDeleteByIdAndOwnerException() {
    final Media model = this.buildTestEntity();
    model.setPath(tempExistingDirectory);
    final UUID ownerId = null;
    final User owner = User.builder().id(ownerId).build();

    when(getMockRepository().findByIdAndOwner(ID, owner)).thenReturn(model);
    doThrow(new IllegalArgumentException()).when(getMockRepository()).delete(model);

    getService().deleteByIdAndOwner(ID, ownerId);
  }

  /**
   * Test method for
   * {@link MediaService#MediaService(MediaRepository, UserRepository, PermissionRepository)}.
   */
  @Test
  public void testMediaService() {
    assertNotNull(getService());
  }

  /**
   * Test method for {@link MediaService#findById(java.util.UUID)}.
   * 
   * @throws MediaNotFoundException if the media is not found.
   */
  @Test
  public void testFindByName() {
    final Media model = this.buildTestEntity();

    when(getMockRepository().findByNameIgnoreCase(DISPLAYNAME)).thenReturn(model);

    final Media actual = getService().findByName(DISPLAYNAME);

    verify(getMockRepository(), times(1)).findByNameIgnoreCase(DISPLAYNAME);
    verifyNoMoreInteractions(getMockRepository());

    assertThat(actual, is(model));
  }

  /**
   * Test method for {@link MediaService#findById(java.util.UUID)}.
   * 
   * @throws MediaNotFoundException if the media is not found.
   */
  @Test
  public void testFindByNameNotFound() {
    when(getMockRepository().findByNameIgnoreCase(DISPLAYNAME)).thenReturn(null);

    final Media actual = getService().findByName(DISPLAYNAME);

    verify(getMockRepository(), times(1)).findByNameIgnoreCase(DISPLAYNAME);
    verifyNoMoreInteractions(getMockRepository());

    assertNull(actual);
  }

  /**
   * Test method for {@link MediaService#findById(java.util.UUID)}.
   * 
   * @throws MediaNotFoundException if the media is not found.
   */
  @Test(expected = MediaNotFoundException.class)
  public void testFindByNameMediaNotFoundException() {
    when(getMockRepository().findByNameIgnoreCase(DISPLAYNAME))
        .thenThrow(new MediaNotFoundException());

    getService().findByName(DISPLAYNAME);
  }

  /**
   * Test method for {@link MediaService#findAllByName(java.lang.String)}.
   */
  @Test
  public void testFindAllByName() {
    final List<Media> models = new ArrayList<>();
    when(getMockRepository().findAllContainingNameIgnoreCase(DISPLAYNAME)).thenReturn(models);

    final List<Media> actual = getService().findAllByName(DISPLAYNAME);

    verify(getMockRepository(), times(1)).findAllContainingNameIgnoreCase(DISPLAYNAME);
    verifyNoMoreInteractions(getMockRepository());

    assertThat(actual, is(models));
  }


  @Test
  public void testLoadById() {
    final Media model = this.buildTestEntity();

    InputStream inputStream = null;
    try {
      inputStream = new FileInputStream(tempFile.toFile());
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      fail(e.getMessage());
    }
    assertNotNull(inputStream);
    model.setInputStream(inputStream);

    when(getMockRepository().exists(model.getId(), model.getName())).thenReturn(false);
    assertTrue(getService().add(model));
    ArgumentCaptor<Media> mediaArgument = ArgumentCaptor.forClass(Media.class);
    verify(getMockRepository(), times(1)).exists(model.getId(), model.getName());
    verify(getMockRepository(), times(1)).add(mediaArgument.capture());

    when(getMockRepository().findById(model.getId())).thenReturn(model);
    final Resource resource = getService().loadById(ID);
    assertNotNull(resource);

    try {
      inputStream.close();
    } catch (IOException e) {
      e.printStackTrace();
      fail(e.getMessage());
    }

    verify(getMockRepository(), times(1)).findById(model.getId());
    verifyNoMoreInteractions(getMockRepository());
  }

  @Test
  public void testLoadByIdAndOwner() {
    final Media model = this.buildTestEntity();
    final UUID ownerId = UUID.randomUUID();
    final User owner = User.builder().id(ownerId).build();

    InputStream inputStream = null;
    try {
      inputStream = new FileInputStream(tempFile.toFile());
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      fail(e.getMessage());
    }
    assertNotNull(inputStream);
    model.setInputStream(inputStream);

    when(getMockRepository().exists(model.getId(), model.getName())).thenReturn(false);
    assertTrue(getService().add(model));
    ArgumentCaptor<Media> mediaArgument = ArgumentCaptor.forClass(Media.class);
    verify(getMockRepository(), times(1)).exists(model.getId(), model.getName());
    verify(getMockRepository(), times(1)).add(mediaArgument.capture());

    when(getMockRepository().findByIdAndOwner(model.getId(), owner)).thenReturn(model);
    final Resource resource = getService().loadByIdAndOwner(ID, owner);
    assertNotNull(resource);

    try {
      inputStream.close();
    } catch (IOException e) {
      e.printStackTrace();
      fail(e.getMessage());
    }

    verify(getMockRepository(), times(1)).findByIdAndOwner(model.getId(), owner);
    verifyNoMoreInteractions(getMockRepository());
  }

}
