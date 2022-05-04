package com.fachhochschulebib.fhb.pruefungsplaner.model.room;

import java.lang.System;

/**
 * Class holding the UUID from the remote database
 */
@androidx.room.Entity(tableName = "Uuid")
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002R\u001e\u0010\u0003\u001a\u00020\u00048\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\b\u00a8\u0006\t"}, d2 = {"Lcom/fachhochschulebib/fhb/pruefungsplaner/model/room/Uuid;", "", "()V", "uuid", "", "getUuid", "()Ljava/lang/String;", "setUuid", "(Ljava/lang/String;)V", "app_debug"})
public final class Uuid {
    @org.jetbrains.annotations.NotNull()
    @androidx.annotation.NonNull()
    @androidx.room.ColumnInfo(name = "uuid")
    @androidx.room.PrimaryKey()
    private java.lang.String uuid = "0";
    
    public Uuid() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getUuid() {
        return null;
    }
    
    public final void setUuid(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
}