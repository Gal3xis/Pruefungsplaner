package com.Fachhochschulebib.fhb.pruefungsplaner

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.RawRes
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.nio.Buffer
import java.util.*
import android.util.Log


object Utils {

    fun getColorFromAttr(@AttrRes attrColor:Int, theme: Resources.Theme, typedValue: TypedValue= TypedValue(), resolveRes:Boolean=true):Int{
        theme.resolveAttribute(attrColor,typedValue,resolveRes)
        return typedValue.data
    }

    val statusColors:Map<String,Int> = mapOf("Früher Vorschlag" to R.attr.frueherVorschlag,"In Diskussion" to R.attr.inDiskussion, "Veröffentlicht" to R.attr.veroeffentlicht, "Veraltet" to R.attr.veraltet, "Zukünftige Planung" to R.attr.zukuenftigePlanung)

    fun readTextFile(context: Context,@RawRes textResource:Int):String?{
        val _is = context.resources.openRawResource(textResource)
        val reader = BufferedReader(InputStreamReader(_is))
        var string:String? = null
        val stringBuilder:StringBuilder = StringBuilder()
        while (true){
            string = reader.readLine()
            try {
                if (string==null) break
            }catch (ex:Exception){
                Log.e("Utils",ex.stackTraceToString())
            }
            stringBuilder.append(string).append("\n")
        }
        _is.close()
        return stringBuilder.toString()
    }
}

