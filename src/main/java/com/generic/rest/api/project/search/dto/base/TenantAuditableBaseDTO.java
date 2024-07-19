package com.generic.rest.api.project.search.dto.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TenantAuditableBaseDTO extends AuditableBaseDTO {
    protected String tenant;
}
