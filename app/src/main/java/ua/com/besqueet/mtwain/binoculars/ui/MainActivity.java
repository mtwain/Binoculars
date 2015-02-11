package ua.com.besqueet.mtwain.binoculars.ui;

import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.quickblox.core.QBSettings;
import com.squareup.otto.Bus;

import ua.com.besqueet.mtwain.binoculars.Constants;
import ua.com.besqueet.mtwain.binoculars.Credentials;
import ua.com.besqueet.mtwain.binoculars.R;
import ua.com.besqueet.mtwain.binoculars.controllers.AuthController;
import ua.com.besqueet.mtwain.binoculars.controllers.BusController;
import ua.com.besqueet.mtwain.binoculars.controllers.ContextController;
import ua.com.besqueet.mtwain.binoculars.ui.fragments.SignInFragment;


public class MainActivity extends Activity implements Constants,Credentials{

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ContextController.INSTANCE.setMainActivity(this);
        BusController.INSTANCE.setBus(new Bus());
        QBSettings.getInstance().fastConfigInit(APP_ID,AUTHORIZATION_KEY,AUTHORIZATION_SECRET);
        sharedPreferences = this.getSharedPreferences(SHARED_PREF,Activity.MODE_PRIVATE);

        setContentView(R.layout.activity_main);

        if (loadLoginInfo()){
            //TODO: Cпроба входу -> у вікні з групами
        }else{
            showFragment(new SignInFragment());
            AuthController.INSTANCE.createAppSession();
        }

        if (savedInstanceState == null) {

        }
    }

    public boolean loadLoginInfo(){
        String login = sharedPreferences.getString(LOGIN,"");
        String password = sharedPreferences.getString(PASSWORD,"");
        if(!login.equals("") && !password.equals("")){
            AuthController.INSTANCE.signIn(login,password);
        }
        return !login.equals("") && !password.equals("");
    }

    public void storeLoginInfo(String login,String password){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LOGIN,login);
        editor.putString(PASSWORD,password);
        editor.apply();
    }

    public void showFragment(Fragment fragment){
        getFragmentManager().beginTransaction()
                .replace(R.id.container,fragment)
                .commit();
    }

}
