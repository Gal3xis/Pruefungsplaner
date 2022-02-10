package com.Fachhochschulebib.fhb.pruefungsplaner.data

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import com.Fachhochschulebib.fhb.pruefungsplaner.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.json.XML
import retrofit2.awaitResponse
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
        return withContext(Dispatchers.IO){
            return@withContext async { remoteDataSource.getCourses()}.await().awaitResponse().body()
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
    suspend fun fetchFaculties():JSONArray {
        return withContext(Dispatchers.IO) {
            val result = StringBuilder()
            val url =
                URL("http://85.214.233.224:8080/MeinPruefplan/resources/org.fh.ppv.entity.faculty/")
            val urlConn: HttpURLConnection = url.openConnection() as HttpURLConnection
            urlConn.connectTimeout = 1000 * 10 // mTimeout is in seconds
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

            //konvertieren zu JSONArray
            return@withContext JSONArray(deletedCling)
        }
    }

    suspend fun fetchEntries(ppSemester:String, pTermin:String, pYear:String, pId:String):JSONArray{
        return withContext(Dispatchers.IO){

            val result = StringBuilder()
            val url =
                URL("http://85.214.233.224:8080/MeinPruefplan/resources/org.fh.ppv.entity.pruefplaneintrag/${ppSemester}/${pTermin}/${pYear}/${pId}/")
            val urlConn: HttpURLConnection = url.openConnection() as HttpURLConnection
            urlConn.connectTimeout = 1000 * 10 // mTimeout is in seconds
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

            //konvertieren zu JSONArray
            return@withContext JSONArray(deletedCling)
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
    suspend fun fetchPruefperiondenObjects(): JSONArray {
        return withContext(Dispatchers.IO){
            val result = StringBuilder()

            //DONE (08/2020 LG)
            val address = "http://85.214.233.224:8080/MeinPruefplan/resources/org.fh.ppv.entity.pruefperioden/"
            val url = URL(address)

            /*
                        HttpURLConnection anstelle Retrofit, um die XML/Json-Daten abzufragen!!!
                     */
            val urlConn = url.openConnection() as HttpURLConnection
            urlConn.connectTimeout = 1000 * 10 // mTimeout is in seconds
            try {
                urlConn.connect()
            } catch (e: Exception) {
                Log.d("Output exception", e.toString())
            }

            //Variablen zum lesen der erhaltenen werte
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

            //hinzufügen der erhaltenen JSONObject werte zum JSONArray
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
            val arrayZuString = examinePeriodArray.toString()
            val erstesUndletztesZeichenentfernen =
                arrayZuString.substring(1, arrayZuString.length - 1)
            return@withContext JSONArray(erstesUndletztesZeichenentfernen)
        }
    }

    /*fun fetchEntries(): Call<List<GSONEntry>> {
        return remoteDataSource.getEntries()
    }*/

    //Room Databas
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

    suspend fun insertFaculty(faculty: Faculty)
    {
        withContext(Dispatchers.IO){
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

    suspend fun updateFaculty(faculty: Faculty)
    {
        withContext(Dispatchers.IO){
            localDataSource.updateFaculty(faculty)
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

    suspend fun deleteCourses(courses:List<Courses>){
        withContext(Dispatchers.IO){
            localDataSource.deleteCourses(courses)
        }
    }

    suspend fun deleteAllCourses(){
        withContext(Dispatchers.IO){
            localDataSource.deleteAllCourses()
        }
    }

    suspend fun deleteFaculties(faculties: List<Faculty>)
    {
        withContext(Dispatchers.IO){
            localDataSource.deleteFaculties(faculties)
        }
    }

    suspend fun deleteAllFaculties(){
        withContext(Dispatchers.IO){
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

    fun getAllEntriesLiveData(): LiveData<List<TestPlanEntry>?> =
        localDataSource.getAllEntriesLiveData()

    fun getAllFavoritsLiveData():LiveData<List<TestPlanEntry>?> = localDataSource.getAllFavoritsLiveData()

    fun getAllCoursesLiveData():LiveData<List<Courses>?> = localDataSource.getAllCoursesLiveData()

    fun getAllFacultiesLiveData():LiveData<List<Faculty>?> = localDataSource.getAllFacultiesLiveData()

    fun getCoursesForFacultyIdLiveData(id:String) = localDataSource.getCoursesForFacultyIdLiveData(id)

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
        return withContext(Dispatchers.IO) {
            return@withContext localDataSource.getChoosenCourseIds(choosen)
        }
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