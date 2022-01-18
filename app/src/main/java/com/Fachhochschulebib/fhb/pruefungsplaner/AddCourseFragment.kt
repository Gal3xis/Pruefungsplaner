package com.Fachhochschulebib.fhb.pruefungsplaner

import android.content.Context
import com.Fachhochschulebib.fhb.pruefungsplaner.data.AppDatabase
import android.os.Bundle
import android.content.SharedPreferences
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.Fachhochschulebib.fhb.pruefungsplaner.model.RetrofitConnect
import android.widget.Toast
import android.content.Intent
import android.os.Handler
import android.view.View
import androidx.fragment.app.Fragment
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList

//TODO Alexander Lange Start
import kotlinx.android.synthetic.main.choose_courses.*
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//TODO Alexander Lange End

/**
 * Class, that creates a list of all courses, from whom the user can decide,
 * which courses he wants to include in his exam-list.
 *
 * @since 1.6
 * @author Alexander Lange
 */
class AddCourseFragment() : Fragment() {
    // References to the shared preferences
    var mSharedPreferencesPPServerAdress: SharedPreferences? = null
    var mSharedPreferencesCurrentTermin: SharedPreferences? = null
    var mSharedPreferencesValidation: SharedPreferences? = null

    // Reference to the Room-Database
    var database: AppDatabase? = null

    var mAdapter: CheckListAdapter? = null
    var courseChosen: MutableList<Boolean> = ArrayList()
    var courseName: MutableList<String> = ArrayList()
    val scope_io = CoroutineScope(CoroutineName("IO-Scope") + Dispatchers.IO)

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
        database = context?.let { AppDatabase.getAppDatabase(it) }

        mSharedPreferencesPPServerAdress =
                context?.getSharedPreferences("Server_Address", Context.MODE_PRIVATE)
        mSharedPreferencesCurrentTermin =
                context?.getSharedPreferences("examineTermin", Context.MODE_PRIVATE)
        mSharedPreferencesValidation =
                context?.getSharedPreferences("validation", Context.MODE_PRIVATE)
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
        val v = inflater.inflate(R.layout.choose_courses, container, false)

        return v
    }

    /**
     * Overrides the onViewCreated()-Method, which is called in the Fragment LifeCycle right after the onCreateView()-Method.

     * @since 1.6
     * @author Alexander Lange (E-Mail:alexander.lange@fh-bielefeld.de)
     * @see Fragment.onViewCreated
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //From onCreateView
        //Komponenten  initialisieren für die Verwendung
        initRecyclerview()
        initOkButton()
    }

    /**
     * Initializes the OK, Button. Implements what happens when the user clicks Ok.
     *
     * @since 1.6
     * @author Alexander Lange
     */
    private fun initOkButton() {
        buttonOk.setOnClickListener { v ->
            scope_io.launch {// Aktualisiere die Studiengänge
                for (i in courseChosen.indices) {
                    database?.userDao()?.updateCourse(
                            courseName[i],
                            courseChosen[i]
                    )
                }
                updateDbEntries()


            }.invokeOnCompletion {
                Handler(Looper.getMainLooper()).post { // Feedback nach Update
                    Toast.makeText(
                            v.context,
                            v.context.getString(R.string.courseActualisation),
                            Toast.LENGTH_SHORT
                    ).show()
                    activity?.recreate()
                    /*val mainWindow = Intent(v.context, MainActivity::class.java)
                    startActivity(mainWindow)*/
                }
            }
        }
    }

    /**
     * Updates the Room-Database with data from the Server.
     *
     * @since 1.6
     * @author Alexander Lange
     */
    private fun updateDbEntries() {
        // die Retrofitdaten aus den Shared Preferences
        val relativePPlanURL =
                mSharedPreferencesPPServerAdress?.getString("ServerRelUrlPath", "0")
        val serverAddress =
                mSharedPreferencesPPServerAdress?.getString("ServerIPAddress", "0")
        val currentDate =
                mSharedPreferencesCurrentTermin?.getString("currentTermin", "0")
        val examineYear =
                mSharedPreferencesValidation?.getString("examineYear", "0")
        val currentExamine =
                mSharedPreferencesValidation?.getString("currentPeriode", "0")
        val courses = database?.userDao()?.allCourses?.sortedBy { it?.courseName }


        // aktualsiere die db Einträge
        val courseIds = JSONArray()
        var courseName: String
        if (courses != null) {
            for (course in courses) {
                try {
                    courseName = course?.courseName ?: ""
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
    }

    /**
     * Initializes the recyclerview. Obtains data from the Room-Database, creates an adapter and passes
     * it to the recyclerview.
     *
     * @since 1.6
     * @author Alexander Lange
     */
    private fun initRecyclerview() {
        recyclerViewChecklist.setHasFixedSize(true)
        //linear layout manager
        val layoutManager = LinearLayoutManager(context)
        recyclerViewChecklist.layoutManager = layoutManager
        scope_io.launch {
            val faculty = mSharedPreferencesValidation?.getString("returnFaculty", "0")

            // Fülle die Recylcerview
            val courses = database?.userDao()?.getAllCoursesByFacultyId(faculty)
            if (courses != null) {
                for (cours in courses) {
                    courseName.add(cours?.courseName ?: "")
                    courseChosen.add(cours?.choosen ?: false)
                }
            }
            mAdapter = CheckListAdapter(
                    courseName,
                    courseChosen,
                    activity?.applicationContext!!
            )
        }.invokeOnCompletion {
            Handler(Looper.getMainLooper()).post {
                recyclerViewChecklist?.adapter = mAdapter
            }
        }

    }
}