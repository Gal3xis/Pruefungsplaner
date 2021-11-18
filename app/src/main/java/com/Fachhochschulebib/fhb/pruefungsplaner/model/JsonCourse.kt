package com.Fachhochschulebib.fhb.pruefungsplaner.model

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

class JsonCourse {
    @SerializedName("CourseName")
    @Expose
    var courseName: String? = null

    @SerializedName("CourseShortName")
    @Expose
    var courseShortName: String? = null

    @SerializedName("SGID")
    @Expose
    var sgid: String? = null

    @SerializedName("FKFBID")
    @Expose
    var fkfbid: String? = null
}