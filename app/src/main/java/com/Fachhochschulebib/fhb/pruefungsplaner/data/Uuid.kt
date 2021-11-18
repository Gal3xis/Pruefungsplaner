package com.Fachhochschulebib.fhb.pruefungsplaner.data

import androidx.annotation.NonNull
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "Uuid")
class Uuid {
    @PrimaryKey
    @ColumnInfo(name = "uuid")
    var uuid: String? = null
}