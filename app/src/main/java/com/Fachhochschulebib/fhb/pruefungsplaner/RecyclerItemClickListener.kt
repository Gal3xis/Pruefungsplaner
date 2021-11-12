//////////////////////////////
// RecycleritemClickListener
//
//
//
// autor:
// inhalt: Überprüfung ob im recyclerview Elemente geklickt wurden
// zugriffsdatum: 11.12.19
//
//
//
//
//
//
//////////////////////////////
package com.Fachhochschulebib.fhb.pruefungsplaner

import android.content.Context
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import android.view.GestureDetector
import androidx.recyclerview.widget.RecyclerView
import android.view.MotionEvent
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.View

class RecyclerItemClickListener(
    context: Context?, //überprüfung welches item geklickt wurde
    private val mListener: (Any, Any) -> Unit
) : OnItemTouchListener {
    interface OnItemClickListener {
        fun onItemClick(view: View?, position: Int)
    }

    var mGestureDetector: GestureDetector

    //position bestimmen von dem geklickten item
    override fun onInterceptTouchEvent(view: RecyclerView, e: MotionEvent): Boolean {
        val childView = view.findChildViewUnder(e.x, e.y)
        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
            mListener.onItemClick(childView, view.getChildAdapterPosition(childView))
        }
        return false
    }

    override fun onTouchEvent(view: RecyclerView, motionEvent: MotionEvent) {}
    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}

    //Überprüfung ob single Klick
    init {
        mGestureDetector = GestureDetector(context, object : SimpleOnGestureListener() {
            override fun onSingleTapUp(e: MotionEvent): Boolean {
                return true
            }
        })
    }
}