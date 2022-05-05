package com.fachhochschulebib.fhb.pruefungsplaner.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
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
import kotlinx.android.synthetic.main.fragment_change_courses.*

/**
 *Fragment to change the courseselection. Lets the user add additional courses and change the main course.
 *
 * @author Alexander Lange (Email:alexander.lange@fh-bielefeld.de)
 * @since 1.6
 */
class ChangeCoursesFragment : MainActivityFragment() {
    /**
     * ViewModel for the ChangeCoursesFragment. Is set in [onViewCreated].
     * @see ChangeCoursesViewModel
     */
    private lateinit var viewModel: ChangeCoursesViewModel

    /**
     * Sets the name of that fragment to "Studiengänge verwalten"
     */
    override var name: String="Studiengänge verwalten"

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
        return inflater.inflate(R.layout.fragment_change_courses, container, false)
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
            fragment_change_courses_spinner_change_main.adapter = ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,courseNames)
            fragment_change_courses_spinner_change_main.setSelection(selectedMainCourseName)
        }
        viewModel.getCourses()
        fragment_change_courses_spinner_change_main.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if(view == null) return
                (view as TextView).setTextColor(Utils.getColorFromAttr(R.attr.colorOnPrimaryDark,requireContext()))
                val name:String =fragment_change_courses_spinner_change_main.selectedItem.toString()
                viewModel.changeMainCourse(name)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    /**
     * Initializes the recyclerview that shows the list of courses an if they are selected or not. Obtains data from the Room-Database, creates an adapter and passes
     * it to the recyclerview.
     *
     * @since 1.6
     * @author Alexander Lange
     */
    private fun initRecyclerview() {
        fragment_change_courses_recyclerview_courses.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(context)
        fragment_change_courses_recyclerview_courses.layoutManager = layoutManager
        viewModel.liveCoursesForFaculty.observe(viewLifecycleOwner){
            fragment_change_courses_recyclerview_courses?.adapter = context?.let { it1 ->
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