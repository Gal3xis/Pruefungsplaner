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

class SpecialButton(context: Context,attrs:AttributeSet): androidx.appcompat.widget.AppCompatButton(context,attrs) {

    private var animation = CreateAnimation()
    private var reverseAnimation = CreateReverseAnimation()
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


    private fun CreateAnimation():ValueAnimator {
        /** Animation with Image Icon*/
//        val imageView = ImageView(context)
//        imageView.setImageDrawable(activity?.getDrawable(R.drawable.ic_update))
//        val relativeLayout = settings_relative_layout
//        val params = RelativeLayout.LayoutParams(
//            RelativeLayout.LayoutParams.WRAP_CONTENT,
//            RelativeLayout.LayoutParams.WRAP_CONTENT
//        )
//
//        params.addRule(RelativeLayout.CENTER_HORIZONTAL)
//        params.addRule(RelativeLayout.ALIGN_PARENT_TOP)
//        val animatorIcon = ObjectAnimator.ofFloat(imageView, "rotation", 360f).setDuration(1000)
//        animatorIcon.repeatCount = 2
//        animatorIcon.doOnEnd {
//            relativeLayout.removeView(imageView)
//        }
//
//        animatorIcon.start()
//        relativeLayout.addView(imageView, params)
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
    private fun CreateReverseAnimation():ValueAnimator {
        val originalColor = this.backgroundTintList?.defaultColor

        val targetColor = context?.theme?.let { Utils.getColorFromAttr(R.attr.colorAccent, it) }
        val animator = ValueAnimator.ofObject(ArgbEvaluator(),targetColor,originalColor).setDuration(300)
        animator.addUpdateListener {
            this.backgroundTintList = ColorStateList.valueOf(it.animatedValue as Int)
        }
        return animator
    }
}