package com.Fachhochschulebib.fhb.pruefungsplaner;

//////////////////////////////
// Terminefragment
//
//
//
// autor:
// inhalt:  Prüfungen aus der Klasse Prüfplaneintrag werden abgefragt und
// zur Darstelllung an den Recycleview-Adapter übergeben
// zugriffsdatum: 20.2.20
//
//
//////////////////////////////

import android.content.SharedPreferences;
import android.os.AsyncTask;
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

import java.util.ArrayList;
import java.util.List;

import com.Fachhochschulebib.fhb.pruefungsplaner.data.AppDatabase;
import com.Fachhochschulebib.fhb.pruefungsplaner.data.PruefplanEintrag;
import com.Fachhochschulebib.fhb.pruefungsplaner.model.RetrofitConnect;


public class Terminefragment extends Fragment {

    SharedPreferences mSharedPreferencesValidation;
    private RecyclerView recyclerView;
    private CalendarView calendar;
    private Button btnsuche;
    private String date;
    private String month2;
    private String day2;
    int positionAlt = 0;
    private String year2;
    List<Boolean> checkList = new ArrayList<>();
    //Variablen
    List<String> modulUndStudiengangsList = new ArrayList<>();
    List<String> prueferUndSemesterList = new ArrayList<>();
    List<String> datumsList = new ArrayList<>();
    List<String> modulList = new ArrayList<>();
    List<String> idList = new ArrayList<>();
    List<String> pruefFormList = new ArrayList<>();
    List<String> raumList = new ArrayList<>();
    String pruefJahr, aktuellePruefphase, rueckgabeStudiengang;

    public static String validation;
    MyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayout;

    AppDatabase datenbank;
    // List<PruefplanEintrag> ppeList = datenbank.userDao().getAll(validation);

    public void onCreate(Bundle savedInstanceState) {

        // Start Merlin Gürtler

        datenbank = AppDatabase.getAppDatabase(this.getContext());
        // Nun aus Shared Preferences

        mSharedPreferencesValidation
                = Terminefragment.
                this.getContext().getSharedPreferences("validation", 0);

        pruefJahr = mSharedPreferencesValidation.getString("pruefJahr", "0");
        aktuellePruefphase = mSharedPreferencesValidation.getString("aktuellePruefphase", "0");
        rueckgabeStudiengang = mSharedPreferencesValidation.getString("rueckgabeStudiengang", "0");

        validation = pruefJahr + rueckgabeStudiengang + aktuellePruefphase;
        // Ende Merlin Gürtler

        LongOperation asynctask = new LongOperation();

        asynctask.execute("");

        super.onCreate(savedInstanceState);
    }

