package com.fachhochschulebib.fhb.pruefungsplaner.viewmodel;

import java.lang.System;

/**
 * Viewmodel that contains the logic of the Application. Implements a more specific access to the
 * Database-Repository and includes the sharedPreferencesRepository. Depper functionalities
 * for different Fragments and Activities are stored in inheriting classes.
 * **See Also:**[LiveData](https://developer.android.com/codelabs/basic-android-kotlin-training-livedata#2)
 */
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u00b2\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u000b\n\u0002\u0010!\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0016\n\u0002\u0018\u0002\n\u0002\b,\b\u0016\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0019\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u001aH\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001bJ\b\u0010\u001c\u001a\u00020\u001dH\u0002J\u0010\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020!H\u0002J\u0010\u0010\"\u001a\u00020#2\u0006\u0010$\u001a\u00020%H\u0002J\u0010\u0010&\u001a\u00020\'2\u0006\u0010(\u001a\u00020)H\u0002J\b\u0010*\u001a\u00020\u001dH\u0016J\u000e\u0010+\u001a\u00020\u001d2\u0006\u0010,\u001a\u00020-J\u0016\u0010.\u001a\u00020\u001d2\f\u0010/\u001a\b\u0012\u0004\u0012\u00020\'00H\u0016J\u0016\u00101\u001a\u00020\u001d2\u0006\u0010,\u001a\u00020-2\u0006\u00102\u001a\u000203J\u001c\u00101\u001a\u00020\u001d2\u0006\u0010,\u001a\u00020-2\f\u00104\u001a\b\u0012\u0004\u0012\u00020300J\b\u00105\u001a\u00020\u001dH\u0002J\u0006\u00106\u001a\u00020\u001dJ\u0006\u00107\u001a\u00020\u001dJ\u0006\u00108\u001a\u00020\u001dJ\u0019\u00109\u001a\n\u0012\u0004\u0012\u00020\u001f\u0018\u000100H\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010:J\b\u0010;\u001a\u00020\u0018H\u0016J\u0015\u0010<\u001a\u0004\u0018\u0001032\u0006\u0010(\u001a\u00020\'\u00a2\u0006\u0002\u0010=J\f\u0010>\u001a\b\u0012\u0004\u0012\u0002030?J\b\u0010@\u001a\u00020AH\u0016J\b\u0010B\u001a\u00020\u0018H\u0016J\b\u0010C\u001a\u00020\u0018H\u0016J\b\u0010D\u001a\u00020EH\u0016J\u0019\u0010F\u001a\u00020\u001f2\u0006\u00102\u001a\u00020\u001aH\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001bJ\u001b\u0010G\u001a\u0004\u0018\u00010\u001a2\u0006\u0010\u0019\u001a\u00020\u001aH\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001bJ!\u0010H\u001a\n\u0012\u0004\u0012\u00020\u001f\u0018\u0001002\u0006\u0010I\u001a\u00020\u001aH\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001bJ\u0012\u0010J\u001a\u0004\u0018\u00010%2\u0006\u0010K\u001a\u00020LH\u0002J\u0010\u0010M\u001a\u00020\u001a2\u0006\u0010N\u001a\u00020\u001aH\u0002J\n\u0010O\u001a\u0004\u0018\u00010PH\u0016J\n\u0010Q\u001a\u0004\u0018\u00010\u001aH\u0016J\u001b\u0010R\u001a\u0004\u0018\u00010#2\u0006\u00102\u001a\u00020\u001aH\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001bJ#\u0010S\u001a\n\u0012\u0004\u0012\u00020\'\u0018\u0001002\b\b\u0002\u0010T\u001a\u00020\u0018H\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010UJ)\u0010V\u001a\n\u0012\u0004\u0012\u00020\'\u0018\u0001002\u0006\u0010W\u001a\u00020\u001a2\u0006\u0010T\u001a\u00020\u0018H\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010XJ\u0011\u0010Y\u001a\u00020\u001aH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010:J\n\u0010Z\u001a\u0004\u0018\u00010\u001aH\u0016J\b\u0010[\u001a\u00020\u0018H\u0016J\n\u0010\\\u001a\u0004\u0018\u00010\u001aH\u0016J\n\u0010]\u001a\u0004\u0018\u00010\u001aH\u0016J\n\u0010^\u001a\u0004\u0018\u00010\u001aH\u0016J\r\u0010_\u001a\u0004\u0018\u000103\u00a2\u0006\u0002\u0010`J\n\u0010a\u001a\u0004\u0018\u00010\u001aH\u0016J\n\u0010b\u001a\u0004\u0018\u00010PH\u0016J\n\u0010c\u001a\u0004\u0018\u00010\u001aH\u0016J\b\u0010d\u001a\u00020EH\u0016J\b\u0010e\u001a\u00020EH\u0016J\u0013\u0010f\u001a\u0004\u0018\u00010gH\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010:J\u0016\u0010h\u001a\u00020\u001d2\f\u0010i\u001a\b\u0012\u0004\u0012\u00020\u001f00H\u0016J\u0016\u0010j\u001a\u00020\u001d2\f\u0010k\u001a\b\u0012\u0004\u0012\u00020!00H\u0016J\u0010\u0010l\u001a\u00020\u001d2\u0006\u0010m\u001a\u00020)H\u0016J\u0010\u0010l\u001a\u00020\u001d2\u0006\u0010n\u001a\u00020\'H\u0016J\u0010\u0010o\u001a\u00020\u001d2\u0006\u0010p\u001a\u00020#H\u0016J\u0016\u0010q\u001a\u00020\u001d2\u0006\u0010,\u001a\u00020-2\u0006\u0010(\u001a\u00020\'J\u001c\u0010q\u001a\u00020\u001d2\u0006\u0010,\u001a\u00020-2\f\u0010/\u001a\b\u0012\u0004\u0012\u00020\'00J\u0010\u0010r\u001a\u00020\u001d2\u0006\u0010s\u001a\u00020\u0018H\u0016J\u0010\u0010t\u001a\u00020\u001d2\u0006\u0010u\u001a\u00020AH\u0016J\u0010\u0010v\u001a\u00020\u001d2\u0006\u0010w\u001a\u00020\u0018H\u0016J\u0010\u0010x\u001a\u00020\u001d2\u0006\u0010y\u001a\u00020\u0018H\u0016J\u0010\u0010z\u001a\u00020\u001d2\u0006\u00102\u001a\u00020EH\u0016J\u0010\u0010{\u001a\u00020\u001d2\u0006\u0010|\u001a\u00020PH\u0016J\u0010\u0010}\u001a\u00020\u001d2\u0006\u0010~\u001a\u00020\u001aH\u0016J\u0010\u0010\u007f\u001a\u00020\u001d2\u0006\u0010s\u001a\u00020\u0018H\u0016J\u0012\u0010\u0080\u0001\u001a\u00020\u001d2\u0007\u0010\u0081\u0001\u001a\u00020\u001aH\u0016J\u0012\u0010\u0082\u0001\u001a\u00020\u001d2\u0007\u0010\u0083\u0001\u001a\u00020\u001aH\u0016J\u0012\u0010\u0084\u0001\u001a\u00020\u001d2\u0007\u0010\u0085\u0001\u001a\u00020\u001aH\u0016J\u000f\u0010\u0086\u0001\u001a\u00020\u001d2\u0006\u00102\u001a\u000203J\u0011\u0010\u0087\u0001\u001a\u00020\u001d2\u0006\u0010p\u001a\u00020#H\u0016J\u0011\u0010\u0087\u0001\u001a\u00020\u001d2\u0006\u0010I\u001a\u00020\u001aH\u0016J\u0011\u0010\u0088\u0001\u001a\u00020\u001d2\u0006\u0010|\u001a\u00020PH\u0016J\u0012\u0010\u0089\u0001\u001a\u00020\u001d2\u0007\u0010\u008a\u0001\u001a\u00020EH\u0016J\u0012\u0010\u008b\u0001\u001a\u00020\u001d2\u0007\u0010\u008c\u0001\u001a\u00020EH\u0016J\u000f\u0010\u008d\u0001\u001a\u00020\u001d2\u0006\u0010,\u001a\u00020-J\u001a\u0010\u008e\u0001\u001a\u00020\u001d2\u0006\u0010\u0019\u001a\u00020\u001a2\u0007\u0010\u008f\u0001\u001a\u00020\u0018H\u0016J\u0007\u0010\u0090\u0001\u001a\u00020\u001dJ!\u0010\u0091\u0001\u001a\u00020\u001d2\u0006\u0010,\u001a\u00020-2\u0006\u0010T\u001a\u00020\u00182\u0006\u0010(\u001a\u00020\'H\u0016J\u0007\u0010\u0092\u0001\u001a\u00020\u001dR\u0014\u0010\u0005\u001a\u00020\u0006X\u0084\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0014\u0010\t\u001a\u00020\nX\u0084\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0014\u0010\r\u001a\u00020\u000eX\u0084\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0014\u0010\u0011\u001a\u00020\u000eX\u0084\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0010R\u0014\u0010\u0013\u001a\u00020\u0014X\u0084\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u0093\u0001"}, d2 = {"Lcom/fachhochschulebib/fhb/pruefungsplaner/viewmodel/BaseViewModel;", "Landroidx/lifecycle/AndroidViewModel;", "application", "Landroid/app/Application;", "(Landroid/app/Application;)V", "coroutineExceptionHandler", "Lkotlinx/coroutines/CoroutineExceptionHandler;", "getCoroutineExceptionHandler", "()Lkotlinx/coroutines/CoroutineExceptionHandler;", "repository", "Lcom/fachhochschulebib/fhb/pruefungsplaner/model/repositories/DatabaseRepository;", "getRepository", "()Lcom/fachhochschulebib/fhb/pruefungsplaner/model/repositories/DatabaseRepository;", "sdfDisplay", "Ljava/text/SimpleDateFormat;", "getSdfDisplay", "()Ljava/text/SimpleDateFormat;", "sdfRetrofit", "getSdfRetrofit", "spRepository", "Lcom/fachhochschulebib/fhb/pruefungsplaner/model/repositories/SharedPreferencesRepository;", "getSpRepository", "()Lcom/fachhochschulebib/fhb/pruefungsplaner/model/repositories/SharedPreferencesRepository;", "checkCourseForFavorites", "", "courseName", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "checkSustainability", "", "createCourse", "Lcom/fachhochschulebib/fhb/pruefungsplaner/model/room/Course;", "jsonCourse", "Lcom/fachhochschulebib/fhb/pruefungsplaner/model/retrofit/GSONCourse;", "createFaculty", "Lcom/fachhochschulebib/fhb/pruefungsplaner/model/room/Faculty;", "json", "Lorg/json/JSONObject;", "createTestPlanEntry", "Lcom/fachhochschulebib/fhb/pruefungsplaner/model/room/TestPlanEntry;", "entry", "Lcom/fachhochschulebib/fhb/pruefungsplaner/model/retrofit/GSONEntry;", "deleteAllEntries", "deleteAllFromGoogleCalendar", "context", "Landroid/content/Context;", "deleteEntries", "entries", "", "deleteFromCalendar", "id", "", "ids", "deleteMainCourse", "fetchCourses", "fetchEntries", "fetchFaculties", "getAllCourses", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getBackgroundUpdates", "getCalendarId", "(Lcom/fachhochschulebib/fhb/pruefungsplaner/model/room/TestPlanEntry;)Ljava/lang/Long;", "getCalendarIds", "", "getCalendarInsertionType", "Lcom/fachhochschulebib/fhb/pruefungsplaner/utils/CalendarIO$InsertionType;", "getCalendarSync", "getChosenDarkMode", "getChosenThemeId", "", "getCourseById", "getCourseId", "getCoursesByFacultyId", "facultyId", "getCurrentPeriod", "periodObjects", "Lorg/json/JSONArray;", "getDate", "dateResponse", "getEndDate", "Ljava/util/Date;", "getEndDateString", "getFacultyById", "getFavorites", "favorite", "(ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getFavoritesByCourseName", "course", "(Ljava/lang/String;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getIDs", "getMainCourseId", "getNotificationSounds", "getPeriodTerm", "getPeriodTermin", "getPeriodYear", "getSelectedCalendar", "()Ljava/lang/Long;", "getSelectedFacultyId", "getStartDate", "getStartDateString", "getUpdateIntervalTimeHour", "getUpdateIntervalTimeMinute", "getUuid", "Lcom/fachhochschulebib/fhb/pruefungsplaner/model/room/Uuid;", "insertCourses", "courses", "insertCoursesJSON", "jsonCourses", "insertEntry", "jsonResponse", "testPlanEntry", "insertFaculty", "faculty", "insertIntoCalendar", "setBackgroundUpdates", "status", "setCalendarInserionType", "insertionType", "setCalendarSync", "sync", "setChosenDarkMode", "darkmode", "setChosenThemeId", "setEndDate", "date", "setMainCourse", "courseId", "setNotificationSounds", "setPeriodTerm", "periode", "setPeriodTermin", "termin", "setPeriodYear", "year", "setSelectedCalendar", "setSelectedFaculty", "setStartDate", "setUpdateIntervalTimeHour", "hour", "setUpdateIntervalTimeMinute", "minute", "updateCalendar", "updateCourse", "chosen", "updateDatabase", "updateEntryFavorite", "updatePeriod", "app_debug"})
public class BaseViewModel extends androidx.lifecycle.AndroidViewModel {
    
    /**
     * The Database Repository
     */
    @org.jetbrains.annotations.NotNull()
    private final com.fachhochschulebib.fhb.pruefungsplaner.model.repositories.DatabaseRepository repository = null;
    
    /**
     * The Shared Preferences Repository
     */
    @org.jetbrains.annotations.NotNull()
    private final com.fachhochschulebib.fhb.pruefungsplaner.model.repositories.SharedPreferencesRepository spRepository = null;
    
    /**
     * The [SimpleDateFormat] to convert dates from the retrofit interface
     */
    @org.jetbrains.annotations.NotNull()
    private final java.text.SimpleDateFormat sdfRetrofit = null;
    
    /**
     * The [SimpleDateFormat] to Display dates in the UI
     */
    @org.jetbrains.annotations.NotNull()
    private final java.text.SimpleDateFormat sdfDisplay = null;
    
    /**
     * The Exceptionhandler that is used in the ViewModel-Coroutines. Just prints the stacktrace to the logcat.
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CoroutineExceptionHandler coroutineExceptionHandler = null;
    
    public BaseViewModel(@org.jetbrains.annotations.NotNull()
    android.app.Application application) {
        super(null);
    }
    
    @org.jetbrains.annotations.NotNull()
    protected final com.fachhochschulebib.fhb.pruefungsplaner.model.repositories.DatabaseRepository getRepository() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    protected final com.fachhochschulebib.fhb.pruefungsplaner.model.repositories.SharedPreferencesRepository getSpRepository() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    protected final java.text.SimpleDateFormat getSdfRetrofit() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    protected final java.text.SimpleDateFormat getSdfDisplay() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    protected final kotlinx.coroutines.CoroutineExceptionHandler getCoroutineExceptionHandler() {
        return null;
    }
    
    /**
     * Retrieves all courses from the remote database via the [com.fachhochschulebib.fhb.pruefungsplaner.model.retrofit.RetrofitInterface] and inserts them
     * into the local room database.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public final void fetchCourses() {
    }
    
    /**
     * Retrieves all entries for the current period from the remote database via the [com.fachhochschulebib.fhb.pruefungsplaner.model.retrofit.RetrofitInterface] and inserts them
     * into the local room database. Gets the needed information for the current period via the from the shared preferences.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public final void fetchEntries() {
    }
    
    /**
     * Retrieves all faculties from the remote database via the [com.fachhochschulebib.fhb.pruefungsplaner.model.retrofit.RetrofitInterface] and inserts them
     * into the local room database.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public final void fetchFaculties() {
    }
    
    /**
     * Updates all data in the local database.
     *
     * @author Alexander Lange
     * @since 1.6
     *
     * @see fetchFaculties
     * @see fetchCourses
     * @see fetchEntries
     */
    public final void updateDatabase() {
    }
    
    /**
     * Return the ids of all choosen courses in the Room-Database.
     *
     * @param[roomData] The Room-Database of the application.
     *
     * @return A String containing every course-ID
     *
     * @author Alexander Lange
     * @since 1.5
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getIDs(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.String> continuation) {
        return null;
    }
    
    /**
     * Checks for a new exam period and updates the exam-data if necessary.
     *
     * @since 1.6
     * @author Alexander Lange (E-Mail:alexander.lange@fh-bielefeld.de)
     */
    public final void updatePeriod() {
    }
    
    /**
     * Checks a list of possible periods for the latest one and returns it.
     *
     * @param periodObjects A list of possible periods.
     *
     * @return The latest period from the fiven list.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private final org.json.JSONObject getCurrentPeriod(org.json.JSONArray periodObjects) {
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
    public void insertEntry(@org.jetbrains.annotations.NotNull()
    com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry testPlanEntry) {
    }
    
    /**
     * Inserts a new [TestPlanEntry] into the local database.
     *
     * @param jsonResponse The jsonobject of the new entry, normally taken from the remote database.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public void insertEntry(@org.jetbrains.annotations.NotNull()
    com.fachhochschulebib.fhb.pruefungsplaner.model.retrofit.GSONEntry jsonResponse) {
    }
    
    /**
     * Inserts a list of [Course]-Objects into the local database.
     *
     * @param courses The list of courses to be inserted into the local database
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public void insertCourses(@org.jetbrains.annotations.NotNull()
    java.util.List<com.fachhochschulebib.fhb.pruefungsplaner.model.room.Course> courses) {
    }
    
    /**
     * Inserts a list of [Course]-Objects into the local database.
     *
     * @param jsonCourses The list of courses to be inserted into the local database, in form of [GSONCourse]-Objects
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public void insertCoursesJSON(@org.jetbrains.annotations.NotNull()
    java.util.List<com.fachhochschulebib.fhb.pruefungsplaner.model.retrofit.GSONCourse> jsonCourses) {
    }
    
    /**
     * Inserts a [Faculty] into the local database.
     *
     * @param faculty The [Faculty] to be inserted into the local database.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public void insertFaculty(@org.jetbrains.annotations.NotNull()
    com.fachhochschulebib.fhb.pruefungsplaner.model.room.Faculty faculty) {
    }
    
    /**
     * Updates if a [TestPlanEntry] is a favorite or not. Also syncs the calendar if necessary.
     *
     * @param context The Applicationcontext
     * @param favorite Whether the [TestPlanEntry] is favorite or not
     * @param entry The [TestPlanEntry] that needs to be updated
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public void updateEntryFavorite(@org.jetbrains.annotations.NotNull()
    android.content.Context context, boolean favorite, @org.jetbrains.annotations.NotNull()
    com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry entry) {
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
    public void updateCourse(@org.jetbrains.annotations.NotNull()
    java.lang.String courseName, boolean chosen) {
    }
    
    /**
     * Deletes a list of [TestPlanEntry]-Objects from the local database.
     *
     * @param entries A list of [TestPlanEntry]-Objects that need to be deleted from the local database.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public void deleteEntries(@org.jetbrains.annotations.NotNull()
    java.util.List<com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry> entries) {
    }
    
    /**
     * Deletes all [TestPlanEntry]-Objects from the local database.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public void deleteAllEntries() {
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
    public java.lang.Object getFavorites(boolean favorite, @org.jetbrains.annotations.NotNull()
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
    public java.lang.Object getFavoritesByCourseName(@org.jetbrains.annotations.NotNull()
    java.lang.String course, boolean favorite, @org.jetbrains.annotations.NotNull()
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
    public java.lang.Object getAllCourses(@org.jetbrains.annotations.NotNull()
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
    public java.lang.Object getCourseById(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.fachhochschulebib.fhb.pruefungsplaner.model.room.Course> continuation) {
        return null;
    }
    
    /**
     * Get all courses for one [Faculty].
     *
     * @param facultyId The id of the faculty to take the courses from
     *
     * @return A list with all courses for the given faculty
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getCoursesByFacultyId(@org.jetbrains.annotations.NotNull()
    java.lang.String facultyId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.fachhochschulebib.fhb.pruefungsplaner.model.room.Course>> continuation) {
        return null;
    }
    
    /**
     * Returns the [Uuid] from the local database.
     *
     * @return The [Uuid] from the local database
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getUuid(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.fachhochschulebib.fhb.pruefungsplaner.model.room.Uuid> continuation) {
        return null;
    }
    
    /**
     * Returns the course id for a given course name.
     *
     * @param courseName The course name for the course id
     *
     * @return The Id for the given course name
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getCourseId(@org.jetbrains.annotations.NotNull()
    java.lang.String courseName, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.String> continuation) {
        return null;
    }
    
    /**
     * Checks if there is a [TestPlanEntry] favorite for a given course.
     *
     * @param courseName The name of the course to look for favorites
     *
     * @return true->There is at least one favorite for this course;false->There is no favorite for this course
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object checkCourseForFavorites(@org.jetbrains.annotations.NotNull()
    java.lang.String courseName, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Boolean> continuation) {
        return null;
    }
    
    /**
     * Returns the [Faculty] for a given Id.
     *
     * @param id The id of the Faculty
     *
     * @return The [Faculty] for the given id
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getFacultyById(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.fachhochschulebib.fhb.pruefungsplaner.model.room.Faculty> continuation) {
        return null;
    }
    
    /**
     * Sets the course, selected as the maincourse
     *
     * @param courseId The id of the course, selected as the maincourse
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public void setMainCourse(@org.jetbrains.annotations.NotNull()
    java.lang.String courseId) {
    }
    
    private final void deleteMainCourse() {
    }
    
    /**
     * Sets the faculty, selected by the user
     *
     * @param facultyId The id of the faculty, the user selected
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public void setSelectedFaculty(@org.jetbrains.annotations.NotNull()
    java.lang.String facultyId) {
    }
    
    /**
     * Sets the faculty, selected by the user
     *
     * @param faculty The object of the faculty, the user selected
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public void setSelectedFaculty(@org.jetbrains.annotations.NotNull()
    com.fachhochschulebib.fhb.pruefungsplaner.model.room.Faculty faculty) {
    }
    
    /**
     * Sets the year of the current period
     *
     * @param year The year of the current period as string (Like '2022')
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public void setPeriodYear(@org.jetbrains.annotations.NotNull()
    java.lang.String year) {
    }
    
    /**
     * Sets the term of the current period (SoSe or WiSe)
     *
     * @param period The term of the current period (SoSe or WiSe)
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public void setPeriodTerm(@org.jetbrains.annotations.NotNull()
    java.lang.String periode) {
    }
    
    public void setPeriodTermin(@org.jetbrains.annotations.NotNull()
    java.lang.String termin) {
    }
    
    /**
     * Sets the start date of the period
     *
     * @param date The start date of the period
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public void setStartDate(@org.jetbrains.annotations.NotNull()
    java.util.Date date) {
    }
    
    /**
     * Sets the end date of the period
     *
     * @param date The end date of the period
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public void setEndDate(@org.jetbrains.annotations.NotNull()
    java.util.Date date) {
    }
    
    /**
     * Sets the setting for the darkmode
     *
     * @param darkmode true-> darkmode is set; false-> darkmode is not set
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public void setChosenDarkMode(boolean darkmode) {
    }
    
    /**
     * Sets the id of the selected theme
     *
     * @param id The id of the selected theme
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public void setChosenThemeId(int id) {
    }
    
    /**
     * Sets the hour component of the update interval for the background worker
     *
     * @param hour The hour component of the update interval for the background worker
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public void setUpdateIntervalTimeHour(int hour) {
    }
    
    /**
     * Sets the minute component of the update interval for the background worker
     *
     * @param minute The hour component of the update interval for the background worker
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public void setUpdateIntervalTimeMinute(int minute) {
    }
    
    /**
     * Sets the setting for background updates
     *
     * @param status true->The app will update in the background; false->The app wont update in the background
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public void setBackgroundUpdates(boolean status) {
    }
    
    /**
     * Sets the setting for notification sounds for the background worker
     *
     * @param status true->The app will make a sound for each notification;false->the app will make no sound for a notification
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public void setNotificationSounds(boolean status) {
    }
    
    /**
     * Sets the setting for calendar synchronization
     *
     * @param sync true->The calendar will synchronize with the selected exams;false->The calendar wont sync with the selected courses
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public void setCalendarSync(boolean sync) {
    }
    
    /**
     * Sets the setting for the insertion type for each calendar entry
     *
     * @param insertionType The insertion type for each new calendar entry.
     * Automatic -> The entry will be placed in the calendar without notifying the user;
     * Manuel->The insertion intent of the calendar will be started, where the user can modify the entry himself;
     * Ask->The user will be asked each time, if an entry should be manuel ro automatic
     */
    public void setCalendarInserionType(@org.jetbrains.annotations.NotNull()
    com.fachhochschulebib.fhb.pruefungsplaner.utils.CalendarIO.InsertionType insertionType) {
    }
    
    /**
     * Saves the id of the selected Calendar into the shared preferences.
     *
     * @param id The id of the selected calendar
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public final void setSelectedCalendar(long id) {
    }
    
    /**
     * Gets the course, selected as the maincourse
     *
     * @return The id of the course, selected as the maincourse
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @org.jetbrains.annotations.Nullable()
    public java.lang.String getMainCourseId() {
        return null;
    }
    
    /**
     * Gets the faculty, selected by the user
     *
     * @return The id of the faculty, the user selected
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @org.jetbrains.annotations.Nullable()
    public java.lang.String getSelectedFacultyId() {
        return null;
    }
    
    /**
     * Gets the year of the current period
     *
     * @return the year of the current period as string
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @org.jetbrains.annotations.Nullable()
    public java.lang.String getPeriodYear() {
        return null;
    }
    
    /**
     * Gets the term of the current period (SoSe or WiSe)
     *
     * @return The term of the current period (SoSe or WiSe)
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @org.jetbrains.annotations.Nullable()
    public java.lang.String getPeriodTerm() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public java.lang.String getPeriodTermin() {
        return null;
    }
    
    /**
     * Gets the start date of the period
     *
     * @return The start date of the period as a string
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @org.jetbrains.annotations.Nullable()
    public java.lang.String getStartDateString() {
        return null;
    }
    
    /**
     * Gets the start date of the period
     *
     * @return The start date of the period
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @org.jetbrains.annotations.Nullable()
    public java.util.Date getStartDate() {
        return null;
    }
    
    /**
     * Gets the end date of the period
     *
     * @return The end date of the period as a string
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @org.jetbrains.annotations.Nullable()
    public java.lang.String getEndDateString() {
        return null;
    }
    
    /**
     * Gets the end date of the period
     *
     * @return The end date of the period
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @org.jetbrains.annotations.Nullable()
    public java.util.Date getEndDate() {
        return null;
    }
    
    /**
     * Gets the setting for the darkmode
     *
     * @return true-> darkmode is set; false-> darkmode is not set. Returns false by default
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public boolean getChosenDarkMode() {
        return false;
    }
    
    /**
     * Gets the id of the selected theme
     *
     * @return The id of the selected theme. Returns the id of the first theme in the list (green) by default
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public int getChosenThemeId() {
        return 0;
    }
    
    /**
     * Gets the hour component of the update interval for the background worker
     *
     * @return The hour component of the update interval for the background worker. Return 0 by default
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public int getUpdateIntervalTimeHour() {
        return 0;
    }
    
    /**
     * Gets the minute component of the update interval for the background worker
     *
     * @return The minute component of the update interval for the background worker. Returns 15 by default, because 0:15 is the minimum interval for the background worker
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public int getUpdateIntervalTimeMinute() {
        return 0;
    }
    
    /**
     * Gets the setting for background updates
     *
     * @return true->The app will update in the background; false->The app wont update in the background. Returns false by default
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public boolean getBackgroundUpdates() {
        return false;
    }
    
    /**
     * Gets the setting for notification sounds for the background worker
     *
     * @return true->The app will make a sound for each notification;false->the app will make no sound for a notification. Returns false by default
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public boolean getNotificationSounds() {
        return false;
    }
    
    /**
     * Gets the setting for calendar synchronization
     *
     * @return true->The calendar will synchronize with the selected exams;false->The calendar wont sync with the selected courses. Returns false by default
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public boolean getCalendarSync() {
        return false;
    }
    
    /**
     * Gets the setting for the insertion type for each calendar entry
     *
     * @return The insertion type for each new calendar entry.
     * Automatic -> The entry will be placed in the calendar without notifying the user;
     * Manuel->The insertion intent of the calendar will be started, where the user can modify the entry himself;
     * Ask->The user will be asked each time, if an entry should be manuel ro automatic. Returns automatic by default.
     */
    @org.jetbrains.annotations.NotNull()
    public com.fachhochschulebib.fhb.pruefungsplaner.utils.CalendarIO.InsertionType getCalendarInsertionType() {
        return null;
    }
    
    /**
     * Returns the id of the selected Calendar from the shared preferences
     *
     * @return The id of the selected Calendar
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long getSelectedCalendar() {
        return null;
    }
    
    /**
     * Returns a list of all ids for Events currently stored in the Calendar that are saved in the shared preferences.
     *
     * @return A list of all ids for Events currently stored in the Calendar
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.Long> getCalendarIds() {
        return null;
    }
    
    /**
     * Inserts a [TestPlanEntry] into the Calendar. On success it saves the id for this entry in the shared preferences.
     *
     * @param context The Applicationcontext
     * @param entry The entry to be inserted into the Calendar
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public final void insertIntoCalendar(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry entry) {
    }
    
    /**
     * Inserts a list of [TestPlanEntry]-Objects into the Calendar. On success it saves the id for each entry in the shared preferences.
     *
     * @param context The Applicationcontext
     * @param entries The entries to be inserted into the Calendar
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public final void insertIntoCalendar(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    java.util.List<com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry> entries) {
    }
    
    /**
     * Returns the Calendar id for a given [TestPlanEntry] from the shared preferences
     *
     * @param entry The [TestPlanEntry] which id is looked for
     *
     * @return The id for the given [TestPlanEntry]
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long getCalendarId(@org.jetbrains.annotations.NotNull()
    com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry entry) {
        return null;
    }
    
    /**
     * Deletes an event from the Calendar.
     *
     * @param context The Applicationcontext
     * @param id The id of the event that shall be deleted
     *
     * @author Alexander Lange
     * @since 1.6
     *
     * @see getCalendarId
     */
    public final void deleteFromCalendar(@org.jetbrains.annotations.NotNull()
    android.content.Context context, long id) {
    }
    
    /**
     * Deletes a list of events from the Calendar.
     *
     * @param context The Applicationcontext
     * @param ids The ids of the events that shall be deleted
     *
     * @author Alexander Lange
     * @since 1.6
     *
     * @see getCalendarId
     */
    public final void deleteFromCalendar(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.Long> ids) {
    }
    
    /**
     * Deletes all events currently stored in the Calendar.
     *
     * @param context The Applicationcontext
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public final void deleteAllFromGoogleCalendar(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
    }
    
    /**
     * Updates the google calendar with the new favorites.
     *
     * @param context The Applicationcontext
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public final void updateCalendar(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
    }
    
    /**
     * Checks if the current main course is also a chosen one. If not id deletes the current main course.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private final void checkSustainability() {
    }
    
    /**
     * Creates a new [TestPlanEntry] from a [GSONEntry].
     *
     * @param[entry] The [GSONEntry], that contains the data for the [TestPlanEntry].
     *
     * @return A [TestPlanEntry] containing the data of the [GSONEntry]
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private final com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry createTestPlanEntry(com.fachhochschulebib.fhb.pruefungsplaner.model.retrofit.GSONEntry entry) {
        return null;
    }
    
    /**
     * Return a formatted date as String to save in the [TestPlanEntry.date]-Paramater.
     *
     * @param[dateResponse] The date from the JSON-Response.
     *
     * @return The formatted date for the [TestPlanEntry.date]
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private final java.lang.String getDate(java.lang.String dateResponse) {
        return null;
    }
    
    /**
     * Creates a new [Course] from a [GSONCourse].
     *
     * @param jsonCourse The [GSONCourse], that contains the data for the [Course].
     *
     * @return A [Course] containing the data of the [GSONCourse]
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private final com.fachhochschulebib.fhb.pruefungsplaner.model.room.Course createCourse(com.fachhochschulebib.fhb.pruefungsplaner.model.retrofit.GSONCourse jsonCourse) {
        return null;
    }
    
    /**
     * Creates a new [Faculty] from a [JSONObject].
     *
     * @param[json] The [JSONObject], that contains the data for the [Faculty].
     *
     * @return A [Faculty] containing the data of the [JSONObject]
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private final com.fachhochschulebib.fhb.pruefungsplaner.model.room.Faculty createFaculty(org.json.JSONObject json) {
        return null;
    }
}