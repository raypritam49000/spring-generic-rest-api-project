package com.generic.rest.api.project.mappers;

import com.generic.rest.api.project.dto.UserEntityDTO;
import com.generic.rest.api.project.mappers.base.BaseMapper;
import com.generic.rest.api.project.model.Role;
import com.generic.rest.api.project.model.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserEntityMapper extends BaseMapper<UserEntityDTO, UserEntity> {

    UserEntityMapper INSTANCE = Mappers.getMapper( UserEntityMapper.class );

    @Override
    @Mapping(source = "passwordHash", target = "password")
    UserEntityDTO toDto(UserEntity entity);

    @Override
    @Mapping(source="password", target="passwordHash")
    UserEntity toEntity(UserEntityDTO dto);

    @Override
    List<UserEntityDTO> toDtoList(List<UserEntity> entities);

    @Override
    List<UserEntity> toEntityList(List<UserEntityDTO> entities);

    default String fromRoles(Role role) {
        return role == null ? null : role.getName();
    }

    default Role fromStringToRole(String role) {
        // Implement your custom mapping logic here
        return null;
    }
}
