package com.Fachhochschulebib.fhb.pruefungsplaner.viewmodel

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class TermineViewModel(application: Application) : BaseViewModel(application) {

    val liveEntryList = repository.getAllEntriesLiveDataByDate()

    init {
        updateDataFromServer()
        updatePruefperiode()
    }

    /**
     * Updates the current local data with the data from the server.
     * Can change the data in the recyclerview and the currentExamPeriod
     * Shows a progressbar while loading the data.
     *
     * @since 1.6
     * @author Alexander Lange (E-Mail:alexander.lange@fh-bielefeld.de)
     */
    fun updateDataFromServer() {
        fetchCourses()
        fetchEntries()
    }

    /**
     * This Method checks, if the user already gave permission to access the Calendar,
     * if not, he is ask to do so.
     *
     * @since 1.6
     * @author Alexander Lange (E-Mail:alexander.lange@fh-bielefeld.de)
     */
    fun getCalendarPermission(activity: Activity) {
        val callbackId = 42
        checkPermission(
                activity,
                callbackId,
                Manifest.permission.READ_CALENDAR,
                Manifest.permission.WRITE_CALENDAR
        )
    }

    /**
     * Checks the Phone for a give permission.
     * If the permission is not granted, the user is asked if he wants to grant permission.
     *
     * @param[callbackId] Id of Permission which called function
     * @param[permissionsId] List of permissions that need to be requested
     *
     * @since 1.6
     * @author Alexander Lange (E-Mail:alexander.lange@fh-bielefeld.de)
     */
    fun checkPermission(activity:Activity,callbackId: Int, vararg permissionsId: String) {
        var permissions = true
        for (p in permissionsId) {
            permissions = (permissions
                    && ContextCompat
                    .checkSelfPermission(activity, p) == PackageManager.PERMISSION_GRANTED)
        }
        if (!permissions) ActivityCompat.requestPermissions(
                activity,
                permissionsId,
                callbackId
        )
    }

    fun getPruefungszeitraum(): String? {
        val start = getStartDate()
        val end = getEndDate()

        if(start==null||end==null)return null

        return sdfDisplay.format(start) + "-" + sdfDisplay.format(end)
    }


}