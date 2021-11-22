package com.Fachhochschulebib.fhb.pruefungsplaner

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import com.Fachhochschulebib.fhb.pruefungsplaner.R
import android.content.SharedPreferences
import android.graphics.drawable.GradientDrawable
import com.Fachhochschulebib.fhb.pruefungsplaner.data.AppDatabase
import com.Fachhochschulebib.fhb.pruefungsplaner.data.TestPlanEntry
import android.os.Looper
import com.Fachhochschulebib.fhb.pruefungsplaner.MyAdapter
import com.Fachhochschulebib.fhb.pruefungsplaner.CheckGoogleCalendar
import android.content.ContentValues
import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.provider.CalendarContract
import android.util.Log
import android.view.View
import android.widget.*
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
class MyAdapter// private Intent calIntent;     // Provide a suitable constructor (depends on the kind of dataset)
    (
    var modules: MutableList<String>,
    private val examinerAndSemester: List<String>,
    private val date: List<String>,
    private val moduleList: List<String>,
    private val planId: List<String>,
    private val examForm: List<String>,
    mLayout: RecyclerView.LayoutManager?,
    private val roomAdapter: List<String>,
    private val statusHintList: List<String>
) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {
    private var save = false
    private var moduleName: String? = null
    private var context: Context? = null
    private var calDate = GregorianCalendar()

    fun add(position: Int, item: String) {
        modules.add(position, item)
        notifyItemInserted(position)
    }

    fun remove(position: Int) {
        modules.removeAt(position)
        notifyItemRemoved(position)
    }

    // Create new views (invoked by the layout manager)
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
        return ViewHolder(v)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            val name = modules[position]

            // Start Merlin Gürtler
            // erhalte den ausgewählten Studiengang
            val sharedPreferencesSelectedCourse =
                context?.getSharedPreferences("validation", Context.MODE_PRIVATE)
            val selectedCourse = sharedPreferencesSelectedCourse?.getString("selectedCourse", "0")
                ?.split(" ")?.toTypedArray()
            val colorElectiveModule = "#7FFFD4"
            val course = name.split(" ").toTypedArray()

            // Ende Merlin Gürtler
            if (selectedCourse?.get(selectedCourse.size - 1) != course?.get(course.size - 1)) {
                // Lege die Farben für die Wahlmodule fest
                val backGroundGradient = GradientDrawable(
                    GradientDrawable.Orientation.TOP_BOTTOM, intArrayOf(
                        Color.parseColor(colorElectiveModule),
                        Color.parseColor(colorElectiveModule)
                    )
                )
                backGroundGradient.cornerRadius = 40f
                val sdk = Build.VERSION.SDK_INT
                if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                    holder.layout.setBackgroundDrawable(backGroundGradient)
                } else {
                    holder.layout.background = backGroundGradient
                }
            }
            //TODO Change to COROUTINE
            Thread {
                moduleName = ""
                for (b in 0 until course.size - 1) {
                    moduleName = moduleName + " " + course[b]
                }

                //Datenbank und Pruefplan laden
                val database = AppDatabase.getAppDatabase(context!!)
                val selectedEntry = database?.userDao()?.getEntryById(planId[position])

                // Überprüfung, ob Prüfitem favorisiert wurde
                //  Toast.makeText(v.getContext(),String.valueOf(userdaten.size()),
                //                  Toast.LENGTH_SHORT).show();
                save = false
                Handler(Looper.getMainLooper()).post {
                    try {
                        if (position >= 0) {
                            Log.d("Handler", position.toString())//TODO REMOVE
                            Log.d("Handler", planId.size.toString())
                            //planId.forEach { Log.d("Handler",it.toString()) }
                            val pruefid = planId[position]?.toInt()
                            if (Integer.valueOf(selectedEntry?.id) == pruefid) {
                                // Start Merlin Gürtler
                                // Setze die Farbe des Icons
                                holder.statusIcon.setColorFilter(Color.parseColor(selectedEntry?.color))

                                //if (eintrag.getStatus().equals("final")) {
                                //    holder.statusIcon.setColorFilter(Color.parseColor("#228B22"));
                                //}
                                // Ende Merlin Gürtler
                                if (selectedEntry?.favorit == true) {
                                    holder.ivicon.setColorFilter(Color.parseColor("#06ABF9"))
                                    // Toast.makeText(v.getContext(), "129", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    } catch (ex: Exception) {
                        Log.d("onBindViewer-ThreadHandler", ex.stackTraceToString())
                    }

                }
            }.start()
            holder.txtHeader.text = name

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
                Thread {
                    val isFavorite = checkFavorite(position)
                    // toggelt den Favoriten
                    if (!isFavorite) {
                        addToFavorites(position, holder)
                    } else {
                        deleteFromFavorites(position, holder)
                    }
                }.start()
            }

            //Aufteilung nach verschiedenen Tagen
            val splitDay = date[position].split(" ").toTypedArray()
            if (position > 0) {
                val splitDayBefore = date[position - 1].split(" ").toTypedArray()

                //Vergleich der beiden Tage
                //wenn ungleich, dann blaue box mit Datumseintrag
                if (splitDay[0] == splitDayBefore[0]) {
                    holder.button.height = 0
                }
            }

            //Darstellen der Werte in der Prüfitem Komponente
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
        } catch (ex: Exception) {
            Log.d("MyAdapter.kt-onBindViewHolder",ex.stackTraceToString())
        }

    }

    //Methode zum Darstellen der "weiteren Informationen"
    fun giveString(position: Int): String {
        val name = modules[position]
        val course = name.split(" ").toTypedArray()
        moduleName = ""
        var b: Int
        b = 0
        while (b < course.size - 1) {
            moduleName = moduleName + " " + course[b]
            b++
        }
        val room2 = roomAdapter[position]
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
        }${context!!.getString(R.string.clock)}${context!!.getString(R.string.room)}$room2${
            context!!.getString(
                R.string.form
            )
        }${examForm[position]}
 
 
 
 
 
 """
    }

    fun deleteFromFavorites(position: Int, holder: ViewHolder) {
        //TODO Change to COROUTINE
        Thread {
            favcheck = false

            //Datenbank und Pruefplan laden
            val database = AppDatabase.getAppDatabase(context!!)
            val selectedEntry = database?.userDao()?.getEntryById(planId[position])

            //Überprüfung ob Prüfitem Favorisiert wurde und angeklickt
            //Toast.makeText(v.getContext(),String.valueOf(userdaten.size()),
            // Toast.LENGTH_SHORT).show();
            if (selectedEntry?.favorit == true) {
                database?.userDao()
                    ?.update(false, planId[position].toInt())
            }
            Handler(Looper.getMainLooper()).post {
                // Start Merlin Gürtler
                //Entferne den Eintrag aus dem Calendar falls vorhanden
                val cal = CheckGoogleCalendar()
                cal.setCtx(context)
                if (!cal.checkCal(planId[position].toInt())) {
                    cal.deleteEntry(planId[position].toInt())
                }
                holder.ivicon.clearColorFilter()
                Toast.makeText(context, context!!.getString(R.string.delete), Toast.LENGTH_SHORT)
                    .show()
                // Ende Merlin Gürtler
            }
        }.start()
    }

    fun addToFavorites(position: Int, holder: ViewHolder) {
        //TODO CHANGE TO COROUTINE
        Thread {
            favcheck = false

            //Datenbank und Pruefplan laden
            val database = AppDatabase.getAppDatabase(context!!)
            val selectedEntry = database?.userDao()?.getEntryById(planId[position])

            //Überprüfung ob Prüfitem Favorisiert wurde und angeklickt
            //Toast.makeText(v.getContext(),String.valueOf(userdaten.size()),
            // Toast.LENGTH_SHORT).show();

            //Speichern des Prüfitem als Favorit
            // Toast.makeText(v.getContext(), "137", Toast.LENGTH_SHORT).show();
            if (selectedEntry?.favorit == false) {
                database?.userDao()
                    ?.update(true, planId[position].toInt())
            }
            Handler(Looper.getMainLooper()).post {
                val pruefid = planId[position].toInt()
                if (Integer.valueOf(selectedEntry?.id) == pruefid) {
                    holder.ivicon.setColorFilter(Color.parseColor("#06ABF9"))
                    // Toast.makeText(v.getContext(), "129", Toast.LENGTH_SHORT).show();
                }

                //Speichern des Prüfitem als Favorit
                // Toast.makeText(v.getContext(), "137", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(context, context!!.getString(R.string.add), Toast.LENGTH_SHORT)
                    .show()
            }
        }.start()
    }

    fun checkFavorite(position: Int): Boolean {
        favcheck = false

        //Datenbank und Pruefplan laden
        val database = AppDatabase.getAppDatabase(context!!)
        val selectedEntry = database?.userDao()?.getEntryById(planId[position])

        //Überprüfung ob Prüfitem Favorisiert wurde und angeklickt
        //Toast.makeText(v.getContext(),String.valueOf(userdaten.size()),
        // Toast.LENGTH_SHORT).show();

        // Toast.makeText(v.getContext(), "129", Toast.LENGTH_SHORT).show();
        save = selectedEntry?.favorit == true
        return save
    }

    //Item anzahl
    override fun getItemCount(): Int {
        return modules.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    inner class ViewHolder internal constructor(v: View) : RecyclerView.ViewHolder(v) {
        // each data item is just a string in this case
        val txtHeader: TextView
        val txtFooter: TextView
        val txtthirdline: TextView
        var layout: LinearLayout
        var bigLayout: LinearLayout
        val ivicon: ImageView
        val statusIcon: ImageView
        val button: Button

        init {
            ivicon = v.findViewById<View>(R.id.icon) as ImageView
            statusIcon = v.findViewById<View>(R.id.icon2) as ImageView
            txtHeader = v.findViewById<View>(R.id.firstLine) as TextView
            txtFooter = v.findViewById<View>(R.id.secondLine) as TextView
            val txtSecondScreen = v.findViewById<View>(R.id.txtSecondscreen) as TextView
            txtthirdline = v.findViewById<View>(R.id.thirdLine) as TextView
            button = v.findViewById<View>(R.id.button7) as Button

            //button.setLayoutParams(new LinearLayout.LayoutParams(
            //    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            layout = v.findViewById<View>(R.id.linearLayout) as LinearLayout
            bigLayout = v.findViewById<View>(R.id.linearLayout6) as LinearLayout
        }
    }

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
}