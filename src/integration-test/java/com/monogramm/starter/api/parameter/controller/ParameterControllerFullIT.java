/*
 * Creation by madmath03 the 2018-01-09.
 */

package com.monogramm.starter.api.parameter.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.monogramm.Application;
import com.monogramm.starter.api.AbstractControllerFullIT;
import com.monogramm.starter.config.data.GenericOperation;
import com.monogramm.starter.config.data.InitialDataLoader;
import com.monogramm.starter.dto.parameter.ParameterDto;
import com.monogramm.starter.persistence.parameter.entity.Parameter;
import com.monogramm.starter.persistence.parameter.exception.ParameterNotFoundException;
import com.monogramm.starter.persistence.parameter.service.IParameterService;
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
 * {@link ParameterController} Integration Test.
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
public class ParameterControllerFullIT extends AbstractControllerFullIT {

  /**
   * Logger for {@link ParameterControllerFullIT}.
   */
  private static final Logger LOG = LogManager.getLogger(ParameterControllerFullIT.class);

  /**
   * The managed parameter of this tested controller.
   */
  public static final String TYPE = ParameterController.TYPE;
  /**
   * The request base path of this tested controller.
   */
  public static final String CONTROLLER_PATH = ParameterController.CONTROLLER_PATH;

  protected static final String DUMMY_NAME = "Foo";
  protected static final Object DUMMY_VALUE = 42;

  @Autowired
  private IParameterService parameterService;

  private User testCreatedBy;
  private User testOwner;
  private Parameter testEntity;
  private ParameterDto testDto;

  private String accessToken;

  @Before
  public void setUp() throws URISyntaxException {
    // Set up a valid user for authentication and such
    super.setUpValidUser(GenericOperation.allPermissionNames(TYPE));

    // Get an access token for later calls to API
    this.accessToken = this.getFullToken();

    testCreatedBy =
        this.createUser(DUMMY_NAME + "_Creator", DUMMY_NAME + ".creator@creation.org", null, null);

    testOwner =
        this.createUser(DUMMY_NAME + "_Owner", DUMMY_NAME + ".owner@creation.org", null, null);

    // Add a parameter
    testEntity = Parameter.builder(DUMMY_NAME, DUMMY_VALUE).createdBy(testCreatedBy)
        .owner(testOwner).build();
    assertTrue(parameterService.add(testEntity));
    testDto = parameterService.toDto(testEntity);
  }

  @After
  public void tearDown() throws URISyntaxException {
    // Tear valid user for authentication
    super.tearDownValidUser();

    // Revoke an access token
    this.revokeToken(this.accessToken);

    try {
      if (testEntity != null && testEntity.getId() != null) {
        parameterService.deleteById(testEntity.getId());
      }
    } catch (ParameterNotFoundException e) {
      LOG.trace("Parameter already deleted: " + testEntity, e);
    }
    testEntity = null;
    testDto = null;

    this.deleteUser(testCreatedBy);
    testCreatedBy = null;

    this.deleteUser(testOwner);
    testOwner = null;
  }

  /**
   * Test method for {@link ParameterController#getDataById(java.lang.String)}.
   * 
   * @throws URISyntaxException if the URL could not be created.
   */
  @Test
  public void testGetParameterById() throws URISyntaxException {
    final HttpHeaders headers = getHeaders(this.accessToken);

    final String url = this.getUrl(CONTROLLER_PATH, "/", testEntity.getId());
    final HttpEntity<String> requestEntity = new HttpEntity<>(headers);

    final ResponseEntity<ParameterDto> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.GET, requestEntity, ParameterDto.class);

    final ParameterDto dto = responseEntity.getBody();

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
   * Test method for {@link ParameterController#getDataById(java.lang.String)}.
   * 
   * @throws URISyntaxException if the URL could not be created.
   */
  @Test
  public void testGetParameterByIdNoAuthorization() throws URISyntaxException {
    final HttpHeaders headers = getHeaders();

    final String url = this.getUrl(CONTROLLER_PATH, "/", testEntity.getId());
    final HttpEntity<String> requestEntity = new HttpEntity<>(headers);

    final ResponseEntity<ParameterDto> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.GET, requestEntity, ParameterDto.class);

    assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
  }

