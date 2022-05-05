package com.fachhochschulebib.fhb.pruefungsplaner.utils

import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.util.Log
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.RawRes
import com.fachhochschulebib.fhb.pruefungsplaner.R
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception

/**
 * Utility-Function for the application.
 *
 * @author Alexander Lange (Email:alexander.lange@fh-bielefeld.de)
 * @since 1.6
 */
object Utils {
    /**
     * Takes an Attribute-ID for a color and the current theme and returns the color for that id.
     * Provides access to the colorscheme, so they can be assigned dynamically in code.
     *
     * @param[attrColor] The id of the Color (R.attr.*)
     * @param[context] The context (not applicationcontext!!!)
     * @param[typedValue] Stores the data of the resolved attribute, does not need to be given
     * @param[resolveRes] If true, resource references will be walked; if false, outValue may be a TYPE_REFERENCE. In either case, it will never be a TYPE_ATTRIBUTE.
     *
     * @return The color as integer, that is associated with the given id.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun getColorFromAttr(@AttrRes attrColor:Int, context: Context, typedValue: TypedValue= TypedValue(), resolveRes:Boolean=true):Int{
        context.theme.resolveAttribute(attrColor,typedValue,resolveRes)
        return typedValue.data
    }

    /**
     * A map, where an Entry-Status is mapped to a specific color
     */
    val statusColors:Map<String,Int> = mapOf("Früher Vorschlag" to R.attr.frueherVorschlag,"In Diskussion" to R.attr.inDiskussion, "Veröffentlicht" to R.attr.veroeffentlicht, "Veraltet" to R.attr.veraltet, "Zukünftige Planung" to R.attr.zukuenftigePlanung)

    /**
     * A map, where the icons for favorite/not favorite entry are defined
     */
    val favoriteIcons:Map<Boolean,Int> = mapOf(true to R.drawable.icon_exam_favorit,false to R.drawable.icon_exam_not_favorit )//TODO Customize

    /**
     * A list of all available themes
     */
    val themeList:List<Int> = listOf(R.style.Theme_AppTheme_1, R.style.Theme_AppTheme_2)

    /**
     * Reads a textfile in the Raw-Folder and returns the content as a string.
     *
     * @param[context] The context of the application
     * @param[textResource] The id of the textfile (R.raw.*)
     *
     * @return The content of the textfile as string
     *
     * @author Alexander Lange
     * @since 1.6
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

    /**
     * Returns the duration for a given exam. Necessary because the duration information
     * is transferred via the examForm.
     *
     * @param[examForm] The exam form that holds information about the duration.
     *
     * @return The duration for the exam in minutes. Returns zero if it doesn't find the information in the input.
     *
     * @author Alexander Lange
     */
    fun getExamDuration(examForm:String?): Int {
        val pattern = Regex("[0-9]+")
        val result: MatchResult? = examForm?.let { pattern.find(it) }
        val duration = result?.value?.toInt()
        return duration?:0
    }

    /**
     * Returns the form of the exam, seperated from the duration.
     *
     * @param examForm The examform from the [com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry]
     *
     * @return The examform, separated from the duration
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun getExamForm(examForm: String?):String{
        val pattern = Regex("[^0-9()]+")
        val result: MatchResult? = examForm?.let { pattern.find(it) }
        val form = result?.value
        return form?:examForm?:""
    }
}