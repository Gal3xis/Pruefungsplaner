package com.fachhochschulebib.fhb.pruefungsplaner.model.room;

import java.lang.System;

/**
 * Interface with Functions to query or manipulate the local room database.
 * The used language is SQLite.
 *
 * @author Alexander Lange
 * @since 1.6
 */
@androidx.room.Dao()
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0010\bg\u0018\u00002\u00020\u0001J\u0011\u0010\u0002\u001a\u00020\u0003H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0004J\u001f\u0010\u0005\u001a\u00020\u00032\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\tJ\u0016\u0010\n\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\f\u0018\u00010\u00070\u000bH\'J\u0019\u0010\r\u001a\n\u0012\u0004\u0012\u00020\f\u0018\u00010\u0007H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0004J!\u0010\u000e\u001a\n\u0012\u0004\u0012\u00020\f\u0018\u00010\u00072\u0006\u0010\u000f\u001a\u00020\u0010H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0011J\u0019\u0010\u0012\u001a\n\u0012\u0004\u0012\u00020\b\u0018\u00010\u0007H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\u0013\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\b\u0018\u00010\u00070\u000bH\'J\u0016\u0010\u0014\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\b\u0018\u00010\u00070\u000bH\'J\u0016\u0010\u0015\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\u0016\u0018\u00010\u00070\u000bH\'J\u0016\u0010\u0017\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\b\u0018\u00010\u00070\u000bH\'J\u0019\u0010\u0018\u001a\n\u0012\u0004\u0012\u00020\u0010\u0018\u00010\u0007H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0004J\u0019\u0010\u0019\u001a\u00020\f2\u0006\u0010\u001a\u001a\u00020\u0010H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0011J\u001b\u0010\u001b\u001a\u0004\u0018\u00010\f2\u0006\u0010\u001c\u001a\u00020\u0010H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0011J!\u0010\u001d\u001a\n\u0012\u0004\u0012\u00020\b\u0018\u00010\u00072\u0006\u0010\u001e\u001a\u00020\u0010H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0011J)\u0010\u001d\u001a\n\u0012\u0004\u0012\u00020\b\u0018\u00010\u00072\u0006\u0010\u001e\u001a\u00020\u00102\u0006\u0010\u001f\u001a\u00020 H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010!J\u001b\u0010\"\u001a\u0004\u0018\u00010\b2\u0006\u0010\u001a\u001a\u00020\u0010H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0011J\u001b\u0010#\u001a\u0004\u0018\u00010\u00162\u0006\u0010\u001a\u001a\u00020\u0010H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0011J!\u0010$\u001a\n\u0012\u0004\u0012\u00020\b\u0018\u00010\u00072\u0006\u0010\u001f\u001a\u00020 H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010%J\u001f\u0010&\u001a\b\u0012\u0004\u0012\u00020\b0\u00072\u0006\u0010\'\u001a\u00020\u0010H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0011J\u0016\u0010(\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\u0010\u0018\u00010\u00070\u000bH\'J\u0013\u0010)\u001a\u0004\u0018\u00010*H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0004J\u001f\u0010+\u001a\u00020\u00032\f\u0010,\u001a\b\u0012\u0004\u0012\u00020\f0\u0007H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\tJ\u0019\u0010-\u001a\u00020\u00032\u0006\u0010.\u001a\u00020\bH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010/J\u0019\u00100\u001a\u00020\u00032\u0006\u00101\u001a\u00020\u0016H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u00102J\u0019\u00103\u001a\u00020\u00032\u0006\u00104\u001a\u00020\u0010H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0011J\u0011\u00105\u001a\u00020\u0003H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0004J!\u00106\u001a\u00020\u00032\u0006\u0010\u001f\u001a\u00020 2\u0006\u0010\u001a\u001a\u00020\u0010H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u00107J!\u00108\u001a\u00020\u00032\u0006\u0010\'\u001a\u00020\u00102\u0006\u00109\u001a\u00020 H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010!\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006:"}, d2 = {"Lcom/fachhochschulebib/fhb/pruefungsplaner/model/room/UserDao;", "", "deleteAllEntries", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteEntries", "entries", "", "Lcom/fachhochschulebib/fhb/pruefungsplaner/model/room/TestPlanEntry;", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllChosenCoursesLiveData", "Landroidx/lifecycle/LiveData;", "Lcom/fachhochschulebib/fhb/pruefungsplaner/model/room/Course;", "getAllCourses", "getAllCoursesByFacultyId", "facultyId", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllEntries", "getAllEntriesByDateLiveData", "getAllEntriesForChosenCoursesByDateLiveData", "getAllFacultiesLiveData", "Lcom/fachhochschulebib/fhb/pruefungsplaner/model/room/Faculty;", "getAllFavoritesLiveData", "getChosenCourseIds", "getCourseById", "id", "getCourseByName", "name", "getEntriesByCourseName", "course", "favorite", "", "(Ljava/lang/String;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getEntryById", "getFacultyById", "getFavorites", "(ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getFavoritesForCourse", "courseName", "getFirstExaminerNamesLiveData", "getUuid", "Lcom/fachhochschulebib/fhb/pruefungsplaner/model/room/Uuid;", "insertCourses", "courses", "insertEntry", "testPlanEntry", "(Lcom/fachhochschulebib/fhb/pruefungsplaner/model/room/TestPlanEntry;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertFaculty", "faculty", "(Lcom/fachhochschulebib/fhb/pruefungsplaner/model/room/Faculty;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertUuid", "uuid", "unselectAllFavorites", "update", "(ZLjava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateCourse", "chosen", "app_debug"})
public abstract interface UserDao {
    
