/*
 * Creation by madmath03 the 2018-01-09.
 */

package com.monogramm.starter.persistence.parameter.service;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.monogramm.starter.dto.parameter.ParameterDto;
import com.monogramm.starter.persistence.AbstractGenericServiceTest;
import com.monogramm.starter.persistence.parameter.dao.IParameterRepository;
import com.monogramm.starter.persistence.parameter.entity.Parameter;
import com.monogramm.starter.persistence.parameter.exception.ParameterNotFoundException;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

/**
 * {@link ParameterService} Unit Test.
 * 
 * @author madmath03
 */
public class ParameterServiceTest
    extends AbstractGenericServiceTest<Parameter, ParameterDto, ParameterService> {

  protected static final String DUMMY_NAME = ParameterServiceTest.class.getSimpleName();
  protected static final Object DUMMY_VALUE = 42;

  @Override
  protected ParameterService buildTestService() {
    return new ParameterService(getMockRepository(), getMockUserRepository(), getMockAuthenticationFacade());
  }

  @Override
  protected IParameterRepository buildMockRepository() {
    return mock(IParameterRepository.class);
  }

  @Override
  public IParameterRepository getMockRepository() {
    return (IParameterRepository) super.getMockRepository();
  }

  @Override
  protected Parameter buildTestEntity() {
    return Parameter.builder(DUMMY_NAME, DUMMY_VALUE).id(ID).build();
  }

  @Override
  protected ParameterNotFoundException buildEntityNotFoundException() {
    return new ParameterNotFoundException();
  }

  @Override
  @Test
  public void testExists() {
    final Parameter model = this.buildTestEntity();

    when(getMockRepository().exists(model.getId(), model.getName())).thenReturn(true);

    assertTrue(getService().exists(model));

    verify(getMockRepository(), times(1)).exists(model.getId(), model.getName());
    verifyNoMoreInteractions(getMockRepository());
  }

  @Override
  @Test
  public void testExistsNotFound() {
    final Parameter model = this.buildTestEntity();

    when(getMockRepository().exists(model.getId(), model.getName())).thenReturn(false);

    assertFalse(getService().exists(model));

    verify(getMockRepository(), times(1)).exists(model.getId(), model.getName());
    verifyNoMoreInteractions(getMockRepository());
  }

  /**
   * Test method for {@link ParameterService#add(Parameter)}.
   */
  @Override
  @Test
  public void testAdd() {
    final Parameter model = this.buildTestEntity();

    when(getMockRepository().exists(model.getId(), model.getName())).thenReturn(false);

    getService().add(model);

    ArgumentCaptor<Parameter> parameterArgument = ArgumentCaptor.forClass(Parameter.class);
    verify(getMockRepository(), times(1)).exists(model.getId(), model.getName());
    verify(getMockRepository(), times(1)).add(parameterArgument.capture());
    verifyNoMoreInteractions(getMockRepository());

    final Parameter actual = parameterArgument.getValue();

    assertThat(actual.getName(), is(model.getName()));
  }

  /**
   * Test method for {@link ParameterService#add(Parameter)}.
   */
  @Override
  @Test
  public void testAddAlreadyExists() {
    final Parameter model = this.buildTestEntity();

    when(getMockRepository().exists(model.getId(), model.getName())).thenReturn(true);

    getService().add(model);

    verify(getMockRepository(), times(1)).exists(model.getId(), model.getName());
    verifyNoMoreInteractions(getMockRepository());
  }

  /**
   * Test method for {@link ParameterService#findById(java.util.UUID)}.
   * 
   * @throws ParameterNotFoundException if the parameter is not found.
   */
  @Test
  public void testFindByName() {
    final Parameter model = this.buildTestEntity();

    when(getMockRepository().findByNameIgnoreCase(DUMMY_NAME)).thenReturn(model);

    final Parameter actual = getService().findByName(DUMMY_NAME);

    verify(getMockRepository(), times(1)).findByNameIgnoreCase(DUMMY_NAME);
    verifyNoMoreInteractions(getMockRepository());

    assertThat(actual, is(model));
  }

  /**
   * Test method for {@link ParameterService#findById(java.util.UUID)}.
   * 
   * @throws ParameterNotFoundException if the parameter is not found.
   */
  @Test
  public void testFindByNameNotFound() {
    when(getMockRepository().findByNameIgnoreCase(DUMMY_NAME)).thenReturn(null);

    final Parameter actual = getService().findByName(DUMMY_NAME);

    verify(getMockRepository(), times(1)).findByNameIgnoreCase(DUMMY_NAME);
    verifyNoMoreInteractions(getMockRepository());

    assertNull(actual);
  }

  /**
   * Test method for {@link ParameterService#findById(java.util.UUID)}.
   * 
   * @throws ParameterNotFoundException if the parameter is not found.
   */
  @Test(expected = ParameterNotFoundException.class)
  public void testFindByNameParameterNotFoundException() {
    when(getMockRepository().findByNameIgnoreCase(DUMMY_NAME))
        .thenThrow(new ParameterNotFoundException());

    getService().findByName(DUMMY_NAME);
  }

}
