package com.Fachhochschulebib.fhb.pruefungsplaner;

//////////////////////////////
// MainActivity
//
// autor:
// inhalt:  Auswahl des Studiengangs mit dazugehörigem PruefJahr und Semester
// zugriffsdatum: 11.12.19, 08/2020 (LG)
//
//////////////////////////////

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import com.Fachhochschulebib.fhb.pruefungsplaner.data.AppDatabase;
import com.Fachhochschulebib.fhb.pruefungsplaner.model.RetrofitConnect;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;


public class MainActivity extends AppCompatActivity {
    static public RecyclerView.Adapter mAdapter;

    public static String pruefJahr = null;
    public static String aktuellePruefphase = null;
    public static String rueckgabeStudiengang = null;
    private Boolean checkReturn = true;
    public static String aktuellerTermin;
    //KlassenVariablen
    private JSONArray jsonArrayStudiengaenge;
    final List<String> spinnerArray = new ArrayList<String>();
    // private Spinner spStudiengangMain;
    //List<String> idList = new ArrayList<String>();
    private Message msg = new Message();

    SharedPreferences mSharedPreferencesPPServerAdress;
    SharedPreferences mSharedPreferencesPruefPhase;
    String serverAddress, relativePPlanURL;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        final Context context = getBaseContext();

        //aufrufen des startlayouts
        setContentView(R.layout.start);

        mSharedPreferencesPPServerAdress
                = getApplicationContext().getSharedPreferences("Server_Address", MODE_PRIVATE);
        SharedPreferences.Editor mEditorPPServerAdress = mSharedPreferencesPPServerAdress.edit();
        mEditorPPServerAdress.putString("ServerIPAddress", "http://192.168.2.104:8080/");
        //mEditorPPServerAdress.putString("ServerIPAddress", "http://192.168.178.39:8080/");
        //alternativ in FH: "http://thor.ad.fh-bielefeld.de:8080/"
        mEditorPPServerAdress.apply(); //Schreiben der Präferenz!
        //Auslesen zur allgemeinen Verwendung in der aktuellen Activity
        serverAddress
                = mSharedPreferencesPPServerAdress.getString("ServerIPAddress", "0");

        SharedPreferences.Editor mEditorRelUrlPath = mSharedPreferencesPPServerAdress.edit();
        mEditorRelUrlPath.putString("ServerRelUrlPath", "PruefplanverwaltungRestIF/webresources/");
        mEditorRelUrlPath.apply(); //Schreiben der Präferenz!
        //Auslesen zur allgemeinen Verwendung in der aktuellen Activity
        relativePPlanURL
                = mSharedPreferencesPPServerAdress.getString("ServerRelUrlPath", "0");

        pingPruefPeriode();

        //Zugriffrechte für den GoogleKalender
        //Id für den Google Kalender
        final int callbackId = 42;

        //Wert1: ID Google Kalender, Wert2: Rechte fürs Lesen, Wert3: Rechte fürs schreiben)
        checkPermission(callbackId,
                Manifest.permission.READ_CALENDAR,
                Manifest.permission.WRITE_CALENDAR);

        /* OK Button, hier wird die neue activity aufgerufen -->
            aufruf von dem layout "hauptfenster" und der Klasse Tabelle
         */
        /*
        Button btngo = (Button) findViewById(R.id.btnGO);
        btngo.setOnClickListener(v -> {
            // Start Merlin Gürtler
            AppDatabase datenbank =  AppDatabase.getAppDatabase(v.getContext());
            // Check if the Database is not empty
            if(datenbank.userDao().getAll2().size() > 0) {
                retroThread(true);
                // Ende Merlin Gürtler
            } else {
                if (spinnerArray.size() > 1) {
                    //auslagern von Retrofit in einen Thread
                    retroThread(false);
                }
            }
            Intent hauptfenster = new Intent(getApplicationContext(), Tabelle.class);
            startActivity(hauptfenster);
        });
        */

