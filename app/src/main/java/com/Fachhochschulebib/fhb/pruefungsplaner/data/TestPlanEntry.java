package com.Fachhochschulebib.fhb.pruefungsplaner.data;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "testPlanEntry")
public class TestPlanEntry {

    @PrimaryKey(autoGenerate = true)
    private int Count;

    @ColumnInfo(name = "ID")
    private String ID;

    @ColumnInfo(name = "Favorit")
    private boolean Favorit;

    @ColumnInfo(name = "Choosen")
    private boolean choosen;

    @ColumnInfo(name = "FirstExaminer")
    private String firstExaminer;

    @ColumnInfo(name = "SecondExaminer")
    private String secondExaminer;

    @ColumnInfo(name = "Validation")
    private String validation;

    @ColumnInfo(name = "Date")
    private String date;

    @ColumnInfo(name = "ExamForm")
    private String examForm;

    @ColumnInfo(name = "Semester")
    private String semester;

    @ColumnInfo(name = "Module")
    private String module;

    @ColumnInfo(name = "course")
    private String course;

    @ColumnInfo(name = "termin")
    private String termin;

    @ColumnInfo(name = "room")
    private String room;

    @ColumnInfo(name = "Status")
    private String status;

    @ColumnInfo(name = "Hint")
    private String hint;

    @ColumnInfo(name = "Color")
    private String color;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFirstExaminer() {
        return firstExaminer;
    }

    public void setFirstExaminer(String firstExaminer) {
        this.firstExaminer = firstExaminer;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSecondExaminer() {
        return secondExaminer;
    }

    public void setSecondExaminer(String secondExaminer) {
        this.secondExaminer = secondExaminer;
    }

    public String getExamForm() {
        return examForm;
    }

    public void setExamForm(String examForm) {
        this.examForm = examForm;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }


    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getTermin() {
        return termin;
    }

    public void setTermin(String termin) {
        this.termin = termin;
    }


    public String getID() {
        return this.ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }

    public String getValidation() {
        return validation;
    }

    public void setValidation(String validation) {
        this.validation = validation;
    }

    public boolean getFavorit() {
        return Favorit;
    }

    public void setFavorit(boolean fav) {
        Favorit = fav;
    }

    public boolean getChoosen() {
        return choosen;
    }

    public void setChoosen(boolean choosen) {
        this.choosen = choosen;
    }

}
