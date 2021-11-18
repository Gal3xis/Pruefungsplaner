package com.Fachhochschulebib.fhb.pruefungsplaner.model

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

class JsonUuid {
    @SerializedName("uuid")
    @Expose
    var uuid: String? = null
}