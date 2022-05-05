package com.fachhochschulebib.fhb.pruefungsplaner.view.activities

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
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
import com.fachhochschulebib.fhb.pruefungsplaner.*
import com.fachhochschulebib.fhb.pruefungsplaner.utils.*
import com.fachhochschulebib.fhb.pruefungsplaner.utils.Filter
import com.fachhochschulebib.fhb.pruefungsplaner.view.fragments.*
import com.fachhochschulebib.fhb.pruefungsplaner.view.helper.MainActivityFragment
import com.fachhochschulebib.fhb.pruefungsplaner.view.helper.MainFragmentPagerAdapter
import com.fachhochschulebib.fhb.pruefungsplaner.viewmodel.MainViewModel
import com.fachhochschulebib.fhb.pruefungsplaner.viewmodel.ViewModelFactory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Main-Class, Controls the main part of the app except the Startpage, where the user picks his faculty and courses in the MainActivity.kt.
 * The MainWindow is the [ExamOverviewFragment], where the user can view and pick exams.
 *
 * @author Alexander Lange (Email:alexander.lange@fh-bielefeld.de)
 * @since 1.6
 *
 */
class MainActivity : AppCompatActivity() {

    /**
     * The dialog that presents the filter to the user.
     */
    private var filterDialog: AlertDialog? = null

    /**
     * The ViewModel for the MainActivity. Is set in [onCreate].
     * @see MainViewModel
     */
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
        setContentView(R.layout.activity_main)
        initViewPager()
        initTabLayout()
        initActionBar()
        initNavigationDrawer()
        setPruefungszeitraum()
        //initBottomNavigationView()
        initFilterDialog()


        viewModel.updatePeriod()
        BackgroundUpdatingService.initPeriodicRequests(applicationContext)

