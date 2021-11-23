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
class Terminefragment : Fragment() {
    var progressBar: ProgressDialog? = null
    private var date: String? = null
    private var month2: String? = null
    private var day2: String? = null
    var positionOld = 0
    private var year2: String? = null
    var checkList: MutableList<Boolean> = ArrayList()

    //Variablen
    var moduleAndCourseList: MutableList<String> = ArrayList()
    var examinerAndSemester: MutableList<String> = ArrayList()
    var dateList: MutableList<String> = ArrayList()
    var moduleList: MutableList<String> = ArrayList()
    var idList: MutableList<String> = ArrayList()
    var formList: MutableList<String> = ArrayList()
    var roomList: MutableList<String> = ArrayList()
    var statusMessage: MutableList<String> = ArrayList()
    var mSharedPreferencesValidation: SharedPreferences? = null
    var examineYear: String? = null
    var currentExaminePeriod: String? = null
    var returnCourse: String? = null
    var mAdapter: MyAdapter? = null
    private var mLayout: RecyclerView.LayoutManager? = null
    var database: AppDatabase? = null
    //TODO Alexander Lange Start
    var filterChangeListenerPosition:Int?=null
    //TODO Alexander Lange End

    // Start Merlin Gürtler
    // Funktion um die Führende 0 hinzuzufügen
    fun formatDate(dateToFormat: String): String {
        var dateToFormat = dateToFormat
        if (dateToFormat.length == 1) {
            dateToFormat = "0$dateToFormat"
        }
        return dateToFormat
    }

