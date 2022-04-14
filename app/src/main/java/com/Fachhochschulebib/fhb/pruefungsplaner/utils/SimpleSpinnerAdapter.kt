package com.Fachhochschulebib.fhb.pruefungsplaner.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.Fachhochschulebib.fhb.pruefungsplaner.R

class SimpleSpinnerAdapter(context: Context, resource: Int, objects: MutableList<String>) :
    ArrayAdapter<String>(context, resource, objects) {




    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * [android.view.LayoutInflater#inflate]
     * to specify a root view and to prevent attachment to the root.
     *
     * @param position The position of the item within the adapter's data set of the item whose view
     *        we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *        is non-null and of an appropriate type before using. If it is not possible to convert
     *        this view to display the correct data, this method can create a new view.
     *        Heterogeneous lists can specify their number of view types, so that this View is
     *        always of the right type (see [getViewTypeCount] and
     *        [getItemViewType]).
     * @param parent The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View = LayoutInflater.from(context).inflate(R.layout.simple_spinner_background,null,true)
        val text = view.findViewById<TextView>(R.id.simple_spinner_background_text)
        text.text = getItem(position)
        text.setTextColor(Utils.getColorFromAttr(R.attr.colorOnBackground,context.theme))
        return view(view,position)
    }

    /**
     * Initializes the view, passes the values to the UI-Elements.
     *
     * @param[view] The view for a Spinnerrow.
     * @param[position] The index of the item
     *
     * @return The spinnerrow, containing the initialized UI-Elements.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private fun view(view: View?, position: Int): View {
        val text = view?.findViewById<TextView>(R.id.simple_spinner_item_text)
        view?.setBackgroundColor(Color.TRANSPARENT)
        text?.setTextColor(Utils.getColorFromAttr(R.attr.colorOnBackground,context.theme))
        text?.text = getItem(position)
        return view?: View(context)
    }

    /**
     * Gets a [android.view.View] that displays in the drop down popup
     * the data at the specified position in the data set.
     *
     * @param position index of the item whose view we want.
     * @param convertView the old view to reuse, if possible. Note: You should
     *        check that this view is non-null and of an appropriate type before
     *        using. If it is not possible to convert this view to display the
     *        correct data, this method can create a new view.
     * @param parent the parent that this view will eventually be attached to
     * @return a [android.view.View] corresponding to the data at the
     *         specified position.
     */
    override fun getDropDownView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {
        val cv = convertView?: LayoutInflater.from(context).inflate(R.layout.simple_spinner_item,parent,false)
        return view(cv,position)
    }
}