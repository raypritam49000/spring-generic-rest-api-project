package com.generic.rest.api.project.service.impl;

import com.generic.rest.api.project.dto.EnumDTO;
import com.generic.rest.api.project.service.UtilityService;
import com.generic.rest.api.project.utility.EnumClassPathScanningCandidateComponentProvider;
import io.jsonwebtoken.lang.Classes;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UtilityServiceImpl implements UtilityService {

    private final Map<String, List<EnumDTO>> enumsList = new HashMap<>();

    private final Map<String, List<EnumDTO>> enumsChatList = new HashMap<>();

    @Autowired
    @Lazy
    UtilityServiceImpl(EnumClassPathScanningCandidateComponentProvider enumProvider, EnumClassPathScanningCandidateComponentProvider enumChatProvider) {
        enumProvider.findCandidateComponents("com.generic.rest.api.project")
                .forEach(component -> {
                    List<EnumDTO> enumDTO = new ArrayList<>();

                    Class<Enum<?>> componentClass = Classes.forName(component.getBeanClassName());

                    Stream.of(componentClass.getEnumConstants()).forEach(e -> {
                        EnumDTO enumDTO1 = new EnumDTO();
                        enumDTO1.setConstant(e.name());
                        enumDTO1.setValue(e.toString());
                        enumDTO.add(enumDTO1);
                    });

                    enumsList.put(componentClass.getSimpleName(), enumDTO);
                });


        enumChatProvider.findCandidateComponents("com.generic.rest.api.project").forEach(component -> {
            try {
                Class<?> componentClass = Class.forName(component.getBeanClassName());

                if (componentClass.isEnum()) {
                    @SuppressWarnings("unchecked")
                    Class<Enum<?>> enumClass = (Class<Enum<?>>) componentClass;

                    List<EnumDTO> enumDTOs = Stream.of(enumClass.getEnumConstants())
                            .map(e -> new EnumDTO(e.name(), e.toString()))
                            .collect(Collectors.toList());

                    enumsChatList.put(enumClass.getSimpleName(), enumDTOs);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public Map<String, List<EnumDTO>> getEnumsList() {
        return enumsList;
    }

    @Override
    public Map<String, List<EnumDTO>> getEnumsChatList() {
        return enumsChatList;
    }

    @Override
    public Map<String, List<Map<String, String>>> findEnumsWithSingleValue(String basePackage) {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<? extends Enum>> enumClasses = reflections.getSubTypesOf(Enum.class);
        Map<String, List<Map<String, String>>> result = new HashMap<>();
        for (Class<? extends Enum> enumClass : enumClasses) {
            List<Map<String, String>> enumList = new ArrayList<>();
            for (Enum<?> enumConstant : enumClass.getEnumConstants()) {
                Map<String, String> enumMap = new HashMap<>();
                enumMap.put("constant", enumConstant.name());
                try {
                    Method getNameMethod = enumClass.getMethod("getName");
                    String value = (String) getNameMethod.invoke(enumConstant);
                    enumMap.put("value", value);
                } catch (Exception e) {
                    enumMap.put("value", enumConstant.name());
                }
                enumList.add(enumMap);
            }
            result.put(enumClass.getSimpleName(), enumList);
        }
        return result;
    }

    @Override
    public Map<String, List<Map<String, Object>>> findEnumsWithMultiValue(String basePackage) {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<? extends Enum>> enumClasses = reflections.getSubTypesOf(Enum.class);
        Map<String, List<Map<String, Object>>> result = new HashMap<>();
        for (Class<? extends Enum> enumClass : enumClasses) {
            List<Map<String, Object>> enumList = new ArrayList<>();
            for (Enum<?> enumConstant : enumClass.getEnumConstants()) {
                Map<String, Object> enumMap = new HashMap<>();
                enumMap.put("constant", enumConstant.name());
                for (Method method : enumClass.getDeclaredMethods()) {
                    if (method.getName().startsWith("get") && method.getParameterCount() == 0) {
                        try {
                            String propertyName = method.getName().substring(3, 4).toLowerCase() + method.getName().substring(4);
                            Object value = method.invoke(enumConstant);
                            enumMap.put(propertyName, value);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                enumList.add(enumMap);
            }
            result.put(enumClass.getSimpleName(), enumList);
        }
        return result;
    }
}
