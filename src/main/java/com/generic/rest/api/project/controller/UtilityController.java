package com.generic.rest.api.project.controller;

import com.generic.rest.api.project.dto.EnumDTO;
import com.generic.rest.api.project.service.impl.UtilityServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/utility")
public class UtilityController {
    private static final Logger logger = LoggerFactory.getLogger(UtilityController.class);
    @Autowired
    private UtilityServiceImpl utilityService;

    @GetMapping("/tgx/enums")
    public Map<String, List<EnumDTO>> getAllEnums() {
        Map<String, List<EnumDTO>> getAllEnums = utilityService.getEnumsList();
        logger.info("@@@ call getAllEnums ::: {}", getAllEnums);
        return getAllEnums;
    }

    @GetMapping("/enums")
    public Map<String, List<EnumDTO>> getAllChatEnums() {
        Map<String, List<EnumDTO>> getAllEnums = utilityService.getEnumsChatList();
        logger.info("@@@ call getAllChatEnums ::: {}", getAllEnums);
        return getAllEnums;
    }

    @GetMapping("/getAllEnums")
    public Map<String,List<Map<String,String>>> getEnums(){
        Map<String,List<Map<String,String>>>  getEnums = utilityService.findEnumsWithSingleValue("com.generic.rest.api.project");
        logger.info("@@@ call getEnums ::: {}", getEnums);
        return getEnums;
    }

    @GetMapping("/findAllEnums")
    public Map<String,List<Map<String,Object>>> findAllEnums(){
        Map<String,List<Map<String,Object>>> findAllEnums = utilityService.findEnumsWithMultiValue("com.generic.rest.api.project");
        logger.info("@@@ call findAllEnums ::: {}", findAllEnums);
        return findAllEnums;
    }
}
