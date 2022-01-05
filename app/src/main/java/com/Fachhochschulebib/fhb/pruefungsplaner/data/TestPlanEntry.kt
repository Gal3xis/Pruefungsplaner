package com.Fachhochschulebib.fhb.pruefungsplaner.data

import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "testPlanEntry")
class TestPlanEntry {
    @PrimaryKey(autoGenerate = true)
    var count = 0

    @ColumnInfo(name = "ID")
    var id: String? = null

    @ColumnInfo(name = "Favorit")
    var favorit = false

    @ColumnInfo(name = "Choosen")
    var choosen = false

    @ColumnInfo(name = "FirstExaminer")
    var firstExaminer: String? = null

    @ColumnInfo(name = "SecondExaminer")
    var secondExaminer: String? = null

    @ColumnInfo(name = "Validation")
    var validation: String? = null

    //Format:yyyy-MM-dd HH:mm:ss
    @ColumnInfo(name = "Date")
    var date: String? = null

    @ColumnInfo(name = "ExamForm")
    var examForm: String? = null

    @ColumnInfo(name = "Semester")
    var semester: String? = null

    @ColumnInfo(name = "Module")
    var module: String? = null

    @ColumnInfo(name = "course")
    var course: String? = null

    @ColumnInfo(name = "termin")
    var termin: String? = null

    @ColumnInfo(name = "room")
    var room: String? = null

    @ColumnInfo(name = "Status")
    var status: String? = null

    @ColumnInfo(name = "Hint")
    var hint: String? = null

    @ColumnInfo(name = "Color")
    var color: String? = null
}