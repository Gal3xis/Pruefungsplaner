package com.fachhochschulebib.fhb.pruefungsplaner.view.fragments;

import java.lang.System;

/**
 * Fragment that displays the favorites of the user. He can display details and delete them from favorites.
 *
 * @author Alexander Lange (Email:alexander.lange@fh-bielefeld.de)
 * @since 1.6
 */
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0016J\b\u0010\u000b\u001a\u00020\fH\u0002J\u0012\u0010\r\u001a\u00020\f2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0016J&\u0010\u0010\u001a\u0004\u0018\u00010\u00112\u0006\u0010\u0012\u001a\u00020\u00132\b\u0010\u0014\u001a\u0004\u0018\u00010\u00152\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0016J\b\u0010\u0016\u001a\u00020\fH\u0017J\u001a\u0010\u0017\u001a\u00020\f2\u0006\u0010\u0018\u001a\u00020\u00112\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0019"}, d2 = {"Lcom/fachhochschulebib/fhb/pruefungsplaner/view/fragments/FavoriteOverviewFragment;", "Lcom/fachhochschulebib/fhb/pruefungsplaner/view/helper/MainActivityFragment;", "()V", "recyclerViewFavoriteAdapter", "Lcom/fachhochschulebib/fhb/pruefungsplaner/view/helper/RecyclerViewFavoriteAdapter;", "viewModel", "Lcom/fachhochschulebib/fhb/pruefungsplaner/viewmodel/FavoriteOverviewViewModel;", "getName", "", "context", "Landroid/content/Context;", "initRecyclerview", "", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "onResume", "onViewCreated", "view", "app_debug"})
public final class FavoriteOverviewFragment extends com.fachhochschulebib.fhb.pruefungsplaner.view.helper.MainActivityFragment {
    
    /**
     * ViewModel for the FavoriteOverviewFragment. Is set in [onViewCreated].
     * @see FavoriteOverviewViewModel
     */
    private com.fachhochschulebib.fhb.pruefungsplaner.viewmodel.FavoriteOverviewViewModel viewModel;
    
    /**
     * The adapter of the recyclerview that displays all favorite exams to the user.
     * Contains a list of all exams for the selected courses with the current Filter applied.
     * @see RecyclerViewFavoriteAdapter
     */
    private com.fachhochschulebib.fhb.pruefungsplaner.view.helper.RecyclerViewFavoriteAdapter recyclerViewFavoriteAdapter;
    private java.util.HashMap _$_findViewCache;
    
    public FavoriteOverviewFragment() {
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
     * Called when this fragment is resumed after being paused. Invalidates the adapter, so external changes will be made visible like calendar entry.
     */
    @android.annotation.SuppressLint(value = {"NotifyDataSetChanged"})
    @java.lang.Override()
    public void onResume() {
    }
    
    /**
     * Initializes the recyclerview, that shows the selected courses.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private final void initRecyclerview() {
    }
}