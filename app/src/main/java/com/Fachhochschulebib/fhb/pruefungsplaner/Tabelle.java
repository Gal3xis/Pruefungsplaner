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

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.Fachhochschulebib.fhb.pruefungsplaner.data.AppDatabase;
import com.Fachhochschulebib.fhb.pruefungsplaner.data.PruefplanEintrag;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

// Eigentlich die Hauptklasse wurde noch nicht umgenannt
// von hier werden die fragmente aufgerufen
public class Tabelle extends AppCompatActivity  {
    static public FragmentTransaction ft ;
    private RecyclerView recyclerView;
    private CalendarView calendar;
    private Button btnsuche;
    private DrawerLayout dl;
    private NavigationView nv;
    private TextView txtAnzeigeMenu;

    SharedPreferences mSharedPreferencesValidation;
    String pruefJahr,
            aktuellePruefphase, rueckgabeStudiengang;
    //Loginhandler login = new Loginhandler();
    //aufruf der starteinstelllungen

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Start Merlin Gürtler
        // Nun aus Shared Preferences
        mSharedPreferencesValidation
                = Tabelle.this.getSharedPreferences("validation", 0);

        pruefJahr = mSharedPreferencesValidation.getString("pruefJahr", "0");
        aktuellePruefphase = mSharedPreferencesValidation.getString("aktuellePruefphase", "0");
        rueckgabeStudiengang = mSharedPreferencesValidation.getString("rueckgabeStudiengang", "0");
        // Ende Merlin Gürtler


        setContentView(R.layout.hauptfenster);
        txtAnzeigeMenu = findViewById(R.id.txtAnzeige);

        dl = findViewById(R.id.drawer_layout);

