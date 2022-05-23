package com.fachhochschulebib.fhb.pruefungsplaner.model.retrofit

import com.fachhochschulebib.fhb.pruefungsplaner.model.URLs.*
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Interface with Functions to query or manipulate the remote database.
 *
 * @author Alexander Lange
 * @since 1.6
 */
interface RetrofitInterface{
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
    @GET(coursesRelativeUrl)
    suspend fun getCourses():List<GSONCourse>

    /**
     * Returns an Array of All Entries from the Rest-Api.
     * Needs to be called inside a coroutine scope.
     *
     * @param ppSemester The current Semester, can be taken from the sharedPreferences.
     * @param pTermin Differences between first and second period. Can be taken from sharedPreferences
     * @param pYear The year from where the entries shall be taken. The current year can be taken from
     * @param pIds A string with all the ids from which the entries shall be taken. Can be get from
     *
     * @return A list of all Faculties. Can be null if no faculty was found.
     *
     * @author Alexander Lange
     * @since 1.6
     *
     * @see RetrofitInterface.getEntries
     *
     */
    @GET("$entriesRelativeUrl/{ppSemester}/{pTermin}/{pYear}/{pIds}/")
    suspend fun getEntries(
        @Path("ppSemester") ppSemester:String,
        @Path("pTermin")pTermin:String,
        @Path("pYear")pYear:String,
        @Path("pIds")pIds:String):List<GSONEntry>


    /**
     * Gets the UUID linked to a faculty.
     *
     * @param ppFaculty The Faculty, linked to the UUID
     *
     * @return The UUID linked to the faculty
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @GET("$uuidRelativeUrl/{pFaculty}/")
    suspend fun getUUID(@Path("pFaculty") ppFaculty: String): GSONUuid?

    /**
     * Send feedback to the server.
     *
     * @param ppUsability How good is the usability of the application
     * @param ppFunctions Are there enough functions in the application
     * @param ppStability How stable was the application, did it crashes?
     * @param ppText A comment from the user.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @POST("$feedbackRelativeUrl/{pUuid}/{pUsability}/{pFunctions}/{pStability}/{pText}/")
    suspend fun sendFeedBack(
        @Path("pUuid")ppUuid:String,
        @Path("pUsability")ppUsability:String,
        @Path("pFunctions")ppFunctions:String,
        @Path("pStability") ppStability:String,
        @Path("pText")ppText:String
    )
}