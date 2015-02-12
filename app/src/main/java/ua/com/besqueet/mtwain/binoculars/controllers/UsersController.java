package ua.com.besqueet.mtwain.binoculars.controllers;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.request.QBPagedRequestBuilder;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;
import java.util.List;

import ua.com.besqueet.mtwain.binoculars.events.LoadUsersFinishEvent;


public enum UsersController {
    INSTANCE;

    private QBUser user;
    private final String L = "UsersController";
    public ArrayList<QBUser> allUsers = new ArrayList<>();

    public QBUser getUser() {
        return user;
    }

    public void setUser(QBUser user) {
        this.user = user;
    }

    public void loadAllUsers(){
        QBPagedRequestBuilder pagedRequestBuilder = new QBPagedRequestBuilder();
        pagedRequestBuilder.setPage(1);
        pagedRequestBuilder.setPerPage(50);

        QBUsers.getUsers(pagedRequestBuilder, new QBEntityCallbackImpl<ArrayList<QBUser>>() {
            @Override
            public void onSuccess(ArrayList<QBUser> users, Bundle params) {
                allUsers = users;
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        BusController.INSTANCE.getBus().post(new LoadUsersFinishEvent());
                    }
                });
            }

            @Override
            public void onError(List<String> errors) {
                Log.d(L, "Error getting users");
            }
        });
    }

}
