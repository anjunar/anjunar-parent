package de.industrialsociety.anjunar.pages.page.forum.topic.replies;

import de.industrialsociety.anjunar.pages.page.forum.Reply_;
import de.industrialsociety.anjunar.pages.page.forum.Topic_;
import de.industrialsociety.common.rest.api.jaxrs.AbstractRestPredicateProvider;
import de.industrialsociety.common.security.Identity;
import de.industrialsociety.anjunar.pages.page.forum.Reply;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.UUID;

public class TopicProvider extends AbstractRestPredicateProvider<UUID, Reply> {
    @Override
    public Predicate build(UUID value, Identity identity, EntityManager entityManager, CriteriaBuilder builder, Root<Reply> root, CriteriaQuery<?> query) {
        if (value == null) {
            return builder.conjunction();
        }
        return builder.equal(root.get(Reply_.topic).get(Topic_.id), value);
    }
}
