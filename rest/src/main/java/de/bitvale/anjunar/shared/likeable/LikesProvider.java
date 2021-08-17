package de.bitvale.anjunar.shared.likeable;

import de.bitvale.anjunar.pages.page.Question;
import de.bitvale.anjunar.shared.Likeable_;
import de.bitvale.anjunar.shared.users.user.UserSelect;
import de.bitvale.common.rest.api.jaxrs.AbstractRestPredicateProvider;
import de.bitvale.common.security.Identity;
import de.bitvale.common.security.User;
import de.bitvale.common.security.User_;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class LikesProvider extends AbstractRestPredicateProvider<Set<UserSelect>, Question> {
    @Override
    public Predicate build(Set<UserSelect> value, Identity identity, EntityManager entityManager, CriteriaBuilder builder, Root<Question> root, CriteriaQuery<?> query) {
        if (value != null && value.size() > 0) {
            Set<UUID> users = new HashSet<>();
            for (UserSelect userSelect : value) {
                User user = entityManager.find(User.class, userSelect.getId());
                users.add(user.getId());
            }
            return root.join(Likeable_.likes).get(User_.id).in(users);
        }
        return builder.conjunction();
    }
}
