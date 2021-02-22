/*
 * Creation by madmath03 the 2017-12-03.
 */

package com.monogramm.starter.config.i8n;

import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.MessageSource;

/**
 * {@link ApplicationMessage} Unit Test.
 * 
 * @author madmath03
 */
public class ApplicationMessageTest {

  private ApplicationMessage message;

  /**
   * @throws java.lang.Exception If test initialization crashes.
   */
  @Before
  public void setUp() throws Exception {
    this.message = new ApplicationMessage();
  }

  /**
   * @throws java.lang.Exception If test clean up crashes.
   */
  @After
  public void tearDown() throws Exception {
    this.message = null;
  }

  /**
   * Test method for {@link ApplicationMessage#messageSource()}.
   */
  @Test
  public void testMessageSource() {
    final MessageSource source = this.message.messageSource();

    assertNotNull(source);
  }

}
