package com.hafiz.pareapp.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import java.text.NumberFormat
import java.util.*

object PareUtils {
    fun getToken(c : Context) : String? {
        val s = c.getSharedPreferences("USER", MODE_PRIVATE)
        return s?.getString("TOKEN", null)
    }

    fun setToken(context: Context, token : String){
        val pref = context.getSharedPreferences("USER", MODE_PRIVATE)
        pref.edit().apply {
            putString("TOKEN", token)
            apply()
        }
    }

    fun clearToken(context: Context){
        val pref = context.getSharedPreferences("USER", MODE_PRIVATE)
        pref.edit().clear().apply()
    }

    fun setToIDR(num : Int) : String {
        val localeID = Locale("in", "ID")
        val formatRupiah: NumberFormat = NumberFormat.getCurrencyInstance(localeID)
        return formatRupiah.format(num)
    }

    fun isValidEmail(email : String) : Boolean = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    fun isValidPassword(password : String) = password.length >= 8

}