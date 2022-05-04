package com.fachhochschulebib.fhb.pruefungsplaner.model.room;

import java.lang.System;

/**
 * Class holding information about a faculty of the university.
 *
 * @author Alexander Lange
 * @since 1.6
 */
@androidx.room.Entity(tableName = "Faculty")
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u000b\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002R\u001e\u0010\u0003\u001a\u00020\u00048\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001e\u0010\t\u001a\u00020\u00048\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u0006\"\u0004\b\u000b\u0010\bR\u001e\u0010\f\u001a\u00020\u00048\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u0006\"\u0004\b\u000e\u0010\b\u00a8\u0006\u000f"}, d2 = {"Lcom/fachhochschulebib/fhb/pruefungsplaner/model/room/Faculty;", "", "()V", "facultyName", "", "getFacultyName", "()Ljava/lang/String;", "setFacultyName", "(Ljava/lang/String;)V", "facultyShortname", "getFacultyShortname", "setFacultyShortname", "fbid", "getFbid", "setFbid", "app_debug"})
public final class Faculty {
    
    /**
     * The unique primary key
     */
    @org.jetbrains.annotations.NotNull()
    @androidx.annotation.NonNull()
    @androidx.room.ColumnInfo(name = "fbid")
    @androidx.room.PrimaryKey()
    private java.lang.String fbid = "0";
    
    /**
     * The name of the faculty
     */
    @org.jetbrains.annotations.NotNull()
    @androidx.annotation.NonNull()
    @androidx.room.ColumnInfo(name = "facName")
    private java.lang.String facultyName = "";
    
    /**
     * The shortname of the faculty
     */
    @org.jetbrains.annotations.NotNull()
    @androidx.room.ColumnInfo(name = "facShortName")
    private java.lang.String facultyShortname = "";
    
    public Faculty() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getFbid() {
        return null;
    }
    
    public final void setFbid(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getFacultyName() {
        return null;
    }
    
    public final void setFacultyName(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getFacultyShortname() {
        return null;
    }
    
    public final void setFacultyShortname(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
}