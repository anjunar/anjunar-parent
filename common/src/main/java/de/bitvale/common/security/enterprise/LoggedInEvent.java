package de.bitvale.common.security.enterprise;

import de.bitvale.common.security.User;

public class LoggedInEvent {

    private final User user;

    public LoggedInEvent(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
