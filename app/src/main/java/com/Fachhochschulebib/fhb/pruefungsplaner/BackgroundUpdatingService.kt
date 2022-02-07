package com.Fachhochschulebib.fhb.pruefungsplaner

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

private const val updateWorkerName = "updateWorker"


object BackgroundUpdatingService {


    /**
     * Sets a backgroundservice, that enables the application to check for changes in the Examplan-database in a specified interval.
     * Gets the intervalltime from shared preferences.
     *
     * @author Alexander Lange
     * @since 1.6
     *
     * **See Also:**[PeriodicWorkRequest](https://developer.android.com/reference/androidx/work/PeriodicWorkRequest)
     * **See Also:**[Define work requests](https://developer.android.com/topic/libraries/architecture/workmanager/how-to/define-work#schedule_periodic_work)
     * **See Also:**[Guide to background work](https://developer.android.com/guide/background)
     * **See Also:**[Youtube](https://www.youtube.com/watch?v=pe_yqM16hPQ)
     */
    fun initPeriodicRequests(context: Context) {
        val sharedPreferencesSettings = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        if(!sharedPreferencesSettings.getBoolean("auto_updates",false)){
            return
        }
        val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        val interval = ((sharedPreferencesSettings?.getInt("update_intervall_time_hour", 0)
                ?: 0) * 60 + (sharedPreferencesSettings?.getInt("update_intervall_time_minute", 15)
                ?: 15)).toLong()
        val checkRequest = PeriodicWorkRequestBuilder<CheckForDatabaseUpdateWorker>(interval, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build()

        context.let { WorkManager.getInstance(it).enqueueUniquePeriodicWork(updateWorkerName, ExistingPeriodicWorkPolicy.KEEP, checkRequest) }
    }

    /**
     * Recreates the periodic requests, used when new settings where set.
     *
     * @param[context] The applicationcontext.
     *
     * @author Alexander Lange
     * @since 1.5
     */
    fun invalidatePeriodicRequests(context: Context) {
        removePeriodicRequest(context)
        initPeriodicRequests(context)
    }


    /**
     * Removes the check for Database-updates from the WorkManager. The App is no longer doing background work.
     *
     * @author Alexander Lange
     * @since 1.6
     *
     * **See Also:**[PeriodicWorkRequest](https://developer.android.com/reference/androidx/work/PeriodicWorkRequest)
     * **See Also:**[Define work requests](https://developer.android.com/topic/libraries/architecture/workmanager/how-to/define-work#schedule_periodic_work)
     * **See Also:**[Guide to background work](https://developer.android.com/guide/background)
     * **See Also:**[Youtube](https://www.youtube.com/watch?v=pe_yqM16hPQ)
     */
    fun removePeriodicRequest(context: Context) {
        context.let { WorkManager.getInstance(it).cancelUniqueWork(updateWorkerName) }
    }
}