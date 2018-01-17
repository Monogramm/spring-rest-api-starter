/*
 * Creation by madmath03 the 2017-12-20.
 */

package com.monogramm.starter.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;

import java.util.Date;
import java.util.UUID;

import org.junit.Test;

/**
 * {@link AbstractTokenDto} Unit Test.
 * 
 * @author madmath03
 */
public abstract class AbstractTokenDtoTest<T extends AbstractTokenDto>
    extends AbstractGenericDtoTest<T> {

  private static final String DUMMY_TOKEN = "Foo";
  private static final Date DUMMY_EXPIRY_DATE = new Date();

  /**
   * Test method for {@link AbstractTokenDto#hashCode()}.
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

    copy.setCode(DUMMY_TOKEN);

    assertNotEquals(copy.hashCode(), this.getDto().hashCode());

    copy.setCode(null);

    assertEquals(copy.hashCode(), this.getDto().hashCode());

    copy.setExpiryDate(DUMMY_EXPIRY_DATE);

    assertNotEquals(copy.hashCode(), this.getDto().hashCode());

    copy.setExpiryDate(null);

    assertEquals(copy.hashCode(), this.getDto().hashCode());
  }

  /**
   * Test method for {@link AbstractTokenDto#equals(java.lang.Object)}.
   */
  @Test
  public void testEqualsObject() {
    assertEquals(this.getDto(), this.getDto());
    assertNotEquals(this.getDto(), null);
    assertNotEquals(this.getDto(), new Object());

    assertEquals(this.getDto(), this.getDto());

    final T copy = this.buildCopyTestDto();

    assertEquals(copy, this.getDto());

    copy.setCode(DUMMY_TOKEN);

    assertNotEquals(copy, this.getDto());

    copy.setCode(null);

    assertEquals(copy, this.getDto());

    copy.setExpiryDate(DUMMY_EXPIRY_DATE);

    assertNotEquals(copy, this.getDto());

    copy.setExpiryDate(null);

    assertEquals(copy, this.getDto());
  }

  /**
   * Test method for {@link AbstractTokenDto#equals(java.lang.Object)}.
   */
  @Test
  public void testEqualsObjectDummy() {
    assertEquals(this.getDto(), this.getDto());

    final AbstractTokenDto dummy = new AbstractTokenDto() {

      /**
       * The {@code serialVersionUID}.
       */
      private static final long serialVersionUID = 1L;

    };

    assertEquals(dummy, this.getDto());

    dummy.setCode(DUMMY_TOKEN);

    assertNotEquals(dummy, this.getDto());

    dummy.setCode(null);

    assertEquals(dummy, this.getDto());

    dummy.setExpiryDate(DUMMY_EXPIRY_DATE);

    assertNotEquals(dummy, this.getDto());

    dummy.setExpiryDate(null);

    assertEquals(dummy, this.getDto());
  }

  /**
   * Test method for {@link AbstractTokenDto#AbstractGenericTokenDto()}.
   */
  @Test
  public void testAbstractGenericTokenDto() {
    final T anotherDto = this.buildTestDto();

    assertNotSame(this.getDto(), anotherDto);

    assertNull(this.getDto().getCode());
    assertEquals(this.getDto().getCode(), anotherDto.getCode());
    assertNull(this.getDto().getExpiryDate());
    assertEquals(this.getDto().getExpiryDate(), anotherDto.getExpiryDate());
    assertNull(this.getDto().getUser());
    assertEquals(this.getDto().getUser(), anotherDto.getUser());
  }

  /**
   * Test method for
   * {@link AbstractTokenDto#AbstractGenericTokenDto(com.monogramm.starter.dto.AbstractTokenDto)}.
   */
  @Test
  public void testAbstractGenericTokenDtoAbstractGenericTokenDto() {
    final T anotherDto = this.buildCopyTestDto();

    assertNotSame(this.getDto(), anotherDto);

    assertNull(this.getDto().getCode());
    assertEquals(this.getDto().getCode(), anotherDto.getCode());
    assertNull(this.getDto().getExpiryDate());
    assertEquals(this.getDto().getExpiryDate(), anotherDto.getExpiryDate());
    assertNull(this.getDto().getUser());
    assertEquals(this.getDto().getUser(), anotherDto.getUser());
  }

  /**
   * Test method for {@link AbstractTokenDto#getCode()}.
   */
  @Test
  public void testGetToken() {
    assertNull(this.getDto().getCode());
  }

  /**
   * Test method for {@link AbstractTokenDto#setCode(java.lang.String)}.
   */
  @Test
  public void testSetToken() {
    final String test = "TEST";
    this.getDto().setCode(test);
    assertEquals(test, this.getDto().getCode());
  }

  /**
   * Test method for {@link AbstractTokenDto#getExpiryDate()}.
   */
  @Test
  public void testGetExpiryDate() {
    assertNull(this.getDto().getExpiryDate());
  }

  /**
   * Test method for {@link AbstractTokenDto#setExpiryDate(java.util.Date)}.
   */
  @Test
  public void testSetExpiryDate() {
    final Date date = new Date();
    this.getDto().setExpiryDate(date);
    assertEquals(date, this.getDto().getExpiryDate());

    final Date newDate = new Date();
    this.getDto().setExpiryDate(newDate);
    assertEquals(newDate, this.getDto().getExpiryDate());
  }

  /**
   * Test method for {@link AbstractTokenDto#getUser()}.
   */
  @Test
  public void testGetUser() {
    assertNull(this.getDto().getUser());
  }

  /**
   * Test method for {@link AbstractTokenDto#setUser(java.util.UUID)}.
   */
  @Test
  public void testSetUser() {
    final UUID testUser = UUID.randomUUID();
    this.getDto().setUser(testUser);
    assertEquals(testUser, this.getDto().getUser());
  }

}