  /**
   * Test method for {@link ParameterController#getAllData()}.
   * 
   * @throws URISyntaxException if the URL could not be created.
   */
  @Test
  public void testGetAllParameters() throws URISyntaxException {
    final HttpHeaders headers = getHeaders(this.accessToken);

    final String url = this.getUrl(CONTROLLER_PATH);
    final HttpEntity<String> requestEntity = new HttpEntity<>(headers);

    final ResponseEntity<ParameterDto[]> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.GET, requestEntity, ParameterDto[].class);

    final ParameterDto[] dtos = responseEntity.getBody();

    assertNotNull(dtos);
    assertTrue(Arrays.stream(dtos).anyMatch(a -> DUMMY_NAME.equals(a.getName())));
    assertTrue(Arrays.stream(dtos).anyMatch(a -> testCreatedBy.getId().equals(a.getCreatedBy())));
    assertTrue(Arrays.stream(dtos).anyMatch(a -> testOwner.getId().equals(a.getOwner())));
  }

  /**
   * Test method for {@link ParameterController#getAllData()}.
   * 
   * @throws URISyntaxException if the URL could not be created.
   */
  @Test
  public void testGetAllParametersNoAuthorization() throws URISyntaxException {
    final HttpHeaders headers = getHeaders();

    final String url = this.getUrl(CONTROLLER_PATH);
    final HttpEntity<String> requestEntity = new HttpEntity<>(headers);

    final ResponseEntity<Object> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.GET, requestEntity, Object.class);

    assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
  }

  /**
   * Test method for
   * {@link ParameterController#getAllDataPaginated(int, int, org.springframework.web.context.request.WebRequest, org.springframework.web.util.UriComponentsBuilder, javax.servlet.http.HttpServletResponse)}.
   * 
   * @throws URISyntaxException if the URL could not be created.
   */
  @Test
  public void testGetAllParametersPaginated() throws URISyntaxException {
    final HttpHeaders headers = getHeaders(this.accessToken);

    final String url = this.getUrl(new String[] {CONTROLLER_PATH}, "page=0");
    final HttpEntity<String> requestEntity = new HttpEntity<>(headers);

    final ResponseEntity<ParameterDto[]> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.GET, requestEntity, ParameterDto[].class);

    final ParameterDto[] dtos = responseEntity.getBody();

    assertNotNull(dtos);
    assertTrue(Arrays.stream(dtos).anyMatch(a -> DUMMY_NAME.equals(a.getName())));
    assertTrue(Arrays.stream(dtos).anyMatch(a -> testCreatedBy.getId().equals(a.getCreatedBy())));
    assertTrue(Arrays.stream(dtos).anyMatch(a -> testOwner.getId().equals(a.getOwner())));
  }

  /**
   * Test method for
   * {@link ParameterController#getAllDataPaginated(int, int, org.springframework.web.context.request.WebRequest, org.springframework.web.util.UriComponentsBuilder, javax.servlet.http.HttpServletResponse)}.
   * 
   * @throws URISyntaxException if the URL could not be created.
   */
  @Test
  public void testGetAllParametersPaginatedNoAuthorization() throws URISyntaxException {
    final HttpHeaders headers = getHeaders();

    final String url = this.getUrl(new String[] {CONTROLLER_PATH}, "page=0");
    final HttpEntity<String> requestEntity = new HttpEntity<>(headers);

    final ResponseEntity<Object> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.GET, requestEntity, Object.class);

    assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
  }

  /**
   * Test method for
   * {@link ParameterController#addData(ParameterDto, org.springframework.web.util.UriComponentsBuilder)}.
   * 
   * @throws ParameterNotFoundException if the parameter added is not found at the end of the test.
   * @throws URISyntaxException if the URL could not be created.
   */
  @Test
  public void testAddParameter() throws URISyntaxException {
    final HttpHeaders headers = getHeaders(this.accessToken);

    final String url = this.getUrl(CONTROLLER_PATH);

    final Parameter model = Parameter.builder("God", 42).build();
    final ParameterDto dto = parameterService.toDto(model);

    final HttpEntity<ParameterDto> requestEntity = new HttpEntity<>(dto, headers);

    final ResponseEntity<ParameterDto> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.POST, requestEntity, ParameterDto.class);

    final ParameterDto content = responseEntity.getBody();
    assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    assertNotNull(content);
    assertNotNull(content.getId());

    // FIXME This test should be rollbacked
    parameterService.deleteById(content.getId());

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
   * {@link ParameterController#addData(com.monogramm.starter.persistence.parameter.entity.Parameter, org.springframework.web.util.UriComponentsBuilder)}.
   * 
   * @throws ParameterNotFoundException if the parameter added is not found at the end of the test.
   * @throws URISyntaxException if the URL could not be created.
   */
  @Test
  public void testAddParameterNoAuthorization() throws URISyntaxException {
    final HttpHeaders headers = getHeaders();

    final String url = this.getUrl(CONTROLLER_PATH);

    final Parameter model = Parameter.builder("God", 42).build();
    final ParameterDto dto = parameterService.toDto(model);

    final HttpEntity<ParameterDto> requestEntity = new HttpEntity<>(dto, headers);

    final ResponseEntity<ParameterDto> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.POST, requestEntity, ParameterDto.class);

    assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
  }

  /**
   * Test method for {@link ParameterController#updateData(String, Parameter)}.
   * 
   * @throws URISyntaxException if the URL could not be created.
   */
  @Test
  public void testUpdateParameter() throws URISyntaxException {
    final HttpHeaders headers = getHeaders(this.accessToken);

    final String url = this.getUrl(CONTROLLER_PATH, "/", this.testEntity.getId());

    this.testEntity.setName("Bar");
    this.testDto = parameterService.toDto(this.testEntity);

    final HttpEntity<ParameterDto> requestEntity = new HttpEntity<>(this.testDto, headers);

    final ResponseEntity<ParameterDto> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.PUT, requestEntity, ParameterDto.class);

    final ParameterDto content = responseEntity.getBody();
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertNotNull(content);
    assertEquals(this.testDto, content);
    assertEquals(this.testDto.getName(), content.getName());
  }

  /**
   * Test method for {@link ParameterController#updateData(String, Parameter)}.
   * 
   * @throws URISyntaxException if the URL could not be created.
   */
  @Test
  public void testUpdateParameterNoAuthorization() throws URISyntaxException {
    final HttpHeaders headers = getHeaders();

    final String url = this.getUrl(CONTROLLER_PATH, "/", this.testEntity.getId());

    this.testEntity.setName("Bar");
    this.testDto = parameterService.toDto(this.testEntity);

    final HttpEntity<ParameterDto> requestEntity = new HttpEntity<>(this.testDto, headers);

    final ResponseEntity<ParameterDto> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.PUT, requestEntity, ParameterDto.class);

    assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
  }

  /**
   * Test method for {@link ParameterController#deleteData(java.lang.String)}.
   * 
   * @throws URISyntaxException if the URL could not be created.
   */
  @Test
  public void testDeleteParameter() throws URISyntaxException {
    final HttpHeaders headers = getHeaders(this.accessToken);

    final String url = this.getUrl(CONTROLLER_PATH, "/", this.testEntity.getId());

    final HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

    final ResponseEntity<Void> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.DELETE, requestEntity, Void.class);

    assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
  }

  /**
   * Test method for {@link ParameterController#deleteData(java.lang.String)}.
   * 
   * @throws URISyntaxException if the URL could not be created.
   */
  @Test
  public void testDeleteParameterNoAuthorization() throws URISyntaxException {
    final HttpHeaders headers = getHeaders();

    final String url = this.getUrl(CONTROLLER_PATH, "/", this.testEntity.getId());

    final HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

    final ResponseEntity<Void> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.DELETE, requestEntity, Void.class);

    assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
  }

}
