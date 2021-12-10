package com.Fachhochschulebib.fhb.pruefungsplaner

import android.Manifest
import android.app.ProgressDialog
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import android.content.SharedPreferences
import com.Fachhochschulebib.fhb.pruefungsplaner.data.AppDatabase
import android.os.Looper
import android.os.Bundle
import com.Fachhochschulebib.fhb.pruefungsplaner.model.RetrofitConnect
import org.json.XML
import androidx.recyclerview.widget.LinearLayoutManager
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView.OnChildAttachStateChangeListener
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import android.os.Handler
import android.util.Log
import android.view.*
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.termine.*
import kotlinx.android.synthetic.main.terminefragment.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.*
import java.lang.Runnable

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
 * @since 1.5
 * @author Alexander Lange (E-Mail:alexander.lange@fh-bielefeld.de)
 */
class Terminefragment : Fragment() {

    //region parameter
    //UI
    private var progressBar: ProgressDialog? = null
    private var mLayout: RecyclerView.LayoutManager? = null
    //TODO REMOVE private var date: String? = null
    //TODO REMOVE private var month2: String? = null
    //TODO REMOVE private var day2: String? = null
    //TODO CHECK REMOVE private var positionOld = 0

    //TODO REMOVE private var year2: String? = null
    private var checkList: MutableList<Boolean> = ArrayList()

    //Links to sharedPreferences
    private var mSharedPreferencesValidation: SharedPreferences? = null
    private var mSharedPreferencesPPServerAdress: SharedPreferences? = null
    private var mSharedPreferencesExamineYear: SharedPreferences? = null

    private var mSharedPreferencesPPeriode: SharedPreferences? = null

    //Globaldata from sharedPreferences
    private var serverAddress: String? = null
    private var relativePPlanURL: String? = null
    private var examineYear: String? = null
    private var currentExaminePeriod: String? = null
    private var returnCourse: String? = null
    private var courseMain: String? = null

    private var mAdapter: MyAdapter? = null

    //Link to Room-Database
    private var database: AppDatabase? = null

    //Scopes
    //IO-Scope,optimized for network and disk operations
    private val scope_io =
        CoroutineScope(CoroutineName("IO Scope") + Dispatchers.IO)

    //UI-Scope, improved for ui operations
    private val scope_ui = CoroutineScope(CoroutineName("UI Scope") + Dispatchers.Main)

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
     * @since 1.5
     *
     * @author Alexander Lange (E-Mail:alexander.lange@fh-bielefeld.de)
     *
     * @see Fragment.onCreate
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        //Get access to the shared preferences
        mSharedPreferencesValidation =
            this@Terminefragment.context?.getSharedPreferences("validation", 0)
        mSharedPreferencesPPServerAdress = this@Terminefragment.context!!.getSharedPreferences(
            "Server_Address",
            Context.MODE_PRIVATE
        )
        mSharedPreferencesExamineYear =
            this@Terminefragment.context?.getSharedPreferences("examineTermin", 0)
        mSharedPreferencesPPeriode =
            context?.getSharedPreferences("currentPeriode", Context.MODE_PRIVATE)

        //Get content from shared preferences
        relativePPlanURL =
            mSharedPreferencesPPServerAdress?.getString("ServerRelUrlPath", "0")
        serverAddress = mSharedPreferencesPPServerAdress?.getString("ServerIPAddress", "0")

