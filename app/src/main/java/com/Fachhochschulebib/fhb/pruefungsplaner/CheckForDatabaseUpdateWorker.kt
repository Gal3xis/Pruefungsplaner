package com.Fachhochschulebib.fhb.pruefungsplaner

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.common.util.concurrent.*
import java.util.concurrent.ScheduledExecutorService

class CheckForDatabaseUpdateWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {

    private val context:Context = appContext
    private val workerParams:WorkerParameters = workerParams


    override fun doWork(): Result {
        if(checkDatabase())
        {
            PushService.sendNotification(context,"The examinationplan has been updated!")
        }
        return Result.success()
    }

    //TODO Implement
    private fun checkDatabase():Boolean{
        return true
    }
}