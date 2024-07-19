package com.generic.rest.api.project.search.dto.base;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PageSearchParameters {
    @NotBlank
    private String sort = "creationDate";

    @NotBlank
    private String sortOrder = "asc";

    @Min(0)
    private int pageNumber = 0;

    @Min(1)
    private int pageSize = 15;

    public void addPageParameters(String sort, String sortOrder, int pageNumber, int pageSize) {
        this.sort = StringUtils.isBlank(sort) ? this.sort : sort;
        this.sortOrder = StringUtils.isBlank(sortOrder) ? this.sortOrder : sortOrder;
        this.pageNumber = Math.max(0, pageNumber);
        this.pageSize = Math.max(1, pageSize);
    }
}
