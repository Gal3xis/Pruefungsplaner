package com.fachhochschulebib.fhb.pruefungsplaner.view.helper

import android.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.*
import com.fachhochschulebib.fhb.pruefungsplaner.R
import com.fachhochschulebib.fhb.pruefungsplaner.utils.add
import com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry
import com.fachhochschulebib.fhb.pruefungsplaner.utils.Utils
import com.fachhochschulebib.fhb.pruefungsplaner.utils.getString
import com.fachhochschulebib.fhb.pruefungsplaner.viewmodel.BaseViewModel
import com.google.android.material.card.MaterialCardView
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

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
class RecyclerViewFavoriteAdapter(
    private val context: Context,
    var entryList: MutableList<TestPlanEntry>,
    private val viewModel: BaseViewModel
) : RecyclerView.Adapter<RecyclerViewFavoriteAdapter.ViewHolder>() {
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
        val inflater = LayoutInflater.from(context)
        val v = inflater.inflate(R.layout.favoriten, parent, false)
        return ViewHolder(v)
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
        holder.set(entry)
    }

    /**
     * Updates the content of the recyclerview with a new List of courses.
     * Replaces the current list with the new list.
     *
     * @param entryList The list of [TestPlanEntry]-Objects to be shown by the recyclerview.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun updateContent(entryList: List<TestPlanEntry>) {
        this.entryList = entryList.toMutableList()
        notifyDataSetChanged()
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
        /**
         * Card that shows all information. Can be clicked on to show/hide detailed information.
         */
        private var card = layout.findViewById<MaterialCardView>(R.id.card)

        /**
         * TextView that shows the name of the module.
         */
        private var headerModule = layout.findViewById<TextView>(R.id.headerModule)

        /**
         * TextView that shows the name of the course.
         */
        private var headerCourse = layout.findViewById<TextView>(R.id.headerCourse)

        /**
         * TextView that shows the date of the exam.
         */
        private var headerDate = layout.findViewById<TextView>(R.id.headerDate)

        /**
         * TextView that shows the timespan of the exam (eg. 08:00-09:30)
         */
        private var headerTime = layout.findViewById<TextView>(R.id.headerTime)

        /**
         * TextView that shows the detailed information
         */
        private var expandedTextView = layout.findViewById<TextView>(R.id.expandedCardTextView)

        /**
         * Layout that can be expanded to show the detailed information
         */
        private var expansion = layout.findViewById<RelativeLayout>(R.id.expansion)

        /**
         * Icon that shows, if the [TestPlanEntry] is currently playced in the calendar
         */
        private var iconInCalendar = layout.findViewById<ImageView>(R.id.icon_in_calendar)

        init {
            //            ivicon = layout.findViewById<View>(R.id.icon) as ImageView
//            txtHeader = layout.findViewById<View>(R.id.firstLine) as TextView
//            txtFooter = layout.findViewById<View>(R.id.secondLine) as TextView
//            txtSecondScreen = layout.findViewById<View>(R.id.txtSecondscreen) as TextView
//            txtthirdline = layout.findViewById<View>(R.id.thirdLine) as TextView
//            layout2 = layout.findViewById<View>(R.id.linearLayout) as LinearLayout
        }

        /**
         * Function to initialize the UI-Elements for a specific [TestPlanEntry].
         *
         * @param entry The [TestPlanEntry] that contains the data to be displayed in this viewholder.
         *
         * @author Alexander Lange
         * @since 1.6
         */
        fun set(entry: TestPlanEntry) {
            val dateParser = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val dateFormatterDay = SimpleDateFormat("dd.MM.yyyy")
            val dateFormatterTime = SimpleDateFormat("HH:mm")

            headerModule.text = entry.module
            headerCourse.text = "(${entry.course})"

            val dateStart = dateParser.parse(entry.date)
            val dateEnd = Calendar.getInstance()
            dateEnd.time = dateStart
            dateEnd.add(Calendar.MINUTE, Utils.getExamDuration(entry.examForm))
            headerDate.text = dateStart?.let { dateFormatterDay.format(it) }
            headerTime.text =
                "${dateFormatterTime.format(dateStart)}-${dateFormatterTime.format(dateEnd.time)}"
            expandedTextView.text = entry.getString(context)

            if (viewModel.getCalendarId(entry) == null) iconInCalendar.setImageResource(R.drawable.ic_not_in_calendar) else iconInCalendar.setImageResource(
                R.drawable.ic_in_calendar
            )

            card.setOnClickListener {
                if (expansion.visibility == View.GONE) expansion.visibility =
                    View.VISIBLE else expansion.visibility = View.GONE
            }

            card.setOnLongClickListener {
                showContextMenu(entry)
                true
            }

            /*            name = entry.module
                txtHeader.text = name

                layout.setOnClickListener {
                    if (txtSecondScreen.visibility == View.VISIBLE) {
                        txtSecondScreen.visibility = View.GONE
                    } else {
                        if (openItem?.txtSecondScreen?.visibility == View.VISIBLE) {
                            openItem?.txtSecondScreen?.visibility = View.GONE
                        }
                        txtSecondScreen.visibility = View.VISIBLE
                        txtSecondScreen.text = entry.getString(context)
                        //Make previous details invisible
                        openItem = this
                    }
                }

                //Prüfitem von der Favoritenliste löschen
                ivicon.setOnClickListener { remove(position) }
                txtFooter.text = context!!.getString(R.string.prof) + "${entry.firstExaminer},${entry.secondExaminer}"*/
//
//            holder.txtthirdline.text = (context!!.getString(R.string.clockTime2)
//                    + splitDateAndTime?.get(1)?.substring(0, 5)
//                    +"\n"
//                    + context!!.getString(R.string.date2)
//                    + (splitDayMonthYear?.get(2) ?: "") + "."
//                    + (splitDayMonthYear?.get(1) ?: "") + "."
//                    + (splitDayMonthYear?.get(0) ?: ""))
//            holder.txtFooter.text = (context!!.getString(R.string.prof)
//                    + entry.firstExaminer + ", " + entry.secondExaminer
//                    + context!!.getString(R.string.semester) + entry.semester)
        }

        /**
         * Displays a menu, that lets the user remove this [TestPlanEntry] from his favorites or delete this [TestPlanEntry] from the calendar.
         *
         * @author Alexander Lange
         * @since 1.6
         */
        private fun showContextMenu(entry: TestPlanEntry) {
            val dialog: AlertDialog = AlertDialog.Builder(context).create()


            val contextMenuView = LayoutInflater.from(context)
                .inflate(R.layout.layout_favorite_contextmenu, null, false)
            contextMenuView.findViewById<Button>(R.id.AddToCalendar).text =
                if (viewModel.getCalendarId(entry) == null) {
                    context.resources.getString(R.string.favorite_contextmenu_add_to_calendar)

                } else context.resources.getString(
                    R.string.favorite_contextmenu_remove_from_calendar
                )
            contextMenuView.findViewById<Button>(R.id.AddToCalendar).setOnClickListener {
                viewModel.getCalendarId(entry)
                    ?.let { viewModel.deleteFromCalendar(context, it) }
                    ?: viewModel.insertIntoCalendar(context, entry)
                if (viewModel.getCalendarId(entry) == null) iconInCalendar.setImageResource(R.drawable.ic_not_in_calendar) else iconInCalendar.setImageResource(
                    R.drawable.ic_in_calendar
                )
                dialog.hide()
            }
            contextMenuView.findViewById<Button>(R.id.removeFromFavorits).setOnClickListener {
                viewModel.updateEntryFavorite(context, false, entry)
                dialog.hide()
            }
            dialog.setView(contextMenuView)
            dialog.show()
        }
    }
}