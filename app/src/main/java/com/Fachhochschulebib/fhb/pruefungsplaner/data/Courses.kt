package com.Fachhochschulebib.fhb.pruefungsplaner.data

import androidx.annotation.NonNull
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "Courses")
class Courses {
    @PrimaryKey
    @ColumnInfo(name = "cId")
    @NonNull
    var sgid: String? = null

    @ColumnInfo(name = "couresName")
    var courseName: String? = null

    @ColumnInfo(name = "facultyId")
    var facultyId: String? = null

    @ColumnInfo(name = "choosen")
    var choosen: Boolean? = null
}