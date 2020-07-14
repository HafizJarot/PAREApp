package com.hafiz.pareapp.activiities.penyewa.order

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import com.hafiz.pareapp.R
import com.hafiz.pareapp.fragments.penyewa.order.PenyewaMyOrderFragment
import com.hafiz.pareapp.models.CreateOrder
import com.hafiz.pareapp.models.Produk
import com.hafiz.pareapp.models.User
import com.hafiz.pareapp.utils.PareUtils
import kotlinx.android.synthetic.main.activity_penyewa_order.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class PenyewaOrderActivity : AppCompatActivity() {

    private val penyewaOrderViewModel : PenyewaOrderViewModel by viewModel()
    private var harga : Int? = null
    private var sisi : Int? = null
    private lateinit var idPenyewa : String
    private val paymentMidtrans = PaymentMidtrans()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_penyewa_order)

        penyewaOrderViewModel.listenToState().observer(this, Observer { handleUI(it) })
        penyewaOrderViewModel.listenToUser().observe(this, Observer { handleUser(it) })
        setDataProduct()
        order()
    }

    private fun handleUI(it : PenyewaOrderState){
        when(it){
            is PenyewaOrderState.IsLoading -> btn_order.isEnabled = !it.state
            is PenyewaOrderState.ShowToast -> toast(it.message)
            is PenyewaOrderState.Success -> startActivity(Intent(this@PenyewaOrderActivity, PenyewaMyOrderFragment::class.java ))
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setDataProduct(){
        getPassedProduct()?.let {
            harga = it.harga_sewa
            sisi = it.sisi
            txt_harga.text = "Harga : ${PareUtils.setToIDR(it.harga_sewa!!)}"
            txt_sisi.text = "Sisi : ${it.sisi}"
            txt_tanggal_mulai.text = "Tanggal : ${getPassedTanggalMulai()}"
            txt_lama_sewa.text = "Lama Sewa : ${getPassedLamaSewa()} Bulan"
            val total = (getPassedProduct()?.harga_sewa!!.times(getPassedProduct()?.sisi!!).times(getPassedLamaSewa()!!.toInt()))
            txt_total_harga.text = "Total Harga : ${PareUtils.setToIDR(total)}"
        }
    }

    private fun order(){
//        btn_order.setOnClickListener {
//            val token = "Bearer ${PareUtils.getToken(this@PenyewaOrderActivity)}"
//            val id_penyewa = getPassedProduct()?.user!!.id
//            val id_produk = getPassedProduct()?.id
//            val harga = getPassedProduct()?.harga_sewa
//            val tanggal_mulai_sewa = getPassedTanggalMulai()
//            val selesai_sewa = getPassedLamaSewa()
//            val sisi = getPassedProduct()?.sisi
//            penyewaOrderViewModel.orderStore(token, id_penyewa.toString(), id_produk.toString(),
//                harga.toString(), tanggal_mulai_sewa!!, selesai_sewa!!, sisi.toString())
//        }
        val token = "Bearer ${PareUtils.getToken(this@PenyewaOrderActivity)}"
        val id_pemilik = getPassedProduct()?.user!!.id
        val id_produk = getPassedProduct()?.id
        val harga = getPassedProduct()?.harga_sewa
        val tanggal_mulai_sewa = getPassedTanggalMulai()
        val selesai_sewa = getPassedLamaSewa()
        val sisi = getPassedProduct()?.sisi
        val createOrder = CreateOrder(idPemilik = id_pemilik.toString(), idProduk = id_produk.toString(), harga = harga.toString(),
            tanggalMulaiSewa = tanggal_mulai_sewa!!, selesaiSewa = selesai_sewa!!, sisi = sisi.toString())

        paymentMidtrans.initPayment(this@PenyewaOrderActivity, penyewaOrderViewModel, token, createOrder)
        btn_order.setOnClickListener {
            val _harga = getPassedProduct()?.harga_sewa
            val _selesai_sewa = getPassedLamaSewa()
            val _sisi = getPassedProduct()?.sisi
            paymentMidtrans.showPayment(this@PenyewaOrderActivity, idPenyewa
                ,_harga!!.times(_sisi!!), _selesai_sewa!!.toInt(), getPassedProduct()?.alamat!!)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun handleUser(it : User){
        idPenyewa = it.id.toString()
        btn_order.isEnabled = true
        txt_username.text = "Nama Pemesan : ${it.name}"
    }
    private fun getPassedProduct() : Produk? = intent.getParcelableExtra("PRODUK")
    private fun getPassedTanggalMulai() = intent.getStringExtra("TANGGAL_MULAI")
    private fun getPassedLamaSewa() = intent.getStringExtra("LAMA_SEWA")
    private fun toast(message : String) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()
        penyewaOrderViewModel.profile("Bearer ${PareUtils.getToken(this@PenyewaOrderActivity)}")
    }
}
