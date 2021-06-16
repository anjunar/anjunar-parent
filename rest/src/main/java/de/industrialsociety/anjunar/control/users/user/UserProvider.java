package de.industrialsociety.anjunar.control.users.user;

import de.industrialsociety.common.rest.api.jaxrs.AbstractRestPredicateProvider;
import de.industrialsociety.common.security.*;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.UUID;

public class UserProvider extends AbstractRestPredicateProvider<UUID, Role> {
    @Override
    public Predicate build(UUID value, Identity identity, EntityManager entityManager, CriteriaBuilder builder, Root<Role> root, CriteriaQuery<?> query) {
        if (value == null) {
            return builder.conjunction();
        }
        SetJoin<Role, Relationship> join = root.join(Role_.relationships);
        Path<User> path = join.get(Relationship_.user);
        Path<UUID> uuidPath = path.get(User_.id);

        return builder.equal(uuidPath, value);
    }
}
