package com.generic.rest.api.project.repository;

import com.generic.rest.api.project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    public Boolean existsByUsername(String username);

    public Optional<User> findByUsername(String username);
}
