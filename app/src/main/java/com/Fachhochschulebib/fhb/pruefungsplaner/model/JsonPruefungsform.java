package com.Fachhochschulebib.fhb.pruefungsplaner.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JsonPruefungsform {

    @SerializedName("PFBez")
    @Expose
    private String PFBez;


    @SerializedName("PFDauer")
    @Expose
    private String PFDauer;


    @SerializedName("PForm")
    @Expose
    private String PForm;


    @SerializedName("pfid")
    @Expose
    private String pfid;

    public String getPFBez() {
        return PFBez;
    }

    public void setPFBez(String PFBez) {
        this.PFBez = PFBez;
    }

    public String getPFDauer() {
        return PFDauer;
    }

    public void setPFDauer(String PFDauer) {
        this.PFDauer = PFDauer;
    }

    public String getPForm() {
        return PForm;
    }

    public void setPForm(String PForm) {
        this.PForm = PForm;
    }

    public String getPfid() {
        return pfid;
    }

    public void setPfid(String pfid) {
        this.pfid = pfid;
    }
}
