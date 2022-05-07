package com.fachhochschulebib.fhb.pruefungsplaner.view.helper

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*

/**
 * The baseclass for all Fragments used inside the main activity. They are given an extra parameter [name] that is used to set the header in the [com.fachhochschulebib.fhb.pruefungsplaner.view.activities.MainActivity]
 * It is abstract, so it cannot be instantiated without inheritation.
 *
 * @author Alexander Lange (Email:alexander.lange@fh-bielefeld.de)
 * @since 1.6
 */
abstract class MainActivityFragment: Fragment() {

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
    abstract fun getName(context:Context):String

}