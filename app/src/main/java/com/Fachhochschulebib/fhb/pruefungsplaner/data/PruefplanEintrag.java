package com.Fachhochschulebib.fhb.pruefungsplaner.data;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "pruefplanEintrag")
public class PruefplanEintrag {

    @PrimaryKey(autoGenerate = true)
    private int Count;

    @ColumnInfo(name = "ID")
    private String ID;

    @ColumnInfo(name = "Favorit")
    private boolean Favorit;

    @ColumnInfo(name = "Ausgewaehlt")
    private boolean ausgewaehlt;

    @ColumnInfo(name = "Erstpruefer")
    private String Erstpruefer;

    @ColumnInfo(name = "Zweitpruefer")
    private String Zweitpruefer;


    @ColumnInfo(name = "Validation")
    private String Validation;

    @ColumnInfo(name = "Datum")
    private String Datum;

    @ColumnInfo(name = "Pruefform")
    private String Pruefform;

    @ColumnInfo(name = "Semester")
    private String Semester;

    @ColumnInfo(name = "Modul")
    private String Modul;

    @ColumnInfo(name = "Studiengang")
    private String Studiengang;

    @ColumnInfo(name = "Termin")
    private String Termin;

    @ColumnInfo(name = "Raum")
    private String Raum;

    public String getErstpruefer() {
        return Erstpruefer;
    }

    public void setErstpruefer(String erstpruefer) {
        this.Erstpruefer = erstpruefer;
    }

    public String getRaum() {
        return Raum;
    }

    public void setRaum(String raum) {
        this.Raum = raum;
    }

    public String getDatum() {
        return Datum;
    }

    public void setDatum(String datum) {
        this.Datum = datum;
    }

    public String getZweitpruefer() {
        return Zweitpruefer;
    }

    public void setZweitpruefer(String zweitpruefer) {
        Zweitpruefer = zweitpruefer;
    }

    public String getPruefform() {
        return Pruefform;
    }

    public void setPruefform(String pruefform) {
        Pruefform = pruefform;
    }

    public String getSemester() {
        return Semester;
    }

    public void setSemester(String semester) {
        Semester = semester;
    }

    public String getModul() {
        return Modul;
    }

    public void setModul(String modul) {
        Modul = modul;
    }


    public String getStudiengang() {
        return Studiengang;
    }

    public void setStudiengang(String studiengang) {
        Studiengang = studiengang;
    }

    public String getTermin() {
        return Termin;
    }

    public void setTermin(String termin) {
        Termin = termin;
    }


    public String getID() {
        return this.ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }

    public String getValidation() {
        return Validation;
    }

    public void setValidation(String validation) {
        Validation = validation;
    }

    public boolean getFavorit() {
        return Favorit;
    }

    public void setFavorit(boolean fav) {
        Favorit = fav;
    }

    public boolean getAusgewaehlt() {
        return ausgewaehlt;
    }

    public void setAusgewaehlt(boolean ausgewaehlt) {
        this.ausgewaehlt = ausgewaehlt;
    }

}
