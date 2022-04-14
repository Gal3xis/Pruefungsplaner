package com.Fachhochschulebib.fhb.pruefungsplaner.view.fragments

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.os.Process
import android.view.*
import android.widget.Spinner
import android.widget.Switch
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.optionfragment.*
import java.lang.Exception
import android.view.*
import android.widget.AdapterView
import androidx.lifecycle.ViewModelProvider
import com.Fachhochschulebib.fhb.pruefungsplaner.R
import com.Fachhochschulebib.fhb.pruefungsplaner.utils.*
import com.Fachhochschulebib.fhb.pruefungsplaner.view.helper.MainActivityFragment
import com.Fachhochschulebib.fhb.pruefungsplaner.viewmodel.SettingsViewModel
import com.Fachhochschulebib.fhb.pruefungsplaner.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.hauptfenster.*
import kotlinx.android.synthetic.main.optionfragment.*


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
class SettingsFragment() : MainActivityFragment() {
    override var name: String = "Einstellungen"
    private lateinit var viewModel: SettingsViewModel

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
        val v = inflater.inflate(R.layout.optionfragment, container, false)
        return v
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


    private fun initView() {
        initThemeSpinner()
        initDarkModeSwitch()
        initUpdateDatabaseButton()
        initCalendarSynchronizationButton()
        initCalendarInsertionTypeSpinner()
        initPrivacyDeclarationButton()
        initImpressumButton()
        initDeleteDatabaseButton()
        initDeleteCalendarEntriesButton()
        initUpdateCalendarButton()
        initDeleteFavoritsButton()
        initSaveButton()
        initBackgroundUpdateSwitch()
        initBackgroundUpdateIntervallButton()
    }

    private fun initSaveButton() {
        optionenfragment_save_btn.setOnClickListener { save() }
    }

    private fun initDeleteFavoritsButton() {
        btnFav.setOnClickListener {
            viewModel.deleteFavorits(requireContext())
            Toast.makeText(
                view?.context,
                view?.context?.getString(R.string.delete_favorite),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun initUpdateCalendarButton() {
        viewModel.liveFavorits.observe(viewLifecycleOwner) {
            if (it != null) {
                viewModel.updateCalendar(requireContext(), it)
            }
        }
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

    private fun initDeleteCalendarEntriesButton() {
        btnCalClear.setOnClickListener {
            val eventIds = viewModel.getAllCalendarEntries(requireContext())
            val events = viewModel.getCalendarModuleNames(requireContext())

            AlertDialog.Builder(requireContext())
                .setTitle("Kalendereinträge löschen")
                .setMessage("Sollen ${eventIds.count()} Einträge gelöscht werden?\n Die Events sind: $events")
                .setPositiveButton("Ja") { _, _ -> viewModel.deleteCalendarEntries(requireContext()) }
                .setNegativeButton("Nein", null)
                .create()
                .show()
        }
    }


    private fun initDeleteDatabaseButton() {
        btnDB.setOnClickListener {
            viewModel.deleteAllEntries()
            Toast.makeText(
                view?.context,
                view?.context?.getString(R.string.delete_db),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun initImpressumButton() {
        optionenfragment_impressum?.setOnClickListener {
            changeFragment( ImpressumFragment())
        }
    }

    private fun changeFragment( fragment: MainActivityFragment) {
        val ft = activity?.supportFragmentManager?.beginTransaction()
        activity?.header?.title = fragment.name
        ft?.replace(R.id.frame_placeholder, fragment)
        ft?.commit()
    }

    private fun initPrivacyDeclarationButton() {
        privacyDeclaration.setOnClickListener {
            changeFragment( PrivacyDeclarationFragment())
        }
    }

    private fun initCalendarSynchronizationButton() {
        switch2.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setCalendarSync(requireContext(), isChecked)

        }
        switch2.isChecked = viewModel.getCalendarSync()
    }

    private fun initCalendarInsertionTypeSpinner(){
        val names = mutableListOf<String>()
        GoogleCalendarIO.InsertionTye.values().forEach {
            names.add(it.name)
        }
        calendarInsertionTypeSpinner.adapter =  SimpleSpinnerAdapter(requireContext(),android.R.layout.simple_list_item_1,names)
        calendarInsertionTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.setCalendarInserionType(GoogleCalendarIO.InsertionTye.valueOf(calendarInsertionTypeSpinner.selectedItem.toString()))
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
        calendarInsertionTypeSpinner.setSelection(viewModel.getCalendarInsertionType().name)
    }

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
    }

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

    private fun initBackgroundUpdateIntervallButton() {
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
        val position = theme.selectedItemPosition
        val chosenThemeId:Int
        when (position) {
            1 -> chosenThemeId = R.style.Theme_AppTheme_2
            else -> chosenThemeId = R.style.Theme_AppTheme_1
        }

        if(chosenThemeId!=viewModel.getChosenThemeId()||darkMode.isChecked!=viewModel.getChosenDarkMode()){
            viewModel.setChosenThemeId(chosenThemeId)
            viewModel.setChosenDarkMode(darkMode.isChecked)

            AlertDialog.Builder(requireContext()).setTitle("Neustart benötigt").setMessage("Jetzt Neustarten?").setPositiveButton("Neustarten"
                ) {  _,_->
                val pid = Process.myPid()
                Process.killProcess(pid)
                }.setNegativeButton("Nicht Neustarten"){
                        _,_->
                    changeFragment(Terminefragment())

                }.create().show()
        }else{
            viewModel.setChosenThemeId(chosenThemeId)
            viewModel.setChosenDarkMode(darkMode.isChecked)

            changeFragment(Terminefragment())
        }


    }
}