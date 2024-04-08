package com.example.scheduler.views

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

class AnimatedImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private var alphaStep = 0.1f
    private var alphaDirection = 1
    private var minAlpha = 0.2f
    private var maxAlpha = 1f

    override fun onDraw(canvas: Canvas) {
        val drawable = drawable ?: return

        // Update alpha based on direction
        alpha += alphaStep * alphaDirection

        // Change direction if alpha reaches limits
        if (alpha <= minAlpha || alpha >= maxAlpha) {
            alphaDirection *= -1
        }

        // Draw the drawable with the updated alpha
        drawable.alpha = (alpha * 255).toInt()
        drawable.draw(canvas)

        // Invalidate the view to trigger redraw
        invalidate()
    }
}