package com.Fachhochschulebib.fhb.pruefungsplaner.view.helper

import androidx.fragment.app.Fragment

/**
 * The baseclass for all Fragments used isnide the mainactivity. They are given an extra parameter [name] that is used to set the header in the [com.Fachhochschulebib.fhb.pruefungsplaner.view.activities.MainActivity]
 * It is abstract, so it cannot be instantiated without inheritation.
 *
 * @author Alexander Lange (Email:alexander.lange@fh-bielefeld.de)
 * @since 1.6
 */
abstract class MainActivityFragment: Fragment() {
    abstract var name:String
}