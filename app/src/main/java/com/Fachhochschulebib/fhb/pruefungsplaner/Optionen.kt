package com.Fachhochschulebib.fhb.pruefungsplaner

import android.app.Activity
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.Fachhochschulebib.fhb.pruefungsplaner.R
import com.Fachhochschulebib.fhb.pruefungsplaner.data.AppDatabase
import com.Fachhochschulebib.fhb.pruefungsplaner.data.TestPlanEntry
import com.Fachhochschulebib.fhb.pruefungsplaner.CheckGoogleCalendar
import android.os.Looper
import com.Fachhochschulebib.fhb.pruefungsplaner.PrivacyDeclarationFragment
import com.Fachhochschulebib.fhb.pruefungsplaner.model.RetrofitConnect
import android.content.ContentValues
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import android.os.Handler
import android.provider.CalendarContract
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.optionfragment.*
import org.json.JSONArray
import org.json.JSONException
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import androidx.core.app.ActivityCompat.recreate
import kotlinx.android.synthetic.main.optionfragment.view.*


//////////////////////////////
// Optionen
//
//
//
// autor:
// inhalt:  Abfragen ob prüfungen zum Kalender hinzugefügt werden sollen  und Methoden zum löschen, aktualisieren der Datenbank
// zugriffsdatum: 20.2.20
//
//
//
//
//
//
//////////////////////////////
class Optionen() : Fragment() {
    private var save = false
    private var response: JSONArray? = null
    private var calDate = GregorianCalendar()
    private var course: String? = null
    var mSharedPreferencesCurrentTermin: SharedPreferences? = null
    private var currentTermin: String? = null

    companion object {
        val idList: List<String> = ArrayList()
    }

    //DONE: 08/2020 LG
    var serverAddress: String? = null
    var relativePPlanURL: String? = null
    var examineYear: String? = null
    var currentExaminePeriod: String? = null
    var returnCourse: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //From onCreate

        //From onCreateView
        //TODO Implement Settings Preferences
        //TODO Alexander Lange Start
        /*if (night_mode.equals(AppCompatDelegate.MODE_NIGHT_YES)) {
            darkMode?.isSelected = true
        }*/

