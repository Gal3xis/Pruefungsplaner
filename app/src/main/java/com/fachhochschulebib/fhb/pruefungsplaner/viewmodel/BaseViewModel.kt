package com.fachhochschulebib.fhb.pruefungsplaner.viewmodel

import android.app.Application
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fachhochschulebib.fhb.pruefungsplaner.model.repositories.SharedPreferencesRepository
import com.fachhochschulebib.fhb.pruefungsplaner.model.*
import com.fachhochschulebib.fhb.pruefungsplaner.model.repositories.DatabaseRepository
import com.fachhochschulebib.fhb.pruefungsplaner.model.retrofit.GSONCourse
import com.fachhochschulebib.fhb.pruefungsplaner.model.retrofit.GSONEntry
import com.fachhochschulebib.fhb.pruefungsplaner.model.room.Course
import com.fachhochschulebib.fhb.pruefungsplaner.model.room.Faculty
import com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry
import com.fachhochschulebib.fhb.pruefungsplaner.model.room.Uuid
import com.fachhochschulebib.fhb.pruefungsplaner.utils.CalendarIO
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
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

    /**
     * The Database Repository
     */
    protected val repository = DatabaseRepository(application)

    /**
     * The Shared Preferences Repository
     */
    protected val spRepository = SharedPreferencesRepository(application)

    /**
     * The [SimpleDateFormat] to convert dates from the retrofit interface
     */
    protected val sdfRetrofit = SimpleDateFormat("dd/MM/yyyy")

    /**
     * The [SimpleDateFormat] to Display dates in the UI
     */
    protected val sdfDisplay = SimpleDateFormat("dd.MM.yyyy")

    /**
     * The Exceptionhandler that is used in the ViewModel-Coroutines. Just prints the stacktrace to the logcat.
     */
    protected val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        Log.d("CoroutineException", exception.stackTraceToString())
    }

    init {
        fetchFaculties()
        fetchCourses()
        checkSustainability()
        updatePeriod()
    }


    //Remote-Access
    /**
     * Retrieves all courses from the remote database via the [com.fachhochschulebib.fhb.pruefungsplaner.model.retrofit.RetrofitInterface] and inserts them
     * into the local room database.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun fetchCourses() {
        viewModelScope.launch(coroutineExceptionHandler) {
            val courses = repository.fetchCourses()
            courses?.let { insertCoursesJSON(it) }
        }
    }

    /**
     * Retrieves all entries for the current period from the remote database via the [com.fachhochschulebib.fhb.pruefungsplaner.model.retrofit.RetrofitInterface] and inserts them
     * into the local room database. Gets the needed information for the current period via the from the shared preferences.
     *
     * @author Alexander Lange
     * @since 1.6
     *
     * */
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
                insertEntry(it)
            }
        }
    }

    /**
     * Retrieves all faculties from the remote database via the [com.fachhochschulebib.fhb.pruefungsplaner.model.retrofit.RetrofitInterface] and inserts them
     * into the local room database.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun fetchFaculties() {
        viewModelScope.launch(coroutineExceptionHandler) {
            val faculties = repository.fetchFaculties()
            if (faculties != null) {
                for (i in 0 until faculties.length()) {
                    val faculty = createFaculty(faculties.getJSONObject(i))
                    insertFaculty(faculty)
                }
            }
        }
    }

    /**
     * Updates all data in the local database.
     *
     * @author Alexander Lange
     * @since 1.6
     *
     * @see fetchFaculties
     * @see fetchCourses
     * @see fetchEntries
     */
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
    fun updatePeriod() {
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

    /**
     * Checks a list of possible periods for the latest one and returns it.
     *
     * @param periodObjects A list of possible periods.
     *
     * @return The latest period from the fiven list.
     *
     * @author Alexander Lange
     * @since 1.6
     *
     */
    private fun getCurrentPeriod(periodObjects: JSONArray): JSONObject? {
        val formatter = SimpleDateFormat("yyyy-MM-dd")

        var latestDate:Date? =null
        var latestPeriode:JSONObject? = null
        val facultyId = getSelectedFacultyId()

        for (i in 0 until periodObjects.length()) {
            //Get single object
            val obj = periodObjects.getJSONObject(i)

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
        }
        return latestPeriode
    }


    //Room Database

    //INSERT
    /**
     * Inserts a new [TestPlanEntry] into the local database.
     *
     * @param testPlanEntry The entry to be inserted into the local database
     *
     * @author Alexander Lange
     * @since 1.6
     */
    open fun insertEntry(testPlanEntry: TestPlanEntry) {
        viewModelScope.launch {
            repository.insertEntry(testPlanEntry)
        }
    }

    /**
     * Inserts a new [TestPlanEntry] into the local database.
     *
     * @param jsonResponse The jsonobject of the new entry, normally taken from the remote database.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    open fun insertEntry(jsonResponse: GSONEntry) {
        insertEntry(createTestPlanEntry(jsonResponse))
    }

    /**
     * Inserts a list of [Course]-Objects into the local database.
     *
     * @param courses The list of courses to be inserted into the local database
     *
     * @author Alexander Lange
     * @since 1.6
     */
    open fun insertCourses(courses: List<Course>) {
        viewModelScope.launch {
            repository.insertCourses(courses)
        }
    }

    /**
     * Inserts a list of [Course]-Objects into the local database.
     *
     * @param jsonCourses The list of courses to be inserted into the local database, in form of [GSONCourse]-Objects
     *
     * @author Alexander Lange
     * @since 1.6
     */
    open fun insertCoursesJSON(jsonCourses: List<GSONCourse>) {
        val courseList: MutableList<Course> = mutableListOf()
        jsonCourses.forEach { item ->
            courseList.add(createCourse(item))
        }
        insertCourses(courseList)
    }

    /**
     * Inserts a [Faculty] into the local database.
     *
     * @param faculty The [Faculty] to be inserted into the local database.
     *
     * @author Alexander Lange
     * @since 1.6
     *
     */
    open fun insertFaculty(faculty: Faculty) {
        viewModelScope.launch {
            repository.insertFaculty(faculty)
        }
    }


    //UPDATE
    /**
     * Updates if a [TestPlanEntry] is a favorite or not. Also syncs the calendar if necessary.
     *
     * @param context The Applicationcontext
     * @param favorite Whether the [TestPlanEntry] is favorite or not
     * @param entry The [TestPlanEntry] that needs to be updated
     *
     * @author Alexander Lange
     * @since 1.6
     */
    open fun updateEntryFavorite(context: Context, favorite: Boolean, entry:TestPlanEntry) {
        viewModelScope.launch {
            repository.updateEntryFavorit(favorite, entry.id)
            if (!getCalendarSync()) return@launch
            if (!favorite) {
                val calendarId = getCalendarId(entry)?:return@launch
                deleteFromCalendar(context,calendarId)
            }
            else
                insertIntoCalendar(context,entry)
        }
    }

    /**
     * Updates if a course is chosen or not.
     *
     * @param courseName The name of the course that needs to be updated
     * @param chosen Whether the course has been chosen or not
     *
     * @author Alexander Lange
     * @since 1.6
     */
    open fun updateCourse(courseName: String, chosen: Boolean) {
        viewModelScope.launch {
            repository.updateCourse(courseName, chosen)
        }
    }

    //DELETE
    /**
     * Deletes a list of [TestPlanEntry]-Objects from the local database.
     *
     * @param entries A list of [TestPlanEntry]-Objects that need to be deleted from the local database.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    open fun deleteEntries(entries: List<TestPlanEntry>) {
        viewModelScope.launch {
            repository.deleteEntries(entries)
        }
    }

    /**
     * Deletes all [TestPlanEntry]-Objects from the local database.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    open fun deleteAllEntries() {
        viewModelScope.launch {
            repository.deleteAllEntries()
        }
    }

    //GET
    /**
     * Returns a list of [TestPlanEntry]-Objects with a given favorite-state.
     *
     * @param favorite whether the entries shall be favorites or not
     *
     * @return A list of [TestPlanEntry]-Objects with the given favorite-state
     *
     * @author Alexander Lange
     * @since 1.6
     */
    open suspend fun getFavorites(favorite: Boolean = true): List<TestPlanEntry>? {
        return repository.getFavorites(favorite)
    }

    /**
     * Returns all [TestPlanEntry]-Objects for a given course name with the given favorite state.
     *
     * @param course The name of the course from where to return the entries
     * @param favorite Whether the courses shall be favorites or not
     *
     * @return A list with all favorites/not favorites for the given course
     *
     * @author Alexander Lange
     * @since 1.6
     */
    open suspend fun getFavoritesByCourseName(
        course: String,
        favorite: Boolean
    ): List<TestPlanEntry>? {
        return repository.getEntriesByCourseName(course, favorite)
    }

    /**
     * Returns all courses from the local database.
     *
     * @return A list with all courses from the local database
     *
     * @author Alexander Lange
     * @since 1.6
     */
    open suspend fun getAllCourses(): List<Course>? {
        return repository.getAllCourses()
    }

    /**
     * Returns one course for a given Id
     *
     * @param id The id of the course
     *
     * @return The course for the given id
     *
     * @author Alexander Lange
     * @since 1.6
     */
    open suspend fun getCourseById(id: String): Course {
        return repository.getCourseById(id)
    }

    /**
     * Get all courses for one [Faculty].
     *
     * @param facultyId The id of the faculty to take the courses from
     *
     * @return A list with all courses for the given faculty
     *
     * @author Alexander Lange
     * @since 1.6
     */
    open suspend fun getCoursesByFacultyId(facultyId: String): List<Course>? {
        return repository.getAllCoursesByFacultyid(facultyId)
    }

    /**
     * Returns the [Uuid] from the local database.
     *
     * @return The [Uuid] from the local database
     *
     * @author Alexander Lange
     * @since 1.6
     */
    open suspend fun getUuid(): Uuid? {
        return repository.getUuid()
    }

    /**
     * Returns the course id for a given course name.
     *
     * @param courseName The course name for the course id
     *
     * @return The Id for the given course name
     *
     * @author Alexander Lange
     * @since 1.6
     */
    open suspend fun getCourseId(courseName: String): String? {
        return repository.getCourseByName(courseName)?.sgid
    }

    /**
     * Checks if there is a [TestPlanEntry] favorite for a given course.
     *
     * @param courseName The name of the course to look for favorites
     *
     * @return true->There is at least one favorite for this course;false->There is no favorite for this course
     *
     * @author Alexander Lange
     * @since 1.6
     */
    open suspend fun checkCourseForFavorites(courseName: String): Boolean {
        return repository.checkCourseForFavorites(courseName)
    }

    /**
     * Returns the [Faculty] for a given Id.
     *
     * @param id The id of the Faculty
     *
     * @return The [Faculty] for the given id
     *
     * @author Alexander Lange
     * @since 1.6
     */
    open suspend fun getFacultyById(id: String): Faculty? {
        return withContext(Dispatchers.IO) {
            return@withContext repository.getFacultyById(id)
        }
    }


    //Shared Preferences

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
    open fun setCalendarInserionType(insertionType: CalendarIO.InsertionType) {
        spRepository.setCalendarInsertionType(insertionType)
    }

    /**
     * Saves the id of the selected Calendar into the shared preferences.
     *
     * @param id The id of the selected calendar
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun setSelectedCalendar(id:Long){
        spRepository.setSelectedCalendar(id)
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
    open fun getMainCourseId(): String? {
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
    open fun getSelectedFacultyId(): String? {
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

    //TODO RENAME
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
    open fun getCalendarInsertionType(): CalendarIO.InsertionType {
        return spRepository.getCalendarInsertionType() ?: CalendarIO.InsertionType.Ask
    }

    /**
     * Returns the id of the selected Calendar from the shared preferences
     *
     * @return The id of the selected Calendar
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun getSelectedCalendar():Long?{
        return if(spRepository.getSelectedCalendar()==0L)null else spRepository.getSelectedCalendar()
    }

    //Calendar
    /**
     * Returns a list of all ids for Events currently stored in the Calendar that are saved in the shared preferences.
     *
     * @return A list of all ids for Events currently stored in the Calendar
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun getCalendarIds():MutableList<Long>{
        return spRepository.getIds().keys.toMutableList()
    }

    /**
     * Inserts a [TestPlanEntry] into the Calendar. On success it saves the id for this entry in the shared preferences.
     *
     * @param context The Applicationcontext
     * @param entry The entry to be inserted into the Calendar
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun insertIntoCalendar(context: Context, entry: TestPlanEntry){
        val CAL_ID = getSelectedCalendar()?:return
        val id = CalendarIO.insertEntry(context,CAL_ID, entry, getCalendarInsertionType())
        id?.let {
            spRepository.addId(it, entry.id )
        }
    }

    /**
     * Inserts a list of [TestPlanEntry]-Objects into the Calendar. On success it saves the id for each entry in the shared preferences.
     *
     * @param context The Applicationcontext
     * @param entries The entries to be inserted into the Calendar
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun insertIntoCalendar(context: Context, entries: List<TestPlanEntry>){
        val CAL_ID = getSelectedCalendar()?:return
        val entryIds = mutableMapOf<Long,String>()
        entries.forEach {
            val id = CalendarIO.insertEntry(context,CAL_ID, it, getCalendarInsertionType())
            if (id != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    entryIds.putIfAbsent(id,it.id)
                }else{
                    if(!entryIds.containsKey(id)){
                        entryIds[id] = it.id
                    }
                }
            }
        }
        spRepository.addIds(entryIds)

    }

    /**
     * Returns the Calendar id for a given [TestPlanEntry] from the shared preferences
     *
     * @param entry The [TestPlanEntry] which id is looked for
     *
     * @return The id for the given [TestPlanEntry]
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun getCalendarId(entry: TestPlanEntry):Long?{
        var ret:Long? = null
        spRepository.getIds().forEach {
            if(it.value==entry.id){
                ret = it.key
            }
        }
        return ret
    }

    /**
     * Deletes an event from the Calendar.
     *
     * @param context The Applicationcontext
     * @param id The id of the event that shall be deleted
     *
     * @author Alexander Lange
     * @since 1.6
     *
     * @see getCalendarId
     */
    fun deleteFromCalendar(context: Context, id:Long){
        CalendarIO.deleteEvent(context,id)
        spRepository.deleteId(id)
    }

    /**
     * Deletes a list of events from the Calendar.
     *
     * @param context The Applicationcontext
     * @param ids The ids of the events that shall be deleted
     *
     * @author Alexander Lange
     * @since 1.6
     *
     * @see getCalendarId
     */
    fun deleteFromCalendar(context: Context, ids:List<Long>){
        CalendarIO.deleteEvents(context,ids)
        spRepository.deleteIds(ids)
    }

    /**
     * Deletes all events currently stored in the Calendar.
     *
     * @param context The Applicationcontext
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun deleteAllFromGoogleCalendar(context: Context){
        getCalendarIds().forEach {
            deleteFromCalendar(context,it)
        }
    }

    /**
     * Updates the google calendar with the new favorites.
     *
     * @param context The Applicationcontext
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun updateCalendar(context: Context){
        viewModelScope.launch (coroutineExceptionHandler){
            val ids = getCalendarIds()
            deleteFromCalendar(context,ids)
            val favorites = getFavorites()?:return@launch
            insertIntoCalendar(context,favorites)
        }
    }


    //UTILS
    /**
     * Checks if the current main course is also a chosen one. If not id deletes the current main course.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private fun checkSustainability() {
        viewModelScope.launch {
            val mainCourse = getMainCourseId()?.let { getCourseById(it) }
            if (mainCourse?.choosen == false) {
                deleteMainCourse()
            }
        }
    }


    /**
     * Creates a new [TestPlanEntry] from a [GSONEntry].
     *
     * @param[entry] The [GSONEntry], that contains the data for the [TestPlanEntry].
     *
     * @return A [TestPlanEntry] containing the data of the [GSONEntry]
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

    /**
     * Creates a new [Course] from a [GSONCourse].
     *
     * @param jsonCourse The [GSONCourse], that contains the data for the [Course].
     *
     * @return A [Course] containing the data of the [GSONCourse]
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private fun createCourse(jsonCourse: GSONCourse): Course {
        val course = Course()
        course.choosen = false
        course.courseName = jsonCourse.CourseName
        course.facultyId = jsonCourse.FKFBID
        course.sgid = jsonCourse.SGID
        return course
    }

    /**
     * Creates a new [Faculty] from a [JSONObject].
     *
     * @param[json] The [JSONObject], that contains the data for the [Faculty].
     *
     * @return A [Faculty] containing the data of the [JSONObject]
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private fun createFaculty(json: JSONObject): Faculty {
        val ret = Faculty()
        ret.fbid = json.getString("fbid")
        ret.facultyName = json.getString("facName")
        ret.facultyShortname = json.getString("facShortName")
        return ret
    }
}