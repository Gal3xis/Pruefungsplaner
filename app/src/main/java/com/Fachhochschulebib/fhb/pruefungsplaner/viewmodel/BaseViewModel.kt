package com.Fachhochschulebib.fhb.pruefungsplaner.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.Fachhochschulebib.fhb.pruefungsplaner.model.repositories.SharedPreferencesRepository
import com.Fachhochschulebib.fhb.pruefungsplaner.model.*
import com.Fachhochschulebib.fhb.pruefungsplaner.model.repositories.DatabaseRepository
import com.Fachhochschulebib.fhb.pruefungsplaner.model.retrofit.GSONCourse
import com.Fachhochschulebib.fhb.pruefungsplaner.model.retrofit.GSONEntry
import com.Fachhochschulebib.fhb.pruefungsplaner.model.room.Course
import com.Fachhochschulebib.fhb.pruefungsplaner.model.room.Faculty
import com.Fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry
import com.Fachhochschulebib.fhb.pruefungsplaner.model.room.Uuid
import com.Fachhochschulebib.fhb.pruefungsplaner.utils.GoogleCalendarIO
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Viewmodel that contains the logic of the Application. Implements a more specific access to the
 * Database-Repository and includes the sharedPreferencesRepository. Depper functionalities
 * for different Fragments and Activities are stored in inheriting classes.
 * **See Also:**[LiveData](https://developer.android.com/codelabs/basic-android-kotlin-training-livedata#2)
 */
open class BaseViewModel(application: Application) : AndroidViewModel(application) {

    protected val context = application.applicationContext
    protected val repository = DatabaseRepository(application)
    protected val spRepository = SharedPreferencesRepository(application)
    protected val sdfRetrofit = SimpleDateFormat("dd/MM/yyyy")
    protected val sdfDisplay = SimpleDateFormat("dd.MM.yyyy")

    protected val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        Log.d("CoroutineException", exception.stackTraceToString())
    }

    init {
        initServer()
        checkSustainability()
        updatePruefperiode()
    }


    //Remote-Access
    fun fetchCourses() {
        viewModelScope.launch(coroutineExceptionHandler) {
            val courses = repository.fetchCourses()
            courses?.let { insertCoursesJSON(it) }
        }
    }

    fun fetchEntries() {
        viewModelScope.launch(coroutineExceptionHandler) {
            val periode = getCurrentPeriode()
            val termin = getCurrentTermin()
            val examinYear = getExamineYear()
            val ids = getIDs()
            if (periode == null || termin == null || examinYear == null || ids.isNullOrEmpty()) {
                return@launch
            }
            val entries = repository.fetchEntries(periode, termin, examinYear, ids)
            entries?.forEach {
                insertEntryJSON(it)
            }
        }
    }

    fun fetchFaculties() {
        viewModelScope.launch(coroutineExceptionHandler) {
            try {
                val faculties = repository.fetchFaculties()
                if (faculties != null) {
                    for (i in 0 until faculties.length()) {
                        val faculty = createFaculty(faculties.getJSONObject(i))
                        insertFaculty(faculty)
                    }
                }
            } catch (e: Exception) {
                Log.d("BaseViewModel:fetchFaculties()", e.stackTraceToString())
            }

        }
    }

    fun updateDatabase() {
        fetchFaculties()
        fetchCourses()
        fetchEntries()
    }

    /**
     * Return the ids of all choosen courses in the Room-Database.
     *
     * @param[roomData] The Room-Database of the application.
     *
     * @return A String containing every course-ID
     *
     * @author Alexander Lange
     * @since 1.5
     */
    suspend fun getIDs(): String {
        val Ids = repository.getChoosenCourseIds(true)
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

    /**
     * Checks for a new exam period and updates the exam-data if necessary.
     *
     * @since 1.6
     * @author Alexander Lange (E-Mail:alexander.lange@fh-bielefeld.de)
     */
    fun updatePruefperiode() {
        viewModelScope.launch(coroutineExceptionHandler) {
            val pruefperiodenObjects = repository.fetchExamPeriods() ?: return@launch
            val currentPeriod= GetCurrentPeriode(pruefperiodenObjects)?:return@launch
            val numberOfWeeks = currentPeriod["PPWochen"].toString().toInt()

            //Set start-and enddate
            val formatter = SimpleDateFormat("yyyy-MM-dd")
            var startDateString = currentPeriod["startDatum"].toString()
            startDateString = startDateString.substring(0, 10)
            val startDate = Calendar.getInstance()
            startDate.time = formatter.parse(startDateString)

            val endDate = Calendar.getInstance()
            endDate.time = startDate.time
            endDate.add(Calendar.DATE,numberOfWeeks*7-2)

            //Delete all entries if a new plan is available
            if(startDate.after(getEndDate())){
                deleteAllEntries()
            }

            setStartDate(startDate.time)
            setEndDate(endDate.time)
            fetchEntries()
//
//            currentPeriod.get("pruefTermin").toString()
//                ?.let { setCurrentTermin(it) }
//            currentPeriod?.get("PPJahr").toString()?.let { setExamineYear(it) }
//            currentPeriod?.get("pruefSemester")?.toString()
//                ?.let { setCurrentPeriode(it) }
//            examineDate?.let { setStartDate(it) }
//            lastDayPp?.let { setEndDate(it) }
        }
    }

    private fun GetCurrentPeriode(pruefperiodenObjects: JSONArray): JSONObject? {
        val formatter = SimpleDateFormat("yyyy-MM-dd")

        var latestDate:Date? =null
        var latestPeriode:JSONObject? = null
        val facultyId = getReturnFaculty()

        for (i in 0 until pruefperiodenObjects.length()) {
            //Get single object
            val obj = pruefperiodenObjects.getJSONObject(i)

            //Conitune if the faculitids does not match
            val facultyIdDB = obj.getJSONObject("fbFbid")["fbid"].toString()
            if(facultyIdDB!=facultyId){
                continue
            }

            //Get Startdate
            var startDateString = obj["startDatum"].toString()
            startDateString = startDateString.substring(0, 10)
            val startDate = formatter.parse(startDateString)

            //If no latest date is set, initialize it and continue the loop
            if(latestDate==null){
                latestDate = startDate
                latestPeriode = obj
                continue
            }

            if(startDate.after(latestDate)){
                latestDate = startDate
                latestPeriode = obj
            }

//            val examineWeek = obj["PPWochen"].toString().toInt()
//            val c = Calendar.getInstance()
//            c.time = startDate
//            c.add(Calendar.DATE, 7 * examineWeek - 2)
//            val lastDayPp = formatter.parse(formatter.format(c.time))
//            if (startDate.before(lastDayPp) && getReturnFaculty() == facultyIdDB) {
//                return obj
//            }
        }
        return latestPeriode
    }

    //Room Database

    //INSERT
    open fun insertEntry(testPlanEntry: TestPlanEntry) {
        viewModelScope.launch {
            repository.insertEntry(testPlanEntry)
        }
    }

    open fun insertEntries(entries: List<TestPlanEntry>) {
        viewModelScope.launch {
            repository.insertEntries(entries)
        }
    }

    open fun insertEntryJSON(jsonResponse: GSONEntry) {
        insertEntry(createTestPlanEntry(jsonResponse))
    }

    open fun insertCourses(cours: List<Course>) {
        viewModelScope.launch {
            repository.insertCourses(cours)
        }
    }

    open fun insertCoursesJSON(jsonCourses: List<GSONCourse>) {
        val courseList: MutableList<Course> = mutableListOf()
        jsonCourses.forEach { item ->
            courseList.add(createCourse(item))
        }
        insertCourses(courseList)
    }

    open fun insertUuid(uuid: String) {
        viewModelScope.launch {
            repository.insertUuid(uuid)
        }
    }

    open fun insertFaculty(faculty: Faculty) {
        viewModelScope.launch {
            repository.insertFaculty(faculty)
        }
    }

    //UPDATE
    open fun updateEntry(testPlanEntry: TestPlanEntry) {
        viewModelScope.launch {
            repository.updateEntry(testPlanEntry)
        }
    }

    open fun updateEntryFavorit(context: Context, favorit: Boolean, id: String) {
        viewModelScope.launch {
            repository.updateEntryFavorit(favorit, id)
            if (!getCalendarSync()) return@launch
            if (!favorit) repository.getEntryById(id)
                ?.let { GoogleCalendarIO.deleteEntry(context, it) }
            else
                repository.getEntryById(id)
                    ?.let { GoogleCalendarIO.insertEntry(context, it, getCalendarInsertionType()) }
        }
    }

    open fun updateEntryFavorit(context: Context, favorit: Boolean, entry: TestPlanEntry) {
        viewModelScope.launch {
            updateEntryFavorit(context, favorit, entry.id)
        }
    }

    open fun updateCourse(courseName: String, choosen: Boolean) {
        viewModelScope.launch {
            repository.updateCourse(courseName, choosen)
        }
    }

    open fun updateCourse(course: Course) {
        course.choosen?.let { course.courseName?.let { it1 -> updateCourse(it1, it) } }
    }

    open fun updateFaculty(faculty: Faculty) {
        viewModelScope.launch {
            repository.updateFaculty(faculty)
        }
    }

    open fun unselectAllFavorits() {
        viewModelScope.launch {
            repository.unselectAllFavorits()
        }
    }

    //DELETE
    open fun deleteEntries(entries: List<TestPlanEntry>) {
        viewModelScope.launch {
            repository.deleteEntries(entries)
        }
    }

    open fun deleteAllEntries() {
        viewModelScope.launch {
            repository.deleteAllEntries()
        }
    }

    open fun deleteCourses(cours: List<Course>) {
        viewModelScope.launch {
            repository.deleteCourses(cours)
        }
    }

    open fun deleteAllCourses() {
        viewModelScope.launch {
            repository.deleteAllCourses()
        }
    }

    open fun deleteFaculties(faculties: List<Faculty>) {
        viewModelScope.launch {
            repository.deleteFaculties(faculties)
        }
    }

    open fun deleteAllFaculties() {
        viewModelScope.launch {
            repository.deleteAllFaculties()
        }
    }

    //GET
    open suspend fun getFavorites(favorit: Boolean): List<TestPlanEntry>? {
        return repository.getFavorites(favorit)
    }

    open suspend fun getModulesOrdered(): List<String>? {
        return repository.getModulesOrdered()
    }

    open suspend fun getEntriesByCourseName(
        course: String,
        favorit: Boolean
    ): List<TestPlanEntry>? {
        return repository.getEntriesByCourseName(course, favorit)
    }

    open suspend fun getAllCourses(): List<Course>? {
        return repository.getAllCourses()
    }

    open suspend fun getCourseByName(name: String): Course {
        return repository.getCourseByName(name)
    }

    open suspend fun getCoursesByFacultyid(facultyId: String): List<Course>? {
        return repository.getAllCoursesByFacultyid(facultyId)
    }

    open suspend fun getUuid(): Uuid? {
        return repository.getUuid()
    }

    open suspend fun getCourseId(courseName: String): String? {
        return repository.getCourseId(courseName)
    }

    open suspend fun getOneEntryByName(courseName: String, favorit: Boolean): TestPlanEntry? {
        return repository.getOneEntryByName(courseName, favorit)
    }

    open suspend fun getFacultyById(id: String): Faculty? {
        return withContext(Dispatchers.IO) {
            return@withContext repository.getFacultyById(id)
        }
    }


    //Shared Preferences
    //SET
    open fun setSelectedCourse(course: String) {
        spRepository.setSelectedCourse(course)
    }

    open fun setReturnCourse(course: String) {
        spRepository.setReturnCourse(course)
    }

    open fun setReturnFaculty(faculty: String) {
        spRepository.setReturnFaculty(faculty)
    }

    open fun setReturnFaculty(faculty: Faculty) {
        setReturnFaculty(faculty.fbid)
    }

    open fun setExamineYear(year: String) {
        spRepository.setExamineYear(year)
    }

    open fun setCurrentPeriode(periode: String) {
        spRepository.setCurrentPeriode(periode)
    }

    open fun setCurrentTermin(termin: String) {
        spRepository.setCurrentTermin(termin)
    }

    open fun setStartDate(date: String) {
        spRepository.setStartDate(date)
    }

    open fun setStartDate(date: Date) {
        spRepository.setStartDate(sdfRetrofit.format(date))
    }

    open fun setEndDate(date: String) {
        spRepository.setEndDate(date)
    }

    open fun setEndDate(date: Date) {
        spRepository.setEndDate(sdfRetrofit.format(date))
    }

    open fun setChosenDarkMode(darkmode: Boolean) {
        spRepository.setChosenDarkmode(darkmode)
    }

    open fun setChosenThemeId(id: Int) {
        spRepository.setChosenThemeId(id)
    }

    open fun setUpdateIntervalTimeHour(hour: Int) {
        spRepository.setUpdateIntervalTimeHour(hour)
    }

    open fun setUpdateIntervalTimeMinute(minute: Int) {
        spRepository.setUpdateIntervalTimeMinute(minute)
    }

    open fun setBackgroundUpdates(status: Boolean) {
        spRepository.setBackgroundUpdates(status)
    }

    open fun setNotificationSounds(status: Boolean) {
        spRepository.setNotificationSounds(status)
    }

    open fun setCalendarSync(sync: Boolean) {
        spRepository.setCalendarSync(sync)
    }

    open fun setCalendarInserionType(insertionTye: GoogleCalendarIO.InsertionTye) {
        spRepository.setCalendarInsertionType(insertionTye)
    }

    open fun setServerIPAddress(address: String) {
        spRepository.setServerIPAddress(address)
    }

    open fun setServerRelUrlPath(path: String) {
        spRepository.setServerRelUrlPath(path)
    }

    open fun setFaculties(faculties: String) {
        spRepository.setFaculties(faculties)
    }


    //GET
    open fun getSelectedCourse(): String? {
        return spRepository.getSelectedCourse()
    }

    open fun getReturnCourse(): String? {
        return spRepository.getReturnCourse()
    }

    open fun getReturnFaculty(): String? {
        return spRepository.getReturnFaculty()
    }

    open fun getExamineYear(): String? {
        return spRepository.getExamineYear()
    }

    open fun getCurrentPeriode(): String? {
        return spRepository.getCurrentPeriode()
    }

    open fun getCurrentTermin(): String? {
        return spRepository.getCurrentTermin()
    }

    open fun getStartDateString(): String? {
        return spRepository.getStartDate()
    }

    open fun getStartDate(): Date? {
        return getStartDateString()?.let { sdfRetrofit.parse(it) }
    }

    open fun getEndDateString(): String? {
        return spRepository.getEndDate()
    }

    open fun getEndDate(): Date? {
        return getEndDateString()?.let { sdfRetrofit.parse(it) }
    }

    open fun getChosenDarkMode(): Boolean {
        return spRepository.getChosenDarkmode()
    }//Returns false by default

    open fun getChosenThemeId(): Int {
        return spRepository.getChosenThemeId()
    }//Returns -1 by default

    open fun getUpdateIntervalTimeHour(): Int {
        return spRepository.getUpdateIntervalTimeHour()
    }

    open fun getUpdateIntervalTimeMinute(): Int {
        return spRepository.getUpdateIntervalTimeMinute()
    } //Return 15 by default

    open fun getBackgroundUpdates(): Boolean {
        return spRepository.getBackgroundUpdates()
    }

    open fun getNotificationSounds(): Boolean {
        return spRepository.getNotificationSounds()
    }

    open fun getCalendarSync(): Boolean {
        return spRepository.getCalendarSync()
    }

    open fun getCalendarInsertionType(): GoogleCalendarIO.InsertionTye {
        return spRepository.getCalendarInsertionType() ?: GoogleCalendarIO.InsertionTye.Fragen
    }

    open fun getServerIPAddress(): String? {
        return spRepository.getServerIPAddress()
    }

    open fun getServerRelUrlPath(): String? {
        return spRepository.getServerRelUrlPath()
    }

    open fun getFaculties(): String? {
        return spRepository.getFaculties()
    }


    //DELETE
    fun deleteSelectedCourse() {
        spRepository.deleteSelectedCourse()
    }


    //Calendar
    fun updateCalendar(context: Context, entries: List<TestPlanEntry>) {
        GoogleCalendarIO.update(context, entries, getCalendarInsertionType())
    }

    fun insertCalendarEntries(context: Context, favorits: List<TestPlanEntry>? = null) {
        viewModelScope.launch {
            if (getCalendarSync()) {
                GoogleCalendarIO.insertEntries(
                    context,
                    favorits ?: getFavorites(true) ?: listOf(),
                    getCalendarInsertionType()
                )
            }
        }
    }


    //UTILS

    private fun initServer() {
        fetchFaculties()
        fetchCourses()
    }

    private fun checkSustainability() {
        viewModelScope.launch {
            val mainCourse = getSelectedCourse()?.let { getCourseByName(it) }
            if (mainCourse?.choosen == false) {
                deleteSelectedCourse()
            }
        }
    }

    /**
     * Creates a new [TestPlanEntry] from a [JsonResponse].
     *
     * @param[entry] The [JsonResponse], that contains the data for the [TestPlanEntry].
     *
     * @return A [TestPlanEntry] containing the data of the [JsonResponse]
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private fun createTestPlanEntry(entry: GSONEntry): TestPlanEntry {
        val dateLastExamFormatted = getDate(entry.Date!!)
        val testPlanEntry = TestPlanEntry()
        testPlanEntry.firstExaminer = entry.FirstExaminer
        testPlanEntry.secondExaminer = entry.SecondExaminer
        testPlanEntry.date = dateLastExamFormatted
        testPlanEntry.id = entry.ID ?: "0"
        testPlanEntry.course = entry.CourseName
        testPlanEntry.module = entry.Module
        testPlanEntry.semester = entry.Semester
        testPlanEntry.termin = entry.Termin
        testPlanEntry.room = entry.Room
        testPlanEntry.examForm = entry.Form
        testPlanEntry.status = entry.Status
        testPlanEntry.hint = entry.Hint
        testPlanEntry.color = entry.Color
        testPlanEntry.timeStamp = entry.Timestamp
        return testPlanEntry
    }

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

    private fun createCourse(jsonCourse: GSONCourse): Course {
        val course = Course()
        course.choosen = false
        course.courseName = jsonCourse.CourseName
        course.facultyId = jsonCourse.FKFBID
        course.sgid = jsonCourse.SGID
        return course
    }

    private fun createFaculty(json: JSONObject): Faculty {
        val ret = Faculty()
        ret.fbid = json.getString("fbid")
        ret.facultyName = json.getString("facName")
        ret.facultyShortname = json.getString("facShortName")
        return ret
    }

}