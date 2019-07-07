/*
 * Creation by madmath03 the 2017-12-20.
 */

package com.monogramm.starter.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import com.monogramm.starter.dto.AbstractTokenDto;
import com.monogramm.starter.persistence.user.dao.UserRepository;
import com.monogramm.starter.persistence.user.entity.User;

import java.util.Date;
import java.util.UUID;

import org.junit.Test;

/**
 * {@link AbstractTokenBridge} Unit Test.
 * 
 * @author madmath03
 */
public abstract class AbstractTokenBridgeTest<E extends AbstractToken, D extends AbstractTokenDto,
    B extends AbstractTokenBridge<E, D>> extends AbstractGenericBridgeTest<E, D, B> {

  /**
   * Build an empty bridge.
   * 
   * @return an DTO for tests.
   */
  abstract protected B buildTestBridge(UserRepository userRepository);

  /**
   * Test method for {@link AbstractTokenBridge#AbstractTokenBridge()}.
   */
  @Test
  public void testAbstractGenericTokenBridge() {
    final B tokenBridge = this.buildTestBridge();

    super.testAbstractGenericBridge(tokenBridge);
  }

  /**
   * Test method for {@link AbstractTokenBridge#AbstractTokenBridge(UserRepository)}.
   */
  @Test
  public void testAbstractGenericTokenBridgeIUserRepository() {
    final B tokenBridge = this.buildTestBridge(getUserRepository());

    super.testAbstractGenericBridgeIUserRepository(tokenBridge);
  }

  /**
   * Test method for {@link AbstractTokenBridge#AbstractTokenBridge(UserRepository)}.
   */
  @Test
  public void testAbstractGenericTokenBridgeNull() {
    final B tokenBridge = this.buildTestBridge(null);

    super.testAbstractGenericBridge(tokenBridge);
  }

  /**
   * Test method for {@link AbstractTokenBridge#buildEntity()}.
   */
  @Test
  public void testBuildEntity() {
    super.testBuildEntity();

    E entity = this.getBridge().buildEntity();

    assertNotNull(entity.getCode());
    assertNotNull(entity.getExpiryDate());
    assertNull(entity.getUser());
  }

  /**
   * Test method for {@link AbstractTokenBridge#buildDto()}.
   */
  @Test
  public void testBuildDto() {
    super.testBuildDto();

    D dto = this.getBridge().buildDto();

    assertNull(dto.getCode());
    assertNull(dto.getExpiryDate());
    assertNull(dto.getUser());
  }

  /**
   * Test method for {@link AbstractTokenBridge#toEntity(AbstractTokenDto)}.
   */
  @Test
  public void testToEntity() {
    final D dto = this.getBridge().buildDto();

    final String token = "TEST";
    dto.setCode(token);
    dto.setExpiryDate(new Date());
    dto.setUser(UUID.randomUUID());

    E entity = this.getBridge().toEntity(dto);

    assertEquals(entity.getCode(), dto.getCode());
    assertEquals(entity.getExpiryDate(), dto.getExpiryDate());
    assertEquals(entity.getUser().getId(), dto.getUser());
  }

  /**
   * Test method for {@link AbstractTokenBridge#toEntity(AbstractTokenDto)}.
   */
  @Test
  public void testToEntityUserRepository() {
    final D dto = this.buildTestBridge(getUserRepository()).buildDto();

    final String token = "TEST";
    dto.setCode(token);
    dto.setExpiryDate(new Date());
    dto.setUser(UUID.randomUUID());

    when(getUserRepository().findById(dto.getUser()))
        .thenReturn(User.builder().id(dto.getUser()).build());

    E entity = this.buildTestBridge(getUserRepository()).toEntity(dto);

    assertEquals(entity.getCode(), dto.getCode());
    assertEquals(entity.getExpiryDate(), dto.getExpiryDate());
    assertEquals(entity.getUser().getId(), dto.getUser());
  }

  /**
   * Test method for {@link AbstractTokenBridge#toEntity(AbstractTokenDto)}.
   */
  @Test
  public void testToEntityNullUserRepository() {
    final D dto = this.buildTestBridge(null).buildDto();

    final String token = "TEST";
    dto.setCode(token);
    dto.setExpiryDate(new Date());
    dto.setUser(UUID.randomUUID());

    E entity = this.buildTestBridge(null).toEntity(dto);

    assertEquals(entity.getCode(), dto.getCode());
    assertEquals(entity.getExpiryDate(), dto.getExpiryDate());
    assertEquals(entity.getUser().getId(), dto.getUser());
  }

  /**
   * Test method for {@link AbstractTokenBridge#toDto(AbstractToken)}.
   */
  @Test
  public void testToDto() {
    final E entity = this.getBridge().buildEntity();

    final String token = "TEST";
    entity.setCode(token);
    entity.setExpiryDate(new Date());
    entity.setUser(User.builder().id(UUID.randomUUID()).build());

    D dto = this.getBridge().toDto(entity);

    entity.setCreatedAt(new Date());
    entity.setCreatedBy(User.builder().id(UUID.randomUUID()).build());
    entity.setModifiedAt(new Date());
    entity.setModifiedBy(User.builder().id(UUID.randomUUID()).build());
    entity.setOwner(User.builder().id(UUID.randomUUID()).build());
    dto = this.getBridge().toDto(entity);

    assertEquals(dto.getCode(), entity.getCode());
    assertEquals(dto.getExpiryDate(), entity.getExpiryDate());
    assertEquals(dto.getUser(), entity.getUser().getId());
  }


}
