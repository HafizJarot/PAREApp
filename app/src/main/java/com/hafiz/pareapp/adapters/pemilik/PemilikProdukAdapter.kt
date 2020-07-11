package com.hafiz.pareapp.adapters.pemilik

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.hafiz.pareapp.R
import com.hafiz.pareapp.activiities.pemilik.produk.PemilikProdukActivity
import com.hafiz.pareapp.models.Produk
import kotlinx.android.synthetic.main.item_produk.view.*

class PemilikProdukAdapter (private var produks : MutableList<Produk>, private var context : Context)
    : RecyclerView.Adapter<PemilikProdukAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_produk, parent, false))
    }

    override fun getItemCount(): Int  = produks.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(produks[position], context)

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
         @SuppressLint("SetTextI18n")
         fun bind(produk: Produk, context: Context){
             with(itemView){
                 iv_produk.load(produk.foto)
                 tv_nama_pemilik.text = produk.user!!.nama_perusahaan
                 tv_harga.text = produk.harga_sewa.toString()
                 tv_ukuran.text = "Panjang : ${produk.panjang} x Lebar : ${produk.lebar}"
                 tv_lokasi.text = produk.alamat
                 setOnClickListener {
                     context.startActivity(Intent(context, PemilikProdukActivity::class.java).apply {
                         putExtra("PRODUK", produk)
                         putExtra("IS_INSERT", false)
                     })
                 }
             }
         }
     }

    fun changelist(c : List<Produk>){
        produks.clear()
        produks.addAll(c)
        notifyDataSetChanged()
    }
}