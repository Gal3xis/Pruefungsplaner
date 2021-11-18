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
package com.Fachhochschulebib.fhb.pruefungsplaner

import android.content.*
import android.net.Uri
import com.Fachhochschulebib.fhb.pruefungsplaner.data.TestPlanEntry
import android.provider.CalendarContract
import android.util.Log
import com.Fachhochschulebib.fhb.pruefungsplaner.data.AppDatabase
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class CheckGoogleCalendar {
    private var pruefID = 0
    private var googleID = 0
    private var context: Context? = null

    //private String[] ids = new String[100];
    fun setCtx(cx: Context?) {
        context = cx
    }

    //Mehtode um Prüfid und Google Kalender Id zu speichern
    fun insertCal(pruefid: Int, googleid: Int) {

        //Variablen
        pruefID = pruefid
        googleID = googleid


        //Creating editor to store uebergebeneModule to shared preferences
        val mSharedPreferences = context?.getSharedPreferences("GoogleID-und-PruefID13", 0)
        val mEditor = mSharedPreferences?.edit()
        val stringids = mSharedPreferences?.getString("IDs", "")

        // step one : converting comma separate String to array of String
        var elements: Array<String?>? = null
        try {
            elements = stringids?.split("/")?.toTypedArray()
        } catch (e: Exception) {
            Log.d("Fehler CheckGoogleCal", "Fehler beim Aufteilen der String-Elemente!")
        }
        // step two : convert String array to list of String
        val fixedLengthList = elements?.toMutableList()

        // Log.i("test", String.valueOf(elements.length));

        // step three : copy fixed list to an ArrayList
        val listOfString = ArrayList(fixedLengthList)

        //step four : check size and add new element
        listOfString.add("-$pruefid,$googleid")

        //step fifth : change Stringarray to String type
        var idsTOstring = "/"
        for (i in listOfString.indices) {
            idsTOstring = idsTOstring + listOfString[i]
        }

        //Log.i("test", String.valueOf(idsTOstring));

        //step six : add to database
        mEditor?.putString("IDs", idsTOstring)
        mEditor?.apply()
    }

    //Methode zum überprüfen ob der eintrag im Googlekalender vorhanden ist
    fun checkCal(pruefid: Int): Boolean {
        //Variablen
        pruefID = pruefid

        //Creating editor to store uebergebeneModule to shared preferences
        val mSharedPreferences = context?.getSharedPreferences("GoogleID-und-PruefID13", 0)
        val stringids = mSharedPreferences?.getString("IDs", "")

        // step one : converting comma separate String to array of String
        val elementList = stringids?.split("-")?.toTypedArray()

        // step two : convert String array to list of String
        val fixedLengthList = elementList?.toMutableList()

        // step three : copy fixed list to an ArrayList
        val listOfString = ArrayList(fixedLengthList)

        //step fifth : change Stringarray to String type
        Log.i("check_Googlekalender", listOfString.size.toString())

        //String idsTOstring = null;
        for (i in 1 until listOfString.size) {
            // step six : split Prüfid und GoogleID
            val element = listOfString[i]?.split(",")?.toTypedArray()
            Log.i("check_Checkbool", element?.get(0)?:"No Element")
            if (element?.get(0) == pruefID.toString()) {
                //wenn prüfid vorhanden return false
                Log.i("check_Checkbool", "Pid stimmt überein")
                return false
            }
        }

        //wenn prüfid nicht vorhanden --> return true
        return true
    }

    //alle bisherigen Google Kalender einträge löschen
    fun clearCal() {

        //Creating editor to store uebergebeneModule to shared preferences
        val mSharedPreferences = context?.getSharedPreferences("GoogleID-und-PruefID13", Context.MODE_PRIVATE)
        val mEditor = mSharedPreferences?.edit()
        val stringids = mSharedPreferences?.getString("IDs", "")

        // step one : converting comma separate String to array of String
        val elementList = stringids?.split("-")?.toTypedArray()

        // step two : convert String array to list of String
        val fixedLengthList = elementList?.toMutableList()

        // step three : copy fixed list to an ArrayList
        val listOfString = ArrayList(fixedLengthList)


        //hier werden die einträge aus dem google Kalender gelöscht
        for (i in 1 until listOfString.size) {
            // step six : split Prüfid und GoogleID
            val element = listOfString[i].split(",").toTypedArray()
            //Google Calendar einträge löschen
            //output tag
            val DEBUG_TAG = "MyActivity"
            //element[1] enthält die googleid
            val eventID = element.get(1).toLong()
            //getContentResolver wird benötigt zum zugriff auf die Kalender API
            val cr = context?.contentResolver
            var deleteUri: Uri? = null
            //delete eintrag mit eventID
            val baseUri = Uri.parse("content://com.android.calendar/events")
            deleteUri = ContentUris.withAppendedId(baseUri, eventID)
            val rows = cr?.delete(deleteUri, null, null)
            //outputlog for Debug
            Log.i(DEBUG_TAG, "Rows deleted: $rows")
        }
        //step six : reset Database
        mEditor?.putString("IDs", "")
        mEditor?.apply()
    }

    //Google Kalender Checkverbindung Methode
    fun updateCal() {
        //Creating editor to store uebergebeneModule to shared preferences
        val googleEntry = context?.getSharedPreferences("GoogleID-und-PruefID13", Context.MODE_PRIVATE)
        val stringids = googleEntry?.getString("IDs", "")

        // step one : converting comma separate String to array of String
        val elementsList = stringids?.split("-")?.toTypedArray()
        Log.i("userID", elementsList?.size?.toString()?:"No Element")
        // step two : convert String array to list of String
        val fixedLengthList = elementsList?.toMutableList()

        // step three : copy fixed list to an ArrayList
        val listOfString = ArrayList(fixedLengthList)

        //step four: Database connect
        val ppeList = databaseConnect()

        //step fifth: Schleifen zum vergleichen
        Log.i("userID", listOfString.size.toString())
        for (i in 1 until listOfString.size) {
            //Variable mit Element[0] prüfplanID und element[1] Google Calendar id
            val element = listOfString[i].split(",").toTypedArray()
            Log.i("userID", Arrays.toString(element))
            Log.i("elemnt[0}", element[0])
            if (ppeList != null) {
                for (entry in ppeList) {
                    // wenn id  gleich id vom google Calendar dann get element[1] dieser id, element[1]
                    // ist die GoogleCalendar Id für den gespeicherten eintrag
                    Log.i("userID2", entry?.id?:"-1")
                    if (entry?.id == element[0]) {
                        //output tag
                        val DEBUG_TAG = "MyActivity"
                        //eventID ist die Google calendar Id
                        val eventID = element[1].toLong()
                        //Klasse für das updaten von werten
                        val cr = context?.contentResolver
                        val values = ContentValues()
                        var updateUri: Uri
                        //Datum und Uhrzeit aufteilen.
                        // Sieht so aus wie 22-01-2019 10:00 Uhr
                        // es wird nach dem Leerzeichen getrennt
                        //trennen von datum und Uhrzeit
                        val s = entry.date?.split(" ")?.toTypedArray()
                        //print Datum
                        //aufteilen von tag, monat und jahr.
                        //sieht aus wie 22-01-2019 aufgeteilt in ss[0] =  22 ,ss[1] = 01, ss[2] = 2019
                        val ss = s?.get(0)?.split("-")?.toTypedArray()
                        //aufteilen von der Uhrzeit Stunden der prüfung und Minuten der prüfung
                        val time1 = s?.get(1)?.substring(0, 2)?.toInt()
                        val time2 = s?.get(1)?.substring(4, 5)?.toInt()
                        // The new title for the updatet event
                        values.put(CalendarContract.Events.TITLE, entry.module)
                        values.put(
                            CalendarContract.Events.EVENT_LOCATION,
                            "Fachhochschule Bielefeld Update"
                        )
                        values.put(CalendarContract.Events.DESCRIPTION, "")
                        //umwandeln von Datum und uhrzeit in GregorianCalender für eine leichtere weiterverarbeitung
                        val calDate = GregorianCalendar(
                            ss?.get(0)?.toInt()?:0,
                            ss?.get(1)?.toInt()?:1 - 1,
                            ss?.get(2)?.toInt()?:0,
                            time1?:0, time2?:0
                        )
                        values.put(CalendarContract.EXTRA_EVENT_ALL_DAY, false)
                        values.put(CalendarContract.Events.DTSTART, calDate.timeInMillis)
                        values.put(
                            CalendarContract.Events.DTEND,
                            calDate.timeInMillis + 90 * 60000
                        )
                        //uebergebeneModule.put(CalendarContract.Events.);
                        //Checkverbindung Eintrag
                        val baseUri = Uri.parse("content://com.android.calendar/events")
                        updateUri = ContentUris.withAppendedId(baseUri, eventID)
                        //variable zum anzeigen der geänderten werte
                        val rows = cr?.update(updateUri, values, null, null)

                        //testausgabe
                        Log.i(DEBUG_TAG, "Rows updated: 240 $rows")
                    }
                }
            }
        }
    }

    // Start Merlin Gürtler
    //Methode zum entfernen eines Kalendereintrages
    fun deleteEntry(pruefid: Int) {

        //Variablen
        pruefID = pruefid

        //Creating editor to store uebergebeneModule to shared preferences
        val mSharedPreferences = context?.getSharedPreferences("GoogleID-und-PruefID13", 0)
        val stringids = mSharedPreferences?.getString("IDs", "")

        // step one : converting comma separate String to array of String
        val elementList = stringids?.split("-")?.toTypedArray()

        // step two : convert String array to list of String
        val fixedLenghtList = elementList?.toMutableList()

        // step three : copy fixed list to an ArrayList
        val listOfString = ArrayList(fixedLenghtList)

        //step fifth : change Stringarray to String type
        Log.i("check_Googlekalender", listOfString.size.toString())
        //String idsTOstring = null;
        for (i in 1 until listOfString.size) {
            // step six : split Prüfid und GoogleID
            val element = listOfString[i].split(",").toTypedArray()
            if (element[0] == pruefID.toString()) {
                //output tag
                val DEBUG_TAG = "MyActivity"

                //getContentResolver wird benötigt zum zugriff auf die Kalender API
                val cr = context?.contentResolver
                var deleteUri: Uri? = null
                val eventID = element[1].toLong()
                val baseUri = Uri.parse("content://com.android.calendar/events")

                //wenn prüfid vorhanden lösche diese
                Log.i("check_Checkbool", "Pid stimmt überein")
                deleteUri = ContentUris.withAppendedId(baseUri, eventID)
                val rows = cr?.delete(deleteUri, null, null)
                Log.i(DEBUG_TAG, "Rows deleted: 240 $rows")
            }
        }
    }

    fun getFavoritePruefung(id: String?): TestPlanEntry? {
        val database2 = AppDatabase.getAppDatabase(context!!)
        // Erhalte die Prüfung die geupdated werden soll
        return database2?.userDao()?.getEntryById(id)
    }

    // Ende Merlin Gürtler
    fun databaseConnect(): List<TestPlanEntry?>? {
        val database2 = AppDatabase.getAppDatabase(context!!)
        // Änderun Merlin Gürtler
        // Nicht alle Einträge, um Iterationen zu sparen
        return database2?.userDao()?.getFavorites(true)
    }

    fun updateCalendarEntry(pruefid: Int) {
        //Variablen
        pruefID = pruefid

        //Creating editor to store uebergebeneModule to shared preferences
        val mSharedPreferences = context?.getSharedPreferences("GoogleID-und-PruefID13", Context.MODE_PRIVATE)
        val stringids = mSharedPreferences?.getString("IDs", "")

        // step one : converting comma separate String to array of String
        val elementList = stringids?.split("-")?.toTypedArray()

        // step two : convert String array to list of String
        val fixedLengthList = elementList?.toMutableList()

        // step three : copy fixed list to an ArrayList
        val listOfString = ArrayList(fixedLengthList)

        //step fifth : change Stringarray to String type
        Log.i("check_Googlekalender", listOfString.size.toString())
        //String idsTOstring = null;
        for (i in 1 until listOfString.size) {
            // step six : split Prüfid und GoogleID
            val element = listOfString[i].split(",").toTypedArray()
            if (element[0] == pruefID.toString()) {
                //output tag
                val DEBUG_TAG = "MyActivity"
                val eventID = element[1].toLong()
                val baseUri = Uri.parse("content://com.android.calendar/events")
                val values = ContentValues()
                val favoriteExam = getFavoritePruefung(pruefID.toString())

                //Datum und Uhrzeit aufteilen.
                // Sieht so aus wie 22-01-2019 10:00 Uhr
                // es wird nach dem Leerzeichen getrennt
                //trennen von datum und Uhrzeit
                val s = favoriteExam?.date?.split(" ")?.toTypedArray()
                //print Datum
                //aufteilen von tag, monat und jahr.
                //sieht aus wie 22-01-2019 aufgeteilt in ss[0] =  22 ,ss[1] = 01, ss[2] = 2019
                val ss = s?.get(0)?.split("-")?.toTypedArray()
                //aufteilen von der Uhrzeit Stunden der prüfung und Minuten der prüfung
                val time1 = s?.get(1)?.substring(0, 2)?.toInt()
                val time2 = s?.get(1)?.substring(4, 5)?.toInt()

                // The new title for the updatet event
                values.put(CalendarContract.Events.TITLE, favoriteExam?.module)
                values.put(
                    CalendarContract.Events.EVENT_LOCATION,
                    "Fachhochschule Bielefeld"
                )
                values.put(CalendarContract.Events.DESCRIPTION, "")
                //umwandeln von Datum und uhrzeit in GregorianCalender für eine leichtere weiterverarbeitung
                val calDate = GregorianCalendar(
                    ss?.get(0)?.toInt()?:0,
                    ss?.get(1)?.toInt()?:1 - 1,
                    ss?.get(2)?.toInt()?:0,
                    time1?:0, time2?:0
                )
                values.put(CalendarContract.EXTRA_EVENT_ALL_DAY, false)
                values.put(CalendarContract.Events.DTSTART, calDate.timeInMillis)
                values.put(
                    CalendarContract.Events.DTEND,
                    calDate.timeInMillis + 90 * 60000
                )

                //wenn prüfid vorhanden update das event
                Log.i("check_Checkbool", "Pid stimmt überein")
                val updateUri = ContentUris.withAppendedId(baseUri, eventID)
                val rows = context?.contentResolver?.update(
                    updateUri, values, null,
                    null
                )
                Log.i(DEBUG_TAG, "Rows updated: 240 $rows")
            }
        }
    }
}