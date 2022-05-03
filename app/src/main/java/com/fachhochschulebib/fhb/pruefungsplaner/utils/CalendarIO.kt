package com.fachhochschulebib.fhb.pruefungsplaner.utils

import android.app.AlertDialog
import android.content.*
import android.database.Cursor
import android.net.Uri
import android.provider.CalendarContract
import com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs
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
object CalendarIO {

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
    enum class InsertionType {
        Manuel, Automatic, Ask
    }

    /**
     * URI that is used to access the android calendar. Is passed to the content resolver with extra information to specify the request.
     */
    private val EVENTS_URI = Uri.parse("content://com.android.calendar/events")

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
    class SmartphoneCalendar(val id:Long,val name:String,val visible:Boolean,val accessLevel:Int)

    /**
     * Gets a list of all calendars,existing on the users smartphone.
     *
     * @param context The Applicationcontext
     * @return A list of [SmartphoneCalendar] that contain information about each calendar on the smartphone.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun getCalendars(context: Context):List<SmartphoneCalendar>{
        val ret = mutableListOf<SmartphoneCalendar>()

        val projection = arrayOf("_id", "calendar_displayName","visible","calendar_access_level")
        val calendars: Uri = Uri.parse("content://com.android.calendar/calendars")

        val contentResolver: ContentResolver = context.getContentResolver()
        val managedCursor: Cursor = contentResolver.query(calendars, projection, null, null, null)?:return emptyList()

        if (managedCursor.moveToFirst()) {
            var calName: String
            var calID: Long
            var calVisible:Boolean
            var calAccessLevel:Int
            var cont = 0
            val nameCol: Int = managedCursor.getColumnIndex(projection[1])
            val idCol: Int = managedCursor.getColumnIndex(projection[0])
            val idVisibility:Int = managedCursor.getColumnIndex(projection[2])
            val idAccessLevel:Int = managedCursor.getColumnIndex(projection[3])
            do {
                calName = managedCursor.getString(nameCol)
                calID = managedCursor.getString(idCol).toLong()
                calVisible = (managedCursor.getInt(idVisibility)) == 1
                calAccessLevel = managedCursor.getInt(idAccessLevel)
                ret.add(SmartphoneCalendar(calID,calName,calVisible,calAccessLevel))
                cont++
            } while (managedCursor.moveToNext())
            managedCursor.close()
        }
        return ret
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
    fun getPrimaryCalendar(context: Context):SmartphoneCalendar?{
        val projection = arrayOf("_id", "calendar_displayName","visible","calendar_access_level","isPrimary")
        val calendars: Uri = Uri.parse("content://com.android.calendar/calendars")

        val contentResolver: ContentResolver = context.getContentResolver()
        val managedCursor: Cursor = contentResolver.query(calendars, projection, null, null, null)?:return null

        if (managedCursor.moveToFirst()) {
            var calName: String
            var calID: Long
            var calVisible:Boolean
            var calAccessLevel:Int
            var calIsPrimary:Boolean
            var cont = 0
            val nameCol: Int = managedCursor.getColumnIndex(projection[1])
            val idCol: Int = managedCursor.getColumnIndex(projection[0])
            val idVisibility:Int = managedCursor.getColumnIndex(projection[2])
            val idAccessLevel:Int = managedCursor.getColumnIndex(projection[3])
            val idIsPrimary:Int = managedCursor.getColumnIndex(projection[4])
            do {
                calName = managedCursor.getString(nameCol)
                calID = managedCursor.getString(idCol).toLong()
                calVisible = (managedCursor.getInt(idVisibility)) == 1
                calAccessLevel = managedCursor.getInt(idAccessLevel)
                calIsPrimary = managedCursor.getInt(idIsPrimary)==1
                if(calIsPrimary){
                    managedCursor.close()
                    return SmartphoneCalendar(calID,calName,calVisible,calAccessLevel)
                }
                cont++
            } while (managedCursor.moveToNext())
            managedCursor.close()
        }
        return null
    }

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
        id:Long,
        CAL_ID:Long,
        title: String = "Unnamed",
        description: String = "",
        startDate: Calendar = Calendar.getInstance(),
        endDate: Calendar = Calendar.getInstance(),
        allDay: Boolean = false,
        hasAlarm: Boolean = false
    ): ContentValues {
        val event = ContentValues()
        event.put(CalendarContract.Events._ID, abs(id))
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
    fun createEvent(context: Context,CAL_ID: Long,id:Long, e: TestPlanEntry?): ContentValues {
        val cs = Calendar.getInstance()
        e?.let { cs.time = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(e.date) }
        val ce = Calendar.getInstance()
        e?.let {
            ce.time = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(e.date)
            ce.add(GregorianCalendar.MINUTE, Utils.getExamDuration(e.examForm) ?: 0)
        }
        return createEvent(
            id = id,
            CAL_ID,
            title = e?.module ?: "",
            description = e?.getString(context) ?: "",
            startDate = cs,
            endDate = ce
        )
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
    fun insertEntry(
        context: Context,
        CAL_ID: Long,
        e: TestPlanEntry,
        insertionType: InsertionType = InsertionType.Ask
    ) :Long?{
        when (insertionType) {
            InsertionType.Automatic -> {
                return forceInsert(context,CAL_ID, e)
            }
            InsertionType.Manuel -> {
                indirectInsert(context, createIntent(context, e))
                return null
            }
            InsertionType.Ask -> {
                var ret:Long? = null
                AlertDialog.Builder(context)
                    .setTitle("Termin Eintragen")
                    .setPositiveButton("Automatisch") { _, _ ->
                        ret = forceInsert(context, CAL_ID, e)
                    }
                    .setNegativeButton("Manuell") { _, _ ->
                        indirectInsert(context, createIntent(context, e))
                    }
                    .create().show()
                return ret
            }
        }
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
    private fun forceInsert(context: Context,CAL_ID: Long,testPlanEntry: TestPlanEntry ):Long? {
        val id: Long = getRandomId()
        val event = createEvent(context,CAL_ID,id,testPlanEntry)
        if(eventExists(context,id)){
            updateEvent(context,id,event)
        }else{
            insertEvent(context,event)
        }
        return if(eventExists(context,id)) id else null
    }

    /**
     * Returns a random ID
     *
     * @return A Random Long number, based on the uuid algorithm
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private fun getRandomId(): Long {
        return abs(UUID.randomUUID().mostSignificantBits)
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
    private fun insertEvent(context: Context,event: ContentValues){
        context.contentResolver?.insert(EVENTS_URI, event)
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
    private fun updateEvent(context: Context,id:Long,event:ContentValues){
        val deletedRows = deleteEvent(context,id)
        if(deletedRows==0) return
        insertEvent(context,event)
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
    private fun eventExists(context: Context,id:Long):Boolean{
        val cursor = context.contentResolver.query(
            EVENTS_URI,
            null,
            null,
            null,
            null
        )?:return true
        if(cursor.moveToFirst())
        {
            val idCol = cursor.getColumnIndex("_id")
            do {
                val calId = cursor.getLong(idCol)
                if(calId == id)
                {
                    return true
                }
            }while (cursor.moveToNext())
            cursor.close()
        }
        return false
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
     *
     */
    fun deleteEvent(context: Context,id: Long):Int{
        return context.contentResolver?.delete(
            ContentUris.withAppendedId(EVENTS_URI, id),
            null,
            null
        )?:0
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
    fun deleteEvents(context: Context,ids:List<Long>):Int{
        var deleted = 0
        ids.forEach {
            deleted += deleteEvent(context,it)
        }
        return deleted
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
}