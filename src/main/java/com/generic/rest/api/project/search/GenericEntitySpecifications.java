package com.generic.rest.api.project.search;

import com.generic.rest.api.project.search.entity.AuditableBaseEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Class containing a number of methods used to get a search specification based
 * on anything extending a BaseEntity
 *
 * @see SearchBuilder
 * @see BaseEntity
 */
public class GenericEntitySpecifications {

    /**
     * Creates a {@link Specification} which tries to search for text like the text
     * to match
     *
     * @param textToMatch the target string the database entries are compared
     *                    against
     * @param path        the entity field that is being compared. multiple inputs
     *                    can be used to specify an entity property in a linked
     *                    entity
     * @param <T>         the type of object (extending {@link BaseEntity} that
     *                    appears in the search result
     * @return {@link Specification} which can be used for general text searches
     */
    public static <T> Specification<T> includesMatchText(String textToMatch, String... path) {
        if (textToMatch == null || textToMatch.isEmpty()) {
            return null;
        }
        return (root, query, builder) -> builder.like(getSubProperty(root, path), "%" + textToMatch + "%");
    }

    /**
     * Creates a {@link Specification} which tries to search for text like the text
     * to match
     *
     * @param textToMatch the target string the database entries are compared
     *                    against
     * @param joinTb      the join table name
     * @param path        the entity field that is being compared. multiple inputs
     *                    can be used to specify an entity property in a linked
     *                    entity
     * @param <T>         the type of object (extending {@link BaseEntity} that
     *                    appears in the search result
     * @return {@link Specification} which can be used for general text searches
     */
    public static <T> Specification<T> includesMatchTextWithJoin(String textToMatch, String joinTb, String path) {
        if (textToMatch == null || textToMatch.isEmpty()) {
            return null;
        }
        return (root, query, builder) -> {
            Join<Object, Object> join = root.join(joinTb);
            return builder.like(builder.lower(join.get(path)), "%" + textToMatch + "%");
        };
    }

    /**
     * Creates a {@link Specification} which tries to search for text exactly
     * equaling the text to match
     *
     * @param textToMatch the target string the database entries are compared
     *                    against
     * @param path        the entity field that is being compared. multiple inputs
     *                    can be used to specify an entity property in a linked
     *                    entity
     * @param <T>         the type of object (extending {@link BaseEntity} that
     *                    appears in the search result
     * @return {@link Specification} which can be used for general text searches
     */
    public static <T> Specification<T> exactMatchText(String textToMatch, String... path) {
        if (textToMatch == null || textToMatch.equals("")) {
            return null;
        }
        return (root, query, builder) -> builder.equal(getSubProperty(root, path), textToMatch);
    }

    /**
     * Creates a {@link Specification} which tries to search for text exactly
     * equaling the text to match
     *
     * @param textToMatch the target int the database entries are compared against
     * @param path        the entity field that is being compared. multiple inputs
     *                    can be used to specify an entity property in a linked
     *                    entity
     * @param <T>         the type of object (extending {@link BaseEntity} that
     *                    appears in the search result
     * @return {@link Specification} which can be used for general text searches
     */
    public static <T> Specification<T> exactMatchText(Integer textToMatch, String... path) {
        if (textToMatch == null) {
            return null;
        }
        return (root, query, builder) -> builder.equal(getSubProperty(root, path), textToMatch);
    }

    /**
     * Creates a {@link Specification} which tries to search for text exactly
     * equaling the text to match
     *
     * @param textToMatch the target int the database entries are compared against
     * @param path        the entity field that is being compared. multiple inputs
     *                    can be used to specify an entity property in a linked
     *                    entity
     * @param <T>         the type of object (extending {@link BaseEntity} that
     *                    appears in the search result
     * @return {@link Specification} which can be used for general text searches
     */
    public static <T> Specification<T> exactMatchText(Double textToMatch, String... path) {
        if (textToMatch == null) {
            return null;
        }
        return (root, query, builder) -> builder.equal(getSubProperty(root, path), textToMatch);
    }

