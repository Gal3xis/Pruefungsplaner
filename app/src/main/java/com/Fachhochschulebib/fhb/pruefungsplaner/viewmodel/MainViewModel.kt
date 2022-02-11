package com.Fachhochschulebib.fhb.pruefungsplaner.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.Fachhochschulebib.fhb.pruefungsplaner.controller.Filter
import com.Fachhochschulebib.fhb.pruefungsplaner.model.repositories.SharedPreferencesRepository
import com.Fachhochschulebib.fhb.pruefungsplaner.model.*
import com.Fachhochschulebib.fhb.pruefungsplaner.model.repositories.DatabaseRepository
import com.Fachhochschulebib.fhb.pruefungsplaner.model.retrofit.GSONCourse
import com.Fachhochschulebib.fhb.pruefungsplaner.model.retrofit.GSONEntry
import com.Fachhochschulebib.fhb.pruefungsplaner.model.room.Courses
import com.Fachhochschulebib.fhb.pruefungsplaner.model.room.Faculty
import com.Fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry
import com.Fachhochschulebib.fhb.pruefungsplaner.model.room.Uuid
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
 *
 * **See Also:**[LiveData](https://developer.android.com/codelabs/basic-android-kotlin-training-livedata#2)
 */
open class MainViewModel(application: Application) : AndroidViewModel(application) {
    private fun initServer() {
        fetchFaculties()
        fetchCourses()
    }

    private fun checkSustainability(){
        viewModelScope.launch {
            val mainCourse = getSelectedCourse()?.let { getCourseByName(it) }
            if(mainCourse?.choosen==false){
                deleteSelectedCourse()
            }
        }
    }

    protected val repository = DatabaseRepository(application)
    protected val spRepository = SharedPreferencesRepository(application)
    protected val sdfRetrofit = SimpleDateFormat("dd/MM/yyyy")
    protected val sdfDisplay = SimpleDateFormat("dd.MM.yyyy")

    val liveFilteredEntriesByDate = MutableLiveData<List<TestPlanEntry>?>()

    val liveEntriesForCourse = MutableLiveData<List<TestPlanEntry>?>()

    val liveChoosenCourses = repository.getAllChoosenCoursesLiveData()

    val liveFilteredFavorits = MutableLiveData<List<TestPlanEntry>?>()

    var liveCoursesForFacultyId = MutableLiveData<List<Courses>?>()

    init {
        initServer()
        checkSustainability()
        updatePruefperiode()
    }

    fun filterCoursename(){
        viewModelScope.launch {
            val entriesForCourse = repository.getEntriesForCourseLiveData(Filter.courseName)
            liveEntriesForCourse.postValue(entriesForCourse)
        }
    }

    fun filter() {
        viewModelScope.launch {
            val entriesByDate = repository.getAllEntries()?.let { Filter.validateList(it) }
            val favorits = repository.getFavorites(true)
            liveFilteredEntriesByDate.postValue(entriesByDate)
            liveFilteredFavorits.postValue(favorits?.let { Filter.validateList(it) })
         }
    }

    //Remote-Access
    fun fetchCourses() {
        viewModelScope.launch {
            val courses = repository.fetchCourses()
            courses?.let { insertCoursesJSON(it) }
        }
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
    private suspend fun getIDs(): String {
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

    fun fetchEntries() {
        viewModelScope.launch {
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

    /**
     * Checks for a new exam period and updates the exam-data if necessary.
     *
     * @since 1.6
     * @author Alexander Lange (E-Mail:alexander.lange@fh-bielefeld.de)
     */
    fun updatePruefperiode() {
        viewModelScope.launch {
            // Erhalte die gewählte Fakultät aus den Shared Preferences
            try {
                //DONE (09/2020 LG) Aktuellen Prüftermin aus JSON-String herausfiltern!
                //Heutiges Datum als Vergleichsdatum ermitteln und den Formatierer festlegen.
                val now = GregorianCalendar()
                val currentDate = now.time
                val formatter = SimpleDateFormat("yyyy-MM-dd")
                var currentExamineDate: JSONObject? = null
                var date: String
                var facultyIdDB: String
                var examineDate: Date? = null
                var lastDayPp: Date? = null
                var examineWeek: Int

                val pruefperiodenObjects = repository.fetchPruefperiondenObjects()

                //Durch-Iterieren durch alle Prüfperioden-Objekte des JSON-Ergebnisses
                for (i in 0 until pruefperiodenObjects.length()) {
                    currentExamineDate = pruefperiodenObjects.getJSONObject(i)
                    date = currentExamineDate["startDatum"].toString()
                    facultyIdDB = currentExamineDate.getJSONObject("fbFbid")["fbid"].toString()
                    //Aus dem String das Datum herauslösen
                    date = date.substring(0, 10)
                    //und in ein Date-Objekt umwandeln
                    examineDate = formatter.parse(date)

                    // Erhalte die Anzahl der Wochen
                    examineWeek = currentExamineDate["PPWochen"].toString().toInt()
                    val c = Calendar.getInstance()
                    c.time = examineDate
                    c.add(Calendar.DATE, 7 * examineWeek - 2) // Anzahl der Tage Klausurenphase
                    lastDayPp = formatter.parse(formatter.format(c.time))

                    //und mit dem heutigen Datum vergleichen.
                    //Die erste Prüfperioden dieser Iteration, die nach dem heutigen Datum
                    //liegt ist die aktuelle Prüfperiode!
                    // die Fakultäts id wird noch mit der gewählten Fakultät verglichen
                    if (currentDate.before(lastDayPp) && getReturnFaculty() == facultyIdDB) break
                }
                currentExamineDate?.get("pruefTermin")?.toString()
                    ?.let { setCurrentTermin(it) }
                currentExamineDate?.get("PPJahr")?.toString()?.let { setExamineYear(it) }
                currentExamineDate?.get("pruefSemester")?.toString()
                    ?.let { setCurrentPeriode(it) }
                examineDate?.let { setStartDate(it) }
                lastDayPp?.let { setEndDate(it) }
                fetchEntries()

                // Ende Merlin Gürtler
            } catch (e: Exception) {
                Log.e("UpdatePruefperioden", e.stackTraceToString())
                Log.d("Output", "Konnte nicht die Pruefphase aktualisieren")
                //Keineverbindung();
            }
        }
    }

    //Room Database
    fun insertEntry(testPlanEntry: TestPlanEntry) {
        viewModelScope.launch {
            repository.insertEntry(testPlanEntry)
        }
    }

    fun insertEntries(entries: List<TestPlanEntry>) {
        viewModelScope.launch {
            repository.insertEntries(entries)
        }
    }

    fun insertEntryJSON(jsonResponse: GSONEntry) {
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
        filter()
    }

    fun updateEntryFavorit(favorit: Boolean, entry: TestPlanEntry) {
        viewModelScope.launch {
            updateEntryFavorit(favorit, entry.id.toInt())
        }
    }

    fun updateCourse(courseName: String, choosen: Boolean) {
        viewModelScope.launch {
            repository.updateCourse(courseName, choosen)
        }
    }

    fun updateCourse(course: Courses) {
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

    fun getAllCourses(): List<Courses>? {
        var ret: List<Courses>? = null
        viewModelScope.launch {
            ret = repository.getAllCourses()
        }
        return ret
    }

    suspend fun getCourseByName(name:String):Courses{
        return withContext(Dispatchers.IO){
            return@withContext repository.getCourseByName(name)
        }
    }

    fun getCoursesByFaculty(faculty: Faculty) {
        getCoursesByFacultyid(faculty.fbid)
    }

    fun getCoursesByFacultyid(facultyId: String) {
        viewModelScope.launch {
            val courses = repository.getAllCoursesByFacultyid(facultyId)
            liveCoursesForFacultyId.postValue(courses)
        }
    }

    suspend fun getCourseById(id:String):Courses{
        return withContext(Dispatchers.IO){
            return@withContext repository.getCourseById(id)
        }
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

    fun getOneEntryByName(courseName: String, favorit: Boolean): TestPlanEntry? {
        var ret: TestPlanEntry? = null
        viewModelScope.launch {
            ret = repository.getOneEntryByName(courseName, favorit)
        }
        return ret
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
    private fun createTestplanEntry(entry: GSONEntry): TestPlanEntry {
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

    fun createCourse(jsonCourse: GSONCourse): Courses {
        val course = Courses()
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

    fun fetchFaculties() {
        viewModelScope.launch {
            val faculties = repository.fetchFaculties()
            if(faculties!=null){
                for (i in 0 until faculties.length()) {
                    val faculty = createFaculty(faculties.getJSONObject(i))
                    insertFaculty(faculty)
                }
            }
        }
    }

    suspend fun getFacultyById(id: String) :Faculty?{
        return withContext(Dispatchers.IO){
            return@withContext repository.getFacultyById(id)
        }
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
        fetchCourses()
    }

    fun deleteSelectedCourse(){
        spRepository.deleteSelectedCourse()
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

    fun getStartDateString(): String? {
        return spRepository.getStartDate()
    }


    fun getStartDate(): Date? {
        return getStartDateString()?.let { sdfRetrofit.parse(it) }
    }

    fun setStartDate(date: String) {
        spRepository.setStartDate(date)
    }

    fun setStartDate(date: Date) {
        spRepository.setStartDate(sdfRetrofit.format(date))
    }

    fun getEndDateString(): String? {
        return spRepository.getEndDate()
    }

    fun getEndDate(): Date? {
        return getEndDateString()?.let { sdfRetrofit.parse(it) }
    }

    fun setEndDate(date: String) {
        spRepository.setEndDate(date)
    }

    fun setEndDate(date: Date) {
        spRepository.setEndDate(sdfRetrofit.format(date))
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


}