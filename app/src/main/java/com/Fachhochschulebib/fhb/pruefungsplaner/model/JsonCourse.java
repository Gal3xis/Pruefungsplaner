package com.Fachhochschulebib.fhb.pruefungsplaner.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JsonCourse {

    @SerializedName("CourseName")
    @Expose
    private String CourseName;


    @SerializedName("CourseShortName")
    @Expose
    private String CourseShortName;


    @SerializedName("SGID")
    @Expose
    private String SGID;

    @SerializedName("FKFBID")
    @Expose
    private String FKFBID;

    public String getFKFBID() {
        return FKFBID;
    }

    public void setFKFBID(String FKFBID) {
        this.FKFBID = FKFBID;
    }

    public String getCourseName() {
        return CourseName;
    }

    public void setCourseName(String courseName) {
        this.CourseName = courseName;
    }

    public String getCourseShortName() {
        return CourseShortName;
    }

    public void setCourseShortName(String courseShortName) {
        this.CourseShortName = courseShortName;
    }

    public String getSGID() {
        return SGID;
    }

    public void setSGID(String SGID) {
        this.SGID = SGID;
    }
}
