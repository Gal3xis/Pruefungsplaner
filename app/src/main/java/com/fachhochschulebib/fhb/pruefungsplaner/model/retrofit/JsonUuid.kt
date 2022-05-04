package com.fachhochschulebib.fhb.pruefungsplaner.model.retrofit

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

/**
 * Class holding the UUID from the remote database.
 *
 * @author Alexander Lange
 * @since 1.6
 */
class JsonUuid {
    @SerializedName("uuid")
    @Expose
    var uuid: String? = null
}