    /**
     * Creates a {@link Specification} which tries to search for text exactly
     * equaling the text to match
     *
     * @param textToMatch the target string the database entries are compared
     *                    against
     * @param joinTb      the join table name
     * @param path        the entity field that is being compared. multiple inputs
     *                    can be used to specify an entity property in a linked
     *                    entity
     * @param <T>         the type of object (extending {@link BaseEntity} that
     *                    appears in the search result
     * @return {@link Specification} which can be used for general text searches
     */
    public static <T> Specification<T> exactMatchTextWithJoin(String textToMatch, String joinTb, String path) {
        if (textToMatch == null || textToMatch.equals("")) {
            return null;
        }
        return (root, query, builder) -> {
            Join<Object, Object> join = root.join(joinTb);
            return builder.equal(builder.lower(join.get(path)), textToMatch);
        };
    }

    // filter all the parent with no reference stored in relational table.
    public static <T> Specification<T> fetchRelatedEntitiesWithJoin(String joinTb, String path) {
        return (root, query, builder) -> {
            Join<Object, Object> join = root.join(joinTb, JoinType.INNER);
            return builder.isNotNull(join.get(path));
        };
    }

    public static <T> Specification<T> fetchNestNullAndNotNullEntitiesWithJoin(String joinTb, String path) {
        return (root, query, builder) -> {
            Join<Object, Object> join = root.join(joinTb, JoinType.LEFT);
            return builder.or(builder.isNull(join.get(path)), builder.isNotNull(join.get(path)));
        };
    }

    public static <T> Specification<T> isNullWithJoin(String joinTb, String path) {
        return (root, query, builder) -> {
            Join<Object, Object> join = root.join(joinTb, JoinType.LEFT);
            return builder.isNull(join.get(path));
        };
    }

    public static <T> Specification<T> isNotNullWithJoin(String joinTb, String path) {
        return (root, query, builder) -> {
            Join<Object, Object> join = root.join(joinTb, JoinType.LEFT);
            return builder.isNotNull(join.get(path));
        };
    }

    /**
     * @param booleanToMatch the target boolean the database entries are compared
     *                       against
     * @param path           the entity field that is being compared. multiple
     *                       inputs can be used to specify an entity property in a
     *                       linked entity
     * @param <T>            the type of object (extending {@link BaseEntity} that
     *                       appears in the search results
     * @return {@link Specification} which can be used for boolean searches
     */
    public static <T> Specification<T> matchBoolean(Boolean booleanToMatch, String... path) {
        if (booleanToMatch == null) {
            return null;
        }

        return (root, query, builder) -> builder.equal(getSubProperty(root, path), booleanToMatch);
    }

    /**
     * @param booleanToMatch the target boolean the database entries are compared
     *                       against
     * @param joinTb         the join table name
     * @param path           the entity field that is being compared. multiple
     *                       inputs can be used to specify an entity property in a
     *                       linked entity
     * @param <T>            the type of object (extending {@link BaseEntity} that
     *                       appears in the search results
     * @return {@link Specification} which can be used for boolean searches
     */
    public static <T> Specification<T> matchBooleanWithJoin(Boolean booleanToMatch, String joinTb, String path) {
        if (booleanToMatch == null) {
            return null;
        }
        return (root, query, builder) -> {
            Join<Object, Object> join = root.join(joinTb);
            return builder.equal(builder.lower(join.get(path)), booleanToMatch);
        };
    }

    /**
     * Creates a {@link Specification} which tries to search for matching objects
     * within a list of objects
     *
     * @param object the list of objects being matched against
     * @param path   the entity field that is being compared. multiple inputs can be
     *               used to specify an entity property in a linked entity
     * @param <T>    the type of object (extending {@link BaseEntity} that appears
     *               in the search result
     * @return {@link Specification} which can be used for general text searches
     */
    public static <T, Y> Specification<T> matchObjectIn(Y object, String... path) {
        if (object == null) {
            return null;
        }
        return (root, query, builder) -> getSubProperty(root, path).in(object);
    }

