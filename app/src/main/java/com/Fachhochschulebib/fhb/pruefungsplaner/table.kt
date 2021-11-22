package com.Fachhochschulebib.fhb.pruefungsplaner

import androidx.appcompat.app.AppCompatActivity
import android.content.SharedPreferences
import android.os.Bundle
import android.app.AlertDialog
import android.content.Context
import androidx.core.view.GravityCompat
import com.Fachhochschulebib.fhb.pruefungsplaner.data.AppDatabase
import android.os.Looper
import android.content.Intent
import android.graphics.Color
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.lang.Exception

//Alexander Lange Start
import kotlinx.android.synthetic.main.hauptfenster.*
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
class table : AppCompatActivity() {
    var mSharedPreferencesValidation: SharedPreferences? = null
    var examineYear: String? = null
    var currentExaminePeriode: String? = null
    var returnCourse: String? = null

    //Start Alexander Lange
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.action_menu, menu);
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return (when (item.itemId) {
            R.id.menu_item_filter -> {
                OpenFilterMenu()
                true
            }
            else ->
                super.onOptionsItemSelected(item)
        })
    }

    object Filter {
        var modulName: String? = null
            set(value) {
                field = value
                modulNameChanged()
                filterChanged()
            }
        var courseName: String? = null
            set(value) {
                field = value
                courseNameChanged()
                filterChanged()
            }

        var facultyId: String? = null
            set(value) {
                field = value
                facultyIdChanged()
                filterChanged()
            }

        var datum: Date? = null
            set(value) {
                field = value
                dateChanged()
                filterChanged()
            }

        private fun modulNameChanged() {
            Log.d("Filter", "Modul changed")
            for (i in onModulNameChangedListener) {
                i.invoke()
            }
        }

        private fun courseNameChanged() {
            Log.d("Filter", "Course changed")
            for (i in onCourseNameChangedListener) {
                i.invoke()
            }
        }

        private fun facultyIdChanged() {
            Log.d("Filter", "Faculty changed")
            for (i in onFacultyIdChangedListener) {
                i.invoke()
            }
        }

        private fun dateChanged() {
            for (i in onDateChangedListener) {
                i.invoke()
            }
        }

        private fun filterChanged(){
            for (i in onFilterChangedListener) {
                i.invoke()
            }
        }

        var onModulNameChangedListener: MutableList<() -> Unit> = mutableListOf()
        var onCourseNameChangedListener: MutableList<() -> Unit> = mutableListOf()
        var onFacultyIdChangedListener: MutableList<() -> Unit> = mutableListOf()
        var onDateChangedListener: MutableList<() -> Unit> = mutableListOf()
        var onFilterChangedListener: MutableList<() -> Unit> = mutableListOf()
    }

    private fun InitFilterSpinner(context: Context,sp_faculty: Spinner,sp_course: Spinner,sp_modul: Spinner){

        UpdateModulFilter(context, sp_modul)
        UpdateCourseFilter(context, sp_course)
        UpdateFacultyFilter(context, sp_faculty)

        var i = 0
        val sharedPrefsFaculty: SharedPreferences =
            context.getSharedPreferences("faculty", Context.MODE_PRIVATE)
        val strFacultys = sharedPrefsFaculty.getString("faculty", "0")
        var jsonArrayFacultys = JSONArray(strFacultys)



        Handler(Looper.getMainLooper()).post{
            while(i < jsonArrayFacultys?.length()?:0){
                if(jsonArrayFacultys?.getJSONObject(i)?.get("fbid")?.equals(Filter.facultyId) == true)
                {
                    val sp_faculty_adapter:ArrayAdapter<String>? = sp_faculty.adapter as ArrayAdapter<String>?
                    sp_faculty.setSelection(sp_faculty_adapter?.getPosition(
                        jsonArrayFacultys!!.getJSONObject(i).get("facName").toString())?:0)
                }
                i++
            }

            val sp_course_adapter:ArrayAdapter<String>? = sp_course.adapter as ArrayAdapter<String>?
            sp_course_adapter?.getPosition(Filter.courseName)
                ?.let { sp_course.setSelection(it) }


            val sp_modul_adapter:ArrayAdapter<String>? = sp_modul.adapter as ArrayAdapter<String>?
            sp_modul_adapter?.getPosition(Filter.modulName)
                ?.let { sp_modul.setSelection(it) }
        }
    }

    private fun UpdateCourseFilter(context: Context, sp_course: Spinner) {
        try {
            var sp_course_adapter: ArrayAdapter<String>? = null
            Thread(object : Runnable {
                override fun run() {
                    val list: MutableList<String?>? =
                        mutableListOf<String?>("Alle")//TODO extract String
                    val database = AppDatabase.getAppDatabase(context)
                    if (list != null) {
                        val courses =
                            database?.userDao()?.getAllCoursesByFacultyId(Filter.facultyId)
                        if (courses != null) {
                            for (course in courses) {
                                list.add(course?.courseName)
                            }
                        }
                    }
                    if (list?.size ?: 0 > 0) {
                        sp_course_adapter = ArrayAdapter<String>(
                            context,
                            android.R.layout.simple_spinner_dropdown_item,
                            list!!
                        )
                    }
                    Handler(Looper.getMainLooper()).post {
                        sp_course.adapter = sp_course_adapter
                    }
                }
            }).start()
        } catch (ex: Exception) {
            System.err.println(ex.stackTrace)
        }
    }

    private fun UpdateFacultyFilter(context: Context, sp_faculty: Spinner) {
        try {
            var jsonArrayFacultys: JSONArray? = null
            var sp_faculty_adapter: ArrayAdapter<String>? = null
            Thread(object : Runnable {
                override fun run() {
                    val list = mutableListOf<String?>("Alle")//TODO extract String
                    val sharedPrefsFaculty: SharedPreferences =
                        context.getSharedPreferences("faculty", Context.MODE_PRIVATE)
                    val strFacultys = sharedPrefsFaculty.getString("faculty", "0")
                    if (strFacultys != null) {
                        try {
                            jsonArrayFacultys = JSONArray(strFacultys)
                            var i: Int = 0
                            while (i < jsonArrayFacultys?.length() ?: 0) {
                                val json: JSONObject? = jsonArrayFacultys?.getJSONObject(i)
                                list.add(json?.get("facName").toString())
                                i++
                            }
                            sp_faculty_adapter = ArrayAdapter<String>(
                                context,
                                android.R.layout.simple_spinner_dropdown_item,
                                list
                            )
                        } catch (b: Exception) {
                            Log.d(
                                "uebergabeAnSpinner",
                                "Fehler beim Parsen des Fakultätsnamen."
                            )
                        }
                    }
                    Handler(Looper.getMainLooper()).post {
                        sp_faculty.adapter = sp_faculty_adapter
                    }
                }
            }).start()
        } catch (ex: Exception) {
            System.err.println(ex.stackTrace)
        }
    }

    private fun UpdateModulFilter(context: Context, sp_modul: Spinner) {
        try {
            var sp_modul_adapter: ArrayAdapter<String>? = null
            Thread(object : Runnable {
                override fun run() {
                    val database = AppDatabase.getAppDatabase(context)
                    val list: MutableList<String?> = mutableListOf("Alle")
                    val modules = database?.userDao()?.getEntriesByCourseName(Filter.courseName)
                    if (modules != null) {
                        for (i in modules) {
                            list.add(i?.module)
                        }
                    }
                    if (list.size > 0) {
                        sp_modul_adapter = ArrayAdapter<String>(
                            context,
                            android.R.layout.simple_spinner_dropdown_item,
                            list
                        )
                    }

                    Handler(Looper.getMainLooper()).post(object : Runnable {
                        override fun run() {
                            sp_modul.adapter = sp_modul_adapter
                        }
                    })
                }
            }).start()
        } catch (ex: Exception) {
            Log.e("UpdateModuleFilter", ex.stackTraceToString())
        }
    }

    private fun OpenFilterMenu() {
        val view = layoutInflater.inflate(R.layout.layout_dialog_filter, null, false)
        val context = this

        //View-Components
        val imgbtn_date = view.findViewById<ImageButton>(R.id.layout_dialog_filter_date_ib)
        val tv_date = view.findViewById<TextView>(R.id.layout_dialog_filter_date_tv)
        val sp_modul = view.findViewById<Spinner>(R.id.layout_dialog_filter_modul_sp)
        val sp_course = view.findViewById<Spinner>(R.id.layout_dialog_filter_course_sp)
        val sp_faculty = view.findViewById<Spinner>(R.id.layout_dialog_filter_faculty_sp)

        Filter.onFacultyIdChangedListener.add { UpdateCourseFilter(context, sp_course) }
        Filter.onCourseNameChangedListener.add { UpdateModulFilter(context, sp_modul) }

        sp_faculty.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                Log.d("sp_studiengang", "Selected new item")
                val sharedPrefsFaculty: SharedPreferences =
                    context.getSharedPreferences("faculty", Context.MODE_PRIVATE)
                val strFacultys = sharedPrefsFaculty.getString("faculty", "0")
                if (strFacultys != null) {
                    try {
                        val jsonArrayFacultys: JSONArray? = JSONArray(strFacultys)
                        for (i in 0 until (jsonArrayFacultys?.length() ?: 0)) {
                            val json = jsonArrayFacultys?.getJSONObject(i)
                            val facName = json?.get("facName").toString()
                            val selectedFaculty = sp_faculty.selectedItem.toString()
                            if (facName.equals(selectedFaculty)) {
                                Filter.facultyId = if(position==0)null else json?.get("fbid").toString()
                                break
                            }
                        }
                    } catch (b: Exception) {
                        Log.d(
                            "uebergabeAnSpinner",
                            "Fehler beim Parsen des Fakultätsnamen."
                        )
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                Log.d("sp_studiengang", "Nothing selected")
            }
        }

        sp_course.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                try {
                    Thread(object : Runnable {
                        override fun run() {
                            val database = AppDatabase.getAppDatabase(context)
                            val selectedCourse = sp_course.selectedItem.toString()
                            Filter.courseName =if(position==0)null else  selectedCourse
                        }
                    }).start()

                } catch (ex: Exception) {
                    Log.e("UpdateCourseFilter", ex.stackTraceToString())
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Filter.courseName = null
            }
        }

        sp_modul.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Filter.modulName =if(position==0)null else sp_modul.selectedItem.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Filter.modulName = null
            }
        }

        InitFilterSpinner(context,sp_faculty,sp_course,sp_modul)

        //Time Management
        val calendar = Calendar.getInstance()
        val local = Locale.getDefault()
        val sdf = SimpleDateFormat("dd.MM.yyyy", local)

        tv_date.text = sdf.format(calendar.time)

        val dialog = AlertDialog.Builder(this, R.style.AlertDialog_Filter)
            .setTitle("Filter")
            .setPositiveButton("Ok", null)
            .setNegativeButton("Cancel", null)
            .setView(view)
            .create()
        dialog.show()
    }

