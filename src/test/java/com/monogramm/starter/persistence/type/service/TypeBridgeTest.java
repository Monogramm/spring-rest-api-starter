/*
 * Creation by madmath03 the 2017-11-19.
 */

package com.monogramm.starter.persistence.type.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.monogramm.starter.dto.type.TypeDto;
import com.monogramm.starter.persistence.AbstractGenericBridgeTest;
import com.monogramm.starter.persistence.type.entity.Type;
import com.monogramm.starter.persistence.user.entity.User;

import java.util.Date;
import java.util.UUID;

import org.junit.Test;

/**
 * {@link TypeBridge} Unit Test.
 * 
 * @author madmath03
 */
public class TypeBridgeTest extends AbstractGenericBridgeTest<Type, TypeDto, TypeBridge> {

  @Override
  protected TypeBridge buildTestBridge() {
    return new TypeBridge();
  }

  /**
   * Test method for {@link TypeBridge#TypeBridge()}.
   */
  @Test
  public void testTypeBridge() {
    final TypeBridge typeBridge = new TypeBridge();

    super.testAbstractGenericBridge(typeBridge);
  }

  /**
   * Test method for
   * {@link TypeBridge#TypeBridge(com.monogramm.starter.persistence.user.dao.UserRepository)}.
   */
  @Test
  public void testTypeBridgeIUserRepository() {
    final TypeBridge typeBridge = new TypeBridge(getUserRepository());

    super.testAbstractGenericBridgeIUserRepository(typeBridge);
  }

  /**
   * Test method for
   * {@link TypeBridge#TypeBridge(com.monogramm.starter.persistence.user.dao.UserRepository)}.
   */
  @Test
  public void testTypeBridgeNull() {
    final TypeBridge typeBridge = new TypeBridge(null);

    super.testAbstractGenericBridge(typeBridge);
  }

  /**
   * Test method for {@link TypeBridge#buildEntity()}.
   */
  @Test
  public void testBuildEntity() {
    super.testBuildEntity();

    assertNull(this.getBridge().buildEntity().getName());
  }

  /**
   * Test method for {@link TypeBridge#buildDto()}.
   */
  @Test
  public void testBuildDto() {
    super.testBuildDto();

    assertNull(this.getBridge().buildDto().getName());
  }

  /**
   * Test method for {@link TypeBridge#toEntity(com.monogramm.starter.dto.type.TypeDto)}.
   */
  @Test
  public void testToEntityTypeDto() {
    final TypeDto dto = this.getBridge().buildDto();

    final String name = "TEST";
    dto.setName(name);

    Type entity = this.getBridge().toEntity(dto);

    assertEquals(entity.getName(), dto.getName());
  }

  /**
   * Test method for {@link TypeBridge#toDto(com.monogramm.starter.persistence.type.entity.Type)}.
   */
  @Test
  public void testToDtoType() {
    final String name = "TEST";
    final Type entity = this.getBridge().buildEntity();
    entity.setName(name);

    TypeDto dto = this.getBridge().toDto(entity);

    entity.setCreatedAt(new Date());
    entity.setCreatedBy(User.builder().id(UUID.randomUUID()).build());
    entity.setModifiedAt(new Date());
    entity.setModifiedBy(User.builder().id(UUID.randomUUID()).build());
    entity.setOwner(User.builder().id(UUID.randomUUID()).build());
    dto = this.getBridge().toDto(entity);

    assertEquals(dto.getName(), entity.getName());
  }

}
