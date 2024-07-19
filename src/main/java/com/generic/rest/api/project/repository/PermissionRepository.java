package com.generic.rest.api.project.repository;

import com.generic.rest.api.project.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {
    Optional<Permission> findByName(String name);

    List<Permission> findAllByOrderByNameAsc();

    Optional<Permission> findByNameAndAbbr(String name, String abbr);
}
