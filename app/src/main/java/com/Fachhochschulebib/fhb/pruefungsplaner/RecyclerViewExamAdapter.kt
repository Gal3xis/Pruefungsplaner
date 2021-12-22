package com.Fachhochschulebib.fhb.pruefungsplaner

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import com.Fachhochschulebib.fhb.pruefungsplaner.data.AppDatabase
import android.os.Looper
import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Handler
import android.provider.CalendarContract
import android.util.Log
import android.view.View
import android.widget.*
import com.Fachhochschulebib.fhb.pruefungsplaner.data.TestPlanEntry
import kotlinx.coroutines.*
import java.lang.Exception
import java.util.*

//////////////////////////////
// MyAdapter Recycleview
//
// autor:
// inhalt:  unterteilung von allen Prüfungen in einzelne tabellen und darstellung
// zugriffsdatum: 11.12.19
//
//
//////////////////////////////
/**
 * The [RecyclerView.Adapter] for the [RecyclerView] that holds information about all exams.
 * The information is stored in multiple [List]-Objects, each holding one kind of information for every exam that
 * needs to be displayed. E.g. the exam at position 1 gets his information from every list at index 1.
 *
 * @author Alexander Lange
 * @since 1.5
 * @see RecyclerView.Adapter
 * @see RecyclerView
 */
