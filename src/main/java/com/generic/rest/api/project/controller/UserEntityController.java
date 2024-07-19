package com.generic.rest.api.project.controller;

import com.generic.rest.api.project.dto.RoleDTO;
import com.generic.rest.api.project.dto.UserEntityDTO;
import com.generic.rest.api.project.pages.UserEntityPageDTO;
import com.generic.rest.api.project.service.UserEntityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserEntityController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private UserEntityService userManagementService;

    @Autowired
    public void setUserManagementService(UserEntityService userManagementService) {
        this.userManagementService = userManagementService;
    }

    @PreAuthorize("hasAuthority('USR-C')")
    @PostMapping("")
    public UserEntityDTO createUser(@RequestHeader("Authorization") String auth, @RequestBody UserEntityDTO userDTO) {
        return userManagementService.createUser(auth, userDTO);
    }

    @PreAuthorize("hasAuthority('USR-D')")
    @DeleteMapping(value = "/{id}")
    public void deleteUser(@RequestHeader("Authorization") String auth, @PathVariable String id) {
        logger.info("@@@@ deleteUser :: {} ", id);
        userManagementService.deleteUser(auth, id);
    }

    @PreAuthorize("hasAuthority('USR-U')")
    @PutMapping(value = "/{id}")
    public UserEntityDTO updateUser(@RequestHeader("Authorization") String auth, @PathVariable String id, @RequestBody UserEntityDTO userDTO) {
        return userManagementService.updateUser(auth, id, userDTO);
    }

    @PreAuthorize("hasAuthority('USR-R')")
    @GetMapping(value = "/{id}")
    public UserEntityDTO findUserById(@RequestHeader("Authorization") String auth, @PathVariable String id) {
        return userManagementService.findUserById(auth, id);
    }

    @PreAuthorize("hasAuthority('USR-R')")
    @GetMapping("")
    public UserEntityPageDTO findAllUsers(@RequestHeader("Authorization") String auth,
                                          @RequestParam(required = false, value = "page", defaultValue = "0") int page,
                                          @RequestParam(required = false, value = "size", defaultValue = "50") int size,
                                          @RequestParam(required = false, value = "sort") String sort,
                                          @RequestParam(required = false, value = "sortOrder") String sortOrder) {
        return userManagementService.findAllUsers(auth, page, size, sort, sortOrder);
    }

    @PreAuthorize("hasAuthority('USR-R')")
    @GetMapping(value = "/username/{username}")
    public UserEntityDTO findUserByUsername(@RequestHeader("Authorization") String auth, @PathVariable String username) {
        return userManagementService.findUserByUsername(auth, username);
    }

    @PostMapping(value = "/findUserByEmailInternal")
    public UserEntityDTO findUserByEmailInternal(@RequestParam("emailId") String emailId) {
        logger.info("@@@ findUserByEmailInternal :::: ");
        return userManagementService.findUserByEmailInternal(emailId);
    }

    @PostMapping(value = "/findUserByEmail")
    public UserEntityDTO findUserByEmail(@RequestHeader("Authorization") String auth, @RequestParam("emailId") String emailId) {
        logger.info("@@@ findUserByEmail :::: ");
        return userManagementService.findUserByEmail(auth, emailId);
    }

    @PreAuthorize("hasAuthority('USR-R')")
    @GetMapping(value = "/{id}/roles")
    public List<RoleDTO> findUserRoles(@RequestHeader("Authorization") String auth, @PathVariable String id) {
        return userManagementService.findAllRolesForUser(auth, id);
    }

    @PreAuthorize("hasAuthority('USR-R')")
    @GetMapping(value = "/email/username")
    public UserEntityDTO findByUsernameOrEmailAndDeletedFalse(@RequestHeader("Authorization") String auth, @RequestParam("username") String username, @RequestParam("email") String email) {
        return userManagementService.findByUsernameOrEmailAndDeletedFalse(auth, username, email);
    }


}