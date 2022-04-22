package com.Fachhochschulebib.fhb.pruefungsplaner.model.repositories

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.Fachhochschulebib.fhb.pruefungsplaner.model.retrofit.*
import com.Fachhochschulebib.fhb.pruefungsplaner.model.room.*
import com.Fachhochschulebib.fhb.pruefungsplaner.model.URLs.*
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
 * Repository for interacting with different databases.
 * Combines access to the retrofit interface and the local room database.
 * Stores only simple requests as Suspendfunctions with the withcontext-Scope,specific
 * implementations are located in the ViewModels [com.Fachhochschulebib.fhb.pruefungsplaner.viewmodel.BaseViewModel].
 * Required for the MVVM-Pattern.
 *
 * @author Alexander Lange (Email:alexander.lange@fh-bielefeld.de)
 * @since 1.6
 *
 * **See Also:**[MVVM](https://itnext.io/android-architecture-hilt-mvvm-kotlin-coroutines-live-data-room-and-retrofit-ft-8b746cab4a06)
 * @see UserDao
 * @see RetrofitHelper
 * @see RetrofitInterface
 */
@Suppress("BlockingMethodInNonBlockingContext")
class DatabaseRepository(
    context: Context
) {
    //Access to the Room Database
    private var localDataSource: UserDao = AppDatabase.getAppDatabase(context).userDao()

    //Access to the Rest-Api via the Retrofit Interface
    private var remoteDataSource =
        RetrofitHelper.getInstance().create(RetrofitInterface::class.java)


    //Retrofit-Functions
    /**
     * Gets all courses from the Rest-Api.
     * Needs to be called inside a Coroutinescope.
     *
     * @return The list of Courses, can be null if no courses where found.
     *
     * @author Alexander Lange
     * @since 1.6
     *
     * @see RetrofitInterface.getCourses
     */
    suspend fun fetchCourses(): List<GSONCourse> {
        return withContext(Dispatchers.IO) {
            return@withContext remoteDataSource.getCourses()
        }
    }


    /**
     * Returns an Array of All Faculties from the Rest-Api.
     * Needs to be called inside a Coroutinescope.
     * Can throw errors if the connection fails and prints them with the Tag "FetchFaculties".
     *
     *
     * @return A list of all Faculties. Can be null if no faculty was found.
     *
     * @author Alexander Lange
     * @since 1.6
     *
     */
    suspend fun fetchFaculties(): JSONArray? {
        return withContext(Dispatchers.IO) {
            try {
                val result = StringBuilder()
                val url = URL(facultyUrl)
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
                    val deletedCling: String =
                        convertedToString.substring(1, convertedToString.length - 1)
                    return@withContext JSONArray(deletedCling)

                } catch (e: Exception) {
                    Log.d("FetchFaculties", e.stackTraceToString())
                    return@withContext null
                }
            } catch (e: Exception) {
                Log.d("FetchFaculties", e.stackTraceToString())
                return@withContext null
            }
        }
    }

    /**
     * Returns an Array of All Entries from the Rest-Api.
     * Needs to be called inside a Coroutinescope.
     *
     * @param ppSemester The current Semester, can be taken from the sharedPreferences[SharedPreferencesRepository.getPeriodTerm].
     * @param pTermin Differences between first and second period. Can be taken from sharedPreferences [SharedPreferencesRepository.getPeriodTermin]]
     * @param pYear The year from where the entries shall be taken. The current year can be taken from [SharedPreferencesRepository.getPeriodYear]]
     * @param pId A string with all the ids from which the entries shall be taken. Can be get from [com.Fachhochschulebib.fhb.pruefungsplaner.viewmodel.BaseViewModel.getIDs]
     *
     * @return A list of all Faculties. Can be null if no faculty was found.
     *
     * @author Alexander Lange
     * @since 1.6
     *
     * @see RetrofitInterface.getEntries
     *
     */
    suspend fun fetchEntries(
        ppSemester: String,
        pTermin: String,
        pYear: String,
        pId: String
    ): List<GSONEntry> {
        return withContext(Dispatchers.IO) {
            return@withContext remoteDataSource.getEntries(ppSemester, pTermin, pYear, pId)
        }
    }

    /**
     * Returns an Array of all examperiods in the retrofit database.
     * Needs to be called inside a Coroutinescope.
     * Can throw errors if the connection fails and prints them with the Tag "FetchPeriods".
     *
     *
     * @return a JSONArray with all examperiods containing information. The Json-Objects contain data about the first day of the period, the semester (WiSe or SoSe), first or second period, weeknumber and faculty.
     * @since 1.6
     * @author Alexander Lange
     *
     */
    suspend fun fetchExamPeriods(): JSONArray? {
        return withContext(Dispatchers.IO) {
            try {
                val result = StringBuilder()
                val url = URL(examPeriodUrl)
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
                        val obj = jsonObject["pruefperioden"]
                        examinePeriodArray.put(obj)
                    }
                    val convertedToString = examinePeriodArray.toString()
                    val deletedCling: String =
                        convertedToString.substring(1, convertedToString.length - 1)
                    return@withContext JSONArray(deletedCling)
                } catch (e: Exception) {
                    Log.d("FetchPeriods", e.stackTraceToString())
                    return@withContext null
                }
            } catch (e: Exception) {
                Log.d("FetchPeriods", e.stackTraceToString())
                return@withContext null
            }
        }
    }


    suspend fun sendFeedBack(
        uuid: String,
        ratingUsability: Float,
        ratingFunctions: Float,
        ratingStability: Float,
        text: String
    ) {
        withContext(Dispatchers.IO) {
            remoteDataSource.sendFeedBack(
                uuid,
                ratingUsability.toString(),
                ratingFunctions.toString(),
                ratingStability.toString(),
                text
            )
        }
    }


    suspend fun fetchUUID(faculty: String): JsonUuid? {
        return withContext(Dispatchers.IO) {
            return@withContext remoteDataSource.getUUID(faculty)
        }
    }

