package com.Fachhochschulebib.fhb.pruefungsplaner

import android.widget.Spinner
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
//TODO Alexander Lange End