package com.fachhochschulebib.fhb.pruefungsplaner.utils;

import java.lang.System;

/**
 * A background worker, that checks the Server-Database for changes and then notifies the user.
 * TODO Checking process not implemented yet.
 *
 * @author Alexander Lange
 * @since 1.6
 */
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0011\u0010\r\u001a\u00020\u000eH\u0082@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000fJ\b\u0010\u0010\u001a\u00020\u0011H\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u0012"}, d2 = {"Lcom/fachhochschulebib/fhb/pruefungsplaner/utils/CheckForDatabaseUpdateWorker;", "Landroidx/work/Worker;", "context", "Landroid/content/Context;", "workerParams", "Landroidx/work/WorkerParameters;", "(Landroid/content/Context;Landroidx/work/WorkerParameters;)V", "iOScope", "Lkotlinx/coroutines/CoroutineScope;", "repository", "Lcom/fachhochschulebib/fhb/pruefungsplaner/model/repositories/DatabaseRepository;", "spRepository", "Lcom/fachhochschulebib/fhb/pruefungsplaner/model/repositories/SharedPreferencesRepository;", "checkDatabase", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "doWork", "Landroidx/work/ListenableWorker$Result;", "app_debug"})
public final class CheckForDatabaseUpdateWorker extends androidx.work.Worker {
    private final android.content.Context context = null;
    private final androidx.work.WorkerParameters workerParams = null;
    
    /**
     * The Coroutinescope, in which the worker will look for new updates
     */
    private final kotlinx.coroutines.CoroutineScope iOScope = null;
    
    /**
     * The database repository
     */
    private final com.fachhochschulebib.fhb.pruefungsplaner.model.repositories.DatabaseRepository repository = null;
    
    /**
     * The shared preferences repository
     */
    private final com.fachhochschulebib.fhb.pruefungsplaner.model.repositories.SharedPreferencesRepository spRepository = null;
    
    public CheckForDatabaseUpdateWorker(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    androidx.work.WorkerParameters workerParams) {
        super(null, null);
    }
    
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
     *        dependent work will not execute if you use
     *       [androidx.work.ListenableWorker.Result#failure()] or
     *      [androidx.work.ListenableWorker.Result#failure(Data)]
     */
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public androidx.work.ListenableWorker.Result doWork() {
        return null;
    }
    
    /**
     * Checks for updates in the remote database.
     *
     * @return true->There is a new plan available;false->There is no new plan available
     */
    private final java.lang.Object checkDatabase(kotlin.coroutines.Continuation<? super java.lang.Boolean> continuation) {
        return null;
    }
}