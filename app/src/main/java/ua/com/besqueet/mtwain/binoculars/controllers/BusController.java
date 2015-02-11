package ua.com.besqueet.mtwain.binoculars.controllers;


import com.squareup.otto.Bus;

public enum BusController {
    INSTANCE;

    private Bus bus;



    public Bus getBus() {
        return bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }
}
