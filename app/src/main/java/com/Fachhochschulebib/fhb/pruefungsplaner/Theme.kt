package com.Fachhochschulebib.fhb.pruefungsplaner

import android.content.res.Resources
import android.graphics.Color
import android.view.View
import androidx.annotation.StyleableRes

class Theme (resId:Int?,view:View?){
    var name:String? = null
    var theme: Resources.Theme? = null

    init {
        if(resId!=null)
        {
            theme = view?.context?.resources?.newTheme()
            theme?.applyStyle(resId,true)
            val ta = view?.context?.obtainStyledAttributes(resId, intArrayOf(R.attr.themeName))
            name = ta?.getString(0)
            ta?.recycle()
        }
    }
}