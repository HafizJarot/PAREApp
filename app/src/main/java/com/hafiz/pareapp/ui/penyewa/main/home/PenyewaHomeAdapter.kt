package com.hafiz.pareapp.ui.penyewa.main.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.hafiz.pareapp.R
import com.hafiz.pareapp.models.Pemilik
import com.hafiz.pareapp.models.Produk
import com.hafiz.pareapp.ui.penyewa.detail.PenyewaDetailProdukActivity
import com.hafiz.pareapp.utils.PareUtils
import kotlinx.android.synthetic.main.penyewa_item_home.view.*

class PenyewaHomeAdapter (private var pemiliks : MutableList<Pemilik>, private var context: Context)
    : RecyclerView.Adapter<PenyewaHomeAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.penyewa_item_home, parent, false))
    }

    override fun getItemCount(): Int = pemiliks.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(pemiliks[position], context)

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(pemilik: Pemilik, context: Context){
            with(itemView) {
                txt_nama_perusahaan.text = pemilik.nama_perusahaan
                txt_lokasi.text = pemilik.alamat
                txt_no_izin.text = pemilik.no_izin
            }
        }
    }

    fun changelist(p : List<Pemilik>){
        pemiliks.clear()
        pemiliks.addAll(p)
        notifyDataSetChanged()
    }

}