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
            val periode = getPeriodTerm()
            val termin = getPeriodTermin()
            val examinYear = getPeriodYear()
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
            val currentPeriod= getCurrentPeriod(pruefperiodenObjects) ?:return@launch
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
            setPeriodTermin(currentPeriod.get("pruefTermin").toString())
            setPeriodYear(currentPeriod.get("PPJahr").toString())
            setPeriodTerm(currentPeriod.get("pruefSemester").toString())
            fetchEntries()
        }
    }

    private fun getCurrentPeriod(pruefperiodenObjects: JSONArray): JSONObject? {
        val formatter = SimpleDateFormat("yyyy-MM-dd")

        var latestDate:Date? =null
        var latestPeriode:JSONObject? = null
        val facultyId = getSelectedFaculty()

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

    open suspend fun getCourseById(id: String): Course {
        return repository.getCourseById(id)
    }

    open suspend fun getCoursesByFacultyid(facultyId: String): List<Course>? {
        return repository.getAllCoursesByFacultyid(facultyId)
    }

    open suspend fun getUuid(): Uuid? {
        return repository.getUuid()
    }

    open suspend fun getCourseId(courseName: String): String? {
        return repository.getCourseByName(courseName)?.sgid
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
    /**
     * Sets the course, selected as the maincourse
     *
     * @param courseId The id of the course, selected as the maincourse
     *
     * @author Alexander Lange
     * @since 1.6
     */
    open fun setMainCourse(courseId: String) {
        spRepository.setMainCourse(courseId)
    }

    private fun deleteMainCourse() {
        spRepository.deleteMainCourse();
    }
    /**
     * Sets the faculty, selected by the user
     *
     * @param facultyId The id of the faculty, the user selected
     *
     * @author Alexander Lange
     * @since 1.6
     */
    open fun setSelectedFaculty(facultyId: String) {
        spRepository.setSelectedFaculty(facultyId)
    }

    /**
     * Sets the faculty, selected by the user
     *
     * @param faculty The object of the faculty, the user selected
     *
     * @author Alexander Lange
     * @since 1.6
     */
    open fun setSelectedFaculty(faculty: Faculty) {
        setSelectedFaculty(faculty.fbid)
    }

    /**
     * Sets the year of the current period
     *
     * @param year The year of the current period as string (Like '2022')
     *
     * @author Alexander Lange
     * @since 1.6
     */
    open fun setPeriodYear(year: String) {
        spRepository.setPeriodYear(year)
    }

    /**
     * Sets the term of the current period (SoSe or WiSe)
     *
     * @param period The term of the current period (SoSe or WiSe)
     *
     * @author Alexander Lange
     * @since 1.6
     */
    open fun setPeriodTerm(periode: String) {
        spRepository.setPeriodTerm(periode)
    }

    //TODO RENAME
    open fun setPeriodTermin(termin: String) {
        spRepository.setPeriodTermin(termin)
    }

    /**
     * Sets the start date of the period
     *
     * @param date The start date of the period as a string
     *
     * @author Alexander Lange
     * @since 1.6
     */
    open fun setStartDate(date: String) {
        spRepository.setStartDate(date)
    }

    /**
     * Sets the start date of the period
     *
     * @param date The start date of the period
     *
     * @author Alexander Lange
     * @since 1.6
     */
    open fun setStartDate(date: Date) {
        spRepository.setStartDate(sdfRetrofit.format(date))
    }

    /**
     * Sets the end date of the period
     *
     * @param date The end date of the period as a string
     *
     * @author Alexander Lange
     * @since 1.6
     */
    open fun setEndDate(date: String) {
        spRepository.setEndDate(date)
    }

    /**
     * Sets the end date of the period
     *
     * @param date The end date of the period
     *
     * @author Alexander Lange
     * @since 1.6
     */
    open fun setEndDate(date: Date) {
        spRepository.setEndDate(sdfRetrofit.format(date))
    }


    /**
     * Sets the setting for the darkmode
     *
     * @param darkmode true-> darkmode is set; false-> darkmode is not set
     *
     * @author Alexander Lange
     * @since 1.6
     */
    open fun setChosenDarkMode(darkmode: Boolean) {
        spRepository.setChosenDarkmode(darkmode)
    }

    /**
     * Sets the id of the selected theme
     *
     * @param id The id of the selected theme
     *
     * @author Alexander Lange
     * @since 1.6
     */
    open fun setChosenThemeId(id: Int) {
        spRepository.setChosenThemeId(id)
    }

    /**
     * Sets the hour component of the update interval for the background worker
     *
     * @param hour The hour component of the update interval for the background worker
     *
     * @author Alexander Lange
     * @since 1.6
     */
    open fun setUpdateIntervalTimeHour(hour: Int) {
        spRepository.setUpdateIntervalTimeHour(hour)
    }

    /**
     * Sets the minute component of the update interval for the background worker
     *
     * @param minute The hour component of the update interval for the background worker
     *
     * @author Alexander Lange
     * @since 1.6
     */
    open fun setUpdateIntervalTimeMinute(minute: Int) {
        spRepository.setUpdateIntervalTimeMinute(minute)
    }

    /**
     * Sets the setting for background updates
     *
     * @param status true->The app will update in the background; false->The app wont update in the background
     *
     * @author Alexander Lange
     * @since 1.6
     */
    open fun setBackgroundUpdates(status: Boolean) {
        spRepository.setBackgroundUpdates(status)
    }

    /**
     * Sets the setting for notification sounds for the background worker
     *
     * @param status true->The app will make a sound for each notification;false->the app will make no sound for a notification
     *
     * @author Alexander Lange
     * @since 1.6
     */
    open fun setNotificationSounds(status: Boolean) {
        spRepository.setNotificationSounds(status)
    }

    /**
     * Sets the setting for calendar synchronization
     *
     * @param sync true->The calendar will synchronize with the selected exams;false->The calendar wont sync with the selected courses
     *
     * @author Alexander Lange
     * @since 1.6
     */
    open fun setCalendarSync(sync: Boolean) {
        spRepository.setCalendarSync(sync)
    }

    /**
     * Sets the setting for the insertion type for each calendar entry
     *
     * @param insertionType The insertion type for each new calendar entry.
     * Automatic -> The entry will be placed in the calendar without notifying the user;
     * Manuel->The insertion intent of the calendar will be started, where the user can modify the entry himself;
     * Ask->The user will be asked each time, if an entry should be manuel ro automatic
     */
    open fun setCalendarInserionType(insertionTye: GoogleCalendarIO.InsertionTye) {
        spRepository.setCalendarInsertionType(insertionTye)
    }

    //GET
    /**
     * Gets the course, selected as the maincourse
     *
     * @return The id of the course, selected as the maincourse
     *
     * @author Alexander Lange
     * @since 1.6
     */
    open fun getMainCourse(): String? {
        return spRepository.getMainCourse()
    }

    /**
     * Gets the faculty, selected by the user
     *
     * @return The id of the faculty, the user selected
     *
     * @author Alexander Lange
     * @since 1.6
     */
    open fun getSelectedFaculty(): String? {
        return spRepository.getSelectedFaculty()
    }

    /**
     * Gets the year of the current period
     *
     * @return the year of the current period as string
     *
     * @author Alexander Lange
     * @since 1.6
     */
    open fun getPeriodYear(): String? {
        return spRepository.getPeriodYear()
    }

    /**
     * Gets the term of the current period (SoSe or WiSe)
     *
     * @return The term of the current period (SoSe or WiSe)
     *
     * @author Alexander Lange
     * @since 1.6
     */
    open fun getPeriodTerm(): String? {
        return spRepository.getPeriodTerm()
    }

    open fun getPeriodTermin(): String? {
        return spRepository.getPeriodTermin()
    }

    /**
     * Gets the start date of the period
     *
     * @return The start date of the period as a string
     *
     * @author Alexander Lange
     * @since 1.6
     */
    open fun getStartDateString(): String? {
        return spRepository.getStartDate()
    }

    /**
     * Gets the start date of the period
     *
     * @return The start date of the period
     *
     * @author Alexander Lange
     * @since 1.6
     */
    open fun getStartDate(): Date? {
        return getStartDateString()?.let { sdfRetrofit.parse(it) }
    }

    /**
     * Gets the end date of the period
     *
     * @return The end date of the period as a string
     *
     * @author Alexander Lange
     * @since 1.6
     */
    open fun getEndDateString(): String? {
        return spRepository.getEndDate()
    }

    /**
     * Gets the end date of the period
     *
     * @return The end date of the period
     *
     * @author Alexander Lange
     * @since 1.6
     */
    open fun getEndDate(): Date? {
        return getEndDateString()?.let { sdfRetrofit.parse(it) }
    }

    /**
     * Gets the setting for the darkmode
     *
     * @return true-> darkmode is set; false-> darkmode is not set. Returns false by default
     *
     * @author Alexander Lange
     * @since 1.6
     */
    open fun getChosenDarkMode(): Boolean {
        return spRepository.getChosenDarkmode()
    }

    /**
     * Gets the id of the selected theme
     *
     * @return The id of the selected theme. Returns the id of the first theme in the list (green) by default
     *
     * @author Alexander Lange
     * @since 1.6
     */
    open fun getChosenThemeId(): Int {
        return spRepository.getChosenThemeId()
    }

    /**
     * Gets the hour component of the update interval for the background worker
     *
     * @return The hour component of the update interval for the background worker. Return 0 by default
     *
     * @author Alexander Lange
     * @since 1.6
     */
    open fun getUpdateIntervalTimeHour(): Int {
        return spRepository.getUpdateIntervalTimeHour()
    }

    /**
     * Gets the minute component of the update interval for the background worker
     *
     * @return The minute component of the update interval for the background worker. Returns 15 by default, because 0:15 is the minimum interval for the background worker
     *
     * @author Alexander Lange
     * @since 1.6
     */
    open fun getUpdateIntervalTimeMinute(): Int {
        return spRepository.getUpdateIntervalTimeMinute()
    }

    /**
     * Gets the setting for background updates
     *
     * @return true->The app will update in the background; false->The app wont update in the background. Returns false by default
     *
     * @author Alexander Lange
     * @since 1.6
     */
    open fun getBackgroundUpdates(): Boolean {
        return spRepository.getBackgroundUpdates()
    }

    /**
     * Gets the setting for notification sounds for the background worker
     *
     * @return true->The app will make a sound for each notification;false->the app will make no sound for a notification. Returns false by default
     *
     * @author Alexander Lange
     * @since 1.6
     */
    open fun getNotificationSounds(): Boolean {
        return spRepository.getNotificationSounds()
    }

    /**
     * Gets the setting for calendar synchronization
     *
     * @return true->The calendar will synchronize with the selected exams;false->The calendar wont sync with the selected courses. Returns false by default
     *
     * @author Alexander Lange
     * @since 1.6
     */
    open fun getCalendarSync(): Boolean {
        return spRepository.getCalendarSync()
    }

    /**
     * Gets the setting for the insertion type for each calendar entry
     *
     * @return The insertion type for each new calendar entry.
     * Automatic -> The entry will be placed in the calendar without notifying the user;
     * Manuel->The insertion intent of the calendar will be started, where the user can modify the entry himself;
     * Ask->The user will be asked each time, if an entry should be manuel ro automatic. Returns automatic by default.
     */
    open fun getCalendarInsertionType(): GoogleCalendarIO.InsertionTye {
        return spRepository.getCalendarInsertionType() ?: GoogleCalendarIO.InsertionTye.Ask
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
            val mainCourse = getMainCourse()?.let { getCourseById(it) }
            if (mainCourse?.choosen == false) {
                deleteMainCourse()
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