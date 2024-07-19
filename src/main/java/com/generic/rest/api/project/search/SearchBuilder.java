package com.generic.rest.api.project.search;

import com.generic.rest.api.project.mappers.base.BaseMapper;
import com.generic.rest.api.project.search.dto.base.AuditableSearchParameters;
import com.generic.rest.api.project.search.dto.base.BasePageDTO;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Encapsulation of boilerplate search code to allow easier searches to be
 * written based on a series of specifications
 *
 * @see GenericEntitySpecifications
 */
public class SearchBuilder<T> {
    private final EntityManager entityManager;
    private final CriteriaBuilder criteriaBuilder;
    private final CriteriaQuery<T> query;
    private final Root<T> root;
    private final List<Predicate> predicates;
    private final int pageNumber;
    private final int pageSize;
    Class<T> classOfT;
    private List<Order> orderList;
    private Pageable pageable;

    private boolean printQuery;
    private Logger logger;

    /**
     * Creates and initializes a SearchBuilder
     *
     * @param pageNumber    the page number of data to be returned
     * @param pageSize      the number of entries to be included in a page
     * @param entityManager an entity manager object used to create the search
     * @param classOfT      the class of the type of entity the search builder is
     *                      searching for
     */
    public SearchBuilder(int pageNumber, int pageSize, EntityManager entityManager, Class<T> classOfT) {
        this.classOfT = classOfT;
        this.entityManager = entityManager;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        criteriaBuilder = entityManager.getCriteriaBuilder();
        query = criteriaBuilder.createQuery(classOfT);
        root = query.from(classOfT);
        predicates = new ArrayList<>();
    }

    /**
     * Creates and initializes a SearchBuilder
     *
     * @param pageNumber    the page number of data to be returned
     * @param pageSize      the number of entries to be included in a page
     * @param entityManager an entity manager object used to create the search
     * @param classOfT      the class of the type of entity the search builder is
     *                      searching for
     * @param printQuery    a flag indicating to print the query that is built
     */
    public SearchBuilder(int pageNumber, int pageSize, EntityManager entityManager, Class<T> classOfT,
                         boolean printQuery, Logger logger) {
        this.classOfT = classOfT;
        this.entityManager = entityManager;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.printQuery = printQuery;
        this.logger = logger;

        criteriaBuilder = entityManager.getCriteriaBuilder();
        query = criteriaBuilder.createQuery(classOfT);
        root = query.from(classOfT);
        predicates = new ArrayList<>();
    }

    /**
     * Takes an input specification and adds it to a list to be used in the search
     *
     * @param spec an input specification parameter used to filter the search
     * @return whether the specification was sucessfully added
     */
    public boolean addSearchFilter(Specification<T> spec) {
        if (spec == null) {
            return false;
        }

        predicates.add(spec.toPredicate(root, query, criteriaBuilder));

        return true;
    }

    public boolean applyAuditableSearchParameters(AuditableSearchParameters parameters) {
        if (parameters == null) {
            return false;
        }

        if (!parameters.isIncludeDeleted()) {
            addSearchFilter(GenericEntitySpecifications.matchBoolean(false, "deleted"));
        }

        addSearchFilter(GenericEntitySpecifications.isDateInRange(parameters.getCreationDateRange(), "creationDate"));
        addSearchFilter(GenericEntitySpecifications.isDateInRange(parameters.getModifiedDateRange(), "modifiedDate"));

        return true;
    }

    /**
     * Determines how the results should be sorted
     *
     * @param sortOrder "asc" to sort in ascending order or "desc" to sort in
     *                  descending
     * @param sort      the table column (referred to by entity property name) to
     *                  sort the results by
     */
    public void setSortCriterion(String sortOrder, String sort) {
        if (StringUtils.isBlank(sort)) {
            logger.warn("Tried to setSortCriterion with sortOrder of '{}' and sortColumn of '{}'", sortOrder, sort);
            return;
        }

        orderList = new ArrayList<>();
        var order = getOrder(criteriaBuilder, isAscending(sortOrder), getPathForSort(sort));

        pageable = PageRequest.of(pageNumber, pageSize, getSortObject(sort, sortOrder));
        orderList.add(order);
    }

    private Path<Object> getPathForSort(String sort) {
        var nestedPaths = Arrays.stream(sort.split("\\.")).filter(StringUtils::isNotBlank).collect(Collectors.toList());

        Path<Object> sortPath = null;

        for (var path : nestedPaths) {
            if (sortPath == null) {
                sortPath = root.get(path);
                continue;
            }

            sortPath = sortPath.get(path);
        }

        return sortPath;
    }

    private Order getOrder(CriteriaBuilder criteriaBuilder, boolean isAscending, Path<Object> path) {
        return isAscending ? criteriaBuilder.asc(path) : criteriaBuilder.desc(path);
    }

    private Sort getSortObject(String sortColumn, String sortOrder) {
        return isAscending(sortOrder) ? Sort.by(sortColumn).ascending() : Sort.by(sortColumn).descending();
    }

    private boolean isAscending(String sortOrder) {
        return StringUtils.equalsIgnoreCase(sortOrder, "asc") || StringUtils.equalsIgnoreCase(sortOrder, "ascending");
    }

