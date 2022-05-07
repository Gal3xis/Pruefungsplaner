package com.fachhochschulebib.fhb.pruefungsplaner.model.repositories

import android.content.Context
import com.fachhochschulebib.fhb.pruefungsplaner.utils.CalendarIO
import com.fachhochschulebib.fhb.pruefungsplaner.utils.Utils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Repository to access the shared preferences.
 *
 * @param context The Applicationcontext
 *
 * @author Alexander Lange
 * @since 1.6
 */
class SharedPreferencesRepository(context: Context) {

    /**
     * File to store data about the current period
     */
    private val periodInformation = context.getSharedPreferences("periodInformation", Context.MODE_PRIVATE)

    /**
     * File to store data about the user app settings
     */
    private val settings = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    /**
     * File to store the maincourse and the faculty, selected by the user.
     */
    private val userSelection = context.getSharedPreferences("userSelection",Context.MODE_PRIVATE)

    /**
     * File to store the ids for each event in the calendar, mapped with the associated [com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry]
     */
    private val calendarEntries = context.getSharedPreferences("calendarEntries",Context.MODE_PRIVATE)

    //Google Calendar
    /**
     * Adds an calendar event id into the shared preferences.
     *
     * @param id The event id of the calendar event.
     * @param entryId The [com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry.id] of the [com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry].
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun addCalendarId(id:Long, entryId: String){
        val existing = getIds()
        //Shortened "put(id,entryId)"
        existing[id] = entryId
        val json = Gson().toJson(existing)
        val editor = calendarEntries.edit()
        editor.putString("ids",json)
        editor.apply()
    }

    /**
     * Adds multiple calendar event ids into the shared preferences.
     *
     * @param ids A prepared Map with calendar event ids and their [com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry]
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun addIds(ids:Map<Long,String>){
        val existing = getIds()
        existing.putAll(ids)
        val json = Gson().toJson(existing)
        val editor = calendarEntries.edit()
        editor.putString("ids",json)
        editor.apply()
    }

    /**
     * Returns all calendar event ids and their [com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry] from the shared preferences.
     *
     * @return A Map with all calendar event ids and their [com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry].
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun getIds():MutableMap<Long,String>{
        val json = calendarEntries.getString("ids",null)
        return if(json==null) mutableMapOf() else Gson().fromJson(json, object :TypeToken<MutableMap<Long,String>>(){}.type)
    }

    /**
     * Deletes one calendar event id from the shared preferences.
     *
     * @param id The Id of the event to be deleted
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun deleteId(id:Long){
        val existing = getIds()
        existing.remove(id)
        val json = Gson().toJson(existing)
        val editor = calendarEntries.edit()
        editor.putString("ids",json)
        editor.apply()
    }

    /**
     * Deletes multiple calendar event ids from the shared preferences.
     *
     * @param ids A list of all event ids to be deleted.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun deleteIds(ids:List<Long>){
        val existing = getIds()
        ids.forEach {
            existing.remove(it)
        }
        val json = Gson().toJson(existing)
        val editor = calendarEntries.edit()
        editor.putString("ids",json)
        editor.apply()
    }

    /**
     * Inserts id of the calendar, the user selected for syncing into the shared preferences.
     *
     * @param id The Calendar id the user selected.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun setSelectedCalendar(id:Long){
        val editor = settings.edit()
        editor.putLong("selectedCalendar",id)
        editor.apply()
    }

    /**
     * Gets the id of the calendar, the user selected for syncing from the shared preferences.
     *
     * @return The Calendar id the user selected.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun getSelectedCalendar():Long{
        return settings.getLong("selectedCalendar",-1)
    }

//Period information
    /**
     * Gets the course, selected as the maincourse
     *
     * @return The id of the course, selected as the maincourse
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun getMainCourse():String?{
        return userSelection.getString("mainCourseId",null)
    }

    /**
     * Sets the course, selected as the maincourse
     *
     * @param courseId The id of the course, selected as the maincourse
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun setMainCourse(courseId: String){
        val editor = userSelection.edit()
        editor.putString("mainCourseId",courseId)
        editor.apply()
    }

    /**
     * Deletes the main course from shared preferences
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun deleteMainCourse() {
        val editor = userSelection.edit()
        editor.remove("mainCourseId")
        editor.apply()
    }

    /**
     * Gets the faculty, selected by the user
     *
     * @return The id of the faculty, the user selected
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun getSelectedFaculty():String?{
        return userSelection.getString("selectedFacultyId",null)
    }

    /**
     * Sets the faculty, selected by the user
     *
     * @param facultyId The id of the faculty, the user selected
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun setSelectedFaculty(facultyId:String){
        val editor = userSelection.edit()
        editor.putString("selectedFacultyId",facultyId)
        editor.apply()
    }

    /**
     * Gets the year of the current period
     *
     * @return the year of the current period as string
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun getPeriodYear():String?{
        return periodInformation.getString("periodYear",null)
    }

    /**
     * Sets the year of the current period
     *
     * @param year The year of the current period as string (Like '2022')
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun setPeriodYear(year:String){
        val editor = periodInformation.edit()
        editor.putString("periodYear",year)
        editor.apply()
    }

    /**
     * Gets the term of the current period (SoSe or WiSe)
     *
     * @return The term of the current period (SoSe or WiSe)
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun getPeriodTerm():String?{
        return periodInformation.getString("periodTerm",null)
    }

    /**
     * Sets the term of the current period (SoSe or WiSe)
     *
     * @param period The term of the current period (SoSe or WiSe)
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun setPeriodTerm(period:String){
        val editor = periodInformation.edit()
        editor.putString("periodTerm",period)
        editor.apply()
    }

    /**
     * TODO Rename
     */
    fun getPeriodTermin():String?{
        return periodInformation.getString("periodTermin",null)
    }

