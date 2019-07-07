/*
 * Creation by madmath03 the 2017-09-04.
 */

package com.monogramm.starter.persistence.type.service;

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

import com.monogramm.starter.dto.type.TypeDto;
import com.monogramm.starter.persistence.AbstractGenericServiceTest;
import com.monogramm.starter.persistence.EntityNotFoundException;
import com.monogramm.starter.persistence.type.dao.TypeRepository;
import com.monogramm.starter.persistence.type.entity.Type;
import com.monogramm.starter.persistence.type.exception.TypeNotFoundException;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

/**
 * {@link TypeServiceImpl} Unit Test.
 * 
 * @author madmath03
 */
public class TypeServiceImplTest extends AbstractGenericServiceTest<Type, TypeDto, TypeServiceImpl> {

  private static final String DISPLAYNAME = "Foo";

  @Override
  protected TypeServiceImpl buildTestService() {
    return new TypeServiceImpl(getMockRepository(), getMockUserRepository(), getMockAuthenticationFacade());
  }

  @Override
  protected TypeRepository buildMockRepository() {
    return mock(TypeRepository.class);
  }

  @Override
  public TypeRepository getMockRepository() {
    return (TypeRepository) super.getMockRepository();
  }

  @Override
  protected Type buildTestEntity() {
    return Type.builder(DISPLAYNAME).id(ID).build();
  }

  @Override
  protected EntityNotFoundException buildEntityNotFoundException() {
    return new TypeNotFoundException();
  }

  @Override
  @Test
  public void testExists() {
    final Type model = this.buildTestEntity();

    when(getMockRepository().exists(model.getId(), model.getName())).thenReturn(true);

    assertTrue(getService().exists(model));

    verify(getMockRepository(), times(1)).exists(model.getId(), model.getName());
    verifyNoMoreInteractions(getMockRepository());
  }

  @Override
  @Test
  public void testExistsNotFound() {
    final Type model = this.buildTestEntity();

    when(getMockRepository().exists(model.getId(), model.getName())).thenReturn(false);

    assertFalse(getService().exists(model));

    verify(getMockRepository(), times(1)).exists(model.getId(), model.getName());
    verifyNoMoreInteractions(getMockRepository());
  }

  /**
   * Test method for {@link TypeServiceImpl#add(Type)}.
   */
  @Override
  @Test
  public void testAdd() {
    final Type model = this.buildTestEntity();

    when(getMockRepository().exists(model.getId(), model.getName())).thenReturn(false);

    getService().add(model);

    ArgumentCaptor<Type> typeArgument = ArgumentCaptor.forClass(Type.class);
    verify(getMockRepository(), times(1)).exists(model.getId(), model.getName());
    verify(getMockRepository(), times(1)).add(typeArgument.capture());
    verifyNoMoreInteractions(getMockRepository());

    final Type actual = typeArgument.getValue();

    assertThat(actual.getName(), is(model.getName()));
  }

  /**
   * Test method for {@link TypeServiceImpl#add(Type)}.
   */
  @Override
  @Test
  public void testAddAlreadyExists() {
    final Type model = this.buildTestEntity();

    when(getMockRepository().exists(model.getId(), model.getName())).thenReturn(true);

    getService().add(model);

    verify(getMockRepository(), times(1)).exists(model.getId(), model.getName());
    verifyNoMoreInteractions(getMockRepository());
  }

  /**
   * Test method for {@link TypeServiceImpl#findById(java.util.UUID)}.
   * 
   * @throws TypeNotFoundException if the type is not found.
   */
  @Test
  public void testFindByName() {
    final Type model = this.buildTestEntity();

    when(getMockRepository().findByNameIgnoreCase(DISPLAYNAME)).thenReturn(model);

    final Type actual = getService().findByName(DISPLAYNAME);

    verify(getMockRepository(), times(1)).findByNameIgnoreCase(DISPLAYNAME);
    verifyNoMoreInteractions(getMockRepository());

    assertThat(actual, is(model));
  }

  /**
   * Test method for {@link TypeServiceImpl#findById(java.util.UUID)}.
   * 
   * @throws TypeNotFoundException if the type is not found.
   */
  @Test
  public void testFindByNameNotFound() {
    when(getMockRepository().findByNameIgnoreCase(DISPLAYNAME)).thenReturn(null);

    final Type actual = getService().findByName(DISPLAYNAME);

    verify(getMockRepository(), times(1)).findByNameIgnoreCase(DISPLAYNAME);
    verifyNoMoreInteractions(getMockRepository());

    assertNull(actual);
  }

  /**
   * Test method for {@link TypeServiceImpl#findById(java.util.UUID)}.
   * 
   * @throws TypeNotFoundException if the type is not found.
   */
  @Test(expected = TypeNotFoundException.class)
  public void testFindByNameTypeNotFoundException() {
    when(getMockRepository().findByNameIgnoreCase(DISPLAYNAME))
        .thenThrow(new TypeNotFoundException());

    getService().findByName(DISPLAYNAME);
  }

  /**
   * Test method for {@link TypeServiceImpl#findAllContainingNameIgnoreCase(java.lang.String)}.
   */
  @Test
  public void testFindAllContainingNameIgnoreCase() {
    final List<Type> models = new ArrayList<>();
    when(getMockRepository().findAllContainingNameIgnoreCase(DISPLAYNAME))
        .thenReturn(models);

    final List<Type> actual = getService().findAllByName(DISPLAYNAME);

    verify(getMockRepository(), times(1)).findAllContainingNameIgnoreCase(DISPLAYNAME);
    verifyNoMoreInteractions(getMockRepository());

    assertThat(actual, is(models));
  }

}
