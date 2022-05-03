package com.fachhochschulebib.fhb.pruefungsplaner.view.helper

import androidx.fragment.app.Fragment

/**
 * The baseclass for all Fragments used isnide the mainactivity. They are given an extra parameter [name] that is used to set the header in the [com.fachhochschulebib.fhb.pruefungsplaner.view.activities.MainActivity]
 * It is abstract, so it cannot be instantiated without inheritation.
 *
 * @author Alexander Lange (Email:alexander.lange@fh-bielefeld.de)
 * @since 1.6
 */
abstract class MainActivityFragment: Fragment() {
    /**
     * A parameter that stores the name of the fragment,which is displayed in the toolbar. Needs to be overridden by ever fragment in the main activity.
     */
    abstract var name:String
}