        //Defininition des Arrays jahreszeit
        List<String> jahresZeit = new ArrayList<String>();
        jahresZeit.add("Sommer");
        jahresZeit.add("Winter");

        //Kalender:: aktuelles Jahr --> Bestimmung der Prüfphase (WiSe, SoSe)
        Calendar calendar = Calendar.getInstance();
        int kalenderMonat = calendar.get(Calendar.MONTH );
        Log.d("Output Monat",String.valueOf(kalenderMonat));

        if (kalenderMonat  <= 4)
        {
            aktuellePruefphase = "WiSe";
            for (int i = 0; i < 1; i++) {
                int thisYear = calendar.get(Calendar.YEAR);
                pruefJahr = String.valueOf(thisYear);
            }
        }

        if (kalenderMonat  > 4)
        {
            aktuellePruefphase = "SoSe";
            for (int i = 0; i < 1; i++) {
                int thisYear = calendar.get(Calendar.YEAR);
                pruefJahr = String.valueOf(thisYear);
            }
        }

        if (kalenderMonat >= 9) {

            aktuellePruefphase = "WiSe";
            for (int i = 0; i < 1; i++) {
                int thisYear = calendar.get(Calendar.YEAR);
                pruefJahr = String.valueOf(thisYear +1);
            }
        }

        // Start Merlin Gürtler
        // Schreiben der Pruefphase in eine shared preference

        mSharedPreferencesPruefPhase
                = getApplicationContext().getSharedPreferences("validation", MODE_PRIVATE);
        SharedPreferences.Editor mEditorPruefPhaseUndJahr = mSharedPreferencesPruefPhase.edit();
        mEditorPruefPhaseUndJahr.putString("aktuellePruefphase", aktuellePruefphase);
        mEditorPruefPhaseUndJahr.putString("pruefJahr", pruefJahr);
        mEditorPruefPhaseUndJahr.apply();

        // Ende Merlin Gürtler

        //Anzahl der Elemente
        //Adapter-Aufruf
        SharedPreferences sharedPrefPruefPeriode
                = getApplicationContext().getSharedPreferences("PruefPeriode", MODE_PRIVATE);
        String strJson = sharedPrefPruefPeriode.getString("aktPruefPeriode", "0");
        try {
            Checkverbindung();
            //Creating editor to store uebergebeneModule to shared preferencess
            TextView txtpruefperiode = (TextView) findViewById(R.id.txtpruefperiode);
            //second parameter is necessary ie.,Value to return if this preference does not exist.
            if (strJson != null) {
                txtpruefperiode.setText(strJson);
            }
        }
        //Wenn Verbindung zum Server nicht möglich, dann Daten aus der Datenbank nehmen
        catch(Exception e) {
            if (strJson != null) {
                try {
                    jsonArrayStudiengaenge = new JSONArray(strJson);

                    for (int i = 0; i < jsonArrayStudiengaenge.length(); i++) {
                        JSONObject json = jsonArrayStudiengaenge.getJSONObject(i);
                        spinnerArray.add(json.get("SGName").toString());
                        SgNamesToSpinner();
                    }
                } catch (Exception b) {
                    Log.d("Datenbankfehler","Keine Daten in der Datenbank vorhanden!");
                }
            }
            TextView txtpruefperiode = (TextView) findViewById(R.id.txtpruefperiode);
            //second parameter is necessary ie.,Value to return if this preference does not exist.
            if (strJson != null) {
                txtpruefperiode.setText(strJson);
            }
        }

