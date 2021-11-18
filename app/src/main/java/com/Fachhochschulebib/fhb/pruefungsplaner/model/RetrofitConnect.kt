//////////////////////////////
// RetrofitConnect
//
// autor:
// inhalt: Verbindungsaufbau zum Webserver
// zugriffsdatum: 11.12.19
//
//
//////////////////////////////
package com.Fachhochschulebib.fhb.pruefungsplaner.model

import android.content.Context
import com.Fachhochschulebib.fhb.pruefungsplaner.data.AppDatabase
import com.Fachhochschulebib.fhb.pruefungsplaner.model.JsonResponse
import com.Fachhochschulebib.fhb.pruefungsplaner.data.TestPlanEntry
import com.Fachhochschulebib.fhb.pruefungsplaner.Optionen
import com.Fachhochschulebib.fhb.pruefungsplaner.model.RetrofitConnect
import android.content.SharedPreferences
import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.Fachhochschulebib.fhb.pruefungsplaner.RequestInterface
import com.Fachhochschulebib.fhb.pruefungsplaner.CheckGoogleCalendar
import com.Fachhochschulebib.fhb.pruefungsplaner.model.JsonUuid
import com.Fachhochschulebib.fhb.pruefungsplaner.data.Uuid
import com.Fachhochschulebib.fhb.pruefungsplaner.R
import com.Fachhochschulebib.fhb.pruefungsplaner.model.JsonCourse
import com.Fachhochschulebib.fhb.pruefungsplaner.data.Courses
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class RetrofitConnect(private val relativePPlanUrl: String) {
    var termine: String? = null

    // private boolean checkvalidate = false;
    private fun getDate(dateResponse: String): String {
        //Festlegen vom Dateformat
        var dateTimeZone: String
        dateTimeZone = dateResponse.replaceFirst("CET".toRegex(), "")
        dateTimeZone = dateTimeZone.replaceFirst("CEST".toRegex(), "")
        var dateLastExamFormatted: String? = null
        try {
            val dateFormat: DateFormat = SimpleDateFormat(
                "EEE MMM dd HH:mm:ss yyyy", Locale.US
            )
            val dateLastExam = dateFormat.parse(dateTimeZone)
            val targetFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            dateLastExamFormatted = targetFormat.format(dateLastExam)
            val currentDate = Calendar.getInstance().time
            val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val currentDateFormated = df.format(currentDate)
            Log.d("Datum letzte Prüfung", dateLastExamFormatted)
            Log.d("Datum aktuell", currentDateFormated)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return dateLastExamFormatted.toString()
    }

    private fun getIDs(roomData: AppDatabase): String {
        val Ids = roomData.userDao()?.getChoosenCourseId(true)
        val courseIds = JSONArray()
        if (Ids != null) {
            for (id in Ids) {
                try {
                    val idJson = JSONObject()
                    idJson.put("ID", id)
                    courseIds.put(idJson)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }
        return courseIds.toString()
    }

    // Start Merlin Gürtler
    // Refactoring
    private fun createTestplanEntry(entryResponse: JsonResponse): TestPlanEntry {
        val dateLastExamFormatted = getDate(entryResponse.date!!)
        val testPlanEntry = TestPlanEntry()
        testPlanEntry.firstExaminer = entryResponse.firstExaminer
        testPlanEntry.secondExaminer = entryResponse.secondExaminer
        testPlanEntry.date = dateLastExamFormatted
        testPlanEntry.id = entryResponse.id
        testPlanEntry.course = entryResponse.courseName
        testPlanEntry.module = entryResponse.module
        testPlanEntry.semester = entryResponse.semester
        testPlanEntry.termin = entryResponse.termin
        testPlanEntry.room = entryResponse.room
        testPlanEntry.examForm = entryResponse.form
        testPlanEntry.status = entryResponse.status
        testPlanEntry.hint = entryResponse.hint
        testPlanEntry.color = entryResponse.color
        return testPlanEntry
    }

    private fun updateTestPlanEntry(
        entryResponse: JsonResponse,
        existingEntry: TestPlanEntry?
    ): TestPlanEntry? {
        val dateLastExamFormatted = getDate(entryResponse.date!!)
        existingEntry?.firstExaminer = entryResponse.firstExaminer
        existingEntry?.secondExaminer = entryResponse.secondExaminer
        existingEntry?.date = dateLastExamFormatted
        existingEntry?.id = entryResponse.id
        existingEntry?.course = entryResponse.courseName
        existingEntry?.module = entryResponse.module
        existingEntry?.semester = entryResponse.semester
        existingEntry?.termin = entryResponse.termin
        existingEntry?.room = entryResponse.room
        existingEntry?.examForm = entryResponse.form
        existingEntry?.status = entryResponse.status
        existingEntry?.hint = entryResponse.hint
        existingEntry?.color = entryResponse.color
        return existingEntry
    }

    private fun inserEntryToDatabase(
        roomData: AppDatabase,
        year: String,
        examinePeriod: String,
        body: List<JsonResponse?>?
    ) {
        // Start Merlin Gürtler
        // Extra Thread da sonst die Db nicht aktualisiert werden kann.
        //TODO Make Coroutine
        Thread {
            //Hole alle Einträge aus der lokalen Room-DB
            var dataListFromLocalDB: List<TestPlanEntry?>? = null
            try { //DONE (08/2020) LG
                dataListFromLocalDB = roomData.userDao()?.allEntries
                //roomdaten.clearAllTables();
            } catch (e: Exception) {
                Log.d("Fehler: ", "Kein Zugriff auf die Datenbank!")
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
            if (body != null) {
                for (entryResponse in body) {
                    //Pruefplan ist die Modelklasse für die angekommenden Prüfungsobjekte
                    /*
                                    DS, die bisher noch nicht in der lokalen DB enthalten sind, werden
                                    jetzt hinzugefügt.
                                 */
                    // if(!checkvalidate){
                    //erhaltene Werte zur Datenbank hinzufügen

                    var testPlanEntry:TestPlanEntry? = if(entryResponse!=null)createTestplanEntry(entryResponse)else null

                    //lokale datenbank initialiseren
                    //DONE (08/2020) LG: Auskommentieren des erneuten Zugriffs
                    //AppDatabase database2 = AppDatabase.getAppDatabase(ctx2);
                    //List<PruefplanEintrag> userdaten2 = database2.userDao().getAllEntries();
                    //Log.d("Test4", String.valueOf(userdaten2.size()));
                    try {
                        for (b in Optionen.idList.indices) {
                            if (testPlanEntry?.id == Optionen.idList[b]) {
                                //Log.d("Test4", String.valueOf(userdaten2.get(b).getID()));
                                testPlanEntry?.favorit = true
                            }
                        }
                    } catch (e: Exception) {
                        Log.d(
                            "Fehler RetrofitConnect",
                            "Fehler beim Ermitteln der favorisierten Prüfungen"
                        )
                    }

                    //Schlüssel für die Erkennung bzw unterscheidung Festlegen
                    testPlanEntry?.validation = year + entryResponse?.courseId + examinePeriod
                    addUser(roomData, testPlanEntry)
                }
            }
        }.start()
        // Ende Merlin Gürtler
        checkTransmission = true
    }

    //DONE (08/2020 LG) Parameter 7,8 eingefügt --> Adresse an zentraler Stelle verwalten
    fun RetrofitWebAccess(
        ctx: Context,
        roomData: AppDatabase,
        year: String,
        currentPeriod: String,
        termin: String,
        serverAdress: String
    ) {
        //Serveradresse
        val mSharedPreferencesAdresse = ctx.getSharedPreferences("Server_Address", 0)
        termine = termin
        val urlfhb = mSharedPreferencesAdresse.getString("ServerIPAddress", serverAdress)
        val courseIds = getIDs(roomData)

        //Uebergabe der Parameter an den relativen Server-Pfad
        val relPathWithParameters = (relativePPlanUrl
                + "entity.pruefplaneintrag/"
                + currentPeriod + "/"
                + termin + "/"
                + year + "/"
                + courseIds + "/")
        val URL = urlfhb + relPathWithParameters
        val builder = Retrofit.Builder()
        builder.baseUrl(URL)
        builder.addConverterFactory(GsonConverterFactory.create())
        val retrofit = builder.build()
        val request = retrofit.create(RequestInterface::class.java)
        val call = request.jSON
        call?.enqueue(object : Callback<List<JsonResponse?>?> {
            override fun onResponse(
                call: Call<List<JsonResponse?>?>?,
                response: Response<List<JsonResponse?>?>
            ) {
                response.body()
                if (response.isSuccessful) {
                    inserEntryToDatabase(roomData, year, currentPeriod, response.body())
                } //if(response.isSuccessful())
                else {
                    Log.d("RESPONSE", ":::NO RESPONSE:::")
                }
            }

            override fun onFailure(call: Call<List<JsonResponse?>?>?, t: Throwable) {
                Log.d("Error", t.message!!)
            }
        })
    }

    // Start Merlin Gürtler
    fun retroUpdate(
        ctx: Context, roomData: AppDatabase,
        year: String,
        currentPeriod: String,
        termin: String,
        serverAdress: String?
    ) {
        //Serveradresse
        val mSharedPreferencesAdresse = ctx.getSharedPreferences("Server-Adresse", 0)
        val courseIds = getIDs(roomData)
        val urlfhb = mSharedPreferencesAdresse.getString("ServerIPAddress", serverAdress)
        //uebergabe der parameter an die Adresse
        val relPathWithParameters = (relativePPlanUrl
                + "entity.pruefplaneintrag/"
                + currentPeriod + "/"
                + termin + "/"
                + year + "/"
                + courseIds + "/")
        val URL = urlfhb + relPathWithParameters
        val retrofit = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val request = retrofit.create(RequestInterface::class.java)
        val call = request.jSON
        call?.enqueue(object : Callback<List<JsonResponse?>?> {
            override fun onResponse(
                call: Call<List<JsonResponse?>?>?,
                response: Response<List<JsonResponse?>?>
            ) {
                response.body()
                if (response.isSuccessful && response.body()?.size?:0 > 0) {
                    Thread {
                        val dataListFromLocalDB = roomData.userDao()?.allEntries
                        var responseId: String
                        var i: Int
                        val listSize = dataListFromLocalDB?.size?:0
                        for (response in response.body()!!) {
                            responseId = response?.id?:"-1"
                            var existingEntry: TestPlanEntry? = TestPlanEntry()
                            existingEntry = roomData.userDao()?.getEntryById(responseId)
                            // prüfe ob der Eintrag schon existiert
                            if (existingEntry != null) {
                                // entfernt den Eintrag, da die Daten geupdatet wurden
                                i = 0
                                while (i < listSize) {
                                    if (dataListFromLocalDB?.get(i)?.id == response?.id) {
                                        existingEntry = dataListFromLocalDB?.get(i)
                                        dataListFromLocalDB?.removeAt(i)
                                        existingEntry = updateTestPlanEntry(response!!, existingEntry)
                                        break
                                    }
                                    i++
                                }
                                roomData.userDao()?.updateExam(existingEntry)
                            } else {
                                var testPlanEntryResponse = TestPlanEntry()
                                testPlanEntryResponse = createTestplanEntry(response!!)
                                roomData.userDao()?.insertAll(testPlanEntryResponse)
                            }

                            //Update den Eintrag aus dem Calendar falls vorhanden
                            val cal = CheckGoogleCalendar()
                            cal.setCtx(ctx)
                            if (!cal.checkCal(responseId.toInt())) {
                                cal.updateCalendarEntry(responseId.toInt())
                            }
                        }

                        // lösche Einträge die nicht geupdatet wurden
                        roomData.userDao()?.deleteEntry(dataListFromLocalDB)
                    }.start()
                } else {
                    Log.d("RESPONSE", ":::NO RESPONSE:::")
                }
            }

            override fun onFailure(call: Call<List<JsonResponse?>?>, t: Throwable) {
                Log.d("Error", t.message?:"No Errormessage")
            }
        })
    }

    fun firstStart(
        ctx: Context, roomData: AppDatabase,
        serverAdress: String?
    ) {
        //Serveradresse
        val mSharedPreferencesAdresse = ctx.getSharedPreferences("Server-Adresse", 0)
        val urlfhb = mSharedPreferencesAdresse.getString("ServerIPAddress", serverAdress)

        // Erhalte die gewählte Fakultät aus den Shared Preferences
        val sahredPreferencesFacultyValidation = ctx.getSharedPreferences("validation", 0)
        val faculty = sahredPreferencesFacultyValidation.getString("returnFaculty", "0")

        //uebergabe der parameter an die Adresse
        val adresse = relativePPlanUrl + "entity.user/firstStart/" +
                faculty + "/"
        val URL = urlfhb + adresse
        val retrofit = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val request = retrofit.create(RequestInterface::class.java)
        val call = request.jsonUuid
        call?.enqueue(object : Callback<JsonUuid?> {
            override fun onResponse(call: Call<JsonUuid?>, response: Response<JsonUuid?>) {
                if (response.isSuccessful) {
                    //TODO Make Coroutine
                    Thread { // Speichere die erhaltene Uuid
                        roomData.userDao()?.insertUuid(response.body()?.uuid)

                        // sende die gewählten Kurse
                        setUserCourses(ctx, roomData, serverAdress)
                    }.start()
                }
            }

            override fun onFailure(call: Call<JsonUuid?>, t: Throwable) {
                Log.d("Error", t.message?:"No Errormessage")
            }
        })
    }

    fun anotherStart(
        ctx: Context, roomdaten: AppDatabase,
        serverAdress: String?
    ) {
        //Serveradresse
        val mSharedPreferencesAdresse = ctx.getSharedPreferences("Server-Adresse", 0)
        val urlfhb = mSharedPreferencesAdresse.getString("ServerIPAddress", serverAdress)
        val uuid = roomdaten.userDao()?.uuid

        //uebergabe der parameter an die Adresse
        val adress = relativePPlanUrl + "entity.user/anotherStart/" + uuid?.uuid + "/"
        val URL = urlfhb + adress
        val retrofit = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val request = retrofit.create(RequestInterface::class.java)
        val call = request.anotherStart()
        call?.enqueue(object : Callback<Void?> {
            override fun onResponse(call: Call<Void?>, response: Response<Void?>) {
                if (response.isSuccessful) {
                    Log.d("Success", "Success")
                }
            }

            override fun onFailure(call: Call<Void?>, t: Throwable) {
                Log.d("Error", t.message?:"No Errormessage")
            }
        })
    }

    fun sendFeedBack(
        ctx: Context, roomdaten: AppDatabase,
        serverAdress: String?, usability: Float,
        functions: Float, stability: Float, text: String
    ) {
        //Serveradresse
        var text = text
        val mSharedPreferencesAdresse = ctx.getSharedPreferences("Server-Adresse", 0)
        val urlfhb = mSharedPreferencesAdresse.getString("ServerIPAddress", serverAdress)
        val uuid = roomdaten.userDao()?.uuid

        // Falls kein Text eingegeben wurde
        if (text.length < 1) {
            text = ctx.getString(R.string.feedbackPlaceholder)
        }

        //uebergabe der parameter an die Adresse
        val adress = (relativePPlanUrl + "entity.feedback/sendFeedback/" + uuid?.uuid
                + "/" + usability + "/" + functions + "/" + stability + "/" + text + "/")
        val URL = urlfhb + adress
        val retrofit = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val request = retrofit.create(RequestInterface::class.java)
        val call = request.sendFeedBack()
        call?.enqueue(object : Callback<Void?> {
            override fun onResponse(call: Call<Void?>, response: Response<Void?>) {
                if (response.isSuccessful) {
                    Log.d("Success", "Success")
                }
            }

            override fun onFailure(call: Call<Void?>, t: Throwable) {
                Log.d("Error", t.message?:"No Errormessage")
            }
        })
    }

    fun getCourses(
        ctx: Context, roomData: AppDatabase,
        serverAdress: String?
    ) {
        //Serveradresse
        val mSharedPreferencesAdresse = ctx.getSharedPreferences("Server-Adresse", 0)
        val urlfhb = mSharedPreferencesAdresse.getString("ServerIPAddress", serverAdress)

        //uebergabe der parameter an die Adresse
        val adress = relativePPlanUrl + "entity.studiengang/"
        val URL = urlfhb + adress
        val retrofit = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val request = retrofit.create(RequestInterface::class.java)
        val call = request.studiengaenge
        call?.enqueue(object : Callback<List<JsonCourse?>?> {
            override fun onResponse(
                call: Call<List<JsonCourse?>?>?,
                response: Response<List<JsonCourse?>?>
            ) {
                if (response.isSuccessful) {
                    //TODO Make Coroutine
                    Thread { // füge die Einträge der db hinzu
                        val insertCourses: MutableList<Courses> = ArrayList()
                        for (course in response.body()!!) {
                            val courseFromApi = Courses()
                            courseFromApi.choosen = false
                            courseFromApi.courseName = course?.courseName
                            courseFromApi.facultyId = course?.fkfbid
                            courseFromApi.sgid = course?.sgid?:"-1"
                            courseFromApi.sgid = course?.sgid?:"-1"
                            insertCourses.add(courseFromApi)
                        }
                        roomData.userDao()?.insertCourse(insertCourses)
                    }.start()
                }
            }

            override fun onFailure(call: Call<List<JsonCourse?>?>, t: Throwable) {
                Log.d("Error", t.message?:"No Errormessage")
            }
        })
    }

    fun setUserCourses(
        ctx: Context, roomData: AppDatabase,
        serverAdress: String?
    ) {
        //Serveradresse
        val mSharedPreferencesAdresse = ctx.getSharedPreferences("Server-Adresse", 0)

        // erhalte die gewählten Studiengänge
        val courseIds = getIDs(roomData)
        val uuid = roomData.userDao()?.uuid
        val urlfhb = mSharedPreferencesAdresse.getString("ServerIPAddress", serverAdress)

        //uebergabe der parameter an die Adresse
        val adress = relativePPlanUrl + "entity.usercourses/" +
                courseIds + "/" + uuid?.uuid + "/"
        val URL = urlfhb + adress
        val retrofit = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val request = retrofit.create(RequestInterface::class.java)
        val call = request.sendCourses()
        call?.enqueue(object : Callback<Void?> {
            override fun onResponse(call: Call<Void?>, response: Response<Void?>) {
                if (response.isSuccessful) {
                    Log.d("Success", "Success")
                }
            }

            override fun onFailure(call: Call<Void?>, t: Throwable) {
                Log.d("Error", t.message?:"No Errormessage")
            }
        })
    }

    fun UpdateUnkownCourses(
        ctx: Context,
        roomData: AppDatabase,
        year: String,
        examinPeriod: String,
        termin: String,
        serverAdress: String,
        courses: String
    ) {
        //Serveradresse
        val mSharedPreferencesAdresse = ctx.getSharedPreferences("Server_Address", 0)
        termine = termin
        val urlfhb = mSharedPreferencesAdresse.getString("ServerIPAddress", serverAdress)

        //Uebergabe der Parameter an den relativen Server-Pfad
        val relPathWithParameters = (relativePPlanUrl
                + "entity.pruefplaneintrag/"
                + examinPeriod + "/"
                + termin + "/"
                + year + "/"
                + courses + "/")
        val URL = urlfhb + relPathWithParameters
        val builder = Retrofit.Builder()
        builder.baseUrl(URL)
        builder.addConverterFactory(GsonConverterFactory.create())
        val retrofit = builder.build()
        val request = retrofit.create(RequestInterface::class.java)
        val call = request.jSON
        call?.enqueue(object : Callback<List<JsonResponse?>?> {
            override fun onResponse(
                call: Call<List<JsonResponse?>?>?,
                response: Response<List<JsonResponse?>?>
            ) {
                response.body()
                if (response.isSuccessful) {
                    inserEntryToDatabase(roomData, year, examinPeriod, response.body()!!)
                } //if(response.isSuccessful())
                else {
                    Log.d("RESPONSE", ":::NO RESPONSE:::")
                }
            }

            override fun onFailure(call: Call<List<JsonResponse?>?>, t: Throwable) {
                Log.d("Error", t.message?:"No Errormessage")
            }
        })
    }

    companion object {
        var checkTransmission = false

        // Ende Merlin Gürtler
        //DONE (08/2020) LG: Rückgabe des PPE wird nicht verwendet, deshalb gelöscht!
        fun addUser(db: AppDatabase, testPlanEntry: TestPlanEntry?) {
            val existingEntry = db.userDao()?.getEntryById(testPlanEntry?.id)
            // Merlin Gürtler fügt den Eintrag nur hinzu wenn er nicht vorhanden ist
            // dies wird verwendet, da die Favoriten behalten werden sollen
            // und um doppelte Eintrage zu verhindern
            if (existingEntry == null) {
                db.userDao()?.insertAll(testPlanEntry)
            }
        }
    }
}