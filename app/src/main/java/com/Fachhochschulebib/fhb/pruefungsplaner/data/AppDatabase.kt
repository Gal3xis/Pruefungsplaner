package com.Fachhochschulebib.fhb.pruefungsplaner.data

import android.content.Context
import androidx.room.Database
import com.Fachhochschulebib.fhb.pruefungsplaner.data.TestPlanEntry
import com.Fachhochschulebib.fhb.pruefungsplaner.data.Uuid
import com.Fachhochschulebib.fhb.pruefungsplaner.data.Courses
import androidx.room.RoomDatabase
import com.Fachhochschulebib.fhb.pruefungsplaner.data.UserDao
import com.Fachhochschulebib.fhb.pruefungsplaner.data.AppDatabase
import androidx.room.Room

@Database(
    entities = [TestPlanEntry::class, Uuid::class, Courses::class, Faculty::class],
    version = 2,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        private var INSTANCE: AppDatabase? = null
        fun getAppDatabase(context: Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java, "pruefplandaten"
                ).fallbackToDestructiveMigration()
                    .build()
            }
            return INSTANCE!!
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}