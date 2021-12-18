package com.Fachhochschulebib.fhb.pruefungsplaner

import android.content.res.Resources
import android.util.TypedValue
import androidx.annotation.AttrRes
import java.util.*

object Utils {

    fun getColorFromAttr(@AttrRes attrColor:Int, theme: Resources.Theme, typedValue: TypedValue= TypedValue(), resolveRes:Boolean=true):Int{
        theme.resolveAttribute(attrColor,typedValue,resolveRes)
        return typedValue.data
    }

    val statusColors:Map<String,Int> = mapOf("Früher Vorschlag" to R.attr.frueherVorschlag,"In Diskussion" to R.attr.inDiskussion, "Veröffentlicht" to R.attr.veroeffentlicht, "Veraltet" to R.attr.veraltet, "Zukünftige Planung" to R.attr.zukuenftigePlanung)
    
}

