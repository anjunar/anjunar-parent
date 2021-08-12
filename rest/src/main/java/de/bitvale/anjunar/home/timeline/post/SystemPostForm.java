package de.bitvale.anjunar.home.timeline.post;

import de.bitvale.anjunar.timeline.SystemPost;
import de.bitvale.common.security.Identity;

import javax.persistence.EntityManager;

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

    public static SystemPostForm factory(SystemPost post) {
        SystemPostForm form = new SystemPostForm();
        form.setHash(post.getHash());
        AbstractPostForm.abstractFactory(form, post);
        return form;
    }

    public static SystemPost updater(SystemPostForm resource, SystemPost post, Identity identity, EntityManager entityManager) {
        post.setHash(resource.getHash());
        AbstractPostForm.abstractUpdater(resource, post, identity, entityManager);
        return post;
    }

}
