package com.Fachhochschulebib.fhb.pruefungsplaner

import android.widget.Spinner
import java.text.SimpleDateFormat
import java.util.*

//TODO Alexander Lange Start
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

fun MutableList<()->Unit>.append(value:()->Unit):Int{
    this.add { value }
    return this.size-1
}
//TODO Alexander Lange End