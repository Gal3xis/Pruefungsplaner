package com.Fachhochschulebib.fhb.pruefungsplaner;

//////////////////////////////
// Tabelle
//
//
//
// autor:
// inhalt:  Verwaltung aufrufen der fragmente, hier ist der navigation bar hinterlegt
// zugriffsdatum: 20.2.20
//
//
//
//
//
//
//////////////////////////////



import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.Fachhochschulebib.fhb.pruefungsplaner.data.AppDatabase;
import com.Fachhochschulebib.fhb.pruefungsplaner.data.Pruefplan;
import java.io.IOException;
import java.util.List;


import static com.Fachhochschulebib.fhb.pruefungsplaner.MainActivity.mAdapter;
import static com.Fachhochschulebib.fhb.pruefungsplaner.MainActivity.pruefJahr;
import static com.Fachhochschulebib.fhb.pruefungsplaner.MainActivity.aktuellePruefphase;
import static com.Fachhochschulebib.fhb.pruefungsplaner.MainActivity.rueckgabeStudiengang;


//Eigentlich die Hauptklasse wurde noch nicht umgenannt von hier werden die fragmente aufgerufen
public class Tabelle extends AppCompatActivity  {
    static public FragmentTransaction ft ;
    private RecyclerView recyclerView;
    private CalendarView calendar;
    private Button btnsuche;
    private DrawerLayout dl;
    private NavigationView nv;
    private TextView txtanzeigemenu;
    //aufruf der starteinstelllungen


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hauptfenster);
        txtanzeigemenu = (TextView) findViewById(R.id.txtAnzeige);

        dl = (DrawerLayout)findViewById(R.id.drawer_layout);


        TextView btnOpen = (TextView) findViewById(R.id.btnopen);

        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("button pressed");
                dl.openDrawer(Gravity.START);
                dl.setVisibility(View.VISIBLE);

            }
        });


        nv = (NavigationView)findViewById(R.id.nav_view);
        if (!nv.isFocused())
        {
            dl.setVisibility(View.GONE);
        }

        dl.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                //Called when a drawer's position changes.

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                dl.setVisibility(View.VISIBLE);
                //Called when a drawer has settled in a completely open state.
                //The drawer is interactive at this point.
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
                // Called when the drawer motion state changes. The new state will be one of STATE_IDLE, STATE_DRAGGING or STATE_SETTLING.
            }
        });


        //Navigation Menü mit den Menüpunkten
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
                        txtanzeigemenu.setText("termine");
                        recyclerView.setVisibility(View.INVISIBLE);
                        calendar.setVisibility(View.GONE);
                        btnsuche.setVisibility(View.GONE);
                        //dl.setVisibility(View.GONE);
                        dl.closeDrawer(Gravity.START);
                        ft.replace(R.id.frame_placeholder, new Terminefragment());
                        ft.commit();
                        return true;

                    case R.id.navigation_medication:
                        //Menüpunkt Suche
                        String validation = pruefJahr + rueckgabeStudiengang + aktuellePruefphase;
                        AppDatabase roomdaten = AppDatabase.getAppDatabase(getApplicationContext());
                        List<Pruefplan> userdaten = roomdaten.userDao().getAll(validation);
                        txtanzeigemenu.setText("Suche");
                        recyclerView.setVisibility(View.INVISIBLE);
                        calendar.setVisibility(View.GONE);
                        btnsuche.setVisibility(View.GONE);
                        dl.closeDrawer(Gravity.START);


                        //Suche Layout wird nicht aufgerufen wenn keine daten vorhanden sind
                        if (userdaten.size() < 2) {
                        }else{
                            ft.replace(R.id.frame_placeholder, new sucheFragment());
                            ft.commit();
                        }

                        return true;
                    case R.id.navigation_diary:
                        txtanzeigemenu.setText("Prüfungen");
                        recyclerView.setVisibility(View.INVISIBLE);
                        calendar.setVisibility(View.GONE);
                        btnsuche.setVisibility(View.GONE);
                        dl.closeDrawer(Gravity.START);
                        ft.replace(R.id.frame_placeholder, new Favoritenfragment());
                        ft.commit();

                        return true;

                    case R.id.navigation_settings:
                        txtanzeigemenu.setText("Optionen");
                        recyclerView.setVisibility(View.INVISIBLE);
                        calendar.setVisibility(View.GONE);
                        btnsuche.setVisibility(View.GONE);
                        dl.closeDrawer(Gravity.START);
                        ft.replace(R.id.frame_placeholder, new Optionen());
                        ft.commit();

                        return true;
                    default:
                        return true;
                }

            }

        });



        //Userinterface Komponenten initialisieren
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
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
                    txtanzeigemenu.setText("Termine");
                    recyclerView.setVisibility(View.INVISIBLE);
                    calendar.setVisibility(View.GONE);
                    btnsuche.setVisibility(View.GONE);
                    ft.replace(R.id.frame_placeholder, new Terminefragment());
                    //ft.addToBackStack(null);
                    ft.commit();
                    return true;

                case R.id.navigation_medication:
                    //fragment fuer das "activity_suche" layout
                    txtanzeigemenu.setText("Suche");
                    recyclerView.setVisibility(View.INVISIBLE);
                    calendar.setVisibility(View.GONE);
                    btnsuche.setVisibility(View.GONE);
                    ft.replace(R.id.frame_placeholder, new sucheFragment());
                    //ft.addToBackStack("suche");
                    ft.commit();
                    return true;

                case R.id.navigation_diary:
                    //fragment fuer das "favoriten" layout
                    txtanzeigemenu.setText("Prüfungen");
                    recyclerView.setVisibility(View.INVISIBLE);
                    calendar.setVisibility(View.GONE);
                    btnsuche.setVisibility(View.GONE);
                    ft.replace(R.id.frame_placeholder, new Favoritenfragment());
                    //ft.addToBackStack(null);
                    ft.commit();
                    return true;

                case R.id.navigation_settings:
                    //fragment fuer das "Optionen" layout
                    txtanzeigemenu.setText("Optionen");
                    recyclerView.setVisibility(View.INVISIBLE);
                    calendar.setVisibility(View.GONE);
                    btnsuche.setVisibility(View.GONE);
                    ft.replace(R.id.frame_placeholder, new Optionen());
                    //ft.addToBackStack(null);
                    ft.commit();
                    return true;
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