        //Get access to the Room-Database
        database = AppDatabase.getAppDatabase(context!!)
    }

    /**
     * Overrides the onCreateView()-Method. It sets the current view to the terminefragment-layout.
     *
     * @return Returns the initialized view of this Fragment
     * @since 1.5
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
     * @since 1.5
     * @author Alexander Lange (E-Mail:alexander.lange@fh-bielefeld.de)
     * @see Fragment.onViewCreated
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getCalendarPermission()

        val courseMain = mSharedPreferencesValidation?.getString("selectedCourse", "0")

        updateDataFromServer()
        enableSwipeToDelete()
        initRecyclerview()

        // LongOperation asynctask = new LongOperation();

        // asynctask.execute("");

        //From onCreateView
        //hinzufügen von recycleview
        //TODO REMOVE recyclerView = v.findViewById<View>(R.id.recyclerView4) as RecyclerView
        //TODO REMOVE currentPeriodeTextView = v.findViewById<View>(R.id.currentPeriode) as TextView


        // Ende Merlin Gürtler

        //Touchhelper für die Recyclerview-Komponente, zum Überprüfen, ob gescrollt wurde
        //ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        //itemTouchhelper.attachToRecyclerView(recyclerView);

        //initialisieren der UI-Komponenten
        //TODO REMOVE calendar = v.findViewById<View>(R.id.caCalender) as CalendarView
        //TODO REMOVE btnSearch = v.findViewById<View>(R.id.btnDatum) as Button

        //Clicklistener für den Kalender,
        //Es wird überprüft, welches Datum ausgewählt wurde.
        //TODO Alexander Lange Start
        MainActivity.Filter.onFilterChangedListener.add{OnFilterChanged()}
        filterChangeListenerPosition = MainActivity.Filter.onFilterChangedListener.size-1
    //TODO Alexander Lange End
    }


    /**
     * This Method is called when the fragment gets destroyed. Its the last called Method in the Fragment-Lifecycle.
     * It needs to remove the Filter-Callback from the table.kt-class so it will no longer update when the filter is changed.
     *
     * @since 1.5
     * @author Alexander Lange (E-Mail:alexander.lange@fh-bielefeld.de)
     * @see Fragment.onDestroy
     */
    override fun onDestroy() {
        super.onDestroy()
        try {
            if (filterChangeListenerPosition != null && MainActivity.Filter.onFilterChangedListener.size>=filterChangeListenerPosition!!) {
                MainActivity.Filter.onFilterChangedListener.removeAt(filterChangeListenerPosition!!)
            }

        }catch (ex:Exception){
            Log.e("TermineFragment.onStop",ex.stackTraceToString())
        }
    }

    /**
     * This Method checks, if the user already gave permission to access the Calendar,
     * if not, he is ask to do so.
     *
     * @since 1.5
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

    /**
     * Checks the Phone for a give permission.
     * If the permission is not granted, the user is asked if he wants to grant permission.
     *
     * @param[callbackId] Id of Permission which called function
     * @param[permissionsId] List of permissions that need to be requested
     *
     * @since 1.5
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
    } // Ende Merlin Gürtler

    // Start Merlin Gürtler
    // Funktion um die Führende 0 hinzuzufügen
    /**
     * Used to format a date with leading zeros. Checks if the given number contains only one digit and then adds the zero.
     * TODO Change with SimpleDatePattern
     *
     * @param[dateToFormat] The Number that needs to be formatted
     * @return The formatted date
     * @since 1.5
     * @author Alexander Lange (E-Mail:alexander.lange@fh-bielefeld.de)
     */
    private fun formatDate(dateToFormat: String): String {
        var dateToFormat = dateToFormat
        if (dateToFormat.length == 1) {
            dateToFormat = "0$dateToFormat"
        }
        return dateToFormat
    }

    // Ende Merlin Gürtler


    // Ende Merlin Gürtler

    /**
     * Initializes the Recyclerview which shows the information about pending exams.
     *
     * @since 1.5
     * @author Alexander Lange (E-Mail:alexander.lange@fh-bielefeld.de)
     */
    fun initRecyclerview() {
        recyclerView4?.visibility = View.VISIBLE

        // use this setting to
        // improve performance if you know that changes
        // in content do not change the layout size
        // of the RecyclerView
        recyclerView4?.setHasFixedSize(true)

        termineFragment_swiperefres.setDistanceToTriggerSync(800)

        termineFragment_swiperefres.setOnRefreshListener {
            val globalVariable = this.context?.applicationContext as StartClass
            globalVariable.isShowNoProgressBar = false
            updateDataFromServer()
            initRecyclerview()
            termineFragment_swiperefres.isRefreshing = false
        }

        //mSharedPreferences = v.getContext().getSharedPreferences("json6", 0);
        // use a linear layout manager
        val layoutManager = LinearLayoutManager(view?.context)
        recyclerView4?.layoutManager = layoutManager
        mLayout = recyclerView4?.layoutManager
        //AdapterPassed() TODO REMOVE

        recyclerView4?.addOnItemTouchListener(
            RecyclerItemClickListener(
                activity,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View?, position: Int) {
                        val viewItem = recyclerView4?.layoutManager?.findViewByPosition(position)
                        val layout1 =
                            viewItem?.findViewById<View>(R.id.linearLayout) as LinearLayout
                        layout1.setOnClickListener { v1: View? ->
                            val txtSecondScreen = view!!.findViewById<View>(R.id.txtSecondscreen) as TextView
                            Log.e("@@@@@", "" + position)
                            if (txtSecondScreen.visibility == View.VISIBLE) {
                                txtSecondScreen.visibility = View.GONE
                                checkList[position] = false
                            } else {
                                for (i in 0 until (recyclerView4?.childCount ?: 0)) {
                                    val holder =
                                        recyclerView4?.layoutManager?.findViewByPosition(i)
                                    // Try and Catch, da die App crasht
                                    // wenn das Element nicht im View Port ist
                                    try {
                                        val txtSecondScreen2 =
                                            holder?.findViewById<View>(R.id.txtSecondscreen) as TextView
                                        if (txtSecondScreen2?.visibility == View.VISIBLE) {
                                            txtSecondScreen2?.visibility = View.GONE
                                        }
                                    } catch (e: Exception) {
                                        Log.d("ERROR", "NOT IN VIEW PORT $e")
                                    }
                                }
                                // Ende Merlin Gürtler
                                txtSecondScreen.visibility = View.VISIBLE
                                txtSecondScreen.text = mAdapter?.giveString(position)
                                // Start Merlin Gürtler
                            }
                        }
                        //TODO CHECK REMOVE positionOld = position
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
                //TODO REMOVE val txtSecondScreen = view.findViewById<View>(R.id.txtSecondscreen) as TextView
                if (txtSecondscreen?.visibility == View.VISIBLE) {
                    txtSecondscreen?.visibility = View.GONE
                }
            }
        })
        createView()
    }

    /**
     * Updates the current local data with the data from the server.
     * Can change the data in the recyclerview and the currentExamPeriod
     * Shows a progressbar while loading the data.
     *
     * @since 1.5
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

            scope_io.launch {
                updatePruefperiode()
                updateRoomDatabase()
            }.invokeOnCompletion {
                Handler(Looper.getMainLooper()).post(object : Runnable {
                    override fun run() {
                        createView()
                    }
                })
            }
        }
    }

    /**
     * Updates the Room-Database. Checks if entries have to be removed or have to be loaded from the server.
     *
     * @since 1.5
     * @author Alexander Lange (E-Mail:alexander.lange@fh-bielefeld.de)
     */
    fun updateRoomDatabase() {
        val currentExamineYear = mSharedPreferencesExamineYear?.getString("currentTermin", "0")
        val retrofit = RetrofitConnect(relativePPlanURL!!)
        // IDs der zu aktualisierenden Kurse
        val courseIds = getUnknownCourseIds()
        //Aktualisiere die notwendigen Kurse
        // > 2 da auch bei einem leeren Json Array [] gesetzt werden
        if (courseIds.toString().length > 2) {
            if (database != null && context != null && currentExamineYear != null && !examineYear.isNullOrEmpty() && !currentExaminePeriod.isNullOrEmpty() && !serverAddress.isNullOrEmpty()) {
                retrofit.UpdateUnkownCourses(
                    context!!,
                    database!!,
                    examineYear!!,
                    currentExaminePeriod!!,
                    currentExamineYear,
                    serverAddress!!,
                    courseIds.toString()
                )
            }
        }
    }


    /**
     * Returns a list of unknown courses that need to be updated
     *
     * @return A JSON-Array of courses that need to be updated.
     * @since 1.5
     * @author Alexander Lange (E-Mail:alexander.lange@fh-bielefeld.de)
     */
    private fun getUnknownCourseIds(): JSONArray {
        var ret = JSONArray()
        val courses = database?.userDao()?.allCourses

        if (courses != null) {
            //Durchlaufe alle Kurse
            for (course in courses) {
                try {

                    val courseName = course?.courseName ?: ""

                    //Prüfe ob Kurs ausgewählt wurde. Falls nicht, lösche TestplanEntries für diesen Kurs
                    //aus der Room-Database
                    if (course?.choosen == false) {
                        // lösche nicht die Einträge der gewählten Studiengänge und Favorit
                        val toDelete =
                            database?.userDao()?.getEntriesByCourseName(courseName, false)
                        database?.userDao()?.deleteEntry(toDelete)
                    }
                    //Prüfe ob Kurs ausgewählt ist und nur unfavorisierte Einträge enthält. Falls ja, füge
                    //Ihn zu den zu aktualisierenden Kursen hinzu
                    if (database?.userDao()?.getOneEntryByName(
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

    /**
     * Checks for a new exam period and updates the exam-data if necessary.
     *
     * @since 1.5
     * @author Alexander Lange (E-Mail:alexander.lange@fh-bielefeld.de)
     */
    //TODO Shorten
    fun updatePruefperiode() {

        val mEditor: SharedPreferences.Editor?
        val retrofit = RetrofitConnect(relativePPlanURL!!)
        // Erhalte die gewählte Fakultät aus den Shared Preferences
        val facultyId = mSharedPreferencesValidation?.getString("returnFaculty", "0")
        try {
            //DONE (09/2020 LG) Aktuellen Prüftermin aus JSON-String herausfiltern!
            //Heutiges Datum als Vergleichsdatum ermitteln und den Formatierer festlegen.
            val now = GregorianCalendar()
            val currentDate = now.time
            val formatter = SimpleDateFormat("yyyy-MM-dd")
            var currentExamineDate: JSONObject? = null
            var date: String
            var facultyIdDB: String
            var examineDate: Date?
            var lastDayPp: Date?
            var examineWeek: Int

            val pruefperiodenObjects = getPruefperiondenObjects()

            //Durch-Iterieren durch alle Prüfperioden-Objekte des JSON-Ergebnisses
            for (i in 0 until pruefperiodenObjects.length()) {
                currentExamineDate = pruefperiodenObjects.getJSONObject(i)
                date = currentExamineDate["startDatum"].toString()
                facultyIdDB = currentExamineDate.getJSONObject("fbFbid")["fbid"].toString()
                //Aus dem String das Datum herauslösen
                date = date.substring(0, 10)
                //und in ein Date-Objekt umwandeln
                examineDate = formatter.parse(date)

                // Erhalte die Anzahl der Wochen
                examineWeek = currentExamineDate["PPWochen"].toString().toInt()
                val c = Calendar.getInstance()
                c.time = examineDate
                c.add(Calendar.DATE, 7 * examineWeek - 2) // Anzahl der Tage Klausurenphase
                lastDayPp = formatter.parse(formatter.format(c.time))

                //und mit dem heutigen Datum vergleichen.
                //Die erste Prüfperioden dieser Iteration, die nach dem heutigen Datum
                //liegt ist die aktuelle Prüfperiode!
                // die Fakultäts id wird noch mit der gewählten Fakultät verglichen
                if (currentDate.before(lastDayPp) && facultyId == facultyIdDB) break
            }
            examineWeek = currentExamineDate?.get("PPWochen")?.toString()?.toInt() ?: 0
            //1 --> 1. Termin; 2 --> 2. Termin des jeweiligen Semesters
            //-------------------------------------------------------------------
            //DONE (08/2020) Termin 1 bzw. 2 in den Präferenzen speichern
            val mSharedPreferencesExamineTermin = context
                ?.getSharedPreferences("examineTermin", Context.MODE_PRIVATE)
            val mEditorExaminePeriodAndYear = mSharedPreferencesValidation?.edit()
            mEditorExaminePeriodAndYear?.putString(
                "examineYear",
                currentExamineDate?.get("PPJahr")?.toString()
            )
            val mEditorTermin = mSharedPreferencesExamineTermin?.edit()
            mEditorTermin?.putString(
                "currentTermin",
                currentExamineDate?.get("pruefTermin")?.toString()
            )
            mEditorExaminePeriodAndYear?.putString(
                "currentPeriode",
                currentExamineDate?.get("pruefSemester")?.toString()
            )
            mEditorExaminePeriodAndYear?.apply()
            mEditorTermin?.apply() //Ausführen der Schreiboperation!
            //-------------------------------------------------------------------
            val currentPeriode = currentExamineDate?.get("startDatum")?.toString()
            val arrayCurrentPeriode = currentPeriode?.split("T")?.toTypedArray()
            val fmt = SimpleDateFormat("yyyy-MM-dd")
            val inputDate = fmt.parse(arrayCurrentPeriode?.get(0))

            //erhaltenes Datum Parsen als Datum
            val calendar: Calendar = GregorianCalendar()
            calendar.time = inputDate
            val year = calendar[Calendar.YEAR]
            //Add one to month {0 - 11}
            val month = calendar[Calendar.MONTH] + 1
            val day = calendar[Calendar.DAY_OF_MONTH]
            calendar.add(Calendar.DATE, 7 * examineWeek - 2)
            val year2 = calendar[Calendar.YEAR]
            //Add one to month {0 - 11}
            val month2 = calendar[Calendar.MONTH] + 1
            val day2 = calendar[Calendar.DAY_OF_MONTH]

            //String Prüfperiode zum Anzeigen
            val currentExamineDateFormatted = (context!!.getString(R.string.current)
                    + formatDate(day.toString())
                    + "." + formatDate(month.toString())
                    + "." + year + context!!.getString(R.string.bis)
                    + formatDate(day2.toString())
                    + "." + formatDate(month2.toString())
                    + "." + year2) // number of days to add;

            //Prüfperiode für die Offline-Verwendung speichern
            mEditor = mSharedPreferencesPPeriode?.edit()
            val strJson = mSharedPreferencesPPeriode?.getString("currentPeriode", "0")
            if (strJson != null) {
                if (strJson == currentExamineDateFormatted) {
                } else {
                    mEditor?.clear()
                    mEditor?.apply()
                    // Start Merlin Gürtler
                    // Speichere das Start und Enddatum der Prüfperiode
                    mEditor?.putString(
                        "startDate", formatDate(day.toString())
                                + "/" + formatDate(month.toString()) + "/" + formatDate(year.toString())
                    )
                    mEditor?.putString(
                        "endDate", formatDate(day2.toString())
                                + "/" + formatDate(month2.toString()) + "/" + formatDate(
                            year2.toString()
                        )
                    )
                    mEditor?.apply()
                    // Ende Merlin Gürtler
                    mEditor?.putString("currentPeriode", currentExamineDateFormatted)
                    mEditor?.apply()
                }
            }
            // Ende Merlin Gürtler
        } catch (e: Exception) {
            Log.d("Output", "Konnte nicht die Pruefphase aktualisieren")
            //Keineverbindung();
        }
        // Nun aus Shared Preferences
        // die Daten für die Periode aus den Shared Preferences
        val sleepTime: Int
        val examineYearThread = mSharedPreferencesValidation?.getString("examineYear", "0")
        val currentExaminePeriodThread =
            mSharedPreferencesValidation?.getString("currentPeriode", "0")
        val currentExamineYearThread =
            mSharedPreferencesExamineYear?.getString("currentTermin", "0")
        sleepTime = if (database?.userDao()?.getEntriesByCourseName(courseMain)?.size == 0
            || currentExamineYearThread != database?.userDao()?.termin
        ) {
            database?.userDao()?.deleteTestPlanEntryAll()
            retrofit.RetrofitWebAccess(
                this@Terminefragment.context!!,
                database!!,
                examineYearThread!!,
                currentExaminePeriodThread!!,
                currentExamineYearThread!!,
                serverAddress!!
            )
            3000
        } else {
            retrofit.retroUpdate(
                this@Terminefragment.context!!,
                database!!,
                examineYearThread!!,
                currentExaminePeriodThread!!,
                currentExamineYearThread!!,
                serverAddress
            )
            2000
        }
        try {
            // Timeout für die Progressbar
            Thread.sleep(sleepTime.toLong())
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        progressBar?.dismiss()
    }

    /**
     * Returns a list of all examperiods in the database.
     *
     * @return a JSONArray with all examperiods containing information. The Json-Objects contain data about the first day of the period, the semester (WiSe or SoSe), first or second period, weeknumber and faculty.
     * @since 1.5
     * @author Alexander Lange (E-Mail:alexander.lange@fh-bielefeld.de)
     */
    private fun getPruefperiondenObjects(): JSONArray {
        val result = StringBuilder()

        //DONE (08/2020 LG)
        val address = serverAddress + relativePPlanURL + "entity.pruefperioden"
        val url = URL(address)

        /*
                    HttpURLConnection anstelle Retrofit, um die XML/Json-Daten abzufragen!!!
                 */
        val urlConn = url.openConnection() as HttpURLConnection
        urlConn.connectTimeout = 1000 * 10 // mTimeout is in seconds
        try {
            urlConn.connect()
        } catch (e: Exception) {
            Log.d("Output exception", e.toString())
        }

        //Variablen zum lesen der erhaltenen werte
        val inputStream: InputStream = BufferedInputStream(urlConn.inputStream)
        val reader = BufferedReader(InputStreamReader(inputStream))
        var line: String?
        while (reader.readLine().also { line = it } != null) {
            result.append(line)
        }
        var jsonObj: JSONObject? = null
        try {
            jsonObj = XML.toJSONObject(result.toString())
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        //hinzufügen der erhaltenen JSONObject werte zum JSONArray
        val x: Iterator<*> = jsonObj!!.keys()
        val jsonArray = JSONArray()
        while (x.hasNext()) {
            val key = x.next() as String
            jsonArray.put(jsonObj[key])
        }
        val examinePeriodArray = JSONArray()
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            examinePeriodArray.put(jsonObject["pruefperioden"])
        }
        val arrayZuString = examinePeriodArray.toString()
        val erstesUndletztesZeichenentfernen =
            arrayZuString.substring(1, arrayZuString.length - 1)
        return JSONArray(erstesUndletztesZeichenentfernen)
    }


// List<PruefplanEintrag> ppeList = datenbank.userDao().getEntriesByValidation(validation);
    // Start Merlin Gürtler
    /**
     * Gets the Entries from the Room-Database and adds them to the Recyclerview.
     *
     * @since 1.5
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


        scope_io.launch {
            examineYear = mSharedPreferencesValidation?.getString("examineYear", "0")
            currentExaminePeriod = mSharedPreferencesValidation?.getString("currentPeriode", "0")
            returnCourse = mSharedPreferencesValidation?.getString("returnCourse", "0")
            validation = examineYear + returnCourse + currentExaminePeriod
            //val ppeList = database?.userDao()?.getEntriesByValidation(validation)
            val ppeList = database?.userDao()?.allEntries
            Log.d("validation", ppeList?.size.toString())//TODO REMVOE
            if (ppeList != null) {
                for (entry in ppeList) {
                    if (!MainActivity.Filter.validateFilter(context, entry)) {
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
            mAdapter = MyAdapter(
                moduleAndCourseList,
                examinerAndSemester,
                dateList,
                moduleList,
                idList,
                formList,
                mLayout,
                roomList,
                statusMessage
            )
            Handler(Looper.getMainLooper()).post {
                recyclerView4?.adapter = mAdapter
                setPruefungszeitraum()
            }
        }
    }

    /**
     * Sets the text for the current period with content from shared preferences
     *
     * @since 1.5
     * @author Alexander Lange (E-Mail:alexander.lange@fh-bielefeld.de)
     */
    fun setPruefungszeitraum() {
        val sdf_read = SimpleDateFormat("dd/MM/yyyy")
        val start = sdf_read.parse(mSharedPreferencesPPeriode?.getString("startDate","0"))
        val end = sdf_read.parse(mSharedPreferencesPPeriode?.getString("endDate","0"))

        val sdf_write = SimpleDateFormat("dd.MM.yyyy")

        val strJson = sdf_write.format(start) + "-" + sdf_write.format(end)
        //TODO REMOVE val strJson = mSharedPreferencesPPeriode?.getString("currentPeriode", "0")
        if (strJson != "0") {
            currentPeriode?.text = strJson
        }
    }

    /**
     * Refreshes the Recyclerview with new Filteroptions. It is appended to the Filter.onFilterChangedListener.
     *
     * @since 1.5
     * @author Alexander Lange (E-Mail:alexander.lange@fh-bielefeld.de)
     * @see MainActivity.Filter
     * @see MainActivity.Filter.onFilterChangedListener
     */
    fun OnFilterChanged() {
        scope_io.launch {
            createView()
            Log.d("Terminefragment.kt-OnFilterChanged", "Updated Filter")
        }
    }

    /**
     * Enables the functionality to swipe an entity from the recyclerview to favor or delete it
     *
     * @since 1.5
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
                        scope_ui.launch {
                            isFavorite = mAdapter?.checkFavorite(viewHolder.adapterPosition)
                        }.invokeOnCompletion {
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
                    }
                }

            // Setzt den Listener
            val itemTouchhelper = ItemTouchHelper(swipeToDeleteCallback)
            itemTouchhelper.attachToRecyclerView(recyclerView4)
        } catch (e: Exception) {
            Log.d("Error", "Orientation error$e")
        }
    }

//    TODO REMOVE
//    fun AdapterPassed() {
//        //TODO CHANGE TO COROUTINE
//        Thread {
//            createAdapter()
//            Handler(Looper.getMainLooper()).post { // Merlin Gürtler
//                // Aktiviert den swipe listener
//            }
//            // System.out.println(String.valueOf(userdaten.size()));
//        }.start()
//        //Datenbankaufruf
//    }
}