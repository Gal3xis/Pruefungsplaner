package com.Fachhochschulebib.fhb.pruefungsplaner

import android.content.SharedPreferences
import com.Fachhochschulebib.fhb.pruefungsplaner.data.TestPlanEntry
import com.Fachhochschulebib.fhb.pruefungsplaner.data.AppDatabase
import com.Fachhochschulebib.fhb.pruefungsplaner.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Looper
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.text.TextWatcher
import android.text.Editable
import android.app.Activity
import android.content.Context
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.fragment.app.Fragment
import com.Fachhochschulebib.fhb.pruefungsplaner.TermineFragmentSearch
import kotlinx.android.synthetic.main.activity_suche.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

//////////////////////////////
// sucheFragment
//
// autor:
// inhalt:  Auswahl und Suche von Professoren, Modulen, Semestern, Prüfungsphase
// zugriffsdatum: 11.12.19
//
//
//////////////////////////////
class searchFragment : Fragment() {
    val courseModuleList: MutableList<String?> = ArrayList<String?>()
    val profList: MutableList<String?> = ArrayList<String?>()
    val returnProfList: MutableList<Int?> = ArrayList<Int?>()
    val returnCourseModuleList: MutableList<Int?> = ArrayList<Int?>()
    val returnDateList: MutableList<Int?> = ArrayList<Int?>()
    val returnSemesterModuleList: MutableList<Int?> = ArrayList<Int?>()
    val sortedList: MutableList<String?> = ArrayList<String?>()
    private var profName: String? = null
    private var dateForSearch: String? = null
    var examineYear: String? = null
    var currentExaminePeriod: String? = null
    var returnCourse: String? = null
    var validation: String? = null
    var ppeList: List<TestPlanEntry?> = ArrayList<TestPlanEntry?>()
    private val database = AppDatabase.getAppDatabase(context!!)
    var roomData = AppDatabase.getAppDatabase(context!!)

    // Start Merlin Gürtler
    // Funktion um die Führende 0 hinzuzufügen
    fun formatDate(dateToFormat: String): String {
        var dateToFormat = dateToFormat
        if (dateToFormat.length == 1) {
            dateToFormat = "0$dateToFormat"
        }
        return dateToFormat
    }

    // Ende Merlin Gürtler
    // Start Merlin Gürtler
    fun registerButton(btn: Button, value: Int) {
        btn.setOnClickListener(object : View.OnClickListener {
            var clicked = true
            override fun onClick(v: View) {
                if (clicked) {
                    if (returnSemesterModuleList.size <= 0) {
                        for (i in 0 until ppeList.size) {
                            returnSemesterModuleList.add(99999)
                        }
                    }
                    for (i in ppeList.indices) {
                        if (value.toString() == ppeList[i]?.semester) {
                            btn.setBackgroundResource(R.drawable.button_rounded_corners)
                            returnSemesterModuleList[i] = i
                        }
                    }
                    clicked = false
                } else {
                    btn.setBackgroundResource(R.drawable.button_rounded_corners2)
                    for (i in ppeList.indices) {
                        if (value.toString() == ppeList[i]?.semester) {
                            returnSemesterModuleList[i] = 99999
                        }
                    }
                    clicked = true
                }
            }
        })
    }

    // Ende Merlin Gürtler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        profName = context!!.getString(R.string.all)

        // Nun aus Shared Preferences
        // Hole die Werte für die Validierung
        val mSharedPreferencesValidation =
            container?.context?.getSharedPreferences("validation", Context.MODE_PRIVATE)
        examineYear = mSharedPreferencesValidation?.getString("examineYear", "0")
        currentExaminePeriod = mSharedPreferencesValidation?.getString("currentPeriode", "0")
        returnCourse = mSharedPreferencesValidation?.getString("returnCourse", "0")
        val selectedCourse = mSharedPreferencesValidation?.getString("selectedCourse", "0")

        // Start Merlin Gürtler
        //TODO Make Coroutine
        Thread { // Erstelle Validierung und starte DB Abfrage
            validation = examineYear + returnCourse + currentExaminePeriod
            ppeList = roomData?.userDao()?.getEntriesByValidation(validation)!!
            // Ende Merlin Gürtler

            //Überprüfung, ob ein Semester-Button geklickt wurde
            //der Wert des Semsters wird gespeichert
            returnSemesterModuleList.clear()

            //Initialisierung der Anfangswerte
            var i: Int
            i = 0
            while (i < ppeList.size) {
                returnProfList.add(i)
                returnCourseModuleList.add(i)
                returnDateList.add(i)
                i++
            }
        }.start()
        val v = inflater.inflate(R.layout.activity_suche, container, false)
        //setContentView(R.layout.hauptfenster);
        //TODO REMOVE val acProf = v.findViewById<View>(R.id.acProfessor) as AutoCompleteTextView
        //TODO REMOVE val spCourseModule = v.findViewById<View>(R.id.spStudiengang) as Spinner

