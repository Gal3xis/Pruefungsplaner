package com.fachhochschulebib.fhb.pruefungsplaner.view.helper;

import java.lang.System;

/**
 * The baseclass for all Fragments used isnide the mainactivity. They are given an extra parameter [name] that is used to set the header in the [com.fachhochschulebib.fhb.pruefungsplaner.view.activities.MainActivity]
 * It is abstract, so it cannot be instantiated without inheritation.
 *
 * @author Alexander Lange (Email:alexander.lange@fh-bielefeld.de)
 * @since 1.6
 */
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\b&\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002R\u0018\u0010\u0003\u001a\u00020\u0004X\u00a6\u000e\u00a2\u0006\f\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\b\u00a8\u0006\t"}, d2 = {"Lcom/fachhochschulebib/fhb/pruefungsplaner/view/helper/MainActivityFragment;", "Landroidx/fragment/app/Fragment;", "()V", "name", "", "getName", "()Ljava/lang/String;", "setName", "(Ljava/lang/String;)V", "app_debug"})
public abstract class MainActivityFragment extends androidx.fragment.app.Fragment {
    private java.util.HashMap _$_findViewCache;
    
    public MainActivityFragment() {
        super();
    }
    
    /**
     * A parameter that stores the name of the fragment,which is displayed in the toolbar. Needs to be overridden by ever fragment in the main activity.
     */
    @org.jetbrains.annotations.NotNull()
    public abstract java.lang.String getName();
    
    /**
     * A parameter that stores the name of the fragment,which is displayed in the toolbar. Needs to be overridden by ever fragment in the main activity.
     */
    public abstract void setName(@org.jetbrains.annotations.NotNull()
    java.lang.String p0);
}