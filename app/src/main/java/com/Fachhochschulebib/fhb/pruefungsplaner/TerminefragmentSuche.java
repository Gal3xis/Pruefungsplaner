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


import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Fachhochschulebib.fhb.pruefungsplaner.data.AppDatabase;
import com.Fachhochschulebib.fhb.pruefungsplaner.data.PruefplanEintrag;

import java.util.ArrayList;
import java.util.List;

public class TerminefragmentSuche extends Fragment {
    SharedPreferences mSharedPreferencesValidation;

    private RecyclerView recyclerView;
    private CalendarView calendar;
    private Button btnsuche;
    private String date;
    String pruefJahr, aktuellePruefphase, rueckgabeStudiengang, validation;

    List<Boolean> checkList = new ArrayList<>();
    List<String> modulUndStudiengangsList = new ArrayList<>();
    List<String> prueferUndSemesterList = new ArrayList<>();
    List<String> datumsList = new ArrayList<>();
    List<String> modulList = new ArrayList<>();
    List<String> idList = new ArrayList<>();
    List<String> pruefFormList = new ArrayList<>();
    List<String> raumList = new ArrayList<>();
    List<String> status = new ArrayList<>();

    private String month2;
    private String day2;
    // private int position2 = 0;
    private String year2;
    private RecyclerView.LayoutManager mLayout;
    MyAdapter mAdapter;
    List<Integer> WerteZumAnzeigenList = new ArrayList<>();



