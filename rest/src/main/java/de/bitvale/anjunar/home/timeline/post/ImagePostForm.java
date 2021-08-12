package de.bitvale.anjunar.home.timeline.post;

import de.bitvale.anjunar.timeline.Image;
import de.bitvale.anjunar.timeline.ImagePost;
import de.bitvale.common.rest.api.Blob;
import de.bitvale.common.rest.api.meta.Input;
import de.bitvale.common.security.Identity;

import javax.persistence.EntityManager;

public class ImagePostForm extends AbstractPostForm {

    @Input(type = "image")
    private Blob image = new Blob();

    @Override
    public <E> E accept(AbstractPostFormVisitor<E> visitor) {
        return visitor.visit(this);
    }

    public Blob getImage() {
        return image;
    }

    public void setImage(Blob image) {
        this.image = image;
    }

    public static ImagePostForm factory(ImagePost form) {
        ImagePostForm post = new ImagePostForm();

        post.setImage(Blob.factory(form.getImage()));

        AbstractPostForm.abstractFactory(post, form);

        return post;
    }

    public static ImagePost updater(ImagePostForm resource, ImagePost post, Identity identity, EntityManager entityManager) {
        if (post.getImage() == null) {
            post.setImage((Image) Blob.updater(resource.getImage(), new Image()));
        } else {
            post.setImage((Image) Blob.updater(resource.getImage(), post.getImage()));
        }

        AbstractPostForm.abstractUpdater(resource, post, identity, entityManager);
        return post;
    }

}
