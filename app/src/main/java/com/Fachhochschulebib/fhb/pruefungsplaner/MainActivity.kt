package com.Fachhochschulebib.fhb.pruefungsplaner

//Alexander Lange Start
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.*
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.SearchAutoComplete
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.hauptfenster.*
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

//Alexander Lange End

//////////////////////////////
// Tabelle
//
// autor:
// inhalt:  Verwaltung der Aufrufe von Fragmenten. Hier ist der "navigation bar" hinterlegt.
// zugriffsdatum: 20.2.20
//
//
//////////////////////////////
// Eigentlich die Hauptklasse wurde noch nicht umgenannt
// von hier werden die fragmente aufgerufen
/**
 * Main-Class, Controls the mainpart of the app except the Startpage, where the user picks his faculty and courses in the MainActivity.kt.
 * The MainWindow is the TermineFragment.fragment, where the user can view and pick the exams.
 *
 * @author Alexander Lange (Email:alexander.lange@fh-bielefeld.de)
 * @since 1.6
 * @see Filter
 */
class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    /**
     * Overrides the onCreate()-Method, which is called first in the Fragment-LifeCycle.
     *
     * @since 1.6
     * @author Alexander Lange (E-Mail:alexander.lange@fh-bielefeld.de)
     * @see Fragment.onCreate
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        applySettings()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.hauptfenster)
        viewModel = ViewModelProvider(
            this,
            MainViewModelFactory(application)
        )[MainViewModel::class.java]

        // Start Merlin Gürtler
        // registriert die Toolbar
        setSupportActionBar(header)
        header.setTitleTextColor(Utils.getColorFromAttr(R.attr.colorOnPrimaryDark, theme))
        val inputMethodManager = baseContext.getSystemService(
            INPUT_METHOD_SERVICE
        ) as InputMethodManager
        header.setNavigationOnClickListener { // Merlin Gürtler schließe die Tastatur falls offen
            try {
                inputMethodManager.hideSoftInputFromWindow(
                    this@MainActivity.currentFocus!!.windowToken, 0
                )
            } catch (e: Exception) {
                Log.d("Exception", "Keyboard not open")
            }
            // Änderung Merlin Gürtler
            // Toggelt die Sichtbarkeit des Drawers
            if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                drawer_layout.closeDrawer(GravityCompat.START)
            } else {
                drawer_layout.openDrawer(GravityCompat.START)
            }
        }

        initNavigationDrawer()
        initBottomNavigationView()

        //TODO Alexander Lange Start
        UserFilter(applicationContext)
        //TODO Alexander Lange End

        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.frame_placeholder, Terminefragment())
        ft.commit()
    }

    //Start Alexander Lange
    /**
     * Overrides the onCreateOptionsMenu()-Method, used to create the action menu in the top-right corner.
     * The menu contains the Filter-button and the search-button.
     *
     * @param[menu] The menu from this fragment. The action-menu is after inflation assigend to this.
     * @return Return true to show the menu, if it returns false, the menu is hidden
     * @author Alexander Lange
     * @since 1.6
     * @see AppCompatActivity.onCreateOptionsMenu
     * @see SearchView.autofill
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.clear()
        menuInflater.inflate(R.menu.action_menu, menu);
        menu.findItem(R.id.menu_item_filter).isVisible = true
        menu.findItem(R.id.menu_item_save).isVisible = false
        val search: SearchView = menu.findItem(R.id.menu_item_search).actionView as SearchView
        val searchAutoComplete: SearchAutoComplete = search.findViewById(R.id.search_src_text)
        val list: MutableList<String> = mutableListOf()
        viewModel.getModulesOrdered()?.forEach { action -> list.add(action ?: "") }
        searchAutoComplete.setAdapter(
            ArrayAdapter(
                this,
                R.layout.simple_spinner_item,
                list
            )
        )

        searchAutoComplete.setOnItemClickListener { adapterView, view, i, l ->
            search.setQuery(adapterView.getItemAtPosition(i).toString(), true)
        }
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(text: String?): Boolean {
                Filter.modulName = if (text.isNullOrBlank()) null else text
                return true
            }

            override fun onQueryTextSubmit(text: String?): Boolean {
                changeFragment(Filter.modulName ?: "Suche", Terminefragment())
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    /**
     * Overrides the onOptionsItemSelected()-Method, used to declare what happens if the user clicked on an menu-item.
     *
     * @param[item] The item which was selected from the user. Use item.itemId to identify the item.
     * @return return false for normal processing.
     * @author Alexander Lange
     * @since 1.6
     * @see AppCompatActivity.onOptionsItemSelected
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return (when (item.itemId) {
            R.id.menu_item_filter -> {
                OpenFilterMenu()
                false
            }
            else ->
                super.onOptionsItemSelected(item)
        })
    }

    /**
     * Overrides the onBackPressed()-Method. Is Called when the user presses the back-button on his smartphone.
     * Ending the App in a controlled manner.
     */
    override fun onBackPressed() {
        if (fragmentManager.backStackEntryCount > 0) {
            fragmentManager.popBackStack()

        } else {
            AlertDialog.Builder(this)
                .setMessage(R.string.close_app)
                .setTitle(R.string.title_close_app)
                .setPositiveButton(R.string.title_close_app) { _, _ ->
                    setResult(0)
                    finishAffinity()
                }
                .setNegativeButton("Cancel", null)
                .create()
                .show()
        }
    }

    /**
     * Initializes the NavigationDrawer
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private fun initNavigationDrawer() {
        val inputMethodManager = baseContext.getSystemService(
            INPUT_METHOD_SERVICE
        ) as InputMethodManager

        nav_view.setBackgroundColor(Utils.getColorFromAttr(R.attr.colorBackground, theme))

        val states = arrayOf(intArrayOf(android.R.attr.state_enabled))
        val colors = intArrayOf(
            Utils.getColorFromAttr(R.attr.colorOnBackground, theme)
        )

        nav_view.itemTextColor = ColorStateList(states, colors)
        nav_view.itemIconTintList = ColorStateList(states, colors)

        //Drawer Navigation Menü mit den Menüpunkten
        //Set listener for NavigationDrawer
        nav_view.setNavigationItemSelectedListener { item ->
            // Merlin Gürtler schließe die Tastatur falls offen
            try {
                inputMethodManager.hideSoftInputFromWindow(
                    this@MainActivity.currentFocus!!.windowToken, 0
                )
            } catch (e: Exception) {
                Log.d("Exception", "Keyboard not open")
            }
            //Fragmentmanager initialisierung
            val id = item.itemId
            when (id) {
                R.id.navigation_calender -> {
                    changeFragment(
                        applicationContext.getString(R.string.title_calender),
                        Terminefragment()
                    )
                }
                R.id.navigation_settings -> {
                    changeFragment(
                        applicationContext.getString(R.string.title_settings),
                        Optionen()
                    )
                }
                R.id.navigation_feedback -> {
                    changeFragment(
                        applicationContext.getString(R.string.title_feedback),
                        FeedbackFragment()
                    )
                }
                //TODO CHANGE
                R.id.navigation_changeFaculty -> {
                    header?.title =
                        applicationContext.getString(R.string.title_changeFaculty)
                    recyclerView4?.visibility = View.INVISIBLE//TODO REMOVE?
                    //TODO Check if needed or remove:caCalender?.visibility = View.GONE
                    //TODO Check if needed or remove:btnDatum?.visibility = View.GONE
                    drawer_layout.closeDrawer(GravityCompat.START)
                    // globale Variable, damit die Fakultät gewechselt werden kann
                    val globalVariable = applicationContext as StartClass
                    globalVariable.isChangeFaculty = true
                    val myIntent = Intent(recyclerView4.context, StartActivity::class.java)
                    recyclerView4.context.startActivity(myIntent)
                    true
                }
                R.id.navigation_addCourse -> {
                    changeFragment(
                        applicationContext.getString(R.string.title_changeCourse),
                        AddCourseFragment()
                    )
                }
                else -> true
            }
        }
    }

    /**
     * Initializes the BottomNavigationView
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private fun initBottomNavigationView() {
        //Set listener for BottomNavigationView

        bottom_navigation.setOnNavigationItemSelectedListener { item -> // Merlin Gürtler schließe die Tastatur falls offen
            val inputMethodManager = baseContext.getSystemService(
                INPUT_METHOD_SERVICE
            ) as InputMethodManager
            try {
                inputMethodManager.hideSoftInputFromWindow(
                    this@MainActivity.currentFocus!!.windowToken, 0
                )
            } catch (e: Exception) {
                Log.d("Exception", "Keyboard not open")
            }
            when (item.itemId) {
                R.id.navigation_calender -> {
                    changeFragment(
                        applicationContext.getString(R.string.title_calender),
                        Terminefragment()
                    )
                }
                R.id.navigation_diary -> {
                    changeFragment(
                        applicationContext.getString(R.string.title_exam),
                        Favoritenfragment()
                    )
                }
                else -> true
            }
        }
    }


    /**
     * Changes the Fragment.
     *
     * @param[headertitle] The title to set in the ActionBar (Toolbar).
     * @param[fragment] The fragment which shall be shown.
     * @return Returns always true, needed for the listener.
     * @author Alexander Lange
     * @since 1.6
     * @see onCreate
     */
    private fun changeFragment(headertitle: String, fragment: Fragment): Boolean {
        val ft = supportFragmentManager.beginTransaction()

        recyclerView4?.visibility = View.INVISIBLE//TODO REMOVE?

        header?.title = headertitle

        drawer_layout.closeDrawer(GravityCompat.START)

        ft.replace(R.id.frame_placeholder, fragment)
        ft.commit()
        return true
    }

    /**
     * Creates and opens the Filter-Dialog.
     *
     * @author Alexander Lange
     * @since 1.6
     * @see InitModulFilter
     * @see InitCourseFilter
     * @see initFacultyFilter
     * @see Filter
     */
    private fun OpenFilterMenu() {
        //Create view for the dialog
        val view = layoutInflater.inflate(R.layout.layout_dialog_filter, null, false)

        //Get view-Components
        val imgbtn_date = view.findViewById<ImageButton>(R.id.layout_dialog_filter_date_ib)
        val btn_reset = view.findViewById<Button>(R.id.layout_dialog_filter_reset_btn)
        val tv_faculty = view.findViewById<TextView>(R.id.layout_dialog_filter_faculty_tv)
        val tv_date = view.findViewById<TextView>(R.id.layout_dialog_filter_date_tv)
        val sp_modul = view.findViewById<Spinner>(R.id.layout_dialog_filter_modul_sp)
        val sp_course = view.findViewById<Spinner>(R.id.layout_dialog_filter_course_sp)

        val spExaminer =
            view.findViewById<Spinner>(R.id.layout_dialog_filter_examiner_sp)

        viewModel.liveSelectedFaculty.observe(this){
            tv_faculty.text = it?.facultyName?:"No Faculty selected"
        }
        viewModel.updateFaculty()

        val c1 = view.findViewById<CheckBox>(R.id.layout_dialog_filter_semester_1)
        val c2 = view.findViewById<CheckBox>(R.id.layout_dialog_filter_semester_2)
        val c3 = view.findViewById<CheckBox>(R.id.layout_dialog_filter_semester_3)
        val c4 = view.findViewById<CheckBox>(R.id.layout_dialog_filter_semester_4)
        val c5 = view.findViewById<CheckBox>(R.id.layout_dialog_filter_semester_5)
        val c6 = view.findViewById<CheckBox>(R.id.layout_dialog_filter_semester_6)

        //Initializes the view-components
        InitCourseFilter(this, sp_course)
        InitModulFilter(this, sp_modul)

        btn_reset?.setOnClickListener { Filter.reset() }

        initFilterExaminer(spExaminer)
        initFilterCheckbox(c1, 1)
        initFilterCheckbox(c2, 2)
        initFilterCheckbox(c3, 3)
        initFilterCheckbox(c4, 4)
        initFilterCheckbox(c5, 5)
        initFilterCheckbox(c6, 6)

        Filter.onDateChangedListener.add {
            tv_date.text =
                if (Filter.datum == null) "Alle" else SimpleDateFormat("dd.MM.yyyy").format(
                    Filter.datum!!
                )
        }

        setCalendarBtn(imgbtn_date)

        tv_date.text = if (Filter.datum == null) "Alle" else SimpleDateFormat("dd.MM.yyyy").format(
            Filter.datum!!
        )
        //Create and open the dialog
        val dialog = AlertDialog.Builder(this, R.style.AlertDialog_Filter)
            .setTitle("Filter")
            .setPositiveButton("Ok", null)
            .setNegativeButton(
                "Reset",
                { _, _ -> UserFilter(this) })
            .setView(view)
            .create()
        dialog.show()
    }

    /**
     * Initializes the examiner-filter in the filtermenu.
     * Creates an adapter with all first-examiners to imlement an autocompletion.
     * @param[spExaminer] The [Spinner] that shall be initialized.
     * @author Alexander Lange
     * @since 1.6
     * @see AdapterView.OnItemSelectedListener.onItemSelected
     */
    private fun initFilterExaminer(spExaminer: Spinner) {
        spExaminer.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (parent?.childCount ?: 0 > 0) {
                    val child = parent?.getChildAt(0)
                    //Set accurate textcolor for the selected item
                    if (child != null) {
                        (child as TextView).setTextColor(
                            Utils.getColorFromAttr(
                                R.attr.colorOnPrimary,
                                theme
                            )
                        )
                    }
                }
                Filter.examiner = if (position == 0) null else spExaminer.selectedItem.toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                Filter.examiner = null
            }
        }
        val spinnerProfArrayList: MutableList<String?> = mutableListOf("Alle")
        viewModel.liveFilteredEntriesByDate.observe(this){
            it?.forEach {
               spinnerProfArrayList.add(it.firstExaminer)
            }
        }
        val adapterProfAutoComplete = ArrayAdapter(
            applicationContext,
            android.R.layout.simple_list_item_1,
            spinnerProfArrayList
        )
        spExaminer.adapter = adapterProfAutoComplete
        spExaminer.setSelection(Filter.examiner)
    }

    /**
     * Initializes a checkbox of the filter-menu.
     *
     * @param[c] The checkbox to be initialized.
     * @param[semester] The semester, the checkbox is representing
     * @author Alexander Lange
     * @since 1.6
     */
    private fun initFilterCheckbox(c: CheckBox, semester: Int) {
        c.isChecked = Filter.semester[semester - 1]
        c.setOnCheckedChangeListener { buttonView, isChecked ->
            Filter.SetSemester(semester - 1, isChecked)
        }
    }

    /**
     * Sets the Filter for the users default configuration.
     * Reads the faculty and maincourse from shared preferences and sets the filter to this values.
     *
     * @param[context] The current Context
     * @author Alexander Lange
     * @since 1.6
     * @see Filter
     */
    fun UserFilter(context: Context) {
        val cou_sel = viewModel.getSelectedCourse()

        //Disable the callback from Filter. Only sets its values.
        Filter.locked = true
        Filter.reset()
        Filter.courseName = cou_sel
        //Resets the callbacks from the Filter
        Filter.locked = false
    }

    /**
     * Updates the Course-Filter-Spinner in the Filter-dialog.
     * Creates a list of coursenames from the room-database and passes them to the spinner.
     *
     * @param[context] the current context
     * @param[spCourse] the spinner from the filtermenu
     * @author Alexander Lange
     * @since 1.6
     * @see setCourseSpinner
     * @see OpenFilterMenu
     * @see initFacultyFilter
     * @see InitModulFilter
     * @see Filter
     */
    private fun InitCourseFilter(context: Context, spCourse: Spinner) {
        try {
            var spCourseAdapter: ArrayAdapter<String>? = null
            viewModel.liveCourses.observe(this) {
                val list = mutableListOf<String>("Alle")//TODO extract String
                it?.forEach {
                    it.courseName?.let { it1 -> list.add(it1) }
                }
                spCourse.adapter = ArrayAdapter<String>(
                    context,
                    android.R.layout.simple_spinner_dropdown_item,
                    list
                )
                //Set selection of spinner to selection from filter (retrieve previous filter)
                spCourse.setSelection(Filter.courseName)
                //Set the onItemSelectedListener (called when the user selects a new item from this spinner)
            }
            spCourse.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (parent?.childCount ?: 0 > 0) {
                        val child = parent?.getChildAt(0)
                        //Set accurate textcolor for the selected item
                        if (child != null) {
                            (child as TextView).setTextColor(
                                Utils.getColorFromAttr(
                                    R.attr.colorOnPrimary,
                                    theme
                                )
                            )
                        }
                    }
                    Filter.courseName =
                        if (position == 0) null else spCourse.selectedItem.toString()
                }

                //Should never been called.
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    Filter.courseName = null
                }
            }


        } catch (ex: Exception) {
            Log.e("table-UpdateCourseFilter:", ex.stackTraceToString())
        }
    }

    /**
     * Initializes the coursespinner with an adapter and an onItemSelectedListener.
     *
     * @param[sp_course_adapter] The adapter to pass to the spinner
     * @param[sp_course] the spinner from the filtermenu
     * @author Alexander Lange
     * @since 1.6
     * @see InitCourseFilter
     * @see Filter
     */
    private fun setCourseSpinner(sp_course_adapter: ArrayAdapter<String>?, sp_course: Spinner) {
    }

    /**
     * Updates the Faculty-Filter-Spinner in the Filter-dialog.
     * Creates a list of faucultynames from the room-database and passes them to the spinner.
     *
     * @param[context] the current context
     * @param[tv_faculty] the spinner from the filtermenu
     * @author Alexander Lange
     * @since 1.6
     * @see setCourseSpinner
     * @see OpenFilterMenu
     * @see InitCourseFilter
     * @see InitModulFilter
     * @see Filter
     */
    private fun initFacultyFilter(context: Context, tv_faculty: TextView) {
        try {
            //Get a list of facultys from shared preferences
            val strFaculties = viewModel.getFaculties()
            val returnFaculty = viewModel.getReturnFaculty()
            //Create a jsonarray from faculty-list
            val jsonArrayFacultys = JSONArray(strFaculties)

            var selected_faculty: String? = null

            if (strFaculties != null) {
                //Loop through jsonarray and create list of facultynames
                var i = 0
                while (i < jsonArrayFacultys.length()) {
                    //Get json-object from jsonarray
                    val json: JSONObject? = jsonArrayFacultys.getJSONObject(i)
                    //compare facultyid from filter with selected facultyid
                    if (json?.get("fbid").toString() == returnFaculty) {
                        //if the facultys agree, save position as selected. Retrieve selection from filter
                        selected_faculty = json?.get("facName")?.toString()
                    }
                    i++
                }

            }
            //Create spinneradapter from stringlist
            tv_faculty.setText(selected_faculty)
        } catch (ex: Exception) {
            System.err.println(ex.stackTrace)
        }
    }

    /**
     * Updates the Modul-Filter-Spinner in the Filter-dialog.
     * Creates a list of faucultynames from the room-database and passes them to the spinner.
     *
     * @param[context] the current context
     * @param[sp_faculty] the spinner from the filtermenu
     * @author Alexander Lange
     * @since 1.6
     * @see setCourseSpinner
     * @see OpenFilterMenu
     * @see InitCourseFilter
     * @see InitModulFilter
     * @see Filter
     */
    private fun InitModulFilter(context: Context, spModul: Spinner) {
        try {
            var sp_modul_adapter: ArrayAdapter<String>? = null
            val list: MutableList<String?> = mutableListOf("Alle")
            //Get filtered list of modules from room-database
            viewModel.liveFilteredEntriesByDate.observe(this){
                it?.forEach {
                    list.add(it.module)
                }
            }
            //Create spinneradapter from list
            spModul.adapter = ArrayAdapter<String>(
                context,
                android.R.layout.simple_spinner_dropdown_item,
                list
            )
            //Pass spinneradapter to spinner
            //Set selection of spinner to selection from filter (retrieve previous filter)
            spModul.setSelection(Filter.modulName)
            //Set the onItemSelectedListener (called when the user selects a new item from this spinner)
            spModul.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    //Set accurate textcolor for the selected item
                    if (parent?.childCount ?: 0 > 0) {
                        val child = parent?.getChildAt(0)
                        if (child != null) {
                            (child as TextView).setTextColor(
                                Utils.getColorFromAttr(
                                    R.attr.colorOnPrimary,
                                    theme
                                )
                            )
                        }
                    }
                    /*if (Filter.modulName == null && position == 0) {
                        return
                    }*/
                    Filter.modulName = if (position == 0) null else spModul.selectedItem.toString()
                }
                //Should never been called
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    Filter.modulName = null
                }
            }
        } catch (ex: Exception) {
            Log.e("UpdateModuleFilter", ex.stackTraceToString())
        }
    }

    /**
     * Initilizes the Calendar-Button of the menu. If the user clicks the Button,
     * he is navigated to a DatePicker-Dialog where he can set a day to filter
     * the moduls.
     *
     * @param[btn_calendar] The Button which is shown in the menu.
     * @author Alexander Lange
     * @since 1.6
     * @see Calendar
     * @see DatePickerDialog
     * @see Filter
     */
    private fun setCalendarBtn(btn_calendar: ImageButton) {
        //Get start-and enddate from sharedPrefs
        val startDate = viewModel.getStartDate()
        val endDate = viewModel.getEndDate()
        val pickedDate = Filter.datum ?: viewModel.getStartDate()
        //Extract day,month and year from startDate as startParameter for the Calendar
        val year: Int = SimpleDateFormat("yyyy").format(pickedDate).toInt()
        val month: Int = SimpleDateFormat("MM").format(pickedDate).toInt()
        val day: Int = SimpleDateFormat("dd").format(pickedDate).toInt()

        btn_calendar.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                //Create DatePicker
                val dialog = DatePickerDialog(
                    this,
                    DatePickerDialog.OnDateSetListener { view, pyear, pmonthOfYear, pdayOfMonth ->

                        Log.d("DatePicker-YEAR", pyear.toString())
                        Log.d("DatePicker-MONTH", pmonthOfYear.toString())
                        Log.d("DatePicker-DAY", pdayOfMonth.toString())

                        val date = Calendar.getInstance()
                        date.set(pyear, pmonthOfYear, pdayOfMonth)
                        Filter.datum = date.time
                    },
                    year,
                    month - 1,
                    day
                )
                startDate?.let { dialog.datePicker.minDate = it.time }
                endDate?.let { dialog.datePicker.maxDate = it.time }
                dialog.setButton(
                    DatePickerDialog.BUTTON_NEUTRAL,
                    "Alle",
                    { dialog, which ->
                        Filter.datum = null
                    })
                dialog.show()

            } else {
                null
            }
        }
    }
}