    fun setPeriodTermin(termin:String){
        val editor = periodInformation.edit()
        editor.putString("periodTermin",termin)
        editor.apply()
    }

    /**
     * Gets the start date of the period
     *
     * @return The start date of the period as a string
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun getStartDate():String?{
        return periodInformation.getString("startDate",null)
    }

    /**
     * Sets the start date of the period
     *
     * @param date The start date of the period as a string
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun setStartDate(date:String){
        val editor = periodInformation.edit()
        editor.putString("startDate",date)
        editor.apply()
    }

    /**
     * Gets the end date of the period
     *
     * @return The end date of the period as a string
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun getEndDate():String?{
        return periodInformation.getString("endDate",null)
    }

    /**
     * Sets the end date of the period
     *
     * @param date The end date of the period as a string
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun setEndDate(date: String){
        val editor = periodInformation.edit()
        editor.putString("endDate",date)
        editor.apply()
    }

//Settings
    /**
     * Gets the setting for the darkmode
     *
     * @return true-> darkmode is set; false-> darkmode is not set. Returns false by default
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun getChosenDarkmode():Boolean{
        return settings.getBoolean("darkmode",false)
    }

    /**
     * Sets the setting for the darkmode
     *
     * @param darkmode true-> darkmode is set; false-> darkmode is not set
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun setChosenDarkmode(darkmode:Boolean){
        val editor = settings.edit()
        editor.putBoolean("darkmode",darkmode)
        editor.apply()
    }

    /**
     * Gets the id of the selected theme
     *
     * @return The id of the selected theme. Returns the id of the first theme in the list (green) by default
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun getChosenThemeId():Int{
        return settings.getInt("themeid", Utils.themeList[0])
    }

    /**
     * Sets the id of the selected theme
     *
     * @param id The id of the selected theme
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun setChosenThemeId(id:Int){
        val editor = settings.edit()
        editor.putInt("themeid",id)
        editor.apply()
    }

    /**
     * Gets the hour component of the update interval for the background worker
     *
     * @return The hour component of the update interval for the background worker. Return 0 by default
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun getUpdateIntervalTimeHour():Int{
        return settings.getInt("updateIntervalTimeHour",0)
    }

    /**
     * Sets the hour component of the update interval for the background worker
     *
     * @param hour The hour component of the update interval for the background worker
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun setUpdateIntervalTimeHour(hour:Int){
        val editor = settings.edit()
        editor.putInt("updateIntervalTimeHour",hour)
        editor.apply()
    }

    /**
     * Gets the minute component of the update interval for the background worker
     *
     * @return The minute component of the update interval for the background worker. Returns 15 by default, because 0:15 is the minimum interval for the background worker
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun getUpdateIntervalTimeMinute():Int{
        return settings.getInt("updateIntervalTimeMinute",15)
    }

    /**
     * Sets the minute component of the update interval for the background worker
     *
     * @param minute The hour component of the update interval for the background worker
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun setUpdateIntervalTimeMinute(minute:Int){
        val editor = settings.edit()
        editor.putInt("updateIntervalTimeMinute",minute)
        editor.apply()
    }

    /**
     * Gets the setting for background updates
     *
     * @return true->The app will update in the background; false->The app wont update in the background. Returns false by default
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun getBackgroundUpdates():Boolean{
        return settings.getBoolean("backgroundUpdates",false)
    }

    /**
     * Sets the setting for background updates
     *
     * @param status true->The app will update in the background; false->The app wont update in the background
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun setBackgroundUpdates(status:Boolean){
        val editor = settings.edit()
        editor.putBoolean("backgroundUpdates",status)
        editor.apply()
    }

    /**
     * Gets the setting for notification sounds for the background worker
     *
     * @return true->The app will make a sound for each notification;false->the app will make no sound for a notification. Returns false by default
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun getNotificationSounds():Boolean{
        return settings.getBoolean("notificationSounds",false)
    }

    /**
     * Sets the setting for notification sounds for the background worker
     *
     * @param status true->The app will make a sound for each notification;false->the app will make no sound for a notification
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun setNotificationSounds(status:Boolean){
        val editor = settings.edit()
        editor.putBoolean("notificationSounds",status)
        editor.apply()
    }

    /**
     * Gets the setting for calendar synchronization
     *
     * @return true->The calendar will synchronize with the selected exams;false->The calendar wont sync with the selected courses. Returns false by default
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun getCalendarSync():Boolean{
        return settings.getBoolean("calSync",false)
    }

    /**
     * Sets the setting for calendar synchronization
     *
     * @param sync true->The calendar will synchronize with the selected exams;false->The calendar wont sync with the selected courses
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun setCalendarSync(sync:Boolean){
        val editor = settings.edit()
        editor.putBoolean("calSync",sync)
        editor.apply()
    }

    /**
     * Gets the setting for the insertion type for each calendar entry
     *
     * @return The insertion type for each new calendar entry.
     * Automatic -> The entry will be placed in the calendar without notifying the user;
     * Manuel->The insertion intent of the calendar will be started, where the user can modify the entry himself;
     * Ask->The user will be asked each time, if an entry should be manuel ro automatic. Returns automatic by default.
     */
    fun getCalendarInsertionType() :CalendarIO.InsertionType?{
        return settings.getString("InsertionType",CalendarIO.InsertionType.Automatic.name)
            ?.let { CalendarIO.InsertionType.valueOf(it) }
    }

    /**
     * Sets the setting for the insertion type for each calendar entry
     *
     * @param insertionType The insertion type for each new calendar entry.
     * Automatic -> The entry will be placed in the calendar without notifying the user;
     * Manuel->The insertion intent of the calendar will be started, where the user can modify the entry himself;
     * Ask->The user will be asked each time, if an entry should be manuel ro automatic
     */
    fun setCalendarInsertionType(insertionType: CalendarIO.InsertionType) {
        val editor = settings.edit()
        editor.putString("InsertionType",insertionType.name)
        editor.apply()
    }
}