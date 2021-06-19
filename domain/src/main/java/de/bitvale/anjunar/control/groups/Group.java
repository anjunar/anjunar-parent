package de.bitvale.anjunar.control.groups;

import de.bitvale.common.security.User;
import de.bitvale.common.ddd.AbstractAggregate;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "co_group")
public class Group extends AbstractAggregate {

    private String name;

    @ManyToOne
    private User owner;

    @OneToMany
    private final List<User> users = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<User> getUsers() {
        return users;
    }

    public void addUser(User user) {
        users.add(user);
    }
}
