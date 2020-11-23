//////////////////////////////
// CheckGoogleCalendar
//
//
//
// autor:
// inhalt: Hinzufügen, Löschen, Aktualisieren von Prüfobjekten in den Google Calendar
// zugriffsdatum: 11.12.19
//
//
//
//
//
//
//////////////////////////////


package com.Fachhochschulebib.fhb.pruefungsplaner;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;
import android.util.Log;

import com.Fachhochschulebib.fhb.pruefungsplaner.data.AppDatabase;
import com.Fachhochschulebib.fhb.pruefungsplaner.data.TestPlanEntry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;


public class CheckGoogleCalendar {

    private int pruefID;
    private int googleID;
    private Context context;
    //private String[] ids = new String[100];


    public void setCtx (Context cx){
        context = cx;
    }


    //Mehtode um Prüfid und Google Kalender Id zu speichern
    public void insertCal(int pruefid, int googleid){

       //Variablen
        pruefID = pruefid;
        googleID = googleid;


        //Creating editor to store uebergebeneModule to shared preferences
        SharedPreferences mSharedPreferences
                = context.getSharedPreferences("GoogleID-und-PruefID13", 0);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        String stringids = mSharedPreferences.getString("IDs","");

        // step one : converting comma separate String to array of String
        String[] elements = null;
        try {
            elements = stringids.split("/");
        }catch (Exception e){
            Log.d("Fehler CheckGoogleCal","Fehler beim Aufteilen der String-Elemente!");
        }
        // step two : convert String array to list of String
        List<String> fixedLenghtList = Arrays.asList(elements);

       // Log.i("test", String.valueOf(elements.length));

        // step three : copy fixed list to an ArrayList
        ArrayList<String> listOfString = new ArrayList<String>(fixedLenghtList);

        //step four : check size and add new element
        listOfString.add("-" + String.valueOf(pruefid) + "," + String.valueOf(googleid));

        //step fifth : change Stringarray to String type
        String idsTOstring = "/";
        for(int i = 0; i < listOfString.size();i++)
        {
            idsTOstring = idsTOstring + listOfString.get(i);
        }

        //Log.i("test", String.valueOf(idsTOstring));

        //step six : add to database

        mEditor.putString("IDs",String.valueOf(idsTOstring));
        mEditor.apply();
    }

    //Methode zum überprüfen ob der eintrag im Googlekalender vorhanden ist
    public boolean checkCal(int pruefid) {
        //Variablen
        pruefID = pruefid;

        //Creating editor to store uebergebeneModule to shared preferences
        SharedPreferences mSharedPreferences
                = context.getSharedPreferences("GoogleID-und-PruefID13", 0);
        String stringids = mSharedPreferences.getString("IDs", "");

        // step one : converting comma separate String to array of String
        String[] elementList = stringids.split("-");

        // step two : convert String array to list of String
        List<String> fixedLengthList = Arrays.asList(elementList);

        // step three : copy fixed list to an ArrayList
        ArrayList<String> listOfString = new ArrayList<String>(fixedLengthList);

        //step fifth : change Stringarray to String type
        Log.i("check_Googlekalender", String.valueOf(listOfString.size()));

        //String idsTOstring = null;
        for (int i = 1; i < listOfString.size(); i++) {
            // step six : split Prüfid und GoogleID
            String[] element = listOfString.get(i).split(",");
            Log.i("check_Checkbool", element[0].toString());

            if (element[0].equals(String.valueOf(pruefID))) {
                //wenn prüfid vorhanden return false
                Log.i("check_Checkbool", "Pid stimmt überein");

                return false;
            }
        }

        //wenn prüfid nicht vorhanden --> return true
        return true;
    }


    //alle bisherigen Google Kalender einträge löschen
    public void clearCal()
    {

        //Creating editor to store uebergebeneModule to shared preferences
        SharedPreferences mSharedPreferences
                = context.getSharedPreferences("GoogleID-und-PruefID13", 0);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        String stringids = mSharedPreferences.getString("IDs", "");

        // step one : converting comma separate String to array of String
        String[] elementList = stringids.split("-");

        // step two : convert String array to list of String
        List<String> fixedLengthList = Arrays.asList(elementList);

        // step three : copy fixed list to an ArrayList
        ArrayList<String> listOfString = new ArrayList<String>(fixedLengthList);


        //hier werden die einträge aus dem google Kalender gelöscht
        for (int i = 1; i < listOfString.size(); i++) {
            // step six : split Prüfid und GoogleID
            String[] element = listOfString.get(i).split(",");
            //Google Calendar einträge löschen
            //output tag
            String DEBUG_TAG = "MyActivity";
            //element[1] enthält die googleid
            long eventID = Long.parseLong(element[1]);
            //getContentResolver wird benötigt zum zugriff auf die Kalender API
            ContentResolver cr = context.getContentResolver();
            Uri deleteUri = null;
            //delete eintrag mit eventID

            Uri baseUri = Uri.parse("content://calendar/events");

            deleteUri = ContentUris.withAppendedId(baseUri, eventID);
            int rows = cr.delete(deleteUri, null, null);
            //outputlog for Debug
            Log.i(DEBUG_TAG, "Rows deleted: " + rows);

        }
        //step six : reset Database
        mEditor.putString("IDs","");
        mEditor.apply();
    }

