package com.Fachhochschulebib.fhb.pruefungsplaner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.Fachhochschulebib.fhb.pruefungsplaner.R
import com.Fachhochschulebib.fhb.pruefungsplaner.Optionen
import kotlinx.android.synthetic.main.privacy_declaration.*

//////////////////////////////
// TerminefragmentSuche
//
//
//
// autor:
// inhalt:  Ermöglicht die Suche nach Wahlmodulen und zur darstelllung an den Recycleview adapter übergeben
// zugriffsdatum: 01.09.20
//
//
//
//
//
//
//////////////////////////////
/**
 * [Fragment], that shows the privacy-declaration.
 * @author Alexander Lange (Email:alexander.lange@fh-bielefeld.de)
 * @since 1.6
 */
class PrivacyDeclarationFragment : Fragment() {

    /**
     * Overrides the onViewCreated()-Method, which is called in the Fragment LifeCycle right after the onCreateView()-Method.
     * In this Method, the UI-Elements choose_course.xml-Layout are being initialized. This cannot be done in the onCreate()-Method,
     * because the UI-Elements, which are directly accessed via synthetic imports
     * are no instantiated in the onCreate()-Method yet.
     * @author Alexander Lange
     * @since 1.6
     * @see Fragment.onViewCreated
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        privacyDeclarationButton.setOnClickListener {
            val ft = activity?.supportFragmentManager?.beginTransaction()
            ft?.replace(R.id.frame_placeholder, Optionen())
            ft?.commit()
        }
    }

    /**
     * Overrides the onCreateView()-Method. It sets the current view to the privacy_declaration-layout.
     *
     * @return Returns the initialized view of this Fragment
     * @since 1.6
     * @author Alexander Lange (E-Mail:alexander.lange@fh-bielefeld.de)
     * @see Fragment.onCreateView
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.privacy_declaration, container, false)

        return v
    }
}