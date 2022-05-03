package com.fachhochschulebib.fhb.pruefungsplaner.viewmodel

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

/**
 * ViewModel for the [com.fachhochschulebib.fhb.pruefungsplaner.view.fragments.ExamOverviewFragment].
 *
 * @author Alexander Lange (Email:alexander.lange@fh-bielefeld.de)
 * @since 1.6
 */
class ExamOverviewViewModel(application: Application) : BaseViewModel(application) {

    /**
     * Live Data containing all entries for the chosen courses.
     */
    val liveEntryList = repository.getAllEntriesForChosenCoursesLiveData()


    init {
        updatePeriod()
        updateDataFromServer()
    }

    /**
     * Updates the current local data with the data from the server.
     * Can change the data in the recyclerview and the currentExamPeriod
     * Shows a progressbar while loading the data.
     *
     * @author Alexander Lange
     * @since 1.6
     *
     */
    fun updateDataFromServer() {
        fetchCourses()
        fetchEntries()
    }

    /**
     * This Method checks, if the user already gave permission to access the Calendar,
     * if not, he is ask to do so.
     *
     * @author Alexander Lange
     * @since 1.6
     *
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
     * @author Alexander Lange
     * @since 1.6
     *
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

}