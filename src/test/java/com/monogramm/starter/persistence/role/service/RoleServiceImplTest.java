/*
 * Creation by madmath03 the 2017-09-04.
 */

package com.monogramm.starter.persistence.role.service;

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

import com.monogramm.starter.dto.role.RoleDto;
import com.monogramm.starter.persistence.AbstractGenericServiceTest;
import com.monogramm.starter.persistence.EntityNotFoundException;
import com.monogramm.starter.persistence.permission.dao.PermissionRepository;
import com.monogramm.starter.persistence.role.dao.RoleRepository;
import com.monogramm.starter.persistence.role.entity.Role;
import com.monogramm.starter.persistence.role.exception.RoleNotFoundException;
import com.monogramm.starter.persistence.user.dao.UserRepository;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

/**
 * {@link RoleServiceImpl} Unit Test.
 * 
 * @author madmath03
 */
public class RoleServiceImplTest extends AbstractGenericServiceTest<Role, RoleDto, RoleServiceImpl> {

  private static final String DISPLAYNAME = RoleServiceImplTest.class.getSimpleName();

  private PermissionRepository permissionDao;

  /**
   * @throws java.lang.Exception if the test setup crashes.
   */
  @Before
  public void setUp() throws Exception {
    permissionDao = mock(PermissionRepository.class);
    super.setUp();
  }

  /**
   * @throws java.lang.Exception if the test cleanup crashes.
   */
  @After
  public void tearDown() throws Exception {
    super.tearDown();
    Mockito.reset(permissionDao);
  }

  @Override
  protected RoleServiceImpl buildTestService() {
    return new RoleServiceImpl(getMockRepository(), getMockUserRepository(), permissionDao, getMockAuthenticationFacade());
  }

  @Override
  protected RoleRepository buildMockRepository() {
    return mock(RoleRepository.class);
  }

  @Override
  public RoleRepository getMockRepository() {
    return (RoleRepository) super.getMockRepository();
  }

  @Override
  protected Role buildTestEntity() {
    return Role.builder(DISPLAYNAME).id(ID).build();
  }

  @Override
  protected EntityNotFoundException buildEntityNotFoundException() {
    return new RoleNotFoundException();
  }

  @Override
  @Test
  public void testExists() {
    final Role model = this.buildTestEntity();

    when(getMockRepository().exists(model.getId(), model.getName())).thenReturn(true);

    assertTrue(getService().exists(model));

    verify(getMockRepository(), times(1)).exists(model.getId(), model.getName());
    verifyNoMoreInteractions(getMockRepository());
  }

  @Override
  @Test
  public void testExistsNotFound() {
    final Role model = this.buildTestEntity();

    when(getMockRepository().exists(model.getId(), model.getName())).thenReturn(false);

    assertFalse(getService().exists(model));

    verify(getMockRepository(), times(1)).exists(model.getId(), model.getName());
    verifyNoMoreInteractions(getMockRepository());
  }

  /**
   * Test method for {@link RoleServiceImpl#add(com.monogramm.starter.persistence.role.entity.Role)}.
   */
  @Override
  @Test
  public void testAdd() {
    final Role model = this.buildTestEntity();

    when(getMockRepository().exists(model.getId(), model.getName())).thenReturn(false);

    assertTrue(getService().add(model));

    ArgumentCaptor<Role> roleArgument = ArgumentCaptor.forClass(Role.class);
    verify(getMockRepository(), times(1)).exists(model.getId(), model.getName());
    verify(getMockRepository(), times(1)).add(roleArgument.capture());
    verifyNoMoreInteractions(getMockRepository());

    final Role actual = roleArgument.getValue();

    assertThat(actual.getName(), is(model.getName()));
  }

  /**
   * Test method for {@link RoleServiceImpl#add(com.monogramm.starter.persistence.role.entity.Role)}.
   */
  @Override
  @Test
  public void testAddAlreadyExists() {
    final Role model = this.buildTestEntity();

    when(getMockRepository().exists(model.getId(), model.getName())).thenReturn(true);

    assertFalse(getService().add(model));

    verify(getMockRepository(), times(1)).exists(model.getId(), model.getName());
    verifyNoMoreInteractions(getMockRepository());
  }

  /**
   * Test method for
   * {@link RoleServiceImpl#RoleService(RoleRepository, UserRepository, PermissionRepository)}.
   */
  @Test
  public void testRoleService() {
    assertNotNull(getService());
  }

  /**
   * Test method for
   * {@link RoleServiceImpl#RoleService(RoleRepository, UserRepository, PermissionRepository)}.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testRoleServiceIRoleRepositoryNull() {
    new RoleServiceImpl(getMockRepository(), getMockUserRepository(), null, getMockAuthenticationFacade());
  }

  /**
   * Test method for {@link RoleServiceImpl#getPermissionRepository()}.
   */
  @Test
  public void testGetPermissionRepository() {
    assertNotNull(getService().getPermissionRepository());
    assertEquals(permissionDao, getService().getPermissionRepository());
  }

  /**
   * Test method for {@link RoleServiceImpl#findById(java.util.UUID)}.
   * 
   * @throws RoleNotFoundException if the role is not found.
   */
  @Test
  public void testFindByName() {
    final Role model = this.buildTestEntity();

    when(getMockRepository().findByNameIgnoreCase(DISPLAYNAME)).thenReturn(model);

    final Role actual = getService().findByName(DISPLAYNAME);

    verify(getMockRepository(), times(1)).findByNameIgnoreCase(DISPLAYNAME);
    verifyNoMoreInteractions(getMockRepository());

    assertThat(actual, is(model));
  }

  /**
   * Test method for {@link RoleServiceImpl#findById(java.util.UUID)}.
   * 
   * @throws RoleNotFoundException if the role is not found.
   */
  @Test
  public void testFindByNameNotFound() {
    when(getMockRepository().findByNameIgnoreCase(DISPLAYNAME)).thenReturn(null);

    final Role actual = getService().findByName(DISPLAYNAME);

    verify(getMockRepository(), times(1)).findByNameIgnoreCase(DISPLAYNAME);
    verifyNoMoreInteractions(getMockRepository());

    assertNull(actual);
  }

  /**
   * Test method for {@link RoleServiceImpl#findById(java.util.UUID)}.
   * 
   * @throws RoleNotFoundException if the role is not found.
   */
  @Test(expected = RoleNotFoundException.class)
  public void testFindByNameRoleNotFoundException() {
    when(getMockRepository().findByNameIgnoreCase(DISPLAYNAME))
        .thenThrow(new RoleNotFoundException());

    getService().findByName(DISPLAYNAME);
  }

  /**
   * Test method for {@link RoleServiceImpl#findAllByName(java.lang.String)}.
   */
  @Test
  public void testFindAllByName() {
    final List<Role> models = new ArrayList<>();
    when(getMockRepository().findAllContainingNameIgnoreCase(DISPLAYNAME))
        .thenReturn(models);

    final List<Role> actual = getService().findAllByName(DISPLAYNAME);

    verify(getMockRepository(), times(1)).findAllContainingNameIgnoreCase(DISPLAYNAME);
    verifyNoMoreInteractions(getMockRepository());

    assertThat(actual, is(models));
  }

}
