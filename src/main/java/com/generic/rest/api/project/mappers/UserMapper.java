package com.generic.rest.api.project.mappers;

import com.generic.rest.api.project.dto.UserDTO;
import com.generic.rest.api.project.mappers.base.BaseMapper;
import com.generic.rest.api.project.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper extends BaseMapper<UserDTO, User> {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
}