    // Ende Merlin Gürtler
    // List<PruefplanEintrag> ppeList = datenbank.userDao().getEntriesByValidation(validation);
    // Start Merlin Gürtler
    private fun createAdapter() {
        // Nun aus Shared Preferences
        examineYear = mSharedPreferencesValidation?.getString("examineYear", "0")
        currentExaminePeriod = mSharedPreferencesValidation?.getString("currentPeriode", "0")
        returnCourse = mSharedPreferencesValidation?.getString("returnCourse", "0")
        validation = examineYear + returnCourse + currentExaminePeriod
        //val ppeList = database?.userDao()?.getEntriesByValidation(validation)
        val ppeList = database?.userDao()?.allEntries
        Log.d("validation",ppeList?.size.toString())//TODO REMVOE
        ClearLists()
        if (ppeList != null) {
            for (entry in ppeList) {
                if (!table.Filter.validateFilter(context,entry)) {
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
        Handler(Looper.getMainLooper()).post { recyclerView4?.adapter = mAdapter }
    }


    // Ende Merlin Gürtler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    /*
    class LongOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            List<TestPlanEntry> ppeList
                    = Terminefragment.this.database.userDao().getEntriesByValidation(validation);
            if (ppeList.size() < 1) {
                for (int c = 0; c < 1000; c++) {
                    try {
                        Thread.sleep(3000);
                        if (RetrofitConnect.checkTransmission) {
                            return "Executed";
                        }
                    } catch (InterruptedException e) {
                        Thread.interrupted();
                    }
                }
            }
            return "null";
        }

        @Override
        protected void onPostExecute(String result) {
            AdapterPassed();
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.terminefragment, container, false)


        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //From OnCreate
        // Start Merlin Gürtler
        //Zugriffrechte für den GoogleKalender
        //Id für den Google Kalender
        val callbackId = 42
        val v = view
        //Wert1: ID Google Kalender, Wert2: Rechte fürs Lesen, Wert3: Rechte fürs schreiben)
        checkPermission(
            callbackId,
            Manifest.permission.READ_CALENDAR,
            Manifest.permission.WRITE_CALENDAR
        )
        mSharedPreferencesValidation = this@Terminefragment.context
            ?.getSharedPreferences("validation", 0)
        val courseMain = mSharedPreferencesValidation?.getString("selectedCourse", "0")
        database = AppDatabase.getAppDatabase(this.context!!)
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

            // Die Daten für update aus den Shared Preferences
            val mSharedPreferencesPPServerAdress = this@Terminefragment.context!!
                .getSharedPreferences("Server_Address", Context.MODE_PRIVATE)
            val relativePPlanURL =
                mSharedPreferencesPPServerAdress.getString("ServerRelUrlPath", "0")
            val serverAddress = mSharedPreferencesPPServerAdress.getString("ServerIPAddress", "0")
            val mSharedPreferencesExamineYear = this@Terminefragment.context
                ?.getSharedPreferences("examineTermin", 0)
            val currentExamineYear = mSharedPreferencesExamineYear?.getString("currentTermin", "0")
            val retrofit = RetrofitConnect(relativePPlanURL ?: "")

            // Thread um die Prüfperiode zu aktualisieren
            //TODO CHANGE TO COROUTINE
            Thread { //Shared Pref. für die Pruefperiode
                val mEditor: SharedPreferences.Editor?
                val mSharedPreferencesPPeriode =
                    context?.getSharedPreferences("currentPeriode", Context.MODE_PRIVATE)

                // Erhalte die gewählte Fakultät aus den Shared Preferences
                val facultyId = mSharedPreferencesValidation?.getString("returnFaculty", "0")
                try {
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
                    val `in`: InputStream = BufferedInputStream(urlConn.inputStream)
                    val reader = BufferedReader(InputStreamReader(`in`))
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

                    //hinzufügen der erhaltenen werte JSONObject werte zum JSONArray
                    val x: Iterator<*> = jsonObj!!.keys()
                    val jsonArray = JSONArray()
                    while (x.hasNext()) {
                        val key = x.next() as String
                        jsonArray.put(jsonObj[key])
                    }
                    val examinePeriodArray = JSONArray()
                    for (i in 0 until jsonArray.length()) {
                        val `object` = jsonArray.getJSONObject(i)
                        examinePeriodArray.put(`object`["pruefperioden"])
                    }
                    val arrayZuString = examinePeriodArray.toString()
                    val erstesUndletztesZeichenentfernen =
                        arrayZuString.substring(1, arrayZuString.length - 1)
                    val mainObject2 = JSONArray(erstesUndletztesZeichenentfernen)

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

                    //Durch-Iterieren durch alle Prüfperioden-Objekte des JSON-Ergebnisses
                    for (i in 0 until mainObject2.length()) {
                        currentExamineDate = mainObject2.getJSONObject(i)
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
                    examineWeek = currentExamineDate?.get("PPWochen")?.toString()?.toInt() ?: 1
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
                createAdapter()
                Handler(Looper.getMainLooper()).post {
                    val strJson = mSharedPreferencesPPeriode?.getString("currentPeriode", "0")
                    if (strJson != "0") {
                        currentPeriode?.text = strJson
                    }
                }
            }.start()

            // prüft ob auch alle ausgewählten Studiengänge vorhanden sind
            Thread {
                val courses = database?.userDao()?.allCourses

                // aktualsiere die db Einträge
                val courseIds = JSONArray()
                var courseName: String
                if (courses != null) {
                    for (course in courses) {
                        try {
                            courseName = course?.courseName ?: ""
                            if (course?.choosen == false) {
                                // lösche nicht die Einträge der gewählten Studiengänge und Favorit
                                val toDelete =
                                    database?.userDao()?.getEntriesByCourseName(courseName, false)
                                database?.userDao()?.deleteEntry(toDelete)
                            }
                            if (database?.userDao()
                                    ?.getOneEntryByName(
                                        courseName,
                                        false
                                    ) == null && course?.choosen == true
                            ) {
                                val idJson = JSONObject()
                                idJson.put("ID", course?.sgid)
                                courseIds.put(idJson)
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                }

                // > 2 da auch bei einem leeren Json Array [] gesetzt werden
                if (courseIds.toString().length > 2) {
                    retrofit.UpdateUnkownCourses(
                        context!!,
                        database!!,
                        examineYear!!,
                        currentExaminePeriod!!,
                        currentExamineYear!!,
                        serverAddress!!,
                        courseIds.toString()
                    )
                }
            }.start()
        }

        // LongOperation asynctask = new LongOperation();

        // asynctask.execute("");

        //From onCreateView
        //hinzufügen von recycleview
        //TODO REMOVE recyclerView = v.findViewById<View>(R.id.recyclerView4) as RecyclerView
        //TODO REMOVE currentPeriodeTextView = v.findViewById<View>(R.id.currentPeriode) as TextView
        val mSharedPreferencesPPeriode =
            context?.getSharedPreferences("currentPeriode", Context.MODE_PRIVATE)
        val strJson = mSharedPreferencesPPeriode?.getString("currentPeriode", "0")
        if (strJson != "0") {
            currentPeriode?.text = strJson
        }
        recyclerView4?.visibility = View.VISIBLE

        // use this setting to
        // improve performance if you know that changes
        // in content do not change the layout size
        // of the RecyclerView
        recyclerView4?.setHasFixedSize(true)

        //mSharedPreferences = v.getContext().getSharedPreferences("json6", 0);
        // use a linear layout manager
        val layoutManager = LinearLayoutManager(v.context)
        recyclerView4?.layoutManager = layoutManager
        mLayout = recyclerView4?.layoutManager
        AdapterPassed()
        recyclerView4?.addOnItemTouchListener(
            RecyclerItemClickListener(
                activity,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View?, position: Int) {
                        //TODO REMOVE val txtSecondScreen =
                        //TODO REMOVE view!!.findViewById<View>(R.id.txtSecondscreen) as TextView
                        val viewItem = recyclerView4?.layoutManager?.findViewByPosition(position)
                        val layout1 =
                            viewItem?.findViewById<View>(R.id.linearLayout) as LinearLayout
                        layout1?.setOnClickListener { v1: View? ->
                            Log.e("@@@@@", "" + position)
                            if (txtSecondscreen?.visibility == View.VISIBLE) {
                                txtSecondscreen?.visibility = View.GONE
                                checkList[position] = false
                            } else {
                                // Start Merlin Gürtler
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
                                txtSecondscreen?.visibility = View.VISIBLE
                                txtSecondscreen?.text = mAdapter?.giveString(position)
                            }
                        }

                        /* Merlin Gürtler
                    Dieser Code scheint nichts zu tun
                    try{
                        if(checkList.get(position)) {
                            for(int i = 0; i < recyclerView.getAdapter().getItemCount(); i++) {

                            }


                            txtSecondScreen.setVisibility(v.VISIBLE);
                            txtSecondScreen.setText(mAdapter.giveString(position));
                        }}
                    catch(Exception e){
                        Log.e("Fehler Terminefragment",
                                "Fehler weitere Informationen");
                    }
                    */positionOld = position
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
        table.Filter.onFilterChangedListener.add {OnFilterChanged()}
        filterChangeListenerPosition = table.Filter.onFilterChangedListener.size-1
        //TODO Alexander Lange End


    }

    override fun onDestroy() {
        super.onDestroy()
        if(filterChangeListenerPosition!=null){
            table.Filter.onFilterChangedListener.removeAt(filterChangeListenerPosition!!)
        }
    }


    fun OnFilterChanged(){
        Thread(object:Runnable{
            override fun run() {
                createAdapter()
                Log.d("Terminefragment.kt-OnFilterChanged","Updated Filter")
            }
        }).start()
    }

    fun AdapterPassed() {
        //TODO CHANGE TO COROUTINE
        Thread {
            createAdapter()
            Handler(Looper.getMainLooper()).post { // Merlin Gürtler
                // Aktiviert den swipe listener
                enableSwipeToDelete()
            }
            // System.out.println(String.valueOf(userdaten.size()));
        }.start()
        //Datenbankaufruf
    }

    fun ClearLists() {
        moduleAndCourseList.clear()
        examinerAndSemester.clear()
        dateList.clear()
        moduleList.clear()
        idList.clear()
        formList.clear()
        roomList.clear()
        statusMessage.clear()
        checkList.clear()
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
    }

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

    companion object {
        var validation: String? = null
    }
}