/*
 * Creation by madmath03 the 2019-01-29.
 */

package com.monogramm.starter.api.media.controller;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.monogramm.Application;
import com.monogramm.starter.api.AbstractControllerIT;
import com.monogramm.starter.api.AbstractControllerMockIT;
import com.monogramm.starter.api.AbstractGenericController;
import com.monogramm.starter.config.data.GenericOperation;
import com.monogramm.starter.config.data.InitialDataLoader;
import com.monogramm.starter.dto.media.MediaDto;
import com.monogramm.starter.persistence.media.entity.Media;
import com.monogramm.starter.persistence.media.exception.MediaNotFoundException;
import com.monogramm.starter.persistence.media.service.MediaService;
import com.monogramm.starter.persistence.user.entity.User;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import javax.transaction.Transactional;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MvcResult;


/**
 * {@link MediaController} Mock Integration Test.
 * 
 * <p>
 * We assume the environment is freshly created and only contains the initial data.
 * </p>
 * 
 * <p>
 * Spring boot test is searching {@code @SpringBootConfiguration} or {@code @SpringBootApplication}.
 * In this case it will automatically find {@link Application} boot main class.
 * </p>
 * 
 * @see Application
 * @see InitialDataLoader
 * @see AbstractControllerIT
 * 
 * @author madmath03
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
public class MediaControllerMockIT extends AbstractControllerMockIT {
  /**
   * Logger for {@link MediaControllerMockIT}.
   */
  private static final Logger LOG = LoggerFactory.getLogger(MediaControllerMockIT.class);

  /**
   * The managed type of this tested controller.
   */
  public static final String TYPE = MediaController.TYPE;
  /**
   * The request base path of this tested controller.
   */
  public static final String CONTROLLER_PATH = MediaController.CONTROLLER_PATH;

  private static final String DISPLAYNAME = "Foo";

  private static final String PREFIX = MediaControllerMockIT.class.getSimpleName() + "_";

  private UUID randomId;

  private User testCreatedBy;
  private User testOwner;
  private Media testEntity;

  @Autowired
  private MediaService mediaService;

  private Path tempDirectory;
  private Path tempFile;
  private InputStream testInputStream;

  @Before
  public void setUp() throws IOException {
    super.setUpMockMvc();
    super.setUpValidUser(GenericOperation.allPermissionNames(TYPE));

    this.randomId = UUID.randomUUID();

    // Add the users
    testCreatedBy =
        User.builder(DISPLAYNAME + "_Creator", DISPLAYNAME + ".creator@creation.org").build();
    assertTrue(getUserService().add(testCreatedBy));
    testOwner = User.builder(DISPLAYNAME + "_Owner", DISPLAYNAME + ".owner@creation.org").build();
    assertTrue(getUserService().add(testOwner));

    // Add a media
    this.tempDirectory = Files.createTempDirectory(PREFIX);
    this.tempFile = Files.createTempFile(tempDirectory, PREFIX, ".tmp");

    testEntity = Media.builder(DISPLAYNAME).createdBy(testCreatedBy).owner(testOwner).build();

    testInputStream = null;
    try {
      testInputStream = new FileInputStream(tempFile.toFile());
    } catch (FileNotFoundException e) {
      fail(e.getMessage());
    }
    assertNotNull(testInputStream);
    testEntity.setInputStream(testInputStream);

    assertTrue(mediaService.add(testEntity));
  }

  @After
  public void tearDown() throws IOException {
    super.tearDownValidUser();

    if (testInputStream != null) {
      try {
        testInputStream.close();
      } catch (IOException e) {
        LOG.error("Error when closing test input stream", e);
      }
      testInputStream = null;
    }

    Files.delete(tempFile);
    FileUtils.cleanDirectory(tempDirectory.toFile());
    Files.delete(tempDirectory);

    this.deleteMedia(testEntity);
    testEntity = null;

    super.deleteUser(testCreatedBy);
    testCreatedBy = null;

    super.deleteUser(testOwner);
    testOwner = null;
  }

  /**
   * Delete a media from database and storage.
   * 
   * @param media the media to delete.
   */
  protected final void deleteMedia(final Media media) {
    try {
      if (media != null && media.getId() != null) {
        mediaService.deleteById(media.getId());
      }
    } catch (MediaNotFoundException e) {
      LOG.trace("Media already deleted: " + media, e);
    }
  }

  /**
   * Test method for
   * {@link MediaController#getDataById(String, org.springframework.web.context.request.WebRequest, javax.servlet.http.HttpServletResponse)}.
   * 
   * @throws Exception if the test crashes.
   */
  @Test
  public void testGetMediaByIdRandomId() throws Exception {
    // No permission returned
    MvcResult result = getMockMvc()
        .perform(get(CONTROLLER_PATH + '/' + randomId).headers(getHeaders(getMockToken())))
        .andExpect(status().isNotFound()).andReturn();

    assertNotNull(result.getResponse());
    assertNotNull(result.getResponse().getContentAsString());
  }

  /**
   * Test method for
   * {@link MediaController#getDataById(String, org.springframework.web.context.request.WebRequest, javax.servlet.http.HttpServletResponse)}.
   * 
   * @throws Exception if the test crashes.
   */
  @Test
  public void testGetMediaById() throws Exception {
    // Media previously created should be returned
    getMockMvc()
        .perform(get(CONTROLLER_PATH + '/' + this.testEntity.getId())
            .headers(getHeaders(getMockToken())))
        .andExpect(status().isOk()).andExpect(jsonPath("$.id", notNullValue()))
        .andExpect(jsonPath("$.id", equalToIgnoringCase(this.testEntity.getId().toString())))
        .andExpect(jsonPath("$.name", equalToIgnoringCase(DISPLAYNAME)))
        .andExpect(jsonPath("$.createdAt", notNullValue()))
        .andExpect(jsonPath("$.createdBy", notNullValue()))
        .andExpect(jsonPath("$.createdBy", equalToIgnoringCase(testCreatedBy.getId().toString())))
        .andExpect(jsonPath("$.modifiedAt", nullValue()))
        .andExpect(jsonPath("$.modifiedBy", nullValue()))
        .andExpect(jsonPath("$.owner", notNullValue()))
        .andExpect(jsonPath("$.owner", equalToIgnoringCase(testOwner.getId().toString())));
  }

  /**
   * Test method for {@link MediaController#getAllData()}.
   * 
   * @throws Exception if the test crashes.
   */
  @Test
  public void testGetAllMedias() throws Exception {
    // There should at least be the test entities...
    int expectedSize = 1;

    getMockMvc().perform(get(CONTROLLER_PATH).headers(getHeaders(getMockToken())))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$", hasSize(expectedSize)));
  }

  /**
   * Test method for
   * {@link MediaController#getAllDataPaginated(int, int, org.springframework.web.context.request.WebRequest, org.springframework.web.util.UriComponentsBuilder, javax.servlet.http.HttpServletResponse)}.
   * 
   * @throws Exception if the test crashes.
   */
  @Test
  public void testGetAllMediasPaginated() throws Exception {
    int expectedSize = 1;

    getMockMvc()
        .perform(get(CONTROLLER_PATH).param("page", "0").param("size", "1")
            .headers(getHeaders(getMockToken())))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$", hasSize(expectedSize)));
  }

  /**
   * Test method for
   * {@link MediaController#getAllDataPaginated(int, int, org.springframework.web.context.request.WebRequest, org.springframework.web.util.UriComponentsBuilder, javax.servlet.http.HttpServletResponse)}.
   * 
   * @throws Exception if the test crashes.
   */
  @Test
  public void testGetAllMediasPaginatedDefaultSize() throws Exception {
    // There should at least be the test entity...
    int expectedSize = 1;
    expectedSize = Math.min(expectedSize, AbstractGenericController.DEFAULT_SIZE_INT);

    getMockMvc()
        .perform(get(CONTROLLER_PATH).param("page", "0").headers(getHeaders(getMockToken())))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$", hasSize(expectedSize)));
  }

  /**
   * Test method for {@link MediaController#updateData(String, Media)}.
   * 
   * @throws MediaNotFoundException if the media entity to update is not found.
   */
  @Test
  public void testUpdateMedia() throws Exception {
    // Update on random UUID should not find any media
    final Media dummyModel = Media.builder("God").id(randomId).build();
    final MediaDto dummyDto = mediaService.toDto(dummyModel);

    this.getMockMvc().perform(put(CONTROLLER_PATH + '/' + randomId)
        .headers(getHeaders(getMockToken())).content(dummyDto.toJson()))
        .andExpect(status().isNotFound());

    // Update the media
    final String newName = "Bar";
    this.testEntity.setName(newName);
    this.testEntity.setModifiedBy(testOwner);
    final MediaDto dto = mediaService.toDto(this.testEntity);

    // Update test media should work
    final String entityJson = this.testEntity.toJson();
    final String dtoJson = dto.toJson();

    assertEquals(dtoJson, entityJson);

    this.getMockMvc()
        .perform(put(CONTROLLER_PATH + '/' + this.testEntity.getId())
            .headers(getHeaders(getMockToken())).content(dtoJson))
        .andExpect(status().isOk()).andExpect(jsonPath("$.id", notNullValue()))
        .andExpect(jsonPath("$.id", equalToIgnoringCase(this.testEntity.getId().toString())))
        .andExpect(jsonPath("$.name", equalToIgnoringCase(newName)))
        .andExpect(jsonPath("$.createdAt", notNullValue()))
        .andExpect(jsonPath("$.createdBy", notNullValue()))
        .andExpect(jsonPath("$.createdBy", equalToIgnoringCase(testCreatedBy.getId().toString())))
        .andExpect(jsonPath("$.modifiedAt", notNullValue()))
        .andExpect(jsonPath("$.modifiedBy", notNullValue()))
        .andExpect(jsonPath("$.modifiedBy", equalToIgnoringCase(testOwner.getId().toString())))
        .andExpect(jsonPath("$.owner", notNullValue()))
        .andExpect(jsonPath("$.owner", equalToIgnoringCase(testOwner.getId().toString())));
  }

  /**
   * Test method for {@link MediaController#deleteData(java.lang.String)}.
   * 
   * @throws MediaNotFoundException if the media entity to delete is not found.
   */
  @Test
  public void testDeleteMedia() throws Exception {
    // Delete on random UUID should not find any media
    this.getMockMvc()
        .perform(delete(CONTROLLER_PATH + '/' + randomId).headers(getHeaders(getMockToken())))
        .andExpect(status().isNotFound());

    // Delete test media should work
    this.getMockMvc().perform(
        delete(CONTROLLER_PATH + '/' + this.testEntity.getId()).headers(getHeaders(getMockToken())))
        .andExpect(status().isNoContent());
  }

}
