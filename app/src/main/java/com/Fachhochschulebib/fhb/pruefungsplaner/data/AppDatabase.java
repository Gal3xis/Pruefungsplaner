package com.Fachhochschulebib.fhb.pruefungsplaner.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {TestPlanEntry.class, Uuid.class, Courses.class}, version = 1, exportSchema = true)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract UserDao userDao();

    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class, "pruefplandaten")
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

}

