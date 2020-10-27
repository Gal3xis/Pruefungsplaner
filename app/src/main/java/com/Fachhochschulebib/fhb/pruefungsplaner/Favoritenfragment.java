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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Fachhochschulebib.fhb.pruefungsplaner.data.AppDatabase;
import com.Fachhochschulebib.fhb.pruefungsplaner.data.PruefplanEintrag;

import java.util.ArrayList;
import java.util.List;


public class Favoritenfragment extends Fragment {
    private RecyclerView recyclerView;
    private CalendarView calendar;
    private  Button btnsuche;
    MyAdapterfavorits mAdapter;
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
        List<String> form = new ArrayList<>();
        btnsuche.setVisibility(View.INVISIBLE);

        // Merlin Gürtler
        // Aktiviert den swipe listener
        enableSwipeToDelete();

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
                    form.add(eintrag.getPruefform());
                    check.add(true);
                }

                // definiere adapter
                // übergabe der variablen an den Recyclerview Adapter, für die darstellung
                mAdapter = new MyAdapterfavorits(studiengang, profnamen, datum, pruefungsNr,raum, form);

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(mAdapter);
                    }
                });
            }
        }).start();

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

        // Start Merlin Gürtler
        recyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {
            }

            // Wenn ein Element den Viewport verlässt, wird
            // der zweite Screen zu geklappt
            @Override
            public void onChildViewDetachedFromWindow(View view) {
                final TextView txtSecondScreen
                        = (TextView) view.findViewById(R.id.txtSecondscreen);
                if (txtSecondScreen.getVisibility() == view.VISIBLE) {
                    txtSecondScreen.setVisibility(view.GONE);
                }
            }
        });
        // Ende Merlin Gürtler
        return v;
    }

    // Start Merlin Gürtler
    private void enableSwipeToDelete() {
        // try and catch, da es bei einer
        // Orientierungsänderung sonst zu
        // einer NullPointerException kommt
        try {
            // Definiert den Listener
            swipeListener swipeToDeleteCallback = new swipeListener(getContext(), true) {
                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                    final int position = viewHolder.getAdapterPosition();
                    mAdapter.remove(position);
                }
            };

            // Setzt den Listener
            ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
            itemTouchhelper.attachToRecyclerView(recyclerView);
        } catch (Exception e) {
            Log.d("Error", "Orientation error" + e);
        }
    }
    // Ende Merlin Gürtler
}