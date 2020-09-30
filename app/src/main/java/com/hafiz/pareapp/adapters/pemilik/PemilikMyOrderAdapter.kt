package com.hafiz.pareapp.adapters.pemilik

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.hafiz.pareapp.R
import com.hafiz.pareapp.models.Order
import com.hafiz.pareapp.utils.PareUtils
import kotlinx.android.synthetic.main.pemilik_item_my_order.view.*

class PemilikMyOrderAdapter (private var orders : MutableList<Order>, private var context: Context)
    : RecyclerView.Adapter<PemilikMyOrderAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.pemilik_item_my_order, parent, false))
    }

    override fun getItemCount(): Int = orders.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(orders[position], context)

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(order: Order, context: Context){
            with(itemView){
                //txt_nama_produk.text = order.pemilik.nama_perusahaan
                txt_harga.text = PareUtils.setToIDR(order.produk.harga_sewa!!)
                txt_alamat.text = order.produk.alamat
                img_produk.load(order.produk.foto)
            }
        }
    }

    fun changelist(c : List<Order>){
        orders.clear()
        orders.addAll(c)
        notifyDataSetChanged()
    }

}