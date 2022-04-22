package com.fachhochschulebib.fhb.pruefungsplaner.model.retrofit

import com.fachhochschulebib.fhb.pruefungsplaner.model.URLs.*
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface RetrofitInterface{
    @GET(coursesRelativeUrl)
    suspend fun getCourses():List<GSONCourse>


    @GET("$entriesRelativeUrl/{ppSemester}/{pTermin}/{pYear}/{pIds}/")
    suspend fun getEntries(
        @Path("ppSemester") ppSemetser:String,
        @Path("pTermin")pTermin:String,
        @Path("pYear")pYear:String,
        @Path("pIds")pIds:String):List<GSONEntry>


    @GET("$uuidRelativeUrl/{pFaculty}/")
    suspend fun getUUID(@Path("pFaculty") ppFaculty: String): JsonUuid?


    @POST("$feedbackRelativeUrl/{pUuid}/{pUsability}/{pFunctions}/{pStability}/{pText}/")
    suspend fun sendFeedBack(
        @Path("pUuid")ppUuid:String,
        @Path("pUsability")ppUsability:String,
        @Path("pFunctions")ppFunctions:String,
        @Path("pStability") ppStability:String,
        @Path("pText")ppText:String
    )
}