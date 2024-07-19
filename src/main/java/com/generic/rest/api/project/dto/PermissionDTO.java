package com.generic.rest.api.project.dto;

import com.generic.rest.api.project.search.dto.base.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermissionDTO extends BaseDTO {
	private String abbr;
	private String name;
}