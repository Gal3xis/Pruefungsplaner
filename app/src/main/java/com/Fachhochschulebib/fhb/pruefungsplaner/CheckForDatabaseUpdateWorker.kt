package com.Fachhochschulebib.fhb.pruefungsplaner

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.common.util.concurrent.*
import java.util.concurrent.ScheduledExecutorService

/**
 * A background worker, that checks the Server-Database for changes and then notifies the user.
 * TODO Checking process not implemented yet.
 *
 * @author Alexander Lange
 * @since 1.6
 */
class CheckForDatabaseUpdateWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {

    private val context:Context = appContext
    private val workerParams:WorkerParameters = workerParams


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
        if(checkDatabase())
        {
            PushService.sendNotification(context,"The examinationplan has been updated!") //
        }
        return Result.success()
    }

    //TODO Implement
    private fun checkDatabase():Boolean{
        return true
    }
}