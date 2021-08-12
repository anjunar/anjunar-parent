package de.bitvale.common.rest.api.jaxrs.provider;

import de.bitvale.common.rest.api.jaxrs.AbstractRestSortProvider;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class GenericSortProvider<E> extends AbstractRestSortProvider<List<String>, E> {
    @Override
    public List<Order> sort(List<String> value, EntityManager entityManager, CriteriaBuilder builder, Root<E> root) {
        List<Order> result = new ArrayList<>();

        if (value == null) {
            return result;
        }

        for (String sortExpression : value) {

            String[] sortSegment = sortExpression.split(":");

            Path cursor = cursor(root, sortSegment[0]);

            String direction = sortSegment[1];

            switch (direction) {
                case "asc": {
                    result.add(builder.asc(cursor));
                }
                break;
                case "desc": {
                    result.add(builder.desc(cursor));
                }
                break;
            }

        }

        return result;
    }
}
