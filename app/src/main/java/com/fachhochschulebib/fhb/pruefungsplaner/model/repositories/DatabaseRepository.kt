package com.fachhochschulebib.fhb.pruefungsplaner.model.repositories

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.fachhochschulebib.fhb.pruefungsplaner.model.retrofit.*
import com.fachhochschulebib.fhb.pruefungsplaner.model.room.*
import com.fachhochschulebib.fhb.pruefungsplaner.model.URLs.*
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
 * Stores only simple requests as suspend functions with the withcontext-Scope,specific
 * implementations are located in the ViewModels [com.fachhochschulebib.fhb.pruefungsplaner.viewmodel.BaseViewModel].
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
    /**
     * Access to the local database
     */
    private var localDataSource: UserDao = AppDatabase.getAppDatabase(context).userDao()

    /**
     * Access to the remote database via the [RetrofitInterface] and [RetrofitHelper]
     */
    private var remoteDataSource =
        RetrofitHelper.getInstance().create(RetrofitInterface::class.java)


    //Retrofit-Functions
    /**
     * Gets all courses from the Rest-Api.
     * Needs to be called inside a coroutine scope.
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
     * Needs to be called inside a coroutine scope.
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
                    val inputStream: InputStream = BufferedInputStream(urlConn.inputStream)
                    val reader = BufferedReader(InputStreamReader(inputStream))
                    var line: String?
                    while ((reader.readLine().also { line = it }) != null) {
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
                        val key: String = x.next() as String
                        jsonArray.put(jsonObj.get(key))
                    }
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
     * Needs to be called inside a coroutine scope.
     *
     * @param ppSemester The current Semester, can be taken from the sharedPreferences[SharedPreferencesRepository.getPeriodTerm].
     * @param pTermin Differences between first and second period. Can be taken from sharedPreferences [SharedPreferencesRepository.getPeriodTermin]]
     * @param pYear The year from where the entries shall be taken. The current year can be taken from [SharedPreferencesRepository.getPeriodYear]]
     * @param pId A string with all the ids from which the entries shall be taken. Can be get from [com.fachhochschulebib.fhb.pruefungsplaner.viewmodel.BaseViewModel.getIDs]
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
     * Needs to be called inside a coroutine scope.
     * Can throw errors if the connection fails and prints them with the Tag "FetchPeriods".
     *
     *
     * @return a JSONArray with all examperiods containing information. The Json-Objects contain data about the first day of the period, the semester (WiSe or SoSe), first or second period, week number and faculty.
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

    /**
     * Send feedback to the server.
     *
     * @param ratingUsability How good is the usability of the application
     * @param ratingFunctions Are there enough functions in the application
     * @param ratingStability How stable was the application, did it crashes?
     * @param text A comment from the user.
     *
     * @author Alexander Lange
     * @since 1.6
     */
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

    /**
     * Gets the UUID linked to a faculty.
     *
     * @param faculty The Faculty, linked to the UUID
     *
     * @return The UUID linked to the faculty
     *
     * @author Alexander Lange
     * @since 1.6
     */
    suspend fun fetchUUID(faculty: String): JsonUuid? {
        return withContext(Dispatchers.IO) {
            return@withContext remoteDataSource.getUUID(faculty)
        }
    }


    //Room Database
    /**
     * Gets the Live Data with all [TestPlanEntry]-Objects, ordered by their exam date
     *
     * @return The LiveData-List with all [TestPlanEntry]-Objects, ordered by their exam date
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun getAllEntriesLiveDataByDate(): LiveData<List<TestPlanEntry>?> =
        localDataSource.getAllEntriesByDateLiveData()

    /**
     * Gets the Live Data with all favorite [TestPlanEntry]-Objects.
     *
     * @return The LiveData-List with all favorite [TestPlanEntry]-Objects.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun getAllFavoritesLiveData(): LiveData<List<TestPlanEntry>?> =
        localDataSource.getAllFavoritesLiveData()

    /**
     * Gets the Live Data with all chosen [Course]-Objects
     *
     * @return The LiveData-List with all chosen [Course]-Objects
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun getAllChosenCoursesLiveData(): LiveData<List<Course>?> =
        localDataSource.getAllChosenCoursesLiveData()

    /**
     * Gets the Live Data with all [Faculty]-Objects
     *
     * @return The LiveData-List with all [Faculty]-Objects
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun getAllFacultiesLiveData(): LiveData<List<Faculty>?> =
        localDataSource.getAllFacultiesLiveData()

    /**
     * Gets the Live Data with all [TestPlanEntry]-Objects for each chosen [Course]-Object
     *
     * @return The LiveData-List with all [TestPlanEntry]-Objects for each chosen [Course]-Object
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun getAllEntriesForChosenCoursesLiveData() =
        localDataSource.getAllEntriesForChosenCoursesByDateLiveData()


    /**
     * Gets the Live Data with the names of all first examiner.
     *
     * @return The LiveData-List with the names of all first examiner.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun getFirstExaminerNamesLiveData() = localDataSource.getFirstExaminerNamesLiveData()

    /**
     * Inserts a new [TestPlanEntry] into the local database.
     *
     * @param testPlanEntry The entry to be inserted into the local database
     *
     * @author Alexander Lange
     * @since 1.6
     */
    suspend fun insertEntry(testPlanEntry: TestPlanEntry) {
        withContext(Dispatchers.IO) {
            localDataSource.insertEntry(testPlanEntry)
        }
    }

    /**
     * Inserts a list of [Course]-Objects into the local database.
     *
     * @param courses The list of courses to be inserted into the local database
     *
     * @author Alexander Lange
     * @since 1.6
     */
    suspend fun insertCourses(courses: List<Course>) {
        withContext(Dispatchers.IO) {
            localDataSource.insertCourses(courses)

        }
    }

    /**
     * Inserts a UUID into the local database
     *
     * @param uuid The UUID to be inserted into the local database
     *
     * @author Alexander Lange
     * @since 1.6
     */
    suspend fun insertUuid(uuid: String) {
        withContext(Dispatchers.IO) {
            localDataSource.insertUuid(uuid)
        }
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
    suspend fun insertFaculty(faculty: Faculty) {
        withContext(Dispatchers.IO) {
            localDataSource.insertFaculty(faculty)
        }
    }

    /**
     * Updates if a [TestPlanEntry] is a favorite or not.
     *
     * @param favorite Whether the [TestPlanEntry] is favorite or not
     * @param id The id of the [TestPlanEntry] that needs to be updated
     *
     * @author Alexander Lange
     * @since 1.6
     */
    suspend fun updateEntryFavorite(favorite: Boolean, id: String) {
        withContext(Dispatchers.IO) {
            localDataSource.update(favorite, id)
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
    suspend fun updateCourse(courseName: String, chosen: Boolean) {
        withContext(Dispatchers.IO) {
            localDataSource.updateCourse(courseName, chosen)
        }
    }

    /**
     * Deletes all favorites.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    suspend fun unselectAllFavorites() {
        withContext(Dispatchers.IO) {
            localDataSource.unselectAllFavorites()
        }
    }

    /**
     * Deletes a list of [TestPlanEntry]-Objects from the local database.
     *
     * @param entries A list of [TestPlanEntry]-Objects that need to be deleted from the local database.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    suspend fun deleteEntries(entries: List<TestPlanEntry>) {
        withContext(Dispatchers.IO) {
            localDataSource.deleteEntries(entries)
        }
    }

    /**
     * Deletes a  [TestPlanEntry] from the local database.
     *
     * @param entry The [TestPlanEntry] that needs to be deleted from the local database.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    suspend fun deleteEntry(entry: TestPlanEntry) {
        withContext(Dispatchers.IO) {
            localDataSource.deleteEntry(entry)
        }
    }

    /**
     * Deletes all [TestPlanEntry]-Objects from the local database.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    suspend fun deleteAllEntries() {
        withContext(Dispatchers.IO) {
            localDataSource.deleteAllEntries()
        }
    }

    /**
     * Returns a single [TestPlanEntry] for a given id.
     *
     * @param id The Id of the [TestPlanEntry]
     *
     * @return The [TestPlanEntry] for the Id
     *
     * @author Alexander Lange
     * @since 1.6
     */
    suspend fun getSingleEntryById(id:String):TestPlanEntry?{
        return withContext(Dispatchers.IO){
            return@withContext localDataSource.getSingleEntryById(id)
        }
    }

    /**
     * Gets all [TestPlanEntry]-Objects from the local database
     *
     * @return A list with all [TestPlanEntry]-Objects in the local database
     *
     * @author Alexander Lange
     * @since 1.6
     */
    suspend fun getAllEntries(): List<TestPlanEntry>? {
        return withContext(Dispatchers.IO) {
            return@withContext localDataSource.getAllEntries()
        }
    }

    /**
     * Gets all [TestPlanEntry]-Objects for a specific [Course].
     *
     * @param name The [Course.courseName] for the [Course].
     *
     * @return A list with all [TestPlanEntry]-Objects for a specific [Course].
     *
     * @author Alexander Lange
     * @since 1.6
     */
    suspend fun getEntriesForCourse(name: String?): List<TestPlanEntry>? {
        return withContext(Dispatchers.IO) {
            return@withContext name?.let { localDataSource.getEntriesByCourseName(it) }
        }

    }

    /**
     * Returns all courses from the local database.
     *
     * @return A list with all courses from the local database
     *
     * @author Alexander Lange
     * @since 1.6
     */
    suspend fun getAllCourses(): List<Course>? {
        return withContext(Dispatchers.IO) {
            return@withContext localDataSource.getAllCourses()
        }
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
    suspend fun getCourseById(id: String): Course {
        return withContext(Dispatchers.IO) {
            return@withContext localDataSource.getCourseById(id)
        }
    }

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
    suspend fun getFavorites(favorite: Boolean): List<TestPlanEntry>? {
        return withContext(Dispatchers.IO) {
            return@withContext localDataSource.getFavorites(favorite)
        }
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
    suspend fun getEntriesByCourseName(course: String, favorite: Boolean): List<TestPlanEntry>? {
        return withContext(Dispatchers.IO) {
            return@withContext localDataSource.getEntriesByCourseName(course, favorite)
        }
    }

    /**
     * Gets the ids for all chosen [Course]-Objects.
     *
     * @return A list with the ids for all chosen [Course]-Objects.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    suspend fun getChosenCourseIds(): List<String>? {
        return withContext(Dispatchers.IO) {
            return@withContext localDataSource.getChosenCourseIds()
        }
    }

    /**
     * Gets all [Course]-Objects for a specific faculty id.
     *
     * @param facultyId of the [Faculty] from where to get all [Course]-Objects
     *
     * @return A list with all [Course]-Objects for the given [facultyId]
     *
     * @author Alexander Lange
     * @since 1.6
     */
    suspend fun getAllCoursesByFacultyId(facultyId: String): List<Course>? {
        return withContext(Dispatchers.IO) {
            return@withContext localDataSource.getAllCoursesByFacultyId(facultyId)
        }
    }

    /**
     * Returns a [TestPlanEntry] for its [TestPlanEntry.id]
     *
     * @param id The Id of the [TestPlanEntry]
     *
     * @return The [TestPlanEntry] for the given Id
     *
     * @author Alexander Lange
     * @since 1.6
     */
    suspend fun getEntryById(id: String): TestPlanEntry? {
        return withContext(Dispatchers.IO) {
            return@withContext localDataSource.getEntryById(id)
        }
    }

    /**
     * Gets the UUID from the local database
     *
     * @return The UUID from the local database
     *
     * @author Alexander Lange
     * @since 1.6
     */
    suspend fun getUuid(): Uuid? {
        return withContext(Dispatchers.IO) {
            return@withContext localDataSource.getUuid()
        }
    }

    /**
     * Returns a [Course] for its [Course.courseName].
     *
     * @param name The [Course.courseName] of the [Course]
     *
     * @return The [Course] for the given [Course.courseName]
     *
     * @author Alexander Lange
     * @since 1.6
     */
    suspend fun getCourseByName(name: String): Course? {
        return withContext(Dispatchers.IO) {
            return@withContext localDataSource.getCourseByName(name)
        }
    }

    /**
     * Checks if a [Course] is a favorite or not.
     *
     * @param courseName The [Course.courseName] of the [Course] to be checked.
     *
     * @return true->The [Course] is a favorite;false->The [Course] is not a favorite
     *
     * @author Alexander Lange
     * @since 1.6
     */
    suspend fun checkCourseForFavorites(courseName: String): Boolean {
        return withContext(Dispatchers.IO) {
            return@withContext localDataSource.getFavoritesForCourse(courseName).isNullOrEmpty()
        }
    }

    /**
     * Gets a [Faculty] for its [Faculty.fbid].
     *
     * @param id The [Faculty.fbid] of the [Faculty] to look for.
     *
     * @return The [Faculty] for the given [Faculty.fbid]
     *
     * @author Alexander Lange
     * @since 1.6
     */
    suspend fun getFacultyById(id: String): Faculty? {
        return withContext(Dispatchers.IO) {
            return@withContext localDataSource.getFacultyById(id)
        }
    }
}