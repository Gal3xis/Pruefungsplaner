package com.Fachhochschulebib.fhb.pruefungsplaner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.Fachhochschulebib.fhb.pruefungsplaner.model.RetrofitConnect
import android.widget.Toast
import android.content.Intent
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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

    var mCourses: CoursesCheckList? = null
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
        viewModel = ViewModelProvider(requireActivity(),MainViewModelFactory(requireActivity().application))[MainViewModel::class.java]
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
            Toast.makeText(view.context,view.context.getString(R.string.courseActualisation),Toast.LENGTH_SHORT).show()
            returnToMainscreen(view)
        }
    }

    private fun returnToMainscreen(view: View) {
        activity?.recreate()
    }

    /**
     * Updates the Room-Database with data from the Server.
     *
     * @since 1.6
     * @author Alexander Lange
     */
    private fun updateDbEntries() {
        val courses = viewModel.getAllCourses()
        // aktualsiere die db Einträge
        val courseIds = JSONArray()
        var courseName: String
        if (courses != null) {
            for (course in courses) {
                try {
                    courseName = course.courseName ?: ""
                    if (course.choosen == false) {
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
        val retrofit = context?.let { RetrofitConnect(it) }
        // > 2 da auch bei einem leeren Json Array [] gesetzt werden
        //TODO retrofit?.setUserCourses()
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
        viewModel.getReturnFaculty()?.let { viewModel.setReturnFaculty(it) }
        viewModel.liveCoursesForFacultyId.observe(viewLifecycleOwner){
            recyclerViewChecklist?.adapter = context?.let { it1 ->
                it?.let { it2 ->
                    CoursesCheckList(
                        it2,viewModel,
                        it1
                    )
                }
            }
        }
    }
}