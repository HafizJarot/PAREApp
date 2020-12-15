package com.hafiz.pareapp.ui.penyewa.order

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import com.hafiz.pareapp.R
import com.hafiz.pareapp.ui.penyewa.main.PenyewaMainActivity
import com.hafiz.pareapp.models.Produk
import com.hafiz.pareapp.models.User
import com.hafiz.pareapp.ui.penyewa.detail_product.PaymentMidtrans
import com.hafiz.pareapp.utils.PareUtils
import com.hafiz.pareapp.utils.extensions.showToast
import kotlinx.android.synthetic.main.penyewa_activity_order.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import com.twigsntwines.daterangepicker.DatePickerDialog
import com.twigsntwines.daterangepicker.DateRangePickedListener


class PenyewaOrderActivity : AppCompatActivity(), DateRangePickedListener {

    private val penyewaOrderViewModel : PenyewaOrderViewModel by viewModel()
    private var harga : Int? = null
    private var sisi : Int? = null
    private lateinit var idPenyewa : String
    private val paymentMidtrans = PaymentMidtrans()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.penyewa_activity_order)
        //setDate()
        observe()
        setDataProduct()
        openDateRangePicker()
        //order()
    }

//    private fun setDate(){
//        val cal = Calendar.getInstance()
//        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
//            cal.set(Calendar.YEAR, year)
//            cal.set(Calendar.MONTH, month)
//            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
//            val myFormat = "yyyy-MM-dd"
//            val simpleDateFormat = SimpleDateFormat(myFormat, Locale.US)
//            txt_tanggal_mulai.text = simpleDateFormat.format(cal.time)
//        }
//
//        txt_pilih_tanggal.setOnClickListener {
//            DatePickerDialog(this, dateSetListener,
//                cal.get(Calendar.YEAR),
//                cal.get(Calendar.MONTH),
//                cal.get(Calendar.DAY_OF_MONTH)).apply {
//                datePicker.minDate = cal.timeInMillis
//            }.show()
//        }
//    }

    private fun observe() {
        observeState()
        observeCurrentUser()
    }

    private fun observeState() = penyewaOrderViewModel.listenToState().observer(this, Observer { handleUI(it) })
    private fun observeCurrentUser() = penyewaOrderViewModel.listenToUser().observe(this, Observer { handleUser(it) })

    private fun handleUI(it : PenyewaOrderState){
        when(it){
            is PenyewaOrderState.IsLoading -> btn_order.isEnabled = !it.state
            is PenyewaOrderState.ShowToast -> toast(it.message)
            is PenyewaOrderState.Success -> startActivity(Intent(this@PenyewaOrderActivity, PenyewaMainActivity::class.java ))
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setDataProduct(){
        getPassedProduct()?.let {
            harga = it.harga_sewa
            sisi = it.sisi
            txt_harga.text = "Harga : ${PareUtils.setToIDR(it.harga_sewa!!)}"
            txt_sisi.text = "Sisi : ${it.sisi}"
            //txt_tanggal_mulai.text = "Tanggal : ${getPassedTanggalMulai()}"
            //txt_lama_sewa.text = "Lama Sewa : ${getPassedLamaSewa()} Bulan"
            //val total = (getPassedProduct()?.harga_sewa!!.times(getPassedProduct()?.sisi!!).times(getPassedLamaSewa()!!.toInt()))
            //txt_total_harga.text = "Total Harga : ${PareUtils.setToIDR(total)}"
        }
    }

//    private fun order(){
//        val token = "Bearer ${PareUtils.getToken(this@PenyewaOrderActivity)}"
//        val id_pemilik = getPassedProduct()?.pemilik!!.id
//        val id_produk = getPassedProduct()?.id
//        val harga = getPassedProduct()?.harga_sewa
//        val tanggal_mulai_sewa = getPassedTanggalMulai()
//        val selesai_sewa = getPassedLamaSewa()
//        val sisi = getPassedProduct()?.sisi
//        val createOrder = CreateOrder(idPemilik = id_pemilik.toString(), idProduk = id_produk.toString(), harga = harga.toString(),
//            tanggalMulaiSewa = tanggal_mulai_sewa!!, selesaiSewa = selesai_sewa!!, sisi = sisi.toString())
//
//        paymentMidtrans.initPayment(this@PenyewaOrderActivity, penyewaOrderViewModel, token, createOrder)
//        btn_order.setOnClickListener {
//            val _harga = getPassedProduct()?.harga_sewa
//            //val _selesai_sewa = getPassedLamaSewa()
//            val _sisi = getPassedProduct()?.sisi
//            paymentMidtrans.showPayment(this@PenyewaOrderActivity, idPenyewa
//                ,_harga!!.times(_sisi!!), _selesai_sewa!!.toInt(), getPassedProduct()?.alamat!!)
//        }
//    }

    @SuppressLint("SetTextI18n")
    private fun handleUser(it : User){
        idPenyewa = it.id.toString()
        btn_order.isEnabled = true
        //txt_username.text = "Nama Pemesan : ${it.penyewa!!.nama}"
    }

    private fun openDateRangePicker(){
        show_calendar.setOnClickListener {
            val fragmentManager = supportFragmentManager
            val datePickerDialog = DatePickerDialog.newInstance()
            datePickerDialog.show(fragmentManager, "Date Picker")
        }
    }

    private fun getPassedProduct() : Produk? = intent.getParcelableExtra("PRODUCT")
    private fun toast(message : String) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()


    override fun OnDateRangePicked(p0: Calendar?, p1: Calendar?) {
        showToast("From Date $p0")
        showToast("To Date $p1")
        println("From Date $p0")
        println("To Date $p1")

    }

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()
        //penyewaOrderViewModel.profile("Bearer ${PareUtils.getToken(this@PenyewaOrderActivity)}")
    }
}