package com.Fachhochschulebib.fhb.pruefungsplaner

import android.content.Context
import android.content.res.Resources
import android.util.Log
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.RawRes
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.util.*

/**
 * Utility-Function for the application.
 *
 * @author Alexander Lange (Email:alexander.lange@fh-bielefeld.de)
 * @since 1.5
 */
object Utils {

    /**
     * Takes an Attribute-ID for a color and the current theme and returns the color for that id.
     * Provides access to the colorscheme, so they can be assigned dynamically in code.
     *
     * @param[attrColor] The id of the Color (R.attr.*)
     * @param[theme] The current theme of the application
     * @param[typedValue] Stores the data of the resolved attribute, does not need to be given
     * @param[resolveRes] If true, resource references will be walked; if false, outValue may be a TYPE_REFERENCE. In either case, it will never be a TYPE_ATTRIBUTE.
     *
     * @return The color as integer, that is associated with the given id.
     *
     * @author Alexander Lange
     * @since 1.5
     */
    fun getColorFromAttr(@AttrRes attrColor:Int, theme: Resources.Theme, typedValue: TypedValue= TypedValue(), resolveRes:Boolean=true):Int{
        theme.resolveAttribute(attrColor,typedValue,resolveRes)
        return typedValue.data
    }

    val statusColors:Map<String,Int> = mapOf("Früher Vorschlag" to R.attr.frueherVorschlag,"In Diskussion" to R.attr.inDiskussion, "Veröffentlicht" to R.attr.veroeffentlicht, "Veraltet" to R.attr.veraltet, "Zukünftige Planung" to R.attr.zukuenftigePlanung)
    val favoritIcons:Map<Boolean,Int> = mapOf(true to android.R.drawable.ic_delete,false to android.R.drawable.ic_input_add )
    val themeList:List<Int> = listOf(R.style.Theme_AppTheme_1,R.style.Theme_AppTheme_2)


    /**
     * Reads a textfile in the Raw-Folder and returns the content as a string.
     *
     * @param[context] The context of the application
     * @param[textResource] The id of the textfile (R.raw.*)
     *
     * @return The content of the textfile as string
     *
     * @author Alexander Lange
     * @since 1.5
     */
    fun readTextFile(context: Context,@RawRes textResource:Int): String {
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

    fun getExamDuration(examForm:String?): Int? {
        return 90//TODO Implement
    }
}

