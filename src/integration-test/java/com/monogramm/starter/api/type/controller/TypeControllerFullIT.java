package com.monogramm.starter.api.type.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.monogramm.Application;
import com.monogramm.starter.api.AbstractControllerFullIT;
import com.monogramm.starter.config.data.GenericOperation;
import com.monogramm.starter.config.data.InitialDataLoader;
import com.monogramm.starter.dto.type.TypeDto;
import com.monogramm.starter.persistence.type.entity.Type;
import com.monogramm.starter.persistence.type.exception.TypeNotFoundException;
import com.monogramm.starter.persistence.type.service.ITypeService;
import com.monogramm.starter.persistence.user.entity.User;

import java.net.URISyntaxException;
import java.util.Arrays;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * {@link TypeController} Integration Test.
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
public class TypeControllerFullIT extends AbstractControllerFullIT {

  /**
   * Logger for {@link TypeControllerFullIT}.
   */
  private static final Logger LOG = LogManager.getLogger(TypeControllerFullIT.class);

  /**
   * The managed type of this tested controller.
   */
  public static final String TYPE = "Types";
  /**
   * The request base path of this tested controller.
   */
  public static final String CONTROLLER_PATH = '/' + TYPE;

  private static final String DISPLAYNAME = "Foo";

  @Autowired
  private ITypeService typeService;

  private User testCreatedBy;
  private User testOwner;
  private Type testEntity;
  private TypeDto testDto;

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

