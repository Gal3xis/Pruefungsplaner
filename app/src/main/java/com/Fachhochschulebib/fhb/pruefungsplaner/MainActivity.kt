package com.Fachhochschulebib.fhb.pruefungsplaner

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.Fachhochschulebib.fhb.pruefungsplaner.CheckListAdapter
import androidx.recyclerview.widget.RecyclerView
import android.content.SharedPreferences
import com.Fachhochschulebib.fhb.pruefungsplaner.data.AppDatabase
import com.Fachhochschulebib.fhb.pruefungsplaner.MainActivity
import com.Fachhochschulebib.fhb.pruefungsplaner.model.RetrofitConnect
import android.content.Intent
import com.Fachhochschulebib.fhb.pruefungsplaner.table
import android.os.Bundle
import android.content.pm.ActivityInfo
import com.Fachhochschulebib.fhb.pruefungsplaner.R
import androidx.recyclerview.widget.LinearLayoutManager
import com.Fachhochschulebib.fhb.pruefungsplaner.StartClass
import com.Fachhochschulebib.fhb.pruefungsplaner.data.Uuid
import android.widget.Toast
import android.content.DialogInterface
import android.graphics.Color
import android.os.Handler
import com.Fachhochschulebib.fhb.pruefungsplaner.data.Courses
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
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
import org.w3c.dom.Attr

//Alexander Lange End

//////////////////////////////
// MainActivity
//
// autor:
// inhalt:  Auswahl des Studiengangs mit dazugehörigem PruefJahr und Semester
// zugriffsdatum: 11.12.19, 08/2020 (LG)
//
//////////////////////////////
class MainActivity() : AppCompatActivity() {
    var mAdapter: CheckListAdapter? = null


    //KlassenVariablen
    private var courseMain: String? = null
    private var jsonArrayFacultys: JSONArray? = null
    val courseChosen: MutableList<Boolean> = ArrayList()
    val courseName: MutableList<String> = ArrayList()
    val facultyName: MutableList<String> = ArrayList()
    var sharedPreferencesSettings:SharedPreferences?=null

    // private Spinner spStudiengangMain;
    // List<String> idList = new ArrayList<String>();
    var mSharedPreferencesPPServerAdress: SharedPreferences? = null
    var serverAddress: String? = null
    var relativePPlanURL: String? = null

    // Start Merlin Gürtler
    // Fügt den Hauptstudiengang zu den Shared Preferences hinzu
    private fun addMainCourse(choosenCourse: String?) {
        Thread(object : Runnable {
            override fun run() {
                // hole die Studiengang ID aus der DB
                val database = AppDatabase.getAppDatabase(baseContext)
                returnCourse = database?.userDao()?.getIdCourse(choosenCourse)

                // Erstelle Shared Pref für die anderen Fragmente
                val sharedPrefCourseValidation =
                    applicationContext.getSharedPreferences("validation", Context.MODE_PRIVATE)
                val editorStudiengangValidation = sharedPrefCourseValidation.edit()
                editorStudiengangValidation.putString("selectedCourse", choosenCourse)
                editorStudiengangValidation.putString("returnCourse", returnCourse)
                editorStudiengangValidation.apply()

                //TODO Change to Coroutine
                // Thread für die Uuid
                Thread(Runnable {
                    val retrofit = RetrofitConnect(relativePPlanURL ?: "")
                    // Überprüfe ob die App schonmal gestartet wurde
                    if (database?.userDao()?.uuid == null) {
                        // Sende nur ans Backend wenn die App wirklich zum ersten mal
                        // gestartet wurde
                        if (database != null) {
                            retrofit.firstStart(
                                applicationContext, database,
                                serverAddress
                            )
                        }
                    } else {
                        retrofit.setUserCourses(applicationContext, database, serverAddress)
                    }
                }).start()
                val mainWindow = Intent(applicationContext, table::class.java)
                startActivityForResult(mainWindow, 0)
            }
        }).start()
    }

