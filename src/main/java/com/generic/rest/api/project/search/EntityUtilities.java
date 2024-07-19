package com.generic.rest.api.project.search;

import com.generic.rest.api.project.mappers.base.BaseMapper;
import com.generic.rest.api.project.search.dto.base.AuditableBaseDTO;
import com.generic.rest.api.project.search.dto.base.BaseDTO;
import com.generic.rest.api.project.search.dto.base.BasePageDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;

import java.net.URL;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * A set of utility functions for return entities consistently and validating
 * common fields
 */
public class EntityUtilities {

    /**
     * Trims the string field of an object removing any leading or trailing spaces.
     * If the string is all space, it sets the field to null.
     *
     * @param getter A reference to the getter method on the object for the string
     *               field
     * @param setter A reference to the setter method on the object for the string
     *               field
     */
    public static void trimStringField(Supplier<String> getter, Consumer<String> setter) {
        String s = getter.get();
        setter.accept(StringUtils.trimToNull(s));
    }

    /**
     * Trims all string fields that are part of the AuditableBaseDTO base class
     *
     * @param inputDto the dto to be cleaned
     */
    public static void trimAuditableDtoFields(AuditableBaseDTO inputDto) {
        trimBaseDtoFields(inputDto);
        trimStringField(inputDto::getCreatedBy, inputDto::setCreatedBy);
        trimStringField(inputDto::getModifiedBy, inputDto::setModifiedBy);
    }

    /**
     * Trims all string fields that are part of the BaseDTO base class
     *
     * @param inputDto the dto to be cleaned
     */
    public static void trimBaseDtoFields(BaseDTO inputDto) {
        trimStringField(inputDto::getId, inputDto::setId);
    }

    /**
     * Tests if a url is valid by trying to create a URL object with it.
     *
     * @param url The url to be tested
     * @return Null if the url is valid or the Exception thrown if the provided url
     *         could not be converted to a URL or URI
     */
    public static Exception isValidUrl(String url) {
        /* Try creating a valid URL */
        try {
            new URL(url).toURI();
            return null;
        }

        // If there was an Exception
        // while creating URL object
        catch (Exception e) {
            return e;
        }
    }

    /**
     * Checks if the provided object is null
     *
     * @param o        the object to check
     * @param errorMsg the message to throw in an exception if the object is null
     * @throws Exception if object is null
     */
    public static void requireNotNull(Object o, String errorMsg) throws Exception {
        if (o == null) {
            throw new Exception(errorMsg);
        }
    }

    public static void requireNotNullAndNotBlank(Object o, String errorMsg) throws Exception {
        if (o == null || o.equals("")) {
            throw new Exception(errorMsg);
        }
    }

    public static void requireNull(Object o, String errorMsg) throws IllegalArgumentException {
        if (o != null) {
            throw new IllegalArgumentException(errorMsg);
        }
    }

    /**
     * Checks if the provided string is blank
     *
     * @param s        the string to check
     * @param errorMsg the message to throw in an exception if the string is blank
     * @throws Exception if string is blank
     */
    public static void requireNotBlank(String s, String errorMsg) throws Exception {
        if (StringUtils.isBlank(s)) {
            throw new Exception(errorMsg);
        }
    }

    /**
     * Converts an entityPage to a pageDTO
     *
     * @param entityPage         Entity page containing result data
     * @param pageDTO            Empty pageDTO of the desired output type which is
     *                           populated with the results from the entityPage
     * @param mapperInstance     Instance of mapper for entity/dto type
     * @param verifiedSortColumn Field that is sorted upon
     * @param verifiedSortOrder  Whether sorting is ascending or descending
     * @param pluralResourceName Plural name of the resource the entity class
     *                           represents
     * @param <Entity>           Entity type
     * @param <DTO>              DTO type
     * @param <PageDTO>          PageDTO type
     * @param <Mapper>           Mapper Type
     * @return Returns pageDTO populated with the results of the entity page
     */
    public static <Entity, DTO, PageDTO extends BasePageDTO<DTO>, Mapper extends BaseMapper<DTO, Entity>> PageDTO transferToPageDTO(
            Page<Entity> entityPage, PageDTO pageDTO, Mapper mapperInstance, String verifiedSortColumn,
            String verifiedSortOrder, String pluralResourceName) {

        pageDTO.setHasContent(entityPage.hasContent());
        pageDTO.setHasNext(entityPage.hasNext());
        pageDTO.setHasPrevious(entityPage.hasPrevious());
        pageDTO.setFirst(entityPage.isFirst());
        pageDTO.setLast(entityPage.isLast());
        pageDTO.setTotalElements(entityPage.getTotalElements());
        pageDTO.setTotalPages(entityPage.getTotalPages());
        pageDTO.setData(mapperInstance.toDtoList(entityPage.toList()));
        pageDTO.setPerPage(entityPage.getSize());
        pageDTO.setPageNumber(entityPage.getNumber());
        pageDTO.setSize(entityPage.getSize());

        pageDTO.setPluralResourceName(pluralResourceName);

        if (verifiedSortOrder != null && !verifiedSortOrder.isEmpty() && verifiedSortColumn != null && !verifiedSortColumn.isEmpty()) {
            pageDTO.setSortOrder(verifiedSortOrder);
            pageDTO.setSortColumn(verifiedSortColumn);
            pageDTO.setSorted(true);
        }

        return pageDTO;
    }
}

