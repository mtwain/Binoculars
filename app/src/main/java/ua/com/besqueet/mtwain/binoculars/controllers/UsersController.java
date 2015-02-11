package ua.com.besqueet.mtwain.binoculars.controllers;

import com.quickblox.users.model.QBUser;


public enum UsersController {
    INSTANCE;

    private QBUser user;

    public QBUser getUser() {
        return user;
    }

    public void setUser(QBUser user) {
        this.user = user;
    }
}
