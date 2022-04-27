package com.fachhochschulebib.fhb.pruefungsplaner.view.fragments

import androidx.recyclerview.widget.RecyclerView
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import com.fachhochschulebib.fhb.pruefungsplaner.*
import com.fachhochschulebib.fhb.pruefungsplaner.utils.Filter
import com.fachhochschulebib.fhb.pruefungsplaner.view.helper.MainActivityFragment
import com.fachhochschulebib.fhb.pruefungsplaner.view.helper.RecyclerViewFavoritAdapter
import com.fachhochschulebib.fhb.pruefungsplaner.view.helper.swipeListener
import com.fachhochschulebib.fhb.pruefungsplaner.viewmodel.FavoriteOverviewViewModel
import com.fachhochschulebib.fhb.pruefungsplaner.viewmodel.ViewModelFactory
import java.lang.Exception

import kotlinx.android.synthetic.main.fragment_exam_overview.*

/**
 * Fragment that displays the favorites of the user. He can display details and delete them from favorites.
 *
 * @author Alexander Lange (Email:alexander.lange@fh-bielefeld.de)
 * @since 1.6
 *
 */
class FavoriteOverviewFragment : MainActivityFragment() {
    override var name: String="Favoriten"
    private lateinit var recyclerViewFavoritAdapter: RecyclerViewFavoritAdapter
    private lateinit var viewModel: FavoriteOverviewViewModel

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
     * Sets the text for the current period with content from shared preferences
     *
     * @author Alexander Lange
     * @since 1.6
     *
     */
    fun setPruefungszeitraum() {
        viewModel.getPeriodeTimeSpan()?.let { currentPeriode?.text = it }
    }

    /**
     * Overrides the onCreateView()-Method. It sets the current view to the terminefragment-layout.
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
            viewModel.liveFavorits.value?.let { Filter.validateList(it) }?.let { recyclerViewFavoritAdapter.updateContent(it) }
        }
        setPruefungszeitraum()
    }

    /**
     * Initializes the recyclerview, that shows the selected courses.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private fun initRecyclerview() {
        recyclerViewFavoritAdapter = RecyclerViewFavoritAdapter(mutableListOf(),viewModel)
        recyclerView4.adapter = recyclerViewFavoritAdapter
        recyclerView4?.layoutManager =  LinearLayoutManager(view?.context)
        viewModel.liveFavorits.observe(viewLifecycleOwner) { entryList ->
            if (entryList != null) {
                recyclerViewFavoritAdapter.updateContent(Filter.validateList(entryList))
            }
        }
    }

    /**
     * Creates the swipe-gesture on the recyclerview,
     * so the user can swipe to delete an entry from the list.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private fun enableSwipeToDelete() {
        try {
            val swipeToDeleteCallback: swipeListener = object : swipeListener(requireContext(), true) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {
                    val position = viewHolder.adapterPosition
                    recyclerViewFavoritAdapter?.remove(position)
                }
            }
            val itemTouchhelper = ItemTouchHelper(swipeToDeleteCallback)
            itemTouchhelper.attachToRecyclerView(recyclerView4)
        } catch (e: Exception) {
            Log.d("Error", "Orientation error$e")
        }
    }
}