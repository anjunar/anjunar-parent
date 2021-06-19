package de.bitvale.common.rest.api.search;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import de.bitvale.common.rest.api.search.predicate.*;
import de.bitvale.common.rest.api.search.sort.LevenstheinExpression;
import de.bitvale.common.rest.api.search.sort.NormalExpression;
import de.bitvale.common.rest.api.search.sort.SortExpression;
import de.bitvale.common.rest.api.search.predicate.*;
import org.apache.commons.lang.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Search {

    private int index;

    private int limit;

    private RestExpression expression = new NoopExpression();

    private List<SortExpression> sorting = new ArrayList<>();

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public RestExpression getExpression() {
        return expression;
    }

    public void setExpression(RestExpression expression) {
        this.expression = expression;
    }

    public List<SortExpression> getSorting() {
        return sorting;
    }

    public void setSorting(List<SortExpression> sorting) {
        this.sorting = sorting;
    }

    public static List<Order> sorting(List<SortExpression> sorting, CriteriaBuilder builder, Path<?> root) {
        List<Order> orders = new ArrayList<>();
        for (SortExpression sort : sorting) {
            orders.add(sort.accept(new SortVisitor() {
                @Override
                public Order visit(NormalExpression sort) {
                    if (sort.getAsc()) {
                        return builder.asc(cursor(root, sort.getPath()));
                    } else {
                        return builder.desc(cursor(root, sort.getPath()));
                    }
                }
                @Override
                public Order visit(LevenstheinExpression levenstheinExpression) {

                    List<Path> paths = Lists.newArrayList();
                    for (String path : levenstheinExpression.getPaths()) {
                        paths.add(cursor(root, path));
                    }
                    Expression<String> concat = builder.function("concat", String.class, Iterables.toArray(paths, Path.class));
                    Expression[] expressions = {
                            builder.literal(levenstheinExpression.getValue().toLowerCase()),
                            builder.lower(concat)};
                    Expression<Integer> levenshtein = builder.function("difference", Integer.class, expressions);

                    if (levenstheinExpression.getAsc()) {
                        return builder.asc(levenshtein);
                    } else {
                        return builder.desc(levenshtein);
                    }
                }
            }));
        }
        return orders;
    }

    public static PredicateVisitor visitorVisit(EntityManager entityManager, CriteriaBuilder builder, AbstractQuery<?> query, Expression<?> root, Map<String, Class<?>> tables) {
        return new PredicateVisitor() {

            @Override
            public Predicate visitAnd(AndExpression and) {
                List<Predicate> predicates = Lists.newArrayList();

                for (RestExpression expression : and.getExpressions()) {
                    Predicate predicate = (Predicate) expression.accept(this);
                    predicates.add(predicate);
                }

                return builder.and(Iterables.toArray(predicates, Predicate.class));
            }

            @Override
            public Predicate visitOr(OrExpression or) {
                List<Predicate> predicates = Lists.newArrayList();

                for (RestExpression expression : or.getExpressions()) {
                    Predicate predicate = (Predicate) expression.accept(this);
                    predicates.add(predicate);
                }

                return builder.or(Iterables.toArray(predicates, Predicate.class));

            }

            @Override
            public Predicate visitPath(PathExpression path) {
                Path cursor = cursor((Path<?>) root, path.getPath());
                return (Predicate) path.getExpression().accept(visitorVisit(entityManager, builder, query, cursor, tables));
            }

            @Override
            public Predicate visitLike(LikeExpression like) {
                if (StringUtils.isEmpty(like.getValue())) {
                    return builder.conjunction();
                }
                return builder.like(builder.lower((Expression<String>) root), like.getValue().toLowerCase() + "%");
            }

            @Override
            public Predicate visitNoop(NoopExpression noop) {
                return builder.conjunction();
            }

            @Override
            public Predicate visitEqual(EqualExpression equal) {
                return builder.equal(root, equal.getValue());
            }

            @Override
            public Predicate visitIn(InExpression in) {
                if (in.getValues() == null || in.getValues().isEmpty()) {
                    return builder.disjunction();
                }
                return root.in(in.getValues());
            }

            @Override
            public Subquery<?> visitSubQuery(SubQueryExpression subQueryExpression) {
                Class<?> fromClass = tables.get(subQueryExpression.getFrom());

                Metamodel metamodel = entityManager.getMetamodel();
                EntityType<?> entity = metamodel.entity(fromClass);
                EntityType<?> cursor = cursor(metamodel, entity, subQueryExpression.getPath());

                Subquery subquery = query.subquery(cursor.getJavaType());
                Root from = subquery.from(fromClass);
                Path<?> path = cursor(from, subQueryExpression.getPath());
                subquery.select(path).where(subQueryExpression.getExpression().accept(visitorVisit(entityManager, builder, subquery, from, tables)));
                return subquery;

            }

            @Override
            public Predicate visitInSelect(InSelectExpression inSelect) {
                return root.in(inSelect.getSubQuery().accept(this));
            }

            @Override
            public Predicate visitIsNull(IsNullExpression isNull) {
                return builder.isNull(root);
            }

            @Override
            public Predicate visitNot(NotExpression notExpression) {
                Expression<Boolean> booleanExpression = (Expression<Boolean>) notExpression.getExpression().accept(this);
                return builder.not(booleanExpression);
            }

            @Override
            public Expression<?> visitConcat(ConcatExpression concatExpression) {
                List<Path> paths = Lists.newArrayList();
                for (String path : concatExpression.getPaths()) {
                    paths.add(cursor((Path<?>) root, path));
                }
                Expression<String> concat = builder.function("concat", String.class, Iterables.toArray(paths, Path.class));
                return concatExpression.getExpression().accept(visitorVisit(entityManager, builder, query, concat, tables));
            }

            @Override
            public Expression<?> visitLevensthein(de.bitvale.common.rest.api.search.predicate.LevenstheinExpression levenstheinExpression) {
                Expression[] expressions = {
                        builder.literal(levenstheinExpression.getValue().toLowerCase()),
                        builder.lower((Expression<String>) root),
                        builder.literal(1),
                        builder.literal(24),
                        builder.literal(24)
                };

                Expression<Integer> levenshtein = builder.function("levenshtein", Integer.class, expressions);
                return levenstheinExpression.accept(visitorVisit(entityManager, builder, query, levenshtein, tables));
            }

        };
    }

    private static Path cursor(Path<?> path, String pathString) {
        if (StringUtils.isEmpty(pathString)) {
            return path;
        }
        String[] paths = pathString.split("\\.");
        Path<?> cursor = path;
        for (String segment : paths) {
            cursor = cursor.get(segment);
        }
        return cursor;
    }

    private static EntityType<?> cursor(Metamodel metamodel, EntityType<?> path, String pathString) {
        if (StringUtils.isEmpty(pathString)) {
            return path;
        }
        String[] paths = pathString.split("\\.");
        EntityType<?> cursor = path;
        for (String segment : paths) {
            Attribute<?, ?> attribute = cursor.getAttribute(segment);
            Class<?> javaType = attribute.getJavaType();
            cursor = metamodel.entity(javaType);
        }
        return cursor;
    }

 }
