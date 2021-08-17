package de.bitvale.anjunar.shared;

import de.bitvale.common.ddd.AbstractEntity;
import de.bitvale.common.security.User;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

@MappedSuperclass
public class Likeable extends AbstractEntity {

    @Column(name = "do_views")
    @Audited(targetAuditMode = NOT_AUDITED)
    private Integer views = 0;

    @ManyToMany
    @Audited(targetAuditMode = NOT_AUDITED)
    private final Set<User> likes = new HashSet<>();

    @PostLoad
    public void postLoad() {
        if (views == null) {
            views = 0;
        }
        views = views + 1;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public Set<User> getLikes() {
        return likes;
    }
}
