package com.fachhochschulebib.fhb.pruefungsplaner.model.room

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Class holding information about a faculty of the university.
 *
 * @author Alexander Lange
 * @since 1.6
 */
@Entity(tableName = "Faculty")
class Faculty {
    /**
     * The unique primary key
     */
    @PrimaryKey
    @ColumnInfo(name = "fbid")
    @NonNull
    var fbid: String = "0"

    /**
     * The name of the faculty
     */
    @ColumnInfo(name = "facName")
    @NonNull
    var facultyName: String = ""

    /**
     * The shortname of the faculty
     */
    @ColumnInfo(name = "facShortName")
    var facultyShortname: String = ""
}