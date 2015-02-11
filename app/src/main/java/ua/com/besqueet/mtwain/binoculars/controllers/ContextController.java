package ua.com.besqueet.mtwain.binoculars.controllers;

import ua.com.besqueet.mtwain.binoculars.ui.MainActivity;

public enum ContextController {
    INSTANCE;

    private MainActivity mainActivity;

    public MainActivity getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }
}
