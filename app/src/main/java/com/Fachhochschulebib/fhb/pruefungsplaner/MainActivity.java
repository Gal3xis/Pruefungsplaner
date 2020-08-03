package com.Fachhochschulebib.fhb.pruefungsplaner;
//////////////////////////////
// MainActivity
//
//
//
// autor:
// inhalt:  auswahl des studiengangs mit dazugehörigen pruefJahr und Semester
// zugriffsdatum: 11.12.19
//
//
//
//
//
//
//////////////////////////////

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.Fachhochschulebib.fhb.pruefungsplaner.data.AppDatabase;
import com.Fachhochschulebib.fhb.pruefungsplaner.data.Pruefplan;
import com.Fachhochschulebib.fhb.pruefungsplaner.model.RetrofitConnect;

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
    private Spinner spStudiengangMain;
    List<String> id = new ArrayList<String>();
    private Message msg = new Message();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        final Context context = getBaseContext();

        //aufrufen des startlayouts
        setContentView(R.layout.start);

        pingpruefperiode();


        //Zugriffrechte für den GoogleKalender

        //Id für den Google Kalender
        final int callbackId = 42;


        //Wert1: ID Google Kalender, Wert2: Rechte fürs Lesen, Wert3: Rechte fürs schreiben)
        checkPermission(callbackId, Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR);

        //OK Button, hier wird die neue activity aufgerufen --> aufruf von dem layout "hauptfenster" und der Klasse Tabelle
        Button btngo = (Button) findViewById(R.id.btnGO);
        btngo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinnerArray.size() > 1) {
                    //auslagern von Retrofit in einen Thread
                    retroThread();
                    Intent hauptfenster = new Intent(getApplicationContext(), Tabelle.class);
                    startActivity(hauptfenster);
                }

            }


        });

        //definieren des Arrays jahreszeit
        List<String> jahreszeit = new ArrayList<String>();
        jahreszeit.add("Sommer");
        jahreszeit.add("Winter");


        //spinner füllen mit werten für Sommer/Winter
        final ArrayAdapter<String> studiengang = new ArrayAdapter<String>(
                getBaseContext(), R.layout.simple_spinner_item, jahreszeit);
        studiengang.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);


        //Kalender damit für das aktuelle Jahr
        Calendar calendar = Calendar.getInstance();
        int kalenderMonat = calendar.get(Calendar.MONTH );
        Log.d("Output Monat",String.valueOf(kalenderMonat));


        if (kalenderMonat  <= 4)
        {
            aktuellePruefphase = "W";
            List<String> spinnerArray3 = new ArrayList<String>();
            for (int i = 0; i < 1; i++) {
                int thisYear = calendar.get(Calendar.YEAR);
                pruefJahr = String.valueOf(thisYear);

            }
        }


        if (kalenderMonat  > 4)
        {
            aktuellePruefphase = "S";
            List<String> spinnerArray3 = new ArrayList<String>();
            for (int i = 0; i < 1; i++) {
                int thisYear = calendar.get(Calendar.YEAR);
                pruefJahr = String.valueOf(thisYear);
            }
        }


        if (kalenderMonat >= 9) {

            aktuellePruefphase = "W";
            List<String> spinnerArray3 = new ArrayList<String>();
            for (int i = 0; i < 1; i++) {
                int thisYear = calendar.get(Calendar.YEAR);
                pruefJahr = String.valueOf(thisYear +1);


            }
        }

        //Log.d("Output thisyear",String.valueOf(aktuellePruefphase));

        //anzahl der elemente
        //adapter aufruf
        SharedPreferences pruefperiode = getApplicationContext().getSharedPreferences("pruefperiode", 0);
        String strJson = pruefperiode.getString("pruefperiode", "");
        try {
            Checkverbindung("test");
            //Creating editor to store uebergebeneModule to shared preferencess
            TextView txtpruefperiode = (TextView) findViewById(R.id.txtpruefperiode);
            //second parameter is necessary ie.,Value to return if this preference does not exist.
            if (strJson != null) {
                txtpruefperiode.setText(strJson);
            }

        }

        //Wenn Verbindung zum Server nicht möglich dann Daten aus der Datenbank nehmen
        catch(Exception e) {
            if (strJson != null) {
                try {
                    jsonArrayStudiengaenge = new JSONArray(strJson);
                    for (int i = 0; i < jsonArrayStudiengaenge.length(); i++) {
                        JSONObject json = jsonArrayStudiengaenge.getJSONObject(i);
                        spinnerArray.add(json.get("SGName").toString());
                        uebergabeAnSpinner();
                    }
                } catch (Exception b) {
                    Log.d("Datenbankfehler","Keine Daten aus der Datenbank vorhanden");
                }

            }
            TextView txtpruefperiode = (TextView) findViewById(R.id.txtpruefperiode);
            //second parameter is necessary ie.,Value to return if this preference does not exist.
            if (strJson != null) {
                txtpruefperiode.setText(strJson);
            }

        }




    }

   public void Checkverbindung(String validation){

       SharedPreferences mSharedPreferencesAdresse = getApplicationContext().getSharedPreferences("Server-Adresse", 0);
      //uebergebene Serveradresse
       SharedPreferences.Editor mEditorAdresse = mSharedPreferencesAdresse.edit();
       String serveradresse = mSharedPreferencesAdresse.getString("Server-Adresse2","http://thor.ad.fh-bielefeld.de:8080/");
       boolean aktuelleurl = pingUrl(serveradresse + "PruefplanApplika/webresources/entities.studiengang");
       if (!aktuelleurl) {
           Keineverbindung();
       }
   }

    public void Keineverbindung(){
        //Toast.makeText(getApplicationContext(), "Keine Verbindung zum Server möglich", Toast.LENGTH_SHORT).show();
        new Thread(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(), "Keine Verbindung zum Server möglich", Toast.LENGTH_SHORT).show();
            }
        });
    }


    //darstellen der Studiengänge in der Spinner-Komponente.
    public void uebergabeAnSpinner(){
        MainActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                //Toast.makeText(getBaseContext(), "Prüfungen wurden aktualisiert", Toast.LENGTH_SHORT).show();
                //spinnerarray für die studiengänge
                //adapter aufruf
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        getBaseContext(), R.layout.simple_spinner_item, spinnerArray);
                adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                spStudiengangMain = (Spinner) findViewById(R.id.spStudiengang);
                spStudiengangMain.setAdapter(adapter);
                final List<String> Studiengang = new ArrayList();

                //spinner auswahl für den studiengang
                spStudiengangMain.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        //((TextView) parent.getChildAt(0)).setBackgroundColor(Color.BLUE);
                        ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                        //((TextView) parent.getChildAt(0)).setTextSize(5);
                        Studiengang.add(parent.getItemAtPosition(position).toString()); //this is your selected item
                        for (int i = 0 ; i < spinnerArray.size(); i++)
                        {
                            if (parent.getItemAtPosition(position).toString().equals(spinnerArray.get(i))) {
                                try{
                                    JSONObject object = jsonArrayStudiengaenge.getJSONObject(i);
                                    rueckgabeStudiengang = object.get("sgid").toString();
                                    Log.d("Output studiengang", rueckgabeStudiengang.toString());

                                }
                                catch (Exception e)
                                {
                                    Log.d("uebergabeAnSpinner","Fehler parsen von uebergabeAnSpinner");
                                }
                            }
                        }
                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }
        });
    }


    //Methode zum überprüfen der studiengänge
    public boolean pingUrl(final String address) {
        //eigenständiger Thread, weil die Abfrage Asynchron ist
        new Thread(new Runnable() {
                public void run() {
                    // Die studiengänge werden in einer shared preferences Variable gespeichert
                    //Creating editor to store uebergebeneModule to shared preferences
                    SharedPreferences.Editor studiengangEditor;
                    SharedPreferences studiengaenge = getApplicationContext().getSharedPreferences("studiengaenge", 0);

                    //Verbindungsaufbau zum Webserver
                try {
                    StringBuilder result = new StringBuilder();
                    final URL url = new URL(address);
                    final HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                    //Log.d("Output studiengang","test");
                    urlConn.setConnectTimeout(100 * 10); // mTimeout is in seconds
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
                    String klammernEntfernen = konvertiertZuString.substring(1,konvertiertZuString.length()-1);
                    //konvertieren zu JSONArray
                    jsonArrayStudiengaenge = new JSONArray(klammernEntfernen);

                    for(int i = 0; i< jsonArrayStudiengaenge.length(); i++) {
                        JSONObject json = jsonArrayStudiengaenge.getJSONObject(i);
                        spinnerArray.add(json.get("SGName").toString());
                    }

                    // Werte Speichern für die offline Verwendung
                    //Log.d("Output studiengang", jsonArrayStudiengaenge.get(0).toString());
                    studiengangEditor = studiengaenge.edit();
                        try {
                                studiengangEditor.clear();
                                studiengangEditor.apply();
                                studiengangEditor.putString("studiengaenge", klammernEntfernen);
                                studiengangEditor.apply();
                        } catch (Exception e) {
                            Log.d("Output checkstudiengang","Fehler parsen von Studiengang");
                        }

                    uebergabeAnSpinner();
                    Log.d("Output checkstudiengang","abgeschlossen");


            }
                //Wenn keine Verbindung zum Server dann catch Zweig und Daten aus den Shared Preferences benutzen
                catch (final Exception e)
                {
                    String strStudiengang = studiengaenge.getString("studiengaenge", "0");
                    //Log.d("Output 426",strStudiengang);
                    if (strStudiengang != null) {
                        try{
                            jsonArrayStudiengaenge = new JSONArray(strStudiengang);
                            for(int i = 0; i< jsonArrayStudiengaenge.length(); i++) {
                                JSONObject json = jsonArrayStudiengaenge.getJSONObject(i);
                                spinnerArray.add(json.get("SGName").toString());
                                uebergabeAnSpinner();
                            }
                        }catch (Exception b)
                        {
                            Log.d("uebergabeAnSpinner","Fehler beim parsen des Studiengangsnamen");
                        }
                    }
                    Keineverbindung();
                }
            }
        }).start();
        return true;
    }



    public boolean retroThread() {
        //eigenständiger Thread, weil die Abfrage Asynchron ist
        new Thread(new Runnable() {
            public void run() {

                //initialisierung room database
                AppDatabase datenbank = AppDatabase.getAppDatabase(getBaseContext());

                //retrofit auruf
                RetrofitConnect retrofit = new RetrofitConnect();
                aktuellerTermin = "0";
                retrofit.retro(getApplicationContext(), datenbank, pruefJahr, rueckgabeStudiengang, aktuellePruefphase, aktuellerTermin);

            }
        }).start();
        return true;
    }

    //Methode zum Abfragen der Aktuellen Prüfperiode
    public boolean pingpruefperiode() {

        new Thread(new Runnable() {

            //shared pref. für die prüefperiode
            SharedPreferences.Editor mEditor;
            SharedPreferences mSharedPreferencesperiode = getApplicationContext().getSharedPreferences("pruefperiode", 0);

            public void run() {
                try {
                    StringBuilder result = new StringBuilder();
                    SharedPreferences mSharedPreferencesAdresse = getApplicationContext().getSharedPreferences("Server-Adresse", 0);
                    //Creating editor to store uebergebeneModule to shared preferences

                    //Serveradresse aus den Benutzeroberfläche Optionen
                    SharedPreferences.Editor mEditorAdresse = mSharedPreferencesAdresse.edit();
                    String serveradresse = mSharedPreferencesAdresse.getString("Server-Adresse2","http://thor.ad.fh-bielefeld.de:8080/");
                    String address = serveradresse + "PruefplanApplika/webresources/entities.pruefperioden";
                    final URL url = new URL(address);

                    final HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                    urlConn.setConnectTimeout(1000 * 10); // mTimeout is in seconds
                    long startTime = System.currentTimeMillis();
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
                    String erstesUndletztesZeichenentfernen = arrayZuString.substring(1,arrayZuString.length()-1);
                    JSONArray mainObject2 = new JSONArray(erstesUndletztesZeichenentfernen);
                    JSONObject pruefperiodeTermin = mainObject2.getJSONObject(mainObject2.length()-1);
                    String pruefperiode = pruefperiodeTermin.get("startDatum").toString();
                    String[] arrayPruefperiode=  pruefperiode.split("T");
                    SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                    Date inputDate = fmt.parse(arrayPruefperiode[0]);

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
                    String pruefperiodedatum;
                    pruefperiodedatum = "Aktuelle Prüfungsphase: \n " +String.valueOf(day) +"."+ String.valueOf(month) +"."+ String.valueOf(year) +" bis "+ String.valueOf(day2) +"."+ String.valueOf(month2) +"."+ String.valueOf(year2) ;  // number of days to add;
                    //Log.d("Output pruefperiode",pruefperiodedatum);

                    //Prüfperiode für die offline Verwendung speichern
                    mEditor = mSharedPreferencesperiode.edit();
                    String strJson = mSharedPreferencesperiode.getString("pruefperiode", "");
                    if (strJson != null) {
                        try {
                            if (strJson.equals(pruefperiodedatum))
                            {

                            }
                            else{
                                mEditor.clear();
                                mEditor.apply();
                                mEditor.putString("pruefperiode", pruefperiodedatum);
                                mEditor.apply();
                            }


                        } catch (Exception e) {

                        }
                    }
                    Log.d("Output pruefperiode","abgeschlossen");
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

    Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            if(msg.arg1==1)
            {
                Toast.makeText(getApplicationContext(), "Keine Verbindung zum Server möglich", Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    });
    private void checkPermission(int callbackId, String... permissionsId) {
        boolean permissions = true;
        for (String p : permissionsId) {
            permissions = permissions && ContextCompat.checkSelfPermission(this, p) == PERMISSION_GRANTED;
        }
        if (!permissions)
            ActivityCompat.requestPermissions(this, permissionsId, callbackId);
    }


}

