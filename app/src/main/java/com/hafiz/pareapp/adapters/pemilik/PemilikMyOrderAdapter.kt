package com.hafiz.pareapp.adapters.pemilik

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hafiz.pareapp.R
import com.hafiz.pareapp.models.Order

class PemilikMyOrderAdapter (private var orders : MutableList<Order>, private var context: Context)
    : RecyclerView.Adapter<PemilikMyOrderAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.penyewa_item_my_produk, parent, false))
    }

    override fun getItemCount(): Int = orders.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(orders[position], context)

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(order: Order, context: Context){
            with(itemView){

            }
        }
    }

    fun changelist(c : List<Order>){
        orders.clear()
        orders.addAll(c)
        notifyDataSetChanged()
    }

}