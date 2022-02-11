package com.Fachhochschulebib.fhb.pruefungsplaner.view.fragments

import android.os.Bundle
import com.Fachhochschulebib.fhb.pruefungsplaner.model.room.AppDatabase
import com.Fachhochschulebib.fhb.pruefungsplaner.model.retrofit.RetrofitConnect
import android.util.Log
import android.widget.*
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.optionfragment.*
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import android.view.*
import androidx.lifecycle.ViewModelProvider
import com.Fachhochschulebib.fhb.pruefungsplaner.*
import com.Fachhochschulebib.fhb.pruefungsplaner.controller.GoogleCalendarIO
import com.Fachhochschulebib.fhb.pruefungsplaner.controller.Theme
import com.Fachhochschulebib.fhb.pruefungsplaner.controller.ThemeAdapter
import com.Fachhochschulebib.fhb.pruefungsplaner.controller.Utils
import com.Fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry
import com.Fachhochschulebib.fhb.pruefungsplaner.viewmodel.MainViewModel
import com.Fachhochschulebib.fhb.pruefungsplaner.viewmodel.MainViewModelFactory
import kotlinx.android.synthetic.main.hauptfenster.*


//////////////////////////////
// Optionen
//
//
//
// autor:
// inhalt:  Abfragen ob prüfungen zum Kalender hinzugefügt werden sollen  und Methoden zum löschen, aktualisieren der Datenbank
// zugriffsdatum: 20.2.20
//
//
//
//
//
//
//////////////////////////////
/**
 * Class to maintain the Options-Fragment.
 *
 * @author Alexander Lange (Email:alexander.lange@fh-bielefeld.de)
 * @since 1.6
 */
class Optionen() : Fragment() {
    private lateinit var viewModel: MainViewModel

    companion object {
        val idList: List<String> = ArrayList()
    }

