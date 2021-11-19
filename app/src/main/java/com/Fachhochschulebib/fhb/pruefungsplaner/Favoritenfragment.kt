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
import androidx.recyclerview.widget.RecyclerView.OnChildAttachStateChangeListener
import com.Fachhochschulebib.fhb.pruefungsplaner.swipeListener
import androidx.recyclerview.widget.ItemTouchHelper
import kotlinx.android.synthetic.main.favoriten.*
import java.lang.Exception
import java.util.ArrayList

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
class Favoritenfragment : Fragment() {
    private var recyclerView: RecyclerView? = null
    var mAdapter: MyAdapterfavorits? = null
    var check: MutableList<Boolean> = ArrayList()

    // Datenbank initialisierung
    var roomdaten: AppDatabase? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //From onCreate

        //From onCreateView
        roomdaten = AppDatabase.getAppDatabase(context!!)
        //Komponenten  initialisieren für die Verwendung
        //TODO REMOVE recyclerView = v.findViewById<View>(R.id.recyclerView4) as RecyclerView
        recyclerView4?.setHasFixedSize(true)
        //linear layout manager
        val layoutManager = LinearLayoutManager(view.context)
        recyclerView4?.layoutManager = layoutManager
        //TODO REMOVE val calendar = v.findViewById<View>(R.id.caCalender) as CalendarView
        //TODO REMOVE val btnSearch = v.findViewById<View>(R.id.btnDatum) as Button
        //TODO REMOVE val currentPeriodeTextView = v.findViewById<View>(R.id.currentPeriode) as TextView
        currentPeriode.visibility = View.INVISIBLE
        //caCalender.visibility = View.GONE
        val courses: MutableList<String> = ArrayList()
        val profnames: MutableList<String> = ArrayList()
        val dates: MutableList<String> = ArrayList()
        val examNo: MutableList<String> = ArrayList()
        val room: MutableList<String> = ArrayList()
        val form: MutableList<String> = ArrayList()
        btnDatum.visibility = View.INVISIBLE

        // Merlin Gürtler
        // Aktiviert den swipe listener
        enableSwipeToDelete()
        //TODO CHANGE TO COROUTINE
        Thread {
            val ppeList = roomdaten?.userDao()?.getFavorites(true)

            // Abfrage ob Prüfungen favorisiert wurden
            // Favorisierte Prüfungen für die Anzeige vorbereiten
            if (ppeList != null) {
                for (entry in ppeList) {
                    courses.add(
                        entry?.module + " "
                                + entry?.course
                    )
                    profnames.add(
                        entry?.firstExaminer + " "
                                + entry?.secondExaminer + " "
                                + entry?.semester
                    )
                    dates.add(entry?.date ?: "")
                    examNo.add(entry?.id ?: "")
                    room.add(entry?.room ?: "")
                    form.add(entry?.examForm ?: "")
                    check.add(true)
                }
            }

            // definiere adapter
            // übergabe der variablen an den Recyclerview Adapter, für die darstellung
            mAdapter = MyAdapterfavorits(courses, profnames, dates, examNo, room, form)
            Handler(Looper.getMainLooper()).post { recyclerView?.adapter = mAdapter }
        }.start()
        //TODO CHECK IF CORRECT
        recyclerView?.addOnItemTouchListener(
            RecyclerItemClickListener(activity,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View?, position: Int) {
                        //TODO REMOVE val txtSecondScreen = view.findViewById<View>(R.id.txtSecondscreen) as TextView
                        val viewItem =
                            recyclerView?.layoutManager?.findViewByPosition(position as Int)
                        val layoutinformationen =
                            viewItem?.findViewById<View>(R.id.linearLayout) as LinearLayout


                        //überprüfung ob das linear layout geklickt wurde
                        layoutinformationen.setOnClickListener {
                            if (txtSecondscreen.visibility == View.VISIBLE) {
                                txtSecondscreen.visibility = View.GONE
                                check[position as Int] = false
                            } else {

                                // Start Merlin Gürtler
                                for (i in 0 until recyclerView!!.childCount) {
                                    val holder = recyclerView!!.layoutManager!!
                                        .findViewByPosition(i)
                                    // Try and Catch, da die App crasht
                                    // wenn das Element nicht im View Port ist
                                    try {
                                        val txtSecondScreen2 =
                                            holder?.findViewById<View>(R.id.txtSecondscreen) as TextView
                                        if (txtSecondScreen2.visibility == View.VISIBLE) {
                                            txtSecondScreen2.visibility = View.GONE
                                        }
                                    } catch (e: Exception) {
                                        Log.d("ERROR", "NOT IN VIEW PORT $e")
                                    }
                                }
                                // Ende Merlin Gürtler
                                txtSecondscreen.visibility = View.VISIBLE
                                txtSecondscreen.text = mAdapter
                                    ?.giveString(position)
                            }
                        }

                        /*
                                                try{
                                                    if(check.get(position)) {
                                                        txtSecondScreen.setVisibility(v.VISIBLE);
                                                        txtSecondScreen.setText(((MyAdapterfavorits) mAdapter)
                                                                       .giveString(position));
                                                    }}
                                                catch(Exception e){

                                                }
                                                 */
                    }
                })
        )

        // Start Merlin Gürtler
        recyclerView?.addOnChildAttachStateChangeListener(
            object :
                OnChildAttachStateChangeListener {
                override fun onChildViewAttachedToWindow(view: View) {}

                // Wenn ein Element den Viewport verlässt, wird
                // der zweite Screen zu geklappt
                override fun onChildViewDetachedFromWindow(view: View) {
                    //TODO REMOVE val txtSecondScreen = view.findViewById<View>(R.id.txtSecondscreen) as TextView
                    if (txtSecondscreen.visibility == View.VISIBLE) { //TODO CHECK RIGHT IMPORT
                        txtSecondscreen.visibility = View.GONE
                    }
                }
            })

        // Ende Merlin Gürtler
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.terminefragment, container, false)


        return v
    }

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
            itemTouchhelper.attachToRecyclerView(recyclerView)
        } catch (e: Exception) {
            Log.d("Error", "Orientation error$e")
        }
    } // Ende Merlin Gürtler
}