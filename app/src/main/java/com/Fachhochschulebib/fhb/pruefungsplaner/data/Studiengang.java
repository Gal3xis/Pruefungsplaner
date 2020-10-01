package com.Fachhochschulebib.fhb.pruefungsplaner.data;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Studiengang")
public class Studiengang {

    @PrimaryKey()
    @ColumnInfo(name = "studiengangName")
    private String Studiengangname;

    @ColumnInfo(name = "fachbereichId")
    private String FachbereichId;

    @ColumnInfo(name = "gewaehlt")
    private Boolean Gewaehlt;

    @ColumnInfo(name = "hauptStudiengang")
    private Boolean HauptStudiengang;

}
