package com.Fachhochschulebib.fhb.pruefungsplaner

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.Fachhochschulebib.fhb.pruefungsplaner.StartClass
import android.view.ViewGroup
import android.view.LayoutInflater
import com.Fachhochschulebib.fhb.pruefungsplaner.R
import android.content.SharedPreferences
import android.view.View
import android.widget.Toast
import android.widget.TextView
import android.widget.CheckBox

//////////////////////////////
// CheckListAdapter
//
// autor:
// inhalt:  erstellt die Checkliste für die Wahl der Studiengänge
// zugriffsdatum: 01.10.20
//
//
//////////////////////////////
class CheckListAdapter     // Provide a suitable constructor (depends on the kind of dataset)
    (
    private val coursesList: List<String>,
    private val chosenList: MutableList<Boolean>,
    private val context: Context
) : RecyclerView.Adapter<CheckListAdapter.ViewHolder>() {
    var selectedCourse: String? = null
    private var globalVariable: StartClass? = null

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        // create a new view
        val inflater = LayoutInflater.from(
            parent.context
        )
        val v = inflater.inflate(R.layout.checkliste, parent, false)
        val vh: ViewHolder = ViewHolder(v)
        globalVariable = context as StartClass
        val sharedPreferencesSelectedCourse =
            context.getSharedPreferences("validation", Context.MODE_PRIVATE)
        selectedCourse = sharedPreferencesSelectedCourse.getString("selectedCourse", "0")
        return vh
    }

    private fun addFavorite(position: Int): Boolean {
        if (coursesList[position] != selectedCourse || globalVariable!!.isChangeFaculty) {
            chosenList[position] = !chosenList[position]
        } else {
            Toast.makeText(
                context, context.getString(R.string.favorite_main_course),
                Toast.LENGTH_SHORT
            ).show()
        }
        return chosenList[position]
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Initialisierung der Komponenten
        holder.nameCourse.text = coursesList[position]
        holder.checkBoxCourse.isChecked = chosenList[position]
        holder.checkBoxCourse.setOnClickListener { // Speichere die auswahl in der Liste
            holder.checkBoxCourse.isChecked = addFavorite(position)
        }
        holder.nameCourse.setOnClickListener { // Speichere die auswahl in der Liste
            holder.checkBoxCourse.isChecked = addFavorite(position)
        }
    }

    //Item anzahl
    override fun getItemCount(): Int {
        return coursesList.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    inner class ViewHolder internal constructor(v: View) : RecyclerView.ViewHolder(v) {
        // each data item is just a string in this case
        val nameCourse: TextView
        val checkBoxCourse: CheckBox

        init {
            nameCourse = v.findViewById<View>(R.id.courseName) as TextView
            checkBoxCourse = v.findViewById<View>(R.id.checkBox) as CheckBox
        }
    }
}