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
class AddCourseFragment : Fragment() {
    private lateinit var viewModel: MainViewModel

    var mAdapter: CheckListAdapter? = null
    var courseChosen: MutableList<Boolean> = ArrayList()
    var courseName: MutableList<String> = ArrayList()

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
        buttonOk.setOnClickListener { view ->
            for (i in courseChosen.indices) {
                viewModel.updateCourse(
                    courseName[i],
                    courseChosen[i]
                )
            }
            updateDbEntries()
            // Feedback nach Update
            Toast.makeText(
                view.context,
                view.context.getString(R.string.courseActualisation),
                Toast.LENGTH_SHORT
            ).show()
            val mainWindow = Intent(view.context, MainActivity::class.java)
            startActivity(mainWindow)
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
        val relativePPlanURL = viewModel.getServerRelUrlPath()

        val serverAddress = viewModel.getServerIPAddress()
        val currentDate = viewModel.getCurrentTermin()
        val examineYear = viewModel.getExamineYear()
        val currentExamine = viewModel.getCurrentPeriode()
        val courses = viewModel.getAllCourses()

        // aktualsiere die db Einträge
        val courseIds = JSONArray()
        var courseName: String
        if (courses != null) {
            for (course in courses) {
                try {
                    courseName = course.courseName ?: ""
                    if (course.choosen == false) {
                        // lösche nicht die Einträge der gewählten Studiengänge und Favorit
                        val toDelete = viewModel.getEntriesByCourseName(courseName, false)
                        toDelete?.let { viewModel.deleteEntries(it) }
                    }
                    if (viewModel.getOneEntryByName(
                            courseName,
                            false
                        ) == null && course.choosen == true
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
        val retrofit = context?.let { RetrofitConnect(viewModel, it) }
        // > 2 da auch bei einem leeren Json Array [] gesetzt werden
        if (courseIds.toString().length > 2 && context != null && examineYear != null && currentExamine != null && currentDate != null && serverAddress != null) {
            retrofit?.UpdateUnkownCourses(
                courseIds.toString(),
                )
        }
        retrofit?.setUserCourses()
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
        val faculty = viewModel.getReturnFaculty()

        // Fülle die Recylcerview
        val courses = faculty?.let { viewModel.getAllCoursesByFacultyid(it) }
        if (courses != null) {
            for (course in courses) {
                courseName.add(course.courseName ?: "")
                courseChosen.add(course.choosen ?: false)
            }
        }
        mAdapter = CheckListAdapter(
            courseName,
            courseChosen,
            activity?.applicationContext!!,
            viewModel
        )
        recyclerViewChecklist?.adapter = mAdapter
    }
}