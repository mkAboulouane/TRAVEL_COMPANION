package com.morocco2030.app.repository;

import com.morocco2030.app.domain.UserPermission;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserPermission entity.
 */
@Repository
public interface UserPermissionRepository extends JpaRepository<UserPermission, Long> {
    @Query("select userPermission from UserPermission userPermission where userPermission.user.login = ?#{authentication.name}")
    List<UserPermission> findByUserIsCurrentUser();

    default Optional<UserPermission> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<UserPermission> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<UserPermission> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select userPermission from UserPermission userPermission left join fetch userPermission.user left join fetch userPermission.permission",
        countQuery = "select count(userPermission) from UserPermission userPermission"
    )
    Page<UserPermission> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select userPermission from UserPermission userPermission left join fetch userPermission.user left join fetch userPermission.permission"
    )
    List<UserPermission> findAllWithToOneRelationships();

    @Query(
        "select userPermission from UserPermission userPermission left join fetch userPermission.user left join fetch userPermission.permission where userPermission.id =:id"
    )
    Optional<UserPermission> findOneWithToOneRelationships(@Param("id") Long id);
}
