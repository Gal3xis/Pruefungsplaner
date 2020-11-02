package com.Fachhochschulebib.fhb.pruefungsplaner;

//////////////////////////////
// MainActivity
//
// autor:
// inhalt:  Auswahl des Studiengangs mit dazugehörigem PruefJahr und Semester
// zugriffsdatum: 11.12.19, 08/2020 (LG)
//
//////////////////////////////

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import com.Fachhochschulebib.fhb.pruefungsplaner.data.AppDatabase;
import com.Fachhochschulebib.fhb.pruefungsplaner.data.Courses;
import com.Fachhochschulebib.fhb.pruefungsplaner.data.Uuid;
import com.Fachhochschulebib.fhb.pruefungsplaner.model.RetrofitConnect;

public class MainActivity extends AppCompatActivity {
    CheckListAdapter mAdapter;
    private RecyclerView recyclerView;

    private Button buttonSpinner;
    private Button buttonOk;
    public static String currentYear = null;
    public static String currentExamine = null;
    public static String returnCourse = null;
    public static String returnFaculty = null;
    private Boolean checkReturn = true;
    public static String currentDate;
    //KlassenVariablen
    private String courseMain;
    private JSONArray jsonArrayFacultys;
    final List<Boolean> courseChosen = new ArrayList<Boolean>();
    final List<String> courseName = new ArrayList<String>();
    final List<String> facultyName = new ArrayList<String>();
    // private Spinner spStudiengangMain;
    // List<String> idList = new ArrayList<String>();

    SharedPreferences mSharedPreferencesPPServerAdress;
    SharedPreferences mSharedPreferencesExaminePeriod;
    String serverAddress, relativePPlanURL;

    // Start Merlin Gürtler
    // Fügt den Hauptstudiengang zu den Shared Preferences hinzu
    private void addMainCourse(String choosenCourse) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // hole die Studiengang ID aus der DB
                AppDatabase database = AppDatabase.getAppDatabase(getBaseContext());
                returnCourse = database.userDao().getIdCourse(choosenCourse);

                // Erstelle Shared Pref für die anderen Fragmente
                SharedPreferences sharedPrefStudiengangValidation =
                        getApplicationContext().
                                getSharedPreferences("validation",0);

                SharedPreferences.Editor editorStudiengangValidation =
                        sharedPrefStudiengangValidation.edit();

                editorStudiengangValidation.putString("selectedCourse", choosenCourse);
                editorStudiengangValidation.putString("returnCourse", returnCourse);
                editorStudiengangValidation.apply();

