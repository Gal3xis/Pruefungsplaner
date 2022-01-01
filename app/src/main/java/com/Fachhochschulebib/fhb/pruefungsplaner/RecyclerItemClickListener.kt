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

/**
 * Custom OnItemTouchListener for the recyclerview.
 * @author Alexander Lange (Email:alexander.lange@fh-bielefeld.de)
 * @since 1.5
 */
class RecyclerItemClickListener(
    context: Context?, //überprüfung welches item geklickt wurde
    private val mListener: OnItemClickListener?): RecyclerView.OnItemTouchListener {
    interface OnItemClickListener {
        fun onItemClick(view: View?, position: Int)
    }

    var mGestureDetector: GestureDetector

    //position bestimmen von dem geklickten item
    /**
     * Overrides the [RecyclerView.OnItemTouchListener.onInterceptTouchEvent]-Method from the [RecyclerView.OnItemTouchListener]-Class.
     * Determines the childview of the recyclerview, that was clicked.
     * @param[view] The recyclerview, which is attatched.
     * @param[e] Information about the touchevent.
     * @return Returns always false.
     * @author Alexander Lange
     * @since 1.5
     * @see RecyclerView.OnItemTouchListener.onInterceptTouchEvent
     * @see RecyclerView.OnItemTouchListener
     */
    override fun onInterceptTouchEvent(view: RecyclerView, e: MotionEvent): Boolean {
        val childView = view.findChildViewUnder(e.x, e.y)
        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
            mListener.onItemClick(childView, view.getChildAdapterPosition(childView))
        }
        return false
    }

    /**
     * Overrides the [onTouchEvent]-Method of the [RecyclerView.OnItemTouchListener]-Class.
     * @param[view] The recyclerview, which is attatched.
     * @param[motionEvent] MotionEvent describing the touch event. All coordinates are in the RecyclerView's coordinate system.
     * @author Alexander Lange
     * @since 1.5
     * @see RecyclerView.OnItemTouchListener.onTouchEvent
     */
    override fun onTouchEvent(view: RecyclerView, motionEvent: MotionEvent) {}

    /**
     * Overrides the [RecyclerView.OnItemTouchListener.onRequestDisallowInterceptTouchEvent] of the [RecyclerView.OnItemTouchListener]-Class.
     * @param[disallowIntercept] True if the child does not want the parent to intercept touch events.
     * @author Alexander Lange
     * @since 1.5
     * @see RecyclerView.OnItemTouchListener.onRequestDisallowInterceptTouchEvent
     */
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