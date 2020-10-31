package com.Fachhochschulebib.fhb.pruefungsplaner.data;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Courses")
public class Courses {

    @PrimaryKey()
    @NonNull
    @ColumnInfo(name = "cId")
    private String sgid;

    @ColumnInfo(name = "couresName")
    private String courseName;

    @ColumnInfo(name = "facultyId")
    private String facultyId;

    @ColumnInfo(name = "choosen")
    private Boolean choosen;

    @NonNull
    public String getSgid() {
        return sgid;
    }

    public void setSgid(@NonNull String sgid) {
        this.sgid = sgid;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(String facultyId) {
        this.facultyId = facultyId;
    }

    public Boolean getChoosen() {
        return choosen;
    }

    public void setChoosen(Boolean choosen) {
        this.choosen = choosen;
    }

}
