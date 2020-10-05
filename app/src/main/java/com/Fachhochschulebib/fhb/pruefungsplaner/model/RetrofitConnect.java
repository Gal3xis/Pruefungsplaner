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
import com.Fachhochschulebib.fhb.pruefungsplaner.RequestInterface;
import com.Fachhochschulebib.fhb.pruefungsplaner.data.AppDatabase;
import com.Fachhochschulebib.fhb.pruefungsplaner.data.PruefplanEintrag;
import com.Fachhochschulebib.fhb.pruefungsplaner.data.Uuid;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    public static boolean checkuebertragung = false;
    private String relativePPlanUrl;
    // private boolean checkvalidate = false;
    Context ctx2;

    public RetrofitConnect(String relativePPlanUrl) {
        this.relativePPlanUrl = relativePPlanUrl;
    }


    //DONE (08/2020 LG) Parameter 7,8 eingefügt --> Adresse an zentraler Stelle verwalten
    public <serverAdress> void RetrofitWebAccess(Context ctx,
                                                 final AppDatabase roomdaten,
                                                 final String jahr,
                                                 final String pruefungsphase,
                                                 final String termin,
                                                 final String serverAdress){
        //Serveradresse
        SharedPreferences mSharedPreferencesAdresse
                = ctx.getSharedPreferences("Server_Address", 0);

        ctx2 = ctx;
        termine = termin;
        //Creating editor to store uebergebeneModule to shared preferences
        String urlfhb = mSharedPreferencesAdresse.getString("ServerIPAddress", serverAdress);

        List <String> Ids = roomdaten.userDao().getChoosenStudiengangId(true);
        JSONArray studiengangIds = new JSONArray();

        for(String id: Ids) {
            try {
                JSONObject idJson = new JSONObject();
                idJson.put("ID", id);
                studiengangIds.put(idJson);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //Uebergabe der Parameter an den relativen Server-Pfad
        String relPathWithParameters = relativePPlanUrl
                            + "entity.pruefplaneintrag/"
                            + pruefungsphase +"/"
                            + termin +"/"
                            + jahr +"/"
                            + studiengangIds.toString() + "/";

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

                    //Hole alle Einträge aus der lokalen Room-DB
                    List<PruefplanEintrag> dataListFromLocalDB = null;
                    try { //DONE (08/2020) LG
                        dataListFromLocalDB = roomdaten.userDao().getAll2();
                        //roomdaten.clearAllTables();
                    } catch (Exception e) {
                        Log.d("Fehler: ","Kein Zugriff auf die Datenbank!");
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
                    for (JsonResponse eintragDB: response.body()) {

                        //Pruefplan ist die Modelklasse für die angekommenden Prüfungsobjekte
                        PruefplanEintrag pruefplanEintrag = new PruefplanEintrag();

                        //Festlegen vom Dateformat
                        String datumZeitzone;
                        String datumDerPruefung = eintragDB.getDatum();
                        datumZeitzone = datumDerPruefung.replaceFirst("CET", "");
                        datumZeitzone = datumZeitzone.replaceFirst("CEST","");
                        String datumLetzePruefungFormatiert = null;

                        try {
                            DateFormat dateFormat = new SimpleDateFormat(
                                    "EEE MMM dd HH:mm:ss yyyy", Locale.US);
                            Date datumLetztePruefung = dateFormat.parse(datumZeitzone);
                            SimpleDateFormat targetFormat
                                    = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            datumLetzePruefungFormatiert = targetFormat.format(datumLetztePruefung);
                            Date datumAktuell = Calendar.getInstance().getTime();
                            SimpleDateFormat df
                                    = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String datumAktuellFormatiert = df.format(datumAktuell);


                            Log.d("Datum letzte Prüfung",datumLetzePruefungFormatiert);
                            Log.d("Datum aktuell",datumAktuellFormatiert);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        /*
                            DS, die bisher noch nicht in der lokalen DB enthalten sind, werden
                            jetzt hinzugefügt.
                         */
                         // if(!checkvalidate){
                             //erhaltene Werte zur Datenbank hinzufügen
                        pruefplanEintrag.setErstpruefer(eintragDB.getErstpruefer());
                        pruefplanEintrag.setZweitpruefer(eintragDB.getZweitpruefer());
                        pruefplanEintrag.setDatum(String.valueOf(datumLetzePruefungFormatiert));
                        pruefplanEintrag.setID(eintragDB.getID());
                        pruefplanEintrag.setStudiengang(eintragDB.getStudiengang());
                        pruefplanEintrag.setModul(eintragDB.getModul());
                        pruefplanEintrag.setSemester(eintragDB.getSemester());
                        pruefplanEintrag.setTermin(eintragDB.getTermin());
                        pruefplanEintrag.setRaum(eintragDB.getRaum());
                        pruefplanEintrag.setPruefform(eintragDB.getPruefform());
                        pruefplanEintrag.setStatus(eintragDB.getStatus());
                        pruefplanEintrag.setHint(eintragDB.getHint());

                        //lokale datenbank initialiseren
                         //DONE (08/2020) LG: Auskommentieren des erneuten Zugriffs
                         //AppDatabase database2 = AppDatabase.getAppDatabase(ctx2);
                         //List<PruefplanEintrag> userdaten2 = database2.userDao().getAll2();
                         //Log.d("Test4", String.valueOf(userdaten2.size()));

                         try {
                             for (int b = 0; b < Optionen.idList.size(); b++) {
                                 if (pruefplanEintrag.getID().equals(Optionen.idList.get(b))) {
                                     //Log.d("Test4", String.valueOf(userdaten2.get(b).getID()));
                                     pruefplanEintrag.setFavorit(true);
                                 }
                             }
                         }
                         catch (Exception e)
                         {
                            Log.d("Fehler RetrofitConnect",
                                  "Fehler beim Ermitteln der favorisierten Prüfungen");
                         }

                         //Schlüssel für die Erkennung bzw unterscheidung Festlegen
                         pruefplanEintrag.setValidation(jahr + eintragDB.getStudiengangId() + pruefungsphase);

                        // Start Merlin Gürtler
                        // Extra Thread da sonst die Db nicht aktualisiert werden kann.
                        new Thread((new Runnable() {
                            @Override
                            public void run() {
                                addUser(roomdaten, pruefplanEintrag);
                            }
                        })).start();
                        // Ende Merlin Gürtler
                    }

                    checkuebertragung = true;


                }//if(response.isSuccessful())
                else { Log.d("RESPONSE", ":::NO RESPONSE:::"); }
            }

            @Override
            public void onFailure(Call<List<JsonResponse>> call, Throwable t) {
                Log.d("Error",t.getMessage());

            }
        });
    }

    // Start Merlin Gürtler
    public void retroUpdate(Context ctx, final AppDatabase roomdaten,
                            final String jahr,
                            final String pruefungsphase,
                            final String termin,
                            final String serverAdress) {
        //Serveradresse
        SharedPreferences mSharedPreferencesAdresse = ctx.getSharedPreferences("Server-Adresse", 0);

        List <String> Ids = roomdaten.userDao().getChoosenStudiengangId(true);
        JSONArray studiengangIds = new JSONArray();

        for(String id: Ids) {
            try {
                JSONObject idJson = new JSONObject();
                idJson.put("ID", id);
                studiengangIds.put(idJson);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        ctx2 = ctx;
        //Creating editor to store uebergebeneModule to shared preferences
        String urlfhb = mSharedPreferencesAdresse.getString("ServerIPAddress", serverAdress);
        //uebergabe der parameter an die Adresse
        String relPathWithParameters = relativePPlanUrl
                + "entity.pruefplaneintrag/update/"
                + pruefungsphase +"/"
                + termin +"/"
                + jahr +"/"
                + studiengangIds.toString() + "/";

        String URL = urlfhb+ relPathWithParameters;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final RequestInterface request = retrofit.create(RequestInterface.class);
        Call<List<JsonUpdate>> call = request.getJSONUpdate();
        call.enqueue(new Callback<List<JsonUpdate>>() {
            @Override
            public void onResponse(Call<List<JsonUpdate>> call, Response<List<JsonUpdate>> response) {
                response.body();
                if (response.isSuccessful() && response.body().size() > 0) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            for(JsonUpdate updateExam: response.body()) {
                                try {
                                    String datumZeitzone;
                                    String datumDerPrüfung = updateExam.getDatum();
                                    String datumLetzePruefungFormatiert = null;
                                    datumZeitzone = datumDerPrüfung.replaceFirst("CET", "");
                                    datumZeitzone = datumZeitzone.replaceFirst("CEST","");
                                    DateFormat dateFormat = new SimpleDateFormat(
                                            "EEE MMM dd HH:mm:ss yyyy", Locale.US);
                                    Date datumLetztePrüfung = null;

                                    datumLetztePrüfung = dateFormat.parse(datumZeitzone);

                                    SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    datumLetzePruefungFormatiert = targetFormat.format(datumLetztePrüfung);

                                    roomdaten.userDao().updateExam(datumLetzePruefungFormatiert,
                                            updateExam.getStatus(),
                                            updateExam.getID(),
                                            updateExam.getHint());

                                    //Update den Eintrag aus dem Calendar falls vorhanden
                                    CheckGoogleCalendar cal = new CheckGoogleCalendar();
                                    cal.setCtx(ctx);
                                    if (!cal.checkCal(Integer.valueOf(updateExam.getID()))) {
                                        cal.updateCalendarEntry(Integer.valueOf(updateExam.getID()));
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }).start();
                }
                else { Log.d("RESPONSE", ":::NO RESPONSE:::"); }
            }



            @Override
            public void onFailure(Call<List<JsonUpdate>> call, Throwable t) {
                Log.d("Error",t.getMessage());
            }
        });
    }

    public void firstStart(Context ctx, final AppDatabase roomdaten,
                            final String serverAdress) {
        //Serveradresse
        SharedPreferences mSharedPreferencesAdresse = ctx.getSharedPreferences("Server-Adresse", 0);

        ctx2 = ctx;
        //Creating editor to store uebergebeneModule to shared preferences
        String urlfhb = mSharedPreferencesAdresse.getString("ServerIPAddress", serverAdress);

        // Erhalte die gewählte Fakultät aus den Shared Preferences
        SharedPreferences sharedPrefFakultaetValidation =
                ctx2.
                        getSharedPreferences("validation",0);

        String faculty = sharedPrefFakultaetValidation.getString("rueckgabeFakultaet", "0");

        //uebergabe der parameter an die Adresse
        String adresse = relativePPlanUrl + "entity.user/firstStart/" +
                faculty + "/";

        String URL = urlfhb+adresse;

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
                            roomdaten.userDao().insertUuid(response.body().getUuid());

                            // sende die gewählten Kurse
                            setUserCourses(ctx, roomdaten, serverAdress);
                        }
                    }).start();
                }
            }

            @Override
            public void onFailure(Call<JsonUuid> call, Throwable t) {
                Log.d("Error",t.getMessage());
            }
        });


    }

    public void anotherStart(Context ctx, final AppDatabase roomdaten,
                           final String serverAdress) {
        //Serveradresse
        SharedPreferences mSharedPreferencesAdresse = ctx.getSharedPreferences("Server-Adresse", 0);

        ctx2 = ctx;
        //Creating editor to store uebergebeneModule to shared preferences
        String urlfhb = mSharedPreferencesAdresse.getString("ServerIPAddress", serverAdress);

        Uuid uuid = roomdaten.userDao().getUuid();

        //uebergabe der parameter an die Adresse
        String adresse = relativePPlanUrl + "entity.user/anotherStart/" + uuid.getUuid() + "/";

        String URL = urlfhb+adresse;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final RequestInterface request = retrofit.create(RequestInterface.class);
        Call<Void> call = request.sendUuid();
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("Success","Success");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("Error",t.getMessage());
            }
        });


    }

    public void sendFeedBack(Context ctx, final AppDatabase roomdaten,
                             final String serverAdress, float usability,
                             float functions, float stability, String text) {
        //Serveradresse
        SharedPreferences mSharedPreferencesAdresse = ctx.getSharedPreferences("Server-Adresse", 0);

        ctx2 = ctx;
        //Creating editor to store uebergebeneModule to shared preferences
        String urlfhb = mSharedPreferencesAdresse.getString("ServerIPAddress", serverAdress);

        Uuid uuid = roomdaten.userDao().getUuid();

        // Falls kein Text eingegeben wurde
        if(text.length() < 1) {
            text = "Platzhalter";
        }

        //uebergabe der parameter an die Adresse
        String adresse = relativePPlanUrl + "entity.feedback/sendFeedback/" + uuid.getUuid()
                + "/" + usability + "/" + functions + "/" + stability + "/" + text + "/";

        String URL = urlfhb+adresse;

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
                    Log.d("Success","Success");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("Error",t.getMessage());
            }
        });

    }

    public void getStudiengaenge(Context ctx, final AppDatabase roomdaten,
                             final String serverAdress) {
        //Serveradresse
        SharedPreferences mSharedPreferencesAdresse = ctx.getSharedPreferences("Server-Adresse", 0);

        ctx2 = ctx;
        //Creating editor to store uebergebeneModule to shared preferences
        String urlfhb = mSharedPreferencesAdresse.getString("ServerIPAddress", serverAdress);

        //uebergabe der parameter an die Adresse
        String adresse = relativePPlanUrl + "entity.studiengang/";

        String URL = urlfhb+adresse;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final RequestInterface request = retrofit.create(RequestInterface.class);
        Call<List<JsonStudiengang>> call = request.getStudiengaenge();
        call.enqueue(new Callback<List<JsonStudiengang>>() {
            @Override
            public void onResponse(Call<List<JsonStudiengang>> call, Response<List<JsonStudiengang>> response) {
                if (response.isSuccessful()) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            // lösche die Tabelle
                            roomdaten.userDao().deleteStudiengang();
                            // füge die Einträge der db hinzu
                            for(JsonStudiengang studiengang: response.body()) {
                                roomdaten.userDao().insertStudiengang(
                                        studiengang.getSGID(),
                                        studiengang.getSGName(),
                                        studiengang.getFKFBID(),
                                        false
                                );
                            }
                        }
                    }).start();
                }
            }

            @Override
            public void onFailure(Call<List<JsonStudiengang>> call, Throwable t) {
                Log.d("Error",t.getMessage());
            }
        });

    }

    public void setUserCourses(Context ctx, final AppDatabase roomdaten,
                             final String serverAdress) {
        //Serveradresse
        SharedPreferences mSharedPreferencesAdresse = ctx.getSharedPreferences("Server-Adresse", 0);

        ctx2 = ctx;

        // erhalte die gewählten Studiengänge
        List <String> Ids = roomdaten.userDao().getChoosenStudiengangId(true);
        JSONArray studiengangIds = new JSONArray();

        for(String id: Ids) {
            try {
                JSONObject idJson = new JSONObject();
                idJson.put("ID", id);
                studiengangIds.put(idJson);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Uuid uuid = roomdaten.userDao().getUuid();

        //Creating editor to store uebergebeneModule to shared preferences
        String urlfhb = mSharedPreferencesAdresse.getString("ServerIPAddress", serverAdress);

        //uebergabe der parameter an die Adresse
        String adresse = relativePPlanUrl + "entity.usercourses/" +
                studiengangIds.toString() + "/" + uuid.getUuid() + "/" ;

        String URL = urlfhb+adresse;

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
                    Log.d("Success","Success");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("Error",t.getMessage());
            }
        });


    }
    // Ende Merlin Gürtler

    //DONE (08/2020) LG: Rückgabe des PPE wird nicht verwendet, deshalb gelöscht!
    public static void addUser(final AppDatabase db, PruefplanEintrag pruefplanEintrag) {
        PruefplanEintrag existingEntry = db.userDao().getPruefung(pruefplanEintrag.getID());
        // Merlin Gürtler fügt den Eintrag nur hinzu wenn er nicht vorhanden ist
        // dies wird verwendet, da die Favoriten behalten werden sollen
        if(existingEntry == null) {
            db.userDao().insertAll(pruefplanEintrag);
        }
    }
}