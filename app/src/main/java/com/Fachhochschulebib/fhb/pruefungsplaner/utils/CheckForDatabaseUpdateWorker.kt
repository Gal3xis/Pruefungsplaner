package com.Fachhochschulebib.fhb.pruefungsplaner.utils

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.work.ListenableWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.Fachhochschulebib.fhb.pruefungsplaner.model.repositories.DatabaseRepository
import com.Fachhochschulebib.fhb.pruefungsplaner.model.repositories.SharedPreferencesRepository
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
class CheckForDatabaseUpdateWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    private val context= context
    private val iOScope:CoroutineScope = CoroutineScope(CoroutineName("IO-Scope")+Dispatchers.IO)
    private val workerParams:WorkerParameters = workerParams
    private val repository:DatabaseRepository = DatabaseRepository(context)
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
        PushService.sendNotification(context,"The examinationplan has been updated!") //

        iOScope.launch {
            if(checkDatabase())
            {
                PushService.sendNotification(context,"The examinationplan has been updated!") //
            }
        }
        return Result.success()
    }

    private suspend fun checkDatabase():Boolean{
        return withContext(Dispatchers.IO){
            val periode = spRepository.getCurrentPeriode()
            val termin = spRepository.getCurrentTermin()
            val examinYear = spRepository.getExamineYear()
            val Ids = repository.getChoosenCourseIds(true)
            val courseIds = JSONArray()
            if (Ids != null) {
                for (id in Ids) {
                    try {
                        val idJson = JSONObject()
                        idJson.put("ID", id)
                        courseIds.put(idJson)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }
            if (periode == null || termin == null || examinYear == null || courseIds.toString().isNullOrEmpty()) {
                return@withContext false
            }
            val remoteEntries = repository.fetchEntries(periode, termin, examinYear, courseIds.toString())
            val localEntries = repository.getAllEntries()
            if(remoteEntries.isNullOrEmpty()||localEntries.isNullOrEmpty())
            {
                return@withContext false
            }
            if(remoteEntries?.size!=localEntries?.size){
                return@withContext true
            }
            for(i in remoteEntries.indices){
                val remoteEntry = remoteEntries.get(i)
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