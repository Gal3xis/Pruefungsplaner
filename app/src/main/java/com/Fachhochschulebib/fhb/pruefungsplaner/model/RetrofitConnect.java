//////////////////////////////
// RetrofitConnect
//
//
//
// autor:
// inhalt: Verbindungsaufbau zum Webserver
// zugriffsdatum: 11.12.19
//
//
//
//
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
import com.Fachhochschulebib.fhb.pruefungsplaner.data.Pruefplan;

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
    private boolean checkvalidate = false;
    Context ctx2;

    public void retro(Context ctx, final AppDatabase roomdaten, final String jahr, final String studiengang, final String pruefungsphase, final String termin){
        //Serveradresse
        SharedPreferences mSharedPreferencesAdresse = ctx.getSharedPreferences("Server-Adresse", 0);


         ctx2 = ctx;
         termine = termin;
        //Creating editor to store uebergebeneModule to shared preferences
        String urlfhb = mSharedPreferencesAdresse.getString("Server-Adresse2","http://thor.ad.fh-bielefeld.de:8080/");

        Log.d("Output Studiengang",pruefungsphase.toString());
        Log.d("Output Studiengang",termin.toString());
        Log.d("Output Studiengang",jahr.toString());




        //String URLFHB = "http://thor.ad.fh-bielefeld.de:8080/";
        //String URLFHB = "http://192.168.178.39:44631/";
        //uebergabe der parameter an die Adresse
        //String adresse = "PruefplanApplika/webresources/entities.pruefplaneintrag/"+Pruefungsphase+"/"+aktuellerTermin+"/"+jahr+"/";
        String adresse = "PruefplanApplika/webresources/entities.pruefplaneintrag/"+pruefungsphase+"/"+termin+"/"+jahr+"/"+studiengang+"/";

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
                if (response.isSuccessful()) {
                    List<Pruefplan> database = roomdaten.userDao().getAll2();
                    //roomdaten.clearAllTables();

                    String validation = jahr+studiengang+pruefungsphase;

                    String checkTermin = "0";
                    for(int j = 0; j < database.size();j++) {
                        if (database.get(j).getValidation().equals(validation)){
                            //System.out.println("aufgerufen2223");
                            checkTermin = database.get(j).getTermin();
                            checkvalidate = true;

                        }}

                    //Schleife um jedes erhaltene Prüfungsobjekt in die lokale Datenbank hinzuzufügen
                    for (int i = response.body().size()-1 ; i > 0; --i) {

                        //Pruefplan ist die modelklasse für die angekommenden Prüfungsobjekte
                        Pruefplan pruefplan = new Pruefplan();

                        //Festlegen vom Dateformat
                        String datumZeitzone;
                        String datumDerPrüfung = response.body().get(i).getDatum();
                        datumZeitzone = datumDerPrüfung.replaceFirst("CET", "");
                        datumZeitzone = datumZeitzone.replaceFirst("CEST","");
                        String datumLetzePruefungFormatiert;
                        datumLetzePruefungFormatiert = null;
                        try {
                            DateFormat dateFormat = new SimpleDateFormat(
                                    "EEE MMM dd HH:mm:ss yyyy", Locale.US);
                            Date datumLetztePrüfung = dateFormat.parse(datumZeitzone);
                            SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            datumLetzePruefungFormatiert = targetFormat.format(datumLetztePrüfung);
                            Date datumAktuell = Calendar.getInstance().getTime();
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String datumAktuellFormatiert = df.format(datumAktuell);


                            Log.d("datum letzte prüfung",datumLetzePruefungFormatiert);
                            Log.d("datum aktuell",datumAktuellFormatiert);


                            //auskommentiert weil noch keine neuen Pürungstermine hochgeladen wurden.

                            /*
                            //Vergleich aktuelles Datum und datum der letzen Prüfung.
                            //wenn true dann datenbank löschen und die neuen Prüfungen laden
                            if(datumAktuell.getTime() > datumLetztePrüfung.getTime())
                            {
                                if (termine.equals("0")) {
                                    if(!termine.equals("1")) {
                                        roomdaten.clearAllTables();
                                    }
                                    //überprüfung zweiter termin aktuelle prüfperiode
                                    retro(ctx2, roomdaten, jahr, studiengang, pruefungsphase, "1");
                                    System.out.println("aufgerufen");
                                    break;
                                }
                            }

                             */

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                         if(!checkvalidate){
                             System.out.println("aufgerufen222");
                             //erhaltene Werte zur Datenbank hinzufügen
                            pruefplan.setErstpruefer(response.body().get(i).getErstpruefer());
                            pruefplan.setZweitpruefer(response.body().get(i).getZweitpruefer());
                            pruefplan.setDatum(String.valueOf(datumLetzePruefungFormatiert));
                            pruefplan.setID(response.body().get(i).getID());
                            pruefplan.setStudiengang(response.body().get(i).getStudiengang());
                            pruefplan.setModul(response.body().get(i).getModul());
                            pruefplan.setSemester(response.body().get(i).getSemester());
                            pruefplan.setTermin(response.body().get(i).getTermin());
                            pruefplan.setRaum(response.body().get(i).getRaum());

                            //lokale datenbank initialiseren
                             AppDatabase database2 = AppDatabase.getAppDatabase(ctx2);
                             List<Pruefplan> userdaten2 = database2.userDao().getAll2();
                             //Log.d("Test4", String.valueOf(userdaten2.size()));

                             try {
                                 for (int b = 0; b < Optionen.ID.size(); b++) {
                                     if (pruefplan.getID().equals(Optionen.ID.get(b))) {
                                         //Log.d("Test4", String.valueOf(userdaten2.get(b).getID()));
                                         pruefplan.setFavorit(true);
                                     }
                                 }
                             }
                             catch (Exception e)
                             {
                                Log.d("Fehler RetrofitConnect","Fehler beim ermitteln der favorisierten Prüfungen");

                             }

                            //Überprüfung von Klausur oder Mündliche Prüfung
                             String klausur = "K_90";
                             String klausur60 = "K_60";
                             String klausur120 = "K_120";
                             String klausur180 = "K_180";

                             pruefplan.setPruefform("Mündliche Prüfung / Hausarbeit");

                             if(klausur.equals(response.body().get(i).getPruefform())) {
                                pruefplan.setPruefform("Klausur 90 Minuten");

                            }
                             if(klausur120.equals(response.body().get(i).getPruefform())) {
                                 pruefplan.setPruefform("Klausur 120 Minuten");

                             }
                             if(klausur60.equals(response.body().get(i).getPruefform())) {
                                 pruefplan.setPruefform("Klausur 60 Minuten");

                             }
                             if(klausur180.equals(response.body().get(i).getPruefform())) {
                                 pruefplan.setPruefform("Klausur 180 Minuten");

                             }

                            //Schlüssel für die Erkennung bzw unterscheidung Festlegen
                            pruefplan.setValidation(jahr + studiengang + pruefungsphase);
                             System.out.println(jahr + studiengang + pruefungsphase);
                             addUser(roomdaten, pruefplan);

                        }
                    }
                    checkuebertragung = true;

                }
                else { System.out.println(" :::. NO RESPONSE .::: "); }
            }



            @Override
            public void onFailure(Call<List<JsonResponse>> call, Throwable t) {
                Log.d("Error",t.getMessage());

            }
        });
    }

    public static Pruefplan addUser(final AppDatabase db, Pruefplan pruefplan) {
        db.userDao().insertAll(pruefplan);
        System.out.println(pruefplan.getErstpruefer());
        System.out.println("\n");
        return pruefplan;
    }




    }
