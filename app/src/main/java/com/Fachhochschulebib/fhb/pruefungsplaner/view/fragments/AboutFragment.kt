package com.Fachhochschulebib.fhb.pruefungsplaner.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.Fachhochschulebib.fhb.pruefungsplaner.R
import com.Fachhochschulebib.fhb.pruefungsplaner.view.helper.MainActivityFragment
import kotlinx.android.synthetic.main.fragment_about.*
import java.lang.StringBuilder

/**
 * A simple [Fragment] subclass.
 * Use the [AboutFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AboutFragment : MainActivityFragment() {

    override var name: String = "Über"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.fragment_about, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        about_app_information?.text = getInformation()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun getInformation():String{
        val builder:StringBuilder = StringBuilder()
        builder.append("Appversion:").append(context?.packageName?.let { activity?.packageManager?.getPackageInfo(it,0) }?.versionName)
        return if(builder.isEmpty())"Test" else builder.toString()
    }
}