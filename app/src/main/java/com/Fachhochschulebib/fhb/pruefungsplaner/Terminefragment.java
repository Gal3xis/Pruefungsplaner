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

import android.Manifest;
import android.app.ProgressDialog;
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

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.Fachhochschulebib.fhb.pruefungsplaner.data.AppDatabase;
import com.Fachhochschulebib.fhb.pruefungsplaner.data.Courses;
import com.Fachhochschulebib.fhb.pruefungsplaner.data.TestPlanEntry;
import com.Fachhochschulebib.fhb.pruefungsplaner.model.RetrofitConnect;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;


public class Terminefragment extends Fragment {

    ProgressDialog progressBar;
    private RecyclerView recyclerView;
    private CalendarView calendar;
    private Button btnSearch;
    private String date;
    private String month2;
    private String day2;
    int positionOld = 0;
    private String year2;
    List<Boolean> checkList = new ArrayList<>();
    //Variablen
    List<String> moduleAndCourseList = new ArrayList<>();
    List<String> testerAndSemesterList = new ArrayList<>();
    List<String> dateList = new ArrayList<>();
    List<String> modulList = new ArrayList<>();
    List<String> idList = new ArrayList<>();
    List<String> formList = new ArrayList<>();
    List<String> roomList = new ArrayList<>();
    List<String> status = new ArrayList<>();
    List<String> statusMessage = new ArrayList<>();
    int sleeptime;
    String examineYear, currentExaminePeriod, returnCourse;

    public static String validation;
    MyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayout;

    AppDatabase database;

    // List<PruefplanEintrag> ppeList = datenbank.userDao().getAll(validation);
    // Start Merlin Gürtler
    private void createAdapter() {
        final List<TestPlanEntry> ppeList = database.userDao().getAll(validation);

        checkList.clear();
        ClearLists();

        for (TestPlanEntry entry : ppeList) {
            status.add(entry.getStatus());
            moduleAndCourseList.add(
                    entry.getModul() + "\n "
                            + entry.getCourse());
            testerAndSemesterList.add(
                    entry.getFirstTester()
                            + " " + entry.getSecondTester()
                            + " " + entry.getSemester() + " ");
            dateList.add(entry.getDate());
            modulList.add(entry.getModul());
            idList.add(entry.getID());
            formList.add(entry.getExamForm());
            roomList.add(entry.getRoom());
            statusMessage.add(entry.getHint());
            checkList.add(true);
        }// define an adapter

        mAdapter = new MyAdapter(moduleAndCourseList,
                testerAndSemesterList,
                dateList,
                modulList,
                idList,
                formList,
                mLayout,
                roomList,
                status,
                statusMessage);
    }


    // Ende Merlin Gürtler

