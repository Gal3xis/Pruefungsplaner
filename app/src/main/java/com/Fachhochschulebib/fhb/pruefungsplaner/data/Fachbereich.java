package com.Fachhochschulebib.fhb.pruefungsplaner.data;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Fachbereich")
public class Fachbereich {

    @PrimaryKey()
    @ColumnInfo(name = "fachbereichId")
    private String fachbereichId;

    @ColumnInfo(name = "fachbereichName")
    private String FachbereichName;

}
