package com.fachhochschulebib.fhb.pruefungsplaner.viewmodel;

import java.lang.System;

/**
 * The ViewModel for the [com.fachhochschulebib.fhb.pruefungsplaner.view.fragments.FavoriteOverviewFragment]
 *
 * @author Alexander Lange (Email:alexander.lange@fh-bielefeld.de)
 * @since 1.6
 */
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u001f\u0010\u0005\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\b\u0018\u00010\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n\u00a8\u0006\u000b"}, d2 = {"Lcom/fachhochschulebib/fhb/pruefungsplaner/viewmodel/FavoriteOverviewViewModel;", "Lcom/fachhochschulebib/fhb/pruefungsplaner/viewmodel/BaseViewModel;", "application", "Landroid/app/Application;", "(Landroid/app/Application;)V", "liveFavorites", "Landroidx/lifecycle/LiveData;", "", "Lcom/fachhochschulebib/fhb/pruefungsplaner/model/room/TestPlanEntry;", "getLiveFavorites", "()Landroidx/lifecycle/LiveData;", "app_debug"})
public final class FavoriteOverviewViewModel extends com.fachhochschulebib.fhb.pruefungsplaner.viewmodel.BaseViewModel {
    
    /**
     * Live Data containing all favorite entries.
     */
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<java.util.List<com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry>> liveFavorites = null;
    
    public FavoriteOverviewViewModel(@org.jetbrains.annotations.NotNull()
    android.app.Application application) {
        super(null);
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.util.List<com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry>> getLiveFavorites() {
        return null;
    }
}