package com.fachhochschulebib.fhb.pruefungsplaner.model.room;

import java.lang.System;

/**
 * Class holding information about a Course of the university.
 *
 * @author Alexander Lange
 */
@androidx.room.Entity(tableName = "Course")
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u000b\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002R\u001e\u0010\u0003\u001a\u00020\u00048\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001e\u0010\t\u001a\u00020\n8\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u001e\u0010\u000f\u001a\u00020\n8\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\f\"\u0004\b\u0011\u0010\u000eR\u001e\u0010\u0012\u001a\u00020\n8\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\f\"\u0004\b\u0014\u0010\u000e\u00a8\u0006\u0015"}, d2 = {"Lcom/fachhochschulebib/fhb/pruefungsplaner/model/room/Course;", "", "()V", "chosen", "", "getChosen", "()Z", "setChosen", "(Z)V", "courseName", "", "getCourseName", "()Ljava/lang/String;", "setCourseName", "(Ljava/lang/String;)V", "facultyId", "getFacultyId", "setFacultyId", "sgid", "getSgid", "setSgid", "app_debug"})
public final class Course {
    
    /**
     * The Unique primary key.
     */
    @org.jetbrains.annotations.NotNull()
    @androidx.annotation.NonNull()
    @androidx.room.ColumnInfo(name = "cId")
    @androidx.room.PrimaryKey()
    private java.lang.String sgid = "0";
    
    /**
     * The name of the course
     */
    @org.jetbrains.annotations.NotNull()
    @androidx.room.ColumnInfo(name = "courseName")
    private java.lang.String courseName = "";
    
    /**
     * The id of the faculty for this course.
     */
    @org.jetbrains.annotations.NotNull()
    @androidx.room.ColumnInfo(name = "facultyId")
    private java.lang.String facultyId = "";
    
    /**
     * Whether the course has been chosen by the user or not.
     */
    @androidx.room.ColumnInfo(name = "chosen")
    private boolean chosen = false;
    
    public Course() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getSgid() {
        return null;
    }
    
    public final void setSgid(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getCourseName() {
        return null;
    }
    
    public final void setCourseName(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getFacultyId() {
        return null;
    }
    
    public final void setFacultyId(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
    
    public final boolean getChosen() {
        return false;
    }
    
    public final void setChosen(boolean p0) {
    }
}