package com.morocco2030.app.domain;

import static com.morocco2030.app.domain.PermissionTestSamples.*;
import static com.morocco2030.app.domain.UserPermissionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.morocco2030.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserPermissionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserPermission.class);
        UserPermission userPermission1 = getUserPermissionSample1();
        UserPermission userPermission2 = new UserPermission();
        assertThat(userPermission1).isNotEqualTo(userPermission2);

        userPermission2.setId(userPermission1.getId());
        assertThat(userPermission1).isEqualTo(userPermission2);

        userPermission2 = getUserPermissionSample2();
        assertThat(userPermission1).isNotEqualTo(userPermission2);
    }

    @Test
    void permissionTest() {
        UserPermission userPermission = getUserPermissionRandomSampleGenerator();
        Permission permissionBack = getPermissionRandomSampleGenerator();

        userPermission.setPermission(permissionBack);
        assertThat(userPermission.getPermission()).isEqualTo(permissionBack);

        userPermission.permission(null);
        assertThat(userPermission.getPermission()).isNull();
    }
}
