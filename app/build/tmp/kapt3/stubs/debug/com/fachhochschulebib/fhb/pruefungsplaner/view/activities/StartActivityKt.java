package com.fachhochschulebib.fhb.pruefungsplaner.view.activities;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 2, d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0086T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0003X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0004"}, d2 = {"CHANGE_FLAG", "", "UPDATE_REQUEST_CODE", "", "app_debug"})
public final class StartActivityKt {
    
    /**
     * Unique code for the update requests.
     * @see StartActivity.initUpdateManager
     */
    public static final int UPDATE_REQUEST_CODE = 100;
    
    /**
     * String contant that is used as extra in the intent to notify that the Start Activity shall not skip if a selected main course is recognized.
     * Used for changing the faculty.
     */
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CHANGE_FLAG = "changeFlag";
}