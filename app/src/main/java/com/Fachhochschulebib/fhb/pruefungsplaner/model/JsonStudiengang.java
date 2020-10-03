package com.Fachhochschulebib.fhb.pruefungsplaner.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JsonStudiengang {

    @SerializedName("SGName")
    @Expose
    private String SGName;


    @SerializedName("SGKurz")
    @Expose
    private String SGKurz;


    @SerializedName("SGID")
    @Expose
    private String SGID;

    @SerializedName("FFBID")
    @Expose
    private String FFBID;

    public String getFFBID() {
        return FFBID;
    }

    public void setFFBID(String FFBID) {
        this.FFBID = FFBID;
    }

    public String getSGName() {
        return SGName;
    }

    public void setSGName(String SGName) {
        this.SGName = SGName;
    }

    public String getSGKurz() {
        return SGKurz;
    }

    public void setSGKurz(String SGKurz) {
        this.SGKurz = SGKurz;
    }

    public String getSGID() {
        return SGID;
    }

    public void setSGID(String SGID) {
        this.SGID = SGID;
    }
}
