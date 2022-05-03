package com.fachhochschulebib.fhb.pruefungsplaner.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fachhochschulebib.fhb.pruefungsplaner.R
import com.fachhochschulebib.fhb.pruefungsplaner.utils.Utils
import com.fachhochschulebib.fhb.pruefungsplaner.view.helper.MainActivityFragment
import kotlinx.android.synthetic.main.fragment_impressum.*

/**
 * Fragment. that displays the impressum for the app.
 *
 * @author Alexander Lange (Email:alexander.lange@fh-bielefeld.de)
 * @since 1.6
 */
class ImpressumFragment : MainActivityFragment() {
    /**
     * Sets the name of that fragment to "Impressum"
     */
    override var name: String="Impressum"

    /**
     * Overrides the onCreateView()-Method. It sets the current view to the fragment_impressum-layout.
     *
     * @return Returns the initialized view of this Fragment
     * @since 1.6
     * @author Alexander Lange
     * @see Fragment.onCreateView
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_impressum, container, false)
    }

    /**
     * Overrides the onViewCreated()-Method, which is called in the Fragment LifeCycle right after the onCreateView()-Method.
     * Sets the test of the impressum-textview
     *
     * @since 1.6
     * @author Alexander Lange
     * @see Fragment.onViewCreated
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        impressum_tv.text = Utils.readTextFile(requireContext(), R.raw.impressum)
        impressum_backButton.setOnClickListener {
            val ft = activity?.supportFragmentManager?.beginTransaction()
            ft?.replace(R.id.frame_placeholder, SettingsFragment())
            ft?.commit()
        }
    }
}