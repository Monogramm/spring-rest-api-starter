package com.monogramm.starter.api.permission.controller;

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
import com.monogramm.starter.dto.permission.PermissionDto;
import com.monogramm.starter.persistence.permission.entity.Permission;
import com.monogramm.starter.persistence.permission.exception.PermissionNotFoundException;
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
 * {@link PermissionController} Mock Integration Test.
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
public class PermissionControllerMockIT extends AbstractControllerMockIT {

  /**
   * Logger for {@link PermissionControllerMockIT}.
   */
  private static final Logger LOG = LogManager.getLogger(PermissionControllerMockIT.class);

  /**
   * The managed permission of this tested controller.
   */
  public static final String TYPE = "Permissions";
  /**
   * The request base path of this tested controller.
   */
  public static final String CONTROLLER_PATH = '/' + TYPE;

  private static final String DISPLAYNAME = "Foo";

  private UUID randomId;

  private User testCreatedBy;
  private User testOwner;
  private Permission testEntity;

  @Autowired
  private InitialDataLoader initialDataLoader;

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

    // Add a test permission
    testEntity = Permission.builder(DISPLAYNAME).createdBy(testCreatedBy).owner(testOwner).build();
    assertTrue(getPermissionService().add(testEntity));
  }

  @After
  public void tearDown() {
    try {
      if (testEntity.getId() != null) {
        getPermissionService().deleteById(testEntity.getId());
      }
    } catch (PermissionNotFoundException e) {
      LOG.trace("Permission already deleted: " + testEntity, e);
    }
    testEntity = null;

    super.deleteUser(testCreatedBy);
    testCreatedBy = null;

    super.deleteUser(testOwner);
    testOwner = null;
  }

  /**
   * Test method for {@link PermissionController#getDataById(java.lang.String)}.
   * 
   * @throws Exception if the test crashes.
   */
  @Test
  public void testGetPermissionById() throws Exception {
    // No permission returned
    getMockMvc().perform(get(CONTROLLER_PATH + '/' + randomId).headers(getHeaders(getMockToken())))
        .andExpect(status().isNotFound()).andExpect(content().bytes(new byte[] {}));

    // Permission previously created should be returned
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
   * Test method for {@link PermissionController#getAllData()}.
   * 
   * @throws Exception if the test crashes.
   */
  @Test
  public void testGetAllPermissions() throws Exception {
    // There should at least be the test entity...
    int expectedSize = 1;
    // ...plus the permissions created at application initialization
    if (initialDataLoader.getPermissions() != null) {
      expectedSize += initialDataLoader.getPermissions().size();
    }

    getMockMvc().perform(get(CONTROLLER_PATH).headers(getHeaders(getMockToken())))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$", hasSize(expectedSize)));
  }

  /**
   * Test method for
   * {@link PermissionController#addData(PermissionDto, org.springframework.web.util.UriComponentsBuilder)}.
   * 
   * @throws Exception if the test crashes.
   */
  @Test
  public void testAddPermission() throws Exception {
    final String newName = "Bar";
    final Permission model = Permission.builder(newName).build();
    final PermissionDto dto = getPermissionService().toDto(model);

    final String permissionJson = dto.toJson();

    // Insert test permission should work
    getMockMvc()
        .perform(post(CONTROLLER_PATH).headers(getHeaders(getMockToken())).content(permissionJson))
        .andExpect(status().isCreated()).andExpect(jsonPath("$.id", notNullValue()))
        .andExpect(jsonPath("$.name", equalToIgnoringCase(newName)))
        .andExpect(jsonPath("$.createdAt", notNullValue()))
        .andExpect(jsonPath("$.createdBy", nullValue()))
        .andExpect(jsonPath("$.modifiedAt", nullValue()))
        .andExpect(jsonPath("$.modifiedBy", nullValue()))
        .andExpect(jsonPath("$.owner", nullValue()));

    // Insert again should generate conflict
    getMockMvc()
        .perform(post(CONTROLLER_PATH).headers(getHeaders(getMockToken())).content(permissionJson))
        .andExpect(status().isConflict()).andExpect(content().bytes(new byte[] {}));
  }

  /**
   * Test method for {@link PermissionController#updateData(String, Permission)}.
   * 
   * @throws PermissionNotFoundException if the permission entity to update is not found.
   */
  @Test
  public void testUpdatePermission() throws Exception {
    // Update on random UUID should not find any permission
    final Permission dummyModel = Permission.builder(DISPLAYNAME).id(randomId).build();
    final PermissionDto dummyDto = getPermissionService().toDto(dummyModel);

    getMockMvc().perform(put(CONTROLLER_PATH + '/' + randomId).headers(getHeaders(getMockToken()))
        .content(dummyDto.toJson())).andExpect(status().isNotFound());

    // Update the permission
    final String newName = "Bar";
    this.testEntity.setName(newName);
    this.testEntity.setModifiedBy(testOwner);
    final PermissionDto dto = getPermissionService().toDto(this.testEntity);

    // Update test permission should work
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
   * Test method for {@link PermissionController#deleteData(java.lang.String)}.
   * 
   * @throws PermissionNotFoundException if the permission entity to delete is not found.
   */
  @Test
  public void testDeletePermission() throws Exception {
    // Delete on random UUID should not find any permission
    getMockMvc()
        .perform(delete(CONTROLLER_PATH + '/' + randomId).headers(getHeaders(getMockToken())))
        .andExpect(status().isNotFound());

    // Delete test permission should work
    getMockMvc().perform(
        delete(CONTROLLER_PATH + '/' + this.testEntity.getId()).headers(getHeaders(getMockToken())))
        .andExpect(status().isNoContent());
  }

}