        //Initialiseren der UI Komponente
        //Spinners spinner = new Spinners();
        //TODO REMOVE val btnSemester1 = v.findViewById<View>(R.id.btns1) as Button
        //TODO REMOVE val btnSemester2 = v.findViewById<View>(R.id.btns2) as Button
        //TODO REMOVE val btnSemester3 = v.findViewById<View>(R.id.btns3) as Button
        //TODO REMOVE val btnSemester4 = v.findViewById<View>(R.id.btns4) as Button
        //TODO REMOVE val btnSemester5 = v.findViewById<View>(R.id.btns5) as Button
        //TODO REMOVE val btnSemester6 = v.findViewById<View>(R.id.btns6) as Button

        // Start Merlin Gürtler
        registerButton(btns1, 1)
        registerButton(btns2, 2)
        registerButton(btns3, 3)
        registerButton(btns4, 4)
        registerButton(btns5, 5)
        registerButton(btns6, 6)
        // Ende Merlin Gürtler

        //Auswahlmöglichkeit "Klicken um Modul zu wählen" hinzufügen
        val spinnerModuleArrayList: MutableList<String> = ArrayList()
        spinnerModuleArrayList.add(0, context!!.getString(R.string.modul_search))

        //Adapter-Aufruf (LG: Sind hier alle drei Adapter notwendig?)
        // Auswahl Module
        // Hier schon setzen für ein besseres UI
        val adapterModule = ArrayAdapter(
            v.context, R.layout.simple_spinner_item, spinnerModuleArrayList
        )
        spStudiengang.adapter = adapterModule
        try {
            //TODO CHANGE TO COROUTINE
            Thread {
                //Spinner-Aufruf und Spinner mit Werten füllen
                spinnerModuleArrayList.addAll(
                    (roomData!!.userDao()!!.getModuleWithCourseDistinct(selectedCourse)?:"") as Collection<String>
                )
                val spinnerProfArrayList =
                    roomData?.userDao()?.getFirstExaminerDistinct(selectedCourse)

                // Für das AutoComplete
                val adapterProfAutoComplete = ArrayAdapter(
                    v.context,
                    android.R.layout.simple_list_item_1,
                    spinnerProfArrayList?: mutableListOf()
                )

                //Grafische Ausgabe dropdown
                adapterModule.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // adapterProf.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Handler(Looper.getMainLooper()).post { //Spinner spProf = (Spinner) v.findViewById(R.id.spProf);
                    //spProf.setAdapter(adapterProf);
                    acProfessor.threshold = 1 //will start working from first character
                    acProfessor.setAdapter(adapterProfAutoComplete) //setting the adapter data
                    // into the AutoCompleteTextView
                    // beim ändern der Orientierung crasht die app wegen Problemen mit dem Context
                    try {
                        profList.add(context!!.getString(R.string.all))
                        courseModuleList.add(context!!.getString(R.string.all))
                    } catch (e: Exception) {
                        Log.d("ERROR", "ERROR $e")
                    }
                }
            }.start()


