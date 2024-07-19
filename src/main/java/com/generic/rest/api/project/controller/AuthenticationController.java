package com.generic.rest.api.project.controller;


import com.generic.rest.api.project.dto.AuthTokenDTO;
import com.generic.rest.api.project.dto.AuthenticationDTO;
import com.generic.rest.api.project.service.UserEntityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class AuthenticationController {
    private final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    private UserEntityService userManagementService;

    @Autowired
    public void setUserManagementService(UserEntityService userManagementService) {
        this.userManagementService = userManagementService;
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public AuthTokenDTO authenticate(@RequestBody AuthenticationDTO authenticationDTO, HttpServletRequest request) {
        logger.info("@@@ authenticate ::: {}", authenticationDTO);
        String logIp = null;
        if (request.getHeader("X-FORWARDED-FOR") != null) {
            logIp = request.getHeader("X-FORWARDED-FOR").split(",")[0];
        }
        return userManagementService.authenticateUser(authenticationDTO.getUsername(), authenticationDTO.getPassword(), logIp, authenticationDTO.getUserZoneId());
    }

}