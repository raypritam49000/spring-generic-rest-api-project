package com.generic.rest.api.project.service;

import com.generic.rest.api.project.dto.PermissionDTO;
import javassist.NotFoundException;

import java.util.List;

public interface PermissionService {
    PermissionDTO createPermission(PermissionDTO permissionDTO);

    void deletePermission(String id);

    List<PermissionDTO> findAllPermission();

    PermissionDTO findPermissionById(String id);

    PermissionDTO updatePermission(String id, PermissionDTO permissionDTO);
}
