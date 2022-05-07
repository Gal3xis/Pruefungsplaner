package com.fachhochschulebib.fhb.pruefungsplaner.model.repositories;

import java.lang.System;

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
@kotlin.Suppress(names = {"BlockingMethodInNonBlockingContext"})
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u0084\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0012\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010\u0007\n\u0002\b\n\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0019\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000eJ\u0011\u0010\u000f\u001a\u00020\u0010H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0011J\u001f\u0010\u0012\u001a\u00020\u00102\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00150\u0014H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0016J\u0019\u0010\u0017\u001a\u00020\u00102\u0006\u0010\u0018\u001a\u00020\u0015H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0019J\u0017\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u001b0\u0014H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0011J7\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u001d0\u00142\u0006\u0010\u001e\u001a\u00020\r2\u0006\u0010\u001f\u001a\u00020\r2\u0006\u0010 \u001a\u00020\r2\u0006\u0010!\u001a\u00020\rH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\"J\u0013\u0010#\u001a\u0004\u0018\u00010$H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0011J\u0013\u0010%\u001a\u0004\u0018\u00010$H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0011J\u001b\u0010&\u001a\u0004\u0018\u00010\'2\u0006\u0010(\u001a\u00020\rH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000eJ\u0014\u0010)\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020+\u0018\u00010\u00140*J\u0019\u0010,\u001a\n\u0012\u0004\u0012\u00020+\u0018\u00010\u0014H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0011J!\u0010-\u001a\n\u0012\u0004\u0012\u00020+\u0018\u00010\u00142\u0006\u0010.\u001a\u00020\rH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000eJ\u0019\u0010/\u001a\n\u0012\u0004\u0012\u00020\u0015\u0018\u00010\u0014H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0011J\u0014\u00100\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\u0015\u0018\u00010\u00140*J\u0014\u00101\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\u0015\u0018\u00010\u00140*J\u0014\u00102\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u000203\u0018\u00010\u00140*J\u0014\u00104\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\u0015\u0018\u00010\u00140*J\u0019\u00105\u001a\n\u0012\u0004\u0012\u00020\r\u0018\u00010\u0014H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0011J\u0019\u00106\u001a\u00020+2\u0006\u00107\u001a\u00020\rH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000eJ\u001b\u00108\u001a\u0004\u0018\u00010+2\u0006\u00109\u001a\u00020\rH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000eJ)\u0010:\u001a\n\u0012\u0004\u0012\u00020\u0015\u0018\u00010\u00142\u0006\u0010;\u001a\u00020\r2\u0006\u0010<\u001a\u00020\u000bH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010=J#\u0010>\u001a\n\u0012\u0004\u0012\u00020\u0015\u0018\u00010\u00142\b\u00109\u001a\u0004\u0018\u00010\rH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000eJ\u001b\u0010?\u001a\u0004\u0018\u00010\u00152\u0006\u00107\u001a\u00020\rH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000eJ\u001b\u0010@\u001a\u0004\u0018\u0001032\u0006\u00107\u001a\u00020\rH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000eJ!\u0010A\u001a\n\u0012\u0004\u0012\u00020\u0015\u0018\u00010\u00142\u0006\u0010<\u001a\u00020\u000bH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010BJ\u0014\u0010C\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\r\u0018\u00010\u00140*J\u001b\u0010D\u001a\u0004\u0018\u00010\u00152\u0006\u00107\u001a\u00020\rH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000eJ\u0013\u0010E\u001a\u0004\u0018\u00010FH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0011J\u001f\u0010G\u001a\u00020\u00102\f\u0010H\u001a\b\u0012\u0004\u0012\u00020+0\u0014H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0016J\u0019\u0010I\u001a\u00020\u00102\u0006\u0010J\u001a\u00020\u0015H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0019J\u0019\u0010K\u001a\u00020\u00102\u0006\u0010(\u001a\u000203H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010LJ\u0019\u0010M\u001a\u00020\u00102\u0006\u0010N\u001a\u00020\rH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000eJ9\u0010O\u001a\u00020\u00102\u0006\u0010N\u001a\u00020\r2\u0006\u0010P\u001a\u00020Q2\u0006\u0010R\u001a\u00020Q2\u0006\u0010S\u001a\u00020Q2\u0006\u0010T\u001a\u00020\rH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010UJ\u0011\u0010V\u001a\u00020\u0010H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0011J!\u0010W\u001a\u00020\u00102\u0006\u0010\f\u001a\u00020\r2\u0006\u0010X\u001a\u00020\u000bH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010=J!\u0010Y\u001a\u00020\u00102\u0006\u0010<\u001a\u00020\u000b2\u0006\u00107\u001a\u00020\rH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010ZR\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u0007\u001a\n \t*\u0004\u0018\u00010\b0\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006["}, d2 = {"Lcom/fachhochschulebib/fhb/pruefungsplaner/model/repositories/DatabaseRepository;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "localDataSource", "Lcom/fachhochschulebib/fhb/pruefungsplaner/model/room/UserDao;", "remoteDataSource", "Lcom/fachhochschulebib/fhb/pruefungsplaner/model/retrofit/RetrofitInterface;", "kotlin.jvm.PlatformType", "checkCourseForFavorites", "", "courseName", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteAllEntries", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteEntries", "entries", "", "Lcom/fachhochschulebib/fhb/pruefungsplaner/model/room/TestPlanEntry;", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteEntry", "entry", "(Lcom/fachhochschulebib/fhb/pruefungsplaner/model/room/TestPlanEntry;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "fetchCourses", "Lcom/fachhochschulebib/fhb/pruefungsplaner/model/retrofit/GSONCourse;", "fetchEntries", "Lcom/fachhochschulebib/fhb/pruefungsplaner/model/retrofit/GSONEntry;", "ppSemester", "pTermin", "pYear", "pId", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "fetchExamPeriods", "Lorg/json/JSONArray;", "fetchFaculties", "fetchUUID", "Lcom/fachhochschulebib/fhb/pruefungsplaner/model/retrofit/JsonUuid;", "faculty", "getAllChosenCoursesLiveData", "Landroidx/lifecycle/LiveData;", "Lcom/fachhochschulebib/fhb/pruefungsplaner/model/room/Course;", "getAllCourses", "getAllCoursesByFacultyId", "facultyId", "getAllEntries", "getAllEntriesForChosenCoursesLiveData", "getAllEntriesLiveDataByDate", "getAllFacultiesLiveData", "Lcom/fachhochschulebib/fhb/pruefungsplaner/model/room/Faculty;", "getAllFavoritesLiveData", "getChosenCourseIds", "getCourseById", "id", "getCourseByName", "name", "getEntriesByCourseName", "course", "favorite", "(Ljava/lang/String;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getEntriesForCourse", "getEntryById", "getFacultyById", "getFavorites", "(ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getFirstExaminerNamesLiveData", "getSingleEntryById", "getUuid", "Lcom/fachhochschulebib/fhb/pruefungsplaner/model/room/Uuid;", "insertCourses", "courses", "insertEntry", "testPlanEntry", "insertFaculty", "(Lcom/fachhochschulebib/fhb/pruefungsplaner/model/room/Faculty;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertUuid", "uuid", "sendFeedBack", "ratingUsability", "", "ratingFunctions", "ratingStability", "text", "(Ljava/lang/String;FFFLjava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "unselectAllFavorites", "updateCourse", "chosen", "updateEntryFavorite", "(ZLjava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class DatabaseRepository {
    
    /**
     * Access to the local database
     */
    private com.fachhochschulebib.fhb.pruefungsplaner.model.room.UserDao localDataSource;
    
    /**
     * Access to the remote database via the [RetrofitInterface] and [RetrofitHelper]
     */
    private com.fachhochschulebib.fhb.pruefungsplaner.model.retrofit.RetrofitInterface remoteDataSource;
    
    public DatabaseRepository(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
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
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object fetchCourses(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.fachhochschulebib.fhb.pruefungsplaner.model.retrofit.GSONCourse>> continuation) {
        return null;
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
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object fetchFaculties(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super org.json.JSONArray> continuation) {
        return null;
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
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object fetchEntries(@org.jetbrains.annotations.NotNull()
    java.lang.String ppSemester, @org.jetbrains.annotations.NotNull()
    java.lang.String pTermin, @org.jetbrains.annotations.NotNull()
    java.lang.String pYear, @org.jetbrains.annotations.NotNull()
    java.lang.String pId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.fachhochschulebib.fhb.pruefungsplaner.model.retrofit.GSONEntry>> continuation) {
        return null;
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
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object fetchExamPeriods(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super org.json.JSONArray> continuation) {
        return null;
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
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object sendFeedBack(@org.jetbrains.annotations.NotNull()
    java.lang.String uuid, float ratingUsability, float ratingFunctions, float ratingStability, @org.jetbrains.annotations.NotNull()
    java.lang.String text, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
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
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object fetchUUID(@org.jetbrains.annotations.NotNull()
    java.lang.String faculty, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.fachhochschulebib.fhb.pruefungsplaner.model.retrofit.JsonUuid> continuation) {
        return null;
    }
    
    /**
     * Gets the Live Data with all [TestPlanEntry]-Objects, ordered by their exam date
     *
     * @return The LiveData-List with all [TestPlanEntry]-Objects, ordered by their exam date
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.util.List<com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry>> getAllEntriesLiveDataByDate() {
        return null;
    }
    
    /**
     * Gets the Live Data with all favorite [TestPlanEntry]-Objects.
     *
     * @return The LiveData-List with all favorite [TestPlanEntry]-Objects.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.util.List<com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry>> getAllFavoritesLiveData() {
        return null;
    }
    
    /**
     * Gets the Live Data with all chosen [Course]-Objects
     *
     * @return The LiveData-List with all chosen [Course]-Objects
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.util.List<com.fachhochschulebib.fhb.pruefungsplaner.model.room.Course>> getAllChosenCoursesLiveData() {
        return null;
    }
    
    /**
     * Gets the Live Data with all [Faculty]-Objects
     *
     * @return The LiveData-List with all [Faculty]-Objects
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.util.List<com.fachhochschulebib.fhb.pruefungsplaner.model.room.Faculty>> getAllFacultiesLiveData() {
        return null;
    }
    
    /**
     * Gets the Live Data with all [TestPlanEntry]-Objects for each chosen [Course]-Object
     *
     * @return The LiveData-List with all [TestPlanEntry]-Objects for each chosen [Course]-Object
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.util.List<com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry>> getAllEntriesForChosenCoursesLiveData() {
        return null;
    }
    
    /**
     * Gets the Live Data with the names of all first examiner.
     *
     * @return The LiveData-List with the names of all first examiner.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.util.List<java.lang.String>> getFirstExaminerNamesLiveData() {
        return null;
    }
    
    /**
     * Inserts a new [TestPlanEntry] into the local database.
     *
     * @param testPlanEntry The entry to be inserted into the local database
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object insertEntry(@org.jetbrains.annotations.NotNull()
    com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry testPlanEntry, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    /**
     * Inserts a list of [Course]-Objects into the local database.
     *
     * @param courses The list of courses to be inserted into the local database
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object insertCourses(@org.jetbrains.annotations.NotNull()
    java.util.List<com.fachhochschulebib.fhb.pruefungsplaner.model.room.Course> courses, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    /**
     * Inserts a UUID into the local database
     *
     * @param uuid The UUID to be inserted into the local database
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object insertUuid(@org.jetbrains.annotations.NotNull()
    java.lang.String uuid, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    /**
     * Inserts a [Faculty] into the local database.
     *
     * @param faculty The [Faculty] to be inserted into the local database.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object insertFaculty(@org.jetbrains.annotations.NotNull()
    com.fachhochschulebib.fhb.pruefungsplaner.model.room.Faculty faculty, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
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
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object updateEntryFavorite(boolean favorite, @org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
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
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object updateCourse(@org.jetbrains.annotations.NotNull()
    java.lang.String courseName, boolean chosen, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    /**
     * Deletes all favorites.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object unselectAllFavorites(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    /**
     * Deletes a list of [TestPlanEntry]-Objects from the local database.
     *
     * @param entries A list of [TestPlanEntry]-Objects that need to be deleted from the local database.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object deleteEntries(@org.jetbrains.annotations.NotNull()
    java.util.List<com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry> entries, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    /**
     * Deletes a  [TestPlanEntry] from the local database.
     *
     * @param entry The [TestPlanEntry] that needs to be deleted from the local database.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object deleteEntry(@org.jetbrains.annotations.NotNull()
    com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry entry, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    /**
     * Deletes all [TestPlanEntry]-Objects from the local database.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object deleteAllEntries(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
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
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getSingleEntryById(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry> continuation) {
        return null;
    }
    
    /**
     * Gets all [TestPlanEntry]-Objects from the local database
     *
     * @return A list with all [TestPlanEntry]-Objects in the local database
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getAllEntries(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry>> continuation) {
        return null;
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
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getEntriesForCourse(@org.jetbrains.annotations.Nullable()
    java.lang.String name, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry>> continuation) {
        return null;
    }
    
    /**
     * Returns all courses from the local database.
     *
     * @return A list with all courses from the local database
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getAllCourses(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.fachhochschulebib.fhb.pruefungsplaner.model.room.Course>> continuation) {
        return null;
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
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getCourseById(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.fachhochschulebib.fhb.pruefungsplaner.model.room.Course> continuation) {
        return null;
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
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getFavorites(boolean favorite, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry>> continuation) {
        return null;
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
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getEntriesByCourseName(@org.jetbrains.annotations.NotNull()
    java.lang.String course, boolean favorite, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry>> continuation) {
        return null;
    }
    
    /**
     * Gets the ids for all chosen [Course]-Objects.
     *
     * @return A list with the ids for all chosen [Course]-Objects.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getChosenCourseIds(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<java.lang.String>> continuation) {
        return null;
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
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getAllCoursesByFacultyId(@org.jetbrains.annotations.NotNull()
    java.lang.String facultyId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.fachhochschulebib.fhb.pruefungsplaner.model.room.Course>> continuation) {
        return null;
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
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getEntryById(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry> continuation) {
        return null;
    }
    
    /**
     * Gets the UUID from the local database
     *
     * @return The UUID from the local database
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getUuid(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.fachhochschulebib.fhb.pruefungsplaner.model.room.Uuid> continuation) {
        return null;
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
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getCourseByName(@org.jetbrains.annotations.NotNull()
    java.lang.String name, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.fachhochschulebib.fhb.pruefungsplaner.model.room.Course> continuation) {
        return null;
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
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object checkCourseForFavorites(@org.jetbrains.annotations.NotNull()
    java.lang.String courseName, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Boolean> continuation) {
        return null;
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
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getFacultyById(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.fachhochschulebib.fhb.pruefungsplaner.model.room.Faculty> continuation) {
        return null;
    }
}