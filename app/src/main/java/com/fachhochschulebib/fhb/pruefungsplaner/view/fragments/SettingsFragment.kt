package com.fachhochschulebib.fhb.pruefungsplaner.view.fragments

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.os.Process
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.fachhochschulebib.fhb.pruefungsplaner.R
import com.fachhochschulebib.fhb.pruefungsplaner.utils.*
import com.fachhochschulebib.fhb.pruefungsplaner.view.activities.MainActivity
import com.fachhochschulebib.fhb.pruefungsplaner.view.helper.MainActivityFragment
import com.fachhochschulebib.fhb.pruefungsplaner.viewmodel.SettingsViewModel
import com.fachhochschulebib.fhb.pruefungsplaner.viewmodel.ViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_settings.*


/**
 * Class to maintain the Options-Fragment.
 *
 * @author Alexander Lange (Email:alexander.lange@fh-bielefeld.de)
 * @since 1.6
 */
class SettingsFragment() : MainActivityFragment() {
    /**
     * ViewModel for the SettingsFragment. Is set in [onViewCreated].
     * @see SettingsViewModel
     */
    private lateinit var viewModel: SettingsViewModel

    /**
     * Sets the name of that fragment to "Datenschutzerklärung"
     */
    override var name: String = "Einstellungen"

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
            ViewModelFactory(requireActivity().application)
        )[SettingsViewModel::class.java]
        setHasOptionsMenu(true)
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
        return inflater.inflate(R.layout.fragment_settings, container, false)
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
        initView()
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
        menu.findItem(R.id.menu_item_filter).isVisible = false
        super.onCreateOptionsMenu(menu, inflater)
    }


    /**
     * Initialized the UI.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private fun initView() {
        initThemeSpinner()
        initDarkModeSwitch()
        initUpdateDatabaseButton()
        initCalendarSynchronizationSwitch()
        initCalendarInsertionTypeSpinner()
        initCalendarIdSpinner()
        initPrivacyDeclarationButton()
        initImpressumButton()
        initDeleteDatabaseButton()
        initDeleteCalendarEntriesButton()
        initUpdateCalendarButton()
        initDeleteFavoritesButton()
        initBackgroundUpdateSwitch()
        initBackgroundUpdateIntervalButton()
    }

    /**
     * Initializes the CalendarId Spinner. The Spinner lets the user pick, in which calendar the exams shall bes synced.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private fun initCalendarIdSpinner() {
        val calendar = CalendarIO.getCalendars(requireContext()).toMutableList()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            calendar.removeIf {
                it.accessLevel < 700
            }
        } else {
            calendar.forEach {
                if (it.accessLevel < 700) {
                    calendar.remove(it)
                }
            }
        }
        val names = mutableListOf<String>()
        var selectedName: String? = null
        calendar.forEach {
            names.add(it.name)
            if (it.id == viewModel.getSelectedCalendar()) {
                selectedName = it.name
            }
        }
        fragment_settings_spinner_calendar_id.adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, names)
        fragment_settings_spinner_calendar_id.setSelection(
            selectedName ?: CalendarIO.getPrimaryCalendar(requireContext())?.name
        )
        fragment_settings_spinner_calendar_id.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (view == null) return
                val textView = (view as TextView)
                textView.setTextColor(
                    Utils.getColorFromAttr(
                        R.attr.colorOnPrimaryDark,
                        requireContext()
                    )
                )
                textView.textSize = 15f
                val id = calendar.find {
                    it.name == fragment_settings_spinner_calendar_id.selectedItem.toString()
                }?.id ?: 0
                viewModel.setSelectedCalendar(id)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    /**
     * Initializes the button to delete all favorites.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private fun initDeleteFavoritesButton() {
        fragment_settings_button_delete_favorites.setOnClickListener {
            val count = viewModel.liveFavorites.value?.size ?: return@setOnClickListener
            AlertDialog.Builder(requireContext())
                .setTitle("Favoriten löschen")
                .setMessage("Sollen alle $count Favoriten gelöscht werden?")
                .setPositiveButton("Löschen") { _, _ ->
                    viewModel.deleteAllFavorites(requireContext())
                    Toast.makeText(
                        requireContext(),
                        requireActivity().getString(R.string.delete_favorite),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .setNegativeButton("Cancel", null)
                .create()
                .show()
        }
    }

    /**
     * Initializes the button to update the calendar. Inserts every favorite into the calendar, despite a deactivated synchronization.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private fun initUpdateCalendarButton() {
        fragment_settings_button_update_calendar.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Kalendereinträge aktualisieren")
                .setMessage("Soll der Kalender aktualisiert werden?")
                .setPositiveButton("Ja") { _, _ ->
                    viewModel.updateCalendar(requireContext())
                }
                .setNegativeButton("Nein", null)
                .create()
                .show()

        }
    }

    /**
     * Initializes the button to delete all calendar entries. Deletes every entry in the calendar that is saved in the shared preferences.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private fun initDeleteCalendarEntriesButton() {
        fragment_settings_button_clear_calendar.setOnClickListener {
            val eventIds = viewModel.getCalendarIds()

            AlertDialog.Builder(requireContext())
                .setTitle("Kalendereinträge löschen")
                .setMessage("Sollen ${eventIds.count()} Einträge gelöscht werden?")
                .setPositiveButton("Ja") { _, _ ->
                    viewModel.deleteFromCalendar(
                        requireContext(),
                        eventIds
                    )
                }
                .setNegativeButton("Nein", null)
                .create()
                .show()
        }
    }

    /**
     * Initializes the button to delete all [com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry]-Objects from the local room database.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private fun initDeleteDatabaseButton() {
        fragment_settings_button_delete_database.setOnClickListener {
            val count = viewModel.liveEntries.value?.size ?: return@setOnClickListener
            AlertDialog.Builder(requireContext())
                .setTitle("Datenbank löschen")
                .setMessage("Sollen alle $count Einträge gelöscht werden?")
                .setPositiveButton("Löschen") { _, _ ->
                    viewModel.deleteAllEntries()
                    Toast.makeText(
                        requireContext(),
                        requireActivity().getString(R.string.delete_db),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .setNegativeButton("Cancel", null)
                .create()
                .show()
        }
    }

    /**
     * Initializes the button to show the impressum.
     * Redirects the user to the [ImpressumFragment].
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private fun initImpressumButton() {
        fragment_settings_button_impressum?.setOnClickListener {
            (activity as MainActivity).changeFragment(ImpressumFragment())
        }
    }

    /**
     * Initializes the button to show the privacy declaration.
     * Redirects the user to the [PrivacyDeclarationFragment].
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private fun initPrivacyDeclarationButton() {
        fragment_settings_button_privacy_declaration.setOnClickListener {
            (activity as MainActivity).changeFragment(PrivacyDeclarationFragment())
        }
    }

    /**
     * Initializes the switch to activate/deactivate the calendar synchronization.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private fun initCalendarSynchronizationSwitch() {
        fragment_settings_switch_synchronize_calendar.isChecked = viewModel.getCalendarSync()
        fragment_settings_switch_synchronize_calendar.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setCalendarSync(requireContext(), isChecked)
        }
    }

    /**
     * Initializes the spinner that lets the user pick an [CalendarIO.InsertionType].
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private fun initCalendarInsertionTypeSpinner() {
        val names = mutableListOf<String>()
        CalendarIO.InsertionType.values().forEach {
            names.add(it.name)
        }
        fragment_settings_spinner_calendar_insertiontype.adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, names)
        fragment_settings_spinner_calendar_insertiontype.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (view == null) return
                    val textView = (view as TextView)
                    textView.setTextColor(
                        Utils.getColorFromAttr(
                            R.attr.colorOnPrimaryDark,
                            requireContext()
                        )
                    )
                    textView.textSize = 15f
                    viewModel.setCalendarInserionType(
                        CalendarIO.InsertionType.valueOf(
                            fragment_settings_spinner_calendar_insertiontype.selectedItem.toString()
                        )
                    )
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
        fragment_settings_spinner_calendar_insertiontype.setSelection(viewModel.getCalendarInsertionType().name)
    }

    /**
     * Initializes the button to update the database.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private fun initUpdateDatabaseButton() {
        fragment_settings_button_update_database?.setOnClickListener {
            viewModel.updateDatabase()
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
        darkMode.isChecked = viewModel.getChosenDarkMode()
        darkMode.setOnCheckedChangeListener { _, _ ->
            if(darkMode.isChecked!=viewModel.getChosenDarkMode()){
                setDirty()
            }
            viewModel.setChosenDarkMode(darkMode.isChecked)
        }
    }

    /**
     * Parameter that stores if the app needs a restart to apply the changes
     */
    private var dirty = false

    private fun setDirty(){
        if(!dirty){
            Snackbar.make(requireView(),requireContext().resources.getString(R.string.settings_snackbar_restart_needed),Snackbar.LENGTH_INDEFINITE)
                .setAction(requireContext().resources.getString(R.string.settings_snackbar_restart)){
                    val pid = Process.myPid()
                    Process.killProcess(pid)
                    dirty = false
                }
                .setBackgroundTint(Utils.getColorFromAttr(R.attr.colorAccent,requireContext()))
                .setTextColor(Utils.getColorFromAttr(R.attr.colorOnAccent,requireContext()))
                .setActionTextColor(Utils.getColorFromAttr(R.attr.colorOnAccent,requireContext()))
                .show()
        }
        dirty = true
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
        Utils.themeList.forEach { x -> themeList.add(Theme(x, requireContext())) }

        val adapter = view?.context?.let {
            ThemeAdapter(
                it,
                R.layout.layout_settings_theme_spinner_row,
                themeList
            )
        }
        fragment_settings_spinner_theme?.adapter = adapter
        val selectedPos: Int
        val themeid = viewModel.getChosenThemeId()
        selectedPos = Utils.themeList.indexOf(themeid)
        fragment_settings_spinner_theme?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val chosenThemeId:Int = when (position) {
                    1 -> R.style.Theme_AppTheme_2
                    else -> R.style.Theme_AppTheme_1
                }
                if(chosenThemeId != viewModel.getChosenThemeId()){
                    setDirty()
                }
                viewModel.setChosenThemeId(chosenThemeId)
            }
        }
        try {
            fragment_settings_spinner_theme?.setSelection(selectedPos)
        } catch (ex: Exception) {

        }

    }

    /**
     * Initializes the switch to activate/deactivate the search for new database updates in the background.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private fun initBackgroundUpdateSwitch() {
        fragment_settings_switch_background_updates.isChecked = viewModel.getBackgroundUpdates()
        fragment_settings_switch_background_updates.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setBackgroundUpdates(isChecked)
            context?.let { BackgroundUpdatingService.invalidatePeriodicRequests(it) }
        }

        fragment_settings_switch_background_updates_notification_sound.isChecked =
            viewModel.getNotificationSounds()
        fragment_settings_switch_background_updates_notification_sound.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setNotificationSounds(isChecked)
            context?.let { BackgroundUpdatingService.invalidatePeriodicRequests(it) }
        }
    }

    /**
     * Initializes the button to change the interval time of the background worker.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private fun initBackgroundUpdateIntervalButton() {
        val currentMinute = { viewModel.getUpdateIntervalTimeMinute() }
        val currentHour = { viewModel.getUpdateIntervalTimeHour() }

        fragment_settings_button_background_updates_interval.setOnClickListener {
            TimePickerDialog(context, 3, { _, hour, minute ->
                var _minute = minute
                if (hour == 0 && _minute < 15) {
                    _minute = 15
                    Toast.makeText(context, "15 Minutes is minimum", Toast.LENGTH_SHORT)
                        .show()//TODO
                }
                setIntervallTime(hour, _minute)

            }, currentHour(), currentMinute(), true)
                .show()
        }
        fragment_settings_button_background_updates_interval.text =
            "${resources.getString(R.string.settings_button_auto_updates_interval)} ${currentHour()}:${currentMinute()}"
    }

    /**
     * Helperfunction for the TimePicker to change the background interval time.
     *
     * @author Alexander Lange
     * @since 1.6
     *
     * @see initBackgroundUpdateIntervalButton
     */
    private fun setIntervallTime(
        hour: Int = viewModel.getUpdateIntervalTimeHour(),
        minute: Int = viewModel.getUpdateIntervalTimeHour()
    ) {
        viewModel.setUpdateIntervalTimeMinute(minute)
        viewModel.setUpdateIntervalTimeHour(hour)
        fragment_settings_button_background_updates_interval.text =
            "${resources.getString(R.string.settings_button_auto_updates_interval)} ${hour}:${minute}"
        context?.let { BackgroundUpdatingService.invalidatePeriodicRequests(it) }
    }

    /**
     * Save the current selected options and recreate the activity to change the theme and the darkmmode.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private fun save() {
        view?:return


        if ( darkMode.isChecked != viewModel.getChosenDarkMode()) {

            AlertDialog.Builder(requireContext()).setTitle("Neustart benötigt")
                .setMessage("Jetzt Neustarten?").setPositiveButton(
                    "Neustarten"
                ) { _, _ ->
                    val pid = Process.myPid()
                    Process.killProcess(pid)
                }.setNegativeButton("Nicht Neustarten") { _, _ ->
                    (activity as MainActivity).changeFragment(ExamOverviewFragment())

                }.create().show()
        } else {
            viewModel.setChosenDarkMode(darkMode.isChecked)
            (activity as MainActivity).changeFragment(ExamOverviewFragment())

        }


    }
}