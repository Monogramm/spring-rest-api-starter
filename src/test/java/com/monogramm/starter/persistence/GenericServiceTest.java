/*
 * Creation by madmath03 the 2017-11-19.
 */

package com.monogramm.starter.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.monogramm.starter.dto.AbstractGenericDto;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

/**
 * {@link GenericService} Unit Test.
 * 
 * @author madmath03
 */
public abstract class GenericServiceTest<E extends AbstractGenericEntity,
    D extends AbstractGenericDto, S extends GenericService<E, D>> {

  /**
   * Get the {@link #service}.
   * 
   * @return the {@link #service}.
   */
  protected abstract S getService();

  /**
   * Test method for {@link GenericService#toDto(AbstractGenericEntity)}.
   */
  @Test
  public void testToDtoAbstractGenericEntity() {
    final E entity = this.getService().getBridge().buildEntity();
    final D dto = this.getService().toDto(entity);

    assertNotNull(dto);
    assertEquals(this.getService().getBridge().toDto(entity), dto);
  }

  /**
   * Test method for {@link GenericService#toDto(AbstractGenericEntity)}.
   */
  @Test
  public void testToDtoCollection() {
    final Collection<E> entity =
        Collections.singletonList(this.getService().getBridge().buildEntity());
    final List<D> dto = this.getService().toDto(entity);

    assertNotNull(dto);
    assertEquals(this.getService().getBridge().toDto(entity), dto);
  }

  /**
   * Test method for {@link GenericService#toDto(AbstractGenericEntity)}.
   */
  @Test
  public void testToDtoCollectionNull() {
    final Collection<E> entity = null;
    final List<D> dto = this.getService().toDto(entity);

    assertNotNull(dto);
    assertEquals(this.getService().getBridge().toDto(entity), dto);
  }

  /**
   * Test method for {@link GenericService#toEntity(AbstractGenericDto)}.
   */
  @Test
  public void testToEntityAbstractGenericDto() {
    final D dto = this.getService().getBridge().buildDto();
    final E entity = this.getService().toEntity(dto);

    assertNotNull(entity);
    assertEquals(this.getService().getBridge().toEntity(dto), entity);
  }

  /**
   * Test method for {@link GenericService#toEntity(java.util.Collection)}.
   */
  @Test
  public void testToEntityCollection() {
    final Collection<D> dto = Collections.singletonList(this.getService().getBridge().buildDto());
    final List<E> entity = this.getService().toEntity(dto);

    assertNotNull(entity);
    assertEquals(this.getService().getBridge().toEntity(dto), entity);
  }

  /**
   * Test method for {@link GenericService#toEntity(java.util.Collection)}.
   */
  @Test
  public void testToEntityCollectionNull() {
    final Collection<D> dto = null;
    final List<E> entity = this.getService().toEntity(dto);

    assertNotNull(entity);
    assertEquals(this.getService().getBridge().toEntity(dto), entity);
  }

}
