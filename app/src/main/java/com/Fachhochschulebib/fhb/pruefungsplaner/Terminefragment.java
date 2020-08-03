package com.Fachhochschulebib.fhb.pruefungsplaner;

//////////////////////////////
// Terminefragment
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
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import com.Fachhochschulebib.fhb.pruefungsplaner.model.RetrofitConnect;
import java.util.ArrayList;
import java.util.List;

import static com.Fachhochschulebib.fhb.pruefungsplaner.MainActivity.pruefJahr;
import static com.Fachhochschulebib.fhb.pruefungsplaner.MainActivity.aktuellePruefphase;
import static com.Fachhochschulebib.fhb.pruefungsplaner.MainActivity.rueckgabeStudiengang;

public class Terminefragment extends Fragment {

    SharedPreferences mSharedPreferences;
    public List<String> WerteZumAnzeigen;
    private FragmentTransaction ft;
    private RecyclerView recyclerView;
    private CalendarView calendar;
    private Button btnsuche;
    private String date;
    private String month2;
    private String day2;
    int positionAlt = 0;
    private int aktuellePosition = 0;
    private String year2;
    List<Boolean> check = new ArrayList<>();
    public static String validation;
    MyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayout;

    AppDatabase datenbank = AppDatabase.getAppDatabase(getContext());
    List<Pruefplan> pruefplandaten = datenbank.userDao().getAll(validation);


    public void onCreate(Bundle savedInstanceState) {




        LongOperation asynctask = new LongOperation();

        asynctask.execute("");
       validation = pruefJahr + rueckgabeStudiengang + aktuellePruefphase;

       super.onCreate(savedInstanceState);
    }

    class LongOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            List<Pruefplan> datenbank = Terminefragment.this.datenbank.userDao().getAll(validation);
            if(datenbank.size() < 1) {
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
            Adapteruebergabe();
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

        mSharedPreferences = v.getContext().getSharedPreferences("json6", 0);
        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(v.getContext());
        recyclerView.setLayoutManager(layoutManager);
        mLayout = recyclerView.getLayoutManager();

        Adapteruebergabe();

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new   RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick( final View view, final  int position) {
                        //LinearLayout layout1 =( LinearLayout) view.findViewById(R.id.linearLayout);
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

                        try{
                        if(check.get(position)) {
                            txtSecondScreen.setVisibility(v.VISIBLE);
                            txtSecondScreen.setText(mAdapter.giveString(position));
                        }}
                        catch(Exception e){
                            Log.e("Fehler Terminefragment","Fehler weitere Informationen");
                        }

                        // TODO Handle item click
                        //Log.e("@@@@@",""+ aktuellePosition);
                    positionAlt = position;
                    }
                })
        );

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }
        });



        //Tocuhhelper für die Recyclerview-Komponente, zum überprüfen ob gescrollt wurde
        //ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        //itemTouchhelper.attachToRecyclerView(recyclerView);

        //initialisieren der UI-Komponenten
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView4);
        recyclerView.setVisibility(View.VISIBLE);
        calendar = (CalendarView) v.findViewById(R.id.caCalender);
        btnsuche = (Button) v.findViewById(R.id.btnDatum);
        calendar.setVisibility(View.GONE);

        //Clicklistener für den Kalender,
        //Es wird überprüft welches Datum ausgewählt wurde
        btnsuche.setOnClickListener(new View.OnClickListener() {
            boolean speicher = true;
            @Override
            public void onClick(View v) {
                if (!speicher) {
                    calendar.setVisibility(View.GONE);
                    Adapteruebergabe();
                    speicher = true;

                } else {
                    //Kalender wurde eingeschalet
                    calendar.setVisibility(View.VISIBLE);

                    //Es werden durch setOnDateChangeListener nur Prüfungen mit dem ausgewählten Datum angezeigt
                    calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                        public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

                            //Datenbank
                            List<Pruefplan> userdaten = datenbank.userDao().getAll(validation);

                            //Variablen
                            List<String> modulUndStudiengang = new ArrayList<>();
                            List<String> prueferUndSemester = new ArrayList<>();
                            List<String> datum = new ArrayList<>();
                            List<String> module = new ArrayList<>();
                            List<String> ID = new ArrayList<>();
                            List<String> Pruefform = new ArrayList<>();
                            List<String> raum = new ArrayList<>();

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
                            check.clear();
                            for (int i = 0; i < userdaten.size(); i++) {
                                String[] date2 = userdaten.get(i).getDatum().split(" ");
                                System.out.println(date2[0]);

                                //Überprüfung ob das Prüfitem Datum mit dem ausgewählten Kalender datum übereinstimmt
                                if (date2[0].equals(date)) {
                                    modulUndStudiengang.add(userdaten.get(i).getModul() + "\n " + userdaten.get(i).getStudiengang());
                                    prueferUndSemester.add(userdaten.get(i).getErstpruefer() + " " + userdaten.get(i).getZweitpruefer() + " " + userdaten.get(i).getSemester() + " ");
                                    datum.add(userdaten.get(i).getDatum());
                                    module.add(userdaten.get(i).getModul());
                                    ID.add(userdaten.get(i).getID());
                                    Pruefform.add(userdaten.get(i).getPruefform());
                                    raum.add(userdaten.get(i).getRaum());
                                    check.add(true);
                                }
                            }// define an adapter

                            //Adapter mit Werten füllen
                            mAdapter = new MyAdapter(modulUndStudiengang, prueferUndSemester, datum, module,ID,Pruefform,mLayout,raum);

                            //Anzeigen
                            recyclerView.setAdapter(mAdapter);
                        }
                    });
                    speicher = false;
                }
            }
        });
        return v;
    }

    public void Adapteruebergabe()
    {

        //Datenbankaufruf
        final List<Pruefplan> pruefplandaten = datenbank.userDao().getAll(validation);

        List<String> modulUndStudiengang = new ArrayList<>();
        List<String> prueferUndSemester = new ArrayList<>();
        List<String> datum = new ArrayList<>();
        List<String> modul = new ArrayList<>();
        List<String> ID = new ArrayList<>();
        List<String> pruefform = new ArrayList<>();
        List<String> raum = new ArrayList<>();
        check.clear();

        for (int i = 0; i < pruefplandaten.size(); i++) {
            modulUndStudiengang.add(pruefplandaten.get(i).getModul() + "\n " + pruefplandaten.get(i).getStudiengang());
            prueferUndSemester.add(pruefplandaten.get(i).getErstpruefer() + " " + pruefplandaten.get(i).getZweitpruefer() + " " + pruefplandaten.get(i).getSemester() + " ");
            datum.add(pruefplandaten.get(i).getDatum());
            modul.add(pruefplandaten.get(i).getModul());
            ID.add(pruefplandaten.get(i).getID());
            pruefform.add(pruefplandaten.get(i).getPruefform());
            raum.add(pruefplandaten.get(i).getRaum());
            check.add(true);
        }// define an adapter

        mAdapter = new MyAdapter(modulUndStudiengang, prueferUndSemester, datum, modul,ID,pruefform,mLayout,raum);
        recyclerView.setAdapter(mAdapter);

        // System.out.println(String.valueOf(userdaten.size()));

    }


}






