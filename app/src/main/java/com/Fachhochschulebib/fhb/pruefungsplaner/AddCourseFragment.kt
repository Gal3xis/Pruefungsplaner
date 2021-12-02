package com.Fachhochschulebib.fhb.pruefungsplaner

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.Fachhochschulebib.fhb.pruefungsplaner.CheckListAdapter
import com.Fachhochschulebib.fhb.pruefungsplaner.data.AppDatabase
import android.os.Bundle
import android.content.SharedPreferences
import com.Fachhochschulebib.fhb.pruefungsplaner.data.Courses
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import com.Fachhochschulebib.fhb.pruefungsplaner.R
import androidx.recyclerview.widget.LinearLayoutManager
import com.Fachhochschulebib.fhb.pruefungsplaner.data.TestPlanEntry
import com.Fachhochschulebib.fhb.pruefungsplaner.model.RetrofitConnect
import android.widget.Toast
import android.content.Intent
import android.os.Handler
import android.view.View
import androidx.fragment.app.Fragment
import com.Fachhochschulebib.fhb.pruefungsplaner.table
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList

//TODO Alexander Lange Start
import kotlinx.android.synthetic.main.choose_courses.*
//TODO Alexander Lange End



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
class AddCourseFragment() : Fragment() {
    private var recyclerView: RecyclerView? = null
    var mAdapter: CheckListAdapter? = null
    var courseChosen: MutableList<Boolean> = ArrayList()
    var courseName: MutableList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //From onCreate
        val database = AppDatabase.getAppDatabase(context!!)
        //TODO Change to Coroutine
        Thread(object : Runnable {
            override fun run() {
                // Erhalte die gewählte Fakultät aus den Shared Preferences
                val sharedPreferencesFacultyEditor = context?.getSharedPreferences("validation", Context.MODE_PRIVATE)
                val faculty = sharedPreferencesFacultyEditor?.getString("returnFaculty", "0")

                // Fülle die Recylcerview
                val courses = database?.userDao()?.getAllCoursesByFacultyId(faculty)
                if (courses != null) {
                    for (cours in courses) {
                        courseName.add(cours?.courseName?:"")
                        courseChosen.add(cours?.choosen?:false)
                    }
                }
                mAdapter = CheckListAdapter(
                    courseName,
                    courseChosen,
                    activity?.applicationContext!!
                )
                Handler(Looper.getMainLooper()).post(Runnable { recyclerView?.adapter = mAdapter })
            }
        }).start()

        //From onCreateView
        //Komponenten  initialisieren für die Verwendung
        /*TODO Remove recyclerView = v.findViewById<View>(R.id.recyclerViewChecklist) as RecyclerView*/
        recyclerViewChecklist.setHasFixedSize(true)
        //linear layout manager
        val layoutManager = LinearLayoutManager(context)
        recyclerViewChecklist.layoutManager = layoutManager
        buttonOk.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                //TODO Change to Coroutine
                Thread(object : Runnable {
                    override fun run() {
                        // Aktualisiere die Studiengänge
                        for (i in courseChosen.indices) {
                            database?.userDao()?.updateCourse(
                                courseName[i],
                                courseChosen[i]
                            )
                        }

                        // die Retrofitdaten aus den Shared Preferences
                        val mSharedPreferencesPPServerAdress = this@AddCourseFragment.context
                            ?.getSharedPreferences("Server_Address", Context.MODE_PRIVATE)
                        val relativePPlanURL =
                            mSharedPreferencesPPServerAdress?.getString("ServerRelUrlPath", "0")
                        val serverAddress =
                            mSharedPreferencesPPServerAdress?.getString("ServerIPAddress", "0")
                        val mSharedPreferencesCurrentTermin = this@AddCourseFragment.context
                            ?.getSharedPreferences("examineTermin", Context.MODE_PRIVATE)
                        val currentDate =
                            mSharedPreferencesCurrentTermin?.getString("currentTermin", "0")
                        val mSharedPreferencesValidation = this@AddCourseFragment.context
                            ?.getSharedPreferences("validation", Context.MODE_PRIVATE)
                        val examineYear = mSharedPreferencesValidation?.getString("examineYear", "0")
                        val currentExamine =
                            mSharedPreferencesValidation?.getString("currentPeriode", "0")
                        val courses = database?.userDao()?.allCourses

                        // aktualsiere die db Einträge
                        val courseIds = JSONArray()
                        var courseName: String
                        if (courses != null) {
                            for (course in courses) {
                                try {
                                    courseName = course?.courseName?:""
                                    if (!course?.choosen!!) {
                                        // lösche nicht die Einträge der gewählten Studiengänge und Favorit
                                        val toDelete = database?.userDao()
                                            ?.getEntriesByCourseName(courseName, false)
                                        database?.userDao()?.deleteEntry(toDelete)
                                    }
                                    if (database?.userDao()?.getOneEntryByName(
                                            courseName,
                                            false
                                        ) == null && course.choosen!!
                                    ) {
                                        val idJson = JSONObject()
                                        idJson.put("ID", course.sgid)
                                        courseIds.put(idJson)
                                    }
                                } catch (e: JSONException) {
                                    e.printStackTrace()
                                }
                            }
                        }
                        val retrofit = RetrofitConnect(relativePPlanURL!!)
                        // > 2 da auch bei einem leeren Json Array [] gesetzt werden
                        if (courseIds.toString().length > 2) {
                            retrofit.UpdateUnkownCourses(
                                context!!,
                                database!!,
                                examineYear!!,
                                currentExamine!!,
                                currentDate!!,
                                serverAddress!!,
                                courseIds.toString()
                            )
                        }
                        retrofit.setUserCourses(
                            context!!, database!!,
                            serverAddress
                        )
                        Handler(Looper.getMainLooper()).post(object : Runnable {
                            override fun run() {
                                // Feedback nach Update
                                Toast.makeText(
                                    v.context,
                                    v.context.getString(R.string.courseActualisation),
                                    Toast.LENGTH_SHORT
                                ).show()
                                val mainWindow = Intent(v.context, table::class.java)
                                startActivity(mainWindow)
                            }
                        })
                    }
                }).start()
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.choose_courses, container, false)

        return v
    }
}