package com.Fachhochschulebib.fhb.pruefungsplaner.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 *
 * **See Also:**[MVVM](https://itnext.io/android-architecture-hilt-mvvm-kotlin-coroutines-live-data-room-and-retrofit-ft-8b746cab4a06)
 */
class AppRepository(
    application: Application
) {
    private var localDataSource: UserDao = AppDatabase.getAppDatabase(application).userDao()

    private val uiScope: CoroutineScope = CoroutineScope(Dispatchers.IO)

    suspend fun insertEntry(testPlanEntry: TestPlanEntry) {
        uiScope.launch {
            localDataSource.insertEntry(testPlanEntry)
        }
    }

    suspend fun insertCourses(courses: List<Courses>) {
        uiScope.launch {
            localDataSource.insertCourses(courses)

        }
    }

    suspend fun insertUuid(uuid: String) {
        uiScope.launch {
            localDataSource.insertUuid(uuid)
        }
    }

    suspend fun updateEntry(testPlanEntry: TestPlanEntry) {
        uiScope.launch {
            localDataSource.updateExam(testPlanEntry)
        }
    }

    suspend fun updateEntryFavorit(favorit: Boolean, id: Int) {
        uiScope.launch {
            localDataSource.update(favorit, id)
        }
    }

    suspend fun updateCourse(courseName: String, choosen: Boolean) {
        uiScope.launch {
            localDataSource.updateCourse(courseName, choosen)
        }
    }

    suspend fun deleteEntries(entries: List<TestPlanEntry>) {
        uiScope.launch {
            localDataSource.deleteEntries(entries)
        }
    }

    suspend fun deleteAllEntries() {
        uiScope.launch {
            localDataSource.deleteAllEntries()
        }
    }

    suspend fun getAllEntries(): List<TestPlanEntry>? {
        var ret: List<TestPlanEntry>? = null
        uiScope.launch {
            ret = localDataSource.getAllEntries()
        }
        return ret
    }

    fun getAllEntriesLiveDate(): LiveData<List<TestPlanEntry>?> =
        localDataSource.getAllEntriesLiveData()

    suspend fun getAllCourses(): List<Courses>? {
        var ret: List<Courses>? = null
        uiScope.launch {
            ret = localDataSource.getAllCourses()
        }
        return ret
    }

    suspend fun getFavorites(favorit: Boolean): List<TestPlanEntry>? {
        var ret: List<TestPlanEntry>? = null
        uiScope.launch {
            ret = localDataSource.getFavorites(favorit)
        }
        return ret
    }

    suspend fun getFirstExaminerSortedByName(selectedCourse: String): List<String>? {
        var ret: List<String>? = null
        uiScope.launch {
            localDataSource.getFirstExaminerSortedByName(selectedCourse)
        }
        return ret
    }

    suspend fun getModulesOrdered(): List<String>? {
        var ret: List<String>? = null
        uiScope.launch {
            ret = localDataSource.getModulesOrdered()
        }
        return ret
    }

    suspend fun getEntriesByCourseName(course:String,favorit: Boolean):List<TestPlanEntry>?{
        var ret:List<TestPlanEntry>? = null
        uiScope.launch {
            ret = localDataSource.getEntriesByCourseName(course,favorit)
        }
        return ret
    }

    suspend fun getChoosenCourses(choosen: Boolean):List<String>?{
        var ret:List<String>? = null
        uiScope.launch {
            ret = localDataSource.getChoosenCourses(choosen)
        }
        return ret
    }

    suspend fun getChoosenCourseIds(choosen: Boolean):List<String>?{
        var ret:List<String>? = null
        uiScope.launch {
            ret = localDataSource.getChoosenCourseIds(choosen)
        }
        return ret
    }

    suspend fun getAllCoursesByFacultyid(facultyId: String):List<Courses>?{
        var ret:List<Courses>? = null
        uiScope.launch {
            ret = localDataSource.getAllCoursesByFacultyId(facultyId)
        }
        return ret
    }

    suspend fun getEntriesByCourseName(course: String):List<TestPlanEntry>?{
        var ret:List<TestPlanEntry>? = null
        uiScope.launch {
            ret = localDataSource.getEntriesByCourseName(course)
        }
        return ret
    }

    suspend fun getEntryById(id: String):TestPlanEntry?{
        var ret:TestPlanEntry? = null
        uiScope.launch {
            ret = localDataSource.getEntryById(id)
        }
        return ret
    }

    suspend fun getUuid():Uuid?{
        var ret:Uuid? = null
        uiScope.launch {
            ret = localDataSource.getUuid()
        }
        return ret
    }

    suspend fun getCourseId(courseName: String):String?{
        var ret:String? = null
        uiScope.launch {
            ret = localDataSource.getCourseId(courseName)
        }
        return ret
    }

    suspend fun getTermin():String?{
        var ret:String? = null
        uiScope.launch {
            ret = localDataSource.getTermin()
        }
        return ret
    }

    suspend fun getOneEntryByName(courseName: String,favorit: Boolean):TestPlanEntry?{
        var ret:TestPlanEntry? = null
        uiScope.launch {
            ret = localDataSource.getOneEntryByName(courseName,favorit)
        }
        return ret
    }
}