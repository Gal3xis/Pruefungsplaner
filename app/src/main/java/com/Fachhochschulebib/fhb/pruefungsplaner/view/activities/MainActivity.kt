package com.Fachhochschulebib.fhb.pruefungsplaner.view.activities

//Alexander Lange Start
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.content.res.Configuration
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
import com.Fachhochschulebib.fhb.pruefungsplaner.*
import com.Fachhochschulebib.fhb.pruefungsplaner.utils.*
import com.Fachhochschulebib.fhb.pruefungsplaner.utils.Filter
import com.Fachhochschulebib.fhb.pruefungsplaner.view.fragments.*
import com.Fachhochschulebib.fhb.pruefungsplaner.view.helper.MainActivityFragment
import com.Fachhochschulebib.fhb.pruefungsplaner.viewmodel.MainViewModel
import com.Fachhochschulebib.fhb.pruefungsplaner.viewmodel.ViewModelFactory
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
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(application)
        )[MainViewModel::class.java]
        applySettings(viewModel)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.hauptfenster)

        initActionBar()
        initNavigationDrawer()
        initBottomNavigationView()
        initFilterDialog()

        UserFilter(applicationContext)

        viewModel.updatePruefperiode()
        BackgroundUpdatingService.initPeriodicRequests(applicationContext)

        changeFragment( Terminefragment())

    }

    private fun initActionBar() {
        setSupportActionBar(header)
        header.setTitleTextColor(Utils.getColorFromAttr(R.attr.colorOnPrimaryDark, theme))
    }

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
        initMenu(menu)
        initSearchView(menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun initMenu(menu: Menu) {
        menu.clear()
        menuInflater.inflate(R.menu.action_menu, menu);
        menu.findItem(R.id.menu_item_filter).isVisible = true
        menu.findItem(R.id.menu_item_save).isVisible = false
    }

    private fun initSearchView(menu: Menu) {
        val search: SearchView = menu.findItem(R.id.menu_item_search).actionView as SearchView
        val searchAutoComplete: SearchAutoComplete = search.findViewById(R.id.search_src_text)
        viewModel.liveEntriesOrdered.observe(this) { entryList ->
            val list: MutableList<String> = mutableListOf()

            entryList?.forEach { entry -> list.add(entry.module ?: "") }
            searchAutoComplete.setAdapter(
                    ArrayAdapter(
                            this,
                            R.layout.simple_spinner_item,
                            list
                    )
            )
        }

        searchAutoComplete.setOnItemClickListener { adapterView, view, i, l ->
            search.setQuery(adapterView.getItemAtPosition(i).toString(), true)
        }
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(text: String?): Boolean {
                return true
            }

            override fun onQueryTextSubmit(text: String?): Boolean {
                Filter.reset()
                Filter.modulName = if (text.isNullOrBlank()) null else text
                changeFragment( Terminefragment(false))
                return true
            }
        })
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
                openFilterMenu()
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
        if(supportFragmentManager.fragments.last()::class==Terminefragment::class){
            CloseApp()
            return
        }
        changeFragment(Terminefragment())
    }



    /**
     * Initializes the NavigationDrawer
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private fun initNavigationDrawer() {
        val states = arrayOf(intArrayOf(android.R.attr.state_enabled))
        val colors = intArrayOf(
                Utils.getColorFromAttr(R.attr.colorOnBackground, theme)
        )

        nav_view.setBackgroundColor(Utils.getColorFromAttr(R.attr.colorBackground, theme))
        nav_view.itemTextColor = ColorStateList(states, colors)
        nav_view.itemIconTintList = ColorStateList(states, colors)

        header.setNavigationOnClickListener {
            onNavigationDrawerButtonClicked()
        }
        nav_view.setNavigationItemSelectedListener { item ->
            onNavigationDrawerItemClicked(item)
        }
    }

    private fun onNavigationDrawerButtonClicked() {
        closeKeyboard()
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            drawer_layout.openDrawer(GravityCompat.START)
        }
    }

    private fun onNavigationDrawerItemClicked(item: MenuItem): Boolean {
        closeKeyboard()
        return when (item.itemId) {
            R.id.navigation_calender -> {
                changeFragment(
                        Terminefragment()
                )
            }
            R.id.navigation_settings -> {
                changeFragment(
                        SettingsFragment()
                )
            }
            R.id.navigation_feedback -> {
                changeFragment(
                        FeedbackFragment()
                )
            }
            //TODO CHANGE
            R.id.navigation_changeFaculty -> {
                /*header?.title = TODO CHECK
                        applicationContext.getString(R.string.title_changeFaculty)
                recyclerView4?.visibility = View.INVISIBLE
                drawer_layout.closeDrawer(GravityCompat.START)*/
                viewModel.deleteSelectedCourse()
                val myIntent = Intent(recyclerView4.context, StartActivity::class.java)
                recyclerView4.context.startActivity(myIntent)
                true
            }
            R.id.navigation_addCourse -> {
                changeFragment(

                        ChangeCoursesFragment()
                )
            }
            else -> true
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

        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            closeKeyboard()
            when (item.itemId) {
                R.id.navigation_calender -> {
                    changeFragment(

                            Terminefragment(supportFragmentManager.fragments.last()::class==Terminefragment::class)
                    )
                }
                R.id.navigation_diary -> {
                    changeFragment(
                            Favoritenfragment()
                    )
                }
                else -> true
            }
        }
    }

    private fun closeKeyboard() {
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
    fun changeFragment( fragment: MainActivityFragment): Boolean {
        val ft = supportFragmentManager.beginTransaction()
        recyclerView4?.visibility = View.INVISIBLE
        header?.title = fragment.name
        drawer_layout.closeDrawer(GravityCompat.START)
        ft.replace(R.id.frame_placeholder, fragment)
        ft.commit()

        return true
    }

    private var filterDialog: AlertDialog?=null


    private fun initFilterDialog(){
        //Create view for the dialog
        val view = layoutInflater.inflate(R.layout.layout_dialog_filter, null, false)

        //Initializes the view-components
        initFilterHeader(this, view)
        initFilterCourse(this, view)
        initFilterModule(this, view)
        initFilterSemesterBoxes(this, view)
        initFilterExaminer(this, view)
        initFilterCalendar(this, view)

        filterDialog = AlertDialog.Builder(this, R.style.AlertDialog_Filter)
            .setTitle("Filter")
            .setPositiveButton("Ok", null)
            .setNegativeButton(
                "Reset"
            ) { _, _ -> UserFilter(this) }
            .setView(view)
            .create()
    }

    /**
     * Creates and opens the Filter-Dialog.
     *
     * @author Alexander Lange
     * @since 1.6
     * @see initFilterModule
     * @see initFilterCourse
     * @see initFacultyFilter
     * @see Filter
     */
    private fun openFilterMenu() {

        filterDialog?.show()
    }


    private fun initFilterHeader(context: Context, filterVIew: View) {
        val tvFaculty = filterVIew.findViewById<TextView>(R.id.layout_dialog_filter_faculty_tv)
        viewModel.liveSelectedFaculty.observe(this) {
            tvFaculty.text = it?.facultyName ?: "No Faculty selected"
        }
        viewModel.getSelectedFaculty()
    }

    private fun initFilterSemesterBoxes(context: Context, filterView: View) {
        val c1 = filterView.findViewById<CheckBox>(R.id.layout_dialog_filter_semester_1)
        val c2 = filterView.findViewById<CheckBox>(R.id.layout_dialog_filter_semester_2)
        val c3 = filterView.findViewById<CheckBox>(R.id.layout_dialog_filter_semester_3)
        val c4 = filterView.findViewById<CheckBox>(R.id.layout_dialog_filter_semester_4)
        val c5 = filterView.findViewById<CheckBox>(R.id.layout_dialog_filter_semester_5)
        val c6 = filterView.findViewById<CheckBox>(R.id.layout_dialog_filter_semester_6)

        initFilterCheckbox(c1, 1)
        initFilterCheckbox(c2, 2)
        initFilterCheckbox(c3, 3)
        initFilterCheckbox(c4, 4)
        initFilterCheckbox(c5, 5)
        initFilterCheckbox(c6, 6)
    }

    /**
     * Initializes the examiner-filter in the filtermenu.
     * Creates an adapter with all first-examiners to imlement an autocompletion.
     * @param[spExaminer] The [Spinner] that shall be initialized.
     * @author Alexander Lange
     * @since 1.6
     * @see AdapterView.OnItemSelectedListener.onItemSelected
     */
    private fun initFilterExaminer(context: Context, filterView: View) {
        val spExaminer = filterView.findViewById<Spinner>(R.id.layout_dialog_filter_examiner_sp)
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
                            val tv = child as TextView
                        tv.setTextColor(
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
        viewModel.liveProfList.observe(this) {
            val list = mutableListOf("Alle")
            it?.let { it1 -> list.addAll(it1) }
            val profAdapter = ArrayAdapter(
                    applicationContext,
                    android.R.layout.simple_spinner_dropdown_item,
                    list
            )
            spExaminer.adapter = profAdapter
            spExaminer.setSelection(Filter.examiner)
        }
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
        val selectedCourse = viewModel.getSelectedCourse()
        Filter.reset()
        Filter.courseName = selectedCourse
        initFilterDialog()
    }

    /**
     * Updates the Course-Filter-Spinner in the Filter-dialog.
     * Creates a list of coursenames from the room-database and passes them to the spinner.
     *
     * @param[context] the current context
     * @param[spCourse] the spinner from the filtermenu
     * @author Alexander Lange
     * @since 1.6
     * @see openFilterMenu
     * @see initFacultyFilter
     * @see initFilterModule
     * @see Filter
     */
    private fun initFilterCourse(context: Context, filterView: View) {
        try {
            val spCourse = filterView.findViewById<Spinner>(R.id.layout_dialog_filter_course_sp)

            viewModel.liveChoosenCourses.observe(this) {
                val list = mutableListOf<String>("Alle")//TODO extract String
                it?.forEach { course ->
                    list.add(course.courseName)
                }
                spCourse.adapter = ArrayAdapter<String>(
                        context,
                        android.R.layout.simple_spinner_dropdown_item,
                        list
                )
                spCourse.setSelection(Filter.courseName)
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
                    viewModel.filterCoursename()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    Filter.courseName = null
                }
            }


        } catch (ex: Exception) {
            Log.e("table-UpdateCourseFilter:", ex.stackTraceToString())
        }
    }

    /**
     * Updates the Faculty-Filter-Spinner in the Filter-dialog.
     * Creates a list of faucultynames from the room-database and passes them to the spinner.
     *
     * @param[context] the current context
     * @param[tv_faculty] the spinner from the filtermenu
     * @author Alexander Lange
     * @since 1.6
     * @see openFilterMenu
     * @see initFilterCourse
     * @see initFilterModule
     * @see Filter
     */
    private fun initFacultyFilter(context: Context, tv_faculty: TextView) {
        try {
            val strFaculties = viewModel.getFaculties()
            val returnFaculty = viewModel.getReturnFaculty()
            val jsonArrayFacultys = JSONArray(strFaculties)
            var selectedFaculty: String? = null
            if (strFaculties != null) {
                var i = 0
                while (i < jsonArrayFacultys.length()) {
                    val json: JSONObject? = jsonArrayFacultys.getJSONObject(i)
                    if (json?.get("fbid").toString() == returnFaculty) {
                        selectedFaculty = json?.get("facName")?.toString()
                    }
                    i++
                }

            }
            tv_faculty.text = selectedFaculty
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
     * @see openFilterMenu
     * @see initFilterCourse
     * @see initFilterModule
     * @see Filter
     */
    private fun initFilterModule(context: Context, filterView: View) {
        try {
            val spModul = filterView.findViewById<Spinner>(R.id.layout_dialog_filter_modul_sp)
            viewModel.liveEntriesForCourse.observe(this) {
                val list: MutableList<String?> = mutableListOf("Alle")
                it?.forEach {
                    list.add(it.module)
                }
                spModul.adapter = ArrayAdapter<String>(
                        context,
                        android.R.layout.simple_spinner_dropdown_item,
                        list
                )
                spModul.setSelection(Filter.modulName)
            }
            spModul.onItemSelectedListener = object :
                    AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                ) {
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
                    Filter.modulName = if (position == 0) null else spModul.selectedItem.toString()
                }

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
    private fun initFilterCalendar(context: Context, filterView: View) {
        val imgbtnDate = filterView.findViewById<ImageButton>(R.id.layout_dialog_filter_date_ib)
        val tvDate = filterView.findViewById<TextView>(R.id.layout_dialog_filter_date_tv)


        //Get start-and enddate from sharedPrefs
        tvDate.text = if (Filter.datum == null) "Alle" else SimpleDateFormat("dd.MM.yyyy").format(
                Filter.datum!!
        )

        imgbtnDate.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val startDate = viewModel.getStartDate()
                val endDate = viewModel.getEndDate()
                val pickedDate = Filter.datum ?: viewModel.getStartDate()
                //Extract day,month and year from startDate as startParameter for the Calendar
                val year: Int = pickedDate?.let { SimpleDateFormat("yyyy").format(it).toInt() } ?: 0
                val month: Int = pickedDate?.let { SimpleDateFormat("MM").format(it).toInt() } ?: 0
                val day: Int = pickedDate?.let { SimpleDateFormat("dd").format(it).toInt() } ?: 0
                //Create DatePicker
                val dialog = DatePickerDialog(
                        this,
                        { _, pyear, pmonthOfYear, pdayOfMonth ->
                            val date = Calendar.getInstance()
                            date.set(pyear, pmonthOfYear, pdayOfMonth,0,0,0)
                            Filter.datum = date.time
                            tvDate.text = Filter.datum?.let { SimpleDateFormat("dd.MM.yyyy",Locale.getDefault()).format(it) }
                        },
                        year,
                        month - 1,
                        day
                )
                startDate?.let { dialog.datePicker.minDate = it.time }
                endDate?.let { dialog.datePicker.maxDate = it.time }
                dialog.setButton(
                        DatePickerDialog.BUTTON_NEUTRAL,
                        "Alle"
                ) { _, _ ->
                    Filter.datum = null
                    tvDate.text="Alle"
                }
                dialog.show()

            } else {
                null
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }
}
