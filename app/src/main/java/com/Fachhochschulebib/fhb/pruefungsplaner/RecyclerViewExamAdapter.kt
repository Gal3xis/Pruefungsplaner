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
import kotlinx.android.synthetic.main.terminefragment.*
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
 * @since 1.6
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

        context?.let { database = AppDatabase.getAppDatabase(it) }

        sharedPreferencesValidation =
            context?.getSharedPreferences("validation", Context.MODE_PRIVATE)

        return ViewHolder(view)
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
            val name = modules[position]
            holder.txtHeader.text = name
            holder.layout.setOnClickListener {
                if (holder.txtSecondScreen.visibility == View.VISIBLE) {
                    holder.txtSecondScreen.visibility = View.GONE
                } else {
                    if (openItem?.txtSecondScreen?.visibility == View.VISIBLE) {
                        openItem?.txtSecondScreen?.visibility = View.GONE
                    }
                    holder.txtSecondScreen.visibility = View.VISIBLE
                    scopeIO.launch {
                        holder.txtSecondScreen.text =
                            context?.let { it1 ->
                                database?.userDao()?.getEntryById(planId[position])?.getString(
                                    it1
                                )
                            }
                    }
                    //Make previous details invisible
                    openItem = holder
                }
            }

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
     * @since 1.6
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
     * @since 1.6
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
     * @since 1.6
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
                        holder.ivicon.setImageDrawable(
                            context!!.resources.getDrawable(
                                Utils.favoritIcons[selectedEntry?.favorit ?: false]!!,
                                context!!.theme
                            )
                        )
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
     * Deletes the exam from favorits. Executed after clicking the add/remove-icon.
     *
     * @param[holder] The [ViewHolder] that holds the UI-Elements.
     * @param[position] The position of the item in the Recyclerview.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun deleteFromFavorites(position: Int, holder: ViewHolder) {
        var selectedEntry: TestPlanEntry? = null
        scopeIO.launch {
            selectedEntry = database?.userDao()?.getEntryById(planId[position])
            //Überprüfung ob Prüfitem Favorisiert wurde und angeklickt
            database?.userDao()
                ?.update(false, planId[position].toInt())
            context?.let {
                selectedEntry?.let { it1 ->
                    GoogleCalendarIO.deleteEntry(it, it1)
                }
            }
        }.invokeOnCompletion {
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(context, context!!.getString(R.string.delete), Toast.LENGTH_SHORT)
                    .show()
                this.notifyItemChanged(position)
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
     * @since 1.6
     */
    fun addToFavorites(position: Int, holder: ViewHolder) {
        var selectedEntry: TestPlanEntry? = null
        scopeIO.launch {
            selectedEntry = database?.userDao()?.getEntryById(planId[position])
            //Speichern des Prüfitem als Favorit
            database?.userDao()
                ?.update(true, planId[position].toInt())
            context?.let {
                selectedEntry?.let { it1 ->
                    GoogleCalendarIO.insertEntry(it, it1,true)
                }
            }
        }.invokeOnCompletion {
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(context, context!!.getString(R.string.add), Toast.LENGTH_SHORT)
                    .show()
                this.notifyItemChanged(position)
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
     * @since 1.6
     */
    fun checkFavorite(position: Int): Boolean {
        try {
            val selectedEntry = database?.userDao()?.getEntryById(planId[position])

            //Überprüfung ob Prüfitem Favorisiert wurde und angeklickt

            return selectedEntry?.favorit ?: false
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
     * @since 1.6
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