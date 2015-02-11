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
import ua.com.besqueet.mtwain.binoculars.events.auth.LoginSuccessEvent;
import ua.com.besqueet.mtwain.binoculars.events.auth.SignUpFailEvent;
import ua.com.besqueet.mtwain.binoculars.utils.ValidationUtils;

public class SignUpFragment extends Fragment {

    public SignUpFragment(){}

    @InjectView(R.id.editLogin)
    AutoCompleteTextView editLogin;
    @InjectView(R.id.editPassword)
    EditText editPassword;
    @InjectView(R.id.editConfirm)
    EditText editConfirm;
    @OnClick(R.id.btnCreate) void onBtnCreateClick(){
        btnCreateClick();
    }
    @OnClick(R.id.btnSignIn) void onBtnSignInClick(){
        btnSignInClick();
    }

    ValidationUtils validationUtils;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BusController.INSTANCE.getBus().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sign_up,container,false);
        ButterKnife.inject(this, rootView);
        validationUtils = new ValidationUtils(getActivity(),
                new EditText[]{editLogin,editPassword,editConfirm}, new String[]{getActivity().getString(R.string.login_field_empty),getActivity().getString(R.string.pass_field_empty)});
        return rootView;
    }

    private void btnSignInClick(){
        ContextController.INSTANCE.getMainActivity().showFragment(new SignInFragment());
    }

    private void btnCreateClick(){
        String userLogin = editLogin.getText().toString();
        String userPassword = editPassword.getText().toString();
        String userConfirm = editConfirm.getText().toString();
        if (validationUtils.isValidUserDate(userLogin, userPassword,userConfirm)) {
            AuthController.INSTANCE.createNewAccount(userLogin,userPassword);
        }
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
    public void onSignUpFail(SignUpFailEvent event){
        String errorMessage = event.getErrorMessage();
        if(errorMessage.equals("")){
            Toast.makeText(ContextController.INSTANCE.getMainActivity(),"Fail",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(ContextController.INSTANCE.getMainActivity(),errorMessage,Toast.LENGTH_SHORT).show();
        }
    }

}
