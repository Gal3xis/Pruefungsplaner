package com.Fachhochschulebib.fhb.pruefungsplaner

import android.content.SharedPreferences
import android.os.Bundle
import com.Fachhochschulebib.fhb.pruefungsplaner.data.AppDatabase
import android.os.Looper
import com.Fachhochschulebib.fhb.pruefungsplaner.model.RetrofitConnect
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.provider.CalendarContract
import android.util.Log
import android.widget.*
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.optionfragment.*
import org.json.JSONArray
import org.json.JSONException
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import android.view.*
import androidx.core.view.GravityCompat
import com.Fachhochschulebib.fhb.pruefungsplaner.data.TestPlanEntry
import kotlinx.android.synthetic.main.fragment_impressum.*
import kotlinx.android.synthetic.main.hauptfenster.*
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


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
/**
 * Class to maintain the Options-Fragment.
 *
 * @author Alexander Lange (Email:alexander.lange@fh-bielefeld.de)
 * @since 1.5
 */
class Optionen() : Fragment() {
    private var save = false
    private var response: JSONArray? = null
    private var calDate = GregorianCalendar()
    private var course: String? = null
    private var currentTermin: String? = null

    private var mSharedPreferencesCurrentTermin: SharedPreferences? = null
    private var sharedPreferencesSettings: SharedPreferences? = null
    private var mSharedPreferencesPPServerAddress: SharedPreferences? = null
    private var mSharedPreferencesValidation: SharedPreferences? = null

    private var database: AppDatabase? = null

    companion object {
        val idList: List<String> = ArrayList()
    }

    //DONE: 08/2020 LG
    var serverAddress: String? = null
    var relativePPlanURL: String? = null
    var examineYear: String? = null
    var currentExaminePeriod: String? = null
    var returnCourse: String? = null

