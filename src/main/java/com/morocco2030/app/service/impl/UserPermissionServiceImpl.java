package com.morocco2030.app.service.impl;

import com.morocco2030.app.domain.UserPermission;
import com.morocco2030.app.repository.UserPermissionRepository;
import com.morocco2030.app.service.UserPermissionService;
import com.morocco2030.app.service.dto.UserPermissionDTO;
import com.morocco2030.app.service.mapper.UserPermissionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.morocco2030.app.domain.UserPermission}.
 */
@Service
@Transactional
public class UserPermissionServiceImpl implements UserPermissionService {

    private static final Logger LOG = LoggerFactory.getLogger(UserPermissionServiceImpl.class);

    private final UserPermissionRepository userPermissionRepository;

    private final UserPermissionMapper userPermissionMapper;

    public UserPermissionServiceImpl(UserPermissionRepository userPermissionRepository, UserPermissionMapper userPermissionMapper) {
        this.userPermissionRepository = userPermissionRepository;
        this.userPermissionMapper = userPermissionMapper;
    }

    @Override
    public UserPermissionDTO save(UserPermissionDTO userPermissionDTO) {
        LOG.debug("Request to save UserPermission : {}", userPermissionDTO);
        UserPermission userPermission = userPermissionMapper.toEntity(userPermissionDTO);
        userPermission = userPermissionRepository.save(userPermission);
        return userPermissionMapper.toDto(userPermission);
    }

    @Override
    public UserPermissionDTO update(UserPermissionDTO userPermissionDTO) {
        LOG.debug("Request to update UserPermission : {}", userPermissionDTO);
        UserPermission userPermission = userPermissionMapper.toEntity(userPermissionDTO);
        userPermission = userPermissionRepository.save(userPermission);
        return userPermissionMapper.toDto(userPermission);
    }

    @Override
    public Optional<UserPermissionDTO> partialUpdate(UserPermissionDTO userPermissionDTO) {
        LOG.debug("Request to partially update UserPermission : {}", userPermissionDTO);

        return userPermissionRepository
            .findById(userPermissionDTO.getId())
            .map(existingUserPermission -> {
                userPermissionMapper.partialUpdate(existingUserPermission, userPermissionDTO);

                return existingUserPermission;
            })
            .map(userPermissionRepository::save)
            .map(userPermissionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserPermissionDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all UserPermissions");
        return userPermissionRepository.findAll(pageable).map(userPermissionMapper::toDto);
    }

    public Page<UserPermissionDTO> findAllWithEagerRelationships(Pageable pageable) {
        return userPermissionRepository.findAllWithEagerRelationships(pageable).map(userPermissionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserPermissionDTO> findOne(Long id) {
        LOG.debug("Request to get UserPermission : {}", id);
        return userPermissionRepository.findOneWithEagerRelationships(id).map(userPermissionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete UserPermission : {}", id);
        userPermissionRepository.deleteById(id);
    }
}
