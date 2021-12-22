package com.Fachhochschulebib.fhb.pruefungsplaner

import androidx.appcompat.app.AppCompatActivity
import android.content.SharedPreferences
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import androidx.core.view.GravityCompat
import com.Fachhochschulebib.fhb.pruefungsplaner.data.AppDatabase
import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.os.*
import android.util.Log
import android.util.SparseArray
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.Fachhochschulebib.fhb.pruefungsplaner.data.TestPlanEntry
import kotlinx.android.synthetic.main.activity_suche.*
import java.lang.Exception

//Alexander Lange Start
import kotlinx.android.synthetic.main.hauptfenster.*
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Runnable
import java.text.SimpleDateFormat
import java.util.*
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.SearchAutoComplete
import androidx.core.view.get

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
 * @since 1.5
 * @see Filter
 */
class MainActivity : AppCompatActivity() {

    /**
     * Inner Class to filter the table of moduls. Used by TermineFragment-fragment and FavoritenFragment-fragment.
     *
     * @author Alexander Lange
     * @since 1.5
     * @see Terminefragment
     * @see Favoritenfragment
     */
    object Filter {
        /**
         * Parameter to lock the Filter.
         * If it is set to true, all listener are not going to be called when a value is changed.
         * If it is set back to false, the [filterChanged()]-Method is called.
         *
         * @author Alexander Lange
         * @since 1.5
         */
        var locked = false
            //TODO Check if working
            set(value) {
                field = value
                if (!value) {
                    filterChanged()
                }
            }

        /**
         * Parameter to Filter with the Modulename.
         * Calls the [onModuleNameChangedListener] and the [onFilterChangedListener].
         *
         * @author Alexander Lange
         * @since 1.5
         * @see onModulNameChangedListener
         * @see onFilterChangedListener
         */
        var modulName: String? = null
            set(value) {
                field = value
                if (locked) {
                    return
                }
                modulNameChanged()
                filterChanged()
            }

        /**
         * Parameter to Filter with the Coursename.
         * Calls the [onCourseNameChangedListener] and the [onFilterChangedListener].
         *
         * @author Alexander Lange
         * @since 1.5
         * @see onCourseNameChangedListener
         * @see onFilterChangedListener
         */
        var courseName: String? = null
            set(value) {
                field = value
                if (locked) {
                    return
                }
                courseNameChanged()
                filterChanged()
            }

        /**
         * Parameter to Filter with a specific date.
         * Calls the [onDateChangedListener] and the [onFilterChangedListener].
         *
         * @author Alexander Lange
         * @since 1.5
         * @see onDateChangedListener
         * @see onFilterChangedListener
         */
        var datum: Date? = null
            set(value) {
                field = value
                if (locked) {
                    return
                }
                dateChanged()
                filterChanged()
            }

        /**
         * Parameter to filter with a specific examiner.
         * Calls the [onExaminerChangedListener] and [onFilterChangedListener].
         * @author Alexander Lange
         * @since 1.5
         * @see onExaminerChangedListener
         * @see onFilterChangedListener
         */
        var examiner: String? = null
            set(value) {
                field = value
                if (locked) {
                    return
                }
                examinerChanged()
                filterChanged()
            }

        /**
         * Array of 6 semester, where each field contains a boolean, if the semester is selected (true), or not (false)
         *
         * @author Alexander Lange
         * @since 1.5
         */
        var semester: Array<Boolean> = arrayOf(true, true, true, true, true, true)
            set(value) {
                return
            }

        /**
         * Public method to set the value for a specific semester.
         * Calls the [onSemesterChangedListener] and the [onFilterChangedListener]
         * @param[pSemester] The semester to set the value.
         * @param[active] If the semester is checked or not.
         * @author Alexander Lange
         * @since 1.5
         * @see onSemesterChangedListener
         * @see onFilterChangedListener

         */
        fun SetSemester(pSemester: Int, active: Boolean) {
            semester[pSemester] = active
            if (locked) {
                return
            }
            semesterChanged()
            filterChanged()
        }


