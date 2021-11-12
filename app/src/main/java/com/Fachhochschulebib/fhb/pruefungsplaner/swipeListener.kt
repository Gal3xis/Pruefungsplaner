package com.Fachhochschulebib.fhb.pruefungsplaner

import android.R
import android.content.Context
import android.graphics.*
import androidx.recyclerview.widget.ItemTouchHelper
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.RecyclerView
import androidx.core.content.ContextCompat

// Erweitere die Callback Funktion
open class swipeListener internal constructor(var mContext: Context, delete: Boolean) :
    ItemTouchHelper.Callback() {
    private val mClearPaint: Paint
    private val mBackground: ColorDrawable
    private var backgroundColor = 0
    private var deleteDrawable: Drawable? = null
    private val intrinsicWidth: Int
    private val intrinsicHeight: Int
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return makeMovementFlags(0, ItemTouchHelper.LEFT)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        viewHolder1: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        val itemView = viewHolder.itemView
        val itemHeight = itemView.height
        val isCancelled = dX == 0f && !isCurrentlyActive

        // Wenn die Bewegung beendet wird,
        // wird die Aktion abgebrochen
        if (isCancelled) {
            clearCanvas(
                c, itemView.right + dX, itemView.top.toFloat(), itemView.right
                    .toFloat(), itemView.bottom.toFloat()
            )
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            return
        }

        // Definiere den Hintergrund
        mBackground.color = backgroundColor
        mBackground.setBounds(
            itemView.right + dX.toInt(),
            itemView.top,
            itemView.right,
            itemView.bottom
        )
        mBackground.draw(c)

        // Die Daten für die Position
        val deleteIconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
        val deleteIconMargin = (itemHeight - intrinsicHeight) / 2
        val deleteIconLeft = itemView.right - deleteIconMargin - intrinsicWidth
        val deleteIconRight = itemView.right - deleteIconMargin
        val deleteIconBottom = deleteIconTop + intrinsicHeight

        // Setzt die Position und zeichene den Hintergrund
        deleteDrawable?.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
        deleteDrawable?.draw(c)
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private fun clearCanvas(c: Canvas, left: Float, top: Float, right: Float, bottom: Float) {
        c.drawRect(left, top, right, bottom, mClearPaint)
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        return 0.7f
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

    init {
        mBackground = ColorDrawable()
        if (delete) {
            // Farbe und Icon fürs löschen
            backgroundColor = Color.parseColor("#CC3333")
            deleteDrawable = ContextCompat.getDrawable(mContext, R.drawable.ic_menu_delete)
        } else {
            // Farbe und Icon fürs hinzufügen
            backgroundColor = Color.parseColor("#088A08")
            deleteDrawable = ContextCompat.getDrawable(mContext, R.drawable.ic_menu_add)
        }
        mClearPaint = Paint()
        mClearPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        intrinsicWidth = deleteDrawable?.intrinsicWidth?:0
        intrinsicHeight = deleteDrawable?.intrinsicHeight?:0
    }
}