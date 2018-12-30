/*
 * Creation by madmath03 the 2017-09-04.
 */

package com.monogramm.starter.api.type.controller;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import com.monogramm.starter.api.AbstractGenericController;
import com.monogramm.starter.api.AbstractGenericControllerTest;
import com.monogramm.starter.dto.type.TypeDto;
import com.monogramm.starter.persistence.AbstractGenericBridge;
import com.monogramm.starter.persistence.type.entity.Type;
import com.monogramm.starter.persistence.type.exception.TypeNotFoundException;
import com.monogramm.starter.persistence.type.service.ITypeService;
import com.monogramm.starter.persistence.type.service.TypeBridge;

import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;

/**
 * {@link TypeController} Unit Test.
 * 
 * @author madmath03
 */
public class TypeControllerTest extends AbstractGenericControllerTest<Type, TypeDto> {

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
  protected ITypeService getMockService() {
    return (ITypeService) super.getMockService();
  }

  @Override
  protected ITypeService buildTestService() {
    return mock(ITypeService.class);
  }

  @Override
  protected Authentication buildMockAuthentication() {
    return mock(Authentication.class);
  }

  @Override
  protected AbstractGenericController<Type, TypeDto> buildTestController() {
    return new TypeController(getMockService());
  }

  @Override
  protected AbstractGenericBridge<Type, TypeDto> buildTestBridge() {
    return new TypeBridge();
  }

  @Override
  protected Type buildTestEntity() {
    return Type.builder(DISPLAYNAME).id(ID).build();
  }

  @Override
  protected Class<Type> getEntityClass() {
    return Type.class;
  }

  @SuppressWarnings("unchecked")
  @Override
  protected TypeNotFoundException buildTestEntityNotFound() {
    return new TypeNotFoundException();
  }

  /**
   * Test method for {@link TypeController#TypeController(ITypeService)}.
   */
  @Test
  public void testTypeController() {
    assertNotNull(getController());
  }

}