    /**
     * Creates a {@link Specification} which tries to search for matching objects
     * within a list of objects
     *
     * @param objects the list of objects being matched against
     * @param path    the entity field that is being compared. multiple inputs can
     *                be used to specify an entity property in a linked entity
     * @param <T>     the type of object (extending {@link BaseEntity} that appears
     *                in the search result
     * @return {@link Specification} which can be used for general text searches
     */
    public static <T, Y> Specification<T> matchObjectsIn(List<Y> objects, String... path) {
        if (objects == null || objects.size() == 0) {
            return null;
        }
        return (root, query, builder) -> getSubProperty(root, path).in(objects);
    }

    /**
     * Creates a {@link Specification} which tries to search for matching object
     *
     * @param object the object being matched against
     * @param joinTb the join table name
     * @param path   the entity field that is being compared. multiple inputs can be
     *               used to specify an entity property in a linked entity
     * @param <T>    the type of object (extending {@link BaseEntity} that appears
     *               in the search result
     * @return {@link Specification} which can be used for general text searches
     */
    public static <T, Y> Specification<T> matchObjectInWithJoin(Y object, String joinTb, String path) {
        if (object == null) {
            return null;
        }
        return (root, query, builder) -> {
            Join<Object, Object> join = root.join(joinTb);
            return join.get(path).in(object);
        };
    }

    /**
     * Creates a {@link Specification} which tries to search for matching object
     * within a list of objects
     *
     * @param objects the object being matched against
     * @param joinTb  the join table name
     * @param path    the entity field that is being compared. multiple inputs can
     *                be used to specify an entity property in a linked entity
     * @param <T>     the type of object (extending {@link BaseEntity} that appears
     *                in the search result
     * @return {@link Specification} which can be used for general text searches
     */
    public static <T, Y> Specification<T> matchObjectsInWithJoin(List<Y> objects, String joinTb, String path) {
        if (objects == null || objects.size() == 0) {
            return null;
        }
        return (root, query, builder) -> {
            Join<Object, Object> join = root.join(joinTb);
            return join.get(path).in(objects);
        };
    }

    public static <T, Y> Specification<T> exactMatchObjectsInWithJoin(Y textToMatch, String joinTb, String path) {
        if (textToMatch == null || textToMatch.equals("")) {
            return null;
        }
        return (root, query, builder) -> {
            Join<Object, Object> join = root.join(joinTb);
            return builder.equal(join.get(path), textToMatch);
        };
    }

    /**
     * Creates a {@link Specification} which tries to search for matching non null
     * or empty fields
     *
     * @param path the entity field that is being compared. multiple inputs can be
     *             used to specify an entity property in a linked entity
     * @param <T>  the type of object (extending {@link BaseEntity} that appears in
     *             the search result
     * @return {@link Specification} which can be used for general text searches
     */
    public static <T, Y> Specification<T> isNotNullOrEmpty(String... path) {
        return (root, query, builder) -> builder.and(builder.isNotNull(getSubProperty(root, path)),
                builder.notEqual(getSubProperty(root, path), ""));
    }

    /**
     * Creates a {@link Specification} which tries to search for matching non null
     * fields
     *
     * @param path the entity field that is being compared. multiple inputs can be
     *             used to specify an entity property in a linked entity
     * @param <T>  the type of object (extending {@link BaseEntity} that appears in
     *             the search result
     * @return {@link Specification} which can be used for general text searches
     */
    public static <T, Y> Specification<T> isNotNull(String... path) {
        return (root, query, builder) -> getSubProperty(root, path).isNotNull();
    }

    public static <T> Specification<T> isNotEqual(String textToMatch, String... path) {
        if (textToMatch == null || textToMatch.isEmpty()) {
            return null;
        }
        return (root, query, builder) -> builder.notEqual(getSubProperty(root, path), textToMatch);
    }

    public static <T, Y> Specification<T> isNullOrExactMatchText(String textToMatch, String... path) {
        return (root, query, builder) -> builder.or(builder.isNull(getSubProperty(root, path)),
                builder.equal(getSubProperty(root, path), textToMatch));
    }

