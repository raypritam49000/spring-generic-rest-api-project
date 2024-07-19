package com.generic.rest.api.project.dto;

import com.generic.rest.api.project.enumeration.GenderType;
import com.generic.rest.api.project.enumeration.Status;
import com.generic.rest.api.project.enumeration.Type;
import com.generic.rest.api.project.search.dto.base.AuditableSearchParameters;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSearchParameters extends AuditableSearchParameters {
    private String username;
    private String email;
    private String password;
    private Type type;
    private GenderType genderType;
    private Status status;
}
