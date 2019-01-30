/*
 * Creation by madmath03 the 2017-12-03.
 */

package com.monogramm.starter.config.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import com.monogramm.starter.config.properties.DataProperties;
import com.monogramm.starter.persistence.parameter.service.ParameterService;
import com.monogramm.starter.persistence.permission.service.PermissionService;
import com.monogramm.starter.persistence.role.service.RoleService;
import com.monogramm.starter.persistence.type.service.TypeService;
import com.monogramm.starter.persistence.user.service.UserService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;

/**
 * {@link InitialDataLoader} Unit Test.
 * 
 * @author madmath03
 */
public class InitialDataLoaderTest {

  private InitialDataLoader loader;


  private DataProperties dataProperties;
  private MessageSource messageSource;


  private ParameterService parameterService;
  private TypeService typeService;
  private PermissionService permissionService;
  private RoleService roleService;
  private UserService userService;


  /**
   * @throws java.lang.Exception If test initialization crashes.
   */
  @Before
  public void setUp() throws Exception {
    this.dataProperties = new DataProperties();
    this.messageSource = mock(MessageSource.class);

    this.parameterService = mock(ParameterService.class);
    this.typeService = mock(TypeService.class);
    this.permissionService = mock(PermissionService.class);
    this.roleService = mock(RoleService.class);
    this.userService = mock(UserService.class);

    this.loader = new InitialDataLoader(dataProperties, messageSource, userService, roleService,
        permissionService, typeService, parameterService);
  }

  /**
   * @throws java.lang.Exception If test clean up crashes.
   */
  @After
  public void tearDown() throws Exception {
    this.dataProperties = null;
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
   * Test method for
   * {@link com.monogramm.starter.config.data.AbstractDataLoader#getDataProperties()}.
   */
  @Test
  public void testDataProperties() {
    assertNotNull(this.loader.getDataProperties());
    assertEquals(this.dataProperties, this.loader.getDataProperties());
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
    this.dataProperties.setDemo(false);

    this.loader.onApplicationEvent(null);

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
    this.dataProperties.setDemo(true);

    this.loader.onApplicationEvent(null);

    assertTrue(this.loader.isAlreadySetup());

    this.loader.onApplicationEvent(null);

    assertTrue(this.loader.isAlreadySetup());
  }

}
