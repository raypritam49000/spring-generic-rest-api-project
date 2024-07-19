package com.generic.rest.api.project.search.dto.base;

import com.generic.rest.api.project.search.DateRange;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.Valid;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class AuditableSearchParameters extends PageSearchParameters {
    boolean includeDeleted;

    @Valid
    protected DateRange creationDateRange;

    @Valid
    protected DateRange modifiedDateRange;

}

