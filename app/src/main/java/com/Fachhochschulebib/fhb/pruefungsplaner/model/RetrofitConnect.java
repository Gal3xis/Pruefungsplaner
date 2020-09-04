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
    // private boolean checkvalidate = false;
    Context ctx2;

    String klausur = "K_90";
    String klausur60 = "K_60";
    String klausur120 = "K_120";
    String klausur180 = "K_180";
    String muendlich = "M_30";
    String projektarbeit = "PA_30";
    String kombination15 = "KP_15";
    String kombination30 = "KP_30";
    String ilias60 = "I_60";
    String ilias90 = "I_90";
    String online60 = "0_60";
    String online90 = "0_90";


    //DONE (08/2020 LG) Parameter 7,8 eingefügt --> Adresse an zentraler Stelle verwalten
    public <serverAdress> void RetrofitWebAccess(Context ctx,
                                                 final com.Fachhochschulebib.fhb.pruefungsplaner.data.AppDatabase roomdaten,
                                                 final String jahr,
                                                 final String pruefungsphase,
                                                 final String termin,
                                                 final String serverAdress,
                                                 final String relativePPlanUrl){
        //Serveradresse
        SharedPreferences mSharedPreferencesAdresse
                = ctx.getSharedPreferences("Server_Address", 0);

        ctx2 = ctx;
        termine = termin;
        //Creating editor to store uebergebeneModule to shared preferences
        String urlfhb = mSharedPreferencesAdresse.getString("ServerIPAddress", serverAdress);
        Log.d("Output Studiengang",pruefungsphase.toString());
        Log.d("Output Studiengang",termin.toString());
        Log.d("Output Studiengang",jahr.toString());

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

        final com.Fachhochschulebib.fhb.pruefungsplaner.RequestInterface request = retrofit.create(com.Fachhochschulebib.fhb.pruefungsplaner.RequestInterface.class);
        Call<List<com.Fachhochschulebib.fhb.pruefungsplaner.model.JsonResponse>> call = request.getJSON();
        call.enqueue(new Callback<List<com.Fachhochschulebib.fhb.pruefungsplaner.model.JsonResponse>>() {
            @Override
            public void onResponse(Call<List<com.Fachhochschulebib.fhb.pruefungsplaner.model.JsonResponse>> call, Response<List<com.Fachhochschulebib.fhb.pruefungsplaner.model.JsonResponse>> response) {
                response.body();
                if (response.isSuccessful()) {

                    //Hole alle Einträge aus der lokalen Room-DB
                    List<com.Fachhochschulebib.fhb.pruefungsplaner.data.PruefplanEintrag> dataListFromLocalDB = null;
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
                        com.Fachhochschulebib.fhb.pruefungsplaner.data.PruefplanEintrag pruefplanEintrag = new com.Fachhochschulebib.fhb.pruefungsplaner.data.PruefplanEintrag();

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
                             for (int b = 0; b < com.Fachhochschulebib.fhb.pruefungsplaner.Optionen.idList.size(); b++) {
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
                         pruefplanEintrag.setPruefform("Mündliche Prüfung / Hausarbeit");

                         if(klausur.equals(response.body().get(i).getPruefform())) {
                            pruefplanEintrag.setPruefform("Klausur 90 Minuten");

                         }
                         if(klausur120.equals(response.body().get(i).getPruefform())) {
                             pruefplanEintrag.setPruefform("Klausur 120 Minuten");

                         }
                         if(klausur60.equals(response.body().get(i).getPruefform())) {
                             pruefplanEintrag.setPruefform("Klausur 60 Minuten");

                         }
                         if(klausur180.equals(response.body().get(i).getPruefform())) {
                             pruefplanEintrag.setPruefform("Klausur 180 Minuten");

                         }
                        if(muendlich.equals(response.body().get(i).getPruefform())) {
                            pruefplanEintrag.setPruefform("Mündliche Prüfung");

                        }
                        if(projektarbeit.equals(response.body().get(i).getPruefform())) {
                            pruefplanEintrag.setPruefform("Projektarbeit");

                        }
                        if(kombination15.equals(response.body().get(i).getPruefform())) {
                            pruefplanEintrag.setPruefform("Kombinationsprüfung 15 Minuten");

                        }
                        if(kombination30.equals(response.body().get(i).getPruefform())) {
                            pruefplanEintrag.setPruefform("Kombinationsprüfung 30 Minuten");

                        }
                        if(ilias60.equals(response.body().get(i).getPruefform())) {
                            pruefplanEintrag.setPruefform("Ilias-Test 60 Minuten");

                        }
                        if(ilias90.equals(response.body().get(i).getPruefform())) {
                            pruefplanEintrag.setPruefform("Ilias-Test 90 Minuten");

                        }
                        if(online60.equals(response.body().get(i).getPruefform())) {
                            pruefplanEintrag.setPruefform("Onlineprüfung 60 Minuten");

                        }
                        if(online90.equals(response.body().get(i).getPruefform())) {
                            pruefplanEintrag.setPruefform("Onlineprüfung 90 Minuten");

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
            public void onFailure(Call<List<com.Fachhochschulebib.fhb.pruefungsplaner.model.JsonResponse>> call, Throwable t) {
                Log.d("Error",t.getMessage());

            }
        });
    }

    // Start Merlin Gürtler
    public void retroUpdate(Context ctx, final com.Fachhochschulebib.fhb.pruefungsplaner.data.AppDatabase roomdaten,
                            final String serverAdress,
                            final String relativePPlanUrl) {
        //Serveradresse
        SharedPreferences mSharedPreferencesAdresse = ctx.getSharedPreferences("Server-Adresse", 0);

        ctx2 = ctx;
        //Creating editor to store uebergebeneModule to shared preferences
        String urlfhb = mSharedPreferencesAdresse.getString("ServerIPAddress", serverAdress);

        JSONArray knownExamsJson = new JSONArray();
        List<com.Fachhochschulebib.fhb.pruefungsplaner.data.PruefplanEintrag> knownExams = roomdaten.userDao().getAll2();
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
        String adresse = "PruefplanverwaltungRestIF/webresources/entity.pruefplaneintrag/update/" + sendExams + "/";

        String URL = urlfhb+adresse;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final com.Fachhochschulebib.fhb.pruefungsplaner.RequestInterface request = retrofit.create(RequestInterface.class);
        Call<List<com.Fachhochschulebib.fhb.pruefungsplaner.model.JsonResponse>> call = request.getJSON();
        call.enqueue(new Callback<List<com.Fachhochschulebib.fhb.pruefungsplaner.model.JsonResponse>>() {
            @Override
            public void onResponse(Call<List<com.Fachhochschulebib.fhb.pruefungsplaner.model.JsonResponse>> call, Response<List<com.Fachhochschulebib.fhb.pruefungsplaner.model.JsonResponse>> response) {
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