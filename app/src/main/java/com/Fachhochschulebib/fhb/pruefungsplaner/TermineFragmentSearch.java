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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Fachhochschulebib.fhb.pruefungsplaner.data.AppDatabase;
import com.Fachhochschulebib.fhb.pruefungsplaner.data.TestPlanEntry;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class TermineFragmentSearch extends Fragment {
    SharedPreferences mSharedPreferencesValidation;

    private RecyclerView recyclerView;
    private CalendarView calendar;
    private TextView currentPeriodeTextView;
    private Button btnSearch;
    private String date;
    String examineYear, currentExaminePeriod, returnCourse, validation;

    List<Boolean> checkList = new ArrayList<>();
    List<String> modulAndCourseList = new ArrayList<>();
    List<String> examinerAndSemester = new ArrayList<>();
    List<String> dateList = new ArrayList<>();
    List<String> moduleList = new ArrayList<>();
    List<String> idList = new ArrayList<>();
    List<String> formList = new ArrayList<>();
    List<String> roomList = new ArrayList<>();
    List<String> statusList = new ArrayList<>();

    private String month2;
    private String day2;
    // private int position2 = 0;
    private String year2;
    private RecyclerView.LayoutManager mLayout;
    MyAdapter mAdapter;
    List<Integer> valuesToShowList = new ArrayList<>();


    public void onCreate(Bundle savedInstanceState) {
        // Start Merlin Gürtler
        // Nun aus Shared Preferences

        mSharedPreferencesValidation
                = TermineFragmentSearch.
                this.getContext().getSharedPreferences("validation", 0);

        examineYear = mSharedPreferencesValidation.getString("examineYear", "0");
        currentExaminePeriod = mSharedPreferencesValidation.getString("currentPeriode", "0");
        returnCourse = mSharedPreferencesValidation.getString("returnCourse", "0");

        validation = examineYear + returnCourse + currentExaminePeriod;
        // Ende Merlin Gürtler

        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.terminefragment, container, false);


        //hinzufügen von recycleview
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView4);

        currentPeriodeTextView = (TextView) v.findViewById(R.id.currentPeriode);

        SharedPreferences mSharedPreferencesPPeriode
                = getContext().getSharedPreferences("currentPeriode", MODE_PRIVATE);

        String strJson
                = mSharedPreferencesPPeriode.getString("currentPeriode", "0");
        currentPeriodeTextView.setText(strJson);

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
        btnSearch = (Button) v.findViewById(R.id.btnDatum);

        adapterPassed();

        calendar.setVisibility(View.GONE);
        //btnsuche clicklistener überprüft, ob der "Kalender öffnen" - Button angetippt wurde
        /*  Es werden bei eingeschaltetem Kalender nur Prüfobjekte mit übereinstimmenden
            Datum angezeigt.
         */
        btnSearch.setOnClickListener(new View.OnClickListener() {
            boolean save = true;

            @Override
            public void onClick(View v) {
                if (!save) {
                    calendar.setVisibility(View.GONE);
                    adapterPassed();
                    save = true;
                } else {
                    //Kalender ist geöffnet, nur übereinstimmende Prüfungen anzeigen
                    calendar.setVisibility(View.VISIBLE);
                    calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                        public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    AppDatabase roomData = AppDatabase.getAppDatabase(getContext());
                                    List<TestPlanEntry> ppeList = roomData.userDao().getAll();

                                    ClearLists();

                                    //Creating editor to store uebergebeneModule to shared preferences
                                    if (month < 9) {
                                        month2 = "0" + String.valueOf(month + 1);
                                    } else {
                                        month2 = String.valueOf(month + 1);
                                    }
                                    if (dayOfMonth < 10) {
                                        day2 = "0" + String.valueOf(dayOfMonth);
                                    } else {
                                        day2 = String.valueOf(dayOfMonth);
                                    }
                                    year2 = String.valueOf(year);
                                    date = year2 + "-" + month2 + "-" + day2;

                                    for (TestPlanEntry entry : ppeList) {
                                        String[] date2 = entry.getDate().split(" ");

                                        if (date2[0].equals(date) && entry.getChoosen()) {
                                            modulAndCourseList.add(
                                                    entry.getModule() + "\n "
                                                            + entry.getCourse());
                                            examinerAndSemester.add(
                                                    entry.getFirstExaminer()
                                                            + " " + entry.getSecondExaminer()
                                                            + " " + entry.getSemester() + " ");
                                            dateList.add(entry.getDate());
                                            moduleList.add(entry.getModule());
                                            idList.add(entry.getID());
                                            formList.add(entry.getExamForm());
                                            roomList.add(entry.getRoom());
                                            statusList.add(entry.getHint());
                                            checkList.add(true);
                                        }
                                    }
                                    // define an adapter
                                    //Werte an den Adapter übergeben
                                    mAdapter = new MyAdapter(
                                            modulAndCourseList,
                                            examinerAndSemester,
                                            dateList,
                                            moduleList,
                                            idList,
                                            formList,
                                            mLayout,
                                            roomList,
                                            statusList);

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
                    save = false;
                }
            }
        });

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {
                        final TextView txtSecondScreen
                                = (TextView) view.findViewById(R.id.txtSecondscreen);
                        View viewItem
                                = recyclerView.getLayoutManager().findViewByPosition(position);
                        LinearLayout layout1
                                = (LinearLayout) viewItem.findViewById(R.id.linearLayout);

                        layout1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.e("@@@@@", "" + position);
                                if (txtSecondScreen.getVisibility() == v.VISIBLE) {
                                    txtSecondScreen.setVisibility(v.GONE);
                                    checkList.set(position, false);
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
                                        } catch (Exception e) {
                                            Log.d("ERROR", "NOT IN VIEW PORT " + e);
                                        }
                                    }
                                    // Ende Merlin Gürtler
                                    txtSecondScreen.setVisibility(v.VISIBLE);
                                    txtSecondScreen.setText(mAdapter.giveString(position));
                                }
                            }
                        });
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

    public void adapterPassed() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                //Datenbank initialisieren
                AppDatabase database = AppDatabase.getAppDatabase(getContext());

                // Änderung Merlin Gürtler
                // List<Pruefplan> pruefplandaten = datenbank.userDao().getByValidation(validation);
                // Für die Suche von Modulen
                List<TestPlanEntry> ppeList = database.userDao().getAll();
                // Ende Änderung Merlin Gürtler

                ClearLists();

                for (int i = 0; i < ppeList.size(); i++) {
                    if (ppeList.get(i).getChoosen()) {
                        valuesToShowList.add(i);
                    }
                }

                //Variablen mit Werten aus der lokalen Datenbank füllen
                for (int i = 0; i < valuesToShowList.size(); i++) {
                    modulAndCourseList.add(ppeList.get(valuesToShowList.get(i)).getModule() + "\n " + ppeList.get(Integer.valueOf(valuesToShowList.get(i))).getCourse());
                    examinerAndSemester.add(ppeList.get(valuesToShowList.get(i)).getFirstExaminer() + " " + ppeList.get(Integer.valueOf(valuesToShowList.get(i))).getSecondExaminer() + " " + ppeList.get(Integer.valueOf(valuesToShowList.get(i))).getSemester() + " ");
                    dateList.add(ppeList.get(valuesToShowList.get(i)).getDate());
                    moduleList.add(ppeList.get(valuesToShowList.get(i)).getModule());
                    idList.add(ppeList.get(valuesToShowList.get(i)).getID());
                    formList.add(ppeList.get(valuesToShowList.get(i)).getExamForm());
                    roomList.add(ppeList.get(valuesToShowList.get(i)).getRoom());
                    statusList.add(ppeList.get(valuesToShowList.get(i)).getHint());
                    checkList.add(true);
                }

                // define an adapter
                mAdapter = new MyAdapter(
                        modulAndCourseList,
                        examinerAndSemester,
                        dateList,
                        moduleList,
                        idList,
                        formList,
                        mLayout,
                        roomList,
                        statusList);

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(mAdapter);
                        enableSwipeToDelete();
                    }
                });
            }
        }).start();
    }

    public void ClearLists() {
        modulAndCourseList.clear();
        examinerAndSemester.clear();
        dateList.clear();
        moduleList.clear();
        idList.clear();
        formList.clear();
        roomList.clear();
        statusList.clear();
    }


    // Start Merlin Gürtler
    private void enableSwipeToDelete() {
        // try and catch, da es bei einer
        // Orientierungsänderung sonst zu
        // einer NullPointerException kommt
        try {
            // Definiert den Listener
            swipeListener swipeToDeleteCallback = new swipeListener(getContext(), false) {
                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                    final int position = viewHolder.getAdapterPosition();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            boolean isFavorite = mAdapter.checkFavorite(viewHolder.getAdapterPosition());
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    if (isFavorite) {
                                        mAdapter.deleteFromFavorites(position, (MyAdapter.ViewHolder) viewHolder);
                                    } else {
                                        mAdapter.addToFavorites(position, (MyAdapter.ViewHolder) viewHolder);
                                    }
                                    mAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }).start();
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






