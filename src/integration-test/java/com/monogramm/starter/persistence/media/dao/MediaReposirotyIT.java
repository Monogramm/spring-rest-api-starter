/*
 * Creation by madmath03 the 2019-01-24.
 */

package com.monogramm.starter.persistence.media.dao;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import com.monogramm.starter.persistence.AbstractGenericRepositoryIT;
import com.monogramm.starter.persistence.media.entity.Media;
import com.monogramm.starter.persistence.media.exception.MediaNotFoundException;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * {@link MediaReposiroty} Integration Test.
 * 
 * @author madmath03
 */
public class MediaReposirotyIT extends AbstractGenericRepositoryIT<Media, MediaRepository> {

  private static final String DISPLAYNAME = MediaReposirotyIT.class.getSimpleName();
  private static final String PATH = "TEST/MYFILE.TXT";

  @Override
  protected Media buildTestEntity() {
    return Media.builder(DISPLAYNAME, PATH).build();
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
