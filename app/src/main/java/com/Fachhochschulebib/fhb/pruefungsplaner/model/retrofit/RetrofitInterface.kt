package com.Fachhochschulebib.fhb.pruefungsplaner.model.retrofit

import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RetrofitInterface{
    @GET("org.fh.ppv.entity.studiengang/")
    suspend fun getCourses():List<GSONCourse>


    @GET("org.fh.ppv.entity.pruefplaneintrag/{ppSemester}/{pTermin}/{pYear}/{pIds}/")
    suspend fun getEntries(
        @Path("ppSemester") ppSemetser:String,
        @Path("pTermin")pTermin:String,
        @Path("pYear")pYear:String,
        @Path("pIds")pIds:String):List<GSONEntry>


    @GET("org.fh.ppv.entity.user/firstStart/{pFaculty}/")
    suspend fun getUUID(@Path("pFaculty") ppFaculty: String): JsonUuid?


    @POST("org.fh.ppv.entity.feedback/sendFeedback/{pUuid}/{pUsability}/{pFunctions}/{pStability}/{pText}/")
    suspend fun sendFeedBack(
        @Path("pUuid")ppUuid:String,
        @Path("pUsability")ppUsability:String,
        @Path("pFunctions")ppFunctions:String,
        @Path("pStability") ppStability:String,
        @Path("pText")ppText:String
    )
}