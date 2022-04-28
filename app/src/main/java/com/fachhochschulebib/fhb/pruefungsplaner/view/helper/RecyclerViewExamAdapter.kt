package com.fachhochschulebib.fhb.pruefungsplaner.view.helper

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.fachhochschulebib.fhb.pruefungsplaner.R
import com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry
import com.fachhochschulebib.fhb.pruefungsplaner.utils.Utils
import com.fachhochschulebib.fhb.pruefungsplaner.utils.atDay
import com.fachhochschulebib.fhb.pruefungsplaner.utils.getString
import com.fachhochschulebib.fhb.pruefungsplaner.viewmodel.BaseViewModel
import java.text.SimpleDateFormat

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
    var entryList: MutableList<TestPlanEntry>,
    private val viewModel: BaseViewModel
) : RecyclerView.Adapter<RecyclerViewExamAdapter.ViewHolder>() {
    private var save = false
    private lateinit var context: Context

    private var openItem: ViewHolder? = null

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
        holder.set(entryList[position])
    }
//
//
//    /**
//     * Deletes the exam from favorits. Executed after clicking the add/remove-icon.
//     *
//     * @param[holder] The [ViewHolder] that holds the UI-Elements.
//     * @param[position] The position of the item in the Recyclerview.
//     *
//     * @author Alexander Lange
//     * @since 1.6
//     */
//    fun deleteFromFavorites(position: Int, holder: ViewHolder) {
//        val entry = entryList[position]
//        //Überprüfung ob Prüfitem Favorisiert wurde und angeklickt
//        viewModel.updateEntryFavorit(context, false, entry)
//        Toast.makeText(context, context!!.getString(R.string.delete), Toast.LENGTH_SHORT)
//            .show()
//        this.notifyItemChanged(position)
//    }
//
//    /**
//     * Adds the exam to the favorits. Executed after clicking the add/remove-icon.
//     *
//     * @param[holder] The [ViewHolder] that holds the UI-Elements.
//     * @param[position] The position of the item in the Recyclerview.
//     *
//     * @author Alexander Lange
//     * @since 1.6
//     */
//    fun addToFavorites(position: Int, holder: ViewHolder) {
//        var entry = entryList[position]
//        //Speichern des Prüfitem als Favorit
//        viewModel.updateEntryFavorit(context, true, entry)
//        Toast.makeText(context, context!!.getString(R.string.add), Toast.LENGTH_SHORT)
//            .show()
//        this.notifyItemChanged(position)
//    }
//
//    /**
//     * Checks if an exam is a favorit.
//     * Needs to be called in a Coroutine-Scope.
//     *
//     * @param[position] The position of the item in the Recyclerview.
//     *
//     * @return true->The exam is a favorit;false->The exam is not a favorit.
//     *
//     * @author Alexander Lange
//     * @since 1.6
//     */
//    fun checkFavorite(position: Int): Boolean {
//        return try {
//            val entry = entryList[position]
//            //Überprüfung ob Prüfitem Favorisiert wurde und angeklickt
//
//            entry.favorit ?: false
//        } catch (ex: Exception) {
//            Log.e("MyAdapter.kt-checkFavorite:", ex.stackTraceToString())
//            false
//        }
//    }

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
        val txtModule: TextView = v.findViewById(R.id.moduleTextView)
        val layout: LinearLayout = v.findViewById(R.id.examInfoLayout)
        val ivicon: ImageView = v.findViewById(R.id.favoriteIcon)
        val statusIcon: ImageView = v.findViewById(R.id.statusIcon)
        val dateTextView: TextView = v.findViewById(R.id.date)
        val secondScreen:LinearLayout = v.findViewById(R.id.secondScreen)
        val txtSecondScreen: TextView = v.findViewById(R.id.txtSecondscreen)

        fun set(entry: TestPlanEntry) {
            val name = entry.module
            txtModule.text = name
            txtSecondScreen.text =  entry.getString(context)

            layout.setOnClickListener {
                secondScreen.visibility = if(secondScreen.isVisible)View.GONE else View.VISIBLE
            }

            // Start Merlin Gürtler
            // erhalte den ausgewählten Studiengang
            val course = name?.split(" ")?.toTypedArray()

            setIcons(entry)
            //Aufteilung nach verschiedenen Tagen
            val splitDay = splitInDays(entry)

            //Darstellen der Werte in der Prüfitem Komponente
            splitDay?.let { initFooter(it) }
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
        private fun splitInDays(entry: TestPlanEntry): Array<String> {
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val date1 = sdf.parse(entry.date)

            val ret = arrayOf(
                SimpleDateFormat("dd.MM.yyyy").format(date1),
                SimpleDateFormat("HH:mm").format(date1)
            )

            dateTextView.visibility = View.VISIBLE

            if (layoutPosition > 0) {
                val entry2 = entryList[layoutPosition - 1]
                val date2 = sdf.parse(entry2.date)
                if (date1.atDay(date2)) {
                    dateTextView.visibility = View.GONE
                }
            }
            return ret
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
            entry: TestPlanEntry
        ) {
            statusIcon.setColorFilter(
                Utils.getColorFromAttr(
                    Utils.statusColors[entry.status]
                        ?: R.attr.defaultStatusColor, context.theme
                )
            )
            ivicon.setImageDrawable(
                Utils.favoritIcons[entry.favorit]?.let {
                    context.resources.getDrawable(
                        it,
                        context.theme
                    )
                }
            )
            statusIcon.setOnClickListener { v: View ->
                Toast.makeText(
                    v.context,
                    entry.status,
                    Toast.LENGTH_SHORT
                ).show()
            }

            ivicon.setOnClickListener {
                viewModel.updateEntryFavorit(context, !entry.favorit, entry)
                notifyItemChanged(layoutPosition)
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
            splitDay: Array<String>
        ) {
            dateTextView.text = splitDay[0]
        }
    }
}