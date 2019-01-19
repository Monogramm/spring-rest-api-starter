/*
 * Creation by madmath03 the 2017-08-27.
 */

package com.monogramm.starter.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jayway.awaitility.Awaitility;
import com.monogramm.starter.persistence.user.entity.User;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link AbstractGenericEntity} Unit Test.
 * 
 * @author madmath03
 */
public abstract class AbstractGenericEntityTest<T extends AbstractGenericEntity> {

  /**
   * Logger for {@link AbstractGenericEntityTest}.
   */
  protected static final Logger LOG = LoggerFactory.getLogger(AbstractGenericEntityTest.class);

  /**
   * The tested entity.
   */
  private T entity;

  /**
   * Build an empty entity to test.
   * 
   * @return an entity for tests.
   */
  abstract protected T buildTestEntity();

  /**
   * Pause the current thread until the condition is true or the given amount of time is reached.
   * 
   * @param timeout the timeout
   * @param unit the unit
   * @param condition the condition that is responsible for executing the assertion and throwing
   *        AssertionError on failure.
   */
  protected final void pause(final long timeout, final TimeUnit unit, final Runnable condition) {
    Awaitility.await().atMost(timeout, unit).until(condition);
  }

  /**
   * @throws java.lang.Exception if the test setup crashes.
   */
  @Before
  public void setUp() throws Exception {
    this.entity = this.buildTestEntity();

    assertNotNull("The tested entity cannot be null!", entity);
  }

  /**
   * @throws java.lang.Exception if the test cleanup crashes.
   */
  @After
  public void tearDown() throws Exception {
    this.entity = null;
  }

  /**
   * Get the {@link #entity} currently tested.
   * 
   * @return the test {@link #entity}.
   */
  protected final T getEntity() {
    return entity;
  }

  /**
   * Test method for {@link AbstractGenericEntity#getId()}.
   */
  @Test
  public void testGetId() {
    assertNull(entity.getId());
  }

  /**
   * Test method for {@link AbstractGenericEntity#setId(java.util.UUID)}.
   */
  @Test
  public void testSetId() {
    final UUID id = UUID.randomUUID();
    entity.setId(id);
    assertEquals(id, entity.getId());
  }

  /**
   * Test method for {@link AbstractGenericEntity#getCreatedAt()}.
   */
  @Test
  public void testGetCreatedAt() {
    assertNull(entity.getCreatedAt());
  }

  /**
   * Test method for {@link AbstractGenericEntity#setCreatedAt(java.util.Date)}.
   */
  @Test
  public void testSetCreatedAt() {
    final Date date = new Date();
    entity.setCreatedAt(date);
    assertEquals(date, entity.getCreatedAt());
  }

  /**
   * Test method for {@link AbstractGenericEntity#getCreatedBy()}.
   */
  @Test
  public void testGetCreatedBy() {
    assertNull(entity.getCreatedBy());
  }

  /**
   * Test method for {@link AbstractGenericEntity#setCreatedBy(java.util.UUID)}.
   */
  @Test
  public final void testSetCreatedBy() {
    final User user = new User("testUser");
    entity.setCreatedBy(user);
    assertEquals(user, entity.getCreatedBy());
  }

  /**
   * Test method for {@link AbstractGenericEntity#getModifiedAt()}.
   */
  @Test
  public void testGetModifiedAt() {
    assertNull(entity.getModifiedAt());
  }

  /**
   * Test method for {@link AbstractGenericEntity#setModifiedAt(java.util.Date)}.
   */
  @Test
  public void testSetModifiedAt() {
    final Date date = new Date();
    entity.setModifiedAt(date);
    assertEquals(date, entity.getModifiedAt());
  }

  /**
   * Test method for {@link AbstractGenericEntity#getModifiedBy()}.
   */
  @Test
  public void testGetModifiedBy() {
    assertNull(entity.getModifiedBy());
  }

