package com.dmc.goyiyi.util

import android.util.Log
import android.view.View
import androidx.core.view.isVisible

class LoadingOverlay(private val view: View) {
    fun show() {
        Log.d("OVERLAY_DEBUG", "Overlay show(): visible=${view.isVisible}")
        if (view.isVisible) return
        view.alpha = 0f
        view.isVisible = true
        view.bringToFront()
        view.animate().alpha(1f).setDuration(120).start()
    }
    fun hide() {
        Log.d("OVERLAY_DEBUG", "Overlay hide(): visible=${view.isVisible}")
        if (!view.isVisible) return
        view.animate().alpha(0f).setDuration(120).withEndAction {
            view.isVisible = false
        }.start()
    }
}
fun View.asLoadingOverlay(): LoadingOverlay = LoadingOverlay(this)
