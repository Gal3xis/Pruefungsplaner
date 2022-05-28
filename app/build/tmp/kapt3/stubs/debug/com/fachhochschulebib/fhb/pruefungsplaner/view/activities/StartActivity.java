package com.fachhochschulebib.fhb.pruefungsplaner.view.activities;

import java.lang.System;

/**
 * Activity, that allows the user to pick a faculty and select courses.
 * First activity called on app start and can also be opened from navigationdrawer in the [MainActivity].
 * Also initializes a background worker to look for new Database-updates and an Updatemanager to look for new App updates.
 *
 * @author Alexander Lange (Email:alexander.lange@fh-bielefeld.de)
 * @since 1.6
 */
@kotlin.Suppress(names = {"DEPRECATION"})
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0007\u001a\u00020\bH\u0002J\b\u0010\t\u001a\u00020\bH\u0002J\u0010\u0010\n\u001a\u00020\b2\u0006\u0010\u000b\u001a\u00020\fH\u0002J\b\u0010\r\u001a\u00020\bH\u0002J\b\u0010\u000e\u001a\u00020\bH\u0002J\u0012\u0010\u000f\u001a\u00020\b2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0011H\u0016J\b\u0010\u0012\u001a\u00020\bH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0013"}, d2 = {"Lcom/fachhochschulebib/fhb/pruefungsplaner/view/activities/StartActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "recyclerViewCourses", "Lcom/fachhochschulebib/fhb/pruefungsplaner/view/helper/CoursesCheckList;", "viewModel", "Lcom/fachhochschulebib/fhb/pruefungsplaner/viewmodel/StartViewModel;", "clickedChooseFaculty", "", "clickedOk", "facultyChosen", "faculty", "Lcom/fachhochschulebib/fhb/pruefungsplaner/model/room/Faculty;", "initButtons", "initRecyclerviewCourses", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "startApplication", "app_debug"})
public final class StartActivity extends androidx.appcompat.app.AppCompatActivity {
    
    /**
     * ViewModel for the StartActivity. Is set in [onCreate].
     * @see StartViewModel
     */
    private com.fachhochschulebib.fhb.pruefungsplaner.viewmodel.StartViewModel viewModel;
    
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
     * @author Alexander Lange
     * @since 1.6
     */
    private final void clickedOk() {
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
     * @param faculty The chosen faculty
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private final void facultyChosen(com.fachhochschulebib.fhb.pruefungsplaner.model.room.Faculty faculty) {
    }
}