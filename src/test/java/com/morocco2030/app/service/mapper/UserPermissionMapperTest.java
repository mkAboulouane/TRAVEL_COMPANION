package com.morocco2030.app.service.mapper;

import static com.morocco2030.app.domain.UserPermissionAsserts.*;
import static com.morocco2030.app.domain.UserPermissionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserPermissionMapperTest {

    private UserPermissionMapper userPermissionMapper;

    @BeforeEach
    void setUp() {
        userPermissionMapper = new UserPermissionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getUserPermissionSample1();
        var actual = userPermissionMapper.toEntity(userPermissionMapper.toDto(expected));
        assertUserPermissionAllPropertiesEquals(expected, actual);
    }
}
