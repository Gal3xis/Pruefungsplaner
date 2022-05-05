package com.fachhochschulebib.fhb.pruefungsplaner.view.fragments;

import java.lang.System;

/**
 * Fragment to change the course selection. Lets the user add additional courses and change the main course.
 *
 * @author Alexander Lange (Email:alexander.lange@fh-bielefeld.de)
 * @since 1.6
 */
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u000b\u001a\u00020\fH\u0002J\b\u0010\r\u001a\u00020\fH\u0002J&\u0010\u000e\u001a\u0004\u0018\u00010\u000f2\u0006\u0010\u0010\u001a\u00020\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u00132\b\u0010\u0014\u001a\u0004\u0018\u00010\u0015H\u0016J\u001a\u0010\u0016\u001a\u00020\f2\u0006\u0010\u0017\u001a\u00020\u000f2\b\u0010\u0014\u001a\u0004\u0018\u00010\u0015H\u0016R\u001a\u0010\u0003\u001a\u00020\u0004X\u0096\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u000e\u0010\t\u001a\u00020\nX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0018"}, d2 = {"Lcom/fachhochschulebib/fhb/pruefungsplaner/view/fragments/ChangeCoursesFragment;", "Lcom/fachhochschulebib/fhb/pruefungsplaner/view/helper/MainActivityFragment;", "()V", "name", "", "getName", "()Ljava/lang/String;", "setName", "(Ljava/lang/String;)V", "viewModel", "Lcom/fachhochschulebib/fhb/pruefungsplaner/viewmodel/ChangeCoursesViewModel;", "initRecyclerview", "", "initSpinner", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onViewCreated", "view", "app_debug"})
public final class ChangeCoursesFragment extends com.fachhochschulebib.fhb.pruefungsplaner.view.helper.MainActivityFragment {
    
    /**
     * ViewModel for the ChangeCoursesFragment. Is set in [onViewCreated].
     * @see ChangeCoursesViewModel
     */
    private com.fachhochschulebib.fhb.pruefungsplaner.viewmodel.ChangeCoursesViewModel viewModel;
    
    /**
     * Sets the name of that fragment
     */
    @org.jetbrains.annotations.NotNull()
    private java.lang.String name = "Studieng\u00e4nge verwalten";
    private java.util.HashMap _$_findViewCache;
    
    public ChangeCoursesFragment() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public java.lang.String getName() {
        return null;
    }
    
    @java.lang.Override()
    public void setName(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
    
    /**
     * Overrides the onCreateView()-Method.
     *
     * @return Returns the initialized view of this Fragment
     *
     * @author Alexander Lange
     * @since 1.6
     *
     * @see Fragment.onCreateView
     */
    @org.jetbrains.annotations.Nullable()
    @java.lang.Override()
    public android.view.View onCreateView(@org.jetbrains.annotations.NotNull()
    android.view.LayoutInflater inflater, @org.jetbrains.annotations.Nullable()
    android.view.ViewGroup container, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
        return null;
    }
    
    /**
     * Overrides the onViewCreated()-Method, which is called in the Fragment LifeCycle right after the onCreateView()-Method.
     *
     * @author Alexander Lange
     * @since 1.6
     *
     * @see Fragment.onViewCreated
     */
    @java.lang.Override()
    public void onViewCreated(@org.jetbrains.annotations.NotNull()
    android.view.View view, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    /**
     * Initializes the spinner to change the main course.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private final void initSpinner() {
    }
    
    /**
     * Initializes the recyclerview that shows the list of courses an if they are selected or not. Obtains data from the Room-Database, creates an adapter and passes
     * it to the recyclerview.
     *
     * @since 1.6
     * @author Alexander Lange
     */
    private final void initRecyclerview() {
    }
}