    // Add a type
    testEntity = Type.builder(DISPLAYNAME).createdBy(testCreatedBy).owner(testOwner).build();
    assertTrue(typeService.add(testEntity));
    testDto = typeService.toDto(testEntity);
  }

  @After
  public void tearDown() throws URISyntaxException {
    // Tear valid user for authentication
    super.tearDownValidUser();

    // Revoke an access token
    this.revokeToken(this.accessToken);

    try {
      if (testEntity != null && testEntity.getId() != null) {
        typeService.deleteById(testEntity.getId());
      }
    } catch (TypeNotFoundException e) {
      LOG.trace("Type already deleted: " + testEntity, e);
    }
    testEntity = null;
    testDto = null;

    this.deleteUser(testCreatedBy);
    testCreatedBy = null;

    this.deleteUser(testOwner);
    testOwner = null;
  }

  /**
   * Test method for {@link TypeController#getDataById(java.lang.String)}.
   * 
   * @throws URISyntaxException if the URL could not be created.
   */
  @Test
  public void testGetTypeById() throws URISyntaxException {
    final HttpHeaders headers = getHeaders(this.accessToken);

    final String url = this.getUrl(CONTROLLER_PATH, "/", testEntity.getId());
    final HttpEntity<String> requestEntity = new HttpEntity<>(headers);

    final ResponseEntity<TypeDto> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.GET, requestEntity, TypeDto.class);

    final TypeDto dto = responseEntity.getBody();

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
   * Test method for {@link TypeController#getDataById(java.lang.String)}.
   * 
   * @throws URISyntaxException if the URL could not be created.
   */
  @Test
  public void testGetTypeByIdNoAuthorization() throws URISyntaxException {
    final HttpHeaders headers = getHeaders();

    final String url = this.getUrl(CONTROLLER_PATH, "/", testEntity.getId());
    final HttpEntity<String> requestEntity = new HttpEntity<>(headers);

    final ResponseEntity<TypeDto> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.GET, requestEntity, TypeDto.class);

    assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
  }

  /**
   * Test method for {@link TypeController#getAllData()}.
   * 
   * @throws URISyntaxException if the URL could not be created.
   */
  @Test
  public void testGetAllTypes() throws URISyntaxException {
    final HttpHeaders headers = getHeaders(this.accessToken);

    final String url = this.getUrl(CONTROLLER_PATH);
    final HttpEntity<String> requestEntity = new HttpEntity<>(headers);

    final ResponseEntity<TypeDto[]> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.GET, requestEntity, TypeDto[].class);

    final TypeDto[] dtos = responseEntity.getBody();

    assertNotNull(dtos);
    assertTrue(Arrays.stream(dtos).anyMatch(a -> DISPLAYNAME.equals(a.getName())));
    assertTrue(Arrays.stream(dtos).anyMatch(a -> testCreatedBy.getId().equals(a.getCreatedBy())));
    assertTrue(Arrays.stream(dtos).anyMatch(a -> testOwner.getId().equals(a.getOwner())));
  }

  /**
   * Test method for {@link TypeController#getAllData()}.
   * 
   * @throws URISyntaxException if the URL could not be created.
   */
  @Test
  public void testGetAllTypesNoAuthorization() throws URISyntaxException {
    final HttpHeaders headers = getHeaders();

    final String url = this.getUrl(CONTROLLER_PATH);
    final HttpEntity<String> requestEntity = new HttpEntity<>(headers);

    final ResponseEntity<Object> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.GET, requestEntity, Object.class);

    assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
  }

  /**
   * Test method for
   * {@link TypeController#addData(TypeDto, org.springframework.web.util.UriComponentsBuilder)}.
   * 
   * @throws TypeNotFoundException if the type added is not found at the end of the test.
   * @throws URISyntaxException if the URL could not be created.
   */
  @Test
  public void testAddType() throws URISyntaxException {
    final HttpHeaders headers = getHeaders(this.accessToken);

    final String url = this.getUrl(CONTROLLER_PATH);

    final Type model = Type.builder("God").build();
    final TypeDto dto = typeService.toDto(model);

    final HttpEntity<TypeDto> requestEntity = new HttpEntity<>(dto, headers);

    final ResponseEntity<TypeDto> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.POST, requestEntity, TypeDto.class);

    final TypeDto content = responseEntity.getBody();
    assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    assertNotNull(content);
    assertNotNull(content.getId());

    // FIXME This test should be rollbacked
    typeService.deleteById(content.getId());

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
   * {@link TypeController#addData(com.monogramm.starter.persistence.type.entity.Type, org.springframework.web.util.UriComponentsBuilder)}.
   * 
   * @throws TypeNotFoundException if the type added is not found at the end of the test.
   * @throws URISyntaxException if the URL could not be created.
   */
  @Test
  public void testAddTypeNoAuthorization() throws URISyntaxException {
    final HttpHeaders headers = getHeaders();

    final String url = this.getUrl(CONTROLLER_PATH);

    final Type model = Type.builder("God").build();
    final TypeDto dto = typeService.toDto(model);

    final HttpEntity<TypeDto> requestEntity = new HttpEntity<>(dto, headers);

    final ResponseEntity<TypeDto> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.POST, requestEntity, TypeDto.class);

    assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
  }

  /**
   * Test method for {@link TypeController#updateData(String, Type)}.
   * 
   * @throws URISyntaxException if the URL could not be created.
   */
  @Test
  public void testUpdateType() throws URISyntaxException {
    final HttpHeaders headers = getHeaders(this.accessToken);

    final String url = this.getUrl(CONTROLLER_PATH, "/", this.testEntity.getId());

    this.testEntity.setName("Bar");
    this.testDto = typeService.toDto(this.testEntity);

    final HttpEntity<TypeDto> requestEntity = new HttpEntity<>(this.testDto, headers);

    final ResponseEntity<TypeDto> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.PUT, requestEntity, TypeDto.class);

    final TypeDto content = responseEntity.getBody();
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertNotNull(content);
    assertEquals(this.testDto, content);
    assertEquals(this.testDto.getName(), content.getName());
  }

  /**
   * Test method for {@link TypeController#updateData(String, Type)}.
   * 
   * @throws URISyntaxException if the URL could not be created.
   */
  @Test
  public void testUpdateTypeNoAuthorization() throws URISyntaxException {
    final HttpHeaders headers = getHeaders();

    final String url = this.getUrl(CONTROLLER_PATH, "/", this.testEntity.getId());

    this.testEntity.setName("Bar");
    this.testDto = typeService.toDto(this.testEntity);

    final HttpEntity<TypeDto> requestEntity = new HttpEntity<>(this.testDto, headers);

    final ResponseEntity<TypeDto> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.PUT, requestEntity, TypeDto.class);

    assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
  }

  /**
   * Test method for {@link TypeController#deleteData(java.lang.String)}.
   * 
   * @throws URISyntaxException if the URL could not be created.
   */
  @Test
  public void testDeleteType() throws URISyntaxException {
    final HttpHeaders headers = getHeaders(this.accessToken);

    final String url = this.getUrl(CONTROLLER_PATH, "/", this.testEntity.getId());

    final HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

    final ResponseEntity<Void> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.DELETE, requestEntity, Void.class);

    assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
  }

  /**
   * Test method for {@link TypeController#deleteData(java.lang.String)}.
   * 
   * @throws URISyntaxException if the URL could not be created.
   */
  @Test
  public void testDeleteTypeNoAuthorization() throws URISyntaxException {
    final HttpHeaders headers = getHeaders();

    final String url = this.getUrl(CONTROLLER_PATH, "/", this.testEntity.getId());

    final HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

    final ResponseEntity<Void> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.DELETE, requestEntity, Void.class);

    assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
  }

}
