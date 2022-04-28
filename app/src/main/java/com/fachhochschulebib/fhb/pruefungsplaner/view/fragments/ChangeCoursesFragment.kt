package com.fachhochschulebib.fhb.pruefungsplaner.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fachhochschulebib.fhb.pruefungsplaner.R
import com.fachhochschulebib.fhb.pruefungsplaner.utils.Utils
import com.fachhochschulebib.fhb.pruefungsplaner.utils.setSelection
import com.fachhochschulebib.fhb.pruefungsplaner.view.helper.CoursesCheckList
import com.fachhochschulebib.fhb.pruefungsplaner.view.helper.MainActivityFragment
import com.fachhochschulebib.fhb.pruefungsplaner.viewmodel.ChangeCoursesViewModel
import com.fachhochschulebib.fhb.pruefungsplaner.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.choose_courses.*

/**
 *Fragment to change the courseselection. Lets the user add additional courses and change the main course.
 *
 * @author Alexander Lange (Email:alexander.lange@fh-bielefeld.de)
 * @since 1.6
 */
class ChangeCoursesFragment : MainActivityFragment() {
    override var name: String="Studiengänge verwalten"

    private lateinit var viewModel: ChangeCoursesViewModel
    var mCourses: CoursesCheckList? = null
    var courseChosen: MutableList<Boolean> = ArrayList()
    var courseName: MutableList<String> = ArrayList()

    /**
     * Overrides the onCreateView()-Method.
     *
     * @return Returns the initialized view of this Fragment
     *
     * @author Alexander Lange
     * @since 1.6
     *
     * @see Fragment.onCreateView
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.choose_courses, container, false)
    }

    /**
     * Overrides the onViewCreated()-Method, which is called in the Fragment LifeCycle right after the onCreateView()-Method.
     *
     * @author Alexander Lange
     * @since 1.6
     *
     * @see Fragment.onViewCreated
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity(), ViewModelFactory(requireActivity().application))[ChangeCoursesViewModel::class.java]
        initRecyclerview()
        initOkButton()
        initSpinner()
    }

    /**
     * Initializes the spinner to change the main course.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private fun initSpinner(){
        viewModel.liveCoursesForFaculty.observe(viewLifecycleOwner){
            val courseNames:MutableList<String> = mutableListOf()
            var selectedMainCourseName:String? = null
            it?.forEach { course ->
                courseNames.add(course.courseName)
                if(course.sgid==viewModel.getMainCourseId()){
                    selectedMainCourseName = course.courseName
                }
            }
            choose_course_change_main_spinner.adapter = ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,courseNames)
            choose_course_change_main_spinner.setSelection(selectedMainCourseName)
        }
        viewModel.getCourses()
        choose_course_change_main_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if(view == null) return
                (view as TextView).setTextColor(Utils.getColorFromAttr(R.attr.colorOnPrimaryDark,requireContext().theme))
                val name:String =choose_course_change_main_spinner.selectedItem.toString()
                viewModel.changeMainCourse(name)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
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
            viewModel.updateDbEntries()
            Toast.makeText(view.context,view.context.getString(R.string.courseActualisation),Toast.LENGTH_SHORT).show()
            returnToMainscreen(view)
        }
    }

    /**
     * Lets the user return to the [MainActivityFragment]
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private fun returnToMainscreen(view: View) {
        activity?.recreate()
    }

    /**
     * Initializes the recyclerview that shows the list of courses an if they are selected or not. Obtains data from the Room-Database, creates an adapter and passes
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
        viewModel.liveCoursesForFaculty.observe(viewLifecycleOwner){
            recyclerViewChecklist?.adapter = context?.let { it1 ->
                it?.let { it2 ->
                    CoursesCheckList(
                        it2,viewModel,
                        it1
                    )
                }
            }
        }
        viewModel.getCourses()
    }
}