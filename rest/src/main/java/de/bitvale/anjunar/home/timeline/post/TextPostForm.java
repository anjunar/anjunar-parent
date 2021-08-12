package de.bitvale.anjunar.home.timeline.post;

import de.bitvale.anjunar.timeline.AbstractPost;
import de.bitvale.anjunar.timeline.TextPost;
import de.bitvale.common.security.Identity;

import javax.persistence.EntityManager;

public class TextPostForm extends AbstractPostForm {

    @Override
    public <E> E accept(AbstractPostFormVisitor<E> visitor) {
        return visitor.visit(this);
    }

    public static TextPostForm factory(TextPost post) {
        TextPostForm form = new TextPostForm();
        AbstractPostForm.abstractFactory(form, post);
        return form;
    }

    public static TextPost updater(TextPostForm resource, TextPost post, Identity identity, EntityManager entityManager) {
        AbstractPostForm.abstractUpdater(resource, post, identity, entityManager);
        return post;
    }

}
