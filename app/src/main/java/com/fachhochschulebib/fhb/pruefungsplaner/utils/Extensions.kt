package com.fachhochschulebib.fhb.pruefungsplaner.utils

import android.app.AlertDialog
import android.content.Context
import android.widget.Spinner
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.fachhochschulebib.fhb.pruefungsplaner.R
import com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry
import com.fachhochschulebib.fhb.pruefungsplaner.viewmodel.BaseViewModel
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*
/**
 * Extension-Function for the [Spinner]. Lets the user set the selection with the text-content of the spinner
 * instead of the position.
 *
 * @param[value] The text, the spinner is showing at the wanted position.
 * @author Alexander Lange (Email:alexander.lange@fh-bielefeld.de)
 * @since 1.6
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
 * @since 1.6
 */
fun TestPlanEntry.getString(context: Context): String? {
    val ret = StringBuilder()
    ret.appendLine(context.getString(R.string.information))
    ret.appendLine()
    ret.appendLine(createStringWithLabel(context, R.string.course, course))
    ret.appendLine(createStringWithLabel(context, R.string.modul, module))
    ret.appendLine()
    ret.appendLine(createStringWithLabel(context, R.string.firstProf, firstExaminer))
    ret.appendLine(createStringWithLabel(context, R.string.secondProf, secondExaminer))
    val d = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date)
    ret.appendLine(createStringWithLabel(context, R.string.date2, SimpleDateFormat("dd.MM.yyyy").format(d)))
    ret.appendLine(createStringWithLabel(context, R.string.clockTime2, SimpleDateFormat("HH:mm").format(d)))
    ret.appendLine(createStringWithLabel(context, R.string.duration,
            Utils.getExamDuration(examForm)?.toString() + "min"
    ))
    ret.appendLine(createStringWithLabel(context, R.string.room, room))
    ret.appendLine(createStringWithLabel(context, R.string.form, Utils.getExamForm(examForm)))

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
 * @since 1.6
 */
private fun createStringWithLabel(context: Context, @StringRes label: Int, info: String?): String {
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
 * @since 1.6
 * @see Date
 */
fun Date.atDay(date: Date): Boolean {

    val year1 = this.year
    val year2 = date.year
    val month1 = this.month
    val month2 = date.month
    val day1 = this.date
    val day2 = date.date

    return year1 == year2&&month1==month2&&day1==day2
}

/** Applies Settings from sharedPreferences to the activity.
 *
 * @author Alexander Lange
 * @since 1.6
 *
 * @see Optionen
 */
fun AppCompatActivity.applySettings(viewModel:BaseViewModel) {
    val themeToApply = if(viewModel.getChosenThemeId()==-1)Utils.themeList[0] else viewModel.getChosenThemeId()
    if (Utils.themeList.contains(themeToApply)) {
        theme.applyStyle(themeToApply, true)
    } else {
        theme.applyStyle(Utils.themeList[0], true)
    }
    val darkMode = viewModel.getChosenDarkMode()
    AppCompatDelegate.setDefaultNightMode(if (darkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
}

/**
 * Closes the Application. First start a dialog to ask the user if the app shall be closed.
 *
 * @author Alexander Lange
 * @since 1.6
 */
fun AppCompatActivity.closeApp() {
    AlertDialog.Builder(this)
        .setMessage(R.string.close_app)
        .setTitle(R.string.title_close_app)
        .setPositiveButton(R.string.title_close_app) { _, _ ->
            setResult(0)
            finishAffinity()
        }
        .setNegativeButton("Cancel", null)
        .create()
        .show()
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
 * @since 1.6
 */
fun <E> MutableList<E>.add(position: Int?, item: E): Int {
    if (position == null) {
        add(item)
        return size - 1
    }
    add(position, item)
    return position
}