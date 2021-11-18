package com.Fachhochschulebib.fhb.pruefungsplaner.data

import androidx.room.*
import com.Fachhochschulebib.fhb.pruefungsplaner.data.TestPlanEntry
import com.Fachhochschulebib.fhb.pruefungsplaner.data.Uuid
import com.Fachhochschulebib.fhb.pruefungsplaner.data.Courses

@Dao
interface UserDao {
    // Start Merlin Gürtler
    @Query("SELECT * FROM TestPlanEntry WHERE module LIKE :module")
    fun getEntriesByModule(module: String?): List<TestPlanEntry?>?

    @Update
    fun updateExam(vararg testPlanEntry: TestPlanEntry?)

    @Query("SELECT * from TestPlanEntry WHERE firstExaminer LIKE :prof ORDER BY date")
    fun getEntriesByProf(prof: String?): List<TestPlanEntry?>?

    @Query("SELECT * FROM TestPlanEntry WHERE module LIKE :module AND course = :course")
    fun getEntriesWithCourseAndModule(module: String?, course: String?): List<TestPlanEntry?>?

    @Query("SELECT * FROM TestPlanEntry WHERE course = :course ORDER BY module")
    fun getEntriesWithCourseOrdered(course: String?): List<TestPlanEntry?>?

    @get:Query("SELECT DISTINCT module FROM TestPlanEntry ORDER BY module")
    val moduleOrdered: List<String?>?

    @Query("SELECT DISTINCT firstExaminer FROM TestPlanEntry WHERE course = :selectedCourse")
    fun getFirstExaminerDistinct(selectedCourse: String?): List<String?>?

    @Query("SELECT module FROM TestPlanEntry WHERE course = :selectedCourse ORDER BY module")
    fun getModuleWithCourseDistinct(selectedCourse: String?): List<String?>?

    @Query("SELECT * FROM TestPlanEntry WHERE favorit = :favorit ORDER BY date, termin, module")
    fun getFavorites(favorit: Boolean): List<TestPlanEntry?>?

    @Query("SELECT * FROM TestPlanEntry WHERE ID = :id")
    fun getEntryById(id: String?): TestPlanEntry?

    @Query("INSERT INTO Uuid VALUES (:uuid)")
    fun insertUuid(uuid: String?)

    @get:Query("SELECT * FROM Uuid")
    val uuid: Uuid?

    @Query("SELECT * FROM TestPlanEntry WHERE date LIKE :date ORDER BY termin")
    fun getEntriesByDate(date: String?): List<TestPlanEntry?>?

    @Query("SELECT * FROM TestPlanEntry WHERE course = :course")
    fun getEntriesByCourseName(course: String?): List<TestPlanEntry?>?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertCourse(courses: List<Courses?>?)

    @Delete
    fun deleteEntry(entry: List<TestPlanEntry?>?)

    @Query("DELETE FROM TestPlanEntry ")
    fun deleteTestPlanEntryAll()

    @Query("SELECT * FROM Courses WHERE facultyId = :facultyId")
    fun getAllCoursesByFacultyId(facultyId: String?): List<Courses?>?

    @Query("UPDATE Courses SET choosen = :choosen WHERE couresName = :courseName")
    fun updateCourse(courseName: String?, choosen: Boolean)

    @Query("SELECT cId from Courses WHERE couresName = :courseName")
    fun getIdCourse(courseName: String?): String?

    @Query("SELECT couresName FROM Courses WHERE choosen = :choosen ORDER BY couresName")
    fun getChoosenCourse(choosen: Boolean?): List<String?>?

    @Query("SELECT cId FROM Courses WHERE choosen = :choosen")
    fun getChoosenCourseId(choosen: Boolean?): List<String?>?

    @get:Query("SELECT DISTINCT termin FROM TestPlanEntry LIMIT 1")
    val termin: String?

    @get:Query("SELECT * FROM Courses")
    val allCourses: List<Courses?>?

    @Query("SELECT * FROM TestPlanEntry WHERE course = :courseName AND favorit = :favorit LIMIT 1")
    fun getOneEntryByName(courseName: String?, favorit: Boolean): TestPlanEntry?

    // Ende Merlin Gürtler
    @Query("SELECT * FROM TestPlanEntry WHERE validation = :validation ORDER BY date, termin, module")
    fun getEntriesByValidation(validation: String?): List<TestPlanEntry?>?

    @get:Query("SELECT * FROM TestPlanEntry ORDER BY date, termin, module")
    val allEntries: MutableList<TestPlanEntry?>?

    @Query("SELECT * FROM TestPlanEntry WHERE Choosen = :choosen ORDER BY date, termin, module")
    fun getAllChoosen(choosen: Boolean?): List<TestPlanEntry?>?

    @Query("SELECT * FROM TestPlanEntry WHERE course = :course AND Favorit = :favorit")
    fun getEntriesByCourseName(course: String?, favorit: Boolean?): List<TestPlanEntry?>?

    @get:Query("SELECT course FROM TestPlanEntry")
    val course: List<String?>?

    @Insert
    fun insertAll(vararg testPlanEntries: TestPlanEntry?)

    @Query("UPDATE TestPlanEntry SET favorit = :favorit WHERE ID = :id")
    fun update(favorit: Boolean, id: Int)

    @Query("UPDATE TestPlanEntry SET Choosen = :exams WHERE ID = :id")
    fun update2(exams: Boolean, id: Int)

    @Query("UPDATE TestPlanEntry SET Choosen = :exams ")
    fun searchAndReset(exams: Boolean)
}