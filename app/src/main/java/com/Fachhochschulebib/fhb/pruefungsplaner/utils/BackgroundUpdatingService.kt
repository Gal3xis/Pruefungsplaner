package com.Fachhochschulebib.fhb.pruefungsplaner.utils

import android.content.Context
import androidx.work.*
import com.Fachhochschulebib.fhb.pruefungsplaner.model.repositories.SharedPreferencesRepository
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
        val spRepository = SharedPreferencesRepository(context)
        if(!spRepository.getBackgroundUpdates()){
            return
        }
        val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        val interval = spRepository.getUpdateIntervalTimeHour()* 60 + spRepository.getUpdateIntervalTimeMinute().toLong()
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