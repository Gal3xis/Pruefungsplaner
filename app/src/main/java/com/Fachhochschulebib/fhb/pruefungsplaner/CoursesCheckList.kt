package com.Fachhochschulebib.fhb.pruefungsplaner

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import android.widget.TextView
import android.widget.CheckBox
import com.Fachhochschulebib.fhb.pruefungsplaner.data.Courses

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
 * @since 1.6
 *
 * @see AddCourseFragment
 * @see RecyclerView.Adapter
 */
class CoursesCheckList(val courseList:List<Courses>,private val viewModel: MainViewModel, private val context: Context) : RecyclerView.Adapter<CoursesCheckList.ViewHolder>() {
    private var globalVariable:StartClass? = null

    fun getChosen():List<Courses>{
        val ret = mutableListOf<Courses>()
        courseList.forEach {
            if(it.choosen==true) ret.add(it)
        }
        return ret
    }

    fun getNotChosen():List<Courses>{
        val ret = mutableListOf<Courses>()
        courseList.forEach {
            if(it.choosen==false) ret.add(it)
        }
        return ret
    }

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
     * @since 1.6
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
     * @since 1.6
     *
     * @see RecyclerView
     * @see RecyclerView.Adapter
     * @see RecyclerView.Adapter.onBindViewHolder
     */
    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Initialisierung der Komponenten
        holder.nameCourse.text = courseList[position].courseName
        holder.checkBoxCourse.isChecked = courseList[position].choosen == true
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
     * @since 1.6
     */
    private fun addFavorite(position: Int): Boolean {
        if (courseList[position].courseName != viewModel.getSelectedCourse() || globalVariable?.isChangeFaculty == true) {
            courseList[position].choosen?.let { courseList[position].choosen = !it }
        } else {
            Toast.makeText(
                context, context.getString(R.string.favorite_main_course),
                Toast.LENGTH_SHORT
            ).show()
        }
        return courseList[position].choosen == true
    }

    /**
     * Returns the size of the courseList.
     *
     * @return the size of the courselist
     *
     * @author Alexander Lange
     * @since 1.6
     * @see RecyclerView
     * @see RecyclerView.Adapter
     * @see RecyclerView.Adapter.getItemCount
     */
    //Item anzahl
    override fun getItemCount(): Int {
        return courseList.size
    }

    /**
     * Return the view type of the item at <code>position</code> for the purposes
     * of view recycling.
     *
     * @param[position] position to query
     * @return integer value identifying the type of the view needed to represent the item at
     *                 <code>position</code>. Type codes need not be contiguous.
     * @author Alexander Lange
     * @since 1.6
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
     * @since 1.6
     */
    inner class ViewHolder internal constructor(v: View) : RecyclerView.ViewHolder(v) {
        // each data item is just a string in this case
        val nameCourse: TextView = v.findViewById<View>(R.id.courseName) as TextView
        val checkBoxCourse: CheckBox = v.findViewById<View>(R.id.checkBox) as CheckBox

    }
}