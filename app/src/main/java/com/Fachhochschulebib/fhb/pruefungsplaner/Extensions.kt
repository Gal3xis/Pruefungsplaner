package com.Fachhochschulebib.fhb.pruefungsplaner

import android.content.Context
import android.util.Log
import android.widget.Spinner
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.Fachhochschulebib.fhb.pruefungsplaner.data.TestPlanEntry
import java.lang.Exception
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

//TODO Alexander Lange Start

/**
 * Extension-Function for the [Spinner]. Lets the user set the selection with the text-content of the spinner
 * instead of the position.
 *
 * @param[value] The text, the spinner is showing at the wanted position.
 * @author Alexander Lange (Email:alexander.lange@fh-bielefeld.de)
 * @since 1.5
 * @see Spinner
 */
fun Spinner.setSelection(value: String?) {
    if (value == null) {
        return
    }
    for (i in 0 until count) {
        val item = getItemAtPosition(i)
        if (item.toString().equals(value)) {
            setSelection(i)
            return
        }
    }
}

/**
 * Returns formatted details for a [TestPlanEntry].
 *
 * @param[context] The application context
 *
 * @return A Formatted String, including the details for the exam.
 *
 * @author Alexander Lange
 * @since 1.5
 */
fun TestPlanEntry.getString(context: Context):String?{
    val ret = StringBuilder()
    ret.appendLine(context.getString(R.string.information))
    ret.appendLine()
    ret.appendLine(createStringWithLabel(context,R.string.course,course))
    ret.appendLine(createStringWithLabel(context,R.string.modul,module))
    ret.appendLine()
    ret.appendLine(createStringWithLabel(context,R.string.firstProf,firstExaminer))
    ret.appendLine(createStringWithLabel(context,R.string.secondProf,secondExaminer))
    val d = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date)
    ret.appendLine(createStringWithLabel(context,R.string.date2,SimpleDateFormat("dd.MM.yyyy").format(d)))
    ret.appendLine(createStringWithLabel(context,R.string.clockTime2,SimpleDateFormat("HH:mm").format(d)))
    ret.appendLine(createStringWithLabel(context,R.string.duration,
        Utils.getExamDuration(examForm)?.toString() + "min"
    ))
    ret.appendLine(createStringWithLabel(context,R.string.room,room))
    ret.appendLine(createStringWithLabel(context,R.string.form,examForm))

    return ret.toString()
}

/**
 * Puts together a label from the strings-resources and an associated value. (E.g. Modul:Datenbankanwendungen)
 *
 * @param[context] The application context.
 * @param[label] The Resources-Id of the label.
 * @param[info] The value, associated with the label.
 *
 * @return A String, that displays the label and the info.
 *
 * @author Alexander Lange
 * @since 1.5
 */
private fun createStringWithLabel(context: Context,@StringRes label:Int,info:String?):String{
    return StringBuilder().append(context.getString(label)).append(info).toString()
}

/**
 * Extension-function for the [Date]-Class, which checks if a given date is at the same day as the current date.
 *
 * @param[date] The date which should be compared.
 *
 * @return true-> both dates are at the same day,false->both dates are not at the same day.
 *
 * @author Alexander Lange (Email:alexander.lange@fh-bielefeld.de)
 * @since 1.5
 * @see Date
 */
fun Date.atDay(date: Date): Boolean {
    val sdfy = SimpleDateFormat("yyyy")
    val sdfm = SimpleDateFormat("MM")
    val sdfd = SimpleDateFormat("dd")

    val date1 = Calendar.getInstance()
    date1.set(sdfy.format(this).toInt(), sdfm.format(this).toInt(), sdfd.format(this).toInt())

    val date2 = Calendar.getInstance()
    date2.set(sdfy.format(date).toInt(), sdfm.format(date).toInt(), sdfd.format(date).toInt())

    return date1.equals(date2)
}

/** Applies Settings from sharedPreferences to the activity.
 *
 * @author Alexander Lange
 * @since 1.5
 *
 * @see Optionen
 */
fun AppCompatActivity.applySettings() {
    val sharedPreferencesSettings = getSharedPreferences("settings", Context.MODE_PRIVATE)

    //Set Theme
    val themeToApply = sharedPreferencesSettings?.getInt("themeid", R.style.Theme_AppTheme_1) ?: R.style.Theme_AppTheme_1

    if(Utils.themeList.contains(themeToApply))
    {
        theme.applyStyle(themeToApply, true)
    }
    else{
        theme.applyStyle(Utils.themeList[0],true)
    }

    //Set Darkmode
    val darkMode = sharedPreferencesSettings.getBoolean("darkmode", false)
    AppCompatDelegate.setDefaultNightMode(if (darkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
}

/**
 * Adds an item to a given position, or, if the position is null, appends the item at the end of the list.
 *
 * @param[position] The position, where the item shall be inserted. Can be null
 * @param[item] The item to be inserted
 *
 * @return The position, where the item was inserted.
 *
 * @author Alexander Lange
 * @since 1.5
 */
fun <E> MutableList<E>.add(position:Int?,item:E):Int{
    if(position==null){
        add(item)
        return size-1
    }
    add(position,item)
    return position
}
//TODO Alexander Lange End