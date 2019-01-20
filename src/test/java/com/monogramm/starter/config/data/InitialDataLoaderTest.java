/*
 * Creation by madmath03 the 2017-12-03.
 */

package com.monogramm.starter.config.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.monogramm.starter.persistence.parameter.service.IParameterService;
import com.monogramm.starter.persistence.permission.service.IPermissionService;
import com.monogramm.starter.persistence.role.service.IRoleService;
import com.monogramm.starter.persistence.type.service.ITypeService;
import com.monogramm.starter.persistence.user.service.IUserService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;

/**
 * {@link InitialDataLoader} Unit Test.
 * 
 * @author madmath03
 */
public class InitialDataLoaderTest {

  private InitialDataLoader loader;


  private Environment env;
  private MessageSource messageSource;


  private IParameterService parameterService;
  private ITypeService typeService;
  private IPermissionService permissionService;
  private IRoleService roleService;
  private IUserService userService;


  /**
   * @throws java.lang.Exception If test initialization crashes.
   */
  @Before
  public void setUp() throws Exception {
    this.env = mock(Environment.class);
    this.messageSource = mock(MessageSource.class);

    this.parameterService = mock(IParameterService.class);
    this.typeService = mock(ITypeService.class);
    this.permissionService = mock(IPermissionService.class);
    this.roleService = mock(IRoleService.class);
    this.userService = mock(IUserService.class);

    this.loader = new InitialDataLoader(env, messageSource, userService, roleService,
        permissionService, typeService, parameterService);
  }

  /**
   * @throws java.lang.Exception If test clean up crashes.
   */
  @After
  public void tearDown() throws Exception {
    Mockito.reset(env);
    Mockito.reset(messageSource);

    Mockito.reset(parameterService);
    Mockito.reset(typeService);
    Mockito.reset(permissionService);
    Mockito.reset(roleService);
    Mockito.reset(userService);

    this.loader = null;
  }

  /**
   * Test method for {@link com.monogramm.starter.config.data.AbstractDataLoader#isAlreadySetup()}.
   */
  @Test
  public void testIsAlreadySetup() {
    assertFalse(this.loader.isAlreadySetup());
  }

  /**
   * Test method for {@link com.monogramm.starter.config.data.AbstractDataLoader#getLocale()}.
   */
  @Test
  public void testGetLocale() {
    assertNotNull(this.loader.getLocale());
  }

  /**
   * Test method for {@link com.monogramm.starter.config.data.AbstractDataLoader#getEnv()}.
   */
  @Test
  public void testGetEnv() {
    assertNotNull(this.loader.getEnv());
    assertEquals(this.env, this.loader.getEnv());
  }

  /**
   * Test method for
   * {@link com.monogramm.starter.config.data.AbstractDataLoader#getMessageSource()}.
   */
  @Test
  public void testGetMessageSource() {
    assertNotNull(this.loader.getMessageSource());
    assertEquals(this.messageSource, this.loader.getMessageSource());
  }

  /**
   * Test method for {@link com.monogramm.starter.config.data.AbstractDataLoader#getUserService()}.
   */
  @Test
  public void testGetUserService() {
    assertNotNull(this.loader.getUserService());
    assertEquals(this.userService, this.loader.getUserService());
  }

  /**
   * Test method for {@link com.monogramm.starter.config.data.AbstractDataLoader#getRoleService()}.
   */
  @Test
  public void testGetRoleService() {
    assertNotNull(this.loader.getRoleService());
    assertEquals(this.roleService, this.loader.getRoleService());
  }

  /**
   * Test method for
   * {@link com.monogramm.starter.config.data.AbstractDataLoader#getPermissionService()}.
   */
  @Test
  public void testGetPermissionService() {
    assertNotNull(this.loader.getPermissionService());
    assertEquals(this.permissionService, this.loader.getPermissionService());
  }

  /**
   * Test method for {@link com.monogramm.starter.config.data.AbstractDataLoader#getTypeService()}.
   */
  @Test
  public void testGetTypeService() {
    assertNotNull(this.loader.getTypeService());
    assertEquals(this.typeService, this.loader.getTypeService());
  }

  /**
   * Test method for
   * {@link InitialDataLoader#onApplicationEvent(org.springframework.context.event.ContextRefreshedEvent)}.
   */
  @Test
  public void testOnApplicationEvent() {
    when(env.getProperty("application.data.demo")).thenReturn("false");

    this.loader.onApplicationEvent(null);

    verify(env, times(1)).getProperty("application.data.demo");
    verify(env, times(1)).getProperty("application.data.admin_password");
    verify(env, times(1)).getProperty("application.data.domain_name", "monogramm.io");
    verifyNoMoreInteractions(env);

    assertTrue(this.loader.isAlreadySetup());

    this.loader.onApplicationEvent(null);

    assertTrue(this.loader.isAlreadySetup());
  }

  /**
   * Test method for
   * {@link InitialDataLoader#onApplicationEvent(org.springframework.context.event.ContextRefreshedEvent)}.
   */
  @Test
  public void testOnApplicationEventWithDemoData() {
    when(env.getProperty("application.data.demo")).thenReturn("true");

    this.loader.onApplicationEvent(null);

    verify(env, times(1)).getProperty("application.data.demo");
    verify(env, times(1)).getProperty("application.data.admin_password");
    verify(env, times(1)).getProperty("application.data.domain_name", "monogramm.io");
    verifyNoMoreInteractions(env);

    assertTrue(this.loader.isAlreadySetup());

    this.loader.onApplicationEvent(null);

    assertTrue(this.loader.isAlreadySetup());
  }

}
