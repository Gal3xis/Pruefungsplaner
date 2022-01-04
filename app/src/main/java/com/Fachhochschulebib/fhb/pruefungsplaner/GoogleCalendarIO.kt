package com.Fachhochschulebib.fhb.pruefungsplaner

import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
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
 */
object GoogleCalendarIO {

    /**
     * Creates an intent for a Calendarevent.
     *
     * **See Also:**[CalendarContract-Documentation](https://developer.android.com/reference/android/provider/CalendarContract)
     *
     * @author Alexander Lange
     * @since 1.5
     */
    fun createIntent(title:String? = "Unnamed", description:String? = "", startDate:Calendar? = Calendar.getInstance(),endDate:Calendar?=null, allDay:Boolean? = false):Intent{
        val intent = Intent(Intent.ACTION_INSERT)
        intent.data = CalendarContract.Events.CONTENT_URI
        intent.putExtra(CalendarContract.Events.TITLE,title?:"")
        intent.putExtra(CalendarContract.Events.DESCRIPTION,description?:"")
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,startDate?.timeInMillis?:Calendar.getInstance().timeInMillis)
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,endDate?.timeInMillis?:startDate?.timeInMillis?:Calendar.getInstance().timeInMillis)
        intent.putExtra(CalendarContract.Events.ALL_DAY,allDay)
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
    fun createIntent(e: TestPlanEntry?):Intent{
        val cs = Calendar.getInstance()
        e?.let { cs.time = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(e.date) }
        val ce = Calendar.getInstance()
        e?.let {
            ce.time = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(e.date)
            ce.add(GregorianCalendar.MINUTE,Utils.getExamDuration(e.examForm)?:0)
        }
        return createIntent(title = e?.module?:"", description = e?.course?:"", startDate = cs, endDate = ce)
    }

    fun insertEntry(context: Context,e:TestPlanEntry?){
        val intent = createIntent(e)
        if(intent.resolveActivity(context.packageManager)!=null)
        {
            context.startActivity(intent)
        }
    }
}