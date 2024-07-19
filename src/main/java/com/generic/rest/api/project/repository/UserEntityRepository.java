package com.generic.rest.api.project.repository;

import com.generic.rest.api.project.model.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, String> {
    Optional<UserEntity> findByUsernameOrEmailAndDeletedFalse(String username, String email);

    Optional<UserEntity> findByIdAndDeletedFalse(String id);

    UserEntity findByUsernameAndDeletedFalse(String id);

    Page<UserEntity> findAllByDeletedFalse(Pageable pageable);

    Page<UserEntity> findAllByTenantAndDeletedFalse(String tenant, Pageable pageable);

    Optional<UserEntity> findByEmailAndDeletedFalse(String email);




}
