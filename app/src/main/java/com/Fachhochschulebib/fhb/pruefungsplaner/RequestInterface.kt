//////////////////////////////
// Requestinterface
//
//
//
// autor:
// inhalt: Interface damit Prüfplanobjekte von der Json Modelklasse zur lokalen Datenbank hinzugefügt werden können
// zugriffsdatum: 11.12.1992
//
//
//
//
//
//
//////////////////////////////
package com.Fachhochschulebib.fhb.pruefungsplaner

import retrofit2.http.GET
import com.Fachhochschulebib.fhb.pruefungsplaner.model.JsonResponse
import com.Fachhochschulebib.fhb.pruefungsplaner.model.JsonUuid
import retrofit2.http.PUT
import retrofit2.http.POST
import com.Fachhochschulebib.fhb.pruefungsplaner.model.JsonCourse
import retrofit2.Call

interface RequestInterface {
    @get:GET(" ")
    val jSON: Call<List<JsonResponse?>?>?

    //Start Merlin Gürtler
    @get:GET(" ")
    val jsonUuid: Call<JsonUuid?>?

    @PUT(" ")
    fun anotherStart(): Call<Void?>?

    @POST(" ")
    fun sendFeedBack(): Call<Void?>?

    @get:GET(" ")
    val studiengaenge: Call<List<JsonCourse?>?>?

    @POST(" ")
    fun sendCourses(): Call<Void?>? //Ende Merlin Gürtler
}