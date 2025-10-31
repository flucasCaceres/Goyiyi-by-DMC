package com.dmc.goyiyi.util

import android.content.Context
import android.view.View
import android.widget.Toast

fun Context.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

inline fun <T : View> T.disableWhile(block: T.() -> Unit) {
    isEnabled = false
    try { block() } finally { isEnabled = true }
}
