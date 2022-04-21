package com.Fachhochschulebib.fhb.pruefungsplaner.model.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao {

    //Inserts
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEntry(testPlanEntry: TestPlanEntry)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEntries(testPlanEntries: List<TestPlanEntry>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCourses(cours: List<Course>)

    @Query("INSERT INTO Uuid VALUES (:uuid)")
    suspend fun insertUuid(uuid: String)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFaculty(faculty: Faculty)

    //Updates
    @Update
    suspend fun updateExam(testPlanEntry: TestPlanEntry)

    @Query("UPDATE TestPlanEntry SET favorit = :favorit WHERE ID = :id")
    suspend fun update(favorit: Boolean, id: String)

    @Query("UPDATE Course SET choosen = :choosen WHERE courseName = :courseName")
    suspend fun updateCourse(courseName: String, choosen: Boolean)

    @Update
    suspend fun updateFaculty(faculty: Faculty)

    @Query("UPDATE testPlanEntry SET favorit = 0")
    suspend fun unselectAllFavorits()


    //Deletes
    @Delete
    suspend fun deleteEntries(entries: List<TestPlanEntry>)

    @Query("DELETE FROM TestPlanEntry ")
    suspend fun deleteAllEntries()

    @Delete
    suspend fun deleteCourses(cours:List<Course>)

    @Query("DELETE FROM Course")
    suspend fun deleteAllCourses()

    @Delete
    suspend fun deleteFaculties(faculty: List<Faculty>)

    @Query("DELETE FROM Faculty")
    suspend fun deleteAllFaculties()
    //Queries


    @Query("SELECT * FROM TestPlanEntry ORDER BY date, termin, module")
    suspend fun getAllEntries(): List<TestPlanEntry>?

    @Query("SELECT * FROM TestPlanEntry ORDER BY module")
    suspend fun getEntriesByModule(): List<TestPlanEntry>?

    @Query("SELECT * FROM TestPlanEntry ORDER BY date, termin, module")
    fun getAllEntriesLiveDataByDate(): LiveData<List<TestPlanEntry>?>

    @Query("SELECT * FROM TestPlanEntry as t INNER JOIN Course as c ON c.courseName LIKE t.course WHERE c.choosen = 1 ORDER BY date, termin, module")
    fun getAllEntriesForSelectedCoursesLiveDataByDate(): LiveData<List<TestPlanEntry>?>


    @Query("SELECT * FROM TestPlanEntry WHERE course = :name ORDER BY module")
    fun getEntriesForCourseLiveData(name:String):LiveData<List<TestPlanEntry>?>

    @Query("SELECT * FROM Course ORDER BY courseName ")
    fun getAllCoursesLiveData():LiveData<List<Course>?>

    @Query("SELECT * FROM Course WHERE choosen = 1 ORDER BY courseName")
    fun getAllChoosenCoursesLiveData():LiveData<List<Course>?>

    @Query("SELECT * FROM TestPlanEntry WHERE favorit = 1 ORDER BY date, termin, module")
    fun getAllFavoritsLiveData():LiveData<List<TestPlanEntry>?>

    @Query("SELECT * FROM Faculty ORDER BY facName LIMIT 100 OFFSET 1")
    fun getAllFacultiesLiveData():LiveData<List<Faculty>?>

    @Query("SELECT DISTINCT firstExaminer FROM testPlanEntry ORDER BY firstExaminer")
    fun getFirstExaminerNames():LiveData<List<String>?>

    @Query("SELECT * FROM Course WHERE facultyId = :id ORDER BY courseName")
    fun getCoursesForFacultyIdLiveData(id:String):LiveData<List<Course>?>

    @Query("SELECT * FROM Course WHERE cId = :id LIMIT 1")
    suspend fun getCourseById(id:String):Course

    @Query("SELECT * FROM Course ORDER BY courseName")
    suspend fun getAllCourses(): List<Course>?

    @Query("SELECT * FROM TestPlanEntry WHERE favorit = :favorit ORDER BY date, termin, module")
    suspend fun getFavorites(favorit: Boolean): List<TestPlanEntry>?

    @Query("SELECT DISTINCT firstExaminer FROM TestPlanEntry WHERE course = :selectedCourse ORDER BY firstExaminer")
    suspend fun getFirstExaminerSortedByName(selectedCourse: String): List<String>?

    @Query("SELECT DISTINCT module FROM TestPlanEntry ORDER BY module")
    suspend fun  getModulesOrdered(): List<String>?

    @Query("SELECT * FROM TestPlanEntry WHERE course = :course AND Favorit = :favorit")
    suspend fun getEntriesByCourseName(course: String, favorit: Boolean): List<TestPlanEntry>?

    @Query("SELECT courseName FROM Course WHERE choosen = :choosen ORDER BY courseName")
    suspend fun getChoosenCourses(choosen: Boolean): List<String>?

    @Query("SELECT cId FROM Course WHERE choosen = :choosen")
    suspend fun getChoosenCourseIds(choosen: Boolean): List<String>?

    @Query("SELECT * FROM Course WHERE facultyId = :facultyId")
    suspend fun getAllCoursesByFacultyId(facultyId: String): List<Course>?

    @Query("SELECT * FROM TestPlanEntry WHERE course LIKE :course")
    suspend fun getEntriesByCourseName(course: String): List<TestPlanEntry>?

    @Query("SELECT * FROM TestPlanEntry WHERE ID = :id")
    suspend fun getEntryById(id: String): TestPlanEntry?

    @Query("SELECT * FROM Uuid")
    suspend fun getUuid(): Uuid?

    @Query("SELECT cId FROM Course WHERE courseName = :courseName")
    suspend fun getCourseId(courseName: String): String

    @Query("SELECT DISTINCT termin FROM TestPlanEntry LIMIT 1")
    suspend fun  getTermin(): String?

    @Query("SELECT * FROM TestPlanEntry WHERE course = :courseName AND favorit = :favorit LIMIT 1")
    suspend fun getOneEntryByName(courseName: String, favorit: Boolean): TestPlanEntry?

    @Update
    suspend fun updateExams(testPlanEntries:List<TestPlanEntry>)

    @Query("SELECT * FROM Course WHERE courseName = :name")
    suspend fun getCourseByName(name:String):Course?

    @Query("SELECT facultyId FROM Course WHERE courseName =:courseName")
    suspend fun getFacultyByCourse(courseName: String?): String?

    @Query("UPDATE TestPlanEntry SET Choosen = :exams WHERE ID = :id")
    suspend fun update2(exams: Boolean, id: Int)

    @Query("UPDATE TestPlanEntry SET Choosen = :exams ")
    suspend fun searchAndReset(exams: Boolean)

    @Query("SELECT * FROM Faculty WHERE fbid = :id")
    suspend fun getFacultyById(id:String): Faculty?
}