package com.fachhochschulebib.fhb.pruefungsplaner.viewmodel;

import java.lang.System;

/**
 * ViewModel for the [com.fachhochschulebib.fhb.pruefungsplaner.view.fragments.ChangeCoursesFragment].
 *
 * @author Alexander Lange (Email:alexander.lange@fh-bielefeld.de)
 * @since 1.6
 */
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eJ\u0006\u0010\u000f\u001a\u00020\fJ\u0006\u0010\u0010\u001a\u00020\fR\u001f\u0010\u0005\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\b\u0018\u00010\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n\u00a8\u0006\u0011"}, d2 = {"Lcom/fachhochschulebib/fhb/pruefungsplaner/viewmodel/ChangeCoursesViewModel;", "Lcom/fachhochschulebib/fhb/pruefungsplaner/viewmodel/BaseViewModel;", "application", "Landroid/app/Application;", "(Landroid/app/Application;)V", "liveCoursesForFaculty", "Landroidx/lifecycle/MutableLiveData;", "", "Lcom/fachhochschulebib/fhb/pruefungsplaner/model/room/Course;", "getLiveCoursesForFaculty", "()Landroidx/lifecycle/MutableLiveData;", "changeMainCourse", "", "course", "", "getCourses", "updateDbEntries", "app_debug"})
public final class ChangeCoursesViewModel extends com.fachhochschulebib.fhb.pruefungsplaner.viewmodel.BaseViewModel {
    
    /**
     * Live Data for storing all courses for a selected Faculty.
     * Is set in [getCourses]
     */
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<java.util.List<com.fachhochschulebib.fhb.pruefungsplaner.model.room.Course>> liveCoursesForFaculty = null;
    
    public ChangeCoursesViewModel(@org.jetbrains.annotations.NotNull()
    android.app.Application application) {
        super(null);
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.MutableLiveData<java.util.List<com.fachhochschulebib.fhb.pruefungsplaner.model.room.Course>> getLiveCoursesForFaculty() {
        return null;
    }
    
    /**
     * Updates the Room-Database with data from the Server.
     *
     * @since 1.6
     * @author Alexander Lange
     */
    public final void updateDbEntries() {
    }
    
    /**
     * Changes the maincourse for the user. If the new course is not a favorite yet,
     * it is also updated.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public final void changeMainCourse(@org.jetbrains.annotations.NotNull()
    java.lang.String course) {
    }
    
    /**
     * Loads new courses for a specific faulty in the livadata-object.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public final void getCourses() {
    }
}