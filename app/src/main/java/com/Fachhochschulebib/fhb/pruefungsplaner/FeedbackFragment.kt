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
import android.os.Handler
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.feedback.*

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
class FeedbackFragment : Fragment() {
    var serverAddress: String? = null
    var relativePPlanURL: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //From onCreate

        //From onCreateView
        // Die UI Komponenten
        //TODO REMOVE val ratingBarUsability = v.findViewById<RatingBar>(R.id.ratingBarUsability)
        //TODO REMOVE val ratingBarStability = v.findViewById<RatingBar>(R.id.ratingBarStability)
        //TODO REMOVE val ratingBarFunctions = v.findViewById<RatingBar>(R.id.ratingBarFuntions)
        //TODO REMOVE val feedBackInput = v.findViewById<TextView>(R.id.feedBackInput)
        //TODO REMOVE val buttonSend = v.findViewById<Button>(R.id.buttonSend)
        buttonSend.setOnClickListener { view ->
            val mSharedPreferencesPPServerAdress =
                view.context.getSharedPreferences("Server_Address", Context.MODE_PRIVATE)
            serverAddress = mSharedPreferencesPPServerAdress.getString("ServerIPAddress", "0")
            relativePPlanURL = mSharedPreferencesPPServerAdress.getString("ServerRelUrlPath", "0")
            //retrofit auruf
            val retrofit = RetrofitConnect(relativePPlanURL?:"")

            // Initialisiere die Datenbank
            val datenbank = AppDatabase.getAppDatabase(view.context)
            Thread {
                // Übergebe die Daten an Retrofit
                if (datenbank != null) {
                    retrofit.sendFeedBack(
                        view.context, datenbank, serverAddress,
                        ratingBarUsability.rating, ratingBarFuntions.rating,
                        ratingBarStability.rating, feedBackInput.text.toString()
                    )
                }
                Handler(Looper.getMainLooper()).post { // Sende eine Nachricht nachdem senden des Feedbacks
                    Toast.makeText(
                        view.context,
                        view.context.getString(R.string.sendedFeedBack),
                        Toast.LENGTH_SHORT
                    ).show()
                    val mainWindow = Intent(view.context, MainActivity::class.java)
                    startActivity(mainWindow)
                }
            }.start()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.feedback, container, false)


        return v
    }
}