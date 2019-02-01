/*
 * Creation by madmath03 the 2017-12-04.
 */

package com.monogramm.starter.api;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.monogramm.starter.api.discoverability.exception.PageNotFoundException;
import com.monogramm.starter.dto.AbstractGenericDto;
import com.monogramm.starter.persistence.AbstractGenericBridge;
import com.monogramm.starter.persistence.AbstractGenericEntity;
import com.monogramm.starter.persistence.EntityNotFoundException;
import com.monogramm.starter.persistence.GenericService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * {@link AbstractGenericController} Unit Test.
 * 
 * @param <T> the entity type used to manage data in persistence storage.
 * @param <D> the DTO type to convert entities to/from.
 * 
 * @author madmath03
 */
public abstract class AbstractGenericControllerTest<T extends AbstractGenericEntity,
    D extends AbstractGenericDto> {

  private static final UUID RANDOM_ID = UUID.randomUUID();

  private MessageSource messageSource;
  private ApplicationEventPublisher eventPublisher;
  private GenericService<T, D> mockService;
  private AbstractGenericController<T, D> controller;
  private AbstractGenericBridge<T, D> bridge;
  private Authentication mockAuthentication;

  private WebRequest mockRequest;
  private UriComponentsBuilder uriBuilder;
  private HttpServletResponse mockResponse;

  /**
   * @throws java.lang.Exception
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {}

  /**
   * @throws java.lang.Exception
   */
  @AfterClass
  public static void tearDownAfterClass() throws Exception {}

  /**
   * @throws java.lang.Exception if the test setup crashes.
   */
  @Before
  public void setUp() throws Exception {
    this.messageSource = this.buildMessageSource();
    assertNotNull(this.messageSource);

    this.eventPublisher = this.buildApplicationEventPublisher();
    assertNotNull(this.eventPublisher);

    this.mockService = this.buildTestService();
    assertNotNull(this.mockService);

    this.bridge = this.buildTestBridge();
    assertNotNull(this.bridge);

    this.mockAuthentication = this.buildMockAuthentication();
    assertNotNull(this.mockAuthentication);

    this.controller = this.buildTestController();
    assertNotNull(this.controller);

    this.mockRequest = this.buildWebRequest();
    assertNotNull(this.mockRequest);

    this.uriBuilder = new UriComponentsBuilder() {};

    this.mockResponse = this.buildHttpServletResponse();
    assertNotNull(this.mockResponse);
  }

  /**
   * @throws java.lang.Exception if the test cleanup crashes.
   */
  @After
  public void tearDown() throws Exception {
    Mockito.reset(messageSource);
    Mockito.reset(eventPublisher);
    this.controller = null;
    Mockito.reset(mockRequest);
    this.uriBuilder = null;
    Mockito.reset(mockResponse);
  }

  protected MessageSource buildMessageSource() {
    return mock(MessageSource.class);
  }

  protected ApplicationEventPublisher buildApplicationEventPublisher() {
    return mock(ApplicationEventPublisher.class);
  }

  protected WebRequest buildWebRequest() {
    return mock(WebRequest.class);
  }

  protected HttpServletResponse buildHttpServletResponse() {
    return mock(HttpServletResponse.class);
  }

  protected abstract GenericService<T, D> buildTestService();

  protected abstract AbstractGenericController<T, D> buildTestController();

  protected abstract AbstractGenericBridge<T, D> buildTestBridge();

  protected abstract Authentication buildMockAuthentication();

  protected abstract T buildTestEntity();

  protected abstract Class<T> getEntityClass();

  protected abstract <E extends EntityNotFoundException> E buildTestEntityNotFound();



  /**
   * Get the {@link #controller}.
   * 
   * @return the {@link #controller}.
   */
  protected AbstractGenericController<T, D> getController() {
    return controller;
  }

  /**
   * Get the {@link #messageSource}.
   * 
   * @return the {@link #messageSource}.
   */
  protected final MessageSource getMessageSource() {
    return messageSource;
  }

  /**
   * Set the {@link #messageSource}.
   * 
   * @param messageSource the {@link #messageSource} to set.
   */
  protected final void setMessageSource(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  /**
   * Get the {@link #eventPublisher}.
   * 
   * @return the {@link #eventPublisher}.
   */
  protected final ApplicationEventPublisher getEventPublisher() {
    return eventPublisher;
  }

  /**
   * Get the {@link #mockService}.
   * 
   * @return the {@link #mockService}.
   */
  protected GenericService<T, D> getMockService() {
    return mockService;
  }

  /**
   * Set the {@link #mockService}.
   * 
   * @param mockService the {@link #mockService} to set.
   */
  protected final void setService(GenericService<T, D> service) {
    this.mockService = service;
  }

  /**
   * Get the {@link #bridge}.
   * 
   * @return the {@link #bridge}.
   */
  protected AbstractGenericBridge<T, D> getBridge() {
    return bridge;
  }

  /**
   * Set the {@link #bridge}.
   * 
   * @param bridge the {@link #bridge} to set.
   */
  protected void setBridge(AbstractGenericBridge<T, D> bridge) {
    this.bridge = bridge;
  }

  /**
   * Get the {@link #mockAuthentication}.
   * 
   * @return the {@link #mockAuthentication}.
   */
  protected final Authentication getMockAuthentication() {
    return mockAuthentication;
  }

  /**
   * Get the {@link #mockRequest}.
   * 
   * @return the {@link #mockRequest}.
   */
  protected WebRequest getMockRequest() {
    return mockRequest;
  }

  /**
   * Get the {@link #mockResponse}.
   * 
   * @return the {@link #mockResponse}.
   */
  protected HttpServletResponse getMockResponse() {
    return mockResponse;
  }



  /**
   * Test method for {@link AbstractGenericController#getService()}.
   */
  @Test
  public void testGetService() {
    assertNotNull(this.controller.getService());
  }

  /**
   * Test method for {@link AbstractGenericController#getAdminAuthorities()}.
   */
  @Test
  public void testGetAdminAuthorities() {
    assertNotNull(this.controller.getAdminAuthorities());
  }

  /**
   * Test method for {@link AbstractGenericController#getControllerPath()}.
   */
  @Test
  public void testGetControllerPath() {
    assertNotNull(this.controller.getControllerPath());
  }



  /**
   * Test method for
   * {@link AbstractGenericController#buildEntityNotFoundException(String, WebRequest)}.
   */
  @Test
  public void testBuildEntityNotFoundException() {
    assertNotNull(this.controller.buildEntityNotFoundException("dummy_uuid", mockRequest));
  }

  /**
   * Test method for
   * {@link AbstractGenericController#buildPageNotFoundException(int, int, WebRequest)}.
   */
  @Test
  public void testBuildPageNotFoundException() {
    assertNotNull(this.controller.buildPageNotFoundException(0, 42, mockRequest));
  }

  /**
   * Test method for
   * {@link AbstractGenericController#buildPaginatedResultsRetrievedEvent(UriComponentsBuilder, HttpServletResponse, int, int, int)}.
   */
  @Test
  public void testBuildPaginatedResultsRetrievedEvent() {
    assertNotNull(
        this.controller.buildPaginatedResultsRetrievedEvent(uriBuilder, mockResponse, 0, 42, 1337));
  }



  /**
   * Test method for
   * {@link AbstractGenericController#getDataById(String, WebRequest, HttpServletResponse)}.
   */
  @Test
  public void testGetDataByIdString() {
    final T model = this.buildTestEntity();
    final D dto = bridge.toDto(model);
    final D expectedResponse = dto;

    when(mockService.findById(model.getId())).thenReturn(model);
    when(mockService.toDto(model)).thenReturn(dto);

    final D actual = controller.getDataById(model.getId().toString(), mockRequest, mockResponse);

    verify(mockService, times(1)).findById(model.getId());
    verify(mockService, times(1)).toDto(model);
    verifyNoMoreInteractions(mockService);
    verify(eventPublisher, times(1)).publishEvent(any());
    verifyNoMoreInteractions(eventPublisher);

    assertThat(actual, is(expectedResponse));
  }

  /**
   * Test method for
   * {@link AbstractGenericController#getDataById(String, WebRequest, HttpServletResponse)}.
   */
  @Test(expected = EntityNotFoundException.class)
  public void testGetDataByIdStringNotFound() {
    final T model = null;

    when(mockService.findById(RANDOM_ID)).thenReturn(model);

    controller.getDataById(RANDOM_ID.toString(), mockRequest, mockResponse);
  }

  /**
   * Test method for
   * {@link AbstractGenericController#getDataById(String, WebRequest, HttpServletResponse)}.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testGetDataByIdStringIllegal() {
    controller.getDataById("this_is_not_a_UUID", mockRequest, mockResponse);
  }



  /**
   * Test method for {@link AbstractGenericController#getAllData(String)}.
   */
  @Test
  public void testGetAllData() {
    final T model = this.buildTestEntity();
    final D dto = bridge.toDto(model);

    final List<T> models = new ArrayList<>();
    models.add(model);
    final List<D> results = new ArrayList<>();
    results.add(dto);

    final List<D> expectedResponse = results;

    when(mockService.findAll(any(Sort.class))).thenReturn(models);
    when(mockService.toDto(models)).thenReturn(results);

    final String sortQuery = "createdAt,DESC;modifiedAt,DESC";
    final List<D> actual = controller.getAllData(sortQuery);

    verify(mockService, times(1)).findAll(any(Sort.class));
    verify(mockService, times(1)).toDto(models);
    verifyNoMoreInteractions(mockService);
    verifyNoMoreInteractions(eventPublisher);

    assertThat(actual, is(expectedResponse));
  }

  /**
   * Test method for {@link AbstractGenericController#getAllData(String)}.
   */
  @Test
  public void testGetAllDataSortNull() {
    final T model = this.buildTestEntity();
    final D dto = bridge.toDto(model);

    final List<T> models = new ArrayList<>();
    models.add(model);
    final List<D> results = new ArrayList<>();
    results.add(dto);

    final List<D> expectedResponse = results;

    when(mockService.findAll()).thenReturn(models);
    when(mockService.toDto(models)).thenReturn(results);

    final List<D> actual = controller.getAllData(null);

    verify(mockService, times(1)).findAll();
    verify(mockService, times(1)).toDto(models);
    verifyNoMoreInteractions(mockService);
    verifyNoMoreInteractions(eventPublisher);

    assertThat(actual, is(expectedResponse));
  }

  /**
   * Test method for {@link AbstractGenericController#getAllData(String)}.
   */
  @Test
  public void testGetAllDataEmpty() {
    final List<T> models = new ArrayList<>();
    final List<D> results = new ArrayList<>();
    final List<D> expectedResponse = results;

    when(mockService.findAll()).thenReturn(models);
    when(mockService.toDto(models)).thenReturn(results);

    final List<D> actual = controller.getAllData(null);

    verify(mockService, times(1)).findAll();
    verify(mockService, times(1)).toDto(models);
    verifyNoMoreInteractions(mockService);
    verifyNoMoreInteractions(eventPublisher);

    assertThat(actual, is(expectedResponse));
  }

  /**
   * Test method for {@link AbstractGenericController#getAllData(String)}.
   */
  @Test
  public void testGetAllDataNull() {
    final List<T> models = null;
    final List<D> results = new ArrayList<>();
    final List<D> expectedResponse = results;

    when(mockService.findAll()).thenReturn(models);
    when(mockService.toDto(models)).thenReturn(results);

    final List<D> actual = controller.getAllData(null);

    verify(mockService, times(1)).findAll();
    verify(mockService, times(1)).toDto(models);
    verifyNoMoreInteractions(mockService);
    verifyNoMoreInteractions(eventPublisher);

    assertThat(actual, is(expectedResponse));
  }



  /**
   * Test method for
   * {@link AbstractGenericController#getAllDataPaginated(String, int, int, WebRequest, UriComponentsBuilder, HttpServletResponse)}.
   */
  @Test
  public void testGetAllPaginatedData() {
    final T model = this.buildTestEntity();
    final D dto = bridge.toDto(model);
    int page = 0;
    int size = 2;

    final Page<T> models = new PageImpl<T>(Collections.singletonList(model));
    final List<D> results = new ArrayList<>();
    results.add(dto);

    final List<D> expectedResponse = results;

    when(mockService.findAll(eq(page), eq(size), any(Sort.class))).thenReturn(models);
    when(mockService.toDto(models.getContent())).thenReturn(results);

    final String sortQuery = "createdAt,DESC;modifiedAt,DESC";
    final List<D> actual = controller.getAllDataPaginated(sortQuery, page, size, mockRequest,
        uriBuilder, mockResponse);

    verify(mockService, times(1)).findAll(eq(page), eq(size), any(Sort.class));
    verify(mockService, times(1)).toDto(models.getContent());
    verifyNoMoreInteractions(mockService);
    verify(eventPublisher, times(1)).publishEvent(any());
    verifyNoMoreInteractions(eventPublisher);

    assertThat(actual, is(expectedResponse));
  }

  /**
   * Test method for
   * {@link AbstractGenericController#getAllDataPaginated(String, int, int, WebRequest, UriComponentsBuilder, HttpServletResponse)}.
   */
  @Test
  public void testGetAllPaginatedDataSortNull() {
    final T model = this.buildTestEntity();
    final D dto = bridge.toDto(model);
    int page = 0;
    int size = 2;

    final Page<T> models = new PageImpl<T>(Collections.singletonList(model));
    final List<D> results = new ArrayList<>();
    results.add(dto);

    final List<D> expectedResponse = results;

    when(mockService.findAll(page, size)).thenReturn(models);
    when(mockService.toDto(models.getContent())).thenReturn(results);

    final List<D> actual =
        controller.getAllDataPaginated(null, page, size, mockRequest, uriBuilder, mockResponse);

    verify(mockService, times(1)).findAll(page, size);
    verify(mockService, times(1)).toDto(models.getContent());
    verifyNoMoreInteractions(mockService);
    verify(eventPublisher, times(1)).publishEvent(any());
    verifyNoMoreInteractions(eventPublisher);

    assertThat(actual, is(expectedResponse));
  }

  /**
   * Test method for
   * {@link AbstractGenericController#getAllDataPaginated(String, int, int, WebRequest, UriComponentsBuilder, HttpServletResponse)}.
   */
  @Test(expected = PageNotFoundException.class)
  public void testGetAllDataPaginatedEmpty() {
    final Page<T> models = new PageImpl<T>(Collections.emptyList());
    int page = 42;
    int size = 2;

    when(mockService.findAll(page, size)).thenReturn(models);

    controller.getAllDataPaginated(null, page, size, mockRequest, uriBuilder, mockResponse);
  }

  /**
   * Test method for
   * {@link AbstractGenericController#getAllDataPaginated(String, int, int, WebRequest, UriComponentsBuilder, HttpServletResponse)}.
   */
  @Test(expected = PageNotFoundException.class)
  public void testGetAllDataPaginatedNull() {
    final Page<T> models = null;
    int page = 42;
    int size = 2;

    when(mockService.findAll(page, size)).thenReturn(models);

    controller.getAllDataPaginated(null, page, size, mockRequest, uriBuilder, mockResponse);
  }



  /**
   * Test method for
   * {@link AbstractGenericController#addData(Authentication, AbstractGenericDto, UriComponentsBuilder, HttpServletResponse)}.
   */
  @Test
  public void testAddData() {
    final T model = this.buildTestEntity();
    final D dto = bridge.toDto(model);
    final ResponseEntity<D> expectedResponse = new ResponseEntity<>(dto, HttpStatus.CREATED);

    when(mockService.toEntity(dto)).thenReturn(model);
    when(mockService.add(model)).thenReturn(true);
    when(mockService.toDto(model)).thenReturn(dto);

    final ResponseEntity<D> actualResponse =
        controller.addData(getMockAuthentication(), dto, uriBuilder, mockResponse);

    assertThat(actualResponse.getStatusCode(), is(expectedResponse.getStatusCode()));
    assertThat(actualResponse.getBody(), is(expectedResponse.getBody()));

    verify(mockService, times(1)).toEntity(dto);
    ArgumentCaptor<T> typeArgument = ArgumentCaptor.forClass(this.getEntityClass());
    verify(mockService, times(1)).add(typeArgument.capture());
    verify(mockService, times(1)).toDto(model);
    verifyNoMoreInteractions(mockService);

    final T actual = typeArgument.getValue();

    assertNotNull(actual.getId());
    assertThat(actual, is(model));
  }

  /**
   * Test method for
   * {@link AbstractGenericController#addData(Authentication, AbstractGenericDto, UriComponentsBuilder, HttpServletResponse)}.
   */
  @Test
  public void testAddDataAlreadyExists() {
    final T model = this.buildTestEntity();
    final D dto = bridge.toDto(model);
    final ResponseEntity<D> expectedResponse = new ResponseEntity<>(HttpStatus.CONFLICT);

    when(mockService.toEntity(dto)).thenReturn(model);
    when(mockService.add(model)).thenReturn(false);

    final ResponseEntity<D> actualResponse =
        controller.addData(getMockAuthentication(), dto, uriBuilder, mockResponse);

    assertThat(actualResponse, is(expectedResponse));

    verify(mockService, times(1)).toEntity(dto);
    verify(mockService, times(1)).add(model);
    verifyNoMoreInteractions(mockService);
  }

  /**
   * Test method for {@link AbstractGenericController#updateData(String, AbstractGenericDto)}.
   * 
   * @throws EntityNotFoundException if the type entity to update is not found.
   */
  @Test
  public void testUpdateData() {
    final T model = this.buildTestEntity();
    final D dto = bridge.toDto(model);
    final ResponseEntity<D> expectedResponse = new ResponseEntity<>(dto, HttpStatus.OK);

    final String[] adminAuthorities = controller.getAdminAuthorities();
    assertNotNull(adminAuthorities);
    final Collection<GrantedAuthority> userAuthorities = new ArrayList<>(adminAuthorities.length);
    for (final String adminAuth : controller.getAdminAuthorities()) {
      userAuthorities.add(new SimpleGrantedAuthority(adminAuth));
    }

    when(mockAuthentication.getAuthorities()).then(invocation -> userAuthorities);
    when(mockAuthentication.getDetails()).thenReturn(null);
    when(mockService.toEntity(dto)).thenReturn(model);
    when(mockService.update(model)).thenReturn(model);
    when(mockService.toDto(model)).thenReturn(dto);

    final ResponseEntity<D> actual =
        controller.updateData(mockAuthentication, model.getId().toString(), dto);

    verify(mockAuthentication, times(1)).getAuthorities();
    verify(mockAuthentication, times(1)).getDetails();
    verifyNoMoreInteractions(mockAuthentication);
    verify(mockService, times(1)).toEntity(dto);
    verify(mockService, times(1)).update(model);
    verify(mockService, times(1)).toDto(model);
    verifyNoMoreInteractions(mockService);

    assertThat(actual, is(expectedResponse));
    assertNotNull(actual.getBody());
  }

  /**
   * Test method for {@link AbstractGenericController#updateData(String, AbstractGenericDto)}.
   * 
   * @throws EntityNotFoundException if the type entity to update is not found.
   */
  @Test
  public void testUpdateDataNotAdmin() {
    final T model = this.buildTestEntity();
    final D dto = bridge.toDto(model);
    final ResponseEntity<D> expectedResponse = new ResponseEntity<>(dto, HttpStatus.OK);
    final UUID principalId = null;

    when(mockAuthentication.getAuthorities()).thenReturn(Collections.emptyList());
    when(mockAuthentication.getDetails()).thenReturn(null);
    when(mockService.toEntity(dto)).thenReturn(model);
    when(mockService.updateByOwner(model, principalId)).thenReturn(model);
    when(mockService.toDto(model)).thenReturn(dto);

    final ResponseEntity<D> actual =
        controller.updateData(mockAuthentication, model.getId().toString(), dto);

    verify(mockAuthentication, times(1)).getAuthorities();
    verify(mockAuthentication, times(1)).getDetails();
    verifyNoMoreInteractions(mockAuthentication);
    verify(mockService, times(1)).toEntity(dto);
    verify(mockService, times(1)).updateByOwner(model, principalId);
    verify(mockService, times(1)).toDto(model);
    verifyNoMoreInteractions(mockService);

    assertThat(actual, is(expectedResponse));
    assertNotNull(actual.getBody());
  }

  /**
   * Test method for {@link AbstractGenericController#updateData(String, AbstractGenericDto)}.
   * 
   * @throws EntityNotFoundException if the type entity to update is not found.
   */
  @Test
  public void testUpdateDataNotAdminNotOwner() {
    final T model = this.buildTestEntity();
    final D dto = bridge.toDto(model);
    final ResponseEntity<D> expectedResponse = new ResponseEntity<>(HttpStatus.NOT_FOUND);
    final UUID principalId = null;

    when(mockAuthentication.getAuthorities()).thenReturn(Collections.emptyList());
    when(mockAuthentication.getDetails()).thenReturn(null);
    when(mockService.toEntity(dto)).thenReturn(model);
    when(mockService.updateByOwner(model, principalId)).thenReturn(null);

    final ResponseEntity<D> actual =
        controller.updateData(mockAuthentication, model.getId().toString(), dto);

    verify(mockAuthentication, times(1)).getAuthorities();
    verify(mockAuthentication, times(1)).getDetails();
    verifyNoMoreInteractions(mockAuthentication);
    verify(mockService, times(1)).toEntity(dto);
    verify(mockService, times(1)).updateByOwner(model, principalId);
    verifyNoMoreInteractions(mockService);

    assertThat(actual, is(expectedResponse));
  }

  /**
   * Test method for {@link AbstractGenericController#updateData(String, AbstractGenericDto)}.
   * 
   * @throws EntityNotFoundException if the type entity to update is not found.
   */
  @Test
  public void testUpdateDataNotFound() {
    final T model = this.buildTestEntity();
    final D dto = bridge.toDto(model);
    final ResponseEntity<D> expectedResponse = new ResponseEntity<>(HttpStatus.NOT_FOUND);

    final String[] adminAuthorities = controller.getAdminAuthorities();
    assertNotNull(adminAuthorities);
    final Collection<GrantedAuthority> userAuthorities = new ArrayList<>(adminAuthorities.length);
    for (final String adminAuth : controller.getAdminAuthorities()) {
      userAuthorities.add(new SimpleGrantedAuthority(adminAuth));
    }

    when(mockAuthentication.getAuthorities()).then(invocation -> userAuthorities);
    when(mockAuthentication.getDetails()).thenReturn(null);
    when(mockService.toEntity(dto)).thenReturn(model);
    when(mockService.update(model)).thenReturn(null);

    final ResponseEntity<D> actual =
        controller.updateData(mockAuthentication, model.getId().toString(), dto);

    verify(mockAuthentication, times(1)).getAuthorities();
    verify(mockAuthentication, times(1)).getDetails();
    verifyNoMoreInteractions(mockAuthentication);
    verify(mockService, times(1)).toEntity(dto);
    verify(mockService, times(1)).update(model);
    verifyNoMoreInteractions(mockService);

    assertThat(actual, is(expectedResponse));
  }

  /**
   * Test method for {@link AbstractGenericController#updateData(String, AbstractGenericDto)}.
   * 
   * @throws EntityNotFoundException if the type entity to update is not found.
   */
  @Test
  public void testUpdateDataNotFoundException() {
    final T model = this.buildTestEntity();
    final D dto = bridge.toDto(model);
    final ResponseEntity<D> expectedResponse = new ResponseEntity<>(HttpStatus.NOT_FOUND);

    final String[] adminAuthorities = controller.getAdminAuthorities();
    assertNotNull(adminAuthorities);
    final Collection<GrantedAuthority> userAuthorities = new ArrayList<>(adminAuthorities.length);
    for (final String adminAuth : controller.getAdminAuthorities()) {
      userAuthorities.add(new SimpleGrantedAuthority(adminAuth));
    }

    when(mockAuthentication.getAuthorities()).then(invocation -> userAuthorities);
    when(mockAuthentication.getDetails()).thenReturn(null);
    when(mockService.toEntity(dto)).thenReturn(model);
    when(mockService.update(model)).thenThrow(this.buildTestEntityNotFound());

    final ResponseEntity<D> actual =
        controller.updateData(mockAuthentication, model.getId().toString(), dto);

    verify(mockAuthentication, times(1)).getAuthorities();
    verify(mockAuthentication, times(1)).getDetails();
    verifyNoMoreInteractions(mockAuthentication);
    verify(mockService, times(1)).toEntity(dto);
    verify(mockService, times(1)).update(model);
    verifyNoMoreInteractions(mockService);

    assertThat(actual, is(expectedResponse));
  }

  /**
   * Test method for {@link AbstractGenericController#updateData(String, AbstractGenericDto)}.
   * 
   * @throws EntityNotFoundException if the type entity to update is not found.
   */
  @Test
  public void testUpdateDataIdentifierNotMatching() {
    final T model = this.buildTestEntity();
    final D dto = bridge.toDto(model);
    final ResponseEntity<D> expectedResponse = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    final ResponseEntity<D> actual =
        controller.updateData(mockAuthentication, RANDOM_ID.toString(), dto);

    verifyNoMoreInteractions(mockService);

    assertThat(actual, is(expectedResponse));
    assertNull(actual.getBody());
  }

  /**
   * Test method for {@link AbstractGenericController#updateData(String, AbstractGenericDto)}.
   * 
   * @throws EntityNotFoundException if the type entity to update is not found.
   */
  @Test
  public void testUpdateDataIdentifierNull() {
    final T model = this.buildTestEntity();
    final D dto = bridge.toDto(model);
    final ResponseEntity<D> expectedResponse = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    final ResponseEntity<D> actual = controller.updateData(mockAuthentication, null, dto);

    verifyNoMoreInteractions(mockService);

    assertThat(actual, is(expectedResponse));
    assertNull(actual.getBody());
  }

  /**
   * Test method for {@link AbstractGenericController#updateData(String, AbstractGenericDto)}.
   * 
   * @throws EntityNotFoundException if the type entity to update is not found.
   */
  @Test
  public void testUpdateDataIdentifierWrongFormat() {
    final T model = this.buildTestEntity();
    final D dto = bridge.toDto(model);
    final ResponseEntity<D> expectedResponse = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    final ResponseEntity<D> actual =
        controller.updateData(mockAuthentication, "this_is_not_a_uuid", dto);

    verifyNoMoreInteractions(mockService);

    assertThat(actual, is(expectedResponse));
    assertNull(actual.getBody());
  }

  /**
   * Test method for {@link AbstractGenericController#updateData(String, AbstractGenericDto)}.
   * 
   * @throws EntityNotFoundException if the type entity to update is not found.
   */
  @Test
  public void testUpdateDataNull() {
    final D dto = null;
    final ResponseEntity<D> expectedResponse = new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);

    final ResponseEntity<D> actual =
        controller.updateData(mockAuthentication, RANDOM_ID.toString(), dto);

    verifyNoMoreInteractions(mockService);

    assertThat(actual, is(expectedResponse));
    assertNull(actual.getBody());
  }

  /**
   * Test method for {@link AbstractGenericController#updateData(String, AbstractGenericDto)}.
   * 
   * @throws EntityNotFoundException if the type entity to update is not found.
   */
  @Test
  public void testUpdateDataNullIdentifier() {
    final T model = this.buildTestEntity();
    model.setId(null);
    final D dto = bridge.toDto(model);
    final ResponseEntity<D> expectedResponse = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    final ResponseEntity<D> actual =
        controller.updateData(mockAuthentication, RANDOM_ID.toString(), dto);

    verifyNoMoreInteractions(mockService);

    assertThat(actual, is(expectedResponse));
    assertNull(actual.getBody());
  }

  /**
   * Test method for {@link AbstractGenericController#deleteData(java.lang.String)}.
   * 
   * @throws EntityNotFoundException if the type entity to delete is not found.
   */
  @Test
  public void testDeleteData() {
    final ResponseEntity<Void> expectedResponse = new ResponseEntity<>(HttpStatus.NO_CONTENT);

    final String[] adminAuthorities = controller.getAdminAuthorities();
    assertNotNull(adminAuthorities);
    final Collection<GrantedAuthority> userAuthorities = new ArrayList<>(adminAuthorities.length);
    for (final String adminAuth : controller.getAdminAuthorities()) {
      userAuthorities.add(new SimpleGrantedAuthority(adminAuth));
    }

    when(mockAuthentication.getAuthorities()).then(invocation -> userAuthorities);

    final ResponseEntity<Void> actual =
        controller.deleteData(mockAuthentication, RANDOM_ID.toString());

    verify(mockAuthentication, times(1)).getAuthorities();
    verifyNoMoreInteractions(mockAuthentication);
    verify(mockService, times(1)).deleteById(RANDOM_ID);
    verifyNoMoreInteractions(mockService);

    assertThat(actual, is(expectedResponse));
  }

  /**
   * Test method for {@link AbstractGenericController#deleteData(java.lang.String)}.
   * 
   * @throws EntityNotFoundException if the type entity to delete is not found.
   */
  @Test
  public void testDeleteDataNotAdmin() {
    final ResponseEntity<Void> expectedResponse = new ResponseEntity<>(HttpStatus.NO_CONTENT);
    final UUID principalId = null;

    when(mockAuthentication.getAuthorities()).thenReturn(Collections.emptyList());
    when(mockAuthentication.getDetails()).thenReturn(null);

    final ResponseEntity<Void> actual =
        controller.deleteData(mockAuthentication, RANDOM_ID.toString());

    verify(mockAuthentication, times(1)).getAuthorities();
    verify(mockAuthentication, times(1)).getDetails();
    verifyNoMoreInteractions(mockAuthentication);
    verify(mockService, times(1)).deleteByIdAndOwner(RANDOM_ID, principalId);
    verifyNoMoreInteractions(mockService);

    assertThat(actual, is(expectedResponse));
  }

  /**
   * Test method for {@link AbstractGenericController#deleteData(java.lang.String)}.
   * 
   * @throws EntityNotFoundException if the type entity to delete is not found.
   */
  @Test
  public void testDeleteDataNotFoundException() {
    final ResponseEntity<Void> expectedResponse = new ResponseEntity<>(HttpStatus.NOT_FOUND);

    final String[] adminAuthorities = controller.getAdminAuthorities();
    assertNotNull(adminAuthorities);
    final Collection<GrantedAuthority> userAuthorities = new ArrayList<>(adminAuthorities.length);
    for (final String adminAuth : controller.getAdminAuthorities()) {
      userAuthorities.add(new SimpleGrantedAuthority(adminAuth));
    }

    when(mockAuthentication.getAuthorities()).then(invocation -> userAuthorities);
    doThrow(this.buildTestEntityNotFound()).when(mockService).deleteById(RANDOM_ID);

    final ResponseEntity<Void> actual =
        controller.deleteData(mockAuthentication, RANDOM_ID.toString());

    verify(mockAuthentication, times(1)).getAuthorities();
    verifyNoMoreInteractions(mockAuthentication);
    verify(mockService, times(1)).deleteById(RANDOM_ID);
    verifyNoMoreInteractions(mockService);

    assertThat(actual, is(expectedResponse));
  }

  /**
   * Test method for {@link AbstractGenericController#deleteData(java.lang.String)}.
   * 
   * @throws EntityNotFoundException if the type entity to delete is not found.
   */
  @Test
  public void testDeleteDataNotAdminNotFoundException() {
    final ResponseEntity<Void> expectedResponse = new ResponseEntity<>(HttpStatus.NOT_FOUND);
    final UUID principalId = null;

    when(mockAuthentication.getAuthorities()).thenReturn(Collections.emptyList());
    when(mockAuthentication.getDetails()).thenReturn(null);
    doThrow(this.buildTestEntityNotFound()).when(mockService).deleteByIdAndOwner(RANDOM_ID,
        principalId);

    final ResponseEntity<Void> actual =
        controller.deleteData(mockAuthentication, RANDOM_ID.toString());

    verify(mockAuthentication, times(1)).getAuthorities();
    verify(mockAuthentication, times(1)).getDetails();
    verifyNoMoreInteractions(mockAuthentication);
    verify(mockService, times(1)).deleteByIdAndOwner(RANDOM_ID, principalId);
    verifyNoMoreInteractions(mockService);

    assertThat(actual, is(expectedResponse));
  }

  /**
   * Test method for {@link AbstractGenericController#deleteData(java.lang.String)}.
   */
  @Test
  public void testDeleteDataIdIllegal() {
    final ResponseEntity<Void> expectedResponse = new ResponseEntity<>(HttpStatus.NOT_FOUND);

    final ResponseEntity<Void> actual =
        controller.deleteData(mockAuthentication, "this_is_not_a_UUID");

    verifyNoMoreInteractions(mockService);

    assertThat(actual, is(expectedResponse));
  }

}