//    suspend fun fetchUUID(faculty: Faculty): JsonUuid? {
//        return fetchUUID(faculty.fbid)
//    }

    //Room Database
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
//
//    suspend fun updateEntry(testPlanEntry: TestPlanEntry) {
//        withContext(Dispatchers.IO) {
//            localDataSource.updateExam(testPlanEntry)
//        }
//    }

    suspend fun updateEntryFavorit(favorit: Boolean, id: String) {
        withContext(Dispatchers.IO) {
            localDataSource.update(favorit, id)
        }
    }

    suspend fun updateCourse(courseName: String, choosen: Boolean) {
        withContext(Dispatchers.IO) {
            localDataSource.updateCourse(courseName, choosen)
        }
    }

//    suspend fun updateFaculty(faculty: Faculty) {
//        withContext(Dispatchers.IO) {
//            localDataSource.updateFaculty(faculty)
//        }
//    }

    suspend fun unselectAllFavorits() {
        withContext(Dispatchers.IO) {
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

//    suspend fun deleteCourses(cours: List<Course>) {
//        withContext(Dispatchers.IO) {
//            localDataSource.deleteCourses(cours)
//        }
//    }
//
//    suspend fun deleteAllCourses() {
//        withContext(Dispatchers.IO) {
//            localDataSource.deleteAllCourses()
//        }
//    }
//
//    suspend fun deleteFaculties(faculties: List<Faculty>) {
//        withContext(Dispatchers.IO) {
//            localDataSource.deleteFaculties(faculties)
//        }
//    }
//
//    suspend fun deleteAllFaculties() {
//        withContext(Dispatchers.IO) {
//            localDataSource.deleteAllFaculties()
//        }
//    }

    suspend fun getAllEntries(): List<TestPlanEntry>? {
        return withContext(Dispatchers.IO) {
            return@withContext localDataSource.getAllEntries()
        }
    }

//    suspend fun getEntriesByModule(): List<TestPlanEntry>? {
//        return withContext(Dispatchers.IO) {
//            return@withContext localDataSource.getEntriesByModule()
//        }
//    }

    fun getAllEntriesLiveDataByDate(): LiveData<List<TestPlanEntry>?> =
        localDataSource.getAllEntriesLiveDataByDate()

    suspend fun getEntriesForCourseLiveData(name: String?): List<TestPlanEntry>? {
        return withContext(Dispatchers.IO) {
            return@withContext name?.let { localDataSource.getEntriesByCourseName(it) }
        }

    }

    fun getAllFavoritesLiveData(): LiveData<List<TestPlanEntry>?> =
        localDataSource.getAllFavoritsLiveData()

//    fun getAllCoursesLiveData(): LiveData<List<Course>?> = localDataSource.getAllCoursesLiveData()

    fun getAllChoosenCoursesLiveData(): LiveData<List<Course>?> =
        localDataSource.getAllChoosenCoursesLiveData()

    fun getAllFacultiesLiveData(): LiveData<List<Faculty>?> =
        localDataSource.getAllFacultiesLiveData()

//    fun getCoursesForFacultyIdLiveData(id: String) =
//        localDataSource.getCoursesForFacultyIdLiveData(id)

    fun getFirstExaminerNames() = localDataSource.getFirstExaminerNames()

    fun getAllEntriesForChoosenCoursesLiveData() =
        localDataSource.getAllEntriesForSelectedCoursesLiveDataByDate()

    suspend fun getAllCourses(): List<Course>? {
        return withContext(Dispatchers.IO) {
            return@withContext localDataSource.getAllCourses()
        }
    }

    suspend fun getCourseById(id: String): Course {
        return withContext(Dispatchers.IO) {
            return@withContext localDataSource.getCourseById(id)
        }
    }

    suspend fun getFavorites(favorit: Boolean): List<TestPlanEntry>? {
        return withContext(Dispatchers.IO) {
            return@withContext localDataSource.getFavorites(favorit)
        }
    }

//    suspend fun getFirstExaminerSortedByName(selectedCourse: String): List<String>? {
//        return withContext(Dispatchers.IO) {
//            return@withContext localDataSource.getFirstExaminerSortedByName(selectedCourse)
//        }
//    }

    suspend fun getModulesOrdered(): List<String>? {
        return withContext(Dispatchers.IO) {
            return@withContext localDataSource.getModulesOrdered()
        }
    }

    suspend fun getEntriesByCourseName(course: String, favorit: Boolean): List<TestPlanEntry>? {
        return withContext(Dispatchers.IO) {
            return@withContext localDataSource.getEntriesByCourseName(course, favorit)
        }
    }

//    suspend fun getChoosenCourses(choosen: Boolean): List<String>? {
//        return withContext(Dispatchers.IO) {
//            return@withContext localDataSource.getChoosenCourses(choosen)
//        }
//    }

    suspend fun getChoosenCourseIds(choosen: Boolean): List<String>? {
        return withContext(Dispatchers.IO) {
            return@withContext localDataSource.getChoosenCourseIds(choosen)
        }
    }

    suspend fun getAllCoursesByFacultyid(facultyId: String): List<Course>? {
        return withContext(Dispatchers.IO) {
            return@withContext localDataSource.getAllCoursesByFacultyId(facultyId)
        }
    }

//    suspend fun getEntriesByCourseName(course: String): List<TestPlanEntry>? {
//        return withContext(Dispatchers.IO) {
//            return@withContext localDataSource.getEntriesByCourseName(course)
//        }
//    }

    suspend fun getEntryById(id: String): TestPlanEntry? {
        return withContext(Dispatchers.IO) {
            return@withContext localDataSource.getEntryById(id)
        }
    }

    suspend fun getUuid(): Uuid? {
        return withContext(Dispatchers.IO) {
            return@withContext localDataSource.getUuid()
        }
    }

    suspend fun getCourseByName(name: String): Course? {
        return withContext(Dispatchers.IO) {
            return@withContext localDataSource.getCourseByName(name)
        }
    }

//    suspend fun getCourseId(courseName: String): String? {
//        return withContext(Dispatchers.IO) {
//            return@withContext localDataSource.getCourseId(courseName)
//        }
//    }


    suspend fun getOneEntryByName(courseName: String, favorit: Boolean): TestPlanEntry? {
        return withContext(Dispatchers.IO) {
            return@withContext localDataSource.getOneEntryByName(courseName, favorit)
        }
    }

    suspend fun getFacultyById(id: String): Faculty? {
        return withContext(Dispatchers.IO) {
            return@withContext localDataSource.getFacultyById(id)
        }
    }

//
//    suspend fun insertEntries(entries: List<TestPlanEntry>) {
//        withContext(Dispatchers.IO) {
//            localDataSource.insertEntries(entries)
//        }
//    }

    suspend fun getCourseName(id: String): String {
        return withContext(Dispatchers.IO) {
            return@withContext localDataSource.getCourseById(id).courseName
        }
    }
}