            // Start Merlin Gürtler
            val searchDate = v.findViewById<TextView>(R.id.daySearch)
            searchDate.setOnClickListener {
                val sharedPrefCurrentPeriode =
                    context?.getSharedPreferences("currentPeriode", Context.MODE_PRIVATE)
                val startDate = sharedPrefCurrentPeriode?.getString("startDate", "0")
                val endDate = sharedPrefCurrentPeriode?.getString("endDate", "0")
                // Daten des Startdatums
                val day = startDate?.substring(0, 2)?.toInt()
                val month = startDate?.substring(3, 5)?.toInt()
                val year = startDate?.substring(6, 10)?.toInt()
                val startDateForPicker: Calendar? = Calendar.getInstance()
                startDateForPicker?.set(Calendar.YEAR, year ?: 0)
                startDateForPicker?.set(Calendar.MONTH, (month ?: 1 - 1))
                startDateForPicker?.set(Calendar.DAY_OF_MONTH, day ?: 0)

                // Daten des Enddatums
                val day2 = endDate?.substring(0, 2)?.toInt()
                val month2 = endDate?.substring(3, 5)?.toInt()
                val year2 = endDate?.substring(6, 10)?.toInt()
                val endDateForPicker: Calendar? = Calendar.getInstance()
                endDateForPicker?.set(Calendar.YEAR, year2 ?: 0)
                endDateForPicker?.set(Calendar.MONTH, month2 ?: 1 - 1)
                endDateForPicker?.set(Calendar.DAY_OF_MONTH, day2 ?: 0)

                val picker = DatePickerDialog(
                    context!!,
                R.style.ProgressStyle,
                DatePickerDialog.OnDateSetListener{
                 view, year, monthOfYear, dayOfMonth ->
                searchDate.text = (formatDate(dayOfMonth.toString())
                            + "." + formatDate(monthOfYear.toString())
                            + "." + formatDate(year.toString()))

                    // Das Datum für die Abfrage
                    val selectedDate = Calendar.getInstance()
                    selectedDate[Calendar.YEAR] = year
                    selectedDate[Calendar.MONTH] = monthOfYear
                    selectedDate[Calendar.DAY_OF_MONTH] = dayOfMonth
                    val targetFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    dateForSearch = targetFormat.format(selectedDate.time)
                }, year?:0, month?:0, day?:0)
                // Setze das Start- und Enddatum
                picker.datePicker.minDate = startDateForPicker?.timeInMillis ?: 0
                picker.datePicker.maxDate = endDateForPicker?.timeInMillis ?: 0
                picker.show()
            }

            // The TextChanged Listener is listening
            // on the input events of the AutoCompleteTextView acProf
            // this is necessary because the rueckgabe before
            // only changed when the field was clicked
            acProfessor.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

