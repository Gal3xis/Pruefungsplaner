package com.Fachhochschulebib.fhb.pruefungsplaner.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 *
 * **See Also:**[MVVM](https://itnext.io/android-architecture-hilt-mvvm-kotlin-coroutines-live-data-room-and-retrofit-ft-8b746cab4a06)
 */
class DatabaseRepository(
    application: Application
) {
    private var localDataSource: UserDao = AppDatabase.getAppDatabase(application).userDao()

    suspend fun insertEntry(testPlanEntry: TestPlanEntry) {
        withContext(Dispatchers.IO) {
            localDataSource.insertEntry(testPlanEntry)
        }
    }

    suspend fun insertCourses(courses: List<Courses>) {
        withContext(Dispatchers.IO) {
            localDataSource.insertCourses(courses)

        }
    }

    suspend fun insertUuid(uuid: String) {
        withContext(Dispatchers.IO) {
            localDataSource.insertUuid(uuid)
        }
    }

    suspend fun updateEntry(testPlanEntry: TestPlanEntry) {
        withContext(Dispatchers.IO) {
            localDataSource.updateExam(testPlanEntry)
        }
    }

    suspend fun updateEntryFavorit(favorit: Boolean, id: Int) {
        withContext(Dispatchers.IO) {
            localDataSource.update(favorit, id)
        }
    }

    suspend fun updateCourse(courseName: String, choosen: Boolean) {
        withContext(Dispatchers.IO) {
            localDataSource.updateCourse(courseName, choosen)
        }
    }

    suspend fun deleteEntries(entries: List<TestPlanEntry>) {
        withContext(Dispatchers.IO) {
            localDataSource.deleteEntries(entries)
        }
    }

    suspend fun deleteAllEntries() {
        withContext(Dispatchers.IO) {
            localDataSource.deleteAllEntries()
        }
    }

    suspend fun getAllEntries(): List<TestPlanEntry>? {
        var ret: List<TestPlanEntry>? = null
        withContext(Dispatchers.IO) {
            ret = localDataSource.getAllEntries()
        }
        return ret
    }

    fun getAllEntriesLiveData(): LiveData<List<TestPlanEntry>?> =
        localDataSource.getAllEntriesLiveData()

    suspend fun getAllCourses(): List<Courses>? {
        var ret: List<Courses>? = null
        withContext(Dispatchers.IO) {
            ret = localDataSource.getAllCourses()
        }
        return ret
    }

    suspend fun getFavorites(favorit: Boolean): List<TestPlanEntry>? {
        var ret: List<TestPlanEntry>? = null
        withContext(Dispatchers.IO) {
            ret = localDataSource.getFavorites(favorit)
        }
        return ret
    }

    suspend fun getFirstExaminerSortedByName(selectedCourse: String): List<String>? {
        var ret: List<String>? = null
        withContext(Dispatchers.IO) {
            localDataSource.getFirstExaminerSortedByName(selectedCourse)
        }
        return ret
    }

    suspend fun getModulesOrdered(): List<String>? {
        var ret: List<String>? = null
        withContext(Dispatchers.IO) {
            ret = localDataSource.getModulesOrdered()
        }
        return ret
    }

    suspend fun getEntriesByCourseName(course:String,favorit: Boolean):List<TestPlanEntry>?{
        var ret:List<TestPlanEntry>? = null
        withContext(Dispatchers.IO) {
            ret = localDataSource.getEntriesByCourseName(course,favorit)
        }
        return ret
    }

    suspend fun getChoosenCourses(choosen: Boolean):List<String>?{
        var ret:List<String>? = null
        withContext(Dispatchers.IO) {
            ret = localDataSource.getChoosenCourses(choosen)
        }
        return ret
    }

    suspend fun getChoosenCourseIds(choosen: Boolean):List<String>?{
        var ret:List<String>? = null
        withContext(Dispatchers.IO) {
            ret = localDataSource.getChoosenCourseIds(choosen)
        }
        return ret
    }

    suspend fun getAllCoursesByFacultyid(facultyId: String):List<Courses>?{
        var ret:List<Courses>? = null
        withContext(Dispatchers.IO) {
            ret = localDataSource.getAllCoursesByFacultyId(facultyId)
        }
        return ret
    }

    suspend fun getEntriesByCourseName(course: String):List<TestPlanEntry>?{
        var ret:List<TestPlanEntry>? = null
        withContext(Dispatchers.IO) {
            ret = localDataSource.getEntriesByCourseName(course)
        }
        return ret
    }

    suspend fun getEntryById(id: String):TestPlanEntry?{
        var ret:TestPlanEntry? = null
        withContext(Dispatchers.IO) {
            ret = localDataSource.getEntryById(id)
        }
        return ret
    }

    suspend fun getUuid():Uuid?{
        var ret:Uuid? = null
        withContext(Dispatchers.IO) {
            ret = localDataSource.getUuid()
        }
        return ret
    }

    suspend fun getCourseId(courseName: String):String?{
        var ret:String? = null
        withContext(Dispatchers.IO) {
            ret = localDataSource.getCourseId(courseName)
        }
        return ret
    }

    suspend fun getTermin():String?{
        var ret:String? = null
        withContext(Dispatchers.IO) {
            ret = localDataSource.getTermin()
        }
        return ret
    }

    suspend fun getOneEntryByName(courseName: String,favorit: Boolean):TestPlanEntry?{
        var ret:TestPlanEntry? = null
        withContext(Dispatchers.IO) {
            ret = localDataSource.getOneEntryByName(courseName,favorit)
        }
        return ret
    }
}