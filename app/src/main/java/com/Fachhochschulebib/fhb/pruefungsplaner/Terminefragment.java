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

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import com.Fachhochschulebib.fhb.pruefungsplaner.data.AppDatabase;
import com.Fachhochschulebib.fhb.pruefungsplaner.data.Courses;
import com.Fachhochschulebib.fhb.pruefungsplaner.data.TestPlanEntry;
import com.Fachhochschulebib.fhb.pruefungsplaner.model.RetrofitConnect;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import static android.content.Context.MODE_PRIVATE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;


public class Terminefragment extends Fragment {

    ProgressDialog progressBar;
    private RecyclerView recyclerView;
    private TextView currentPeriodeTextView;
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
    List<String> examinerAndSemester = new ArrayList<>();
    List<String> dateList = new ArrayList<>();
    List<String> moduleList = new ArrayList<>();
    List<String> idList = new ArrayList<>();
    List<String> formList = new ArrayList<>();
    List<String> roomList = new ArrayList<>();
    List<String> statusMessage = new ArrayList<>();
    SharedPreferences mSharedPreferencesValidation;
    int sleeptime;
    String examineYear, currentExaminePeriod, returnCourse;

    public static String validation;
    MyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayout;

    AppDatabase database;

    // Start Merlin Gürtler
    // Funktion um die Führende 0 hinzuzufügen
    public String formatDate (String dateToFormat) {
        if(dateToFormat.length() == 1) {
            dateToFormat = "0" + dateToFormat;
        }
        return dateToFormat;
    }
    // Ende Merlin Gürtler

