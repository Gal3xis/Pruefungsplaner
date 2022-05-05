package com.fachhochschulebib.fhb.pruefungsplaner.utils

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.fachhochschulebib.fhb.pruefungsplaner.R
import com.fachhochschulebib.fhb.pruefungsplaner.model.repositories.DatabaseRepository
import com.fachhochschulebib.fhb.pruefungsplaner.model.repositories.SharedPreferencesRepository
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 * A background worker, that checks the Server-Database for changes and then notifies the user.
 * TODO Checking process not implemented yet.
 *
 * @author Alexander Lange
 * @since 1.6
 */
class CheckForDatabaseUpdateWorker(private val context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    /**
     * The coroutine scope, in which the worker will look for new updates
     */
    private val iOScope:CoroutineScope = CoroutineScope(CoroutineName("IO-Scope")+Dispatchers.IO)

    /**
     * The database repository
     */
    private val repository:DatabaseRepository = DatabaseRepository(context)

    /**
     * The shared preferences repository
     */
    private val spRepository:SharedPreferencesRepository = SharedPreferencesRepository(context)

    /**
     * Override this method to do your actual background processing.  This method is called on a
     * background thread - you are required to <b>synchronously</b> do your work and return the
     * [androidx.work.ListenableWorker.Result] from this method.  Once you return from this
     * method, the Worker is considered to have finished what its doing and will be destroyed.  If
     * you need to do your work asynchronously on a thread of your own choice, see
     * [ListenableWorker].
     * <p>
     * A Worker is given a maximum of ten minutes to finish its execution and return a
     * [androidx.work.ListenableWorker.Result].  After this time has expired, the Worker will
     * be signalled to stop.
     *
     * @return The [androidx.work.ListenableWorker.Result] of the computation; note that
     *         dependent work will not execute if you use
     *        [androidx.work.ListenableWorker.Result#failure()] or
     *       [androidx.work.ListenableWorker.Result#failure(Data)]
     */
    override fun doWork(): Result {
        iOScope.launch {
            if(checkDatabase())
            {
                PushService.sendNotification(context,context.getString(R.string.background_worker_notification)) //
            }
        }
        return Result.success()
    }

    /**
     * Checks for updates in the remote database.
     *
     * @return true->There is a new plan available;false->There is no new plan available
     */
    private suspend fun checkDatabase():Boolean{
        return withContext(Dispatchers.IO){
            val period = spRepository.getPeriodTerm()
            val termin = spRepository.getPeriodTermin()
            val examineYear = spRepository.getPeriodYear()
            val ids = repository.getChosenCourseIds()
            val courseIds = JSONArray()
            if (ids != null) {
                for (id in ids) {
                    try {
                        val idJson = JSONObject()
                        idJson.put("ID", id)
                        courseIds.put(idJson)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }
            if (period == null || termin == null || examineYear == null || courseIds.toString().isEmpty()) {
                return@withContext false
            }
            val remoteEntries = repository.fetchEntries(period, termin, examineYear, courseIds.toString())
            val localEntries = repository.getAllEntries()
            if(remoteEntries.isNullOrEmpty()||localEntries.isNullOrEmpty())
            {
                return@withContext false
            }
            if(remoteEntries.size != localEntries.size){
                return@withContext true
            }
            for(i in remoteEntries.indices){
                val remoteEntry = remoteEntries[i]
                val remoteId = remoteEntry.ID
                val localEntry = remoteId?.let { repository.getEntryById(it) }
                if(remoteEntry.Timestamp!=localEntry?.timeStamp)
                {
                    return@withContext true
                }
            }
            return@withContext false
        }
    }
}