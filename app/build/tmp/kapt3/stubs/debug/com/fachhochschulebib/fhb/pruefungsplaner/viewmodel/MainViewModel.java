package com.fachhochschulebib.fhb.pruefungsplaner.viewmodel;

import java.lang.System;

/**
 * ViewModel for the [com.fachhochschulebib.fhb.pruefungsplaner.view.activities.MainActivity]
 *
 * @author Alexander Lange (Email:alexander.lange@fh-bielefeld.de)
 * @since 1.6
 */
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\u001c\u001a\u00020\u001dJ\u0006\u0010\u001e\u001a\u00020\u001dJ\u0006\u0010\u001f\u001a\u00020\u001dR\u001f\u0010\u0005\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\b\u0018\u00010\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u001f\u0010\u000b\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\r\u0018\u00010\u00070\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u001f\u0010\u0010\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\r\u0018\u00010\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\nR\u0017\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\b0\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u000fR(\u0010\u0014\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\u0015\u0018\u00010\u00070\u0006X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\n\"\u0004\b\u0017\u0010\u0018R\u0019\u0010\u0019\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u001a0\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u000f\u00a8\u0006 "}, d2 = {"Lcom/fachhochschulebib/fhb/pruefungsplaner/viewmodel/MainViewModel;", "Lcom/fachhochschulebib/fhb/pruefungsplaner/viewmodel/BaseViewModel;", "application", "Landroid/app/Application;", "(Landroid/app/Application;)V", "liveChoosenCourses", "Landroidx/lifecycle/LiveData;", "", "Lcom/fachhochschulebib/fhb/pruefungsplaner/model/room/Course;", "getLiveChoosenCourses", "()Landroidx/lifecycle/LiveData;", "liveEntriesForCourse", "Landroidx/lifecycle/MutableLiveData;", "Lcom/fachhochschulebib/fhb/pruefungsplaner/model/room/TestPlanEntry;", "getLiveEntriesForCourse", "()Landroidx/lifecycle/MutableLiveData;", "liveEntriesOrdered", "getLiveEntriesOrdered", "liveMainCourse", "getLiveMainCourse", "liveProfList", "", "getLiveProfList", "setLiveProfList", "(Landroidx/lifecycle/LiveData;)V", "liveSelectedFaculty", "Lcom/fachhochschulebib/fhb/pruefungsplaner/model/room/Faculty;", "getLiveSelectedFaculty", "filterCoursename", "", "getMainCourse", "getSelectedFaculty", "app_debug"})
public final class MainViewModel extends com.fachhochschulebib.fhb.pruefungsplaner.viewmodel.BaseViewModel {
    
    /**
     * Live Data containing all entries, ordered by the exam date.
     */
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<java.util.List<com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry>> liveEntriesOrdered = null;
    
    /**
     * Live Data for storing the selected faculty. Is set in [getSelectedFaculty].
     */
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<com.fachhochschulebib.fhb.pruefungsplaner.model.room.Faculty> liveSelectedFaculty = null;
    
    /**
     * Live Data for storing all entries either for a specific course or if not course was selected, for all chosen courses.
     */
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<java.util.List<com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry>> liveEntriesForCourse = null;
    
    /**
     * Live Data containg all chosen courses.
     */
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<java.util.List<com.fachhochschulebib.fhb.pruefungsplaner.model.room.Course>> liveChoosenCourses = null;
    
    /**
     * Live Data containing the names of all first examiners.
     */
    @org.jetbrains.annotations.NotNull()
    private androidx.lifecycle.LiveData<java.util.List<java.lang.String>> liveProfList;
    
    /**
     * Live Data for storing the maincourse.
     */
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<com.fachhochschulebib.fhb.pruefungsplaner.model.room.Course> liveMainCourse = null;
    
    public MainViewModel(@org.jetbrains.annotations.NotNull()
    android.app.Application application) {
        super(null);
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.util.List<com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry>> getLiveEntriesOrdered() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.MutableLiveData<com.fachhochschulebib.fhb.pruefungsplaner.model.room.Faculty> getLiveSelectedFaculty() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.MutableLiveData<java.util.List<com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry>> getLiveEntriesForCourse() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.util.List<com.fachhochschulebib.fhb.pruefungsplaner.model.room.Course>> getLiveChoosenCourses() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.util.List<java.lang.String>> getLiveProfList() {
        return null;
    }
    
    public final void setLiveProfList(@org.jetbrains.annotations.NotNull()
    androidx.lifecycle.LiveData<java.util.List<java.lang.String>> p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.MutableLiveData<com.fachhochschulebib.fhb.pruefungsplaner.model.room.Course> getLiveMainCourse() {
        return null;
    }
    
    /**
     * Gets the selected Faculty from the room database.
     * Stores the result in the [liveSelectedFaculty]-LiveDataObject
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public final void getSelectedFaculty() {
    }
    
    /**
     * Gets a list of all entries for the coursename selected in the Filter.
     * Stores the result in the [liveEntriesForCourse]-LiveDataObject.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public final void filterCoursename() {
    }
    
    /**
     * Gets the main course from the shared preferences and stores it in the [liveMainCourse].LiveData-Object.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public final void getMainCourse() {
    }
}