                // Thread für die Uuid
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // Überprüfe ob die App schonmal gestartet wurde
                        if(database.userDao().getUuid() == null) {
                            RetrofitConnect retrofit = new RetrofitConnect(relativePPlanURL);
                            // Sende nur ans Backend wenn die App wirklich zum ersten mal
                            // gestartet wurde
                            retrofit.firstStart(getApplicationContext(), database,
                                    serverAddress);
                        }
                    }
                }).start();

                Intent mainWindow = new Intent(getApplicationContext(), table.class);
                startActivityForResult(mainWindow, 0);
            }
        }).start();
    }

    // Schließt die App beim BackButton
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0) {
            finish();
        }
    }
    // Ende Merlin Gürtler

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        final Context context = getBaseContext();

        //aufrufen des startlayouts
        setContentView(R.layout.start);

        buttonSpinner = findViewById(R.id.buttonForSpinner);
        buttonOk = findViewById(R.id.buttonOk);

        // Start Merlin Gürtler
        //Komponenten  initialisieren für die Verwendung
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewChecklist);
        recyclerView.setHasFixedSize(true);
        //linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        SharedPreferences mSharedPreferencesValidation
                = getApplication().getSharedPreferences("validation", 0);

        courseMain = mSharedPreferencesValidation.getString("selectedCourse", "0");
        // Ende Merlin Gürtler

        mSharedPreferencesPPServerAdress
                = getApplicationContext().getSharedPreferences("Server_Address", MODE_PRIVATE);
        SharedPreferences.Editor mEditorPPServerAdress = mSharedPreferencesPPServerAdress.edit();
        mEditorPPServerAdress.putString("ServerIPAddress", context.getString(R.string.server_adress));
        //mEditorPPServerAdress.putString("ServerIPAddress", "http://192.168.178.39:8080/");
        //alternativ in FH: "http://thor.ad.fh-bielefeld.de:8080/"
        mEditorPPServerAdress.apply(); //Schreiben der Präferenz!
        //Auslesen zur allgemeinen Verwendung in der aktuellen Activity
        serverAddress
                = mSharedPreferencesPPServerAdress.getString("ServerIPAddress", "0");

        SharedPreferences.Editor mEditorRelUrlPath = mSharedPreferencesPPServerAdress.edit();
        mEditorRelUrlPath.putString("ServerRelUrlPath", context.getString(R.string.server_url));
        mEditorRelUrlPath.apply(); //Schreiben der Präferenz!
        //Auslesen zur allgemeinen Verwendung in der aktuellen Activity
        relativePPlanURL
                = mSharedPreferencesPPServerAdress.getString("ServerRelUrlPath", "0");

        pingExaminePeriod();

        //Defininition des Arrays jahreszeit
        List<String> season = new ArrayList<String>();
        season.add(context.getString(R.string.sommer));
        season.add(context.getString(R.string.winter));

        //Kalender:: aktuelles Jahr --> Bestimmung der Prüfphase (WiSe, SoSe)
        Calendar calendar = Calendar.getInstance();
        int calendarMonth = calendar.get(Calendar.MONTH );
        Log.d("Output Monat",String.valueOf(calendarMonth));

        if (calendarMonth  <= 4)
        {
            currentExamine = context.getString(R.string.winter_short);
            int thisYear = calendar.get(Calendar.YEAR);
            currentYear = String.valueOf(thisYear);
        }

        if (calendarMonth  > 4)
        {
            currentExamine = context.getString(R.string.sommer_short);
            int thisYear = calendar.get(Calendar.YEAR);
            currentYear = String.valueOf(thisYear);
        }

        // TODO Nach Corona muss der Wert in der strings.xml wieder auf 9 geändert werden
        if (calendarMonth >= Integer.parseInt(
                getApplicationContext().getString(R.string.month_wise)
        )) {

            currentExamine = context.getString(R.string.winter_short);
            int thisYear = calendar.get(Calendar.YEAR);
            currentYear = String.valueOf(thisYear +1);
        }

        // Start Merlin Gürtler
        // Schreiben der Pruefphase in eine shared preference
        mSharedPreferencesExaminePeriod
                = getApplicationContext().getSharedPreferences("validation", MODE_PRIVATE);
        SharedPreferences.Editor mEditorExaminePeriodAndYear = mSharedPreferencesExaminePeriod.edit();
        mEditorExaminePeriodAndYear.putString("currentPeriode", currentExamine);
        mEditorExaminePeriodAndYear.putString("examineYear", currentYear);
        mEditorExaminePeriodAndYear.apply();

        // Ende Merlin Gürtler

        //Anzahl der Elemente
        //Adapter-Aufruf
        SharedPreferences sharedPrefPruefPeriode
                = getApplicationContext().getSharedPreferences("periode", MODE_PRIVATE);
        String strJson = sharedPrefPruefPeriode.getString("currentPeriode", "0");
        try {
            checkConnection();
            //Creating editor to store uebergebeneModule to shared preferencess
            //second parameter is necessary ie.,Value to return if this preference does not exist.
        }
        //Wenn Verbindung zum Server nicht möglich, dann Daten aus der Datenbank nehmen
        catch(Exception e) {
            if (strJson != null) {
                try {
                    jsonArrayFacultys = new JSONArray(strJson);

                    for (int i = 0; i < jsonArrayFacultys.length(); i++) {
                        JSONObject json = jsonArrayFacultys.getJSONObject(i);
                        facultyName.add(json.get("facName").toString());
                        facNamesToSpinner();
                    }

                } catch (Exception b) {
                    Log.d("Datenbankfehler","Keine Daten in der Datenbank vorhanden!");
                }
            }
        }
    }

    //Aufruf in onCreate()
    public void checkConnection(){
        boolean aktuelleurl
                = pingUrl(serverAddress + relativePPlanURL + "entity.faculty");

        if (!aktuelleurl) {
            KeineVerbindung();
        } else {
            // Start Merlin Gürtler
            final StartClass globalVariable = (StartClass) getApplicationContext();
            RetrofitConnect retrofit = new RetrofitConnect(relativePPlanURL);

            // initialisierung db
            AppDatabase database = AppDatabase.getAppDatabase(getBaseContext());

            // Thread für die Studiengänge
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if(database.userDao().getCourse().size() == 0) {
                        retrofit.getCourses(getApplication(), database, serverAddress);
                    }
                }
            }).start();

            // Thread für die UUid
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Uuid uuid = database.userDao().getUuid();
                    if(!globalVariable.getAppStarted() && uuid != null && !globalVariable.isChangeFaculty()) {
                        globalVariable.setAppStarted(true);
                        retrofit.anotherStart(getApplicationContext(), database,
                                serverAddress);
                    }
                }
            }).start();

            // Thread für die Navigation
            new Thread(new Runnable() {
                @Override
                public void run() {

                    // Skippe die erstauswahl, wenn schon ein Studiengang gewählt wurde
                    if(!courseMain.equals("0") && !globalVariable.isChangeFaculty()) {
                        Intent mainWindow = new Intent(getApplicationContext(), table.class);
                        startActivityForResult(mainWindow, 0);
                    }

                }
            }).start();
            // Ende Merlin Gürtler
        }

    }

    public void KeineVerbindung(){
        new Thread(() -> Toast.makeText(getApplicationContext(),
                getApplicationContext().getString(R.string.noConnection),
                Toast.LENGTH_SHORT).show());
    }


    //Übernahme der Fakultätsnamen in die Spinner-Komponente.
    public void facNamesToSpinner(){
        MainActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                /* Toast.makeText(getBaseContext(), "Prüfungen wurden aktualisiert",
                                    Toast.LENGTH_SHORT).show();
                 */
                //spinnerarray für die fakultaeten
                //adapter aufruf

                // Start Merlin Gürtler
                buttonSpinner.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Erstelle das Dialog Feld für die Auswahl
                        AlertDialog.Builder chooseFaculty = new AlertDialog.Builder(MainActivity.this,
                                R.style.customAlertDialog);

                        String[] facultys = new String [facultyName.size()];

                        for(int i = 0; i < facultyName.size(); i++) {
                            facultys[i] = facultyName.get(i);
                        }

                        // Der Listener für die Item Auswahl
                        chooseFaculty.setItems(facultys, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                for (int i = 0; i < facultyName.size(); i++)
                                {
                                    if (facultys[which]
                                            .equals(facultyName.get(i))) {
                                        try{
                                            JSONObject object = jsonArrayFacultys.getJSONObject(i);
                                            returnFaculty = object.get("fbid").toString();
                                            Log.d("Output Fakultaet",
                                                    returnFaculty);
                                            // Erstelle Shared Pref für die anderen Fragmente
                                            SharedPreferences sharedPrefFacultyValidation =
                                                    getApplicationContext().
                                                            getSharedPreferences("validation",0);

                                            SharedPreferences.Editor editorFacultyValidation =
                                                    sharedPrefFacultyValidation.edit();
                                            editorFacultyValidation.putString("returnFaculty", returnFaculty);
                                            editorFacultyValidation.apply();

                                            // füllt die Liste mit Studiengängena
                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    AppDatabase database = AppDatabase.getAppDatabase(getBaseContext());
                                                    List<Courses> courses =
                                                            database.userDao().getCourses(returnFaculty);

                                                    courseChosen.clear();
                                                    courseName.clear();

                                                    for(Courses course: courses) {
                                                        courseName.add(course.getCourseName());
                                                        courseChosen.add(course.getChoosen());
                                                    }

                                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            // schneidet ab dem Leerzeichen ab, da sonst nicht genug platz ist
                                                            if(facultys[which].contains(" ")) {
                                                                buttonSpinner.setText(facultys[which].substring
                                                                        (0, facultys[which].indexOf(' ')));
                                                            } else {
                                                                buttonSpinner.setText(facultys[which]);
                                                            }

                                                            // füge den Adapter der Recyclerview hinzu
                                                            mAdapter = new CheckListAdapter(courseName,
                                                                    courseChosen,
                                                                    getApplicationContext());
                                                            recyclerView.setAdapter(mAdapter);

                                                            TextView chooseCourse = findViewById(R.id.chooseCourseId);

                                                            if(chooseCourse.getVisibility() != View.VISIBLE) {
                                                                chooseCourse.setVisibility(View.VISIBLE);
                                                            }

                                                            if(courseName.size() == 0) {
                                                                if(buttonOk.getVisibility() == View.VISIBLE) {
                                                                    buttonOk.setVisibility(View.INVISIBLE);
                                                                }
                                                                chooseCourse.setText(R.string.no_course);
                                                                chooseCourse.setTextColor(Color.parseColor("#ffa500"));
                                                            } else {
                                                                if(buttonOk.getVisibility() != View.VISIBLE) {
                                                                    buttonOk.setVisibility(View.VISIBLE);
                                                                }
                                                                chooseCourse.setText(R.string.choose_course);
                                                                chooseCourse.setTextColor(Color.parseColor("#eeeeee"));
                                                            } }

                                                    });
                                                }
                                            }).start();
                                        }
                                        catch (Exception e)
                                        {
                                            Log.d("uebergabeAnSpinner",
                                                    "Fehler: Parsen von 'uebergabeAnSpinner'");
                                        }
                                    }//if
                                }//for

                                dialog.dismiss();
                                // Intent hauptfenster = new Intent(getApplicationContext(), Tabelle.class);
                                // startActivity(hauptfenster);
                            }

                        });

                        chooseFaculty.show();
                    }
                });


                buttonOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Erstele einen Dialog um den Hauptstudiengang zu wählen
                        AlertDialog.Builder chooseCourse = new AlertDialog.Builder(MainActivity.this,
                                R.style.customAlertDialog);

                        final boolean[] oneFavorite = {false};

                        AppDatabase database = AppDatabase.getAppDatabase(getBaseContext());

                        List<String> favoriteCourses = new ArrayList<>();

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // prüfe ob mindesten ein Studiengang favorisiert wurde
                                for(Boolean choosen: courseChosen) {
                                    if(choosen) {
                                        oneFavorite[0] = true;
                                        break;
                                    }
                                }
                                if(oneFavorite[0]) {
                                    // aktualisiere die db

                                    for(int i = 0; i < courseChosen.size(); i++) {
                                        database.userDao().updateCourse(courseName.get(i),
                                                courseChosen.get(i));
                                        if(courseChosen.get(i)) {
                                            favoriteCourses.add(courseName.get(i));
                                        }
                                    }
                                }

                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(oneFavorite[0]) {
                                            if(favoriteCourses.size() == 1) {
                                                addMainCourse(favoriteCourses.get(0));
                                            } else {

                                                String[] courses = new String [favoriteCourses.size()];

                                                for(int i = 0; i < favoriteCourses.size(); i++) {
                                                    courses[i] = favoriteCourses.get(i);
                                                }

                                                chooseCourse.setTitle(R.string.choose_main);

                                                chooseCourse.setItems(courses, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        new Thread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                addMainCourse(courses[which]);
                                                            }
                                                        }).start();

                                                    }
                                                });

                                                chooseCourse.show();

                                            }

                                        } else {
                                            Toast.makeText(v.getContext(),v.getContext().getString(R.string.favorite_one_course), Toast.LENGTH_SHORT)
                                                    .show();
                                        }

                                    }
                                });
                            }
                        }).start();
                    }
                });
                // Ende Merlin Gürtler
            }
        });
    }


    //Methode zum Überprüfen der fakultaeten
    //Aufruf in checkVerbindung()
    public boolean pingUrl(final String address) {
        //eigenständiger Thread, weil die Abfrage Asynchron ist
        new Thread(() -> {
            // Die Fakultaeten werden in einer Shared Preferences Variable gespeichert.
            // Creating editor to store Fakultaeten to shared preferences
            SharedPreferences.Editor facultyEditor;
            SharedPreferences sharedPrefsFaculty
                    = getApplicationContext()
                    .getSharedPreferences("faculty", 0);

            //Verbindungsaufbau zum Webserver
            try {
                StringBuilder result = new StringBuilder();
                final URL url = new URL(address);
                final HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                urlConn.setConnectTimeout(1000 * 10); // mTimeout is in seconds
                final long startTime = System.currentTimeMillis();
                urlConn.connect();
                final long endTime = System.currentTimeMillis();
                if (urlConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    // System.out.println("Time (ms) : " + (endTime - startTime));
                    // System.out.println("Ping to " + address + " was success");
                    //uebergabeAnSpinner();
                }

                //Parsen von den  erhaltene Werte
                InputStream in = new BufferedInputStream(urlConn.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                //Erstellen von JSON
                JSONObject jsonObj = null;
                try {
                    jsonObj = XML.toJSONObject(String.valueOf(result));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Iterator x = jsonObj.keys();
                JSONArray jsonArray = new JSONArray();

                while (x.hasNext()){
                    String key = (String) x.next();
                    jsonArray.put(jsonObj.get(key));
                }

                //Werte von JSONARRay in JSONObject konvertieren
                JSONArray receivesFacultys = new JSONArray();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    receivesFacultys.put(object.get("faculty"));
                }

                String convertedToString = receivesFacultys.toString();
                String deletedCling
                        = convertedToString.substring(1,convertedToString.length()-1);
                //konvertieren zu JSONArray
                jsonArrayFacultys = new JSONArray(deletedCling);

                for(int i = 0; i< jsonArrayFacultys.length(); i++) {
                    JSONObject json = jsonArrayFacultys.getJSONObject(i);
                    facultyName.add(json.get("facName").toString());
                }

                // Werte Speichern für die offline Verwendung
                //Log.d("Output fakultaet", jsonArrayFakultaeten.get(0).toString());
                facultyEditor = sharedPrefsFaculty.edit();
                try {
                    facultyEditor.clear();
                    facultyEditor.apply();
                    facultyEditor.putString("faculty", deletedCling);
                    facultyEditor.apply();
                } catch (Exception e) {
                    Log.d("Output checkFakultaet",
                            "Fehler: Parsen von Fakultaet");
                }

                facNamesToSpinner();
                Log.d("Output checkFakultaet","abgeschlossen");

            }
            /*  Wenn keine Verbindung zum Server dann catch Zweig und Daten
                aus den Shared Preferences benutzen
             */
            catch (final Exception e)
            {
                String strFacultys
                        = sharedPrefsFaculty.getString("faculty","0");
                //Log.d("Output 426",strFakultaet);
                if (strFacultys != null) {
                    try{
                        jsonArrayFacultys = new JSONArray(strFacultys);
                        for(int i = 0; i< jsonArrayFacultys.length(); i++) {
                            JSONObject json = jsonArrayFacultys.getJSONObject(i);
                            facultyName.add(json.get("facName").toString());
                            facNamesToSpinner();
                        }

                    }catch (Exception b)
                    {
                        Log.d("uebergabeAnSpinner",
                                "Fehler beim Parsen des Fakultätsnamen.");
                    }
                }
                KeineVerbindung();
            }
        }).start();
        return true;
    }

    //Methode zum Abfragen der Aktuellen Prüfperiode
    public boolean pingExaminePeriod() {

        Thread retrothreadMain = new Thread(new Runnable() {



            public void run() {

            }
        });

        retrothreadMain.start();

        if (checkReturn) {
            return true;
        }else{
            return false;
        }
    }

    Handler handler = new Handler(msg -> {
        if(msg.arg1==1)
        {
            Toast.makeText(getApplicationContext(),
                    getApplicationContext().getString(R.string.noConnection),
                    Toast.LENGTH_SHORT).show();
        }
        return false;
    });

}