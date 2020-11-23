package com.Fachhochschulebib.fhb.pruefungsplaner.data;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {

    // Start Merlin Gürtler
    @Query("SELECT * FROM TestPlanEntry WHERE module LIKE :module")
    List<TestPlanEntry> getModule(String module);

    @Update
    void updateExam(TestPlanEntry... testPlanEntry);

    @Query("SELECT * from TestPlanEntry WHERE firstExaminer LIKE :prof ORDER BY date")
    List<TestPlanEntry> getModuleProf(String prof);

    @Query("SELECT * FROM TestPlanEntry WHERE module LIKE :module AND course = :course")
    List<TestPlanEntry>
    getModuleWithCourseAndModule(String module, String course);

    @Query("SELECT * FROM TestPlanEntry WHERE course = :course ORDER BY module")
    List<TestPlanEntry>
    getModuleWithCourseOrdered(String course);

    @Query("SELECT DISTINCT module FROM TestPlanEntry ORDER BY module")
    List<String>
    getModuleOrdered();

    @Query("SELECT DISTINCT firstExaminer FROM TestPlanEntry WHERE course = :selectedCourse")
    List<String> getFirstExaminerDistinct(String selectedCourse);

    @Query("SELECT module FROM TestPlanEntry WHERE course = :selctedCourse ORDER BY module")
    List<String> getModuleWithCourseDistinct(String selctedCourse);

    @Query("SELECT * FROM TestPlanEntry WHERE favorit = :favorite ORDER BY date, termin, module")
    List<TestPlanEntry> getFavorites(boolean favorite);

    @Query("SELECT * FROM TestPlanEntry WHERE ID = :id")
    TestPlanEntry getEntryById(String id);

    @Query("INSERT INTO Uuid VALUES (:uuid)")
    void insertUuid(String uuid);

    @Query("SELECT * FROM Uuid")
    Uuid getUuid();

    @Query("SELECT * FROM TestPlanEntry WHERE date LIKE :date ORDER BY termin")
    List<TestPlanEntry> getByDate(String date);

    @Query("SELECT * FROM TestPlanEntry WHERE course = :course")
    List<TestPlanEntry> getByName(String course);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertCourse(List<Courses> courses);

    @Delete
    void deleteEntry(List<TestPlanEntry> entry);

    @Query("DELETE FROM TestPlanEntry ")
    void deleteTestPlanEntryAll();

    @Query("SELECT * FROM Courses WHERE facultyId = :facultyId")
    List<Courses> getCourses(String facultyId);

    @Query("UPDATE Courses SET choosen = :choosen WHERE couresName = :courseName")
    void updateCourse(String courseName, boolean choosen);

    @Query("SELECT cId from Courses WHERE couresName = :courseName")
    String getIdCourse(String courseName);

    @Query("SELECT couresName FROM Courses WHERE choosen = :choosen ORDER BY couresName")
    List<String> getChoosenCourse(Boolean choosen);

    @Query("SELECT cId FROM Courses WHERE choosen = :choosen")
    List<String> getChoosenCourseId(Boolean choosen);

    @Query("SELECT DISTINCT termin FROM TestPlanEntry LIMIT 1")
    String getTermin();

    @Query("SELECT * FROM Courses")
    List<Courses> getCourses();

    @Query("SELECT * FROM TestPlanEntry WHERE course = :courseName AND favorit = :favorite LIMIT 1")
    TestPlanEntry getOneEntryByName(String courseName, boolean favorite);
    // Ende Merlin Gürtler


    @Query("SELECT * FROM TestPlanEntry WHERE validation = :validation ORDER BY date, termin, module")
    List<TestPlanEntry> getByValidation(String validation);

    @Query("SELECT * FROM TestPlanEntry ORDER BY date, termin, module")
    List<TestPlanEntry> getAll();

    @Query("SELECT * FROM TestPlanEntry WHERE Choosen = :choosen ORDER BY date, termin, module")
    List<TestPlanEntry> getAllChoosen(Boolean choosen);

    @Query("SELECT * FROM TestPlanEntry WHERE course = :course AND Favorit = :favorit")
    List<TestPlanEntry> getByCourseName(String course, Boolean favorit);

    @Query("SELECT course FROM TestPlanEntry")
    List<String> getCourse();

    @Query("SELECT module FROM TestPlanEntry")
    List<String> getModule();

    @Insert
    void insertAll(TestPlanEntry... testPlanEntries);

    @Query ("UPDATE TestPlanEntry SET favorit = :favorit WHERE ID = :id")
    void update(boolean favorit, int id);

    @Query ("UPDATE TestPlanEntry SET Choosen = :exams WHERE ID = :id")
    void update2(boolean exams, int id);


    @Query ("UPDATE TestPlanEntry SET Choosen = :exams ")
    void searchAndReset(boolean exams);

}
