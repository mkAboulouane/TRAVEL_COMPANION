package com.morocco2030.app.service;

import com.morocco2030.app.service.dto.UserPermissionDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.morocco2030.app.domain.UserPermission}.
 */
public interface UserPermissionService {
    /**
     * Save a userPermission.
     *
     * @param userPermissionDTO the entity to save.
     * @return the persisted entity.
     */
    UserPermissionDTO save(UserPermissionDTO userPermissionDTO);

    /**
     * Updates a userPermission.
     *
     * @param userPermissionDTO the entity to update.
     * @return the persisted entity.
     */
    UserPermissionDTO update(UserPermissionDTO userPermissionDTO);

    /**
     * Partially updates a userPermission.
     *
     * @param userPermissionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UserPermissionDTO> partialUpdate(UserPermissionDTO userPermissionDTO);

    /**
     * Get all the userPermissions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UserPermissionDTO> findAll(Pageable pageable);

    /**
     * Get all the userPermissions with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UserPermissionDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" userPermission.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UserPermissionDTO> findOne(Long id);

    /**
     * Delete the "id" userPermission.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
