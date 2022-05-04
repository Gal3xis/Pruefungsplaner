package com.fachhochschulebib.fhb.pruefungsplaner.model.retrofit

/**
 * Dataclass holding a simple replication of the Course-Entity in the remote database.
 *
 * @param CourseName The Name of the course.
 * @param CourseShortName The shortname of the course.
 * @param FKFBID The foreign key of the faculty for this course.
 * @param SGID The primary key of the course.
 *
 * @author Alexander Lange
 * @since 1.6
 */
data class GSONCourse(
    val CourseName:String,
    val CourseShortName:String,
    val FKFBID:String,
    val SGID:String
    )
