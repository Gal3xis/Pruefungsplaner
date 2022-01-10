package com.Fachhochschulebib.fhb.pruefungsplaner

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.content.SharedPreferences
import com.Fachhochschulebib.fhb.pruefungsplaner.data.AppDatabase
import com.Fachhochschulebib.fhb.pruefungsplaner.model.RetrofitConnect
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.*
import androidx.recyclerview.widget.LinearLayoutManager
import android.widget.Toast
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.Fachhochschulebib.fhb.pruefungsplaner.R.attr.colorOnPrimary
import com.Fachhochschulebib.fhb.pruefungsplaner.Utils.getColorFromAttr
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.json.XML
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

//Alexander Lange Start
import kotlinx.android.synthetic.main.start.*
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//Alexander Lange End

//////////////////////////////
// MainActivity
//
// autor:
// inhalt:  Auswahl des Studiengangs mit dazugehörigem PruefJahr und Semester
// zugriffsdatum: 11.12.19, 08/2020 (LG)
//
//////////////////////////////
/**
 * Activity, that allows the user to pick a faculty and select courses.
 *
 * @author Alexander Lange (Email:alexander.lange@fh-bielefeld.de)
 * @since 1.5
 */
class StartActivity() : AppCompatActivity() {
    var mAdapter: CheckListAdapter? = null


    //KlassenVariablen
    private var courseMain: String? = null
    private var jsonArrayFacultys: JSONArray? = null
    val courseChosen: MutableList<Boolean> = ArrayList()
    val courseName: MutableList<String> = ArrayList()
    val facultyName: MutableList<String> = ArrayList()

    // private Spinner spStudiengangMain;
    // List<String> idList = new ArrayList<String>();
    var mSharedPreferencesPPServerAdress: SharedPreferences? = null
    var sharedPreferencesSettings: SharedPreferences? = null
    var mSharedPreferencesValidation: SharedPreferences? = null
    var sharedPrefPruefPeriode: SharedPreferences? = null
    var sharedPrefsFaculty:SharedPreferences? = null
    var database: AppDatabase? = null
    var context: Context? = null

    val SCOPE_IO = CoroutineScope(CoroutineName("IO-Scope") + Dispatchers.IO)

    var serverAddress: String? = null
    var relativePPlanURL: String? = null

    // Start Merlin Gürtler
    // Fügt den Hauptstudiengang zu den Shared Preferences hinzu
    /**
     * Selects a course a the main course.
     *
     * @param[choosenCourse] The course that is supposed to be the mein course.
     *
     * @author Alexander Lange
     * @since 1.5
     */
    private fun addMainCourse(choosenCourse: String?) {
        if (database == null) {
            return
        }
        SCOPE_IO.launch {
            returnCourse = database?.userDao()?.getIdCourse(choosenCourse)

            // Erstelle Shared Pref für die anderen Fragmente

            val editorStudiengangValidation = mSharedPreferencesValidation?.edit()
            editorStudiengangValidation?.putString("selectedCourse", choosenCourse)
            editorStudiengangValidation?.putString("returnCourse", returnCourse)
            editorStudiengangValidation?.apply()
            val retrofit = RetrofitConnect(relativePPlanURL ?: "")
            // Überprüfe ob die App schonmal gestartet wurde

            if (database!!.userDao()?.uuid == null) {
                // Sende nur ans Backend wenn die App wirklich zum ersten mal
                // gestartet wurde
                retrofit.firstStart(
                        applicationContext, database!!,
                        serverAddress
                )
            } else {
                retrofit.setUserCourses(applicationContext, database!!, serverAddress)
            }
        }
    }

