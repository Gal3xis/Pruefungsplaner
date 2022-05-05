package com.fachhochschulebib.fhb.pruefungsplaner.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import android.content.Intent
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.fachhochschulebib.fhb.pruefungsplaner.viewmodel.ViewModelFactory
import com.fachhochschulebib.fhb.pruefungsplaner.R
import com.fachhochschulebib.fhb.pruefungsplaner.view.activities.MainActivity
import com.fachhochschulebib.fhb.pruefungsplaner.view.helper.MainActivityFragment
import com.fachhochschulebib.fhb.pruefungsplaner.viewmodel.FeedbackViewModel
import kotlinx.android.synthetic.main.fragment_feedback.*
/**
 * Fragment that lets the user give Feedback about the app.
 *
 * @author Alexander Lange (Email:alexander.lange@fh.bielefeld.de)
 * @since 1.6
 */
class FeedbackFragment : MainActivityFragment() {

    /**
     * ViewModel for the FeedbackFragment. Is set in [onViewCreated].
     * @see FeedbackViewModel
     */
    private lateinit var viewModel: FeedbackViewModel

    /**
     * Sets the name of that fragment to "Feedback"
     */
    override var name: String="Feedback"

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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(
            requireActivity(), ViewModelFactory(requireActivity().application)
        )[FeedbackViewModel::class.java]
    }

    /**
     * Overrides the onViewCreated()-Method, which is called in the Fragment LifeCycle right after the onCreateView()-Method.
     *
     * @author Alexander Lange
     * @since 1.6
     *
     * @see Fragment.onViewCreated
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragment_feedback_button_send.setOnClickListener { view ->
            viewModel.sendFeedBack(fragment_feedback_ratingbar_usability.rating,fragment_feedback_ratingbar_functions.rating,fragment_feedback_ratingbar_stability.rating,fragment_feedback_text_input_feedbacktext.text.toString())
            Toast.makeText(
                view.context,
                view.context.getString(R.string.sendedFeedBack),
                Toast.LENGTH_SHORT
            ).show()
            val mainWindow = Intent(view.context, MainActivity::class.java)
            startActivity(mainWindow)
        }
    }

    /**
     * Overrides the onCreateView()-Method. It sets the current view to the terminefragment-layout.
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
        return inflater.inflate(R.layout.fragment_feedback, container, false)
    }
}