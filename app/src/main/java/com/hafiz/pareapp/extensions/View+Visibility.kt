package com.hafiz.pareapp.extensions

import android.view.View

fun View.visible(){
    visibility = View.VISIBLE
}

fun View.gone(){
    visibility = View.GONE
}
fun View.disable(){
    isEnabled = false
}

fun View.enabled(){
    isEnabled = true
}