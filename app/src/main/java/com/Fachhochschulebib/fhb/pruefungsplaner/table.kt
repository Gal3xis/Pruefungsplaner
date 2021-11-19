package com.Fachhochschulebib.fhb.pruefungsplaner

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import android.widget.CalendarView
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import android.content.SharedPreferences
import android.os.Bundle
import com.Fachhochschulebib.fhb.pruefungsplaner.R
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import androidx.core.view.GravityCompat
import com.Fachhochschulebib.fhb.pruefungsplaner.table
import com.Fachhochschulebib.fhb.pruefungsplaner.Terminefragment
import com.Fachhochschulebib.fhb.pruefungsplaner.data.AppDatabase
import com.Fachhochschulebib.fhb.pruefungsplaner.data.TestPlanEntry
import android.os.Looper
import com.Fachhochschulebib.fhb.pruefungsplaner.searchFragment
import com.Fachhochschulebib.fhb.pruefungsplaner.Favoritenfragment
import com.Fachhochschulebib.fhb.pruefungsplaner.Optionen
import com.Fachhochschulebib.fhb.pruefungsplaner.ChoiceModulSearchFragment
import com.Fachhochschulebib.fhb.pruefungsplaner.FeedbackFragment
import com.Fachhochschulebib.fhb.pruefungsplaner.StartClass
import android.content.Intent
import android.graphics.Color
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.Fachhochschulebib.fhb.pruefungsplaner.MainActivity
import com.Fachhochschulebib.fhb.pruefungsplaner.AddCourseFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.lang.Exception

//Alexander Lange Start
import kotlinx.android.synthetic.main.hauptfenster.*
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
    override fun onCreateOptionsMenu(menu: Menu):Boolean {
        menuInflater.inflate(R.menu.action_menu, menu);
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return (when(item.itemId) {
            R.id.menu_item_filter -> {
                OpenFilterMenu()
                true
            }
            else ->
                super.onOptionsItemSelected(item)
        })
    }

    private fun OpenFilterMenu() {
        val view = layoutInflater.inflate(R.layout.layout_dialog_filter,null,false)

        val imgbtn_date = view.findViewById<ImageButton>(R.id.layout_dialog_filter_date_ib)
        val tv_date = view.findViewById<TextView>(R.id.layout_dialog_filter_date_tv)

        val calendar =  Calendar.getInstance()
        val local = Locale.getDefault()
        val sdf = SimpleDateFormat("dd.MM.yyyy",local)

        tv_date.text = sdf.format(calendar.time)

        val dialog = AlertDialog.Builder(this,R.style.AlertDialog_Filter)
            .setTitle("Filter")
            .setPositiveButton("Ok",null)
            .setNegativeButton("Cancel",null)
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
        Log.d("table","Test") //TODO REMOVE
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
        currentExaminePeriode = mSharedPreferencesValidation.getString("currentPeriode", "0")
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
        nav_view.setNavigationItemSelectedListener{ item ->
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
                        val ppeList = rommData?.userDao()?.getEntriesByValidation(validation)
                        Handler(Looper.getMainLooper()).post {
                            header?.title = applicationContext.getString(R.string.title_search)
                            recyclerView4?.visibility = View.INVISIBLE
                            //TODO Check if needed or remove:caCalender?.visibility = View.GONE
                            //TODO Check if needed or remove:btnDatum?.visibility = View.GONE
                            drawer_layout.closeDrawer(GravityCompat.START)


                            //Suche Layout wird nicht aufgerufen wenn keine daten vorhanden sind
                            if (ppeList?.size?:0 < 2) {
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
                    header?.title = applicationContext.getString(R.string.title_electiveModule)
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
                    header?.title = applicationContext.getString(R.string.title_changeFaculty)
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
                    header?.title = applicationContext.getString(R.string.title_changeCourse)
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
    private val mOnNavigationItemSelectedListener =
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
                    header?.title = applicationContext.getString(R.string.title_calender)
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
                    header?.title = applicationContext.getString(R.string.title_search)
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
                    header?.title = applicationContext.getString(R.string.title_exam)
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
                    header?.title = applicationContext.getString(R.string.title_settings)
                    recyclerView4?.visibility = View.INVISIBLE
                    //TODO Check if needed or remove:caCalender?.visibility = View.GONE
                    //TODO Check if needed or remove:btnDatum?.visibility = View.GONE
                    ft.replace(R.id.frame_placeholder, Optionen())
                    //ft.addToBackStack(null);
                    ft.commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_electiveModule -> {
                    header?.title = applicationContext.getString(R.string.title_electiveModule)
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