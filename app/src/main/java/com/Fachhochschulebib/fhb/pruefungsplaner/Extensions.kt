package com.Fachhochschulebib.fhb.pruefungsplaner

import android.widget.Spinner
import java.text.SimpleDateFormat
import java.util.*

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
fun Spinner.setSelection(value:String?){
    if(value==null)
    {
        return
    }
    for (i in 0 until count){
        val item = getItemAtPosition(i)
        if(item.toString().equals(value)){
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
fun Date.atDay(date:Date):Boolean{
    val sdfy  = SimpleDateFormat("yyyy")
    val sdfm  = SimpleDateFormat("MM")
    val sdfd  = SimpleDateFormat("dd")

    val date1 = Calendar.getInstance()
    date1.set(sdfy.format(this).toInt(),sdfm.format(this).toInt(),sdfd.format(this).toInt())

    val date2 = Calendar.getInstance()
    date2.set(sdfy.format(date).toInt(),sdfm.format(date).toInt(),sdfd.format(date).toInt())

    return date1.equals(date2)
}
//TODO Alexander Lange End