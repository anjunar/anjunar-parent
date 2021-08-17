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

    public static class ImagePostFormConverter extends AbstractPostFormConverter<ImagePost, ImagePostForm> {

        public static ImagePostFormConverter INSTANCE = new ImagePostFormConverter();

        public ImagePostForm factory(ImagePostForm post, ImagePost form) {
            post.setImage(Blob.factory(form.getImage()));

            return super.factory(post, form);
        }

        public ImagePost updater(ImagePostForm resource, ImagePost post, EntityManager entityManager, Identity identity) {
            if (post.getImage() == null) {
                post.setImage((Image) Blob.updater(resource.getImage(), new Image()));
            } else {
                post.setImage((Image) Blob.updater(resource.getImage(), post.getImage()));
            }

            return super.updater(resource, post, entityManager, identity);
        }
    }

    public static ImagePostForm factory(ImagePost form) {
        return new ImagePostFormConverter().factory(new ImagePostForm(), form);
    }

    public static ImagePost updater(ImagePostForm resource, ImagePost post, Identity identity, EntityManager entityManager) {
        return ImagePostFormConverter.INSTANCE.updater(resource, post, entityManager, identity);
    }

}
