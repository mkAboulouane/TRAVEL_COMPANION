package com.morocco2030.app.web.rest;

import static com.morocco2030.app.domain.UserPermissionAsserts.*;
import static com.morocco2030.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.morocco2030.app.IntegrationTest;
import com.morocco2030.app.domain.Permission;
import com.morocco2030.app.domain.User;
import com.morocco2030.app.domain.UserPermission;
import com.morocco2030.app.repository.UserPermissionRepository;
import com.morocco2030.app.repository.UserRepository;
import com.morocco2030.app.service.UserPermissionService;
import com.morocco2030.app.service.dto.UserPermissionDTO;
import com.morocco2030.app.service.mapper.UserPermissionMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link UserPermissionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class UserPermissionResourceIT {

    private static final Instant DEFAULT_GRANTED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_GRANTED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/user-permissions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserPermissionRepository userPermissionRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private UserPermissionRepository userPermissionRepositoryMock;

    @Autowired
    private UserPermissionMapper userPermissionMapper;

    @Mock
    private UserPermissionService userPermissionServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserPermissionMockMvc;

    private UserPermission userPermission;

    private UserPermission insertedUserPermission;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserPermission createEntity(EntityManager em) {
        UserPermission userPermission = new UserPermission().grantedAt(DEFAULT_GRANTED_AT).isActive(DEFAULT_IS_ACTIVE);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        userPermission.setUser(user);
        // Add required entity
        Permission permission;
        if (TestUtil.findAll(em, Permission.class).isEmpty()) {
            permission = PermissionResourceIT.createEntity();
            em.persist(permission);
            em.flush();
        } else {
            permission = TestUtil.findAll(em, Permission.class).get(0);
        }
        userPermission.setPermission(permission);
        return userPermission;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserPermission createUpdatedEntity(EntityManager em) {
        UserPermission updatedUserPermission = new UserPermission().grantedAt(UPDATED_GRANTED_AT).isActive(UPDATED_IS_ACTIVE);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedUserPermission.setUser(user);
        // Add required entity
        Permission permission;
        if (TestUtil.findAll(em, Permission.class).isEmpty()) {
            permission = PermissionResourceIT.createUpdatedEntity();
            em.persist(permission);
            em.flush();
        } else {
            permission = TestUtil.findAll(em, Permission.class).get(0);
        }
        updatedUserPermission.setPermission(permission);
        return updatedUserPermission;
    }

    @BeforeEach
    void initTest() {
        userPermission = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedUserPermission != null) {
            userPermissionRepository.delete(insertedUserPermission);
            insertedUserPermission = null;
        }
    }

    @Test
    @Transactional
    void createUserPermission() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the UserPermission
        UserPermissionDTO userPermissionDTO = userPermissionMapper.toDto(userPermission);
        var returnedUserPermissionDTO = om.readValue(
            restUserPermissionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userPermissionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UserPermissionDTO.class
        );

        // Validate the UserPermission in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedUserPermission = userPermissionMapper.toEntity(returnedUserPermissionDTO);
        assertUserPermissionUpdatableFieldsEquals(returnedUserPermission, getPersistedUserPermission(returnedUserPermission));

        insertedUserPermission = returnedUserPermission;
    }

    @Test
    @Transactional
    void createUserPermissionWithExistingId() throws Exception {
        // Create the UserPermission with an existing ID
        userPermission.setId(1L);
        UserPermissionDTO userPermissionDTO = userPermissionMapper.toDto(userPermission);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserPermissionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userPermissionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserPermission in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUserPermissions() throws Exception {
        // Initialize the database
        insertedUserPermission = userPermissionRepository.saveAndFlush(userPermission);

        // Get all the userPermissionList
        restUserPermissionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userPermission.getId().intValue())))
            .andExpect(jsonPath("$.[*].grantedAt").value(hasItem(DEFAULT_GRANTED_AT.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUserPermissionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(userPermissionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUserPermissionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(userPermissionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUserPermissionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(userPermissionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUserPermissionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(userPermissionRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getUserPermission() throws Exception {
        // Initialize the database
        insertedUserPermission = userPermissionRepository.saveAndFlush(userPermission);

        // Get the userPermission
        restUserPermissionMockMvc
            .perform(get(ENTITY_API_URL_ID, userPermission.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userPermission.getId().intValue()))
            .andExpect(jsonPath("$.grantedAt").value(DEFAULT_GRANTED_AT.toString()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE));
    }

    @Test
    @Transactional
    void getNonExistingUserPermission() throws Exception {
        // Get the userPermission
        restUserPermissionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserPermission() throws Exception {
        // Initialize the database
        insertedUserPermission = userPermissionRepository.saveAndFlush(userPermission);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userPermission
        UserPermission updatedUserPermission = userPermissionRepository.findById(userPermission.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUserPermission are not directly saved in db
        em.detach(updatedUserPermission);
        updatedUserPermission.grantedAt(UPDATED_GRANTED_AT).isActive(UPDATED_IS_ACTIVE);
        UserPermissionDTO userPermissionDTO = userPermissionMapper.toDto(updatedUserPermission);

        restUserPermissionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userPermissionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userPermissionDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserPermission in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUserPermissionToMatchAllProperties(updatedUserPermission);
    }

    @Test
    @Transactional
    void putNonExistingUserPermission() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userPermission.setId(longCount.incrementAndGet());

        // Create the UserPermission
        UserPermissionDTO userPermissionDTO = userPermissionMapper.toDto(userPermission);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserPermissionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userPermissionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userPermissionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserPermission in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserPermission() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userPermission.setId(longCount.incrementAndGet());

        // Create the UserPermission
        UserPermissionDTO userPermissionDTO = userPermissionMapper.toDto(userPermission);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserPermissionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userPermissionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserPermission in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserPermission() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userPermission.setId(longCount.incrementAndGet());

        // Create the UserPermission
        UserPermissionDTO userPermissionDTO = userPermissionMapper.toDto(userPermission);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserPermissionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userPermissionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserPermission in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserPermissionWithPatch() throws Exception {
        // Initialize the database
        insertedUserPermission = userPermissionRepository.saveAndFlush(userPermission);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userPermission using partial update
        UserPermission partialUpdatedUserPermission = new UserPermission();
        partialUpdatedUserPermission.setId(userPermission.getId());

        partialUpdatedUserPermission.isActive(UPDATED_IS_ACTIVE);

        restUserPermissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserPermission.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserPermission))
            )
            .andExpect(status().isOk());

        // Validate the UserPermission in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserPermissionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedUserPermission, userPermission),
            getPersistedUserPermission(userPermission)
        );
    }

    @Test
    @Transactional
    void fullUpdateUserPermissionWithPatch() throws Exception {
        // Initialize the database
        insertedUserPermission = userPermissionRepository.saveAndFlush(userPermission);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userPermission using partial update
        UserPermission partialUpdatedUserPermission = new UserPermission();
        partialUpdatedUserPermission.setId(userPermission.getId());

        partialUpdatedUserPermission.grantedAt(UPDATED_GRANTED_AT).isActive(UPDATED_IS_ACTIVE);

        restUserPermissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserPermission.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserPermission))
            )
            .andExpect(status().isOk());

        // Validate the UserPermission in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserPermissionUpdatableFieldsEquals(partialUpdatedUserPermission, getPersistedUserPermission(partialUpdatedUserPermission));
    }

    @Test
    @Transactional
    void patchNonExistingUserPermission() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userPermission.setId(longCount.incrementAndGet());

        // Create the UserPermission
        UserPermissionDTO userPermissionDTO = userPermissionMapper.toDto(userPermission);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserPermissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userPermissionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userPermissionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserPermission in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserPermission() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userPermission.setId(longCount.incrementAndGet());

        // Create the UserPermission
        UserPermissionDTO userPermissionDTO = userPermissionMapper.toDto(userPermission);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserPermissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userPermissionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserPermission in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserPermission() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userPermission.setId(longCount.incrementAndGet());

        // Create the UserPermission
        UserPermissionDTO userPermissionDTO = userPermissionMapper.toDto(userPermission);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserPermissionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(userPermissionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserPermission in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserPermission() throws Exception {
        // Initialize the database
        insertedUserPermission = userPermissionRepository.saveAndFlush(userPermission);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the userPermission
        restUserPermissionMockMvc
            .perform(delete(ENTITY_API_URL_ID, userPermission.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return userPermissionRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected UserPermission getPersistedUserPermission(UserPermission userPermission) {
        return userPermissionRepository.findById(userPermission.getId()).orElseThrow();
    }

    protected void assertPersistedUserPermissionToMatchAllProperties(UserPermission expectedUserPermission) {
        assertUserPermissionAllPropertiesEquals(expectedUserPermission, getPersistedUserPermission(expectedUserPermission));
    }

    protected void assertPersistedUserPermissionToMatchUpdatableProperties(UserPermission expectedUserPermission) {
        assertUserPermissionAllUpdatablePropertiesEquals(expectedUserPermission, getPersistedUserPermission(expectedUserPermission));
    }
}