    // Schließt die App beim BackButton
    /**
     * Dispatch incoming result to the correct fragment.
     *
     * @param[requestCode] – The integer request code originally supplied to startActivityForResult(), allowing you to identify who this result came from.
     * @param[resultCode] – The integer result code returned by the child activity through its setResult().
     * @param[data] – An Intent, which can return result data to the caller (various data can be attached to Intent "extras").
     *
     * @author Alexander Lange
     * @since 1.5
     *
     * @see AppCompatActivity.onActivityResult
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 0) {
            finish()
        }
    }


    // Ende Merlin Gürtler
    /**
     * Overrides the onCreate()-Method, which is called first in the Fragment-LifeCycle.
     *
     * @since 1.5
     *
     * @author Alexander Lange (E-Mail:alexander.lange@fh-bielefeld.de)
     *
     * @see Fragment.onCreate
     */

    public override fun onCreate(savedInstanceState: Bundle?) {
        applySettings()
        //TODO Alexander Lange Start
        //TODO Alexander Lange End
        super.onCreate(savedInstanceState)
        setContentView(R.layout.start)
        context = baseContext
        PushService.createNotificationChannel(this)

        context?.let { PushService.sendNotification(it,"Test") }//TODO REMOVE

        database = AppDatabase.getAppDatabase(baseContext)
        initSharedPreferences()

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        recyclerViewChecklist.setHasFixedSize(true)
        recyclerViewChecklist.layoutManager = LinearLayoutManager(applicationContext)

        //Defininition des Arrays jahreszeit
        val season: MutableList<String> = ArrayList()
        season.add(context?.getString(R.string.sommer).toString())
        season.add(context?.getString(R.string.winter).toString())

        //Kalender:: aktuelles Jahr --> Bestimmung der Prüfphase (WiSe, SoSe)
        val calendar = Calendar.getInstance()
        val calendarMonth = calendar[Calendar.MONTH]
        Log.d("Output Monat", calendarMonth.toString())

        // Start Merlin Gürtler
        //Anzahl der Elemente
        //Adapter-Aufruf
        val strJson = sharedPrefPruefPeriode?.getString("currentPeriode", "0")
        try {
            initServer()
            initButtons()
            val globalVariable = applicationContext as StartClass
            if (courseMain != "0" && !globalVariable.isChangeFaculty) {
                val mainWindow = Intent(applicationContext, MainActivity::class.java)
                startActivityForResult(mainWindow, 0)
            }
            //Creating editor to store uebergebeneModule to shared preferencess
            //second parameter is necessary ie.,Value to return if this preference does not exist.
        } //Wenn Verbindung zum Server nicht möglich, dann Daten aus der Datenbank nehmen
        catch (e: Exception) {
            if (strJson != null) {
                try {
                    jsonArrayFacultys = JSONArray(strJson)
                    var i = 0
                    jsonArrayFacultys?.let {
                        while (i < it.length()) {
                            val json = it.getJSONObject(i)
                            facultyName.add(json["facName"].toString())
                            initButtons()
                            i++
                        }
                    }
                } catch (b: Exception) {
                    Log.d("Datenbankfehler", "Keine Daten in der Datenbank vorhanden!")
                }
            }
        }
    }

    /**
     * Initializes the sharedPrefernces and the parameter which are attatched to them.
     *
     * @author Alexander Lange
     * @since 1.5
     */
    private fun initSharedPreferences() {
        mSharedPreferencesValidation =
                application.getSharedPreferences("validation", MODE_PRIVATE)
        // Ende Merlin Gürtler
        mSharedPreferencesPPServerAdress =
                applicationContext.getSharedPreferences("Server_Address", MODE_PRIVATE)
        sharedPrefsFaculty = getApplicationContext()
                .getSharedPreferences("faculty", Context.MODE_PRIVATE)
        courseMain = mSharedPreferencesValidation?.getString("selectedCourse", "0")
        val mEditorPPServerAdress = mSharedPreferencesPPServerAdress?.edit()
        mEditorPPServerAdress?.putString(
                "ServerIPAddress",
                context?.getString(R.string.server_adress)
        )
        mEditorPPServerAdress?.apply() //Schreiben der Präferenz!
        serverAddress = mSharedPreferencesPPServerAdress?.getString("ServerIPAddress", "0")
        val mEditorRelUrlPath = mSharedPreferencesPPServerAdress?.edit()
        mEditorRelUrlPath?.putString("ServerRelUrlPath", context?.getString(R.string.server_url))
        mEditorRelUrlPath?.apply() //Schreiben der Präferenz!
        //Auslesen zur allgemeinen Verwendung in der aktuellen Activity
        relativePPlanURL = mSharedPreferencesPPServerAdress?.getString("ServerRelUrlPath", "0")
        sharedPrefPruefPeriode = applicationContext.getSharedPreferences("periode", MODE_PRIVATE)
    }

