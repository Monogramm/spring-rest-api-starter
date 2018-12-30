/*
 * Creation by madmath03 the 2018-01-09.
 */

package com.monogramm.starter.api.parameter.controller;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.monogramm.Application;
import com.monogramm.starter.api.AbstractControllerIT;
import com.monogramm.starter.api.AbstractControllerMockIT;
import com.monogramm.starter.config.data.GenericOperation;
import com.monogramm.starter.config.data.InitialDataLoader;
import com.monogramm.starter.dto.parameter.ParameterDto;
import com.monogramm.starter.persistence.parameter.entity.Parameter;
import com.monogramm.starter.persistence.parameter.exception.ParameterNotFoundException;
import com.monogramm.starter.persistence.parameter.service.IParameterService;
import com.monogramm.starter.persistence.user.entity.User;

import java.util.UUID;

import javax.transaction.Transactional;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * {@link ParameterController} Mock Integration Test.
 * 
 * <p>
 * We assume the environment is freshly created and only contains the initial data.
 * </p>
 * 
 * <p>
 * Spring boot test is searching {@code @SpringBootConfiguration} or {@code @SpringBootApplication}.
 * In this case it will automatically find {@link Application} boot main class.
 * </p>
 * 
 * @see Application
 * @see InitialDataLoader
 * @see AbstractControllerIT
 * 
 * @author madmath03
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
public class ParameterControllerMockIT extends AbstractControllerMockIT {

  /**
   * Logger for {@link ParameterControllerMockIT}.
   */
  private static final Logger LOG = LogManager.getLogger(ParameterControllerMockIT.class);

  /**
   * The managed parameter of this tested controller.
   */
  public static final String TYPE = ParameterController.TYPE;
  /**
   * The request base path of this tested controller.
   */
  public static final String CONTROLLER_PATH = ParameterController.CONTROLLER_PATH;

  protected static final String DUMMY_NAME = "Foo";
  protected static final Object DUMMY_VALUE = 42;

  @Autowired
  private InitialDataLoader initialDataLoader;

  @Autowired
  private IParameterService parameterService;

  private UUID randomId;

  private User testCreatedBy;
  private User testOwner;
  private Parameter testEntity;

  @Before
  public void setUp() {
    super.setUpMockMvc();
    super.setUpValidUser(GenericOperation.allPermissionNames(TYPE));

    this.randomId = UUID.randomUUID();

    // Add the users
    testCreatedBy =
        User.builder(DUMMY_NAME + "_Creator", DUMMY_NAME + ".creator@creation.org").build();
    assertTrue(getUserService().add(testCreatedBy));
    testOwner = User.builder(DUMMY_NAME + "_Owner", DUMMY_NAME + ".owner@creation.org").build();
    assertTrue(getUserService().add(testOwner));

    // Add a test parameter
    testEntity = Parameter.builder(DUMMY_NAME, DUMMY_VALUE).createdBy(testCreatedBy).owner(testOwner).build();
    assertTrue(parameterService.add(testEntity));
  }

  @After
  public void tearDown() {
    try {
      if (testEntity.getId() != null) {
        parameterService.deleteById(testEntity.getId());
      }
    } catch (ParameterNotFoundException e) {
      LOG.trace("Parameter already deleted: " + testEntity, e);
    }
    testEntity = null;

    super.deleteUser(testCreatedBy);
    testCreatedBy = null;

    super.deleteUser(testOwner);
    testOwner = null;
  }

  /**
   * Test method for {@link ParameterController#getDataById(java.lang.String)}.
   * 
   * @throws Exception if the test crashes.
   */
  @Test
  public void testGetParameterById() throws Exception {
    // No parameter returned
    getMockMvc().perform(get(CONTROLLER_PATH + '/' + randomId).headers(getHeaders(getMockToken())))
        .andExpect(status().isNotFound()).andExpect(content().bytes(new byte[] {}));

    // Parameter previously created should be returned
    getMockMvc()
        .perform(get(CONTROLLER_PATH + '/' + this.testEntity.getId())
            .headers(getHeaders(getMockToken())))
        .andExpect(status().isOk()).andExpect(jsonPath("$.id", notNullValue()))
        .andExpect(jsonPath("$.id", equalToIgnoringCase(this.testEntity.getId().toString())))
        .andExpect(jsonPath("$.name", equalToIgnoringCase(DUMMY_NAME)))
        .andExpect(jsonPath("$.value", equalToIgnoringCase(DUMMY_VALUE.toString())))
        .andExpect(jsonPath("$.createdAt", notNullValue()))
        .andExpect(jsonPath("$.createdBy", notNullValue()))
        .andExpect(jsonPath("$.createdBy", equalToIgnoringCase(testCreatedBy.getId().toString())))
        .andExpect(jsonPath("$.modifiedAt", nullValue()))
        .andExpect(jsonPath("$.modifiedBy", nullValue()))
        .andExpect(jsonPath("$.owner", notNullValue()))
        .andExpect(jsonPath("$.owner", equalToIgnoringCase(testOwner.getId().toString())));
  }

