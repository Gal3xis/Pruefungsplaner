package com.Fachhochschulebib.fhb.pruefungsplaner

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.*
import com.Fachhochschulebib.fhb.pruefungsplaner.data.TestPlanEntry
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
 * @since 1.6
 * @see RecyclerView.Adapter
 * @see RecyclerView
 */
class RecyclerViewExamAdapter    // Provide a suitable constructor (depends on the kind of dataset)
    (
    private var entryList: MutableList<TestPlanEntry>,
    private val viewModel: MainViewModel
) : RecyclerView.Adapter<RecyclerViewExamAdapter.ViewHolder>() {
    private var save = false
    private var moduleName: String? = null
    private var context: Context? = null
    private var calDate = GregorianCalendar()

    private var openItem: ViewHolder? = null


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
     * @since 1.6
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
        val view = inflater.inflate(R.layout.termine, parent, false)
        context = view.context
        return ViewHolder(view)
    }

    fun updateContent(entryList:List<TestPlanEntry>){
        this.entryList= entryList.toMutableList()
        notifyDataSetChanged()
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
     * @since 1.6
     *
     * @see RecyclerView.Adapter.onBindViewHolder
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            val name = entryList[position].module
            holder.txtHeader.text = name
            holder.layout.setOnClickListener {
                if (holder.txtSecondScreen.visibility == View.VISIBLE) {
                    holder.txtSecondScreen.visibility = View.GONE
                } else {
                    if (openItem?.txtSecondScreen?.visibility == View.VISIBLE) {
                        openItem?.txtSecondScreen?.visibility = View.GONE
                    }
                    holder.txtSecondScreen.visibility = View.VISIBLE
                    holder.txtSecondScreen.text =
                        context?.let { it1 ->
                            entryList[position].getString(
                                it1
                            )
                        }
                    //Make previous details invisible
                    openItem = holder
                }
            }

            // Start Merlin Gürtler
            // erhalte den ausgewählten Studiengang
            val course = name?.split(" ")?.toTypedArray()

            setIcons(position, holder)
            //Aufteilung nach verschiedenen Tagen
            val splitDay = splitInDays(position, holder)

            //Darstellen der Werte in der Prüfitem Komponente
            splitDay?.let { initFooter(it, holder, position) }

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
     * @since 1.6
     */
    private fun initFooter(
        splitDay: Array<String>,
        holder: ViewHolder,
        position: Int
    ) {
        val entry = entryList[position]
        val splitMonthDayYear = splitDay[0].split("-").toTypedArray()
        holder.txtthirdline.text =
            context!!.getString(R.string.time) + splitDay[1].substring(0, 5)
        holder.button.text = (splitMonthDayYear[2] + "."
                + splitMonthDayYear[1] + "."
                + splitMonthDayYear[0])
        holder.txtFooter.text = (context!!.getString(R.string.prof)
                + entry.firstExaminer + ", "
                + entry.secondExaminer
                + context!!.getString(R.string.semester) + entry.semester)
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
     * @since 1.6
     */
    private fun splitInDays(
        position: Int,
        holder: ViewHolder
    ): Array<String>? {
        val entry = entryList[position]
        val splitDay = entry.date?.split(" ")?.toTypedArray()
        if (position > 0) {
            val splitDayBefore = entryList[position-1].date?.split(" ")?.toTypedArray()

            //Vergleich der beiden Tage
            //wenn ungleich, dann blaue box mit Datumseintrag
            if (splitDay?.get(0) == splitDayBefore?.get(0) ?: splitDay?.get(0)) {
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
     * @since 1.6
     */
    private fun setIcons(
        position: Int,
        holder: ViewHolder
    ) {
        var entry:TestPlanEntry? = null
        try {
             entry = entryList[position]
            //Datenbank und Pruefplan laden
            save = false
        } catch (ex: Exception) {
            Log.d("MyAdapter.kt-onBindViewHolder", ex.stackTraceToString())
        }
        try {
            if (position < 0) {
                return
            }
            val pruefid = entry?.id?.toInt()
            if (Integer.valueOf(entry?.id) != pruefid) {
                return
            }
            // Start Merlin Gürtler
            // Setze die Farbe des Icons
            if (context != null) {
                holder.statusIcon.setColorFilter(
                    Utils.getColorFromAttr(
                        Utils.statusColors[entry?.status]
                            ?: R.attr.defaultStatusColor, context!!.theme
                    )
                )
                holder.ivicon.setImageDrawable(
                    context!!.resources.getDrawable(
                        Utils.favoritIcons[entry?.favorit ?: false]!!,
                        context!!.theme
                    )
                )
            }
        } catch (ex: Exception) {
            Log.d("onBindViewer-ThreadHandler", ex.stackTraceToString())
        }
        //OnClickListener
        // Start Merlin Gürtler
        // Gibt die Statusmeldung aus
        holder.statusIcon.setOnClickListener { v: View ->
            Toast.makeText(
                v.context,
                entry?.status,
                Toast.LENGTH_SHORT
            ).show()
        }
        // Ende Merlin Gürtler
        holder.ivicon.setOnClickListener {
            val isFavorite = checkFavorite(position)
            // toggelt den Favoriten
            if (!isFavorite) {
                addToFavorites(position, holder)
            } else {
                deleteFromFavorites(position, holder)
            }
        }
    }

    /**
     * Deletes the exam from favorits. Executed after clicking the add/remove-icon.
     *
     * @param[holder] The [ViewHolder] that holds the UI-Elements.
     * @param[position] The position of the item in the Recyclerview.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun deleteFromFavorites(position: Int, holder: ViewHolder) {
        val entry = entryList[position]
        //Überprüfung ob Prüfitem Favorisiert wurde und angeklickt
        viewModel.updateEntryFavorit(false, entry)
        context?.let {
            entry.let { it1 ->
                GoogleCalendarIO.deleteEntry(it, it1)
            }
        }
        Toast.makeText(context, context!!.getString(R.string.delete), Toast.LENGTH_SHORT)
            .show()
        this.notifyItemChanged(position)
    }

    /**
     * Adds the exam to the favorits. Executed after clicking the add/remove-icon.
     *
     * @param[holder] The [ViewHolder] that holds the UI-Elements.
     * @param[position] The position of the item in the Recyclerview.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun addToFavorites(position: Int, holder: ViewHolder) {
        var entry=entryList[position]
        //Speichern des Prüfitem als Favorit
        viewModel.updateEntryFavorit(true, entry)
        context?.let {
            entry.let { it1 ->
                GoogleCalendarIO.insertEntry(it, it1, true)
            }
        }
        Toast.makeText(context, context!!.getString(R.string.add), Toast.LENGTH_SHORT)
            .show()
        this.notifyItemChanged(position)
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
     * @since 1.6
     */
    fun checkFavorite(position: Int): Boolean {
        return try {
            val entry = entryList[position]
            //Überprüfung ob Prüfitem Favorisiert wurde und angeklickt

            entry.favorit ?: false
        } catch (ex: Exception) {
            Log.e("MyAdapter.kt-checkFavorite:", ex.stackTraceToString())
            false
        }
    }

    /**
     * Returns the amount of items in the recyclerview, based on the size of the moduleslist.
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
     * @since 1.6
     *
     * @see RecyclerView.Adapter.getItemViewType
     */
    override fun getItemViewType(position: Int): Int {
        return position
    }


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