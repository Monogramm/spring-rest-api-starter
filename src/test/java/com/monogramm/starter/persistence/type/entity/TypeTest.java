/*
 * Creation by madmath03 the 2017-08-29.
 */

package com.monogramm.starter.persistence.type.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;

import com.monogramm.starter.persistence.AbstractGenericEntityTest;
import com.monogramm.starter.persistence.type.entity.Type;

import org.junit.Test;

/**
 * {@link Type} Unit Test.
 * 
 * @author madmath03
 */
public class TypeTest extends AbstractGenericEntityTest<Type> {

  private static final String DISPLAYNAME = "Foo";

  @Override
  protected Type buildTestEntity() {
    return Type.builder().build();
  }

  protected Type buildTestEntity(final String name) {
    return Type.builder(name).build();
  }

  @Override
  public void testHashCode() {
    super.testHashCode();

    final Type copy = new Type(this.getEntity());

    assertEquals(copy.hashCode(), this.getEntity().hashCode());

    copy.setName(DISPLAYNAME);

    assertNotEquals(copy.hashCode(), this.getEntity().hashCode());

    copy.setName(null);

    assertEquals(copy.hashCode(), this.getEntity().hashCode());
  }

  @Override
  public void testEqualsObject() {
    super.testEqualsObject();

    final Type anotherEntity = new Type(this.getEntity());

    assertEquals(anotherEntity, this.getEntity());

    anotherEntity.setName(DISPLAYNAME);

    assertNotEquals(anotherEntity, this.getEntity());

    anotherEntity.setName(null);

    assertEquals(anotherEntity, this.getEntity());
  }

  /**
   * Test method for {@link Type#Type()}.
   */
  @Test
  public void testType() {
    final Type type = new Type();

    assertNull(type.getName());
  }

  /**
   * Test method for {@link Type#Type()}.
   */
  @Test
  public void testTypeString() {
    final Type type = new Type(DISPLAYNAME);

    assertEquals(DISPLAYNAME, type.getName());
  }

  /**
   * Test method for {@link Type#Type()}.
   */
  @Test
  public void testTypeType() {
    new Type(this.getEntity());
  }

  /**
   * Test method for {@link Type#Type()}.
   */
  @Test(expected = NullPointerException.class)
  public void testTypeTypeNull() {
    new Type((Type) null);
  }

  /**
   * Test method for {@link Type#getName()}.
   */
  @Test
  public void testGetName() {
    assertNull(this.getEntity().getName());
  }

  /**
   * Test method for {@link Type#setName(java.lang.String)}.
   */
  @Test
  public void testSetName() {
    final String name = "TEST";

    this.getEntity().setName(name);

    assertEquals(name, this.getEntity().getName());
  }

  /**
   * Test method for {@link Type#update(Type)}.
   */
  @Test
  public void testUpdate() {
    super.testUpdate();

    final Type anotherEntity = this.buildTestEntity();

    this.getEntity().update(anotherEntity);

    assertNotSame(this.getEntity(), anotherEntity);

    assertNull(this.getEntity().getName());
    assertEquals(this.getEntity().getName(), anotherEntity.getName());
  }

  /**
   * Test method for {@link Type#update(Type)}.
   */
  @Test
  public void testUpdateName() {
    final Type anotherEntity = this.buildTestEntity(DISPLAYNAME);

    this.getEntity().update(anotherEntity);

    assertNotSame(this.getEntity(), anotherEntity);

    assertNotNull(this.getEntity().getName());
    assertEquals(this.getEntity().getName(), anotherEntity.getName());
  }

}
