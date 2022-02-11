package com.Fachhochschulebib.fhb.pruefungsplaner.model.retrofit

import android.content.Context
import com.Fachhochschulebib.fhb.pruefungsplaner.*
import com.Fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry
import com.google.gson.GsonBuilder
import retrofit2.converter.gson.GsonConverterFactory
import kotlinx.coroutines.*
import retrofit2.*
import retrofit2.http.GET
import retrofit2.http.Path
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

interface API{
    @GET("org.fh.ppv.entity.studiengang/")
    suspend fun getCourses():List<GSONCourse>


    @GET("org.fh.ppv.entity.pruefplaneintrag/{ppSemester}/{pTermin}/{pYear}/{pIds}/")
    suspend fun getEntries(
        @Path("ppSemester") ppSemetser:String,
        @Path("pTermin")pTermin:String,
        @Path("pYear")pYear:String,
        @Path("pIds")pIds:String):List<GSONEntry>
}

object RetrofitHelper{
    private val serverIp = "http://85.214.233.224:8080/"
    private val relativeUrl = "MeinPruefplan/resources/"
    private val baseUrl = serverIp + relativeUrl

    fun getInstance(): Retrofit {
        val gson = GsonBuilder().setLenient().create()
        return Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create(gson)).build()
    }
}

/**
 * Interface for a connection to the REST-API and the Room-Database. Data is exchanged via JSON-Objects.
 *
 * @author Alexander Lange (Email:alexander.lange@fh-bielefeld.de)
 * @since 1.6
 */
