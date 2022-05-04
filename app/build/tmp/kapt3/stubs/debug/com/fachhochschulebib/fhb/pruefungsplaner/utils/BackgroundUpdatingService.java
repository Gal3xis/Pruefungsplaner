package com.fachhochschulebib.fhb.pruefungsplaner.utils;

import java.lang.System;

/**
 * Class with functionalities to start and remove periodic Requests, that run in the background even when the app is closed, to look for updates in the remote database.
 *
 * @author Alexander Lange (Email:alexander.lange@fh-bielefeld.de)
 * @since 1.6
 */
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006J\u000e\u0010\u0007\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006J\u000e\u0010\b\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006\u00a8\u0006\t"}, d2 = {"Lcom/fachhochschulebib/fhb/pruefungsplaner/utils/BackgroundUpdatingService;", "", "()V", "initPeriodicRequests", "", "context", "Landroid/content/Context;", "invalidatePeriodicRequests", "removePeriodicRequest", "app_debug"})
public final class BackgroundUpdatingService {
    @org.jetbrains.annotations.NotNull()
    public static final com.fachhochschulebib.fhb.pruefungsplaner.utils.BackgroundUpdatingService INSTANCE = null;
    
    private BackgroundUpdatingService() {
        super();
    }
    
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
    public final void initPeriodicRequests(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
    }
    
    /**
     * Recreates the periodic requests, used when new settings where set.
     *
     * @param[context] The applicationcontext.
     *
     * @author Alexander Lange
     * @since 1.5
     */
    public final void invalidatePeriodicRequests(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
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
    public final void removePeriodicRequest(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
    }
}