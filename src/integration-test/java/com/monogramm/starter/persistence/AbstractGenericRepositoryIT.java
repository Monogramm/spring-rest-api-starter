/*
 * Creation by madmath03 the 2018-01-07.
 */

package com.monogramm.starter.persistence;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import com.jayway.awaitility.Awaitility;
import com.monogramm.starter.persistence.user.dao.IUserRepository;
import com.monogramm.starter.persistence.user.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

/**
 * Abstract {@link GenericRepository} Integration Test.
 * 
 * @author madmath03
 */
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class})
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@DirtiesContext
@Transactional
@ActiveProfiles(profiles = {"it"})
public abstract class AbstractGenericRepositoryIT<T extends AbstractGenericEntity,
    R extends GenericRepository<T>> {

  protected static final UUID RANDOM_ID = UUID.randomUUID();

  protected static final String OWNER_USERNAME = "owner";
  protected static final String OWNER_EMAIL = "owner@email.com";

  protected User owner;

  @Autowired
  private IUserRepository userRepository;

  @Autowired
  private R repository;

  @PersistenceContext
  private EntityManager entityManager;

  @Before
  public void setUp() {
    this.owner = User.builder(OWNER_USERNAME, OWNER_EMAIL).build();
    userRepository.add(this.owner);
  }

  @After
  public void tearDown() {
    userRepository.delete(this.owner);
  }

  /**
   * Get the {@link #repository}.
   * 
   * @return the {@link #repository}.
   */
  protected R getRepository() {
    return repository;
  }

  protected abstract T buildTestEntity();

  /**
   * Test method for {@link AbstractGenericRepositoryIT#buildTestEntity()}.
   */
  @Test
  public void testBuildTestEntity() {
    assertNotNull(this.buildTestEntity());
  }

  /**
   * Test method for {@link GenericRepository#findAll()}.
   */
  @Test
  public abstract void testFindAll();

  /**
   * Test method for {@link GenericRepository#findById(java.util.UUID)}.
   */
  @Test
  public void testFindById() {
    final T model = this.buildTestEntity();
    repository.add(model);

    final T actual = repository.findById(model.getId());

    assertThat(actual, is(model));
  }

  /**
   * Test method for {@link GenericRepository#add(AbstractGenericEntity)}.
   */
  @Test
  public void testAdd() {
    final T model = this.buildTestEntity();

    repository.add(model);

    assertNotNull(model.getId());
    assertNotNull(model.getCreatedAt());
    assertNull(model.getModifiedAt());
  }

  /**
   * Test method for {@link GenericRepository#update(AbstractGenericEntity)}.
   */
  @Test
  public void testUpdate() {
    final T model = this.buildTestEntity();
    repository.add(model);

    assertNotNull(model.getId());
    assertNotNull(model.getCreatedAt());
    assertNull(model.getModifiedAt());



    // FIXME How to properly test PreUpdate behavior?!
    // Wait...
    Awaitility.await().atMost(2, TimeUnit.SECONDS);

    // Detach models forcefully
    this.entityManager.clear();

    // Wait...
    Awaitility.await().atMost(2, TimeUnit.SECONDS);



    final T actual = repository.update(model);

    assertNotNull(actual);
    assertNotNull(actual.getId());
    assertEquals(model.getId(), actual.getId());
    assertNotNull(actual.getCreatedAt());
    // FIXME why is there a gap ??
    assertTrue(Math.abs(model.getCreatedAt().getTime() - actual.getCreatedAt().getTime()) < 1_000L);
    // FIXME How to properly test this behavior?!
    // assertNotNull(actual.getModifiedAt());
    // assertTrue(actual.getCreatedAt().before(actual.getModifiedAt()));
  }

  /**
   * Test method for {@link GenericRepository#update(AbstractGenericEntity)}.
   */
  @Test
  public void testUpdateNotFound() {
    final T model = this.buildTestEntity();

    final T updatedModel = repository.update(model);

    assertNull(updatedModel);
  }

  /**
   * Test method for
   * {@link GenericRepository#updateByOwner(AbstractGenericEntity, com.monogramm.starter.persistence.user.entity.User)}.
   */
  @Test
  public void testUpdateByOwner() {
    final T model = this.buildTestEntity();
    model.setOwner(owner);
    repository.add(model);

    assertNotNull(model.getId());
    assertNotNull(model.getCreatedAt());
    assertNull(model.getModifiedAt());
    assertEquals(owner, model.getOwner());



    // FIXME How to properly test PreUpdate behavior?!
    // Wait...
    Awaitility.await().atMost(2, TimeUnit.SECONDS);

    // Detach models forcefully
    this.entityManager.clear();

    // Wait...
    Awaitility.await().atMost(2, TimeUnit.SECONDS);



    final T actual = repository.updateByOwner(model, owner);

    assertNotNull(actual);
    assertNotNull(actual.getId());
    assertEquals(model.getId(), actual.getId());
    assertNotNull(actual.getCreatedAt());
    // FIXME why is there a gap ??
    assertTrue(Math.abs(model.getCreatedAt().getTime() - actual.getCreatedAt().getTime()) < 1_000L);
    // FIXME How to properly test this behavior?!
    // assertNotNull(actual.getModifiedAt());
    // assertTrue(actual.getCreatedAt().before(actual.getModifiedAt()));
  }

  /**
   * Test method for {@link GenericRepository#updateByOwner(AbstractGenericEntity, User)}.
   */
  @Test
  public void testUpdateByOwnerNotFound() {
    final T model = this.buildTestEntity();

    final T updatedModel = repository.updateByOwner(model, null);

    assertNull(updatedModel);
  }
  
  

  /**
   * Test method for {@link GenericRepository#deleteById(java.util.UUID)}.
   */
  @Test
  public void testDeleteById() {
    final T model = this.buildTestEntity();
    repository.add(model);

    final Integer deleted = repository.deleteById(model.getId());

    assertEquals(Integer.valueOf(1), deleted);
  }

  /**
   * Test method for {@link GenericRepository#deleteById(java.util.UUID)}.
   */
  @Test
  public void testDeleteByIdNotFound() {
    assertEquals(Integer.valueOf(0), repository.deleteById(RANDOM_ID));
  }

  
  /**
   * Test method for {@link GenericRepository#deleteById(java.util.UUID)}.
   */
  @Test
  public void testDeleteByIdAndOwner() {
    final T model = this.buildTestEntity();
    model.setOwner(owner);
    repository.add(model);

    final Integer deleted = repository.deleteByIdAndOwner(model.getId(), owner);

    assertEquals(Integer.valueOf(1), deleted);
  }

  /**
   * Test method for {@link GenericRepository#deleteById(java.util.UUID)}.
   */
  @Test
  public void testDeleteByIdAndOwnerNotFound() {
    assertEquals(Integer.valueOf(0), repository.deleteByIdAndOwner(RANDOM_ID, null));
  }
  
  

  /**
   * Test method for {@link GenericRepository#exists(java.util.UUID)}.
   */
  @Test
  public void testExistsUuid() {
    final boolean expected = true;
    final T model = this.buildTestEntity();
    final List<T> models = new ArrayList<>(1);
    models.add(model);
    repository.save(models);

    final boolean actual = repository.exists(model.getId());

    assertThat(actual, is(expected));
  }

  /**
   * Test method for {@link GenericRepository#exists(java.util.UUID)}.
   */
  @Test
  public void testExistsUuidNotFound() {
    final boolean expected = false;

    final boolean actual = repository.exists(RANDOM_ID);

    assertThat(actual, is(expected));
  }

}