class RecyclerViewExamAdapter    // Provide a suitable constructor (depends on the kind of dataset)
    (
    var modules: MutableList<String>,
    private val examinerAndSemester: List<String>,
    private val date: List<String>,
    private val moduleList: List<String>,
    private val planId: List<String>,
    private val examForm: List<String>,
    mLayout: RecyclerView.LayoutManager?,
    private val room: List<String>,
    private val statusHintList: List<String>
) : RecyclerView.Adapter<RecyclerViewExamAdapter.ViewHolder>() {

    private val scopeIO = CoroutineScope(CoroutineName("IO-Scope") + Dispatchers.IO)

    private var database: AppDatabase? = null

    private var sharedPreferencesValidation: SharedPreferences? = null


    private var save = false
    private var moduleName: String? = null
    private var context: Context? = null
    private var calDate = GregorianCalendar()

    // Create new views (invoked by the layout manager)
    /**
     * Inflates the view that shows the information for the passed viewType. In this case the information
     * about the exam.
     *
     * @param[parent] The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param[viewType] The view type of the new View.
     * @return The [ViewHolder], that shows the information.
     *
     * @author Alexander Lange
     * @since 1.5
     *
     * @see RecyclerView.Adapter.onCreateViewHolder
     */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        // create a new view
        val inflater = LayoutInflater.from(
            parent.context
        )
        val v = inflater.inflate(R.layout.termine, parent, false)
        context = v.context

        context?.let { database = AppDatabase.getAppDatabase(it) }

        sharedPreferencesValidation =
            context?.getSharedPreferences("validation", Context.MODE_PRIVATE)

        return ViewHolder(v)
    }

    // Replace the contents of a view (invoked by the layout manager)
    /**
     * Initializes the [ViewHolder] with information of the viewtype. In this case,
     * passes the examinformation to the UI-Elements.
     *
     * @param[holder] The ViewHolder which should be updated to represent the contents of the item at the given position in the data set.
     * @param[position] The position of the item within the adapter's data set.
     *
     * @author Alexander Lange
     * @since 1.5
     *
     * @see RecyclerView.Adapter.onBindViewHolder
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            val name = modules[position]
            holder.txtHeader.text = name

            // Start Merlin Gürtler
            // erhalte den ausgewählten Studiengang
            val course = name.split(" ").toTypedArray()

            setIcons(position, holder)
            //Aufteilung nach verschiedenen Tagen
            val splitDay = splitInDays(position, holder)

            //Darstellen der Werte in der Prüfitem Komponente
            initFooter(splitDay, holder, position)
        } catch (ex: Exception) {
            Log.d("MyAdapter.kt-onBindViewHolder", ex.stackTraceToString())
        }

    }

    /**
     * Initializes the UI-Elements, that hold deeper information about the exam.
     *
     * @param[splitDay] The day, the exam takes place.
     * @param[holder] The [ViewHolder] that holds the UI-Elements.
     * @param[position] The position of the item in the Recyclerview.
     *
     * @author Alexander Lange
     * @since 1.5
     */
    private fun initFooter(
        splitDay: Array<String>,
        holder: ViewHolder,
        position: Int
    ) {
        val splitMonthDayYear = splitDay[0].split("-").toTypedArray()
        holder.txtthirdline.text =
            context!!.getString(R.string.time) + splitDay[1].substring(0, 5)
        holder.button.text = (splitMonthDayYear[2] + "."
                + splitMonthDayYear[1] + "."
                + splitMonthDayYear[0])
        val splitExaminerAndSemester = examinerAndSemester[position].split(" ").toTypedArray()
        holder.txtFooter.text = (context!!.getString(R.string.prof)
                + splitExaminerAndSemester[0] + ", "
                + splitExaminerAndSemester[1]
                + context!!.getString(R.string.semester) + splitExaminerAndSemester[2])
        //holder.txtthirdline.setText("Semester: " + Semester5.toString());
    }

    /**
     * Tests if the exam before that takes place at the same time as itself.
     * If that is the case, it removes the date-indicator, so its placed under the indicator
     * of the first exam that day.
     *
     * @param[holder] The [ViewHolder] that holds the UI-Elements.
     * @param[position] The position of the item in the Recyclerview.
     * @return The day the exam takes place.
     * @author Alexander Lange
     * @since 1.5
     */
    private fun splitInDays(
        position: Int,
        holder: ViewHolder
    ): Array<String> {
        val splitDay = date[position].split(" ").toTypedArray()
        if (position > 0) {
            val splitDayBefore = date[position - 1].split(" ").toTypedArray()

            //Vergleich der beiden Tage
            //wenn ungleich, dann blaue box mit Datumseintrag
            if (splitDay[0] == splitDayBefore[0]) {
                holder.button.height = 0
            }
        }
        return splitDay
    }

    /**
     * Sets the icons for the items. Determines, the status of the exam an whether it is a favorit
     * or not and sets the corresponding colors.
     *
     * @param[holder] The [ViewHolder] that holds the UI-Elements.
     * @param[position] The position of the item in the Recyclerview.
     *
     * @author Alexander Lange
     * @since 1.5
     */
    private fun setIcons(
        position: Int,
        holder: ViewHolder
    ) {
        var selectedEntry: TestPlanEntry? = null
        scopeIO.launch {
            try {
                selectedEntry = database?.userDao()?.getEntryById(planId[position])
                //Datenbank und Pruefplan laden
                save = false
            } catch (ex: Exception) {
                Log.d("MyAdapter.kt-onBindViewHolder", ex.stackTraceToString())
            }
        }.invokeOnCompletion {
            Handler(Looper.getMainLooper()).post {
                try {
                    if (position < 0) {
                        return@post
                    }
                    val pruefid = planId[position]?.toInt()
                    if (Integer.valueOf(selectedEntry?.id) != pruefid) {
                        return@post
                    }
                    // Start Merlin Gürtler
                    // Setze die Farbe des Icons
                    if (context != null) {
                        holder.statusIcon.setColorFilter(
                            Utils.getColorFromAttr(
                                Utils.statusColors[selectedEntry?.status]
                                    ?: R.attr.defaultStatusColor, context!!.theme
                            )
                        )
                        holder.ivicon.setImageDrawable(context!!.resources.getDrawable(Utils.favoritIcons[selectedEntry?.favorit?:false]!!,context!!.theme))
                    }
                } catch (ex: Exception) {
                    Log.d("onBindViewer-ThreadHandler", ex.stackTraceToString())
                }
            }
        }
        //OnClickListener
        // Start Merlin Gürtler
        // Gibt die Statusmeldung aus
        holder.statusIcon.setOnClickListener { v: View ->
            Toast.makeText(
                v.context,
                statusHintList[position],
                Toast.LENGTH_SHORT
            ).show()
        }
        // Ende Merlin Gürtler
        holder.ivicon.setOnClickListener { v: View? ->
            scopeIO.launch {
                val isFavorite = checkFavorite(position)
                // toggelt den Favoriten
                if (!isFavorite) {
                    addToFavorites(position, holder)
                } else {
                    deleteFromFavorites(position, holder)
                }
            }
            // Ende Merlin Gürtler
        }
    }

    /**
     * Returns a string that shows the extended information of the exam.
     *
     * @param[position] The position of the item in the Recyclerview.
     * @return The string that contains the information
     *
     * @author Alexander Lange
     * @since 1.5
     */
    //Methode zum Darstellen der "weiteren Informationen"
    fun giveString(position: Int): String {
        try {
            val name = modules[position]
            val course = name.split(" ").toTypedArray()
            moduleName = ""
            var b: Int
            b = 0
            while (b < course.size - 1) {
                moduleName = moduleName + " " + course[b]
                b++
            }
            val room = room[position]
            val division1 = date[position].split(" ").toTypedArray()
            val division2 = division1[0].split("-").toTypedArray()
            //holder.txtthirdline.setText("Uhrzeit: " + aufteilung1[1].substring(0, 5).toString());
            val sa = examinerAndSemester[position].split(" ").toTypedArray()

            //String mit dem Inhalt für weitere Informationen
            return """${context!!.getString(R.string.information)}${context!!.getString(R.string.course)}${course[course.size - 1]}${
                context!!.getString(
                    R.string.modul
                )
            }$moduleName${context!!.getString(R.string.firstProf)}${sa[0]}${context!!.getString(R.string.secondProf)}${sa[1]}${
                context!!.getString(
                    R.string.date
                )
            }${division2[2]}.${division2[1]}.${division2[0]}${context!!.getString(R.string.clockTime)}${
                division1[1].substring(
                    0,
                    5
                )
            }${context!!.getString(R.string.clock)}${context!!.getString(R.string.room)}$room${
                context!!.getString(
                    R.string.form
                )
            }${examForm[position]}
 
 
 
 
 
 """
        } catch (ex: Exception) {
            Log.e("MyAdapter.kt-giveStrtng:", ex.stackTraceToString())
            return ""
        }
    }

    /**
     * Deletes the exam from favorits. Executed after clicking the add/remove-icon.
     *
     * @param[holder] The [ViewHolder] that holds the UI-Elements.
     * @param[position] The position of the item in the Recyclerview.
     *
     * @author Alexander Lange
     * @since 1.5
     */
    fun deleteFromFavorites(position: Int, holder: ViewHolder) {
        var selectedEntry:TestPlanEntry? = null
        scopeIO.launch {
            favcheck = false
            selectedEntry = database?.userDao()?.getEntryById(planId[position])
            //Überprüfung ob Prüfitem Favorisiert wurde und angeklickt
            if (selectedEntry?.favorit == true) {
                database?.userDao()
                    ?.update(false, planId[position].toInt())
            }
        }.invokeOnCompletion {
            Handler(Looper.getMainLooper()).post {
                // Start Merlin Gürtler
                //Entferne den Eintrag aus dem Calendar falls vorhanden
                val cal = CheckGoogleCalendar()
                cal.setCtx(context)
                if (!cal.checkCal(planId[position].toInt())) {
                    cal.deleteEntry(planId[position].toInt())
                }

                Toast.makeText(context, context!!.getString(R.string.delete), Toast.LENGTH_SHORT)
                    .show()
                // Ende Merlin Gürtler
                this.notifyDataSetChanged()
            }
        }
    }

    /**
     * Adds the exam to the favorits. Executed after clicking the add/remove-icon.
     *
     * @param[holder] The [ViewHolder] that holds the UI-Elements.
     * @param[position] The position of the item in the Recyclerview.
     *
     * @author Alexander Lange
     * @since 1.5
     */
    fun addToFavorites(position: Int, holder: ViewHolder) {
        var selectedEntry: TestPlanEntry? = null
        scopeIO.launch {
            favcheck = false
            selectedEntry = database?.userDao()?.getEntryById(planId[position])
            //Speichern des Prüfitem als Favorit
            // Toast.makeText(v.getContext(), "137", Toast.LENGTH_SHORT).show();
            if (selectedEntry?.favorit == false) {
                database?.userDao()
                    ?.update(true, planId[position].toInt())
            }
        }.invokeOnCompletion {
            Handler(Looper.getMainLooper()).post {

                //Speichern des Prüfitem als Favorit

                SaveInCalendar(position, holder)

                Toast.makeText(context, context!!.getString(R.string.add), Toast.LENGTH_SHORT)
                    .show()
                this.notifyDataSetChanged()
            }
        }

    }

    /**
     * Transfers the exam to the google calendar if necessary.
     *
     * @param[holder] The [ViewHolder] that holds the UI-Elements.
     * @param[position] The position of the item in the Recyclerview.
     *
     * @author Alexander Lange
     * @since 1.5
     */
    private fun SaveInCalendar(
        position: Int,
        holder: ViewHolder
    ) {
        //Überprüfung ob Pürfungen zum Google Kalender Hinzugefügt werden sollen
        val GoogleCalenderWert = context?.getSharedPreferences("json8", 0)
        //Creating editor to store uebergebeneModule to shared preferences
        val googleCalendarEditor = GoogleCalenderWert?.edit()
        googleCalendarEditor?.apply()
        val checkGoogleCalendar = GoogleCalenderWert?.getString("jsondata2", "0")


        // Überprüfung des Wertes, wenn strJson2 "true" ist dann ist der
        // Google Kalender aktiviert
        var save2 = false
        for (counter in 0 until checkGoogleCalendar?.length!!) {
            val ss1 = checkGoogleCalendar[counter].toString()
            if (ss1 == 1.toString()) {
                save2 = true
            }
        }

        //Hinzufügen der Prüfungen zum Google Kalender
        val checkEntry = CheckGoogleCalendar()
        checkEntry.setCtx(context)

        //Abfrage des geklickten Items
        if (checkEntry.checkCal(planId[position].toInt())) {
            if (save2) {

                //Ermitteln benötigter Variablen
                val splitDateAndTime = date[position].split(" ").toTypedArray()
                val splitDayMonthYear = splitDateAndTime[0].split("-").toTypedArray()
                holder.txtthirdline.text = (context!!.getString(R.string.time)
                        + splitDateAndTime[1].substring(0, 5))
                holder.button.text = (splitDayMonthYear[2] + "."
                        + splitDayMonthYear[1] + "."
                        + splitDayMonthYear[0])
                val sa = examinerAndSemester[position].split(" ").toTypedArray()
                holder.txtFooter.text =
                    (context!!.getString(R.string.prof) + sa[0] + ", " + sa[1]
                            + context!!.getString(R.string.semester) + sa[2])
                val name1 = modules[position]
                val modulname1 = name1.split(" ").toTypedArray()
                moduleName = ""
                var b: Int
                b = 0
                while (b < modulname1.size - 1) {
                    moduleName = moduleName + " " + modulname1[b]
                    b++
                }
                val timeStart = splitDateAndTime[1].substring(0, 2).toInt()
                val timeEnd = splitDateAndTime[1].substring(4, 5).toInt()
                calDate = GregorianCalendar(
                    splitDayMonthYear[0].toInt(),
                    splitDayMonthYear[1].toInt() - 1, splitDayMonthYear[2].toInt(),
                    timeStart, timeEnd
                )

                //Methode zum Speichern im Kalender
                val calendarid = calendarID(moduleName)

                //Funktion im Google-Kalender, um PrüfID und calenderID zu speichern
                checkEntry.insertCal(
                    planId[position].toInt(),
                    calendarid
                )
            }
        }
    }

    /**
     * Checks if an exam is a favorit.
     * Needs to be called in a Coroutine-Scope.
     *
     * @param[position] The position of the item in the Recyclerview.
     *
     * @return true->The exam is a favorit;false->The exam is not a favorit.
     *
     * @author Alexander Lange
     * @since 1.5
     */
    fun checkFavorite(position: Int): Boolean {
        try {
            favcheck = false
            val selectedEntry = database?.userDao()?.getEntryById(planId[position])

            //Überprüfung ob Prüfitem Favorisiert wurde und angeklickt

            return selectedEntry?.favorit?:false
        } catch (ex: Exception) {
            Log.e("MyAdapter.kt-checkFavorite:", ex.stackTraceToString())
            return false
        }
    }

    /**
     * Returns the amount of items in the recyclerview, based on the size of the moduleslist.
     *
     * @return The amount of items in the recyclerview.
     *
     * @author Alexander Lange
     * @since 1.5
     *
     * @see RecyclerView.Adapter.getItemCount
     */
    override fun getItemCount(): Int {
        return modules.size
    }

    /**
     * Return the view type of the item at position for the purposes of view recycling.
     * The default implementation of this method returns 0, making the assumption of a single view type for the adapter.
     * Unlike ListView adapters, types need not be contiguous.
     * Consider using id resources to uniquely identify item view types.
     *
     * @param[position] Position to query
     *
     * @return integer value identifying the type of the view needed to represent the item at position.
     * Type codes need not be contiguous.
     *
     * @author Alexander Lange
     * @since 1.5
     *
     * @see RecyclerView.Adapter.getItemViewType
     */
    override fun getItemViewType(position: Int): Int {
        return position
    }

    /**
     * Used to put an event into the google calendar.
     *
     * @param[eventtitle] The title of the event.
     *
     * @return The id of the event in the google calendar.
     *
     * @author Alexander Lange
     * @since 1.5
     */
    fun calendarID(eventtitle: String?): Int {
        val event = ContentValues()
        event.put(CalendarContract.Events.CALENDAR_ID, 2)
        event.put(CalendarContract.Events.TITLE, moduleName)
        event.put(CalendarContract.Events.DESCRIPTION, context!!.getString(R.string.fh_name))
        event.put(CalendarContract.Events.DTSTART, calDate.timeInMillis)
        event.put(CalendarContract.Events.DTEND, calDate.timeInMillis + 90 * 60000)
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
                if (calName != null && calName.contains(eventtitle!!)) {
                    result = calID.toInt()
                }
            } while (cursor.moveToNext())
            cursor.close()
        }
        return result
    }

    companion object {
        var favcheck = true
    }

    // Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
    /**
     * Inner class [ViewHolder], that contains the references to the UI-Elements.
     *
     * @author Alexander Lange
     * @since 1.5
     *
     * @see RecyclerView.ViewHolder
     */
    inner class ViewHolder internal constructor(v: View) : RecyclerView.ViewHolder(v) {
        // each data item is just a string in this case
        val txtHeader: TextView
        val txtFooter: TextView
        val txtthirdline: TextView
        val layout: LinearLayout
        val bigLayout: LinearLayout
        val ivicon: ImageView
        val statusIcon: ImageView
        val button: Button
        val txtSecondScreen: TextView

        init {
            ivicon = v.findViewById<View>(R.id.icon) as ImageView
            statusIcon = v.findViewById<View>(R.id.icon2) as ImageView
            txtHeader = v.findViewById<View>(R.id.firstLine) as TextView
            txtFooter = v.findViewById<View>(R.id.secondLine) as TextView
            txtSecondScreen = v.findViewById<View>(R.id.txtSecondscreen) as TextView
            txtthirdline = v.findViewById<View>(R.id.thirdLine) as TextView
            button = v.findViewById<View>(R.id.button7) as Button

            //button.setLayoutParams(new LinearLayout.LayoutParams(
            //    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            layout = v.findViewById<View>(R.id.linearLayout) as LinearLayout
            bigLayout = v.findViewById<View>(R.id.linearLayout6) as LinearLayout
        }
    }


}