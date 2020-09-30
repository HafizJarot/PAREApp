package com.hafiz.pareapp.adapters.pemilik

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.hafiz.pareapp.R
import com.hafiz.pareapp.ui.pemilik.produk.PemilikProdukActivity
import com.hafiz.pareapp.ui.pemilik.main.home.PemilikHomeViewModel
import com.hafiz.pareapp.models.Produk
import com.hafiz.pareapp.utils.PareUtils
import kotlinx.android.synthetic.main.pemilik_item_produk.view.*

class PemilikProdukAdapter (private var produks : MutableList<Produk>,
                            private var context : Context,
                            private var pemilikHomeViewModel: PemilikHomeViewModel)
    : RecyclerView.Adapter<PemilikProdukAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.pemilik_item_produk, parent, false))
    }

    override fun getItemCount(): Int  = produks.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(produks[position], context, pemilikHomeViewModel)

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
         @SuppressLint("SetTextI18n")
         fun bind(produk: Produk, context: Context, pemilikHomeViewModel: PemilikHomeViewModel){
             with(itemView){
                 iv_produk.load(produk.foto)
                 //tv_nama_pemilik.text = produk.user!!.nama_perusahaan
                 tv_harga.text = PareUtils.setToIDR(produk.harga_sewa!!)
                 tv_ukuran.text = "Panjang : ${produk.panjang} x Lebar : ${produk.lebar}"
                 tv_lokasi.text = produk.alamat
                 setOnClickListener {
                     context.startActivity(Intent(context, PemilikProdukActivity::class.java).apply {
                         putExtra("PRODUK", produk)
                         putExtra("IS_INSERT", false)
                     })
                 }
                 img_delete.setOnClickListener {
                     val m = "apakah anda yakin ingin menghapus data ini?"
                     alert(m, context, pemilikHomeViewModel, produk.id.toString())
                 }
             }
         }

        fun alert(message : String, context: Context, pemilikHomeViewModel: PemilikHomeViewModel, id : String){
            AlertDialog.Builder(context).apply {
                setMessage(message)
                setPositiveButton("ya"){dialog, _ ->
                    dialog.dismiss()
                    val token = "Bearer ${PareUtils.getToken(context)}"
                    pemilikHomeViewModel.deleteProduk(token, id)
                }
                setNegativeButton("tidak"){dialog, _ -> dialog.dismiss() }
            }.show()
        }
     }

    fun changelist(c : List<Produk>){
        produks.clear()
        produks.addAll(c)
        notifyDataSetChanged()
    }
}