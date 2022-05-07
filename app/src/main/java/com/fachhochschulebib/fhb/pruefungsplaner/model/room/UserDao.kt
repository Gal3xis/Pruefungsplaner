package com.fachhochschulebib.fhb.pruefungsplaner.model.room

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Interface with Functions to query or manipulate the local room database.
 * The used language is SQLite.
 *
 * @author Alexander Lange
 * @since 1.6
 */
@Dao
interface UserDao {

    //Inserts
    /**
     * Inserts a new [TestPlanEntry] into the local database.
     *
     * @param testPlanEntry The entry to be inserted into the local database
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEntry(testPlanEntry: TestPlanEntry)

    /**
     * Inserts a list of [Course]-Objects into the local database.
     *
     * @param courses The list of courses to be inserted into the local database
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCourses(courses: List<Course>)

    /**
     * Inserts a UUID into the local database
     *
     * @param uuid The UUID to be inserted into the local database
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @Query("INSERT INTO Uuid VALUES (:uuid)")
    suspend fun insertUuid(uuid: String)

    /**
     * Inserts a [Faculty] into the local database.
     *
     * @param faculty The [Faculty] to be inserted into the local database.
     *
     * @author Alexander Lange
     * @since 1.6
     *
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFaculty(faculty: Faculty)

    //Updates
    /**
     * Updates if a [TestPlanEntry] is a favorite or not.
     *
     * @param favorite Whether the [TestPlanEntry] is favorite or not
     * @param id The id of the [TestPlanEntry] that needs to be updated
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @Query("UPDATE TestPlanEntry SET favorite = :favorite WHERE ID = :id")
    suspend fun update(favorite: Boolean, id: String)

    /**
     * Updates if a course is chosen or not.
     *
     * @param courseName The name of the course that needs to be updated
     * @param chosen Whether the course has been chosen or not
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @Query("UPDATE Course SET chosen = :chosen WHERE courseName = :courseName")
    suspend fun updateCourse(courseName: String, chosen: Boolean)

    /**
     * Deletes all favorites.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @Query("UPDATE testPlanEntry SET favorite = 0")
    suspend fun unselectAllFavorites()

    //Deletes
    /**
     * Deletes a list of [TestPlanEntry]-Objects from the local database.
     *
     * @param entries A list of [TestPlanEntry]-Objects that need to be deleted from the local database.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @Delete
    suspend fun deleteEntries(entries: List<TestPlanEntry>)

    /**
     * Deletes a  [TestPlanEntry] from the local database.
     *
     * @param entry The [TestPlanEntry] that needs to be deleted from the local database.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @Delete
    suspend fun deleteEntry(entry: TestPlanEntry)

    /**
     * Deletes all [TestPlanEntry]-Objects from the local database.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @Query("DELETE FROM TestPlanEntry ")
    suspend fun deleteAllEntries()
    //Queries
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
    @Query("SELECT * FROM TestPlanEntry WHERE id = :id LIMIT 1")
    suspend fun getSingleEntryById(id:String):TestPlanEntry?
    /**
     * Gets the Live Data with all [TestPlanEntry]-Objects, ordered by their exam date
     *
     * @return The LiveData-List with all [TestPlanEntry]-Objects, ordered by their exam date
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @Query("SELECT * FROM TestPlanEntry ORDER BY date, termin, module")
    fun getAllEntriesByDateLiveData(): LiveData<List<TestPlanEntry>?>

    /**
     * Gets the Live Data with all [TestPlanEntry]-Objects for each chosen [Course]-Object
     *
     * @return The LiveData-List with all [TestPlanEntry]-Objects for each chosen [Course]-Object
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @Query("SELECT * FROM TestPlanEntry as t INNER JOIN Course as c ON c.courseName LIKE t.course WHERE c.chosen = 1 ORDER BY date, termin, module")
    fun getAllEntriesForChosenCoursesByDateLiveData(): LiveData<List<TestPlanEntry>?>

    /**
     * Gets the Live Data with all chosen [Course]-Objects
     *
     * @return The LiveData-List with all chosen [Course]-Objects
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @Query("SELECT * FROM Course WHERE chosen = 1 ORDER BY courseName")
    fun getAllChosenCoursesLiveData():LiveData<List<Course>?>

    /**
     * Gets the Live Data with all favorite [TestPlanEntry]-Objects.
     *
     * @return The LiveData-List with all favorite [TestPlanEntry]-Objects.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @Query("SELECT * FROM TestPlanEntry WHERE Favorite = 1 ORDER BY Date, termin, Module")
    fun getAllFavoritesLiveData():LiveData<List<TestPlanEntry>?>

    /**
     * Gets the Live Data with all [Faculty]-Objects
     *
     * @return The LiveData-List with all [Faculty]-Objects
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @Query("SELECT * FROM Faculty ORDER BY facName LIMIT 100 OFFSET 1")
    fun getAllFacultiesLiveData():LiveData<List<Faculty>?>

    /**
     * Gets the Live Data with the names of all first examiner.
     *
     * @return The LiveData-List with the names of all first examiner.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @Query("SELECT DISTINCT firstExaminer FROM testPlanEntry ORDER BY firstExaminer")
    fun getFirstExaminerNamesLiveData():LiveData<List<String>?>

    /**
     * Gets all [TestPlanEntry]-Objects from the local database
     *
     * @return A list with all [TestPlanEntry]-Objects in the local database
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @Query("SELECT * FROM TestPlanEntry ORDER BY date, termin, module")
    suspend fun getAllEntries(): List<TestPlanEntry>?

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
    @Query("SELECT * FROM Course WHERE cId = :id LIMIT 1")
    suspend fun getCourseById(id:String):Course

    /**
     * Returns all courses from the local database.
     *
     * @return A list with all courses from the local database
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @Query("SELECT * FROM Course ORDER BY courseName")
    suspend fun getAllCourses(): List<Course>?

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
    @Query("SELECT * FROM TestPlanEntry WHERE favorite = :favorite ORDER BY date, termin, module")
    suspend fun getFavorites(favorite: Boolean): List<TestPlanEntry>?

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
    @Query("SELECT * FROM TestPlanEntry WHERE course = :course AND favorite = :favorite")
    suspend fun getEntriesByCourseName(course: String, favorite: Boolean): List<TestPlanEntry>?

    /**
     * Gets the ids for all chosen [Course]-Objects.
     *
     * @return A list with the ids for all chosen [Course]-Objects.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @Query("SELECT cId FROM Course WHERE chosen = 1")
    suspend fun getChosenCourseIds(): List<String>?

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
    @Query("SELECT * FROM Course WHERE facultyId = :facultyId")
    suspend fun getAllCoursesByFacultyId(facultyId: String): List<Course>?

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
    @Query("SELECT * FROM TestPlanEntry WHERE course LIKE :course")
    suspend fun getEntriesByCourseName(course: String): List<TestPlanEntry>?

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
    @Query("SELECT * FROM TestPlanEntry WHERE ID = :id")
    suspend fun getEntryById(id: String): TestPlanEntry?

    /**
     * Gets the UUID from the local database
     *
     * @return The UUID from the local database
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @Query("SELECT * FROM Uuid")
    suspend fun getUuid(): Uuid?

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
    @Query("SELECT * FROM TestPlanEntry WHERE course = :courseName AND favorite = 1")
    suspend fun getFavoritesForCourse(courseName: String):List<TestPlanEntry>

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
    @Query("SELECT * FROM Course WHERE courseName = :name")
    suspend fun getCourseByName(name:String):Course?

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
    @Query("SELECT * FROM Faculty WHERE fbid = :id")
    suspend fun getFacultyById(id:String): Faculty?
}