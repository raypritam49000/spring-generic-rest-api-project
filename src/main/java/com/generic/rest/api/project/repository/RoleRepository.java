package com.generic.rest.api.project.repository;

import com.generic.rest.api.project.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,String> {
    List<Role> findAllByOrderByNameAsc();
    Optional<Role> findFirstByName(String name);
    List<Role> findAllByNameIn(List<String> name);
    List<Role> findByNameIsContainingOrderByNameAsc(String value);
    List<Role> findByNameIsNotContainingOrderByNameAsc(String value);
    List<Role> findAllByAuthorityLevelIsLessThanEqualOrderByAuthorityLevelDesc(int level);
}
