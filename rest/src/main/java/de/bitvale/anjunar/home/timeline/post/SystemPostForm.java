package de.bitvale.anjunar.home.timeline.post;

import de.bitvale.anjunar.timeline.SystemPost;
import de.bitvale.common.security.Identity;

import javax.persistence.EntityManager;
import java.awt.desktop.SystemSleepEvent;

public class SystemPostForm extends AbstractPostForm{

    private String hash;

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    @Override
    public <E> E accept(AbstractPostFormVisitor<E> visitor) {
        return visitor.visit(this);
    }

    private static class SystemPostFormConverter extends AbstractPostFormConverter<SystemPost, SystemPostForm> {

        public static SystemPostFormConverter INSTANCE = new SystemPostFormConverter();

        public SystemPostForm factory(SystemPostForm form, SystemPost post) {
            form.setHash(post.getHash());
            return super.factory(form, post);
        }

        public SystemPost updater(SystemPostForm resource, SystemPost post, Identity identity, EntityManager entityManager) {
            post.setHash(resource.getHash());
            return super.updater(resource, post, entityManager, identity);
        }
    }

    public static SystemPostForm factory(SystemPost post) {
        return SystemPostFormConverter.INSTANCE.factory(new SystemPostForm(), post);
    }

    public static SystemPost updater(SystemPostForm resource, SystemPost post, Identity identity, EntityManager entityManager) {
        return SystemPostFormConverter.INSTANCE.updater(resource, post, entityManager, identity);
    }

}
