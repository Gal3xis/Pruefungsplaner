//////////////////////////////
// RetrofitConnect
//
// autor:
// inhalt: Verbindungsaufbau zum Webserver
// zugriffsdatum: 11.12.19
//
//
//////////////////////////////

package com.Fachhochschulebib.fhb.pruefungsplaner.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.Fachhochschulebib.fhb.pruefungsplaner.CheckGoogleCalendar;
import com.Fachhochschulebib.fhb.pruefungsplaner.Optionen;
import com.Fachhochschulebib.fhb.pruefungsplaner.R;
import com.Fachhochschulebib.fhb.pruefungsplaner.RequestInterface;
import com.Fachhochschulebib.fhb.pruefungsplaner.data.AppDatabase;
import com.Fachhochschulebib.fhb.pruefungsplaner.data.Courses;
import com.Fachhochschulebib.fhb.pruefungsplaner.data.TestPlanEntry;
import com.Fachhochschulebib.fhb.pruefungsplaner.data.Uuid;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitConnect {
    public String termine;
    public static boolean checkTransmission = false;
    private final String relativePPlanUrl;
    // private boolean checkvalidate = false;
    Context ctx2;

    public RetrofitConnect(String relativePPlanUrl) {
        this.relativePPlanUrl = relativePPlanUrl;
    }

    private String getDate(String dateResponse) {
        //Festlegen vom Dateformat
        String dateTimeZone;
        dateTimeZone = dateResponse.replaceFirst("CET", "");
        dateTimeZone = dateTimeZone.replaceFirst("CEST", "");
        String dateLastExamFormatted = null;

        try {
            DateFormat dateFormat = new SimpleDateFormat(
                    "EEE MMM dd HH:mm:ss yyyy", Locale.US);
            Date dateLastExam = dateFormat.parse(dateTimeZone);
            SimpleDateFormat targetFormat
                    = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dateLastExamFormatted = targetFormat.format(dateLastExam);
            Date currentDate = Calendar.getInstance().getTime();
            SimpleDateFormat df
                    = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentDateFormated = df.format(currentDate);


            Log.d("Datum letzte Prüfung", dateLastExamFormatted);
            Log.d("Datum aktuell", currentDateFormated);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return String.valueOf(dateLastExamFormatted);
    }

    private String getIDs(final AppDatabase roomData) {
        List<String> Ids = roomData.userDao().getChoosenCourseId(true);
        JSONArray courseIds = new JSONArray();

        for (String id : Ids) {
            try {
                JSONObject idJson = new JSONObject();
                idJson.put("ID", id);
                courseIds.put(idJson);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return courseIds.toString();
    }

    // Start Merlin Gürtler
    // Refactoring
    private TestPlanEntry createTestplanEntry(JsonResponse entryResponse)
    {
        String dateLastExamFormatted = getDate(entryResponse.getDate());

        TestPlanEntry testPlanEntry = new TestPlanEntry();
        testPlanEntry.setFirstExaminer(entryResponse.getFirstExaminer());
        testPlanEntry.setSecondExaminer(entryResponse.getSecondExaminer());
        testPlanEntry.setDate(dateLastExamFormatted);
        testPlanEntry.setID(entryResponse.getID());
        testPlanEntry.setCourse(entryResponse.getCourseName());
        testPlanEntry.setModule(entryResponse.getModule());
        testPlanEntry.setSemester(entryResponse.getSemester());
        testPlanEntry.setTermin(entryResponse.getTermin());
        testPlanEntry.setRoom(entryResponse.getRoom());
        testPlanEntry.setExamForm(entryResponse.getForm());
        testPlanEntry.setStatus(entryResponse.getStatus());
        testPlanEntry.setHint(entryResponse.getHint());
        testPlanEntry.setColor(entryResponse.getColor());
        return testPlanEntry;
    }

    private TestPlanEntry updateTestPlanEntry(JsonResponse entryResponse, TestPlanEntry existingEntry)
    {
        String dateLastExamFormatted = getDate(entryResponse.getDate());

        existingEntry.setFirstExaminer(entryResponse.getFirstExaminer());
        existingEntry.setSecondExaminer(entryResponse.getSecondExaminer());
        existingEntry.setDate(dateLastExamFormatted);
        existingEntry.setID(entryResponse.getID());
        existingEntry.setCourse(entryResponse.getCourseName());
        existingEntry.setModule(entryResponse.getModule());
        existingEntry.setSemester(entryResponse.getSemester());
        existingEntry.setTermin(entryResponse.getTermin());
        existingEntry.setRoom(entryResponse.getRoom());
        existingEntry.setExamForm(entryResponse.getForm());
        existingEntry.setStatus(entryResponse.getStatus());
        existingEntry.setHint(entryResponse.getHint());
        existingEntry.setColor(entryResponse.getColor());
        return existingEntry;
    }

    private void inserEntryToDatabase(final AppDatabase roomData,
                                      final String year,
                                      final String examinePeriod,
                                      List<JsonResponse> body) {
            // Start Merlin Gürtler
            // Extra Thread da sonst die Db nicht aktualisiert werden kann.
            new Thread((new Runnable() {
                @Override
                public void run() {
                    //Hole alle Einträge aus der lokalen Room-DB
                    List<TestPlanEntry> dataListFromLocalDB = null;
                    try { //DONE (08/2020) LG
                        dataListFromLocalDB = roomData.userDao().getAllEntries();
                        //roomdaten.clearAllTables();
                    } catch (Exception e) {
                        Log.d("Fehler: ", "Kein Zugriff auf die Datenbank!");
                    }

                    // String validation = jahr+studiengang+pruefungsphase;
                    //String checkTermin = "0";

                    //Durchlaufe die Room-DB-Prüfplaneinträge mit dem aktuellen Validationwert
                    /*
                    Merlin Gürtler
                    Es Funktioniert auch ohne checkvalidate
                    if(dataListFromLocalDB!=null) { //DONE (08/2020) LG
                        for (int j = 0; j < dataListFromLocalDB.size(); j++) {
                            if (dataListFromLocalDB.get(j).getValidation().equals(validation)) {
                                //checkTermin = dataListFromLocalDB.get(j).getTermin();
                                checkvalidate = true;
                            }
                        }
                    }//if
                    */

                    //Schleife zum Einfügen jedes erhaltenes Prüfungsobjekt in die lokale Datenbank
                    //DONE (08/2020) LG: Die 0 für i muss doch auch beachtet werden, oder?
                    for (JsonResponse entryResponse : body) {
                        //Pruefplan ist die Modelklasse für die angekommenden Prüfungsobjekte
                        TestPlanEntry testPlanEntry = new TestPlanEntry();

                        /*
                            DS, die bisher noch nicht in der lokalen DB enthalten sind, werden
                            jetzt hinzugefügt.
                         */
                        // if(!checkvalidate){
                        //erhaltene Werte zur Datenbank hinzufügen
                        testPlanEntry = createTestplanEntry(entryResponse);

                        //lokale datenbank initialiseren
                        //DONE (08/2020) LG: Auskommentieren des erneuten Zugriffs
                        //AppDatabase database2 = AppDatabase.getAppDatabase(ctx2);
                        //List<PruefplanEintrag> userdaten2 = database2.userDao().getAllEntries();
                        //Log.d("Test4", String.valueOf(userdaten2.size()));

                        try {
                            for (int b = 0; b < Optionen.idList.size(); b++) {
                                if (testPlanEntry.getID().equals(Optionen.idList.get(b))) {
                                    //Log.d("Test4", String.valueOf(userdaten2.get(b).getID()));
                                    testPlanEntry.setFavorit(true);
                                }
                            }
                        } catch (Exception e) {
                            Log.d("Fehler RetrofitConnect",
                                    "Fehler beim Ermitteln der favorisierten Prüfungen");
                        }

                        //Schlüssel für die Erkennung bzw unterscheidung Festlegen
                        testPlanEntry.setValidation(year + entryResponse.getCourseId() + examinePeriod);

                        addUser(roomData, testPlanEntry);
                   }
                }
            })).start();
            // Ende Merlin Gürtler


        checkTransmission = true;
    };

    //DONE (08/2020 LG) Parameter 7,8 eingefügt --> Adresse an zentraler Stelle verwalten
    public <serverAdress> void RetrofitWebAccess(Context ctx,
                                                 final AppDatabase roomData,
                                                 final String year,
                                                 final String currentPeriod,
                                                 final String termin,
                                                 final String serverAdress) {
        //Serveradresse
        SharedPreferences mSharedPreferencesAdresse
                = ctx.getSharedPreferences("Server_Address", 0);

        ctx2 = ctx;
        termine = termin;
        String urlfhb = mSharedPreferencesAdresse.getString("ServerIPAddress", serverAdress);

        String courseIds = getIDs(roomData);

        //Uebergabe der Parameter an den relativen Server-Pfad
        String relPathWithParameters = relativePPlanUrl
                + "entity.pruefplaneintrag/"
                + currentPeriod + "/"
                + termin + "/"
                + year + "/"
                + courseIds + "/";

        String URL = urlfhb + relPathWithParameters;

        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(URL);
        builder.addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        final RequestInterface request = retrofit.create(RequestInterface.class);
        Call<List<JsonResponse>> call = request.getJSON();
        call.enqueue(new Callback<List<JsonResponse>>() {
            @Override
            public void onResponse(Call<List<JsonResponse>> call, Response<List<JsonResponse>> response) {
                response.body();
                if (response.isSuccessful()) {
                    inserEntryToDatabase(roomData, year, currentPeriod, response.body());

                }//if(response.isSuccessful())
                else {
                    Log.d("RESPONSE", ":::NO RESPONSE:::");
                }
            }

            @Override
            public void onFailure(Call<List<JsonResponse>> call, Throwable t) {
                Log.d("Error", t.getMessage());

            }
        });
    }

    // Start Merlin Gürtler
    public void retroUpdate(Context ctx, final AppDatabase roomData,
                            final String year,
                            final String currentPeriod,
                            final String termin,
                            final String serverAdress) {
        //Serveradresse
        SharedPreferences mSharedPreferencesAdresse = ctx.getSharedPreferences("Server-Adresse", 0);

        String courseIds = getIDs(roomData);

        ctx2 = ctx;
        String urlfhb = mSharedPreferencesAdresse.getString("ServerIPAddress", serverAdress);
        //uebergabe der parameter an die Adresse
        String relPathWithParameters = relativePPlanUrl
                + "entity.pruefplaneintrag/"
                + currentPeriod + "/"
                + termin + "/"
                + year + "/"
                + courseIds + "/";

        String URL = urlfhb + relPathWithParameters;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final RequestInterface request = retrofit.create(RequestInterface.class);
        Call<List<JsonResponse>> call = request.getJSON();

        call.enqueue(new Callback<List<JsonResponse>>() {
            @Override
            public void onResponse(Call<List<JsonResponse>> call, Response<List<JsonResponse>> response) {
                response.body();
                if (response.isSuccessful() && response.body().size() > 0) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            List<TestPlanEntry> dataListFromLocalDB = roomData.userDao().getAllEntries();
                            String responseId;
                            int i;
                            int listSize = dataListFromLocalDB.size();
                            for (JsonResponse response : response.body()) {
                                responseId = response.getID();
                                TestPlanEntry existingEntry = new TestPlanEntry();
                                existingEntry = roomData.userDao().getEntryById(responseId);
                                // prüfe ob der Eintrag schon existiert
                                if(existingEntry != null)
                                {
                                    // entfernt den Eintrag, da die Daten geupdatet wurden
                                    for(i=0 ; i < listSize; i++) {
                                        if(dataListFromLocalDB.get(i).getID().
                                            equals(response.getID())) {
                                            existingEntry = dataListFromLocalDB.get(i);
                                            dataListFromLocalDB.remove(i);
                                            existingEntry = updateTestPlanEntry(response, existingEntry);
                                            break;
                                        }
                                    }
                                    roomData.userDao().updateExam(existingEntry);
                                } else {
                                    TestPlanEntry testPlanEntryResponse = new TestPlanEntry();
                                    testPlanEntryResponse = createTestplanEntry(response);
                                    roomData.userDao().insertAll(testPlanEntryResponse);
                                }

                                //Update den Eintrag aus dem Calendar falls vorhanden
                                CheckGoogleCalendar cal = new CheckGoogleCalendar();
                                cal.setCtx(ctx);
                                if (!cal.checkCal(Integer.parseInt(responseId))) {
                                    cal.updateCalendarEntry(Integer.parseInt(responseId));
                                }
                            }

                            // lösche Einträge die nicht geupdatet wurden
                            roomData.userDao().deleteEntry(dataListFromLocalDB);
                        }
                    }).start();
                } else {
                    Log.d("RESPONSE", ":::NO RESPONSE:::");
                }
            }


            @Override
            public void onFailure(Call<List<JsonResponse>> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });
    }

    public void firstStart(Context ctx, final AppDatabase roomData,
                           final String serverAdress) {
        //Serveradresse
        SharedPreferences mSharedPreferencesAdresse = ctx.getSharedPreferences("Server-Adresse", 0);

        ctx2 = ctx;
        String urlfhb = mSharedPreferencesAdresse.getString("ServerIPAddress", serverAdress);

        // Erhalte die gewählte Fakultät aus den Shared Preferences
        SharedPreferences sahredPreferencesFacultyValidation =
                ctx2.getSharedPreferences("validation", 0);

        String faculty = sahredPreferencesFacultyValidation.getString("returnFaculty", "0");

        //uebergabe der parameter an die Adresse
        String adresse = relativePPlanUrl + "entity.user/firstStart/" +
                faculty + "/";

        String URL = urlfhb + adresse;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final RequestInterface request = retrofit.create(RequestInterface.class);
        Call<JsonUuid> call = request.getJsonUuid();
        call.enqueue(new Callback<JsonUuid>() {
            @Override
            public void onResponse(Call<JsonUuid> call, Response<JsonUuid> response) {
                if (response.isSuccessful()) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            // Speichere die erhaltene Uuid
                            roomData.userDao().insertUuid(response.body().getUuid());

                            // sende die gewählten Kurse
                            setUserCourses(ctx, roomData, serverAdress);
                        }
                    }).start();
                }
            }

            @Override
            public void onFailure(Call<JsonUuid> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });


    }

    public void anotherStart(Context ctx, final AppDatabase roomdaten,
                             final String serverAdress) {
        //Serveradresse
        SharedPreferences mSharedPreferencesAdresse = ctx.getSharedPreferences("Server-Adresse", 0);

        ctx2 = ctx;
        String urlfhb = mSharedPreferencesAdresse.getString("ServerIPAddress", serverAdress);

        Uuid uuid = roomdaten.userDao().getUuid();

        //uebergabe der parameter an die Adresse
        String adress = relativePPlanUrl + "entity.user/anotherStart/" + uuid.getUuid() + "/";

        String URL = urlfhb + adress;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final RequestInterface request = retrofit.create(RequestInterface.class);
        Call<Void> call = request.anotherStart();
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("Success", "Success");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });


    }

    public void sendFeedBack(Context ctx, final AppDatabase roomdaten,
                             final String serverAdress, float usability,
                             float functions, float stability, String text) {
        //Serveradresse
        SharedPreferences mSharedPreferencesAdresse = ctx.getSharedPreferences("Server-Adresse", 0);

        ctx2 = ctx;

        String urlfhb = mSharedPreferencesAdresse.getString("ServerIPAddress", serverAdress);

        Uuid uuid = roomdaten.userDao().getUuid();

        // Falls kein Text eingegeben wurde
        if (text.length() < 1) {
            text = ctx2.getString(R.string.feedbackPlaceholder);
        }

        //uebergabe der parameter an die Adresse
        String adress = relativePPlanUrl + "entity.feedback/sendFeedback/" + uuid.getUuid()
                + "/" + usability + "/" + functions + "/" + stability + "/" + text + "/";

        String URL = urlfhb + adress;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final RequestInterface request = retrofit.create(RequestInterface.class);
        Call<Void> call = request.sendFeedBack();
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("Success", "Success");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });

    }

    public void getCourses(Context ctx, final AppDatabase roomData,
                           final String serverAdress) {
        //Serveradresse
        SharedPreferences mSharedPreferencesAdresse = ctx.getSharedPreferences("Server-Adresse", 0);

        ctx2 = ctx;

        String urlfhb = mSharedPreferencesAdresse.getString("ServerIPAddress", serverAdress);

        //uebergabe der parameter an die Adresse
        String adress = relativePPlanUrl + "entity.studiengang/";

        String URL = urlfhb + adress;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final RequestInterface request = retrofit.create(RequestInterface.class);
        Call<List<JsonCourse>> call = request.getStudiengaenge();
        call.enqueue(new Callback<List<JsonCourse>>() {
            @Override
            public void onResponse(Call<List<JsonCourse>> call, Response<List<JsonCourse>> response) {
                if (response.isSuccessful()) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            // füge die Einträge der db hinzu
                            List<Courses> insertCourses = new ArrayList<>();
                            for (JsonCourse course : response.body()) {
                                Courses courseFromApi = new Courses();
                                courseFromApi.setChoosen(false);
                                courseFromApi.setCourseName(course.getCourseName());
                                courseFromApi.setFacultyId(course.getFKFBID());
                                courseFromApi.setSgid(course.getSGID());
                                insertCourses.add(courseFromApi);
                            }
                            roomData.userDao().insertCourse(insertCourses);
                        }
                    }).start();
                }
            }

            @Override
            public void onFailure(Call<List<JsonCourse>> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });

    }

    public void setUserCourses(Context ctx, final AppDatabase roomData,
                               final String serverAdress) {
        //Serveradresse
        SharedPreferences mSharedPreferencesAdresse = ctx.getSharedPreferences("Server-Adresse", 0);

        ctx2 = ctx;

        // erhalte die gewählten Studiengänge
        String courseIds = getIDs(roomData);

        Uuid uuid = roomData.userDao().getUuid();

        String urlfhb = mSharedPreferencesAdresse.getString("ServerIPAddress", serverAdress);

        //uebergabe der parameter an die Adresse
        String adress = relativePPlanUrl + "entity.usercourses/" +
                courseIds + "/" + uuid.getUuid() + "/";

        String URL = urlfhb + adress;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final RequestInterface request = retrofit.create(RequestInterface.class);
        Call<Void> call = request.sendCourses();
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("Success", "Success");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });


    }

    public <serverAdress> void UpdateUnkownCourses(Context ctx,
                                                   final AppDatabase roomData,
                                                   final String year,
                                                   final String examinPeriod,
                                                   final String termin,
                                                   final String serverAdress,
                                                   final String courses) {
        //Serveradresse
        SharedPreferences mSharedPreferencesAdresse
                = ctx.getSharedPreferences("Server_Address", 0);

        ctx2 = ctx;
        termine = termin;

        String urlfhb = mSharedPreferencesAdresse.getString("ServerIPAddress", serverAdress);

        //Uebergabe der Parameter an den relativen Server-Pfad
        String relPathWithParameters = relativePPlanUrl
                + "entity.pruefplaneintrag/"
                + examinPeriod + "/"
                + termin + "/"
                + year + "/"
                + courses + "/";

        String URL = urlfhb + relPathWithParameters;

        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(URL);
        builder.addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        final RequestInterface request = retrofit.create(RequestInterface.class);
        Call<List<JsonResponse>> call = request.getJSON();
        call.enqueue(new Callback<List<JsonResponse>>() {
            @Override
            public void onResponse(Call<List<JsonResponse>> call, Response<List<JsonResponse>> response) {
                response.body();
                if (response.isSuccessful()) {
                    inserEntryToDatabase(roomData, year, examinPeriod, response.body());
                }//if(response.isSuccessful())
                else {
                    Log.d("RESPONSE", ":::NO RESPONSE:::");
                }
            }

            @Override
            public void onFailure(Call<List<JsonResponse>> call, Throwable t) {
                Log.d("Error", t.getMessage());

            }
        });
    }
    // Ende Merlin Gürtler

    //DONE (08/2020) LG: Rückgabe des PPE wird nicht verwendet, deshalb gelöscht!
    public static void addUser(final AppDatabase db, TestPlanEntry testPlanEntry) {
        TestPlanEntry existingEntry = db.userDao().getEntryById(testPlanEntry.getID());
        // Merlin Gürtler fügt den Eintrag nur hinzu wenn er nicht vorhanden ist
        // dies wird verwendet, da die Favoriten behalten werden sollen
        // und um doppelte Eintrage zu verhindern
        if (existingEntry == null) {
            db.userDao().insertAll(testPlanEntry);
        }
    }
}