//////////////////////////////
// JSONResponse
//
//
//
// autor:
// inhalt: parsen von den erhaltenen Retrofit Daten
// zugriffsdatum: 11.12.19
//
//
//
//
//
//
//////////////////////////////
package com.Fachhochschulebib.fhb.pruefungsplaner.model

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

class JsonResponse {
    @SerializedName("FirstExaminer")
    @Expose
    var firstExaminer: String? = null

    @SerializedName("SecondExaminer")
    @Expose
    var secondExaminer: String? = null

    @SerializedName("Form")
    @Expose
    var form: String? = null

    @SerializedName("Semester")
    @Expose
    var semester: String? = null

    @SerializedName("Date")
    @Expose
    var date: String? = null

    @SerializedName("Module")
    @Expose
    var module: String? = null

    @SerializedName("CourseName")
    @Expose
    var courseName: String? = null

    @SerializedName("Termin")
    @Expose
    var termin: String? = null

    @SerializedName("ID")
    @Expose
    var id: String? = null

    @SerializedName("Room")
    @Expose
    var room: String? = null

    // Start Merlin Gürtler
    @SerializedName("CourseId")
    @Expose
    var courseId: String? = null

    @SerializedName("Status")
    @Expose
    var status: String? = null

    @SerializedName("Hint")
    @Expose
    var hint: String? = null

    // Ende Merlin Gürtler
    @SerializedName("Color")
    @Expose
    var color: String? = null
}