        theme.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                when (position) {
                    0 -> view.context.theme.applyStyle(R.style.Theme_AppTheme_1,false)
                    1 -> view.context.theme.applyStyle(R.style.Theme_AppTheme_2,false)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        val theme1 = Theme(R.style.Theme_AppTheme_1,view)
        val theme2 = Theme(R.style.Theme_AppTheme_2,view)
        val adapter = ThemeAdapter(view.context,R.layout.layout_theme_spinner_row,mutableListOf(theme1,theme2))
        theme.adapter = adapter
        //TODO Alexander Lange End

        //Button zum updaten der Prüfungen
        val btngo2 = view.findViewById<View>(R.id.btnupdate) as Button
        btngo2.setOnClickListener(View.OnClickListener {
            val validation = examineYear + returnCourse + currentExaminePeriod
            updatePlan(validation)
        })

        //layout Komponenten
        //TODO REMOVE val btnDb = v.findViewById<View>(R.id.btnDB) as Button
        //TODO REMOVE val btnFav = v.findViewById<View>(R.id.btnFav) as Button
        //TODO REMOVE val btnGoogleloeschen = v.findViewById<View>(R.id.btnCalClear) as Button
        //TODO REMOVE val btnGoogleupdate = v.findViewById<View>(R.id.btnGoogleUpdate) as Button
        //TODO REMOVE val SWgooglecalender = v.findViewById<View>(R.id.switch2) as Switch
        //TODO REMOVE val privacyDeclaration = v.findViewById<View>(R.id.privacyDeclaration) as Button
        //holder.zahl1 = position;
        val serverAdresse = view.context.getSharedPreferences("json8", 0)
        //Creating editor to store uebergebeneModule to shared preferences
        val mEditorGoogleCalendar = serverAdresse.edit()
        val mSharedPreferencesPPServerAddress =
            view.context.getSharedPreferences("Server_Address", 0)
        //Creating editor to store uebergebeneModule to shared preferences

        //------------------------------------------------------------------
        //DONE: 08/2020 LG
        serverAddress = mSharedPreferencesPPServerAddress.getString("ServerIPAddress", "0")
        relativePPlanURL = mSharedPreferencesPPServerAddress.getString("ServerRelUrlPath", "0")
        //------------------------------------------------------------------

        //----------------------------------------------------------------------------------------
        val mSharedPreferencesCurrentTermin = view.context
            .getSharedPreferences("examineTermin", 0)
        currentTermin = mSharedPreferencesCurrentTermin.getString("currentTermin", "0")
        //----------------------------------------------------------------------------------------
        response = JSONArray()
        val strServerAddress = serverAdresse.getString("jsondata2", "0")
        //second parameter is necessary ie.,Value to return if this preference does not exist.
        if (strServerAddress != null) {
            try {
                response = JSONArray(strServerAddress)
            } catch (e: JSONException) {
                Log.d("Fehler Optionen", "Server-Adresse Fehler")
            }
        }
        var i: Int
        save = false
        i = 0
        while (i < response?.length() ?: 0) {
            run {
                try {
                    if ((response?.get(i).toString() == "1")) {
                        switch2.setChecked(true)
                        save = true
                    } else {
                        switch2.setChecked(false)
                        save = false
                    }
                } catch (e: JSONException) {
                    Log.d("Fehler Optionen", "Google Kalender aktivierung")
                }
            }
            i++
        }

        //Abfrage ob der Google kalender Ein/Ausgeschaltet ist
        switch2.setOnCheckedChangeListener(object :
            CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
                //TODO CHANGE TO COROUTINE
                Thread(object : Runnable {
                    override fun run() {
                        // do something, the isChecked will be
                        // true if the switch is in the On position
                        if (isChecked) {
                            mEditorGoogleCalendar.clear()
                            mEditorGoogleCalendar.apply()
                            response?.put("1")
                            mEditorGoogleCalendar.putString("jsondata2", response.toString())
                            mEditorGoogleCalendar.apply()
                            val database = AppDatabase.getAppDatabase(context!!)
                            val ppeList = database?.userDao()?.getFavorites(true)
                            val googlecal = CheckGoogleCalendar()
                            googlecal.setCtx(context)
                            if (ppeList != null) {
                                for (entry in ppeList) {
                                    val id = entry?.id

                                    //überprüfung von ein/aus Google Kalender
                                    if (googlecal.checkCal(id?.toInt() ?: 0)) {
                                        //ermitteln von benötigten Variablen
                                        val splitDateAndTime =
                                            entry?.date?.split(" ")?.toTypedArray()
                                        val splitDayMonthYear =
                                            splitDateAndTime?.get(0)?.split("-")?.toTypedArray()
                                        course = entry?.course
                                        course = course + " " + entry?.module
                                        val timeStart =
                                            splitDateAndTime?.get(1)?.substring(0, 2)?.toInt()
                                        val timeEnd =
                                            splitDateAndTime?.get(1)?.substring(4, 5)?.toInt()
                                        calDate = GregorianCalendar(
                                            splitDayMonthYear?.get(0)?.toInt() ?: 0,
                                            splitDayMonthYear?.get(1)?.toInt() ?: 1 - 1,
                                            splitDayMonthYear?.get(2)?.toInt() ?: 0,
                                            timeStart ?: 0,
                                            timeEnd ?: 0
                                        )

                                        //Methode zum Speichern im Kalender
                                        val calendarid = calendarID(course)

                                        //Funktion im Google Kalender, um PrüfID und calenderID zu speichern
                                        googlecal.insertCal(id?.toInt() ?: 0, calendarid)
                                    }
                                }
                            }
                            if (!isChecked) {
                                mEditorGoogleCalendar.clear().apply()
                                mEditorGoogleCalendar.remove("jsondata2").apply()
                            }
                            Handler(Looper.getMainLooper()).post(object : Runnable {
                                override fun run() {
                                    Toast.makeText(
                                        view.context,
                                        view.context.getString(R.string.add_calendar),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            })
                        }
                    }
                }).start()
            }
        })

        //Start Alexander Lange
        //Dark mode Switch

        //Start Alexander Lange
        //Dark mode Switch
        darkMode.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->
            if (b) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                return@OnCheckedChangeListener
            }
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        })
        //End Alexander Lange

        //Change Listener für die Serveradresse
        //speichert den neu eingegebenen Wert
        /*
        txtServerAddress.addTextChangedListener(new TextWatcher() {
            boolean validate = false;

            @Override
            public void afterTextChanged(Editable s) {

                String splitAdresse = txtServerAddress.getText().subSequence(0, 7).toString();
                String splitAdresseEnde
                        = String.valueOf(txtServerAddress
                        .getText()
                        .charAt(txtServerAddress.getText().length() - 1));

                //System.out.println(splitAdresseEnde);

                if (splitAdresse.equals("http://")) {
                    if (splitAdresseEnde.equals("/")) {
                        if (android.util.Patterns.WEB_URL.matcher(
                                txtServerAddress.getText().toString()).matches()) {
                            mEditorAdresse.clear();
                            mEditorAdresse.apply();
                            mEditorAdresse.putString(
                                    "ServerIPAddress", txtServerAddress.getText().toString());
                            mEditorAdresse.apply();
                        }
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            }
        });
         */privacyDeclaration.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                val ft = activity?.supportFragmentManager?.beginTransaction()
                ft?.replace(R.id.frame_placeholder, PrivacyDeclarationFragment())
                ft?.commit()
            }
        })

        //interne DB löschen
        btnDB.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                Thread(object : Runnable {
                    override fun run() {
                        val database = AppDatabase.getAppDatabase(v.context)
                        Log.d("Test", "Lokale DB löschen.")
                        database?.userDao()?.deleteTestPlanEntryAll()

                        // Start Merlin Gürtler

                        // Update nachdem löschen
                        val retrofit = RetrofitConnect(relativePPlanURL ?: "")
                        if (database != null) {
                            retrofit.RetrofitWebAccess(
                                context!!,
                                database,
                                examineYear!!,
                                currentExaminePeriod!!,
                                currentTermin!!,
                                serverAddress!!
                            )
                        }
                        // Ende Merlin Gürtler
                        Handler(Looper.getMainLooper()).post(object : Runnable {
                            override fun run() {
                                Toast.makeText(
                                    v.context,
                                    v.context.getString(R.string.delete_db),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        })
                    }
                }).start()
            }
        })

        //Google Kalender einträge löschen
        btnCalClear.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                deleteCalendar()
                Toast.makeText(
                    v.context,
                    v.context.getString(R.string.delete_calendar),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        //Google Kalender einträge updaten
        btnGoogleUpdate.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                //TODO CHANGE TO COROUTINE
                Thread(object : Runnable {
                    override fun run() {
                        updateCalendar()
                    }
                }).start()
                Handler(Looper.getMainLooper()).post(object : Runnable {
                    override fun run() {
                        Toast.makeText(
                            v.context,
                            v.context.getString(R.string.actualisation_calendar),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            }
        })

        //Favoriten Löschen
        btnFav.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                Thread(object : Runnable {
                    override fun run() {
                        val database = AppDatabase.getAppDatabase(v.context)
                        val ppeList = database?.userDao()?.getFavorites(true)
                        if (ppeList != null) {
                            for (entry in ppeList) {
                                Log.d("Test Favoriten löschen.", entry?.id?.toString() ?: "")
                                database?.userDao()
                                    ?.update(false, entry?.id?.toInt() ?: 0)
                            }
                        }
                        Handler(Looper.getMainLooper()).post(object : Runnable {
                            override fun run() {
                                Toast.makeText(
                                    v.context,
                                    v.context.getString(R.string.delete_favorite),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        })
                        // define an adapter
                    }
                }).start()
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.optionfragment, container, false)
        // Start Merlin Gürtler
        // Nun aus Shared Preferences
        val mSharedPreferencesValidation = container?.context?.getSharedPreferences("validation", 0)
        examineYear = mSharedPreferencesValidation?.getString("examineYear", "0")
        currentExaminePeriod = mSharedPreferencesValidation?.getString("currentPeriode", "0")
        returnCourse = mSharedPreferencesValidation?.getString("returnCourse", "0")
        // Ende Merlin Gürtler
        return v
    }

    fun updatePlan(validation: String?) {
        val a = PingUrl(serverAddress)
    }

    //Methode zum Anzeigen das keine Verbindungs zum Server möglich ist
    fun noConnection() {
        activity?.runOnUiThread(object : Runnable {
            override fun run() {
                Toast.makeText(
                    context,
                    context!!.getString(R.string.noConnection),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    // Methode zum Aktualiseren der Prüfungen
    // die Abfrage Methodes des Webservers
    // gibt Mögliche Änderungen wie den Status zurück,
    // diese werden dann geupdated
    fun updateCheckPlan() {
        Thread(object : Runnable {
            override fun run() {
                val database = AppDatabase.getAppDatabase(context!!)


                //Log.d("Test",String.valueOf(pruefplanDaten.size()));
                //aktuellerTermin, serverAddress, relativePPlanURL aus SharedPreferences

                //retrofit auruf
                val retrofit = RetrofitConnect(relativePPlanURL ?: "")
                retrofit.retroUpdate(
                    context!!,
                    database!!,
                    examineYear!!,
                    currentExaminePeriod!!,
                    currentTermin!!,
                    serverAddress
                )

                // Log.d("Test3",String.valueOf(stringaufteilung[5]));
                Handler(Looper.getMainLooper()).post(object : Runnable {
                    override fun run() {
                        Toast.makeText(
                            context,
                            context!!.getString(R.string.add_favorite),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            }
        }).start()
    }

    //Verbindungsaufbau zum Webserver
    //Überprüfung ob Webserver erreichbar
    fun PingUrl(address: String?): Boolean {
        //TODO CHANGE TO COROUTINE
        Thread(object : Runnable {
            override fun run() {
                try {
                    val url = URL(address)
                    val urlConn = url.openConnection() as HttpURLConnection
                    urlConn.connectTimeout = 1000 * 10 // mTimeout is in seconds
                    val startTime = System.currentTimeMillis()
                    urlConn.connect()
                    val endTime = System.currentTimeMillis()
                    if (urlConn.responseCode == HttpURLConnection.HTTP_OK) {
                        // System.out.println("Time (ms) : " + (endTime - startTime));
                        // System.out.println("Ping to " + address + " successful.");
                        updateCheckPlan()
                    }
                } catch (e: Exception) {
                    noConnection()
                }
            }
        }).start()
        return true
    }

    //Google Kalender einträge löschen
    fun deleteCalendar() {
        val cal = CheckGoogleCalendar()
        cal.setCtx(context)
        cal.clearCal()
    }

    //Google Kalender aktualisieren
    fun updateCalendar() {
        val cal = CheckGoogleCalendar()
        cal.setCtx(context)
        cal.updateCal()
    }

    fun calendarID(eventtitle: String?): Int {
        val event = ContentValues()
        event.put(CalendarContract.Events.CALENDAR_ID, 2)
        event.put(CalendarContract.Events.TITLE, course)
        event.put(CalendarContract.Events.DESCRIPTION, context!!.getString(R.string.fh_name))
        event.put(CalendarContract.Events.DTSTART, calDate.timeInMillis)
        event.put(CalendarContract.Events.DTEND, calDate.timeInMillis + (90 * 60000))
        event.put(CalendarContract.Events.ALL_DAY, 0) // 0 for false, 1 for true
        event.put(CalendarContract.Events.HAS_ALARM, 0) // 0 for false, 1 for true
        val timeZone = TimeZone.getDefault().id
        event.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone)
        val baseUri = Uri.parse("content://com.android.calendar/events")
        context?.contentResolver?.insert(baseUri, event)
        var result = 0
        val projection = arrayOf("_id", "title")
        val cursor = context?.contentResolver
            ?.query(
                baseUri, null,
                null, null, null
            )
        if (cursor!!.moveToFirst()) {
            var calName: String?
            var calID: String
            val nameCol = cursor.getColumnIndex(projection[1])
            val idCol = cursor.getColumnIndex(projection[0])
            do {
                calName = cursor.getString(nameCol)
                calID = cursor.getString(idCol)
                if (calName != null && calName.contains((eventtitle)!!)) {
                    result = calID.toInt()
                }
            } while (cursor.moveToNext())
            cursor.close()
        }
        return (result)
    }

}