        /**
         * Invokes every Method, appended to the onModuleNameChangedListener.
         *
         * @author Alexander Lange
         * @since 1.5
         * @see onModulNameChangedListener
         */
        private fun modulNameChanged() {
            Log.d("Filter", "Modul changed")
            for (i in onModulNameChangedListener) {
                i.invoke()
            }
        }

        /**
         * Invokes every Method, appended to the onCourseNameChangedListener.
         *
         * @author Alexander Lange
         * @since 1.5
         * @see onCourseNameChangedListener
         */
        private fun courseNameChanged() {
            Log.d("Filter", "Course changed")
            for (i in onCourseNameChangedListener) {
                i.invoke()
            }
        }

        /**
         * Invokes every Method, appended to the onDateChangedListener.
         *
         * @author Alexander Lange
         * @since 1.5
         * @see onDateChangedListener
         */
        private fun dateChanged() {
            for (i in onDateChangedListener) {
                i.invoke()
            }
        }

        /**
         * Invokes every method, appended to the onExaminerChangedListener.
         *
         * @author Alexander Lange
         * @since 1.5
         * @see onExaminerChangedListener
         */
        private fun examinerChanged() {
            for (i in onExaminerChangedListener) {
                i.invoke()
            }

        }

        /**
         * Invokes every method, appended to the [onSemesterChangedListener].
         *
         * @author Alexander Lange
         * @since 1.5
         * @see onSemesterChangedListener
         */
        private fun semesterChanged() {
            for (i in onSemesterChangedListener) {
                i.invoke()
            }

        }

        /**
         * Invokes every Method, appended to the onFilterChangedListener.
         *
         * @author Alexander Lange
         * @since 1.5
         * @see onFilterChangedListener
         */
        private fun filterChanged() {
            for (i in onFilterChangedListener) {
                i.invoke()
            }
        }

        /**
         * Validates a testplanentry-Object. Checks if all Filter-values agree with the given entry.
         *
         * @param[context] Current context
         * @param[entry] The Entry that needs to be validated
         * @return true->The entry agrees with the filter,false->the entry does not agree with the filter
         * @author Alexander Lange
         * @since 1.5
         */
        fun validateFilter(context: Context?, entry: TestPlanEntry?): Boolean {
            if (context == null) {
                return false
            }
            val database = AppDatabase.getAppDatabase(context)
            if (entry == null) {
                return false
            }
            if (entry.module?.lowercase()?.startsWith(
                    modulName?.lowercase() ?: entry.module?.lowercase() ?: "-1"
                ) == false
            ) {
                return false
            }
            if (entry.course?.lowercase()?.startsWith(
                    courseName?.lowercase() ?: entry.course?.lowercase() ?: "-1"
                ) == false
            ) {
                return false
            }
            if (this.datum != null) {
                val sdf = SimpleDateFormat("yyyy-MM-dd")
                val date = sdf.parse(entry.date)
                if (!this.datum!!.atDay(date)) {
                    return false
                }
            }
            if (entry.firstExaminer?.lowercase()?.startsWith(
                    examiner?.lowercase() ?: entry.firstExaminer?.lowercase() ?: "-1"
                ) == false
            ) {
                return false
            }
            if (entry.semester != null && !semester[entry.semester!!.toInt().minus(1)]) {
                return false
            }
            return true
        }

        /**
         * Resets the Filter, sets every value to null.
         * Calls the onResetListener.
         *
         * @author Alexander Lange
         * @since 1.5
         */
        fun reset() {
            courseName = null
            modulName = null
            datum = null
            semester.fill(true)
            for (i in onResetListener) {
                i.invoke()
            }
        }

        //Declaration and empty initilization of every listener. To add an Method, write: listenerX.add{ ... }
        var onModulNameChangedListener: MutableList<() -> Unit> = mutableListOf()
        var onCourseNameChangedListener: MutableList<() -> Unit> = mutableListOf()
        var onDateChangedListener: MutableList<() -> Unit> = mutableListOf()
        var onExaminerChangedListener: MutableList<() -> Unit> = mutableListOf()
        var onSemesterChangedListener: MutableList<() -> Unit> = mutableListOf()
        var onFilterChangedListener: MutableList<() -> Unit> = mutableListOf()
        var onResetListener: MutableList<() -> Unit> = mutableListOf()
    }

