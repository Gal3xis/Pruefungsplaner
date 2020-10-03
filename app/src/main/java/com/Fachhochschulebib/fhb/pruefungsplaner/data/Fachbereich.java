package com.Fachhochschulebib.fhb.pruefungsplaner.data;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Fachbereich")
public class Fachbereich {

    @PrimaryKey()
    @NonNull
    @ColumnInfo(name = "fachbereichId")
    private String fachbereichId;

    @ColumnInfo(name = "fachbereichName")
    private String FachbereichName;

    public String getFachbereichId() {
        return fachbereichId;
    }

    public void setFachbereichId(String fachbereichId) {
        this.fachbereichId = fachbereichId;
    }

    public String getFachbereichName() {
        return FachbereichName;
    }

    public void setFachbereichName(String fachbereichName) {
        FachbereichName = fachbereichName;
    }
}
