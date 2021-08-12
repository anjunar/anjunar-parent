package de.bitvale.anjunar.control.users.user;

import de.bitvale.common.rest.api.jaxrs.AbstractRestPredicateProvider;
import de.bitvale.common.security.*;
import de.bitvale.common.security.*;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.UUID;

public class UserProvider extends AbstractRestPredicateProvider<UUID, Role> {
    @Override
    public Predicate build(UUID value, Identity identity, EntityManager entityManager, CriteriaBuilder builder, Root<Role> root, CriteriaQuery<?> query) {
        if (value == null) {
            return builder.conjunction();
        }
        SetJoin<Role, User> join = root.join(Role_.users);
        Path<UUID> uuidPath = join.get(User_.id);

        return builder.equal(uuidPath, value);
    }
}
