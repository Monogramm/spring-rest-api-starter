/*
 * Creation by madmath03 the 2017-08-27.
 */

package com.monogramm.starter.persistence.type.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.monogramm.starter.persistence.AbstractGenericEntityBuilderTest;
import com.monogramm.starter.persistence.type.entity.Type;
import com.monogramm.starter.persistence.type.entity.Type.TypeBuilder;

import org.junit.Test;

/**
 * {@link TypeBuilder} Unit Test.
 * 
 * @author madmath03
 */
public class TypeBuilderTest extends AbstractGenericEntityBuilderTest<Type.TypeBuilder> {

  private static final String DISPLAYNAME = "TEST";

  @Override
  protected TypeBuilder buildTestEntityBuilder() {
    return Type.builder();
  }

  /**
   * Test method for {@link Type#builder()}.
   */
  @Test
  public void testGetBuilder() {
    Type.TypeBuilder builder = Type.builder();

    assertNotNull(builder);

    final Type typeBuilt = builder.build();
    assertNotNull(typeBuilt);
    assertNull(typeBuilt.getName());
  }

  /**
   * Test method for {@link Type#builder(java.lang.String)}.
   */
  @Test
  public void testGetBuilderString() {
    Type.TypeBuilder builder = Type.builder(DISPLAYNAME);

    assertNotNull(builder);

    final Type typeBuilt = builder.build();
    assertNotNull(typeBuilt);
    assertEquals(DISPLAYNAME, typeBuilt.getName());
  }

  /**
   * Test method for {@link Type.TypeBuilder#name(java.lang.String)}.
   */
  @Test
  public void testName() {
    assertEquals(this.getEntityBuilder(), this.getEntityBuilder().name(null));

    final String name = "TEST";
    assertEquals(name,
        this.getEntityBuilder().name(name).build().getName());
  }


}
