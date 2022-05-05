package com.fachhochschulebib.fhb.pruefungsplaner.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fachhochschulebib.fhb.pruefungsplaner.R
import com.fachhochschulebib.fhb.pruefungsplaner.utils.Utils
import com.fachhochschulebib.fhb.pruefungsplaner.view.helper.MainActivityFragment
import kotlinx.android.synthetic.main.fragment_privacy_declaration.*
/**
 * Fragment, that shows the privacy-declaration.
 * @author Alexander Lange (Email:alexander.lange@fh-bielefeld.de)
 * @since 1.6
 */
class PrivacyDeclarationFragment : MainActivityFragment() {

    /**
     * Sets the name of that fragment to "Datenschutzerklärung"
     */
    override var name: String="Datenschutzerklärung"

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
        fragment_privacy_declaration_textview_privacy_declaration.text = context?.let { Utils.readTextFile(it,R.raw.text_privacy_declarement) }
    }

    /**
     * Overrides the onCreateView()-Method. It sets the current view to the privacy_declaration-layout.
     *
     * @return Returns the initialized view of this Fragment
     *
     * @author Alexander Lange
     * @since 1.6
     *
     * @see Fragment.onCreateView
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_privacy_declaration, container, false)
    }
}