    /**
     * Overrides the onCreate()-Method, which is called first in the Fragment-LifeCycle.
     * In this Method, the global parameter which are independent of the UI get initialized,
     * like the App-SharedPreferences and the reference to the Room-Database
     *
     * @since 1.6
     *
     * @author Alexander Lange (E-Mail:alexander.lange@fh-bielefeld.de)
     *
     * @see Fragment.onCreate
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(
            requireActivity(),
            MainViewModelFactory(requireActivity().application)
        )[MainViewModel::class.java]
        setHasOptionsMenu(true)
    }

    /**
     * Overrides the onViewCreated()-Method, which is called in the Fragment LifeCycle right after the onCreateView()-Method.
     * In this Method, the UI-Elements choose_course.xml-Layout are being initialized. This cannot be done in the onCreate()-Method,
     * because the UI-Elements, which are directly accessed via synthetic imports
     * are no instantiated in the onCreate()-Method yet.
     *
     * @since 1.6
     * @author Alexander Lange (E-Mail:alexander.lange@fh-bielefeld.de)
     * @see Fragment.onViewCreated
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initThemeSpinner()
        initDarkModeSwitch()

        //Button zum updaten der Prüfungen
        btnupdate?.setOnClickListener {
            val validation =
                viewModel.getExamineYear() + viewModel.getReturnCourse() + viewModel.getCurrentPeriode()
            updatePlan(validation)
        }
        //Abfrage ob der Google kalender Ein/Ausgeschaltet ist
        switch2.setOnCheckedChangeListener { _, isChecked -> // do something, the isChecked will be
            // true if the switch is in the On position
            setCalendarSynchro(isChecked)
        }
        switch2.isChecked = viewModel.getCalendarSync()

        privacyDeclaration.setOnClickListener {
            val ft = activity?.supportFragmentManager?.beginTransaction()
            header?.title = "Privacy"
            ft?.replace(R.id.frame_placeholder, PrivacyDeclarationFragment())
            ft?.commit()
        }

        optionenfragment_impressum?.setOnClickListener {
            val ft = activity?.supportFragmentManager?.beginTransaction()
            header?.title = "Impressum"
            ft?.replace(R.id.frame_placeholder, ImpressumFragment())
            ft?.commit()
        }

        //interne DB löschen
        btnDB.setOnClickListener { deleteInternalDatabase() }

        //Google Kalender einträge löschen
        btnCalClear.setOnClickListener {
            context?.let { it1 ->
                GoogleCalendarIO.deleteAll(it1)
            }
        }

        //Google Kalender einträge updaten
        btnGoogleUpdate.setOnClickListener {
            context?.let { it1 ->
                GoogleCalendarIO.deleteAll(it1)
            }
            var favorits: List<TestPlanEntry?>? = null
            favorits = viewModel.getFavorites(true)
            context?.let { it1 ->
                favorits?.let { it2 ->
                    GoogleCalendarIO.insertEntries(it1, it2, true)
                }
            }
        }
        //Favoriten Löschen
        btnFav.setOnClickListener { deleteFavorits() }
        optionenfragment_save_btn.setOnClickListener { save() }
    }

    /**
     * Toggles, if the Googlecalendar shall get favorits as events or not.
     * If it is toggled on, all current favorits get inserted into the calendar.
     * If it is toggled of, all events in the calendar for this application get deleted.
     *
     * @param[active] If synchronization is active or not.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private fun setCalendarSynchro(active: Boolean) {
        val favorites: MutableList<TestPlanEntry?> = mutableListOf()
        viewModel.getFavorites(true)?.let { favorites.addAll(it) }
        if (!active) {
            context?.let { it1 ->
                GoogleCalendarIO.deleteAll(it1)
            }
        }
        viewModel.setCalendarSync(active)
        if (active) {
            context?.let { it1 ->
                GoogleCalendarIO.insertEntries(it1, favorites, true)
            }
        }
    }

    /**
     * Deletes the stored favorits of the user from the Room-Database.
     *
     * @author Alexander Lange
     * @since 1.6
     * @see AppDatabase
     */
    private fun deleteFavorits() {
        val ppeList = viewModel.getFavorites(true)
        ppeList?.forEach {
            it.id?.toInt()?.let { it1 -> viewModel.updateEntryFavorit(false, it1) }
        }
        context?.let {
            ppeList?.let { it1 ->
                GoogleCalendarIO.deleteEntries(it, it1)
            }
        }
        Toast.makeText(
            view?.context,
            view?.context?.getString(R.string.delete_favorite),
            Toast.LENGTH_SHORT
        ).show()
    }

    /**
     * Deletes every entry in the Room-Database. After the deletion,
     * it updates the empty database
     * with the default entries for the user.
     *
     * @author Alexander Lange
     * @since 1.6
     * @see AppDatabase
     */
    private fun deleteInternalDatabase() {
        Log.d("Test", "Lokale DB löschen.")
        //Delete all entries in the Room-Database
        viewModel.deleteAllEntries()
        // Start Merlin Gürtler
        //Get the default entries for the user from the REST-API and store them in the room-database
        val retrofit = context?.let { RetrofitConnect( it) }
        //TODO retrofit?.RetrofitWebAccess()
        // Ende Merlin Gürtler
        Toast.makeText(
            view?.context,
            view?.context?.getString(R.string.delete_db),
            Toast.LENGTH_SHORT
        ).show()
    }

    /**
     * Overrides the [onCreateOptionsMenu]-Method from the [Fragment]-Superclass.
     * Initializes the items for the actionmenu. In this case the save-button.
     * @param[menu] The menu where the items should be displayed.
     * @param[inflater] The [MenuInflater] to inflate the actionmenu.
     * @author Alexander Lange
     * @since 1.6
     * @see Fragment.onCreateOptionsMenu
     * @see Menu
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.findItem(R.id.menu_item_save).isVisible = true
        //Make Filter-Button invisible
        menu.findItem(R.id.menu_item_filter).isVisible = false
        super.onCreateOptionsMenu(menu, inflater)
    }

    /**
     * Overrides the [onOptionsItemSelected]-Method from the [Fragment]-Superclass.
     * Called when the user clicks an item in the actionmenu.
     * Determines what to do for each item.
     * @param[item] The item, which was clicked by the user.
     * @return Return false to allow normal menu processing to proceed, true to consume it here.
     * @author Alexander Lange
     * @since 1.6
     * @see Fragment.onOptionsItemSelected
     * @see MenuItem
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //Set behaviour for each clicked item.
        return when (item.itemId) {
            //When the user clicked the savebutton.
            R.id.menu_item_save -> {
                save()
                true
            }
            else -> false
        }
    }

    /**
     * Initializes the darkmode-[Switch]. Get the previous selection from sharedpreferences and
     * pass them to the darkmode-[Switch].
     * @author Alexander Lange
     * @since 1.6
     * @see Switch
     */
    private fun initDarkModeSwitch() {
        darkMode.isChecked = viewModel.getChosenDarkmode()
    }