    var sharedPrefsFaculty: SharedPreferences? = null
    var mSharedPreferencesValidation: SharedPreferences? = null
    var sharedPreferencesPeriod: SharedPreferences? = null

    var examineYear: String? = null
    var currentExaminePeriode: String? = null
    var returnCourse: String? = null
    var database: AppDatabase? = null

    val scope_io = CoroutineScope(CoroutineName("IO-Scope") + Dispatchers.IO)
    val scope_ui = CoroutineScope(CoroutineName("UI-Scope") + Dispatchers.Main)

    /**
     * Overrides the onCreate()-Method, which is called first in the Fragment-LifeCycle.
     * In this Method, the global parameter which are independent of the UI get initialized,
     * like the App-SharedPreferences and the reference to the Room-Database
     *
     * @since 1.5
     * @author Alexander Lange (E-Mail:alexander.lange@fh-bielefeld.de)
     * @see Fragment.onCreate
     */
    override fun onCreate(savedInstanceState: Bundle?) {

        applySettings()
        super.onCreate(savedInstanceState)

        setContentView(R.layout.hauptfenster)

        sharedPrefsFaculty = getSharedPreferences("faculty", Context.MODE_PRIVATE)
        mSharedPreferencesValidation = getSharedPreferences("validation", Context.MODE_PRIVATE)
        sharedPreferencesPeriod = getSharedPreferences("currentPeriode", Context.MODE_PRIVATE)
        database = AppDatabase.getAppDatabase(applicationContext)

        // Start Merlin Gürtler
        // registriert die Toolbar
        setSupportActionBar(header)
        header.setTitleTextColor(Color.WHITE)//TODO Set reference
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


        // Nun aus Shared Preferences
        examineYear = mSharedPreferencesValidation?.getString("examineYear", "0")
        currentExaminePeriode =
            mSharedPreferencesValidation?.getString("currentPeriode", "0")
        returnCourse = mSharedPreferencesValidation?.getString("returnCourse", "0")
        // Ende Merlin Gürtler

        /* TODO REMOVE
        if (!nv.isFocused())
        {
            dl.setVisibility(View.GONE);
        }


        dl.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                // Called when a drawer's position changes.
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Called when a drawer has settled in a completely open state.
                // The drawer is interactive at this point.
                // If you have 2 drawers (left and right) you can distinguish
                // them by using id of the drawerView. int id = drawerView.getId();
                // id will be your layout's id: for example R.id.left_drawer
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                dl.setVisibility(View.GONE);
                // Called when a drawer has settled in a completely closed state.
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                // Called when the drawer motion state changes.
                // The new state will be one of STATE_IDLE, STATE_DRAGGING or STATE_SETTLING.
            }
        });
         */


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
     * @since 1.5
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
        scope_io.launch {
            database?.userDao()?.moduleOrdered?.forEach { action -> list.add(action ?: "") }
        }.invokeOnCompletion {
            Handler(Looper.getMainLooper()).post {
                searchAutoComplete.setAdapter(
                    ArrayAdapter(
                        this,
                        R.layout.simple_spinner_item,
                        list
                    )
                )

                searchAutoComplete.setOnItemClickListener { adapterView, view, i, l ->
                    search.setQuery(adapterView.getItemAtPosition(i).toString(),true)
                }
                search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextChange(text: String?): Boolean {
                        Filter.modulName = if (text.isNullOrBlank()) null else text
                        return true
                    }

                    override fun onQueryTextSubmit(text: String?): Boolean {
                        changeFragment(Filter.modulName?:"Suche", Terminefragment())
                        return true
                    }
                })
            }
        }
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
                .setMessage("App wirklich beenden?")//TODO Extract String
                .setTitle("Beenden")
                .setPositiveButton("Beenden", DialogInterface.OnClickListener { dialog, which ->
                    setResult(0)
                    finishAffinity()
                })
                .setNegativeButton("Cancel", null)
                .create()
                .show()
        }
    }

    /**
     * Initializes the NavigationDrawer
     *
     * @author Alexander Lange
     * @since 1.5
     */
    private fun initNavigationDrawer() {
        val inputMethodManager = baseContext.getSystemService(
            INPUT_METHOD_SERVICE
        ) as InputMethodManager

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
                R.id.navigation_medication -> {
                    changeFragment(
                        applicationContext.getString(R.string.title_search),
                        searchFragment()
                    )
                    /*TODO REMOVE? //Menüpunkt Suche
                    //TODO Change to Coroutine
                    Thread {
                        val validation = examineYear + returnCourse + currentExaminePeriode
                        val rommData = AppDatabase.getAppDatabase(applicationContext)
                        val ppeList =
                            rommData?.userDao()?.getEntriesByValidation(validation)
                        Handler(Looper.getMainLooper()).post {
                            header?.title =
                                applicationContext.getString(R.string.title_search)
                            recyclerView4?.visibility = View.INVISIBLE//TODO REMOVE?
                            //TODO Check if needed or remove:caCalender?.visibility = View.GONE
                            //TODO Check if needed or remove:btnDatum?.visibility = View.GONE
                            drawer_layout.closeDrawer(GravityCompat.START)


                            //Suche Layout wird nicht aufgerufen wenn keine daten vorhanden sind
                            if (ppeList?.size ?: 0 < 2) {
                            } else {
                                ft.replace(R.id.frame_placeholder, searchFragment())
                                ft.commit()
                            }
                        }
                    }.start()
                    true*/
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
     * @since 1.5
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
     * Applies Settings from sharedPreferences to the activity.
     *
     * @author Alexander Lange
     * @since 1.5
     * @see Optionen
     */


    /**
     * Changes the Fragment.
     *
     * @param[headertitle] The title to set in the ActionBar (Toolbar).
     * @param[fragment] The fragment which shall be shown.
     * @return Returns always true, needed for the listener.
     * @author Alexander Lange
     * @since 1.5
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
     * @since 1.5
     * @see UpdateModulFilter
     * @see UpdateCourseFilter
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

        val sp_examiner =
            view.findViewById<Spinner>(R.id.layout_dialog_filter_examiner_sp)

        initFacultyFilter(this, tv_faculty)


        val c1 = view.findViewById<CheckBox>(R.id.layout_dialog_filter_semester_1)
        val c2 = view.findViewById<CheckBox>(R.id.layout_dialog_filter_semester_2)
        val c3 = view.findViewById<CheckBox>(R.id.layout_dialog_filter_semester_3)
        val c4 = view.findViewById<CheckBox>(R.id.layout_dialog_filter_semester_4)
        val c5 = view.findViewById<CheckBox>(R.id.layout_dialog_filter_semester_5)
        val c6 = view.findViewById<CheckBox>(R.id.layout_dialog_filter_semester_6)

        //Initializes the view-components
        UpdateCourseFilter(this, sp_course)
        UpdateModulFilter(this, sp_modul)

        btn_reset?.setOnClickListener { MainActivity.Filter.reset() }

        initFilterExaminer(sp_examiner)

        initFilterCheckbox(c1, 1)
        initFilterCheckbox(c2, 2)
        initFilterCheckbox(c3, 3)
        initFilterCheckbox(c4, 4)
        initFilterCheckbox(c5, 5)
        initFilterCheckbox(c6, 6)

        //Sets filterhooks, so the menu dynamically changes when the filter changes
        Filter.onCourseNameChangedListener.add {
            UpdateModulFilter(this, sp_modul)
        }

        Filter.onResetListener.add {
            UpdateCourseFilter(this, sp_course)
            UpdateModulFilter(this, sp_modul)
        }

        val now = Calendar.getInstance().time
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
                DialogInterface.OnClickListener { dialog, which -> UserFilter(this) })
            .setView(view)
            .create()
        dialog.show()
    }

    /**
     * Initializes the examiner-filter in the filtermenu.
     * Creates an adapter with all first-examiners to imlement an autocompletion.
     * @param[sp_examiner] The [Spinner] that shall be initialized.
     * @author Alexander Lange
     * @since 1.5
     * @see AdapterView.OnItemSelectedListener.onItemSelected
     */
    private fun initFilterExaminer(sp_examiner: Spinner) {
        sp_examiner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
                Filter.examiner = if (position == 0) null else sp_examiner.selectedItem.toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                Filter.examiner = null
            }
        }
        val spinnerProfArrayList: MutableList<String?> = mutableListOf("Alle")
        scope_io.launch {
            val selectedCourse = mSharedPreferencesValidation?.getString("selectedCourse", "")
            spinnerProfArrayList.addAll(
                database?.userDao()?.getFirstExaminerDistinctSortedByName(selectedCourse)?.toList()
                    ?: mutableListOf()
            )
        }.invokeOnCompletion {
            Handler(Looper.getMainLooper()).post {
                val adapterProfAutoComplete = ArrayAdapter(
                    applicationContext,
                    android.R.layout.simple_list_item_1,
                    spinnerProfArrayList ?: mutableListOf()
                )
                sp_examiner.adapter = adapterProfAutoComplete
                sp_examiner.setSelection(Filter.examiner)
            }
        }

    }

    /**
     * Initializes a checkbox of the filter-menu.
     *
     * @param[c] The checkbox to be initialized.
     * @param[semester] The semester, the checkbox is representing
     * @author Alexander Lange
     * @since 1.5
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
     * @since 1.5
     * @see Filter
     */
    fun UserFilter(context: Context) {
        val cou_sel = mSharedPreferencesValidation?.getString("selectedCourse", null)

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
     * @param[sp_course] the spinner from the filtermenu
     * @author Alexander Lange
     * @since 1.5
     * @see setCourseSpinner
     * @see OpenFilterMenu
     * @see initFacultyFilter
     * @see UpdateModulFilter
     * @see Filter
     */
    private fun UpdateCourseFilter(context: Context, sp_course: Spinner) {
        try {
            var sp_course_adapter: ArrayAdapter<String>? = null
            val list: MutableList<String?> =
                mutableListOf<String?>("Alle")//TODO extract String

            scope_io.launch {
                //Get Courses from Room-Database
                val courses =
                    database?.userDao()?.getChoosenCourse(true)
                //Create a list of Course-Names
                courses?.forEach { course ->
                    list.add(course.toString())
                }
                //Create Spinneradapter from coursename-list
                sp_course_adapter = ArrayAdapter<String>(
                    context,
                    android.R.layout.simple_spinner_dropdown_item,
                    list
                )
            }.invokeOnCompletion {
                Handler(Looper.getMainLooper()).post(object : Runnable {
                    override fun run() {
                        setCourseSpinner(sp_course_adapter, sp_course)
                    }
                })
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
     * @since 1.5
     * @see UpdateCourseFilter
     * @see Filter
     */
    private fun setCourseSpinner(sp_course_adapter: ArrayAdapter<String>?, sp_course: Spinner) {
        //Pass created adapter to spinner
        sp_course.adapter = sp_course_adapter
        //Set selection of spinner to selection from filter (retrieve previous filter)
        sp_course.setSelection(Filter.courseName)
        //Set the onItemSelectedListener (called when the user selects a new item from this spinner)
        sp_course.onItemSelectedListener = object :
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
                }/*TODO REMOVE
                        //Return if user al
                        if (Filter.courseName == null && position == 0) {
                            return
                        }*/
                Filter.courseName =
                    if (position == 0) null else sp_course.selectedItem.toString()
            }

            //Should never been called.
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Filter.courseName = null
            }
        }
    }

    /**
     * Updates the Faculty-Filter-Spinner in the Filter-dialog.
     * Creates a list of faucultynames from the room-database and passes them to the spinner.
     *
     * @param[context] the current context
     * @param[tv_faculty] the spinner from the filtermenu
     * @author Alexander Lange
     * @since 1.5
     * @see setCourseSpinner
     * @see OpenFilterMenu
     * @see UpdateCourseFilter
     * @see UpdateModulFilter
     * @see Filter
     */
    private fun initFacultyFilter(context: Context, tv_faculty: TextView) {
        try {
            //Get a list of facultys from shared preferences
            val strFacultys = sharedPrefsFaculty?.getString("faculty", "0")
            val returnFaculty = mSharedPreferencesValidation?.getString("returnFaculty", "0")
            //Create a jsonarray from faculty-list
            val jsonArrayFacultys = JSONArray(strFacultys)

            var selected_faculty: String? = null

            if (strFacultys != null) {
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
     * @since 1.5
     * @see setCourseSpinner
     * @see OpenFilterMenu
     * @see UpdateCourseFilter
     * @see UpdateModulFilter
     * @see Filter
     */
    private fun UpdateModulFilter(context: Context, sp_modul: Spinner) {
        try {
            var sp_modul_adapter: ArrayAdapter<String>? = null
            var pos_selected: Int = 0
            val list: MutableList<String?> = mutableListOf("Alle")
            scope_io.launch {
                //Get filtered list of modules from room-database
                val modules =
                    if (Filter.courseName == null) database?.userDao()?.allEntries else database?.userDao()
                        ?.getEntriesByCourseName(Filter.courseName)
                //Loop through modules and create list of modulnames
                modules?.forEach { i ->
                    list.add(i?.module)
                }
            }.invokeOnCompletion {
                //Create spinneradapter from list
                sp_modul_adapter = ArrayAdapter<String>(
                    context,
                    android.R.layout.simple_spinner_dropdown_item,
                    list
                )
                Handler(Looper.getMainLooper()).post(object : Runnable {
                    override fun run() {
                        setModuleSpinner(sp_modul_adapter, sp_modul)
                    }
                })
            }
        } catch (ex: Exception) {
            Log.e("UpdateModuleFilter", ex.stackTraceToString())
        }
    }

    /**
     * Initializes the modulespinner with an adapter and an onItemSelectedListener.
     *
     * @param[sp_modul_adapter] The adapter to pass to the spinner
     * @param[sp_modul] the spinner from the filtermenu
     * @author Alexander Lange
     * @since 1.5
     * @see UpdateModulFilter
     * @see Filter
     */
    private fun setModuleSpinner(sp_modul_adapter: ArrayAdapter<String>?, sp_modul: Spinner) {
        //Pass spinneradapter to spinner
        sp_modul.adapter = sp_modul_adapter
        //Set selection of spinner to selection from filter (retrieve previous filter)
        sp_modul.setSelection(Filter.modulName)
        //Set the onItemSelectedListener (called when the user selects a new item from this spinner)
        sp_modul.onItemSelectedListener = object :
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
                Filter.modulName = if (position == 0) null else sp_modul.selectedItem.toString()
            }

            //Should never been called
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Filter.modulName = null
            }
        }
    }

    /**
     * Initilizes the Calendar-Button of the menu. If the user clicks the Button,
     * he is navigated to a DatePicker-Dialog where he can set a day to filter
     * the moduls.
     *
     * @param[btn_calendar] The Button which is shown in the menu.
     * @author Alexander Lange
     * @since 1.5
     * @see Calendar
     * @see DatePickerDialog
     * @see Filter
     */
    private fun setCalendarBtn(btn_calendar: ImageButton) {
        //Get start-and enddate from sharedPrefs
        val startDate: Date = SimpleDateFormat("dd/MM/yyyy").parse(
            sharedPreferencesPeriod?.getString(
                "startDate",
                "01/01/1990"
            ) ?: "01/01/1990"
        )
        val endDate: Date = SimpleDateFormat("dd/MM/yyyy").parse(
            sharedPreferencesPeriod?.getString(
                "endDate",
                "01/01/1990"
            ) ?: "01/01/1990"
        )
        val pickedDate = Filter.datum ?: SimpleDateFormat("dd/MM/yyyy").parse(
            sharedPreferencesPeriod?.getString(
                "startDate",
                "01/01/1990"
            ) ?: "01/01/1990"
        )
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
                dialog.datePicker.minDate = startDate.time
                dialog.datePicker.maxDate = endDate.time
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
