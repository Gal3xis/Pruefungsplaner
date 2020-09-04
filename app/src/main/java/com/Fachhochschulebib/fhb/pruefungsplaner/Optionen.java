package com.Fachhochschulebib.fhb.pruefungsplaner;


//////////////////////////////
// Optionen
//
//
//
// autor:
// inhalt:  Abfragen ob prüfungen zum Kalender hinzugefügt werden sollen  und Methoden zum löschen, aktualisieren der Datenbank
// zugriffsdatum: 20.2.20
//
//
//
//
//
//
//////////////////////////////

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

import com.Fachhochschulebib.fhb.pruefungsplaner.data.AppDatabase;
import com.Fachhochschulebib.fhb.pruefungsplaner.data.PruefplanEintrag;
import com.Fachhochschulebib.fhb.pruefungsplaner.model.RetrofitConnect;

import org.json.JSONArray;
import org.json.JSONException;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;


public class Optionen extends Fragment {
    private boolean speicher;
    private SharedPreferences.Editor mEditorGoogleKalender;
    private SharedPreferences.Editor mEditorAdresse;
    private JSONArray response;
    private GregorianCalendar calDate = new GregorianCalendar();
    private String studiengang;
    SharedPreferences mSharedPreferencesPruefTermin;
    private String aktuellerTermin;
    static EditText txtServerAddress;

    //DONE: 08/2020 LG
    SharedPreferences mSharedPreferencesPPServerAddress;
    SharedPreferences mSharedPreferencesValidation;
    String serverAddress, relativePPlanURL, pruefJahr,
            aktuellePruefphase, rueckgabeStudiengang;

    public static List<String> idList = new ArrayList<String>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Start Merlin Gürtler
        // Nun aus Shared Preferences
        mSharedPreferencesValidation
                = container.getContext().getSharedPreferences("validation", 0);

        pruefJahr = mSharedPreferencesValidation.getString("pruefJahr", "0");
        aktuellePruefphase = mSharedPreferencesValidation.getString("aktuellePruefphase", "0");
        rueckgabeStudiengang = mSharedPreferencesValidation.getString("rueckgabeStudiengang", "0");
        // Ende Merlin Gürtler

        final View v = inflater.inflate(R.layout.optionfragment, container, false);