        changeFragment(ExamOverviewFragment())
        userFilter()

    }

    /**
     * Initializes the Tab Layout. The tablayout presents the user multiple fragments as tabs with a navigation bar.
     * In this case, the tabs are [ExamOverviewFragment] and [FavoriteOverviewFragment].
     * With help of the viewPager, the user can switch between the fragments with sliding the page to the left or right.
     *
     * @author Alexander Lange
     * @since 1.6
     *
     * @see TabLayout
     * @see MainFragmentPagerAdapter
     */
    private fun initTabLayout(){
        TabLayoutMediator(activity_main_tab_layout,activity_main_viewpager
        ) {
                tab, position ->
            tab.text = when(position){
                1->resources.getString(R.string.tab_layout_exam)
                else->resources.getString(R.string.tab_layout_exam_overview)
            }
            activity_main_tab_layout.addOnTabSelectedListener(object:TabLayout.OnTabSelectedListener{
                override fun onTabReselected(tab: TabLayout.Tab?) {
                    when(tab?.position){
                        1->changeFragment(FavoriteOverviewFragment())
                        else->{
                            userFilter()
                            changeFragment(ExamOverviewFragment())
                        }
                    }
                }

                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when(tab?.position){
                        1->changeFragment(FavoriteOverviewFragment())
                        else->changeFragment(ExamOverviewFragment())
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {

                }
            })
        }.attach()
    }

    /**
     * Initializes the ViewPager that is attatched to the TabLayout.
     * Sets the adapter to the [MainFragmentPagerAdapter]
     *
     * @author Alexander Lange
     * @since 1.6
     *
     * @see MainFragmentPagerAdapter
     */
    private fun initViewPager(){
        activity_main_viewpager.adapter = MainFragmentPagerAdapter(this)
    }

    /**
     * Initialized the Actionbar
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private fun initActionBar() {
        setSupportActionBar(activity_main_toolbar)
        activity_main_toolbar.setTitleTextColor(Utils.getColorFromAttr(R.attr.colorOnPrimaryDark, this))
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

    /**
     * Initializes the menu in the actionbar, shows the filtericon and hides the savebutton.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private fun initMenu(menu: Menu) {
        menu.clear()
        menuInflater.inflate(R.menu.menu_actionbar, menu);
        menu.findItem(R.id.menu_item_filter).isVisible = true
    }

    /**
     * Initializes the searchview. The searchview is located in the actionbar, where the user
     * can search a specific module from everywhere in the Application except the start page.
     *
     * @param menu The menu in which the searchview is embedded.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private fun initSearchView(menu: Menu) {
        val search: SearchView = menu.findItem(R.id.menu_item_search).actionView as SearchView
        val searchAutoComplete: SearchAutoComplete = search.findViewById(R.id.search_src_text)
        viewModel.liveEntriesOrdered.observe(this) { entryList ->
            val list: MutableList<String> = mutableListOf()

            entryList?.forEach { entry -> list.add(entry.module ?: "") }
            searchAutoComplete.setAdapter(
                SimpleSpinnerAdapter(
                    this,
                    R.layout.layout_simpler_spinner_adapter_item,
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
                changeFragment(ExamOverviewFragment())
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
     * Ending the App in a controlled manner. That means if the [ExamOverviewFragment] is open, then the user is asked if he wants to exit the Application.
     * Else he is directed to the [ExamOverviewFragment]
     *
     * @author Alexander Lange
     * @since 1.6
     */
    override fun onBackPressed() {
        if (supportFragmentManager.fragments.last()::class == ExamOverviewFragment::class) {
            closeApp()
            return
        }
        changeFragment(ExamOverviewFragment())
    }

    /**
     * Initializes the NavigationDrawer. The nvaigationdrawer is the main tool for the user to switch between fragments.
     * It can be accessed via the hamburger-icon from the actionbar.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private fun initNavigationDrawer() {
        val states = arrayOf(intArrayOf(android.R.attr.state_enabled))
        val colors = intArrayOf(
            Utils.getColorFromAttr(R.attr.colorOnBackground, this)
        )

        activity_main_navigation_drawer.setBackgroundColor(Utils.getColorFromAttr(R.attr.colorBackground, this))
        activity_main_navigation_drawer.itemTextColor = ColorStateList(states, colors)
        activity_main_navigation_drawer.itemIconTintList = ColorStateList(states, colors)

        activity_main_toolbar.setNavigationOnClickListener {
            onNavigationDrawerButtonClicked()
        }
        activity_main_navigation_drawer.setNavigationItemSelectedListener { item ->
            onNavigationDrawerItemClicked(item)
        }
    }

    /**
     * Handler for when the user clicked the hamburger-icon in the actionbar to open or close the navigationdrawer.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private fun onNavigationDrawerButtonClicked() {
        closeKeyboard()
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            drawer_layout.openDrawer(GravityCompat.START)
        }
    }

    /**
     * Handler for when the user clicked an icon in the navigationdrawer. Directs him to the selected fragment.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private fun onNavigationDrawerItemClicked(item: MenuItem): Boolean {
        closeKeyboard()
        return when (item.itemId) {
            R.id.navigation_calender -> changeFragment(ExamOverviewFragment())
            R.id.navigation_settings -> changeFragment(SettingsFragment())
            R.id.navigation_feedback -> changeFragment(FeedbackFragment())
            R.id.navigation_changeFaculty -> {
                val intent = Intent(applicationContext, StartActivity::class.java)
                intent.putExtra(CHANGE_FLAG,true)
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
                applicationContext.startActivity(intent)
                true
            }
            R.id.navigation_addCourse -> changeFragment(ChangeCoursesFragment())
            else -> true
        }
    }

    /**
     * Closes the Keyboard of the smartphone.
     *
     * @author Alexander Lange
     * @since 1.6
     */
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
     * Directs the user to a new Fragment.
     *
     * @param[fragment] The fragment which shall be shown.
     *
     * @return Returns always true, needed for the listener.
     *
     * @author Alexander Lange
     * @since 1.6
     *
     */
    fun changeFragment(fragment: MainActivityFragment): Boolean {
        activity_main_toolbar?.title = fragment.name
        drawer_layout.closeDrawer(GravityCompat.START)
        activity_main_textview_current_period_timestamp.visibility = View.VISIBLE
        if(fragment::class==ExamOverviewFragment::class){
            activity_main_placeholder.visibility = View.INVISIBLE
            activity_main_viewpager.visibility = View.VISIBLE
            activity_main_viewpager.setCurrentItem(0,true)
            activity_main_viewpager.invalidate()
        }else if(fragment::class==FavoriteOverviewFragment::class){
            activity_main_placeholder.visibility = View.INVISIBLE
            activity_main_viewpager.visibility = View.VISIBLE
            activity_main_viewpager.setCurrentItem(1,true)
            activity_main_viewpager.invalidate()
        }else{
            val ft = supportFragmentManager.beginTransaction()
            activity_main_viewpager.visibility = View.INVISIBLE
            activity_main_textview_current_period_timestamp.visibility = View.GONE
            activity_main_placeholder.visibility = View.VISIBLE
            ft.replace(R.id.activity_main_placeholder, fragment)

            ft.commit()
        }

        return true
    }

    /**
     * Initializes the filterdialog. With the Filterdialog, the user can Filter through the shown exams.
     * The Filter for the [ExamOverviewFragment] and the [FavoriteOverviewFragment] are the same, so
     * a change in one Fragment is also a change in the other.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private fun initFilterDialog() {
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
            ) { _, _ -> userFilter() }
            .setView(view)
            .create()
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
    fun userFilter() {
        viewModel.liveMainCourse.observe(this){
            Filter.reset()
            Filter.courseName = it.courseName
            initFilterDialog()

        }
        viewModel.getMainCourse()
    }


    /**
     * Sets the text for the current period with content from shared preferences
     *
     * @author Alexander Lange
     * @since 1.6
     *
     */
    fun setPruefungszeitraum() {
        viewModel.getPeriodeTimeSpan()?.let { activity_main_textview_current_period_timestamp?.text = it }
    }

    /**
     * Creates and opens the Filter-Dialog.
     *
     * @author Alexander Lange
     * @since 1.6
     *
     */
    private fun openFilterMenu() {

        filterDialog?.show()
    }

    /**
     * Initilizes the header of the filter. The header shows the faculty, the user has chosen.
     *
     * @param context The applicationcontext.
     * @param filterView The view of the filter.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private fun initFilterHeader(context: Context, filterView: View) {
        val tvFaculty = filterView.findViewById<TextView>(R.id.layout_dialog_filter_textview_faculty)
        viewModel.liveSelectedFaculty.observe(this) {
            tvFaculty.text = it?.facultyName ?: "No Faculty selected"
        }
        viewModel.getSelectedFaculty()
    }

    /**
     * Initializes the checkboxes of the filter, where the user can pick specific semester.
     *
     * @param context The applicationcontext.
     * @param filterView The view of the filter.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private fun initFilterSemesterBoxes(context: Context, filterView: View) {
        val c1 = filterView.findViewById<CheckBox>(R.id.layout_dialog_filter_checkbox_semester_1)
        val c2 = filterView.findViewById<CheckBox>(R.id.layout_dialog_filter_checkbox_semester_2)
        val c3 = filterView.findViewById<CheckBox>(R.id.layout_dialog_filter_checkbox_semester_3)
        val c4 = filterView.findViewById<CheckBox>(R.id.layout_dialog_filter_checkbox_semester_4)
        val c5 = filterView.findViewById<CheckBox>(R.id.layout_dialog_filter_checkbox_semester_5)
        val c6 = filterView.findViewById<CheckBox>(R.id.layout_dialog_filter_checkbox_semester_6)

        initFilterCheckbox(c1, 1)
        initFilterCheckbox(c2, 2)
        initFilterCheckbox(c3, 3)
        initFilterCheckbox(c4, 4)
        initFilterCheckbox(c5, 5)
        initFilterCheckbox(c6, 6)
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
            Filter.setSemester(semester - 1, isChecked)
        }
    }

    /**
     * Initializes the examiner-filter in the filtermenu.
     * Creates an adapter with all first-examiners to imlement an autocompletion.
     *
     * @param context The applicationcontext.
     * @param filterView The view of the filter.
     *
     * @author Alexander Lange
     * @since 1.6
     *
     */
    private fun initFilterExaminer(context: Context, filterView: View) {
        val spExaminer = filterView.findViewById<Spinner>(R.id.layout_dialog_filter_spinner_examiner)
        spExaminer.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (view == null) return
                (view as TextView).setTextColor(
                    Utils.getColorFromAttr(
                        R.attr.colorOnPrimaryDark,
                        context
                    )
                )
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
                context,
                android.R.layout.simple_spinner_dropdown_item,
                list
            )
            spExaminer.adapter = profAdapter
            spExaminer.setSelection(Filter.examiner)
        }
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
            val spCourse = filterView.findViewById<Spinner>(R.id.layout_dialog_filter_spinner_course)

            viewModel.liveChoosenCourses.observe(this) {
                val list = mutableListOf<String>("Alle")//TODO extract String
                it?.forEach { course ->
                    list.add(course.courseName)
                }
                spCourse.adapter = ArrayAdapter(
                    context,
                    android.R.layout.simple_list_item_1,
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
                    if (view == null) return
                    (view as TextView).setTextColor(
                        Utils.getColorFromAttr(
                            R.attr.colorOnPrimaryDark,
                            context
                        )
                    )
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
            val spModul = filterView.findViewById<Spinner>(R.id.layout_dialog_filter_spinner_module)
            viewModel.liveEntriesForCourse.observe(this) { it ->
                val list: MutableList<String> = mutableListOf("Alle")
                it?.forEach { entry ->
                    list.add(entry.module ?: "Unnamed")
                }
                spModul.adapter = ArrayAdapter(
                    context,
                    android.R.layout.simple_list_item_1,
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
                    if (view == null) return
                    (view as TextView).setTextColor(
                        Utils.getColorFromAttr(
                            R.attr.colorOnPrimaryDark,
                            context
                        )
                    )
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
        val imgbtnDate = filterView.findViewById<ImageButton>(R.id.layout_dialog_filter_button_date)
        val tvDate = filterView.findViewById<TextView>(R.id.layout_dialog_filter_textview_date)


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
                        date.set(pyear, pmonthOfYear, pdayOfMonth, 0, 0, 0)
                        Filter.datum = date.time
                        tvDate.text = Filter.datum?.let {
                            SimpleDateFormat(
                                "dd.MM.yyyy",
                                Locale.getDefault()
                            ).format(it)
                        }
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
                    tvDate.text = "Alle"
                }
                dialog.show()

            } else {
                null
            }
        }
    }
}
