package com.Fachhochschulebib.fhb.pruefungsplaner

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.NonNull

class ThemeAdapter(context: Context, resource: Int, objects: MutableList<Theme>) :
    ArrayAdapter<Theme>(context, resource, objects) {

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view:View = LayoutInflater.from(context).inflate(R.layout.layout_theme_spinner_background,null,true)
        return view(view,position)
    }

    private fun view(view: View?, position: Int): View {
        val image = view?.findViewById<View>(R.id.layout_theme_spinner_row_image)
        val text = view?.findViewById<TextView>(R.id.layout_theme_spinner_row_text)
        val theme = getItem(position)
        val drawable = view?.resources?.getDrawable(R.drawable.ic_themecolor,theme?.theme?:view.context.theme)
        image?.background = drawable
        text?.text = theme?.name?:"Unnamed"
        return view?:View(context)
    }

    override fun getDropDownView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View? {
        var cv = convertView?:LayoutInflater.from(context).inflate(R.layout.layout_theme_spinner_row,parent,false)
        return view(cv,position)
    }
}