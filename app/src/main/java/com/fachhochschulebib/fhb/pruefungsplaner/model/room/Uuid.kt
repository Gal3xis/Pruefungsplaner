package com.fachhochschulebib.fhb.pruefungsplaner.model.room

import androidx.annotation.NonNull
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Entity

/**
 * Class holding the UUID from the remote database
 */
@Entity(tableName = "Uuid")
class Uuid {

    @PrimaryKey
    @ColumnInfo(name = "uuid")
    @NonNull
    var uuid: String = "0"
}