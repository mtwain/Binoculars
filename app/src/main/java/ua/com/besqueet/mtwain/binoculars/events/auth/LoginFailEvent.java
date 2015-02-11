package ua.com.besqueet.mtwain.binoculars.events.auth;


import ua.com.besqueet.mtwain.binoculars.ErrorCodes;

public class LoginFailEvent implements ErrorCodes{

    private String errorMessage = "";

    public LoginFailEvent(String code){
        //TODO: закінчити світч
        switch (code){
            case LOGIN_UNAUTHORIZED:
                setErrorMessage("Wrong login or password");
                break;
            case LOGIN_EMAIL_BLANK:
                break;
            default:
                setErrorMessage("");
                break;
        }
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