  /**
   * Test method for {@link ParameterController#getAllData()}.
   * 
   * @throws Exception if the test crashes.
   */
  @Test
  public void testGetAllParameters() throws Exception {
    // There should at least be the test entity...
    int expectedSize = 1;
    // ...plus the parameters created at application initialization
    if (initialDataLoader.getParameters() != null) {
      expectedSize += initialDataLoader.getParameters().size();
    }

    getMockMvc().perform(get(CONTROLLER_PATH).headers(getHeaders(getMockToken())))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$", hasSize(expectedSize)));
  }

  /**
   * Test method for
   * {@link ParameterController#addData(ParameterDto, org.springframework.web.util.UriComponentsBuilder)}.
   * 
   * @throws Exception if the test crashes.
   */
  @Test
  public void testAddParameter() throws Exception {
    final String newName = "Bar";
    final Boolean newValue = true;
    final Parameter model = Parameter.builder(newName, newValue).build();
    final ParameterDto dto = parameterService.toDto(model);

    final String parameterJson = dto.toJson();

    // Insert test parameter should work
    getMockMvc()
        .perform(post(CONTROLLER_PATH).headers(getHeaders(getMockToken())).content(parameterJson))
        .andExpect(status().isCreated()).andExpect(jsonPath("$.id", notNullValue()))
        .andExpect(jsonPath("$.name", equalToIgnoringCase(newName)))
        .andExpect(jsonPath("$.createdAt", notNullValue()))
        .andExpect(jsonPath("$.createdBy", nullValue()))
        .andExpect(jsonPath("$.modifiedAt", nullValue()))
        .andExpect(jsonPath("$.modifiedBy", nullValue()))
        .andExpect(jsonPath("$.owner", nullValue()));

    // Insert again should generate conflict
    getMockMvc()
        .perform(post(CONTROLLER_PATH).headers(getHeaders(getMockToken())).content(parameterJson))
        .andExpect(status().isConflict()).andExpect(content().bytes(new byte[] {}));
  }

  /**
   * Test method for {@link ParameterController#updateData(String, Parameter)}.
   * 
   * @throws ParameterNotFoundException if the parameter entity to update is not found.
   */
  @Test
  public void testUpdateParameter() throws Exception {
    // Update on random UUID should not find any parameter
    final Parameter dummyModel = Parameter.builder(DUMMY_NAME, DUMMY_VALUE).id(randomId).build();
    final ParameterDto dummyDto = parameterService.toDto(dummyModel);

    getMockMvc().perform(put(CONTROLLER_PATH + '/' + randomId).headers(getHeaders(getMockToken()))
        .content(dummyDto.toJson())).andExpect(status().isNotFound());

    // Update the parameter
    final String newName = "Bar";
    this.testEntity.setName(newName);
    this.testEntity.setModifiedBy(testOwner);
    final ParameterDto dto = parameterService.toDto(this.testEntity);

    // Update test parameter should work
    final String entityJson = this.testEntity.toJson();
    final String dtoJson = dto.toJson();

    assertEquals(dtoJson, entityJson);

    getMockMvc()
        .perform(put(CONTROLLER_PATH + '/' + this.testEntity.getId())
            .headers(getHeaders(getMockToken())).content(dtoJson))
        .andExpect(status().isOk()).andExpect(jsonPath("$.id", notNullValue()))
        .andExpect(jsonPath("$.id", equalToIgnoringCase(this.testEntity.getId().toString())))
        .andExpect(jsonPath("$.name", equalToIgnoringCase(newName)))
        .andExpect(jsonPath("$.value", equalToIgnoringCase(DUMMY_VALUE.toString())))
        .andExpect(jsonPath("$.createdAt", notNullValue()))
        .andExpect(jsonPath("$.createdBy", notNullValue()))
        .andExpect(jsonPath("$.createdBy", equalToIgnoringCase(testCreatedBy.getId().toString())))
        .andExpect(jsonPath("$.modifiedAt", notNullValue()))
        .andExpect(jsonPath("$.modifiedBy", notNullValue()))
        .andExpect(jsonPath("$.modifiedBy", equalToIgnoringCase(testOwner.getId().toString())))
        .andExpect(jsonPath("$.owner", notNullValue()))
        .andExpect(jsonPath("$.owner", equalToIgnoringCase(testOwner.getId().toString())));
  }

  /**
   * Test method for {@link ParameterController#deleteData(java.lang.String)}.
   * 
   * @throws ParameterNotFoundException if the parameter entity to delete is not found.
   */
  @Test
  public void testDeleteParameter() throws Exception {
    // Delete on random UUID should not find any parameter
    getMockMvc()
        .perform(delete(CONTROLLER_PATH + '/' + randomId).headers(getHeaders(getMockToken())))
        .andExpect(status().isNotFound());

    // Delete test parameter should work
    getMockMvc().perform(
        delete(CONTROLLER_PATH + '/' + this.testEntity.getId()).headers(getHeaders(getMockToken())))
        .andExpect(status().isNoContent());
  }

}
