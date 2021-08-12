package de.bitvale.anjunar.pages.page.questions;

import de.bitvale.anjunar.pages.page.Question;
import de.bitvale.anjunar.pages.page.Question_;
import de.bitvale.common.rest.api.jaxrs.AbstractRestPredicateProvider;
import de.bitvale.common.security.Identity;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.time.ZoneId;

public class CreatedProvider extends AbstractRestPredicateProvider<String, Question> {
    @Override
    public Predicate build(String value, Identity identity, EntityManager entityManager, CriteriaBuilder builder, Root<Question> root, CriteriaQuery<?> query) {
        if (value == null) {
            return builder.conjunction();
        }
        LocalDate localDate = LocalDate.parse(value);
        return builder.between(root.get(Question_.created), localDate.atStartOfDay(ZoneId.systemDefault()).toInstant(), localDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}
