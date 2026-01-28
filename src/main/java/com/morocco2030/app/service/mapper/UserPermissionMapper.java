package com.morocco2030.app.service.mapper;

import com.morocco2030.app.domain.Permission;
import com.morocco2030.app.domain.User;
import com.morocco2030.app.domain.UserPermission;
import com.morocco2030.app.service.dto.PermissionDTO;
import com.morocco2030.app.service.dto.UserDTO;
import com.morocco2030.app.service.dto.UserPermissionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserPermission} and its DTO {@link UserPermissionDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserPermissionMapper extends EntityMapper<UserPermissionDTO, UserPermission> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    @Mapping(target = "permission", source = "permission", qualifiedByName = "permissionName")
    UserPermissionDTO toDto(UserPermission s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);

    @Named("permissionName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    PermissionDTO toDtoPermissionName(Permission permission);
}
