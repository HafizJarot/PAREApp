package com.hafiz.pareapp.ui.penyewa.detail_company

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.hafiz.pareapp.R
import com.hafiz.pareapp.models.Produk
import com.hafiz.pareapp.ui.penyewa.detail_product.PenyewaDetailProductActivity
import com.hafiz.pareapp.utils.PareUtils
import kotlinx.android.synthetic.main.penyewa_item_product.view.*

class PenyewaProductAdapter (private var products : MutableList<Produk>, private val companyClickListener: CompanyClickListener)
    : RecyclerView.Adapter<PenyewaProductAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.penyewa_item_product, parent, false))
    }

    override fun getItemCount(): Int = products.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(products[position], companyClickListener)

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        @SuppressLint("SetTextI18n")
        fun bind(produk: Produk, companyClickListener: CompanyClickListener){
            with(itemView){
                img_produk.load(produk.foto)
                txt_p_l_and_sisi.text = "${produk.panjang} Panjang x ${produk.lebar} Lebar dan ${produk.sisi} Sisi"
                txt_price.text = "${PareUtils.setToIDR(produk.harga_sewa!!)} / Bulan"
                txt_lokasi.text = produk.alamat
                setOnClickListener { companyClickListener.click(produk) }
            }
        }
    }

    fun changeList(c : List<Produk>){
        products.clear()
        products.addAll(c)
        notifyDataSetChanged()
    }

}