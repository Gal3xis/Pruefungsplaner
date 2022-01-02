package com.Fachhochschulebib.fhb.pruefungsplaner

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_impressum.*

/**
 * A simple [Fragment] subclass.
 * Use the [ImpressumFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ImpressumFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_impressum, container, false)


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        impressum_tv.text = Utils.readTextFile(context!!, R.raw.impressum)
    }
}