class RetrofitConnect(
    private val context: Context
) {
    // private boolean checkvalidate = false;
    /**
     * Return a formatted date as String to save in the [TestPlanEntry.date]-Paramater.
     *
     * @param[dateResponse] The date from the JSON-Response.
     *
     * @return The formatted date for the [TestPlanEntry.date]
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private fun getDate(dateResponse: String): String {
        //Festlegen vom Dateformat
        var dateTimeZone: String
        dateTimeZone = dateResponse.replaceFirst("CET".toRegex(), "")
        dateTimeZone = dateTimeZone.replaceFirst("CEST".toRegex(), "")
        var dateLastExamFormatted: String? = null
        try {
            val dateFormat: DateFormat = SimpleDateFormat(
                "EEE MMM dd HH:mm:ss yyyy", Locale.US//TODO CHANGE LOCAL
            )
            val dateLastExam = dateFormat.parse(dateTimeZone)
            val targetFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            dateLastExamFormatted = targetFormat.format(dateLastExam)
            val currentDate = Calendar.getInstance().time
            val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val currentDateFormated = df.format(currentDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return dateLastExamFormatted.toString()
    }
/*

    */
    /**
     * Return the ids of all choosen courses in the Room-Database.
     *
     * @param[roomData] The Room-Database of the application.
     *
     * @return A String containing every course-ID
     *
     * @author Alexander Lange
     * @since 1.6
     *//*

    private fun getIDs(): String {
        val Ids = viewModel.getChoosenCourseIds(true)
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
*/


    private fun createRequest(url: String): RequestInterface {
        val builder = Retrofit.Builder()
        builder.baseUrl(url)
        builder.addConverterFactory(GsonConverterFactory.create())
        val retrofit = builder.build()
        return retrofit.create(RequestInterface::class.java)
    }

//    suspend fun fetchCourses(): List<JsonCourse?>? {
//        var ret: List<JsonCourse?>? = null
//        val call = createRequest(courseUrl).studiengaenge
//        call?.enqueue(object : Callback<List<JsonCourse?>?> {
//            override fun onResponse(
//                call: Call<List<JsonCourse?>?>?,
//                response: Response<List<JsonCourse?>?>
//            ) {
//                if (response.isSuccessful) {
//                    ret = response.body()
//                } else {
//                    Log.d("RESPONSE", ":::NO RESPONSE:::")
//                }
//            }
//
//            override fun onFailure(call: Call<List<JsonCourse?>?>?, t: Throwable) {
//                Log.e("Error", t.message!!)
//            }
//        })
//        return ret
//    }
//
//
//    suspend fun fetchEntries():List<JsonResponse?>?{
//        var ret:List<JsonResponse?>? = null
//        val call = createRequest(entryUrl).jSON
//        call?.enqueue(object : Callback<List<JsonResponse?>?>{
//            override fun onResponse(
//                call: Call<List<JsonResponse?>?>,
//                response: Response<List<JsonResponse?>?>
//            ) {
//                if (response.isSuccessful) {
//                    ret = response.body()
//                } else {
//                    Log.d("RESPONSE", ":::NO RESPONSE:::")
//                }
//            }
//
//            override fun onFailure(call: Call<List<JsonResponse?>?>, t: Throwable) {
//                Log.e("Error", t.message!!)
//            }
//
//        })
//        return ret
//    }


//    // Start Merlin Gürtler
//    // Refactoring
//    /**
//     * Creates a new [TestPlanEntry] from a [JsonResponse].
//     *
//     * @param[entryResponse] The [JsonResponse], that contains the data for the [TestPlanEntry].
//     *
//     * @return A [TestPlanEntry] containing the data of the [JsonResponse]
//     *
//     * @author Alexander Lange
//     * @since 1.6
//     */
//    private fun createTestplanEntry(entryResponse: JsonResponse): TestPlanEntry {
//        val dateLastExamFormatted = getDate(entryResponse.date!!)
//        val testPlanEntry = TestPlanEntry()
//        testPlanEntry.firstExaminer = entryResponse.firstExaminer
//        testPlanEntry.secondExaminer = entryResponse.secondExaminer
//        testPlanEntry.date = dateLastExamFormatted
//        testPlanEntry.id = entryResponse.id?:"0"
//        testPlanEntry.course = entryResponse.courseName
//        testPlanEntry.module = entryResponse.module
//        testPlanEntry.semester = entryResponse.semester
//        testPlanEntry.termin = entryResponse.termin
//        testPlanEntry.room = entryResponse.room
//        testPlanEntry.examForm = entryResponse.form
//        testPlanEntry.status = entryResponse.status
//        testPlanEntry.hint = entryResponse.hint
//        testPlanEntry.color = entryResponse.color
//        return testPlanEntry
//    }

    //TODO Implement
    fun firstStart(){

    }
//
//    /**
//     * Update an existing [TestPlanEntry] with new data from a [JsonResponse].
//     *
//     * @param[entryResponse] The [JsonResponse] with the new data.
//     * @param[existingEntry] The [TestPlanEntry] that needs to be updated.
//     *
//     * @return The updated [TestPlanEntry]
//     *
//     * @author Alexander Lange
//     * @since 1.6
//     */
//    private fun updateTestPlanEntry(
//        entryResponse: JsonResponse,
//        existingEntry: TestPlanEntry?
//    ): TestPlanEntry? {
//        val dateLastExamFormatted = getDate(entryResponse.date!!)
//        existingEntry?.firstExaminer = entryResponse.firstExaminer
//        existingEntry?.secondExaminer = entryResponse.secondExaminer
//        existingEntry?.date = dateLastExamFormatted
//        existingEntry?.id = entryResponse.id?:"0"
//        existingEntry?.course = entryResponse.courseName
//        existingEntry?.module = entryResponse.module
//        existingEntry?.semester = entryResponse.semester
//        existingEntry?.termin = entryResponse.termin
//        existingEntry?.room = entryResponse.room
//        existingEntry?.examForm = entryResponse.form
//        existingEntry?.status = entryResponse.status
//        existingEntry?.hint = entryResponse.hint
//        existingEntry?.color = entryResponse.color
//        return existingEntry
//    }

    //TODO Implement
    fun setUserCourses() {
    }
//
//    /**
//     * Insert new [TestPlanEntry]-Objects into the Room-Database, based on a List of [JsonResponse].
//     *
//     * @param[roomData] The database, in which the [TestPlanEntry]-Objects shall be inserted.
//     * @param[year] The current year
//     * @param[examinePeriod] The current examperiod
//     * @param[body] A list of [JsonResponse], containing data to store in the Room-Database.
//     *
//     * @author Alexander Lange
//     * @since 1.6
//     */
//    private fun inserEntryToDatabase(
//        body: List<JsonResponse?>?
//    ) {
//        if (body == null) {
//            return
//        }
//        for (entryResponse in body) {
//            val testPlanEntry: TestPlanEntry? =
//                if (entryResponse != null) createTestplanEntry(entryResponse) else null
//            try {
//                for (b in Optionen.idList.indices) {
//                    if (testPlanEntry?.id == Optionen.idList[b]) {
//                        testPlanEntry.favorit = true
//                    }
//                }
//            } catch (e: Exception) {
//                Log.d(
//                    "Fehler RetrofitConnect",
//                    "Fehler beim Ermitteln der favorisierten Prüfungen"
//                )
//            }
//            //Schlüssel für die Erkennung bzw unterscheidung Festlegen
//            testPlanEntry?.validation =
//                viewModel.getExamineYear() + entryResponse?.courseId + viewModel.getCurrentPeriode()
//            addUser(viewModel, testPlanEntry)
//        }
//        // Ende Merlin Gürtler
//        checkTransmission = true
//    }
//
//    //DONE (08/2020 LG) Parameter 7,8 eingefügt --> Adresse an zentraler Stelle verwalten
//    /**
//     * Access to the Rest-Api, to get the data from the Server.
//     *
//     * @param[ctx] The current context of the application to retrieve information from the sharedpreferences.
//     * @param[roomData] The database, in witch the response is saved.
//     * @param[year] The current year.
//     * @param[currentPeriod] The current examperiod.
//     * @param[termin] The current termin.
//     * @param[serverAdress] The address of the server.
//     *
//     * @author Alexander Lange
//     * @since 1.6
//     */
//    fun RetrofitWebAccess() {
//        val urlfhb = viewModel.getServerIPAddress()
//        val courseIds = getIDs()
//        val currentPeriod = viewModel.getCurrentPeriode()
//        val termin = viewModel.getCurrentTermin()
//        val year = viewModel.getExamineYear()
//        if (currentPeriod == null || termin == null || year == null) {
//            return
//        }
//        //Uebergabe der Parameter an den relativen Server-Pfad
//        val relPathWithParameters = (viewModel.getServerRelUrlPath()
//                + "entity.pruefplaneintrag/"
//                + currentPeriod + "/"
//                + termin + "/"
//                + year + "/"
//                + courseIds + "/")
//        val url = urlfhb + relPathWithParameters
//        val builder = Retrofit.Builder()
//        builder.baseUrl(url)
//        builder.addConverterFactory(GsonConverterFactory.create())
//        val retrofit = builder.build()
//        val request = retrofit.create(RequestInterface::class.java)
//        val call = request.jSON
//        call?.enqueue(object : Callback<List<JsonResponse?>?> {
//            override fun onResponse(
//                call: Call<List<JsonResponse?>?>?,
//                response: Response<List<JsonResponse?>?>
//            ) {
//                response.body()
//                if (response.isSuccessful) {
//                    inserEntryToDatabase(response.body())
//                } //if(response.isSuccessful())
//                else {
//                    Log.d("RESPONSE", ":::NO RESPONSE:::")
//                }
//            }
//
//            override fun onFailure(call: Call<List<JsonResponse?>?>?, t: Throwable) {
//                Log.d("Error", t.message!!)
//            }
//        })
//    }
//
//    // Start Merlin Gürtler
//    /**
//     * Update the Room-Database with current data on the server.
//     *
//     * @param[ctx] The current context of the application to retrieve information from the sharedpreferences.
//     * @param[roomData] The database, in witch the response is saved.
//     * @param[year] The current year.
//     * @param[currentPeriod] The current examperiod.
//     * @param[termin] The current termin.
//     * @param[serverAdress] The address of the server.
//     *
//     * @author Alexander Lange
//     * @since 1.6
//     */
//    fun retroUpdate() {
//        //Serveradresse
//        val courseIds = getIDs()
//        val urlfhb = viewModel.getServerIPAddress()
//        val currentPeriod = viewModel.getCurrentPeriode()
//        val termin = viewModel.getCurrentTermin()
//        val year = viewModel.getExamineYear()
//        if (currentPeriod == null || termin == null || year == null) {
//            return
//        }
//        //uebergabe der parameter an die Adresse
//        val relPathWithParameters = (viewModel.getServerRelUrlPath()
//                + "entity.pruefplaneintrag/"
//                + currentPeriod + "/"
//                + termin + "/"
//                + year + "/"
//                + courseIds + "/")
//        val URL = urlfhb + relPathWithParameters
//        val retrofit = Retrofit.Builder()
//            .baseUrl(URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//        val request = retrofit.create(RequestInterface::class.java)
//        val call = request.jSON
//        call?.enqueue(object : Callback<List<JsonResponse?>?> {
//            override fun onResponse(
//                call: Call<List<JsonResponse?>?>?,
//                response: Response<List<JsonResponse?>?>
//            ) {
//                updateDatabase(response)
//            }
//
//            override fun onFailure(call: Call<List<JsonResponse?>?>, t: Throwable) {
//                Log.d("Error", t.message ?: "No Errormessage")
//            }
//        })
//    }
//
//    /**
//     * Updates the RoomDatabase with a list of [JsonResponse]-Objects.
//     *
//     * @param[response] A list of Entries that need to be updated.
//     * @param[roomData] The database to be updated.
//     * @param[ctx] The context of the application.
//     *
//     * @author Alexander Lange
//     * @since 1.6
//     */
//    private fun updateDatabase(response: Response<List<JsonResponse?>?>) {
//        response.body()
//        if (response.isSuccessful && response.body()?.size ?: 0 > 0) {
//            val dataListFromLocalDB = mutableListOf<TestPlanEntry>()
//            viewModel.getAllEntries()?.let { dataListFromLocalDB.addAll(it) }
//            var responseId: String
//            var i: Int
//            val listSize = dataListFromLocalDB?.size ?: 0
//            for (response in response.body()!!) {
//                responseId = response?.id ?: "-1"
//                var existingEntry: TestPlanEntry? = TestPlanEntry()
//                existingEntry = viewModel.getEntryById(responseId)
//                // prüfe ob der Eintrag schon existiert
//                if (existingEntry != null) {
//                    // entfernt den Eintrag, da die Daten geupdatet wurden
//                    i = 0
//                    while (i < listSize) {
//                        if (dataListFromLocalDB?.get(i).id == response?.id) {
//                            existingEntry = dataListFromLocalDB.get(i)
//                            dataListFromLocalDB.removeAt(i)
//                            existingEntry = updateTestPlanEntry(response!!, existingEntry)
//                            break
//                        }
//                        i++
//                    }
//                    existingEntry?.let { viewModel.updateEntry(it) }
//                } else {
//                    var testPlanEntryResponse = TestPlanEntry()
//                    testPlanEntryResponse = createTestplanEntry(response!!)
//                    viewModel.insertEntry(testPlanEntryResponse)
//                }
//            }
//
//            // lösche Einträge die nicht geupdatet wurden
//            viewModel.deleteEntries(dataListFromLocalDB)
//
//            viewModel.getFavorites(true)?.let {
//                GoogleCalendarIO.update(context, it)
//            }
//        } else {
//            Log.d("RESPONSE", ":::NO RESPONSE:::")
//        }
//    }
//
//    /**
//     * Initialization of the application on the first start.
//     *
//     * @param[ctx] The context of the application.
//     * @param[roomData] The Room-Database.
//     * @param[serverAdress] The address of the Web-Server.
//     *
//     * @author Alexander Lange
//     * @since 1.6
//     */
//    fun firstStart() {
//        //Serveradresse
//        val urlfhb = viewModel.getServerIPAddress()
//
//        // Erhalte die gewählte Fakultät aus den Shared Preferences
//        val faculty = viewModel.getReturnFaculty()
//
//        //uebergabe der parameter an die Adresse
//        val adresse = viewModel.getServerRelUrlPath() + "entity.user/firstStart/" +
//                faculty + "/"
//        val URL = urlfhb + adresse
//        val retrofit = Retrofit.Builder()
//            .baseUrl(URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//        val request = retrofit.create(RequestInterface::class.java)
//        val call = request.jsonUuid
//        call?.enqueue(object : Callback<JsonUuid?> {
//            override fun onResponse(call: Call<JsonUuid?>, response: Response<JsonUuid?>) {
//                if (response.isSuccessful) {
//                    response.body()?.uuid?.let { viewModel.insertUuid(it) }
//
//                    // sende die gewählten Kurse
//                    setUserCourses()
//                }
//            }
//
//            override fun onFailure(call: Call<JsonUuid?>, t: Throwable) {
//                Log.d("Error", t.message ?: "No Errormessage")
//            }
//        })
//    }
//
//    /**
//     * Called on every appstart except the first one.
//     *
//     * @param[ctx] The context of the application.
//     * @param[roomData] The Room-Database.
//     * @param[serverAdress] The address of the Web-Server.
//     *
//     * @author Alexander Lange
//     * @since 1.6
//     */
//    fun anotherStart() {
//        //Serveradresse
//        val urlfhb = viewModel.getServerIPAddress()
//        val uuid = viewModel.getUuid()
//
//        //uebergabe der parameter an die Adresse
//        val adress =
//            viewModel.getServerRelUrlPath() + "entity.user/anotherStart/" + uuid?.uuid + "/"
//        val URL = urlfhb + adress
//        val retrofit = Retrofit.Builder()
//            .baseUrl(URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//        val request = retrofit.create(RequestInterface::class.java)
//        val call = request.anotherStart()
//        call?.enqueue(object : Callback<Void?> {
//            override fun onResponse(call: Call<Void?>, response: Response<Void?>) {
//                if (response.isSuccessful) {
//                    Log.d("Success", "Success")
//                }
//            }
//
//            override fun onFailure(call: Call<Void?>, t: Throwable) {
//                Log.d("Error", t.message ?: "No Errormessage")
//            }
//        })
//    }
//
//    /**
//     * Send Feedback to the Webserver.
//     *
//     * @param[ctx] The context of the application.
//     * @param[roomData] The Room-Database.
//     * @param[serverAdress] The address of the Web-Server.
//     * @param[usability] The feedback-value for the usability of the app.
//     * @param[functions] The feedback-value for the functionality of the app.
//     * @param[stability] The feedback-value for the stability of the app.
//     * @param[text] The written feedbacktext of the user.
//     *
//     * @author Alexander Lange
//     * @since 1.6
//     */
//    fun sendFeedBack(
//        usability: Float,
//        functions: Float,
//        stability: Float,
//        text: String,
//    ) {
//        //Serveradresse
//        var text = text
//        val urlfhb = viewModel.getServerIPAddress()
//        val uuid = viewModel.getUuid()
//
//        // Falls kein Text eingegeben wurde
//        if (text.length < 1) {
//            text = context.getString(R.string.feedbackPlaceholder)
//        }
//
//        //uebergabe der parameter an die Adresse
//        val adress = (viewModel.getServerRelUrlPath() + "entity.feedback/sendFeedback/" + uuid?.uuid
//                + "/" + usability + "/" + functions + "/" + stability + "/" + text + "/")
//        val URL = urlfhb + adress
//        val retrofit = Retrofit.Builder()
//            .baseUrl(URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//        val request = retrofit.create(RequestInterface::class.java)
//        val call = request.sendFeedBack()
//        call?.enqueue(object : Callback<Void?> {
//            override fun onResponse(call: Call<Void?>, response: Response<Void?>) {
//                if (response.isSuccessful) {
//                    Log.d("Success", "Success")
//                }
//            }
//
//            override fun onFailure(call: Call<Void?>, t: Throwable) {
//                Log.d("Error", t.message ?: "No Errormessage")
//            }
//        })
//    }
//
//    /**
//     * Retrieve a list of courses from the Webserver and store them in the Room-Database.
//     *
//     * @param[ctx] The context of the application.
//     * @param[roomData] The Room-Database.
//     * @param[serverAdress] The address of the Web-Server.
//     *
//     * @author Alexander Lange
//     * @since 1.6
//     */
//    fun getCourses() {
//        //Serveradresse
//        val urlfhb = viewModel.getServerIPAddress()
//
//        //uebergabe der parameter an die Adresse
//        val adress = viewModel.getServerRelUrlPath() + "entity.studiengang/"
//        val URL = urlfhb + adress
//        val retrofit = Retrofit.Builder()
//            .baseUrl(URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//        val request = retrofit.create(RequestInterface::class.java)
//        val call = request.studiengaenge
//        call?.enqueue(object : Callback<List<JsonCourse?>?> {
//            override fun onResponse(
//                call: Call<List<JsonCourse?>?>?,
//                response: Response<List<JsonCourse?>?>
//            ) {
//                if (response.isSuccessful) {
//                    val insertCourses: MutableList<Courses> = ArrayList()
//                    for (course in response.body()!!) {
//                        val courseFromApi = Courses()
//                        courseFromApi.choosen = false
//                        courseFromApi.courseName = course?.courseName
//                        courseFromApi.facultyId = course?.fkfbid
//                        courseFromApi.sgid = course?.sgid ?: "-1"
//                        courseFromApi.sgid = course?.sgid ?: "-1"
//                        insertCourses.add(courseFromApi)
//                    }
//                    viewModel.insertCourses(insertCourses)
//                }
//            }
//
//            override fun onFailure(call: Call<List<JsonCourse?>?>, t: Throwable) {
//                Log.d("Error", t.message ?: "No Errormessage")
//            }
//        })
//    }
//
//    /**
//     * TODO Needs to be implemented
//     *
//     * @param[ctx] The context of the application.
//     * @param[roomData] The Room-Database.
//     * @param[serverAdress] The address of the Web-Server.
//     *
//     * @author Alexander Lange
//     * @since 1.6
//     */
//    fun setUserCourses() {
//        //Serveradresse
//
//        // erhalte die gewählten Studiengänge
//        val courseIds = getIDs()
//        val uuid = viewModel.getUuid()
//        val urlfhb = viewModel.getServerIPAddress()
//
//        //uebergabe der parameter an die Adresse
//        val adress = viewModel.getServerRelUrlPath() + "entity.usercourses/" +
//                courseIds + "/" + uuid?.uuid + "/"
//        val URL = urlfhb + adress
//        val retrofit = Retrofit.Builder()
//            .baseUrl(URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//        val request = retrofit.create(RequestInterface::class.java)
//        val call = request.sendCourses()
//        call?.enqueue(object : Callback<Void?> {
//            override fun onResponse(call: Call<Void?>, response: Response<Void?>) {
//                if (response.isSuccessful) {
//                    Log.d("Success", "Success")
//                }
//            }
//
//            override fun onFailure(call: Call<Void?>, t: Throwable) {
//                Log.d("Error", t.message ?: "No Errormessage")
//            }
//        })
//    }
//
//    /**
//     * Updates the Room-Database, retrieve all entries for given courses from the Webserver and insert the Unknown ones into the database.
//     *
//     * @param[ctx] The current context of the application to retrieve information from the sharedpreferences.
//     * @param[roomData] The database, in witch the response is saved.
//     * @param[year] The current year.
//     * @param[examinPeriod] The current examperiod.
//     * @param[termin] The current termin.
//     * @param[serverAdress] The address of the server.
//     * @param[courses] The courses, that shall be updated.
//     *
//     * @author Alexander Lange
//     * @since 1.6
//     */
//    fun UpdateUnkownCourses(
//        courses: String
//    ) {
//        val termin = viewModel.getCurrentTermin()
//        val urlfhb = viewModel.getServerIPAddress()
//        val examinPeriod = viewModel.getCurrentPeriode()
//        val year = viewModel.getExamineYear()
//
//        if (termin == null || examinPeriod == null || year == null) {
//            return
//        }
//
//        val relPathWithParameters = (viewModel.getServerRelUrlPath()
//                + "entity.pruefplaneintrag/"
//                + examinPeriod + "/"
//                + termin + "/"
//                + year + "/"
//                + courses + "/")
//        val URL = urlfhb + relPathWithParameters
//        val builder = Retrofit.Builder()
//        builder.baseUrl(URL)
//        builder.addConverterFactory(GsonConverterFactory.create())
//        val retrofit = builder.build()
//        val request = retrofit.create(RequestInterface::class.java)
//        val call = request.jSON
//        call?.enqueue(object : Callback<List<JsonResponse?>?> {
//            override fun onResponse(
//                call: Call<List<JsonResponse?>?>?,
//                response: Response<List<JsonResponse?>?>
//            ) {
//                response.body()
//                if (response.isSuccessful) {
//                    inserEntryToDatabase(response.body())
//                } //if(response.isSuccessful())
//                else {
//                    Log.d("RESPONSE", ":::NO RESPONSE:::")
//                }
//            }
//
//            override fun onFailure(call: Call<List<JsonResponse?>?>, t: Throwable) {
//                Log.d("Error", t.message ?: "No Errormessage")
//            }
//        })
//    }
//
//    companion object {
//        var checkTransmission = false
//
//        // Ende Merlin Gürtler
//        //DONE (08/2020) LG: Rückgabe des PPE wird nicht verwendet, deshalb gelöscht!
//        /**
//         * Insert a new [TestPlanEntry] into the Room-Database. Check if the entry is already present, and if not, insert it.
//         *
//         * @param[db] The Room-Database.
//         * @param[testPlanEntry] The entry that shall be inserted into the Database.
//         *
//         * @author Alexander Lange
//         * @since 1.6
//         */
//        fun addUser(viewModel: MainViewModel, testPlanEntry: TestPlanEntry?) {
//            val existingEntry = testPlanEntry?.id?.let { viewModel.getEntryById(it) }
//            // Merlin Gürtler fügt den Eintrag nur hinzu wenn er nicht vorhanden ist
//            // dies wird verwendet, da die Favoriten behalten werden sollen
//            // und um doppelte Eintrage zu verhindern
//            if (existingEntry == null) {
//                testPlanEntry?.let { viewModel.insertEntry(it) }
//            }
//        }
//    }
}