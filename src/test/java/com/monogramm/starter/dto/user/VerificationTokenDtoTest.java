/*
 * Creation by madmath03 the 2017-12-20.
 */

package com.monogramm.starter.dto.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import com.monogramm.starter.dto.AbstractTokenDtoTest;

import org.junit.Test;

/**
 * {@link VerificationTokenDto} Unit Test.
 * 
 * @author madmath03
 */
public class VerificationTokenDtoTest extends AbstractTokenDtoTest<VerificationTokenDto> {

  private static final String DUMMY_FIELD = "email";
  private static final String DUMMY_VALUE = "my_email@address.com";

  @Override
  protected VerificationTokenDto buildTestDto(VerificationTokenDto other) {
    return new VerificationTokenDto(other);
  }

  @Override
  protected VerificationTokenDto buildTestDto() {
    return new VerificationTokenDto();
  }

  /**
   * Test method for {@link VerificationTokenDto#getField()}.
   */
  @Test
  public void testGetField() {
    assertNull(this.getDto().getField());
  }

  /**
   * Test method for {@link VerificationTokenDto#setField(java.lang.String)}.
   */
  @Test
  public void testSetField() {
    final String test = "TEST";
    this.getDto().setField(test);
    assertEquals(test, this.getDto().getField());
  }

  /**
   * Test method for {@link VerificationTokenDto#getValue()}.
   */
  @Test
  public void testGetValue() {
    assertNull(this.getDto().getValue());
  }

  /**
   * Test method for {@link VerificationTokenDto#setValue(java.lang.String)}.
   */
  @Test
  public void testSetValue() {
    final String test = "TEST";
    this.getDto().setValue(test);
    assertEquals(test, this.getDto().getValue());
  }

  /**
   * Test method for {@link VerificationTokenDto#hashCode()}.
   */
  @Test
  public void testHashCode() {
    super.testHashCode();

    final VerificationTokenDto copy = this.buildTestDto(this.getDto());

    copy.setField(DUMMY_FIELD);

    assertNotEquals(copy.hashCode(), this.getDto().hashCode());

    copy.setField(null);

    assertEquals(copy.hashCode(), this.getDto().hashCode());

    copy.setValue(DUMMY_VALUE);

    assertNotEquals(copy.hashCode(), this.getDto().hashCode());

    copy.setValue(null);

    assertEquals(copy.hashCode(), this.getDto().hashCode());
  }

  /**
   * Test method for {@link VerificationTokenDto#equals(java.lang.Object)}.
   */
  @Test
  public void testEqualsObject() {
    super.testEqualsObject();

    final VerificationTokenDto copy = this.buildTestDto(this.getDto());

    copy.setField(DUMMY_FIELD);

    assertNotEquals(copy, this.getDto());

    copy.setField(null);

    assertEquals(copy, this.getDto());

    copy.setValue(DUMMY_VALUE);

    assertNotEquals(copy, this.getDto());

    copy.setValue(null);

    assertEquals(copy, this.getDto());
  }

}
