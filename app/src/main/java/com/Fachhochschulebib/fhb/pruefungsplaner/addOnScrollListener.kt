//////////////////////////////
// addOnScrollListener
//
//
//
// autor:
// inhalt: Überprüfung ob im RecyclerView gescrollt wird
// zugriffsdatum: 11.12.19
//
//
//
//
//
//
//////////////////////////////
package com.Fachhochschulebib.fhb.pruefungsplaner

import androidx.recyclerview.widget.RecyclerView

//überprüfung ob im RecyclerView gescrollt wird.
//wird von den Klasse myAdapter abgefragt
//dient zur überprüfung bei scrollen ob ein Prüfitem mit geöffneten Tab "mehr informationen" geschlossen werden kann
class addOnScrollListener : RecyclerView.OnScrollListener() {
    fun CustomScrollListener() {} //TODO Remove?
    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        when (newState) {
            RecyclerView.SCROLL_STATE_IDLE -> {
            }
            RecyclerView.SCROLL_STATE_DRAGGING -> {
            }
            RecyclerView.SCROLL_STATE_SETTLING -> {
            }
        }
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        if (dx > 0) {
            // System.out.println("Scrolled Right");
        } else if (dx < 0) {
            // System.out.println("Scrolled Left");
        } else {
            // System.out.println("No Horizontal Scrolled");
        }
        if (dy > 0) {
            // System.out.println("Scrolled Downwards");
        } else if (dy < 0) {
            // System.out.println("Scrolled Upwards");
        } else {
            // System.out.println("No Vertical Scrolled");
        }
    }
}