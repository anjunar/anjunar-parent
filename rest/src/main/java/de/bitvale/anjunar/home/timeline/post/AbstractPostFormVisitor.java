package de.bitvale.anjunar.home.timeline.post;

public interface AbstractPostFormVisitor<E> {
    E visit(LinkPostForm form);

    E visit(ImagePostForm form);

    E visit(TextPostForm form);

    E visit(SystemPostForm post);
}
