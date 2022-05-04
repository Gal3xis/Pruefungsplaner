package com.fachhochschulebib.fhb.pruefungsplaner.model.room;

import java.lang.System;

/**
 * Class holding information about an entry in the plan.
 *
 * @author Alexander Lange
 * @since 1.6
 */
@androidx.room.Entity(tableName = "testPlanEntry")
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u000b\n\u0002\u0010\u000b\n\u0002\b#\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002R \u0010\u0003\u001a\u0004\u0018\u00010\u00048\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR \u0010\t\u001a\u0004\u0018\u00010\u00048\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u0006\"\u0004\b\u000b\u0010\bR \u0010\f\u001a\u0004\u0018\u00010\u00048\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u0006\"\u0004\b\u000e\u0010\bR\u001e\u0010\u000f\u001a\u00020\u00108\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R \u0010\u0015\u001a\u0004\u0018\u00010\u00048\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u0006\"\u0004\b\u0017\u0010\bR \u0010\u0018\u001a\u0004\u0018\u00010\u00048\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\u0006\"\u0004\b\u001a\u0010\bR\u001e\u0010\u001b\u001a\u00020\u00048\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001c\u0010\u0006\"\u0004\b\u001d\u0010\bR \u0010\u001e\u001a\u0004\u0018\u00010\u00048\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001f\u0010\u0006\"\u0004\b \u0010\bR \u0010!\u001a\u0004\u0018\u00010\u00048\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\"\u0010\u0006\"\u0004\b#\u0010\bR \u0010$\u001a\u0004\u0018\u00010\u00048\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b%\u0010\u0006\"\u0004\b&\u0010\bR \u0010\'\u001a\u0004\u0018\u00010\u00048\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b(\u0010\u0006\"\u0004\b)\u0010\bR \u0010*\u001a\u0004\u0018\u00010\u00048\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b+\u0010\u0006\"\u0004\b,\u0010\bR \u0010-\u001a\u0004\u0018\u00010\u00048\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b.\u0010\u0006\"\u0004\b/\u0010\bR \u00100\u001a\u0004\u0018\u00010\u00048\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b1\u0010\u0006\"\u0004\b2\u0010\b\u00a8\u00063"}, d2 = {"Lcom/fachhochschulebib/fhb/pruefungsplaner/model/room/TestPlanEntry;", "", "()V", "course", "", "getCourse", "()Ljava/lang/String;", "setCourse", "(Ljava/lang/String;)V", "date", "getDate", "setDate", "examForm", "getExamForm", "setExamForm", "favorite", "", "getFavorite", "()Z", "setFavorite", "(Z)V", "firstExaminer", "getFirstExaminer", "setFirstExaminer", "hint", "getHint", "setHint", "id", "getId", "setId", "module", "getModule", "setModule", "room", "getRoom", "setRoom", "secondExaminer", "getSecondExaminer", "setSecondExaminer", "semester", "getSemester", "setSemester", "status", "getStatus", "setStatus", "termin", "getTermin", "setTermin", "timeStamp", "getTimeStamp", "setTimeStamp", "app_debug"})
public final class TestPlanEntry {
    
    /**
     * The unique primary key
     */
    @org.jetbrains.annotations.NotNull()
    @androidx.annotation.NonNull()
    @androidx.room.ColumnInfo(name = "ID")
    @androidx.room.PrimaryKey()
    private java.lang.String id = "0";
    
    /**
     * Whether the entry has been selected as a favorite or not
     */
    @androidx.room.ColumnInfo(name = "Favorite")
    private boolean favorite = false;
    
    /**
     * The first examiner of this exam
     */
    @org.jetbrains.annotations.Nullable()
    @androidx.room.ColumnInfo(name = "FirstExaminer")
    private java.lang.String firstExaminer;
    
    /**
     * The second examiner of this exam
     */
    @org.jetbrains.annotations.Nullable()
    @androidx.room.ColumnInfo(name = "SecondExaminer")
    private java.lang.String secondExaminer;
    
    /**
     * The date of the exam, formatted like "yyyy-MM-dd HH:mm:ss"
     */
    @org.jetbrains.annotations.Nullable()
    @androidx.room.ColumnInfo(name = "Date")
    private java.lang.String date;
    
    /**
     * The form of the exam (written,oral etc), combined with the duration.
     */
    @org.jetbrains.annotations.Nullable()
    @androidx.room.ColumnInfo(name = "ExamForm")
    private java.lang.String examForm;
    
    /**
     * The default semester for this exam
     */
    @org.jetbrains.annotations.Nullable()
    @androidx.room.ColumnInfo(name = "Semester")
    private java.lang.String semester;
    
    /**
     * The module for this exam
     */
    @org.jetbrains.annotations.Nullable()
    @androidx.room.ColumnInfo(name = "Module")
    private java.lang.String module;
    
    /**
     * The course for this exam
     */
    @org.jetbrains.annotations.Nullable()
    @androidx.room.ColumnInfo(name = "course")
    private java.lang.String course;
    
    /**
     * The termin of the exam TODO Rename
     */
    @org.jetbrains.annotations.Nullable()
    @androidx.room.ColumnInfo(name = "termin")
    private java.lang.String termin;
    
    /**
     * The room of the exam
     */
    @org.jetbrains.annotations.Nullable()
    @androidx.room.ColumnInfo(name = "room")
    private java.lang.String room;
    
    /**
     * The state of the entry
     * @see com.fachhochschulebib.fhb.pruefungsplaner.utils.Utils.statusColors
     */
    @org.jetbrains.annotations.Nullable()
    @androidx.room.ColumnInfo(name = "Status")
    private java.lang.String status;
    
    /**
     * A hint for this entry
     */
    @org.jetbrains.annotations.Nullable()
    @androidx.room.ColumnInfo(name = "Hint")
    private java.lang.String hint;
    
    /**
     * The TimeStamp of the last change of this entry
     */
    @org.jetbrains.annotations.Nullable()
    @androidx.room.ColumnInfo(name = "TimeStamp")
    private java.lang.String timeStamp;
    
    public TestPlanEntry() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getId() {
        return null;
    }
    
    public final void setId(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
    
    public final boolean getFavorite() {
        return false;
    }
    
    public final void setFavorite(boolean p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getFirstExaminer() {
        return null;
    }
    
    public final void setFirstExaminer(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getSecondExaminer() {
        return null;
    }
    
    public final void setSecondExaminer(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getDate() {
        return null;
    }
    
    public final void setDate(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getExamForm() {
        return null;
    }
    
    public final void setExamForm(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getSemester() {
        return null;
    }
    
    public final void setSemester(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getModule() {
        return null;
    }
    
    public final void setModule(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getCourse() {
        return null;
    }
    
    public final void setCourse(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getTermin() {
        return null;
    }
    
    public final void setTermin(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getRoom() {
        return null;
    }
    
    public final void setRoom(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getStatus() {
        return null;
    }
    
    public final void setStatus(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getHint() {
        return null;
    }
    
    public final void setHint(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getTimeStamp() {
        return null;
    }
    
    public final void setTimeStamp(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
}