        // Start Merlin Gürtler
        AppDatabase datenbank =  AppDatabase.getAppDatabase(getBaseContext());
        // Check if the Database is not empty
        if(datenbank.userDao().getAll2().size() > 0) {
            retroThread(true);
        } else {
            if (spinnerArray.size() > 1) {
                //auslagern von Retrofit in einen Thread
                retroThread(false);
            }
        }
        // Ende Merlin Gürtler
    }

    //Aufruf in onCreate()
    public void Checkverbindung(){
        boolean aktuelleurl
                = PingUrl(serverAddress + relativePPlanURL + "entity.studiengang");

        if (!aktuelleurl) {
            KeineVerbindung();
        }

    }

    public void KeineVerbindung(){
        new Thread(() -> Toast.makeText(getApplicationContext(),
                "Keine Verbindung zum Server möglich",
                Toast.LENGTH_SHORT).show());
    }


    //Übernahme der Studiengangsnamen in die Spinner-Komponente.
    public void SgNamesToSpinner(){
        com.Fachhochschulebib.fhb.pruefungsplaner.MainActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                /* Toast.makeText(getBaseContext(), "Prüfungen wurden aktualisiert",
                                    Toast.LENGTH_SHORT).show();
                 */
                //spinnerarray für die studiengänge
                //adapter aufruf
                final List<String> studiengangsList = new ArrayList();

                // Start Merlin Gürtler
                findViewById(R.id.buttonForSpinner).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Erstelle das Dialog Feld für die Auswahl
                        AlertDialog.Builder chooseCourse = new AlertDialog.Builder(MainActivity.this,
                                R.style.customAlertDialog);
                        String[] courses = new String [spinnerArray.size()];

                        for(int i = 0; i < spinnerArray.size(); i++) {
                            courses[i] = spinnerArray.get(i);
                        }
                        // Der Listener für die Item Auswahl
                        chooseCourse.setItems(courses, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                studiengangsList.add(courses[which]); // das ausgewählte Item
                                for (int i = 0 ; i < spinnerArray.size(); i++)
                                {
                                    if (courses[which]
                                            .equals(spinnerArray.get(i))) {
                                        try{
                                            JSONObject object = jsonArrayStudiengaenge.getJSONObject(i);
                                            rueckgabeStudiengang = object.get("sgid").toString();
                                            Log.d("Output Studiengang",
                                                    rueckgabeStudiengang.toString());
                                            // Erstelle Shared Pref für die anderen Fragmente
                                            SharedPreferences sharedPrefStudiengangValidation =
                                                    getApplicationContext().
                                                            getSharedPreferences("validation",0);

                                            SharedPreferences.Editor editorStudiengangValidation =
                                                    sharedPrefStudiengangValidation.edit();

                                            editorStudiengangValidation.putString("selectedStudiengang", courses[which]);
                                            editorStudiengangValidation.putString("rueckgabeStudiengang", rueckgabeStudiengang);
                                            editorStudiengangValidation.apply();
                                        }
                                        catch (Exception e)
                                        {
                                            Log.d("uebergabeAnSpinner",
                                                    "Fehler: Parsen von 'uebergabeAnSpinner'");
                                        }
                                    }//if
                                }//for

                                dialog.dismiss();
                                Intent hauptfenster = new Intent(getApplicationContext(), Tabelle.class);
                                startActivity(hauptfenster);
                            }

                        });

                        chooseCourse.show();
                    }
                });
                // Ende Merlin Gürtler
            }
        });
    }


    //Methode zum Überprüfen der studiengänge
    //Aufruf in checkVerbindung()
    public boolean PingUrl(final String address) {
        //eigenständiger Thread, weil die Abfrage Asynchron ist
        new Thread(() -> {
            // Die Studiengänge werden in einer Shared Preferences Variable gespeichert.
            // Creating editor to store Studiengänge to shared preferences
            SharedPreferences.Editor studiengangEditor;
            SharedPreferences sharedPrefsStudiengaenge
                    = getApplicationContext()
                    .getSharedPreferences("Studiengaenge", 0);

            //Verbindungsaufbau zum Webserver
            try {
                StringBuilder result = new StringBuilder();
                final URL url = new URL(address);
                final HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                //Log.d("Output studiengang","test");
                urlConn.setConnectTimeout(1000 * 10); // mTimeout is in seconds
                final long startTime = System.currentTimeMillis();
                //Log.d("Output studiengang","test");
                urlConn.connect();
                final long endTime = System.currentTimeMillis();
                if (urlConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    System.out.println("Time (ms) : " + (endTime - startTime));
                    System.out.println("Ping to " + address + " was success");
                    //uebergabeAnSpinner();
                }

                //Parsen von den  erhaltene Werte
                //Log.d("Output studiengang","test2");
                InputStream in = new BufferedInputStream(urlConn.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                //Erstellen von JSON
                //Log.d("Output studiengang","test3");
                //Log.d("Output studiengang",String.valueOf(result.toString()));
                JSONObject jsonObj = null;
                try {
                    jsonObj = XML.toJSONObject(String.valueOf(result));
                    //Log.d("Output studiengang",jsonObj.toString());

                } catch (JSONException e) {
                    // Log.e("JSON exception", e.getMessage());
                    e.printStackTrace();
                }

                Iterator x = jsonObj.keys();
                JSONArray jsonArray = new JSONArray();
                //Log.d("Output studiengang","test5");
                while (x.hasNext()){
                    String key = (String) x.next();
                    jsonArray.put(jsonObj.get(key));
                }

                //Log.d("Output studiengang","test 6");

                //Werte von JSONARRay in JSONObject konvertieren
                JSONArray erhalteneStudiengaenge = new JSONArray();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    //  Log.d("Output studiengang",object.get("studiengang").toString());
                    erhalteneStudiengaenge.put(object.get("studiengang"));
                    // Log.d("Output studiengang",String.valueOf(object2.length()));
                }

                String konvertiertZuString = erhalteneStudiengaenge.toString();
                String klammernEntfernen
                        = konvertiertZuString.substring(1,konvertiertZuString.length()-1);
                //konvertieren zu JSONArray
                jsonArrayStudiengaenge = new JSONArray(klammernEntfernen);

                for(int i = 0; i< jsonArrayStudiengaenge.length(); i++) {
                    JSONObject json = jsonArrayStudiengaenge.getJSONObject(i);
                    spinnerArray.add(json.get("SGName").toString());
                }

                // Werte Speichern für die offline Verwendung
                //Log.d("Output studiengang", jsonArrayStudiengaenge.get(0).toString());
                studiengangEditor = sharedPrefsStudiengaenge.edit();
                try {
                    studiengangEditor.clear();
                    studiengangEditor.apply();
                    studiengangEditor.putString("studiengaenge", klammernEntfernen);
                    studiengangEditor.apply();
                } catch (Exception e) {
                    Log.d("Output checkStudiengang",
                            "Fehler: Parsen von Studiengang");
                }

                SgNamesToSpinner();
                Log.d("Output checkStudiengang","abgeschlossen");

            }
            /*  Wenn keine Verbindung zum Server dann catch Zweig und Daten
                aus den Shared Preferences benutzen
             */
            catch (final Exception e)
            {
                String strStudiengang
                        = sharedPrefsStudiengaenge.getString("studiengaenge","0");
                //Log.d("Output 426",strStudiengang);
                if (strStudiengang != null) {
                    try{
                        jsonArrayStudiengaenge = new JSONArray(strStudiengang);
                        for(int i = 0; i< jsonArrayStudiengaenge.length(); i++) {
                            JSONObject json = jsonArrayStudiengaenge.getJSONObject(i);
                            spinnerArray.add(json.get("SGName").toString());
                            SgNamesToSpinner();
                        }
                    }catch (Exception b)
                    {
                        Log.d("uebergabeAnSpinner",
                                "Fehler beim Parsen des Studiengangsnamen.");
                    }
                }
                KeineVerbindung();
            }
        }).start();
        return true;
    }


    public boolean retroThread(boolean update) {
        //eigenständiger Thread, weil die Abfrage asynchron ist

        // Start Merlin Gürtker
        final ProgressDialog progressBar;
        progressBar = new ProgressDialog(MainActivity.this,
                R.style.ProgressStyle);

        // Erstelle den Fortschrittsbalken
        progressBar.setMessage("Prüfungsdaten werden geladen");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setCancelable(false);
        // Zeige den Fortschrittsbalken
        progressBar.show();
        // Ende Merlin Gürtler
        new Thread(new Runnable() {
            public void run() {

                //initialisierung room database
                AppDatabase datenbank = AppDatabase.getAppDatabase(getBaseContext());

                //retrofit auruf
                RetrofitConnect retrofit = new RetrofitConnect(relativePPlanURL);

                //TODO: Aktuellen Termin aus Prüfperiode (ppNum) abfragen!!!
                //DONE (8/2020 LG): s. Prüfperiodenabfrage etwa Zeile: 529
                //aktuellerTermin = "2";
                pingPruefPeriode();

                // Datei: RetrofitConnect.java
                // DONE (08/2020) Parameter 7,8 eingefügt --> Adresse an zentraler Stelle verwalten
                // Änderung Merlin Gürtler
                if(update) {
                    retrofit.retroUpdate(getApplicationContext(), datenbank,
                            serverAddress);
                } else {
                    retrofit.RetrofitWebAccess(
                        getApplicationContext(),
                        datenbank,
                        pruefJahr,
                        aktuellePruefphase,
                        aktuellerTermin,
                        serverAddress);
                }
                progressBar.dismiss();
            }
        }).start();

        return true;
    }

    //Methode zum Abfragen der Aktuellen Prüfperiode
    public boolean pingPruefPeriode() {

        new Thread(new Runnable() {

            //Shared Pref. für die Pruefperiode
            SharedPreferences.Editor mEditor;
            SharedPreferences mSharedPreferencesPPeriode
                    = getApplicationContext().getSharedPreferences("PruefPeriode", MODE_PRIVATE);

            public void run() {
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
                    //long startTime = System.currentTimeMillis();
                    //Log.d("Output studiengang","test");

                    try {
                        urlConn.connect();
                    }catch (Exception e)
                    {
                        Log.d("Output exception",e.toString());
                        msg.arg1=1;
                        handler.sendMessage(msg);
                    }

                    // Log.d("Output studiengang","test2");
                    //Variablen zum lesen der erhaltenen werte
                    InputStream in = new BufferedInputStream(urlConn.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Log.d("Output pruefperiode","test3");
                    //Log.d("Output pruefperiode",String.valueOf(result.toString()));

                    JSONObject jsonObj = null;
                    try {
                        jsonObj = XML.toJSONObject(String.valueOf(result));
                        //Log.d("Output pruefperiode",jsonObj.toString());
                    } catch (JSONException e) {
                        //Log.e("JSON exception", e.getMessage());
                        e.printStackTrace();
                    }

                    //hinzufügen der erhaltenen werte JSONObject werte zum JSONArray
                    Iterator x = jsonObj.keys();
                    JSONArray jsonArray = new JSONArray();
                    // Log.d("Output pruefperiode","test5");
                    while (x.hasNext()){
                        String key = (String) x.next();
                        jsonArray.put(jsonObj.get(key));
                    }

                    //Log.d("Output pruefperiode","test 6");
                    JSONArray pruefperiodeArray = new JSONArray();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        // Log.d("Output pruefperiode",object.get("pruefperioden").toString());
                        pruefperiodeArray.put(object.get("pruefperioden"));
                        //Log.d("Output pruefperiode",String.valueOf(object2.length()));
                    }
                    String arrayZuString = pruefperiodeArray.toString();
                    String erstesUndletztesZeichenentfernen
                            = arrayZuString.substring(1,arrayZuString.length()-1);
                    JSONArray mainObject2 = new JSONArray(erstesUndletztesZeichenentfernen);
                    JSONObject pruefperiodeTermin
                            = mainObject2.getJSONObject(mainObject2.length()-1);

                    //1 --> 1. Termin; 2 --> 2. Termin des jeweiligen Semesters
                    aktuellerTermin =  pruefperiodeTermin.get("PPNum").toString();
                    //-------------------------------------------------------------------
                    //DONE (08/2020) Termin 1 bzw. 2 in den Präferenzen speichern
                    SharedPreferences mSharedPreferencesPruefTermin
                            = getApplicationContext()
                            .getSharedPreferences("PruefTermin", MODE_PRIVATE);

                    SharedPreferences.Editor mEditorTermin = mSharedPreferencesPruefTermin.edit();
                    mEditorTermin.putString("aktPruefTermin", aktuellerTermin);

                    mEditorTermin.apply();  //Ausführen der Schreiboperation!
                    //-------------------------------------------------------------------

                    String pruefPeriode = pruefperiodeTermin.get("startDatum").toString();
                    String[] arrayPruefPeriode=  pruefPeriode.split("T");
                    SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                    Date inputDate = fmt.parse(arrayPruefPeriode[0]);

                    //erhaltenes Datum Parsen als Datum
                    Calendar calendar = new GregorianCalendar();
                    calendar.setTime(inputDate);
                    int year = calendar.get(Calendar.YEAR);
                    //Add one to month {0 - 11}
                    int month = calendar.get(Calendar.MONTH) + 1;
                    int day = calendar.get(Calendar.DAY_OF_MONTH);
                    calendar.add(Calendar.DATE, 14);
                    int year2 = calendar.get(Calendar.YEAR);
                    //Add one to month {0 - 11}
                    int month2 = calendar.get(Calendar.MONTH) + 1;
                    int day2 = calendar.get(Calendar.DAY_OF_MONTH);
                    //String Prüfperiode zum Anzeigen
                    String pruefPeriodeDatum = "Aktuelle Prüfungsphase: \n "
                            +String.valueOf(day)
                            +"."+ String.valueOf(month)
                            +"."+ String.valueOf(year) +" bis "
                            + String.valueOf(day2)
                            +"."+ String.valueOf(month2)
                            +"."+ String.valueOf(year2) ;  // number of days to add;
                    //Log.d("Output pruefperiode",pruefPeriodeDatum);

                    //Prüfperiode für die Offline-Verwendung speichern
                    mEditor = mSharedPreferencesPPeriode.edit();
                    String strJson
                            = mSharedPreferencesPPeriode.getString("aktPruefPeriode", "0");
                    if (strJson != null) {
                        if (strJson.equals(pruefPeriodeDatum))
                        {

                        }
                        else{
                            mEditor.clear();
                            mEditor.apply();
                            mEditor.putString("aktPruefPeriode", pruefPeriodeDatum);
                            mEditor.apply();
                            // Start Merlin Gürtler
                            // Dies ist nötig, da die Serverantwort dauert,
                            // somit wird vorher der Standardwert 0 geladen
                            TextView txtpruefperiode = (TextView) findViewById(R.id.txtpruefperiode);
                            txtpruefperiode.setText(pruefPeriodeDatum);
                            // Ende Merlin Gürtler
                        }
                    }
                    Log.d("Output PruefPeriode","abgeschlossen");
                }
                catch (final Exception e)
                {
                    checkReturn = false;
                    //Keineverbindung();
                }
            }
        }).start();

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
                    "Keine Verbindung zum Server möglich",
                    Toast.LENGTH_SHORT).show();
        }
        return false;
    });

    private void checkPermission(int callbackId, String... permissionsId) {
        boolean permissions = true;
        for (String p : permissionsId) {
            permissions = permissions
                    && ContextCompat
                    .checkSelfPermission(this, p) == PERMISSION_GRANTED;
        }
        if (!permissions)
            ActivityCompat.requestPermissions(this, permissionsId, callbackId);
    }
}