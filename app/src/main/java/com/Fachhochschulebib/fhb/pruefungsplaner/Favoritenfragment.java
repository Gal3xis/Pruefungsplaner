package com.Fachhochschulebib.fhb.pruefungsplaner;
//////////////////////////////
// favoritenfragment
//
//
// autor:
// inhalt:  stelllt die favorisierten prüfungen bereit.
// zugriffsdatum: 11.12.19
//
//
//
//////////////////////////////


import android.os.Bundle;
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

import static com.Fachhochschulebib.fhb.pruefungsplaner.MainActivity.mAdapter;


public class Favoritenfragment extends Fragment {
    private RecyclerView recyclerView;
    private CalendarView calendar;
    private  Button btnsuche;
    List<Boolean> check = new ArrayList<>();

    // Datenbank initialisierung
    AppDatabase roomdaten = AppDatabase.getAppDatabase(getContext());

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.terminefragment, container, false);

        //Komponenten  initialisieren für die Verwendung
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView4);
        recyclerView.setHasFixedSize(true);
        //linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(v.getContext());
        recyclerView.setLayoutManager(layoutManager);
        calendar = (CalendarView) v.findViewById(R.id.caCalender);
        btnsuche = (Button) v.findViewById(R.id.btnDatum);

        calendar.setVisibility(View.GONE);
        List<String> studiengang = new ArrayList<>();
        List<String> profnamen = new ArrayList<>();
        List<String> datum = new ArrayList<>();
        List<String> pruefungsNr = new ArrayList<>();
        List<String> raum = new ArrayList<>();
        btnsuche.setVisibility(View.INVISIBLE);

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<PruefplanEintrag> ppeList = roomdaten.userDao().getFavorites(true);


                // Abfrage ob Prüfungen favorisiert wurden
                // Favorisierte Prüfungen für die Anzeige vorbereiten
                for (PruefplanEintrag eintrag: ppeList) {
                    studiengang.add(eintrag.getModul() + " "
                            + eintrag.getStudiengang());
                    profnamen.add(eintrag.getErstpruefer() + " "
                            + eintrag.getZweitpruefer() + " "
                            + eintrag.getSemester());
                    datum.add(eintrag.getDatum());
                    pruefungsNr.add(eintrag.getID());
                    raum.add(eintrag.getRaum());
                    check.add(true);
                }
            }
        }).start();

        // definiere adapter
        // übergabe der variablen an den Recyclerview Adapter, für die darstellung
        mAdapter = new MyAdapterfavorits(studiengang, profnamen, datum, pruefungsNr,raum);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new   RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick( final View view, final  int position) {

                        //einblenden vom Textview für die weiteren Informationen
                        final TextView txtSecondScreen
                                = (TextView) view.findViewById(R.id.txtSecondscreen);
                        View viewItem
                                = recyclerView.getLayoutManager().findViewByPosition(position);
                        LinearLayout layoutinformationen
                                =(LinearLayout) viewItem.findViewById(R.id.linearLayout);


                        //überprüfung ob das linear layout geklickt wurde
                        layoutinformationen.setOnClickListener(new  View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (txtSecondScreen.getVisibility() == v.VISIBLE) {
                                    txtSecondScreen.setVisibility(v.GONE);
                                    check.set(position,false);

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
                                    txtSecondScreen.setText(((MyAdapterfavorits) mAdapter)
                                                   .giveString(position));
                                }
                            }
                        });

                        /*
                        try{
                            if(check.get(position)) {
                                txtSecondScreen.setVisibility(v.VISIBLE);
                                txtSecondScreen.setText(((MyAdapterfavorits) mAdapter)
                                               .giveString(position));
                            }}
                        catch(Exception e){

                        }
                         */
                    }
                })
        );
        recyclerView.setAdapter(mAdapter);
        return v;
    }
}