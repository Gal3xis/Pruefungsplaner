package com.Fachhochschulebib.fhb.pruefungsplaner.data;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Studiengang")
public class Studiengang {

    @PrimaryKey()
    @NonNull
    @ColumnInfo(name = "sgid")
    private String sgid;

    @ColumnInfo(name = "studiengangName")
    private String StudiengangName;

    @ColumnInfo(name = "fachbereichId")
    private String FachbereichId;

    @ColumnInfo(name = "gewaehlt")
    private Boolean Gewaehlt;

    @NonNull
    public String getSgid() {
        return sgid;
    }

    public void setSgid(@NonNull String sgid) {
        this.sgid = sgid;
    }

    public String getStudiengangName() {
        return StudiengangName;
    }

    public void setStudiengangName(String studiengangName) {
        StudiengangName = studiengangName;
    }

    public String getFachbereichId() {
        return FachbereichId;
    }

    public void setFachbereichId(String fachbereichId) {
        FachbereichId = fachbereichId;
    }

    public Boolean getGewaehlt() {
        return Gewaehlt;
    }

    public void setGewaehlt(Boolean gewaehlt) {
        Gewaehlt = gewaehlt;
    }

}