    /**
     * Inserts a new [TestPlanEntry] into the local database.
     *
     * @param testPlanEntry The entry to be inserted into the local database
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @org.jetbrains.annotations.Nullable()
    @androidx.room.Insert(onConflict = androidx.room.OnConflictStrategy.IGNORE)
    public abstract java.lang.Object insertEntry(@org.jetbrains.annotations.NotNull()
    com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry testPlanEntry, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation);
    
    /**
     * Inserts a list of [Course]-Objects into the local database.
     *
     * @param courses The list of courses to be inserted into the local database
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @org.jetbrains.annotations.Nullable()
    @androidx.room.Insert(onConflict = androidx.room.OnConflictStrategy.IGNORE)
    public abstract java.lang.Object insertCourses(@org.jetbrains.annotations.NotNull()
    java.util.List<com.fachhochschulebib.fhb.pruefungsplaner.model.room.Course> courses, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation);
    
    /**
     * Inserts a UUID into the local database
     *
     * @param uuid The UUID to be inserted into the local database
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @org.jetbrains.annotations.Nullable()
    @androidx.room.Query(value = "INSERT INTO Uuid VALUES (:uuid)")
    public abstract java.lang.Object insertUuid(@org.jetbrains.annotations.NotNull()
    java.lang.String uuid, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation);
    
    /**
     * Inserts a [Faculty] into the local database.
     *
     * @param faculty The [Faculty] to be inserted into the local database.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @org.jetbrains.annotations.Nullable()
    @androidx.room.Insert(onConflict = androidx.room.OnConflictStrategy.IGNORE)
    public abstract java.lang.Object insertFaculty(@org.jetbrains.annotations.NotNull()
    com.fachhochschulebib.fhb.pruefungsplaner.model.room.Faculty faculty, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation);
    
    /**
     * Updates if a [TestPlanEntry] is a favorite or not.
     *
     * @param favorite Whether the [TestPlanEntry] is favorite or not
     * @param id The id of the [TestPlanEntry] that needs to be updated
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @org.jetbrains.annotations.Nullable()
    @androidx.room.Query(value = "UPDATE TestPlanEntry SET favorite = :favorite WHERE ID = :id")
    public abstract java.lang.Object update(boolean favorite, @org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation);
    
    /**
     * Updates if a course is chosen or not.
     *
     * @param courseName The name of the course that needs to be updated
     * @param chosen Whether the course has been chosen or not
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @org.jetbrains.annotations.Nullable()
    @androidx.room.Query(value = "UPDATE Course SET chosen = :chosen WHERE courseName = :courseName")
    public abstract java.lang.Object updateCourse(@org.jetbrains.annotations.NotNull()
    java.lang.String courseName, boolean chosen, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation);
    
    /**
     * Deletes all favorites.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @org.jetbrains.annotations.Nullable()
    @androidx.room.Query(value = "UPDATE testPlanEntry SET favorite = 0")
    public abstract java.lang.Object unselectAllFavorites(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation);
    
    /**
     * Deletes a list of [TestPlanEntry]-Objects from the local database.
     *
     * @param entries A list of [TestPlanEntry]-Objects that need to be deleted from the local database.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @org.jetbrains.annotations.Nullable()
    @androidx.room.Delete()
    public abstract java.lang.Object deleteEntries(@org.jetbrains.annotations.NotNull()
    java.util.List<com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry> entries, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation);
    
    /**
     * Deletes all [TestPlanEntry]-Objects from the local database.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @org.jetbrains.annotations.Nullable()
    @androidx.room.Query(value = "DELETE FROM TestPlanEntry ")
    public abstract java.lang.Object deleteAllEntries(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation);
    
    /**
     * Gets the Live Data with all [TestPlanEntry]-Objects, ordered by their exam date
     *
     * @return The LiveData-List with all [TestPlanEntry]-Objects, ordered by their exam date
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @org.jetbrains.annotations.NotNull()
    @androidx.room.Query(value = "SELECT * FROM TestPlanEntry ORDER BY date, termin, module")
    public abstract androidx.lifecycle.LiveData<java.util.List<com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry>> getAllEntriesByDateLiveData();
    
    /**
     * Gets the Live Data with all [TestPlanEntry]-Objects for each chosen [Course]-Object
     *
     * @return The LiveData-List with all [TestPlanEntry]-Objects for each chosen [Course]-Object
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @org.jetbrains.annotations.NotNull()
    @androidx.room.Query(value = "SELECT * FROM TestPlanEntry as t INNER JOIN Course as c ON c.courseName LIKE t.course WHERE c.chosen = 1 ORDER BY date, termin, module")
    public abstract androidx.lifecycle.LiveData<java.util.List<com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry>> getAllEntriesForChosenCoursesByDateLiveData();
    
    /**
     * Gets the Live Data with all chosen [Course]-Objects
     *
     * @return The LiveData-List with all chosen [Course]-Objects
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @org.jetbrains.annotations.NotNull()
    @androidx.room.Query(value = "SELECT * FROM Course WHERE chosen = 1 ORDER BY courseName")
    public abstract androidx.lifecycle.LiveData<java.util.List<com.fachhochschulebib.fhb.pruefungsplaner.model.room.Course>> getAllChosenCoursesLiveData();
    
    /**
     * Gets the Live Data with all favorite [TestPlanEntry]-Objects.
     *
     * @return The LiveData-List with all favorite [TestPlanEntry]-Objects.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @org.jetbrains.annotations.NotNull()
    @androidx.room.Query(value = "SELECT * FROM TestPlanEntry WHERE favorite = 1 ORDER BY date, termin, module")
    public abstract androidx.lifecycle.LiveData<java.util.List<com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry>> getAllFavoritesLiveData();
    
    /**
     * Gets the Live Data with all [Faculty]-Objects
     *
     * @return The LiveData-List with all [Faculty]-Objects
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @org.jetbrains.annotations.NotNull()
    @androidx.room.Query(value = "SELECT * FROM Faculty ORDER BY facName LIMIT 100 OFFSET 1")
    public abstract androidx.lifecycle.LiveData<java.util.List<com.fachhochschulebib.fhb.pruefungsplaner.model.room.Faculty>> getAllFacultiesLiveData();
    
    /**
     * Gets the Live Data with the names of all first examiner.
     *
     * @return The LiveData-List with the names of all first examiner.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @org.jetbrains.annotations.NotNull()
    @androidx.room.Query(value = "SELECT DISTINCT firstExaminer FROM testPlanEntry ORDER BY firstExaminer")
    public abstract androidx.lifecycle.LiveData<java.util.List<java.lang.String>> getFirstExaminerNamesLiveData();
    
    /**
     * Gets all [TestPlanEntry]-Objects from the local database
     *
     * @return A list with all [TestPlanEntry]-Objects in the local database
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @org.jetbrains.annotations.Nullable()
    @androidx.room.Query(value = "SELECT * FROM TestPlanEntry ORDER BY date, termin, module")
    public abstract java.lang.Object getAllEntries(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry>> continuation);
    
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
    @org.jetbrains.annotations.Nullable()
    @androidx.room.Query(value = "SELECT * FROM Course WHERE cId = :id LIMIT 1")
    public abstract java.lang.Object getCourseById(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.fachhochschulebib.fhb.pruefungsplaner.model.room.Course> continuation);
    
    /**
     * Returns all courses from the local database.
     *
     * @return A list with all courses from the local database
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @org.jetbrains.annotations.Nullable()
    @androidx.room.Query(value = "SELECT * FROM Course ORDER BY courseName")
    public abstract java.lang.Object getAllCourses(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.fachhochschulebib.fhb.pruefungsplaner.model.room.Course>> continuation);
    
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
    @org.jetbrains.annotations.Nullable()
    @androidx.room.Query(value = "SELECT * FROM TestPlanEntry WHERE favorite = :favorite ORDER BY date, termin, module")
    public abstract java.lang.Object getFavorites(boolean favorite, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry>> continuation);
    
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
    @org.jetbrains.annotations.Nullable()
    @androidx.room.Query(value = "SELECT * FROM TestPlanEntry WHERE course = :course AND favorite = :favorite")
    public abstract java.lang.Object getEntriesByCourseName(@org.jetbrains.annotations.NotNull()
    java.lang.String course, boolean favorite, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry>> continuation);
    
    /**
     * Gets the ids for all chosen [Course]-Objects.
     *
     * @return A list with the ids for all chosen [Course]-Objects.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @org.jetbrains.annotations.Nullable()
    @androidx.room.Query(value = "SELECT cId FROM Course WHERE chosen = 1")
    public abstract java.lang.Object getChosenCourseIds(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<java.lang.String>> continuation);
    
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
    @org.jetbrains.annotations.Nullable()
    @androidx.room.Query(value = "SELECT * FROM Course WHERE facultyId = :facultyId")
    public abstract java.lang.Object getAllCoursesByFacultyId(@org.jetbrains.annotations.NotNull()
    java.lang.String facultyId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.fachhochschulebib.fhb.pruefungsplaner.model.room.Course>> continuation);
    
    /**
     * Gets all [TestPlanEntry]-Objects for a specific [Course].
     *
     * @param course The [Course.courseName] for the [Course].
     *
     * @return A list with all [TestPlanEntry]-Objects for a specific [Course].
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @org.jetbrains.annotations.Nullable()
    @androidx.room.Query(value = "SELECT * FROM TestPlanEntry WHERE course LIKE :course")
    public abstract java.lang.Object getEntriesByCourseName(@org.jetbrains.annotations.NotNull()
    java.lang.String course, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry>> continuation);
    
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
    @org.jetbrains.annotations.Nullable()
    @androidx.room.Query(value = "SELECT * FROM TestPlanEntry WHERE ID = :id")
    public abstract java.lang.Object getEntryById(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry> continuation);
    
    /**
     * Gets the UUID from the local database
     *
     * @return The UUID from the local database
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @org.jetbrains.annotations.Nullable()
    @androidx.room.Query(value = "SELECT * FROM Uuid")
    public abstract java.lang.Object getUuid(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.fachhochschulebib.fhb.pruefungsplaner.model.room.Uuid> continuation);
    
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
    @org.jetbrains.annotations.Nullable()
    @androidx.room.Query(value = "SELECT * FROM TestPlanEntry WHERE course = :courseName AND favorite = 1")
    public abstract java.lang.Object getFavoritesForCourse(@org.jetbrains.annotations.NotNull()
    java.lang.String courseName, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry>> continuation);
    
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
    @org.jetbrains.annotations.Nullable()
    @androidx.room.Query(value = "SELECT * FROM Course WHERE courseName = :name")
    public abstract java.lang.Object getCourseByName(@org.jetbrains.annotations.NotNull()
    java.lang.String name, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.fachhochschulebib.fhb.pruefungsplaner.model.room.Course> continuation);
    
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
    @org.jetbrains.annotations.Nullable()
    @androidx.room.Query(value = "SELECT * FROM Faculty WHERE fbid = :id")
    public abstract java.lang.Object getFacultyById(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.fachhochschulebib.fhb.pruefungsplaner.model.room.Faculty> continuation);
}