  /**
   * Test method for {@link AbstractGenericEntity#setModifiedBy(java.util.UUID)}.
   */
  @Test
  public void testSetModifiedBy() {
    final User user = new User("testUser");
    entity.setModifiedBy(user);
    assertEquals(user, entity.getModifiedBy());
  }

  /**
   * Test method for {@link AbstractGenericEntity#getOwner()}.
   */
  @Test
  public void testGetOwner() {
    assertNull(entity.getOwner());
  }

  /**
   * Test method for {@link AbstractGenericEntity#setOwner(java.util.UUID)}.
   */
  @Test
  public void testSetOwner() {
    final User user = new User("testUser");
    entity.setOwner(user);
    assertEquals(user, entity.getOwner());
  }

  /**
   * Test method for {@link AbstractGenericEntity#update(AbstractGenericEntity))}.
   */
  @Test
  public void testUpdate() {
    assertNull(entity.getOwner());

    final T anotherEntity = this.buildTestEntity();
    entity.update(anotherEntity);
    assertNull(entity.getOwner());

    final User owner = new User();
    anotherEntity.setOwner(owner);
    entity.update(anotherEntity);
    assertEquals(owner, entity.getOwner());
  }

  /**
   * Test method for {@link AbstractGenericEntity#update(AbstractGenericEntity))}.
   */
  @Test
  public void testUpdateDummyEntity() {
    final AbstractGenericEntity dummy = new AbstractGenericEntity() {

      /**
       * The {@code serialVersionUID}.
       */
      private static final long serialVersionUID = 1L;

    };

    assertNull(entity.getOwner());

    entity.update(dummy);

    assertNull(entity.getOwner());

    final User owner = new User();
    dummy.setOwner(owner);
    entity.update(dummy);
    assertEquals(owner, entity.getOwner());
  }

  /**
   * Test method for {@link AbstractGenericEntity#update(AbstractGenericEntity))}.
   */
  @Test(expected = NullPointerException.class)
  public void testUpdateNull() {
    entity.update(null);
  }

  /**
   * Test method for {@link AbstractGenericEntity#hashCode()}.
   */
  @Test
  public void testHashCode() {
    assertEquals(entity.hashCode(), entity.hashCode());

    final T anotherEntity = this.buildTestEntity();
    assertEquals(anotherEntity.hashCode(), entity.hashCode());


    entity.setId(UUID.randomUUID());
    assertEquals(entity.hashCode(), entity.hashCode());
    assertNotEquals(anotherEntity.hashCode(), entity.hashCode());

    anotherEntity.setId(UUID.randomUUID());
    assertNotEquals(anotherEntity.hashCode(), entity.hashCode());

    anotherEntity.setId(entity.getId());
    assertEquals(anotherEntity.hashCode(), entity.hashCode());
  }

  /**
   * Test method for {@link AbstractGenericEntity#equals(java.lang.Object)}.
   */
  @Test
  public void testEqualsObject() {
    assertEquals(entity, entity);
    assertNotEquals(entity, null);
    assertNotEquals(entity, new Object());

    final T anotherEntity = this.buildTestEntity();

    assertEquals(entity, anotherEntity);
    assertEquals(anotherEntity, entity);

    entity.preInsert();

    assertNotEquals(entity, anotherEntity);
    assertNotEquals(anotherEntity, entity);

    anotherEntity.preInsert();

    assertNotEquals(entity, anotherEntity);
    assertNotEquals(anotherEntity, entity);
  }

  /**
   * Test method for {@link AbstractGenericEntity#equals(java.lang.Object)}.
   */
  @Test
  public void testEqualsObjectDummy() {
    assertEquals(entity, entity);
    assertNotEquals(entity, null);
    assertNotEquals(entity, new Object());

    final AbstractGenericEntity dummy = new AbstractGenericEntity() {

      /**
       * The {@code serialVersionUID}.
       */
      private static final long serialVersionUID = 1L;

    };

    assertEquals(entity, dummy);
    assertEquals(dummy, entity);

    entity.preInsert();

    assertNotEquals(entity, dummy);
    assertNotEquals(dummy, entity);

    dummy.preInsert();

    assertNotEquals(entity, dummy);
    assertNotEquals(dummy, entity);
  }

