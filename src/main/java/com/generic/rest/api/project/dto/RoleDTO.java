package com.generic.rest.api.project.dto;

import com.generic.rest.api.project.search.dto.base.AuditableBaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO extends AuditableBaseDTO {
    private String name;
    private Set<String> permissions;
    private int authorityLevel;
}
