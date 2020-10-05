package com.Fachhochschulebib.fhb.pruefungsplaner.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JsonUpdate {

    @SerializedName("ID")
    @Expose
    private String ID;


    @SerializedName("Datum")
    @Expose
    private String Datum;


    @SerializedName("Status")
    @Expose
    private String Status;

    @SerializedName("Hint")
    @Expose
    private String hint;

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getDatum() {
        return Datum;
    }

    public void setDatum(String datum) {
        Datum = datum;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
