package com.morocco2030.app.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.morocco2030.app.domain.UserPermission} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserPermissionDTO implements Serializable {

    private Long id;

    private Instant grantedAt;

    private Boolean isActive;

    @NotNull
    private UserDTO user;

    @NotNull
    private PermissionDTO permission;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getGrantedAt() {
        return grantedAt;
    }

    public void setGrantedAt(Instant grantedAt) {
        this.grantedAt = grantedAt;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public PermissionDTO getPermission() {
        return permission;
    }

    public void setPermission(PermissionDTO permission) {
        this.permission = permission;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserPermissionDTO)) {
            return false;
        }

        UserPermissionDTO userPermissionDTO = (UserPermissionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userPermissionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserPermissionDTO{" +
            "id=" + getId() +
            ", grantedAt='" + getGrantedAt() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", user=" + getUser() +
            ", permission=" + getPermission() +
            "}";
    }
}
