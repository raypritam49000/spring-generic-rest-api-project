package com.generic.rest.api.project.controller;

import com.generic.rest.api.project.dto.UserSearchParameters;
import com.generic.rest.api.project.pages.UserPageDTO;
import com.generic.rest.api.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PreAuthorize("hasAuthority('USER-R')")
    @PostMapping("/search")
    public UserPageDTO searchUsers(@RequestBody @Valid UserSearchParameters userSearchParameters)  {
        return userService.searchEmployee(userSearchParameters);
    }
}