    //Google Kalender Checkverbindung Methode
    public void updateCal()
    {
        //Creating editor to store uebergebeneModule to shared preferences
        SharedPreferences googleEntry
                = context.getSharedPreferences("GoogleID-und-PruefID13", 0);
        String stringids = googleEntry.getString("IDs", "");

        // step one : converting comma separate String to array of String
        String[] elementsList = stringids.split("-");

        Log.i("userID", String.valueOf(elementsList.length));
        // step two : convert String array to list of String
        List<String> fixedLengthList = Arrays.asList(elementsList);

        // step three : copy fixed list to an ArrayList
        ArrayList<String> listOfString = new ArrayList<String>(fixedLengthList);

        //step four: Database connect
        List<TestPlanEntry> ppeList = databaseConnect();

        //step fifth: Schleifen zum vergleichen
        Log.i("userID", String.valueOf(listOfString.size()));
        for (int i = 1; i < listOfString.size(); i++) {
            //Variable mit Element[0] prüfplanID und element[1] Google Calendar id
            String[] element = listOfString.get(i).split(",");
            Log.i("userID", Arrays.toString(element));
            Log.i("elemnt[0}", String.valueOf(element[0]));
            for (TestPlanEntry entry: ppeList) {
                // wenn id  gleich id vom google Calendar dann get element[1] dieser id, element[1]
                // ist die GoogleCalendar Id für den gespeicherten eintrag
                Log.i("userID2", entry.getID());
                if(entry.getID().equals(element[0])) {
                    //output tag
                    String DEBUG_TAG = "MyActivity";
                    //eventID ist die Google calendar Id
                    long eventID = Long.parseLong(element[1]);
                    //Klasse für das updaten von werten
                    ContentResolver cr = context.getContentResolver();
                    ContentValues values = new ContentValues();
                    Uri updateUri;
                    //Datum und Uhrzeit aufteilen.
                    // Sieht so aus wie 22-01-2019 10:00 Uhr
                    // es wird nach dem Leerzeichen getrennt
                    //trennen von datum und Uhrzeit
                    String[] s = entry.getDate().split(" ");
                    //print Datum
                    //aufteilen von tag, monat und jahr.
                    //sieht aus wie 22-01-2019 aufgeteilt in ss[0] =  22 ,ss[1] = 01, ss[2] = 2019
                    String[] ss = s[0].split("-");
                    //aufteilen von der Uhrzeit Stunden der prüfung und Minuten der prüfung
                    int time1 = Integer.parseInt(s[1].substring(0, 2));
                    int time2 = Integer.parseInt(s[1].substring(4, 5));
                    // The new title for the updatet event
                    values.put(CalendarContract.Events.TITLE, entry.getModule());
                    values.put(CalendarContract.Events.EVENT_LOCATION,
                               "Fachhochschule Bielefeld Update");
                    values.put(CalendarContract.Events.DESCRIPTION, "");
                    //umwandeln von Datum und uhrzeit in GregorianCalender für eine leichtere weiterverarbeitung
                    GregorianCalendar calDate = new GregorianCalendar(
                            Integer.parseInt(ss[0]),
                            Integer.parseInt(ss[1]) - 1,
                            Integer.parseInt(ss[2]),
                            time1, time2);
                    values.put(CalendarContract.EXTRA_EVENT_ALL_DAY, false);
                    values.put(CalendarContract.Events.DTSTART, calDate.getTimeInMillis());
                    values.put( CalendarContract.Events.DTEND,
                                calDate.getTimeInMillis() + (90 * 60000));
                    //uebergebeneModule.put(CalendarContract.Events.);
                    //Checkverbindung Eintrag
                    Uri baseUri = Uri.parse("content://calendar/events");

                    updateUri = ContentUris.withAppendedId(baseUri, eventID);
                    //variable zum anzeigen der geänderten werte
                    int rows = cr.update(updateUri, values, null, null);

                    //testausgabe
                    Log.i(DEBUG_TAG, "Rows updated: 240 " + rows);
                }
            }
        }
    }

