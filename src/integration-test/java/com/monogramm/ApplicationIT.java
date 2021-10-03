/*
 * Creation by madmath03 the 2017-09-08.
 */

package com.monogramm;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * {@link Application} Integration Test.
 * 
 * <p>
 * Spring boot test is searching @SpringBootConfiguration or @SpringBootApplication. In this case it
 * will automatically find {@link Application} boot main class.
 * </p>
 * 
 * @author madmath03
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class ApplicationIT {

  @Test
  public void testContextLoads() throws Exception {
    // sanity check
  }

}
