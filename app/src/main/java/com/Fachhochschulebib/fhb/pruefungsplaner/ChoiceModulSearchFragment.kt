package com.Fachhochschulebib.fhb.pruefungsplaner

import com.Fachhochschulebib.fhb.pruefungsplaner.data.AppDatabase
import com.Fachhochschulebib.fhb.pruefungsplaner.data.TestPlanEntry
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.Fachhochschulebib.fhb.pruefungsplaner.R
import android.os.Looper
import android.app.Activity
import android.os.Handler
import android.widget.AdapterView.OnItemSelectedListener
import android.text.TextWatcher
import android.text.Editable
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.Fachhochschulebib.fhb.pruefungsplaner.TermineFragmentSearch
import androidx.fragment.app.Fragment
import java.lang.Exception
import java.util.ArrayList


import kotlinx.android.synthetic.main.wahlmodul.*

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
class ChoiceModulSearchFragment : Fragment() {
    private val database = AppDatabase.getAppDatabase(context)
    var ppeList: List<TestPlanEntry>? = null
    private var selectedCourseSpinner: String? = null
    private var modulName: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.wahlmodul, container, false)
        //TODO REMOVEval spCourse = v.findViewById<Spinner>(R.id.spStudiengang)
        //Todo removeval searchBtn = v.findViewById<Button>(R.id.BtnOk)
        //TODO REMOVEval editChoiceModul = v.findViewById<AutoCompleteTextView>(R.id.wahlModulName)

        // Studiengang auswahl
        val courseArrayList: MutableList<String> = ArrayList()
        courseArrayList.add(0, v.context.getString(R.string.all_cours))

        // Design für den Spinner
        // Hier schon setzen für ein besseres UI
        val adapterCourse = ArrayAdapter(
            v.context, R.layout.simple_spinner_item, courseArrayList
        )
        adapterCourse.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spStudiengang.adapter = adapterCourse
        selectedCourseSpinner = context?.getString(R.string.all_cours)
        modulName = context?.getString(R.string.all)
        //TODO Change to Coroutine
        Thread {
            val modulNameArrayList = database.userDao().moduleOrdered
            val adapterModuleAutoComplete =
                ArrayAdapter(v.context, android.R.layout.simple_list_item_1, modulNameArrayList)
            courseArrayList.addAll(database.userDao().getChoosenCourse(true))
            Handler(Looper.getMainLooper()).post {
                wahlModulName.setAdapter(adapterModuleAutoComplete)
                // spStudiengang.setAdapter(adapterStudiengang);
            }
        }.start()
        wahlModulName.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id -> // Schließe das Keyboard nach auswahl des Modules
                val inputMethodManager = activity?.getSystemService(
                    Activity.INPUT_METHOD_SERVICE
                ) as InputMethodManager
                try {
                    inputMethodManager.hideSoftInputFromWindow(
                        activity?.currentFocus?.windowToken, 0
                    )
                } catch (e: Exception) {
                    Log.d("Exception", "Keyboard not open")
                }
            }
        spStudiengang.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                // Setze den ausgewählten Studiengang
                selectedCourseSpinner = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //
            }
        }
        wahlModulName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                //
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                //
            }

            override fun afterTextChanged(s: Editable) {
                // Setze das ausgewählte Modul
                modulName = wahlModulName.text.toString()
            }
        })
        BtnOk.setOnClickListener { v ->
            /*
                    Um nicht zu viele Module zu laden muss der Name mindestens drei Zeichen lang sein,
                    oder ein Studiengang ausgewählt sein
                     */
            Thread {
                if (modulName?.trim { it <= ' ' }?.length!=null&&modulName?.trim { it <= ' ' }?.length!! > 3 && modulName != context!!.getString(R.string.all)
                    || selectedCourseSpinner != context?.getString(R.string.all_cours)
                ) {

                    // Nur ein Modulnamen eingetragen
                    if (selectedCourseSpinner == context?.getString(R.string.all_cours) && modulName != context?.getString(
                            R.string.all
                        )
                    ) {
                        ppeList = database.userDao()
                            .getEntriesByModule("%" + modulName?.trim { it <= ' ' } + "%")

                        // Alles eingegeben
                    } else if (selectedCourseSpinner != context?.getString(R.string.all_cours)
                        && modulName != context?.getString(R.string.all)
                    ) {
                        ppeList = database.userDao().getEntriesWithCourseAndModule("%" +
                                modulName?.trim { it <= ' ' } + "%", selectedCourseSpinner)

                        // Nur ein Studiengang ausgewählt
                    } else if (selectedCourseSpinner != context?.getString(R.string.all_cours)
                        && modulName == context?.getString(R.string.all)
                    ) {
                        ppeList =
                            database.userDao().getEntriesWithCourseOrdered(selectedCourseSpinner)
                    }


                    // Setze die gewählten Daten in der DB
                    database.userDao().searchAndReset(false)
                    for (entry in ppeList!!) {
                        database.userDao().update2(true, entry.id.toInt())
                    }

                    // Merlin Gürtler
                    // Schließe das Keyboard falls offen
                    val inputMethodManager = activity?.getSystemService(
                        Activity.INPUT_METHOD_SERVICE
                    ) as InputMethodManager
                    try {
                        inputMethodManager?.hideSoftInputFromWindow(
                            activity?.currentFocus?.windowToken, 0
                        )
                    } catch (e: Exception) {
                        Log.d("Exception", "Keyboard not open")
                    }
                    val ft = activity?.supportFragmentManager?.beginTransaction()
                    ft?.replace(R.id.frame_placeholder, TermineFragmentSearch())
                    ft?.commit()
                }
                Handler(Looper.getMainLooper()).post {
                    if (!(modulName?.trim { it <= ' ' }?.length!=null&&modulName?.trim { it <= ' ' }?.length!! > 3 && modulName != v.context.getString(
                            R.string.all
                        )
                                || selectedCourseSpinner != v.context.getString(R.string.all_cours))
                    ) {
                        Toast.makeText(
                            v.context,
                            v.context.getString(R.string.elective_search),
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                }
            }.start()
        }
        return v
    }
}