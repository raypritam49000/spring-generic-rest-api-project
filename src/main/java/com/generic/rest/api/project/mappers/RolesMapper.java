package com.generic.rest.api.project.mappers;

import com.generic.rest.api.project.dto.RoleDTO;
import com.generic.rest.api.project.mappers.base.BaseMapper;
import com.generic.rest.api.project.model.Permission;
import com.generic.rest.api.project.model.Role;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface RolesMapper extends BaseMapper<RoleDTO, Role> {
    RolesMapper INSTANCE = Mappers.getMapper(RolesMapper.class);

    @Override
    RoleDTO toDto(Role entity);

    @Override
    Role toEntity(RoleDTO dto);

    @Override
    List<RoleDTO> toDtoList(List<Role> entities);

    @Override
    List<Role> toEntityList(List<RoleDTO> entities);

    default String fromPermission(Permission permission) {
        return permission == null ? null : permission.getName();
    }

    default Permission fromStringToPermission(String permission) {
        return null;
    }
}
