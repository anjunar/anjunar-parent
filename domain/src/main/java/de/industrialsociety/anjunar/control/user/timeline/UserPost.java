package de.industrialsociety.anjunar.control.user.timeline;

import de.industrialsociety.anjunar.timeline.Post;
import de.industrialsociety.common.security.User;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "pa_user_post")
public class UserPost extends Post {

    @ManyToOne
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