    //Aufruf in onCreate()
    /**
     * Equalizes the Room-Database with the Webserver.
     *
     * @author Alexander Lange
     * @since 1.5
     */
    private fun initServer() {
        checkFaculties(serverAddress + relativePPlanURL + "entity.faculty")
        // Start Merlin Gürtler
        val globalVariable = applicationContext as StartClass
        val retrofit = RetrofitConnect(relativePPlanURL ?: "")

        // Thread für die Studiengänge
        SCOPE_IO.launch {
            if (database != null) {
                retrofit.getCourses(application, database!!, serverAddress)
            }
        }

        // Thread für die UUid
        SCOPE_IO.launch {
            val uuid = database?.userDao()?.uuid
            if (!globalVariable.appStarted && (uuid != null) && !globalVariable.isChangeFaculty) {
                globalVariable.appStarted = true
                if (database != null) {
                    retrofit.anotherStart(
                            applicationContext, database!!,
                            serverAddress
                    )
                }
            }
        }

    }

    /**
     * Shows a Toast for the case, that No connection to the server is available.
     *
     * @author Alexander Lange
     * @since 1.5
     */
    fun noConnection() {
        Toast.makeText(
                applicationContext,
                applicationContext.getString(R.string.noConnection),
                Toast.LENGTH_SHORT
        ).show()
    }

    /**
     * Initializes the buttons of the UI.
     *
     * @author Alexander Lange
     * @since 1.5
     */
    private fun initButtons() {
        runOnUiThread {
            // Start Merlin Gürtler
            buttonForSpinner.setOnClickListener {
                createAlertDialogChooseFaculty()
            }
            buttonOk.setOnClickListener { v ->
                // Erstele einen Dialog um den Hauptstudiengang zu wählen
                createAlertDialogSelectMainCourse(v)
            }
            // Ende Merlin Gürtler
        }
    }

