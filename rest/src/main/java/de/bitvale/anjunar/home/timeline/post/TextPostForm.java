package de.bitvale.anjunar.home.timeline.post;

import de.bitvale.anjunar.timeline.TextPost;
import de.bitvale.common.security.Identity;

import javax.persistence.EntityManager;

public class TextPostForm extends AbstractPostForm {

    @Override
    public <E> E accept(AbstractPostFormVisitor<E> visitor) {
        return visitor.visit(this);
    }

    private static class TextPostFormConverter extends AbstractPostFormConverter<TextPost, TextPostForm> {

        public static TextPostFormConverter INSTANCE = new TextPostFormConverter();

        public TextPostForm factory(TextPostForm form, TextPost post) {
            return super.factory(form, post);
        }

        public TextPost updater(TextPostForm resource, TextPost post, Identity identity, EntityManager entityManager) {
            return super.updater(resource, post, entityManager, identity);
        }
    }

    public static TextPostForm factory(TextPost post) {
        return TextPostFormConverter.INSTANCE.factory(new TextPostForm(), post);
    }

    public static TextPost updater(TextPostForm resource, TextPost post, Identity identity, EntityManager entityManager) {
        return TextPostFormConverter.INSTANCE.updater(resource, post, entityManager, identity);
    }

}
