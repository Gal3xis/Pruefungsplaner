package com.Fachhochschulebib.fhb.pruefungsplaner

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnChildAttachStateChangeListener
import kotlinx.android.synthetic.main.termine.*
import kotlinx.android.synthetic.main.terminefragment.*

/**
 * Class to maintain the view for all exams. Requests information about exams and fills the recyclerview with them.
 *
 * @since 1.6
 * @author Alexander Lange (E-Mail:alexander.lange@fh-bielefeld.de)
 */
class Terminefragment : Fragment() {
    private lateinit var viewModel: TermineViewModel
    private lateinit var recyclerViewExamAdapter: RecyclerViewExamAdapter

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
        setHasOptionsMenu(true)
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
        inflater: LayoutInflater,
        container: ViewGroup?,
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
        viewModel = ViewModelProvider(
                requireActivity(),
                MainViewModelFactory(requireActivity().application)
        )[TermineViewModel::class.java]
        viewModel.getCalendarPermission(requireActivity())
        setPruefungszeitraum()
        initRecyclerview()
    }


    /**
     * Initializes the Recyclerview which shows the information about pending exams.
     *
     * @since 1.6
     * @author Alexander Lange (E-Mail:alexander.lange@fh-bielefeld.de)
     */
    private fun initRecyclerview() {
        recyclerViewExamAdapter = RecyclerViewExamAdapter(mutableListOf(),viewModel)
        recyclerView4.adapter = recyclerViewExamAdapter
        recyclerView4.visibility = RecyclerView.VISIBLE
        viewModel.liveEntryList.observe(viewLifecycleOwner){ entryList ->
            entryList?.let { recyclerViewExamAdapter.updateContent(Filter.validateList(it).toMutableList()) }
            termineFragment_swiperefres.isRefreshing = false
        }
        termineFragment_swiperefres.setDistanceToTriggerSync(800)
        termineFragment_swiperefres.setOnRefreshListener {
            viewModel.updatePruefperiode()
        }
        recyclerView4?.layoutManager = LinearLayoutManager(view?.context)

        recyclerView4?.addOnChildAttachStateChangeListener(object :
            OnChildAttachStateChangeListener {
            override fun onChildViewAttachedToWindow(view: View) {}
            override fun onChildViewDetachedFromWindow(view: View) {
                if (txtSecondscreen?.visibility == View.VISIBLE) {
                    txtSecondscreen?.visibility = View.GONE
                }
            }
        })

        enableSwipeToDelete()
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
     * Enables the functionality to swipe an entity from the recyclerview to favor or delete it
     *
     * @since 1.6
     * @author Alexander Lange (E-Mail:alexander.lange@fh-bielefeld.de)
     */
    private fun enableSwipeToDelete() {
        // try and catch, da es bei einer
        // Orientierungsänderung sonst zu
        // einer NullPointerException kommt
        try {
            // Definiert den Listener
            val swipeToDeleteCallback: swipeListener =
                object : swipeListener(requireContext(), false) {
                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {
                        val position = viewHolder.adapterPosition
                        var isFavorite: Boolean? = null
                        isFavorite = recyclerViewExamAdapter.checkFavorite(viewHolder.adapterPosition)
                        if (isFavorite == true) {
                            recyclerViewExamAdapter.deleteFromFavorites(
                                    position,
                                    (viewHolder as RecyclerViewExamAdapter.ViewHolder)
                            )
                        } else {
                            recyclerViewExamAdapter.addToFavorites(
                                    position,
                                    (viewHolder as RecyclerViewExamAdapter.ViewHolder)
                            )
                        }
                        recyclerViewExamAdapter.notifyDataSetChanged()
                    }
                }

            // Setzt den Listener
            val itemTouchhelper = ItemTouchHelper(swipeToDeleteCallback)
            itemTouchhelper.attachToRecyclerView(recyclerView4)
        } catch (e: Exception) {
            Log.d("Error", "Orientation error${e.stackTraceToString()}")
        }
    }
}