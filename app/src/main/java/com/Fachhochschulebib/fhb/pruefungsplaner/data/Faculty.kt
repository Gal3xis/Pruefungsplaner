package com.Fachhochschulebib.fhb.pruefungsplaner.data

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Faculty")
class Faculty {
    @PrimaryKey
    @ColumnInfo(name = "fbid")
    @NonNull
    var fbid: String = "0"

    @ColumnInfo(name = "facName")
    @NonNull
    var facultyName: String = ""

    @ColumnInfo(name = "facShortName")
    var facultyShortname: String = ""
}