    /**
     * Creates a dialog that asks the User to select a main course.
     *
     * @param[view] The view that calls this method.
     *
     * @author Alexander Lange
     * @since 1.5
     */
    private fun createAlertDialogSelectMainCourse(view: View) {
        val chooseCourse = AlertDialog.Builder(
                this@StartActivity,
                R.style.customAlertDialog
        )
        var oneFavorite = false
        val favoriteCourses: MutableList<String> = ArrayList()
        SCOPE_IO.launch {
            // prüfe ob mindesten ein Studiengang favorisiert wurde
            for (chosen: Boolean in courseChosen) {
                if (chosen) {
                    oneFavorite = true
                    break
                }
            }
            if (oneFavorite) {
                // aktualisiere die db
                for (i in courseChosen.indices) {
                    database?.userDao()?.updateCourse(
                            courseName[i],
                            courseChosen[i]
                    )
                    if (courseChosen[i]) {
                        favoriteCourses.add(courseName[i])
                    }
                }
            }
        }.invokeOnCompletion {
            Handler(Looper.getMainLooper()).post {
                if (oneFavorite) {
                    if (favoriteCourses.size == 1) {
                        addMainCourse(favoriteCourses[0])
                        val mainWindow = Intent(applicationContext, MainActivity::class.java)
                        startActivityForResult(mainWindow, 0)
                    } else {
                        val courses =
                                arrayOfNulls<String>(favoriteCourses.size)
                        for (i in favoriteCourses.indices) {
                            courses[i] = favoriteCourses[i]
                        }
                        chooseCourse.setTitle(R.string.choose_main)
                        chooseCourse.setItems(
                                courses
                        ) { _, which ->
                            addMainCourse(courses[which])
                            val mainWindow = Intent(applicationContext, MainActivity::class.java)
                            startActivityForResult(mainWindow, 0)
                        }
                        chooseCourse.show()
                    }
                } else {
                    Toast.makeText(
                            view.context,
                            view.context.getString(R.string.favorite_one_course),
                            Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    /**
     * Creates a dialog that asks the user to select a faculty.
     * @author Alexander Lange
     * @since 1.5
     */
    private fun createAlertDialogChooseFaculty() {
        val chooseFaculty = AlertDialog.Builder(
                this@StartActivity,
                R.style.customAlertDialog
        )
        val faculties = arrayOfNulls<String>(facultyName.size)
        for (i in facultyName.indices) {
            faculties[i] = facultyName[i]
        }

        // Der Listener für die Item Auswahl
        chooseFaculty.setItems(faculties) { dialog, which ->
            facultyChosen(faculties, which) //for
            dialog.dismiss()
            // Intent hauptfenster = new Intent(getApplicationContext(), Tabelle.class);
            // startActivity(hauptfenster);
        }
        chooseFaculty.show()
    }

    /**
     * Called, when the user picked a faculty.
     * Fills the recyclerview with courses.
     *
     * @param[faculties] The list of faculties.
     * @param[which] The index of the picked item.
     *
     * @author Alexander Lange
     * @since 1.5
     */
    private fun facultyChosen(faculties: Array<String?>, which: Int) {
        for (i in facultyName.indices) {
            if ((faculties[which] == facultyName[i])
            ) {
                try {
                    val `object` = jsonArrayFacultys!!.getJSONObject(i)
                    returnFaculty = `object`["fbid"].toString()
                    Log.d(
                            "Output Fakultaet",
                            returnFaculty ?: "No Faculty"
                    )
                    // Erstelle Shared Pref für die anderen Fragmente

                    val editorFacultyValidation =
                            mSharedPreferencesValidation?.edit()
                    editorFacultyValidation?.putString(
                            "returnFaculty",
                            returnFaculty
                    )
                    editorFacultyValidation?.apply()
                    // füllt die Liste mit Studiengängena
                    SCOPE_IO.launch {
                        val courses =
                                database?.userDao()
                                        ?.getAllCoursesByFacultyId(
                                                returnFaculty
                                        )
                        courseChosen.clear()
                        courseName.clear()
                        if (courses != null) {
                            for (course in courses) {
                                courseName.add(course?.courseName ?: "")
                                courseChosen.add(
                                        course?.choosen ?: false
                                )
                            }
                        }
                    }.invokeOnCompletion {
                        Handler(Looper.getMainLooper()).post {
                            val faculty = faculties[which]
                                    ?: "No Faculty"
                            if (faculty.contains(" ")) {
                                buttonForSpinner.text =
                                        faculty
                                                .substring(
                                                        0,
                                                        faculty
                                                                .indexOf(' ')
                                                )
                            } else {
                                buttonForSpinner.text = faculty
                            }

                            // füge den Adapter der Recyclerview hinzu
                            mAdapter = CheckListAdapter(
                                    courseName,
                                    courseChosen,
                                    applicationContext
                            )
                            recyclerViewChecklist.adapter = mAdapter
                            if (chooseCourseId.visibility != View.VISIBLE) {
                                chooseCourseId.visibility =
                                        View.VISIBLE
                            }
                            if (courseName.size == 0) {
                                if (buttonOk!!.visibility == View.VISIBLE) {
                                    buttonOk!!.visibility =
                                            View.INVISIBLE
                                }
                                chooseCourseId.setText(R.string.no_course)
                                chooseCourseId.setTextColor(
                                        getColorFromAttr(colorOnPrimary, theme)
                                )
                            } else {
                                if (buttonOk!!.visibility != View.VISIBLE) {
                                    buttonOk!!.visibility =
                                            View.VISIBLE
                                }
                                chooseCourseId.setText(R.string.choose_course)
                                chooseCourseId.setTextColor(
                                        getColorFromAttr(colorOnPrimary, theme)
                                )
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.d(
                            "uebergabeAnSpinner",
                            "Fehler: Parsen von 'uebergabeAnSpinner'"
                    )
                }
            } //if
        }
    }

    //Methode zum Überprüfen der fakultaeten
    //Aufruf in checkVerbindung()
    /**
     * Get a list of faculties from the webserver and stores them into the SharedPreferences.
     *
     * @param[address] The address of the server.
     *
     * @author Alexander Lange
     * @since 1.5
     */
    fun checkFaculties(address: String?) {
        SCOPE_IO.launch {
            //Verbindungsaufbau zum Webserver
            try {
                updateFaculties(address)
            } catch (e: Exception) {
                val strFacultys: String? = sharedPrefsFaculty?.getString("faculty", "0")
                if (strFacultys != null) {
                    try {
                        jsonArrayFacultys = JSONArray(strFacultys)
                        var i: Int = 0
                        while (i < jsonArrayFacultys!!.length()) {
                            val json: JSONObject = jsonArrayFacultys!!.getJSONObject(i)
                            facultyName.add(json.get("facName").toString())
                            i++
                        }
                    } catch (b: Exception) {
                        Log.d(
                                "uebergabeAnSpinner",
                                "Fehler beim Parsen des Fakultätsnamen."
                        )
                    }
                }
                Handler(Looper.getMainLooper()).post { noConnection() }
            }
        }
    }

    /**
     * Updates the faculties. Get all faculties from the webserver and synchronize them withe the SharedPreferences.
     *
     * @param[address] The address of the server.
     *
     * @author Alexander Lange
     * @since 1.5
     *
     */
    private fun updateFaculties(address: String?) {
        val result = StringBuilder()
        val url = URL(address)
        val urlConn: HttpURLConnection = url.openConnection() as HttpURLConnection
        urlConn.setConnectTimeout(1000 * 10) // mTimeout is in seconds
        urlConn.connect()

        //Parsen von den  erhaltene Werte
        val `in`: InputStream = BufferedInputStream(urlConn.getInputStream())
        val reader: BufferedReader = BufferedReader(InputStreamReader(`in`))
        var line: String?
        while ((reader.readLine().also { line = it }) != null) {
            result.append(line)
        }

        //Erstellen von JSON
        var jsonObj: JSONObject? = null
        try {
            jsonObj = XML.toJSONObject(result.toString())
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val x: Iterator<*> = jsonObj!!.keys()
        val jsonArray: JSONArray = JSONArray()
        while (x.hasNext()) {
            val key: String = x.next() as String
            jsonArray.put(jsonObj.get(key))
        }

        //Werte von JSONARRay in JSONObject konvertieren
        val receivesFacultys: JSONArray = JSONArray()
        for (i in 0 until jsonArray.length()) {
            val `object`: JSONObject = jsonArray.getJSONObject(i)
            receivesFacultys.put(`object`.get("faculty"))
        }
        val convertedToString: String = receivesFacultys.toString()
        val deletedCling: String =
                convertedToString.substring(1, convertedToString.length - 1)
        //konvertieren zu JSONArray
        jsonArrayFacultys = JSONArray(deletedCling)
        for (i in 0 until jsonArrayFacultys!!.length()) {
            val json: JSONObject = jsonArrayFacultys!!.getJSONObject(i)
            facultyName.add(json.get("facName").toString())
        }

        // Werte Speichern für die offline Verwendung
        //Log.d("Output fakultaet", jsonArrayFakultaeten.get(0).toString());
        val facultyEditor = sharedPrefsFaculty?.edit()
        try {
            facultyEditor?.clear()
            facultyEditor?.apply()
            facultyEditor?.putString("faculty", deletedCling)
            facultyEditor?.apply()
        } catch (e: Exception) {
            Log.d(
                    "Output checkFakultaet",
                    "Fehler: Parsen von Fakultaet"
            )
        }
        Log.d("Output checkFakultaet", "abgeschlossen")
    }


    companion object {
        var returnCourse: String? = null
        var returnFaculty: String? = null
    }
}