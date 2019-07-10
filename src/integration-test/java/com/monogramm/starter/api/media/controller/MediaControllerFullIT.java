/*
 * Creation by madmath03 the 2019-01-29.
 */

package com.monogramm.starter.api.media.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.monogramm.Application;
import com.monogramm.starter.api.AbstractControllerFullIT;
import com.monogramm.starter.config.data.GenericOperation;
import com.monogramm.starter.config.data.InitialDataLoader;
import com.monogramm.starter.dto.media.MediaDto;
import com.monogramm.starter.persistence.media.entity.Media;
import com.monogramm.starter.persistence.media.exception.MediaNotFoundException;
import com.monogramm.starter.persistence.media.service.MediaService;
import com.monogramm.starter.persistence.role.entity.Role;
import com.monogramm.starter.persistence.user.entity.User;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * {@link MediaController} Integration Test.
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
 * @see AbstractControllerFullIT
 * 
 * @author madmath03
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class MediaControllerFullIT extends AbstractControllerFullIT {
  /**
   * Logger for {@link MediaControllerFullIT}.
   */
  private static final Logger LOG = LoggerFactory.getLogger(MediaControllerFullIT.class);

  /**
   * The managed type of this tested controller.
   */
  public static final String TYPE = MediaController.TYPE;
  /**
   * The request base path of this tested controller.
   */
  public static final String CONTROLLER_PATH = MediaController.CONTROLLER_PATH;
  public static final String UPLOAD_PATH = MediaController.UPLOAD_PATH;
  public static final String DOWNLOAD_PATH = MediaController.DOWNLOAD_PATH;

  private static final String DISPLAYNAME = MediaControllerFullIT.class.getSimpleName();

  private static final String PREFIX = MediaControllerFullIT.class.getSimpleName() + "_";

  private User testCreatedBy;
  private String creatorAccessToken;
  private User testOwner;
  private String ownerAccessToken;

  private Media testEntity;
  private MediaDto testDto;

  private Role nonAdminRole;

  private String accessToken;

  @Autowired
  private MediaService mediaService;

  private Path tempDirectory;
  private Path tempFile;
  private InputStream testInputStream;

  @Autowired
  private ObjectMapper mapper;

  @Before
  public void setUp() throws URISyntaxException, IOException {
    // Set up a valid user for authentication and such
    super.setUpValidUser(GenericOperation.allPermissionNames(TYPE));

    // Add a non-admin role
    final String[] nonAdminPermissions =
        GenericOperation.allPermissionNames(TYPE, GenericOperation.READ, GenericOperation.CREATE,
            GenericOperation.UPDATE, GenericOperation.DELETE);
    nonAdminRole = this.createRole("Media-User", nonAdminPermissions);
    assertNotNull(nonAdminRole);

    // Get an access token for later calls to API
    this.accessToken = this.getFullToken();

    testCreatedBy = this.createUser(DISPLAYNAME + "_Creator", DISPLAYNAME + ".creator@creation.org",
        PASSWORD, nonAdminRole);
    this.creatorAccessToken = this.getFullToken(testCreatedBy, PASSWORD);

    testOwner = this.createUser(DISPLAYNAME + "_Owner", DISPLAYNAME + ".owner@creation.org",
        PASSWORD, nonAdminRole);
    this.ownerAccessToken = this.getFullToken(testOwner, PASSWORD);

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
    testDto = mediaService.toDto(testEntity);

    mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
  }

  @After
  public void tearDown() throws URISyntaxException, IOException {
    // Tear valid user for authentication
    super.tearDownValidUser();

    // Revoke an access token
    this.revokeToken(this.accessToken);

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
    testDto = null;

    this.deleteUser(testCreatedBy);
    testCreatedBy = null;

    this.deleteUser(testOwner);
    testOwner = null;

    this.deleteRole(nonAdminRole);
    nonAdminRole = null;
  }

  /**
   * Delete a media from database and storage.
   * 
   * @param media the media to delete.
   */
  protected final void deleteMedia(final Media media) {
    try {
      if (media != null && media.getId() != null) {
        mediaService.deleteById(testEntity.getId());
      }
    } catch (MediaNotFoundException e) {
      LOG.trace("Media already deleted: " + media, e);
    }
  }

  /**
   * Test method for {@link MediaController#getDataById(java.lang.String)}.
   * 
   * @throws URISyntaxException if the URL could not be created.
   */
  @Test
  public void testGetMediaById() throws URISyntaxException {
    final HttpHeaders headers = getHeaders(this.accessToken);

    final String url = this.getUrl(CONTROLLER_PATH, "/", testEntity.getId());
    final HttpEntity<String> requestEntity = new HttpEntity<>(headers);

    final ResponseEntity<MediaDto> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.GET, requestEntity, MediaDto.class);

    final MediaDto dto = responseEntity.getBody();

    assertNotNull(dto);
    assertEquals(this.testDto, dto);
    assertNotNull(dto.getId());
    assertNotNull(dto.getCreatedAt());
    assertNotNull(dto.getCreatedBy());
    assertEquals(testCreatedBy.getId(), dto.getCreatedBy());
    assertNull(dto.getModifiedAt());
    assertNull(dto.getModifiedBy());
    assertNotNull(dto.getOwner());
    assertEquals(testOwner.getId(), dto.getOwner());
  }

  /**
   * Test method for {@link MediaController#getDataById(java.lang.String)}.
   * 
   * @throws URISyntaxException if the URL could not be created.
   */
  @Test
  public void testGetMediaByIdNoAuthorization() throws URISyntaxException {
    final HttpHeaders headers = getHeaders();

    final String url = this.getUrl(CONTROLLER_PATH, "/", testEntity.getId());
    final HttpEntity<String> requestEntity = new HttpEntity<>(headers);

    final ResponseEntity<MediaDto> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.GET, requestEntity, MediaDto.class);

    assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
  }

  /**
   * Test method for {@link MediaController#getAllData()}.
   * 
   * @throws URISyntaxException if the URL could not be created.
   */
  @Test
  public void testGetAllMedias() throws URISyntaxException {
    final HttpHeaders headers = getHeaders(this.accessToken);

    final String url = this.getUrl(CONTROLLER_PATH);
    final HttpEntity<String> requestEntity = new HttpEntity<>(headers);

    final ResponseEntity<MediaDto[]> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.GET, requestEntity, MediaDto[].class);

    final MediaDto[] dtos = responseEntity.getBody();

    assertNotNull(dtos);
    assertTrue(Arrays.stream(dtos).anyMatch(a -> DISPLAYNAME.equals(a.getName())));
    assertTrue(Arrays.stream(dtos).anyMatch(a -> testCreatedBy.getId().equals(a.getCreatedBy())));
    assertTrue(Arrays.stream(dtos).anyMatch(a -> testOwner.getId().equals(a.getOwner())));
  }

  /**
   * Test method for {@link MediaController#getAllData()}.
   * 
   * @throws URISyntaxException if the URL could not be created.
   */
  @Test
  public void testGetAllMediasOwner() throws URISyntaxException {
    final HttpHeaders headers = getHeaders(this.ownerAccessToken);

    final String url = this.getUrl(CONTROLLER_PATH);
    final HttpEntity<String> requestEntity = new HttpEntity<>(headers);

    final ResponseEntity<MediaDto[]> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.GET, requestEntity, MediaDto[].class);

    final MediaDto[] dtos = responseEntity.getBody();

    assertNotNull(dtos);
    assertTrue(Arrays.stream(dtos).anyMatch(a -> DISPLAYNAME.equals(a.getName())));
    assertTrue(Arrays.stream(dtos).anyMatch(a -> testCreatedBy.getId().equals(a.getCreatedBy())));
    assertTrue(Arrays.stream(dtos).anyMatch(a -> testOwner.getId().equals(a.getOwner())));
  }

  /**
   * Test method for {@link MediaController#getAllData()}.
   * 
   * @throws URISyntaxException if the URL could not be created.
   */
  @Test
  public void testGetAllMediasCreator() throws URISyntaxException {
    final HttpHeaders headers = getHeaders(this.creatorAccessToken);

    final String url = this.getUrl(CONTROLLER_PATH);
    final HttpEntity<String> requestEntity = new HttpEntity<>(headers);

    final ResponseEntity<MediaDto[]> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.GET, requestEntity, MediaDto[].class);

    final MediaDto[] dtos = responseEntity.getBody();

    assertNotNull(dtos);
    assertEquals(0, dtos.length);
  }

  /**
   * Test method for {@link MediaController#getAllData()}.
   * 
   * @throws URISyntaxException if the URL could not be created.
   */
  @Test
  public void testGetAllMediasNoAuthorization() throws URISyntaxException {
    final HttpHeaders headers = getHeaders();

    final String url = this.getUrl(CONTROLLER_PATH);
    final HttpEntity<String> requestEntity = new HttpEntity<>(headers);

    final ResponseEntity<Object> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.GET, requestEntity, Object.class);

    assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
  }

  /**
   * Test method for
   * {@link MediaController#getAllDataPaginated(int, int, org.springframework.web.context.request.WebRequest, org.springframework.web.util.UriComponentsBuilder, javax.servlet.http.HttpServletResponse)}.
   * 
   * @throws URISyntaxException if the URL could not be created.
   */
  @Test
  public void testGetAllMediasPaginated() throws URISyntaxException {
    final HttpHeaders headers = getHeaders(this.accessToken);

    final String url = this.getUrl(new String[] {CONTROLLER_PATH}, "page=0");
    final HttpEntity<String> requestEntity = new HttpEntity<>(headers);

    final ResponseEntity<MediaDto[]> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.GET, requestEntity, MediaDto[].class);

    final MediaDto[] dtos = responseEntity.getBody();

    assertNotNull(dtos);
    assertTrue(Arrays.stream(dtos).anyMatch(a -> DISPLAYNAME.equals(a.getName())));
    assertTrue(Arrays.stream(dtos).anyMatch(a -> testCreatedBy.getId().equals(a.getCreatedBy())));
    assertTrue(Arrays.stream(dtos).anyMatch(a -> testOwner.getId().equals(a.getOwner())));
  }

  /**
   * Test method for
   * {@link MediaController#getAllDataPaginated(int, int, org.springframework.web.context.request.WebRequest, org.springframework.web.util.UriComponentsBuilder, javax.servlet.http.HttpServletResponse)}.
   * 
   * @throws URISyntaxException if the URL could not be created.
   */
  @Test
  public void testGetAllMediasPaginatedOwner() throws URISyntaxException {
    final HttpHeaders headers = getHeaders(this.ownerAccessToken);

    final String url = this.getUrl(new String[] {CONTROLLER_PATH}, "page=0");
    final HttpEntity<String> requestEntity = new HttpEntity<>(headers);

    final ResponseEntity<MediaDto[]> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.GET, requestEntity, MediaDto[].class);

    final MediaDto[] dtos = responseEntity.getBody();

    assertNotNull(dtos);
    assertTrue(Arrays.stream(dtos).anyMatch(a -> DISPLAYNAME.equals(a.getName())));
    assertTrue(Arrays.stream(dtos).anyMatch(a -> testCreatedBy.getId().equals(a.getCreatedBy())));
    assertTrue(Arrays.stream(dtos).anyMatch(a -> testOwner.getId().equals(a.getOwner())));
  }

  /**
   * Test method for
   * {@link MediaController#getAllDataPaginated(int, int, org.springframework.web.context.request.WebRequest, org.springframework.web.util.UriComponentsBuilder, javax.servlet.http.HttpServletResponse)}.
   * 
   * @throws URISyntaxException if the URL could not be created.
   */
  @Test
  public void testGetAllMediasPaginatedCreator() throws URISyntaxException {
    final HttpHeaders headers = getHeaders(this.creatorAccessToken);

    final String url = this.getUrl(new String[] {CONTROLLER_PATH}, "page=0");
    final HttpEntity<String> requestEntity = new HttpEntity<>(headers);

    final ResponseEntity<MediaDto[]> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.GET, requestEntity, MediaDto[].class);

    final MediaDto[] dtos = responseEntity.getBody();

    assertNotNull(dtos);
    assertEquals(0, dtos.length);
  }

  /**
   * Test method for
   * {@link MediaController#getAllDataPaginated(int, int, org.springframework.web.context.request.WebRequest, org.springframework.web.util.UriComponentsBuilder, javax.servlet.http.HttpServletResponse)}.
   * 
   * @throws URISyntaxException if the URL could not be created.
   */
  @Test
  public void testGetAllMediasPaginatedNoAuthorization() throws URISyntaxException {
    final HttpHeaders headers = getHeaders();

    final String url = this.getUrl(new String[] {CONTROLLER_PATH}, "page=0");
    final HttpEntity<String> requestEntity = new HttpEntity<>(headers);

    final ResponseEntity<Object> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.GET, requestEntity, Object.class);

    assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
  }

  /**
   * Test method for {@link MediaController#updateData(String, Media)}.
   * 
   * @throws URISyntaxException if the URL could not be created.
   */
  @Test
  public void testUpdateMedia() throws URISyntaxException {
    final HttpHeaders headers = getHeaders(this.accessToken);

    final String url = this.getUrl(CONTROLLER_PATH, "/", this.testEntity.getId());

    this.testEntity.setStartDate(new Date());
    this.testDto = mediaService.toDto(this.testEntity);

    final HttpEntity<MediaDto> requestEntity = new HttpEntity<>(this.testDto, headers);

    final ResponseEntity<MediaDto> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.PUT, requestEntity, MediaDto.class);

    final MediaDto content = responseEntity.getBody();
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertNotNull(content);
    assertEquals(this.testDto, content);
    assertEquals(this.testDto.getName(), content.getName());
  }

  /**
   * Test method for {@link MediaController#updateData(String, Media)}.
   * 
   * @throws URISyntaxException if the URL could not be created.
   */
  @Test
  public void testUpdateMediaOwner() throws URISyntaxException {
    final HttpHeaders headers = getHeaders(this.ownerAccessToken);

    final String url = this.getUrl(CONTROLLER_PATH, "/", this.testEntity.getId());

    this.testEntity.setStartDate(new Date());
    this.testDto = mediaService.toDto(this.testEntity);

    final HttpEntity<MediaDto> requestEntity = new HttpEntity<>(this.testDto, headers);

    final ResponseEntity<MediaDto> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.PUT, requestEntity, MediaDto.class);

    final MediaDto content = responseEntity.getBody();
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertNotNull(content);
    assertEquals(this.testDto, content);
    assertEquals(this.testDto.getName(), content.getName());
  }

  /**
   * Test method for {@link MediaController#updateData(String, Media)}.
   * 
   * @throws URISyntaxException if the URL could not be created.
   */
  @Test
  public void testUpdateMediaCreator() throws URISyntaxException {
    final HttpHeaders headers = getHeaders(this.creatorAccessToken);

    final String url = this.getUrl(CONTROLLER_PATH, "/", this.testEntity.getId());

    this.testEntity.setStartDate(new Date());
    this.testDto = mediaService.toDto(this.testEntity);

    final HttpEntity<MediaDto> requestEntity = new HttpEntity<>(this.testDto, headers);

    final ResponseEntity<MediaDto> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.PUT, requestEntity, MediaDto.class);

    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
  }

  /**
   * Test method for {@link MediaController#updateData(String, Media)}.
   * 
   * @throws URISyntaxException if the URL could not be created.
   */
  @Test
  public void testUpdateMediaNoAuthorization() throws URISyntaxException {
    final HttpHeaders headers = getHeaders();

    final String url = this.getUrl(CONTROLLER_PATH, "/", this.testEntity.getId());

    this.testEntity.setName("Bar");
    this.testDto = mediaService.toDto(this.testEntity);

    final HttpEntity<MediaDto> requestEntity = new HttpEntity<>(this.testDto, headers);

    final ResponseEntity<MediaDto> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.PUT, requestEntity, MediaDto.class);

    assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
  }

  /**
   * Test method for {@link MediaController#deleteData(java.lang.String)}.
   * 
   * @throws URISyntaxException if the URL could not be created.
   */
  @Test
  public void testDeleteMedia() throws URISyntaxException {
    final HttpHeaders headers = getHeaders(this.accessToken);

    final String url = this.getUrl(CONTROLLER_PATH, "/", this.testEntity.getId());

    final HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

    final ResponseEntity<Void> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.DELETE, requestEntity, Void.class);

    assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
  }

  /**
   * Test method for {@link MediaController#deleteData(java.lang.String)}.
   * 
   * @throws URISyntaxException if the URL could not be created.
   */
  @Test
  public void testDeleteMediaOwner() throws URISyntaxException {
    final HttpHeaders headers = getHeaders(this.ownerAccessToken);

    final String url = this.getUrl(CONTROLLER_PATH, "/", this.testEntity.getId());

    final HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

    final ResponseEntity<Void> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.DELETE, requestEntity, Void.class);

    assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
  }

  /**
   * Test method for {@link MediaController#deleteData(java.lang.String)}.
   * 
   * @throws URISyntaxException if the URL could not be created.
   */
  @Test
  public void testDeleteMediaCreator() throws URISyntaxException {
    final HttpHeaders headers = getHeaders(this.creatorAccessToken);

    final String url = this.getUrl(CONTROLLER_PATH, "/", this.testEntity.getId());

    final HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

    final ResponseEntity<Void> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.DELETE, requestEntity, Void.class);

    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
  }

  /**
   * Test method for {@link MediaController#deleteData(java.lang.String)}.
   * 
   * @throws URISyntaxException if the URL could not be created.
   */
  @Test
  public void testDeleteMediaNoAuthorization() throws URISyntaxException {
    final HttpHeaders headers = getHeaders();

    final String url = this.getUrl(CONTROLLER_PATH, "/", this.testEntity.getId());

    final HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

    final ResponseEntity<Void> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.DELETE, requestEntity, Void.class);

    assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
  }

}