    public void onCreate(Bundle savedInstanceState) {
        // Start Merlin Gürtler
        // Nun aus Shared Preferences

        mSharedPreferencesValidation
                = TerminefragmentSuche.
                this.getContext().getSharedPreferences("validation", 0);

        pruefJahr = mSharedPreferencesValidation.getString("pruefJahr", "0");
        aktuellePruefphase = mSharedPreferencesValidation.getString("aktuellePruefphase", "0");
        rueckgabeStudiengang = mSharedPreferencesValidation.getString("rueckgabeStudiengang", "0");

        validation = pruefJahr + rueckgabeStudiengang + aktuellePruefphase;
        // Ende Merlin Gürtler

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
        // recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView4);
        // recyclerView.setVisibility(View.VISIBLE);
        calendar = (CalendarView) v.findViewById(R.id.caCalender);
        btnsuche = (Button) v.findViewById(R.id.btnDatum);

        Adapteruebergabe();

        calendar.setVisibility(View.GONE);
        //btnsuche clicklistener überprüft, ob der "Kalender öffnen" - Button angetippt wurde
        /*  Es werden bei eingeschaltetem Kalender nur Prüfobjekte mit übereinstimmenden
            Datum angezeigt.
         */
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

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    AppDatabase roomdaten = AppDatabase.getAppDatabase(getContext());
                                    List<PruefplanEintrag> ppeList = roomdaten.userDao().getAll(validation);

                                    ClearLists();

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
                                    for (PruefplanEintrag eintrag: ppeList) {
                                        String[] date2 = eintrag.getDatum().split(" ");
                                        if (date2[0].equals(date)) {
                                            modulUndStudiengangsList.add(
                                                    eintrag.getModul() + "\n "
                                                            + eintrag.getStudiengang());
                                            prueferUndSemesterList.add(
                                                    eintrag.getErstpruefer()
                                                            + " " + eintrag.getZweitpruefer()
                                                            + " " + eintrag.getSemester() + " ");
                                            datumsList.add(eintrag.getDatum());
                                            modulList.add(eintrag.getModul());
                                            idList.add(eintrag.getID());
                                            pruefFormList.add(eintrag.getPruefform());
                                            raumList.add(eintrag.getRaum());
                                            status.add(eintrag.getStatus());
                                        }
                                    }
                                    // define an adapter
                                    //Werte an den Adapter übergeben
                                    mAdapter = new MyAdapter(
                                            modulUndStudiengangsList,
                                            prueferUndSemesterList,
                                            datumsList,
                                            modulList,
                                            idList,
                                            pruefFormList,
                                            mLayout,
                                            raumList,
                                            status);

                                    //Anzeigen von recyclerview
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            recyclerView.setAdapter(mAdapter);
                                        }
                                    });
                                }
                            }).start();
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
                        final TextView txtSecondScreen
                                = (TextView) view.findViewById(R.id.txtSecondscreen);
                        View viewItem
                                = recyclerView.getLayoutManager().findViewByPosition(position);
                        LinearLayout layout1
                                =(LinearLayout) viewItem.findViewById(R.id.linearLayout);

                        layout1.setOnClickListener(new  View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.e("@@@@@", "" + position);
                                if (txtSecondScreen.getVisibility() == v.VISIBLE) {
                                    txtSecondScreen.setVisibility(v.GONE);
                                    checkList.set(position,false);
                                } else {
                                    // Start Merlin Gürtler
                                    for (int i = 0; i < recyclerView.getChildCount(); i++) {
                                        View holder = recyclerView.getLayoutManager().findViewByPosition(i);
                                        // Try and Catch, da die App crasht
                                        // wenn das Element nicht im View Port ist
                                        try {
                                            final TextView txtSecondScreen2
                                                    = (TextView) holder.findViewById(R.id.txtSecondscreen);
                                            if (txtSecondScreen2.getVisibility() == v.VISIBLE) {
                                                txtSecondScreen2.setVisibility(v.GONE);
                                            }
                                        }catch (Exception e) {
                                            Log.d("ERROR","NOT IN VIEW PORT " + e);
                                        }
                                    }
                                    // Ende Merlin Gürtler
                                    txtSecondScreen.setVisibility(v.VISIBLE);
                                    txtSecondScreen.setText(mAdapter.giveString(position));
                                }
                            }
                        });

                        if(checkList.get(position)) {
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

        new Thread(new Runnable() {
            @Override
            public void run() {
                //Datenbank initialisieren
                AppDatabase datenbank = AppDatabase.getAppDatabase(getContext());

                // Änderung Merlin Gürtler
                // List<Pruefplan> pruefplandaten = datenbank.userDao().getAll(validation);
                // Für die Suche von Modulen
                List<PruefplanEintrag> ppeList = datenbank.userDao().getAll2();
                // Ende Änderung Merlin Gürtler

                ClearLists();

                for (int i =0;i<ppeList.size();i++) {
                    System.out.println(ppeList.get(i).getAusgewaehlt());
                    if(ppeList.get(i).getAusgewaehlt()) {
                        WerteZumAnzeigenList.add(i);
                    }
                }

                //Variablen mit Werten aus der lokalen Datenbank füllen
                for (int i = 0; i < WerteZumAnzeigenList.size(); i++) {
                    modulUndStudiengangsList.add(ppeList.get(Integer.valueOf(WerteZumAnzeigenList.get(i))).getModul() + "\n " + ppeList.get(Integer.valueOf(WerteZumAnzeigenList.get(i))).getStudiengang());
                    prueferUndSemesterList.add(ppeList.get(Integer.valueOf(WerteZumAnzeigenList.get(i))).getErstpruefer() + " " + ppeList.get(Integer.valueOf(WerteZumAnzeigenList.get(i))).getZweitpruefer() + " " + ppeList.get(Integer.valueOf(WerteZumAnzeigenList.get(i))).getSemester() + " ");
                    datumsList.add(ppeList.get(Integer.valueOf(WerteZumAnzeigenList.get(i))).getDatum());
                    modulList.add(ppeList.get(Integer.valueOf(WerteZumAnzeigenList.get(i))).getModul());
                    idList.add(ppeList.get(Integer.valueOf(WerteZumAnzeigenList.get(i))).getID());
                    pruefFormList.add(ppeList.get(Integer.valueOf(WerteZumAnzeigenList.get(i))).getPruefform());
                    raumList.add(ppeList.get(Integer.valueOf(WerteZumAnzeigenList.get(i))).getRaum());
                    status.add(ppeList.get(Integer.valueOf(WerteZumAnzeigenList.get(i))).getStatus());
                    checkList.add(true);
                }

                // define an adapter
                mAdapter = new MyAdapter(
                        modulUndStudiengangsList,
                        prueferUndSemesterList,
                        datumsList,
                        modulList,
                        idList,
                        pruefFormList,
                        mLayout,
                        raumList,
                        status);

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(mAdapter);
                    }
                });
            }
        }).start();
    }

    public void ClearLists()
    {
        modulUndStudiengangsList.clear();
        prueferUndSemesterList.clear();
        datumsList.clear();
        modulList.clear();
        idList.clear();
        pruefFormList.clear();
        raumList.clear();
    }

}






