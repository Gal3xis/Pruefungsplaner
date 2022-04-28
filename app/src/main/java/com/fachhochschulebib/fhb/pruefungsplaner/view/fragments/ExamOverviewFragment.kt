package com.fachhochschulebib.fhb.pruefungsplaner.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fachhochschulebib.fhb.pruefungsplaner.*
import com.fachhochschulebib.fhb.pruefungsplaner.utils.Filter
import com.fachhochschulebib.fhb.pruefungsplaner.view.activities.MainActivity
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
    override var name: String="Prüfungen"
    private lateinit var viewModel: ExamOverviewViewModel
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
        setPruefungszeitraum()
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
        recyclerViewExamAdapter = RecyclerViewExamAdapter(mutableListOf(),viewModel)
        recyclerView4.adapter = recyclerViewExamAdapter
        recyclerView4.visibility = RecyclerView.VISIBLE
        viewModel.liveEntryList.observe(viewLifecycleOwner){ entryList ->
            entryList?.let {
                recyclerViewExamAdapter.updateContent(Filter.validateList(it).toMutableList())
            }
            termineFragment_swiperefres.isRefreshing = false
        }
        termineFragment_swiperefres.setDistanceToTriggerSync(800)
        termineFragment_swiperefres.setOnRefreshListener {
            viewModel.updatePruefperiode()
            viewModel.updateDataFromServer()
            termineFragment_swiperefres.isRefreshing = false
        }
        recyclerView4?.layoutManager = LinearLayoutManager(view?.context)
    }

    /**
     * Sets the text for the current period with content from shared preferences
     *
     * @author Alexander Lange
     * @since 1.6
     *
     */
    fun setPruefungszeitraum() {
        viewModel.getPeriodeTimeSpan()?.let { currentPeriode?.text = it }
    }
//
//    /**
//     * Enables the functionality to swipe an entity from the recyclerview to favor or delete it
//     *
//     * @author Alexander Lange
//     * @since 1.6
//     *
//     */
//    private fun enableSwipeToDelete() {
//        // try and catch, da es bei einer
//        // Orientierungsänderung sonst zu
//        // einer NullPointerException kommt
//        try {
//            // Definiert den Listener
//            val swipeToDeleteCallback: swipeListener =
//                object : swipeListener(requireContext(), false) {
//                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {
//                        val position = viewHolder.adapterPosition
//                        var isFavorite: Boolean? = null
//                        isFavorite = recyclerViewExamAdapter.checkFavorite(viewHolder.adapterPosition)
//                        if (isFavorite == true) {
//                            recyclerViewExamAdapter.deleteFromFavorites(
//                                    position,
//                                    (viewHolder as RecyclerViewExamAdapter.ViewHolder)
//                            )
//                        } else {
//                            recyclerViewExamAdapter.addToFavorites(
//                                    position,
//                                    (viewHolder as RecyclerViewExamAdapter.ViewHolder)
//                            )
//                        }
//                        recyclerViewExamAdapter.notifyDataSetChanged()
//                    }
//                }
//
//            // Setzt den Listener
//            val itemTouchhelper = ItemTouchHelper(swipeToDeleteCallback)
//            itemTouchhelper.attachToRecyclerView(recyclerView4)
//        } catch (e: Exception) {
//            Log.d("Error", "Orientation error${e.stackTraceToString()}")
//        }
//    }
}