    // List<PruefplanEintrag> ppeList = datenbank.userDao().getByValidation(validation);
    // Start Merlin Gürtler
    private void createAdapter() {
        // Nun aus Shared Preferences
        examineYear = mSharedPreferencesValidation.getString("examineYear", "0");
        currentExaminePeriod = mSharedPreferencesValidation.getString("currentPeriode", "0");
        returnCourse = mSharedPreferencesValidation.getString("returnCourse", "0");

        validation = examineYear + returnCourse + currentExaminePeriod;

        final List<TestPlanEntry> ppeList = database.userDao().getByValidation(validation);

        ClearLists();
        for (TestPlanEntry entry : ppeList) {
            moduleAndCourseList.add(
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
            statusMessage.add(entry.getHint());
            checkList.add(true);
        }// define an adapter

        mAdapter = new MyAdapter(moduleAndCourseList,
                examinerAndSemester,
                dateList,
                moduleList,
                idList,
                formList,
                mLayout,
                roomList,
                statusMessage);

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                recyclerView.setAdapter(mAdapter);
            }
        });
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

        mSharedPreferencesValidation
                = Terminefragment.this.getContext().getSharedPreferences("validation", 0);

        String courseMain = mSharedPreferencesValidation.getString("selectedCourse", "0");

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
                    .getSharedPreferences("examineTermin", 0);

            String currentExamineYear
                    = mSharedPreferencesExamineYear.getString("currentTermin", "0");

            RetrofitConnect retrofit = new RetrofitConnect(relativePPlanURL);

            // Thread um die Prüfperiode zu aktualisieren
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //Shared Pref. für die Pruefperiode
                    SharedPreferences.Editor mEditor;
                    SharedPreferences mSharedPreferencesPPeriode
                            = getContext().getSharedPreferences("currentPeriode", MODE_PRIVATE);

                    // Erhalte die gewählte Fakultät aus den Shared Preferences
                    String facultyId = mSharedPreferencesValidation.getString("returnFaculty", "0");

                    try {
                        StringBuilder result = new StringBuilder();

                        //DONE (08/2020 LG)
                        String address = serverAddress + relativePPlanURL + "entity.pruefperioden";

                        final URL url = new URL(address);

                    /*
                        HttpURLConnection anstelle Retrofit, um die XML/Json-Daten abzufragen!!!
                     */
                        final HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                        urlConn.setConnectTimeout(1000 * 10); // mTimeout is in seconds

                        try {
                            urlConn.connect();
                        }catch (Exception e)
                        {
                            Log.d("Output exception",e.toString());
                        }

                        //Variablen zum lesen der erhaltenen werte
                        InputStream in = new BufferedInputStream(urlConn.getInputStream());
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                        String line;
                        while ((line = reader.readLine()) != null) {
                            result.append(line);
                        }


                        JSONObject jsonObj = null;
                        try {
                            jsonObj = XML.toJSONObject(String.valueOf(result));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //hinzufügen der erhaltenen werte JSONObject werte zum JSONArray
                        Iterator x = jsonObj.keys();
                        JSONArray jsonArray = new JSONArray();
                        while (x.hasNext()){
                            String key = (String) x.next();
                            jsonArray.put(jsonObj.get(key));
                        }

                        JSONArray examinePeriodArray = new JSONArray();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            examinePeriodArray.put(object.get("pruefperioden"));
                        }
                        String arrayZuString = examinePeriodArray.toString();
                        String erstesUndletztesZeichenentfernen
                                = arrayZuString.substring(1,arrayZuString.length()-1);
                        JSONArray mainObject2 = new JSONArray(erstesUndletztesZeichenentfernen);

                        //DONE (09/2020 LG) Aktuellen Prüftermin aus JSON-String herausfiltern!
                        //Heutiges Datum als Vergleichsdatum ermitteln und den Formatierer festlegen.
                        GregorianCalendar now = new GregorianCalendar();
                        Date currentDate = now.getTime();
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

                        JSONObject currentExamineDate = null;
                        String date;
                        String facultyIdDB;
                        Date examineDate;
                        Date lastDayPp;
                        int examineWeek;

                        //Durch-Iterieren durch alle Prüfperioden-Objekte des JSON-Ergebnisses
                        for (int i=0;i<mainObject2.length();i++) {
                            currentExamineDate = mainObject2.getJSONObject(i);
                            date = currentExamineDate.get("startDatum").toString();
                            facultyIdDB =  currentExamineDate.getJSONObject("fbFbid").get("fbid").toString();
                            //Aus dem String das Datum herauslösen
                            date = date.substring(0,10);
                            //und in ein Date-Objekt umwandeln
                            examineDate = formatter.parse(date);

                            // Erhalte die Anzahl der Wochen
                            examineWeek = Integer.parseInt(currentExamineDate.get("PPWochen").toString());

                            Calendar c = Calendar.getInstance();
                            c.setTime(examineDate);
                            c.add(Calendar.DATE, 7 * examineWeek - 2);  // Anzahl der Tage Klausurenphase
                            lastDayPp = formatter.parse(formatter.format(c.getTime()));

                            //und mit dem heutigen Datum vergleichen.
                            //Die erste Prüfperioden dieser Iteration, die nach dem heutigen Datum
                            //liegt ist die aktuelle Prüfperiode!
                            // die Fakultäts id wird noch mit der gewählten Fakultät verglichen
                            if(currentDate.before(lastDayPp) && facultyId.equals(facultyIdDB)) break;
                        }

                        examineWeek = Integer.parseInt(currentExamineDate.get("PPWochen").toString());

                        //1 --> 1. Termin; 2 --> 2. Termin des jeweiligen Semesters
                        //-------------------------------------------------------------------
                        //DONE (08/2020) Termin 1 bzw. 2 in den Präferenzen speichern
                        SharedPreferences mSharedPreferencesExamineTermin
                                = getContext()
                                .getSharedPreferences("examineTermin", MODE_PRIVATE);

                        SharedPreferences.Editor mEditorExaminePeriodAndYear = mSharedPreferencesValidation.edit();

                        mEditorExaminePeriodAndYear.putString("examineYear", currentExamineDate.get("PPJahr").toString());

                        SharedPreferences.Editor mEditorTermin = mSharedPreferencesExamineTermin.edit();
                        mEditorTermin.putString("currentTermin", currentExamineDate.get("pruefTermin").toString());
                        mEditorExaminePeriodAndYear.putString("currentPeriode", currentExamineDate.get("pruefSemester").toString());

                        mEditorExaminePeriodAndYear.apply();


                        mEditorTermin.apply();  //Ausführen der Schreiboperation!
                        //-------------------------------------------------------------------

                        String currentPeriode = currentExamineDate.get("startDatum").toString();
                        String[] arrayCurrentPeriode=  currentPeriode.split("T");
                        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                        Date inputDate = fmt.parse(arrayCurrentPeriode[0]);

                        //erhaltenes Datum Parsen als Datum
                        Calendar calendar = new GregorianCalendar();
                        calendar.setTime(inputDate);
                        int year = calendar.get(Calendar.YEAR);
                        //Add one to month {0 - 11}
                        int month = calendar.get(Calendar.MONTH) + 1;
                        int day = calendar.get(Calendar.DAY_OF_MONTH);
                        calendar.add(Calendar.DATE, 7 * examineWeek - 2);
                        int year2 = calendar.get(Calendar.YEAR);
                        //Add one to month {0 - 11}
                        int month2 = calendar.get(Calendar.MONTH) + 1;
                        int day2 = calendar.get(Calendar.DAY_OF_MONTH);

                        //String Prüfperiode zum Anzeigen
                        String currentExamineDateFormatted = getContext().getString(R.string.current)
                                + formatDate(String.valueOf(day))
                                +"."+ formatDate(String.valueOf(month))
                                +"."+ year +getContext().getString(R.string.bis)
                                + formatDate(String.valueOf(day2))
                                +"."+ formatDate(String.valueOf(month2))
                                +"."+ year2;  // number of days to add;

                        //Prüfperiode für die Offline-Verwendung speichern
                        mEditor = mSharedPreferencesPPeriode.edit();

                        // Start Merlin Gürtler
                        // Speichere das Start und Enddatum der Prüfperiode
                        mEditor.putString("startDate", formatDate(String.valueOf(day))
                                + "/" + formatDate(String.valueOf(month)) + "/" + formatDate(String.valueOf(year)));
                        mEditor.putString("endDate", formatDate(String.valueOf(day2))
                                + "/" + formatDate(String.valueOf(month2)) + "/" + formatDate(String.valueOf(year2)));
                        mEditor.apply();
                        // Ende Merlin Gürtler

                        String strJson
                                = mSharedPreferencesPPeriode.getString("currentPeriode", "0");
                        if (strJson != null) {
                            if (strJson.equals(currentExamineDateFormatted))
                            {
                            }
                            else{
                                mEditor.clear();
                                mEditor.apply();
                                mEditor.putString("currentPeriode", currentExamineDateFormatted);
                                mEditor.apply();
                            }
                        }
                        // Ende Merlin Gürtler
                    }
                    catch (final Exception e)
                    {
                        Log.d("Output", "Konnte nicht die Pruefphase aktualisieren");
                        //Keineverbindung();
                    }
                    // Nun aus Shared Preferences
                    // die Daten für die Periode aus den Shared Preferences
                    String examineYearThread = mSharedPreferencesValidation.getString("examineYear", "0");
                    String currentExaminePeriodThread = mSharedPreferencesValidation.getString("currentPeriode", "0");

                    String currentExamineYearThread
                            = mSharedPreferencesExamineYear.getString("currentTermin", "0");
                    if (database.userDao().getByName(courseMain).size() == 0
                            || !currentExamineYearThread.equals(database.userDao().getTermin())) {

                        database.userDao().deleteTestPlanEntryAll();

                        retrofit.RetrofitWebAccess(
                                Terminefragment.this.getContext(),
                                database,
                                examineYearThread,
                                currentExaminePeriodThread,
                                currentExamineYearThread,
                                serverAddress);

                        sleeptime = 3000;
                    } else {

                        retrofit.retroUpdate(Terminefragment.this.getContext(), database,
                                examineYearThread,
                                currentExaminePeriodThread,
                                currentExamineYearThread,
                                serverAddress);

                        sleeptime = 2000;
                    }
                    try {
                        // Timeout für die Progressbar
                        Thread.sleep(sleeptime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    progressBar.dismiss();

                    createAdapter();

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            String strJson
                                    = mSharedPreferencesPPeriode.getString("currentPeriode", "0");
                            if (!strJson.equals("0")) {
                                currentPeriodeTextView.setText(strJson);
                            }
                        }
                    });
                }
            }).start();

            // prüft ob auch alle ausgewählten Studiengänge vorhanden sind
            new Thread(new Runnable() {
                @Override
                public void run() {
                    List <Courses> courses = database.userDao().getCourses();

                    // aktualsiere die db Einträge
                    JSONArray courseIds = new JSONArray();

                    String courseName;

                    for(Courses course: courses) {
                        try {
                            courseName = course.getCourseName();
                            if(!course.getChoosen()) {
                                // lösche nicht die Einträge der gewählten Studiengänge und Favorit
                                List<TestPlanEntry> toDelete = database.userDao().getByCourseName(courseName,false);
                                database.userDao().deleteEntry(toDelete);
                            }
                            if(database.userDao().getOneEntryByName(courseName, false) == null && course.getChoosen()) {
                                JSONObject idJson = new JSONObject();
                                idJson.put("ID", course.getSgid());
                                courseIds.put(idJson);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    // > 2 da auch bei einem leeren Json Array [] gesetzt werden

                    if(courseIds.toString().length() > 2) {
                        retrofit.UpdateUnkownCourses(
                                getContext(),
                                database,
                                examineYear,
                                currentExaminePeriod,
                                currentExamineYear,
                                serverAddress,
                                courseIds.toString());
                    }
                }
            }).start();
        }

        LongOperation asynctask = new LongOperation();

        asynctask.execute("");

        super.onCreate(savedInstanceState);
    }

    class LongOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            List<TestPlanEntry> ppeList
                    = Terminefragment.this.database.userDao().getByValidation(validation);
            if (ppeList.size() < 1) {
                for (int c = 0; c < 1000; c++) {
                    try {
                        Thread.sleep(3000);
                        if (RetrofitConnect.checkTransmission) {
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
        currentPeriodeTextView = (TextView) v.findViewById(R.id.currentPeriode);

        SharedPreferences mSharedPreferencesPPeriode
                = getContext().getSharedPreferences("currentPeriode", MODE_PRIVATE);

        String strJson
                = mSharedPreferencesPPeriode.getString("currentPeriode", "0");
        if(!strJson.equals("0")) {
            currentPeriodeTextView.setText(strJson);
        }

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
                                    List<TestPlanEntry> ppeList = database.userDao().getByValidation(validation);

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

                                    ClearLists();
                                    for (TestPlanEntry entry : ppeList) {
                                        String[] date2 = entry.getDate().split(" ");

                                /*  Überprüfung ob das Prüfitem Datum mit dem ausgewählten
                                    Kalender datum übereinstimmt
                                 */
                                        if (date2[0].equals(date)) {
                                            moduleAndCourseList.add(
                                                    entry.getModule()
                                                            + "\n " + entry.getCourse());
                                            examinerAndSemester.add(
                                                    entry.getFirstExaminer()
                                                            + " " + entry.getSecondExaminer()
                                                            + " " + entry.getSemester() + " ");
                                            dateList.add(entry.getDate());
                                            moduleList.add(entry.getModule());
                                            idList.add(entry.getID());
                                            formList.add(entry.getExamForm());
                                            roomList.add(entry.getRoom());
                                            statusMessage.add(entry.getHint());
                                            checkList.add(true);
                                        }
                                    }// define an adapter

                                    //Adapter mit Werten füllen
                                    mAdapter = new MyAdapter(moduleAndCourseList,
                                            examinerAndSemester,
                                            dateList,
                                            moduleList,
                                            idList,
                                            formList,
                                            mLayout,
                                            roomList,
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
        examinerAndSemester.clear();
        dateList.clear();
        moduleList.clear();
        idList.clear();
        formList.clear();
        roomList.clear();
        statusMessage.clear();
        checkList.clear();
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