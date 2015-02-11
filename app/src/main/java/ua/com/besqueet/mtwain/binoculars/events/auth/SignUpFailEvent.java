package ua.com.besqueet.mtwain.binoculars.events.auth;

import ua.com.besqueet.mtwain.binoculars.ErrorCodes;

public class SignUpFailEvent implements ErrorCodes{

    private String errorMessage = "";

    public SignUpFailEvent(String code){
        //TODO: закінчити світч
        switch (code){
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
