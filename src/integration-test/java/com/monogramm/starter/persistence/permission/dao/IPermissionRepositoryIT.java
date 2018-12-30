/*
 * Creation by madmath03 the 2017-11-25
 */

package com.monogramm.starter.persistence.permission.dao;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import com.monogramm.starter.config.data.InitialDataLoader;
import com.monogramm.starter.persistence.AbstractGenericRepositoryIT;
import com.monogramm.starter.persistence.permission.entity.Permission;
import com.monogramm.starter.persistence.permission.exception.PermissionNotFoundException;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * {@link IPermissionRepository} Integration Test.
 * 
 * @author madmath03
 */
public class IPermissionRepositoryIT
    extends AbstractGenericRepositoryIT<Permission, IPermissionRepository> {

  private static final String DISPLAYNAME = IPermissionRepositoryIT.class.getSimpleName();

  @Autowired
  private InitialDataLoader initialDataLoader;

  @Override
  protected Permission buildTestEntity() {
    return Permission.builder(DISPLAYNAME).build();
  }

  /**
   * Test method for {@link IPermissionRepository#findAll()}.
   */
  @Override
  @Test
  public void testFindAll() {
    int expectedSize = 0;
    // ...plus the permissions created at application initialization
    if (initialDataLoader.getPermissions() != null) {
      expectedSize += initialDataLoader.getPermissions().size();
    }

    final List<Permission> actual = getRepository().findAll();

    assertNotNull(actual);
    assertEquals(expectedSize, actual.size());
  }

  /**
   * Test method for
   * {@link IPermissionRepository#findAllContainingNameIgnoreCase(java.lang.String)}.
   */
  @Test
  public void testFindAllContainingNameIgnoreCase() {
    final List<Permission> models = new ArrayList<>();

    final List<Permission> actual =
        getRepository().findAllContainingNameIgnoreCase(DISPLAYNAME);

    assertThat(actual, is(models));
  }

  /**
   * Test method for {@link IPermissionRepository#findByNameIgnoreCase(String)}.
   * 
   * @throws PermissionNotFoundException if the permission is not found.
   */
  @Test
  public void testFindByNameIgnoreCase() {
    final Permission model = this.buildTestEntity();
    model.setName(model.getName().toUpperCase());
    getRepository().add(model);

    final Permission actual = getRepository().findByNameIgnoreCase(DISPLAYNAME);

    assertThat(actual, is(model));
  }

  /**
   * Test method for {@link IPermissionRepository#findByNameIgnoreCase(java.lang.String)}.
   * 
   * @throws PermissionNotFoundException if the permission is not found.
   */
  @Test
  public void testFindByNameIgnoreCaseNoResult() {
    assertNull(getRepository().findByNameIgnoreCase(null));
  }

  /**
   * Test method for {@link IPermissionRepository#findByNameIgnoreCase(java.lang.String)}.
   * 
   * @throws PermissionNotFoundException if the permission is not found.
   */
  @Test
  public void testFindByNameIgnoreCaseNonUnique() {
    getRepository().add(Permission.builder(DISPLAYNAME + "1").build());
    getRepository().add(Permission.builder(DISPLAYNAME + "2").build());

    assertNull(getRepository().findByNameIgnoreCase(DISPLAYNAME));
  }

  /**
   * Test method for {@link IPermissionRepository#findByNameIgnoreCase(java.lang.String)}.
   * 
   * @throws PermissionNotFoundException if the permission is not found.
   */
  @Test
  public void testFindByNameIgnoreCaseNotFound() {
    final Permission model = null;

    final Permission actual = getRepository().findByNameIgnoreCase(DISPLAYNAME);

    assertThat(actual, is(model));
  }

  /**
   * Test method for {@link IPermissionRepository#exists(java.util.UUID, java.lang.String)}.
   */
  @Test
  public void testExistsUuidString() {
    final boolean expected = true;
    final Permission model = this.buildTestEntity();
    final List<Permission> models = new ArrayList<>(1);
    models.add(model);
    getRepository().save(models);

    final boolean actual = getRepository().exists(RANDOM_ID, DISPLAYNAME);

    assertThat(actual, is(expected));
  }

  /**
   * Test method for {@link IPermissionRepository#exists(java.util.UUID, java.lang.String)}.
   */
  @Test
  public void testExistsUuidStringNotFound() {
    final boolean expected = false;

    final boolean actual = getRepository().exists(RANDOM_ID, DISPLAYNAME);

    assertThat(actual, is(expected));
  }

}
