package com.fachhochschulebib.fhb.pruefungsplaner.model.retrofit;

import java.lang.System;

/**
 * Interface with Functions to query or manipulate the remote database.
 *
 * @author Alexander Lange
 * @since 1.6
 */
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0007\bf\u0018\u00002\u00020\u0001J\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0005J?\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00070\u00032\b\b\u0001\u0010\b\u001a\u00020\t2\b\b\u0001\u0010\n\u001a\u00020\t2\b\b\u0001\u0010\u000b\u001a\u00020\t2\b\b\u0001\u0010\f\u001a\u00020\tH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\rJ\u001d\u0010\u000e\u001a\u0004\u0018\u00010\u000f2\b\b\u0001\u0010\u0010\u001a\u00020\tH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0011JC\u0010\u0012\u001a\u00020\u00132\b\b\u0001\u0010\u0014\u001a\u00020\t2\b\b\u0001\u0010\u0015\u001a\u00020\t2\b\b\u0001\u0010\u0016\u001a\u00020\t2\b\b\u0001\u0010\u0017\u001a\u00020\t2\b\b\u0001\u0010\u0018\u001a\u00020\tH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0019\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u001a"}, d2 = {"Lcom/fachhochschulebib/fhb/pruefungsplaner/model/retrofit/RetrofitInterface;", "", "getCourses", "", "Lcom/fachhochschulebib/fhb/pruefungsplaner/model/retrofit/GSONCourse;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getEntries", "Lcom/fachhochschulebib/fhb/pruefungsplaner/model/retrofit/GSONEntry;", "ppSemetser", "", "pTermin", "pYear", "pIds", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getUUID", "Lcom/fachhochschulebib/fhb/pruefungsplaner/model/retrofit/JsonUuid;", "ppFaculty", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "sendFeedBack", "", "ppUuid", "ppUsability", "ppFunctions", "ppStability", "ppText", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public abstract interface RetrofitInterface {
    
    /**
     * Gets all courses from the Rest-Api.
     * Needs to be called inside a Coroutinescope.
     *
     * @return The list of Courses, can be null if no courses where found.
     *
     * @author Alexander Lange
     * @since 1.6
     *
     * @see RetrofitInterface.getCourses
     */
    @org.jetbrains.annotations.Nullable()
    @retrofit2.http.GET(value = "org.fh.ppv.entity.studiengang/")
    public abstract java.lang.Object getCourses(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.fachhochschulebib.fhb.pruefungsplaner.model.retrofit.GSONCourse>> continuation);
    
    /**
     * Returns an Array of All Entries from the Rest-Api.
     * Needs to be called inside a Coroutinescope.
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
    @retrofit2.http.GET(value = "org.fh.ppv.entity.pruefplaneintrag/{ppSemester}/{pTermin}/{pYear}/{pIds}/")
    public abstract java.lang.Object getEntries(@org.jetbrains.annotations.NotNull()
    @retrofit2.http.Path(value = "ppSemester")
    java.lang.String ppSemetser, @org.jetbrains.annotations.NotNull()
    @retrofit2.http.Path(value = "pTermin")
    java.lang.String pTermin, @org.jetbrains.annotations.NotNull()
    @retrofit2.http.Path(value = "pYear")
    java.lang.String pYear, @org.jetbrains.annotations.NotNull()
    @retrofit2.http.Path(value = "pIds")
    java.lang.String pIds, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.fachhochschulebib.fhb.pruefungsplaner.model.retrofit.GSONEntry>> continuation);
    
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
    @retrofit2.http.GET(value = "org.fh.ppv.entity.user/firstStart/{pFaculty}/")
    public abstract java.lang.Object getUUID(@org.jetbrains.annotations.NotNull()
    @retrofit2.http.Path(value = "pFaculty")
    java.lang.String ppFaculty, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.fachhochschulebib.fhb.pruefungsplaner.model.retrofit.JsonUuid> continuation);
    
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
    @retrofit2.http.POST(value = "org.fh.ppv.entity.feedback/sendFeedback/{pUuid}/{pUsability}/{pFunctions}/{pStability}/{pText}/")
    public abstract java.lang.Object sendFeedBack(@org.jetbrains.annotations.NotNull()
    @retrofit2.http.Path(value = "pUuid")
    java.lang.String ppUuid, @org.jetbrains.annotations.NotNull()
    @retrofit2.http.Path(value = "pUsability")
    java.lang.String ppUsability, @org.jetbrains.annotations.NotNull()
    @retrofit2.http.Path(value = "pFunctions")
    java.lang.String ppFunctions, @org.jetbrains.annotations.NotNull()
    @retrofit2.http.Path(value = "pStability")
    java.lang.String ppStability, @org.jetbrains.annotations.NotNull()
    @retrofit2.http.Path(value = "pText")
    java.lang.String ppText, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation);
}