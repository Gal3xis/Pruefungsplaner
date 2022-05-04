package com.fachhochschulebib.fhb.pruefungsplaner.model.retrofit

/**
 * Dataclass holding a simple replication of the TestPlanEntry in the remote database.
 *
 * @author Alexander Lange
 * @since 1.6
 */
data class GSONEntry(
    val FirstExaminer:String?,
    val SecondExaminer:String?,
    val Form:String?,
    val Semester:String?,
    val Date:String?,
    val Module:String?,
    val CourseName:String?,
    val Termin:String?,
    val ID:String?,
    val Room:String?,
    val CourseId:String?,
    val Status:String?,
    val Hint:String?,
    val Color:String?,
    val Timestamp:String?
)
