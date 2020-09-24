package com.Fachhochschulebib.fhb.pruefungsplaner;

import android.app.Application;

// Globale Variable um den ersten App Start zu detektieren
public class StartClass extends Application {
    private boolean appStarted;

    public boolean getAppStarted() {

        return appStarted;
    }

    public void setAppStarted(boolean appStartedSet) {

        appStarted = appStartedSet;

    }
}
