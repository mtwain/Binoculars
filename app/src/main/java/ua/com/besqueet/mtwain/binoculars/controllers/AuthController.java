package ua.com.besqueet.mtwain.binoculars.controllers;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.quickblox.auth.QBAuth;
import com.quickblox.auth.model.QBSession;
import com.quickblox.chat.QBChatService;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;
import java.util.List;

import ua.com.besqueet.mtwain.binoculars.ErrorCodes;
import ua.com.besqueet.mtwain.binoculars.events.auth.LoginFailEvent;
import ua.com.besqueet.mtwain.binoculars.events.auth.LoginSuccessEvent;
import ua.com.besqueet.mtwain.binoculars.events.auth.SignUpFailEvent;

public enum AuthController implements ErrorCodes{
    INSTANCE;

    private final String L = "AuthController";
    private QBChatService chatService;
    private String login = "";
    private String password = "";
    private ArrayList<String>logins = new ArrayList<>();

    public ArrayList<String> getLogins() {
        return logins;
    }

    public void addLoginToList(String login){
        logins.add(login);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void signIn(final String login, final String password){
        final QBUser user = new QBUser(login,password);
        QBAuth.createSession(user, new QBEntityCallbackImpl<QBSession>() {
            @Override
            public void onSuccess(QBSession session, Bundle params) {
                user.setId(session.getUserId());
                UsersController.INSTANCE.setUser(user);
                setLogin(login);
                setPassword(password);
                addLoginToList(login);
                signInToChatService();

            }

            @Override
            public void onError(List<String> errors) {
                BusController.INSTANCE.getBus().post(new LoginFailEvent(errors.get(0)));
                showLogErrors(errors);
            }
        });
    }


    private void showLogErrors(List<String> errors){
        for (int i=0; i<errors.size();i++){
            Log.d(L,i+". "+errors.get(i));
        }
    }

    private void signInToChatService(){
        if (!QBChatService.isInitialized()) {
            QBChatService.init(ContextController.INSTANCE.getMainActivity());
            chatService = QBChatService.getInstance();
        }
        if(!chatService.isLoggedIn()) {
            chatService.login(UsersController.INSTANCE.getUser(), new QBEntityCallbackImpl() {
                @Override
                public void onSuccess() {
                    Log.d(L, "Login Success");
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            BusController.INSTANCE.getBus().post(new LoginSuccessEvent());
                        }
                    });
                }

                @Override
                public void onError(List errors) {
                    Log.d(L, "On Error: " + errors.get(0).toString());
                }
            });
        }else {
            Log.d(L, "Already Logged in");
        }
    }

    public void createNewAccount(final String login, final String password){
        final QBUser user = new QBUser(login,password);
        QBUsers.signUp(user, new QBEntityCallbackImpl<QBUser>() {
            @Override
            public void onSuccess(QBUser user, Bundle args) {
                signIn(login, password);
            }

            @Override
            public void onError(List<String> errors) {
                BusController.INSTANCE.getBus().post(new SignUpFailEvent(errors.get(0)));
            }
        });
    }

    public void createAppSession(){
        QBAuth.createSession(new QBEntityCallbackImpl<QBSession>() {
            @Override
            public void onSuccess(QBSession session, Bundle params) {
                Log.d(L,"App session created");
            }

            @Override
            public void onError(List<String> errors) {
                Log.d(L,"App session error");
            }
        });
    }
}
