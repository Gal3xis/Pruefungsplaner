package com.Fachhochschulebib.fhb.pruefungsplaner

import android.content.Context
import android.content.SharedPreferences
import androidx.recyclerview.widget.RecyclerView
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.Fachhochschulebib.fhb.pruefungsplaner.data.AppDatabase
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView.OnChildAttachStateChangeListener
import androidx.recyclerview.widget.ItemTouchHelper
import kotlinx.android.synthetic.main.termine.*
import kotlinx.android.synthetic.main.terminefragment.*
import java.lang.Exception
import java.util.ArrayList

//////////////////////////////
// TerminefragmentSuche
//
//
//
// autor:
// inhalt:  Prüfungen aus der Klasse Prüfplaneintrag werden abgefragt und zur darstelllung an den Recycleview adapter übergeben
// zugriffsdatum: 20.2.20
//
//
//
//
//
//
//////////////////////////////
class TermineFragmentSearch : Fragment() {
    var mSharedPreferencesValidation: SharedPreferences? = null
    private var date: String? = null
    var examineYear: String? = null
    var currentExaminePeriod: String? = null
    var returnCourse: String? = null
    var validation: String? = null
    var checkList: MutableList<Boolean> = ArrayList()
    var moduleAndCourseList: MutableList<String> = ArrayList()
    var examinerAndSemester: MutableList<String> = ArrayList()
    var dateList: MutableList<String> = ArrayList()
    var moduleList: MutableList<String> = ArrayList()
    var idList: MutableList<String> = ArrayList()
    var formList: MutableList<String> = ArrayList()
    var roomList: MutableList<String> = ArrayList()
    var statusList: MutableList<String> = ArrayList()
    private var month2: String? = null
    private var day2: String? = null

