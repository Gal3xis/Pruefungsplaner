package com.Fachhochschulebib.fhb.pruefungsplaner.model.repositories

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import com.Fachhochschulebib.fhb.pruefungsplaner.model.*
import com.Fachhochschulebib.fhb.pruefungsplaner.model.retrofit.API
import com.Fachhochschulebib.fhb.pruefungsplaner.model.retrofit.GSONCourse
import com.Fachhochschulebib.fhb.pruefungsplaner.model.retrofit.GSONEntry
import com.Fachhochschulebib.fhb.pruefungsplaner.model.retrofit.RetrofitHelper
import com.Fachhochschulebib.fhb.pruefungsplaner.model.room.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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

/**
 *
 * **See Also:**[MVVM](https://itnext.io/android-architecture-hilt-mvvm-kotlin-coroutines-live-data-room-and-retrofit-ft-8b746cab4a06)
 */
class DatabaseRepository(
    application: Application
) {
    private var localDataSource: UserDao = AppDatabase.getAppDatabase(application).userDao()
    private var remoteDataSource = RetrofitHelper.getInstance().create(API::class.java)

    //Retrofit
    suspend fun fetchCourses(): List<GSONCourse>? {
        return withContext(Dispatchers.IO) {
            return@withContext remoteDataSource.getCourses()
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
    suspend fun fetchFaculties(): JSONArray? {
        return withContext(Dispatchers.IO) {
            try {
                val result = StringBuilder()
                val url =
                        URL("http://85.214.233.224:8080/MeinPruefplan/resources/org.fh.ppv.entity.faculty/")
                val urlConn: HttpURLConnection = url.openConnection() as HttpURLConnection
                urlConn.connectTimeout = 1000 * 10 // mTimeout is in seconds
                try {
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
                    return@withContext JSONArray(deletedCling)

                }catch (e:Exception){
                    Log.d("DataBaseRepository:fetchFaculties",e.stackTraceToString())
                    return@withContext null
                }
            }catch (e:Exception){
                Log.d("FetchFaculties",e.stackTraceToString())
                return@withContext null
            }
        }
    }

    suspend fun fetchEntries(
        ppSemester: String,
        pTermin: String,
        pYear: String,
        pId: String
    ): List<GSONEntry>? {
        return withContext(Dispatchers.IO) {
            return@withContext remoteDataSource.getEntries(ppSemester, pTermin, pYear, pId)
        }
    }

    //TODO Move
    /**
     * Returns a list of all examperiods in the database.
     *
     * @return a JSONArray with all examperiods containing information. The Json-Objects contain data about the first day of the period, the semester (WiSe or SoSe), first or second period, weeknumber and faculty.
     * @since 1.6
     * @author Alexander Lange (E-Mail:alexander.lange@fh-bielefeld.de)
     */
    suspend fun fetchPruefperiondenObjects(): JSONArray? {
        return withContext(Dispatchers.IO) {
            try {
                val result = StringBuilder()
                val address =
                        "http://85.214.233.224:8080/MeinPruefplan/resources/org.fh.ppv.entity.pruefperioden/"
                val url = URL(address)
                val urlConn = url.openConnection() as HttpURLConnection
                urlConn.connectTimeout = 1000 * 10 // mTimeout is in seconds
                try {
                    urlConn.connect()
                    val inputStream: InputStream = BufferedInputStream(urlConn.inputStream)
                    val reader = BufferedReader(InputStreamReader(inputStream))
                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        result.append(line)
                    }
                    var jsonObj: JSONObject? = null
                    try {
                        jsonObj = XML.toJSONObject(result.toString())
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    val x: Iterator<*> = jsonObj!!.keys()
                    val jsonArray = JSONArray()
                    while (x.hasNext()) {
                        val key = x.next() as String
                        jsonArray.put(jsonObj[key])
                    }
                    val examinePeriodArray = JSONArray()
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        examinePeriodArray.put(jsonObject["pruefperioden"])
                    }
                    return@withContext examinePeriodArray
                } catch (e: Exception) {
                    Log.d("Output exception", e.stackTraceToString())
                    return@withContext null
                }
            }catch (e:Exception){
                Log.d("Output exception", e.stackTraceToString())
                return@withContext null
            }
        }
    }

    //Room Databas
    suspend fun insertEntry(testPlanEntry: TestPlanEntry) {
        withContext(Dispatchers.IO) {
            localDataSource.insertEntry(testPlanEntry)
        }
    }

    suspend fun insertCourses(cours: List<Course>) {
        withContext(Dispatchers.IO) {
            localDataSource.insertCourses(cours)

        }
    }

    suspend fun insertUuid(uuid: String) {
        withContext(Dispatchers.IO) {
            localDataSource.insertUuid(uuid)
        }
    }

    suspend fun insertFaculty(faculty: Faculty) {
        withContext(Dispatchers.IO) {
            localDataSource.insertFaculty(faculty)
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

    suspend fun updateFaculty(faculty: Faculty) {
        withContext(Dispatchers.IO) {
            localDataSource.updateFaculty(faculty)
        }
    }

    suspend fun unselectAllFavorits(){
        withContext(Dispatchers.IO){
            localDataSource.unselectAllFavorits()
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

    suspend fun deleteCourses(cours: List<Course>) {
        withContext(Dispatchers.IO) {
            localDataSource.deleteCourses(cours)
        }
    }

    suspend fun deleteAllCourses() {
        withContext(Dispatchers.IO) {
            localDataSource.deleteAllCourses()
        }
    }

    suspend fun deleteFaculties(faculties: List<Faculty>) {
        withContext(Dispatchers.IO) {
            localDataSource.deleteFaculties(faculties)
        }
    }

    suspend fun deleteAllFaculties() {
        withContext(Dispatchers.IO) {
            localDataSource.deleteAllFaculties()
        }
    }

    suspend fun getAllEntries(): List<TestPlanEntry>? {
        var ret: List<TestPlanEntry>? = null
        withContext(Dispatchers.IO) {
            ret = localDataSource.getAllEntries()
        }
        return ret
    }

    suspend fun getEntriesByModule():List<TestPlanEntry>?{
        return withContext(Dispatchers.IO){
            return@withContext localDataSource.getEntriesByModule()
        }
    }

    fun getAllEntriesLiveDataByDate(): LiveData<List<TestPlanEntry>?> =
        localDataSource.getAllEntriesLiveDataByDate()

    suspend fun getEntriesForCourseLiveData(name: String?): List<TestPlanEntry>? {
        return withContext(Dispatchers.IO){
            return@withContext name?.let { localDataSource.getEntriesByCourseName(it) }
        }

    }

    fun getAllFavoritsLiveData(): LiveData<List<TestPlanEntry>?> =
        localDataSource.getAllFavoritsLiveData()

    fun getAllCoursesLiveData(): LiveData<List<Course>?> = localDataSource.getAllCoursesLiveData()

    fun getAllChoosenCoursesLiveData(): LiveData<List<Course>?> =
        localDataSource.getAllChoosenCoursesLiveData()

    fun getAllFacultiesLiveData(): LiveData<List<Faculty>?> =
        localDataSource.getAllFacultiesLiveData()

    fun getCoursesForFacultyIdLiveData(id: String) =
        localDataSource.getCoursesForFacultyIdLiveData(id)

    fun getFirstExaminerNames() = localDataSource.getFirstExaminerNames()

    suspend fun getAllCourses(): List<Course>? {
        return withContext(Dispatchers.IO) {
            return@withContext localDataSource.getAllCourses()
        }
    }

    suspend fun getCourseById(id:String):Course{
        return withContext(Dispatchers.IO){
            return@withContext localDataSource.getCourseById(id)
        }
    }

    suspend fun getFavorites(favorit: Boolean): List<TestPlanEntry>? {
       return withContext(Dispatchers.IO) {
            return@withContext localDataSource.getFavorites(favorit)
        }
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

    suspend fun getEntriesByCourseName(course: String, favorit: Boolean): List<TestPlanEntry>? {
        var ret: List<TestPlanEntry>? = null
        withContext(Dispatchers.IO) {
            ret = localDataSource.getEntriesByCourseName(course, favorit)
        }
        return ret
    }

    suspend fun getChoosenCourses(choosen: Boolean): List<String>? {
        var ret: List<String>? = null
        withContext(Dispatchers.IO) {
            ret = localDataSource.getChoosenCourses(choosen)
        }
        return ret
    }

    suspend fun getChoosenCourseIds(choosen: Boolean): List<String>? {
        return withContext(Dispatchers.IO) {
            return@withContext localDataSource.getChoosenCourseIds(choosen)
        }
    }

    suspend fun getAllCoursesByFacultyid(facultyId: String): List<Course>? {
        var ret: List<Course>? = null
        withContext(Dispatchers.IO) {
            ret = localDataSource.getAllCoursesByFacultyId(facultyId)
        }
        return ret
    }

    suspend fun getEntriesByCourseName(course: String): List<TestPlanEntry>? {
        return withContext(Dispatchers.IO) {
            return@withContext localDataSource.getEntriesByCourseName(course)
        }
    }

    suspend fun getEntryById(id: String): TestPlanEntry? {
        var ret: TestPlanEntry? = null
        withContext(Dispatchers.IO) {
            ret = localDataSource.getEntryById(id)
        }
        return ret
    }

    suspend fun getUuid(): Uuid? {
        var ret: Uuid? = null
        withContext(Dispatchers.IO) {
            ret = localDataSource.getUuid()
        }
        return ret
    }

    suspend fun getCourseByName(name:String):Course{
        return withContext(Dispatchers.IO){
            return@withContext localDataSource.getCourseByName(name)
        }
    }

    suspend fun getCourseId(courseName: String): String? {
        var ret: String? = null
        withContext(Dispatchers.IO) {
            ret = localDataSource.getCourseId(courseName)
        }
        return ret
    }

    suspend fun getTermin(): String? {
        var ret: String? = null
        withContext(Dispatchers.IO) {
            ret = localDataSource.getTermin()
        }
        return ret
    }

    suspend fun getOneEntryByName(courseName: String, favorit: Boolean): TestPlanEntry? {
        var ret: TestPlanEntry? = null
        withContext(Dispatchers.IO) {
            ret = localDataSource.getOneEntryByName(courseName, favorit)
        }
        return ret
    }

    suspend fun getFacultyById(id: String): Faculty? {
        return withContext(Dispatchers.IO) {
            return@withContext localDataSource.getFacultyById(id)
        }
    }


    suspend fun insertEntries(entries: List<TestPlanEntry>) {
        withContext(Dispatchers.IO) {
            localDataSource.insertEntries(entries)
        }
    }
}