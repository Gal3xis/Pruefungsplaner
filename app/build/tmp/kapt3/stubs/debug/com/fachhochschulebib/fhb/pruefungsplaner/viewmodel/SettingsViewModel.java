package com.fachhochschulebib.fhb.pruefungsplaner.viewmodel;

import java.lang.System;

/**
 * ViewModel for the [com.fachhochschulebib.fhb.pruefungsplaner.view.fragments.SettingsFragment].
 *
 * @author Alexander Lange (Email:alexander.lange@fh-bielefeld.de)
 * @since 1.6
 */
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010J\u0016\u0010\u0011\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0012\u001a\u00020\u0013R\u001f\u0010\u0005\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\b\u0018\u00010\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u001f\u0010\u000b\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\b\u0018\u00010\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\n\u00a8\u0006\u0014"}, d2 = {"Lcom/fachhochschulebib/fhb/pruefungsplaner/viewmodel/SettingsViewModel;", "Lcom/fachhochschulebib/fhb/pruefungsplaner/viewmodel/BaseViewModel;", "application", "Landroid/app/Application;", "(Landroid/app/Application;)V", "liveEntries", "Landroidx/lifecycle/LiveData;", "", "Lcom/fachhochschulebib/fhb/pruefungsplaner/model/room/TestPlanEntry;", "getLiveEntries", "()Landroidx/lifecycle/LiveData;", "liveFavorites", "getLiveFavorites", "deleteAllFavorites", "", "context", "Landroid/content/Context;", "setCalendarSync", "sync", "", "app_debug"})
public final class SettingsViewModel extends com.fachhochschulebib.fhb.pruefungsplaner.viewmodel.BaseViewModel {
    
    /**
     * Live Data containing all favorites.
     */
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<java.util.List<com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry>> liveFavorites = null;
    
    /**
     * Live Data containing all [com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry]-Objects in the local database.
     */
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<java.util.List<com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry>> liveEntries = null;
    
    public SettingsViewModel(@org.jetbrains.annotations.NotNull()
    android.app.Application application) {
        super(null);
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.util.List<com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry>> getLiveFavorites() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.util.List<com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry>> getLiveEntries() {
        return null;
    }
    
    /**
     * Sets in the sharedPreferences, if the calendar should be synced or not.
     *
     * @param context The Applicationcontext
     * @param sync If the calendar should be synced or not.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public final void setCalendarSync(@org.jetbrains.annotations.NotNull()
    android.content.Context context, boolean sync) {
    }
    
    /**
     * Deletes all favorites.
     *
     * @param context The Applicationcontext
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public final void deleteAllFavorites(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
    }
}