package com.Fachhochschulebib.fhb.pruefungsplaner

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.Fachhochschulebib.fhb.pruefungsplaner.data.DatabaseRepository
import com.Fachhochschulebib.fhb.pruefungsplaner.data.Courses
import com.Fachhochschulebib.fhb.pruefungsplaner.data.TestPlanEntry
import com.Fachhochschulebib.fhb.pruefungsplaner.data.Uuid
import com.Fachhochschulebib.fhb.pruefungsplaner.model.JsonCourse
import com.Fachhochschulebib.fhb.pruefungsplaner.model.JsonResponse
import kotlinx.coroutines.launch
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


    /**
     * Initializes the shared preferences and its values for later use.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private fun initSharedPreferences() {
        //Get access to the shared preferences
        var mSharedPreferencesValidation = context?.getSharedPreferences("validation", 0)
        var mSharedPreferencesPPServerAdress = context?.getSharedPreferences(
            "Server_Address",
            Context.MODE_PRIVATE
        )
        var mSharedPreferencesExamineYear = context?.getSharedPreferences("examineTermin", 0)
        var mSharedPreferencesPPeriode =
            context?.getSharedPreferences("currentPeriode", Context.MODE_PRIVATE)
    }


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

    fun insertCoursesJSON(jsonCourses: List<JsonCourse>) {
        val courseList:MutableList<Courses> = mutableListOf()
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

    fun getAllCoursesByFacultyid(facultyId: String): List<Courses>? {
        var ret: List<Courses>? = null
        viewModelScope.launch {
            ret = repository.getAllCoursesByFacultyid(facultyId)
        }
        return ret
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

    fun createCourse(jsonCourse: JsonCourse): Courses {
        val course = Courses()
        course.choosen = false
        course.courseName = jsonCourse?.courseName
        course.facultyId = jsonCourse?.fkfbid
        course.sgid = jsonCourse?.sgid ?: "-1"
        course.sgid = jsonCourse?.sgid ?: "-1"
        return course
    }


    //Shared Preferences
    fun getSelectedCourse():String?{
        return spRepository.getSelectedCourse()
    }

    fun setSelectedCourse(course:String){
        spRepository.setSelectedCourse(course)
    }

    fun getReturnCourse():String?{
        return spRepository.getReturnCourse()
        }

    fun setReturnCourse(course: String){
        spRepository.setReturnCourse(course)
    }

    fun getReturnFaculty():String?{
        return spRepository.getReturnFaculty()
    }

    fun setReturnFaculty(faculty:String){
        spRepository.setReturnFaculty(faculty)
    }

    fun getExamineYear():String?{
        return spRepository.getExamineYear()
    }

    fun setExamineYear(year:String){
        spRepository.setExamineYear(year)
    }

    fun getCurrentPeriode():String?{
        return spRepository.getCurrentPeriode()
    }

    fun setCurrentPeriode(periode:String){
        spRepository.setCurrentPeriode(periode)
    }

    fun getCurrentTermin():String?{
        return spRepository.getCurrentTermin()
    }

    fun setCurrentTermin(termin:String){
        spRepository.setCurrentTermin(termin)
    }

    //TODO Needed?
    fun getCurrentPeriodeString():String?{
        return spRepository.getCurrentPeriodeString()
    }

    fun setCurrentPeriodeString(str:String){
        spRepository.setCurrentPeriodeString(str)
    }

    fun getStartDateString():String?{
        return spRepository.getStartDate()
    }


    fun getStartDate():Date?
    {
        return getStartDateString()?.let { sdf.parse(it) }
    }

    fun setStartDate(date:String){
        spRepository.setStartDate(date)
    }

    fun setStartDate(date:Date){
        spRepository.setStartDate(sdf.format(date))
    }

    fun getEndDateString():String?{
        return spRepository.getEndDate()
    }

    fun getEndDate():Date?{
        return getEndDateString()?.let { sdf.parse(it) }
    }

    fun setEndDate(date: String){
        spRepository.setEndDate(date)
    }

    fun setEndDate(date:Date){
        spRepository.setEndDate(sdf.format(date))
    }

    //Returns false by default
    fun getChosenDarkmode():Boolean{
        return spRepository.getChosenDarkmode()
    }

    fun setChosenDarkmode(darkmode:Boolean){
        spRepository.setChosenDarkmode(darkmode)
    }

    //Returns -1 by default
    fun getChosenThemeId():Int{
        return spRepository.getChosenThemeId()
    }

    fun setChosenThemeId(id:Int){
        spRepository.setChosenThemeId(id)
    }

    fun getUpdateIntervalTimeHour():Int{
        return spRepository.getUpdateIntervalTimeHour()
    }

    fun setUpdateIntervalTimeHour(hour:Int){
        spRepository.setUpdateIntervalTimeHour(hour)
    }

    //Return 15 by default
    fun getUpdateIntervalTimeMinute():Int{
        return spRepository.getUpdateIntervalTimeMinute()
    }

    fun setUpdateIntervalTimeMinute(minute:Int){
        spRepository.setUpdateIntervalTimeMinute(minute)
    }

    fun getCalendarSync():Boolean{
        return spRepository.getCalendarSync()
    }

    fun setCalendarSync(sync:Boolean){
        spRepository.setCalendarSync(sync)
    }

    fun getServerIPAddress():String?{
        return spRepository.getServerIPAddress()
    }

    fun setServerIPAddress(address:String){
        spRepository.setServerIPAddress(address)
    }

    fun getServerRelUrlPath():String?{
        return spRepository.getServerRelUrlPath()
    }

    fun setServerRelUrlPath(path:String){
        spRepository.setServerRelUrlPath(path)
    }

    fun getFaculties():String?{
        return spRepository.getFaculties()
    }

    fun setFaculties(faculties:String){
        spRepository.setFaculties(faculties)
    }
}