    /**
     * Gets the results of the search request based on the provided sort criterion
     * and the given search filters.
     * <p>
     * If no sort criterion is provided, the method defaults to sorting by creation
     * date in descending order.
     *
     * @return A {@link Page} of objects extending {@link BaseEntity} which contain
     * the search results
     */
    public Page<T> get() {
        handleNullPageable();

        Predicate[] compiledPredicates = getCompiledPredicates();

        List<T> result = getResult(compiledPredicates, pageSize);

        long count = getCount(compiledPredicates);

        return new PageImpl<>(result, pageable, count);
    }

    /**
     * Gets the results of the search request based on the provided sort criterion
     * and the given search filters.
     * <p>
     * This method does not attempt to count the number of entities that match the
     * query and is more performant for large datasets. This endpoint supports our
     * existing page infrastructure for this case, but in the future we should
     * probably switch to using Slices for this case. {@link #getSlice() getSlice}
     * <p>
     * If no sort criterion is provided, the method defaults to sorting by creation
     * date in descending order.
     *
     * @return A {@link Page} of objects extending {@link BaseEntity} which contain
     * the search results
     */
    public Page<T> getWithoutPageCount() {

        handleNullPageable();

        List<T> result = getResult(getCompiledPredicates(), pageSize + 1);

        long fakeCount = (long) pageNumber * pageSize + result.size();

        if (result.size() > pageSize) {
            result = result.subList(0, result.size() - 1);
        }

        return new PageImpl<>(result, pageable, fakeCount);
    }

    /**
     * Returns the results of the search as a slice
     *
     * @return Slice containing the search data
     */
    public Slice<T> getSlice() {

        handleNullPageable();

        List<T> result = getResult(getCompiledPredicates(), pageSize + 1);

        boolean hasNext = result.size() > pageSize;

        if (hasNext) {
            result = result.subList(0, result.size() - 1);
        }

        return new SliceImpl<>(result, pageable, hasNext);
    }

    private Predicate[] getCompiledPredicates() {
        return predicates.toArray(new Predicate[0]);
    }

    private List<T> getResult(Predicate[] compiledPredicates, int pageSize) {
        query.select(root);
        query.where(compiledPredicates);
        query.orderBy(orderList);

        TypedQuery<T> typedQuery = entityManager.createQuery(query).setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageSize);

        if (printQuery && logger != null) {
            logger.info("Query from SearchBuilder => {}", typedQuery.unwrap(Query.class).getQueryString());
        }

        return typedQuery.getResultList();
    }

    private void handleNullPageable() {
        if (pageable == null) {
            orderList = new ArrayList<>();
            String sort = "creationDate";
            pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sort).descending());
            orderList.add(criteriaBuilder.desc(root.get(sort)));
        }
    }

    private long getCount(Predicate[] compiledPredicates) {
        CriteriaQuery<Long> countCriteria = criteriaBuilder.createQuery(Long.class);
        Root<T> countRoot = countCriteria.from(query.getResultType());
        Set<Join<T, ?>> joins = root.getJoins();

        for (Join<T, ?> join : joins) {
            countRoot.join(join.getAttribute().getName());
        }

        countCriteria.select(criteriaBuilder.count(countRoot));
        if (compiledPredicates != null)
            countCriteria.where(compiledPredicates);

        TypedQuery<Long> queryCount = entityManager.createQuery(countCriteria);

        if (printQuery && logger != null) {
            logger.info("Query from SearchBuilder => {}", queryCount.unwrap(Query.class).getQueryString());
        }

        return queryCount.getSingleResult();
    }

    /**
     * @param pageDTO            An empty page DTO of the type requested
     * @param mapperInstance     An instance of an appropriate mapper to convert the
     *                           DTO type to a corresponding entity and back
     * @param verifiedSortColumn The property name that is being sorted by
     * @param verifiedSortOrder  Whether the sort is ascending or descending
     * @param pluralResourceName The plural name of the type of entity that was
     *                           searched on.
     * @param <DTO>              The type of the DTO
     * @param <PageDTO>          The type of the page DTO
     * @param <Mapper>           The type of the mapper
     * @return A page DTO with the results of the search query
     */
    public <DTO, PageDTO extends BasePageDTO<DTO>, Mapper extends BaseMapper<DTO, T>> PageDTO getAsPageDTO(
            PageDTO pageDTO, Mapper mapperInstance, String verifiedSortColumn, String verifiedSortOrder,
            String pluralResourceName) {

        return getAsPageDTO(pageDTO, mapperInstance, verifiedSortColumn, verifiedSortOrder, pluralResourceName, true);
    }

    public <DTO, PageDTO extends BasePageDTO<DTO>, Mapper extends BaseMapper<DTO, T>> PageDTO getAsPageDTO(
            PageDTO pageDTO, Mapper mapperInstance, String verifiedSortColumn, String verifiedSortOrder,
            String pluralResourceName, boolean countEntities) {

        Page<T> entityPage;

        if (countEntities) {
            entityPage = get();
        } else {
            entityPage = getWithoutPageCount();
        }
        pageDTO.setCountEntities(!countEntities);
        return EntityUtilities.transferToPageDTO(entityPage, pageDTO, mapperInstance, verifiedSortColumn,
                verifiedSortOrder, pluralResourceName);
    }

}