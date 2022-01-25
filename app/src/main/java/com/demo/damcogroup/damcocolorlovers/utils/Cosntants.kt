package com.demo.damcogroup.damcocolorlovers.utils

import android.content.Context
import android.net.ConnectivityManager
import android.view.View

var BASE_URL: String = "https://www.colourlovers.com/api/"
var KEY_WORD: String = "KEYWORD"

//  This function is using to check the internet connection
fun avoidDoubleClicks(view: View) {
    val DELAY_IN_MS: Long = 900
    if (!view.isClickable) {
        return
    }
    view.isClickable = false
    view.postDelayed({ view.isClickable = true }, DELAY_IN_MS)
}

//  This function is using to check the internet connection
fun isConnectionAvailable(context: Context): Boolean {
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = cm.activeNetworkInfo
    return activeNetwork != null
}
