package com.Fachhochschulebib.fhb.pruefungsplaner.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;



@Dao
public interface UserDao {


    @Query("SELECT * FROM Pruefplan WHERE Validation = :validation")
    List<Pruefplan> getAll(String validation);

    @Query("SELECT * FROM Pruefplan")
    List<Pruefplan> getAll2();

    @Query("SELECT Studiengang FROM Pruefplan")
    List<String> getStudiengang();

    @Query("SELECT Erstpruefer FROM Pruefplan")
    List<String> getErstpruefer();

    @Query("SELECT Modul FROM Pruefplan")
    List<String> getModul();

    @Query("SELECT COUNT(*) from Pruefplan")
    int countUsers();

    @Insert
    void insertAll(Pruefplan... pruefplans);

    @Query ("UPDATE Pruefplan SET Favorit = :favorit WHERE ID = :id")
    void update(boolean favorit, int id );

    @Query ("UPDATE Pruefplan SET Ausgewaehlt = :pruefungen WHERE ID = :id")
    void update2(boolean pruefungen,int id);

    @Query ("UPDATE Pruefplan SET Validation  = :nullSetzen WHERE Validation = :validation")
    void updateValidation(String nullSetzen, String validation);

    @Query ("UPDATE Pruefplan SET Ausgewaehlt = :pruefungen ")
    void suchezuruecksetzen(boolean pruefungen);

    @Delete
    void delete(Pruefplan pruefplan);
}
