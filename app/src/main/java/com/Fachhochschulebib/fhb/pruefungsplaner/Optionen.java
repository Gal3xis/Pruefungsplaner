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
import android.os.Handler;
import android.os.Looper;
import android.provider.CalendarContract;
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
import com.Fachhochschulebib.fhb.pruefungsplaner.data.TestPlanEntry;
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
    private boolean save;
    private SharedPreferences.Editor mEditorGoogleKalender;
    private JSONArray response;
    private GregorianCalendar calDate = new GregorianCalendar();
    private String course;
    SharedPreferences mSharedPreferencesCurrentTermin;
    private String currentTermin;

    //DONE: 08/2020 LG
    SharedPreferences mSharedPreferencesPPServerAddress;
    SharedPreferences mSharedPreferencesValidation;
    String serverAddress, relativePPlanURL, examineYear,
            currentExaminePeriod, returnCourse;

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

        examineYear = mSharedPreferencesValidation.getString("examineYear", "0");
        currentExaminePeriod = mSharedPreferencesValidation.getString("currentPeriode", "0");
        returnCourse = mSharedPreferencesValidation.getString("returnCourse", "0");
        // Ende Merlin Gürtler

        final View v = inflater.inflate(R.layout.optionfragment, container, false);

        //Button zum updaten der Prüfungen
        Button btngo2 = (Button) v.findViewById(R.id.btnupdate);
        btngo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String validation = examineYear + returnCourse + currentExaminePeriod;
                updatePlan(validation);
            }


        });

        //layout Komponenten
        Button btnDb = (Button) v.findViewById(R.id.btnDB);
        Button btnFav = (Button) v.findViewById(R.id.btnFav);
        Button btnGoogleloeschen = (Button) v.findViewById(R.id.btnCalClear);
        Button btnGoogleupdate = (Button) v.findViewById(R.id.btnGoogleUpdate);
        Switch SWgooglecalender = (Switch) v.findViewById(R.id.switch2);
        //holder.zahl1 = position;

        SharedPreferences serverAdresse
                = v.getContext().getSharedPreferences("json8", 0);
        //Creating editor to store uebergebeneModule to shared preferences
        mEditorGoogleKalender = serverAdresse.edit();

        mSharedPreferencesPPServerAddress
                = v.getContext().getSharedPreferences("Server_Address", 0);
        //Creating editor to store uebergebeneModule to shared preferences

        //------------------------------------------------------------------
        //DONE: 08/2020 LG
        serverAddress
                = mSharedPreferencesPPServerAddress.getString("ServerIPAddress", "0");
        relativePPlanURL
                = mSharedPreferencesPPServerAddress.getString("ServerRelUrlPath", "0");
        //------------------------------------------------------------------

        //----------------------------------------------------------------------------------------
        mSharedPreferencesCurrentTermin
                = v.getContext()
                .getSharedPreferences("examineTermin", 0);

        currentTermin
                = mSharedPreferencesCurrentTermin.getString("currentTermin", "0");
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
        save = false;
        for (i = 0; i < response.length(); i++) {
            {
                try {
                    if (response.get(i).toString().equals("1")) {
                        SWgooglecalender.setChecked(true);
                        save = true;
                    }
                } catch (JSONException e) {
                    Log.d("Fehler Optionen", "Google Kalender aktivierung");

                }
            }
        }

        //Abfrage ob der Google kalender Ein/Ausgeschaltet ist
        SWgooglecalender.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // do something, the isChecked will be
                        // true if the switch is in the On position
                        if (isChecked) {
                            mEditorGoogleKalender.clear();
                            mEditorGoogleKalender.apply();
                            response.put("1");
                            mEditorGoogleKalender.putString("jsondata2", response.toString());
                            mEditorGoogleKalender.apply();

                            AppDatabase database = AppDatabase.getAppDatabase(getContext());
                            List<TestPlanEntry> ppeList = database.userDao().getFavorites(true);


                            CheckGoogleCalendar googlecal = new CheckGoogleCalendar();
                            googlecal.setCtx(getContext());

                            for (TestPlanEntry entry : ppeList) {
                                String id = entry.getID();

                                //überprüfung von ein/aus Google Kalender
                                if (googlecal.checkCal(Integer.valueOf(id))) {
                                    //ermitteln von benötigten Variablen
                                    String[] splitDateAndTime
                                            = entry.getDate().split(" ");

                                    String[] splitDayMonthYear
                                            = splitDateAndTime[0].split("-");
                                    course = entry.getCourse();
                                    course = course + " " + entry.getModule();
                                    int timeStart
                                            = Integer.valueOf(splitDateAndTime[1].substring(0, 2));
                                    int timeEnd
                                            = Integer.valueOf(splitDateAndTime[1].substring(4, 5));

                                    calDate = new GregorianCalendar(
                                            Integer.valueOf(splitDayMonthYear[0]),
                                            Integer.valueOf(splitDayMonthYear[1]) - 1,
                                            Integer.valueOf(splitDayMonthYear[2]),
                                            timeStart, timeEnd);

                                    //Methode zum Speichern im Kalender
                                    int calendarid = calendarID(course);

                                    //Funktion im Google Kalender, um PrüfID und calenderID zu speichern
                                    googlecal.insertCal(Integer.valueOf(id), calendarid);

                                }
                            }

                            if (!isChecked) {
                                mEditorGoogleKalender.clear().apply();
                                mEditorGoogleKalender.remove("jsondata2").apply();
                            }

                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(
                                            v.getContext(),
                                            v.getContext().getString(R.string.add_calendar),
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).start();
            }
        });

        //Change Listener für die Serveradresse
        //speichert den neu eingegebenen Wert
        /*
        txtServerAddress.addTextChangedListener(new TextWatcher() {
            boolean validate = false;

            @Override
            public void afterTextChanged(Editable s) {

                String splitAdresse = txtServerAddress.getText().subSequence(0, 7).toString();
                String splitAdresseEnde
                        = String.valueOf(txtServerAddress
                        .getText()
                        .charAt(txtServerAddress.getText().length() - 1));

                //System.out.println(splitAdresseEnde);

                if (splitAdresse.equals("http://")) {
                    if (splitAdresseEnde.equals("/")) {
                        if (android.util.Patterns.WEB_URL.matcher(
                                txtServerAddress.getText().toString()).matches()) {
                            mEditorAdresse.clear();
                            mEditorAdresse.apply();
                            mEditorAdresse.putString(
                                    "ServerIPAddress", txtServerAddress.getText().toString());
                            mEditorAdresse.apply();
                        }
                    }
                }
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
         */

        //interne DB löschen
        btnDb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        AppDatabase database = AppDatabase.getAppDatabase(v.getContext());
                        Log.d("Test", "Lokale DB löschen.");

                        database.userDao().deleteTestPlanEntryAll();

                        // Start Merlin Gürtler

                        // Update nachdem löschen
                        RetrofitConnect retrofit = new RetrofitConnect(relativePPlanURL);
                        retrofit.RetrofitWebAccess(
                                getContext(),
                                database,
                                examineYear,
                                currentExaminePeriod,
                                currentTermin,
                                serverAddress);
                        // Ende Merlin Gürtler

                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(
                                        v.getContext(),
                                        v.getContext().getString(R.string.delete_db),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).start();
            }
        });

        //Google Kalender einträge löschen
        btnGoogleloeschen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                deleteCalendar();
                Toast.makeText(
                        v.getContext(),
                        v.getContext().getString(R.string.delete_calendar),
                        Toast.LENGTH_SHORT).show();
            }
        });

        //Google Kalender einträge updaten
        btnGoogleupdate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        updateCalendar();
                    }
                }).start();

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(
                                v.getContext(),
                                v.getContext().getString(R.string.actualisation_calendar),
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        //Favoriten Löschen
        btnFav.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        AppDatabase database = AppDatabase.getAppDatabase(v.getContext());
                        List<TestPlanEntry> ppeList = database.userDao().getFavorites(true);
                        for (TestPlanEntry entry : ppeList) {

                            Log.d("Test Favoriten löschen.",
                                    String.valueOf(entry.getID()));
                            database.userDao()
                                    .update(false,
                                            Integer.valueOf(entry.getID()));
                        }

                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(
                                        v.getContext(),
                                        v.getContext().getString(R.string.delete_favorite),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                        // define an adapter
                    }
                }).start();
            }
        });

        return v;
    }

    public void updatePlan(String validation) {

        boolean a = PingUrl(serverAddress);
    }

    //Methode zum Anzeigen das keine Verbindungs zum Server möglich ist
    public void noConnection() {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(
                        getContext(),
                        getContext().getString(R.string.noConnection),
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    // Methode zum Aktualiseren der Prüfungen
    // die Abfrage Methodes des Webservers
    // gibt Mögliche Änderungen wie den Status zurück,
    // diese werden dann geupdated
    public void updateCheckPlan() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                AppDatabase database = AppDatabase.getAppDatabase(getContext());


                //Log.d("Test",String.valueOf(pruefplanDaten.size()));
                //aktuellerTermin, serverAddress, relativePPlanURL aus SharedPreferences

                //retrofit auruf
                RetrofitConnect retrofit = new RetrofitConnect(relativePPlanURL);
                retrofit.retroUpdate(getContext(),
                        database,
                        examineYear,
                        currentExaminePeriod,
                        currentTermin,
                        serverAddress);

                // Log.d("Test3",String.valueOf(stringaufteilung[5]));
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(
                                getContext(),
                                getContext().getString(R.string.add_favorite),
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
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
                        // System.out.println("Time (ms) : " + (endTime - startTime));
                        // System.out.println("Ping to " + address + " successful.");
                        updateCheckPlan();
                    }
                } catch (final Exception e) {
                    noConnection();
                }

            }
        }).start();

        return true;
    }

    //Google Kalender einträge löschen
    public void deleteCalendar() {
        CheckGoogleCalendar cal = new CheckGoogleCalendar();
        cal.setCtx(getContext());
        cal.clearCal();

    }

    //Google Kalender aktualisieren
    public void updateCalendar() {
        CheckGoogleCalendar cal = new CheckGoogleCalendar();
        cal.setCtx(getContext());
        cal.updateCal();
    }

    public int calendarID(String eventtitle) {

        final ContentValues event = new ContentValues();
        event.put(CalendarContract.Events.CALENDAR_ID, 2);
        event.put(CalendarContract.Events.TITLE, course);
        event.put(CalendarContract.Events.DESCRIPTION, getContext().getString(R.string.fh_name));
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
        String projection[] = {"_id", "title"};
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