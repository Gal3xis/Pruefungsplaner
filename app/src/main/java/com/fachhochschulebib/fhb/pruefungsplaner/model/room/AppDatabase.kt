package com.fachhochschulebib.fhb.pruefungsplaner.model.room

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.Room

/**
 * Class to access the local room database that holds a simple replication of the needed entities from the remote database.
 *
 * @author Alexander Lange
 * @since 1.6
 */
@Database(
    entities = [TestPlanEntry::class, Uuid::class, Course::class, Faculty::class],
    version = 10,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        /**
         * Holds an instance of the [AppDatabase]
         */
        private var INSTANCE: AppDatabase? = null

        /**
         * Returns an [AppDatabase] that implements the [UserDao] to access the room database.
         *
         * @param context The Applicationcontext.
         *
         * @return An [AppDatabase] that implements the [UserDao] to access the room database.
         *
         * @author Alexander Lange
         * @since 1.6
         */
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
    }
}