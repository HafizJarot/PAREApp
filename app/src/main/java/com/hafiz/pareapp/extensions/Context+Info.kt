package com.hafiz.pareapp.extensions

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

fun Context.showToast(message: String){
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Context.showInfoAlert(message: String){
    AlertDialog.Builder(this).apply {
        setMessage(message)
        setPositiveButton("OK"){ d, _ ->
            d.dismiss()
        }
    }.show()
}