        TextView btnOpen = (TextView) findViewById(R.id.btnopen);

        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("button pressed");
                dl.openDrawer(GravityCompat.START);
                dl.setVisibility(View.VISIBLE);

            }
        });

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


        //Bottom Navigation Menü mit den Menüpunkten
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //Fragmentmanager initialisierung
                int id = item.getItemId();
                ft = getSupportFragmentManager().beginTransaction();
                switch(id)
                {
                    case R.id.navigation_calender:
                        //Menüpunkt termine
                        txtAnzeigeMenu.setText(getApplicationContext().getString(R.string.title_calender));
                        recyclerView.setVisibility(View.INVISIBLE);
                        calendar.setVisibility(View.GONE);
                        btnsuche.setVisibility(View.GONE);
                        //dl.setVisibility(View.GONE);
                        dl.closeDrawer(GravityCompat.START);
                        ft.replace(R.id.frame_placeholder, new Terminefragment());
                        ft.commit();
                        return true;

                    case R.id.navigation_medication:
                        //Menüpunkt Suche
                        String validation
                                = pruefJahr + rueckgabeStudiengang + aktuellePruefphase;
                        AppDatabase roomdaten
                                = AppDatabase.getAppDatabase(getApplicationContext());
                        List<PruefplanEintrag> ppeList
                                = roomdaten.userDao().getAll(validation);
                        txtAnzeigeMenu.setText(getApplicationContext().getString(R.string.title_search));
                        recyclerView.setVisibility(View.INVISIBLE);
                        calendar.setVisibility(View.GONE);
                        btnsuche.setVisibility(View.GONE);
                        dl.closeDrawer(GravityCompat.START);


                        //Suche Layout wird nicht aufgerufen wenn keine daten vorhanden sind
                        if (ppeList.size() < 2) {
                        }else{
                            ft.replace(R.id.frame_placeholder, new sucheFragment());
                            ft.commit();
                        }

                        return true;
                    case R.id.navigation_diary:
                        txtAnzeigeMenu.setText(getApplicationContext().getString(R.string.title_exam));
                        recyclerView.setVisibility(View.INVISIBLE);
                        calendar.setVisibility(View.GONE);
                        btnsuche.setVisibility(View.GONE);
                        dl.closeDrawer(GravityCompat.START);
                        ft.replace(R.id.frame_placeholder, new Favoritenfragment());
                        ft.commit();

                        return true;

                    case R.id.navigation_settings:
                        txtAnzeigeMenu.setText(getApplicationContext().getString(R.string.title_settings));
                        recyclerView.setVisibility(View.INVISIBLE);
                        calendar.setVisibility(View.GONE);
                        btnsuche.setVisibility(View.GONE);
                        dl.closeDrawer(GravityCompat.START);
                        ft.replace(R.id.frame_placeholder, new Optionen());
                        ft.commit();

                        return true;

                    // Start Merlin Gürtler
                    case R.id.navigation_electiveModule:
                        txtAnzeigeMenu.setText(getApplicationContext().getString(R.string.title_electiveModule));
                        recyclerView.setVisibility(View.INVISIBLE);
                        calendar.setVisibility(View.GONE);
                        btnsuche.setVisibility(View.GONE);
                        dl.closeDrawer(GravityCompat.START);
                        ft.replace(R.id.frame_placeholder, new WahlModulSucheFragment());
                        ft.commit();

                        return true;


                    case R.id.navigation_changeCourse:
                        txtAnzeigeMenu.setText(getApplicationContext().getString(R.string.title_changeCourse));
                        recyclerView.setVisibility(View.INVISIBLE);
                        calendar.setVisibility(View.GONE);
                        btnsuche.setVisibility(View.GONE);
                        dl.closeDrawer(GravityCompat.START);
                        Intent myIntent = new Intent(recyclerView.getContext(), MainActivity.class);
                        recyclerView.getContext().startActivity(myIntent);

                        return true;
                    // Ende Merlin Gürtler
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
        btnsuche = (Button) findViewById(R.id.btnDatum);
        recyclerView.setVisibility(View.INVISIBLE);
        calendar.setVisibility(View.GONE);
        btnsuche.setVisibility(View.GONE);
        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame_placeholder, new Terminefragment());
        ft.commit();

    }

    //navigation mit den menuepunkten
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            ft = getSupportFragmentManager().beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_calender:
                    //fragment fuer das "terminefragment" layout
                    txtAnzeigeMenu.setText(getApplicationContext().getString(R.string.title_calender));
                    recyclerView.setVisibility(View.INVISIBLE);
                    calendar.setVisibility(View.GONE);
                    btnsuche.setVisibility(View.GONE);
                    ft.replace(R.id.frame_placeholder, new Terminefragment());
                    //ft.addToBackStack(null);
                    ft.commit();
                    return true;

                case R.id.navigation_medication:
                    //fragment fuer das "activity_suche" layout
                    txtAnzeigeMenu.setText(getApplicationContext().getString(R.string.title_search));
                    recyclerView.setVisibility(View.INVISIBLE);
                    calendar.setVisibility(View.GONE);
                    btnsuche.setVisibility(View.GONE);
                    ft.replace(R.id.frame_placeholder, new sucheFragment());
                    //ft.addToBackStack("suche");
                    ft.commit();
                    return true;

                case R.id.navigation_diary:
                    //fragment fuer das "favoriten" layout
                    txtAnzeigeMenu.setText(getApplicationContext().getString(R.string.title_exam));
                    recyclerView.setVisibility(View.INVISIBLE);
                    calendar.setVisibility(View.GONE);
                    btnsuche.setVisibility(View.GONE);
                    ft.replace(R.id.frame_placeholder, new Favoritenfragment());
                    //ft.addToBackStack(null);
                    ft.commit();
                    return true;

                case R.id.navigation_settings:
                    //fragment fuer das "Optionen" layout
                    txtAnzeigeMenu.setText(getApplicationContext().getString(R.string.title_settings));
                    recyclerView.setVisibility(View.INVISIBLE);
                    calendar.setVisibility(View.GONE);
                    btnsuche.setVisibility(View.GONE);
                    ft.replace(R.id.frame_placeholder, new Optionen());
                    //ft.addToBackStack(null);
                    ft.commit();
                    return true;
                // Start Merlin Gürtler
                case R.id.navigation_electiveModule:
                    txtAnzeigeMenu.setText(getApplicationContext().getString(R.string.title_electiveModule));
                    recyclerView.setVisibility(View.INVISIBLE);
                    calendar.setVisibility(View.GONE);
                    btnsuche.setVisibility(View.GONE);
                    ft.replace(R.id.frame_placeholder, new WahlModulSucheFragment());
                    ft.commit();


                    return true;
                // Ende Merlin Gürtler
            }
            return false;
        }
    };

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {

            getFragmentManager().popBackStack();

        } else {
            super.onBackPressed();
        }
    }
}