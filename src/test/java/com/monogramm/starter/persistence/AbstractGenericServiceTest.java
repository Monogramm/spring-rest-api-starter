/*
 * Creation by madmath03 the 2018-01-09.
 */

package com.monogramm.starter.persistence;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
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

import com.monogramm.starter.dto.AbstractGenericDto;
import com.monogramm.starter.persistence.user.dao.IUserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * {@link AbstractGenericService} Unit Test.
 * 
 * @author madmath03
 */
public abstract class AbstractGenericServiceTest<T extends AbstractGenericEntity,
    D extends AbstractGenericDto, S extends AbstractGenericService<T, D>>
    extends GenericServiceTest<T, D, S> {

  protected static final UUID ID = UUID.randomUUID();


  private GenericRepository<T> mockRepository;

  private IUserRepository mockUserRepository;

  private S service;


  protected abstract S buildTestService();

  protected abstract GenericRepository<T> buildMockRepository();

  protected abstract T buildTestEntity();

  protected abstract EntityNotFoundException buildEntityNotFoundException();

  /**
   * @throws java.lang.Exception if the test setup crashes.
   */
  @Before
  public void setUp() throws Exception {
    mockUserRepository = mock(IUserRepository.class);
    mockRepository = this.buildMockRepository();

    this.service = this.buildTestService();
    assertNotNull(this.service);
  }

  /**
   * @throws java.lang.Exception if the test setup crashes.
   */
  @After
  public void tearDown() throws Exception {
    Mockito.reset(mockUserRepository);

    this.service = null;
  }

  /**
   * Get the {@link #mockRepository}.
   * 
   * @return the {@link #mockRepository}.
   */
  public GenericRepository<T> getMockRepository() {
    return mockRepository;
  }

  /**
   * Get the {@link #mockUserRepository}.
   * 
   * @return the {@link #mockUserRepository}.
   */
  public IUserRepository getMockUserRepository() {
    return mockUserRepository;
  }

  /**
   * Get the {@link #service}.
   * 
   * @return the {@link #service}.
   */
  public final S getService() {
    return service;
  }

  /**
   * Test method for
   * {@link AbstractGenericService#AbstractGenericService(GenericRepository, IUserRepository, AbstractGenericBridge)}.
   */
  @Test
  public void testAbstractGenericService() {
    assertNotNull(service);
    assertEquals(mockRepository, service.getRepository());
    assertEquals(mockUserRepository, service.getUserRepository());
  }

  /**
   * Test method for
   * {@link AbstractGenericService#createEntityNotFoundException(AbstractGenericEntity)}.
   */
  @Test
  public void testCreateEntityNotFoundExceptionT() {
    assertNotNull(service.createEntityNotFoundException((T) null));
  }

  /**
   * Test method for {@link AbstractGenericService#createEntityNotFoundException(java.util.UUID)}.
   */
  @Test
  public void testCreateEntityNotFoundExceptionUUID() {
    assertNotNull(service.createEntityNotFoundException(UUID.randomUUID()));
  }

  /**
   * Test method for {@link AbstractGenericService#getRepository()}.
   */
  @Test
  public void testGetRepository() {
    assertNotNull(service.getRepository());
    assertEquals(mockRepository, service.getRepository());
  }

  /**
   * Test method for {@link AbstractGenericService#getUserRepository()}.
   */
  @Test
  public void testGetUserRepository() {
    assertNotNull(service.getUserRepository());
    assertEquals(mockUserRepository, service.getUserRepository());
  }

  /**
   * Test method for {@link AbstractGenericService#getBridge()}.
   */
  @Test
  public void testGetBridge() {
    assertNotNull(service.getBridge());
  }

  /**
   * Test method for {@link AbstractGenericService#exists(AbstractGenericEntity)}.
   */
  @Test
  public void testExists() {
    final T model = this.buildTestEntity();

    when(mockRepository.exists(model.getId())).thenReturn(true);

    assertTrue(service.exists(model));

    verify(mockRepository, times(1)).exists(model.getId());
    verifyNoMoreInteractions(mockRepository);
  }

  /**
   * Test method for {@link AbstractGenericService#exists(AbstractGenericEntity)}.
   */
  @Test
  public void testExistsNotFound() {
    final T model = this.buildTestEntity();

    when(mockRepository.exists(model.getId())).thenReturn(false);

    assertFalse(service.exists(model));

    verify(mockRepository, times(1)).exists(model.getId());
    verifyNoMoreInteractions(mockRepository);
  }

  /**
   * Test method for {@link AbstractGenericService#findById(java.util.UUID)}.
   */
  @Test
  public void testFindById() {
    final T model = this.buildTestEntity();

    when(mockRepository.findById(model.getId())).thenReturn(model);

    final T actual = service.findById(model.getId());

    verify(mockRepository, times(1)).findById(model.getId());
    verifyNoMoreInteractions(mockRepository);

    assertThat(actual, is(model));
  }

  /**
   * Test method for {@link AbstractGenericService#findById(java.util.UUID)}.
   */
  @Test
  public void testFindByIdNotFound() {
    when(mockRepository.findById(ID)).thenReturn(null);

    final T actual = service.findById(ID);

    verify(mockRepository, times(1)).findById(ID);
    verifyNoMoreInteractions(mockRepository);

    assertNull(actual);
  }

  /**
   * Test method for {@link AbstractGenericService#findAll()}.
   */
  @Test
  public void testFindAll() {
    final List<T> models = new ArrayList<>();
    when(mockRepository.findAll()).thenReturn(models);

    final List<T> actual = service.findAll();

    verify(mockRepository, times(1)).findAll();
    verifyNoMoreInteractions(mockRepository);

    assertThat(actual, is(models));
  }

  /**
   * Test method for
   * {@link AbstractGenericService#add(com.monogramm.starter.persistence.type.entity.T)}.
   */
  @Test
  public void testAdd() {
    final T model = this.buildTestEntity();

    when(mockRepository.exists(model.getId())).thenReturn(false);

    assertTrue(service.add(model));

    verify(mockRepository, times(1)).exists(model.getId());
    verify(mockRepository, times(1)).add(model);
    verifyNoMoreInteractions(mockRepository);
  }

  /**
   * Test method for
   * {@link AbstractGenericService#add(com.monogramm.starter.persistence.type.entity.T)}.
   */
  @Test
  public void testAddAlreadyExists() {
    final T model = this.buildTestEntity();

    when(mockRepository.exists(model.getId())).thenReturn(true);

    assertFalse(service.add(model));

    verify(mockRepository, times(1)).exists(model.getId());
    verifyNoMoreInteractions(mockRepository);
  }

  /**
   * Test method for
   * {@link AbstractGenericService#update(com.monogramm.starter.persistence.type.entity.T)}.
   * 
   * @throws EntityNotFoundException if the type is not found.
   */
  @Test
  public void testUpdate() {
    final T model = this.buildTestEntity();

    when(mockRepository.update(model)).thenReturn(model);

    final T actual = service.update(model);

    verify(mockRepository, times(1)).update(model);
    verifyNoMoreInteractions(mockRepository);

    assertThat(actual, is(model));
  }

  /**
   * Test method for
   * {@link AbstractGenericService#update(com.monogramm.starter.persistence.type.entity.T)}.
   * 
   * @throws EntityNotFoundException if the type is not found.
   */
  @Test(expected = EntityNotFoundException.class)
  public void testUpdateNotFound() {
    final T model = this.buildTestEntity();

    when(mockRepository.update(model)).thenReturn(null);

    service.update(model);
  }

  /**
   * Test method for
   * {@link AbstractGenericService#update(com.monogramm.starter.persistence.type.entity.T)}.
   * 
   * @throws EntityNotFoundException if the type is not found.
   */
  @Test(expected = EntityNotFoundException.class)
  public void testUpdateEntityNotFoundException() {
    final T model = this.buildTestEntity();

    when(mockRepository.update(model)).thenThrow(this.buildEntityNotFoundException());

    service.update(model);
  }

  /**
   * Test method for {@link AbstractGenericService#deleteById(java.util.UUID)}.
   * 
   * @throws EntityNotFoundException if the type is not found.
   */
  @Test
  public void testDeleteById() {
    when(mockRepository.deleteById(ID)).thenReturn(1);

    service.deleteById(ID);

    verify(mockRepository, times(1)).deleteById(ID);
    verifyNoMoreInteractions(mockRepository);
  }

  /**
   * Test method for {@link AbstractGenericService#deleteById(java.util.UUID)}.
   * 
   * @throws EntityNotFoundException if the type is not found.
   */
  @Test(expected = EntityNotFoundException.class)
  public void testDeleteByIdNotFound() {
    when(mockRepository.deleteById(ID)).thenReturn(null);

    service.deleteById(ID);
  }

  /**
   * Test method for {@link AbstractGenericService#deleteById(java.util.UUID)}.
   * 
   * @throws EntityNotFoundException if the type is not found.
   */
  @Test(expected = EntityNotFoundException.class)
  public void testDeleteByIdNoDeletion() {
    when(mockRepository.deleteById(ID)).thenReturn(0);

    service.deleteById(ID);
  }

  /**
   * Test method for {@link AbstractGenericService#deleteById(java.util.UUID)}.
   * 
   * @throws EntityNotFoundException if the type is not found.
   */
  @Test(expected = EntityNotFoundException.class)
  public void testDeleteByIdEntityNotFoundException() {
    when(mockRepository.deleteById(ID)).thenThrow(this.buildEntityNotFoundException());

    service.deleteById(ID);
  }

}
