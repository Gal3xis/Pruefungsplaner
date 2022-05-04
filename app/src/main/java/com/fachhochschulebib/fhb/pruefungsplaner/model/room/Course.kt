package com.fachhochschulebib.fhb.pruefungsplaner.model.room

import androidx.annotation.NonNull
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Entity

/**
 * Class holding information about a Course of the university.
 *
 * @author Alexander Lange
 */
@Entity(tableName = "Course")
class Course {
    /**
     * The Unique primary key.
     */
    @PrimaryKey
    @ColumnInfo(name = "cId")
    @NonNull
    var sgid: String = "0"

    /**
     * The name of the course
     */
    @ColumnInfo(name = "courseName")
    var courseName: String = ""

    /**
     * The id of the faculty for this course.
     */
    @ColumnInfo(name = "facultyId")
    var facultyId: String = ""

    /**
     * Whether the course has been chosen by the user or not.
     */
    @ColumnInfo(name = "chosen")
    var chosen: Boolean = false
}