  /**
   * Test method for {@link AbstractGenericEntity#toJson()}.
   */
  @Test
  public void testToJson() throws JsonProcessingException {
    assertNotNull(entity.toJson());
    LOG.debug(entity.toJson());

    final User testUser = User.builder(this.getClass().toString()).id(UUID.randomUUID()).build();
    testUser.setOwner(testUser);
    entity.setCreatedAt(new Date());
    entity.setCreatedBy(testUser);
    entity.setModifiedAt(new Date());
    entity.setModifiedBy(testUser);
    entity.setOwner(testUser);
    assertNotNull(entity.toJson());
    LOG.debug(entity.toJson());
  }

  /**
   * Test method for {@link AbstractGenericEntity#toString()}.
   */
  @Test
  public void testToString() {
    assertNotNull(entity.toString());
    LOG.debug(entity.toString());

    final User testUser = new User(this.getClass().toString());
    testUser.setOwner(testUser);
    entity.setCreatedAt(new Date());
    entity.setCreatedBy(testUser);
    entity.setModifiedAt(new Date());
    entity.setModifiedBy(testUser);
    entity.setOwner(testUser);
    assertNotNull(entity.toString());
    LOG.debug(entity.toString());
  }

  /**
   * Test method for {@link AbstractGenericEntity#preInsert()}.
   */
  @Test
  public void testPreInsert() {
    entity.preInsert();

    this.pause(5, TimeUnit.SECONDS, () -> assertNotNull(entity.getId()));

    assertNotNull(entity.getCreatedAt());
    assertNull(entity.getCreatedBy());
    assertNull(entity.getModifiedAt());
    assertNull(entity.getModifiedBy());
    assertNull(entity.getOwner());

    UUID id = entity.getId();
    Date createdAt = entity.getCreatedAt();
    entity.preInsert();

    this.pause(5, TimeUnit.SECONDS, () -> assertNotNull(entity.getCreatedAt()));

    assertEquals(id, entity.getId());
    assertNotNull(entity.getCreatedAt());
    assertEquals(createdAt, entity.getCreatedAt());
    assertNull(entity.getCreatedBy());
    assertNull(entity.getModifiedAt());
    assertNull(entity.getModifiedBy());
    assertNull(entity.getOwner());
  }

  /**
   * Test method for {@link AbstractGenericEntity#preUpdate()}.
   */
  @Test
  public void testPreUpdate() {
    entity.preInsert();

    this.pause(5, TimeUnit.SECONDS, () -> assertNotNull(entity.getId()));

    // Retrieve values after insert
    UUID id = entity.getId();
    final Date creationDate = entity.getCreatedAt();

    entity.preUpdate();

    this.pause(5, TimeUnit.SECONDS, () -> assertNotNull(entity.getModifiedAt()));

    // Check pre-update did not change creation fields and modification were set
    assertEquals(id, entity.getId());
    assertNotNull(entity.getCreatedAt());
    assertNull(entity.getCreatedBy());
    assertNotNull(entity.getModifiedAt());
    assertNull(entity.getModifiedBy());
    assertNull(entity.getOwner());


    // Retrieve values after update
    final Date modificationDate = entity.getModifiedAt();
    entity.preUpdate();

    this.pause(5, TimeUnit.SECONDS,
        () -> assertNotEquals(modificationDate, entity.getModifiedAt()));

    // Check pre-update did not change creation fields and modification was updated
    assertEquals(id, entity.getId());
    assertEquals(creationDate, entity.getCreatedAt());
    assertNull(entity.getCreatedBy());
    assertNotEquals(modificationDate, entity.getModifiedAt());
    assertNull(entity.getModifiedBy());
    assertNull(entity.getOwner());
  }

}
