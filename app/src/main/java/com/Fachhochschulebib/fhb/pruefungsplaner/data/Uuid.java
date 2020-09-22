package com.Fachhochschulebib.fhb.pruefungsplaner.data;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Uuid")
public class Uuid {

    @PrimaryKey()
    @NonNull
    @ColumnInfo(name = "uuid")
    private String Uuid;

    public String getUuid() {
        return Uuid;
    }

    public void setUuid(String uuid) {
        Uuid = uuid;
    }
}