    // Start Merlin Gürtler
    //Methode zum entfernen eines Kalendereintrages
    public void deleteEntry(int pruefid) {

        //Variablen
        pruefID = pruefid;

        //Creating editor to store uebergebeneModule to shared preferences
        SharedPreferences mSharedPreferences
                = context.getSharedPreferences("GoogleID-und-PruefID13", 0);

        String stringids = mSharedPreferences.getString("IDs", "");

        // step one : converting comma separate String to array of String
        String[] elementList = stringids.split("-");

        // step two : convert String array to list of String
        List<String> fixedLenghtList = Arrays.asList(elementList);

        // step three : copy fixed list to an ArrayList
        ArrayList<String> listOfString = new ArrayList<String>(fixedLenghtList);

        //step fifth : change Stringarray to String type
        Log.i("check_Googlekalender", String.valueOf(listOfString.size()));
        //String idsTOstring = null;
        for (int i = 1; i < listOfString.size(); i++) {
            // step six : split Prüfid und GoogleID
            String[] element = listOfString.get(i).split(",");
            if (element[0].equals(String.valueOf(pruefID))) {
                //output tag
                String DEBUG_TAG = "MyActivity";

                //getContentResolver wird benötigt zum zugriff auf die Kalender API
                ContentResolver cr = context.getContentResolver();
                Uri deleteUri = null;

                long eventID = Long.parseLong(element[1]);

                Uri baseUri = Uri.parse("content://calendar/events");

                //wenn prüfid vorhanden lösche diese
                Log.i("check_Checkbool", "Pid stimmt überein");
                deleteUri = ContentUris.withAppendedId(baseUri, eventID);
                int rows = cr.delete(deleteUri, null, null);
                Log.i(DEBUG_TAG, "Rows deleted: 240 " + rows);

            }
        }

    }

    public TestPlanEntry getFavoritePruefung(String id){
        AppDatabase database2 = AppDatabase.getAppDatabase(context);
        // Erhalte die Prüfung die geupdated werden soll
        return(database2.userDao().getEntryById(id));
    }


    // Ende Merlin Gürtler

    public List<TestPlanEntry> databaseConnect(){
        AppDatabase database2 = AppDatabase.getAppDatabase(context);
        // Änderun Merlin Gürtler
        // Nicht alle Einträge, um Iterationen zu sparen
        return(database2.userDao().getFavorites(true));
    }

    public void updateCalendarEntry(int pruefid) {
        //Variablen
        pruefID = pruefid;

        //Creating editor to store uebergebeneModule to shared preferences
        SharedPreferences mSharedPreferences
                = context.getSharedPreferences("GoogleID-und-PruefID13", 0);

        String stringids = mSharedPreferences.getString("IDs", "");

        // step one : converting comma separate String to array of String
        String[] elementList = stringids.split("-");

        // step two : convert String array to list of String
        List<String> fixedLenghtList = Arrays.asList(elementList);

        // step three : copy fixed list to an ArrayList
        ArrayList<String> listOfString = new ArrayList<String>(fixedLenghtList);

        //step fifth : change Stringarray to String type
        Log.i("check_Googlekalender", String.valueOf(listOfString.size()));
        //String idsTOstring = null;
        for (int i = 1; i < listOfString.size(); i++) {
            // step six : split Prüfid und GoogleID
            String[] element = listOfString.get(i).split(",");
            if (element[0].equals(String.valueOf(pruefID))) {
                //output tag
                String DEBUG_TAG = "MyActivity";

                long eventID = Long.parseLong(element[1]);

                Uri baseUri = Uri.parse("content://calendar/events");

                ContentValues values = new ContentValues();

                TestPlanEntry favoriteExam = getFavoritePruefung(String.valueOf(pruefID));

                //Datum und Uhrzeit aufteilen.
                // Sieht so aus wie 22-01-2019 10:00 Uhr
                // es wird nach dem Leerzeichen getrennt
                //trennen von datum und Uhrzeit
                String[] s = favoriteExam.getDate().split(" ");
                //print Datum
                //aufteilen von tag, monat und jahr.
                //sieht aus wie 22-01-2019 aufgeteilt in ss[0] =  22 ,ss[1] = 01, ss[2] = 2019
                String[] ss = s[0].split("-");
                //aufteilen von der Uhrzeit Stunden der prüfung und Minuten der prüfung
                int time1 = Integer.parseInt(s[1].substring(0, 2));
                int time2 = Integer.parseInt(s[1].substring(4, 5));

                // The new title for the updatet event
                values.put(CalendarContract.Events.TITLE, favoriteExam.getModule());
                values.put(CalendarContract.Events.EVENT_LOCATION,
                        "Fachhochschule Bielefeld");
                values.put(CalendarContract.Events.DESCRIPTION, "");
                //umwandeln von Datum und uhrzeit in GregorianCalender für eine leichtere weiterverarbeitung
                GregorianCalendar calDate = new GregorianCalendar(
                        Integer.parseInt(ss[0]),
                        Integer.parseInt(ss[1]) - 1,
                        Integer.parseInt(ss[2]),
                        time1, time2);
                values.put(CalendarContract.EXTRA_EVENT_ALL_DAY, false);
                values.put(CalendarContract.Events.DTSTART, calDate.getTimeInMillis());
                values.put( CalendarContract.Events.DTEND,
                        calDate.getTimeInMillis() + (90 * 60000));

                //wenn prüfid vorhanden update das event
                Log.i("check_Checkbool", "Pid stimmt überein");
                Uri updateUri = ContentUris.withAppendedId(baseUri, eventID);
                int rows = context.getContentResolver().update(updateUri, values, null,
                        null);
                Log.i(DEBUG_TAG, "Rows updated: 240 " + rows);

            }
        }
    }
}