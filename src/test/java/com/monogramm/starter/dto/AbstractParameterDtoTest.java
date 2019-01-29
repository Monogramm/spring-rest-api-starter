/*
 * Creation by madmath03 the 2017-12-20.
 */

package com.monogramm.starter.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;

import com.monogramm.starter.persistence.AbstractParameter;
import com.monogramm.starter.persistence.ParameterType;

import org.junit.Test;

/**
 * {@link AbstractParameterDto} Unit Test.
 * 
 * @author madmath03
 */
public abstract class AbstractParameterDtoTest<T extends AbstractParameterDto>
    extends AbstractGenericDtoTest<T> {

  protected static final String DUMMY_NAME = "Foo";
  protected static final String DUMMY_DESCRIPTION = "Foo";
  protected static final String DUMMY_VALUE = "42";

  /**
   * Test method for {@link AbstractParameterDto#hashCode()}.
   */
  @Test
  public void testHashCode() {
    assertEquals(this.getDto().hashCode(), this.getDto().hashCode());
    assertNotEquals(this.getDto().hashCode(), null);
    assertNotEquals(this.getDto(), new Object().hashCode());


    final T anotherDto = this.buildTestDto();

    assertEquals(this.getDto().hashCode(), anotherDto.hashCode());
    assertEquals(anotherDto.hashCode(), this.getDto().hashCode());


    final T copy = this.buildCopyTestDto();

    assertEquals(copy.hashCode(), this.getDto().hashCode());

    copy.setName(DUMMY_NAME);

    assertNotEquals(copy.hashCode(), this.getDto().hashCode());

    copy.setName(null);

    assertEquals(copy.hashCode(), this.getDto().hashCode());

    copy.setDescription(DUMMY_DESCRIPTION);

    assertNotEquals(copy.hashCode(), this.getDto().hashCode());

    copy.setDescription(null);

    assertEquals(copy.hashCode(), this.getDto().hashCode());

    copy.setValue(DUMMY_VALUE);

    assertNotEquals(copy.hashCode(), this.getDto().hashCode());

    copy.setValue(null);

    assertEquals(copy.hashCode(), this.getDto().hashCode());

    copy.setType(ParameterType.ANY.toString());

    assertEquals(copy.hashCode(), this.getDto().hashCode());

    copy.setType(null);

    assertNotEquals(copy.hashCode(), this.getDto().hashCode());
  }

  /**
   * Test method for {@link AbstractParameterDto#equals(java.lang.Object)}.
   */
  @Test
  public void testEqualsObject() {
    assertEquals(this.getDto(), this.getDto());
    assertNotEquals(this.getDto(), null);
    assertNotEquals(this.getDto(), new Object());

    assertEquals(this.getDto(), this.getDto());

    final T copy = this.buildCopyTestDto();

    assertEquals(copy, this.getDto());

    copy.setName(DUMMY_NAME);

    assertNotEquals(copy, this.getDto());

    copy.setName(null);

    assertEquals(copy, this.getDto());

    copy.setDescription(DUMMY_DESCRIPTION);

    assertNotEquals(copy, this.getDto());

    copy.setDescription(null);

    assertEquals(copy, this.getDto());

    copy.setValue(DUMMY_VALUE);

    assertNotEquals(copy, this.getDto());

    copy.setValue(null);

    assertEquals(copy, this.getDto());

    copy.setType(ParameterType.ANY.toString());

    assertEquals(copy, this.getDto());

    copy.setType(null);

    assertNotEquals(copy, this.getDto());
  }

  /**
   * Test method for {@link AbstractParameterDto#equals(java.lang.Object)}.
   */
  @Test
  public void testEqualsObjectDummy() {
    assertEquals(this.getDto(), this.getDto());

    final AbstractParameterDto dummy = new AbstractParameterDto() {

      /**
       * The {@code serialVersionUID}.
       */
      private static final long serialVersionUID = 1L;

    };

    assertEquals(dummy, this.getDto());

    dummy.setName(DUMMY_NAME);

    assertNotEquals(dummy, this.getDto());

    dummy.setName(null);

    assertEquals(dummy, this.getDto());

    dummy.setDescription(DUMMY_DESCRIPTION);

    assertNotEquals(dummy, this.getDto());

    dummy.setDescription(null);

    assertEquals(dummy, this.getDto());

    dummy.setValue(DUMMY_VALUE);

    assertNotEquals(dummy, this.getDto());

    dummy.setValue(null);

    assertEquals(dummy, this.getDto());

    dummy.setType(ParameterType.ANY.toString());

    assertEquals(dummy, this.getDto());

    dummy.setType(null);

    assertNotEquals(dummy, this.getDto());
  }

  /**
   * Test method for {@link AbstractParameterDto#AbstractGenericTokenDto()}.
   */
  @Test
  public void testAbstractGenericTokenDto() {
    final T anotherDto = this.buildTestDto();

    assertNotNull(anotherDto);
    assertNotSame(this.getDto(), anotherDto);

    assertNull(anotherDto.getName());
    assertEquals(this.getDto().getName(), anotherDto.getName());

    assertNull(anotherDto.getDescription());
    assertEquals(this.getDto().getDescription(), anotherDto.getDescription());

    assertEquals(ParameterType.ANY.toString(), anotherDto.getType());
    assertEquals(this.getDto().getType(), anotherDto.getType());

    assertNull(anotherDto.getValue());
    assertEquals(this.getDto().getValue(), anotherDto.getValue());
  }

  /**
   * Test method for
   * {@link AbstractParameterDto#AbstractGenericTokenDto(com.monogramm.starter.getDto().AbstractParameterDto)}.
   */
  @Test
  public void testAbstractGenericTokenDtoAbstractGenericTokenDto() {
    final T copyDto = this.buildCopyTestDto();

    assertNotNull(copyDto);
    assertNotSame(this.getDto(), copyDto);

    assertNull(copyDto.getName());
    assertEquals(this.getDto().getName(), copyDto.getName());

    assertNull(copyDto.getDescription());
    assertEquals(this.getDto().getDescription(), copyDto.getDescription());

    assertEquals(ParameterType.ANY.toString(), copyDto.getType());
    assertEquals(this.getDto().getType(), copyDto.getType());

    assertNull(copyDto.getValue());
    assertEquals(this.getDto().getValue(), copyDto.getValue());
  }

  /**
   * Test method for {@link AbstractParameter#getName()}.
   */
  @Test
  public void testGetName() {
    assertNull(this.getDto().getName());
  }

  /**
   * Test method for {@link AbstractParameter#setName(java.lang.String)}.
   */
  @Test
  public void testSetName() {
    final String test = "TEST";
    this.getDto().setName(test);
    assertEquals(test, this.getDto().getName());
  }

  /**
   * Test method for {@link AbstractParameter#getDescription()}.
   */
  @Test
  public void testGetDescription() {
    assertNull(this.getDto().getDescription());
  }

  /**
   * Test method for {@link AbstractParameter#setDescription(String)}.
   */
  @Test
  public void testSetDescription() {
    final String test = "TEST";
    this.getDto().setDescription(test);
    assertEquals(test, this.getDto().getDescription());
  }

  /**
   * Test method for {@link AbstractParameter#getType()}.
   */
  @Test
  public void testGetType() {
    assertNotNull(this.getDto().getType());
  }

  /**
   * Test method for {@link AbstractParameter#setType(ParameterType)}.
   */
  @Test
  public void testSetType() {
    final String test = ParameterType.typeOf(DUMMY_VALUE).toString();
    this.getDto().setType(test);
    assertEquals(test, this.getDto().getType());
  }

  /**
   * Test method for {@link AbstractParameter#getValue()}.
   */
  @Test
  public void testGetValue() {
    assertNull(this.getDto().getValue());
  }

  /**
   * Test method for {@link AbstractParameter#setValue(java.lang.String)}.
   */
  @Test
  public void testSetValue() {
    final String test = "TEST";
    this.getDto().setValue(test);
    assertEquals(test, this.getDto().getValue());
  }

  /**
   * Test method for {@link AbstractGenericDto#compareTo(AbstractGenericDto)}.
   */
  @Test
  public void testCompareToCopyAlteredName() {
    assertNotNull(getDto());

    final T otherDto = this.buildTestDto(getDto());
    assertNotNull(otherDto);

    final String name = this.getClass().getSimpleName();
    getDto().setName(name);
    assertEquals(1, getDto().compareTo(otherDto));
    assertEquals(-1, otherDto.compareTo(getDto()));

    otherDto.setName(name);
    assertEquals(0, getDto().compareTo(otherDto));
    assertEquals(0, otherDto.compareTo(getDto()));
  }

}
