package com.Fachhochschulebib.fhb.pruefungsplaner

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import com.Fachhochschulebib.fhb.pruefungsplaner.R
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Handler
import com.Fachhochschulebib.fhb.pruefungsplaner.data.AppDatabase
import com.Fachhochschulebib.fhb.pruefungsplaner.data.TestPlanEntry
import com.Fachhochschulebib.fhb.pruefungsplaner.CheckGoogleCalendar
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import android.widget.TextView
import android.widget.LinearLayout
import java.lang.Exception

//////////////////////////////
// MyAdapterfavoriten für Recycleview
//
// autor:
// inhalt: Anzeigen der favorisierten Prüfungen in einzelnen Tabellen.
// zugriffsdatum: 11.12.19, Aug. 2020
//
//
//////////////////////////////
class MyAdapterfavorits     // Provide a suitable constructor (depends on the kind of dataset)
    (
    private val moduleAndCourseList: MutableList<String>,
    private val examinerAndSemester: List<String>,
    private val datesList: List<String>,
    private val ppIdList: List<String>,
    private val roomList: List<String>,
    private val formList: List<String>
) : RecyclerView.Adapter<MyAdapterfavorits.ViewHolder>() {
    private var modulName: String? = null
    private var name: String? = null
    private var context: Context? = null
    fun add(position: Int, item: String) {
        moduleAndCourseList.add(position, item)
        notifyItemInserted(position)
    }

    fun remove(position: Int) {
        moduleAndCourseList.removeAt(position)
        notifyItemRemoved(position)
        deleteItemThread(position)
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
        val v = inflater.inflate(R.layout.favoriten, parent, false)
        // set the view's size, margins, paddings and layout parameters
        val vh: ViewHolder = ViewHolder(v)

        // Merlin Gürtler für den globalen Context
        context = parent.context
        return vh
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(
        holder: ViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        name = moduleAndCourseList[holder.adapterPosition]
        holder.txtHeader.text = name

        //Prüfitem von der Favoritenliste löschen
        holder.ivicon.setOnClickListener { remove(position) }
        holder.txtFooter.text = context!!.getString(R.string.prof) + examinerAndSemester[position]
        name = moduleAndCourseList[position]
        val course = name?.split(" ")?.toTypedArray()
        modulName = ""
        var b: Int
        b = 0
        while (b < (course?.size?:0) - 1) {
            modulName = modulName + " " + (course?.get(b) ?: "")
            b++
        }

        // Start Merlin Gürtler
        // erhalte den ausgewählten Studiengang
        val sharedPreferencesCourse =
            context?.getSharedPreferences("validation", Context.MODE_PRIVATE)
        val selectedCourse = sharedPreferencesCourse?.getString("selectedCourse", "0")
            ?.split(" ")?.toTypedArray()
        val colorElectiveModule = "#7FFFD4"
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

        // Ende Merlin Gürtler

        //darstellen der Informationen für das Prüfitem
        val splitDateAndTime = datesList[position].split(" ").toTypedArray()
        val splitDayMonthYear = splitDateAndTime[0].split("-").toTypedArray()
        holder.txtthirdline.text = (context!!.getString(R.string.clockTime2)
                + splitDateAndTime[1].substring(0, 5)
                + context!!.getString(R.string.date2)
                + splitDayMonthYear[2] + "."
                + splitDayMonthYear[1] + "."
                + splitDayMonthYear[0])
        val splitExaminerAndSemester = examinerAndSemester[position].split(" ").toTypedArray()
        holder.txtFooter.text = (context!!.getString(R.string.prof)
                + splitExaminerAndSemester[0] + ", " + splitExaminerAndSemester[1]
                + context!!.getString(R.string.semester) + splitExaminerAndSemester[2])
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return moduleAndCourseList.size
    }

    fun giveString(position: Int): String {
        try {
            val name = moduleAndCourseList[position]
            val course = name.split(" ").toTypedArray()
            modulName = ""
            var b: Int
            b = 0
            while (b < course.size - 1) {
                modulName = modulName + " " + course[b]
                b++
            }
            val division1 = datesList[position].split(" ").toTypedArray()
            val division2 = division1[0].split("-").toTypedArray()
            //holder.txtthirdline.setText("Uhrzeit: " + aufteilung1[1].substring(0, 5).toString());
            val sa = examinerAndSemester[position].split(" ").toTypedArray()
            //AlertDialog.Builder builder1 = new AlertDialog.Builder(v.getContext());
            return """${context!!.getString(R.string.information)}${context!!.getString(R.string.course)}${course[course.size - 1]}${
                context!!.getString(
                    R.string.modul
                )
            }$modulName${context!!.getString(R.string.firstProf)}${sa[0]}${context!!.getString(R.string.secondProf)}${sa[1]}${
                context!!.getString(
                    R.string.date
                )
            }${division2[2]}.${division2[1]}.${division2[0]}${context!!.getString(R.string.clockTime)}${
                division1[1].substring(
                    0,
                    5
                )
            }${context!!.getString(R.string.clock)}${context!!.getString(R.string.room)}${roomList[position]}${
                context!!.getString(
                    R.string.form
                )
            }${formList[position]}
 
 
 
 
 
 
 """
        } catch (e: Exception) {
            Log.d(
                "Fehler Adapterfavorits",
                "Fehler bei ermittlung der weiteren Informationen"
            )
        }
        return "0" //????
    }

    // Start Merlin Gürtler
    // da die Funktion mehrmals genutzt wird ausglagern in Funktion
    private fun deleteItemThread(position: Int) {
        //TODO CHANGE TO COROUTINE
        Thread {
            val database = AppDatabase.getAppDatabase(context)
            val ppeList = database.userDao().getFavorites(true)
            // second parameter is necessary ie.,
            // Value to return if this preference does not exist.
            for (entry in ppeList) {
                if (entry.id == ppIdList[position]) {
                    database.userDao()
                        .update(false, Integer.valueOf(ppIdList[position]))

                    //Entferne den Eintrag aus dem Calendar falls vorhanden
                    val cal = CheckGoogleCalendar()
                    cal.setCtx(context)
                    if (!cal.checkCal(ppIdList[position].toInt())) {
                        cal.deleteEntry(ppIdList[position].toInt())
                    }
                }
            }
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(
                    context,
                    context!!.getString(R.string.delete), Toast.LENGTH_SHORT
                ).show()
            }
        }.start()
    }

    // Ende Merlin Gürtler
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    inner class ViewHolder(var layout: View) : RecyclerView.ViewHolder(
        layout
    ) {
        // each data item is just a string in this case
        var txtHeader: TextView
        var txtFooter: TextView
        var txtthirdline: TextView
        var ivicon: ImageView
        var layout2: LinearLayout

        init {
            ivicon = layout.findViewById<View>(R.id.icon) as ImageView
            txtHeader = layout.findViewById<View>(R.id.firstLine) as TextView
            txtFooter = layout.findViewById<View>(R.id.secondLine) as TextView
            txtthirdline = layout.findViewById<View>(R.id.thirdLine) as TextView
            layout2 = layout.findViewById<View>(R.id.linearLayout) as LinearLayout
            //button.setLayoutParams(new LinearLayout.LayoutParams(
            // LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        }
    }
}