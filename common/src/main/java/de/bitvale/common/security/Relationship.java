package de.bitvale.common.security;

import de.bitvale.common.ddd.AbstractEntity;

import javax.persistence.*;

@Entity
@Table(name = "ge_relationship")
public class Relationship extends AbstractEntity {

    @ManyToOne
    private User user;

    @ManyToOne
    private Role role;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Role getGroup() {
        return role;
    }

    public void setGroup(Role role) {
        this.role = role;
    }
}
