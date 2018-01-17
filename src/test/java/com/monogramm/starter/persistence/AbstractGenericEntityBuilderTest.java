/*
 * Creation by madmath03 the 2017-08-27.
 */

package com.monogramm.starter.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.monogramm.starter.persistence.AbstractGenericEntity;
import com.monogramm.starter.persistence.user.entity.User;

import java.util.Date;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * {@link AbstractGenericEntityBuilder} Unit Test.
 * 
 * @author madmath03
 */
public abstract class AbstractGenericEntityBuilderTest<
    T extends AbstractGenericEntity.Builder<? extends AbstractGenericEntity>> {

  /**
   * The tested entity builder.
   */
  private T entityBuilder = this.buildTestEntityBuilder();

  /**
   * Build an entity builder to test.
   * 
   * @return an entity builder for tests.
   */
  abstract protected T buildTestEntityBuilder();

  /**
   * @throws java.lang.Exception If test initialization crashes.
   */
  @Before
  public void setUp() throws Exception {
    this.entityBuilder = this.buildTestEntityBuilder();

    assertNotNull("The tested entity builder cannot be null!", entityBuilder);
  }

  /**
   * @throws java.lang.Exception If test clean up crashes.
   */
  @After
  public void tearDown() throws Exception {
    this.entityBuilder = null;
  }

  /**
   * Get the {@link #entityBuilder}.
   * 
   * @return the {@link #entityBuilder}.
   */
  protected final T getEntityBuilder() {
    return entityBuilder;
  }

  /**
   * Test method for {@link AbstractGenericEntity.Builder#buildEntity()}.
   */
  @Test
  public void testBuildEntity() {
    assertNotNull(entityBuilder.buildEntity());
  }

  /**
   * Test method for {@link AbstractGenericEntity.Builder#getEntity()}.
   */
  @Test
  public void testGetEntity() {
    assertNotNull(entityBuilder.getEntity());
  }

  /**
   * Test method for {@link AbstractGenericEntity.Builder#build()}.
   */
  @Test
  public void testBuild() {
    assertNotNull(entityBuilder.build());

    assertEquals(entityBuilder.getEntity(), entityBuilder.build());
  }

  /**
   * Test method for {@link AbstractGenericEntity.Builder#id(java.util.UUID)}.
   */
  @Test
  public void testId() {
    assertEquals(entityBuilder, entityBuilder.id(null));

    final UUID id = UUID.randomUUID();
    assertEquals(id, entityBuilder.id(id).build().getId());
  }

  /**
   * Test method for {@link AbstractGenericEntity.Builder#createdAt(java.util.Date)}.
   */
  @Test
  public void testCreatedAt() {
    assertEquals(entityBuilder, entityBuilder.createdAt(null));

    final Date date = new Date();
    assertEquals(date, entityBuilder.createdAt(date).build().getCreatedAt());
  }

  /**
   * Test method for {@link AbstractGenericEntity.Builder#createdBy(java.util.UUID)}.
   */
  @Test
  public void testCreatedBy() {
    assertEquals(entityBuilder, entityBuilder.createdBy(null));

    final User user = new User("testUser");
    assertEquals(user, entityBuilder.createdBy(user).build().getCreatedBy());
  }

  /**
   * Test method for {@link AbstractGenericEntity.Builder#modifiedAt(java.util.Date)}.
   */
  @Test
  public void testModifiedAt() {
    assertEquals(entityBuilder, entityBuilder.modifiedAt(null));

    final Date date = new Date();
    assertEquals(date, entityBuilder.modifiedAt(date).build().getModifiedAt());
  }

  /**
   * Test method for {@link AbstractGenericEntity.Builder#modifiedBy(java.util.UUID)}.
   */
  @Test
  public void testModifiedBy() {
    assertEquals(entityBuilder, entityBuilder.modifiedBy(null));

    final User user = new User("testUser");
    assertEquals(user, entityBuilder.modifiedBy(user).build().getModifiedBy());
  }

  /**
   * Test method for {@link AbstractGenericEntity.Builder#owner(java.util.UUID)}.
   */
  @Test
  public void testOwner() {
    assertEquals(entityBuilder, entityBuilder.owner(null));

    final User user = new User("testUser");
    assertEquals(user, entityBuilder.owner(user).build().getOwner());
  }

}
