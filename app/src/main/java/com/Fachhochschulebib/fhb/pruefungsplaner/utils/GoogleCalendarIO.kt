package com.Fachhochschulebib.fhb.pruefungsplaner.utils

import android.app.AlertDialog
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

    enum class InsertionTye {
        Manuel, Automatic, Ask
    }

    private val EVENTS_URI = Uri.parse("content://com.android.calendar/events")
    private const val CAL_ID = 2

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
    fun insertEntry(
        context: Context,
        e: TestPlanEntry,
        insertionTye: InsertionTye = InsertionTye.Ask
    ) {
        when (insertionTye) {
            InsertionTye.Automatic -> {
                findEventSingle(context, e)?.let { deleteEntry(context, it) }
                forceInsert(context, createEvent(context, e))
            }
            InsertionTye.Manuel -> indirectInsert(context, createIntent(context, e))
            InsertionTye.Ask -> {
                AlertDialog.Builder(context)
                    .setTitle("Termin Eintragen")
                    .setPositiveButton("Automatisch") { _, _ ->
                        findEventSingle(context, e)?.let { deleteEntry(context, it) }
                        forceInsert(context, createEvent(context, e))
                    }
                    .setNegativeButton("Manuell") { _, _ ->
                        indirectInsert(context, createIntent(context, e))
                    }
                    .create().show()
            }
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
    fun insertEntries(context: Context, list: List<TestPlanEntry?>, insertionTye: InsertionTye) {
        list.forEach {
            if (it != null) {
                insertEntry(context, it, insertionTye)
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
     * Deletes multiple events from the google calendar, based on [TestPlanEntry]-Objects.
     * This method has no effect when the synchronization is turned of in the settings.
     *
     * @param[context] The application context.
     * @param[list] A list of [TestPlanEntry]-Objects that shall be removed from the calendar.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun deleteEntries(context: Context, list: List<TestPlanEntry>) {
        findEventIds(context, list).forEach { deleteEntry(context, it) }
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
        val numRows = context.contentResolver?.delete(
            ContentUris.withAppendedId(EVENTS_URI, entryId),
            null,
            null
        )
        Log.d("GoogleCalendarIO", "Deleted $numRows events")
    }

    fun deleteEntry(context: Context, entry: TestPlanEntry) {
        findEventSingle(context, entry)?.let { deleteEntry(context, it) }
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
    fun deleteAll(context: Context, entries: List<TestPlanEntry>) {
        findEventIds(context, entries).forEach {
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
    fun findEventIds(context: Context, entries: List<TestPlanEntry>): List<Long> {
        val moduleNames = mutableListOf<String>()
        entries.forEach { entry -> entry.module?.let { moduleName -> moduleNames.add(moduleName) } }
        val ret: MutableList<Long> = mutableListOf()
        val cursor = context.contentResolver?.query(
            EVENTS_URI, arrayOf("_id", "title"),
            "calendar_id=$CAL_ID", null, null
        )
        while (cursor?.moveToNext() == true) {
            val index = cursor.getColumnIndex("_id")
            val indexTitle = cursor.getColumnIndex("title")
            val module = cursor.getString(indexTitle)
            if (!moduleNames.contains(module)) continue
            if (index >= 0) {
                ret.add(cursor.getLong(index))
            }
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
            val indexId = cursor.getColumnIndex("_id")
            val indexTitle = cursor.getColumnIndex("title")
            val indexDescription = cursor.getColumnIndex("description")
            if (indexId >= 0 && indexTitle >= 0 && indexDescription >= 0) {
                val id = cursor.getLong(indexId)
                val module = cursor.getString(indexTitle)
                val description = cursor.getString(indexDescription)
                if (module == entry.module && description == entry.getString(context)) {
                    Log.d("EventTest", "$id;$module")
                    cursor.close()
                    return id
                }
            }
        }
        cursor?.close()
        return null
    }

    fun findEventModuleNames(context: Context, entries: List<TestPlanEntry>): List<String> {
        val moduleNames = mutableListOf<String>()
        entries.forEach { entry -> entry.module?.let { moduleName -> moduleNames.add(moduleName) } }
        val ret: MutableList<String> = mutableListOf()
        val cursor = context.contentResolver?.query(
            EVENTS_URI, arrayOf("_id", "title"),
            "calendar_id=$CAL_ID", null, null
        )
        while (cursor?.moveToNext() == true) {
            val index = cursor.getColumnIndex("_id")
            val indexTitle = cursor.getColumnIndex("title")
            val module = cursor.getString(indexTitle)
            if (!moduleNames.contains(module)) continue
            if (index >= 0) {
                ret.add(cursor.getString(indexTitle))
            }
        }
        cursor?.close()
        return ret
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
    fun update(context: Context, entries: List<TestPlanEntry>,insertionTye: InsertionTye) {
        deleteAll(context, entries)
        insertEntries(context, entries, insertionTye)
    }
}