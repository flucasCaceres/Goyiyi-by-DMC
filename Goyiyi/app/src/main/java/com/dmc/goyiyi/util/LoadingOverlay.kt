package com.dmc.goyiyi.util

import android.view.View
import androidx.core.view.isVisible

class LoadingOverlay(private val view: View) {
    fun show() {
        if (view.isVisible) return
        view.alpha = 0f
        view.isVisible = true
        view.bringToFront()
        view.animate().alpha(1f).setDuration(120).start()
    }
    fun hide() {
        if (!view.isVisible) return
        view.animate().alpha(0f).setDuration(120).withEndAction { view.isVisible = false }.start()
    }
}
fun View.asLoadingOverlay(): LoadingOverlay = LoadingOverlay(this)