    class LongOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            List<PruefplanEintrag> ppeList
                    = Terminefragment.this.datenbank.userDao().getAll(validation);
            if(ppeList.size() < 1) {
                for (int c = 0; c < 1000; c++) {
                    try {
                        Thread.sleep(3000);
                        if (RetrofitConnect.checkuebertragung) {
                            return "Executed";
                        }
                    } catch (InterruptedException e) {
                        Thread.interrupted();
                    }
                }
            }
            return "null";
        }

        @Override
        protected void onPostExecute(String result) {
            AdapterUebergabe();
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {}
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

        //mSharedPreferences = v.getContext().getSharedPreferences("json6", 0);
        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(v.getContext());
        recyclerView.setLayoutManager(layoutManager);
        mLayout = recyclerView.getLayoutManager();

        AdapterUebergabe();

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), (RecyclerItemClickListener.OnItemClickListener) (view, position) -> {
                    //LinearLayout layout1 =( LinearLayout) view.findViewById(R.id.linearLayout);
                    final TextView txtSecondScreen
                            = (TextView) view.findViewById(R.id.txtSecondscreen);
                    View viewItem
                            = recyclerView.getLayoutManager().findViewByPosition(position);

                    LinearLayout layout1
                            =(LinearLayout) viewItem.findViewById(R.id.linearLayout);
                    layout1.setOnClickListener(v1 -> {
                        Log.e("@@@@@", "" + position);
                        if (txtSecondScreen.getVisibility() == v1.VISIBLE) {
                            txtSecondScreen.setVisibility(v1.GONE);
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
                            txtSecondScreen.setVisibility(v1.VISIBLE);
                            txtSecondScreen.setText(mAdapter.giveString(position));
                        }
                    });

                    /* Merlin Gürtler
                    Dieser Code scheint nichts zu tun
                    try{
                        if(checkList.get(position)) {
                            for(int i = 0; i < recyclerView.getAdapter().getItemCount(); i++) {

                            }


                            txtSecondScreen.setVisibility(v.VISIBLE);
                            txtSecondScreen.setText(mAdapter.giveString(position));
                        }}
                    catch(Exception e){
                        Log.e("Fehler Terminefragment",
                                "Fehler weitere Informationen");
                    }
                    */
                    positionAlt = position;
                })
        );

        //Touchhelper für die Recyclerview-Komponente, zum Überprüfen, ob gescrollt wurde
        //ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        //itemTouchhelper.attachToRecyclerView(recyclerView);

        //initialisieren der UI-Komponenten
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView4);
        recyclerView.setVisibility(View.VISIBLE);
        calendar = (CalendarView) v.findViewById(R.id.caCalender);
        btnsuche = (Button) v.findViewById(R.id.btnDatum);
        calendar.setVisibility(View.GONE);

        //Clicklistener für den Kalender,
        //Es wird überprüft, welches Datum ausgewählt wurde.
        btnsuche.setOnClickListener(new View.OnClickListener() {
            boolean speicher = true;
            @Override
            public void onClick(View v) {
                if (!speicher) {
                    calendar.setVisibility(View.GONE);
                    AdapterUebergabe();
                    speicher = true;

                } else {
                    //Kalender wurde eingeschalet
                    calendar.setVisibility(View.VISIBLE);

                    /*  Es werden durch setOnDateChangeListener nur Prüfungen
                        mit dem ausgewählten Datum angezeigt
                     */
                    calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                        public void onSelectedDayChange(CalendarView view,
                                                        int year, int month, int dayOfMonth) {

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    //Datenbank
                                    List<PruefplanEintrag> ppeList = datenbank.userDao().getAll(validation);

                                    //unnötige Werte entfernen
                                    if (month < 9) {
                                        month2 = "0" + String.valueOf(month + 1);
                                    } else {
                                        month2 = String.valueOf(month+1);
                                    }
                                    if (dayOfMonth < 10) {
                                        day2 = "0" + String.valueOf(dayOfMonth);
                                    } else {
                                        day2 = String.valueOf(dayOfMonth);
                                    }
                                    year2 = String.valueOf(year);
                                    date = year2 + "-" + month2 + "-" + day2;
                                    System.out.println(date);
                                    checkList.clear();

                                    ClearLists();
                                    for(PruefplanEintrag eintrag: ppeList) {
                                        String[] date2 = eintrag.getDatum().split(" ");
                                        System.out.println(date2[0]);

                                /*  Überprüfung ob das Prüfitem Datum mit dem ausgewählten
                                    Kalender datum übereinstimmt
                                 */
                                        if (date2[0].equals(date)) {
                                            modulUndStudiengangsList.add(
                                                    eintrag.getModul()
                                                            + "\n " + eintrag.getStudiengang());
                                            prueferUndSemesterList.add(
                                                    eintrag.getErstpruefer()
                                                            + " " + eintrag.getZweitpruefer()
                                                            + " " + eintrag.getSemester() + " ");
                                            datumsList.add(eintrag.getDatum());
                                            modulList.add(eintrag.getModul());
                                            idList.add(eintrag.getID());
                                            pruefFormList.add(eintrag.getPruefform());
                                            raumList.add(eintrag.getRaum());
                                            checkList.add(true);
                                        }
                                    }// define an adapter

                                    //Adapter mit Werten füllen
                                    mAdapter = new MyAdapter(   modulUndStudiengangsList,
                                            prueferUndSemesterList,
                                            datumsList,
                                            modulList,
                                            idList,
                                            pruefFormList,mLayout,
                                            raumList);
                                    //Anzeigen
                                    recyclerView.setAdapter(mAdapter);
                                }
                            }).start();
                        }
                    });
                    speicher = false;
                }
            }
        });
        return v;
    }

    public void AdapterUebergabe()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<PruefplanEintrag> ppeList = datenbank.userDao().getAll(validation);

                checkList.clear();
                ClearLists();

                for (PruefplanEintrag eintrag: ppeList) {
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
                    checkList.add(true);
                }// define an adapter

                mAdapter = new MyAdapter(   modulUndStudiengangsList,
                        prueferUndSemesterList,
                        datumsList,
                        modulList,
                        idList,
                        pruefFormList,
                        mLayout,
                        raumList);

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(mAdapter);
                    }
                });
                // System.out.println(String.valueOf(userdaten.size()));
            }
        }).start();
        //Datenbankaufruf
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