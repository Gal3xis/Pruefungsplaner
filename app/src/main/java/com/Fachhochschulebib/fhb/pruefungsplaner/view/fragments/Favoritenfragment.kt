package com.Fachhochschulebib.fhb.pruefungsplaner.view.fragments

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
import com.Fachhochschulebib.fhb.pruefungsplaner.*
import com.Fachhochschulebib.fhb.pruefungsplaner.utils.Filter
import com.Fachhochschulebib.fhb.pruefungsplaner.view.helper.MainActivityFragment
import com.Fachhochschulebib.fhb.pruefungsplaner.view.helper.RecyclerViewFavoritAdapter
import com.Fachhochschulebib.fhb.pruefungsplaner.view.helper.swipeListener
import com.Fachhochschulebib.fhb.pruefungsplaner.viewmodel.FavoritenViewModel
import com.Fachhochschulebib.fhb.pruefungsplaner.viewmodel.ViewModelFactory
import java.lang.Exception

import kotlinx.android.synthetic.main.terminefragment.*

//////////////////////////////
// favoritenfragment
//
//
// autor:
// inhalt:  stelllt die favorisierten prüfungen bereit.
// zugriffsdatum: 11.12.19
//
//
//
//////////////////////////////
/**
 * Class for the Fragment, where the user can see his selected exams.
 *
 * @author Alexander Lange (Email:alexander.lange@fh-bielefeld.de)
 * @since 1.6
 * @see Fragment
 */
class Favoritenfragment : MainActivityFragment() {

    override var name: String="Favoriten"
    private lateinit var recyclerViewFavoritAdapter: RecyclerViewFavoritAdapter
    private lateinit var viewModel: FavoritenViewModel
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
        viewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory(requireActivity().application)
        )[FavoritenViewModel::class.java]
    }

    /**
     * Sets the text for the current period with content from shared preferences
     *
     * @since 1.6
     * @author Alexander Lange (E-Mail:alexander.lange@fh-bielefeld.de)
     */
    fun setPruefungszeitraum() {
        viewModel.getPruefungszeitraum()?.let { currentPeriode?.text = it }
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
        val v = inflater.inflate(R.layout.terminefragment, container, false)
        return v
    }

    /**
     * Overrides the onViewCreated()-Method, which is called in the Fragment LifeCycle right after the onCreateView()-Method.
     *
     * @since 1.6
     * @author Alexander Lange (E-Mail:alexander.lange@fh-bielefeld.de)
     * @see Fragment.onViewCreated
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerview()

        Filter.onFilterChangedListener.add {
            viewModel.liveFavorits.value?.let { Filter.validateList(it) }?.let { recyclerViewFavoritAdapter.updateContent(it) }
        }
        enableSwipeToDelete()
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