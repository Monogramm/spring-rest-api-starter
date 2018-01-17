/*
 * Creation by madmath03 the 2017-12-20.
 */

package com.monogramm.starter.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.monogramm.starter.dto.AbstractParameterDto;
import com.monogramm.starter.persistence.user.dao.IUserRepository;
import com.monogramm.starter.persistence.user.entity.User;

import java.util.Date;
import java.util.UUID;

import org.junit.Test;

/**
 * {@link AbstractParameterBridge} Unit Test.
 * 
 * @author madmath03
 */
public abstract class AbstractParameterBridgeTest<E extends AbstractParameter,
    D extends AbstractParameterDto, B extends AbstractParameterBridge<E, D>>
    extends AbstractGenericBridgeTest<E, D, B> {

  /**
   * Build an empty bridge.
   * 
   * @return an DTO for tests.
   */
  abstract protected B buildTestBridge(IUserRepository userRepository);

  /**
   * Test method for {@link AbstractParameterBridge#AbstractParameterBridge()}.
   */
  @Test
  public void testAbstractGenericTokenBridge() {
    final B bridge = this.buildTestBridge();

    super.testAbstractGenericBridge(bridge);
  }

  /**
   * Test method for {@link AbstractParameterBridge#AbstractParameterBridge(IUserRepository)}.
   */
  @Test
  public void testAbstractGenericTokenBridgeIUserRepository() {
    final B bridge = this.buildTestBridge(getUserRepository());

    super.testAbstractGenericBridgeIUserRepository(bridge);
  }

  /**
   * Test method for {@link AbstractParameterBridge#AbstractParameterBridge(IUserRepository)}.
   */
  @Test
  public void testAbstractGenericTokenBridgeNull() {
    final B bridge = this.buildTestBridge(null);

    super.testAbstractGenericBridge(bridge);
  }

  /**
   * Test method for {@link AbstractParameterBridge#buildEntity()}.
   */
  @Test
  public void testBuildEntity() {
    super.testBuildEntity();

    E entity = this.getBridge().buildEntity();

    assertNull(entity.getName());
    assertNull(entity.getDescription());
    assertEquals(ParameterType.ANY, entity.getType());
    assertNull(entity.getValue());
  }

  /**
   * Test method for {@link AbstractParameterBridge#buildDto()}.
   */
  @Test
  public void testBuildDto() {
    super.testBuildDto();

    D dto = this.getBridge().buildDto();

    assertNull(dto.getName());
    assertNull(dto.getDescription());
    assertEquals(ParameterType.ANY.toString(), dto.getType());
    assertNull(dto.getValue());
  }

  /**
   * Test method for {@link AbstractParameterBridge#toEntity(AbstractParameterDto)}.
   */
  @Test
  public void testToEntity() {
    final D dto = this.getBridge().buildDto();

    final String test = "TEST";
    dto.setName(test);
    dto.setDescription("Test");
    dto.setType(ParameterType.DOUBLE.toString());
    dto.setValue("42.0");

    E entity = this.getBridge().toEntity(dto);

    assertEquals(dto.getName(), entity.getName());
    assertEquals(dto.getDescription(), entity.getDescription());
    assertEquals(dto.getType(), entity.getType().toString());
    assertEquals(dto.getValue(), entity.getValue());
  }

  /**
   * Test method for {@link AbstractParameterBridge#toEntity(AbstractParameterDto)}.
   */
  @Test
  public void testToEntityNullUserRepository() {
    final D dto = this.buildTestBridge(null).buildDto();

    final String test = "TEST";
    dto.setName(test);
    dto.setDescription("Test");
    dto.setType(ParameterType.DOUBLE.toString());
    dto.setValue("42.0");

    E entity = this.buildTestBridge(null).toEntity(dto);

    assertEquals(dto.getName(), entity.getName());
    assertEquals(dto.getDescription(), entity.getDescription());
    assertEquals(dto.getType(), entity.getType().toString());
    assertEquals(dto.getValue(), entity.getValue());
  }

  /**
   * Test method for {@link AbstractParameterBridge#toDto(AbstractParameter)}.
   */
  @Test
  public void testToDto() {
    final E entity = this.getBridge().buildEntity();

    final String token = "TEST";
    entity.setName(token);
    entity.setDescription("Test");
    entity.setValue("42.0");

    D dto = this.getBridge().toDto(entity);

    entity.setCreatedAt(new Date());
    entity.setCreatedBy(User.builder().id(UUID.randomUUID()).build());
    entity.setModifiedAt(new Date());
    entity.setModifiedBy(User.builder().id(UUID.randomUUID()).build());
    entity.setOwner(User.builder().id(UUID.randomUUID()).build());
    dto = this.getBridge().toDto(entity);
 
    assertEquals(entity.getName(), dto.getName());
    assertEquals(entity.getDescription(), dto.getDescription());
    assertEquals(entity.getType().toString(), dto.getType());
    assertEquals(entity.getValue(), dto.getValue());
  }


}
