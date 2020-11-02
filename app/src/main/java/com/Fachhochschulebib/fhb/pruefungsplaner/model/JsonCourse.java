package com.Fachhochschulebib.fhb.pruefungsplaner.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JsonCourse {

    @SerializedName("Course")
    @Expose
    private String Course;


    @SerializedName("CourseShort")
    @Expose
    private String CourseShort;


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

    public String getCourse() {
        return Course;
    }

    public void setCourse(String course) {
        this.Course = course;
    }

    public String getCourseShort() {
        return CourseShort;
    }

    public void setCourseShort(String courseShort) {
        this.CourseShort = courseShort;
    }

    public String getSGID() {
        return SGID;
    }

    public void setSGID(String SGID) {
        this.SGID = SGID;
    }
}
