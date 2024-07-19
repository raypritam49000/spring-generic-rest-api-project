package com.generic.rest.api.project.mappers;

import com.generic.rest.api.project.dto.PersonDTO;
import com.generic.rest.api.project.mappers.base.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PersonMapper extends BaseMapper<PersonDTO, Person> {
    PersonMapper INSTANCE = Mappers.getMapper(PersonMapper.class);
}