    public void onCreate(Bundle savedInstanceState) {

        // Start Merlin Gürtler
        //Zugriffrechte für den GoogleKalender
        //Id für den Google Kalender
        final int callbackId = 42;

        //Wert1: ID Google Kalender, Wert2: Rechte fürs Lesen, Wert3: Rechte fürs schreiben)
        checkPermission(callbackId,
                Manifest.permission.READ_CALENDAR,
                Manifest.permission.WRITE_CALENDAR);


        SharedPreferences mSharedPreferencesValidation
                = Terminefragment.this.getContext().getSharedPreferences("validation", 0);

        String courseMain = mSharedPreferencesValidation.getString("selectedStudiengang", "0");

        database = AppDatabase.getAppDatabase(this.getContext());

        final StartClass globalVariable = (StartClass) this.getContext().getApplicationContext();
        if (!globalVariable.isShowNoProgressBar() || globalVariable.isChangeFaculty()) {
            globalVariable.setShowNoProgressBar(true);
            globalVariable.setChangeFaculty(false);

            progressBar = new ProgressDialog(Terminefragment.this.getContext(),
                    R.style.ProgressStyle);

            // Erstelle den Fortschrittsbalken
            progressBar.setMessage(Terminefragment.this.getContext().getString(R.string.load));
            progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressBar.setCancelable(false);
            // Zeige den Fortschrittsbalken
            progressBar.show();

            // Die Daten für update aus den Shared Preferences
            SharedPreferences mSharedPreferencesPPServerAdress
                    = Terminefragment.this.getContext().getSharedPreferences("Server_Address", MODE_PRIVATE);

            String relativePPlanURL
                    = mSharedPreferencesPPServerAdress.getString("ServerRelUrlPath", "0");

            String serverAddress
                    = mSharedPreferencesPPServerAdress.getString("ServerIPAddress", "0");

            SharedPreferences mSharedPreferencesExamineYear
                    = Terminefragment.this.getContext()
                    .getSharedPreferences("PruefTermin", 0);

            String currentExamineYear
                    = mSharedPreferencesExamineYear.getString("aktPruefTermin", "0");

            RetrofitConnect retrofit = new RetrofitConnect(relativePPlanURL);

            // prüft ob auch alle ausgewählten Studiengänge vorhanden sind
            new Thread(new Runnable() {
                @Override
                public void run() {
                    List <Courses> courses = database.userDao().getCourses();

                    // aktualsiere die db Einträge

                    JSONArray studiengangIds = new JSONArray();

                    String courseName;

                    for(Courses course: courses) {
                        try {
                            courseName = course.getCourseName();
                            if(!course.getChoosen()) {
                                // lösche nicht die Einträge der gewählten Studiengänge und favorit
                                database.userDao().deletePruefplanEintragExceptChoosen(courseName, false);
                            }
                            if(database.userDao().getOneEntryByName(courseName) == null && course.getChoosen()) {
                                JSONObject idJson = new JSONObject();
                                idJson.put("ID", course.getSgid());
                                studiengangIds.put(idJson);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    // > 2 da auch bei einem leeren Json Array [] gesetzt werden
                    if(studiengangIds.toString().length() > 2) {
                        retrofit.UpdateUnkownCourses(
                                getContext(),
                                database,
                                examineYear,
                                currentExaminePeriod,
                                currentExamineYear,
                                serverAddress,
                                studiengangIds.toString());
                    }
                }
            }).start();

            new Thread(new Runnable() {
                @Override
                public void run() {

                        SharedPreferences sharedPrefSPruefTermin = Terminefragment.this.getContext().
                                getSharedPreferences("PruefTermin",Terminefragment.this.getContext().MODE_PRIVATE);
                        String examinePeriod  = sharedPrefSPruefTermin.
                                getString("aktPruefTermin","0");

                    // Prüft zusätzlich nioch ob sich die Prüfungsphase geändert hat
                    if (database.userDao().getByName(courseMain).size() == 0
                    || !examinePeriod.equals(database.userDao().getTermin())) {

                        database.userDao().deleteTestPlanEntryAll();

                        retrofit.RetrofitWebAccess(
                                Terminefragment.this.getContext(),
                                database,
                                examineYear,
                                currentExaminePeriod,
                                currentExamineYear,
                                serverAddress);

                        sleeptime = 3000;
                    } else {

                        retrofit.retroUpdate(Terminefragment.this.getContext(), database,
                                examineYear,
                                currentExaminePeriod,
                                currentExamineYear,
                                serverAddress);

                        sleeptime = 2000;
                    }

                    createAdapter();

                    try {
                        // Timeout für die Progressbar
                        Thread.sleep(sleeptime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    progressBar.dismiss();

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.setAdapter(mAdapter);
                        }
                    });
                }
            }).start();
        }

        // Nun aus Shared Preferences
        mSharedPreferencesValidation
                = Terminefragment.
                this.getContext().getSharedPreferences("validation", 0);

        examineYear = mSharedPreferencesValidation.getString("pruefJahr", "0");
        currentExaminePeriod = mSharedPreferencesValidation.getString("aktuellePruefphase", "0");
        returnCourse = mSharedPreferencesValidation.getString("rueckgabeStudiengang", "0");

        validation = examineYear + returnCourse + currentExaminePeriod;
        // Ende Merlin Gürtler

        LongOperation asynctask = new LongOperation();

        asynctask.execute("");

        super.onCreate(savedInstanceState);
    }

    class LongOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            List<TestPlanEntry> ppeList
                    = Terminefragment.this.database.userDao().getAll(validation);
            if (ppeList.size() < 1) {
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
            AdapterPassed();
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
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

        AdapterPassed();

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
                        layout1.setOnClickListener(v1 -> {
                            Log.e("@@@@@", "" + position);
                            if (txtSecondScreen.getVisibility() == v1.VISIBLE) {
                                txtSecondScreen.setVisibility(v1.GONE);
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
                        positionOld = position;
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

        //Touchhelper für die Recyclerview-Komponente, zum Überprüfen, ob gescrollt wurde
        //ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        //itemTouchhelper.attachToRecyclerView(recyclerView);

        //initialisieren der UI-Komponenten
        calendar = (CalendarView) v.findViewById(R.id.caCalender);
        btnSearch = (Button) v.findViewById(R.id.btnDatum);
        calendar.setVisibility(View.GONE);

        //Clicklistener für den Kalender,
        //Es wird überprüft, welches Datum ausgewählt wurde.
        btnSearch.setOnClickListener(new View.OnClickListener() {
            boolean save = true;

            @Override
            public void onClick(View v) {
                if (!save) {
                    calendar.setVisibility(View.GONE);
                    AdapterPassed();
                    save = true;

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
                                    List<TestPlanEntry> ppeList = database.userDao().getAll(validation);

                                    //unnötige Werte entfernen
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
                                    checkList.clear();

                                    ClearLists();
                                    for (TestPlanEntry eintrag : ppeList) {
                                        String[] date2 = eintrag.getDate().split(" ");

                                /*  Überprüfung ob das Prüfitem Datum mit dem ausgewählten
                                    Kalender datum übereinstimmt
                                 */
                                        if (date2[0].equals(date)) {
                                            status.add(eintrag.getStatus());
                                            moduleAndCourseList.add(
                                                    eintrag.getModul()
                                                            + "\n " + eintrag.getCourse());
                                            testerAndSemesterList.add(
                                                    eintrag.getFirstTester()
                                                            + " " + eintrag.getSecondTester()
                                                            + " " + eintrag.getSemester() + " ");
                                            dateList.add(eintrag.getDate());
                                            modulList.add(eintrag.getModul());
                                            idList.add(eintrag.getID());
                                            formList.add(eintrag.getExamForm());
                                            roomList.add(eintrag.getRoom());
                                            statusMessage.add(eintrag.getHint());
                                            checkList.add(true);
                                        }
                                    }// define an adapter

                                    //Adapter mit Werten füllen
                                    mAdapter = new MyAdapter(moduleAndCourseList,
                                            testerAndSemesterList,
                                            dateList,
                                            modulList,
                                            idList,
                                            formList, mLayout,
                                            roomList,
                                            status,
                                            statusMessage);
                                    //Anzeigen
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
        return v;
    }

    public void AdapterPassed() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                createAdapter();

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(mAdapter);
                        // Merlin Gürtler
                        // Aktiviert den swipe listener
                        enableSwipeToDelete();
                    }
                });
                // System.out.println(String.valueOf(userdaten.size()));
            }
        }).start();
        //Datenbankaufruf
    }

    public void ClearLists() {
        moduleAndCourseList.clear();
        testerAndSemesterList.clear();
        dateList.clear();
        modulList.clear();
        idList.clear();
        formList.clear();
        roomList.clear();
        status.clear();
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

    private void checkPermission(int callbackId, String... permissionsId) {
        boolean permissions = true;
        for (String p : permissionsId) {
            permissions = permissions
                    && ContextCompat
                    .checkSelfPermission(this.getContext(), p) == PERMISSION_GRANTED;
        }
        if (!permissions)
            ActivityCompat.requestPermissions(Terminefragment.this.getActivity(), permissionsId, callbackId);
    }
    // Ende Merlin Gürtler
}