package com.morocco2030.app.service.mapper;

import static com.morocco2030.app.domain.PermissionAsserts.*;
import static com.morocco2030.app.domain.PermissionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PermissionMapperTest {

    private PermissionMapper permissionMapper;

    @BeforeEach
    void setUp() {
        permissionMapper = new PermissionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPermissionSample1();
        var actual = permissionMapper.toEntity(permissionMapper.toDto(expected));
        assertPermissionAllPropertiesEquals(expected, actual);
    }
}