    /**
     * Initializes the theme-[Spinner]. Create a [Theme] for every implemented theme in
     * the styles.xml and passes them to a custom [ThemeAdapter], which is then passed to
     * the theme-[Spinner].
     * @author Alexander Lange
     * @since 1.6
     * @see Theme
     * @see ThemeAdapter
     * @see Spinner
     */
    private fun initThemeSpinner() {
        val themeList = mutableListOf<Theme>()
        Utils.themeList.forEach { x -> themeList.add(Theme(x, view)) }

        val adapter = view?.context?.let {
            ThemeAdapter(
                it,
                R.layout.layout_theme_spinner_row,
                themeList
            )
        }
        theme?.adapter = adapter
        val selectedPos: Int
        val themeid = viewModel.getChosenThemeId()
        selectedPos = Utils.themeList.indexOf(themeid)
        try {
            theme?.setSelection(selectedPos)
        } catch (ex: Exception) {

        }
        //TODO Alexander Lange End
    }

    /**
     * Save the current selected options and recreate the activity to change the theme and the darkmmode.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private fun save() {
        //Theme
        val position = theme.selectedItemPosition

        when (position) {
            1 -> viewModel.setChosenThemeId(R.style.Theme_AppTheme_2)
            else -> viewModel.setChosenThemeId(R.style.Theme_AppTheme_1)
        }
        viewModel.setChosenDarkmode(darkMode.isChecked)
        activity?.recreate()
    }

    /**
     * Overrides the onCreateView()-Method. It sets the current view to the optionfragmnet-layout.
     *
     * @return Returns the initialized view of this Fragment
     * @since 1.6
     * @author Alexander Lange (E-Mail:alexander.lange@fh-bielefeld.de)
     * @see Fragment.onCreateView
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.optionfragment, container, false)
        return v
    }

    //TODO Implement?
    fun updatePlan(validation: String?) {
        PingUrl(viewModel.getServerIPAddress())
    }

    /**
     * Updates the data for the exams, currently stored in the Room-Database.
     * @author Alexander Lange
     * @since 1.6
     * @see RetrofitConnect
     */
    fun updateCheckPlan() {
        val retrofit = context?.let { RetrofitConnect( it) }
        //TODO retrofit?.retroUpdate()
        Toast.makeText(
            context,
            context!!.getString(R.string.add_favorite),
            Toast.LENGTH_SHORT
        ).show()
    }

//Verbindungsaufbau zum Webserver
//Überprüfung ob Webserver erreichbar
    /**
     * Ping the webserver to check if there is a connection.
     * After success it updates the Room-Database.
     * @param[address] The address of the server to ping to.
     * @author Alexander Lange
     * @since 1.6
     * @see updateCheckPlan
     */
    fun PingUrl(address: String?) {
        try {
            val url = URL(address)
            val urlConn = url.openConnection() as HttpURLConnection
            urlConn.connectTimeout = 1000 * 10 // mTimeout is in seconds
            val startTime = System.currentTimeMillis()
            urlConn.connect()
            val endTime = System.currentTimeMillis()
            if (urlConn.responseCode == HttpURLConnection.HTTP_OK) {
                updateCheckPlan()
            }
        } catch (e: Exception) {
            Toast.makeText(
                context,
                context!!.getString(R.string.noConnection),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}