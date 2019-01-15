package com.monogramm.starter.api.permission.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.monogramm.Application;
import com.monogramm.starter.api.AbstractControllerFullIT;
import com.monogramm.starter.config.data.GenericOperation;
import com.monogramm.starter.config.data.InitialDataLoader;
import com.monogramm.starter.dto.permission.PermissionDto;
import com.monogramm.starter.persistence.permission.entity.Permission;
import com.monogramm.starter.persistence.permission.exception.PermissionNotFoundException;
import com.monogramm.starter.persistence.user.entity.User;

import java.net.URISyntaxException;
import java.util.Arrays;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * {@link PermissionController} Integration Test.
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
public class PermissionControllerFullIT extends AbstractControllerFullIT {

  /**
   * Logger for {@link PermissionControllerFullIT}.
   */
  private static final Logger LOG = LogManager.getLogger(PermissionControllerFullIT.class);

  /**
   * The managed type of this tested controller.
   */
  public static final String TYPE = PermissionController.TYPE;
  /**
   * The request base path of this tested controller.
   */
  public static final String CONTROLLER_PATH = PermissionController.CONTROLLER_PATH;

  private static final String DISPLAYNAME = "Foo";

  private User testCreatedBy;
  private User testOwner;
  private Permission testEntity;
  private PermissionDto testDto;

  private String accessToken;

  @Before
  public void setUp() throws URISyntaxException {
    // Set up a valid user for authentication and such
    super.setUpValidUser(GenericOperation.allPermissionNames(TYPE));

    // Get an access token for later calls to API
    this.accessToken = this.getFullToken();

    testCreatedBy = this.createUser(DISPLAYNAME + "_Creator", DISPLAYNAME + ".creator@creation.org",
        null, null);

    testOwner =
        this.createUser(DISPLAYNAME + "_Owner", DISPLAYNAME + ".owner@creation.org", null, null);

    // Add a permission
    testEntity = Permission.builder(DISPLAYNAME).createdBy(testCreatedBy).owner(testOwner).build();
    assertTrue(getPermissionService().add(testEntity));
    testDto = getPermissionService().toDto(testEntity);
  }

  @After
  public void tearDown() throws URISyntaxException {
    // Tear valid user for authentication
    super.tearDownValidUser();

    // Revoke an access token
    this.revokeToken(this.accessToken);

    try {
      if (testEntity != null && testEntity.getId() != null) {
        getPermissionService().deleteById(testEntity.getId());
      }
    } catch (PermissionNotFoundException e) {
      LOG.trace("Permission already deleted: " + testEntity, e);
    }
    testEntity = null;
    testDto = null;

    this.deleteUser(testCreatedBy);
    testCreatedBy = null;

    this.deleteUser(testOwner);
    testOwner = null;
  }

  /**
   * Test method for {@link PermissionController#getDataById(java.lang.String)}.
   * 
   * @throws URISyntaxException if the URL could not be created.
   */
  @Test
  public void testGetPermissionById() throws URISyntaxException {
    final HttpHeaders headers = getHeaders(this.accessToken);

    final String url = this.getUrl(CONTROLLER_PATH, "/", testEntity.getId());
    final HttpEntity<String> requestEntity = new HttpEntity<>(headers);

    final ResponseEntity<PermissionDto> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.GET, requestEntity, PermissionDto.class);

    final PermissionDto dto = responseEntity.getBody();

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
   * Test method for {@link PermissionController#getDataById(java.lang.String)}.
   * 
   * @throws URISyntaxException if the URL could not be created.
   */
  @Test
  public void testGetPermissionByIdNoAuthorization() throws URISyntaxException {
    final HttpHeaders headers = getHeaders();

    final String url = this.getUrl(CONTROLLER_PATH, "/", testEntity.getId());
    final HttpEntity<String> requestEntity = new HttpEntity<>(headers);

    final ResponseEntity<PermissionDto> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.GET, requestEntity, PermissionDto.class);

    assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
  }

