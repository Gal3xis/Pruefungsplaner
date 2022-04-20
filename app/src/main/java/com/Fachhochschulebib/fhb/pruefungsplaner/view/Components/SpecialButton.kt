package com.Fachhochschulebib.fhb.pruefungsplaner.view.Components

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.DragEvent
import android.view.MotionEvent
import androidx.core.animation.doOnEnd
import com.Fachhochschulebib.fhb.pruefungsplaner.R
import com.Fachhochschulebib.fhb.pruefungsplaner.utils.Utils

/**
 * A Button with custom touch animations. Used in [com.Fachhochschulebib.fhb.pruefungsplaner.view.fragments.SettingsFragment]
 *
 * @author Alexander Lange (Email:alexander.lange@fh-bielefeld.de)
 * @since 1.6
 */
class SpecialButton(context: Context,attrs:AttributeSet): androidx.appcompat.widget.AppCompatButton(context,attrs) {

    private var animation = createAnimation()
    private var reverseAnimation = createReverseAnimation()
    private var fingerDown = false

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action){
            MotionEvent.ACTION_UP->{
                fingerDown = false
                if(!animation.isRunning) reverseAnimation.start()
            }
            MotionEvent.ACTION_DOWN->{
                fingerDown = true
                if(!animation.isRunning) animation.start()
            }
            MotionEvent.ACTION_CANCEL->{
                reverseAnimation.start()
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onDragEvent(event: DragEvent?): Boolean {
        reverseAnimation.start()
        return super.onDragEvent(event)
    }

    private fun createAnimation():ValueAnimator {
        val originalColor = this.backgroundTintList?.defaultColor

        val targetColor = context?.theme?.let { Utils.getColorFromAttr(R.attr.colorAccent, it) }
        val animator = ValueAnimator.ofObject(ArgbEvaluator(),originalColor,targetColor).setDuration(300)
        animator.addUpdateListener {
            this.backgroundTintList = ColorStateList.valueOf(it.animatedValue as Int)
        }
        animator.doOnEnd {
            if(!fingerDown)reverseAnimation.start()
        }
        return animator
    }

    private fun createReverseAnimation():ValueAnimator {
        val originalColor = this.backgroundTintList?.defaultColor

        val targetColor = context?.theme?.let { Utils.getColorFromAttr(R.attr.colorAccent, it) }
        val animator = ValueAnimator.ofObject(ArgbEvaluator(),targetColor,originalColor).setDuration(300)
        animator.addUpdateListener {
            this.backgroundTintList = ColorStateList.valueOf(it.animatedValue as Int)
        }
        return animator
    }
}