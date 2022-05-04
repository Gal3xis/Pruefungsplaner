package com.fachhochschulebib.fhb.pruefungsplaner.utils;

import java.lang.System;

/**
 * IO-Interface to access the GoogleCalendar. Provides Methods to insert and delete
 * entries and also works with [TestPlanEntry]-Objects.
 *
 * @author Alexander Lange
 * @since 1.6
 *
 * **See Also:**[Android Calendar Intent-Tutorial](https://itnext.io/android-calendar-intent-8536232ecb38)
 * **See Also:**[StackOverflow guide for get,update and delete](https://stackoverflow.com/questions/23626240/edit-delete-google-calendar-events-and-get-event-id)
 */
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000v\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u00c6\u0002\u0018\u00002\u00020\u0001:\u000223B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J(\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000b2\b\u0010\r\u001a\u0004\u0018\u00010\u000eJR\u0010\u0006\u001a\u00020\u00072\u0006\u0010\f\u001a\u00020\u000b2\u0006\u0010\n\u001a\u00020\u000b2\b\b\u0002\u0010\u000f\u001a\u00020\u00102\b\b\u0002\u0010\u0011\u001a\u00020\u00102\b\b\u0002\u0010\u0012\u001a\u00020\u00132\b\b\u0002\u0010\u0014\u001a\u00020\u00132\b\b\u0002\u0010\u0015\u001a\u00020\u00162\b\b\u0002\u0010\u0017\u001a\u00020\u0016J\u0018\u0010\u0018\u001a\u00020\u00192\u0006\u0010\b\u001a\u00020\t2\b\u0010\r\u001a\u0004\u0018\u00010\u000eJG\u0010\u0018\u001a\u00020\u00192\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u00102\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u00102\n\b\u0002\u0010\u0012\u001a\u0004\u0018\u00010\u00132\n\b\u0002\u0010\u0014\u001a\u0004\u0018\u00010\u00132\n\b\u0002\u0010\u0015\u001a\u0004\u0018\u00010\u0016\u00a2\u0006\u0002\u0010\u001aJ\u0016\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\f\u001a\u00020\u000bJ\u001c\u0010\u001d\u001a\u00020\u001c2\u0006\u0010\b\u001a\u00020\t2\f\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u000b0\u001fJ\u0018\u0010 \u001a\u00020\u00162\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\f\u001a\u00020\u000bH\u0002J\'\u0010!\u001a\u0004\u0018\u00010\u000b2\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\"\u001a\u00020\u000eH\u0002\u00a2\u0006\u0002\u0010#J\u0014\u0010$\u001a\b\u0012\u0004\u0012\u00020%0\u001f2\u0006\u0010\b\u001a\u00020\tJ\u0010\u0010&\u001a\u0004\u0018\u00010%2\u0006\u0010\b\u001a\u00020\tJ\b\u0010\'\u001a\u00020\u000bH\u0002J\u0018\u0010(\u001a\u00020)2\u0006\u0010\b\u001a\u00020\t2\u0006\u0010*\u001a\u00020\u0019H\u0002J/\u0010+\u001a\u0004\u0018\u00010\u000b2\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\r\u001a\u00020\u000e2\b\b\u0002\u0010,\u001a\u00020-\u00a2\u0006\u0002\u0010.J\u0018\u0010/\u001a\u00020)2\u0006\u0010\b\u001a\u00020\t2\u0006\u00100\u001a\u00020\u0007H\u0002J \u00101\u001a\u00020)2\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\f\u001a\u00020\u000b2\u0006\u00100\u001a\u00020\u0007H\u0002R\u0016\u0010\u0003\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u00064"}, d2 = {"Lcom/fachhochschulebib/fhb/pruefungsplaner/utils/CalendarIO;", "", "()V", "EVENTS_URI", "Landroid/net/Uri;", "kotlin.jvm.PlatformType", "createEvent", "Landroid/content/ContentValues;", "context", "Landroid/content/Context;", "CAL_ID", "", "id", "e", "Lcom/fachhochschulebib/fhb/pruefungsplaner/model/room/TestPlanEntry;", "title", "", "description", "startDate", "Ljava/util/Calendar;", "endDate", "allDay", "", "hasAlarm", "createIntent", "Landroid/content/Intent;", "(Ljava/lang/String;Ljava/lang/String;Ljava/util/Calendar;Ljava/util/Calendar;Ljava/lang/Boolean;)Landroid/content/Intent;", "deleteEvent", "", "deleteEvents", "ids", "", "eventExists", "forceInsert", "testPlanEntry", "(Landroid/content/Context;JLcom/fachhochschulebib/fhb/pruefungsplaner/model/room/TestPlanEntry;)Ljava/lang/Long;", "getCalendars", "Lcom/fachhochschulebib/fhb/pruefungsplaner/utils/CalendarIO$SmartphoneCalendar;", "getPrimaryCalendar", "getRandomId", "indirectInsert", "", "intent", "insertEntry", "insertionType", "Lcom/fachhochschulebib/fhb/pruefungsplaner/utils/CalendarIO$InsertionType;", "(Landroid/content/Context;JLcom/fachhochschulebib/fhb/pruefungsplaner/model/room/TestPlanEntry;Lcom/fachhochschulebib/fhb/pruefungsplaner/utils/CalendarIO$InsertionType;)Ljava/lang/Long;", "insertEvent", "event", "updateEvent", "InsertionType", "SmartphoneCalendar", "app_debug"})
public final class CalendarIO {
    @org.jetbrains.annotations.NotNull()
    public static final com.fachhochschulebib.fhb.pruefungsplaner.utils.CalendarIO INSTANCE = null;
    
    /**
     * URI that is used to access the android calendar. Is passed to the content resolver with extra information to specify the request.
     */
    private static final android.net.Uri EVENTS_URI = null;
    
    private CalendarIO() {
        super();
    }
    
    /**
     * Gets a list of all calendars,existing on the users smartphone.
     *
     * @param context The Applicationcontext
     * @return A list of [SmartphoneCalendar] that contain information about each calendar on the smartphone.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.fachhochschulebib.fhb.pruefungsplaner.utils.CalendarIO.SmartphoneCalendar> getCalendars(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    /**
     * Gets the primary calendar of the users smartphone.
     *
     * @param context The Applicationcontext
     *
     * @return The [SmartphoneCalendar] for the primary calendar.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @org.jetbrains.annotations.Nullable()
    public final com.fachhochschulebib.fhb.pruefungsplaner.utils.CalendarIO.SmartphoneCalendar getPrimaryCalendar(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    /**
     * Creates an intent for a Calendarevent.
     *
     * **See Also:**[CalendarContract-Documentation](https://developer.android.com/reference/android/provider/CalendarContract)
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @org.jetbrains.annotations.NotNull()
    public final android.content.Intent createIntent(@org.jetbrains.annotations.Nullable()
    java.lang.String title, @org.jetbrains.annotations.Nullable()
    java.lang.String description, @org.jetbrains.annotations.Nullable()
    java.util.Calendar startDate, @org.jetbrains.annotations.Nullable()
    java.util.Calendar endDate, @org.jetbrains.annotations.Nullable()
    java.lang.Boolean allDay) {
        return null;
    }
    
    /**
     * Creates an intent for a given [TestPlanEntry].
     *
     * @param[e] The [TestPlanEntry] to be transformed into an calendar intent.
     *
     * @return The intent to put into the Google Calendar
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @org.jetbrains.annotations.NotNull()
    public final android.content.Intent createIntent(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.Nullable()
    com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry e) {
        return null;
    }
    
    /**
     * Creates an event, that can be passed to the Google Calendar.
     *
     * @param[title] The title of the event, "Unnamed" by default.
     * @param[description] The description of the event, empty by default.
     * @param[startDate] The startdate of the event, including day and time, current time by default.
     * @param[endDate] The enddate of the event, including day and time, current time by default.
     * @param[allDay] Determines if the event is for the whole day,false by default.
     * @param[hasAlarm] Determines if the event has an alarm attatched, false by default.
     *
     * @return A set of values, that define an event and can be inserted to the GoogleCalendar.
     *
     * **See Also:**[CalendarContract-Documentation](https://developer.android.com/reference/android/provider/CalendarContract)
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @org.jetbrains.annotations.NotNull()
    public final android.content.ContentValues createEvent(long id, long CAL_ID, @org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.NotNull()
    java.lang.String description, @org.jetbrains.annotations.NotNull()
    java.util.Calendar startDate, @org.jetbrains.annotations.NotNull()
    java.util.Calendar endDate, boolean allDay, boolean hasAlarm) {
        return null;
    }
    
    /**
     * Creates an event for a given [TestPlanEntry].
     *
     * @param[e] The [TestPlanEntry] to be transformed into an calendar intent.
     *
     * @return The event to put into the Google Calendar
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @org.jetbrains.annotations.NotNull()
    public final android.content.ContentValues createEvent(@org.jetbrains.annotations.NotNull()
    android.content.Context context, long CAL_ID, long id, @org.jetbrains.annotations.Nullable()
    com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry e) {
        return null;
    }
    
    /**
     * Inserts an entry into the selected calender. If the synchronization in the settings
     * is turned of, this method has no effect,
     *
     * @param[context] The application context
     * @param[e] The [TestPlanEntry] that holds the necessary information
     * @param[force] If false, the User gets an dialog, in which he can view the entry,
     * before it is saved into the calendar. If true, it is passed directly, without
     * any user confirmation. The defaulvalue is false.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long insertEntry(@org.jetbrains.annotations.NotNull()
    android.content.Context context, long CAL_ID, @org.jetbrains.annotations.NotNull()
    com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry e, @org.jetbrains.annotations.NotNull()
    com.fachhochschulebib.fhb.pruefungsplaner.utils.CalendarIO.InsertionType insertionType) {
        return null;
    }
    
    /**
     * Inserts an event into the selected calendar without showing a dialog to the user.
     *
     * @param[context] The application context.
     * @param[event] The event to be inserted to the calendar.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private final java.lang.Long forceInsert(android.content.Context context, long CAL_ID, com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry testPlanEntry) {
        return null;
    }
    
    /**
     * Returns a random ID
     *
     * @return A Random Long number, based on the uuid algorithm
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private final long getRandomId() {
        return 0L;
    }
    
    /**
     * Inserts an event into the selected calendar, using the contentresolver.
     *
     * @param context The Applicationontext
     * @param event The event to be inserted
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private final void insertEvent(android.content.Context context, android.content.ContentValues event) {
    }
    
    /**
     * Updates an event in the selected calendar
     *
     * @param context The Applicationcontext
     * @param id The id of the event that shall be updated
     * @param event The event that shall replace the current one
     *
     * @param Alexander Lange
     * @since 1.6
     */
    private final void updateEvent(android.content.Context context, long id, android.content.ContentValues event) {
    }
    
    /**
     * Checks if a single event is currently existing in the selected calendar
     *
     * @param context The ApplicationContext
     * @param id The id of the event that is to look for
     *
     * @return true-> The event already exists;false->The event does not exists
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private final boolean eventExists(android.content.Context context, long id) {
        return false;
    }
    
    /**
     * Deletes all events from the selected calendar with a specific id
     *
     * @param context The Applicationcontext
     * @param id The id of the event/s to be deleted
     *
     * @return The number of events deleted
     *
     * @author ALexander Lange
     * @since 1.6
     */
    public final int deleteEvent(@org.jetbrains.annotations.NotNull()
    android.content.Context context, long id) {
        return 0;
    }
    
    /**
     * Deletes a list of events from the selected calendar
     *
     * @param context The Applicationcontext
     * @param ids A list of ids that shall be deleted
     *
     * @return The number of events deleted
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public final int deleteEvents(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.Long> ids) {
        return 0;
    }
    
    /**
     * Redirects the user to the Calendar to insert an event.
     * After he saved the event or canceled the process, the user is directed back
     * to where he was.
     *
     * @param[context] The application context.
     * @param[intent] An intent, containing the information to start the insertion process.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private final void indirectInsert(android.content.Context context, android.content.Intent intent) {
    }
    
    /**
     * Enum to differ between mutliple kind of insertion.<br/>
     * ```
     * Manuel->Always start the insertion process of the calendar, where the user can edit the entry.
     *
     * Automatic->Inserts the entry without any notification or redirection
     *
     * Ask->Always asks the user, if he wants to insert the entry Manuel or Automatic
     * ```
     * @author Alexander Lange
     * @since 1.6
     */
    @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0005\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005\u00a8\u0006\u0006"}, d2 = {"Lcom/fachhochschulebib/fhb/pruefungsplaner/utils/CalendarIO$InsertionType;", "", "(Ljava/lang/String;I)V", "Manuel", "Automatic", "Ask", "app_debug"})
    public static enum InsertionType {
        /*public static final*/ Manuel /* = new Manuel() */,
        /*public static final*/ Automatic /* = new Automatic() */,
        /*public static final*/ Ask /* = new Ask() */;
        
        InsertionType() {
        }
    }
    
    /**
     * Class that holds information about a single calendar.
     * @param id The Id of the calendar
     * @param name The name of the calendar
     * @param visible If the calendar is selected to be visible by the user
     * @param accessLevel The level of access (700) needed to write to the calendar
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\n\u0018\u00002\u00020\u0001B%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nR\u0011\u0010\b\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012\u00a8\u0006\u0013"}, d2 = {"Lcom/fachhochschulebib/fhb/pruefungsplaner/utils/CalendarIO$SmartphoneCalendar;", "", "id", "", "name", "", "visible", "", "accessLevel", "", "(JLjava/lang/String;ZI)V", "getAccessLevel", "()I", "getId", "()J", "getName", "()Ljava/lang/String;", "getVisible", "()Z", "app_debug"})
    public static final class SmartphoneCalendar {
        private final long id = 0L;
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String name = null;
        private final boolean visible = false;
        private final int accessLevel = 0;
        
        public SmartphoneCalendar(long id, @org.jetbrains.annotations.NotNull()
        java.lang.String name, boolean visible, int accessLevel) {
            super();
        }
        
        public final long getId() {
            return 0L;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getName() {
            return null;
        }
        
        public final boolean getVisible() {
            return false;
        }
        
        public final int getAccessLevel() {
            return 0;
        }
    }
}