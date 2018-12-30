/*
 * Creation by madmath03 the 2018-01-09.
 */

package com.monogramm.starter.api.parameter.controller;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import com.monogramm.starter.api.AbstractGenericControllerTest;
import com.monogramm.starter.dto.parameter.ParameterDto;
import com.monogramm.starter.persistence.parameter.entity.Parameter;
import com.monogramm.starter.persistence.parameter.exception.ParameterNotFoundException;
import com.monogramm.starter.persistence.parameter.service.IParameterService;
import com.monogramm.starter.persistence.parameter.service.ParameterBridge;

import java.util.UUID;

import org.junit.After;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;

/**
 * {@link ParameterController} Unit Test.
 * 
 * @author madmath03
 */
public class ParameterControllerTest
    extends AbstractGenericControllerTest<Parameter, ParameterDto> {

  private static final UUID ID = UUID.randomUUID();
  private static final String NAME = "Foo";
  private static final Object VALUE = 42;

  /**
   * @throws java.lang.Exception if the test cleanup crashes.
   */
  @After
  public void tearDown() throws Exception {
    Mockito.reset(getMockService());
    this.setBridge(null);
  }

  @Override
  protected IParameterService getMockService() {
    return (IParameterService) super.getMockService();
  }

  @Override
  protected IParameterService buildTestService() {
    return mock(IParameterService.class);
  }

  @Override
  protected Authentication buildMockAuthentication() {
    return mock(Authentication.class);
  }

  @Override
  protected ParameterController buildTestController() {
    return new ParameterController(getMockService());
  }

  @Override
  protected ParameterBridge buildTestBridge() {
    return new ParameterBridge();
  }

  @Override
  protected Parameter buildTestEntity() {
    return Parameter.builder(NAME, VALUE).id(ID).build();
  }

  @Override
  protected Class<Parameter> getEntityClass() {
    return Parameter.class;
  }

  @SuppressWarnings("unchecked")
  @Override
  protected ParameterNotFoundException buildTestEntityNotFound() {
    return new ParameterNotFoundException();
  }

  /**
   * Test method for {@link ParameterController#ParameterController(IParameterService)}.
   */
  @Test
  public void testParameterController() {
    assertNotNull(getController());
  }

}
