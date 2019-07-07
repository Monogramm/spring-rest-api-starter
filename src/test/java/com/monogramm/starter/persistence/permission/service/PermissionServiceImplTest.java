/*
 * Creation by madmath03 the 2017-11-25
 */

package com.monogramm.starter.persistence.permission.service;

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

import com.monogramm.starter.dto.permission.PermissionDto;
import com.monogramm.starter.persistence.AbstractGenericServiceTest;
import com.monogramm.starter.persistence.EntityNotFoundException;
import com.monogramm.starter.persistence.permission.dao.PermissionRepository;
import com.monogramm.starter.persistence.permission.entity.Permission;
import com.monogramm.starter.persistence.permission.exception.PermissionNotFoundException;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

/**
 * {@link PermissionServiceImpl} Unit Test.
 * 
 * @author madmath03
 */
public class PermissionServiceImplTest
    extends AbstractGenericServiceTest<Permission, PermissionDto, PermissionServiceImpl> {

  private static final String DISPLAYNAME = "Foo";

  @Override
  protected PermissionServiceImpl buildTestService() {
    return new PermissionServiceImpl(getMockRepository(), getMockUserRepository(), getMockAuthenticationFacade());
  }

  @Override
  protected PermissionRepository buildMockRepository() {
    return mock(PermissionRepository.class);
  }

  @Override
  public PermissionRepository getMockRepository() {
    return (PermissionRepository) super.getMockRepository();
  }

  @Override
  protected Permission buildTestEntity() {
    return Permission.builder(DISPLAYNAME).id(ID).build();
  }

  @Override
  protected EntityNotFoundException buildEntityNotFoundException() {
    return new PermissionNotFoundException();
  }

  @Override
  @Test
  public void testExists() {
    final Permission model = this.buildTestEntity();

    when(getMockRepository().exists(model.getId(), model.getName())).thenReturn(true);

    assertTrue(getService().exists(model));

    verify(getMockRepository(), times(1)).exists(model.getId(), model.getName());
    verifyNoMoreInteractions(getMockRepository());
  }

  @Override
  @Test
  public void testExistsNotFound() {
    final Permission model = this.buildTestEntity();

    when(getMockRepository().exists(model.getId(), model.getName())).thenReturn(false);

    assertFalse(getService().exists(model));

    verify(getMockRepository(), times(1)).exists(model.getId(), model.getName());
    verifyNoMoreInteractions(getMockRepository());
  }

  /**
   * Test method for {@link PermissionServiceImpl#add(Permission)}.
   */
  @Override
  @Test
  public void testAdd() {
    final Permission model = this.buildTestEntity();

    when(getMockRepository().exists(model.getId(), model.getName())).thenReturn(false);

    assertTrue(getService().add(model));

    ArgumentCaptor<Permission> permissionArgument = ArgumentCaptor.forClass(Permission.class);
    verify(getMockRepository(), times(1)).exists(model.getId(), model.getName());
    verify(getMockRepository(), times(1)).add(permissionArgument.capture());
    verifyNoMoreInteractions(getMockRepository());

    final Permission actual = permissionArgument.getValue();

    assertThat(actual.getName(), is(model.getName()));
  }

  /**
   * Test method for {@link PermissionServiceImpl#add(Permission)}.
   */
  @Override
  @Test
  public void testAddAlreadyExists() {
    final Permission model = this.buildTestEntity();

    when(getMockRepository().exists(model.getId(), model.getName())).thenReturn(true);

    assertFalse(getService().add(model));

    verify(getMockRepository(), times(1)).exists(model.getId(), model.getName());
    verifyNoMoreInteractions(getMockRepository());
  }

  /**
   * Test method for {@link PermissionServiceImpl#findById(java.util.UUID)}.
   * 
   * @throws PermissionNotFoundException if the permission is not found.
   */
  @Test
  public void testFindByName() {
    final Permission model = this.buildTestEntity();

    when(getMockRepository().findByNameIgnoreCase(DISPLAYNAME)).thenReturn(model);

    final Permission actual = getService().findByName(DISPLAYNAME);

    verify(getMockRepository(), times(1)).findByNameIgnoreCase(DISPLAYNAME);
    verifyNoMoreInteractions(getMockRepository());

    assertThat(actual, is(model));
  }

  /**
   * Test method for {@link PermissionServiceImpl#findById(java.util.UUID)}.
   * 
   * @throws PermissionNotFoundException if the permission is not found.
   */
  @Test
  public void testFindByNameNotFound() {
    when(getMockRepository().findByNameIgnoreCase(DISPLAYNAME)).thenReturn(null);

    final Permission actual = getService().findByName(DISPLAYNAME);

    verify(getMockRepository(), times(1)).findByNameIgnoreCase(DISPLAYNAME);
    verifyNoMoreInteractions(getMockRepository());

    assertNull(actual);
  }

  /**
   * Test method for {@link PermissionServiceImpl#findById(java.util.UUID)}.
   * 
   * @throws PermissionNotFoundException if the permission is not found.
   */
  @Test(expected = PermissionNotFoundException.class)
  public void testFindByNamePermissionNotFoundException() {
    when(getMockRepository().findByNameIgnoreCase(DISPLAYNAME))
        .thenThrow(new PermissionNotFoundException());

    getService().findByName(DISPLAYNAME);
  }

  /**
   * Test method for {@link PermissionServiceImpl#findAllByName(java.lang.String)}.
   */
  @Test
  public void testFindAllByName() {
    final List<Permission> models = new ArrayList<>();
    when(getMockRepository().findAllContainingNameIgnoreCase(DISPLAYNAME))
        .thenReturn(models);

    final List<Permission> actual = getService().findAllByName(DISPLAYNAME);

    verify(getMockRepository(), times(1)).findAllContainingNameIgnoreCase(DISPLAYNAME);
    verifyNoMoreInteractions(getMockRepository());

    assertThat(actual, is(models));
  }

}
