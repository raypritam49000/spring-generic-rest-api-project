package com.generic.rest.api.project.service;

import com.generic.rest.api.project.dto.EnumDTO;

import java.util.List;
import java.util.Map;

public interface UtilityService {
    public Map<String, List<EnumDTO>> getEnumsList();

    public Map<String, List<EnumDTO>> getEnumsChatList();

    public Map<String, List<Map<String, String>>> findEnumsWithSingleValue(String basePackage);

    public Map<String, List<Map<String, Object>>> findEnumsWithMultiValue(String basePackage);
}
