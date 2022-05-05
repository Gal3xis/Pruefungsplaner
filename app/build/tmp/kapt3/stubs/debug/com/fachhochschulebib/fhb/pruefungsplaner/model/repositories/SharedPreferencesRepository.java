package com.fachhochschulebib.fhb.pruefungsplaner.model.repositories;

import java.lang.System;

/**
 * Repository to access the shared preferences.
 *
 * @param context The Applicationcontext
 *
 * @author Alexander Lange
 * @since 1.6
 */
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010$\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010%\n\u0002\b\'\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010J\u001a\u0010\u0011\u001a\u00020\f2\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00100\u0013J\u000e\u0010\u0014\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eJ\u0014\u0010\u0015\u001a\u00020\f2\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u000e0\u0016J\u0006\u0010\u0017\u001a\u00020\fJ\u0006\u0010\u0018\u001a\u00020\u0019J\b\u0010\u001a\u001a\u0004\u0018\u00010\u001bJ\u0006\u0010\u001c\u001a\u00020\u0019J\u0006\u0010\u001d\u001a\u00020\u0019J\u0006\u0010\u001e\u001a\u00020\u001fJ\b\u0010 \u001a\u0004\u0018\u00010\u0010J\u0012\u0010!\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00100\"J\b\u0010#\u001a\u0004\u0018\u00010\u0010J\u0006\u0010$\u001a\u00020\u0019J\b\u0010%\u001a\u0004\u0018\u00010\u0010J\b\u0010&\u001a\u0004\u0018\u00010\u0010J\b\u0010\'\u001a\u0004\u0018\u00010\u0010J\u0006\u0010(\u001a\u00020\u000eJ\b\u0010)\u001a\u0004\u0018\u00010\u0010J\b\u0010*\u001a\u0004\u0018\u00010\u0010J\u0006\u0010+\u001a\u00020\u001fJ\u0006\u0010,\u001a\u00020\u001fJ\u000e\u0010-\u001a\u00020\f2\u0006\u0010.\u001a\u00020\u0019J\u000e\u0010/\u001a\u00020\f2\u0006\u00100\u001a\u00020\u001bJ\u000e\u00101\u001a\u00020\f2\u0006\u00102\u001a\u00020\u0019J\u000e\u00103\u001a\u00020\f2\u0006\u00104\u001a\u00020\u0019J\u000e\u00105\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u001fJ\u000e\u00106\u001a\u00020\f2\u0006\u00107\u001a\u00020\u0010J\u000e\u00108\u001a\u00020\f2\u0006\u00109\u001a\u00020\u0010J\u000e\u0010:\u001a\u00020\f2\u0006\u0010.\u001a\u00020\u0019J\u000e\u0010;\u001a\u00020\f2\u0006\u0010<\u001a\u00020\u0010J\u000e\u0010=\u001a\u00020\f2\u0006\u0010>\u001a\u00020\u0010J\u000e\u0010?\u001a\u00020\f2\u0006\u0010@\u001a\u00020\u0010J\u000e\u0010A\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eJ\u000e\u0010B\u001a\u00020\f2\u0006\u0010C\u001a\u00020\u0010J\u000e\u0010D\u001a\u00020\f2\u0006\u00107\u001a\u00020\u0010J\u000e\u0010E\u001a\u00020\f2\u0006\u0010F\u001a\u00020\u001fJ\u000e\u0010G\u001a\u00020\f2\u0006\u0010H\u001a\u00020\u001fR\u0016\u0010\u0005\u001a\n \u0007*\u0004\u0018\u00010\u00060\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\b\u001a\n \u0007*\u0004\u0018\u00010\u00060\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\t\u001a\n \u0007*\u0004\u0018\u00010\u00060\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\n\u001a\n \u0007*\u0004\u0018\u00010\u00060\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006I"}, d2 = {"Lcom/fachhochschulebib/fhb/pruefungsplaner/model/repositories/SharedPreferencesRepository;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "calendarEntries", "Landroid/content/SharedPreferences;", "kotlin.jvm.PlatformType", "periodInformation", "settings", "userSelection", "addCalendarId", "", "id", "", "entryId", "", "addIds", "ids", "", "deleteId", "deleteIds", "", "deleteMainCourse", "getBackgroundUpdates", "", "getCalendarInsertionType", "Lcom/fachhochschulebib/fhb/pruefungsplaner/utils/CalendarIO$InsertionType;", "getCalendarSync", "getChosenDarkmode", "getChosenThemeId", "", "getEndDate", "getIds", "", "getMainCourse", "getNotificationSounds", "getPeriodTerm", "getPeriodTermin", "getPeriodYear", "getSelectedCalendar", "getSelectedFaculty", "getStartDate", "getUpdateIntervalTimeHour", "getUpdateIntervalTimeMinute", "setBackgroundUpdates", "status", "setCalendarInsertionType", "insertionType", "setCalendarSync", "sync", "setChosenDarkmode", "darkmode", "setChosenThemeId", "setEndDate", "date", "setMainCourse", "courseId", "setNotificationSounds", "setPeriodTerm", "period", "setPeriodTermin", "termin", "setPeriodYear", "year", "setSelectedCalendar", "setSelectedFaculty", "facultyId", "setStartDate", "setUpdateIntervalTimeHour", "hour", "setUpdateIntervalTimeMinute", "minute", "app_debug"})
public final class SharedPreferencesRepository {
    
