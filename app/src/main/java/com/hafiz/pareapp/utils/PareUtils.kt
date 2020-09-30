package com.hafiz.pareapp.utils

import android.app.DatePickerDialog
import android.content.Context
import android.content.Context.MODE_PRIVATE
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.xml.datatype.DatatypeConstants.MONTHS

object PareUtils {
    fun getToken(c : Context) : String? {
        val s = c.getSharedPreferences("USER", MODE_PRIVATE)
        return s?.getString("TOKEN", null)
    }

    fun getRole(c : Context) : Boolean? {
        val s = c.getSharedPreferences("USER", MODE_PRIVATE)
        return s?.getBoolean("ROLE", false)
    }

    fun setRole(context: Context, role: Boolean){
        val pref = context.getSharedPreferences("USER", MODE_PRIVATE)
        pref.edit().apply {
            putBoolean("ROLE", role)
            apply()
        }
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