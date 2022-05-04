package com.fachhochschulebib.fhb.pruefungsplaner.viewmodel;

import java.lang.System;

/**
 * ViewModel for the [com.fachhochschulebib.fhb.pruefungsplaner.view.fragments.ExamOverviewFragment].
 *
 * @author Alexander Lange (Email:alexander.lange@fh-bielefeld.de)
 * @since 1.6
 */
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J/\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0012\u0010\u0011\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00130\u0012\"\u00020\u0013\u00a2\u0006\u0002\u0010\u0014J\u000e\u0010\u0015\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eJ\u0006\u0010\u0016\u001a\u00020\fR\u001f\u0010\u0005\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\b\u0018\u00010\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n\u00a8\u0006\u0017"}, d2 = {"Lcom/fachhochschulebib/fhb/pruefungsplaner/viewmodel/ExamOverviewViewModel;", "Lcom/fachhochschulebib/fhb/pruefungsplaner/viewmodel/BaseViewModel;", "application", "Landroid/app/Application;", "(Landroid/app/Application;)V", "liveEntryList", "Landroidx/lifecycle/LiveData;", "", "Lcom/fachhochschulebib/fhb/pruefungsplaner/model/room/TestPlanEntry;", "getLiveEntryList", "()Landroidx/lifecycle/LiveData;", "checkPermission", "", "activity", "Landroid/app/Activity;", "callbackId", "", "permissionsId", "", "", "(Landroid/app/Activity;I[Ljava/lang/String;)V", "getCalendarPermission", "updateDataFromServer", "app_debug"})
public final class ExamOverviewViewModel extends com.fachhochschulebib.fhb.pruefungsplaner.viewmodel.BaseViewModel {
    
    /**
     * Live Data containing all entries for the chosen courses.
     */
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<java.util.List<com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry>> liveEntryList = null;
    
    public ExamOverviewViewModel(@org.jetbrains.annotations.NotNull()
    android.app.Application application) {
        super(null);
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.util.List<com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry>> getLiveEntryList() {
        return null;
    }
    
    /**
     * Updates the current local data with the data from the server.
     * Can change the data in the recyclerview and the currentExamPeriod
     * Shows a progressbar while loading the data.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public final void updateDataFromServer() {
    }
    
    /**
     * This Method checks, if the user already gave permission to access the Calendar,
     * if not, he is ask to do so.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public final void getCalendarPermission(@org.jetbrains.annotations.NotNull()
    android.app.Activity activity) {
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
     */
    public final void checkPermission(@org.jetbrains.annotations.NotNull()
    android.app.Activity activity, int callbackId, @org.jetbrains.annotations.NotNull()
    java.lang.String... permissionsId) {
    }
}