/*
 * Creation by madmath03 the 2017-09-04.
 */

package com.monogramm.starter.api.permission.controller;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import com.monogramm.starter.api.AbstractGenericController;
import com.monogramm.starter.api.AbstractGenericControllerTest;
import com.monogramm.starter.dto.permission.PermissionDto;
import com.monogramm.starter.persistence.AbstractGenericBridge;
import com.monogramm.starter.persistence.permission.entity.Permission;
import com.monogramm.starter.persistence.permission.exception.PermissionNotFoundException;
import com.monogramm.starter.persistence.permission.service.PermissionService;
import com.monogramm.starter.persistence.permission.service.PermissionBridge;

import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;

/**
 * {@link PermissionController} Unit Test.
 * 
 * @author madmath03
 */
public class PermissionControllerTest
    extends AbstractGenericControllerTest<Permission, PermissionDto> {

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
  protected PermissionService getMockService() {
    return (PermissionService) super.getMockService();
  }

  @Override
  protected PermissionService buildTestService() {
    return mock(PermissionService.class);
  }

  @Override
  protected Authentication buildMockAuthentication() {
    return mock(Authentication.class);
  }

  @Override
  protected AbstractGenericController<Permission, PermissionDto> buildTestController() {
    return new PermissionController(getMessageSource(), getEventPublisher(), getMockService());
  }

  @Override
  protected AbstractGenericBridge<Permission, PermissionDto> buildTestBridge() {
    return new PermissionBridge();
  }

  @Override
  protected Permission buildTestEntity() {
    return Permission.builder(DISPLAYNAME).id(ID).build();
  }

  @Override
  protected Class<Permission> getEntityClass() {
    return Permission.class;
  }

  @SuppressWarnings("unchecked")
  @Override
  protected PermissionNotFoundException buildTestEntityNotFound() {
    return new PermissionNotFoundException();
  }

  /**
   * Test method for {@link PermissionController#PermissionController(PermissionService)}.
   */
  @Test
  public void testPermissionController() {
    assertNotNull(getController());
  }

}
