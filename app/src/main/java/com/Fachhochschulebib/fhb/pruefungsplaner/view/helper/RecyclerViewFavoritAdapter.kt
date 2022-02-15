package com.Fachhochschulebib.fhb.pruefungsplaner.view.helper

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import android.widget.TextView
import android.widget.LinearLayout
import com.Fachhochschulebib.fhb.pruefungsplaner.R
import com.Fachhochschulebib.fhb.pruefungsplaner.utils.add
import com.Fachhochschulebib.fhb.pruefungsplaner.utils.getString
import com.Fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry
import com.Fachhochschulebib.fhb.pruefungsplaner.viewmodel.BaseViewModel
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
/**
 * The [RecyclerView.Adapter] for the [RecyclerView] that holds information about the favorit exams.
 * The information is stored in multiple [List]-Objects, each holding one kind of information for every exam that
 * needs to be displayed. E.g. the exam at position 1 gets his information from every list at index 1.
 *
 * @author Alexander Lange
 * @since 1.6
 * @see RecyclerView.Adapter
 * @see RecyclerView
 */
class RecyclerViewFavoritAdapter     // Provide a suitable constructor (depends on the kind of dataset)
(
        var entryList: MutableList<TestPlanEntry>,
        private val viewModel: BaseViewModel
) : RecyclerView.Adapter<RecyclerViewFavoritAdapter.ViewHolder>() {
    private var modulName: String? = null
    private var name: String? = null
    private lateinit var context: Context
    private var openItem: ViewHolder? = null

    /**
     * Adds an item to the recyclerview.
     *
     * @param[position] The position, where the item needs to be inserted.
     * @param[item] The item, that needs to be inserted.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun add(position: Int?, entry: TestPlanEntry) {
        notifyItemInserted(
                entryList.add(position, entry)
        )
    }

    /**
     * Removes an item of the recyclerview.
     *
     * @param[position] The position of the item that is to remove.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun remove(position: Int) {
        try {
            val entry = entryList[position]
            viewModel.updateEntryFavorit(context, false, entry)
            notifyItemChanged(position)
            Toast.makeText(
                    context,
                    context!!.getString(R.string.delete), Toast.LENGTH_SHORT
            ).show()
        } catch (ex: Exception) {
            Log.e("MyAdapterfavorits.kt-remove", ex.stackTraceToString())
        }
    }

    /**
     * Inflates the view that shows the information for the passed viewType. In this case the information
     * about the exam.
     *
     * @param[parent] The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param[viewType] The view type of the new View.
     * @return The [ViewHolder], that shows the information.
     *
     * @author Alexander Lange
     * @since 1.6
     *
     * @see RecyclerView.Adapter.onCreateViewHolder
     */
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

    /**
     * Initializes the [ViewHolder] with information of the viewtype. In this case,
     * passes the examinformation to the UI-Elements.
     *
     * @param[holder] The ViewHolder which should be updated to represent the contents of the item at the given position in the data set.
     * @param[position] The position of the item within the adapter's data set.
     *
     * @author Alexander Lange
     * @since 1.6
     *
     * @see RecyclerView.Adapter.onBindViewHolder
     */
    override fun onBindViewHolder(
            holder: ViewHolder,
            position: Int
    ) {
        val entry = entryList[position]
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        name = entry.module
        holder.txtHeader.text = name

        holder.layout.setOnClickListener {
            if (holder.txtSecondScreen.visibility == View.VISIBLE) {
                holder.txtSecondScreen.visibility = View.GONE
            } else {
                if (openItem?.txtSecondScreen?.visibility == View.VISIBLE) {
                    openItem?.txtSecondScreen?.visibility = View.GONE
                }
                holder.txtSecondScreen.visibility = View.VISIBLE
                holder.txtSecondScreen.text = entry.getString(context)
                //Make previous details invisible
                openItem = holder
            }
        }

        //Prüfitem von der Favoritenliste löschen
        holder.ivicon.setOnClickListener { remove(position) }
        holder.txtFooter.text = context!!.getString(R.string.prof) + "${entry.firstExaminer},${entry.secondExaminer}"

        val course = name?.split(" ")?.toTypedArray()
        modulName = ""
        var b: Int
        b = 0
        while (b < (course?.size ?: 0) - 1) {
            modulName = modulName + " " + (course?.get(b) ?: "")
            b++
        }
        displayExamInformation(position, holder)
    }

    fun updateContent(entryList: List<TestPlanEntry>) {
        this.entryList = entryList.toMutableList()
        notifyDataSetChanged()
    }

    /**
     * Displays the examinformation. Passes the information to the corresponding UI-Elements.
     *
     * @param[holder] The ViewHolder which should be updated to represent the contents of the item at the given position in the data set.
     * @param[position] The position of the item within the adapter's data set.
     *
     * @author Alexander Lange
     * @since 1.6
     *
     */
    private fun displayExamInformation(
            position: Int,
            holder: ViewHolder
    ) {
        val entry = entryList[position]
        //darstellen der Informationen für das Prüfitem
        val splitDateAndTime = entry.date?.split(" ")?.toTypedArray()
        val splitDayMonthYear = splitDateAndTime?.get(0)?.split("-")?.toTypedArray()
        holder.txtthirdline.text = (context!!.getString(R.string.clockTime2)
                + splitDateAndTime?.get(1)?.substring(0, 5)
                +"\n"
                + context!!.getString(R.string.date2)
                + (splitDayMonthYear?.get(2) ?: "") + "."
                + (splitDayMonthYear?.get(1) ?: "") + "."
                + (splitDayMonthYear?.get(0) ?: ""))
        holder.txtFooter.text = (context!!.getString(R.string.prof)
                + entry.firstExaminer + ", " + entry.secondExaminer
                + context!!.getString(R.string.semester) + entry.semester)
    }


    /**
     * Returns the amount of items in the recyclerview, based on the size of the [moduleAndCourseList].
     *
     * @return The amount of items in the recyclerview.
     *
     * @author Alexander Lange
     * @since 1.6
     *
     * @see RecyclerView.Adapter.getItemCount
     */
    override fun getItemCount(): Int {
        return entryList.size
    }


// Start Merlin Gürtler
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    /**
     * Inner class [ViewHolder], that contains the references to the UI-Elements.
     *
     * @author Alexander Lange
     * @since 1.6
     *
     * @see RecyclerView.ViewHolder
     */
    inner class ViewHolder(var layout: View) : RecyclerView.ViewHolder(
            layout
    ) {
        // each data item is just a string in this case
        var txtHeader: TextView
        var txtFooter: TextView
        var txtthirdline: TextView
        var ivicon: ImageView
        var layout2: LinearLayout
        val txtSecondScreen: TextView

        init {
            ivicon = layout.findViewById<View>(R.id.icon) as ImageView
            txtHeader = layout.findViewById<View>(R.id.firstLine) as TextView
            txtFooter = layout.findViewById<View>(R.id.secondLine) as TextView
            txtSecondScreen = layout.findViewById<View>(R.id.txtSecondscreen) as TextView
            txtthirdline = layout.findViewById<View>(R.id.thirdLine) as TextView
            layout2 = layout.findViewById<View>(R.id.linearLayout) as LinearLayout
            //button.setLayoutParams(new LinearLayout.LayoutParams(
            // LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        }
    }
}