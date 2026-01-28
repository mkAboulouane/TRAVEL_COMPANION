package com.morocco2030.app.web.rest;

import com.morocco2030.app.repository.UserPermissionRepository;
import com.morocco2030.app.service.UserPermissionService;
import com.morocco2030.app.service.dto.UserPermissionDTO;
import com.morocco2030.app.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.morocco2030.app.domain.UserPermission}.
 */
@RestController
@RequestMapping("/api/user-permissions")
public class UserPermissionResource {

    private static final Logger LOG = LoggerFactory.getLogger(UserPermissionResource.class);

    private static final String ENTITY_NAME = "userPermission";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserPermissionService userPermissionService;

    private final UserPermissionRepository userPermissionRepository;

    public UserPermissionResource(UserPermissionService userPermissionService, UserPermissionRepository userPermissionRepository) {
        this.userPermissionService = userPermissionService;
        this.userPermissionRepository = userPermissionRepository;
    }

    /**
     * {@code POST  /user-permissions} : Create a new userPermission.
     *
     * @param userPermissionDTO the userPermissionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userPermissionDTO, or with status {@code 400 (Bad Request)} if the userPermission has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UserPermissionDTO> createUserPermission(@Valid @RequestBody UserPermissionDTO userPermissionDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save UserPermission : {}", userPermissionDTO);
        if (userPermissionDTO.getId() != null) {
            throw new BadRequestAlertException("A new userPermission cannot already have an ID", ENTITY_NAME, "idexists");
        }
        userPermissionDTO = userPermissionService.save(userPermissionDTO);
        return ResponseEntity.created(new URI("/api/user-permissions/" + userPermissionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, userPermissionDTO.getId().toString()))
            .body(userPermissionDTO);
    }

    /**
     * {@code PUT  /user-permissions/:id} : Updates an existing userPermission.
     *
     * @param id the id of the userPermissionDTO to save.
     * @param userPermissionDTO the userPermissionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userPermissionDTO,
     * or with status {@code 400 (Bad Request)} if the userPermissionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userPermissionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserPermissionDTO> updateUserPermission(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UserPermissionDTO userPermissionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update UserPermission : {}, {}", id, userPermissionDTO);
        if (userPermissionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userPermissionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userPermissionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        userPermissionDTO = userPermissionService.update(userPermissionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userPermissionDTO.getId().toString()))
            .body(userPermissionDTO);
    }

    /**
     * {@code PATCH  /user-permissions/:id} : Partial updates given fields of an existing userPermission, field will ignore if it is null
     *
     * @param id the id of the userPermissionDTO to save.
     * @param userPermissionDTO the userPermissionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userPermissionDTO,
     * or with status {@code 400 (Bad Request)} if the userPermissionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userPermissionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userPermissionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserPermissionDTO> partialUpdateUserPermission(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UserPermissionDTO userPermissionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update UserPermission partially : {}, {}", id, userPermissionDTO);
        if (userPermissionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userPermissionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userPermissionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserPermissionDTO> result = userPermissionService.partialUpdate(userPermissionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userPermissionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /user-permissions} : get all the userPermissions.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userPermissions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<UserPermissionDTO>> getAllUserPermissions(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of UserPermissions");
        Page<UserPermissionDTO> page;
        if (eagerload) {
            page = userPermissionService.findAllWithEagerRelationships(pageable);
        } else {
            page = userPermissionService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /user-permissions/:id} : get the "id" userPermission.
     *
     * @param id the id of the userPermissionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userPermissionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserPermissionDTO> getUserPermission(@PathVariable("id") Long id) {
        LOG.debug("REST request to get UserPermission : {}", id);
        Optional<UserPermissionDTO> userPermissionDTO = userPermissionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userPermissionDTO);
    }

    /**
     * {@code DELETE  /user-permissions/:id} : delete the "id" userPermission.
     *
     * @param id the id of the userPermissionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserPermission(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete UserPermission : {}", id);
        userPermissionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
