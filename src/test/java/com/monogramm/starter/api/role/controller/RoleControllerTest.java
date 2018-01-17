/*
 * Creation by madmath03 the 2017-09-04.
 */

package com.monogramm.starter.api.role.controller;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import com.monogramm.starter.api.AbstractGenericController;
import com.monogramm.starter.api.AbstractGenericControllerTest;
import com.monogramm.starter.dto.role.RoleDto;
import com.monogramm.starter.persistence.AbstractGenericBridge;
import com.monogramm.starter.persistence.role.entity.Role;
import com.monogramm.starter.persistence.role.exception.RoleNotFoundException;
import com.monogramm.starter.persistence.role.service.IRoleService;
import com.monogramm.starter.persistence.role.service.RoleBridge;

import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * {@link RoleController} Unit Test.
 * 
 * @author madmath03
 */
public class RoleControllerTest extends AbstractGenericControllerTest<Role, RoleDto> {

  private static final UUID ID = UUID.randomUUID();
  private static final String DISPLAYNAME = "Foo";

  /**
   * @throws java.lang.Exception if the test setup crashes.
   */
  @Before
  public void setUp() throws Exception {
    super.setUp();
  }

  /**
   * @throws java.lang.Exception if the test cleanup crashes.
   */
  @After
  public void tearDown() throws Exception {
    Mockito.reset(getMockService());
    this.setBridge(null);
  }

  @Override
  protected IRoleService getMockService() {
    return (IRoleService) super.getMockService();
  }

  @Override
  protected IRoleService buildTestService() {
    return mock(IRoleService.class);
  }

  @Override
  protected AbstractGenericController<Role, RoleDto> buildTestController() {
    return new RoleController(getMockService());
  }

  @Override
  protected AbstractGenericBridge<Role, RoleDto> buildTestBridge() {
    return new RoleBridge();
  }

  @Override
  protected Role buildTestEntity() {
    return Role.builder(DISPLAYNAME).id(ID).build();
  }

  @Override
  protected Class<Role> getEntityClass() {
    return Role.class;
  }

  @SuppressWarnings("unchecked")
  @Override
  protected RoleNotFoundException buildTestEntityNotFound() {
    return new RoleNotFoundException();
  }

  /**
   * Test method for {@link RoleController#RoleController(IRoleService)}.
   */
  @Test
  public void testRoleController() {
    assertNotNull(getController());
  }

}
