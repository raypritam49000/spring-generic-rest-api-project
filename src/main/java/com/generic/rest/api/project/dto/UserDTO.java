package com.generic.rest.api.project.dto;

import com.generic.rest.api.project.enumeration.GenderType;
import com.generic.rest.api.project.enumeration.Status;
import com.generic.rest.api.project.enumeration.Type;
import com.generic.rest.api.project.search.dto.base.AuditableBaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO extends AuditableBaseDTO {
    private String username;
    private String email;
    private String password;
    private GenderType genderType;
    private Type type;
    private Status status;
}