    /**
     * Creates a {@link Specification} which tries to search for matching null
     * fields
     *
     * @param path the entity field that is being compared. multiple inputs can be
     *             used to specify an entity property in a linked entity
     * @param <T>  the type of object (extending {@link BaseEntity} that appears in
     *             the search result
     * @return {@link Specification} which can be used for general text searches
     */
    public static <T, Y> Specification<T> isNull(String... path) {
        return (root, query, builder) -> getSubProperty(root, path).isNull();
    }

    /**
     * Creates a {@link Specification} which tries to search for matching date
     * fields
     *
     * @param from target from Date being matched against
     * @param to   target to Date being matched against
     * @param path the entity field that is being compared.
     * @param <T>  the type of object (extending {@link BaseEntity} that appears in
     *             the search result
     * @return {@link Specification} which can be used for general text searches
     */
    public static <T> Specification<T> isDateInRange(Date from, Date to, String path) {
        if (from == null && to == null) {
            return null;
        }

        return (root, query, builder) -> builder.and(builder.isNotNull(root.get(path)),
                builder.between(root.get(path), from, to));
    }

    public static <T> Specification<T> isDateInRange(Date from, Date to, String... path) {
        if (from == null && to == null) {
            return null;
        }

        return (root, query, builder) -> builder.and(builder.isNotNull(getSubProperty(root, path)),
                builder.between(getSubProperty(root, path), from, to));
    }

    public static <T> Specification<T> matchesRegexPattern(String pattern, String... path) {
        if (StringUtils.isBlank(pattern)) {
            return null;
        }

        try {
            Pattern.compile(pattern);
        } catch (PatternSyntaxException ignored) {
            return null;
        }

        return (root, query, builder) -> builder.equal(
                builder.function("regexp", Integer.class, getSubProperty(root, path), builder.literal(pattern)), 1);
    }

    public static <T> Specification<T> isDateInRange(DateRange range, String path) {
        return range == null ? null : isDateInRange(range.getStartDate(), range.getEndDate(), path);
    }

    public static <T> Specification<T> isDateInRange(DateRange range, String... path) {
        return range == null ? null : isDateInRange(range.getStartDate(), range.getEndDate(), path);

    }

    /**
     * Creates a {@link Specification} which tries to search for entities whose
     * field (specified by the path) evaluate to true for the given comparison
     * operation againt the target value
     *
     * @param operation enum specifying what type of comparison should be used
     * @param target    value that the entity property is compared to
     * @param path      the entity field that is being compared. multiple inputs can
     *                  be used to specify an entity property in a linked entity
     * @param <T>       the type of object (extending {@link BaseEntity} that
     *                  appears in the search result
     * @return {@link Specification} which can be used for general text searches
     */
    public static <T, Y extends Comparable<Y>> Specification<T> compareField(ComparativeOperation operation, Y target,
                                                                             String... path) {
        if (target == null) {
            return null;
        }
        switch (operation) {
            case GT:
                return (root, query, builder) -> builder.greaterThan(getSubProperty(root, path), target);
            case GTE:
                return (root, query, builder) -> builder.greaterThanOrEqualTo(getSubProperty(root, path), target);
            case E:
                return (root, query, builder) -> builder.equal(getSubProperty(root, path), target);
            case LT:
                return (root, query, builder) -> builder.lessThan(getSubProperty(root, path), target);
            case LTE:
                return (root, query, builder) -> builder.lessThanOrEqualTo(getSubProperty(root, path), target);
            default:
                throw new IllegalArgumentException("Operation cannot be null");
        }
    }

    private static <T, Y> Path<Y> getSubProperty(Root<T> root, String[] pathNames) {
        Path<Y> path = root.get(pathNames[0]);

        for (int i = 1; i < pathNames.length; i++) {
            path = path.get(pathNames[i]);
        }

        return path;
    }

    // TODO: Java Doc + rename to isDeleted... maybe combine to matchActiveStatus?
    public static <T extends AuditableBaseEntity> Specification<T> isActive() {
        return (root, query, builder) -> builder.equal(root.get("deleted"), false);
    }

    // TODO: Java Doc + rename to isNotDeleted... maybe combine to
    // matchActiveStatus?
    public static <T extends AuditableBaseEntity> Specification<T> isNotActive() {
        return (root, query, builder) -> builder.equal(root.get("deleted"), true);
    }

}
