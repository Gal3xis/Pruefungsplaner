package com.Fachhochschulebib.fhb.pruefungsplaner

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.Fachhochschulebib.fhb.pruefungsplaner.data.AppRepository
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


    private val repository = AppRepository(application)
    val liveEntryList = repository.getAllEntriesLiveData()

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

}