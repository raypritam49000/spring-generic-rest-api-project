package com.generic.rest.api.project.service.impl;

import com.generic.rest.api.project.dto.RoleDTO;
import com.generic.rest.api.project.exception.ResourceConflictException;
import com.generic.rest.api.project.exception.ResourceNotFoundException;
import com.generic.rest.api.project.jsonwebtoken.AuthTokenDetailsDTO;
import com.generic.rest.api.project.jsonwebtoken.JsonWebTokenUtility;
import com.generic.rest.api.project.mappers.RolesMapper;
import com.generic.rest.api.project.model.Permission;
import com.generic.rest.api.project.model.Role;
import com.generic.rest.api.project.model.UserEntity;
import com.generic.rest.api.project.repository.PermissionRepository;
import com.generic.rest.api.project.repository.RoleRepository;
import com.generic.rest.api.project.repository.UserEntityRepository;
import com.generic.rest.api.project.service.RoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    public List<RoleDTO> findAllRoles(String auth) {
        AuthTokenDetailsDTO authTokenDetailsDTO = JsonWebTokenUtility.parseAndValidate(auth);
        assert authTokenDetailsDTO != null;
        UserEntity user = userEntityRepository.findById(authTokenDetailsDTO.getUserId()).orElseThrow(() -> new ResourceNotFoundException("Role not found with given id: " + authTokenDetailsDTO.getUserId()));
        int highestAuthLevel = user.getRoles().stream().mapToInt(Role::getAuthorityLevel).max().orElse(0);
        List<Role> roles = roleRepository.findAllByAuthorityLevelIsLessThanEqualOrderByAuthorityLevelDesc(highestAuthLevel);
        return RolesMapper.INSTANCE.toDtoList(roles);
    }

    @Override
    public RoleDTO createRole(RoleDTO roleDTO) {
        roleRepository.findFirstByName(StringUtils.trimToNull(roleDTO.getName())).ifPresent(role -> {
            throw new ResourceConflictException("Role already exists with given role name: " + role.getName());
        });
        Set<Permission> permissions = roleDTO.getPermissions().stream().map(permissionRepository::findByName).flatMap(Optional::stream).collect(Collectors.toSet());
        return RolesMapper.INSTANCE.toDto(roleRepository.save(new Role(StringUtils.trimToNull(roleDTO.getName()), permissions, roleDTO.getAuthorityLevel())));
    }

    @Override
    public void deleteRole(String id) {
        Role role = roleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Role not found with given id: " + id));
        role.setDeleted(true);
        roleRepository.save(role);
    }

    @Override
    public List<String> getAllPermissionByRoleName(String auth, List<String> roleNames) {
        return roleNames.stream()
                .map(roleRepository::findFirstByName)
                .flatMap(optionalRole -> optionalRole.stream().flatMap(role -> role.getPermissions().stream()))
                .map(Permission::getAbbr)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public RoleDTO findRoleById(String auth, String id) {
        var role = roleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Role not found with given id: " + id));
        return RolesMapper.INSTANCE.toDto(role);
    }


    @Override
    public RoleDTO updateRole(String id, RoleDTO roleDTO) {
        Role role = roleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Role not found with given id: " + id));

        if (!role.getName().equalsIgnoreCase(StringUtils.trimToNull(roleDTO.getName()))) {
            roleRepository.findFirstByName(StringUtils.trimToNull(roleDTO.getName())).ifPresent(existingRole -> {
                throw new ResourceConflictException("Role already exists with given role name: " + existingRole.getName());
            });
        }

        role.setName(StringUtils.trimToNull(roleDTO.getName()));
        role.setPermissions(convertPermissionNamesToPermissions(new ArrayList<>(roleDTO.getPermissions())));
        role.setAuthorityLevel(roleDTO.getAuthorityLevel());
        return RolesMapper.INSTANCE.toDto(roleRepository.save(role));
    }

    private Set<Permission> convertPermissionNamesToPermissions(List<String> permissionNames) {
        return permissionNames.stream()
                .map(permissionRepository::findByName)
                .flatMap(Optional::stream)
                .collect(Collectors.toSet());
    }

}
