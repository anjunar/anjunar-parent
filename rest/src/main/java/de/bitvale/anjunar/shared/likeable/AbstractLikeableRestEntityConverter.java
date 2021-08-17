package de.bitvale.anjunar.shared.likeable;

import de.bitvale.anjunar.shared.Likeable;
import de.bitvale.anjunar.shared.users.user.UserSelect;
import de.bitvale.common.rest.api.AbstractRestEntityConverter;
import de.bitvale.common.security.Identity;
import de.bitvale.common.security.User;

import javax.persistence.EntityManager;

public class AbstractLikeableRestEntityConverter<E extends Likeable, R extends AbstractLikeableRestEntity> extends AbstractRestEntityConverter<E, R> {

    @Override
    public R factory(R restEntity, E entity) {
        restEntity.setViews(entity.getViews());
        for (User user : entity.getLikes()) {
            restEntity.getLikes().add(UserSelect.factory(user));
        }
        return super.factory(restEntity, entity);
    }

    @Override
    public E updater(R restEntity, E entity, EntityManager entityManager, Identity identity) {
        entity.setViews(restEntity.getViews());
        entity.getLikes().clear();
        for (UserSelect like : restEntity.getLikes()) {
            entity.getLikes().add(entityManager.find(User.class, like.getId()));
        }
        return entity;
    }
}
