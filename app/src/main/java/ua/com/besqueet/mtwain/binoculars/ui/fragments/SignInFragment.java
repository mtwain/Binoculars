package ua.com.besqueet.mtwain.binoculars.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import ua.com.besqueet.mtwain.binoculars.R;
import ua.com.besqueet.mtwain.binoculars.controllers.AuthController;
import ua.com.besqueet.mtwain.binoculars.controllers.BusController;
import ua.com.besqueet.mtwain.binoculars.controllers.ContextController;
import ua.com.besqueet.mtwain.binoculars.events.auth.LoginFailEvent;
import ua.com.besqueet.mtwain.binoculars.events.auth.LoginSuccessEvent;
import ua.com.besqueet.mtwain.binoculars.utils.ValidationUtils;

public class SignInFragment extends Fragment {

    public SignInFragment(){}

    ValidationUtils validationUtils;

    @InjectView(R.id.editLogin)
    AutoCompleteTextView editLogin;
    @InjectView(R.id.editPassword)
    EditText editPassword;

    @OnClick(R.id.btnIn) void onBtnInClick(){
        btnInClick();
    }
    @OnClick(R.id.btnNewAccount) void onBtnNewAccountClick(){
        btnNewAccountClick();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BusController.INSTANCE.getBus().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sign_in,container,false);
        ButterKnife.inject(this,rootView);
        validationUtils = new ValidationUtils(getActivity(),
                new EditText[]{editLogin,editPassword}, new String[]{getActivity().getString(R.string.login_field_empty),getActivity().getString(R.string.pass_field_empty)});
        return rootView;
    }

    private void btnInClick(){
        String userLogin = editLogin.getText().toString();
        String userPassword = editPassword.getText().toString();
        if (validationUtils.isValidUserDate(userLogin, userPassword)) {
            AuthController.INSTANCE.signIn(userLogin, userPassword);
        }
    }

    private void btnNewAccountClick(){
        ContextController.INSTANCE.getMainActivity().showFragment(new SignUpFragment());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BusController.INSTANCE.getBus().unregister(this);
    }

    @Subscribe
    public void onLoginSuccess(LoginSuccessEvent event){
        ContextController.INSTANCE.getMainActivity().showFragment(new DialogListFragment());
    }

    @Subscribe
    public void onLoginFail(LoginFailEvent event){
        String errorMessage = event.getErrorMessage();
        if (errorMessage.equals("")){
            Toast.makeText(ContextController.INSTANCE.getMainActivity(),"Login Fail",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(ContextController.INSTANCE.getMainActivity(),errorMessage,Toast.LENGTH_SHORT).show();
        }
        editPassword.setText("");
        editLogin.requestFocus();
        editLogin.setSelection(editLogin.getText().length(),editLogin.getText().length());
    }

}
