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

package com.Fachhochschulebib.fhb.pruefungsplaner;

import java.util.List;

import com.Fachhochschulebib.fhb.pruefungsplaner.model.JsonPruefungsform;
import com.Fachhochschulebib.fhb.pruefungsplaner.model.JsonResponse;
import retrofit2.Call;
import retrofit2.http.GET;


public interface RequestInterface {
    @GET(" ")
    Call<List<JsonResponse>> getJSON();

    //Start Merlin Gürtler
    @GET(" ")
    Call<List<JsonPruefungsform>> getJSONPruefForm();
    //Ende Merlin Gürtler
}