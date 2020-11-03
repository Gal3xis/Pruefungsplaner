//////////////////////////////
// JSONResponse
//
//
//
// autor:
// inhalt: parsen von den erhaltenen Retrofit Daten
// zugriffsdatum: 11.12.19
//
//
//
//
//
//
//////////////////////////////

package com.Fachhochschulebib.fhb.pruefungsplaner.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JsonResponse {

    @SerializedName("FirstExaminer")
    @Expose
    private String FirstExaminer;


    @SerializedName("SecondExaminer")
    @Expose
    private String SecondExaminer;


    @SerializedName("Form")
    @Expose
    private String Form;


    @SerializedName("Semester")
    @Expose
    private String Semester;


    @SerializedName("Date")
    @Expose
    private String Date;


    @SerializedName("Module")
    @Expose
    private String Module;


    @SerializedName("CourseName")
    @Expose
    private String CourseName;


    @SerializedName("Termin")
    @Expose
    private String Termin;


    @SerializedName("ID")
    @Expose
    private String ID;

    @SerializedName("Room")
    @Expose
    private String Room;

    // Start Merlin Gürtler
    @SerializedName("CourseId")
    @Expose
    private String CourseId;

    @SerializedName("Status")
    @Expose
    private String Status;

    @SerializedName("Hint")
    @Expose
    private String Hint;

    @SerializedName("Color")
    @Expose
    private String Color;
    // Ende Merlin Gürtler


    public String getColor() {
        return Color;
    }

    public void setColor(String color) {
        Color = color;
    }

    public String getHint() {
        return Hint;
    }

    public void setHint(String hint) {
        Hint = hint;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getFirstExaminer() {
        return FirstExaminer;
    }

    public void setFirstExaminer(String firstExaminer) {
        this.FirstExaminer = firstExaminer;
    }

    public String getRoom() {
        return Room;
    }

    public void setRoom(String room) {
        this.Room = room;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        this.Date = date;
    }

    public String getSecondExaminer() {
        return SecondExaminer;
    }

    public void setSecondExaminer(String secondExaminer) {
        SecondExaminer = secondExaminer;
    }

    public String getForm() {
        return Form;
    }

    public void setForm(String form) {
        Form = form;
    }

    public String getSemester() {
        return Semester;
    }

    public void setSemester(String semester) {
        Semester = semester;
    }

    public String getModule() {
        return Module;
    }

    public void setModule(String module) {
        Module = module;
    }

    public String getCourseName() {
        return CourseName;
    }

    public void setCourseName(String courseName) {
        CourseName = courseName;
    }

    public String getTermin() {
        return Termin;
    }

    public void setTermin(String termin) {
        Termin = termin;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getCourseId() {
        return CourseId;
    }

    public void setCourseId(String courseId) {
        CourseId = courseId;
    }

}
