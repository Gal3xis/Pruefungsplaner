package com.Fachhochschulebib.fhb.pruefungsplaner;

//////////////////////////////
// TerminefragmentSuche
//
//
//
// autor:
// inhalt:  Prüfungen aus der Klasse Prüfplaneintrag werden abgefragt und zur darstelllung an den Recycleview adapter übergeben
// zugriffsdatum: 20.2.20
//
//
//
//
//
//
//////////////////////////////


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.Fachhochschulebib.fhb.pruefungsplaner.data.AppDatabase;
import com.Fachhochschulebib.fhb.pruefungsplaner.data.Pruefplan;

import java.util.ArrayList;
import java.util.List;


import static com.Fachhochschulebib.fhb.pruefungsplaner.Terminefragment.validation;


public class TerminefragmentSuche extends Fragment {


    private RecyclerView recyclerView;
    private CalendarView calendar;
    private Button btnsuche;
    private String date;
    List<Boolean> check = new ArrayList<>();
    private String month2;
    private String day2;
    private int position2 = 0;
    private String year2;
    private RecyclerView.LayoutManager mLayout;
    SwipeController swipeController = null;
    MyAdapter mAdapter;
    List<Integer> WerteZumAnzeigen= new ArrayList<>();



    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.terminefragment, container, false);



        //hinzufügen von recycleview
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView4);
        recyclerView.setVisibility(View.VISIBLE);
        // use this setting to
        // improve performance if you know that changes
        // in content do not change the layout size
        // of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(v.getContext());
        recyclerView.setLayoutManager(layoutManager);
        mLayout = recyclerView.getLayoutManager();



        //Userinterface Komponenten Initialiseren
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView4);
        recyclerView.setVisibility(View.VISIBLE);
        calendar = (CalendarView) v.findViewById(R.id.caCalender);
        btnsuche = (Button) v.findViewById(R.id.btnDatum);

        Adapteruebergabe();

        //btnsuche clicklistener überprüft ob der Kalender öffnen button angetippt wurde
        //Es werden bei eingeschalteten Kalender nur Prüfobjekte mit übereinstimmenden Datum angezeigt
        calendar.setVisibility(View.GONE);
        btnsuche.setOnClickListener(new View.OnClickListener() {
            boolean speicher = true;
            @Override
            public void onClick(View v) {
                if (!speicher) {
                    calendar.setVisibility(View.GONE);
                    Adapteruebergabe();
                    speicher = true;
                } else {
                    //Kalender ist geöffnet, nur übereinstimmende Prüfungen anzeigen
                    calendar.setVisibility(View.VISIBLE);
                    calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                        public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                            AppDatabase roomdaten = AppDatabase.getAppDatabase(getContext());
                            List<Pruefplan> userdaten = roomdaten.userDao().getAll(validation);
                            List<String> studiengangUndModul = new ArrayList<>();
                            List<String> prueferUndSemester = new ArrayList<>();
                            List<String> datum = new ArrayList<>();
                            List<String> modul = new ArrayList<>();
                            List<String> ID = new ArrayList<>();
                            List<String> pruefform = new ArrayList<>();
                            List<String> raum = new ArrayList<>();

                            //Creating editor to store uebergebeneModule to shared preferences
                            if (month < 10) {
                                month2 = "0" + String.valueOf(month + 1);
                            } else {
                                month2 = String.valueOf(month);
                            }
                            if (dayOfMonth < 10) {
                                day2 = "0" + String.valueOf(dayOfMonth);
                            } else {
                                day2 = String.valueOf(dayOfMonth);
                            }
                            year2 = String.valueOf(year);
                            date = year2 + "-" + month2 + "-" + day2;
                            for (int i = 0; i < userdaten.size(); i++) {
                                String[] date2 = userdaten.get(i).getDatum().split(" ");
                                if (date2[0].equals(date)) {
                                    studiengangUndModul.add(userdaten.get(i).getModul() + "\n " + userdaten.get(i).getStudiengang());
                                    prueferUndSemester.add(userdaten.get(i).getErstpruefer() + " " + userdaten.get(i).getZweitpruefer() + " " + userdaten.get(i).getSemester() + " ");
                                    datum.add(userdaten.get(i).getDatum());
                                    modul.add(userdaten.get(i).getModul());
                                    ID.add(userdaten.get(i).getID());
                                    pruefform.add(userdaten.get(i).getPruefform());
                                    raum.add(userdaten.get(i).getRaum());
                                }
                            }
                            // define an adapter
                            //Werte an den Adapter übergeben
                            mAdapter = new MyAdapter(studiengangUndModul, prueferUndSemester, datum, modul,ID,pruefform,mLayout,raum);
                            //Anzeigen von recyclerview
                            recyclerView.setAdapter(mAdapter);
                        }
                    });
                    speicher = false;
                }
            }
        });

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new   RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {
                        final TextView txtSecondScreen = (TextView) view.findViewById(R.id.txtSecondscreen);
                        View viewItem = recyclerView.getLayoutManager().findViewByPosition(position);
                        LinearLayout layout1 =(LinearLayout) viewItem.findViewById(R.id.linearLayout);

                        layout1.setOnClickListener(new  View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.e("@@@@@", "" + position);
                                if (txtSecondScreen.getVisibility() == v.VISIBLE) {
                                    txtSecondScreen.setVisibility(v.GONE);
                                    check.set(position,false);
                                } else {
                                    txtSecondScreen.setVisibility(v.VISIBLE);
                                    txtSecondScreen.setText(mAdapter.giveString(position));
                                }
                            }
                        });

                        if(check.get(position)) {
                            txtSecondScreen.setVisibility(v.VISIBLE);
                            txtSecondScreen.setText(mAdapter.giveString(position));
                        }
                    }
                })
        );
        return v;
    }

    public void Adapteruebergabe()
    {

        //Datenbank initialisieren
        AppDatabase datenbank = AppDatabase.getAppDatabase(getContext());
        List<Pruefplan> pruefplandaten = datenbank.userDao().getAll(validation);


        //
        for (int i =0;i<pruefplandaten.size();i++) {
            System.out.println(pruefplandaten.get(i).getAusgewaehlt());
            if(pruefplandaten.get(i).getAusgewaehlt()) {
                WerteZumAnzeigen.add(i);
            }
        }


        //Variablen zum speichern der Werte
        List<String> studiengangUndModul = new ArrayList<>();
        List<String> prueferUndSemster = new ArrayList<>();
        List<String> datum = new ArrayList<>();
        List<String> module = new ArrayList<>();
        List<String> ID = new ArrayList<>();
        List<String> Pruefform = new ArrayList<>();
        List<String> raum = new ArrayList<>();

        //Variablen mit Werten aus der lokalen Datenbank füllen
        for (int i = 0; i < WerteZumAnzeigen.size(); i++) {
            studiengangUndModul.add(pruefplandaten.get(Integer.valueOf(WerteZumAnzeigen.get(i))).getModul() + "\n " + pruefplandaten.get(Integer.valueOf(WerteZumAnzeigen.get(i))).getStudiengang());
            prueferUndSemster.add(pruefplandaten.get(Integer.valueOf(WerteZumAnzeigen.get(i))).getErstpruefer() + " " + pruefplandaten.get(Integer.valueOf(WerteZumAnzeigen.get(i))).getZweitpruefer() + " " + pruefplandaten.get(Integer.valueOf(WerteZumAnzeigen.get(i))).getSemester() + " ");
            datum.add(pruefplandaten.get(Integer.valueOf(WerteZumAnzeigen.get(i))).getDatum());
            module.add(pruefplandaten.get(Integer.valueOf(WerteZumAnzeigen.get(i))).getModul());
            ID.add(pruefplandaten.get(Integer.valueOf(WerteZumAnzeigen.get(i))).getID());
            Pruefform.add(pruefplandaten.get(Integer.valueOf(WerteZumAnzeigen.get(i))).getPruefform());
            raum.add(pruefplandaten.get(Integer.valueOf(WerteZumAnzeigen.get(i))).getRaum());
            check.add(true);
        }

        // define an adapter
        mAdapter = new MyAdapter(studiengangUndModul, prueferUndSemster, datum, module,ID,Pruefform,mLayout,raum);
        recyclerView.setAdapter(mAdapter);
    }

}






