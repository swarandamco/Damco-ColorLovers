package com.demo.damcogroup.damcocolorlovers.utils

import android.content.Context
import android.net.ConnectivityManager
import android.view.View

var baseUrl: String = "https://www.colourlovers.com/api/"
var KEY_WORD: String = "KEYWORDS"

fun avoidDoubleClicks(view: View) {
    val DELAY_IN_MS: Long = 900
    if (!view.isClickable) {
        return
    }
    view.isClickable = false
    view.postDelayed({ view.isClickable = true }, DELAY_IN_MS)
}


fun isConnectionAvailable(context: Context): Boolean {
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = cm.activeNetworkInfo
    return activeNetwork != null
}
