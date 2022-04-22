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
import kotlinx.android.synthetic.main.feedback.*
/**
 * Fragment that lets the user give Feedback about the app.
 *
 * @author Alexander Lange (Email:alexander.lange@fh.bielefeld.de)
 * @since 1.6
 */
class FeedbackFragment : MainActivityFragment() {
    override var name: String="Feedback"
    private lateinit var viewModel: FeedbackViewModel

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
        //From onCreate

        //From onCreateView
        buttonSend.setOnClickListener { view ->
            viewModel.sendFeedBack(ratingBarUsability.rating,ratingBarFuntions.rating,ratingBarStability.rating,feedBackInput.text.toString())
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
        return inflater.inflate(R.layout.feedback, container, false)
    }
}