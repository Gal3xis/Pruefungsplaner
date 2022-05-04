package com.fachhochschulebib.fhb.pruefungsplaner.viewmodel;

import java.lang.System;

/**
 * ViewModel for the [com.fachhochschulebib.fhb.pruefungsplaner.view.activities.StartActivity]
 *
 * @author Alexander Lange (Email:alexander.lange@fh-bielefeld.de)
 * @since 1.6
 */
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\bJ\u000e\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0013\u001a\u00020\u0014J\u0006\u0010\u0015\u001a\u00020\u0016J\u000e\u0010\u0017\u001a\u00020\u00112\u0006\u0010\u0018\u001a\u00020\u0014J\u0010\u0010\u0019\u001a\u00020\u00112\u0006\u0010\u0018\u001a\u00020\rH\u0016R\u001f\u0010\u0005\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\b\u0018\u00010\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u001f\u0010\u000b\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\r\u0018\u00010\u00070\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000f\u00a8\u0006\u001a"}, d2 = {"Lcom/fachhochschulebib/fhb/pruefungsplaner/viewmodel/StartViewModel;", "Lcom/fachhochschulebib/fhb/pruefungsplaner/viewmodel/BaseViewModel;", "application", "Landroid/app/Application;", "(Landroid/app/Application;)V", "liveCoursesForFaculty", "Landroidx/lifecycle/MutableLiveData;", "", "Lcom/fachhochschulebib/fhb/pruefungsplaner/model/room/Course;", "getLiveCoursesForFaculty", "()Landroidx/lifecycle/MutableLiveData;", "liveFaculties", "Landroidx/lifecycle/LiveData;", "Lcom/fachhochschulebib/fhb/pruefungsplaner/model/room/Faculty;", "getLiveFaculties", "()Landroidx/lifecycle/LiveData;", "addMainCourse", "", "course", "choosenCourse", "", "checkLoginStatus", "", "firstStart", "faculty", "setSelectedFaculty", "app_debug"})
public final class StartViewModel extends com.fachhochschulebib.fhb.pruefungsplaner.viewmodel.BaseViewModel {
    
    /**
     * Live Data containing all faculties.
     */
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<java.util.List<com.fachhochschulebib.fhb.pruefungsplaner.model.room.Faculty>> liveFaculties = null;
    
    /**
     * Live Data for storing all courses for a specific faculty. Is set in [setSelectedFaculty]
     */
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<java.util.List<com.fachhochschulebib.fhb.pruefungsplaner.model.room.Course>> liveCoursesForFaculty = null;
    
    public StartViewModel(@org.jetbrains.annotations.NotNull()
    android.app.Application application) {
        super(null);
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.util.List<com.fachhochschulebib.fhb.pruefungsplaner.model.room.Faculty>> getLiveFaculties() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.MutableLiveData<java.util.List<com.fachhochschulebib.fhb.pruefungsplaner.model.room.Course>> getLiveCoursesForFaculty() {
        return null;
    }
    
    /**
     * Selects a course a the main course.
     *
     * @param[course] The course that is supposed to be the mein course.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public final void addMainCourse(@org.jetbrains.annotations.NotNull()
    com.fachhochschulebib.fhb.pruefungsplaner.model.room.Course course) {
    }
    
    /**
     * Selects a course as the main course.
     *
     * @param[choosenCourse] The name of th course that is supposed to be the main course.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public final void addMainCourse(@org.jetbrains.annotations.NotNull()
    java.lang.String choosenCourse) {
    }
    
    /**
     * Sets the selected Faculty. Override to update the liveData.
     *
     * @param faculty The new Faculty
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @java.lang.Override()
    public void setSelectedFaculty(@org.jetbrains.annotations.NotNull()
    com.fachhochschulebib.fhb.pruefungsplaner.model.room.Faculty faculty) {
    }
    
    /**
     * Checks if the user already selected a maincourse. If that is the case, he is not asked again and instead redirected to the MainActivity.
     *
     * @return Wheter a course has been picked or not.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public final boolean checkLoginStatus() {
        return false;
    }
    
    /**
     * Updates the UUID on the Rest-Api when the app is started for the first time.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public final void firstStart(@org.jetbrains.annotations.NotNull()
    java.lang.String faculty) {
    }
}