/*
 * Creation by madmath03 the 2017-08-27.
 */

package com.monogramm.starter.persistence.role.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.monogramm.starter.persistence.AbstractGenericEntityBuilderTest;
import com.monogramm.starter.persistence.permission.entity.Permission;
import com.monogramm.starter.persistence.role.entity.Role.RoleBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.Test;

/**
 * {@link RoleBuilder} Unit Test.
 * 
 * @author madmath03
 */
public class RoleBuilderTest extends AbstractGenericEntityBuilderTest<Role.RoleBuilder> {

  private static final String DISPLAYNAME = "TEST";

  @Override
  protected RoleBuilder buildTestEntityBuilder() {
    return Role.builder();
  }

  /**
   * Test method for {@link Role#builder()}.
   */
  @Test
  public void testGetBuilder() {
    Role.RoleBuilder builder = Role.builder();

    assertNotNull(builder);

    final Role roleBuilt = builder.build();
    assertNotNull(roleBuilt);
    assertNull(roleBuilt.getName());
  }

  /**
   * Test method for {@link Role#builder(java.lang.String)}.
   */
  @Test
  public void testGetBuilderString() {
    Role.RoleBuilder builder = Role.builder(DISPLAYNAME);

    assertNotNull(builder);

    final Role roleBuilt = builder.build();
    assertNotNull(roleBuilt);
    assertEquals(DISPLAYNAME, roleBuilt.getName());
  }

  /**
   * Test method for {@link Role.RoleBuilder#name(java.lang.String)}.
   */
  @Test
  public void testName() {
    assertEquals(this.getEntityBuilder(), this.getEntityBuilder().name(null));

    final String name = "TEST";
    assertEquals(name,
        this.getEntityBuilder().name(name).build().getName());
  }

  /**
   * Test method for {@link Role.RoleBuilder#permissions(java.util.Collection)}.
   */
  @Test
  public void testPermissions() {
    assertEquals(this.getEntityBuilder(),
        this.getEntityBuilder().permissions(Collections.emptyList()));

    final List<Permission> permissions = new ArrayList<>(2);
    for (int i = 0, n = permissions.size(); i < n; i++) {
      permissions.add(Permission.builder().id(UUID.randomUUID()).build());
    }

    final Set<Permission> entityPermissions =
        this.getEntityBuilder().permissions(permissions).build().getPermissions();
    assertEquals(permissions.size(), entityPermissions.size());

    final Permission[] permissionsArray = entityPermissions.toArray(new Permission[] {});
    for (int i = 0, n = permissions.size(); i < n; i++) {
      assertEquals(permissions.get(i), permissionsArray[i]);
    }
  }

}
