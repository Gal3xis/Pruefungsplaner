package com.fachhochschulebib.fhb.pruefungsplaner.model.room

import androidx.annotation.NonNull
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Entity

/**
 * Class holding information about an entry in the plan.
 *
 * @author Alexander Lange
 * @since 1.6
 */
@Entity(tableName = "testPlanEntry")
class TestPlanEntry {
    /**
     * The unique primary key
     */
    @PrimaryKey
    @ColumnInfo(name = "ID")
    @NonNull
    var id: String = "0"

    /**
     * Whether the entry has been selected as a favorite or not
     */
    @ColumnInfo(name = "Favorite")
    var favorite = false

    /**
     * The first examiner of this exam
     */
    @ColumnInfo(name = "FirstExaminer")
    var firstExaminer: String? = null

    /**
     * The second examiner of this exam
     */
    @ColumnInfo(name = "SecondExaminer")
    var secondExaminer: String? = null

    /**
     * The date of the exam, formatted like "yyyy-MM-dd HH:mm:ss"
     */
    @ColumnInfo(name = "Date")
    var date: String? = null

    /**
     * The form of the exam (written,oral etc), combined with the duration.
     */
    @ColumnInfo(name = "ExamForm")
    var examForm: String? = null

    /**
     * The default semester for this exam
     */
    @ColumnInfo(name = "Semester")
    var semester: String? = null

    /**
     * The module for this exam
     */
    @ColumnInfo(name = "Module")
    var module: String? = null

    /**
     * The course for this exam
     */
    @ColumnInfo(name = "course")
    var course: String? = null

    /**
     * The termin of the exam TODO Rename
     */
    @ColumnInfo(name = "termin")
    var termin: String? = null

    /**
     * The room of the exam
     */
    @ColumnInfo(name = "room")
    var room: String? = null

    /**
     * The state of the entry
     * @see com.fachhochschulebib.fhb.pruefungsplaner.utils.Utils.statusColors
     */
    @ColumnInfo(name = "Status")
    var status: String? = null

    /**
     * A hint for this entry
     */
    @ColumnInfo(name = "Hint")
    var hint: String? = null

    /**
     * The TimeStamp of the last change of this entry
     */
    @ColumnInfo(name = "TimeStamp")
    var timeStamp:String? = null
}