  /**
   * Test method for {@link PermissionController#getAllData()}.
   * 
   * @throws URISyntaxException if the URL could not be created.
   */
  @Test
  public void testGetAllPermissions() throws URISyntaxException {
    final HttpHeaders headers = getHeaders(this.accessToken);

    final String url = this.getUrl(CONTROLLER_PATH);
    final HttpEntity<String> requestEntity = new HttpEntity<>(headers);

    final ResponseEntity<PermissionDto[]> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.GET, requestEntity, PermissionDto[].class);

    final PermissionDto[] dtos = responseEntity.getBody();

    assertNotNull(dtos);
    assertTrue(Arrays.stream(dtos).anyMatch(a -> DISPLAYNAME.equals(a.getName())));
    assertTrue(Arrays.stream(dtos).anyMatch(a -> testCreatedBy.getId().equals(a.getCreatedBy())));
    assertTrue(Arrays.stream(dtos).anyMatch(a -> testOwner.getId().equals(a.getOwner())));
  }

  /**
   * Test method for {@link PermissionController#getAllData()}.
   * 
   * @throws URISyntaxException if the URL could not be created.
   */
  @Test
  public void testGetAllPermissionsNoAuthorization() throws URISyntaxException {
    final HttpHeaders headers = getHeaders();

    final String url = this.getUrl(CONTROLLER_PATH);
    final HttpEntity<String> requestEntity = new HttpEntity<>(headers);

    final ResponseEntity<Object> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.GET, requestEntity, Object.class);

    assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
  }

  /**
   * Test method for
   * {@link PermissionController#getAllDataPaginated(int, int, org.springframework.web.context.request.WebRequest, org.springframework.web.util.UriComponentsBuilder, javax.servlet.http.HttpServletResponse)}.
   * 
   * @throws URISyntaxException if the URL could not be created.
   */
  @Test
  public void testGetAllPermissionsPaginated() throws URISyntaxException {
    final HttpHeaders headers = getHeaders(this.accessToken);

    final String url = this.getUrl(new String[] {CONTROLLER_PATH}, "page=0");
    final HttpEntity<String> requestEntity = new HttpEntity<>(headers);

    final ResponseEntity<PermissionDto[]> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.GET, requestEntity, PermissionDto[].class);

    final PermissionDto[] dtos = responseEntity.getBody();

    assertNotNull(dtos);
    assertTrue(dtos.length > 0);
    // No default sort on pagination means we cannot be sure of the content returned in first page
  }

  /**
   * Test method for
   * {@link PermissionController#getAllDataPaginated(int, int, org.springframework.web.context.request.WebRequest, org.springframework.web.util.UriComponentsBuilder, javax.servlet.http.HttpServletResponse)}.
   * 
   * @throws URISyntaxException if the URL could not be created.
   */
  @Test
  public void testGetAllPermissionsPaginatedNoAuthorization() throws URISyntaxException {
    final HttpHeaders headers = getHeaders();

    final String url = this.getUrl(new String[] {CONTROLLER_PATH}, "page=0");
    final HttpEntity<String> requestEntity = new HttpEntity<>(headers);

    final ResponseEntity<Object> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.GET, requestEntity, Object.class);

    assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
  }

  /**
   * Test method for
   * {@link PermissionController#addData(com.monogramm.starter.persistence.permission.entity.Permission, org.springframework.web.util.UriComponentsBuilder)}.
   * 
   * @throws PermissionNotFoundException if the permission added is not found at the end of the
   *         test.
   * @throws URISyntaxException if the URL could not be created.
   */
  @Test
  public void testAddPermission() throws URISyntaxException {
    final HttpHeaders headers = getHeaders(this.accessToken);

    final String url = this.getUrl(CONTROLLER_PATH);

    final Permission model = Permission.builder("God").build();
    final PermissionDto dto = getPermissionService().toDto(model);

    final HttpEntity<PermissionDto> requestEntity = new HttpEntity<>(dto, headers);

    final ResponseEntity<PermissionDto> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.POST, requestEntity, PermissionDto.class);

    final PermissionDto content = responseEntity.getBody();
    assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    assertNotNull(content);
    assertNotNull(content.getId());

    // FIXME This test should be rollbacked
    getPermissionService().deleteById(content.getId());

    assertNotNull(content.getCreatedAt());
    assertNull(content.getCreatedBy());
    assertNull(content.getModifiedAt());
    assertNull(content.getModifiedBy());
    assertNull(content.getOwner());
    assertEquals(model.getName(), content.getName());

    assertNotNull(responseEntity.getHeaders().getLocation());
  }

  /**
   * Test method for
   * {@link PermissionController#addData(com.monogramm.starter.persistence.permission.entity.Permission, org.springframework.web.util.UriComponentsBuilder)}.
   * 
   * @throws PermissionNotFoundException if the permission added is not found at the end of the
   *         test.
   * @throws URISyntaxException if the URL could not be created.
   */
  @Test
  public void testAddPermissionNoAuthorization() throws URISyntaxException {
    final HttpHeaders headers = getHeaders();

    final String url = this.getUrl(CONTROLLER_PATH);

    final Permission model = Permission.builder("God").build();
    final PermissionDto dto = getPermissionService().toDto(model);

    final HttpEntity<PermissionDto> requestEntity = new HttpEntity<>(dto, headers);

    final ResponseEntity<PermissionDto> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.POST, requestEntity, PermissionDto.class);

    assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
  }

  /**
   * Test method for {@link PermissionController#updateData(String, Permission)}.
   * 
   * @throws URISyntaxException if the URL could not be created.
   */
  @Test
  public void testUpdatePermission() throws URISyntaxException {
    final HttpHeaders headers = getHeaders(this.accessToken);

    final String url = this.getUrl(CONTROLLER_PATH, "/", this.testEntity.getId());

    this.testEntity.setName("Bar");
    this.testDto = getPermissionService().toDto(this.testEntity);

    final HttpEntity<PermissionDto> requestEntity = new HttpEntity<>(this.testDto, headers);

    final ResponseEntity<PermissionDto> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.PUT, requestEntity, PermissionDto.class);

    final PermissionDto content = responseEntity.getBody();
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertNotNull(content);
    assertEquals(this.testDto, content);
    assertEquals(this.testDto.getName(), content.getName());
  }

  /**
   * Test method for {@link PermissionController#updateData(String, Permission)}.
   * 
   * @throws URISyntaxException if the URL could not be created.
   */
  @Test
  public void testUpdatePermissionNoAuthorization() throws URISyntaxException {
    final HttpHeaders headers = getHeaders();

    final String url = this.getUrl(CONTROLLER_PATH, "/", this.testEntity.getId());

    this.testEntity.setName("Bar");
    this.testDto = getPermissionService().toDto(this.testEntity);

    final HttpEntity<PermissionDto> requestEntity = new HttpEntity<>(this.testDto, headers);

    final ResponseEntity<PermissionDto> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.PUT, requestEntity, PermissionDto.class);

    assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
  }

  /**
   * Test method for {@link PermissionController#deleteData(java.lang.String)}.
   * 
   * @throws URISyntaxException if the URL could not be created.
   */
  @Test
  public void testDeletePermission() throws URISyntaxException {
    final HttpHeaders headers = getHeaders(this.accessToken);

    final String url = this.getUrl(CONTROLLER_PATH, "/", this.testEntity.getId());

    final HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

    final ResponseEntity<Void> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.DELETE, requestEntity, Void.class);

    assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
  }

  /**
   * Test method for {@link PermissionController#deleteData(java.lang.String)}.
   * 
   * @throws URISyntaxException if the URL could not be created.
   */
  @Test
  public void testDeletePermissionNoAuthorization() throws URISyntaxException {
    final HttpHeaders headers = getHeaders();

    final String url = this.getUrl(CONTROLLER_PATH, "/", this.testEntity.getId());

    final HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

    final ResponseEntity<Void> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.DELETE, requestEntity, Void.class);

    assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
  }

}
