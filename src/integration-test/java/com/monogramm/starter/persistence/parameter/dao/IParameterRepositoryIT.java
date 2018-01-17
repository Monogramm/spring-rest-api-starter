/*
 * Creation by madmath03 the 2018-01-09.
 */

package com.monogramm.starter.persistence.parameter.dao;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import com.monogramm.starter.config.data.InitialDataLoader;
import com.monogramm.starter.persistence.AbstractGenericRepositoryIT;
import com.monogramm.starter.persistence.parameter.entity.Parameter;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * {@link IParameterRepository} Integration Test.
 * 
 * @author madmath03
 */
public class IParameterRepositoryIT
    extends AbstractGenericRepositoryIT<Parameter, IParameterRepository> {

  protected static final String DUMMY_NAME = "Foo";
  protected static final Object DUMMY_VALUE = 42;

  @Autowired
  private InitialDataLoader initialDataLoader;

  @Override
  protected Parameter buildTestEntity() {
    return Parameter.builder(DUMMY_NAME, DUMMY_VALUE).build();
  }

  @Override
  public void testFindAll() {
    int expectedSize = 0;
    // ...plus the permissions created at application initialization
    if (initialDataLoader.getParameters() != null) {
      expectedSize += initialDataLoader.getParameters().size();
    }

    final List<Parameter> actual = getRepository().findAll();

    assertNotNull(actual);
    assertEquals(expectedSize, actual.size());
  }

  /**
   * Test method for {@link IParameterRepository#findByNameIgnoreCase(java.lang.String)}.
   */
  @Test
  public void testFindByNameIgnoreCase() {
    final Parameter model = this.buildTestEntity();
    model.setName(model.getName().toUpperCase());
    getRepository().add(model);

    final Parameter actual = getRepository().findByNameIgnoreCase(DUMMY_NAME);

    assertThat(actual, is(model));
  }

  /**
   * Test method for {@link IParameterRepository#exists(java.util.UUID, java.lang.String)}.
   */
  @Test
  public void testExistsUUIDString() {
    final boolean expected = true;
    final Parameter model = this.buildTestEntity();
    final List<Parameter> models = new ArrayList<>(1);
    models.add(model);
    getRepository().save(models);

    final boolean actual = getRepository().exists(model.getId(), model.getName());

    assertThat(actual, is(expected));
  }

  /**
   * Test method for {@link IParameterRepository#exists(java.util.UUID, java.lang.String)}.
   */
  @Test
  public void testExistsUUIDStringNotFound() {
    final boolean expected = false;

    final boolean actual = getRepository().exists(RANDOM_ID, DUMMY_NAME);

    assertThat(actual, is(expected));
  }

}
