package com.fachhochschulebib.fhb.pruefungsplaner.view.fragments;

import java.lang.System;

/**
 * Fragment that lets the user give Feedback about the app.
 *
 * @author Alexander Lange (Email:alexander.lange@fh.bielefeld.de)
 * @since 1.6
 */
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0016J\u0012\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u0016J&\u0010\r\u001a\u0004\u0018\u00010\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u00122\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u0016J\u001a\u0010\u0013\u001a\u00020\n2\u0006\u0010\u0014\u001a\u00020\u000e2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0015"}, d2 = {"Lcom/fachhochschulebib/fhb/pruefungsplaner/view/fragments/FeedbackFragment;", "Lcom/fachhochschulebib/fhb/pruefungsplaner/view/helper/MainActivityFragment;", "()V", "viewModel", "Lcom/fachhochschulebib/fhb/pruefungsplaner/viewmodel/FeedbackViewModel;", "getName", "", "context", "Landroid/content/Context;", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "onViewCreated", "view", "app_debug"})
public final class FeedbackFragment extends com.fachhochschulebib.fhb.pruefungsplaner.view.helper.MainActivityFragment {
    
    /**
     * ViewModel for the FeedbackFragment. Is set in [onViewCreated].
     * @see FeedbackViewModel
     */
    private com.fachhochschulebib.fhb.pruefungsplaner.viewmodel.FeedbackViewModel viewModel;
    private java.util.HashMap _$_findViewCache;
    
    public FeedbackFragment() {
        super();
    }
    
    /**
     * Overrides the onCreate()-Method, which is called first in the Fragment-LifeCycle.
     * In this Method, the global parameter which are independent of the UI get initialized,
     * like the App-SharedPreferences and the reference to the Room-Database
     *
     * @author Alexander Lange
     * @since 1.6
     *
     * @see Fragment.onCreate
     */
    @java.lang.Override()
    public void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    /**
     * Needs to be implemented by every fragment to return the name of the fragment
     *
     * @param context The applicationcontext to access the string resources
     *
     * @return The name of the fragment
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public java.lang.String getName(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
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
     * Overrides the onCreateView()-Method
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
}