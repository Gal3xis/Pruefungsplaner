package com.Fachhochschulebib.fhb.pruefungsplaner

import android.Manifest
import android.app.ProgressDialog
import androidx.recyclerview.widget.RecyclerView
import android.os.Looper
import android.os.Bundle
import com.Fachhochschulebib.fhb.pruefungsplaner.model.RetrofitConnect
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.OnChildAttachStateChangeListener
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import android.os.Handler
import android.util.Log
import android.view.*
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.termine.*
import kotlinx.android.synthetic.main.terminefragment.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.*

//////////////////////////////
// Terminefragment
//
//
//
// autor:
// inhalt:  Prüfungen aus der Klasse Prüfplaneintrag werden abgefragt und
// zur Darstelllung an den Recycleview-Adapter übergeben
// zugriffsdatum: 20.2.20
//
//
//////////////////////////////
/**
 * Class to maintain the view for all exams. Requests information about exams and fills the recyclerview with them.
 *
 * @since 1.6
 * @author Alexander Lange (E-Mail:alexander.lange@fh-bielefeld.de)
 */
class Terminefragment : Fragment() {
    //region parameter
    //UI
    private var progressBar: ProgressDialog? = null
    private var mLayout: RecyclerView.LayoutManager? = null
    private var checkList: MutableList<Boolean> = ArrayList()

    private var mAdapter: RecyclerViewExamAdapter? = null

    //ViewModel
    private lateinit var viewModel: MainViewModel

    //TODO Alexander Lange Start
    var filterChangeListenerPosition: Int? = null
    //TODO Alexander Lange End
    //endregion

