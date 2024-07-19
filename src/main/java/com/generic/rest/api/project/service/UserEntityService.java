package com.generic.rest.api.project.service;

import com.generic.rest.api.project.dto.AuthTokenDTO;
import com.generic.rest.api.project.dto.RoleDTO;
import com.generic.rest.api.project.dto.UserEntityDTO;
import com.generic.rest.api.project.pages.UserEntityPageDTO;

import java.util.List;

public interface UserEntityService {
    UserEntityDTO createUser(String auth, UserEntityDTO userDTO);

    AuthTokenDTO authenticateUser(String emailOrUsername, String password, String lockIp, String userZoneId);

    void deleteUser(String auth, String id);

    UserEntityDTO updateUser(String auth, String id, UserEntityDTO userDTO);

    UserEntityPageDTO findAllUsers(String auth, int page, int size, String sort, String sortOrder);

    UserEntityDTO findUserById(String auth, String id);

    UserEntityDTO findUserByUsername(String auth, String username);

    UserEntityDTO findUserByEmailInternal(String email);

    UserEntityDTO findUserByEmail(String auth, String email);

    public List<RoleDTO> findAllRolesForUser(String auth, String id);

    UserEntityDTO findByUsernameOrEmailAndDeletedFalse(String auth, String username, String email);

}
