package com.fachhochschulebib.fhb.pruefungsplaner.view.helper;

import java.lang.System;

/**
 * The baseclass for all Fragments used inside the main activity. They are given an extra parameter [name] that is used to set the header in the [com.fachhochschulebib.fhb.pruefungsplaner.view.activities.MainActivity]
 * It is abstract, so it cannot be instantiated without inheritation.
 *
 * @author Alexander Lange (Email:alexander.lange@fh-bielefeld.de)
 * @since 1.6
 */
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\b&\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H&\u00a8\u0006\u0007"}, d2 = {"Lcom/fachhochschulebib/fhb/pruefungsplaner/view/helper/MainActivityFragment;", "Landroidx/fragment/app/Fragment;", "()V", "getName", "", "context", "Landroid/content/Context;", "app_debug"})
public abstract class MainActivityFragment extends androidx.fragment.app.Fragment {
    private java.util.HashMap _$_findViewCache;
    
    public MainActivityFragment() {
        super();
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
    public abstract java.lang.String getName(@org.jetbrains.annotations.NotNull()
    android.content.Context context);
}