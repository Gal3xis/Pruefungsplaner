package com.fachhochschulebib.fhb.pruefungsplaner.model.room;

import java.lang.System;

/**
 * Class to access the local room database that holds a simple replication of the needed entities from the remote database.
 *
 * @author Alexander Lange
 * @since 1.6
 */
@androidx.room.Database(entities = {com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry.class, com.fachhochschulebib.fhb.pruefungsplaner.model.room.Uuid.class, com.fachhochschulebib.fhb.pruefungsplaner.model.room.Course.class, com.fachhochschulebib.fhb.pruefungsplaner.model.room.Faculty.class}, version = 10, exportSchema = true)
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\'\u0018\u0000 \u00052\u00020\u0001:\u0001\u0005B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H&\u00a8\u0006\u0006"}, d2 = {"Lcom/fachhochschulebib/fhb/pruefungsplaner/model/room/AppDatabase;", "Landroidx/room/RoomDatabase;", "()V", "userDao", "Lcom/fachhochschulebib/fhb/pruefungsplaner/model/room/UserDao;", "Companion", "app_debug"})
public abstract class AppDatabase extends androidx.room.RoomDatabase {
    @org.jetbrains.annotations.NotNull()
    public static final com.fachhochschulebib.fhb.pruefungsplaner.model.room.AppDatabase.Companion Companion = null;
    
    /**
     * Holds an instance of the [AppDatabase]
     */
    private static com.fachhochschulebib.fhb.pruefungsplaner.model.room.AppDatabase INSTANCE;
    
    public AppDatabase() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.fachhochschulebib.fhb.pruefungsplaner.model.room.UserDao userDao();
    
    @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u0007R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2 = {"Lcom/fachhochschulebib/fhb/pruefungsplaner/model/room/AppDatabase$Companion;", "", "()V", "INSTANCE", "Lcom/fachhochschulebib/fhb/pruefungsplaner/model/room/AppDatabase;", "getAppDatabase", "context", "Landroid/content/Context;", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
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
        @org.jetbrains.annotations.NotNull()
        public final com.fachhochschulebib.fhb.pruefungsplaner.model.room.AppDatabase getAppDatabase(@org.jetbrains.annotations.NotNull()
        android.content.Context context) {
            return null;
        }
    }
}