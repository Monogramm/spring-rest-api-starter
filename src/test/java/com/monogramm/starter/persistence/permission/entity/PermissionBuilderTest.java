/*
 * Creation by madmath03 the 2017-11-25
 */

package com.monogramm.starter.persistence.permission.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.monogramm.starter.persistence.AbstractGenericEntityBuilderTest;
import com.monogramm.starter.persistence.permission.entity.Permission;
import com.monogramm.starter.persistence.permission.entity.Permission.PermissionBuilder;

import org.junit.Test;

/**
 * {@link PermissionBuilder} Unit Test.
 * 
 * @author madmath03
 */
public class PermissionBuilderTest
    extends AbstractGenericEntityBuilderTest<Permission.PermissionBuilder> {

  private static final String DISPLAYNAME = "TEST";

  @Override
  protected PermissionBuilder buildTestEntityBuilder() {
    return Permission.builder();
  }

  /**
   * Test method for {@link Permission#builder()}.
   */
  @Test
  public void testGetBuilder() {
    Permission.PermissionBuilder builder = Permission.builder();

    assertNotNull(builder);

    final Permission permissionBuilt = builder.build();
    assertNotNull(permissionBuilt);
    assertNull(permissionBuilt.getName());
  }

  /**
   * Test method for {@link Permission#builder(java.lang.String)}.
   */
  @Test
  public void testGetBuilderString() {
    Permission.PermissionBuilder builder = Permission.builder(DISPLAYNAME);

    assertNotNull(builder);

    final Permission permissionBuilt = builder.build();
    assertNotNull(permissionBuilt);
    assertEquals(DISPLAYNAME, permissionBuilt.getName());
  }

  /**
   * Test method for {@link Permission.PermissionBuilder#name(java.lang.String)}.
   */
  @Test
  public void testName() {
    assertEquals(this.getEntityBuilder(), this.getEntityBuilder().name(null));

    final String name = "TEST";
    assertEquals(name,
        this.getEntityBuilder().name(name).build().getName());
  }


}