                // After the text changed the name is saved in the List
                // after the input the app iterate
                // through the intern Database and select
                // the dozent
                override fun afterTextChanged(s: Editable) {
                    var a: Int
                    returnProfList.clear()
                    profName = acProfessor.text.toString()
                    a = 0
                    while (a < ppeList.size) {
                        if (acProfessor.text.toString() == context!!.getString(R.string.all)) {
                            returnProfList.add(a)
                        } else if (Pattern.matches(
                                "^.*(" // Wildcard begin
                                        + acProfessor.text.toString().trim { it <= ' ' }
                                    .toLowerCase() // Input Name
                                        + ").*$",  // Wildcard end
                                ppeList[a]?.firstExaminer?.toLowerCase()
                            )
                        ) // Name in db
                        {
                            returnProfList.add(a)
                        }
                        a++
                    }
                }
            })

            // Ende Merlin Gürtler
            acProfessor.onItemClickListener =
                AdapterView.OnItemClickListener { parent, view, position, id -> // Start Merlin Gürtler
                    // Schließe das Keyboard nachdem ein Dozent gewählt wurde
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
                    // This part is unecessary the Text changed Listener is the better
                    // solution because now the user can write an professor on his own,
                    // so he must not choose the Item
                    // Ende Merlin Gürtler
                    /*
                    int a;
                    rueckgabeProfList.clear();
                    for (a = 0; a < ppeList.size(); a++) {
                        if (acProf.getText().toString().equals("Alle")) {
                            rueckgabeProfList.add(a);

                        } else if (acProf.getText()
                                         .toString()
                                         .equals(ppeList.get(a).getErstpruefer())) {
                            rueckgabeProfList.add(a);
                            //TextView textt = (TextView) v.findViewById(R.id.txtmessage);
                        }
                    }
                    //txtview.setText(prof.get(prof.size()-1).toString()
                    // + pruefplandaten.profname[i].toString());
                    */
                } //... your stuf
            spStudiengang.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    returnCourseModuleList.clear()
                    //this is your selected item
                    courseModuleList.add(parent.getItemAtPosition(position).toString())
                    var i: Int
                    var a: String
                    i = 0
                    while (i < ppeList.size) {
                        if (courseModuleList[courseModuleList.size - 1].toString()
                            == context!!.getString(R.string.modul_search)
                        ) {
                            returnCourseModuleList.add(i)
                        } else {
                            if (courseModuleList[courseModuleList.size - 1].toString()
                                == ppeList[i]?.module.toString()
                            ) {
                                returnCourseModuleList.add(i)
                                // database.userDao().Checkverbindung(tableReturn());
                            }
                        }
                        i++
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
            //TODO REMOVE val btnOk = v.findViewById<View>(R.id.BtnOk) as Button
            //final TextView textt = (TextView) v.findViewById(R.id.txtmessage);
            BtnOk.setOnClickListener { v ->
                // Start Merlin Gürtler
                //TODO CHANGE TO COROUTINE
                Thread {
                    if (dateForSearch != null) {
                        sortedList.clear()
                        ppeList = roomData?.userDao()
                            ?.getEntriesByDate(dateForSearch?.substring(0, 10) + "%")?: mutableListOf()
                        database?.userDao()?.searchAndReset(false)
                        for (entry in ppeList) {
                            database?.userDao()?.update2(true, entry?.id?.toInt() ?: -1)
                        }
                    } else {
                        if (profName == context!!.getString(R.string.all) && courseModuleList[courseModuleList.size - 1].toString() != context!!.getString(
                                R.string.modul_search
                            )
                        ) {
                            sortedList.clear()
                            ppeList = roomData?.userDao()
                                ?.getEntriesByModule(courseModuleList[courseModuleList.size - 1])?: mutableListOf()
                            database?.userDao()?.searchAndReset(false)
                            for (entry in ppeList) {
                                database?.userDao()?.update2(true, entry?.id?.toInt()?:0)
                            }
                        } else if (profName != context!!.getString(R.string.all)) {
                            sortedList.clear()
                            ppeList = roomData?.userDao()?.getEntriesByProf("%" +
                                    acProfessor.text.toString().trim { it <= ' ' } + "%")?: mutableListOf()
                            for (entry in ppeList) {
                                database?.userDao()?.update2(true, entry?.id?.toInt() ?: -1)
                            }
                        } else {
                            // Ende Merlin Gürtler
                            if (acProfessor.text.toString() == context!!.getString(R.string.all)) {
                                var a: Int
                                returnProfList.clear()
                                a = 0
                                while (a < ppeList.size) {
                                    returnProfList.add(a)
                                    a++
                                }
                            }
                            database?.userDao()?.searchAndReset(false)
                            val ppeList = AppDatabase.getAppDatabase(v.context)
                                ?.userDao()?.getEntriesByValidation(validation)
                            for (i in tableReturn().indices) {
                                // Toast.makeText(getContext(),tableReturn().get(i),
                                // Toast.LENGTH_SHORT).show();
                                if (tableReturn()[i]?.toInt() != null) {
                                    database?.userDao()
                                        ?.update2(true, ppeList?.get(tableReturn()[i]?.toInt()?:0)?.id?.toInt()?:0)
                                }
                            }
                        }
                    }

                    // Merlin Gürtler
                    // Schließe das Keyboard falls offen
                    val inputMethodManager = activity!!.getSystemService(
                        Activity.INPUT_METHOD_SERVICE
                    ) as InputMethodManager
                    try {
                        inputMethodManager.hideSoftInputFromWindow(
                            activity?.currentFocus?.windowToken, 0
                        )
                    } catch (e: Exception) {
                        Log.d("Exception", "Keyboard not open")
                    }
                    val ft = activity?.supportFragmentManager?.beginTransaction()
                    ft?.replace(R.id.frame_placeholder, TermineFragmentSearch())
                    ft?.commit()
                }.start()
            }
        } catch (e: Exception) {
            Log.d("Fehler sucheFragment", "Fehler beim Ermitteln der Module")
        }
        return v
    }

    fun tableReturn(): List<String?> {
        var i: Int
        var j: Int
        var k: Int
        var l: Int
        var test = "a"
        var checkSemester = true
        for (z in returnSemesterModuleList.indices) {
            //überprüfung, ob Semester ausgewählt wurden. Sonst alle Semester anzeigen.
            if (returnSemesterModuleList[z] != returnSemesterModuleList[z + 1]) {
                // DONE (08/2020) LG: Vereinfachung if(!...)
                // Gäbler: Nicht alle Semester anzeigen, weil ein oder
                // mehrere Semester ausgewählt wurden
                checkSemester = false
                break
            }
        }
        if (checkSemester) {
            for (z in ppeList.indices) {
                returnSemesterModuleList.add(z)
            }
        }
        sortedList.clear()
        i = 0
        while (i < returnCourseModuleList.size) {
            j = 0
            while (j < returnSemesterModuleList.size) {
                if (returnCourseModuleList[i] == returnSemesterModuleList[j]) {
                    k = 0
                    while (k < returnDateList.size) {
                        if (returnDateList[k]
                            == returnCourseModuleList[i]
                        ) {
                            l = 0
                            while (l < returnProfList.size) {
                                if (returnProfList[l]
                                    == returnCourseModuleList[i]
                                ) {
                                    sortedList.add(returnCourseModuleList[i].toString())
                                    test = returnDateList[k].toString() + test
                                } //if
                                l++
                            }
                        } //of
                        k++
                    }
                    //rueckgabeStudiengang.add(i);
                } //if
                j++
            }
            i++
        }
        return sortedList
    }
}