    private var scope_io = CoroutineScope(CoroutineName("IO-SCOPE") + Dispatchers.IO)

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
        database = context?.let { AppDatabase.getAppDatabase(it) }
        sharedPreferencesSettings = context?.getSharedPreferences("settings", Context.MODE_PRIVATE)
        mSharedPreferencesCurrentTermin = context
            ?.getSharedPreferences("examineTermin", Context.MODE_PRIVATE)
        mSharedPreferencesPPServerAddress =
            context?.getSharedPreferences("Server_Address", Context.MODE_PRIVATE)
        mSharedPreferencesValidation =
            context?.getSharedPreferences("validation", Context.MODE_PRIVATE)
        serverAddress = mSharedPreferencesPPServerAddress?.getString("ServerIPAddress", "0")
        relativePPlanURL = mSharedPreferencesPPServerAddress?.getString("ServerRelUrlPath", "0")
        currentTermin = mSharedPreferencesCurrentTermin?.getString("currentTermin", "0")
        examineYear = mSharedPreferencesValidation?.getString("examineYear", "0")
        currentExaminePeriod = mSharedPreferencesValidation?.getString("currentPeriode", "0")
        returnCourse = mSharedPreferencesValidation?.getString("returnCourse", "0")
        // Ende Merlin Gürtler
        setHasOptionsMenu(true)
    }

    /**
     * Overrides the onViewCreated()-Method, which is called in the Fragment LifeCycle right after the onCreateView()-Method.
     * In this Method, the UI-Elements choose_course.xml-Layout are being initialized. This cannot be done in the onCreate()-Method,
     * because the UI-Elements, which are directly accessed via synthetic imports
     * are no instantiated in the onCreate()-Method yet.
     *
     * @since 1.5
     * @author Alexander Lange (E-Mail:alexander.lange@fh-bielefeld.de)
     * @see Fragment.onViewCreated
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initThemeSpinner()
        initDarkModeSwitch()

        //Button zum updaten der Prüfungen
        btnupdate?.setOnClickListener {
            val validation = examineYear + returnCourse + currentExaminePeriod
            updatePlan(validation)
        }

        //layout Komponenten
        //holder.zahl1 = position;
        val serverAdresse = view.context.getSharedPreferences("json8", Context.MODE_PRIVATE)
        //Creating editor to store uebergebeneModule to shared preferences
        val mEditorGoogleCalendar = serverAdresse.edit()

        doSomething()

        //Abfrage ob der Google kalender Ein/Ausgeschaltet ist
        switch2.setOnCheckedChangeListener { _, isChecked -> // do something, the isChecked will be
            // true if the switch is in the On position
            setCalendarSynchronization(isChecked)
        }

        privacyDeclaration.setOnClickListener {
            val ft = activity?.supportFragmentManager?.beginTransaction()
            header?.title = "Privacy"
            ft?.replace(R.id.frame_placeholder, PrivacyDeclarationFragment())
            ft?.commit()
        }

        optionenfragment_impressum?.setOnClickListener {
            val ft = activity?.supportFragmentManager?.beginTransaction()
            header?.title = "Impressum"
            ft?.replace(R.id.frame_placeholder, ImpressumFragment())
            ft?.commit()
        }

        //interne DB löschen
        btnDB.setOnClickListener { deleteInternalDatabase() }

        //Google Kalender einträge löschen
        btnCalClear.setOnClickListener {
            scope_io.launch {
                val entries = database?.userDao()?.getFavorites(true)
                if (entries != null) {
                    if (entries.isNotEmpty()) {
                        context?.let { it1 ->
                            entries[0]?.let { it2 -> GoogleCalendarIO.deleteEntry(it1, it2) }
                        }

                    }
                }
            }
            /*deleteCalendar()
            Toast.makeText(
                v.context,
                v.context.getString(R.string.delete_calendar),
                Toast.LENGTH_SHORT
            ).show()*/
        }

        //Google Kalender einträge updaten
        //TODO Implement btnGoogleUpdate.setOnClickListener { updateCalendar() }
        btnGoogleUpdate.setOnClickListener {
            scope_io.launch {
                val entries = database?.userDao()?.getFavorites(true)
                if (entries != null) {
                    if (entries.isNotEmpty()) {
                        context?.let { it1 ->
                            entries[0]?.let { it2 ->
                                GoogleCalendarIO.insertEntry(it1, it2, true)
                            }
                        }
                    }
                }
            }
        }
        //Favoriten Löschen
        btnFav.setOnClickListener { deleteFavorits() }

        optionenfragment_save_btn.setOnClickListener { save() }
    }

    /**
     * Deletes the stored favorits of the user from the Room-Database.
     *
     * @author Alexander Lange
     * @since 1.5
     * @see AppDatabase
     */
    private fun deleteFavorits() {
        scope_io.launch {
            val ppeList = database?.userDao()?.getFavorites(true)
            if (ppeList != null) {
                for (entry in ppeList) {
                    Log.d("Test Favoriten löschen.", entry?.id?.toString() ?: "")
                    //Set favorit value for every favorit to false.
                    database?.userDao()
                        ?.update(false, entry?.id?.toInt() ?: 0)
                }
            }
        }.invokeOnCompletion {
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(
                    view?.context,
                    view?.context?.getString(R.string.delete_favorite),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    /**
     * Deletes every entry in the Room-Database. After the deletion,
     * it updates the empty database
     * with the default entries for the user.
     *
     * @author Alexander Lange
     * @since 1.5
     * @see AppDatabase
     */
    private fun deleteInternalDatabase() {
        scope_io.launch {
            Log.d("Test", "Lokale DB löschen.")
            //Delete all entries in the Room-Database
            database?.userDao()?.deleteTestPlanEntryAll()
            // Start Merlin Gürtler
            //Get the default entries for the user from the REST-API and store them in the room-database
            Log.d("TestCal", relativePPlanURL.toString())
            val retrofit = RetrofitConnect(relativePPlanURL ?: "")
            if (database != null && context != null && examineYear != null && currentExaminePeriod != null && currentTermin != null && serverAddress != null) {
                retrofit.RetrofitWebAccess(
                    context!!,
                    database!!,
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
                        view?.context,
                        view?.context?.getString(R.string.delete_db),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }

    /**
     * Sets the calendarsynchronization.
     *
     * @param[status] True->The calendar shall be synced with the app;False->The calendar shall not be synced.
     * @author Alexander Lange
     * @since 1.5
     * @see CheckGoogleCalendar
     */
    private fun setCalendarSynchronization(status: Boolean) {
        scope_io.launch {
            val mEditorGoogleCalendar = sharedPreferencesSettings?.edit()
            if (status) {
                mEditorGoogleCalendar?.clear()
                mEditorGoogleCalendar?.apply()
                response?.put("1")
                mEditorGoogleCalendar?.putString("calSync", response.toString())
                mEditorGoogleCalendar?.apply()
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
                if (!status) {
                    mEditorGoogleCalendar?.clear()?.apply()
                    mEditorGoogleCalendar?.remove("calSync")?.apply()
                }

            }
        }.invokeOnCompletion {
            Handler(Looper.getMainLooper()).post(object : Runnable {
                override fun run() {
                    Toast.makeText(
                        view?.context,
                        view?.context?.getString(R.string.add_calendar),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }

    }

    //TODO RENAME
    private fun doSomething() {

        val serverAdresse = view?.context?.getSharedPreferences("json8", Context.MODE_PRIVATE)
        response = JSONArray()
        val strServerAddress = serverAdresse?.getString("jsondata2", "0")
        //second parameter is necessary ie.,Value to return if this preference does not exist.
        if (strServerAddress != null) {
            try {
                response = JSONArray(strServerAddress)
            } catch (e: JSONException) {
                Log.d("Fehler Optionen", "Server-Adresse Fehler")
            }
        }
        var i = 0
        save = false
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
    }

    /**
     * Overrides the [onCreateOptionsMenu]-Method from the [Fragment]-Superclass.
     * Initializes the items for the actionmenu. In this case the save-button.
     * @param[menu] The menu where the items should be displayed.
     * @param[inflater] The [MenuInflater] to inflate the actionmenu.
     * @author Alexander Lange
     * @since 1.5
     * @see Fragment.onCreateOptionsMenu
     * @see Menu
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.findItem(R.id.menu_item_save).isVisible = true
        //Make Filter-Button invisible
        menu.findItem(R.id.menu_item_filter).isVisible = false
        super.onCreateOptionsMenu(menu, inflater)
    }

    /**
     * Overrides the [onOptionsItemSelected]-Method from the [Fragment]-Superclass.
     * Called when the user clicks an item in the actionmenu.
     * Determines what to do for each item.
     * @param[item] The item, which was clicked by the user.
     * @return Return false to allow normal menu processing to proceed, true to consume it here.
     * @author Alexander Lange
     * @since 1.5
     * @see Fragment.onOptionsItemSelected
     * @see MenuItem
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //Set behaviour for each clicked item.
        return when (item.itemId) {
            //When the user clicked the savebutton.
            R.id.menu_item_save -> {
                save()
                true
            }
            else -> false
        }
    }

    /**
     * Initializes the darkmode-[Switch]. Get the previous selection from sharedpreferences and
     * pass them to the darkmode-[Switch].
     * @author Alexander Lange
     * @since 1.5
     * @see Switch
     */
    private fun initDarkModeSwitch() {
        darkMode.isChecked = sharedPreferencesSettings?.getBoolean("darkmode", false) ?: false
    }

    /**
     * Initializes the theme-[Spinner]. Create a [Theme] for every implemented theme in
     * the styles.xml and passes them to a custom [ThemeAdapter], which is then passed to
     * the theme-[Spinner].
     * @author Alexander Lange
     * @since 1.5
     * @see Theme
     * @see ThemeAdapter
     * @see Spinner
     */
    private fun initThemeSpinner() {
        val themeList = mutableListOf<Theme>()
        Utils.themeList.forEach { x -> themeList.add(Theme(x, view)) }

        val adapter = view?.context?.let {
            ThemeAdapter(
                it,
                R.layout.layout_theme_spinner_row,
                themeList
            )
        }
        theme?.adapter = adapter
        val selectedPos: Int
        val themeid = sharedPreferencesSettings?.getInt("themeid", 0)
        selectedPos = Utils.themeList.indexOf(themeid)
        try {
            theme?.setSelection(selectedPos)
        } catch (ex: Exception) {

        }
        //TODO Alexander Lange End
    }

    /**
     * Save the current selected options and recreate the activity to change the theme and the darkmmode.
     *
     * @author Alexander Lange
     * @since 1.5
     */
    private fun save() {
        val editor = sharedPreferencesSettings?.edit()

        //Theme
        val position = theme.selectedItemPosition

        when (position) {
            1 -> editor?.putInt("themeid", R.style.Theme_AppTheme_2)?.apply()
            else -> editor?.putInt("themeid", R.style.Theme_AppTheme_1)?.apply()
        }

        //Darkmode
        editor?.putBoolean("darkmode", darkMode.isChecked)?.apply()

        activity?.recreate()
        //fragmentManager?.beginTransaction()?.detach(this)?.attach(this)?.commit()
    }

    /**
     * Overrides the onCreateView()-Method. It sets the current view to the optionfragmnet-layout.
     *
     * @return Returns the initialized view of this Fragment
     * @since 1.5
     * @author Alexander Lange (E-Mail:alexander.lange@fh-bielefeld.de)
     * @see Fragment.onCreateView
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.optionfragment, container, false)
        // Start Merlin Gürtler
        // Nun aus Shared Preferences

        return v
    }

    //TODO Implement?
    fun updatePlan(validation: String?) {
        PingUrl(serverAddress)
    }

    // Methode zum Aktualiseren der Prüfungen
    // die Abfrage Methodes des Webservers
    // gibt Mögliche Änderungen wie den Status zurück,
    // diese werden dann geupdated
    /**
     * Updates the data for the exams, currently stored in the Room-Database.
     * @author Alexander Lange
     * @since 1.5
     * @see RetrofitConnect
     */
    fun updateCheckPlan() {
        scope_io.launch {
            //Log.d("Test",String.valueOf(pruefplanDaten.size()));
            //aktuellerTermin, serverAddress, relativePPlanURL aus SharedPreferences

            //retrofit auruf
            val retrofit = RetrofitConnect(relativePPlanURL ?: "")
            if (context != null && database != null && examineYear != null && currentExaminePeriod != null && currentTermin != null && serverAddress != null) {
                retrofit.retroUpdate(
                    context!!,
                    database!!,
                    examineYear!!,
                    currentExaminePeriod!!,
                    currentTermin!!,
                    serverAddress
                )
            }
            // Log.d("Test3",String.valueOf(stringaufteilung[5]));
        }.invokeOnCompletion {
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(
                    context,
                    context!!.getString(R.string.add_favorite),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    //Verbindungsaufbau zum Webserver
    //Überprüfung ob Webserver erreichbar
    /**
     * Ping the webserver to check if there is a connection.
     * After success it updates the Room-Database.
     * @param[address] The address of the server to ping to.
     * @author Alexander Lange
     * @since 1.5
     * @see updateCheckPlan
     */
    fun PingUrl(address: String?) {
        scope_io.launch {
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
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(
                        context,
                        context!!.getString(R.string.noConnection),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    //Google Kalender einträge löschen
    /**
     * Deletes the entries in the googlecalendar.
     * @author Alexander Lange
     * @since 1.5
     * @see CheckGoogleCalendar
     */
    fun deleteCalendar() {
        val cal = CheckGoogleCalendar()
        cal.setCtx(context)
        cal.clearCal()
    }

    //Google Kalender aktualisieren
    /**
     * Updates the googlecalendar. Refreshes the favorites and removed favorites.
     * @author Alexander Lange
     * @since 1.5
     * @see CheckGoogleCalendar
     */
    fun updateCalendar() {
        scope_io.launch {
            val cal = CheckGoogleCalendar()
            cal.setCtx(context)
            cal.updateCal()
        }.invokeOnCompletion {
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(
                    view?.context,
                    view?.context?.getString(R.string.actualisation_calendar),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    /**
     * Put an event into the googlecalendar and returns the id.
     * @param[eventtitle] The exam to put into the googlecalendar.
     * @return The id of the event, which was created in the googlecalendar.
     * @author Alexander Lange
     * @since 1.5
     */
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