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
class PrivacyDeclarationFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.privacy_declaration, container, false)
        //TODO REMOVE val okButton = v.findViewById<Button>(R.id.privacyDeclarationButton)
        privacyDeclarationButton.setOnClickListener {
            val ft = activity?.supportFragmentManager?.beginTransaction()
            ft?.replace(R.id.frame_placeholder, Optionen())
            ft?.commit()
        }
        return v
    }
}