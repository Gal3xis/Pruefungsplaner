package com.fachhochschulebib.fhb.pruefungsplaner.view.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.fachhochschulebib.fhb.pruefungsplaner.*
import com.fachhochschulebib.fhb.pruefungsplaner.utils.Filter
import com.fachhochschulebib.fhb.pruefungsplaner.view.helper.MainActivityFragment
import com.fachhochschulebib.fhb.pruefungsplaner.view.helper.RecyclerViewFavoriteAdapter
import com.fachhochschulebib.fhb.pruefungsplaner.viewmodel.FavoriteOverviewViewModel
import com.fachhochschulebib.fhb.pruefungsplaner.viewmodel.ViewModelFactory

import kotlinx.android.synthetic.main.fragment_exam_overview.*

/**
 * Fragment that displays the favorites of the user. He can display details and delete them from favorites.
 *
 * @author Alexander Lange (Email:alexander.lange@fh-bielefeld.de)
 * @since 1.6
 *
 */
class FavoriteOverviewFragment : MainActivityFragment() {

    /**
     * ViewModel for the FavoriteOverviewFragment. Is set in [onViewCreated].
     * @see FavoriteOverviewViewModel
     */
    private lateinit var viewModel: FavoriteOverviewViewModel


    /**
     * The adapter of the recyclerview that displays all favorite exams to the user.
     * Contains a list of all exams for the selected courses with the current Filter applied.
     * @see RecyclerViewFavoriteAdapter
     */
    private lateinit var recyclerViewFavoriteAdapter: RecyclerViewFavoriteAdapter

    /**
     * Overrides the onCreate()-Method, which is called first in the Fragment-LifeCycle.
     * In this Method, the global parameter which are independent of the UI get initialized,
     * like the App-SharedPreferences and the reference to the Room-Database
     *
     * @author Alexander Lange
     * @since 1.6
     *
     * @see Fragment.onCreate
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory(requireActivity().application)
        )[FavoriteOverviewViewModel::class.java]
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
        return context.getString(R.string.fragment_favorites_overview_name)

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
        inflater: LayoutInflater, container: ViewGroup?,
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
        initRecyclerview()

        Filter.onFilterChangedListener.add {
            viewModel.liveFavorites.value?.let { Filter.validateList(it) }?.let { recyclerViewFavoriteAdapter.updateContent(it) }
        }
    }

    /**
     * Called when this fragment is resumed after being paused. Invalidates the adapter, so external changes will be made visible like calendar entry.
     */
    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        recyclerViewFavoriteAdapter.notifyDataSetChanged()
    }

    /**
     * Initializes the recyclerview, that shows the selected courses.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private fun initRecyclerview() {
        recyclerViewFavoriteAdapter = RecyclerViewFavoriteAdapter(requireContext(),mutableListOf(),viewModel)
        fragment_exam_overview_recyclerview_exam.adapter = recyclerViewFavoriteAdapter
        fragment_exam_overview_recyclerview_exam?.layoutManager =  LinearLayoutManager(view?.context)
        viewModel.liveFavorites.observe(viewLifecycleOwner) { entryList ->
            if (entryList != null) {
                recyclerViewFavoriteAdapter.updateContent(Filter.validateList(entryList))
            }
        }
        fragment_exam_overview_swipe_refresh_layout.setDistanceToTriggerSync(800)
        fragment_exam_overview_swipe_refresh_layout.setOnRefreshListener {
            viewModel.updatePeriod(requireContext())
            viewModel.updateDatabase()
            fragment_exam_overview_swipe_refresh_layout.isRefreshing = false
        }
    }
}