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
package com.Fachhochschulebib.fhb.pruefungsplaner.model.retrofit

import com.Fachhochschulebib.fhb.pruefungsplaner.model.retrofit.GSONCourse
import com.Fachhochschulebib.fhb.pruefungsplaner.model.retrofit.GSONEntry
import retrofit2.http.GET
import com.Fachhochschulebib.fhb.pruefungsplaner.model.retrofit.JsonUuid
import retrofit2.http.PUT
import retrofit2.http.POST
import retrofit2.Call

interface RequestInterface {
    @get:GET(" ")
    val jSON: Call<List<GSONEntry?>?>?

    //Start Merlin Gürtler
    @get:GET(" ")
    val jsonUuid: Call<JsonUuid?>?

    @PUT(" ")
    fun anotherStart(): Call<Void?>?

    @POST(" ")
    fun sendFeedBack(): Call<Void?>?

    @get:GET(" ")
    val studiengaenge: Call<List<GSONCourse?>?>?

    @POST(" ")
    fun sendCourses(): Call<Void?>?
}