    // private int position2 = 0;
    private var year2: String? = null
    private var mLayout: RecyclerView.LayoutManager? = null
    var mAdapter: MyAdapter? = null
    var valuesToShowList: MutableList<Int> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        // Start Merlin Gürtler
        // Nun aus Shared Preferences
        mSharedPreferencesValidation = this@TermineFragmentSearch.context
            ?.getSharedPreferences("validation", 0)
        examineYear = mSharedPreferencesValidation?.getString("examineYear", "0")
        currentExaminePeriod = mSharedPreferencesValidation?.getString("currentPeriode", "0")
        returnCourse = mSharedPreferencesValidation?.getString("returnCourse", "0")
        validation = examineYear + returnCourse + currentExaminePeriod
        // Ende Merlin Gürtler
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.terminefragment, container, false)


        //hinzufügen von recycleview
        //TODO REMOVE recyclerView = v.findViewById<View>(R.id.recyclerView4) as RecyclerView
        //TODO REMOVE val currentPeriodeTextView = v.findViewById<View>(R.id.currentPeriode) as TextView
        val mSharedPreferencesPPeriode =
            context?.getSharedPreferences("currentPeriode", Context.MODE_PRIVATE)
        val strJson = mSharedPreferencesPPeriode?.getString("currentPeriode", "0")
        currentPeriode.text = strJson
        recyclerView4.visibility = View.VISIBLE
        // use this setting to
        // improve performance if you know that changes
        // in content do not change the layout size
        // of the RecyclerView
        recyclerView4.setHasFixedSize(true)

        // use a linear layout manager
        val layoutManager = LinearLayoutManager(v.context)
        recyclerView4.layoutManager = layoutManager
        mLayout = recyclerView4.layoutManager


        //Userinterface Komponenten Initialiseren
        // recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView4);
        // recyclerView.setVisibility(View.VISIBLE);
        //TODO REMOVE calendar = v.findViewById<View>(R.id.caCalender) as CalendarView
        val btnSearch = v.findViewById<View>(R.id.btnDatum) as Button
        adapterPassed()
        caCalender.visibility = View.GONE
        //btnsuche clicklistener überprüft, ob der "Kalender öffnen" - Button angetippt wurde
        /*  Es werden bei eingeschaltetem Kalender nur Prüfobjekte mit übereinstimmenden
            Datum angezeigt.
         */btnSearch.setOnClickListener(object : View.OnClickListener {
            var save = true
            override fun onClick(v: View) {
                if (!save) {
                    caCalender.visibility = View.GONE
                    adapterPassed()
                    save = true
                } else {
                    //Kalender ist geöffnet, nur übereinstimmende Prüfungen anzeigen
                    caCalender.visibility = View.VISIBLE
                    caCalender.setOnDateChangeListener { view, year, month, dayOfMonth ->
                        Thread {
                            val roomData = AppDatabase.getAppDatabase(context!!)
                            val ppeList = roomData?.userDao()?.getAllChoosen(true)
                            ClearLists()

                            //Creating editor to store uebergebeneModule to shared preferences
                            month2 = if (month < 9) {
                                "0" + (month + 1).toString()
                            } else {
                                (month + 1).toString()
                            }
                            day2 = if (dayOfMonth < 10) {
                                "0$dayOfMonth"
                            } else {
                                dayOfMonth.toString()
                            }
                            year2 = year.toString()
                            date = "$year2-$month2-$day2"
                            if (ppeList != null) {
                                for (entry in ppeList) {
                                    val date2 = entry?.date?.split(" ")?.toTypedArray()
                                    if (date2?.get(0) ?: 0 == date) {
                                        moduleAndCourseList.add(
                                            """${entry?.module}
                         ${entry?.course}"""
                                        )
                                        examinerAndSemester.add(
                                            entry?.firstExaminer
                                                    + " " + entry?.secondExaminer
                                                    + " " + entry?.semester + " "
                                        )
                                        dateList.add(entry?.date?:"")
                                        moduleList.add(entry?.module?:"")
                                        idList.add(entry?.id?:"")
                                        formList.add(entry?.examForm?:"")
                                        roomList.add(entry?.room?:"")
                                        statusList.add(entry?.hint?:"")
                                        checkList.add(true)
                                    }
                                }
                            }
                            // define an adapter
                            //Werte an den Adapter übergeben
                            mAdapter = MyAdapter(
                                moduleAndCourseList,
                                examinerAndSemester,
                                dateList,
                                moduleList,
                                idList,
                                formList,
                                mLayout,
                                roomList,
                                statusList
                            )

                            //Anzeigen von recyclerview
                            Handler(Looper.getMainLooper()).post {
                                recyclerView4?.adapter = mAdapter
                            }
                        }.start()
                    }
                    save = false
                }
            }
        })
        recyclerView4?.addOnItemTouchListener(
            RecyclerItemClickListener(
                activity,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View?, position: Int) {
                        val txtSecondScreen =
                            view!!.findViewById<View>(R.id.txtSecondscreen) as TextView
                        val viewItem = recyclerView4?.layoutManager?.findViewByPosition(position)
                        val layout1 =
                            viewItem?.findViewById<View>(R.id.linearLayout) as LinearLayout
                        layout1.setOnClickListener {
                            Log.e("@@@@@", "" + position)
                            if (txtSecondScreen.visibility == View.VISIBLE) {
                                txtSecondScreen.visibility = View.GONE
                                checkList[position] = false
                            } else {
                                // Start Merlin Gürtler
                                for (i in 0 until recyclerView4?.childCount!!) {
                                    val holder = recyclerView4?.layoutManager
                                        ?.findViewByPosition(i)
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
                                txtSecondScreen.visibility = View.VISIBLE
                                txtSecondScreen.text = mAdapter?.giveString(position)
                            }
                        }
                    }
                })
        )

        // Start Merlin Gürtler
        recyclerView4?.addOnChildAttachStateChangeListener(object :
            OnChildAttachStateChangeListener {
            override fun onChildViewAttachedToWindow(view: View) {}

            // Wenn ein Element den Viewport verlässt, wird
            // der zweite Screen zu geklappt
            override fun onChildViewDetachedFromWindow(view: View) {
                //TODO REMVOE val txtSecondScreen = view.findViewById<View>(R.id.txtSecondscreen) as TextView
                if (txtSecondscreen?.visibility == View.VISIBLE) {
                    txtSecondscreen?.visibility = View.GONE
                }
            }
        })
        // Ende Merlin Gürtler
        return v
    }

    fun adapterPassed() {
        //TODO CHANGE TO COROUTINE
        Thread { //Datenbank initialisieren
            val database = AppDatabase.getAppDatabase(context!!)

            // Änderung Merlin Gürtler
            // List<Pruefplan> pruefplandaten = datenbank.userDao().getEntriesByValidation(validation);
            // Für die Suche von Modulen
            val ppeList = database?.userDao()?.getAllChoosen(true)
            // Ende Änderung Merlin Gürtler
            ClearLists()
            for (i in ppeList?.indices!!) {
                valuesToShowList.add(i)
            }

            //Variablen mit Werten aus der lokalen Datenbank füllen
            for (i in valuesToShowList.indices) {
                moduleAndCourseList.add(
                    """${ppeList?.get(valuesToShowList[i])?.module}
 """ + ppeList?.get(
     Integer.valueOf(
         valuesToShowList[i]
     )
                    )?.course
                )
                examinerAndSemester.add(
                    ppeList?.get(valuesToShowList[i])?.firstExaminer + " " + ppeList?.get(
                        Integer.valueOf(
                            valuesToShowList[i]
                        )
                    )?.secondExaminer + " " + ppeList?.get(Integer.valueOf(valuesToShowList[i]))?.semester + " "
                )
                dateList.add(ppeList?.get(valuesToShowList[i])?.date?:"")
                moduleList.add(ppeList?.get(valuesToShowList[i])?.module?:"")
                idList.add(ppeList?.get(valuesToShowList[i])?.id?:"")
                formList.add(ppeList?.get(valuesToShowList[i])?.examForm ?:"")
                roomList.add(ppeList?.get(valuesToShowList[i])?.room?:"")
                statusList.add(ppeList?.get(valuesToShowList[i])?.hint?:"")
                checkList.add(true)
            }

            // define an adapter
            mAdapter = MyAdapter(
                moduleAndCourseList,
                examinerAndSemester,
                dateList,
                moduleList,
                idList,
                formList,
                mLayout,
                roomList,
                statusList
            )
            Handler(Looper.getMainLooper()).post {
                recyclerView4?.adapter = mAdapter
                enableSwipeToDelete()
            }
        }.start()
    }

    fun ClearLists() {
        moduleAndCourseList.clear()
        examinerAndSemester.clear()
        dateList.clear()
        moduleList.clear()
        idList.clear()
        formList.clear()
        roomList.clear()
        statusList.clear()
    }

    // Start Merlin Gürtler
    private fun enableSwipeToDelete() {
        // try and catch, da es bei einer
        // Orientierungsänderung sonst zu
        // einer NullPointerException kommt
        try {
            // Definiert den Listener
            val swipeToDeleteCallback: swipeListener = object : swipeListener(context!!, false) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {
                    val position = viewHolder.adapterPosition
                    //TODO CHANGE TO COROUTINE
                    Thread {
                        val isFavorite = mAdapter?.checkFavorite(viewHolder.adapterPosition)
                        Handler(Looper.getMainLooper()).post {
                            if (isFavorite == true) {
                                mAdapter?.deleteFromFavorites(
                                    position,
                                    (viewHolder as MyAdapter.ViewHolder)
                                )
                            } else {
                                mAdapter?.addToFavorites(
                                    position,
                                    (viewHolder as MyAdapter.ViewHolder)
                                )
                            }
                            mAdapter?.notifyDataSetChanged()
                        }
                    }.start()
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