package com.Fachhochschulebib.fhb.pruefungsplaner

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.Fachhochschulebib.fhb.pruefungsplaner.data.*
import com.Fachhochschulebib.fhb.pruefungsplaner.model.GSONCourse
import com.Fachhochschulebib.fhb.pruefungsplaner.model.JsonResponse
import com.google.android.play.core.appupdate.testing.FakeAppUpdateManager
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.json.XML
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val context = application.applicationContext
    private val repository = DatabaseRepository(application)
    private val spRepository = SharedPreferencesRepository(application)
    private val sdf = SimpleDateFormat("dd/MM/yyyy")

    val liveEntryList = repository.getAllEntriesLiveData()

    val liveCourses = repository.getAllCoursesLiveData()

    val liveFavorits = repository.getAllFavoritsLiveData()

    val liveFaculties = repository.getAllFacultiesLiveData()

    var liveCoursesForFacultyId = MutableLiveData<List<Courses>?>()

    //Retrofit
    fun fetchCourses() {
        viewModelScope.launch {
            val courses = repository.fetchCourses()
            courses?.let { insertCoursesJSON(it) }
        }
    }

    /*fun fetchEntries():List<GSONEntry>?{
        var ret:List<GSONEntry>? = null
        viewModelScope.launch {
            ret = repository.fetchEntries().await()
        }
        return ret
    }*/

    //Room Database
    fun insertEntry(testPlanEntry: TestPlanEntry) {
        viewModelScope.launch {
            repository.insertEntry(testPlanEntry)
        }
    }

    fun insertEntryJSON(jsonResponse: JsonResponse) {
        insertEntry(createTestplanEntry(jsonResponse))
    }

    fun insertCourses(courses: List<Courses>) {
        viewModelScope.launch {
            repository.insertCourses(courses)
        }
    }

    fun insertCoursesJSON(jsonCourses: List<GSONCourse>) {
        val courseList: MutableList<Courses> = mutableListOf()
        jsonCourses.forEach { item ->
            courseList.add(createCourse(item))
        }
        insertCourses(courseList)
    }

    fun insertUuid(uuid: String) {
        viewModelScope.launch {
            repository.insertUuid(uuid)
        }
    }

    fun insertFaculty(faculty: Faculty) {
        viewModelScope.launch {
            repository.insertFaculty(faculty)
        }
    }

    fun updateEntry(testPlanEntry: TestPlanEntry) {
        viewModelScope.launch {
            repository.updateEntry(testPlanEntry)
        }
    }

    fun updateEntryFavorit(favorit: Boolean, id: Int) {
        viewModelScope.launch {
            repository.updateEntryFavorit(favorit, id)
        }
    }

    fun updateCourse(courseName: String, choosen: Boolean) {
        viewModelScope.launch {
            repository.updateCourse(courseName, choosen)
        }
    }

    fun updateCourse(course:Courses){
        course.choosen?.let { course.courseName?.let { it1 -> updateCourse(it1, it) } }
    }

    fun updateFaculty(faculty: Faculty) {
        viewModelScope.launch {
            repository.updateFaculty(faculty)
        }
    }

    fun deleteEntries(entries: List<TestPlanEntry>) {
        viewModelScope.launch {
            repository.deleteEntries(entries)
        }
    }

    fun deleteAllEntries() {
        viewModelScope.launch {
            repository.deleteAllEntries()
        }
    }

    fun deleteCourses(courses: List<Courses>) {
        viewModelScope.launch {
            repository.deleteCourses(courses)
        }
    }

    fun deleteAllCourses() {
        viewModelScope.launch {
            repository.deleteAllCourses()
        }
    }

    fun deleteFaculties(faculties: List<Faculty>) {
        viewModelScope.launch {
            repository.deleteFaculties(faculties)
        }
    }

    fun deleteAllFaculties() {
        viewModelScope.launch {
            repository.deleteAllFaculties()
        }
    }

    fun getAllEntries(): List<TestPlanEntry>? {
        var ret: List<TestPlanEntry>? = null
        viewModelScope.launch {
            ret = repository.getAllEntries()
        }
        return ret
    }

    fun getAllCourses(): List<Courses>? {
        var ret: List<Courses>? = null
        viewModelScope.launch {
            ret = repository.getAllCourses()
        }
        return ret
    }

    fun getFavorites(favorit: Boolean): List<TestPlanEntry>? {
        var ret: List<TestPlanEntry>? = null
        viewModelScope.launch {
            ret = repository.getFavorites(favorit)
        }
        return ret
    }

    fun getFirstExaminerSortedByName(selectedCourse: String): List<String>? {
        var ret: List<String>? = null
        viewModelScope.launch {
            repository.getFirstExaminerSortedByName(selectedCourse)
        }
        return ret
    }

    fun getModulesOrdered(): List<String>? {
        var ret: List<String>? = null
        viewModelScope.launch {
            ret = repository.getModulesOrdered()
        }
        return ret
    }

    fun getEntriesByCourseName(course: String, favorit: Boolean): List<TestPlanEntry>? {
        var ret: List<TestPlanEntry>? = null
        viewModelScope.launch {
            ret = repository.getEntriesByCourseName(course, favorit)
        }
        return ret
    }

    fun getChoosenCourses(choosen: Boolean): List<String>? {
        var ret: List<String>? = null
        viewModelScope.launch {
            ret = repository.getChoosenCourses(choosen)
        }
        return ret
    }

    fun getChoosenCourseIds(choosen: Boolean): List<String>? {
        var ret: List<String>? = null
        viewModelScope.launch {
            ret = repository.getChoosenCourseIds(choosen)
        }
        return ret
    }
    fun getCoursesByFaculty(faculty: Faculty) {
        getCoursesByFacultyid(faculty.fbid)
    }
    fun getCoursesByFacultyid(facultyId: String){
        viewModelScope.launch {
            val courses = repository.getAllCoursesByFacultyid(facultyId)
            liveCoursesForFacultyId.postValue(courses)
        }
    }

    fun getEntriesByCourseName(course: String): List<TestPlanEntry>? {
        var ret: List<TestPlanEntry>? = null
        viewModelScope.launch {
            ret = repository.getEntriesByCourseName(course)
        }
        return ret
    }

    fun getEntryById(id: String): TestPlanEntry? {
        var ret: TestPlanEntry? = null
        viewModelScope.launch {
            ret = repository.getEntryById(id)
        }
        return ret
    }

    fun getUuid(): Uuid? {
        var ret: Uuid? = null
        viewModelScope.launch {
            ret = repository.getUuid()
        }
        return ret
    }

    fun getCourseId(courseName: String): String? {
        var ret: String? = null
        viewModelScope.launch {
            ret = repository.getCourseId(courseName)
        }
        return ret
    }

    fun getTermin(): String? {
        var ret: String? = null
        viewModelScope.launch {
            ret = repository.getTermin()
        }
        return ret
    }

    fun getOneEntryByName(courseName: String, favorit: Boolean): TestPlanEntry? {
        var ret: TestPlanEntry? = null
        viewModelScope.launch {
            ret = repository.getOneEntryByName(courseName, favorit)
        }
        return ret
    }


    //Utilities
    /**
     * Creates a new [TestPlanEntry] from a [JsonResponse].
     *
     * @param[entryResponse] The [JsonResponse], that contains the data for the [TestPlanEntry].
     *
     * @return A [TestPlanEntry] containing the data of the [JsonResponse]
     *
     * @author Alexander Lange
     * @since 1.6
     */
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

    fun createCourse(jsonCourse: GSONCourse): Courses {
        val course = Courses()
        course.choosen = false
        course.courseName = jsonCourse.CourseName
        course.facultyId = jsonCourse.FKFBID
        course.sgid = jsonCourse.SGID
        return course
    }


    //Shared Preferences
    fun getSelectedCourse(): String? {
        return spRepository.getSelectedCourse()
    }

    fun setSelectedCourse(course: String) {
        spRepository.setSelectedCourse(course)
    }

    fun getReturnCourse(): String? {
        return spRepository.getReturnCourse()
    }

    fun setReturnCourse(course: String) {
        spRepository.setReturnCourse(course)
    }

    fun getReturnFaculty(): String? {
        return spRepository.getReturnFaculty()
    }

    fun setReturnFaculty(faculty: String) {
        spRepository.setReturnFaculty(faculty)
        getCoursesByFacultyid(faculty)
    }

    fun setReturnFaculty(faculty: Faculty) {
        setReturnFaculty(faculty.fbid)
    }

    fun getExamineYear(): String? {
        return spRepository.getExamineYear()
    }

    fun setExamineYear(year: String) {
        spRepository.setExamineYear(year)
    }

    fun getCurrentPeriode(): String? {
        return spRepository.getCurrentPeriode()
    }

    fun setCurrentPeriode(periode: String) {
        spRepository.setCurrentPeriode(periode)
    }

    fun getCurrentTermin(): String? {
        return spRepository.getCurrentTermin()
    }

    fun setCurrentTermin(termin: String) {
        spRepository.setCurrentTermin(termin)
    }

    //TODO Needed?
    fun getCurrentPeriodeString(): String? {
        return spRepository.getCurrentPeriodeString()
    }

    fun setCurrentPeriodeString(str: String) {
        spRepository.setCurrentPeriodeString(str)
    }

    fun getStartDateString(): String? {
        return spRepository.getStartDate()
    }


    fun getStartDate(): Date? {
        return getStartDateString()?.let { sdf.parse(it) }
    }

    fun setStartDate(date: String) {
        spRepository.setStartDate(date)
    }

    fun setStartDate(date: Date) {
        spRepository.setStartDate(sdf.format(date))
    }

    fun getEndDateString(): String? {
        return spRepository.getEndDate()
    }

    fun getEndDate(): Date? {
        return getEndDateString()?.let { sdf.parse(it) }
    }

    fun setEndDate(date: String) {
        spRepository.setEndDate(date)
    }

    fun setEndDate(date: Date) {
        spRepository.setEndDate(sdf.format(date))
    }

    //Returns false by default
    fun getChosenDarkmode(): Boolean {
        return spRepository.getChosenDarkmode()
    }

    fun setChosenDarkmode(darkmode: Boolean) {
        spRepository.setChosenDarkmode(darkmode)
    }

    //Returns -1 by default
    fun getChosenThemeId(): Int {
        return spRepository.getChosenThemeId()
    }

    fun setChosenThemeId(id: Int) {
        spRepository.setChosenThemeId(id)
    }

    fun getUpdateIntervalTimeHour(): Int {
        return spRepository.getUpdateIntervalTimeHour()
    }

    fun setUpdateIntervalTimeHour(hour: Int) {
        spRepository.setUpdateIntervalTimeHour(hour)
    }

    //Return 15 by default
    fun getUpdateIntervalTimeMinute(): Int {
        return spRepository.getUpdateIntervalTimeMinute()
    }

    fun setUpdateIntervalTimeMinute(minute: Int) {
        spRepository.setUpdateIntervalTimeMinute(minute)
    }

    fun getCalendarSync(): Boolean {
        return spRepository.getCalendarSync()
    }

    fun setCalendarSync(sync: Boolean) {
        spRepository.setCalendarSync(sync)
    }

    fun getServerIPAddress(): String? {
        return spRepository.getServerIPAddress()
    }

    fun setServerIPAddress(address: String) {
        spRepository.setServerIPAddress(address)
    }

    fun getServerRelUrlPath(): String? {
        return spRepository.getServerRelUrlPath()
    }

    fun setServerRelUrlPath(path: String) {
        spRepository.setServerRelUrlPath(path)
    }

    fun getFaculties(): String? {
        return spRepository.getFaculties()
    }

    fun setFaculties(faculties: String) {
        spRepository.setFaculties(faculties)
    }

    private fun createFaculty(json: JSONObject): Faculty {
        val ret = Faculty()
        ret.fbid = json.getString("fbid")
        ret.facultyName = json.getString("facName")
        ret.facultyShortname = json.getString("facShortName")
        return ret
    }


    fun fetchFaculties() {
        viewModelScope.launch {
            val faculties = repository.fetchFaculties()
            for (i in 0 until faculties.length()) {
                val faculty = createFaculty(faculties.getJSONObject(i))
                insertFaculty(faculty)
            }

            //Verbindungsaufbau zum Webserver
            /*

                val receivesFaculties = JSONArray()
                for (i in 0 until faculties.length()) {
                    val jsonObject = faculties.getJSONObject(i)
                    try {
                        val obj = jsonObject.get("faculty")
                        receivesFaculties.put(obj)
                    }catch (e:Exception){
                        continue
                    }
                }
                val convertedToString = receivesFaculties.toString()
                val deletedCling: String = convertedToString.substring(1, convertedToString.length - 1)
                //konvertieren zu JSONArray
                val jsonArrayFacultys = JSONArray(deletedCling)
                val ret = mutableListOf<String>()
                for (i in 0 until jsonArrayFacultys.length()) {
                    val json: JSONObject = jsonArrayFacultys.getJSONObject(i)
                    ret.add(json.get("facName").toString())
                }

                // Werte Speichern für die offline Verwendung
                try {
                    setFaculties(deletedCling)
                } catch (e: Exception) {
                    Log.d(
                        "Output checkFakultaet",
                        "Fehler: Parsen von Fakultaet"
                    )
                }

            }



            try {
                ret = updateFaculties()
            } catch (e: Exception) {
                Log.e("UrlError", e.stackTraceToString())
                val strFacultys = getFaculties()
                if (strFacultys != null) {
                    try {
                        val jsonArrayFacultys = JSONArray(strFacultys)
                        var i: Int = 0
                        while (i < jsonArrayFacultys.length()) {
                            val json: JSONObject = jsonArrayFacultys.getJSONObject(i)
                            ret.add(json.get("facName").toString())
                            i++
                        }
                    } catch (b: Exception) {
                        Log.d(
                            "uebergabeAnSpinner",
                            "Fehler beim Parsen des Fakultätsnamen."
                        )
                    }
                }
            }
            return ret*/
        }
    }

    /**
     * Updates the faculties. Get all faculties from the webserver and synchronize them withe the SharedPreferences.
     *
     * @param[address] The address of the server.
     *
     * @author Alexander Lange
     * @since 1.6
     *
     */
    private fun updateFaculties() {
        viewModelScope.launch {
            val result = StringBuilder()
            val url =
                URL("http://85.214.233.224:8080/MeinPruefplan/resources/org.fh.ppv.entity.faculty/")
            val urlConn: HttpURLConnection = url.openConnection() as HttpURLConnection
            urlConn.connectTimeout = 1000 * 10 // mTimeout is in seconds
            urlConn.connect()

            //Parsen von den  erhaltene Werte
            val inputStream: InputStream = BufferedInputStream(urlConn.inputStream)
            val reader = BufferedReader(InputStreamReader(inputStream))
            var line: String?
            while ((reader.readLine().also { line = it }) != null) {
                result.append(line)
            }

            //Erstellen von JSON
            var jsonObj: JSONObject? = null
            try {
                jsonObj = XML.toJSONObject(result.toString())
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            val x: Iterator<*> = jsonObj!!.keys()
            val jsonArray = JSONArray()
            while (x.hasNext()) {
                val key: String = x.next() as String
                jsonArray.put(jsonObj.get(key))
            }

            //Werte von JSONARRay in JSONObject konvertieren
            val receivesFaculties = JSONArray()
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                receivesFaculties.put(jsonObject.get("faculty"))
            }
            val convertedToString = receivesFaculties.toString()
            val deletedCling: String = convertedToString.substring(1, convertedToString.length - 1)
            //konvertieren zu JSONArray
            val jsonArrayFacultys = JSONArray(deletedCling)
            val ret = mutableListOf<String>()
            for (i in 0 until jsonArrayFacultys.length()) {
                val json: JSONObject = jsonArrayFacultys.getJSONObject(i)
                ret.add(json.get("facName").toString())
            }

            // Werte Speichern für die offline Verwendung
            try {
                setFaculties(deletedCling)
            } catch (e: Exception) {
                Log.d(
                    "Output checkFakultaet",
                    "Fehler: Parsen von Fakultaet"
                )
            }
            Log.d("Output checkFakultaet", "abgeschlossen")
        }
    }
}