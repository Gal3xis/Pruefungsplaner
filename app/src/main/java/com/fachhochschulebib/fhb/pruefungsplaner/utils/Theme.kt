package com.fachhochschulebib.fhb.pruefungsplaner.utils

import android.content.Context
import android.content.res.Resources
import android.view.View
import com.fachhochschulebib.fhb.pruefungsplaner.R

/**
 * Class to store data for a Theme
 *
 * @param resId The ThemeId
 * @param context The Applicationcontext
 *
 * @author Alexander Lange
 * @since 1.6
 */
class Theme (resId:Int?,context: Context){
    /**
     * The name of the theme
     */
    var name:String? = null

    /**
     * The theme
     */
    var theme: Resources.Theme? = null


    init {
        if(resId!=null)
        {
            theme = context.resources?.newTheme()
            theme?.applyStyle(resId,true)
            val ta = context.obtainStyledAttributes(resId, intArrayOf(R.attr.themeName))
            name = ta?.getString(0)
            ta?.recycle()
        }
    }
}