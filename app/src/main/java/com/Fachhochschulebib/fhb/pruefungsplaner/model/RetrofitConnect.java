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

import com.Fachhochschulebib.fhb.pruefungsplaner.Optionen;
import com.Fachhochschulebib.fhb.pruefungsplaner.R;
import com.Fachhochschulebib.fhb.pruefungsplaner.RequestInterface;
import com.Fachhochschulebib.fhb.pruefungsplaner.data.AppDatabase;
import com.Fachhochschulebib.fhb.pruefungsplaner.data.PruefplanEintrag;

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

        //Uebergabe der Parameter an den relativen Server-Pfad
        String relPathWithParameters = relativePPlanUrl
                            + "entity.pruefplaneintrag/"
                            + pruefungsphase +"/"
                            + termin +"/"
                            + jahr +"/";

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
                    for (int i = response.body().size()-1 ; i >= 0; --i) {

                        //Pruefplan ist die Modelklasse für die angekommenden Prüfungsobjekte
                        PruefplanEintrag pruefplanEintrag = new PruefplanEintrag();

                        //Festlegen vom Dateformat
                        String datumZeitzone;
                        String datumDerPruefung = response.body().get(i).getDatum();
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
                        pruefplanEintrag.setErstpruefer(response.body().get(i).getErstpruefer());
                        pruefplanEintrag.setZweitpruefer(response.body().get(i).getZweitpruefer());
                        pruefplanEintrag.setDatum(String.valueOf(datumLetzePruefungFormatiert));
                        pruefplanEintrag.setID(response.body().get(i).getID());
                        pruefplanEintrag.setStudiengang(response.body().get(i).getStudiengang());
                        pruefplanEintrag.setModul(response.body().get(i).getModul());
                        pruefplanEintrag.setSemester(response.body().get(i).getSemester());
                        pruefplanEintrag.setTermin(response.body().get(i).getTermin());
                        pruefplanEintrag.setRaum(response.body().get(i).getRaum());

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

                         //Überprüfung von Klausur oder Mündliche Prüfung
                         pruefplanEintrag.setPruefform(ctx.getString(R.string.mundHaus_text));

                         if(ctx.getString(R.string.klausur).equals(response.body().get(i).getPruefform())) {
                            pruefplanEintrag.setPruefform(ctx.getString(R.string.klausur_text));

                         }
                         if(ctx.getString(R.string.klausur120).equals(response.body().get(i).getPruefform())) {
                             pruefplanEintrag.setPruefform(ctx.getString(R.string.klausur120_text));

                         }
                         if(ctx.getString(R.string.klausur60).equals(response.body().get(i).getPruefform())) {
                             pruefplanEintrag.setPruefform(ctx.getString(R.string.klausur60_text));

                         }
                         if(ctx.getString(R.string.klausur180).equals(response.body().get(i).getPruefform())) {
                             pruefplanEintrag.setPruefform(ctx.getString(R.string.klausur180_text));

                         }
                        if(ctx.getString(R.string.projektarbeit).equals(response.body().get(i).getPruefform())) {
                            pruefplanEintrag.setPruefform(ctx.getString(R.string.projektarbeit_text));

                        }
                        if(ctx.getString(R.string.kombination15).equals(response.body().get(i).getPruefform())) {
                            pruefplanEintrag.setPruefform(ctx.getString(R.string.kombination15_text));

                        }
                        if(ctx.getString(R.string.kombination30).equals(response.body().get(i).getPruefform())) {
                            pruefplanEintrag.setPruefform(ctx.getString(R.string.kombination30_text));

                        }
                        if(ctx.getString(R.string.ilias60).equals(response.body().get(i).getPruefform())) {
                            pruefplanEintrag.setPruefform(ctx.getString(R.string.ilias60_text));

                        }
                        if(ctx.getString(R.string.ilias90).equals(response.body().get(i).getPruefform())) {
                            pruefplanEintrag.setPruefform(ctx.getString(R.string.ilias90_text));

                        }
                        if(ctx.getString(R.string.online60).equals(response.body().get(i).getPruefform())) {
                            pruefplanEintrag.setPruefform(ctx.getString(R.string.online60_text));

                        }
                        if(ctx.getString(R.string.online90).equals(response.body().get(i).getPruefform())) {
                            pruefplanEintrag.setPruefform(ctx.getString(R.string.online90_text));

                        }

                         //Start Änderungen vorgenommen Merlin Gürtler
                         //Schlüssel für die Erkennung bzw unterscheidung Festlegen
                         pruefplanEintrag.setValidation(jahr + response.body().get(i).getStudiengangId() + pruefungsphase);

                         // Ende Merlin Gürtler
                         addUser(roomdaten, pruefplanEintrag);

                        // }
                    }
                    checkuebertragung = true;

                }//if(response.isSuccessful())
                else { System.out.println(" :::. NO Retrofit RESPONSE .::: "); }
            }

            @Override
            public void onFailure(Call<List<JsonResponse>> call, Throwable t) {
                Log.d("Error",t.getMessage());

            }
        });
    }

    // Start Merlin Gürtler
    public void retroUpdate(Context ctx, final AppDatabase roomdaten,
                            final String serverAdress) {
        //Serveradresse
        SharedPreferences mSharedPreferencesAdresse = ctx.getSharedPreferences("Server-Adresse", 0);

        ctx2 = ctx;
        //Creating editor to store uebergebeneModule to shared preferences
        String urlfhb = mSharedPreferencesAdresse.getString("ServerIPAddress", serverAdress);

        JSONArray knownExamsJson = new JSONArray();
        List<PruefplanEintrag> knownExams = roomdaten.userDao().getAll2();
        // Create the JSON Array
        for(int i = 0; i < knownExams.size(); i++) {
            JSONObject knownExam = new JSONObject();
            try {
                knownExam.put("Datum", knownExams.get(i).getDatum());
                knownExam.put("ID", knownExams.get(i).getID());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            knownExamsJson.put(knownExam);

        }
        // Stringify for the Request
        String sendExams = knownExamsJson.toString();
        //uebergabe der parameter an die Adresse
        String adresse = relativePPlanUrl + "entity.pruefplaneintrag/update/" + sendExams + "/";

        String URL = urlfhb+adresse;
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
                    for(int i = 0; response.body().size() > i; i++) {
                        try {
                            String datumZeitzone;
                            String datumDerPrüfung = response.body().get(i).getDatum();
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
                                    response.body().get(i).getID());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
                else { System.out.println(" :::. NO RESPONSE .::: "); }
            }



            @Override
            public void onFailure(Call<List<JsonResponse>> call, Throwable t) {
                Log.d("Error",t.getMessage());

            }
        });
    }

    // Ende Merlin Gürtler

    //DONE (08/2020) LG: Rückgabe des PPE wird nicht verwendet, deshalb gelöscht!
    public static void addUser(final AppDatabase db, PruefplanEintrag pruefplanEintrag) {
        db.userDao().insertAll(pruefplanEintrag);
    }
}