//End Alexander Lange

    //Loginhandler login = new Loginhandler();
//aufruf der starteinstelllungen
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.hauptfenster)
        Log.d("table", "Test") //TODO REMOVE
        // Start Merlin Gürtler
        // registriert die Toolbar
        setSupportActionBar(header)
        header.setTitleTextColor(Color.WHITE)
        val inputMethodManager = baseContext.getSystemService(
            INPUT_METHOD_SERVICE
        ) as InputMethodManager
        header.setNavigationOnClickListener { // Merlin Gürtler schließe die Tastatur falls offen
            try {
                inputMethodManager.hideSoftInputFromWindow(
                    this@table.currentFocus!!.windowToken, 0
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
        val mSharedPreferencesValidation = getSharedPreferences("validation", 0)
        examineYear = mSharedPreferencesValidation.getString("examineYear", "0")
        currentExaminePeriode =
            mSharedPreferencesValidation.getString("currentPeriode", "0")
        returnCourse = mSharedPreferencesValidation.getString("returnCourse", "0")
        // Ende Merlin Gürtler

        /*
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


        //Drawer Navigation Menü mit den Menüpunkten
        nav_view.setNavigationItemSelectedListener { item ->
            // Merlin Gürtler schließe die Tastatur falls offen
            try {
                inputMethodManager.hideSoftInputFromWindow(
                    this@table.currentFocus!!.windowToken, 0
                )
            } catch (e: Exception) {
                Log.d("Exception", "Keyboard not open")
            }
            //Fragmentmanager initialisierung
            val id = item.itemId
            val ft = supportFragmentManager.beginTransaction()
            when (id) {
                R.id.navigation_calender -> {
                    //Menüpunkt termine
                    header?.title = applicationContext.getString(R.string.title_calender)
                    recyclerView4?.visibility = View.INVISIBLE
                    //TODO Check if needed or remove:caCalender?.visibility = View.GONE
                    //TODO Check if needed or remove:btnDatum?.visibility = View.GONE
                    //dl.setVisibility(View.GONE);
                    drawer_layout.closeDrawer(GravityCompat.START)
                    ft.replace(R.id.frame_placeholder, Terminefragment())
                    ft.commit()
                    true
                }
                R.id.navigation_medication -> {
                    //Menüpunkt Suche
                    //TODO Change to Coroutine
                    Thread {
                        val validation = examineYear + returnCourse + currentExaminePeriode
                        val rommData = AppDatabase.getAppDatabase(applicationContext)
                        val ppeList =
                            rommData?.userDao()?.getEntriesByValidation(validation)
                        Handler(Looper.getMainLooper()).post {
                            header?.title =
                                applicationContext.getString(R.string.title_search)
                            recyclerView4?.visibility = View.INVISIBLE
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
                    true
                }
                R.id.navigation_diary -> {
                    header?.title = applicationContext.getString(R.string.title_exam)
                    recyclerView4?.visibility = View.INVISIBLE
                    //TODO Check if needed or remove:caCalender?.visibility = View.GONE
                    //TODO Check if needed or remove:btnDatum?.visibility = View.GONE
                    drawer_layout.closeDrawer(GravityCompat.START)
                    ft.replace(R.id.frame_placeholder, Favoritenfragment())
                    ft.commit()
                    true
                }
                R.id.navigation_settings -> {
                    header?.title = applicationContext.getString(R.string.title_settings)
                    recyclerView4?.visibility = View.INVISIBLE
                    //TODO Check if needed or remove:caCalender?.visibility = View.GONE
                    //TODO Check if needed or remove:btnDatum?.visibility = View.GONE
                    drawer_layout.closeDrawer(GravityCompat.START)
                    ft.replace(R.id.frame_placeholder, Optionen())
                    ft.commit()
                    true
                }
                R.id.navigation_electiveModule -> {
                    header?.title =
                        applicationContext.getString(R.string.title_electiveModule)
                    recyclerView4?.visibility = View.INVISIBLE
                    //TODO Check if needed or remove:caCalender?.visibility = View.GONE
                    //TODO Check if needed or remove:btnDatum?.visibility = View.GONE
                    drawer_layout.closeDrawer(GravityCompat.START)
                    ft.replace(R.id.frame_placeholder, ChoiceModulSearchFragment())
                    ft.commit()
                    true
                }
                R.id.navigation_feedback -> {
                    header?.title = applicationContext.getString(R.string.title_feedback)
                    recyclerView4?.visibility = View.INVISIBLE
                    //TODO Check if needed or remove:caCalender?.visibility = View.GONE
                    //TODO Check if needed or remove:btnDatum?.visibility = View.GONE
                    drawer_layout.closeDrawer(GravityCompat.START)
                    ft.replace(R.id.frame_placeholder, FeedbackFragment())
                    ft.commit()
                    true
                }
                R.id.navigation_changeFaculty -> {
                    header?.title =
                        applicationContext.getString(R.string.title_changeFaculty)
                    recyclerView4?.visibility = View.INVISIBLE
                    //TODO Check if needed or remove:caCalender?.visibility = View.GONE
                    //TODO Check if needed or remove:btnDatum?.visibility = View.GONE
                    drawer_layout.closeDrawer(GravityCompat.START)
                    // globale Variable, damit die Fakultät gewechselt werden kann
                    val globalVariable = applicationContext as StartClass
                    globalVariable.isChangeFaculty = true
                    val myIntent = Intent(recyclerView4.context, MainActivity::class.java)
                    recyclerView4.context.startActivity(myIntent)
                    true
                }
                R.id.navigation_addCourse -> {
                    header?.title =
                        applicationContext.getString(R.string.title_changeCourse)
                    recyclerView4?.visibility = View.INVISIBLE
                    //TODO Check if needed or remove:caCalender?.visibility = View.GONE
                    //TODO Check if needed or remove:btnDatum?.visibility = View.GONE
                    drawer_layout.closeDrawer(GravityCompat.START)
                    ft.replace(R.id.frame_placeholder, AddCourseFragment())
                    ft.commit()
                    true
                }
                else -> true
            }
        }


        //Userinterface Komponenten initialisieren
        val navigation = findViewById<View>(R.id.bottom_navigation) as BottomNavigationView
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.frame_placeholder, Terminefragment())
        ft.commit()
    }

    //navigation mit den menuepunkten Bottom
    private
    val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item -> // Merlin Gürtler schließe die Tastatur falls offen
            val inputMethodManager = baseContext.getSystemService(
                INPUT_METHOD_SERVICE
            ) as InputMethodManager
            try {
                inputMethodManager.hideSoftInputFromWindow(
                    this@table.currentFocus!!.windowToken, 0
                )
            } catch (e: Exception) {
                Log.d("Exception", "Keyboard not open")
            }
            val ft = supportFragmentManager.beginTransaction()
            when (item.itemId) {
                R.id.navigation_calender -> {
                    //fragment fuer das "terminefragment" layout
                    header?.title =
                        applicationContext.getString(R.string.title_calender)
                    recyclerView4?.visibility = View.INVISIBLE
                    //TODO Check if needed or remove:caCalender?.visibility = View.GONE
                    //TODO Check if needed or remove:btnDatum?.visibility = View.GONE
                    ft.replace(R.id.frame_placeholder, Terminefragment())
                    //ft.addToBackStack(null);
                    ft.commit()
                    return@OnNavigationItemSelectedListener true
                }
                //TODO Remove
                R.id.navigation_medication -> {
                    //fragment fuer das "activity_suche" layout
                    header?.title =
                        applicationContext.getString(R.string.title_search)
                    recyclerView4?.visibility = View.INVISIBLE
                    //TODO Check if needed or remove:caCalender?.visibility = View.GONE
                    //TODO Check if needed or remove:btnDatum?.visibility = View.GONE

                    ft.replace(R.id.frame_placeholder, searchFragment())
                    //ft.addToBackStack("suche");
                    ft.commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_diary -> {
                    //fragment fuer das "favoriten" layout
                    header?.title =
                        applicationContext.getString(R.string.title_exam)
                    recyclerView4?.visibility = View.INVISIBLE
                    //TODO Check if needed or remove:caCalender?.visibility = View.GONE
                    //TODO Check if needed or remove:btnDatum?.visibility = View.GONE
                    ft.replace(R.id.frame_placeholder, Favoritenfragment())
                    //ft.addToBackStack(null);
                    ft.commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_settings -> {
                    //fragment fuer das "Optionen" layout
                    header?.title =
                        applicationContext.getString(R.string.title_settings)
                    recyclerView4?.visibility = View.INVISIBLE
                    //TODO Check if needed or remove:caCalender?.visibility = View.GONE
                    //TODO Check if needed or remove:btnDatum?.visibility = View.GONE
                    ft.replace(R.id.frame_placeholder, Optionen())
                    //ft.addToBackStack(null);
                    ft.commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_electiveModule -> {
                    header?.title =
                        applicationContext.getString(R.string.title_electiveModule)
                    recyclerView4?.visibility = View.INVISIBLE
                    //TODO Check if needed or remove:caCalender?.visibility = View.GONE
                    //TODO Check if needed or remove:btnDatum?.visibility = View.GONE
                    ft.replace(R.id.frame_placeholder, ChoiceModulSearchFragment())
                    ft.commit()
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    override fun onBackPressed() {
        if (fragmentManager.backStackEntryCount > 0) {
            fragmentManager.popBackStack()
        } else {
            setResult(0)
            finish()
        }
    }
}