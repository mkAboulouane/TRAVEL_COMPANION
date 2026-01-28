package com.morocco2030.app.service.mapper;

import com.morocco2030.app.domain.Permission;
import com.morocco2030.app.service.dto.PermissionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Permission} and its DTO {@link PermissionDTO}.
 */
@Mapper(componentModel = "spring")
public interface PermissionMapper extends EntityMapper<PermissionDTO, Permission> {}
