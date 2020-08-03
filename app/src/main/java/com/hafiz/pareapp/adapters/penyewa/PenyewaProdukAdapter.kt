package com.hafiz.pareapp.adapters.penyewa

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.hafiz.pareapp.MyOnClickListener
import com.hafiz.pareapp.R
import com.hafiz.pareapp.models.Produk
import kotlinx.android.synthetic.main.pemilik_item_produk.view.*

class PenyewaProdukAdapter (private var produks : MutableList<Produk>, private var myOnClickListener: MyOnClickListener)
    : RecyclerView.Adapter<PenyewaProdukAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.penyewa_item_produk, parent, false))
    }

    override fun getItemCount(): Int  = produks.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(produks[position], myOnClickListener)

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        @SuppressLint("SetTextI18n")
        fun bind(produk: Produk, myOnClickListener: MyOnClickListener){
            with(itemView){
                iv_produk.load(produk.foto)
                tv_nama_pemilik.text = produk.user!!.nama_perusahaan
                tv_harga.text = produk.harga_sewa.toString()
                tv_ukuran.text = "Panjang : ${produk.panjang} x Lebar : ${produk.lebar}"
                tv_lokasi.text = produk.alamat
                setOnClickListener { myOnClickListener.click(produk) }
            }
        }
    }

    fun changelist(c : List<Produk>){
        produks.clear()
        produks.addAll(c)
        notifyDataSetChanged()
    }
}