    companion object {
        var validation: String? = null
    }


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
        )[MainViewModel::class.java]
        viewModel.updatePruefperiode()

        getCalendarPermission()
        //updateDataFromServer()
        enableSwipeToDelete()
        initRecyclerview()
        Filter.onFilterChangedListener.add { OnFilterChanged() }
        filterChangeListenerPosition = Filter.onFilterChangedListener.size - 1
    }



    /**
     * This Method is called when the fragment gets destroyed. Its the last called Method in the Fragment-Lifecycle.
     * It needs to remove the Filter-Callback from the table.kt-class so it will no longer update when the filter is changed.
     *
     * @since 1.6
     * @author Alexander Lange (E-Mail:alexander.lange@fh-bielefeld.de)
     * @see Fragment.onDestroy
     */
    override fun onDestroy() {
        super.onDestroy()
        try {
            if (filterChangeListenerPosition != null && Filter.onFilterChangedListener.size >= filterChangeListenerPosition!!) {
                Filter.onFilterChangedListener.removeAt(filterChangeListenerPosition!!)
            }

        } catch (ex: Exception) {
            Log.e("TermineFragment.onStop", ex.stackTraceToString())
        }
    }

    //TODO Move
    /**
     * This Method checks, if the user already gave permission to access the Calendar,
     * if not, he is ask to do so.
     *
     * @since 1.6
     * @author Alexander Lange (E-Mail:alexander.lange@fh-bielefeld.de)
     */
    fun getCalendarPermission() {
        // Start Merlin Gürtler
        //Zugriffrechte für den GoogleKalender
        //Id für den Google Kalender
        val callbackId = 42
        //Wert1: ID Google Kalender, Wert2: Rechte fürs Lesen, Wert3: Rechte fürs schreiben)
        checkPermission(
            callbackId,
            Manifest.permission.READ_CALENDAR,
            Manifest.permission.WRITE_CALENDAR
        )
    }

    //TODO Move
    /**
     * Checks the Phone for a give permission.
     * If the permission is not granted, the user is asked if he wants to grant permission.
     *
     * @param[callbackId] Id of Permission which called function
     * @param[permissionsId] List of permissions that need to be requested
     *
     * @since 1.6
     * @author Alexander Lange (E-Mail:alexander.lange@fh-bielefeld.de)
     */
    private fun checkPermission(callbackId: Int, vararg permissionsId: String) {
        var permissions = true
        for (p in permissionsId) {
            permissions = (permissions
                    && ContextCompat
                .checkSelfPermission(this.context!!, p) == PackageManager.PERMISSION_GRANTED)
        }
        if (!permissions) ActivityCompat.requestPermissions(
            this@Terminefragment.activity!!,
            permissionsId,
            callbackId
        )
    }

    /**
     * Initializes the Recyclerview which shows the information about pending exams.
     *
     * @since 1.6
     * @author Alexander Lange (E-Mail:alexander.lange@fh-bielefeld.de)
     */
    private fun initRecyclerview() {
        viewModel.Filter()
        viewModel.liveFilteredEntriesByDate.observe(viewLifecycleOwner){
            recyclerView4.adapter = it?.let { RecyclerViewExamAdapter(Filter.validateList(context,it).toMutableList(),viewModel) }
            termineFragment_swiperefres.isRefreshing = false
        }
        recyclerView4?.visibility = View.VISIBLE
        recyclerView4?.setHasFixedSize(true)
        termineFragment_swiperefres.setDistanceToTriggerSync(800)
        termineFragment_swiperefres.setOnRefreshListener {
            viewModel.updatePruefperiode()
        }
        val layoutManager = LinearLayoutManager(view?.context)
        recyclerView4?.layoutManager = layoutManager
        mLayout = recyclerView4?.layoutManager
        recyclerView4?.addOnChildAttachStateChangeListener(object :
            OnChildAttachStateChangeListener {
            override fun onChildViewAttachedToWindow(view: View) {}

            // Wenn ein Element den Viewport verlässt, wird
            // der zweite Screen zu geklappt
            override fun onChildViewDetachedFromWindow(view: View) {
                if (txtSecondscreen?.visibility == View.VISIBLE) {
                    txtSecondscreen?.visibility = View.GONE
                }
            }
        })
        //createView()
    }

    //TODO place into viewmodel
    /**
     * Updates the current local data with the data from the server.
     * Can change the data in the recyclerview and the currentExamPeriod
     * Shows a progressbar while loading the data.
     *
     * @since 1.6
     * @author Alexander Lange (E-Mail:alexander.lange@fh-bielefeld.de)
     */
    fun updateDataFromServer() {
        val globalVariable = this.context?.applicationContext as StartClass
        if (!globalVariable.isShowNoProgressBar || globalVariable.isChangeFaculty) {
            globalVariable.isShowNoProgressBar = true
            globalVariable.isChangeFaculty = false
            progressBar = ProgressDialog(
                this@Terminefragment.context,
                R.style.ProgressStyle
            )

            // Erstelle den Fortschrittsbalken
            progressBar?.setMessage(this@Terminefragment.context!!.getString(R.string.load))
            progressBar?.setProgressStyle(ProgressDialog.STYLE_SPINNER)
            progressBar?.setCancelable(false)
            // Zeige den Fortschrittsbalken
            progressBar?.show()

            //TODO Change after implementing retrofit repository
            runBlocking {
                viewModel.updatePruefperiode()
                updateRoomDatabase()
            }
            createView()
        }
    }

    //TODO place into viewmodel
    /**
     * Updates the Room-Database. Checks if entries have to be removed or have to be loaded from the server.
     *
     * @since 1.6
     * @author Alexander Lange (E-Mail:alexander.lange@fh-bielefeld.de)
     */
    fun updateRoomDatabase() {
        val retrofit = context?.let { RetrofitConnect(it) }

        // IDs der zu aktualisierenden Kurse
        val courseIds = getUnknownCourseIds()
        //Aktualisiere die notwendigen Kurse
        // > 2 da auch bei einem leeren Json Array [] gesetzt werden

        if (courseIds.toString().length > 2) {
            // TODO retrofit?.UpdateUnkownCourses(courseIds.toString())
        }
    }

    //TODO Place into viewmodel
    /**
     * Returns a list of unknown courses that need to be updated
     *
     * @return A JSON-Array of courses that need to be updated.
     * @since 1.6
     * @author Alexander Lange (E-Mail:alexander.lange@fh-bielefeld.de)
     */
    private fun getUnknownCourseIds(): JSONArray {
        var ret = JSONArray()
        val courses = viewModel.getAllCourses()

        if (courses != null) {
            //Durchlaufe alle Kurse
            for (course in courses) {
                try {
                    val courseName = course.courseName ?: ""
                    //Prüfe ob Kurs ausgewählt wurde. Falls nicht, lösche TestplanEntries für diesen Kurs
                    //aus der Room-Database
                    if (course.choosen == false) {
                        // lösche nicht die Einträge der gewählten Studiengänge und Favorit
                        val toDelete = viewModel.getEntriesByCourseName(courseName, false)
                        toDelete?.let { viewModel.deleteEntries(it) }
                    }
                    //Prüfe ob Kurs ausgewählt ist und nur unfavorisierte Einträge enthält. Falls ja, füge
                    //Ihn zu den zu aktualisierenden Kursen hinzu
                    if (viewModel.getOneEntryByName(
                            courseName,
                            false
                        ) == null && course?.choosen == true
                    ) {
                        val idJson = JSONObject()
                        idJson.put("ID", course.sgid)
                        ret.put(idJson)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }
        return ret
    }




// List<PruefplanEintrag> ppeList = datenbank.userDao().getEntriesByValidation(validation);

    //TODO MOVE
    /**
     * Gets the Entries from the Room-Database and adds them to the Recyclerview.
     *
     * @since 1.6
     * @author Alexander Lange (E-Mail:alexander.lange@fh-bielefeld.de)
     */
    private fun createView() {
        val moduleAndCourseList: MutableList<String> = ArrayList()
        val examinerAndSemester: MutableList<String> = ArrayList()
        val dateList: MutableList<String> = ArrayList()
        val moduleList: MutableList<String> = ArrayList()
        val idList: MutableList<String> = ArrayList()
        val formList: MutableList<String> = ArrayList()
        val roomList: MutableList<String> = ArrayList()
        var statusMessage: MutableList<String> = ArrayList()


        validation = viewModel.getExamineYear() + viewModel.getReturnCourse() + viewModel.getCurrentPeriode()
        //val ppeList = database?.userDao()?.getEntriesByValidation(validation)
        val ppeList = viewModel.getAllEntries()
        Handler(Looper.getMainLooper()).post {
            if (ppeList != null) {
                for (entry in ppeList) {
                    if (!Filter.validateFilter(context, entry)) {
                        continue
                    }
                    moduleAndCourseList.add(
                        """${entry?.module}
     ${entry?.course}"""
                    )
                    examinerAndSemester.add(
                        entry?.firstExaminer
                                + " " + entry?.secondExaminer
                                + " " + entry?.semester + " "
                    )
                    dateList.add(entry?.date ?: "")
                    moduleList.add(entry?.module ?: "")
                    idList.add(entry?.id ?: "")
                    formList.add(entry?.examForm ?: "")
                    roomList.add(entry?.room ?: "")
                    statusMessage.add(entry?.hint ?: "")
                    checkList.add(true)
                }
            } // define an adapter
            setPruefungszeitraum()
        }
    }

    /**
     * Sets the text for the current period with content from shared preferences
     *
     * @since 1.6
     * @author Alexander Lange (E-Mail:alexander.lange@fh-bielefeld.de)
     */
    fun setPruefungszeitraum() {
        val start = viewModel.getStartDate()
        val end = viewModel.getEndDate()

        val sdf_write = SimpleDateFormat("dd.MM.yyyy")

        val str = start?.let { start->end?.let { end->sdf_write.format(start) + "-" + sdf_write.format(end) } }
        if (str != "0") {
            currentPeriode?.text = str
        }
    }

    /**
     * Refreshes the Recyclerview with new Filteroptions. It is appended to the Filter.onFilterChangedListener.
     *
     * @since 1.6
     * @author Alexander Lange (E-Mail:alexander.lange@fh-bielefeld.de)
     * @see MainActivity.Filter
     * @see MainActivity.Filter.onFilterChangedListener
     */
    fun OnFilterChanged() {
        viewModel.Filter()
    }

    /**
     * Enables the functionality to swipe an entity from the recyclerview to favor or delete it
     *
     * @since 1.6
     * @author Alexander Lange (E-Mail:alexander.lange@fh-bielefeld.de)
     */
    // Start Merlin Gürtler
    private fun enableSwipeToDelete() {
        // try and catch, da es bei einer
        // Orientierungsänderung sonst zu
        // einer NullPointerException kommt
        try {
            // Definiert den Listener
            val swipeToDeleteCallback: swipeListener =
                object : swipeListener(context!!, false) {
                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {
                        val position = viewHolder.adapterPosition
                        var isFavorite: Boolean? = null
                        isFavorite = mAdapter?.checkFavorite(viewHolder.adapterPosition)
                        if (isFavorite == true) {
                            mAdapter?.deleteFromFavorites(
                                position,
                                (viewHolder as RecyclerViewExamAdapter.ViewHolder)
                            )
                        } else {
                            mAdapter?.addToFavorites(
                                position,
                                (viewHolder as RecyclerViewExamAdapter.ViewHolder)
                            )
                        }
                        mAdapter?.notifyDataSetChanged()
                    }
                }

            // Setzt den Listener
            val itemTouchhelper = ItemTouchHelper(swipeToDeleteCallback)
            itemTouchhelper.attachToRecyclerView(recyclerView4)
        } catch (e: Exception) {
            Log.d("Error", "Orientation error$e")
        }
    }
}