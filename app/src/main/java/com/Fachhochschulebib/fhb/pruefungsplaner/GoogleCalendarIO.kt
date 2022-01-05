package com.Fachhochschulebib.fhb.pruefungsplaner

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.CalendarContract
import android.util.Log
import com.Fachhochschulebib.fhb.pruefungsplaner.data.AppDatabase
import com.Fachhochschulebib.fhb.pruefungsplaner.data.TestPlanEntry
import java.text.SimpleDateFormat
import java.util.*

/**
 * IO-Interface to access the GoogleCalendar. Provides Methods to insert and delete
 * entries and also works with [TestPlanEntry]-Objects.
 *
 * @author Alexander Lange
 * @since 1.5
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
     * @since 1.5
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
     * @since 1.5
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
     *
     *
     * **See Also:**[CalendarContract-Documentation](https://developer.android.com/reference/android/provider/CalendarContract)
     *
     * @author Alexander Lange
     * @since 1.5
     */
    fun createEvent(
        entryId: String = "-1",
        title: String = "Unnamed",
        description: String = "",
        startDate: Calendar = Calendar.getInstance(),
        endDate: Calendar = Calendar.getInstance(),
        allDay: Boolean = false,
        hasAlarm:Boolean = false
    ): ContentValues {

        val event = ContentValues()
        event.put(CalendarContract.Events.CALENDAR_ID, CAL_ID)
        event.put(CalendarContract.Events.TITLE, title)
        event.put(CalendarContract.Events.DESCRIPTION, description)
        event.put(CalendarContract.Events.DTSTART, startDate.timeInMillis)
        event.put(CalendarContract.Events.DTEND, endDate.timeInMillis)
        event.put(CalendarContract.Events.ALL_DAY, if(allDay)1 else 0) // 0 for false, 1 for true
        event.put(CalendarContract.Events.HAS_ALARM, if(hasAlarm)1 else 0) // 0 for false, 1 for true
        event.put(CalendarContract.Events.EVENT_TIMEZONE,TimeZone.getDefault().id)
        return event
    }

    /**
     * Creates an intent for a given [TestPlanEntry].
     *
     * @param[e] The [TestPlanEntry] to be transformed into an calendar intent.
     *
     * @return The intent to put into the Google Calendar
     *
     * @author Alexander Lange
     * @since 1.5
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
     * Inserts an entry into the googlecalender.
     *
     * @param[context] The application context
     * @param[e] The [TestPlanEntry] that holds the necessary information
     * @param[force] If false, the User gets an dialog, in which he can view the entry,
     * before it is saved into the calendar. If true, it is passed directly, without
     * any user confirmation. The defaulvalue is false.
     *
     * @author Alexander Lange
     * @since 1.5
     */
    fun insertEntry(context: Context, e: TestPlanEntry, force: Boolean = false) {
        if (force) {
            if(findEventSingle(context,e)!=null){
                deleteEntry(context,e)
            }
            forceInsert(context, createEvent(context,e))
        } else {
            indirectInsert(context, createIntent(context, e))
        }
    }

    private fun forceInsert(context: Context,event:ContentValues){
        context.contentResolver?.insert(EVENTS_URI, event)
    }

    private fun indirectInsert(context: Context, intent: Intent) {
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        }
    }

    /**
     *
     */
    fun deleteEntry(context: Context,e:TestPlanEntry)
    {
        val entryId = findEventSingle(context,e)
        entryId?.let { ContentUris.withAppendedId(EVENTS_URI, it) }
            ?.let {
                val numRows = context.contentResolver?.delete(it,null,null)
                Log.d("GoogleCalendarIO","Deleted $numRows events")
            }
    }

    fun findEventIds(context: Context):List<Long>{
        val ret: MutableList<Long> = mutableListOf()
        val cursor = context.contentResolver?.query(
            EVENTS_URI,arrayOf("_id"),
            "calendar_id=$CAL_ID",null,null)
        while(cursor?.moveToNext() == true){
            ret.add(cursor.getLong(cursor.getColumnIndex("_id")))
            Log.d("EventTestCalId",cursor.getString(cursor.getColumnIndex("_id")))
            }
        cursor?.close()
        return ret
    }

    fun findEventSingle(context: Context,entry:TestPlanEntry):Long?{
        val cursor = context.contentResolver?.query(
            EVENTS_URI,arrayOf("_id","title","description"),
            "calendar_id=$CAL_ID",null,null)
        while(cursor?.moveToNext() == true){
            val id = cursor.getLong(cursor.getColumnIndex("_id"))
            val module = cursor.getString(cursor.getColumnIndex("title"))
            val description = cursor.getString(cursor.getColumnIndex("description"))
            if(module == entry.module&&description == entry.getString(context))
            {
                Log.d("EventTest", "$id;$module")
                cursor.close()
                return id
            }
        }
        cursor?.close()
        return null
    }
}