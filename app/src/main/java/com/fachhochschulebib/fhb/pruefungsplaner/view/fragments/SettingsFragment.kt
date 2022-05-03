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
        menu.findItem(R.id.menu_item_save).isVisible = true
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
        return when (item.itemId) {
            R.id.menu_item_save -> {
                save()
                true
            }
            else -> false
        }
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
        initSaveButton()
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
        calendarIdSpinner.adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, names)
        calendarIdSpinner.setSelection(
            selectedName ?: CalendarIO.getPrimaryCalendar(requireContext())?.name
        )
        calendarIdSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
                        requireContext().theme
                    )
                )
                textView.textSize = 15f
                val id = calendar.find {
                    it.name == calendarIdSpinner.selectedItem.toString()
                }?.id ?: 0
                viewModel.setSelectedCalendar(id)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    /**
     * Initializes the save button. Saves all remaining changes to the shared preferences, and if necessary, asks the user to restart the app.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private fun initSaveButton() {
        optionenfragment_save_btn.setOnClickListener { save() }
    }

    /**
     * Initializes the button to delete all favorites.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private fun initDeleteFavoritesButton() {
        btnFav.setOnClickListener {
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
        btnGoogleUpdate.setOnClickListener {
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
        btnCalClear.setOnClickListener {
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
        btnDB.setOnClickListener {
            val count = viewModel.liveEntries.value?.size?:return@setOnClickListener
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
        optionenfragment_impressum?.setOnClickListener {
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
        privacyDeclaration.setOnClickListener {
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
        switch2.isChecked = viewModel.getCalendarSync()
        switch2.setOnCheckedChangeListener { _, isChecked ->
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
        calendarInsertionTypeSpinner.adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, names)
        calendarInsertionTypeSpinner.onItemSelectedListener =
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
                            requireContext().theme
                        )
                    )
                    textView.textSize = 15f
                    viewModel.setCalendarInserionType(
                        CalendarIO.InsertionType.valueOf(
                            calendarInsertionTypeSpinner.selectedItem.toString()
                        )
                    )
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
        calendarInsertionTypeSpinner.setSelection(viewModel.getCalendarInsertionType().name)
    }

    /**
     * Initializes the button to update the database.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private fun initUpdateDatabaseButton() {
        btnupdate?.setOnClickListener {
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
                R.layout.layout_theme_spinner_row,
                themeList
            )
        }
        themeSpinner?.adapter = adapter
        val selectedPos: Int
        val themeid = viewModel.getChosenThemeId()
        selectedPos = Utils.themeList.indexOf(themeid)
        try {
            themeSpinner?.setSelection(selectedPos)
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
        optionenfragment_auto_updates.isChecked = viewModel.getBackgroundUpdates()
        optionenfragment_auto_updates.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setBackgroundUpdates(isChecked)
            context?.let { BackgroundUpdatingService.invalidatePeriodicRequests(it) }
        }

        optionenfragment_auto_updates_notificationsound.isChecked =
            viewModel.getNotificationSounds()
        optionenfragment_auto_updates_notificationsound.setOnCheckedChangeListener { _, isChecked ->
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
        optionenfragment_auto_updates_intervall_button.setOnClickListener {
            TimePickerDialog(context, 3, { _, hour, minute ->
                var _minute = minute
                if (hour == 0 && _minute < 15) {
                    _minute = 15
                    Toast.makeText(context, "15 Minutes is minimum", Toast.LENGTH_SHORT).show()
                }
                setIntervallTime(hour, _minute)

            }, viewModel.getUpdateIntervalTimeHour(), viewModel.getUpdateIntervalTimeMinute(), true)
                .show()
        }
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
        optionenfragment_auto_updates_intervall_button.text = "%s%02d:%02d".format(
            resources.getString(R.string.optionenfragment_auto_updates_intervall_text),
            hour,
            minute
        )
        context?.let { BackgroundUpdatingService.invalidatePeriodicRequests(it) }
    }

    /**
     * Save the current selected options and recreate the activity to change the theme and the darkmmode.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private fun save() {
        val position = themeSpinner.selectedItemPosition
        val chosenThemeId: Int
        when (position) {
            1 -> chosenThemeId = R.style.Theme_AppTheme_2
            else -> chosenThemeId = R.style.Theme_AppTheme_1
        }

        if (chosenThemeId != viewModel.getChosenThemeId() || darkMode.isChecked != viewModel.getChosenDarkMode()) {
            viewModel.setChosenThemeId(chosenThemeId)
            viewModel.setChosenDarkMode(darkMode.isChecked)

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
            viewModel.setChosenThemeId(chosenThemeId)
            viewModel.setChosenDarkMode(darkMode.isChecked)
            (activity as MainActivity).changeFragment(ExamOverviewFragment())

        }


    }
}