package com.generic.rest.api.project.mappers;

import com.generic.rest.api.project.dto.PermissionDTO;
import com.generic.rest.api.project.mappers.base.BaseMapper;
import com.generic.rest.api.project.model.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface PermissionsMapper extends BaseMapper<PermissionDTO, Permission> {
    PermissionsMapper INSTANCE = Mappers.getMapper(PermissionsMapper.class);

    @Override
    PermissionDTO toDto(Permission entity);

    @Override
    Permission toEntity(PermissionDTO dto);

    @Override
    List<PermissionDTO> toDtoList(List<Permission> entities);

    @Override
    List<Permission> toEntityList(List<PermissionDTO> entities);
}
