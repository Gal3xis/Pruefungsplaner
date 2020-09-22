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
    List<PruefplanEintrag> getModule(String modul);

    @Query("UPDATE PruefplanEintrag SET Datum = :datum where ID = :id")
    void updateExam(String datum, String id);

    @Query("SELECT * from PruefplanEintrag WHERE Erstpruefer LIKE :prof")
    List<PruefplanEintrag> getModuleProf(String prof);

    @Query("SELECT DISTINCT Studiengang FROM PruefplanEintrag ORDER BY Studiengang")
    List<String> getStudiengangOrdered();

    @Query("SELECT * FROM PruefplanEintrag WHERE Modul LIKE :modul AND Studiengang = :studiengang")
    List<PruefplanEintrag>
    getModuleWithCourseAndModule(String modul, String studiengang);

    @Query("SELECT * FROM PruefplanEintrag WHERE Studiengang = :studiengang ORDER BY Modul")
    List<PruefplanEintrag>
    getModuleWithCourseOrdered(String studiengang);

    @Query("SELECT Modul FROM PruefplanEintrag ORDER BY Modul")
    List<String>
    getModuleOrdered();

    @Query("SELECT DISTINCT Erstpruefer FROM PruefplanEintrag WHERE Studiengang = :selectedStudiengang")
    List<String> getErstprueferDistinct(String selectedStudiengang);

    @Query("SELECT Modul FROM PruefplanEintrag WHERE Studiengang = :selectedStudiengang ORDER BY Modul")
    List<String> getModuleWithCourseDistinct(String selectedStudiengang);

    @Query("SELECT DISTINCT Termin FROM PruefplanEintrag LIMIT 1")
    String getTermin();

    @Query("SELECT * FROM PruefplanEintrag WHERE Favorit = :favorite")
    List<PruefplanEintrag> getFavorites(boolean favorite);

    @Query("SELECT * FROM PruefplanEintrag WHERE ID = :id")
    PruefplanEintrag getPruefung(String id);

    @Query ("UPDATE PruefplanEintrag SET Pruefform = :pruefform WHERE ID = :id")
    void updatePruefform(String pruefform, int id);

    @Query("INSERT INTO Uuid VALUES (:uuid)")
    void insertUuid(String uuid);

    @Query("SELECT * FROM Uuid")
    Uuid getUuid();
    // Ende Merlin Gürtler


    @Query("SELECT * FROM PruefplanEintrag WHERE Validation = :validation")
    List<PruefplanEintrag> getAll(String validation);

    @Query("SELECT * FROM PruefplanEintrag")
    List<PruefplanEintrag> getAll2();

    @Query("SELECT Studiengang FROM PruefplanEintrag")
    List<String> getStudiengang();

    @Query("SELECT Erstpruefer FROM PruefplanEintrag")
    List<String> getErstpruefer();

    @Query("SELECT Modul FROM PruefplanEintrag")
    List<String> getModul();

    @Query("SELECT COUNT(*) from PruefplanEintrag")
    int countUsers();

    @Insert
    void insertAll(PruefplanEintrag... pruefplanEintrags);

    @Query ("UPDATE PruefplanEintrag SET Favorit = :favorit WHERE ID = :id")
    void update(boolean favorit, int id);

    @Query ("UPDATE PruefplanEintrag SET Ausgewaehlt = :pruefungen WHERE ID = :id")
    void update2(boolean pruefungen, int id);

    @Query ("UPDATE PruefplanEintrag SET Validation  = :nullSetzen WHERE Validation = :validation")
    void updateValidation(String nullSetzen, String validation);

    @Query ("UPDATE PruefplanEintrag SET Ausgewaehlt = :pruefungen ")
    void sucheUndZurueckSetzen(boolean pruefungen);

    @Delete
    void delete(PruefplanEintrag pruefplanEintrag);
}
