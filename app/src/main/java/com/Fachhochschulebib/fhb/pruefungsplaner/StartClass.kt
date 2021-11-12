package com.Fachhochschulebib.fhb.pruefungsplaner

import android.app.Application

// An die App gebundene Variablen
class StartClass : Application() {
    var appStarted = false
    var isShowNoProgressBar = false
    var isChangeFaculty = false
}