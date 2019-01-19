package com.monogramm.starter.api.type.controller;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
import com.monogramm.starter.api.AbstractGenericController;
import com.monogramm.starter.config.data.GenericOperation;
import com.monogramm.starter.config.data.InitialDataLoader;
import com.monogramm.starter.dto.type.TypeDto;
import com.monogramm.starter.persistence.type.entity.Type;
import com.monogramm.starter.persistence.type.exception.TypeNotFoundException;
import com.monogramm.starter.persistence.type.service.ITypeService;
import com.monogramm.starter.persistence.user.entity.User;

import java.util.UUID;

import javax.transaction.Transactional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MvcResult;


/**
 * {@link TypeController} Mock Integration Test.
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
public class TypeControllerMockIT extends AbstractControllerMockIT {

  /**
   * Logger for {@link TypeControllerMockIT}.
   */
  private static final Logger LOG = LoggerFactory.getLogger(TypeControllerMockIT.class);

  /**
   * The managed type of this tested controller.
   */
  public static final String TYPE = TypeController.TYPE;
  /**
   * The request base path of this tested controller.
   */
  public static final String CONTROLLER_PATH = TypeController.CONTROLLER_PATH;

  private static final String DISPLAYNAME = TypeControllerMockIT.class.getSimpleName();

  @Autowired
  private InitialDataLoader initialDataLoader;

  @Autowired
  private ITypeService typeService;

  private UUID randomId;

  private User testCreatedBy;
  private User testOwner;
  private Type testEntity;

  @Before
  public void setUp() {
    super.setUpMockMvc();
    super.setUpValidUser(GenericOperation.allPermissionNames(TYPE));

    this.randomId = UUID.randomUUID();

    // Add the users
    testCreatedBy =
        User.builder(DISPLAYNAME + "_Creator", DISPLAYNAME + ".creator@creation.org").build();
    assertTrue(getUserService().add(testCreatedBy));
    testOwner = User.builder(DISPLAYNAME + "_Owner", DISPLAYNAME + ".owner@creation.org").build();
    assertTrue(getUserService().add(testOwner));

    // Add a test type
    testEntity = Type.builder(DISPLAYNAME).createdBy(testCreatedBy).owner(testOwner).build();
    assertTrue(typeService.add(testEntity));
  }

  @After
  public void tearDown() {
    try {
      if (testEntity.getId() != null) {
        typeService.deleteById(testEntity.getId());
      }
    } catch (TypeNotFoundException e) {
      LOG.trace("Type already deleted: " + testEntity, e);
    }
    testEntity = null;

    super.deleteUser(testCreatedBy);
    testCreatedBy = null;

    super.deleteUser(testOwner);
    testOwner = null;
  }

  /**
   * Test method for
   * {@link TypeController#getDataById(String, org.springframework.web.context.request.WebRequest, javax.servlet.http.HttpServletResponse)}.
   * 
   * @throws Exception if the test crashes.
   */
  @Test
  public void testGetTypeByIdRandomId() throws Exception {
    // No permission returned
    MvcResult result = getMockMvc()
        .perform(get(CONTROLLER_PATH + '/' + randomId).headers(getHeaders(getMockToken())))
        .andExpect(status().isNotFound()).andReturn();

    assertNotNull(result.getResponse());
    assertNotNull(result.getResponse().getContentAsString());
  }

  /**
   * Test method for
   * {@link TypeController#getDataById(String, org.springframework.web.context.request.WebRequest, javax.servlet.http.HttpServletResponse)}.
   * 
   * @throws Exception if the test crashes.
   */
  @Test
  public void testGetTypeById() throws Exception {
    // Type previously created should be returned
    getMockMvc()
        .perform(get(CONTROLLER_PATH + '/' + this.testEntity.getId())
            .headers(getHeaders(getMockToken())))
        .andExpect(status().isOk()).andExpect(jsonPath("$.id", notNullValue()))
        .andExpect(jsonPath("$.id", equalToIgnoringCase(this.testEntity.getId().toString())))
        .andExpect(jsonPath("$.name", equalToIgnoringCase(DISPLAYNAME)))
        .andExpect(jsonPath("$.createdAt", notNullValue()))
        .andExpect(jsonPath("$.createdBy", notNullValue()))
        .andExpect(jsonPath("$.createdBy", equalToIgnoringCase(testCreatedBy.getId().toString())))
        .andExpect(jsonPath("$.modifiedAt", nullValue()))
        .andExpect(jsonPath("$.modifiedBy", nullValue()))
        .andExpect(jsonPath("$.owner", notNullValue()))
        .andExpect(jsonPath("$.owner", equalToIgnoringCase(testOwner.getId().toString())));
  }

  /**
   * Test method for {@link TypeController#getAllData()}.
   * 
   * @throws Exception if the test crashes.
   */
  @Test
  public void testGetAllTypes() throws Exception {
    // There should at least be the test entity...
    int expectedSize = 1;
    // ...plus the types created at application initialization
    if (initialDataLoader.getTypes() != null) {
      expectedSize += initialDataLoader.getTypes().size();
    }

    getMockMvc().perform(get(CONTROLLER_PATH).headers(getHeaders(getMockToken())))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$", hasSize(expectedSize)));
  }

  /**
   * Test method for
   * {@link TypeController#getAllDataPaginated(int, int, org.springframework.web.context.request.WebRequest, org.springframework.web.util.UriComponentsBuilder, javax.servlet.http.HttpServletResponse)}.
   * 
   * @throws Exception if the test crashes.
   */
  @Test
  public void testGetAllTypesPaginated() throws Exception {
    int expectedSize = 1;

    getMockMvc()
        .perform(get(CONTROLLER_PATH).param("page", "0").param("size", "1")
            .headers(getHeaders(getMockToken())))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$", hasSize(expectedSize)));
  }

  /**
   * Test method for
   * {@link TypeController#getAllDataPaginated(int, int, org.springframework.web.context.request.WebRequest, org.springframework.web.util.UriComponentsBuilder, javax.servlet.http.HttpServletResponse)}.
   * 
   * @throws Exception if the test crashes.
   */
  @Test
  public void testGetAllTypesPaginatedDefaultSize() throws Exception {
    // There should at least be the test entity...
    int expectedSize = 1;
    // ...plus the types created at application initialization
    if (initialDataLoader.getTypes() != null) {
      expectedSize += initialDataLoader.getTypes().size();
    }
    expectedSize = Math.min(expectedSize, AbstractGenericController.DEFAULT_SIZE_INT);

    getMockMvc()
        .perform(get(CONTROLLER_PATH).param("page", "0").headers(getHeaders(getMockToken())))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$", hasSize(expectedSize)));
  }

  /**
   * Test method for
   * {@link TypeController#addData(TypeDto, org.springframework.web.util.UriComponentsBuilder)}.
   * 
   * @throws Exception if the test crashes.
   */
  @Test
  public void testAddType() throws Exception {
    final String newName = "Bar";
    final Type model = Type.builder(newName).build();
    final TypeDto dto = typeService.toDto(model);

    final String typeJson = dto.toJson();

    // Insert test type should work
    getMockMvc()
        .perform(post(CONTROLLER_PATH).headers(getHeaders(getMockToken())).content(typeJson))
        .andExpect(status().isCreated()).andExpect(jsonPath("$.id", notNullValue()))
        .andExpect(jsonPath("$.name", equalToIgnoringCase(newName)))
        .andExpect(jsonPath("$.createdAt", notNullValue()))
        .andExpect(jsonPath("$.createdBy", nullValue()))
        .andExpect(jsonPath("$.modifiedAt", nullValue()))
        .andExpect(jsonPath("$.modifiedBy", nullValue()))
        .andExpect(jsonPath("$.owner", nullValue()));

    // Insert again should generate conflict
    getMockMvc()
        .perform(post(CONTROLLER_PATH).headers(getHeaders(getMockToken())).content(typeJson))
        .andExpect(status().isConflict()).andExpect(content().bytes(new byte[] {}));
  }

  /**
   * Test method for {@link TypeController#updateData(String, Type)}.
   * 
   * @throws TypeNotFoundException if the type entity to update is not found.
   */
  @Test
  public void testUpdateType() throws Exception {
    // Update on random UUID should not find any type
    final Type dummyModel = Type.builder(DISPLAYNAME).id(randomId).build();
    final TypeDto dummyDto = typeService.toDto(dummyModel);

    getMockMvc().perform(put(CONTROLLER_PATH + '/' + randomId).headers(getHeaders(getMockToken()))
        .content(dummyDto.toJson())).andExpect(status().isNotFound());

    // Update the type
    final String newName = "Bar";
    this.testEntity.setName(newName);
    this.testEntity.setModifiedBy(testOwner);
    final TypeDto dto = typeService.toDto(this.testEntity);

    // Update test type should work
    final String entityJson = this.testEntity.toJson();
    final String dtoJson = dto.toJson();

    assertEquals(dtoJson, entityJson);

    getMockMvc()
        .perform(put(CONTROLLER_PATH + '/' + this.testEntity.getId())
            .headers(getHeaders(getMockToken())).content(dtoJson))
        .andExpect(status().isOk()).andExpect(jsonPath("$.id", notNullValue()))
        .andExpect(jsonPath("$.id", equalToIgnoringCase(this.testEntity.getId().toString())))
        .andExpect(jsonPath("$.name", equalToIgnoringCase(newName)))
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
   * Test method for {@link TypeController#deleteData(java.lang.String)}.
   * 
   * @throws TypeNotFoundException if the type entity to delete is not found.
   */
  @Test
  public void testDeleteType() throws Exception {
    // Delete on random UUID should not find any type
    getMockMvc()
        .perform(delete(CONTROLLER_PATH + '/' + randomId).headers(getHeaders(getMockToken())))
        .andExpect(status().isNotFound());

    // Delete test type should work
    getMockMvc().perform(
        delete(CONTROLLER_PATH + '/' + this.testEntity.getId()).headers(getHeaders(getMockToken())))
        .andExpect(status().isNoContent());
  }

}
