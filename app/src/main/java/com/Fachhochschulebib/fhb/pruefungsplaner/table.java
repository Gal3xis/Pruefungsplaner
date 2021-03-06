package com.Fachhochschulebib.fhb.pruefungsplaner;

//////////////////////////////
// Tabelle
//
// autor:
// inhalt:  Verwaltung der Aufrufe von Fragmenten. Hier ist der "navigation bar" hinterlegt.
// zugriffsdatum: 20.2.20
//
//
//////////////////////////////

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.Fachhochschulebib.fhb.pruefungsplaner.data.AppDatabase;
import com.Fachhochschulebib.fhb.pruefungsplaner.data.TestPlanEntry;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

// Eigentlich die Hauptklasse wurde noch nicht umgenannt
// von hier werden die fragmente aufgerufen
public class table extends AppCompatActivity  {
    static public FragmentTransaction ft ;
    private RecyclerView recyclerView;
    private CalendarView calendar;
    private Button btnSearch;
    private DrawerLayout dl;
    private NavigationView nv;
    private Toolbar header;

    SharedPreferences mSharedPreferencesValidation;
    String examineYear,
            currentExaminePeriode, returnCourse;
    //Loginhandler login = new Loginhandler();
    //aufruf der starteinstelllungen


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.hauptfenster);

        // Start Merlin G??rtler
        // registriert die Toolbar
        header = (Toolbar) findViewById(R.id.header);
        setSupportActionBar(header);
        header.setTitleTextColor(Color.WHITE);

        InputMethodManager inputMethodManager =
                (InputMethodManager) getBaseContext().getSystemService(
                        Activity.INPUT_METHOD_SERVICE);

        header.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Merlin G??rtler schlie??e die Tastatur falls offen
                try {
                    inputMethodManager.hideSoftInputFromWindow(
                            table.this.getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    Log.d("Exception", "Keyboard not open");
                }
                // ??nderung Merlin G??rtler
                // Toggelt die Sichtbarkeit des Drawers
                if(dl.isDrawerOpen(GravityCompat.START)) {
                    dl.closeDrawer(GravityCompat.START);
                } else {
                    dl.openDrawer(GravityCompat.START);
                }
            }
        });


        // Nun aus Shared Preferences
        mSharedPreferencesValidation
                = table.this.getSharedPreferences("validation", 0);

        examineYear = mSharedPreferencesValidation.getString("examineYear", "0");
        currentExaminePeriode = mSharedPreferencesValidation.getString("currentPeriode", "0");
        returnCourse = mSharedPreferencesValidation.getString("returnCourse", "0");
        // Ende Merlin G??rtler

        dl = findViewById(R.id.drawer_layout);

        nv = findViewById(R.id.nav_view);
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


        //Drawer Navigation Men?? mit den Men??punkten
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Merlin G??rtler schlie??e die Tastatur falls offen
                try {
                    inputMethodManager.hideSoftInputFromWindow(
                            table.this.getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    Log.d("Exception", "Keyboard not open");
                }
                //Fragmentmanager initialisierung
                int id = item.getItemId();
                ft = getSupportFragmentManager().beginTransaction();
                switch(id)
                {
                    case R.id.navigation_calender:
                        //Men??punkt termine
                        header.setTitle(getApplicationContext().getString(R.string.title_calender));
                        recyclerView.setVisibility(View.INVISIBLE);
                        calendar.setVisibility(View.GONE);
                        btnSearch.setVisibility(View.GONE);
                        //dl.setVisibility(View.GONE);
                        dl.closeDrawer(GravityCompat.START);
                        ft.replace(R.id.frame_placeholder, new Terminefragment());
                        ft.commit();
                        return true;

                    case R.id.navigation_medication:
                        //Men??punkt Suche
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String validation
                                        = examineYear + returnCourse + currentExaminePeriode;
                                AppDatabase rommData
                                        = AppDatabase.getAppDatabase(getApplicationContext());
                                List<TestPlanEntry> ppeList
                                        = rommData.userDao().getEntriesByValidation(validation);

                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        header.setTitle(getApplicationContext().getString(R.string.title_search));
                                        recyclerView.setVisibility(View.INVISIBLE);
                                        calendar.setVisibility(View.GONE);
                                        btnSearch.setVisibility(View.GONE);
                                        dl.closeDrawer(GravityCompat.START);


                                        //Suche Layout wird nicht aufgerufen wenn keine daten vorhanden sind
                                        if (ppeList.size() < 2) {
                                        }else{
                                            ft.replace(R.id.frame_placeholder, new searchFragment());
                                            ft.commit();
                                        }
                                    }
                                });
                            }
                        }).start();

                        return true;
                    case R.id.navigation_diary:
                        header.setTitle(getApplicationContext().getString(R.string.title_exam));
                        recyclerView.setVisibility(View.INVISIBLE);
                        calendar.setVisibility(View.GONE);
                        btnSearch.setVisibility(View.GONE);
                        dl.closeDrawer(GravityCompat.START);
                        ft.replace(R.id.frame_placeholder, new Favoritenfragment());
                        ft.commit();

                        return true;

                    case R.id.navigation_settings:
                        header.setTitle(getApplicationContext().getString(R.string.title_settings));
                        recyclerView.setVisibility(View.INVISIBLE);
                        calendar.setVisibility(View.GONE);
                        btnSearch.setVisibility(View.GONE);
                        dl.closeDrawer(GravityCompat.START);
                        ft.replace(R.id.frame_placeholder, new Optionen());
                        ft.commit();

                        return true;

                    // Start Merlin G??rtler
                    case R.id.navigation_electiveModule:
                        header.setTitle(getApplicationContext().getString(R.string.title_electiveModule));
                        recyclerView.setVisibility(View.INVISIBLE);
                        calendar.setVisibility(View.GONE);
                        btnSearch.setVisibility(View.GONE);
                        dl.closeDrawer(GravityCompat.START);
                        ft.replace(R.id.frame_placeholder, new ChoiceModulSearchFragment());
                        ft.commit();

                        return true;

                    // Start Merlin G??rtler
                    case R.id.navigation_feedback:
                        header.setTitle(getApplicationContext().getString(R.string.title_feedback));
                        recyclerView.setVisibility(View.INVISIBLE);
                        calendar.setVisibility(View.GONE);
                        btnSearch.setVisibility(View.GONE);
                        dl.closeDrawer(GravityCompat.START);
                        ft.replace(R.id.frame_placeholder, new FeedbackFragment());
                        ft.commit();

                        return true;

                    case R.id.navigation_changeFaculty:
                        header.setTitle(getApplicationContext().getString(R.string.title_changeFaculty));
                        recyclerView.setVisibility(View.INVISIBLE);
                        calendar.setVisibility(View.GONE);
                        btnSearch.setVisibility(View.GONE);
                        dl.closeDrawer(GravityCompat.START);
                        // globale Variable, damit die Fakult??t gewechselt werden kann
                        final StartClass globalVariable = (StartClass) getApplicationContext();
                        globalVariable.setChangeFaculty(true);
                        Intent myIntent = new Intent(recyclerView.getContext(), MainActivity.class);
                        recyclerView.getContext().startActivity(myIntent);

                        return true;

                    case R.id.navigation_addCourse:
                        header.setTitle(getApplicationContext().getString(R.string.title_changeCourse));
                        recyclerView.setVisibility(View.INVISIBLE);
                        calendar.setVisibility(View.GONE);
                        btnSearch.setVisibility(View.GONE);
                        dl.closeDrawer(GravityCompat.START);
                        ft.replace(R.id.frame_placeholder, new AddCourseFragment());
                        ft.commit();

                        return true;
                    // Ende Merlin G??rtler
                    default:
                        return true;
                }

            }

        });


        //Userinterface Komponenten initialisieren
        BottomNavigationView navigation
                = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView4);
        recyclerView.setVisibility(View.VISIBLE);
        calendar = (CalendarView) findViewById(R.id.caCalender);
        btnSearch = (Button) findViewById(R.id.btnDatum);
        recyclerView.setVisibility(View.INVISIBLE);
        calendar.setVisibility(View.GONE);
        btnSearch.setVisibility(View.GONE);
        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame_placeholder, new Terminefragment());
        ft.commit();

    }

    //navigation mit den menuepunkten Bottom
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            // Merlin G??rtler schlie??e die Tastatur falls offen
            InputMethodManager inputMethodManager =
                    (InputMethodManager) getBaseContext().getSystemService(
                            Activity.INPUT_METHOD_SERVICE);

            try {
                inputMethodManager.hideSoftInputFromWindow(
                        table.this.getCurrentFocus().getWindowToken(), 0);
            } catch (Exception e) {
                Log.d("Exception", "Keyboard not open");
            }
            ft = getSupportFragmentManager().beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_calender:
                    //fragment fuer das "terminefragment" layout
                    header.setTitle(getApplicationContext().getString(R.string.title_calender));
                    recyclerView.setVisibility(View.INVISIBLE);
                    calendar.setVisibility(View.GONE);
                    btnSearch.setVisibility(View.GONE);
                    ft.replace(R.id.frame_placeholder, new Terminefragment());
                    //ft.addToBackStack(null);
                    ft.commit();
                    return true;

                case R.id.navigation_medication:
                    //fragment fuer das "activity_suche" layout
                    header.setTitle(getApplicationContext().getString(R.string.title_search));
                    recyclerView.setVisibility(View.INVISIBLE);
                    calendar.setVisibility(View.GONE);
                    btnSearch.setVisibility(View.GONE);
                    ft.replace(R.id.frame_placeholder, new searchFragment());
                    //ft.addToBackStack("suche");
                    ft.commit();
                    return true;

                case R.id.navigation_diary:
                    //fragment fuer das "favoriten" layout
                    header.setTitle(getApplicationContext().getString(R.string.title_exam));
                    recyclerView.setVisibility(View.INVISIBLE);
                    calendar.setVisibility(View.GONE);
                    btnSearch.setVisibility(View.GONE);
                    ft.replace(R.id.frame_placeholder, new Favoritenfragment());
                    //ft.addToBackStack(null);
                    ft.commit();
                    return true;

                case R.id.navigation_settings:
                    //fragment fuer das "Optionen" layout
                    header.setTitle(getApplicationContext().getString(R.string.title_settings));
                    recyclerView.setVisibility(View.INVISIBLE);
                    calendar.setVisibility(View.GONE);
                    btnSearch.setVisibility(View.GONE);
                    ft.replace(R.id.frame_placeholder, new Optionen());
                    //ft.addToBackStack(null);
                    ft.commit();
                    return true;
                // Start Merlin G??rtler
                case R.id.navigation_electiveModule:
                    header.setTitle(getApplicationContext().getString(R.string.title_electiveModule));
                    recyclerView.setVisibility(View.INVISIBLE);
                    calendar.setVisibility(View.GONE);
                    btnSearch.setVisibility(View.GONE);
                    ft.replace(R.id.frame_placeholder, new ChoiceModulSearchFragment());
                    ft.commit();


                    return true;
                // Ende Merlin G??rtler
            }
            return false;
        }
    };

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();

        } else {
            setResult(0);
            finish();
        }
    }
}