    /**
     * File to store data about the current period
     */
    private final android.content.SharedPreferences periodInformation = null;
    
    /**
     * File to store data about the user app settings
     */
    private final android.content.SharedPreferences settings = null;
    
    /**
     * File to store the maincourse and the faculty, selected by the user.
     */
    private final android.content.SharedPreferences userSelection = null;
    
    /**
     * File to store the ids for each event in the calendar, mapped with the associated [com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry]
     */
    private final android.content.SharedPreferences calendarEntries = null;
    
    public SharedPreferencesRepository(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    /**
     * Adds an calendar event id into the shared preferences.
     *
     * @param id The event id of the calendar event.
     * @param entryId The [com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry.id] of the [com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry].
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public final void addCalendarId(long id, @org.jetbrains.annotations.NotNull()
    java.lang.String entryId) {
    }
    
    /**
     * Adds multiple calendar event ids into the shared preferences.
     *
     * @param ids A prepared Map with calendar event ids and their [com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry]
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public final void addIds(@org.jetbrains.annotations.NotNull()
    java.util.Map<java.lang.Long, java.lang.String> ids) {
    }
    
    /**
     * Returns all calendar event ids and their [com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry] from the shared preferences.
     *
     * @return A Map with all calendar event ids and their [com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry].
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<java.lang.Long, java.lang.String> getIds() {
        return null;
    }
    
    /**
     * Deletes one calendar event id from the shared preferences.
     *
     * @param id The Id of the event to be deleted
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public final void deleteId(long id) {
    }
    
    /**
     * Deletes multiple calendar event ids from the shared preferences.
     *
     * @param ids A list of all event ids to be deleted.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public final void deleteIds(@org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.Long> ids) {
    }
    
    /**
     * Inserts id of the calendar, the user selected for syncing into the shared preferences.
     *
     * @param id The Calendar id the user selected.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public final void setSelectedCalendar(long id) {
    }
    
    /**
     * Gets the id of the calendar, the user selected for syncing from the shared preferences.
     *
     * @return The Calendar id the user selected.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public final long getSelectedCalendar() {
        return 0L;
    }
    
    /**
     * Gets the course, selected as the maincourse
     *
     * @return The id of the course, selected as the maincourse
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getMainCourse() {
        return null;
    }
    
    /**
     * Sets the course, selected as the maincourse
     *
     * @param courseId The id of the course, selected as the maincourse
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public final void setMainCourse(@org.jetbrains.annotations.NotNull()
    java.lang.String courseId) {
    }
    
    /**
     * Deletes the main course from shared preferences
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public final void deleteMainCourse() {
    }
    
    /**
     * Gets the faculty, selected by the user
     *
     * @return The id of the faculty, the user selected
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getSelectedFaculty() {
        return null;
    }
    
    /**
     * Sets the faculty, selected by the user
     *
     * @param facultyId The id of the faculty, the user selected
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public final void setSelectedFaculty(@org.jetbrains.annotations.NotNull()
    java.lang.String facultyId) {
    }
    
    /**
     * Gets the year of the current period
     *
     * @return the year of the current period as string
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getPeriodYear() {
        return null;
    }
    
    /**
     * Sets the year of the current period
     *
     * @param year The year of the current period as string (Like '2022')
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public final void setPeriodYear(@org.jetbrains.annotations.NotNull()
    java.lang.String year) {
    }
    
    /**
     * Gets the term of the current period (SoSe or WiSe)
     *
     * @return The term of the current period (SoSe or WiSe)
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getPeriodTerm() {
        return null;
    }
    
    /**
     * Sets the term of the current period (SoSe or WiSe)
     *
     * @param period The term of the current period (SoSe or WiSe)
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public final void setPeriodTerm(@org.jetbrains.annotations.NotNull()
    java.lang.String period) {
    }
    
    /**
     * TODO Rename
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getPeriodTermin() {
        return null;
    }
    
    public final void setPeriodTermin(@org.jetbrains.annotations.NotNull()
    java.lang.String termin) {
    }
    
    /**
     * Gets the start date of the period
     *
     * @return The start date of the period as a string
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getStartDate() {
        return null;
    }
    
    /**
     * Sets the start date of the period
     *
     * @param date The start date of the period as a string
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public final void setStartDate(@org.jetbrains.annotations.NotNull()
    java.lang.String date) {
    }
    
    /**
     * Gets the end date of the period
     *
     * @return The end date of the period as a string
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getEndDate() {
        return null;
    }
    
    /**
     * Sets the end date of the period
     *
     * @param date The end date of the period as a string
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public final void setEndDate(@org.jetbrains.annotations.NotNull()
    java.lang.String date) {
    }
    
    /**
     * Gets the setting for the darkmode
     *
     * @return true-> darkmode is set; false-> darkmode is not set. Returns false by default
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public final boolean getChosenDarkmode() {
        return false;
    }
    
    /**
     * Sets the setting for the darkmode
     *
     * @param darkmode true-> darkmode is set; false-> darkmode is not set
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public final void setChosenDarkmode(boolean darkmode) {
    }
    
    /**
     * Gets the id of the selected theme
     *
     * @return The id of the selected theme. Returns the id of the first theme in the list (green) by default
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public final int getChosenThemeId() {
        return 0;
    }
    
    /**
     * Sets the id of the selected theme
     *
     * @param id The id of the selected theme
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public final void setChosenThemeId(int id) {
    }
    
    /**
     * Gets the hour component of the update interval for the background worker
     *
     * @return The hour component of the update interval for the background worker. Return 0 by default
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public final int getUpdateIntervalTimeHour() {
        return 0;
    }
    
    /**
     * Sets the hour component of the update interval for the background worker
     *
     * @param hour The hour component of the update interval for the background worker
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public final void setUpdateIntervalTimeHour(int hour) {
    }
    
    /**
     * Gets the minute component of the update interval for the background worker
     *
     * @return The minute component of the update interval for the background worker. Returns 15 by default, because 0:15 is the minimum interval for the background worker
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public final int getUpdateIntervalTimeMinute() {
        return 0;
    }
    
    /**
     * Sets the minute component of the update interval for the background worker
     *
     * @param minute The hour component of the update interval for the background worker
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public final void setUpdateIntervalTimeMinute(int minute) {
    }
    
    /**
     * Gets the setting for background updates
     *
     * @return true->The app will update in the background; false->The app wont update in the background. Returns false by default
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public final boolean getBackgroundUpdates() {
        return false;
    }
    
    /**
     * Sets the setting for background updates
     *
     * @param status true->The app will update in the background; false->The app wont update in the background
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public final void setBackgroundUpdates(boolean status) {
    }
    
    /**
     * Gets the setting for notification sounds for the background worker
     *
     * @return true->The app will make a sound for each notification;false->the app will make no sound for a notification. Returns false by default
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public final boolean getNotificationSounds() {
        return false;
    }
    
    /**
     * Sets the setting for notification sounds for the background worker
     *
     * @param status true->The app will make a sound for each notification;false->the app will make no sound for a notification
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public final void setNotificationSounds(boolean status) {
    }
    
    /**
     * Gets the setting for calendar synchronization
     *
     * @return true->The calendar will synchronize with the selected exams;false->The calendar wont sync with the selected courses. Returns false by default
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public final boolean getCalendarSync() {
        return false;
    }
    
    /**
     * Sets the setting for calendar synchronization
     *
     * @param sync true->The calendar will synchronize with the selected exams;false->The calendar wont sync with the selected courses
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public final void setCalendarSync(boolean sync) {
    }
    
    /**
     * Gets the setting for the insertion type for each calendar entry
     *
     * @return The insertion type for each new calendar entry.
     * Automatic -> The entry will be placed in the calendar without notifying the user;
     * Manuel->The insertion intent of the calendar will be started, where the user can modify the entry himself;
     * Ask->The user will be asked each time, if an entry should be manuel ro automatic. Returns automatic by default.
     */
    @org.jetbrains.annotations.Nullable()
    public final com.fachhochschulebib.fhb.pruefungsplaner.utils.CalendarIO.InsertionType getCalendarInsertionType() {
        return null;
    }
    
    /**
     * Sets the setting for the insertion type for each calendar entry
     *
     * @param insertionType The insertion type for each new calendar entry.
     * Automatic -> The entry will be placed in the calendar without notifying the user;
     * Manuel->The insertion intent of the calendar will be started, where the user can modify the entry himself;
     * Ask->The user will be asked each time, if an entry should be manuel ro automatic
     */
    public final void setCalendarInsertionType(@org.jetbrains.annotations.NotNull()
    com.fachhochschulebib.fhb.pruefungsplaner.utils.CalendarIO.InsertionType insertionType) {
    }
}