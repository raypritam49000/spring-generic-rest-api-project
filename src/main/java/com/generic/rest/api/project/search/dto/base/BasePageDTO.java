package com.generic.rest.api.project.search.dto.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

@Data
@AllArgsConstructor
public class BasePageDTO<T> {
    private static final Logger logger = LoggerFactory.getLogger(BasePageDTO.class);

    public boolean hasContent;
    public boolean hasNext;
    public boolean hasPrevious;
    public boolean isFirst;
    public boolean isLast;
    public long totalElements;
    public int totalPages;
    public Collection<T> data;
    public int perPage;
    public int pageNumber;
    public int size;
    public String pluralResourceName;

    public boolean isSorted;
    public String sortColumn;
    public String sortOrder;

    public boolean countEntities;

    public String getDisplayingText() {

        if (!hasContent) {
            logger.info("hasContent is false");
            return "There are no " + pluralResourceName + " to display";
        }

        String total = " of " + this.totalElements + " " + pluralResourceName;

        int startNumber = (size * pageNumber) + 1;
        int endNumber;

        if (isLast) {
            endNumber = startNumber + data.size() - 1;
        } else {
            endNumber = startNumber + size - 1;
        }

        return "Displaying " + startNumber + " through " + endNumber
                + (countEntities ? " of many " + pluralResourceName : total);
    }

    public BasePageDTO() {
    }

    public BasePageDTO(boolean hasContent, boolean hasNext, boolean hasPrevious, boolean isFirst, boolean isLast,
                       long totalElements, int totalPages, Collection<T> data, int perPage, int pageNumber, int size,
                       String pluralResourceName, boolean isSorted, String sortColumn, String sortOrder) {
        this.hasContent = hasContent;
        this.hasNext = hasNext;
        this.hasPrevious = hasPrevious;
        this.isFirst = isFirst;
        this.isLast = isLast;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.data = data;
        this.perPage = perPage;
        this.pageNumber = pageNumber;
        this.size = size;
        this.pluralResourceName = pluralResourceName;
        this.isSorted = isSorted;
        this.sortColumn = sortColumn;
        this.sortOrder = sortOrder;
    }
}
