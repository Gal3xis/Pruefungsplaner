package com.Fachhochschulebib.fhb.pruefungsplaner

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.Fachhochschulebib.fhb.pruefungsplaner.model.RetrofitConnect
import com.Fachhochschulebib.fhb.pruefungsplaner.data.AppDatabase
import android.os.Looper
import android.widget.Toast
import android.content.Intent
import android.content.SharedPreferences
import android.os.Handler
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.feedback.*
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
 * Lets the user give Feedback about the app.
 *
 * @author Alexander Lange (Email:alexander.lange@fh.bielefeld.de)
 * @since 1.6
 */
class FeedbackFragment : Fragment() {
    private lateinit var viewModel: MainViewModel

    /**
     * Overrides the onCreate()-Method, which is called first in the Fragment-LifeCycle.
     * In this Method, the global parameter which are independent of the UI get initialized,
     * like the App-SharedPreferences and the reference to the Room-Database
     *
     * @since 1.6
     *
     * @author Alexander Lange (E-Mail:alexander.lange@fh-bielefeld.de)
     *
     * @see Fragment.onCreate
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(
            requireActivity(), MainViewModelFactory(requireActivity().application)
        )[MainViewModel::class.java]
    }

    /**
     * Overrides the onViewCreated()-Method, which is called in the Fragment LifeCycle right after the onCreateView()-Method.

     * @since 1.6
     * @author Alexander Lange (E-Mail:alexander.lange@fh-bielefeld.de)
     * @see Fragment.onViewCreated
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //From onCreate

        //From onCreateView
        buttonSend.setOnClickListener { view ->
            val serverAddress = viewModel.getServerIPAddress()
            val relativePPlanURL = viewModel.getServerRelUrlPath()
            //retrofit auruf
            val retrofit = context?.let { RetrofitConnect(viewModel, it) }
                retrofit?.sendFeedBack(
                    ratingBarUsability.rating,
                    ratingBarFuntions.rating,
                    ratingBarStability.rating,
                    feedBackInput.text.toString(),
                )
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
     * @since 1.6
     * @author Alexander Lange (E-Mail:alexander.lange@fh-bielefeld.de)
     * @see Fragment.onCreateView
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.feedback, container, false)
        return v
    }
}