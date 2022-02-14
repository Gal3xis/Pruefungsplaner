package com.Fachhochschulebib.fhb.pruefungsplaner.model.room

import androidx.annotation.NonNull
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "Course")
class Course {
    @PrimaryKey
    @ColumnInfo(name = "cId")
    @NonNull
    var sgid: String = "0"

    @ColumnInfo(name = "couresName")
    var courseName: String = ""

    @ColumnInfo(name = "facultyId")
    var facultyId: String = ""

    @ColumnInfo(name = "choosen")
    var choosen: Boolean = false
}