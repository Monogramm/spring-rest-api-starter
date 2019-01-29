/*
 * Creation by madmath03 the 2017-09-11.
 */

package com.monogramm.starter.persistence.type.dao;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import com.monogramm.starter.config.data.InitialDataLoader;
import com.monogramm.starter.persistence.AbstractGenericRepositoryIT;
import com.monogramm.starter.persistence.type.entity.Type;
import com.monogramm.starter.persistence.type.exception.TypeNotFoundException;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * {@link TypeRepository} Integration Test.
 * 
 * @author madmath03
 */
public class TypeRepositoryIT extends AbstractGenericRepositoryIT<Type, TypeRepository> {

  private static final String DISPLAYNAME = TypeRepositoryIT.class.getSimpleName();

  @Autowired
  private InitialDataLoader initialDataLoader;

  @Override
  protected Type buildTestEntity() {
    return Type.builder(DISPLAYNAME).build();
  }

  /**
   * Test method for {@link TypeRepository#findAll()}.
   */
  @Override
  @Test
  public void testFindAll() {
    int expectedSize = 0;
    // ...plus the permissions created at application initialization
    if (initialDataLoader.getTypes() != null) {
      expectedSize += initialDataLoader.getTypes().size();
    }

    final List<Type> actual = getRepository().findAll();

    assertNotNull(actual);
    assertEquals(expectedSize, actual.size());
  }

  /**
   * Test method for
   * {@link TypeRepository#findAllContainingNameIgnoreCase(java.lang.String)}.
   */
  @Test
  public void testFindAllContainingNameIgnoreCase() {
    final List<Type> models = new ArrayList<>();

    final List<Type> actual = getRepository().findAllContainingNameIgnoreCase(DISPLAYNAME);

    assertThat(actual, is(models));
  }

  /**
   * Test method for {@link TypeRepository#findByNameIgnoreCase(String)}.
   * 
   * @throws TypeNotFoundException if the type is not found.
   */
  @Test
  public void testFindByNameIgnoreCase() {
    final Type model = this.buildTestEntity();
    model.setName(model.getName().toUpperCase());
    addTestEntity(model);

    final Type actual = getRepository().findByNameIgnoreCase(DISPLAYNAME);

    assertThat(actual, is(model));
  }

  /**
   * Test method for {@link TypeRepository#findByNameIgnoreCase(java.lang.String)}.
   * 
   * @throws TypeNotFoundException if the type is not found.
   */
  @Test
  public void testFindByNameIgnoreCaseNoResult() {
    assertNull(getRepository().findByNameIgnoreCase(null));
  }

  /**
   * Test method for {@link TypeRepository#findByNameIgnoreCase(java.lang.String)}.
   * 
   * @throws TypeNotFoundException if the type is not found.
   */
  @Test
  public void testFindByNameIgnoreCaseNonUnique() {
    addTestEntity(Type.builder(DISPLAYNAME + "1").build());
    addTestEntity(Type.builder(DISPLAYNAME + "2").build());

    assertNull(getRepository().findByNameIgnoreCase(DISPLAYNAME));
  }

  /**
   * Test method for {@link TypeRepository#findByNameIgnoreCase(java.lang.String)}.
   * 
   * @throws TypeNotFoundException if the type is not found.
   */
  @Test
  public void testFindByNameIgnoreCaseNotFound() {
    assertNull(getRepository().findByNameIgnoreCase(null));
  }

  /**
   * Test method for {@link TypeRepository#exists(java.util.UUID, java.lang.String)}.
   */
  @Test
  public void testExistsUuidString() {
    final boolean expected = true;
    final Type model = this.buildTestEntity();
    final List<Type> models = new ArrayList<>(1);
    models.add(model);
    getRepository().save(models);

    final boolean actual = getRepository().exists(model.getId(), model.getName());

    assertThat(actual, is(expected));
  }

  /**
   * Test method for {@link TypeRepository#exists(java.util.UUID, java.lang.String)}.
   */
  @Test
  public void testExistsUuidStringNotFound() {
    final boolean expected = false;

    final boolean actual = getRepository().exists(RANDOM_ID, DISPLAYNAME);

    assertThat(actual, is(expected));
  }

}
