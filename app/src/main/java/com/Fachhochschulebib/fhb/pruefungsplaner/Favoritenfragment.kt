package com.Fachhochschulebib.fhb.pruefungsplaner

import androidx.recyclerview.widget.RecyclerView
import com.Fachhochschulebib.fhb.pruefungsplaner.data.AppDatabase
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import android.widget.TextView
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView.OnChildAttachStateChangeListener
import androidx.recyclerview.widget.ItemTouchHelper
import java.lang.Exception
import java.util.ArrayList

import kotlinx.android.synthetic.main.terminefragment.*
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
class Favoritenfragment : Fragment() {
    private var mAdapter: RecyclerViewFavoritAdapter? = null
    private var check: MutableList<Boolean> = ArrayList()

    //TODO Alexander Lange Start
    private var filterChangeListenerPosition: Int? = null

    //TODO Alexander Lange End
    private lateinit var viewModel: MainViewModel

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
            MainViewModelFactory(requireActivity().application)
        )[MainViewModel::class.java]
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
        //From onCreate

        //From onCreateView
        //createAdapter()
        //Komponenten  initialisieren für die Verwendung
        initRecyclerview()
        currentPeriode.visibility = View.GONE
        // Merlin Gürtler
        // Aktiviert den swipe listener
        enableSwipeToDelete()

        // Ende Merlin Gürtler
        //TODO Alexander Lange Start
        //Filter.onFilterChangedListener.add { OnFilterChanged() }
        //filterChangeListenerPosition = Filter.onFilterChangedListener.size - 1
        //TODO Alexander Lange End
    }

    /**
     * Initializes the recyclerview, that shows the selected courses.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private fun initRecyclerview() {
        recyclerView4?.setHasFixedSize(true)
        //linear layout manager
        val layoutManager = LinearLayoutManager(view?.context)
        recyclerView4?.layoutManager = layoutManager
        viewModel.liveFilteredFavorits.observe(viewLifecycleOwner){
            recyclerView4.adapter = it?.let { it1 ->
                Filter.validateList(it1).toMutableList()
            }?.let { it2 -> RecyclerViewFavoritAdapter(it2,viewModel) }
        }
    }


    /**
     * Called, when the [MainActivity.Filter] of the [MainActivity]-Class changes.
     * It recreates the recyclerview, so the user can see the new filtered items.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private fun OnFilterChanged() {
        createAdapter()
    }

    /**
     * Creates the adapter for the recyclerview. Loads the chosen exams from the Room-Database
     * and passes them into an adapter for the recyclerview.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun createAdapter() {
        val courses: MutableList<String> = ArrayList()
        val profnames: MutableList<String> = ArrayList()
        val dates: MutableList<String> = ArrayList()
        val examNo: MutableList<String> = ArrayList()
        val room: MutableList<String> = ArrayList()
        val form: MutableList<String> = ArrayList()
        val ppeList = viewModel.getFavorites(true)

        // Abfrage ob Prüfungen favorisiert wurden
        // Favorisierte Prüfungen für die Anzeige vorbereiten
        if (ppeList != null) {
            for (entry in ppeList) {
                if (!Filter.validateFilter(entry)) {
                    continue
                }
                courses.add(
                    entry.module + " "
                            + entry?.course
                )
                profnames.add(
                    entry.firstExaminer + " "
                            + entry.secondExaminer + " "
                            + entry.semester
                )
                dates.add(entry.date ?: "")
                examNo.add(entry.id ?: "")
                room.add(entry.room ?: "")
                form.add(entry.examForm ?: "")
                check.add(true)
            }
        }
        // definiere adapter
        // übergabe der variablen an den Recyclerview Adapter, für die darstellung
    }

    //TODO Alexander Lange Start
    /**
     * Removes the listener, when this fragment is not longer visible.
     *
     * @author Alexander Lange
     * @since 1.6
     * @see Fragment.onDestroy
     * @see MainActivity.Filter
     */
    override fun onDestroy() {
        super.onDestroy()
        if (filterChangeListenerPosition != null) {
           // Filter.onFilterChangedListener.removeAt(filterChangeListenerPosition!!)
        }
    }
    //TODO Alexander Lange End

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
     * Creates the swipe-gesture on the recyclerview,
     * so the user can swipe to delete an entry from the list.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    // Start Merlin Gürtler
    private fun enableSwipeToDelete() {
        // try and catch, da es bei einer
        // Orientierungsänderung sonst zu
        // einer NullPointerException kommt
        try {
            // Definiert den Listener
            val swipeToDeleteCallback: swipeListener = object : swipeListener(context!!, true) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {
                    val position = viewHolder.adapterPosition
                    mAdapter?.remove(position)
                }
            }

            // Setzt den Listener
            val itemTouchhelper = ItemTouchHelper(swipeToDeleteCallback)
            itemTouchhelper.attachToRecyclerView(recyclerView4)
        } catch (e: Exception) {
            Log.d("Error", "Orientation error$e")
        }
    } // Ende Merlin Gürtler
}