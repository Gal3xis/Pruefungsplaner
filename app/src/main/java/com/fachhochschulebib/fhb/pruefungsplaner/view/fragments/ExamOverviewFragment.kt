package com.fachhochschulebib.fhb.pruefungsplaner.view.fragments

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fachhochschulebib.fhb.pruefungsplaner.*
import com.fachhochschulebib.fhb.pruefungsplaner.utils.Filter
import com.fachhochschulebib.fhb.pruefungsplaner.view.helper.MainActivityFragment
import com.fachhochschulebib.fhb.pruefungsplaner.view.helper.RecyclerViewExamAdapter
import com.fachhochschulebib.fhb.pruefungsplaner.viewmodel.ViewModelFactory
import com.fachhochschulebib.fhb.pruefungsplaner.viewmodel.ExamOverviewViewModel
import kotlinx.android.synthetic.main.fragment_exam_overview.*

/**
 * Fragment that shows all Exams in the next period for the selected courses.
 * The user can pick them as favorites or display details for each exam.
 *
 * @constructor Whether the filter shall be resetted when opening the fragment.
 *
 * @author Alexander Lange (Email:alexander.lange@fh-bielefeld.de)
 * @since 1.6
 *
 */
class ExamOverviewFragment() : MainActivityFragment() {

    /**
     * ViewModel for the ExamOverviewFragment. Is set in [onViewCreated].
     * @see ExamOverviewViewModel
     */
    private lateinit var viewModel: ExamOverviewViewModel

    /**
     * The adapter of the recyclerview that displays all exams to the user.
     * Contains a list of all exams for the selected courses with the current Filter applied.
     * @see RecyclerViewExamAdapter
     */
    private lateinit var recyclerViewExamAdapter: RecyclerViewExamAdapter

    /**
     * Overrides the onCreate()-Method, which is called first in the Fragment-LifeCycle.
     *
     * @author Alexander Lange
     * @since 1.6
     *
     * @see Fragment.onCreate
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    /**
     * Needs to be implemented by every fragment to return the name of the fragment
     *
     * @param context The applicationcontext to access the string resources
     *
     * @return The name of the fragment
     *
     * @author Alexander Lange
     * @since 1.6
     */
    override fun getName(context: Context): String {
        return context.getString(R.string.fragment_exam_overview_name)
    }

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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_exam_overview, container, false)
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
        viewModel = ViewModelProvider(
                requireActivity(),
                ViewModelFactory(requireActivity().application)
        )[ExamOverviewViewModel::class.java]
        viewModel.getCalendarPermission(requireActivity())
        Filter.onFilterChangedListener.add {
            viewModel.liveEntryList.value?.let { Filter.validateList(it) }?.let { recyclerViewExamAdapter.updateContent(it) }
        }
        initRecyclerview()
    }

    /**
     * Initializes the Recyclerview which shows the information about pending exams.
     *
     * @author Alexander Lange
     * @since 1.6
     *
     */
    private fun initRecyclerview() {
        recyclerViewExamAdapter = RecyclerViewExamAdapter(requireContext(),mutableListOf(),viewModel)
        fragment_exam_overview_recyclerview_exam.adapter = recyclerViewExamAdapter
        fragment_exam_overview_recyclerview_exam.visibility = RecyclerView.VISIBLE
        viewModel.liveEntryList.observe(viewLifecycleOwner){ entryList ->
            entryList?.let {
                recyclerViewExamAdapter.updateContent(Filter.validateList(it).toMutableList())
            }
            fragment_exam_overview_swipe_refresh_layout.isRefreshing = false
        }
        fragment_exam_overview_swipe_refresh_layout.setDistanceToTriggerSync(800)
        fragment_exam_overview_swipe_refresh_layout.setOnRefreshListener {
            viewModel.updatePeriod(requireContext())
            viewModel.updateDataFromServer()
            fragment_exam_overview_swipe_refresh_layout.isRefreshing = false
        }
        fragment_exam_overview_recyclerview_exam?.layoutManager = LinearLayoutManager(view?.context)
    }
}