package com.Fachhochschulebib.fhb.pruefungsplaner.data;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {

    // Start Merlin Gürtler
    @Query("SELECT * FROM PruefplanEintrag WHERE Modul LIKE :modul")
    List<com.Fachhochschulebib.fhb.pruefungsplaner.data.PruefplanEintrag> getModule(String modul);

    @Query("UPDATE PruefplanEintrag SET Datum = :datum where ID = :id")
    void updateExam(String datum, String id);
    // Ende Merlin Gürtler


    @Query("SELECT * FROM PruefplanEintrag WHERE Validation = :validation")
    List<com.Fachhochschulebib.fhb.pruefungsplaner.data.PruefplanEintrag> getAll(String validation);

    @Query("SELECT * FROM PruefplanEintrag")
    List<com.Fachhochschulebib.fhb.pruefungsplaner.data.PruefplanEintrag> getAll2();

    @Query("SELECT Studiengang FROM PruefplanEintrag")
    List<String> getStudiengang();

    @Query("SELECT Erstpruefer FROM PruefplanEintrag")
    List<String> getErstpruefer();

    @Query("SELECT Modul FROM PruefplanEintrag")
    List<String> getModul();

    @Query("SELECT COUNT(*) from PruefplanEintrag")
    int countUsers();

    @Insert
    void insertAll(com.Fachhochschulebib.fhb.pruefungsplaner.data.PruefplanEintrag... pruefplanEintrags);

    @Query ("UPDATE PruefplanEintrag SET Favorit = :favorit WHERE ID = :id")
    void update(boolean favorit, int id);

    @Query ("UPDATE PruefplanEintrag SET Ausgewaehlt = :pruefungen WHERE ID = :id")
    void update2(boolean pruefungen, int id);

    @Query ("UPDATE PruefplanEintrag SET Validation  = :nullSetzen WHERE Validation = :validation")
    void updateValidation(String nullSetzen, String validation);

    @Query ("UPDATE PruefplanEintrag SET Ausgewaehlt = :pruefungen ")
    void sucheUndZurueckSetzen(boolean pruefungen);

    @Delete
    void delete(com.Fachhochschulebib.fhb.pruefungsplaner.data.PruefplanEintrag pruefplanEintrag);
}
