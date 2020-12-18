package com.Fachhochschulebib.fhb.pruefungsplaner;

import android.app.Application;

// An die App gebundene Variablen
public class StartClass extends Application {
    private boolean appStarted;
    private boolean showNoProgressBar;
    private boolean changeFaculty;

    public boolean isShowNoProgressBar() {
        return showNoProgressBar;
    }

    public void setShowNoProgressBar(boolean showNoProgressBar) {
        this.showNoProgressBar = showNoProgressBar;
    }

    public boolean isChangeFaculty() {
        return changeFaculty;
    }

    public void setChangeFaculty(boolean changeFaculty) {
        this.changeFaculty = changeFaculty;
    }

    public boolean getAppStarted() {
        return appStarted;
    }

    public void setAppStarted(boolean appStartedSet) {

        appStarted = appStartedSet;

    }
}