        //Button zum updaten der Prüfungen
        Button btngo2 = (Button) v.findViewById(R.id.btnupdate);
        btngo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String validation = pruefJahr + rueckgabeStudiengang + aktuellePruefphase;
                updatePruefplan(validation);
            }




        });

        //layout Komponenten
        Button btnDb = (Button) v.findViewById(R.id.btnDB);
        Button btnFav = (Button) v.findViewById(R.id.btnFav);
        Button btnGoogleloeschen = (Button) v.findViewById(R.id.btnCalClear);
        Button btnGoogleupdate = (Button) v.findViewById(R.id.btnGoogleUpdate);
        Switch SWgooglecalender = (Switch) v.findViewById(R.id.switch2);
        txtServerAddress = (EditText) v.findViewById(R.id.txtAdresse);
        //holder.zahl1 = position;

        SharedPreferences serverAdresse
                = v.getContext().getSharedPreferences("json8", 0);
        //Creating editor to store uebergebeneModule to shared preferences
        mEditorGoogleKalender = serverAdresse.edit();

        mSharedPreferencesPPServerAddress
                = v.getContext().getSharedPreferences("Server_Address", 0);
        //Creating editor to store uebergebeneModule to shared preferences
        mEditorAdresse = mSharedPreferencesPPServerAddress.edit();

        //------------------------------------------------------------------
        //DONE: 08/2020 LG
        serverAddress
                = mSharedPreferencesPPServerAddress.getString("ServerIPAddress", "0");
        relativePPlanURL
                = mSharedPreferencesPPServerAddress.getString("ServerRelUrlPath", "0");
        //------------------------------------------------------------------

        txtServerAddress.setText(serverAddress);

        //----------------------------------------------------------------------------------------
        mSharedPreferencesPruefTermin
                =  v.getContext()
                    .getSharedPreferences("PruefTermin", 0);

        SharedPreferences.Editor mEditorTermin = mSharedPreferencesPruefTermin.edit();
        aktuellerTermin
                = mSharedPreferencesPruefTermin.getString("aktPruefTermin", "0");
        //----------------------------------------------------------------------------------------

        response = new JSONArray();
        String strServerAddress = serverAdresse.getString("jsondata2", "0");
        //second parameter is necessary ie.,Value to return if this preference does not exist.
        if (strServerAddress != null) {
            try {
                response = new JSONArray(strServerAddress);
            } catch (JSONException e) {
                Log.d("Fehler Optionen", "Server-Adresse Fehler");
            }
        }

        int i;
        speicher = false;
        for (i = 0; i < response.length(); i++) {
            {
                try {
                    if (response.get(i).toString().equals("1")) {
                        SWgooglecalender.setChecked(true);
                        speicher = true;
                    }
                } catch (JSONException e) {
                    Log.d("Fehler Optionen", "Google Kalender aktivierung");

                }
            }
        }

        //Abfrage ob der Google kalender Ein/Ausgeschaltet ist
        SWgooglecalender.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                if (isChecked) {
                    mEditorGoogleKalender.clear();
                    mEditorGoogleKalender.apply();
                    response.put("1");
                    mEditorGoogleKalender.putString("jsondata2", response.toString());
                    mEditorGoogleKalender.apply();

                    com.Fachhochschulebib.fhb.pruefungsplaner.data.AppDatabase datenbank =  com.Fachhochschulebib.fhb.pruefungsplaner.data.AppDatabase.getAppDatabase(getContext());
                    List<com.Fachhochschulebib.fhb.pruefungsplaner.data.PruefplanEintrag> ppeList = datenbank.userDao().getAll2();


                    com.Fachhochschulebib.fhb.pruefungsplaner.CheckGoogleCalendar googlecal = new com.Fachhochschulebib.fhb.pruefungsplaner.CheckGoogleCalendar();
                    googlecal.setCtx(getContext());

                    for(int i = 0; i <ppeList.size(); i++)
                    {
                        //überprüfung von favorisierten Prüfungen
                        if (ppeList.get(i).getFavorit()) {
                            String id = ppeList.get(i).getID();

                            //überprüfung von ein/aus Google Kalender
                            if(googlecal.checkCal(Integer.valueOf(id)))
                            {
                                //ermitteln von benötigten Variablen
                                String[] splitDatumUndUhrzeit
                                        = ppeList.get(i).getDatum().split(" ");
                                System.out.println(splitDatumUndUhrzeit[0]);
                                String[] splitTagMonatJahr
                                        = splitDatumUndUhrzeit[0].split("-");
                                studiengang = ppeList.get(i).getStudiengang();
                                studiengang = studiengang + " " + ppeList.get(i).getModul();
                                int uhrzeitStart
                                        = Integer.valueOf(splitDatumUndUhrzeit[1].substring(0, 2));
                                int uhrzeitEnde
                                        = Integer.valueOf(splitDatumUndUhrzeit[1].substring(4, 5));

                                calDate = new GregorianCalendar(
                                        Integer.valueOf(splitTagMonatJahr[0]),
                                        Integer.valueOf(splitTagMonatJahr[1]) - 1,
                                        Integer.valueOf(splitTagMonatJahr[2]),
                                        uhrzeitStart, uhrzeitEnde);

                                //Methode zum Speichern im Kalender
                                int calendarid = calendarID(studiengang);

                                //Funktion im Google Kalender, um PrüfID und calenderID zu speichern
                                googlecal.insertCal(Integer.valueOf(id), calendarid);

                            }
                        }
                    }

                    Toast.makeText(
                            v.getContext(),
                            "Prüfungen werden jetzt zum Kalender hinzugefügt.",
                            Toast.LENGTH_SHORT).show();
                }

                if (!isChecked) {
                    mEditorGoogleKalender.clear().apply();
                    mEditorGoogleKalender.remove("jsondata2").apply();
                }

            }
        });

        //Change Listener für die Serveradresse
        //speichert den neu eingegebenen Wert
        txtServerAddress.addTextChangedListener(new TextWatcher() {
            boolean validate = false;
            @Override
            public void afterTextChanged(Editable s) {

                    String splitAdresse = txtServerAddress.getText().subSequence(0,7).toString();
                    String splitAdresseEnde
                            = String.valueOf(txtServerAddress
                                                .getText()
                                                .charAt(txtServerAddress.getText().length()-1));

                    //System.out.println(splitAdresseEnde);

                    if(splitAdresse.equals("http://")) {
                        if(splitAdresseEnde.equals("/")) {
                            if(android.util.Patterns.WEB_URL.matcher(
                                    txtServerAddress.getText().toString()).matches()){
                                mEditorAdresse.clear();
                                mEditorAdresse.apply();
                                mEditorAdresse.putString(
                                        "ServerIPAddress", txtServerAddress.getText().toString());
                                mEditorAdresse.apply();
                            }
                    }}
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            }
        });

        //interne DB löschen
        btnDb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                com.Fachhochschulebib.fhb.pruefungsplaner.data.AppDatabase datenbank = com.Fachhochschulebib.fhb.pruefungsplaner.data.AppDatabase.getAppDatabase(v.getContext());
                Log.d("Test", "Lokale DB löschen.");
                datenbank.clearAllTables();

                Toast.makeText(
                        v.getContext(),
                        "Lokale Datenbank gelöscht.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        //Google Kalender einträge löschen
        btnGoogleloeschen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               kalenderLöschen();
                Toast.makeText(
                        v.getContext(),
                        "Google Kalender Einträge gelöscht",
                        Toast.LENGTH_SHORT).show();
            }
        });

        //Google Kalender einträge updaten
        btnGoogleupdate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                kalenderUpdate();
                Toast.makeText(
                        v.getContext(),
                        "Google Kalender aktualisiert",
                        Toast.LENGTH_SHORT).show();
            }
        });

        //Favoriten Löschen
        btnFav.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                com.Fachhochschulebib.fhb.pruefungsplaner.data.AppDatabase datenbank = com.Fachhochschulebib.fhb.pruefungsplaner.data.AppDatabase.getAppDatabase(v.getContext());
                List<com.Fachhochschulebib.fhb.pruefungsplaner.data.PruefplanEintrag> ppeList = datenbank.userDao().getAll2();
                for (int i = 0; i < ppeList.size(); i++) {
                        if (ppeList.get(i).getFavorit()) {

                            Log.d("Test Favoriten löschen.",
                                    String.valueOf(ppeList.get(i).getID()));
                            datenbank.userDao()
                                     .update(false,
                                             Integer.valueOf(ppeList.get(i).getID()));
                            Toast.makeText(
                                    v.getContext(),
                                    "Favorisierte Prüfungen gelöscht.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                // define an adapter
            }
        });

        return v;
    }

    public void updatePruefplan(String validation){

        boolean a = PingUrl(serverAddress);
    }

    //Methode zum Anzeigen das keine Verbindungs zum Server möglich ist
    public void keineVerbindung(){
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(
                        getContext(),
                        "Keine Verbindung zum Server möglich!",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    // Methode zum Aktualiseren der Prüfungen
    // Prüfungen werden gespeichert und dann wird die Datenbank gelöscht.
    // Die Prüfungen werden erneut vom Webserver geladen und mit
    // den gespeicherten IDs favorisiert

    public void pruefplanAktualisieren(){
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(
                        getContext(),
                        "Prüfungen wurden aktualisiert.",
                        Toast.LENGTH_SHORT).show();

                com.Fachhochschulebib.fhb.pruefungsplaner.data.AppDatabase database = com.Fachhochschulebib.fhb.pruefungsplaner.data.AppDatabase.getAppDatabase(getContext());

                List<PruefplanEintrag> ppeList = database.userDao().getAll2();
                List<String> validationList = new ArrayList<String>();

                validationList.add("0");

                //Log.d("Test",String.valueOf(pruefplanDaten.size()));
                if (ppeList.size() > 1) {
                    validationList.add(ppeList.get(0).getValidation().toString());
                    for (int i = 0; i < ppeList.size(); i++) {
                        //Log.d("Test",String.valueOf(pruefplanDaten.get(i).getFavorit()));
                        if (ppeList.get(i).getFavorit()) {
                            idList.add(ppeList.get(i).getID().toString());
                            validationList.add(ppeList.get(i).getValidation().toString());
                            //Log.d("Test2",String.valueOf(pruefplanDaten.get(i).getValidation()));
                        }
                    }// define an adapter
                    database.clearAllTables();

                    //aktuellerTermin, serverAddress, relativePPlanURL aus SharedPreferences

                    //Initialisierung: room database
                    com.Fachhochschulebib.fhb.pruefungsplaner.data.AppDatabase roomdaten = AppDatabase.getAppDatabase(getContext());
                    //retrofit auruf
                    for (int a = 1; a < validationList.size(); a++) {
                        com.Fachhochschulebib.fhb.pruefungsplaner.model.RetrofitConnect retrofit = new RetrofitConnect();
                        retrofit.RetrofitWebAccess(
                                        getContext(),
                                        roomdaten,
                                        pruefJahr,
                                        aktuellePruefphase,
                                        aktuellerTermin,
                                        serverAddress,
                                        relativePPlanURL);

                        // Log.d("Test3",String.valueOf(stringaufteilung[5]));
                    }
                }
            }
        });
    }


    //Verbindungsaufbau zum Webserver
    //Überprüfung ob Webserver erreichbar
    public boolean PingUrl(final String address) {

        new Thread(new Runnable() {
            public void run() {
                try {
                    final URL url = new URL(address);
                    final HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                    urlConn.setConnectTimeout(1000 * 10); // mTimeout is in seconds
                    final long startTime = System.currentTimeMillis();
                    urlConn.connect();
                    final long endTime = System.currentTimeMillis();
                    if (urlConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        System.out.println("Time (ms) : " + (endTime - startTime));
                        System.out.println("Ping to " + address + " successful.");
                        pruefplanAktualisieren();
                    }
                }
                catch (final Exception e)
                {
                    keineVerbindung();
                }

            }
        }).start();

        return true;
    }

    //Google Kalender einträge löschen
    public void kalenderLöschen(){
        com.Fachhochschulebib.fhb.pruefungsplaner.CheckGoogleCalendar cal = new com.Fachhochschulebib.fhb.pruefungsplaner.CheckGoogleCalendar();
        cal.setCtx(getContext());
        cal.clearCal();

    }

    //Google Kalender aktualisieren
    public void kalenderUpdate(){
        com.Fachhochschulebib.fhb.pruefungsplaner.CheckGoogleCalendar cal = new CheckGoogleCalendar();
        cal.setCtx(getContext());
        cal.updateCal();
    }

    public int calendarID(String eventtitle){

        final ContentValues event = new ContentValues();
        event.put(CalendarContract.Events.CALENDAR_ID, 2);
        event.put(CalendarContract.Events.TITLE, studiengang);
        event.put(CalendarContract.Events.DESCRIPTION, "Fachhochschule Bielefeld");
        event.put(CalendarContract.Events.DTSTART, calDate.getTimeInMillis());
        event.put(CalendarContract.Events.DTEND, calDate.getTimeInMillis() + (90 * 60000));
        event.put(CalendarContract.Events.ALL_DAY, 0);   // 0 for false, 1 for true
        event.put(CalendarContract.Events.HAS_ALARM, 0); // 0 for false, 1 for true
        String timeZone = TimeZone.getDefault().getID();
        event.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone);
        Uri baseUri;

        if (Build.VERSION.SDK_INT >= 8) {
            baseUri = Uri.parse("content://com.android.calendar/events");

        } else {
            baseUri = Uri.parse("content://calendar/events");
        }

        getContext().getContentResolver().insert(baseUri, event);


        int result = 0;
        String projection[] = { "_id", "title" };
        Cursor cursor = getContext().getContentResolver()
                                    .query(baseUri, null,
                                           null, null, null);

        if (cursor.moveToFirst()) {

            String calName;
            String calID;

            int nameCol = cursor.getColumnIndex(projection[1]);
            int idCol = cursor.getColumnIndex(projection[0]);
            do {
                calName = cursor.getString(nameCol);
                calID = cursor.getString(idCol);

                if (calName != null && calName.contains(eventtitle)) {
                    result = Integer.parseInt(calID);
                }

            } while (cursor.moveToNext());
            cursor.close();

        }
        return (result);
    }
}