    // Schließt die App beim BackButton
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 0) {
            finish()
        }
    }

    //TODO FIX

    // Ende Merlin Gürtler
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Set Theme
        sharedPreferencesSettings = getSharedPreferences("settings",Context.MODE_PRIVATE)
        sharedPreferencesSettings?.getInt("themeid",0)?.let { theme.applyStyle(it,true) }

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        val context = baseContext

        //aufrufen des startlayouts
        setContentView(R.layout.start)

        // Start Merlin Gürtler
        //Komponenten  initialisieren für die Verwendung
        recyclerViewChecklist.setHasFixedSize(true)
        //linear layout manager
        val layoutManager = LinearLayoutManager(applicationContext)
        recyclerViewChecklist.layoutManager = layoutManager
        val mSharedPreferencesValidation =
            application.getSharedPreferences("validation", Context.MODE_PRIVATE)
        courseMain = mSharedPreferencesValidation.getString("selectedCourse", "0")
        // Ende Merlin Gürtler
        val mSharedPreferencesPPServerAdress =
            applicationContext.getSharedPreferences("Server_Address", MODE_PRIVATE)
        val mEditorPPServerAdress = mSharedPreferencesPPServerAdress.edit()
        mEditorPPServerAdress.putString(
            "ServerIPAddress",
            context.getString(R.string.server_adress)
        )
        //mEditorPPServerAdress.putString("ServerIPAddress", "http://192.168.178.39:8080/");
        //alternativ in FH: "http://thor.ad.fh-bielefeld.de:8080/"
        mEditorPPServerAdress.apply() //Schreiben der Präferenz!
        //Auslesen zur allgemeinen Verwendung in der aktuellen Activity
        serverAddress = mSharedPreferencesPPServerAdress.getString("ServerIPAddress", "0")
        val mEditorRelUrlPath = mSharedPreferencesPPServerAdress.edit()
        mEditorRelUrlPath.putString("ServerRelUrlPath", context.getString(R.string.server_url))
        mEditorRelUrlPath.apply() //Schreiben der Präferenz!
        //Auslesen zur allgemeinen Verwendung in der aktuellen Activity
        relativePPlanURL = mSharedPreferencesPPServerAdress.getString("ServerRelUrlPath", "0")

        //Defininition des Arrays jahreszeit
        val season: MutableList<String> = ArrayList()
        season.add(context.getString(R.string.sommer))
        season.add(context.getString(R.string.winter))

        //Kalender:: aktuelles Jahr --> Bestimmung der Prüfphase (WiSe, SoSe)
        val calendar = Calendar.getInstance()
        val calendarMonth = calendar[Calendar.MONTH]
        Log.d("Output Monat", calendarMonth.toString())

        // Start Merlin Gürtler
        //Anzahl der Elemente
        //Adapter-Aufruf
        val sharedPrefPruefPeriode = applicationContext.getSharedPreferences("periode", MODE_PRIVATE)
        val strJson = sharedPrefPruefPeriode.getString("currentPeriode", "0")
        try {
            checkConnection()
            //Creating editor to store uebergebeneModule to shared preferencess
            //second parameter is necessary ie.,Value to return if this preference does not exist.
        } //Wenn Verbindung zum Server nicht möglich, dann Daten aus der Datenbank nehmen
        catch (e: Exception) {
            if (strJson != null) {
                try {
                    jsonArrayFacultys = JSONArray(strJson)
                    var i = 0
                    while (i < jsonArrayFacultys!!.length()) {//TODO Try Remove !!
                        val json = jsonArrayFacultys!!.getJSONObject(i)
                        facultyName.add(json["facName"].toString())
                        facNamesToSpinner()
                        i++
                    }
                } catch (b: Exception) {
                    Log.d("Datenbankfehler", "Keine Daten in der Datenbank vorhanden!")
                }
            }
        }
    }

    //Aufruf in onCreate()
    fun checkConnection() {
        pingUrl(serverAddress + relativePPlanURL + "entity.faculty")
        // Start Merlin Gürtler
        val globalVariable = applicationContext as StartClass
        val retrofit = RetrofitConnect(relativePPlanURL ?: "")

        // initialisierung db
        val database = AppDatabase.getAppDatabase(baseContext)

        //TODO Change to Coroutine
        // Thread für die Studiengänge
        Thread(object : Runnable {
            override fun run() {
                if (database != null) {
                    retrofit.getCourses(application, database, serverAddress)
                }
            }
        }).start()

        //TODO Change to Coroutine
        // Thread für die UUid
        Thread(object : Runnable {
            override fun run() {
                val uuid = database?.userDao()?.uuid
                if (!globalVariable.appStarted && (uuid != null) && !globalVariable.isChangeFaculty) {
                    globalVariable.appStarted = true
                    retrofit.anotherStart(
                        applicationContext, database,
                        serverAddress
                    )
                }
            }
        }).start()

        //TODO Change to Coroutine
        // Thread für die Navigation
        Thread(object : Runnable {
            override fun run() {

                // Skippe die erstauswahl, wenn schon ein Studiengang gewählt wurde
                if (courseMain != "0" && !globalVariable.isChangeFaculty) {
                    val mainWindow = Intent(applicationContext, table::class.java)
                    startActivityForResult(mainWindow, 0)
                }
            }
        }).start()
        // Ende Merlin Gürtler
    }

    fun NoConnection() {
        Toast.makeText(
            applicationContext,
            applicationContext.getString(R.string.noConnection),
            Toast.LENGTH_SHORT
        ).show()
    }

    //Übernahme der Fakultätsnamen in die Spinner-Komponente.
    fun facNamesToSpinner() {
        runOnUiThread(object : Runnable {
            override fun run() {
                /* Toast.makeText(getBaseContext(), "Prüfungen wurden aktualisiert",
                                    Toast.LENGTH_SHORT).show();
                 */
                //spinnerarray für die fakultaeten
                //adapter aufruf

                // Start Merlin Gürtler
                //TODO Shorten
                buttonForSpinner.setOnClickListener(object : View.OnClickListener {
                    override fun onClick(v: View) {
                        // Erstelle das Dialog Feld für die Auswahl
                        val chooseFaculty = AlertDialog.Builder(
                            this@MainActivity,
                            R.style.customAlertDialog
                        )
                        val faculties = arrayOfNulls<String>(facultyName.size)
                        for (i in facultyName.indices) {
                            faculties[i] = facultyName[i]
                        }

                        // Der Listener für die Item Auswahl
                        chooseFaculty.setItems(faculties, object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface, which: Int) {
                                for (i in facultyName.indices) {
                                    if ((faculties[which]
                                                == facultyName[i])
                                    ) {
                                        try {
                                            val `object` = jsonArrayFacultys!!.getJSONObject(i)
                                            returnFaculty = `object`["fbid"].toString()
                                            Log.d(
                                                "Output Fakultaet",
                                                returnFaculty ?: "No Faculty"
                                            )
                                            // Erstelle Shared Pref für die anderen Fragmente
                                            val sharedPrefFacultyValidation =
                                                applicationContext.getSharedPreferences(
                                                    "validation",
                                                    Context.MODE_PRIVATE
                                                )
                                            val editorFacultyValidation =
                                                sharedPrefFacultyValidation.edit()
                                            editorFacultyValidation.putString(
                                                "returnFaculty",
                                                returnFaculty
                                            )
                                            editorFacultyValidation.apply()

                                            //TODO Change to Coroutine
                                            // füllt die Liste mit Studiengängena
                                            Thread(object : Runnable {
                                                override fun run() {
                                                    val database = AppDatabase.getAppDatabase(
                                                        baseContext
                                                    )
                                                    val courses =
                                                        database?.userDao()
                                                            ?.getAllCoursesByFacultyId(
                                                                returnFaculty
                                                            )
                                                    //TODO Change to MAP?
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
                                                    Handler(Looper.getMainLooper()).post(object :
                                                        Runnable {
                                                        override fun run() {
                                                            // schneidet ab dem Leerzeichen ab, da sonst nicht genug platz ist
                                                            val faculty = faculties[which]
                                                                ?: "No Faculty"//Alexander Lange
                                                            if (faculty.contains(" ")) {
                                                                buttonForSpinner.text =
                                                                    faculty
                                                                        .substring(
                                                                            0,
                                                                            faculty
                                                                                .indexOf(' ')
                                                                        )
                                                            } else {
                                                                buttonForSpinner.text =
                                                                    faculty
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
                                                            //TODO Remove IF-ELSEIF
                                                            if (courseName.size == 0) {
                                                                if (buttonOk!!.visibility == View.VISIBLE) {
                                                                    buttonOk!!.visibility =
                                                                        View.INVISIBLE
                                                                }
                                                                chooseCourseId.setText(R.string.no_course)
                                                                chooseCourseId.setTextColor(
                                                                    getColorFromAttr(colorOnPrimary,theme)
                                                                )
                                                            } else {
                                                                if (buttonOk!!.visibility != View.VISIBLE) {
                                                                    buttonOk!!.visibility =
                                                                        View.VISIBLE
                                                                }
                                                                chooseCourseId.setText(R.string.choose_course)
                                                                chooseCourseId.setTextColor(
                                                                        getColorFromAttr(colorOnPrimary,theme)
                                                                )
                                                                System.out.println(colorOnPrimary.toString())
                                                            }
                                                        }
                                                    })
                                                }
                                            }).start()
                                        } catch (e: Exception) {
                                            Log.d(
                                                "uebergabeAnSpinner",
                                                "Fehler: Parsen von 'uebergabeAnSpinner'"
                                            )
                                        }
                                    } //if
                                } //for
                                dialog.dismiss()
                                // Intent hauptfenster = new Intent(getApplicationContext(), Tabelle.class);
                                // startActivity(hauptfenster);
                            }
                        })
                        chooseFaculty.show()
                    }
                })
                buttonOk.setOnClickListener(object : View.OnClickListener {
                    override fun onClick(v: View) {
                        // Erstele einen Dialog um den Hauptstudiengang zu wählen
                        val chooseCourse = AlertDialog.Builder(
                            this@MainActivity,
                            R.style.customAlertDialog
                        )
                        var oneFavorite = false
                        val database = AppDatabase.getAppDatabase(baseContext)
                        val favoriteCourses: MutableList<String> = ArrayList()
                        //TODO Change to Coroutine
                        Thread(object : Runnable {
                            override fun run() {
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
                                Handler(Looper.getMainLooper()).post(object : Runnable {
                                    override fun run() {
                                        if (oneFavorite) {
                                            if (favoriteCourses.size == 1) {
                                                addMainCourse(favoriteCourses[0])
                                            } else {
                                                val courses =
                                                    arrayOfNulls<String>(favoriteCourses.size)
                                                for (i in favoriteCourses.indices) {
                                                    courses[i] = favoriteCourses[i]
                                                }
                                                chooseCourse.setTitle(R.string.choose_main)
                                                chooseCourse.setItems(
                                                    courses,
                                                    object : DialogInterface.OnClickListener {
                                                        override fun onClick(
                                                            dialog: DialogInterface,
                                                            which: Int
                                                        ) {
                                                            //TODO Change to Coroutine
                                                            Thread(object : Runnable {
                                                                override fun run() {
                                                                    addMainCourse(courses[which])
                                                                }
                                                            }).start()
                                                        }
                                                    })
                                                chooseCourse.show()
                                            }
                                        } else {
                                            Toast.makeText(
                                                v.context,
                                                v.context.getString(R.string.favorite_one_course),
                                                Toast.LENGTH_SHORT
                                            )
                                                .show()
                                        }
                                    }
                                })
                            }
                        }).start()
                    }
                })
                // Ende Merlin Gürtler
            }
        })
    }

    //Methode zum Überprüfen der fakultaeten
    //Aufruf in checkVerbindung()
    fun pingUrl(address: String?) {
        //eigenständiger Thread, weil die Abfrage Asynchron ist
        //TODO Change to Coroutine
        Thread {

            // Die Fakultaeten werden in einer Shared Preferences Variable gespeichert.
            // Creating editor to store Fakultaeten to shared preferences
            val facultyEditor: SharedPreferences.Editor
            val sharedPrefsFaculty: SharedPreferences = getApplicationContext()
                .getSharedPreferences("faculty", 0)

            //Verbindungsaufbau zum Webserver
            try {
                val result: StringBuilder = StringBuilder()
                val url: URL = URL(address)
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
                facultyEditor = sharedPrefsFaculty.edit()
                try {
                    facultyEditor.clear()
                    facultyEditor.apply()
                    facultyEditor.putString("faculty", deletedCling)
                    facultyEditor.apply()
                } catch (e: Exception) {
                    Log.d(
                        "Output checkFakultaet",
                        "Fehler: Parsen von Fakultaet"
                    )
                }
                facNamesToSpinner()
                Log.d("Output checkFakultaet", "abgeschlossen")
            } /*  Wenn keine Verbindung zum Server dann catch Zweig und Daten
                aus den Shared Preferences benutzen
             */ catch (e: Exception) {
                val strFacultys: String? = sharedPrefsFaculty.getString("faculty", "0")
                //Log.d("Output 426",strFakultaet);
                if (strFacultys != null) {
                    try {
                        jsonArrayFacultys = JSONArray(strFacultys)
                        var i: Int = 0
                        while (i < jsonArrayFacultys!!.length()) {
                            val json: JSONObject = jsonArrayFacultys!!.getJSONObject(i)
                            facultyName.add(json.get("facName").toString())
                            facNamesToSpinner()
                            i++
                        }
                    } catch (b: Exception) {
                        Log.d(
                            "uebergabeAnSpinner",
                            "Fehler beim Parsen des Fakultätsnamen."
                        )
                    }
                }
                Handler(Looper.getMainLooper()).post(object : Runnable {
                    override fun run() {
                        NoConnection()
                    }
                })
            }
        }.start()
    }

    var handler = Handler { msg: Message ->
        if (msg.arg1 == 1) {
            Toast.makeText(
                getApplicationContext(),
                getApplicationContext().getString(R.string.noConnection),
                Toast.LENGTH_SHORT
            ).show()
        }
        false
    }

    companion object {
        var returnCourse: String? = null
        var returnFaculty: String? = null
    }
}