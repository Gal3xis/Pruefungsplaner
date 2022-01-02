package com.Fachhochschulebib.fhb.pruefungsplaner

import android.content.Context
import android.util.Log
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
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

    //Set Darkmode
    val darkMode = sharedPreferencesSettings.getBoolean("darkmode", false)
    AppCompatDelegate.setDefaultNightMode(if (darkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
    Log.d("ThemeTest", darkMode.toString())

    //Set Theme
    val themeToApply = sharedPreferencesSettings?.getInt("themeid", R.style.Theme_AppTheme_1) ?: R.style.Theme_AppTheme_1

    if(Utils.themeList.contains(themeToApply))
    {
        theme.applyStyle(themeToApply, true)
    }
    else{
        theme.applyStyle(Utils.themeList[0],true)
    }
    Log.d("ThemeTest", themeToApply.toString())
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