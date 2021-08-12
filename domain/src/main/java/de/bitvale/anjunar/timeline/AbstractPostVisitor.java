package de.bitvale.anjunar.timeline;

public interface AbstractPostVisitor<E> {
    E visit(ImagePost post);

    E visit(LinkPost post);

    E visit(TextPost post);

    E visit(SystemPost post);
}
