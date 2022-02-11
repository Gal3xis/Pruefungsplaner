package com.Fachhochschulebib.fhb.pruefungsplaner.controller

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.CalendarContract
import android.util.Log
import com.Fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry
import java.text.SimpleDateFormat
import java.util.*

/**
 * IO-Interface to access the GoogleCalendar. Provides Methods to insert and delete
 * entries and also works with [TestPlanEntry]-Objects.
 *
 * @author Alexander Lange
 * @since 1.6
 *
 * **See Also:**[Android Calendar Intent-Tutorial](https://itnext.io/android-calendar-intent-8536232ecb38)

 */
object GoogleCalendarIO {

    private val EVENTS_URI = Uri.parse("content://com.android.calendar/events")
    private val CAL_ID = 2

    /**
     * Creates an intent for a Calendarevent.
     *
     * **See Also:**[CalendarContract-Documentation](https://developer.android.com/reference/android/provider/CalendarContract)
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun createIntent(
        title: String? = "Unnamed",
        description: String? = "",
        startDate: Calendar? = Calendar.getInstance(),
        endDate: Calendar? = null,
        allDay: Boolean? = false
    ): Intent {
        val intent = Intent(Intent.ACTION_INSERT)
        intent.data = CalendarContract.Events.CONTENT_URI
        intent.putExtra(CalendarContract.Events.TITLE, title ?: "")
        intent.putExtra(CalendarContract.Events.DESCRIPTION, description ?: "")
        intent.putExtra(
            CalendarContract.EXTRA_EVENT_BEGIN_TIME,
            startDate?.timeInMillis ?: Calendar.getInstance().timeInMillis
        )
        intent.putExtra(
            CalendarContract.EXTRA_EVENT_END_TIME,
            endDate?.timeInMillis ?: startDate?.timeInMillis ?: Calendar.getInstance().timeInMillis
        )
        intent.putExtra(CalendarContract.Events.ALL_DAY, allDay)
        return intent
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
    fun createIntent(context: Context, e: TestPlanEntry?): Intent {
        val cs = Calendar.getInstance()
        e?.let { cs.time = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(e.date) }
        val ce = Calendar.getInstance()
        e?.let {
            ce.time = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(e.date)
            ce.add(GregorianCalendar.MINUTE, Utils.getExamDuration(e.examForm) ?: 0)
        }
        return createIntent(
            title = e?.module ?: "",
            description = e?.getString(context) ?: "",
            startDate = cs,
            endDate = ce
        )
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
    fun createEvent(
        title: String = "Unnamed",
        description: String = "",
        startDate: Calendar = Calendar.getInstance(),
        endDate: Calendar = Calendar.getInstance(),
        allDay: Boolean = false,
        hasAlarm: Boolean = false
    ): ContentValues {
        val event = ContentValues()
        event.put(CalendarContract.Events.CALENDAR_ID, CAL_ID)
        event.put(CalendarContract.Events.TITLE, title)
        event.put(CalendarContract.Events.DESCRIPTION, description)
        event.put(CalendarContract.Events.DTSTART, startDate.timeInMillis)
        event.put(CalendarContract.Events.DTEND, endDate.timeInMillis)
        event.put(CalendarContract.Events.ALL_DAY, if (allDay) 1 else 0) // 0 for false, 1 for true
        event.put(
            CalendarContract.Events.HAS_ALARM,
            if (hasAlarm) 1 else 0
        ) // 0 for false, 1 for true
        event.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().id)
        return event
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
    fun createEvent(context: Context, e: TestPlanEntry?): ContentValues {
        val cs = Calendar.getInstance()
        e?.let { cs.time = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(e.date) }
        val ce = Calendar.getInstance()
        e?.let {
            ce.time = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(e.date)
            ce.add(GregorianCalendar.MINUTE, Utils.getExamDuration(e.examForm) ?: 0)
        }
        return createEvent(
            title = e?.module ?: "",
            description = e?.getString(context) ?: "",
            startDate = cs,
            endDate = ce
        )
    }

    /**
     * Inserts an entry into the googlecalender. If the synchronization in the settings
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
    fun insertEntry(context: Context, e: TestPlanEntry, force: Boolean = false) {
        if (!context.getSharedPreferences("settings", Context.MODE_PRIVATE)
                .getBoolean("calSync", false)
        ) {
            return
        }
        if (force) {
            if (findEventSingle(context, e) != null) {
                deleteEntry(context, e)
            }
            forceInsert(context, createEvent(context, e))
        } else {
            indirectInsert(context, createIntent(context, e))
        }
    }

    /**
     * Inserts a list of entries into the google calender. If the synchronization in the settings
     * is turned of, this method has no effect,
     *
     * @param[context] The application context
     * @param[list] The list of [TestPlanEntry] to be inserted.
     * @param[force] If false, the User gets an dialog, in which he can view the entry,
     * before it is saved into the calendar. If true, it is passed directly, without
     * any user confirmation. The defaulvalue is false.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun insertEntries(context: Context, list: List<TestPlanEntry?>, force: Boolean) {
        list.forEach {
            if (it != null) {
                insertEntry(context, it, force)
            }
        }
    }

    /**
     * Inserts an event into the google calendar without showing a dialog to the user.
     *
     * @param[context] The application context.
     * @param[event] The event to be inserted to the calendar.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private fun forceInsert(context: Context, event: ContentValues) {
        context.contentResolver?.insert(EVENTS_URI, event)
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
    private fun indirectInsert(context: Context, intent: Intent) {
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        }
    }

    /**
     * Deletes an event from the google calendar from a given [TestPlanEntry].
     * This method has no effect when the synchronization is turned of in the settings.
     *
     * @param[context] The application context.
     * @param[e] The [TestPlanEntry] to be removed from the calendar.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun deleteEntry(context: Context, e: TestPlanEntry) {
        findEventSingle(context, e)?.let { deleteEntry(context, it) }
    }

    /**
     * Deletes multiple events from the google calendar, based on [TestPlanEntry]-Objects.
     * This method has no effect when the synchronization is turned of in the settings.
     *
     * @param[context] The application context.
     * @param[list] A list of [TestPlanEntry]-Objects that shall be removed from the calendar.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun deleteEntries(context: Context, list: List<TestPlanEntry?>) {
        list.forEach {
            if (it != null) {
                deleteEntry(context, it)
            }
        }
    }

    /**
     * Deletes an event from the google calendar, based on its id.
     * This method has no effect when the synchronization is turned of in the settings.
     *
     * @param[context] The application context.
     * @param[entryId] The id of the event to be removed.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun deleteEntry(context: Context, entryId: Long) {
        if (!context.getSharedPreferences("settings", Context.MODE_PRIVATE)
                .getBoolean("calSync", false)
        ) {
            return
        }
        val numRows = context.contentResolver?.delete(
            ContentUris.withAppendedId(EVENTS_URI, entryId),
            null,
            null
        )
        Log.d("GoogleCalendarIO", "Deleted $numRows events")
    }

    /**
     * Deletes every event from the google calendar, connected with this application.
     * This method has no effect when the synchronization is turned of in the settings.
     *
     * @param[context] The application context.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun deleteAll(context: Context) {
        findEventIds(context).forEach {
            deleteEntry(context, it)
        }
    }

    /**
     * Returns a list of every event-id, connected to this application.
     *
     * @param[context] The application context.
     *
     * @return The list of ids
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun findEventIds(context: Context): List<Long> {
        val ret: MutableList<Long> = mutableListOf()
        val cursor = context.contentResolver?.query(
            EVENTS_URI, arrayOf("_id"),
            "calendar_id=$CAL_ID", null, null
        )
        while (cursor?.moveToNext() == true) {
            ret.add(cursor.getLong(cursor.getColumnIndex("_id")))
            Log.d("EventTestCalId", cursor.getString(cursor.getColumnIndex("_id")))
        }
        cursor?.close()
        return ret
    }

    /**
     * Returns the id of an event that is connected with a [TestPlanEntry].
     * Can return null if no event was found, but cannot find more than one event.
     *
     * @param[context] The application context.
     * @param[entry] The [TestPlanEntry], connected to the event.
     *
     * @return The id of the event for the [TestPlanEntry].
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun findEventSingle(context: Context, entry: TestPlanEntry): Long? {
        val cursor = context.contentResolver?.query(
            EVENTS_URI, arrayOf("_id", "title", "description"),
            "calendar_id=$CAL_ID", null, null
        )
        while (cursor?.moveToNext() == true) {
            val id = cursor.getLong(cursor.getColumnIndex("_id"))
            val module = cursor.getString(cursor.getColumnIndex("title"))
            val description = cursor.getString(cursor.getColumnIndex("description"))
            if (module == entry.module && description == entry.getString(context)) {
                Log.d("EventTest", "$id;$module")
                cursor.close()
                return id
            }
        }
        cursor?.close()
        return null
    }

    /**
     * Updates the GoogleCalendar with a new set of events. Deletes everything for the app and reinsert the events.
     *
     * @param[context] The application context.
     * @param[entries] The up-to-date list of entries to put into the calendar.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun update(context: Context,entries:List<TestPlanEntry?>){
        deleteAll(context)
        insertEntries(context,entries,true)
    }
}