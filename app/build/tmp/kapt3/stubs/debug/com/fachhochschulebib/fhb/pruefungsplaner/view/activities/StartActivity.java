package com.fachhochschulebib.fhb.pruefungsplaner.view.activities;

import java.lang.System;

/**
 * Activity, that allows the user to pick a faculty and select courses.
 * First activity called on appstart and can also be opend from navigationdrawer in the [MainActivity].
 * Also initializes a backgroundworker to look for new Database-updates and an Updatemanager to look for new Appupdates.
 *
 * @author Alexander Lange (Email:alexander.lange@fh-bielefeld.de)
 * @since 1.6
 */
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u000b\u001a\u00020\fH\u0002J\b\u0010\r\u001a\u00020\fH\u0002J\u0010\u0010\u000e\u001a\u00020\f2\u0006\u0010\u000f\u001a\u00020\u0010H\u0002J\b\u0010\u0011\u001a\u00020\fH\u0002J\b\u0010\u0012\u001a\u00020\fH\u0002J\b\u0010\u0013\u001a\u00020\fH\u0002J\"\u0010\u0014\u001a\u00020\f2\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u00162\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019H\u0014J\u0012\u0010\u001a\u001a\u00020\f2\b\u0010\u001b\u001a\u0004\u0018\u00010\u001cH\u0016J\b\u0010\u001d\u001a\u00020\fH\u0014J\b\u0010\u001e\u001a\u00020\fH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001f"}, d2 = {"Lcom/fachhochschulebib/fhb/pruefungsplaner/view/activities/StartActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "installStateUpdateListener", "Lcom/google/android/play/core/install/InstallStateUpdatedListener;", "recyclerViewCourses", "Lcom/fachhochschulebib/fhb/pruefungsplaner/view/helper/CoursesCheckList;", "updateManager", "Lcom/google/android/play/core/appupdate/AppUpdateManager;", "viewModel", "Lcom/fachhochschulebib/fhb/pruefungsplaner/viewmodel/StartViewModel;", "clickedChooseFaculty", "", "clickedOk", "facultyChosen", "faculty", "Lcom/fachhochschulebib/fhb/pruefungsplaner/model/room/Faculty;", "initButtons", "initRecyclerviewCourses", "initUpdateManager", "onActivityResult", "requestCode", "", "resultCode", "data", "Landroid/content/Intent;", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onStop", "startApplication", "app_debug"})
public final class StartActivity extends androidx.appcompat.app.AppCompatActivity {
    
    /**
     * ViewModel for the StartActivity. Is set in [onCreate].
     * @see StartViewModel
     */
    private com.fachhochschulebib.fhb.pruefungsplaner.viewmodel.StartViewModel viewModel;
    
    /**
     * Updatemanager that checks the Playstore for new App updates.
     */
    private com.google.android.play.core.appupdate.AppUpdateManager updateManager;
    
    /**
     * Listener that checks the state of the update download if an update is initiated.
     * Displays a [Snackbar] to let the user know that the update is ready to install.
     */
    private final com.google.android.play.core.install.InstallStateUpdatedListener installStateUpdateListener = null;
    
    /**
     * The recyclerview to display all courses for a selected faculty, from where the user can pick his courses.
     */
    private com.fachhochschulebib.fhb.pruefungsplaner.view.helper.CoursesCheckList recyclerViewCourses;
    private java.util.HashMap _$_findViewCache;
    
    public StartActivity() {
        super();
    }
    
    /**
     * Overrides the onCreate()-Method, which is called first in the Fragment-LifeCycle.
     *
     * @since 1.6
     *
     * @author Alexander Lange (E-Mail:alexander.lange@fh-bielefeld.de)
     *
     * @see Fragment.onCreate
     */
    @java.lang.Override()
    public void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    /**
     * Initializes the updatemanager. The updatemanager checks the google playstore for new appupdates
     * and if one was found he starts a dialog in which the user can choose if he wants
     * to update or not.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private final void initUpdateManager() {
    }
    
    /**
     * Initializes the recyclerview that shows all courses for the selected faculty.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private final void initRecyclerviewCourses() {
    }
    
    /**
     * Initializes the buttons of the UI.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private final void initButtons() {
    }
    
    /**
     * Creates a dialog that asks the user to select a faculty.
     * @author Alexander Lange
     * @since 1.6
     */
    private final void clickedChooseFaculty() {
    }
    
    /**
     * Creates a dialog that asks the User to select a main course.
     *
     * @param[view] The view that calls this method.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private final void clickedOk() {
    }
    
    /**
     * Dispatch incoming result to the correct fragment.
     *
     * @param[requestCode] – The integer request code originally supplied to startActivityForResult(), allowing you to identify who this result came from.
     * @param[resultCode] – The integer result code returned by the child activity through its setResult().
     * @param[data] – An Intent, which can return result data to the caller (various data can be attached to Intent "extras").
     *
     * @author Alexander Lange
     * @since 1.6
     *
     * @see AppCompatActivity.onActivityResult
     */
    @java.lang.Override()
    protected void onActivityResult(int requestCode, int resultCode, @org.jetbrains.annotations.Nullable()
    android.content.Intent data) {
    }
    
    /**
     * Called when the app is stopped.
     *
     * @author Alexander Lange
     * @since 1.6
     *
     * @see AppCompatActivity.onStop
     */
    @java.lang.Override()
    protected void onStop() {
    }
    
    /**
     * Opens the [MainActivity].
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private final void startApplication() {
    }
    
    /**
     * Called, when the user picked a faculty.
     * Fills the recyclerview with courses.
     *
     * @param[faculties] The list of faculties.
     * @param[which] The index of the picked item.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private final void facultyChosen(com.fachhochschulebib.fhb.pruefungsplaner.model.room.Faculty faculty) {
    }
}