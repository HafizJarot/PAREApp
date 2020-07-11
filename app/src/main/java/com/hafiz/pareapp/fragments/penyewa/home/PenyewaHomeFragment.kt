package com.hafiz.pareapp.fragments.penyewa.home

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.hafiz.pareapp.R
import com.hafiz.pareapp.activiities.penyewa.produk.PenyewaProdukActivity
import kotlinx.android.synthetic.main.penyewa_fragment_home.*
import java.text.SimpleDateFormat
import java.util.*

class PenyewaHomeFragment :Fragment(R.layout.penyewa_fragment_home){

    private lateinit var tanggal_mulai : String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setDate()
    }

    private fun setDate(){
        val cal = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, month)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "yyyy-MM-dd"
            val simpleDateFormat = SimpleDateFormat(myFormat, Locale.US)
            txt_tanggal_mulai.setText(simpleDateFormat.format(cal.time))
            tanggal_mulai = txt_tanggal_mulai.text.toString().trim()
        }
        txt_tanggal_mulai.setOnClickListener {
            DatePickerDialog(requireActivity(), dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        btn_search.setOnClickListener { startActivity(Intent(requireActivity(), PenyewaProdukActivity::class.java).apply {
            val lamaSewa = et_lama_sewa.text.toString().trim()
            putExtra("TANGGAL_MULAI", tanggal_mulai)
            putExtra("LAMA_SEWA", lamaSewa)
        })}
    }
}