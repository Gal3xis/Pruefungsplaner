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
/**
 * Adapter-Class for the Recyclerview in the AddCourseFragment-Class
 *
 * @author Alexander Lange (Email:alexander.lange@fh-bielefeld.de)
 * @since 1.5
 *
 * @see AddCourseFragment
 * @see RecyclerView.Adapter
 */
class CheckListAdapter     // Provide a suitable constructor (depends on the kind of dataset)
    (
    private val coursesList: List<String>,
    private val chosenList: MutableList<Boolean>,
    private val context: Context
) : RecyclerView.Adapter<CheckListAdapter.ViewHolder>() {
    var selectedCourse: String? = null
    private var globalVariable: StartClass? = null

    /**
     * Called when Recyclerview needs a new ViewHolder.
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return A new ViewHolder that holds a View of the given view type.
     *
     * @author Alexander Lange
     * @since 1.5
     *
     * @see RecyclerView
     * @see RecyclerView.Adapter
     * @see RecyclerView.Adapter.onCreateViewHolder
     */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        // create a new view
        // Get LayoutInflater
        val inflater = LayoutInflater.from(
            parent.context
        )
        // Inflate view to add to the Recyclerview
        val v = inflater.inflate(R.layout.checkliste, parent, false)
        //Create ViewHolder
        val vh = ViewHolder(v)

        globalVariable = context as StartClass

        //Get selected Course
        val sharedPreferencesSelectedCourse =
            context.getSharedPreferences("validation", Context.MODE_PRIVATE)
        selectedCourse = sharedPreferencesSelectedCourse.getString("selectedCourse", "0")

        //Return ViewHolder
        return vh
    }


    /**
     * Displays the data at a specific position.
     *
     * @param[holder] The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param[position] The position of the item within the adapter's data set.
     *
     * @author Alexander Lange
     * @since 1.5
     *
     * @see RecyclerView
     * @see RecyclerView.Adapter
     * @see RecyclerView.Adapter.onBindViewHolder
     */
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


    /**
     * Selects/Unselects a course as favorite.
     *
     * @param[position] The position of the course in the list.
     *
     * @return true if the course was set as favorite,false if the course is no longer a favorite.
     * @author Alexander Lange
     * @since 1.5
     */
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

    /**
     * Returns the size of the courseList.
     *
     * @return the size of the courselist
     *
     * @author Alexander Lange
     * @since 1.5
     * @see RecyclerView
     * @see RecyclerView.Adapter
     * @see RecyclerView.Adapter.getItemCount
     */
    //Item anzahl
    override fun getItemCount(): Int {
        return coursesList.size
    }

    /**
     * Return the view type of the item at <code>position</code> for the purposes
     * of view recycling.
     *
     * @param[position] position to query
     * @return integer value identifying the type of the view needed to represent the item at
     *                 <code>position</code>. Type codes need not be contiguous.
     * @author Alexander Lange
     * @since 1.5
     * @see RecyclerView
     * @see RecyclerView.Adapter
     * @see RecyclerView.Adapter.getItemViewType
     */
    override fun getItemViewType(position: Int): Int {
        return position
    }

    /**
     * Inner class to provide a reference to the views for each data item
     * Complex data items may need more than one view per item, and
     * you provide access to all the views for a data item in a view holder
     *
     * @author Alexander Lange
     * @since 1.5
     */
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