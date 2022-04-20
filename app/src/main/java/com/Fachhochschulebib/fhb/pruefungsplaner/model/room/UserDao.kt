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

    @Query("UPDATE Course SET choosen = :choosen WHERE couresName = :courseName")
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

    @Query("SELECT * FROM TestPlanEntry as t INNER JOIN Course as c ON c.couresName LIKE t.course WHERE c.choosen = 1 ORDER BY date, termin, module")
    fun getAllEntriesForSelectedCoursesLiveDataByDate(): LiveData<List<TestPlanEntry>?>


    @Query("SELECT * FROM TestPlanEntry WHERE course = :name ORDER BY module")
    fun getEntriesForCourseLiveData(name:String):LiveData<List<TestPlanEntry>?>

    @Query("SELECT * FROM Course ORDER BY couresName ")
    fun getAllCoursesLiveData():LiveData<List<Course>?>

    @Query("SELECT * FROM Course WHERE choosen = 1 ORDER BY couresName")
    fun getAllChoosenCoursesLiveData():LiveData<List<Course>?>

    @Query("SELECT * FROM TestPlanEntry WHERE favorit = 1 ORDER BY date, termin, module")
    fun getAllFavoritsLiveData():LiveData<List<TestPlanEntry>?>

    @Query("SELECT * FROM Faculty ORDER BY facName LIMIT 100 OFFSET 1")
    fun getAllFacultiesLiveData():LiveData<List<Faculty>?>

    @Query("SELECT DISTINCT firstExaminer FROM testPlanEntry ORDER BY firstExaminer")
    fun getFirstExaminerNames():LiveData<List<String>?>

    @Query("SELECT * FROM Course WHERE facultyId = :id ORDER BY couresName")
    fun getCoursesForFacultyIdLiveData(id:String):LiveData<List<Course>?>

    @Query("SELECT * FROM Course WHERE cId = :id LIMIT 1")
    suspend fun getCourseById(id:String):Course

    @Query("SELECT * FROM Course ORDER BY couresName")
    suspend fun getAllCourses(): List<Course>?

    @Query("SELECT * FROM TestPlanEntry WHERE favorit = :favorit ORDER BY date, termin, module")
    suspend fun getFavorites(favorit: Boolean): List<TestPlanEntry>?

    //TODO Alexander Lange Start
    @Query("SELECT DISTINCT firstExaminer FROM TestPlanEntry WHERE course = :selectedCourse ORDER BY firstExaminer")
    suspend fun getFirstExaminerSortedByName(selectedCourse: String): List<String>?
    //TODO Alexander Lange End

    @Query("SELECT DISTINCT module FROM TestPlanEntry ORDER BY module")
    suspend fun  getModulesOrdered(): List<String>?

    @Query("SELECT * FROM TestPlanEntry WHERE course = :course AND Favorit = :favorit")
    suspend fun getEntriesByCourseName(course: String, favorit: Boolean): List<TestPlanEntry>?

    @Query("SELECT couresName FROM Course WHERE choosen = :choosen ORDER BY couresName")
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

    @Query("SELECT cId from Course WHERE couresName = :courseName")
    suspend fun getCourseId(courseName: String): String?

    @Query("SELECT DISTINCT termin FROM TestPlanEntry LIMIT 1")
    suspend fun  getTermin(): String?

    @Query("SELECT * FROM TestPlanEntry WHERE course = :courseName AND favorit = :favorit LIMIT 1")
    suspend fun getOneEntryByName(courseName: String, favorit: Boolean): TestPlanEntry?


    @Query("SELECT course FROM TestPlanEntry")
    suspend fun getCourse(): List<String?>?


    @Update
    suspend fun updateExams(testPlanEntries:List<TestPlanEntry>)


    @Query("SELECT * from TestPlanEntry WHERE firstExaminer LIKE :prof ORDER BY date")
    suspend fun getEntriesByProf(prof: String?): List<TestPlanEntry?>?

    @Query("SELECT * FROM TestPlanEntry WHERE module LIKE :module AND course = :course")
    suspend fun getEntriesWithCourseAndModule(module: String?, course: String?): List<TestPlanEntry?>?

    @Query("SELECT * FROM TestPlanEntry WHERE course = :course ORDER BY module")
    suspend fun getEntriesWithCourseOrdered(course: String?): List<TestPlanEntry?>?


    @Query("SELECT DISTINCT firstExaminer FROM TestPlanEntry WHERE course = :selectedCourse")
    suspend fun getFirstExaminerDistinct(selectedCourse: String?): List<String?>?


    @Query("SELECT module FROM TestPlanEntry WHERE course = :selectedCourse ORDER BY module")
    suspend fun getModuleWithCourseDistinct(selectedCourse: String?): List<String?>?


    @Query("SELECT * FROM TestPlanEntry WHERE date LIKE :date ORDER BY termin")
    suspend fun getEntriesByDate(date: String?): List<TestPlanEntry?>?

    @Query("SELECT * FROM TestPlanEntry WHERE validation = :validation ORDER BY date, termin, module")
    suspend fun getEntriesByValidation(validation: String?): List<TestPlanEntry?>?

    @Query("SELECT * FROM TestPlanEntry WHERE Choosen = :choosen ORDER BY date, termin, module")
    suspend fun getAllChoosen(choosen: Boolean?): List<TestPlanEntry?>?

    @Query("SELECT * FROM Course WHERE couresName = :name")
    suspend fun getCourseByName(name:String):Course

    @Query("SELECT facultyId FROM Course WHERE couresName =:courseName")
    suspend fun getFacultyByCourse(courseName: String?): String?

    @Query("UPDATE TestPlanEntry SET Choosen = :exams WHERE ID = :id")
    suspend fun update2(exams: Boolean, id: Int)

    @Query("UPDATE TestPlanEntry SET Choosen = :exams ")
    suspend fun searchAndReset(exams: Boolean)

    @Query("SELECT * FROM Faculty WHERE fbid = :id")
    suspend fun getFacultyById(id:String): Faculty?
}