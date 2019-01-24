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
import com.monogramm.starter.persistence.permission.dao.IPermissionRepository;
import com.monogramm.starter.persistence.user.dao.IUserRepository;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

/**
 * {@link MediaServiceImpl} Unit Test.
 * 
 * @author madmath03
 */
public class MediaServiceImplTest
    extends AbstractGenericServiceTest<Media, MediaDto, MediaServiceImpl> {

  private static final String DISPLAYNAME = MediaServiceImplTest.class.getSimpleName();

  /**
   * @throws java.lang.Exception if the test setup crashes.
   */
  @Before
  public void setUp() throws Exception {
    super.setUp();
  }

  /**
   * @throws java.lang.Exception if the test cleanup crashes.
   */
  @After
  public void tearDown() throws Exception {
    super.tearDown();
  }

  @Override
  protected MediaServiceImpl buildTestService() {
    return new MediaServiceImpl(getMockRepository(), getMockUserRepository(),
        getMockAuthenticationFacade());
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
    return Media.builder(DISPLAYNAME).id(ID).build();
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

    when(getMockRepository().exists(model.getId(), model.getName())).thenReturn(false);

    assertTrue(getService().add(model));

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

  /**
   * Test method for
   * {@link MediaService#MediaService(MediaRepository, IUserRepository, IPermissionRepository)}.
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

}
